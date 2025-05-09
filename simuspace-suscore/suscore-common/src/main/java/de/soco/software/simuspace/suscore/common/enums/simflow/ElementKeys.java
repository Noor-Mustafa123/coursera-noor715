package de.soco.software.simuspace.suscore.common.enums.simflow;

import java.util.HashSet;
import java.util.Set;

/**
 * A Workflow can contain multiple elements and each element has unique key defined in this Enum.
 */
public enum ElementKeys {

    /**
     * A workflow element key of the input/output element.
     */
    IO( "wfe_io" ),
    /**
     * A workflow element key of the script element.
     */
    SCRIPT( "wfe_script" ),

    /**
     * The python.
     */
    PYTHON( "wfe_python" ),

    /**
     * Workflow Element Key of the SUSAction Element.
     */
    SUS_ACTION( "wfe_sus_action" ),

    /**
     * A workflow element key of export files.
     */
    SUS_EXPORT_FILES( "wfe_export_files" ),

    /**
     * A workflow element key of export dataobject.
     */
    SUS_EXPORT_DATAOBJECT( "wfe_export_dataobject" ),

    /**
     * The sus dataobject import.
     */
    SUS_DATAOBJECT_IMPORT( "wfe_dataObject_import" ),

    /**
     * A workflow element key of import dataobject.
     */
    SUS_IMPORT_DATAOBJECT( "wfe_import_dataobject" ),

    /**
     * A workflow element key of the conditional element.
     */
    SUS_CONDITIONAL( "wfe_condition" ),

    /**
     * A workflow element key of the wait element.
     */
    WFE_WAIT( "wfe_wait" ),

    /**
     * A workflow element key of import objects.
     */
    WFE_IMPORT_OBJECTS( "wfe_import_objects" ),

    /**
     * A workflow element key of subworkflow.
     */
    WFE_SUB_WORKFLOW( "wfe_subworkflow" ),

    /**
     * A workflow element key of transfer location.
     */
    WFE_TRANSFER_LOCATION( "wfe_transfer_location" ),

    /**
     * A workflow element key of import cb2.
     */
    WFE_IMPORT_CB2( "wfe_import_cb2" ),

    /**
     * A workflow element key of Abaqus.
     */
    WFE_ABAQUSEXPLIZIT( "wfe_abaqusexplizit" ),

    /**
     * A workflow element key of Abaqus.
     */
    WFE_ABAQUSSTANDARD( "wfe_abaqusstandard" ),

    /**
     * A workflow element key of LsDyna.
     */
    WFE_LSDYNA( "wfe_lsDyna" ),
    /**
     * A workflow element key of Nastran.
     */
    WFE_NASTRAN( "wfe_nastran" ),

    /**
     * A workflow element key of optiStruct.
     */
    WFE_OPTISTRUCT( "wfe_optistruct" ),

    /**
     * A workflow element key of for each loop.
     */
    WFE_FOREACHLOOP( "wfe_foreachloop" ),

    /**
     * A workflow element key of while loop.
     */
    WFE_WHILELOOP( "wfe_whileloop" ),

    /**
     * A workflow element key of until loop.
     */
    WFE_UNTILLOOP( "wfe_untilloop" ),

    /**
     * A workflow element key of email.
     */
    WFE_EMAIL( "wfe_email" ),

    /**
     * The wfe assemble and simulate.
     */
    WFE_ASSEMBLE_AND_SIMULATE( "wfe_assemble_and_simulate" ),

    /**
     * The wfe machine learning.
     */
    WFE_MACHINE_LEARNING( "wfe_train_model_sus" );

    /**
     * Gets all the possible keys a workflow element can contain.
     *
     * @return the keys
     */

    public static Set< String > getkeys() {
        final Set< String > result = new HashSet<>();
        for ( final ElementKeys key : ElementKeys.values() ) {
            result.add( key.getKey() );
        }
        return result;
    }

    /**
     * The key of the element.
     */
    private final String key;

    /**
     * Instantiates a new element keys.
     *
     * @param key
     *         the key
     */
    ElementKeys( String key ) {
        this.key = key;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */

    public String getKey() {
        return key;
    }

}
