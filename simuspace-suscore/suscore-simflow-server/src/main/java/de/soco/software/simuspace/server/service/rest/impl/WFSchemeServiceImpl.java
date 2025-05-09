package de.soco.software.simuspace.server.service.rest.impl;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.cxf.message.Message;
import org.json.JSONObject;

import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.server.manager.WFSchemeManager;
import de.soco.software.simuspace.server.manager.WorkflowManager;
import de.soco.software.simuspace.server.service.rest.WFSchemeService;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.JobSubmitResponseDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.ScanFileDTO;
import de.soco.software.simuspace.suscore.common.properties.DesignPlotingConfig;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ObjectiveVariableDTO;
import de.soco.software.simuspace.suscore.data.model.WFSchemeDTO;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.data.utility.ContextUtil;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.dto.WorkflowDefinitionDTO;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.JobParametersImpl;
import de.soco.software.simuspace.workflow.util.WorkflowDefinitionUtil;

/**
 * The Class WFSchemeServiceImpl.
 */
public class WFSchemeServiceImpl extends SuSBaseService implements WFSchemeService {

    /**
     * The wf scheme manager.
     */
    private WFSchemeManager wfSchemeManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The job manager reference.
     */
    private JobManager jobManager;

    private WorkflowManager workflowManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWFSchemeUI() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.WF_SCHEME.getKey() ) ),
                    new TableUI( wfSchemeManager.getWFSchemeUI( getUserIdFromGeneralHeader() ),
                            objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.SCHEME_TABLE_KEY,
                                    getUserIdFromGeneralHeader(), null ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWFSchemeData( String json ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.WF_SCHEME.getKey() ) ),
                    wfSchemeManager.getWFSchemeData( getUserIdFromGeneralHeader(), JsonUtils.jsonToObject( json, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWFSchemeContextMenu( String json ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( json, FiltersDTO.class );
            return ResponseUtils.success( wfSchemeManager.getWFSchemeContextMenu( getUserIdFromGeneralHeader(), filter ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createWFSchemeUI() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.CREATE.getKey() ) + ConstantsString.SPACE + MessageBundleFactory.getMessage(
                            Messages.WF_SCHEME.getKey() ) ), wfSchemeManager.createWFSchemeUI( getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createWFScheme( String json ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.WF_SCHEME_CREATED.getKey() ),
                    wfSchemeManager.createWFScheme( getUserIdFromGeneralHeader(), JsonUtils.jsonToObject( json, WFSchemeDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editWFSchemeUI( String workflowschemeId ) {
        try {
            return ResponseUtils.success( wfSchemeManager.editWFSchemeUI( getUserIdFromGeneralHeader(), workflowschemeId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateWFScheme( String workflowschemeId, String json ) {
        try {
            return ResponseUtils.success( wfSchemeManager.updateWFScheme( getUserIdFromGeneralHeader(), workflowschemeId,
                    JsonUtils.jsonToObject( json, WFSchemeDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteWFScheme( String workflowschemeId, String mode ) {
        try {
            wfSchemeManager.deleteWFScheme( getUserIdFromGeneralHeader(), workflowschemeId, mode );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.OBJECT_AND_DEPENDENCIES_DELETED_SUCCESSFULLY.getKey() ),
                    true );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listSchemeTabsUI( String workflowId ) {
        try {
            return ResponseUtils.success( wfSchemeManager.listSchemeTabsUI( getUserIdFromGeneralHeader(), workflowId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getCategoryOptionForm( String workflowId ) {
        try {
            return ResponseUtils.success( wfSchemeManager.getCategoryOptionForm( getUserIdFromGeneralHeader(), workflowId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveOrUpdateSchemeOptionForm( String workflowId, String json ) {
        try {
            return ResponseUtils.success(
                    wfSchemeManager.saveOrUpdateSchemeOptionForm( getUserIdFromGeneralHeader(), UUID.fromString( workflowId ), json ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSchemeOptionForm( String workflowId, int categoryId ) {
        try {
            return ResponseUtils.success( wfSchemeManager.getSchemeOptionForm( getUserIdFromGeneralHeader(), workflowId, categoryId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSchemeOptionFormUI( String workflowId, String schemeId ) {
        try {
            return ResponseUtils.success( wfSchemeManager.getSchemeOptionFormUI( getUserIdFromGeneralHeader(), workflowId, schemeId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response scanObjectiveFile( String json ) {
        try {
            return ResponseUtils.success(
                    wfSchemeManager.scanObjectiveFile( getUserIdFromGeneralHeader(), JsonUtils.jsonToObject( json, List.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response scanObjectiveFileOnServerSide( String json ) {
        try {
            return ResponseUtils.success( wfSchemeManager.scanObjectiveFileFromPath( getUserIdFromGeneralHeader(),
                    JsonUtils.jsonToObject( json, ScanFileDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveAskOnRunVariables( String workflowId, String json ) {
        try {
            wfSchemeManager.saveAskOnRunVariables( getUserIdFromGeneralHeader(), workflowId, json );
            return ResponseUtils.success( "success message" );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response scanObjectiveFileFromPath( String json ) {
        try {
            return ResponseUtils.success( wfSchemeManager.scanObjectiveFileFromPath( getUserIdFromGeneralHeader(),
                    JsonUtils.jsonToObject( json, ScanFileDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDesignVariablesUI( String workflowId ) {
        try {
            return ResponseUtils.success( new TableUI( wfSchemeManager.getDesignvariablesUI( workflowId, getUserIdFromGeneralHeader() ),
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.DESIGN_VARIABLE_TABLE_KEY,
                            getUserIdFromGeneralHeader(), null ) ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDesignSummaryUI( String workflowId ) {
        try {
            return ResponseUtils.success( new TableUI( wfSchemeManager.getDesignSummaryUI( workflowId, getUserIdFromGeneralHeader() ) ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDesignVariableData( String wortkflowId, String json ) {
        try {

            return ResponseUtils.success( wfSchemeManager.getDesignVariableData( getUserIdFromGeneralHeader(), wortkflowId,
                    JsonUtils.jsonToObject( json, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the design variable data as label.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the design variable data as labe
     */
    @Override
    public Response getDesignVariableDataAsLabel( UUID workflowId ) {
        try {
            return ResponseUtils.success( wfSchemeManager.getDesignVariableDataAsLabel( getUserIdFromGeneralHeader(), workflowId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Update design variables relation with expression.
     *
     * @param workflowId
     *         the workflow id
     * @param jsonMap
     *         the json map
     *
     * @return the response
     */
    @Override
    public Response updateDesignVariablesRelationWithExpression( UUID workflowId, String jsonMap ) {
        try {
            Map< String, String > map = new HashMap<>();
            map = ( Map< String, String > ) JsonUtils.jsonToMap( jsonMap, map );
            return ResponseUtils.success(
                    wfSchemeManager.updateDesignVariablesRelationWithExpression( getUserIdFromGeneralHeader(), workflowId,
                            map.get( "expression" ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the design variable all data.
     *
     * @param wortkflowId
     *         the wortkflow id
     * @param json
     *         the json
     *
     * @return the design variable all data
     */
    @Override
    public Response getDesignVariableAllData( String wortkflowId, String json ) {
        try {
            return ResponseUtils.success( wfSchemeManager.getDesignVariableAllData( getUserIdFromGeneralHeader(), wortkflowId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDesignSummaryData( String workflowId, String json ) {
        try {
            return ResponseUtils.success( wfSchemeManager.getDesignSummaryData( getUserIdFromGeneralHeader(), workflowId,
                    JsonUtils.jsonToObject( json, FiltersDTO.class ), false ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the generate design summary data.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the generate design summary data
     */
    @Override
    public Response getGenerateDesignSummaryData( String workflowId ) {
        try {
            return ResponseUtils.success( wfSchemeManager.getGenerateDesignSummaryData( getUserIdFromGeneralHeader(), workflowId, true ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Download design summary data.
     *
     * @param workflowId
     *         the workflow id
     * @param token
     *         the token
     *
     * @return the response
     */
    @Override
    public Response downloadDesignSummaryDataFileCSVs( String workflowId ) {
        try {
            String fileAddress = wfSchemeManager.downloadDesignSummaryDataFileCSV(
                    CommonUtils.getCurrentUser( getTokenFromGeneralHeader() ).getId(), workflowId );
            File file = new File( fileAddress );
            ResponseBuilder response = Response.ok( file );
            response.header( "Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" );
            response.header( Message.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM );
            response.header( "File-Size", file.length() );
            return response.build();

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDesignSummaryOriginal( String workflowId, String json ) {
        try {
            return ResponseUtils.success( wfSchemeManager.getDesignSummaryData( getUserIdFromGeneralHeader(), workflowId,
                    JsonUtils.jsonToObject( json, FiltersDTO.class ), true ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDesignVariableContextMenu( String json ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( json, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext( wfSchemeManager.getDesignVariableContextMenu( filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectiveVariableContextRouter( String json ) {

        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( json, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext( wfSchemeManager.getObjectiveVariableContextMenu( filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editDesignVariableUI( String designVariableId ) {
        try {
            return ResponseUtils.success( wfSchemeManager.editDesignVariableUI( getUserIdFromGeneralHeader(), designVariableId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateDesignvariable( String designVariableId, String json ) {
        try {
            return ResponseUtils.success( wfSchemeManager.updateDesignVariable( getUserIdFromGeneralHeader(), designVariableId, json ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response addDesignSummaryUI( String workflowId ) {
        try {
            return ResponseUtils.success( wfSchemeManager.addDesignSummaryUI( getUserIdFromGeneralHeader(), workflowId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response addDesignSummary( String workflowId, String json ) {
        try {
            Map< String, Object > map = new HashMap<>();
            return ResponseUtils.success( wfSchemeManager.addDesignSummary( getUserIdFromGeneralHeader(), workflowId,
                    ( Map< String, Object > ) JsonUtils.jsonToMap( json, map ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editDesignSummaryUI( String workflowId, String id ) {
        try {
            return ResponseUtils.success( wfSchemeManager.editDesignSummaryUI( getUserIdFromGeneralHeader(), workflowId, id ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateDesignSummary( String workflowId, String id, String json ) {
        try {
            Map< String, Object > map = new HashMap<>();
            return ResponseUtils.success( wfSchemeManager.updateDesignSummary( getUserIdFromGeneralHeader(), workflowId, id,
                    ( Map< String, Object > ) JsonUtils.jsonToMap( json, map ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteDesignSummary( String workflowId, String json ) {
        try {
            return ResponseUtils.success( wfSchemeManager.deleteDesignSummary( getUserIdFromGeneralHeader(), workflowId,
                    JsonUtils.jsonToObject( json, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateObjectiveVariable( String objectiveVariableId, String json ) {
        try {
            ObjectiveVariableDTO objectiveVariableDTO = JsonUtils.jsonToObject( json, ObjectiveVariableDTO.class );
            objectiveVariableDTO.setId( UUID.fromString( objectiveVariableId ) );
            return ResponseUtils.success( wfSchemeManager.updateObjectiveVariable( getUserIdFromGeneralHeader(), objectiveVariableDTO ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectiveVariablesUI( String workflowId ) {
        try {
            return ResponseUtils.success( new TableUI( wfSchemeManager.getObjectiveVariablesUI(),
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.OBJECTIVE_VARIABLE_TABLE_KEY,
                            getUserIdFromGeneralHeader(), null ) ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectiveVariableData( String workflowId, String json ) {
        try {
            return ResponseUtils.success( wfSchemeManager.getObjectiveVariableData( getUserIdFromGeneralHeader(), workflowId,
                    JsonUtils.jsonToObject( json, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editObjectiveVariableUI( String objectiveVariableId ) {
        try {
            return ResponseUtils.success( wfSchemeManager.editObjectiveVariableUI( getUserIdFromGeneralHeader(), objectiveVariableId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response runWorkflowScheme( String workflowId, String objectFilterJson ) {
        try {
            wfSchemeManager.validateUserSchemeLicense( getUserIdFromGeneralHeader(), workflowId );
            JobParameters jobParameters = JsonUtils.jsonToObject( objectFilterJson, JobParametersImpl.class );
            jobParameters.setJobRunByUserUID( getUserNameFromGeneralHeader() );
            // Getting workflow elements from job parameter
            final WorkflowDefinitionDTO jobParamDef = WorkflowDefinitionUtil.getWorkflowDefinitionDTOFromMap(
                    jobParameters.getWorkflow().prepareDefinition() );
            final List< UserWFElement > jobWFElements = WorkflowDefinitionUtil.prepareWorkflowElements( jobParamDef );
            Set< String > filePaths = workflowManager.getFilePathsFromWfJobParameters( getUserNameFromGeneralHeader(), jobParameters,
                    jobParamDef, jobWFElements );
            if ( !filePaths.isEmpty() ) {
                for ( String path : filePaths ) {
                    File file = new File( path );
                    if ( !file.exists() ) {
                        return ResponseUtils.failure( "WF Files Do not Exist on server: " + file.getAbsolutePath() );
                    }
                }
            }
            jobParameters = wfSchemeManager.runWfScheme( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(),
                    getTokenFromGeneralHeader(), workflowId, objectFilterJson );
            return ResponseUtils.success( "scheme submitted", new JobSubmitResponseDTO( jobParameters.getId(), jobParameters.getName() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowSchemeViews() {
        try {
            return ResponseUtils.success(
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.SCHEME_TABLE_KEY, getUserIdFromGeneralHeader(),
                            null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveWorkflowSchemeView( String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.SCHEME_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setWorkflowSchemeViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.SCHEME_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteWorkflowSchemeView( String viewId ) {
        try {
            if ( objectViewManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateWorkflowSchemeView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.SCHEME_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectiveVariablesViews() {
        try {
            return ResponseUtils.success( objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.OBJECTIVE_VARIABLE_TABLE_KEY,
                    getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveObjectiveVariablesView( String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.OBJECTIVE_VARIABLE_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setObjectiveVariablesViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.OBJECTIVE_VARIABLE_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteObjectiveVariablesView( String viewId ) {
        try {
            if ( objectViewManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateObjectiveVariablesView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.OBJECTIVE_VARIABLE_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDesignVariablesViews() {
        try {
            return ResponseUtils.success( objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.DESIGN_VARIABLE_TABLE_KEY,
                    getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDesignSummaryViews() {
        try {
            return ResponseUtils.success(
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.Design_Summary_KEY, getUserIdFromGeneralHeader(),
                            null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveDesignVariablesView( String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.DESIGN_VARIABLE_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setDesignVariablesViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.DESIGN_VARIABLE_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteDesignVariablesView( String viewId ) {
        try {
            if ( objectViewManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateDesignVariablesView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.DESIGN_VARIABLE_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response validateWFRunSchemeByWorkflowId( String workflowId ) {
        try {
            wfSchemeManager.validateWFRunSchemeByWorkflowId( getUserIdFromGeneralHeader(), workflowId );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.SCHEME_VALIDATED_SUCCESSFULLY.getKey() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateJobAndItsChilds( UUID jobId, String jobJson ) {
        try {
            final Job parentJob = JsonUtils.jsonToObject( jobJson, JobImpl.class );
            parentJob.setId( jobId );
            for ( Job job : jobManager.getAllChildrensOfMasterJob( parentJob ) ) {
                if ( job.getStatus().getName().equals( WorkflowStatus.COMPLETED.getValue() ) ) {
                    job.setStatus( new Status( WorkflowStatus.ABORTED ) );
                    jobManager.updateJob( job );
                }
            }
            parentJob.setStatus( new Status( WorkflowStatus.ABORTED ) );
            jobManager.updateJob( parentJob );
            return ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.JOB_UPDATED ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getGenerateImageOptions() {
        try {
            return ResponseUtils.success( DesignPlotingConfig.getGenerateImageOptions() );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response checkSyntax( String workflowId, String json ) {
        try {
            JSONObject jObj = new JSONObject( json );
            String excepression = ( String ) jObj.get( "expression" );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.SYNTAX_IS_CORRECT.getKey() ),
                    wfSchemeManager.checkSyntax( getUserIdFromGeneralHeader(), workflowId, excepression ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the wf scheme manager.
     *
     * @return the wf scheme manager
     */
    public WFSchemeManager getWfSchemeManager() {
        return wfSchemeManager;
    }

    /**
     * Sets the wf scheme manager.
     *
     * @param wfSchemeManager
     *         the new wf scheme manager
     */
    public void setWfSchemeManager( WFSchemeManager wfSchemeManager ) {
        this.wfSchemeManager = wfSchemeManager;
    }

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    public ObjectViewManager getObjectViewManager() {
        return objectViewManager;
    }

    /**
     * Sets the object view manager.
     *
     * @param objectViewManager
     *         the new object view manager
     */
    public void setObjectViewManager( ObjectViewManager objectViewManager ) {
        this.objectViewManager = objectViewManager;
    }

    /**
     * Gets the job manager.
     *
     * @return the job manager
     */
    public JobManager getJobManager() {
        return jobManager;
    }

    /**
     * Sets the job manager.
     *
     * @param jobManager
     *         the new job manager
     */
    public void setJobManager( JobManager jobManager ) {
        this.jobManager = jobManager;
    }

    public WorkflowManager getWorkflowManager() {
        return workflowManager;
    }

    public void setWorkflowManager( WorkflowManager workflowManager ) {
        this.workflowManager = workflowManager;
    }

    /**
     * Import design summary data from csv UI.
     *
     * @param wfId
     *         the wf id
     *
     * @return the response
     */
    @Override
    public Response importDesignSummaryDataFromCsvUI( String wfId ) {
        try {
            return ResponseUtils.success( wfSchemeManager.getDesignSummaryImportUI( wfId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Import design summary data from CSV file.
     *
     * @param workflowId
     *         the workflow id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @Override
    public Response importDesignSummaryDataFromCSVFile( String workflowId, String objectJson ) {
        try {
            JSONObject obj = new JSONObject( objectJson );
            JSONObject objDoc = ( JSONObject ) obj.get( "designsummary" );
            String docId = ( String ) objDoc.get( "id" );

            return ResponseUtils.success(
                    wfSchemeManager.updateDesignSummaryWithImportedCSV( workflowId, docId, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getPostProcessForm( String workflowId ) {
        try {
            return ResponseUtils.success( wfSchemeManager.getPostProcessForm( getUserIdFromGeneralHeader(), workflowId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getWorkflowFromPostProcessForm( String workflowId, String objectFilterJson ) {
        try {
            return ResponseUtils.success( wfSchemeManager.getWorkflowFromPostProcessForm( getUserIdFromGeneralHeader(), workflowId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

}
