package de.soco.software.simuspace.server.service.rest.impl;

import javax.ws.rs.core.Response;

import lombok.Getter;
import lombok.Setter;

import de.soco.software.simuspace.server.manager.PropertiesManager;
import de.soco.software.simuspace.server.service.rest.PropertiesService;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;

/**
 * The type Properties service.
 */
@Getter
@Setter
public class PropertiesServiceImpl extends SuSBaseService implements PropertiesService {

    /**
     * The Properties manager.
     */
    PropertiesManager propertiesManager;

    /**
     * @inheritDoc
     */
    @Override
    public Response getAllProperties() {
        try {
            return ResponseUtils.success( propertiesManager.getAllProperties() );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }
}
