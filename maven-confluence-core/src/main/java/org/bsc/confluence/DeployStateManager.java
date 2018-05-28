package org.bsc.confluence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;

public class DeployStateManager {

    private final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy hhmmss");

    private Map<String, Map<String,JsonValue>> storage ;

    private final String endpoint ;
    private final Path basedir ;
    
    public static DeployStateManager load( Path baseDir,  String endpoint ) {
        DeployStateManager  result = new DeployStateManager( endpoint, baseDir );
        
        result.load();

        return result;
    }

    protected DeployStateManager( String endpoint, Path basedir ) {

        Objects.requireNonNull(endpoint);
        Objects.requireNonNull(basedir);

        this.endpoint = endpoint;
        this.basedir = basedir;
    }

    public Path getBasedir() {
        return basedir;
    }

    private Path resolveDir( Path dir ) {
        return Files.isDirectory(dir) ?
                Paths.get(dir.toString()) :
                Paths.get(dir.getParent().toString()) ;
        
    }
    
    public Path getFile() {
        
        return Paths.get(resolveDir(basedir).toString(), ".state.json") ;
        
    }
    
    /**
        * 
        * @param stateDir
        */
    private void load() {
        
        final Path file = getFile();
        
        storage = new HashMap<>();
        
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
            storage.put( endpoint, new HashMap<String,JsonValue>() );   
            save();
        }
        
        
    }
    
    /**
        * 
        * @param stateDir
        */
    private void save() {
        
        final Path file = getFile();
        
        if( !Files.exists(file)) return;
        
        JsonObjectBuilder b = Json.createObjectBuilder();
        storage.entrySet().forEach( e -> {
            JsonObjectBuilder b1 = Json.createObjectBuilder();           
            e.getValue().entrySet().forEach( ee -> b1.add( ee.getKey(), ee.getValue()) );
            b.add( e.getKey(), b1 );
            
        });
        
        try(JsonWriter w = Json.createWriter(Files.newBufferedWriter(file)) ) {
  
            //storage.put("updated", Json.createValue(sdf.format(new java.util.Date())));
            w.writeObject(b.build());
        }
        catch( Exception ex ) {
            // TODD
            ex.printStackTrace();
        }
            
    }

    private JsonNumber createValue( long value ) {
        return Json.createArrayBuilder().add(value).build().getJsonNumber(0);
        // From 1.1
        // return Json.createValue(0L) 
    }
    
    public boolean isUpdated(Path file) {
        
        final Path b = file.toAbsolutePath();
        final Path a = resolveDir(basedir);
        
        System.out.printf("file[%s] - basedir[%s]\n", b, a );
        
        Map<String,JsonValue> s =  storage.get(endpoint);
        
        
        final String key = a.relativize( b ).toString();

        JsonNumber value = s.containsKey(key) ? 
                                (JsonNumber)s.get(key) :
                                createValue(0L);
    
        final long lastModified = file.toFile().lastModified();
        
        final long lastModifiedStored = value.longValue();
        
        if( lastModified > lastModifiedStored ) {
            s.put(key, createValue(lastModified));
            save();
            return true;
        }
        
        return false;
    }
    

}