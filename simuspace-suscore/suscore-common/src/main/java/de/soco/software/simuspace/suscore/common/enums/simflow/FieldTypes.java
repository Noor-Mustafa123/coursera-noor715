package de.soco.software.simuspace.suscore.common.enums.simflow;

import java.util.HashSet;
import java.util.Set;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * Each field of a workflow element has a type defined in this Enum.
 *
 * @author Huzaifah
 */
public enum FieldTypes {

    /**
     * A field type "boolean" of the field of Workflow Element.
     */
    BOOLEAN( "boolean" ),

    /**
     * TA field type "directory" of the field of Workflow Element.
     */
    DIRECTORY( "os-directory" ),

    /**
     * A field type "file" of the field of Workflow Element.
     */
    FILE( "os-file" ),

    /**
     * A field type "float" of the field of Workflow Element.
     */
    FLOAT( "float" ),

    /**
     * A field type "integer" of the field of Workflow Element.
     */
    INTEGER( "integer" ),

    /**
     * A field type "object" of the field of Workflow Element.
     */
    OBJECT( "object" ),

    /**
     * A field type "section" of the field of Workflow Element.
     */
    SECTION( "section" ),

    /**
     * A field type "select" of the field of Workflow Element.
     */
    SELECTION( "select" ),

    /**
     * A field type "text" of the field of Workflow Element.
     */
    TEXT( "text" ),

    /**
     * A field type "textarea" of the field of Workflow Element.
     */
    TEXTAREA( "textarea" ),

    /**
     * A field type "vector" of the field of Workflow Element.
     */
    VECTOR( "vector" ),

    /**
     * A field type "checkbox" of the field of Workflow Element.
     */
    CHECKBOX( "checkbox" ),

    /**
     * A field type "date" of the field of Workflow Element.
     */
    DATE( "date" ),

    /**
     * A field type "dateRange" of the field of Workflow Element.
     */
    DATE_RANGE( "dateRange" ),

    /**
     * A field type "projectSelector" of the field of Workflow Element.
     */
    PROJECT_SELECTOR( "projectSelector" ),

    /**
     * A field type "locationSelector" of the field of Workflow Element.
     */
    LOCATION_SELECTOR( "locationSelector" ),

    /**
     * A field type "disciplineSelector" of the field of Workflow Element.
     */
    DISCIPLINE_SELECTOR( "disciplineSelector" ),

    /**
     * A field type "variantSelector" of the field of Workflow Element.
     */
    VARIANT_SELECTOR( "variantSelector" ),

    /**
     * A field type "divider" of the field of Workflow Element.
     */
    DIVIDER( "divider" ),

    /**
     * A field type "workflow" of the field of Workflow Element.
     */
    WORKFLOW( "workflow" ),

    /**
     * The subworkflow.
     */
    SUBWORKFLOW( "sub-workflow" ),

    /**
     * A field type "table" of the field of Workflow Element.
     */
    TABLE( "table" ),

    /**
     * The template file.
     */
    REGEX_FILE( "scheme-regex-file" ),

    /**
     * The Scheme parser object.
     */
    OBJECT_PARSER( "scheme-parse-file" ),

    /**
     * The regex scan server.
     */
    REGEX_SCAN_SERVER( "scheme-regex-object" ),

    /**
     * The Template File object.
     */
    TEMPLATE_FILE( "scheme-template-file" ),

    /**
     * The template object.
     */
    TEMPLATE_SCAN_SERVER( "scheme-template-object" ),

    /**
     * The server file.
     */
    SERVER_FILE_EXPLORER( "server-file-explorer" ),

    /**
     * The cb2 objects.
     */
    CB2_OBJECTS( "CB2 Objects" ),

    /**
     * The cb2 include.
     */
    CB2_INCLUDE( "cb2 includes" ),

    /**
     * The sus object.
     */
    SUS_OBJECT( "sus-object" ),

    /**
     * The sus workflow.
     */
    SUS_WORKFLOW( "sus-workflow" ),

    /**
     * The color.
     */
    COLOR( "color" ),

    /**
     * The input table.
     */
    INPUT_TABLE( "input-table" ),

    /**
     * The connected table.
     */
    CONNECTED_TABLE( "connected-table" ),

    /**
     * Workflow element field types.
     */
    WORKFLOW_ELEMENT( "wf-element" ),

    /**
     * Code field types.
     */
    CODE( "code" );

    /**
     * Gets all the possible field types of fields of a workflow element.
     *
     * @return the types
     */
    public static Set< String > getTypes() {
        final Set< String > result = new HashSet<>();
        for ( final FieldTypes type : FieldTypes.values() ) {
            result.add( type.getType() );
        }
        return result;
    }

    /**
     * Gets by type.
     *
     * @param type
     *         the type
     *
     * @return the by type
     */
    public static FieldTypes getByType( String type ) {
        for ( FieldTypes option : FieldTypes.values() ) {
            if ( type.equals( option.getType() ) ) {
                return option;
            }
        }
        throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), type ) );
    }

    /**
     * The type.
     */
    private final String type;

    /**
     * Instantiates a new field types.
     *
     * @param type
     *         the type
     */
    FieldTypes( String type ) {
        this.type = type;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }
}
