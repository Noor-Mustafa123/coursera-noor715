package de.soco.software.simuspace.suscore.common.enums;

/**
 * The enum Overview plugin enums.
 */
public enum OverviewPluginEnums {

    /**
     * The Vmcl overview.
     */
    VMCL_OVERVIEW( 0, "vmclOverview", "vMCL Overview" );

    /**
     * The Key.
     */
    private final int key;

    /**
     * The Id.
     */
    private final String id;

    /**
     * The Name.
     */
    private final String name;

    /**
     * Instantiates a new Overview plugin enums.
     *
     * @param key
     *         the key
     * @param id
     *         the id
     * @param name
     *         the name
     */
    OverviewPluginEnums( int key, String id, String name ) {
        this.key = key;
        this.id = id;
        this.name = name;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public int getKey() {
        return key;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}
