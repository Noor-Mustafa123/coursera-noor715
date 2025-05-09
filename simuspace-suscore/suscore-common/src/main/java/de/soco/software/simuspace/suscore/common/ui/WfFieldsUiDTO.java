package de.soco.software.simuspace.suscore.common.ui;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class SgeUiDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class WfFieldsUiDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 7345063754106029139L;

    /**
     * The id.
     */
    String id;

    /**
     * The name.
     */
    String name;

    /**
     * The icon.
     */
    String icon;

    /**
     * Instantiates a new sge ui DTO.
     */
    public WfFieldsUiDTO() {
    }

    /**
     * Instantiates a new sge ui DTO.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param icon
     *         the icon
     */
    public WfFieldsUiDTO( String id, String name, String icon ) {
        super();
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

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
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets the icon.
     *
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the icon.
     *
     * @param icon
     *         the new icon
     */
    public void setIcon( String icon ) {
        this.icon = icon;
    }

}
