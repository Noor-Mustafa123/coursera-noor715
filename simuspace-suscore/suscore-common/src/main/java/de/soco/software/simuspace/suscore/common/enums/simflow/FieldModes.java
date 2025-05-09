package de.soco.software.simuspace.suscore.common.enums.simflow;

import java.util.HashSet;
import java.util.Set;

/**
 * A Workflow Element can contain multiple types of fields/data. These field types are defined in this Enum.
 *
 * @author shaista
 */
public enum FieldModes {

    /**
     * A field mode "template" of the Workflow Element for FE use.
     */
    TEMPLATE( "template" ),

    /**
     * A field mode "user" of the Workflow Element only with user added fields.
     */
    USER( "user" );

    /**
     * Gets all the possible field modes a workflow element can contain.
     *
     * @return the types
     */
    public static Set< String > getTypes() {
        final Set< String > result = new HashSet<>();
        for ( final FieldModes type : FieldModes.values() ) {
            result.add( type.getType() );
        }
        return result;
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

    FieldModes( String type ) {
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
