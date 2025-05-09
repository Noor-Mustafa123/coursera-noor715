package de.soco.software.simuspace.suscore.common.enums;

/**
 * The Enum BmwCaeBenchVariantEnum.
 *
 * @author noman arshad
 */
public enum BmwCaeBenchVariantEnum {

    /**
     * The name.
     */
    NAME( "name", "name" ),

    /**
     * The description.
     */
    DESCRIPTION( "description", "description" ),

    /**
     * The label.
     */
    LABEL( "label", "label" ),

    /**
     * The type.
     */
    TYPE( "type", "type.name" ),

    /**
     * The created at.
     */
    CREATED_AT( "createdAt", "createdAt" ),

    /**
     * The owner.
     */
    OWNER( "owner.userName", "owner.userName" ),

    /**
     * The created by.
     */
    CREATED_BY( "createdBy.userName", "createdBy.userName" ),

    /**
     * The discipline context.
     */
    DISCIPLINE_CONTEXT( "disciplineContext.name", "disciplineContext.name" ),

    /**
     * The derived from.
     */
    DERIVED_FROM( "derivedFrom", "derivedFrom.name" ),

    /**
     * The release level lable.
     */
    RELEASE_LEVEL_LABLE( "releaseLevelLabel", "releaseLevelLabel" ),

    /**
     * The reference.
     */
    REFERENCE( "reference", "reference" ),

    /**
     * The inputdeck.
     */
    INPUTDECK( "inputDecks", "inputDecks" ),

    /**
     * The phase.
     */
    PHASE( "phase", "phase.name" ),

    /**
     * The project.
     */
    PROJECT( "project", "project.name" );

    /**
     * Instantiates a new bmw cae bench enums.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    BmwCaeBenchVariantEnum( String key, String value ) {
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
        for ( BmwCaeBenchVariantEnum bmwCaeBenchEnums : values() ) {
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
        for ( BmwCaeBenchVariantEnum bmwCaeBenchEnums : values() ) {
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
    public static BmwCaeBenchVariantEnum[] getAllEnums() {
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