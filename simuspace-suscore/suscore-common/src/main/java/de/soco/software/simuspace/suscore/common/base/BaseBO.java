/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.base;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Shan.Arshad, Ahmar.Nadeem
 *
 * the top level Business Object to be used in paginated response
 */
// @JsonFilter(ConstantsIJson.BO_FILTER)
public class BaseBO implements Serializable {

    private static final long serialVersionUID = -1111974083083620580L;

    /**
     * This function returns the reflection string of a bean
     *
     * @return String
     */
    public String showBean() {
        return ToStringBuilder.reflectionToString( this, ToStringStyle.SHORT_PREFIX_STYLE );
    }

}
