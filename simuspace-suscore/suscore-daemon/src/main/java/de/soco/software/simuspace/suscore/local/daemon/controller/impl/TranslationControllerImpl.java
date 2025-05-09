package de.soco.software.simuspace.suscore.local.daemon.controller.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.SuSCoreSessionDTO;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.local.daemon.controller.TranslationController;
import de.soco.software.simuspace.suscore.local.daemon.manager.SuscoreDaemonManager;

@RestController
@RequestMapping( value = "api/translation" )
public class TranslationControllerImpl implements TranslationController {

    /**
     * The daemon manager.
     */
    @Autowired
    private SuscoreDaemonManager daemonManager;

    @Override
    @RequestMapping( value = "tokenizeMap", method = RequestMethod.GET )
    public ResponseEntity< SusResponseDTO > getUserTokenMap( @RequestHeader( value = "X-Auth-Token" ) String authToken ) {
        try {
            final String tokenMapURL = daemonManager.getServerAPIBase() + "system/user/tokenizeMap";
            SusResponseDTO request = SuSClient.getRequest( tokenMapURL, CommonUtils.prepareHeadersWithAuthToken( authToken ) );
            Map< String, SuSCoreSessionDTO > sessionTokenMap = new HashMap<>();
            sessionTokenMap = ( Map< String, SuSCoreSessionDTO > ) JsonUtils.jsonToMap( JsonUtils.toJson( request.getData() ),
                    sessionTokenMap );
            for ( Map.Entry< String, SuSCoreSessionDTO > sessionTokenEntry : sessionTokenMap.entrySet() ) {
                TokenizedLicenseUtil.addSession( sessionTokenEntry.getKey(),
                        JsonUtils.linkedMapObjectToClassObject( sessionTokenEntry.getValue(), SuSCoreSessionDTO.class ) );
            }
            return new ResponseEntity<>( ResponseUtils.successResponse( "User Token Map updated" ), HttpStatus.OK );
        } catch ( SusException | IOException e ) {
            ExceptionLogger.logException( e, getClass() );
            return new ResponseEntity<>( ResponseUtils.failureResponse( e.getMessage(), null ), HttpStatus.OK );
        }
    }

    /**
     * Gets the daemon manager.
     *
     * @return the daemon manager
     */
    public SuscoreDaemonManager getDaemonManager() {
        return daemonManager;
    }

    /**
     * Sets the daemon manager.
     *
     * @param daemonManager
     *         the new daemon manager
     */
    public void setDaemonManager( SuscoreDaemonManager daemonManager ) {
        this.daemonManager = daemonManager;
    }

}
