/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.interceptors.utils;

import java.util.Date;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;

/**
 * The Class TokenUtils building the token for authentication.
 *
 * @since 1.0
 */
public class TokenUtils {

    /**
     * Generate token.
     *
     * @param userId
     *         the userId
     * @param createdOn
     *         the createdOn
     * @param ipAddress
     *         the ipAddress
     * @param key
     *         the key
     *
     * @return the string
     */
    public static String generateToken( final String userId, final Date createdOn, final String ipAddress, final String key ) {

        return CipherUtils.encryptSHA1( buildTokenKey( userId, createdOn.getTime(), ipAddress, key ) );

    }

    /**
     * Builds the token key.
     *
     * @param userId
     *         the the userId
     * @param browserAgent
     *         the browserAgent
     * @param createdOn
     *         the createdOn
     * @param ipAddress
     *         the ipAddress
     * @param key
     *         the key
     *
     * @return the string
     */
    private static String buildTokenKey( final String userId, final Long createdOn, final String ipAddress, final String key ) {
        StringBuilder tokenBuilder = new StringBuilder();
        tokenBuilder.append( userId );
        tokenBuilder.append( ConstantsString.COLON );
        tokenBuilder.append( createdOn );
        tokenBuilder.append( ConstantsString.COLON );
        tokenBuilder.append( ipAddress );
        tokenBuilder.append( ":" );
        tokenBuilder.append( key );
        return tokenBuilder.toString();
    }

}
