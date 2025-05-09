package de.soco.software.simuspace.suscore.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class PasswordUtils.
 *
 * @author noman arshad
 */
public class PasswordUtils {

    /**
     * The passwords.
     */
    private static final Map< String, String > passwords = new HashMap<>();

    /**
     * {@inheritDoc}
     */

    public static void addToPasswordMap( String userid, String password ) {
        passwords.put( userid, password );
    }

    /**
     * {@inheritDoc}
     */

    public static void removeFromPasswordMap( String userId ) {
        passwords.remove( userId );
    }

    /**
     * {@inheritDoc}
     */

    public static String getPasswordById( String userId ) {
        return passwords.get( userId );
    }

}
