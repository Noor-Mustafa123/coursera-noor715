package de.soco.software.simuspace.suscore.object.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.activator.Activator;
import de.soco.software.simuspace.suscore.data.model.RouterConfigItem;
import de.soco.software.simuspace.suscore.data.model.RouterConfigList;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.object.service.rest.SuSRouterService;

/**
 * the service class for getting routers configurations
 */
public class SuSRouterServiceImpl extends SuSBaseService implements SuSRouterService {

    @Override
    public Response getRouters() {
        try {
            Map< String, List< RouterConfigItem > > routers = Activator.getRouters();

            List< RouterConfigList > rcl = new ArrayList<>();
            for ( Entry< String, List< RouterConfigItem > > router : routers.entrySet() ) {
                RouterConfigList rc = new RouterConfigList();
                rc.setRoutes( router.getValue() );
                rc.setName( router.getKey() );

                rcl.add( rc );
            }
            return ResponseUtils.success( rcl );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

}
