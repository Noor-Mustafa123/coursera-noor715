package de.soco.software.simuspace.suscore.local.daemon.controller;

import org.springframework.http.ResponseEntity;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;

public interface SchemeController {

    /**
     * Run scheme.
     *
     * @param authToken
     *         the auth token
     * @param jobParametersString
     *         the job parameters string
     *
     * @return the response entity
     */
    public abstract ResponseEntity< SusResponseDTO > runScheme( String authToken, String jobParametersString );

}
