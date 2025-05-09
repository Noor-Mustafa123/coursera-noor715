package de.soco.software.simuspace.suscore.core.location.model;

import org.apache.cxf.message.Message;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.core.location.manager.LocationCoreManager;
import de.soco.software.simuspace.workflow.location.JobParametersLocationModel;
import de.soco.software.simuspace.workflow.model.impl.WorkflowLocationThread;

/**
 * The class is responsible to provide runnable for run workflow and will do all executions on the behalf of the user logged in;
 *
 * @author M.Nasir.Farooq
 */
@Log4j2
public class RunWorkflowLocationThread extends WorkflowLocationThread {

    /**
     * The workflowManager.
     */
    private final LocationCoreManager locationManager;

    private final Message message;

    /**
     * Instantiates a new run workflow thread.
     *
     * @param uid
     *         the user id
     * @param jobParameters
     *         the jobParameters
     * @param workflowManager
     *         the workflowManager
     */
    public RunWorkflowLocationThread( JobParametersLocationModel jobParametersLocationModel, LocationCoreManager locationManager,
            Message message ) {
        super();
        this.jobParametersLocationModel = jobParametersLocationModel;
        this.locationManager = locationManager;
        this.message = message;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            locationManager.executeWFProcessOnServer( jobParametersLocationModel, message );
        } catch ( JsonGenerationException | JsonMappingException e ) {
            log.error( "Json Exception", e );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

}
