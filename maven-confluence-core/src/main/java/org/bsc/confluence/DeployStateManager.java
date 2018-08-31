package org.bsc.confluence;

import org.apache.commons.codec.digest.DigestUtils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
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

public class DeployStateManager {

    public static class Parameters {
        private Optional<Path> _outdir = Optional.empty() ;
        private boolean active = true;

        /**
         * @return the active
         */
        public boolean isActive() {
            return active;
        }


        /**
         * @param active the active to set
         */
        public void setActive(boolean active) {
            this.active = active;
        }

        /**
         * @param outdir the basedir to set
         */
        public void setOutdir(java.io.File outdir) {
            if( outdir == null ) {
               this._outdir = Optional.empty();
               return;
            }
            this._outdir = Optional.of(outdir.toPath());
        }

        /**
         * @return the _outdir
         */
        public Optional<Path> getOutdir() {
            return _outdir;
        }


        /**
         *
         */
        @Override
        public String toString() {
            return new StringBuilder()
                        .append("DeployStateManager").append("\n\t")
                        .append("active=").append(active).append("\n\t")
                        .append("outdir=").append(_outdir).append("\n")
                        .toString();
        }

    }

    public static final String STORAGE_NAME = "confluence-reporting-maven-plugin-storage.json";

    private Map<String, Map<String,JsonValue>> storage = new HashMap<>();

    private final Parameters _info;
    private final String _endpoint;

    private final JsonWriterFactory writerFactory;

    private DeployStateManager( String endpoint, Parameters info) {
        Objects.requireNonNull(endpoint, "endpoint is null!");
        Objects.requireNonNull(info, "info is null!");

        this._endpoint = endpoint;
        this._info = info;

        this._info._outdir.ifPresent( p -> {
            if( !Files.exists(p)) {
                try {
                    Files.createDirectories(p);
                } catch (IOException e) {
                    throw new IllegalArgumentException( String.format("Impossible create path [%s]", p));
                }
            }
            else if( !Files.isDirectory(p))
                throw new IllegalArgumentException( String.format("Path [%s] is not a directory", p));
        });

        // Create JsonWriterFactory with PRETTY_PRINTING = true
        Map<String, Boolean> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        this.writerFactory = Json.createWriterFactory(config);
    }

    /**
     *
     * @param endpoint
     */
    public static DeployStateManager load( String endpoint, Parameters info ) {
        DeployStateManager result = new DeployStateManager( endpoint, info );
        result._load();
        return result;
    }

    private boolean isValid() {
        return _info.active && _info._outdir.isPresent();
    }

    /**
     *
     */
    private void _load() {

        if( !isValid() ) return;

        final Path file = Paths.get(_info._outdir.get().toString(), STORAGE_NAME);

        if( !Files.exists(file)) {
            try {
                Files.createFile(file);
            } catch (IOException e) {
                // TODO
                e.printStackTrace();
            }

        } else {
            try( JsonReader r = Json.createReader(Files.newBufferedReader(file)) ) {

                r.readObject().entrySet().forEach( e -> {
                    final Map<String,JsonValue> ss = new HashMap<>();
                    ((JsonObject)e.getValue()).entrySet().forEach( ee -> ss.put( ee.getKey(), ee.getValue()));
                    storage.put( e.getKey(), ss);
                });
            }
            catch( Exception ex ) {
                // TODD
                ex.printStackTrace();
            }
        }
        if( !storage.containsKey(_endpoint) ) {
            storage.put(_endpoint, new HashMap<String,JsonValue>() );
            save();
        }


    }

    private void save() {
        if( !isValid() ) return;

        final Path file = Paths.get(_info._outdir.get().toString(), STORAGE_NAME);

        if( !Files.exists(file)) return;

        JsonObjectBuilder b = Json.createObjectBuilder();
        storage.entrySet().forEach( e -> {
            JsonObjectBuilder b1 = Json.createObjectBuilder();
            e.getValue().entrySet().forEach( ee -> b1.add( ee.getKey(), ee.getValue()) );
            b.add( e.getKey(), b1 );

        });

        try(JsonWriter w = writerFactory.createWriter(Files.newBufferedWriter(file)) ) {

            //storage.put("updated", Json.createValue(sdf.format(new java.util.Date())));
            w.writeObject(b.build());
        }
        catch( Exception ex ) {
            // TODD
            ex.printStackTrace();
        }

    }

    private JsonString createValue( String value ) {
        return Json.createArrayBuilder().add(value).build().getJsonString(0);
        // From 1.1
        // return Json.createValue(0L)
    }

    /**
     *
     * @param uri
     * @return
     */
    public boolean isUpdated( java.net.URI uri ) {
        Objects.requireNonNull(uri, "uri is null!");

        final String scheme = uri.getScheme();

        if ( Objects.nonNull(scheme) && "file".equalsIgnoreCase(scheme) ) {

            return isUpdated( Paths.get(uri) );

        }

        return true;

    }

    /**
     *
     * @param file
     * @return
     */
    public boolean isUpdated(Path file) {
        Objects.requireNonNull(file, "file is null!");

        if( !isValid() ) return true;

        final Path b = file.toAbsolutePath();

        final Map<String,JsonValue> s =  storage.get(_endpoint);

        if( s==null ) return true;

        final String key = _info._outdir.get().relativize( b ).toString();

        JsonString value = s.containsKey(key) ?
                                (JsonString) s.get(key) :
                                createValue("0");

        final String fileMd5Hash = md5Hash(file);
        final String lastStoredFileMd5Hash = value.getString();

        if(!Objects.equals(fileMd5Hash, lastStoredFileMd5Hash)) {
            s.put(key, createValue(fileMd5Hash));
            save();
            return true;
        }

        return false;
    }

    private static String md5Hash(Path file) {
        try(FileInputStream fis = new FileInputStream(file.toFile())) {
            return DigestUtils.md5Hex(fis);
        } catch (IOException e) {
            //todo
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param uri
     */
    public void resetState( java.net.URI uri ) {
        Objects.requireNonNull(uri, "uri is null!");

        final String scheme = uri.getScheme();

        if ( Objects.nonNull(scheme) && "file".equalsIgnoreCase(scheme) ) {

            resetState( Paths.get(uri) );

        }
    }

    /**
     *
     * @param file
     */
    public void resetState( Path file ) {
        Objects.requireNonNull(file, "file is null!");

        if( !isValid() ) return;

        final Path b = file.toAbsolutePath();

        final Map<String,JsonValue> s =  storage.get(_endpoint);

        final String key = _info._outdir.get().relativize( b ).toString();

        s.put(key, createValue("0"));
        save();

    }

    @Override
    public String toString() {
        return _info.toString();
    }

}
