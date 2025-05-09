package de.soco.software.simuspace.server.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.executor.util.SusExecutorUtil;

/**
 * The Bundle Activator.
 */
@Log4j2
public class Activator implements BundleActivator {

    @Override
    public void start( BundleContext context ) throws Exception {
        log.info( "Starting Simflow Server Bundle" );
        SusExecutorUtil.resubmitJobsQueue();
    }

    @Override
    public void stop( BundleContext context ) throws Exception {
        log.info( "Stopping Simflow Server Bundle" );
        SusExecutorUtil.saveJobsQueueToFile();
    }

}
