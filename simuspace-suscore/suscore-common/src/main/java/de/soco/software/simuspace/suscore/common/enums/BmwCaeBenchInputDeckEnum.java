package de.soco.software.simuspace.suscore.common.enums;

/**
 * The Enum BmwCaeBenchInputDeckEnum.
 *
 * @author noman arshad
 */
public enum BmwCaeBenchInputDeckEnum {

    /**
     * The name.
     */
    NAME( "name", "name" ),

    /**
     * The lable.
     */
    LABLE( "label", "label" ),

    /**
     * The format.
     */
    FORMAT( "format", "format.name" ),

    /**
     * The type.
     */
    TYPE( "type", "type.name" ),

    /**
     * The project.
     */
    PROJECT( "project", "project.name" ),

    /**
     * The project phase.
     */
    PROJECT_PHASE( "projectPhase", "projectPhase.name" ),

    /**
     * The variant.
     */
    VARIANT( "variant", "variant.name" ),

    /**
     * The sim process status.
     */
    SIM_PROCESS_STATUS( "simProcessStatus", "simProcessStatus" ),

    /**
     * The sim post process status.
     */
    SIM_POST_PROCESS_STATUS( "simPostProcessStatus", "simPostProcessStatus" ),

    /**
     * The description.
     */
    DESCRIPTION( "description", "description" ),

    /**
     * Discipline context bmw cae bench sub model enums.
     */
    DISCIPLINE_CONTEXT( "disciplineContext", "disciplineContext.name" ),

    /**
     * The owner.
     */
    OWNER( "owner", "owner.userName" ),

    /**
     * The created at.
     */
    CREATED_AT( "createdAt", "createdAt" );

    /**
     * Instantiates a new bmw cae bench enums.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    BmwCaeBenchInputDeckEnum( String key, String value ) {
        this.key = key;
        this.value = value;
    }

    /**
     * The key.
     */
    private final String key;

    /**
     * The value.
     */
    private final String value;

    /**
     * Gets the key by value.
     *
     * @param matrexValue
     *         the matrex value
     *
     * @return the key by value
     */
    public static String getKeyByValue( String matrexValue ) {
        String key = "";
        for ( BmwCaeBenchInputDeckEnum bmwCaeBenchEnums : values() ) {
            if ( bmwCaeBenchEnums.getValue().contains( matrexValue ) ) {
                key = bmwCaeBenchEnums.getKey();
                break;
            }
        }
        return key;
    }

    /**
     * Gets the value by key.
     *
     * @param matrixKey
     *         the matrix key
     *
     * @return the value by key
     */
    public static String getValueByKey( String matrixKey ) {
        String value = "";
        for ( BmwCaeBenchInputDeckEnum bmwCaeBenchEnums : values() ) {
            if ( bmwCaeBenchEnums.getKey().contains( matrixKey ) ) {
                value = bmwCaeBenchEnums.getValue();
                break;
            }
        }
        return value;
    }

    public static BmwCaeBenchInputDeckEnum[] getAllEnums() {
        return values();
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

}
