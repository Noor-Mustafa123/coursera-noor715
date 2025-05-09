package de.soco.software.simuspace.suscore.common.base;

/* ******************************************************************************
 *  Copyright (C) 2013 - now()
 *  SOCO engineers GmbH
 *  All rights reserved.
 *
 * ******************************************************************************/

import java.io.Serializable;

/**
 * The Class related to data transfer object to be used by JsonUtils and ResponseDTO for building paginated response.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
// @JsonFilter(ConstantsIJson.DTO_FILTER)
public class DataTransferObject implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -2796608726830303927L;

    /**
     * Instantiates a new data transfer object.
     */
    public DataTransferObject() {

    }

}
