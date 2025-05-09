package de.soco.software.simuspace.suscore.local.daemon.controller;

import org.springframework.http.ResponseEntity;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;

public interface TranslationController {

    /**
     * Gets the transfer UI.
     *
     * @return the transfer UI
     */
    ResponseEntity< SusResponseDTO > getUserTokenMap( String authToken );

}
