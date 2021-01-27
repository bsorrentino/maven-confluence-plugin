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

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public class DeployStateManager {
    private static final java.util.logging.Logger log =
            java.util.logging.Logger.getLogger(DeployStateManager.class.getName());

    private static void debug( String msg, Object ...args ) {
        log.fine( () -> format( msg, args));
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
        // return Json.createValue(0L)
    }

    public static final String STORAGE_NAME = ".confluence-maven-plugin-storage.json";

    static class Data {

        public static Data of(JsonValue entry) {
            requireNonNull(entry, "entry is null!");
            switch( entry.getValueType() ) {
                case STRING:
                    return new Data( (JsonString)entry );
                case OBJECT:
                    return new Data( (JsonObject)entry);
                case NULL:
                    return new Data( createValue(""));
                default:
                    throw new IllegalArgumentException( format("value [%s] is not valid type!", entry.toString()) );
            }
        }

        public static Data empty() {
            return Data.of( createValue("") );
        }

        private Optional<JsonValue> optExtraAttribute;
        private JsonString hash;

        private Data(JsonString hash ) {
            optExtraAttribute = Optional.empty();
            this.hash = hash;
        }
        private Data( JsonObject entry ) {
            this.optExtraAttribute = ofNullable(entry.get("extra"));
            this.hash = entry.getJsonString("hash");
        }

        public JsonString getHash() {
            return hash;
        }

        public void setHash(JsonString hash) {
            this.hash = hash;
        }

        public Optional<JsonValue> getOptExtraAttribute() {
            return optExtraAttribute;
        }

        public void setExtraAttribute(JsonValue value) {
            debug( "Data.setExtraAttribute( %s )", String.valueOf(value) );
            this.optExtraAttribute = ofNullable(value);
        }

        final JsonValue toJson() {
            final JsonObjectBuilder b = Json.createObjectBuilder();
            optExtraAttribute.ifPresent( extra ->  b.add( "extra", extra) );
            return b.add("hash", hash ).build();
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
        if (!storage.containsKey(endpoint)) {
            storage.put(endpoint, new HashMap<>());
        }

    }
    /**
     *
     */
    public void save() {
        synchronized (this) {

            final Path file = Paths.get(outdir.toString(), STORAGE_NAME);

            if (!Files.exists(file)) return;

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
        }
    }
    /**
     *
     * @param uri
     * @param extraAttribute
     * @param yes
     * @param no
     * @param <U>
     * @return
     */
    public <U> CompletableFuture<U>
            isUpdated(java.net.URI uri,
                      Optional<JsonValue> extraAttribute,
                      Supplier<CompletableFuture<U>> yes,
                      Supplier<CompletableFuture<U>> no )
    {
        synchronized (this) {
            return isUpdated(uri, extraAttribute) ? yes.get() : no.get();
        }
    }
    /**
     *
     * @param uri
     * @return
     */
    private boolean isUpdated( java.net.URI uri, Optional<JsonValue> extraAttribute ) {
        if (uri == null) return false;
        return ( !isFileSchema(uri) || isUpdated(Paths.get(uri), extraAttribute) );
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
    public Optional<JsonValue> getOptExtraAttribute( java.net.URI uri ) {

        if( !isFileSchema(uri) ) return Optional.empty(); // GUARD

        return ofNullable( storage.get(endpoint) )
                .flatMap( s ->
                    getKeyFormUri( uri )
                        .flatMap( key -> ofNullable(s.get(key))
                                .flatMap( data -> data.getOptExtraAttribute()) ));
    }
    /**
     *
     * @param uri
     * @param value
     */
    public void setExtraAttribute( java.net.URI uri, JsonValue value ) {
        debug( "DeployStateManager.setExtraAttribute( '%s', '%s' )", String.valueOf(uri), String.valueOf(value) );

        synchronized (this) {
            isUpdated( uri, ofNullable(value));
        }
    }
    /**
     *
     * @param file
     * @return
     */
    boolean isUpdated(Path file, Optional<JsonValue> extraAttribute ) {
        debug( "isUpdated( %s )",  file );

        if( file == null ) return false;

        return ofNullable(storage.get(endpoint)).map( s -> {
            final String key = String.valueOf(outdir.relativize( file.toAbsolutePath() ));

            final String fileMd5Hash = md5Hash(file);

            boolean updated =  ofNullable(s.get(key)).map( data -> {
                final String lastStoredFileMd5Hash = data.getHash().getString();

                if(!Objects.equals(fileMd5Hash, lastStoredFileMd5Hash)) {
                    debug( "%s - data.setHash( %s )",  key, fileMd5Hash );
                    data.setHash(createValue(fileMd5Hash));
                    return true;
                }
                return false;
            }).orElseGet( () -> {
                debug( "%s - data.setHash( %s )",  key, fileMd5Hash );
                s.put( key, Data.of(createValue(fileMd5Hash)) );
                return true;
            });

            extraAttribute.ifPresent( extra -> s.get(key).setExtraAttribute(extra) );

            return updated;

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
