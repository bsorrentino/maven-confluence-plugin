package org.bsc.confluence;

import org.apache.commons.codec.digest.DigestUtils;
import org.bsc.confluence.model.Site;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

public class DeployStateManager {

    private static JsonString createValue( String value ) {
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
                default:
                    throw new IllegalArgumentException( format("value [%s] is not valid type!", entry.toString()) );
            }
        }

        final Optional<String> optId;
        private JsonString hash;

        public JsonString getHash() {
            return hash;
        }

        public void setHash(JsonString hash) {
            this.hash = hash;
        }

        private Data(JsonString hash ) {
            optId = empty();
            this.hash = hash;
        }
        private Data( JsonObject entry ) {
            this.optId = ofNullable(entry.getJsonString("id").getString());
            this.hash = entry.getJsonString("hash");
        }

        final JsonValue toJson() {
            return optId.map( id -> (JsonValue)Json.createObjectBuilder()
                                        .add( "id", id)
                                        .add("hash", hash )
                                        .build())
                                    .orElseGet( () -> hash);

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

    private final String endpoint;
    private final Path outdir;
    private final JsonWriterFactory writerFactory;

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
     * @param msg
     * @param args
     */
    private void trace( String msg, Object ...args) {
//        System.out.print( "TRACE ==> ");
//        System.out.println( String.format( msg, (Object[])args ));
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
                e.printStackTrace();
            }

        } else {
            try (JsonReader r = Json.createReader(Files.newBufferedReader(file))) {
                r.readObject().entrySet().forEach(e -> {
                    final Map<String, Data> ss = new HashMap<>();
                    ((JsonObject) e.getValue()).entrySet().forEach(ee -> ss.put(ee.getKey(), Data.of(ee.getValue())));
                    storage.put(e.getKey(), ss);
                });
            } catch (Exception ex) {
                // TODO
                ex.printStackTrace();
            }
        }
        if (!storage.containsKey(endpoint)) {
            storage.put(endpoint, new HashMap<>());
            save();
        }

    }

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

                //storage.put("updated", Json.createValue(sdf.format(new java.util.Date())));
                w.writeObject(b.build());
            } catch (Exception ex) {
                // TODD
                ex.printStackTrace();
            }
        }
    }

    /**
     *
     * @param source
     * @param yes
     * @param no
     * @param <U>
     * @param <S>
     * @return
     */
    public <U,S extends Site.Source>
            CompletableFuture<U>
            isUpdated(S source,
                      Supplier<CompletableFuture<U>> yes,
                      Supplier<CompletableFuture<U>> no )
    {
        final java.net.URI uri = source.getUri();
        trace( "%s isUpdated( %s )", source.getName(), uri );

        return ( isUpdated(uri) )
                ? yes.get().thenApply(v -> {
                        save();
                        return v;
                    })
                : no.get();
    }

    /**
     *
     * @param uri
     * @return
     */
    private boolean isUpdated( java.net.URI uri ) {
        if (uri == null) return false;

        final String scheme = uri.getScheme();

        return Objects.isNull(scheme) ||
                !"file".equalsIgnoreCase(scheme) ||
                isUpdated(Paths.get(uri));
    }

    /**
     *
     * @param file
     * @return
     */
    protected boolean isUpdated(Path file) {
        trace( "isUpdated( %s )",  file );

        if( file == null ) return false;

        final Path b = file.toAbsolutePath();

        final Map<String,Data> s =  storage.get(endpoint);

        if( s==null ) return true;

        final String key = outdir.relativize( b ).toString();

        final String fileMd5Hash = md5Hash(file);
        return ofNullable(s.get(key)).map( data -> {
            final String lastStoredFileMd5Hash = data.getHash().getString();

            if(!Objects.equals(fileMd5Hash, lastStoredFileMd5Hash)) {
                trace( "%s - data.setHash( %s )",  key, fileMd5Hash );
                data.setHash(createValue(fileMd5Hash));
                return true;
            }
            return false;
        }).orElseGet( () -> {
            trace( "%s - data.setHash( %s )",  key, fileMd5Hash );
            s.put( key, Data.of(createValue(fileMd5Hash)) );
            return true;
        });
    }

    private static String md5Hash(Path file) {
        try(FileInputStream fis = new FileInputStream(file.toFile())) {
            return DigestUtils.md5Hex(fis);
        } catch (IOException e) {
            // todo
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param source
     * @param <S>
     * @return
     */
    public <S extends Site.Source> boolean resetState( S source ) {

        final java.net.URI uri = source.getUri();

        if( uri == null ) return false;

        final String scheme = uri.getScheme();

        return Objects.nonNull(scheme) &&
                "file".equalsIgnoreCase(scheme) &&
                resetState(Paths.get(uri));
    }

    /**
     *
     * @param file
     */
    protected boolean resetState( Path file ) {
        if( file == null ) return false;

        final Path b = file.toAbsolutePath();

        final Map<String,Data> s =  storage.get(endpoint);

        final String key = outdir.relativize( b ).toString();

        ofNullable( s.get(key) )
                .map( data -> { data.setHash( createValue("")); return data; } )
                .orElseGet( () -> s.put( key, Data.of(createValue(""))) );
        save();

        return true;
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
