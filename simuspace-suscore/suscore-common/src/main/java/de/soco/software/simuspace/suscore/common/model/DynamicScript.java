package de.soco.software.simuspace.suscore.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class ActionScripts. Maps script details for dynamic scripts
 *
 * @author Shahzeb Iqbal
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DynamicScript {

    /**
     * The constant ACTION_SCRIPTS_KEY.
     */
    public static final String ACTION_SCRIPTS_KEY = "actionScripts";

    /**
     * The constant SELECT_SCRIPTS_KEY.
     */
    public static final String SELECT_SCRIPTS_KEY = "selectScripts";

    /**
     * The constant FIELD_SCRIPTS_KEY.
     */
    public static final String FIELD_SCRIPTS_KEY = "fieldScripts";

    /**
     * The constant DASHBOARD_SCRIPTS_KEY.
     */
    public static final String DASHBOARD_SCRIPTS_KEY = "dashboardScript";

    /**
     * The Name
     */
    private String name;

    /**
     * The path
     */
    private String path;

    /**
     * The type
     */
    private String type;

    /**
     * The envCmd
     */
    private String envCmd;

}
