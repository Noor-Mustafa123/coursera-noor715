/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.util;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;

/**
 * This Class is responsible to prepare String message for suscore
 */

public class MessagesUtil {

    /**
     * Gets the message from the {@link Messages}. and replace parameters in massage according to index.
     *
     * @param messageEnum
     *         the message enum
     * @param params
     *         the params
     *
     * @return the message
     *
     * @description method takes enum message and return String after replacing its params values e.g. {0} by desired String of array.
     */
    public static String getMessage( Messages messageEnum, Object... params ) {
        return MessageBundleFactory.getMessage( messageEnum.getKey(), params );
    }

    /**
     * Gets the message from the {@link Messages}. and replace parameters in massage according to index.
     *
     * @param messageEnum
     *         the message enum
     * @param params
     *         the params
     *
     * @return the message
     *
     * @description method takes enum message and return String after replacing its params values e.g. {0} by desired String of array.
     */
    public static String getMessage( WFEMessages messageEnum, Object... params ) {

        String message = messageEnum.getValue();

        if ( message.contains( "{0}" ) && ( params != null ) && ( params.length > 0 ) ) {
            for ( int paramNo = 0; paramNo < params.length; paramNo++ ) {
                message = message.replace( "{" + paramNo + "}", params[ paramNo ] + "" );
            }
        }
        return message;
    }

    /**
     * Private Constructor to avoid Object Instantiation
     */
    private MessagesUtil() {
    }

}
