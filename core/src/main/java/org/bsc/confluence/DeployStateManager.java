package org.bsc.confluence;

import org.apache.commons.codec.digest.DigestUtils;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public class DeployStateManager {
    private static final java.util.logging.Logger log =
            java.util.logging.Logger.getLogger(DeployStateManager.class.getName());

    private static void debug( String msg, Object ...args ) {
        log.fine( () -> format( msg, args));
    }
    private static void info( String msg, Object ...args ) {
        log.info( () -> format( msg, args));
    }
    private static void warn( String msg, Throwable ex  ) {
        log.log(Level.WARNING, msg, ex);
    }

    private static boolean isFileSchema( java.net.URI uri ) {
        return ofNullable(uri)
                .map( u -> "file".equalsIgnoreCase(u.getScheme()) )
                .orElse(false);
    }

    public static JsonString createValue( String value ) {
        return Json.createArrayBuilder().add(value).build().getJsonString(0);
        // From 1.1
        // return Json.createValue("")
    }
    public static JsonNumber createValue( int value ) {
        return Json.createArrayBuilder().add(value).build().getJsonNumber( 0 );
        // From 1.1
        // return Json.createValue(0L)
    }

    public static final String STORAGE_NAME = ".confluence-maven-plugin-storage.json";

    public static class Data {
        final static String HASH = "hash";

        public static Data of(JsonValue entry) {
            requireNonNull(entry, "entry is null!");
            switch( entry.getValueType() ) {
                case STRING:
                    return create().setHash( ((JsonString)entry).getString() );
                case OBJECT:
                    return new Data( (JsonObject)entry);
                case NULL:
                    return new Data();
                default:
                    throw new IllegalArgumentException( format("value [%s] is not valid type!", entry.toString()) );
            }
        }

        public static Data of( Data data ) {
            return ( data == null ) ? new Data() : new Data( data.attributes );
        }

        public static Data create() {
            return new Data();
        }

        private Map<String,JsonValue>  attributes = new HashMap<>();

        private Data() {
        }

        private Data( Map<String,JsonValue> attributes ) {
            this.attributes.putAll(attributes);
        }

        public Optional<String> getHash() {
            return getAttributeString(HASH);
        }

        public Data setHash(String hash) {
            setAttributeString(HASH,hash);
            return this;
        }

        public Optional<String> getAttributeString(String name)  {
            debug( "Data.getAttributeString( %s )", name );
            return ofNullable(attributes.get(name.toLowerCase())).flatMap( v -> {
                if (v.getValueType() == JsonValue.ValueType.STRING) {
                    return ofNullable( ((JsonString)v).getString() );
                }
                return  Optional.empty();
            });
        }

        public Optional<Integer> getAttributeInt( String name)  {
            debug( "Data.getAttributeInt( %s )", name );
            return ofNullable(attributes.get(name.toLowerCase())).flatMap( v -> {
                if (v.getValueType() == JsonValue.ValueType.NUMBER) {
                    return ofNullable( ((JsonNumber)v).intValue());
                }
                return  Optional.empty();
            });
        }

        public Data setAttributeString(String name, String value) {
            debug( "Data.setAttributeString( %s, %s )", name, value );
            this.attributes.put( name.toLowerCase(), createValue(value) );
            return this;
        }
        public Data setAttributeInt(String name, Integer value) {
            debug( "Data.setAttributeInt( %s, %d )", name, value );
            this.attributes.put( name.toLowerCase(), createValue(value) );
            return this;
        }

        public final Data copyAttributes( Data data, boolean excludeHash ) {
            // Objects.requireNonNull(data, "argument 'data' is null!");

            if( data == null ) return this;

            if( excludeHash ) {
                data.attributes.entrySet().stream()
                        .filter( attr -> HASH.compareTo(attr.getKey())!=0 )
                        .forEach( attr -> this.attributes.put( attr.getKey(), attr.getValue() ));
            }
            else {
                this.attributes.putAll(data.attributes);
            }
            return this;
        }

        final JsonValue toJson() {

            final  JsonObjectBuilder builder = Json.createObjectBuilder();

            attributes.entrySet().forEach( e -> builder.add( e.getKey(), e.getValue()));

            return builder.build();
        }

        @Override
        public String toString() {
            return attributes.entrySet().stream()
                    .map(e -> format("[%s,%s]", e.getKey(), e.getValue()))
                    .collect(Collectors.joining(","));
        }
    }

    /**
     * factory method
     *
     * @param endpoint
     */
    public static DeployStateManager load( String endpoint, Path outdir ) {
        final DeployStateManager result = new DeployStateManager( endpoint, outdir );
        result.load();
        return result;
    }

    private Map<String, Map<String,Data>> storage = new HashMap<>();

    private final String            endpoint;
    private final Path              outdir;
    private final JsonWriterFactory writerFactory;

    /**
     *
     * @param endpoint
     * @param outdir
     */
    private DeployStateManager( String endpoint, Path outdir ) {
        requireNonNull(endpoint, "endpoint is null!");
        requireNonNull(outdir, "outdir is null!");

        if( !Files.exists(outdir)) {
            try {
                Files.createDirectories(outdir);
            } catch (IOException e) {
                throw new IllegalArgumentException( format("Impossible create path [%s]", outdir));
            }
        }
        else if( !Files.isDirectory(outdir))
            throw new IllegalArgumentException( format("Path [%s] is not a directory", outdir));

        this.endpoint = endpoint;
        this.outdir = outdir;

        // Create JsonWriterFactory with PRETTY_PRINTING = true
        Map<String, Boolean> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        this.writerFactory = Json.createWriterFactory(config);
    }
    /**
     *
     */
    private void load() {
        final Path file = Paths.get(outdir.toString(), STORAGE_NAME);

        if (!Files.exists(file)) {
            try {
                Files.createFile(file);
                save();
            } catch (IOException e) {
                // TODO
                warn( format("error creating file '%s'", file),e );
            }

        } else {
            try (JsonReader r = Json.createReader(Files.newBufferedReader(file))) {
                r.readObject().entrySet().forEach(e -> {
                    final Map<String, Data> ss = new HashMap<>();
                    ((JsonObject) e.getValue()).entrySet()
                            .forEach(ee -> ss.put(ee.getKey(), Data.of(ee.getValue())));
                    storage.put(e.getKey(), ss);
                });
            } catch (Exception ex) {
                // TODO
                warn( format("error reading file '%s'", file),ex );
            }
        }

        storage.computeIfAbsent( endpoint, key -> new HashMap<>() );

//        if (!storage.containsKey(endpoint)) {
//            storage.put(endpoint, new HashMap<>());
//        }

    }
    /**
     *
     */
    public DeployStateManager save() {
        synchronized (this) {

            final Path file = Paths.get(outdir.toString(), STORAGE_NAME);

            if (!Files.exists(file)) return this;

            final JsonObjectBuilder b = Json.createObjectBuilder();

            storage.entrySet().forEach(e -> {
                final JsonObjectBuilder b1 = Json.createObjectBuilder();
                e.getValue().entrySet().forEach(ee -> b1.add(ee.getKey(), ee.getValue().toJson()));
                b.add(e.getKey(), b1);
            });

            try (JsonWriter w = writerFactory.createWriter(Files.newBufferedWriter(file))) {
//                storage.put("updated", Json.createValue(sdf.format(new java.util.Date())));
                w.writeObject(b.build());
            } catch (Exception ex) {
                // TODD
                warn( format("error saving file '%s'", file),ex );
            }

            return this;
        }
    }
    /**
     *
     * @param uri
     * @param attributes
     * @param yes
     * @param no
     * @param <U>
     * @return
     */
    public <U> CompletableFuture<U>
            isUpdated(java.net.URI uri,
                      Data attributes,
                      Supplier<CompletableFuture<U>> yes,
                      Supplier<CompletableFuture<U>> no )
    {
        synchronized (this) {
            return isUpdated(uri, attributes) ? yes.get() : no.get();
        }
    }
    /**
     *
     * @param uri
     * @return
     */
    private boolean isUpdated( java.net.URI uri, Data attributes ) {
        if (uri == null) return false;
        return ( !isFileSchema(uri) || isUpdated(Paths.get(uri), attributes) );
    }
    /**
     *
     * @param uri
     * @return
     */
    private Optional<String> getKeyFormUri( java.net.URI uri ) {
        return (uri != null && "file".equalsIgnoreCase(uri.getScheme())) ?
            ofNullable(Paths.get(uri))
                    .flatMap( file -> ofNullable( String.valueOf(outdir.relativize(file.toAbsolutePath())) )) :
                Optional.empty();
    }
    /**
     *
     * @param uri
     * @return
     */
    public Optional<Data> getAttributes( java.net.URI uri ) {

        if( !isFileSchema(uri) ) return Optional.empty(); // GUARD

        return ofNullable( storage.get(endpoint) )
                .flatMap( s ->
                    getKeyFormUri( uri )
                        .flatMap( key -> ofNullable(s.get(key))) );
    }
    /**
     *
     * @param uri
     * @param attributes
     */
    public void setAttributes( java.net.URI uri, Data attributes ) {
        debug( "DeployStateManager.setAttributes( '%s', '%s' )",
                String.valueOf(uri),
                String.valueOf(attributes) );

        synchronized (this) {
            isUpdated( uri, attributes );
        }
    }
    /**
     *
     * @param file
     * @return
     */
    boolean isUpdated(Path file, Data attributes ) {
        debug( "isUpdated( %s, %s )",  file, attributes );

        if( file == null ) return false;

        return ofNullable(storage.get(endpoint)).map( s -> {
            final String key = String.valueOf(outdir.relativize( file.toAbsolutePath() ));

            final String fileMd5Hash = md5Hash(file);

            return
                ofNullable(s.get(key))
                    .map( data -> data.copyAttributes(attributes, true) )
                    .map( data ->
                        data.getHash().map( lastStoredFileMd5Hash -> {

                        if(!Objects.equals(fileMd5Hash, lastStoredFileMd5Hash)) {
                            debug( "%s - data.setHash( %s )",  key, fileMd5Hash );
                            data.setHash(fileMd5Hash);
                            return true;
                        }
                        return false;
                    }).orElse(false)

            ).orElseGet( () -> {
                debug( "%s - data.setHash( %s )",  key, fileMd5Hash );
                final DeployStateManager.Data attrs =
                        Data.of(attributes)
                        .setHash(fileMd5Hash);
                s.put( key, attrs);
                return true;
            });

        })
        .orElse(true);

    }
    /**
     *
     * @param file
     * @return
     */
    private String md5Hash(Path file) {
        try(FileInputStream fis = new FileInputStream(file.toFile())) {
            return DigestUtils.md5Hex(fis);
        } catch (IOException e) {
            // TODO
            warn( format("error generating md5Hash for file '%s'", file), e  );
        }
        return null;
    }

    /**
     *
     */
    public DeployStateManager clear() {
        storage.computeIfPresent( endpoint, (key, prev) -> {
            info( "cleared [%d] entries in deploy state manager!", prev.size());
            return new HashMap<>();
        });
        return this;
    }

    /**
     *
     * @param uri
     * @return
     */
    public boolean removeState( java.net.URI uri ) {
        return isFileSchema(uri) && removeState(Paths.get(uri));
    }

    /**
     *
     * @param file
     */
    boolean removeState( Path file ) {
        if( file == null ) return false;

        return ofNullable(storage.get(endpoint))
                .map( s -> {
                    final String key = String.valueOf(outdir.relativize( file.toAbsolutePath() ));
                    s.remove( key );
                    return true;
                })
                .orElse(false);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("DeployStateManager").append("\n\t")
                .append("endopint=").append(endpoint).append("\n\t")
                .append("outdir=").append(outdir).append("\n")
                .toString();
    }

}
