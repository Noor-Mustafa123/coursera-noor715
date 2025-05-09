package de.soco.software.simuspace.suscore.authentication.service.rest.impl;

import com.fasterxml.jackson.databind.JsonNode;

import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;

public class AuthMutualServiceImpl extends SuSBaseService {

    protected JsonNode getOAuth( JsonNode jsonNode ) {
        if ( null == jsonNode ) {
            throw new SusException( "OAuth not configured " );
        }
        return jsonNode;
    }

}
