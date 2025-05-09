package de.soco.software.simuspace.suscore.object.model;

import javax.validation.constraints.NotNull;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The Class MetaDataEntryDTO is used for the mapping of object meta data map .
 *
 * @author Zeeshan jamal
 */
public class MetaDataEntryDTO {

    /**
     * The key.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "metadata[].key", title = "3000062x4", isAsk = true )
    @UIColumn( data = "metadata[].key", name = "key", filter = "text", isSortable = false, renderer = "text", title = "3000062x4" )
    private String key;

    /**
     * The value.
     */
    @UIFormField( name = "metadata[].value", title = "3000063x4", isAsk = true )
    @UIColumn( data = "metadata[].value", name = "value", filter = "text", isSortable = false, renderer = "text", title = "3000063x4" )
    private String value;

    /**
     * Instantiates a new meta data entry DTO.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    public MetaDataEntryDTO( String key, String value ) {
        this.key = key;
        this.value = value;
    }

    /**
     * Instantiates a new meta data entry DTO.
     */
    public MetaDataEntryDTO() {
        super();
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the key.
     *
     * @param key
     *         the new key
     */
    public void setKey( String key ) {
        this.key = key;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *         the new value
     */
    public void setValue( String value ) {
        this.value = value;
    }

}
