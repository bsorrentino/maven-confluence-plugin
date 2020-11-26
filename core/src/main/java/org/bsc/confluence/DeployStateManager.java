package org.bsc.confluence;

import org.apache.commons.codec.digest.DigestUtils;

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

public class DeployStateManager {

    public static final String STORAGE_NAME = ".confluence-maven-plugin-storage.json";

    private Map<String, Map<String,JsonValue>> storage = new HashMap<>();

    private final String endpoint;
    private final Path outdir;
    private final JsonWriterFactory writerFactory;

    private DeployStateManager( String endpoint, Path outdir ) {
        Objects.requireNonNull(endpoint, "endpoint is null!");
        Objects.requireNonNull(outdir, "outdir is null!");

        if( !Files.exists(outdir)) {
            try {
                Files.createDirectories(outdir);
            } catch (IOException e) {
                throw new IllegalArgumentException( String.format("Impossible create path [%s]", outdir));
            }
        }
        else if( !Files.isDirectory(outdir))
            throw new IllegalArgumentException( String.format("Path [%s] is not a directory", outdir));

        this.endpoint = endpoint;
        this.outdir = outdir;

        // Create JsonWriterFactory with PRETTY_PRINTING = true
        Map<String, Boolean> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        this.writerFactory = Json.createWriterFactory(config);
    }

    /**
     * factory method
     *
     * @param endpoint
     */
    public static DeployStateManager load( String endpoint, Path outdir ) {
        DeployStateManager result = new DeployStateManager( endpoint, outdir );
        result.load();
        return result;
    }

    /**
     *
     */
    private void load() {

        final Path file = Paths.get(outdir.toString(), STORAGE_NAME);

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
        if( !storage.containsKey(endpoint) ) {
            storage.put(endpoint, new HashMap<String,JsonValue>() );
            save();
        }


    }

    private void save() {
        final Path file = Paths.get(outdir.toString(), STORAGE_NAME);

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
        //Objects.requireNonNull(uri, "uri is null!");
        if (uri == null) return false;

        final String scheme = uri.getScheme();

        return (Objects.nonNull(scheme) && "file".equalsIgnoreCase(scheme))
                ? isUpdated(Paths.get(uri))
                : true;
    }

    /**
     *
     * @param file
     * @return
     */
    public boolean isUpdated(Path file) {
        //Objects.requireNonNull(file, "file is null!");
        if( file == null ) return false;

        final Path b = file.toAbsolutePath();

        final Map<String,JsonValue> s =  storage.get(endpoint);

        if( s==null ) return true;

        final String key = outdir.relativize( b ).toString();

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
    public boolean resetState( java.net.URI uri ) {
        //Objects.requireNonNull(uri, "uri is null!");
        if( uri == null ) return false;

        final String scheme = uri.getScheme();

        return ( Objects.nonNull(scheme) && "file".equalsIgnoreCase(scheme) )
                ? resetState( Paths.get(uri) )
                : false;
    }

    /**
     *
     * @param file
     */
    public boolean resetState( Path file ) {
        //Objects.requireNonNull(file, "file is null!");
        if( file == null ) return false;

        final Path b = file.toAbsolutePath();

        final Map<String,JsonValue> s =  storage.get(endpoint);

        final String key = outdir.relativize( b ).toString();

        s.put(key, createValue("0"));
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
