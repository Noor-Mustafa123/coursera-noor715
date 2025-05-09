package de.soco.software.simuspace.workflow.model;

import de.soco.software.simuspace.suscore.common.constants.ConstantsID;

/**
 * The Enum contains system workflows.
 *
 * @author M.Nasir.Farooq
 */
public enum SystemWorkflow {

    /**
     * The restore.
     */
    RESTORE( "Restore System-WF", ConstantsID.RESTORE_SYSTEM_WORKFLOW_ID, "This is system workflow for restore objects" ),

    /**
     * The delete.
     */
    DELETE( "Delete System-WF", ConstantsID.DELETE_SYSTEM_WORKFLOW_ID, "This is system workflow for delete objects" ),

    /**
     * The master.
     */
    MASTER( "Master Workflow", ConstantsID.MASTER_SYSTEM_WORKFLOW_ID, "This is master workflow" );

    private final String description;

    /**
     * key of the constant.
     */
    private final String name;

    /**
     * value against the constant key.
     */
    private final String id;

    /**
     * Instantiates a new system workflow.
     *
     * @param name
     *         the name
     * @param id
     *         the id
     */
    SystemWorkflow( String name, String id, String description ) {
        this.name = name;
        this.id = id;
        this.description = description;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getName() {
        return name;
    }
}
