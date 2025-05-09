/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.Headers;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMessageHeaders;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;

/**
 * The Class is used to get user information from {@link Message}.
 *
 * @author Aroosa.Bukhari
 */
public class BundleUtils {

    /**
     * The constant for the parameter for the user ID key in {@link Message} that will hold user's id.
     */
    private static final String PARAMETER_USER_ID = "userId";

    /**
     * The constant for the parameter for the user name
     */
    private static final String PARAMETER_USER_NAME = "userName";

    /**
     * The constant for the headers for authorization token
     */
    private static final String X_AUTH_TOKEN = "X-Auth-Token";

    /**
     * Instantiates a new bundle utils.
     */
    private BundleUtils() {
        super();
    }

    /**
     * Gets the authorization token from message.
     *
     * @param message
     *         the message
     *
     * @return the token from message or <code>""</code> if the message is <code>null<code> or does not contains token.
     */
    public static String getTokenFromMessage( Message message ) {

        String token = ConstantsString.EMPTY_STRING;
        if ( null != message ) {
            final Map< String, List< String > > headers = Headers.getSetProtocolHeaders( message );
            if ( ( null != headers ) && !headers.isEmpty() && headers.containsKey( X_AUTH_TOKEN )
                    && CollectionUtil.isNotEmpty( headers.get( X_AUTH_TOKEN ) ) ) {
                final List< String > tokenlist = headers.get( X_AUTH_TOKEN );

                token = tokenlist.get( ConstantsInteger.INTEGER_VALUE_ZERO );
            }

        }
        return token;
    }

    /**
     * Gets the user id from {@link Message}.
     *
     * @param message
     *         the message
     *
     * @return the user id from message or <code>""</code> if the message is <code>null<code> or does not contains user id.
     */
    public static String getUserIdFromMessage( Message message ) {
        String userId = "-1";
        if ( ( null != message ) && ( null != message.get( PARAMETER_USER_ID ) ) ) {
            userId = ( String ) message.get( PARAMETER_USER_ID );

        }
        return userId;
    }

    /**
     * Gets the user Name from {@link Message}.
     *
     * @param message
     *         the message
     *
     * @return the user id from message or <code>""</code> if the message is <code>null<code> or does not contains u.
     */
    public static String getUserNameFromMessageBundle( Message message ) {

        if ( ( null != message ) && ( null != message.get( PARAMETER_USER_NAME ) ) ) {

            return message.get( PARAMETER_USER_NAME ).toString();

        }
        return "";

    }

    public static String getUserTokenFromMessageBundle( Message message ) {

        if ( ( null != message ) && ( null != message.get( "token" ) ) ) {

            return message.get( "token" ).toString();

        }
        return "";

    }

    /**
     * Gets the browser agent.
     *
     * @param message
     *         the message
     *
     * @return the browser agent
     */
    public static String getBrowserAgent( Message message ) {
        Map< String, List< String > > headers = Headers.getSetProtocolHeaders( message );

        List< String > userAgentList = headers.get( ConstantsMessageHeaders.USER_AGENT );

        if ( CollectionUtils.isNotEmpty( userAgentList ) ) {
            return userAgentList.get( ConstantsInteger.INTEGER_VALUE_ZERO );
        }
        return ConstantsString.EMPTY_STRING;

    }

}
