package de.soco.software.simuspace.suscore.common.enums;

/**
 * The Enum ParserDropDownEnum.
 *
 * @author noman arshad
 * @since 2.0
 */
public enum VariableDropDownEnum {

    /**
     * The internal.
     */
    INTERNAL( "SuS Object", "sus-object" ),

    /**
     * The server.
     */
    SERVER( "Server File", "server-file-explorer" ),

    /**
     * The cb2.
     */
    CB2( "CB2 Object", "cb2-object" ),

    /**
     * The local.
     */
    LOCAL( "Local File", "local-file" ),

    /**
     * The Variable.
     */
    VARIABLE( "Variable", "variable" );

    /**
     * Full constructor of the enum.
     *
     * @param name
     *         the key
     * @param id
     *         the value
     */
    VariableDropDownEnum( String name, String id ) {
        this.name = name;
        this.id = id;
    }

    /**
     * key of the constant.
     */
    private final String name;

    /**
     * value against the constant key.
     */
    private final String id;

    /**
     * Gets name by id.
     *
     * @param id
     *         the id
     *
     * @return the name by id
     */
    public static String getNameById( String id ) {
        String name = null;
        for ( VariableDropDownEnum variableDropDownEnum : values() ) {
            if ( variableDropDownEnum.getId().equalsIgnoreCase( id ) ) {
                name = variableDropDownEnum.getName();
                break;
            }
        }
        return name;
    }

    /**
     * Gets id by name.
     *
     * @param name
     *         the name
     *
     * @return the id by name
     */
    public static String getIdByName( String name ) {
        String id = "";
        for ( VariableDropDownEnum variableDropDownEnum : values() ) {
            if ( variableDropDownEnum.getName().equalsIgnoreCase( name ) ) {
                id = variableDropDownEnum.getId();
                break;
            }
        }
        return id;
    }

    /**
     * Gets the key.
     *
     * @return key of the constant
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the value.
     *
     * @return value of the constant
     */
    public String getId() {
        return id;
    }

}
