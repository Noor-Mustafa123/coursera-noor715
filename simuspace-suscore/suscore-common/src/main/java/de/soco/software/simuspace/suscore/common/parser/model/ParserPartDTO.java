package de.soco.software.simuspace.suscore.common.parser.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class ParserPartDTO.
 *
 * @author noman arshad
 * @since 2.0
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ParserPartDTO {

    /**
     * The name.
     */
    private String name;

    /**
     * The properties.
     */
    private List< ParserPropertiesDTO > properties;

    /**
     * The index.
     */
    private String index;

    /**
     * The variable name.
     */
    private String variableName;

    /**
     * The full index.
     */
    private String fullIndex;

    /**
     * The info.
     */
    private Object info;

    /**
     * The parameter value.
     */
    private String parameterValue;

    /**
     * The is part.
     */
    private boolean isPart = true;

    /**
     * Gets the parameter value.
     *
     * @return the parameter value
     */
    public String getParameterValue() {
        return parameterValue;
    }

    /**
     * Sets the parameter value.
     *
     * @param parameterValue
     *         the new parameter value
     */
    public void setParameterValue( String parameterValue ) {
        this.parameterValue = parameterValue;
    }

    /**
     * Checks if is part.
     *
     * @return true, if is part
     */
    public boolean isPart() {
        return isPart;
    }

    /**
     * Sets the part.
     *
     * @param isPart
     *         the new part
     */
    public void setPart( boolean isPart ) {
        this.isPart = isPart;
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
     * Instantiates a new parser part DTO.
     */
    public ParserPartDTO() {

    }

    /**
     * Instantiates a new parser part DTO.
     *
     * @param name
     *         the name
     * @param properties
     *         the properties
     * @param index
     *         the index
     * @param variableName
     *         the variable name
     */
    public ParserPartDTO( String name, List< ParserPropertiesDTO > properties, String index, String variableName ) {
        super();
        this.name = name;
        this.properties = properties;
        this.index = index;
        this.variableName = variableName;
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
     * Gets the properties.
     *
     * @return the properties
     */
    public List< ParserPropertiesDTO > getProperties() {
        return properties;
    }

    /**
     * Sets the properties.
     *
     * @param properties
     *         the new properties
     */
    public void setProperties( List< ParserPropertiesDTO > properties ) {
        this.properties = properties;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "ParserPartDTO [name=" + name + ", properties=" + properties + ", index=" + index + ", variableName=" + variableName
                + ", info=" + info + "]";
    }

    /**
     * Gets the index.
     *
     * @return the index
     */
    public String getIndex() {
        return index;
    }

    /**
     * Sets the index.
     *
     * @param index
     *         the new index
     */
    public void setIndex( String index ) {
        this.index = index;
    }

    /**
     * Gets the variable name.
     *
     * @return the variable name
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Sets the variable name.
     *
     * @param variableName
     *         the new variable name
     */
    public void setVariableName( String variableName ) {
        this.variableName = variableName;
    }

    /**
     * Gets the info.
     *
     * @return the info
     */
    public Object getInfo() {
        return info;
    }

    /**
     * Sets the info.
     *
     * @param info
     *         the new info
     */
    public void setInfo( Object info ) {
        this.info = info;
    }

    /**
     * Gets the full index.
     *
     * @return the full index
     */
    public String getFullIndex() {
        return fullIndex;
    }

    /**
     * Sets the full index.
     *
     * @param fullIndex
     *         the new full index
     */
    public void setFullIndex( String fullIndex ) {
        this.fullIndex = fullIndex;
    }

}
