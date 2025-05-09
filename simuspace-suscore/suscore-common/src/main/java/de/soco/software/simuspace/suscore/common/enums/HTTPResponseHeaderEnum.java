package de.soco.software.simuspace.suscore.common.enums;

/**
 * @author Ahmar Nadeem
 *
 * The enum specific to the HTTP response header properties
 */
public enum HTTPResponseHeaderEnum {

    SERVER( "Server", "SIMuSPACE" ),
    ALLOW( "Allow", "POST, Options" ),
    ACCESS_CONTROL_ALLOW_HEADER( "Access-Control-Allow-Headers", "Origin,X-Requested-With,X-Auth-Token,Content-Type, Accept" ),
    ACCESS_CONTROL_ALLOW_METHOD( "Access-Control-Allow-Methods", "POST, Options" ),
    ACCESS_CONTROL_ALLOW_ORIGIN( "Access-Control-Allow-Origin", "*" );

    /**
     * Full constructor of the enum
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    HTTPResponseHeaderEnum( String key, String value ) {
        this.key = key;
        this.value = value;
    }

    /**
     * key of the constant
     */
    private final String key;

    /**
     * value against the constant key
     */
    private final String value;

    /**
     * @return key of the constant
     */
    public String getKey() {
        return key;
    }

    /**
     * @return value of the constant
     */
    public String getValue() {
        return value;
    }

}
