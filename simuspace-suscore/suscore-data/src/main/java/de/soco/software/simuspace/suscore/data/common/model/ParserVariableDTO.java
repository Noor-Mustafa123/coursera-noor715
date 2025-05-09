package de.soco.software.simuspace.suscore.data.common.model;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The Class ParserVariableDTO is DTO and Entity for temp table.
 *
 * @author noman arshad
 * @since 2.0
 */

@JsonIgnoreProperties( ignoreUnknown = true )
public class ParserVariableDTO implements Serializable {

    /**
     * serial num.
     */
    @Serial
    private static final long serialVersionUID = -978327249846258593L;

    /**
     * The id.
     */
    @UIFormField( name = "id", title = "3000021x4", type = "hidden")
    @UIColumn( data = "id", name = "id", filter = "uuid", renderer = "text", title = "3000021x4" )
    private String id;

    /**
     * The variable name.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "variableName", title = "3000155x4", orderNum = 1 )
    @UIColumn( data = "variableName", name = "variableName", filter = "", renderer = "text", title = "3000155x4", orderNum = 1, isSortable = false )
    @Column( name = "variableName" )
    private String variableName;

    /**
     * The name.
     */
    @UIFormField( name = "name", title = "3000032x4", orderNum = 2 )
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000032x4", orderNum = 2, width = 0 )
    private String name;

    /**
     * The description.
     */
    @UIFormField( name = "type", title = "3000051x4", orderNum = 3 )
    @UIColumn( data = "type", name = "type", filter = "text", renderer = "text", title = "3000051x4", orderNum = 3 )
    private String type;

    /**
     * The description.
     */
    @UIFormField( name = "scannedValue", title = "3000156x4", orderNum = 4 )
    @UIColumn( data = "scannedValue", name = "scannedValue", filter = "", renderer = "text", title = "3000156x4", orderNum = 4, isSortable = false )
    private String scannedValue;

    /**
     * The info.
     */
    private Object info;

    /**
     * The full index.
     */
    private String fullIndex;

    /**
     * The is part.
     */
    private boolean isPart;

    /**
     * Instantiates a new parser variable DTO.
     */
    public ParserVariableDTO() {

    }

    /**
     * Instantiates a new parser variable DTO.
     *
     * @param id
     *         the id
     * @param variableName
     *         the variable name
     * @param name
     *         the name
     * @param type
     *         the type
     * @param scannedValue
     *         the scanned value
     */
    public ParserVariableDTO( String id, String variableName, String name, String type, String scannedValue ) {
        super();
        this.id = id;
        this.variableName = variableName;
        this.name = name;
        this.type = type;
        this.scannedValue = scannedValue;
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
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * Gets the scanned value.
     *
     * @return the scanned value
     */
    public String getScannedValue() {
        return scannedValue;
    }

    /**
     * Sets the scanned value.
     *
     * @param scannedValue
     *         the new scanned value
     */
    public void setScannedValue( String scannedValue ) {
        this.scannedValue = scannedValue;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "ParserVariableDTO [id=" + id + ", variableName=" + variableName + ", name=" + name + ", type=" + type + ", scannedValue="
                + scannedValue + ", info=" + info + ", isPart=" + isPart + "]";
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
