package de.soco.software.simuspace.suscore.common.enums;

/**
 * The Enum AlgoTypeEnum.
 *
 * @author noman arshad
 */
public enum AlgoTypeEnum {

    /**
     * The default.All Columns will be visible
     */
    DEFAULT( "Default", "Default" ),

    /**
     * The Min Max Step.
     */
    MMS( "MMS", "MMS" ),

    /**
     * The Min Max Level.
     */
    MML( "MML", "MML" ),

    /**
     * The values.
     */
    VAL( "VAL", "VAL" ),

    /**
     * The LEVEL
     */
    LEVELS( "LEVELS", "LEVELS" ),

    /**
     * The Start Stop Intervel.
     */
    SSI( "SSI", "SSI" ),

    /**
     * The Min Max.
     */
    MM( "MM", "MM" );

    /**
     * Instantiates a new bmw cae bench enums.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    AlgoTypeEnum( String key, String value ) {
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
        for ( AlgoTypeEnum algoTypeEnum : values() ) {
            if ( algoTypeEnum.getValue().contains( matrexValue ) ) {
                key = algoTypeEnum.getKey();
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
        for ( AlgoTypeEnum algoTypeEnum : values() ) {
            if ( algoTypeEnum.getKey().contains( matrixKey ) ) {
                value = algoTypeEnum.getValue();
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
