package de.soco.software.simuspace.suscore.common.enums;

/**
 * The enum Dashboard plugin enums.
 *
 * @deprecated since soco/2.3.1/release
 */
@Deprecated( since = "soco/2.3.1/release", forRemoval = true )
public enum DashboardPluginEnums {

    /**
     * Plungerlift dashboard plugin enums.
     */
    PLUNGERLIFT( 0, "plungerlift", "Plunger Lift" ),
    /**
     * Licence monitor dashboard plugin enums.
     */
    LICENCE_MONITOR( 1, "licenseMonitor", "License Monitor" ),

    /**
     * Hpc dashboard plugin enums.
     */
    HPC( 2, "HPC", "HPC" ),

    /**
     * Materials dashboard plugin enums.
     */
    PROJECT( 3, "project", "Project" ),

    /**
     * Pst dashboard plugin enums.
     */
    PST( 4, "pst", "PST" );

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
     * Instantiates a new Dashboard plugin enums.
     *
     * @param key
     *         the key
     * @param id
     *         the id
     * @param name
     *         the name
     */
    DashboardPluginEnums( int key, String id, String name ) {
        this.key = key;
        this.id = id;
        this.name = name;
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

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }
}
