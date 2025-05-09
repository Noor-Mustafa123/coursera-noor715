package de.soco.software.simuspace.suscore.common.enums;

/**
 * The Enum BmwCaeBenchKeyResultEnum.
 *
 * @author NOMAN ARSHAD
 */
public enum BmwCaeBenchKeyResultEnum {

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
    LEBEL( "label", "label" ),

    /**
     * The type.
     */
    TYPE( "type", "type.name" ),

    /**
     * The project.
     */
    PROJECT( "project", "project.name" ),

    /**
     * The created by.
     */
    CREATED_BY( "createdBy", "createdBy.name" ),

    /**
     * The owner.
     */
    OWNER( "owner", "owner.userName" ),

    /**
     * Discipline context bmw cae bench sub model enums.
     */
    DISCIPLINE_CONTEXT( "disciplineContext", "disciplineContext.name" ),

    /**
     * The input deck.
     */
    INPUT_DECK( "inputDeck", "inputDeck.name" );

    /**
     * Instantiates a new bmw cae bench enums.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    BmwCaeBenchKeyResultEnum( String key, String value ) {
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
        for ( BmwCaeBenchKeyResultEnum bmwCaeBenchEnums : values() ) {
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
        for ( BmwCaeBenchKeyResultEnum bmwCaeBenchEnums : values() ) {
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
    public static BmwCaeBenchKeyResultEnum[] getAllEnums() {
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
