package de.soco.software.simuspace.suscore.common.parser.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class ParserPropertiesDTO.
 *
 * @author noman arshad
 * @since 2.0
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ParserPropertiesDTO {

    /**
     * The name.
     */
    private String name;

    /**
     * The value.
     */
    private String value;

    /**
     * Instantiates a new parser properties DTO.
     */
    public ParserPropertiesDTO() {
    }

    /**
     * Instantiates a new parser properties DTO.
     *
     * @param name
     *         the name
     * @param value
     *         the value
     */
    public ParserPropertiesDTO( String name, String value ) {
        super();
        this.name = name;
        this.value = value;
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

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "ParserPropertiesDTO [name=" + name + ", value=" + value + "]";
    }

}
