package de.soco.software.simuspace.suscore.common.enums;

/**
 * The Enum SystemGenratedViewEnum.
 *
 * @author noman arshad
 */

public enum SystemGenratedViewEnum {

    /**
     * The restricted datauser.
     */
    RESTRICTED_DATAUSER( 0, "Restricted+DataUser" ),

    /**
     * The datauser.
     */
    DATAUSER( 1, "DataUser" ),

    /**
     * The datauser wfuser wfmanager.
     */
    DATAUSER_WFUSER_WFMANAGER( 2, "DataUser+WfUser+WfManager" ),

    /**
     * The datauser wfuser.
     */
    DATAUSER_WFUSER( 3, "DataUser+WfUser" ),

    /**
     * The restricted datauser wfuser.
     */
    RESTRICTED_DATAUSER_WFUSER( 4, "Restricted+DataUser+WfUser" ),

    ;

    /**
     * Instantiates a new system genrated view enum.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    SystemGenratedViewEnum( int key, String value ) {
        this.key = key;
        this.value = value;
    }

    /**
     * key of the constant.
     */
    private final int key;

    /**
     * value against the constant key.
     */
    private final String value;

    /**
     * Gets the key.
     *
     * @return the key
     */
    public int getKey() {
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

    /**
     * Gets the key by value.
     *
     * @param matrexValue
     *         the matrex value
     *
     * @return the key by value
     */
    public static int getKeyByValue( String matrexValue ) {
        int key = -1;
        for ( SystemGenratedViewEnum systemGenratedViewEnum : values() ) {
            if ( systemGenratedViewEnum.getValue().contains( matrexValue ) ) {
                key = systemGenratedViewEnum.getKey();
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
    public static String getValueByKey( int matrixKey ) {
        String value = "";
        for ( SystemGenratedViewEnum systemGenratedViewEnum : values() ) {
            if ( systemGenratedViewEnum.getKey() == matrixKey ) {
                value = systemGenratedViewEnum.getValue();
                break;
            }
        }
        return value;
    }

}
