/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.interceptors;

import java.util.List;

import org.apache.cxf.Bus;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.common.gzip.GZIPOutInterceptor;

/**
 * The Class InterceptorManager is used to register busses provided in the blue prints.
 *
 * @author Ali Haider
 */
public class SusInterceptorManager extends AbstractFeature {

    /**
     * The Constant COMMON_BUS_NAME.
     */
    private static final String COMMON_BUS_NAME = "suscore_bus";

    /**
     * The Constant IN.
     */
    private static final Interceptor< Message > IN = new SusAuthenticationInterceptor();

    private static final Interceptor< Message > CORS = new EnableCORSInterceptor();

    private static final Interceptor< Message > FAULT = new CustomOutFaultInterceptor();

    private static final Interceptor< Message > GZIP = new GZIPOutInterceptor();

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.cxf.feature.AbstractFeature#initializeProvider(org.apache.
     * cxf.interceptor.InterceptorProvider, org.apache.cxf.Bus)
     */
    @Override
    protected void initializeProvider( InterceptorProvider provider, Bus bus ) {

        if ( !bus.getOutInterceptors().contains( CORS ) ) {
            provider.getOutInterceptors().add( CORS );
        }

        if ( !bus.getOutInterceptors().contains( GZIP ) ) {
            provider.getOutInterceptors().add( GZIP );
        }

        if ( !bus.getOutFaultInterceptors().contains( FAULT ) ) {
            provider.getOutFaultInterceptors().add( FAULT );
        }

        final List< Interceptor< ? extends Message > > inters = bus.getInInterceptors();
        for ( final Interceptor< ? extends Message > a : inters ) {
            if ( IN.getClass().isInstance( a ) ) {
                bus.getInInterceptors().remove( a );
            }
        }
        if ( COMMON_BUS_NAME.equals( bus.getId() ) ) {
            bus.getInInterceptors().add( IN );
        }
    }

}
