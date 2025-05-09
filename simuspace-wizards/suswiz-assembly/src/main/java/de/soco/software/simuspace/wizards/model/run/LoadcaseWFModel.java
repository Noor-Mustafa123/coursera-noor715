package de.soco.software.simuspace.wizards.model.run;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class LoadcaseWFModel.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class LoadcaseWFModel {

    /**
     * The id.
     */
    private String id;

    /**
     * The elements.
     */
    private Map< String, Map< String, Object > > elements;

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

    /**
     * Gets the elements.
     *
     * @return the elements
     */
    public Map< String, Map< String, Object > > getElements() {
        return elements;
    }

    /**
     * Sets the elements.
     *
     * @param elements
     *         the elements
     */
    public void setElements( Map< String, Map< String, Object > > elements ) {
        this.elements = elements;
    }

}
