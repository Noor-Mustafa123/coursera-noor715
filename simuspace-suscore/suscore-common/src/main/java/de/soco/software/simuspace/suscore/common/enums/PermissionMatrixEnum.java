package de.soco.software.simuspace.suscore.common.enums;

import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * Utility class which will contains all the permissions with MASK which represents the permissions like VIEW, VIEW_AUDIT_LOG, READ, WRITE,
 * EXECUTE. This is represented by 32 bits. Each of these bits represents a permission, and by default the permissions are VIEW (bit 0),
 * VIEW_AUDIT_LOG(bit 1), READ(bit 2), WRITE(bit 3) and EXECUTE (bit 4) etc.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public enum PermissionMatrixEnum {

    /**
     * View permission with mask 2 raise power 1
     */
    NONE( 0, "4100084x4" ),

    /**
     * View permission with mask 2 raise power 1
     */
    VIEW( 2, "4100085x4" ),

    /**
     * Read permission with mask 2 raise power 2
     */
    READ( 4, "4100086x4" ),

    /**
     * Write permission with mask 2 raise power 3
     */
    WRITE( 8, "4100087x4" ),

    /**
     * Delete permission with mask 2 raise power 4
     */
    DELETE( 16, "4100088x4" ),

    /**
     * Restore permission with mask 2 raise power 5
     */
    RESTORE( 32, "4100089x4" ),

    /**
     * Restore permission with mask 2 raise power 6
     */
    MANAGE( 64, "4100090x4" ),

    /**
     * Create new object permission with mask 2 raise power 7
     */
    CREATE_NEW_OBJECT( 128, "4100091x4" ),

    /**
     * Execute permission with mask 2 raise power 8
     */
    EXECUTE( 256, "4100092x4" ),

    /**
     * Kill permission with mask 2 raise power 9
     */
    KILL( 512, "4100093x4" ),

    /**
     * Share permission with mask 2 raise power 10
     */
    SHARE( 1024, "4100094x4" );

    /**
     * Instantiates a new permission matrix.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    PermissionMatrixEnum( int key, String value ) {
        this.key = key;
        this.value = value;
    }

    /**
     * key of the constant
     */
    private final int key;

    /**
     * value against the constant key
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
        return MessageBundleFactory.getMessage( value );
    }

    /**
     * Gets value.
     *
     * @param token
     *         the token
     *
     * @return the value
     */
    public String getValue( boolean hasTranslation, String token ) {
        return MessageBundleFactory.getExternalMessage( hasTranslation, token, value );
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
        for ( PermissionMatrixEnum permissionMatrix : values() ) {
            if ( permissionMatrix.getValue().contains( matrexValue ) ) {
                key = permissionMatrix.getKey();
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
        for ( PermissionMatrixEnum permissionMatrix : values() ) {
            if ( permissionMatrix.getKey() == matrixKey ) {
                value = permissionMatrix.getValue();
                break;
            }
        }
        return value;
    }
}