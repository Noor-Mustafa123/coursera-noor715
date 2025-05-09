package de.soco.software.simuspace.suscore.common.enums;

/**
 * The Enum BmwCaeBenchEnums.
 *
 * @author noman arshad
 */
public enum BmwCaeBenchEnums {

    /**
     * The cb2 keyresults.
     */
    CB2_KEYRESULTS( "bmw-cb2-keyresults", "KeyResult" ),

    /**
     * The cb2 result.
     */
    CB2_RESULT( "bmw-cb2-result", "Result" ),

    /**
     * The cb2 submodel.
     */
    CB2_SUBMODEL( "bmw-cb2-submodel", "SubModel" ),

    /**
     * The cb2 input deck.
     */
    CB2_INPUT_DECK( "bmw-cb2-inputdeck", "InputDeck" ),

    /**
     * The cb2 variant.
     */
    CB2_VARIANT( "bmw-cb2-variant", "Variant" ),

    /**
     * The cb2 object tree.
     */
    CB2_OBJECT_TREE( "bmw-cb2-project-tree", "SubModel" ),

    /**
     * The cb2 dummy temp tree.
     */
    CB2_DUMMY_TEMP_TREE( "bmw-dummyfiles", "SubModel" ),

    /**
     * The cb2 story board.
     */
    CB2_STORY_BOARD( "bmw-cb2-storyboard", "KeyResultRequest" ),

    /**
     * The cb2 report.
     */
    CB2_REPORT( "bmw-cb2-report", "ComparisonReport" ),

    CB2_SCENARIO( "bmw-cb2-scenario-tree", "Senario" ),

    CB2_PROJECT( "bmw-cb2-project", "Project" );

    /**
     * Instantiates a new bmw cae bench enums.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    BmwCaeBenchEnums( String key, String value ) {
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
        for ( BmwCaeBenchEnums bmwCaeBenchEnums : values() ) {
            if ( bmwCaeBenchEnums.getValue().equalsIgnoreCase( matrexValue ) ) {
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
        for ( BmwCaeBenchEnums bmwCaeBenchEnums : values() ) {
            if ( bmwCaeBenchEnums.getKey().equalsIgnoreCase( matrixKey ) ) {
                value = bmwCaeBenchEnums.getValue();
                break;
            }
        }
        return value;
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
