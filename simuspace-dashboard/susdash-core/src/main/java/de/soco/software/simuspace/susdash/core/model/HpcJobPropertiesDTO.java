package de.soco.software.simuspace.susdash.core.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.model.UIColumn;

/**
 * The Class HpcJobPropertiesDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class HpcJobPropertiesDTO implements Serializable {

    private static final long serialVersionUID = -2001311394889600881L;

    /**
     * The Key.
     */
    @UIColumn( data = "key", name = "Key", filter = "text", renderer = "text", title = "3000062x4", orderNum = 0 )
    private String key;

    /**
     * The Value.
     */
    @UIColumn( data = "value", name = "Value", filter = "text", renderer = "text", title = "3000063x4", orderNum = 1 )
    private String value;

    /**
     *
     */
    public HpcJobPropertiesDTO() {
        super();
    }

    /**
     * Instantiates a new Hpc properties dto.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    public HpcJobPropertiesDTO( String key, String value ) {
        super();
        this.key = key;
        this.value = value;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     *         the key to set
     */
    public void setKey( String key ) {
        this.key = key;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *         the value to set
     */
    public void setValue( String value ) {
        this.value = value;
    }

}