package de.soco.software.simuspace.server.model.jsonschema;

public class SMSnippet {

    String label;

    String description;

    Object body;

    public String getLabel() {
        return label;
    }

    public void setLabel( String label ) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Object getBody() {
        return body;
    }

    public void setBody( Object body ) {
        this.body = body;
    }

}