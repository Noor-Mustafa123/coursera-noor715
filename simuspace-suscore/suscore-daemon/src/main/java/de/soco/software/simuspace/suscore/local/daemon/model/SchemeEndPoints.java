package de.soco.software.simuspace.suscore.local.daemon.model;

/**
 * The Class SchemeEndPoints.
 */
public class SchemeEndPoints {

    /**
     * The Constant API_V1_WORKFLOW.
     */
    public static final String API_CONFIG_WORKFLOWSCHEME = "/api/config/workflowscheme";

    /**
     * The Constant RUNSCHEME.
     */
    public static final String RUNSCHEME = "/{workflowId}/runscheme";

    /**
     * Private constructor to hide implicit one.
     */
    private SchemeEndPoints() {
        super();
    }

}
