package org.bsc.confluence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;

public class DeployStateManager {

    //private final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy hhmmss");

    private static final String STORAGE_NAME = "state.json";

    private Map<String, Map<String,JsonValue>> storage ;

    private Optional<String> _endpoint = Optional.empty();
    private Optional<Path> _outdir = Optional.empty() ;
    private boolean active = false;
    
    public DeployStateManager() {
    }

    /**
     * 
     * @param endpoint
     */
    public void init( String endpoint ) {
        this._endpoint = Optional.ofNullable(endpoint);
    }
    
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
    public void setOutdir(Path outdir) {
        this._outdir = Optional.ofNullable(outdir);
    }

    /**
     * @param outdir the basedir to set
     */
    public Optional<Path> getOutdir() {
        return this._outdir;  
    }

    public final String getFileName() {
        return STORAGE_NAME;
    }
    
    private boolean isValid() {
        return active && _endpoint.isPresent() && _outdir.isPresent();
    }
    /**
        * 
        * @param stateDir
        */
    public void load() {
        
        if( !isValid() ) return;
        
        final Path file = Paths.get(_outdir.get().toString(), STORAGE_NAME);
        
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
        if( !storage.containsKey(_endpoint.get()) ) {              
            storage.put( _endpoint.get(), new HashMap<String,JsonValue>() );   
            save();
        }
        
        
    }
    
    /**
        * 
        * @param stateDir
        */
    private void save() {
        if( !isValid() ) return;
        
        final Path file = Paths.get(_outdir.get().toString(), STORAGE_NAME);
        
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
    
    public boolean isUpdated( java.net.URI uri ) {
        Objects.requireNonNull(uri, "uri is null!");
        
        final String scheme = uri.getScheme();
        
        if ( Objects.nonNull(scheme) && "file".equalsIgnoreCase(scheme) ) {

            return isUpdated( Paths.get(uri) );
        
        }

        return true;

    }
    
    public boolean isUpdated(Path file) {
        Objects.requireNonNull(file, "file is null!");

        if( !isValid() ) return true;

        final Path b = file.toAbsolutePath();
        
        final Map<String,JsonValue> s =  storage.get(_endpoint.get());
           
        final String key = _outdir.get().relativize( b ).toString();

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


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuilder()
                    .append("DeployStateManager").append("\n\t")
                    .append("active=").append(active).append("\n\t")
                    .append("endpoint=").append(_endpoint).append("\n\t")
                    .append("outdir=").append(_outdir).append("\n")
                    .toString();
    }
    

}