package org.bsc.confluence.rest.model;

import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.ConfluenceService.Storage;

import javax.json.JsonObject;

public class Blogpost implements Model.Blogpost {

    private Model.ID id = null;
    private String space;
    private String title;
    private int version = 0;
    private Storage content;

    public Blogpost() {
    }

    public Blogpost(JsonObject data) {
        if( data==null ) {
            throw new IllegalArgumentException("data argument is null!");
        }

        setId( Model.ID.of(data.getString("id")) );
        setSpace( data.getJsonObject("space").getString("key") );
        setTitle( data.getString("title") );
        setVersion( data.getJsonObject("version").getInt("number", 0) );
    }

    @Override
    public Model.ID getId() { return id; }
    public void setId(Model.ID id) { this.id = id; }

    @Override
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) { this.title = title; }

    @Override
    public String getSpace() {
        return space;
    }
    public void setSpace(String space) { this.space = space; }

    @Override
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) { this.version = version; }

    public Storage getContent() { return content; }

    public void setContent(Storage content) { this.content = content; }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Blogpost [id=")
                .append(id)
                .append(", space=")
                .append(space)
                .append(", title=")
                .append(title)
                .append(", version=")
                .append(version)
                .append("]")
                .toString();
    }
}
