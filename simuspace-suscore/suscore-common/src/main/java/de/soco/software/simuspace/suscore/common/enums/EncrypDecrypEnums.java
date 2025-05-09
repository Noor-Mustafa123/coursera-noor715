package de.soco.software.simuspace.suscore.common.enums;

/**
 * The Enum EncrypDecrypEnums.
 *
 * @author noman arshad
 */
public enum EncrypDecrypEnums {

    /**
     * The aes.
     */
    AES( "AES", "AES" ),

    /**
     * The des.
     */
    DES( "DES", "DES/ECB/PKCS5Padding" ),

    /**
     * The DE sede.
     */
    DESede( "DESede", "DESede/CBC/PKCS5Padding" );

    /**
     * The key.
     */
    private final String key;

    /**
     * The value.
     */
    private final String value;

    /**
     * Instantiates a new encryp decryp enums.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    EncrypDecrypEnums( String key, String value ) {
        this.key = key;
        this.value = value;
    }

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
        for ( EncrypDecrypEnums encrypDecrypEnums : values() ) {
            if ( encrypDecrypEnums.getValue().contains( matrexValue ) ) {
                key = encrypDecrypEnums.getKey();
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
        for ( EncrypDecrypEnums encrypDecrypEnums : values() ) {
            if ( encrypDecrypEnums.getKey().contains( matrixKey ) ) {
                value = encrypDecrypEnums.getValue();
                break;
            }
        }
        return value;
    }

    /**
     * Gets the enum by key.
     *
     * @param matrixKey
     *         the matrix key
     *
     * @return the enum by key
     */
    public static EncrypDecrypEnums getEnumByKey( String matrixKey ) {
        for ( EncrypDecrypEnums encrypDecrypEnums : values() ) {
            if ( encrypDecrypEnums.getKey().contains( matrixKey ) ) {
                return encrypDecrypEnums;

            }
        }
        return null;
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
