package de.soco.software.simuspace.suscore.common.enums;

/**
 * The Enum UIThemeEnums.
 */
public enum UIThemeEnums {

    SYSTEM( "System", "Sync with system" ),

    /**
     * The light.
     */
    LIGHT( "Light", "Light" ),

    /**
     * The dark.
     */
    DARK( "Dark", "Dark" );

    /**
     * The id.
     */
    private final String id;

    /**
     * The name.
     */
    private final String name;

    /**
     * Instantiates a new UI theme enums.
     *
     * @param name
     *         the name
     */
    UIThemeEnums( String id, String name ) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the all.
     *
     * @return the all
     */
    public static UIThemeEnums[] getAll() {
        return UIThemeEnums.class.getEnumConstants();
    }

}
