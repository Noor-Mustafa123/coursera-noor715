package de.soco.software.simuspace.suscore.common.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;

/**
 * The Class TemplateScanDTO.
 *
 * @author Fahad Rafi
 */
public class TemplateScanDTO {

    /**
     * The id.
     */
    @UIFormField( name = "id", title = "3000021x4", isAsk = false, type = "hidden")
    @UIColumn( data = "id", filter = "uuid", renderer = "text", title = "3000021x4", name = "id", type = "link", isShow = false )
    private UUID id;

    /**
     * The variable name.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIColumn( data = "variableName", name = "variableName", filter = "text", renderer = "text", title = "9700001x4", orderNum = 1 )
    @UIFormField( name = "variableName", title = "9700001x4", orderNum = 1 )
    private String variableName;

    /**
     * The line number.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIColumn( data = "lineNumber", name = "lineNumber", filter = "text", renderer = "text", title = "9710001x4", orderNum = 2 )
    @UIFormField( name = "lineNumber", title = "9710001x4", orderNum = 2 )
    private String lineNumber;

    /**
     * The start.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIColumn( data = "start", name = "start", filter = "text", renderer = "text", title = "9710002x4", orderNum = 2 )
    @UIFormField( name = "start", title = "9710002x4", orderNum = 2 )
    private String start;

    /**
     * The end.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIColumn( data = "end", name = "end", filter = "text", renderer = "text", title = "9710003x4", orderNum = 2 )
    @UIFormField( name = "end", title = "9710003x4", orderNum = 2 )
    private String end;

    /**
     * The match.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIColumn( data = "match", name = "match", filter = "text", renderer = "text", title = "9710004x4", orderNum = 2 )
    @UIFormField( name = "match", title = "9710004x4", orderNum = 2 )
    private String match;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( UUID id ) {
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
     * Gets the line number.
     *
     * @return the line number
     */
    public String getLineNumber() {
        return lineNumber;
    }

    /**
     * Sets the line number.
     *
     * @param lineNumber
     *         the new line number
     */
    public void setLineNumber( String lineNumber ) {
        this.lineNumber = lineNumber;
    }

    /**
     * Gets the start.
     *
     * @return the start
     */
    public String getStart() {
        return start;
    }

    /**
     * Sets the start.
     *
     * @param start
     *         the new start
     */
    public void setStart( String start ) {
        this.start = start;
    }

    /**
     * Gets the end.
     *
     * @return the end
     */
    public String getEnd() {
        return end;
    }

    /**
     * Sets the end.
     *
     * @param end
     *         the new end
     */
    public void setEnd( String end ) {
        this.end = end;
    }

    /**
     * Gets the match.
     *
     * @return the match
     */
    public String getMatch() {
        return match;
    }

    /**
     * Sets the match.
     *
     * @param match
     *         the new match
     */
    public void setMatch( String match ) {
        this.match = match;
    }

    /**
     * Validate.
     *
     * @return the notification
     */
    public Notification validate() {
        Notification notification = new Notification();
        notification.addNotification( ValidationUtils.validateFieldAndLength( getVariableName(), "Variable Name",
                ConstantsLength.STANDARD_NAME_LENGTH, false, false ) );
        if ( !ValidationUtils.validatePositiveInteger( getLineNumber() ) ) {
            notification.addError( new Error( "please provide correct positive Line Match" ) );
            return notification;
        }

        if ( !ValidationUtils.validatePositiveInteger( getStart() ) ) {
            notification.addError( new Error( "please provide correct start position" ) );
            return notification;
        }

        if ( !ValidationUtils.validatePositiveInteger( getEnd() ) ) {
            notification.addError( new Error( "please provide correct end position" ) );
            return notification;
        }
        return notification;

    }

}