package de.soco.software.simuspace.suscore.common.enums;

/**
 * The Enum BmwCaeBenchResultEnum.
 *
 * @author noman arshad
 */
public enum BmwCaeBenchResultEnum {

    /**
     * The name.
     */
    NAME( "name", "name" ),

    /**
     * The description.
     */
    DESCRIPTION( "description", "description" ),

    /**
     * The created at.
     */
    CREATED_AT( "createdAt", "createdAt" ),

    /**
     * The release level label.
     */
    RELEASE_LEVEL_LABEL( "releaseLevelLabel", "releaseLevelLabel" ),

    /**
     * The lebel.
     */
    ALERT( "alert", "alert" ),

    /**
     * The type.
     */
    AUTO_DELETE( "autoDelete", "autoDelete" ),

    /**
     * Type bmw cae bench sub model enums.
     */
    TYPE( "type", "type.name" ),

    /**
     * Discipline context bmw cae bench sub model enums.
     */
    DISCIPLINE_CONTEXT( "disciplineContext", "disciplineContext.name" ),

    /**
     * The owner.
     */
    OWNER( "owner", "owner.userName" );

    /**
     * Instantiates a new bmw cae bench enums.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    BmwCaeBenchResultEnum( String key, String value ) {
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
        for ( BmwCaeBenchResultEnum bmwCaeBenchEnums : values() ) {
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
        for ( BmwCaeBenchResultEnum bmwCaeBenchEnums : values() ) {
            if ( bmwCaeBenchEnums.getKey().contains( matrixKey ) ) {
                value = bmwCaeBenchEnums.getValue();
                break;
            }
        }
        return value;
    }

    /**
     * Gets the all enums.
     *
     * @return the all enums
     */
    public static BmwCaeBenchResultEnum[] getAllEnums() {
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