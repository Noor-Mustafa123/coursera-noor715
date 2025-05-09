package de.soco.software.simuspace.suscore.common.enums;

/**
 * The Enum Cb2OperationEnum.
 *
 * @author noman arshad
 * @since 2.0
 */
public enum Cb2OperationEnum {

    /**
     * The cb2 login.
     */
    CB2_LOGIN_OR_KEEP_ALIVE_SESSION( 1, "login and save OR keep alive session" ),

    /**
     * The cb2 logout.
     */
    CB2_LOGOUT( 2, "logout and clear session" ),

    /**
     * The cb2 tree.
     */
    CB2_TREE( 3, "tree management" ),

    /**
     * The cb2 file download.
     */
    CB2_FILE_DOWNLOAD( 4, "CB2 file download to specific path by oid" );

    /**
     * Instantiates a new permission matrix.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    Cb2OperationEnum( int key, String value ) {
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
        for ( Cb2OperationEnum cb2OperationEnum : values() ) {
            if ( cb2OperationEnum.getValue().contains( matrexValue ) ) {
                key = cb2OperationEnum.getKey();
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
        for ( Cb2OperationEnum cb2OperationEnum : values() ) {
            if ( cb2OperationEnum.getKey() == matrixKey ) {
                value = cb2OperationEnum.getValue();
                break;
            }
        }
        return value;
    }

}
