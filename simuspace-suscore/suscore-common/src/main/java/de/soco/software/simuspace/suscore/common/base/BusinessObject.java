package de.soco.software.simuspace.suscore.common.base;

/* ******************************************************************************
 *  Copyright (C) 2013 - now()
 *  SOCO engineers GmbH
 *  All rights reserved.
 *
 * ******************************************************************************/

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * The Class related to business object to be used by JsonUtils for paginated response.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
// @JsonFilter(ConstantsIJson.BO_FILTER)
public class BusinessObject implements Serializable {

    private static final long serialVersionUID = -1111974083083620580L;

    public String showBean() {
        return ToStringBuilder.reflectionToString( this, ToStringStyle.SHORT_PREFIX_STYLE );
    }

}
