package de.soco.software.simuspace.suscore.common.enums;

/**
 * The Enum BmwCaeBenchSubModelEnums.
 *
 * @author NOMAN ARSHAD
 */
public enum BmwCaeBenchSubModelEnums {

    /**
     * The name.
     */
    NAME( "name", "name" ),

    LABEL( "label", "label" ),

    /**
     * The description.
     */
    DESCRIPTION( "description", "description" ),

    /**
     * The created at.
     */
    CREATED_AT( "createdAt", "createdAt" ),

    /**
     * The assembly type.
     */
    ASSEMBLY_TYPE( "assembleType", "assembleType.name" ),

    /**
     * The car project.
     */
    CAR_PROJECT( "carProject", "carProject.name" ),

    /**
     * Model state bmw cae bench sub model enums.
     */
    MODEL_STATE( "modelState", "modelState.name" ),

    /**
     * The owner.
     */
    OWNER( "owner", "owner.userName" ),

    /**
     * The variant lable.
     */
    VARIANT_LABLE( "variantLabel", "variantLabel" ),

    /**
     * The model state label.
     */
    MODEL_STATE_LABEL( "modelStatelabel", "modelStatelabel" ),

    /**
     * The model def label.
     */
    MODEL_DEF_LABEL( "modelDefLabel", "modelDefLabel" ),

    /**
     * The project.
     */
    PROJECT( "project", "project.name" ),

    /**
     * Type bmw cae bench sub model enums.
     */
    TYPE( "type", "type.name" ),

    /**
     * The created by.
     */
    CREATED_BY( "createdBy", "createdBy.userName" ),

    /**
     * Discipline context bmw cae bench sub model enums.
     */
    DISCIPLINE_CONTEXT( "disciplineContext", "disciplineContext.name" ),

    /**
     * Format bmw cae bench sub model enums.
     */
    FORMAT( "format", "format.name" ),

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
    BmwCaeBenchSubModelEnums( String key, String value ) {
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
        for ( BmwCaeBenchSubModelEnums bmwCaeBenchEnums : values() ) {
            if ( bmwCaeBenchEnums.getValue().equals( matrexValue ) ) {
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
        for ( BmwCaeBenchSubModelEnums bmwCaeBenchEnums : values() ) {
            if ( bmwCaeBenchEnums.getKey().equals( matrixKey ) ) {
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
    public static BmwCaeBenchSubModelEnums[] getAllEnums() {
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