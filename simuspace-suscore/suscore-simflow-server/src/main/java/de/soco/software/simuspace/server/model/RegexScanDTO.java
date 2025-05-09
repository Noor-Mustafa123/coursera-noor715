package de.soco.software.simuspace.server.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;

public class RegexScanDTO {

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
    @UIColumn( data = "variableName", name = "variableName", filter = "text", renderer = "text", title = "9700001x4", orderNum = 1, width = 0 )
    @UIFormField( name = "variableName", title = "9700001x4", orderNum = 1 )
    private String variableName;

    /**
     * The line regex.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIColumn( data = "lineRegex", name = "lineRegex", filter = "text", renderer = "text", title = "9700002x4", orderNum = 2 )
    @UIFormField( name = "lineRegex", title = "9700002x4", orderNum = 2 )
    private String lineRegex;

    /**
     * The line match.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIColumn( data = "lineMatch", name = "lineMatch", filter = "", renderer = "text", title = "9700003x4", orderNum = 3 )
    @UIFormField( name = "lineMatch", title = "9700003x4", orderNum = 3 )
    private String lineMatch;

    /**
     * The line offset.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIColumn( data = "lineOffset", name = "lineOffset", filter = "", renderer = "text", title = "9700004x4", orderNum = 4 )
    @UIFormField( name = "lineOffset", title = "9700004x4", orderNum = 4 )
    private String lineOffset;

    /**
     * The variable regex.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIColumn( data = "variableRegex", name = "variableRegex", filter = "", renderer = "text", title = "9700005x4", orderNum = 5 )
    @UIFormField( name = "variableRegex", title = "9700005x4", orderNum = 5 )
    private String variableRegex;

    /**
     * The variable match.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIColumn( data = "variableMatch", name = "variableMatch", filter = "", renderer = "text", title = "9700006x4", orderNum = 6 )
    @UIFormField( name = "variableMatch", title = "9700006x4", orderNum = 6 )
    private String variableMatch;

    /**
     * The variable group.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIColumn( data = "variableGroup", name = "variableGroup", filter = "", renderer = "text", title = "9700007x4", orderNum = 7 )
    @UIFormField( name = "variableGroup", title = "9700007x4", orderNum = 7 )
    private String variableGroup;

    @UIColumn( data = "scannedLine", name = "scannedLine", filter = "", isSortable = false, renderer = "text", title = "9700008x4", orderNum = 8 )
    private String scannedLine;

    @UIColumn( data = "scannedValue", name = "scannedValue", filter = "", isSortable = false, renderer = "text", title = "9700009x4", orderNum = 9 )
    private String scannedValue;

    public UUID getId() {
        return id;
    }

    public void setId( UUID id ) {
        this.id = id;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName( String variableName ) {
        this.variableName = variableName;
    }

    public String getLineRegex() {
        return lineRegex;
    }

    public void setLineRegex( String lineRegex ) {
        this.lineRegex = lineRegex;
    }

    public String getLineMatch() {
        return lineMatch;
    }

    public void setLineMatch( String lineMatch ) {
        this.lineMatch = lineMatch;
    }

    public String getLineOffset() {
        return lineOffset;
    }

    public void setLineOffset( String lineOffset ) {
        this.lineOffset = lineOffset;
    }

    public String getVariableRegex() {
        return variableRegex;
    }

    public void setVariableRegex( String variableRegex ) {
        this.variableRegex = variableRegex;
    }

    public String getVariableMatch() {
        return variableMatch;
    }

    public void setVariableMatch( String variableMatch ) {
        this.variableMatch = variableMatch;
    }

    public String getVariableGroup() {
        return variableGroup;
    }

    public void setVariableGroup( String variableGroup ) {
        this.variableGroup = variableGroup;
    }

    public String getScannedLine() {
        return scannedLine;
    }

    public void setScannedLine( String scannedLine ) {
        this.scannedLine = scannedLine;
    }

    public String getScannedValue() {
        return scannedValue;
    }

    public void setScannedValue( String scannedValue ) {
        this.scannedValue = scannedValue;
    }

    public Notification validate() {
        Notification notification = new Notification();
        notification.addNotification( ValidationUtils.validateFieldAndLength( getVariableName(), "Variable Name",
                ConstantsLength.STANDARD_NAME_LENGTH, false, false ) );
        if ( !ValidationUtils.validatePositiveInteger( getLineMatch() ) ) {
            notification.addError( new Error( "please provide correct positive Line Match!" ) );
            return notification;
        }

        if ( !ValidationUtils.validatePositiveInteger( getLineOffset() ) ) {
            notification.addError( new Error( "please provide correct positive Line Offset!" ) );
            return notification;
        }

        if ( !ValidationUtils.validatePositiveInteger( getVariableMatch() ) ) {
            notification.addError( new Error( "please provide correct positive Variable Match!" ) );
            return notification;
        }

        if ( !ValidationUtils.validatePositiveInteger( getVariableGroup() ) ) {
            notification.addError( new Error( "please provide correct positive Variable Group!" ) );
            return notification;
        }
        return notification;

    }

}
