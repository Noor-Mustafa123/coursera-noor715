package de.soco.software.suscore.jsonschema.model;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class PostProcessWorkflow.
 *
 * @author noman arshad
 */

@JsonIgnoreProperties( ignoreUnknown = true )
public class PostProcessWorkflow implements Serializable {

    /**
     * The id.
     */
    private String id;

    /**
     * The elements.
     */
    private Map< String, Object > elements;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( String id ) {
        this.id = id;
    }

    public Map< String, Object > getElements() {
        return elements;
    }

    public void setElements( Map< String, Object > elements ) {
        this.elements = elements;
    }

}
