package de.soco.software.simuspace.server.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Response;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.dao.JobGlobalVariableDAO;
import de.soco.software.simuspace.server.dao.JobIdsDAO;
import de.soco.software.simuspace.server.dao.VariableDAO;
import de.soco.software.simuspace.server.manager.BaseManager;
import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.server.manager.WorkflowManager;
import de.soco.software.simuspace.server.manager.WorkflowUserManager;
import de.soco.software.simuspace.server.model.JobTreeNodeDTO;
import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsCustomFlagFields;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileExtension;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUrlViews;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTemplates;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobRelationTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.RunMode;
import de.soco.software.simuspace.suscore.common.enums.simflow.SchemeCategoryEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SectionFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.BreadCrumbItemDTO;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.JobScheme;
import de.soco.software.simuspace.suscore.common.model.JobSchemeAlgo;
import de.soco.software.simuspace.suscore.common.model.JobSchemeDetail;
import de.soco.software.simuspace.suscore.common.model.JobSchemeExperiment;
import de.soco.software.simuspace.suscore.common.model.JobSchemeFileSummary;
import de.soco.software.simuspace.suscore.common.model.JobSchemeSummary;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.model.SchemeSummaryResults;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.model.WorkflowScheme;
import de.soco.software.simuspace.suscore.common.properties.DesignPlotingConfig;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.Renderer;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.SubTabsUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.ExecutionHosts;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.Hosts;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.OSValidator;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.common.util.ZipUtils;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.JobDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectTraceEntity;
import de.soco.software.simuspace.suscore.data.entity.DesignVariableEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.JobIdsEntity;
import de.soco.software.simuspace.suscore.data.entity.LocationEntity;
import de.soco.software.simuspace.suscore.data.entity.ObjectiveVariableEntity;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.entity.WFSchemeEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.DataObjectDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectImageDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectTraceDTO;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobGlobalVariablesEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.document.dao.DocumentDAO;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.simuspace.suscore.location.manager.LocationManager;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.permissions.model.ManageObjectDTO;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.dto.WorkflowDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowDefinitionDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowElement;
import de.soco.software.simuspace.workflow.dto.WorkflowModel;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.SystemWorkflow;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.ProgressBar;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.model.impl.UserJobDTO;
import de.soco.software.suscore.jsonschema.model.PostProcess;

/**
 * This class is to create/update workflow jobs. Workflow job is created when a workflow is executed.
 *
 * @author Aroosa.Bukhari
 */
@Log4j2
public class JobManagerImpl extends BaseManager implements JobManager {

    /**
     * The constant DESIGN_SUMMARY_CSV.
     */
    private static final String DESIGN_SUMMARY_CSV = "designSummary.csv";

    /**
     * The Constant API_DOCUMENT_UPLOAD.
     */
    private static final String API_DOCUMENT_UPLOAD = "api/document/upload";

    /**
     * The Constant OBJECT.
     */
    private static final String OBJECT = "Object";

    /**
     * The Constant TRACE.
     */
    private static final String TRACE = "Trace";

    /**
     * The Constant SCHEME.
     */
    private static final String SCHEME = "Scheme";

    /**
     * The Constant RE_RUN_JOB.
     */
    private static final String RE_RUN_JOB = "4100046x4";

    /**
     * The Constant DISCARD_JOB.
     */
    private static final String DISCARD_JOB = "4100047x4";

    /**
     * The Constant LOG_GENERAL_NAME.
     */
    private static final String LOG_GENERAL_NAME = "logGeneral";

    /**
     * The Constant LOG_SUMMARY_NAME.
     */
    private static final String LOG_SUMMARY_NAME = "logSummary";

    /**
     * The Constant NAME_FIELD.
     */
    private static final String NAME_FIELD = "name";

    /**
     * The constant RUN_ON_FIELD.
     */
    private static final String RUN_ON_FIELD = "runsOn";

    /**
     * The constant MAX_EXE_TIME_FIELD.
     */
    private static final String MAX_EXE_TIME_FIELD = "max_execution_time";

    /**
     * The constant DESCRIPTION_FIELD.
     */
    private static final String DESCRIPTION_FIELD = "description";

    /**
     * The Constant VIEW_WORKFLOW_BY_ID_AND_VERSION_URL.
     */
    public static final String VIEW_JOB_BY_ID = "view/job/{id}";

    /**
     * The Constant POST_WORKFLOW.
     */
    public static final String POST_WORKFLOW = ">>Post Workflow: ";

    /**
     * The Constant DOWNLOAD_LOG_SUMMARY_URL.
     */
    private static final String DOWNLOAD_LOG_SUMMARY_URL = "document/{logSummary.id}/{logSummary.version.id}/download";

    /**
     * The Constant DOWNLOAD_LOG_GENERAL_URL.
     */
    private static final String DOWNLOAD_LOG_GENERAL_URL = "document/{logGeneral.id}/{logGeneral.version.id}/download";

    /**
     * The Constant TAB_NAME_PERMISSIONS.
     */
    private static final String TAB_NAME_PERMISSIONS = "Permissions";

    /**
     * The Constant TAB_NAME_VERSIONS.
     */
    private static final String TAB_NAME_PROPERTIES = "Properties";

    /**
     * The Constant TAB_NAME_DATA_CREATED.
     */
    private static final String TAB_NAME_DATA_CREATED = "dataCreated";

    /**
     * The Constant TAB_NAME_INPUT_PARAMETERS.
     */
    private static final String TAB_NAME_INPUT_PARAMETERS = "Parameters";

    /**
     * The Constant TAB_KEY_SCHEME_OPTIONS
     */
    private static final String TAB_KEY_SCHEME_OPTIONS = "Scheme Options";

    /**
     * The Constant TAB_NAME_JOB_LOG.
     */
    private static final String TAB_NAME_JOB_LOG = "Log";

    /**
     * The Constant PLUGIN_OBJECT.
     */
    private static final String PLUGIN_OBJECT = "plugin_workflow";

    /**
     * The Constant COMPLETED.
     */
    private static final String COMPLETED = "Completed";

    /**
     * The Constant CONTAINS.
     */
    private static final String CONTAINS = "Contains";

    /**
     * The Constant DOES_NOT_CONTAIN.
     */
    private static final String DOES_NOT_CONTAIN = "Does Not Contain";

    /**
     * The equals.
     */
    private static final String EQUALS = "Equals";

    /**
     * The not equals.
     */
    private static final String NOT_EQUALS = "Does Not Equal";

    /**
     * The begins with.
     */
    private static final String BEGINS_WITH = "Begins With";

    /**
     * The ends with.
     */
    private static final String ENDS_WITH = "Ends With";

    /**
     * The Constant USER_KEY.
     */
    private static final String USER_KEY = "user";

    /**
     * The Constant RESTRICTED_USER.
     */
    private static final String RESTRICTED_USER = "Yes";

    /**
     * The Constant URL_TYPE.
     */
    private static final String URL_TYPE = "urlType";

    /**
     * The Constant GENERATE_IMAGE_CURVES.
     */
    private static final String GENERATE_IMAGE_CURVES = "curve";

    /**
     * The Constant COMBINED_GOAL.
     */
    private static final String COMBINED_GOAL = "combined_goal";

    /**
     * The Constant OBJ_VAR_GOAL_POSTFIX.
     */
    private static final String OBJ_VAR_GOAL_POSTFIX = "_goal";

    /**
     * The Constant GOAL.
     */
    private static final String GOAL = "goal";

    /**
     * The Constant GENERATE_STACK_PLOT.
     */
    private static final String GENERATE_STACK_PLOT = "stack";

    /**
     * The Constant EXP_NUM.
     */
    private static final String EXP_NUM = "expNum";

    /**
     * The Constant ELEMENTS.
     */
    private static final String ELEMENTS = "elements";

    /**
     * The Constant NODES.
     */
    private static final String NODES = "nodes";

    /**
     * The Constant DATA.
     */
    private static final String DATA = "data";

    /**
     * The Constant ID.
     */
    private static final String ID = "id";

    /**
     * The Constant FIELDS.
     */
    private static final String FIELDS = "fields";

    /**
     * The Constant NAME.
     */
    private static final String NAME = "name";

    /**
     * The Constant TOOL_TIP.
     */
    private static final String TOOL_TIP = "tooltip";

    private static final String SYSTEM_JOBS = "systemJobs";

    private static final String WORKFLOW_JOBS = "workflowJobs";

    /**
     * It is a service reference for the user services such as getUser.
     */
    private WorkflowUserManager userManager;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The object type config manager.
     */
    private ObjectTypeConfigManager configManager;

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private SuSGenericObjectDAO< JobEntity > jobEntityDAO;

    /**
     * The Job global variable dao.
     */
    private JobGlobalVariableDAO jobGlobalVariableDAO;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The location manager.
     */
    private LocationManager locationManager;

    /**
     * it is a service reference, which stores and retrieves job information from database.
     */
    private JobDAO jobDao;

    /**
     * It is a service reference, which offers workflow services such as getWorkflow.
     */
    private WorkflowManager workflowManager;

    /**
     * The context menu manager.
     */
    private ContextMenuManager contextMenuManager;

    /**
     * The Variable dao.
     */
    private VariableDAO variableDAO;

    /**
     * The job ids DAO.
     */
    private JobIdsDAO jobIdsDAO;

    /**
     * The token manager.
     */
    private UserTokenManager tokenManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Default empty constructor. All dependencies for this class are injected via setter methods with OSGI Blueprint
     */
    public JobManagerImpl() {
        super();
    }

    /**
     * It converts from byte array to log record list.
     *
     * @param bytes
     *         serialized data
     *
     * @return list of parameters of a job
     */
    @SuppressWarnings( "unchecked" )
    private List< LogRecord > convertFromByteArrayToLogRecordList( byte[] bytes ) {
        List< LogRecord > logger = null;
        if ( bytes != null ) {
            try ( ByteArrayInputStream bis = new ByteArrayInputStream( bytes ); ObjectInputStream ois = new ObjectInputStream( bis ) ) {
                logger = ( List< LogRecord > ) ois.readObject();

            } catch ( final ClassNotFoundException | IOException e ) {
                log.error( e.getMessage(), e );
            }
            if ( logger != null ) {
                Collections.reverse( logger );
            }
        }
        return logger;

    }

    /**
     * It converts from log record list to byte array.
     *
     * @param logRecords
     *         log records of a job
     *
     * @return the serialized logs from the records list passed, an empty byte array in case there are no log records.
     */
    private byte[] convertFromLogRecordListToByteArray( List< LogRecord > logRecords ) {
        byte[] result = null;
        if ( logRecords != null ) {

            try ( ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream( bos ) ) {

                oos.writeObject( new ArrayList<>( logRecords ) );
                result = bos.toByteArray();
            } catch ( final IOException e ) {
                log.error( e.getMessage(), e );
                result = null;
            }

        }
        return result;
    }

    /**
     * Fills workflow entity variables taking from job object. This method is used when storing job to database
     *
     * @param jobImpl
     *         the job impl
     *
     * @return filled entity or <code>null</code> if input is null
     */
    private WorkflowEntity fillWorkflowEntity( Job jobImpl ) {
        final WorkflowEntity workflowEntity = new WorkflowEntity();
        workflowEntity.setName( jobImpl.getWorkflowName() );
        if ( jobImpl.getWorkflowVersion() != null ) {
            workflowEntity.setComposedId( new VersionPrimaryKey( jobImpl.getWorkflowId(), jobImpl.getWorkflowVersion().getId() ) );
            workflowEntity.setVersionId( jobImpl.getWorkflowVersion().getId() );
        }
        return workflowEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Job getJob( UUID userId, String jobId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getJob( entityManager, userId, jobId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Job getJob( EntityManager entityManager, UUID userId, String jobId ) {
        /*
         * validates if the user has the role workflow user, else throws exception
         */
        if ( jobId == null ) {
            throw new SusException( MessagesUtil.getMessage( WFEMessages.JOB_ID_IS_NULL ) );
        } else {
            JobEntity entity;
            UUID uuid = null;
            Integer integer = null;
            try {
                uuid = UUID.fromString( jobId );
            } catch ( IllegalArgumentException e ) {
                integer = Integer.parseInt( jobId );
            }
            if ( uuid != null ) {
                entity = jobDao.getLatestNonDeletedObjectById( entityManager, uuid );
            } else {
                JobIdsEntity jobIdsEntity = jobIdsDAO.getObjectByProperty( entityManager, JobIdsEntity.class, "id", integer );
                entity = jobDao.getLatestNonDeletedObjectById( entityManager, jobIdsEntity.getUuid() );
            }
            if ( entity == null ) {
                throw new SusException( MessagesUtil.getMessage( WFEMessages.JOB_NOT_FOUND ) );
            }
            return prepareJob( entityManager, entity );
        }
    }

    /**
     * Process tree children.
     *
     * @param userId
     *         the user id
     * @param jobId
     *         the job id
     *
     * @return the job tree node DTO
     */
    @Override
    public JobTreeNodeDTO processTreeChildren( String userId, UUID jobId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                    jobId + ConstantsString.COLON + PermissionMatrixEnum.READ.getValue() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_READ.getKey(), OBJECT ) );
            }
            return processTreeChildren( entityManager, jobId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Process tree children.
     *
     * @param entityManager
     *         the entity manager
     * @param jobId
     *         the job id
     *
     * @return the job tree node DTO
     */
    private JobTreeNodeDTO processTreeChildren( EntityManager entityManager, UUID jobId ) {
        JobEntity jobEntity = jobDao.getLatestObjectById( entityManager, JobEntity.class, jobId );
        JobTreeNodeDTO jobTreeNodeDTO = prepareJobTreeNodeDTO( jobEntity );
        if ( jobTreeNodeDTO != null ) {
            List< JobEntity > childrenEntities = jobDao.getAllChildrenOfMasterJob( entityManager, jobId );
            if ( CollectionUtils.isNotEmpty( childrenEntities ) ) {
                List< JobTreeNodeDTO > jobTreeNodeDTOs = new ArrayList<>();
                if ( jobEntity.getJobType() == JobTypeEnums.VARIANT.getKey() || jobEntity.getName().contains( "Assembly" )
                        || jobEntity.getName().contains( "Solver" ) ) {
                    prepareVariantJobTreeLog( entityManager, jobTreeNodeDTO, childrenEntities, jobTreeNodeDTOs );
                } else {
                    prepareJobTreeLog( jobTreeNodeDTO, childrenEntities, jobTreeNodeDTOs );
                }
                sortTreeChildrensByTitle( jobTreeNodeDTOs );
                jobTreeNodeDTO.setChildren( jobTreeNodeDTOs );
            }
        }
        return jobTreeNodeDTO;
    }

    /**
     * Prepare job tree log.
     *
     * @param jobTreeNodeDTO
     *         the job tree node DTO
     * @param childrenEntities
     *         the children entities
     * @param jobTreeNodeDTOs
     *         the job tree node DT os
     */
    private void prepareJobTreeLog( JobTreeNodeDTO jobTreeNodeDTO, List< JobEntity > childrenEntities,
            List< JobTreeNodeDTO > jobTreeNodeDTOs ) {
        for ( JobEntity childJobEntity : childrenEntities ) {
            if ( !childJobEntity.getId().toString().equals( jobTreeNodeDTO.getId() ) ) {
                jobTreeNodeDTOs.add( prepareJobTreeNodeDTO( childJobEntity ) );
            }
        }
    }

    /**
     * Prepare variant job tree log.
     *
     * @param entityManager
     *         the entity manager
     * @param jobTreeNodeDTO
     *         the job tree node DTO
     * @param childrenEntities
     *         the children entities
     * @param jobTreeNodeDTOs
     *         the job tree node DT os
     */
    private void prepareVariantJobTreeLog( EntityManager entityManager, JobTreeNodeDTO jobTreeNodeDTO, List< JobEntity > childrenEntities,
            List< JobTreeNodeDTO > jobTreeNodeDTOs ) {
        for ( JobEntity childJobEntity : childrenEntities ) {
            // this condition deal If cyclic relation occur
            if ( !childJobEntity.getId().toString().equals( jobTreeNodeDTO.getId() ) ) {
                jobTreeNodeDTOs.add( processTreeChildren( entityManager, childJobEntity.getId() ) );
            }
        }
    }

    /**
     * Sort Tree Childrens by Title.
     *
     * @param jobTreeNodeDTOs
     *         the job Tree Node DTOs
     */
    private void sortTreeChildrensByTitle( List< JobTreeNodeDTO > jobTreeNodeDTOs ) {
        jobTreeNodeDTOs.sort( ( o1, o2 ) -> StringUtils.compareAlphaNumericStrings( o1.getTitle(), o2.getTitle() ) );
    }

    /**
     * Prepare job tree node DTO.
     *
     * @param jobEntity
     *         the job entity
     *
     * @return the job tree node DTO
     */
    private JobTreeNodeDTO prepareJobTreeNodeDTO( JobEntity jobEntity ) {
        JobTreeNodeDTO jobTreeNodeDTO = null;
        if ( jobEntity != null ) {
            String jobRunningIdentifier = "*";
            if ( !String.valueOf( jobEntity.getStatus() ).equalsIgnoreCase( String.valueOf( WorkflowStatus.RUNNING.getKey() ) ) ) {
                jobRunningIdentifier = "";
            }

            jobTreeNodeDTO = new JobTreeNodeDTO();
            jobTreeNodeDTO.setId( jobEntity.getId().toString() );
            jobTreeNodeDTO.setTitle( jobEntity.getName() + jobRunningIdentifier );
            jobTreeNodeDTO.setFolder( true );
            jobTreeNodeDTO.setIcon( "icon fa fa-gear" );
            jobTreeNodeDTO.setExpanded( true );
        }
        return jobTreeNodeDTO;
    }

    /**
     * It converts the job implementation object into the job entity object to save in Database.
     *
     * @param entityManager
     *         the entity manager
     * @param jobImpl
     *         the job impl
     *
     * @return JobEntity Object
     */
    private JobEntity prepareJobEntity( EntityManager entityManager, Job jobImpl ) {

        JobEntity entity = null;

        if ( jobImpl == null ) {
            return entity;
        } else {
            entity = new JobEntity();
            setJobEntityBasicAttributes( jobImpl, entity );

            if ( jobImpl.getRunMode() != null ) {

                if ( LocationsEnum.getLocalLocations().contains( jobImpl.getRunMode() ) ) {
                    entity.setRunMode( RunMode.LOCAL.getId() );
                } else {
                    entity.setRunMode( RunMode.SERVER.getId() );
                }
            }

            entity.setRestAPI( ByteUtil.convertStringToByte( JsonUtils.toJson( jobImpl.getServer() ) ) );
            entity.setLifeCycleStatus( configManager.getLifeCycleManager().getDefaultStatusOfJob() );

            if ( null != jobImpl.getRunsOn() ) {
                entity.setRunsOn( prepareLocationEntity( jobImpl.getRunsOn() ) );
            }

            if ( jobImpl.getWorkflowId() != null ) {
                entity.setWorkflowId( jobImpl.getWorkflowId() );
            }

            if ( jobImpl.getCreatedBy() != null ) {
                entity.setCreatedBy( userManager.getSimUser( entityManager, UUID.fromString( jobImpl.getCreatedBy().getId() ) ) );
            }

            if ( jobImpl.getWorkingDir() != null ) {
                entity.setJobDirectory( jobImpl.getWorkingDir().getPath() );
            }

            if ( jobImpl.getIOParameters() != null ) {
                entity.setIOParameters( CollectionUtil.convertMapToByteArray( jobImpl.getIOParameters() ) );
            }

            if ( jobImpl.getResultSummary() != null ) {
                entity.setResultSummary( ByteUtil.convertStringToByte( JsonUtils.toJson( jobImpl.getResultSummary() ) ) );
            }

            if ( jobImpl.getAskOnRunParameters() != null ) {
                entity.setAskOnRunParameters( ByteUtil.convertStringToByte( JsonUtils.toJson( jobImpl.getAskOnRunParameters() ) ) );
            }

            entity.setJobLogPath( jobImpl.getLogPath() );

            if ( ( jobImpl.getRerunFromJob() != null ) && ( jobImpl.getRerunFromJob().getId() != null ) ) {
                entity.setRefJob( jobDao.getLatestNonDeletedObjectById( entityManager, jobImpl.getRerunFromJob().getId() ) );
            } else {
                entity.setRefJob( null );
            }

            entity.setCreateChild( jobImpl.isCreateChild() );
            entity.setJobSchemeCategory( jobImpl.getJobSchemeCategory() );
            entity.setLog( convertFromLogRecordListToByteArray( jobImpl.getLog() ) );
            entity.setJobType( jobImpl.getJobType() );
            entity.setJobRelationType( jobImpl.getJobRelationType() );
            if ( jobImpl.getPostprocess() != null ) {
                entity.setPostProcess( ByteUtil.convertStringToByte( JsonUtils.objectToJson( jobImpl.getPostprocess() ) ) );
            }
            entity.setJobMaxExecutionTime( jobImpl.getJobMaxExecutionTime() );
            entity.setPostprocessParametersJson( ByteUtil.convertStringToByte( jobImpl.getPostprocessParametersJson() ) );

        }

        if ( null != jobImpl.getRequestHeaders() && null != jobImpl.getRequestHeaders().getJobAuthToken() && !jobImpl.getRequestHeaders()
                .getJobAuthToken().isEmpty() ) {
            entity.setToken( jobImpl.getRequestHeaders().getJobAuthToken() );
        }
        return entity;

    }

    /**
     * Sets the job entity basic attributes.
     *
     * @param jobImpl
     *         the job impl
     * @param entity
     *         the entity
     */
    private void setJobEntityBasicAttributes( Job jobImpl, JobEntity entity ) {
        // Set Entity Object
        UUID jobId = UUID.randomUUID();
        if ( jobImpl.getId() != null ) {
            jobId = jobImpl.getId();
        }
        entity.setId( jobId );
        // Set workflow id and version id
        entity.setWorkflow( fillWorkflowEntity( jobImpl ) );
        entity.setDescription( jobImpl.getDescription() );
        entity.setCreatedBy( userManager.prepareUserEntity( jobImpl.getCreatedBy() ) );
        final Date date = new Date();
        entity.setCreatedOn( date );
        entity.setModifiedOn( date );
        entity.setStartedOn( date );
        entity.setMachine( jobImpl.getMachine() );
        entity.setPid( jobImpl.getJobProcessId() );
        entity.setOs( jobImpl.getOs() );

        if ( jobImpl.getStatus() != null ) {
            entity.setStatus( jobImpl.getStatus().getId() );
        }
        entity.setName( jobImpl.getName() );
        if ( jobImpl.getProgress() != null ) {
            entity.setTotalElements( jobImpl.getProgress().getTotal() );
            entity.setCompletedElements( jobImpl.getProgress().getCompleted() );
        }

        if ( jobImpl.getChildJobsCount() != null ) {
            entity.setChildJobsCount( jobImpl.getChildJobsCount() );
        }
    }

    /**
     * It converts the job entity object into the job implementation object if jobEntity is null then JObImpl =null should be return.
     *
     * @param entityManager
     *         the entity manager
     * @param jobEntity
     *         the job entity
     * @param isCreate
     *         the is create
     *
     * @return Job Object or <code>null</code>
     */
    private JobImpl prepareJobModel( EntityManager entityManager, JobEntity jobEntity, boolean isCreate ) {

        JobImpl jobImpl = null;
        if ( jobEntity != null ) {
            jobImpl = new JobImpl();
            setJobBasicAttributes( entityManager, jobEntity, jobImpl, isCreate );

            if ( jobEntity.getRestAPI() != null ) {
                jobImpl.setServer( JsonUtils.jsonToObject( ByteUtil.convertByteToString( jobEntity.getRestAPI() ), RestAPI.class ) );
            }

            if ( jobEntity.getPostProcess() != null ) {
                jobImpl.setPostprocess(
                        JsonUtils.jsonToObject( ByteUtil.convertByteToString( jobEntity.getPostProcess() ), PostProcess.class ) );
            }

            if ( jobEntity.getPostprocessParametersJson() != null ) {
                jobImpl.setPostprocessParametersJson( ByteUtil.convertByteToString( jobEntity.getPostprocessParametersJson() ) );
            }

            if ( ( jobEntity.getWorkflow() != null ) && ( jobEntity.getWorkflow().getComposedId() != null ) ) {
                jobImpl.setWorkflowName( jobEntity.getWorkflow().getName() );
                jobImpl.setWorkflowId( jobEntity.getWorkflow().getComposedId().getId() );
                jobImpl.setWorkflowVersion( new VersionDTO( jobEntity.getWorkflow().getVersionId() ) );
            }
            // job log
            if ( ( jobEntity.getLogSummary() != null ) && ( jobEntity.getLogSummary().getId() != null ) ) {
                jobImpl.setLogSummary( prepareDocumentDTO( jobEntity.getLogSummary() ) );
            }
            if ( jobEntity.getResultSummary() != null ) {
                jobImpl.setResultSummary( ByteUtil.convertByteToString( jobEntity.getResultSummary() ) );
            }
            if ( ( jobEntity.getLogGeneral() != null ) && ( jobEntity.getLogGeneral().getId() != null ) ) {
                jobImpl.setLogGeneral( prepareDocumentDTO( jobEntity.getLogGeneral() ) );
            }
            if ( null != jobEntity.getRefJob() ) {
                jobImpl.setRerunFromJob( prepareJobModel( entityManager, jobEntity.getRefJob(), Boolean.TRUE ) );
            } else {
                jobImpl.setRerunFromJob( null );
            }
            final String srcfileName = jobEntity.getLogSummary() != null ? jobEntity.getLogSummary().getFileName() : null;
            jobImpl.setLogPath( jobEntity.getJobDirectory() + File.separator + srcfileName );
            jobImpl.setJobType( ( jobEntity.getJobType() == null ) ? JobTypeEnums.SYSTEM.getKey() : jobEntity.getJobType() );
            jobImpl.setJobRelationType( ( jobEntity.getJobRelationType() == null ) ? 0 : jobEntity.getJobRelationType() );
            jobImpl.setWorkflow( getWorkflowManager().prepareWorkflowDTO( jobEntity.getWorkflow() ) );
            jobImpl.setJobMaxExecutionTime( jobEntity.getJobMaxExecutionTime() );
            jobImpl.setJobSchemeCategory( jobEntity.getJobSchemeCategory() );
            if ( null != jobEntity.isCreateChild() ) {
                jobImpl.setCreateChild( jobEntity.isCreateChild() );
            }
            if ( null != jobEntity.getAskOnRunParameters() ) {
                LinkedHashMap< String, Object > askOnRunParams = new LinkedHashMap<>();
                String askOnRunJsonString = ByteUtil.convertByteToString( jobEntity.getAskOnRunParameters() );
                if ( StringUtils.isNotNullOrEmpty( askOnRunJsonString ) && JsonUtils.isValidJSON( askOnRunJsonString ) ) {
                    LinkedHashMap< String, Object > askOnRunMap = ( LinkedHashMap< String, Object > ) JsonUtils.jsonToMap(
                            askOnRunJsonString, askOnRunParams );
                    jobImpl.setAskOnRunParameters( askOnRunMap );
                } else {
                    jobImpl.setAskOnRunParameters( new LinkedHashMap<>() );
                }
            }
            JobIdsEntity jobIdsEntity = jobIdsDAO.getObjectByProperty( entityManager, JobIdsEntity.class, "uuid", jobEntity.getId() );
            if ( jobIdsEntity != null ) {
                jobImpl.setJobInteger( jobIdsEntity.getId() );
            }
            if ( jobEntity.isAutoDelete() != null ) {
                jobImpl.setAutoDelete( jobEntity.isAutoDelete() );
            }
            if ( null != jobEntity.getToken() ) {
                RequestHeaders requestHeaders = new RequestHeaders();
                requestHeaders.setJobAuthToken( jobEntity.getToken() );
                jobImpl.setRequestHeaders( requestHeaders );
            }
        }
        return jobImpl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobEntity getJobById( EntityManager entityManager, UUID jobId ) {
        if ( jobId == null ) {
            throw new SusException( MessagesUtil.getMessage( WFEMessages.JOB_ID_IS_NULL ) );
        } else {
            return jobDao.getLatestNonDeletedObjectById( entityManager, jobId );
        }

    }

    /**
     * Prepare document DTO.
     *
     * @param documentEntity
     *         the document entity
     *
     * @return the document DTO
     */
    private DocumentDTO prepareDocumentDTO( DocumentEntity documentEntity ) {
        final DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setName( documentEntity.getFileName() );
        documentDTO.setId( documentEntity.getId().toString() );
        if ( PropertiesManager.isEncrypted() ) {
            documentDTO.setEncryptionDecryption( PropertiesManager.getEncryptionDecryptionDTO() );
        }
        documentDTO.setVersion( new VersionDTO( ConstantsInteger.INTEGER_VALUE_ONE ) );
        final String url = CommonUtils.getBaseUrl( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.SUS_WEB_BASE_URL ) )
                + File.separator + ConstantsString.STATIC_PATH + documentEntity.getFilePath() + File.separator + documentDTO.getName();
        documentDTO.setUrl( url );
        return documentDTO;
    }

    /**
     * Sets the job basic attributes.
     *
     * @param entityManager
     *         the entity manager
     * @param jobEntity
     *         the job entity
     * @param jobImpl
     *         the job impl
     * @param isCreate
     *         the is create
     */
    @SuppressWarnings( "unchecked" )
    private void setJobBasicAttributes( EntityManager entityManager, JobEntity jobEntity, JobImpl jobImpl, boolean isCreate ) {
        jobImpl.setId( jobEntity.getId() );
        jobImpl.setName( jobEntity.getName() );
        jobImpl.setDescription( jobEntity.getDescription() );
        jobImpl.setJobProcessId( jobEntity.getPid() );

        jobImpl.setStatus( new Status( WorkflowStatus.getById( jobEntity.getStatus() ) ) );
        jobImpl.setRunMode( RunMode.getById( jobEntity.getRunMode() ).getKey() );

        if ( jobEntity.getRunsOn() != null ) {
            LocationDTO locationDTO;
            if ( isCreate ) {
                locationDTO = locationManager.getLocation( entityManager, jobEntity.getRunsOn().getId().toString() );
            } else {
                locationDTO = locationManager.prepareDTO( jobEntity.getRunsOn() );
            }
            jobImpl.setRunsOn( locationDTO );
        }

        if ( jobEntity.getCreatedBy() != null ) {
            jobImpl.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( jobEntity.getCreatedBy() ) );
        }

        // Set execution log
        jobImpl.setLog( convertFromByteArrayToLogRecordList( jobEntity.getLog() ) );

        // Set Input Parameters
        if ( jobEntity.getIOParameters() != null ) {
            jobImpl.setIOParameters(
                    ( Map< String, Map< String, Object > > ) CollectionUtil.convertByteArrayToMap( jobEntity.getIOParameters() ) );
        }
        if ( jobEntity.getResultSummary() != null ) {
            jobImpl.setResultSummary( ByteUtil.convertByteToString( jobEntity.getResultSummary() ) );
        }
        final EngineFile file = new EngineFile();
        file.setPath( jobEntity.getJobDirectory() );
        file.setItems( new ArrayList<>( Collections.singletonList( jobEntity.getJobDirectory() ) ) );
        jobImpl.setWorkingDir( file );
        jobImpl.setSubmitTime( jobEntity.getCreatedOn() );
        jobImpl.setCompletionTime( jobEntity.getFinishedOn() );
        jobImpl.setOs( jobEntity.getOs() );
        // other fields set

        jobImpl.setMachine( jobEntity.getMachine() );

        if ( jobEntity.getChildJobsCount() != null ) {
            jobImpl.setChildJobsCount( jobEntity.getChildJobsCount() );
        }

        final ProgressBar progres = new ProgressBar();
        if ( jobEntity.getTotalElements() != null ) {
            progres.setTotal( jobEntity.getTotalElements() );
        }
        if ( jobEntity.getCompletedElements() != null ) {
            progres.setCompleted( jobEntity.getCompletedElements() );
        }

        if ( ( jobEntity.getWorkflowId() != null ) && ( jobEntity.getWorkflowId().equals( UUID.fromString( SystemWorkflow.DELETE.getId() ) )
                || jobEntity.getWorkflowId().equals( UUID.fromString( SystemWorkflow.RESTORE.getId() ) ) ) && jobImpl.getStatus().getName()
                .equals( COMPLETED ) ) {
            progres.setTotal( 1 );
            progres.setCompleted( 1 );
        }
        jobImpl.setProgress( progres );
        jobImpl.setLogPath( jobEntity.getJobLogPath() );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Job createJob( UUID userId, Job job ) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return createJob( entityManager, userId, job );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Job createJob( EntityManager entityManager, UUID userId, Job job ) {

        Job jobToReturn;
        try {
            JobEntity entity = prepareJobEntity( entityManager, job );

            final JobEntity createdEntity = jobDao.saveJob( entityManager, entity );
            addPermissionToJob( entityManager, userId, createdEntity );

            jobToReturn = prepareJobModel( entityManager, createdEntity, Boolean.TRUE );
            saveJobGlobalVariables( entityManager, entity, job, userId );
        } catch ( final Exception ex ) {
            ExceptionLogger.logException( ex, getClass() );
            jobToReturn = null;
        }
        return jobToReturn;
    }

    /**
     * saves job's global variables
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the job entity
     * @param job
     *         the job
     * @param userId
     *         the user id
     */
    private void saveJobGlobalVariables( EntityManager entityManager, JobEntity entity, Job job, UUID userId ) {
        if ( job.getGlobalVariables() != null && !job.getGlobalVariables().isEmpty() ) {
            jobGlobalVariableDAO.saveOrUpdate( entityManager, new JobGlobalVariablesEntity( UUID.randomUUID(), entity.getId(),
                    ByteUtil.convertStringToByte( JsonUtils.convertMapToStringGernericValue( job.getGlobalVariables() ) ), userId ) );
        }

    }

    @Override
    public Integer getJobIdByUUID( UUID uuid ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getJobIdByUUID( entityManager, uuid );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Integer getJobIdByUUID( EntityManager entityManager, UUID uuid ) {
        Integer id = null;
        if ( uuid != null ) {
            JobIdsEntity jobIdsEntity = jobIdsDAO.getJobIdsEntityByUUID( entityManager, uuid );
            if ( jobIdsEntity != null ) {
                id = jobIdsEntity.getId();
            }
        }
        return id;
    }

    @Override
    public Integer saveJobIds( UUID uuid ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return saveJobIds( entityManager, uuid );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Integer saveJobIds( EntityManager entityManager, UUID uuid ) {
        Integer id = null;
        if ( uuid != null ) {
            JobIdsEntity jobIdsEntity = jobIdsDAO.saveJobIds( entityManager, new JobIdsEntity( uuid, "job" ) );
            if ( jobIdsEntity != null ) {
                id = jobIdsEntity.getId();
            }
        }
        return id;
    }

    @Override
    public Integer saveJobIdIfNotExist( EntityManager entityManager, UUID uuid ) {
        Integer id = getJobIdByUUID( entityManager, uuid );

        if ( id != null ) {
            return id;
        } else {
            return saveJobIds( entityManager, uuid );
        }
    }

    /**
     * Adds the permission to job.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param createdEntity
     *         the created entity
     */
    private void addPermissionToJob( EntityManager entityManager, UUID userId, final JobEntity createdEntity ) {
        final LatestWorkFlowDTO parentDto = workflowManager.getWorkflowById( entityManager, userId,
                createdEntity.getWorkflow().getComposedId().getId() );
        final WorkflowEntity parentEntity = new WorkflowEntity( parentDto.getId(), parentDto.getVersion().getId() ); // add permission
        List< UUID > selectionOfUser = new ArrayList<>();
        selectionOfUser.add( userId );
        SelectionResponseUI userSelectionId = permissionManager.saveSelectionItems( entityManager, userId.toString(),
                SelectionOrigins.USER.getOrigin(), selectionOfUser );
        parentEntity.setUserSelectionId( userSelectionId.getId() );
        workflowManager.addJobToAcl( entityManager, userId.toString(), createdEntity, parentEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Job updateJob( Job job ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return updateJob( entityManager, job );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Job updateJob( EntityManager entityManager, Job job ) {
        final JobEntity entity = this.updateJobEntity( entityManager, job );
        // Update job status
        final JobEntity returnEntity = jobDao.updateJobStatus( entityManager, entity );
        if ( returnEntity == null ) {
            return null;
        }
        return prepareJobModel( entityManager, returnEntity, Boolean.FALSE );

    }

    /**
     * It updates the job entity with the additional parameters "status" and "job id" and Input/Output parameters.
     *
     * @param entityManager
     *         the entity manager
     * @param job
     *         the job
     *
     * @return JobEntity object
     */
    private JobEntity updateJobEntity( EntityManager entityManager, Job job ) {
        final JobEntity entity = jobDao.getLatestNonDeletedObjectById( entityManager, job.getId() );
        entity.setLog( convertFromLogRecordListToByteArray( job.getLog() ) );
        job.setJobType( job.getJobType() );
        if ( job.getStatus() != null ) {
            entity.setStatus( job.getStatus().getId() );
        }
        if ( job.getWorkingDir() != null ) {
            entity.setJobDirectory( job.getWorkingDir().getPath() );
        }
        if ( job.getLogGeneral() != null ) {
            entity.setLogGeneral( new DocumentEntity( UUID.fromString( job.getLogGeneral().getId() ) ) );
        }
        if ( job.getResultSummary() != null ) {
            entity.setResultSummary( ByteUtil.convertStringToByte( job.getResultSummary() ) );
        }
        if ( job.getLogSummary() != null ) {
            entity.setLogSummary( new DocumentEntity( UUID.fromString( job.getLogSummary().getId() ) ) );
        }
        if ( job.getLogGeneralFilePath() != null && !job.getLogGeneralFilePath().isEmpty() ) {
            entity.setJobLogPath( job.getLogGeneralFilePath() );
        }
        entity.setFinishedOn( new Date() );
        // to avoid overriden completed elements count
        if ( ( job.getProgress() != null ) && ( ConstantsInteger.INTEGER_VALUE_ZERO != job.getProgress().getCompleted() ) ) {
            entity.setCompletedElements( job.getProgress().getCompleted() );
        }

        if ( ( job.getProgress() != null ) && ( ConstantsInteger.INTEGER_VALUE_ZERO != job.getProgress().getTotal() ) ) {
            entity.setTotalElements( job.getProgress().getTotal() );
        }
        if ( null != job.getRequestHeaders() && null != job.getRequestHeaders().getJobAuthToken() && !job.getRequestHeaders()
                .getJobAuthToken().isEmpty() ) {
            entity.setToken( job.getRequestHeaders().getJobAuthToken() );
        }
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Job > getJobsList( UUID userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final List< Job > list = new ArrayList<>();
            List< JobEntity > jobList = jobDao.getJobsList( entityManager );
            if ( CollectionUtil.isNotEmpty( jobList ) ) {
                for ( final JobEntity entity : jobList ) {
                    list.add( prepareJobModel( entityManager, entity, Boolean.FALSE ) );
                }
            }
            return list;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Job > getAllDOEMasterRunningJobsList( EntityManager entityManager ) {
        final List< Job > list = new ArrayList<>();
        List< JobEntity > jobList = jobDao.getMasterRunningJobsList( entityManager );
        if ( CollectionUtil.isNotEmpty( jobList ) ) {
            for ( final JobEntity entity : jobList ) {
                list.add( prepareJobModel( entityManager, entity, Boolean.FALSE ) );
            }
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Job > getRunningChildsAndSingleJobsList( EntityManager entityManager ) {
        final List< Job > list = new ArrayList<>();
        List< JobEntity > jobList = jobDao.getRunningChildsAndSingleJobsList( entityManager );
        if ( CollectionUtil.isNotEmpty( jobList ) ) {
            for ( final JobEntity entity : jobList ) {
                list.add( prepareJobModel( entityManager, entity, Boolean.FALSE ) );
            }
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Job updateJobLog( Job job ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final JobEntity entity = jobDao.getLatestNonDeletedObjectById( entityManager, job.getId() );
            if ( entity != null ) {
                final byte[] updatedLog = convertFromLogRecordListToByteArray( job.getLog() );
                entity.setLog( updatedLog );
                final JobEntity retEntity = jobDao.updateJobLog( entityManager, entity );
                return prepareJobModel( entityManager, retEntity, Boolean.FALSE );
            } else {
                return null;
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Job updateJobLogAndProgress( Job job ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final JobEntity entity = jobDao.getLatestNonDeletedObjectById( entityManager, job.getId() );
            if ( entity != null ) {
                final byte[] updatedLog = convertFromLogRecordListToByteArray( job.getLog() );
                entity.setLog( updatedLog );
                // update progress
                if ( ( job.getProgress() != null ) && ( ConstantsInteger.INTEGER_VALUE_ZERO != job.getProgress().getCompleted() ) ) {
                    entity.setCompletedElements( job.getProgress().getCompleted() );
                }
                if ( ( job.getProgress() != null ) && ( ConstantsInteger.INTEGER_VALUE_ZERO != job.getProgress().getTotal() ) ) {
                    entity.setTotalElements( job.getProgress().getTotal() );
                }
                final JobEntity retEntity = jobDao.updateJobLog( entityManager, entity );
                return prepareJobModel( entityManager, retEntity, Boolean.FALSE );
            } else {
                return null;
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map< String, String > getLastJobDirectoryByWorkFlow( String workflowId ) throws UnknownHostException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Map< String, String > map = null;
            if ( StringUtils.isNotNullOrEmpty( workflowId ) ) {
                if ( !StringUtils.validateUUIDString( workflowId ) ) {
                    throw new SusException(
                            new Exception( MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID, workflowId ) ),
                            getClass() );
                }
                final JobEntity entity = jobDao.getLastJobByWorkFlow( entityManager, UUID.fromString( workflowId ),
                        InetAddress.getLocalHost().getHostName() );
                if ( entity != null ) {
                    map = new HashMap<>();
                    map.put( ConstantsString.JOB_DIR, entity.getJobDirectory() );
                }
                // no need of else it means there no job dir exist
            } else {
                throw new SusException( MessagesUtil.getMessage( WFEMessages.WORKFLOW_ID_CANT_BE_NULL ) );
            }
            return map;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Job > getMySusJobsList( UUID userId, FiltersDTO filtersDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< Job > allJobsList = new ArrayList<>();
            replaceEnumsInJobFilters( filtersDTO );
            final List< JobEntity > entityList = jobDao.getJobsByUserId( entityManager, userId, filtersDTO );
            if ( CollectionUtil.isNotEmpty( entityList ) ) {
                for ( final JobEntity jobEntity : entityList ) {
                    allJobsList.add( prepareJob( entityManager, jobEntity ) );
                }
            }
            return PaginationUtil.constructFilteredResponse( filtersDTO, allJobsList );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List< Object > getAllValuesForJobTableColumn( String columnName, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var allColumns = GUIUtils.listColumns( JobImpl.class );
            GUIUtils.validateColumnForAllValues( columnName, allColumns );
            List< Object > allValues;
            if ( columnName.equals( ConstantsString.STATUS ) ) {
                allValues = new ArrayList<>();
                var statusValues = jobDao.getAllPropertyValues( entityManager, columnName );
                for ( var statusValue : statusValues ) {
                    allValues.add( WorkflowStatus.getById( ( Integer ) statusValue ).getValue() );
                }
            } else {
                allValues = jobDao.getAllPropertyValues( entityManager, columnName );
            }
            return allValues;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List< Object > getAllValuesForChildJobsTableColumn( String masterJobId, String columnName, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var allColumns = GUIUtils.listColumns( JobImpl.class );
            GUIUtils.validateColumnForAllValues( columnName, allColumns );
            List< Object > allValues;
            if ( columnName.equals( ConstantsString.STATUS ) ) {
                allValues = new ArrayList<>();
                var statusValues = jobDao.getAllPropertyValuesWithParentId( entityManager, columnName, UUID.fromString( masterJobId ) );
                for ( var statusValue : statusValues ) {
                    allValues.add( WorkflowStatus.getById( ( Integer ) statusValue ).getValue() );
                }
            } else {
                allValues = jobDao.getAllPropertyValuesWithParentId( entityManager, columnName, UUID.fromString( masterJobId ) );
            }
            return allValues;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI getUserJobsUI() {
        final List< TableColumn > columns = GUIUtils.listColumns( UserJobDTO.class );
        for ( final TableColumn tableColumn : columns ) {
            if ( NAME_FIELD.equalsIgnoreCase( tableColumn.getName() ) ) {
                tableColumn.getRenderer().setUrl( VIEW_JOB_BY_ID );
                tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );
            }
        }
        return new TableUI( columns );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI getListOfJobUITableColumns() {
        final List< TableColumn > columns = GUIUtils.listColumns( JobImpl.class );
        for ( final TableColumn tableColumn : columns ) {
            if ( NAME_FIELD.equalsIgnoreCase( tableColumn.getName() ) ) {
                tableColumn.getRenderer().setUrl( VIEW_JOB_BY_ID );
                tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );
            } else if ( LOG_SUMMARY_NAME.equalsIgnoreCase( tableColumn.getName() ) ) {
                tableColumn.getRenderer().setUrl( DOWNLOAD_LOG_SUMMARY_URL );
                tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );
            } else if ( LOG_GENERAL_NAME.equalsIgnoreCase( tableColumn.getName() ) ) {
                tableColumn.getRenderer().setUrl( DOWNLOAD_LOG_GENERAL_URL );
                tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );
            }
        }
        return new TableUI( columns );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI getListOfChildJobUITableColumns() {
        final List< TableColumn > columns = GUIUtils.listColumns( JobImpl.class );
        for ( final TableColumn tableColumn : columns ) {
            if ( LOG_SUMMARY_NAME.equalsIgnoreCase( tableColumn.getName() ) ) {
                tableColumn.getRenderer().setUrl( DOWNLOAD_LOG_SUMMARY_URL );
                tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );
            } else if ( LOG_GENERAL_NAME.equalsIgnoreCase( tableColumn.getName() ) ) {
                tableColumn.getRenderer().setUrl( DOWNLOAD_LOG_GENERAL_URL );
                tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );
            }
        }
        return new TableUI( columns );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubTabsItem getTabsViewJobUI( String jobId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( SimuspaceFeaturesEnum.JOBS.getId().equals( jobId ) ) {
                final SuSEntity entity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( jobId ) );
                final List< SubTabsUI > subTabsUIs = new ArrayList<>();
                subTabsUIs.add( new SubTabsUI( null, WORKFLOW_JOBS, MessageBundleFactory.getMessage( Messages.WORKFLOW_JOBS.getKey() ) ) );
                subTabsUIs.add( new SubTabsUI( null, SYSTEM_JOBS, MessageBundleFactory.getMessage( Messages.SYSTEM_JOBS.getKey() ) ) );
                return new SubTabsItem( entity.getComposedId().getId().toString(), entity.getName(), ConstantsInteger.INTEGER_VALUE_ONE,
                        subTabsUIs, null );
            } else {
                return getJobSubTabsItem( jobId, entityManager );
            }
        } finally {
            entityManager.close();
        }
    }

    private SubTabsItem getJobSubTabsItem( String jobId, EntityManager entityManager ) {
        final JobEntity entity = jobDao.getLatestObjectById( entityManager, JobEntity.class, UUID.fromString( jobId ) );
        final List< SubTabsUI > subTabsUIs = new ArrayList<>();
        subTabsUIs.add( new SubTabsUI( null, TAB_NAME_JOB_LOG, MessageBundleFactory.getMessage( Messages.LOG.getKey() ) ) );
        subTabsUIs.add( new SubTabsUI( null, TAB_NAME_PROPERTIES, MessageBundleFactory.getMessage( Messages.PROPERTIES.getKey() ) ) );
        subTabsUIs.add( new SubTabsUI( null, TAB_NAME_PERMISSIONS, MessageBundleFactory.getMessage( Messages.PERMISSIONS.getKey() ) ) );
        if ( !entity.getWorkflowId().equals( UUID.fromString( SystemWorkflow.DELETE.getId() ) ) ) {
            subTabsUIs.add(
                    new SubTabsUI( null, TAB_NAME_DATA_CREATED, MessageBundleFactory.getMessage( Messages.DATA_CREATED.getKey() ) ) );
            subTabsUIs.add(
                    new SubTabsUI( null, TAB_NAME_INPUT_PARAMETERS, MessageBundleFactory.getMessage( Messages.PARAMETERS.getKey() ) ) );
        }
        if ( entity.getJobRelationType() == JobRelationTypeEnums.MASTER.getKey() && entity.getJobType() != JobTypeEnums.WORKFLOW.getKey()
                && entity.getJobType() != JobTypeEnums.SYSTEM.getKey() ) {
            subTabsUIs.add( new SubTabsUI( null, "childjobs", MessageBundleFactory.getMessage( Messages.CHILD_JOBS.getKey() ) ) );
        }
        if ( entity.getJobType() == JobTypeEnums.SCHEME.getKey() ) {
            subTabsUIs.add( new SubTabsUI( null, SCHEME, MessageBundleFactory.getMessage( Messages.SCHEME.getKey() ) ) );
        }
        return new SubTabsItem( entity.getId().toString(), entity.getName(), ConstantsInteger.INTEGER_VALUE_ONE, subTabsUIs, null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Job > getFilteredJobsList( UUID userId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }
            isRestricted( entityManager, userId );
            List< Job > allJobsList = new ArrayList<>();
            replaceEnumsInJobFilters( filter );
            final List< JobEntity > jobEntityList = jobDao.getAllJobsForList( entityManager, userId, filter );
            if ( !CollectionUtil.isNotEmpty( jobEntityList ) ) {
                return PaginationUtil.constructFilteredResponse( filter, allJobsList );
            }
            for ( final JobEntity jobEntity : jobEntityList ) {
                Job preparedJob = prepareJobModelForList( entityManager, jobEntity );
                if ( preparedJob != null ) {
                    allJobsList.add( preparedJob );
                } else {
                    log.error( MessagesUtil.getMessage( Messages.ERROR_IN_PREPARING_JOB_ENTITIES ) );
                }
            }
            return PaginationUtil.constructFilteredResponse( filter, allJobsList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Job > getFilteredSystemJobsList( UUID userId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }
            isRestricted( entityManager, userId );
            replaceEnumsInJobFilters( filter );
            final List< JobEntity > jobEntityList = jobDao.getAllSystemJobsForList( entityManager, userId, filter );
            List< Job > allJobsList = new ArrayList<>();
            if ( !CollectionUtil.isNotEmpty( jobEntityList ) ) {
                return PaginationUtil.constructFilteredResponse( filter, allJobsList );
            }
            for ( final JobEntity jobEntity : jobEntityList ) {
                Job preparedJob = prepareJobModelForList( entityManager, jobEntity );
                if ( preparedJob != null ) {
                    allJobsList.add( preparedJob );
                } else {
                    log.error( MessagesUtil.getMessage( Messages.ERROR_IN_PREPARING_JOB_ENTITIES ) );
                }
            }
            return PaginationUtil.constructFilteredResponse( filter, allJobsList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Job > getFilteredChildJobsList( UUID id, UUID userId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( !String.valueOf( userId ).equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager,
                    String.valueOf( userId ), id + ConstantsString.COLON + PermissionMatrixEnum.READ.getValue() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_READ.getKey(), OBJECT ) );
            }
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }
            List< Job > allJobsList = new ArrayList<>();
            replaceEnumsInJobFilters( filter );
            final List< JobEntity > jobEntityList = jobDao.getAllFilteredChildrenOfMasterJob( entityManager, filter, id );
            if ( CollectionUtil.isNotEmpty( jobEntityList ) ) {
                for ( final JobEntity jobEntity : jobEntityList ) {
                    allJobsList.add( prepareJobModel( entityManager, jobEntity, Boolean.FALSE ) );
                }
            }
            return PaginationUtil.constructFilteredResponse( filter, allJobsList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare master job for scheme.
     *
     * @param entityManager
     *         the entity manager
     * @param jobEntity
     *         the job entity
     *
     * @return the job impl
     */
    private JobImpl prepareJob( EntityManager entityManager, JobEntity jobEntity ) {
        JobImpl job = prepareJobModel( entityManager, jobEntity, Boolean.FALSE );
        if ( jobEntity.getJobType() == JobTypeEnums.WORKFLOW.getKey() || jobEntity.getJobType() == JobTypeEnums.SYSTEM.getKey() ) {
            return job;
        }
        if ( job != null ) {
            if ( job.getStatus().getId() == WorkflowStatus.COMPLETED.getKey() ) {
                job.setProgress( getCompletedProgressBar() );
                return job;
            }
            jobEntity.setStatus( job.getStatus().getId() );
            if ( jobEntity.isAutoDelete() != null ) {
                job.setAutoDelete( jobEntity.isAutoDelete() );
            }
        }
        return job;
    }

    /**
     * {@inheritDoc}
     *
     * @param entityManager
     *         the entity manager
     * @param jobEntity
     *         the job entity
     *
     * @return the job
     */
    private JobImpl prepareJobModelForList( EntityManager entityManager, JobEntity jobEntity ) {
        JobImpl jobImpl = null;
        if ( jobEntity != null ) {
            jobImpl = new JobImpl();

            // basic attributes
            jobImpl.setId( jobEntity.getId() );
            jobImpl.setName( jobEntity.getName() );
            jobImpl.setDescription( jobEntity.getDescription() );
            jobImpl.setJobProcessId( jobEntity.getPid() );
            jobImpl.setSubmitTime( jobEntity.getCreatedOn() );
            jobImpl.setCompletionTime( jobEntity.getFinishedOn() );
            jobImpl.setOs( jobEntity.getOs() );
            jobImpl.setMachine( jobEntity.getMachine() );
            jobImpl.setStatus( new Status( WorkflowStatus.getById( jobEntity.getStatus() ) ) );
            jobImpl.setRunMode( RunMode.getById( jobEntity.getRunMode() ).getKey() );

            // location
            if ( jobEntity.getRunsOn() != null ) {
                LocationDTO locationDTO = locationManager.prepareDTO( jobEntity.getRunsOn() );
                jobImpl.setRunsOn( locationDTO );
            }

            // user
            if ( jobEntity.getCreatedBy() != null ) {
                jobImpl.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( jobEntity.getCreatedBy() ) );
            }

            // working dir
            final EngineFile file = new EngineFile();
            file.setPath( jobEntity.getJobDirectory() );
            file.setItems( new ArrayList<>( Collections.singletonList( jobEntity.getJobDirectory() ) ) );
            jobImpl.setWorkingDir( file );

            // progress bar
            ProgressBar progres = new ProgressBar();
            if ( jobEntity.getTotalElements() != null ) {
                progres.setTotal( jobEntity.getTotalElements() );
            }
            if ( jobEntity.getCompletedElements() != null ) {
                progres.setCompleted( jobEntity.getCompletedElements() );
            }
            if ( ( jobEntity.getWorkflowId() != null ) && (
                    jobEntity.getWorkflowId().equals( UUID.fromString( SystemWorkflow.DELETE.getId() ) ) || jobEntity.getWorkflowId()
                            .equals( UUID.fromString( SystemWorkflow.RESTORE.getId() ) ) ) && jobImpl.getStatus().getName()
                    .equals( COMPLETED ) ) {
                progres.setTotal( 1 );
                progres.setCompleted( 1 );
            }
            jobImpl.setProgress( progres );

            // workflow
            if ( ( jobEntity.getWorkflow() != null ) && ( jobEntity.getWorkflow().getComposedId() != null ) ) {
                jobImpl.setWorkflowName( jobEntity.getWorkflow().getName() );
                jobImpl.setWorkflowId( jobEntity.getWorkflow().getComposedId().getId() );
                jobImpl.setWorkflowVersion( new VersionDTO( jobEntity.getWorkflow().getVersionId() ) );
            }

            // job number
            JobIdsEntity jobIdsEntity = jobIdsDAO.getObjectByProperty( entityManager, JobIdsEntity.class, "uuid", jobEntity.getId() );
            if ( jobIdsEntity != null ) {
                jobImpl.setJobInteger( jobIdsEntity.getId() );
            }
        }
        return jobImpl;
    }

    /**
     * Prepare personal job dto personal job dto.
     *
     * @param entityManager
     *         the entity manager
     * @param jobEntity
     *         the job entity
     *
     * @return the personal job dto
     */
    private UserJobDTO prepareUserJobDTO( EntityManager entityManager, JobEntity jobEntity ) {
        UserJobDTO userJobDTO = null;
        if ( jobEntity != null ) {
            userJobDTO = new UserJobDTO();

            // basic attributes
            userJobDTO.setName( jobEntity.getName() );
            userJobDTO.setStatus( new Status( WorkflowStatus.getById( jobEntity.getStatus() ) ) );

            // progress bar
            ProgressBar progres = new ProgressBar();
            if ( jobEntity.getTotalElements() != null ) {
                progres.setTotal( jobEntity.getTotalElements() );
            }
            if ( jobEntity.getCompletedElements() != null ) {
                progres.setCompleted( jobEntity.getCompletedElements() );
            }
            if ( ( jobEntity.getWorkflowId() != null ) && (
                    jobEntity.getWorkflowId().equals( UUID.fromString( SystemWorkflow.DELETE.getId() ) ) || jobEntity.getWorkflowId()
                            .equals( UUID.fromString( SystemWorkflow.RESTORE.getId() ) ) ) && userJobDTO.getStatus().getName()
                    .equals( COMPLETED ) ) {
                progres.setTotal( 1 );
                progres.setCompleted( 1 );
            }
            userJobDTO.setProgress( progres );
            // job number
            JobIdsEntity jobIdsEntity = jobIdsDAO.getObjectByProperty( entityManager, JobIdsEntity.class, "uuid", jobEntity.getId() );
            if ( jobIdsEntity != null ) {
                userJobDTO.setJobInteger( jobIdsEntity.getId() );
            }
        }
        return userJobDTO;
    }

    /**
     * Get completed progress.
     *
     * @return the completed progress bar
     */
    private ProgressBar getCompletedProgressBar() {
        final ProgressBar progres = new ProgressBar();
        progres.setTotal( 1 );
        progres.setCompleted( 1 );
        return progres;
    }

    /**
     * Updates filters that use enums so filters can be used in database queries. Replaces enum values by db values
     *
     * @param filterDTO
     *         the filterDTO
     */
    private void replaceEnumsInJobFilters( FiltersDTO filterDTO ) {
        if ( filterDTO.getColumns() != null ) {
            for ( FilterColumn filterColumn : filterDTO.getColumns() ) {
                if ( "status".equals( filterColumn.getName() ) && filterColumn.getFilters() != null ) {
                    updateStatusFilter( filterColumn );
                }

                if ( "runMode".equals( filterColumn.getName() ) && filterColumn.getFilters() != null ) {
                    updateRunModeFilter( filterColumn );
                }
            }
        }
    }

    /**
     * Updates status filter so that filter can be used in database queries. Replaces enum values by db values
     *
     * @param filterColumn
     *         the filter column
     */
    private void updateStatusFilter( FilterColumn filterColumn ) {
        for ( Filter filter : filterColumn.getFilters() ) {
            WorkflowStatus status = WorkflowStatus.getByValue( filter.getValue() );
            if ( status != null ) {
                filter.setValue( Integer.toString( status.getKey() ) );
            } else {
                filter.setValue( "-1" );
            }
        }
    }

    /**
     * Updates runmode filter so that filter can be used in database queries. Replaces enum values by db values
     *
     * @param filterColumn
     *         the filter column
     */
    private void updateRunModeFilter( FilterColumn filterColumn ) {
        for ( Filter filter : filterColumn.getFilters() ) {
            RunMode runMode = RunMode.getByValue( filter.getValue() );
            if ( runMode != null ) {
                filter.setValue( Integer.toString( runMode.getId() ) );
            } else {
                filter.setValue( "-1" );
            }
        }
    }

    /**
     * Get all childrens of master job i.e assembly jobs, solver jobs, post jobs.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     * @param jobType
     *         the job type
     *
     * @return allChildren all childrens of master job
     */
    private List< JobEntity > getAllChildrensOfMasterJob( EntityManager entityManager, UUID id, int jobType ) {
        List< JobEntity > allChildren = new ArrayList<>();
        if ( JobTypeEnums.VARIANT.getKey() == jobType ) {
            allChildren = getAllChildrensOfVariantMasterJobByJpa( entityManager, id );
        } else {
            List< JobEntity > childList = jobDao.getAllChildrenOfMasterJob( entityManager, id );
            allChildren.addAll( childList );
        }
        return allChildren;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Job > getAllChildrensOfMasterJob( Job job ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getAllChildrensOfMasterJob( entityManager, job );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Job > getAllChildrensOfMasterJob( EntityManager entityManager, Job job ) {
        if ( job.getJobType() == JobTypeEnums.VARIANT.getKey() ) {
            return getAllChildrensOfVariantMasterJob( entityManager, job.getId() ).stream()
                    .map( entity -> prepareJobModel( entityManager, entity, Boolean.FALSE ) ).collect( Collectors.toList() );
        } else {
            return jobDao.getAllChildrenOfMasterJob( entityManager, job.getId() ).stream()
                    .map( entity -> prepareJobModel( entityManager, entity, Boolean.FALSE ) ).collect( Collectors.toList() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< UserJobDTO > getUserJobsList( String token, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }
            UUID userId = UUID.fromString( TokenizedLicenseUtil.getUser( token ).getId() );
            isRestricted( entityManager, userId );
            List< UserJobDTO > allUserJobs = new ArrayList<>();
            replaceEnumsInJobFilters( filter );
            final List< JobEntity > minimalJobsList = jobDao.getAllUserJobsMinimal( entityManager, userId, filter );
            if ( CollectionUtil.isNotEmpty( minimalJobsList ) ) {
                for ( final JobEntity jobEntity : minimalJobsList ) {
                    var userJobDTO = prepareUserJobDTO( entityManager, jobEntity );
                    if ( userJobDTO != null ) {
                        allUserJobs.add( userJobDTO );
                    } else {
                        log.error( MessagesUtil.getMessage( Messages.ERROR_IN_PREPARING_JOB_ENTITIES ) );
                    }
                }
            }
            return PaginationUtil.constructFilteredResponse( filter, allUserJobs );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Get all childrens of variant master job i.e assembly jobs, solver jobs, post jobs.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return allChildren all childrens of variant master job by jpa
     */
    private List< JobEntity > getAllChildrensOfVariantMasterJobByJpa( EntityManager entityManager, UUID id ) {
        List< JobEntity > allChildren = new ArrayList<>();

        List< JobEntity > assemblyList = jobDao.getAllChildrenOfMasterJob( entityManager, id );
        List< JobEntity > solveList = new ArrayList<>();
        List< JobEntity > postList = new ArrayList<>();
        assemblyList.stream().forEach( assembly -> {
            List< JobEntity > solve = jobDao.getAllChildrenOfMasterJob( entityManager, assembly.getId() );
            solveList.addAll( solve );
        } );
        solveList.stream().forEach( solve -> {
            List< JobEntity > post = jobDao.getAllChildrenOfMasterJob( entityManager, solve.getId() );
            postList.addAll( post );
        } );
        allChildren.addAll( assemblyList );
        allChildren.addAll( solveList );
        allChildren.addAll( postList );

        return allChildren;
    }

    /**
     * Get all childrens of variant master job i.e assembly jobs, solver jobs, post jobs.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return allChildren all childrens of variant master job
     */
    private List< JobEntity > getAllChildrensOfVariantMasterJob( EntityManager entityManager, UUID id ) {
        List< JobEntity > allChildren = new ArrayList<>();

        List< JobEntity > assemblyList = jobDao.getAllChildrenOfMasterJob( entityManager, id );
        List< JobEntity > solveList = new ArrayList<>();
        List< JobEntity > postList = new ArrayList<>();
        assemblyList.stream().forEach( assembly -> {
            List< JobEntity > solve = jobDao.getAllChildrenOfMasterJob( entityManager, assembly.getId() );
            solveList.addAll( solve );
        } );
        solveList.stream().forEach( solve -> {
            List< JobEntity > post = jobDao.getAllChildrenOfMasterJob( entityManager, solve.getId() );
            postList.addAll( post );
        } );
        allChildren.addAll( assemblyList );
        allChildren.addAll( solveList );
        allChildren.addAll( postList );

        return allChildren;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< LogRecord > getFilteredJobLogList( UUID userId, String jobId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final List< LogRecord > list = new ArrayList<>();
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }

            final Job job = getJob( entityManager, userId, jobId );

            if ( null != job && null != job.getLog() ) {
                list.addAll( job.getLog() );
            }

            List< LogRecord > logList = addLogSearch( list, filter );
            List< LogRecord > filteredLogList = addTypeFilter( logList, filter );

            filter.setFilteredRecords( ( long ) filteredLogList.size() );
            filter.setTotalRecords( ( long ) list.size() );

            return PaginationUtil.constructFilteredResponse( filter, addLogPaging( filteredLogList, filter ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Adds type filters.
     *
     * @param logList
     *         the log List
     * @param filter
     *         the filter
     *
     * @return logList list
     */
    private List< LogRecord > addTypeFilter( List< LogRecord > logList, FiltersDTO filter ) {
        for ( FilterColumn colm : filter.getColumns() ) {
            if ( "Type".equalsIgnoreCase( colm.getName() ) && colm.getFilters() != null && !colm.getFilters().isEmpty() ) {
                return logList.stream().filter( log -> ( isTypeFiltersTrueForLog( log, colm.getFilters() ) ) ).toList();
            }
        }

        return logList;
    }

    /**
     * Checks if type filters true for log record.
     *
     * @param log
     *         the log
     * @param typeColumnFilters
     *         the type Column Filters
     *
     * @return if filters true for log
     */
    private boolean isTypeFiltersTrueForLog( LogRecord log, List< Filter > typeColumnFilters ) {
        boolean allFiltersStatus = false;

        int filterNum = 1;
        for ( Filter filter : typeColumnFilters ) {
            if ( filter.getOperator().equalsIgnoreCase( CONTAINS ) ) {
                boolean filterStatus = log.getType().toLowerCase().contains( filter.getValue().toLowerCase() );
                allFiltersStatus = addFilterStatusToAllFilters( filterNum, filterStatus, filter.getCondition(), allFiltersStatus );

            } else if ( filter.getOperator().equalsIgnoreCase( DOES_NOT_CONTAIN ) ) {
                boolean filterStatus = !( log.getType().toLowerCase().contains( filter.getValue().toLowerCase() ) );
                allFiltersStatus = addFilterStatusToAllFilters( filterNum, filterStatus, filter.getCondition(), allFiltersStatus );

            } else if ( filter.getOperator().equalsIgnoreCase( EQUALS ) ) {
                boolean filterStatus = log.getType().equals( filter.getValue() );
                allFiltersStatus = addFilterStatusToAllFilters( filterNum, filterStatus, filter.getCondition(), allFiltersStatus );

            } else if ( filter.getOperator().equalsIgnoreCase( NOT_EQUALS ) ) {
                boolean filterStatus = !( log.getType().equals( filter.getValue() ) );
                allFiltersStatus = addFilterStatusToAllFilters( filterNum, filterStatus, filter.getCondition(), allFiltersStatus );

            } else if ( filter.getOperator().equalsIgnoreCase( BEGINS_WITH ) ) {
                boolean filterStatus = ( log.getType().startsWith( filter.getValue() ) );
                allFiltersStatus = addFilterStatusToAllFilters( filterNum, filterStatus, filter.getCondition(), allFiltersStatus );

            } else if ( filter.getOperator().equalsIgnoreCase( ENDS_WITH ) ) {
                boolean filterStatus = ( log.getType().endsWith( filter.getValue() ) );
                allFiltersStatus = addFilterStatusToAllFilters( filterNum, filterStatus, filter.getCondition(), allFiltersStatus );

            }

            filterNum++;
        }

        return allFiltersStatus;
    }

    /**
     * Adds filter status to all filter status.
     *
     * @param filterNum
     *         the filterNum
     * @param filterStatus
     *         the filterStatus
     * @param filterCondition
     *         the filter condition
     * @param allFilterStatus
     *         the all filter status
     *
     * @return searched items list
     */
    private boolean addFilterStatusToAllFilters( int filterNum, boolean filterStatus, String filterCondition, boolean allFilterStatus ) {
        if ( 1 == filterNum ) {
            allFilterStatus = filterStatus;
        } else if ( "OR".equals( filterCondition ) ) {
            allFilterStatus = allFilterStatus || filterStatus;
        } else if ( "AND".equals( filterCondition ) ) {
            allFilterStatus = allFilterStatus && filterStatus;
        }

        return allFilterStatus;
    }

    /**
     * Performs search on a List of Log Records and returns list with searched items only.
     *
     * @param list
     *         the list
     * @param filter
     *         the filter
     *
     * @return searched items list
     */
    private List< LogRecord > addLogSearch( List< LogRecord > list, FiltersDTO filter ) {

        if ( !filter.getSearch().isEmpty() ) {
            list = list.stream()
                    .filter( x -> org.apache.commons.lang3.StringUtils.containsIgnoreCase( x.getLogMessage(), filter.getSearch() ) )
                    .toList();
        }

        return list;
    }

    /**
     * Performs paging on logs list and returns list with items of current page only.
     *
     * @param list
     *         the list
     * @param filter
     *         the filter
     *
     * @return paged items list
     */
    private List< LogRecord > addLogPaging( List< LogRecord > list, FiltersDTO filter ) {
        List< LogRecord > pageList = new ArrayList<>();

        // Getting list for current page only
        if ( filter.getStart() < list.size() ) {
            pageList = list.subList( filter.getStart(),
                    ( list.size() - filter.getStart() ) <= filter.getLength() ? list.size() : ( filter.getStart() + filter.getLength() ) );
        }

        return pageList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< GenericDTO > getFilteredJobDataCreatedObjectsList( UUID userId, String jobId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( !String.valueOf( userId ).equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager,
                    String.valueOf( userId ), jobId + ConstantsString.COLON + PermissionMatrixEnum.READ.getValue() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_READ.getKey(), OBJECT ) );
            }
            final List< GenericDTO > list = new ArrayList<>();
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }
            List< SuSEntity > suSEntityEntries;
            JobEntity job = jobDao.getLatestNonDeletedObjectById( entityManager, UUID.fromString( jobId ) );
            if ( job.getJobRelationType().equals( JobRelationTypeEnums.MASTER.getKey() ) && job.getJobType()
                    .equals( JobTypeEnums.VARIANT.getKey() ) ) {
                List< JobEntity > child = getAllChildrensOfVariantMasterJobByJpa( entityManager, UUID.fromString( jobId ) );
                suSEntityEntries = susDAO.getAllFilteredRecordsByProperty( entityManager, SuSEntity.class, ConstantsDAO.JOB_ID,
                        ConstantsDAO.JOB_ID, job.getId(), filter, false, configManager.getAllTypesFromConfiguration() );
                for ( JobEntity jobEntity : child ) {
                    suSEntityEntries.addAll( susDAO.getAllFilteredRecordsByProperty( entityManager, SuSEntity.class, ConstantsDAO.JOB_ID,
                            ConstantsDAO.JOB_ID, jobEntity.getId(), filter, false, configManager.getAllTypesFromConfiguration() ) );
                }
            } else {
                suSEntityEntries = susDAO.getAllFilteredRecordsByProperty( entityManager, SuSEntity.class, ConstantsDAO.JOB_ID,
                        ConstantsDAO.JOB_ID, UUID.fromString( jobId ), filter, false, configManager.getAllTypesFromConfiguration() );
            }
            filter.setTotalRecords( filter.getTotalRecords() );
            filter.setFilteredRecords( filter.getFilteredRecords() );
            if ( CollectionUtils.isNotEmpty( suSEntityEntries ) ) {
                for ( final SuSEntity suSEntity : suSEntityEntries ) {
                    list.add( createGenericDTOFromSusEntity( suSEntity ) );
                }
            }
            return PaginationUtil.constructFilteredResponse( filter, list );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the job data created objects list.
     *
     * @param userId
     *         the user id
     * @param jobId
     *         the job id
     *
     * @return the job data created objects list
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.server.manager.JobManager#getJobDataCreatedObjectsList(java.util.UUID, java.lang.String)
     */
    @Override
    public List< SuSEntity > getJobDataCreatedObjectsList( UUID userId, String jobId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getJobDataCreatedObjectsList( entityManager, userId, jobId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSEntity > getJobDataCreatedObjectsList( EntityManager entityManager, UUID userId, String jobId ) {
        List< SuSEntity > requiredObjects = new ArrayList<>();

        final List< SuSEntity > suSEntityEntries = susDAO.getNonDeletedObjectListByProperty( entityManager, SuSEntity.class,
                ConstantsDAO.JOB_ID, UUID.fromString( jobId ) );

        if ( CollectionUtils.isNotEmpty( suSEntityEntries ) ) {
            for ( SuSEntity suSEntity : suSEntityEntries ) {
                if ( suSEntity != null && suSEntity.getJobId().equals( UUID.fromString( jobId ) ) ) {
                    requiredObjects.add( suSEntity );
                }
            }
        }

        return requiredObjects;
    }

    @Override
    public List< String > getCreatedDataObjectsIdsForJobAndChildren( UUID userId, String jobId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getCreatedDataObjectsIdsForJobAndChildren( entityManager, userId, jobId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets created data objects ids for job and children.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param jobId
     *         the job id
     *
     * @return the created data objects ids for job and children
     */
    private List< String > getCreatedDataObjectsIdsForJobAndChildren( EntityManager entityManager, UUID userId, String jobId ) {
        JobEntity jobEntity = getJobById( entityManager, UUID.fromString( jobId ) );
        List< String > createdObjects = getCreatedDataObjectsIdsForJob( entityManager, UUID.fromString( jobId ) );
        if ( jobEntity.getJobRelationType() == JobRelationTypeEnums.MASTER.getKey()
                && jobEntity.getJobType() != JobTypeEnums.WORKFLOW.getKey() && jobEntity.getJobType() != JobTypeEnums.SYSTEM.getKey() ) {
            List< JobEntity > allChildren = getAllChildrensOfMasterJob( entityManager, jobEntity.getId(), jobEntity.getJobType() );
            for ( JobEntity child : allChildren ) {
                createdObjects.addAll( getCreatedDataObjectsIdsForJob( entityManager, child.getId() ) );
            }
        }
        return createdObjects;
    }

    /**
     * Gets data created objects for job.
     *
     * @param entityManager
     *         the entity manager
     * @param jobId
     *         the job id
     *
     * @return the job data created objects ids list
     */
    private List< String > getCreatedDataObjectsIdsForJob( EntityManager entityManager, UUID jobId ) {
        List< String > requiredObjects = new ArrayList<>();
        final List< SuSEntity > suSEntityEntries = susDAO.getNonDeletedObjectListByProperty( entityManager, SuSEntity.class,
                ConstantsDAO.JOB_ID, jobId );
        if ( CollectionUtils.isNotEmpty( suSEntityEntries ) ) {
            for ( SuSEntity suSEntity : suSEntityEntries ) {
                if ( suSEntity != null && suSEntity.getJobId().equals( jobId ) ) {
                    requiredObjects.add( suSEntity.getComposedId().getId().toString() );
                }
            }
        }
        return requiredObjects;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI getJobPermissionUITable( UUID userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final List< TableColumn > columnsList = workflowManager.workflowPermissionTableUI();
            final TableUI tableUI = new TableUI();
            tableUI.setColumns( columnsList );
            tableUI.setViews( objectViewManager.getUserObjectViewsByKey( entityManager, ConstantsObjectViewKey.PERMISSION_TABLE_KEY,
                    userId.toString(), null ) );
            return tableUI;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< ManageObjectDTO > showPermittedUsersAndGroupsForObject( FiltersDTO filter, UUID jobId, UUID userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( !String.valueOf( userId ).equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager,
                    String.valueOf( userId ), jobId + ConstantsString.COLON + PermissionMatrixEnum.READ.getValue() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_READ.getKey(), OBJECT ) );
            }
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_OBJECT.getKey() ) );
            }
            final boolean isManageable = permissionManager.isManageable( entityManager, userId, jobId );
            final List< ManageObjectDTO > objectManagerDTOs = permissionManager.prepareObjectManagerDTOs( entityManager, jobId,
                    isManageable, filter );

            filter.setFilteredRecords( ( long ) objectManagerDTOs.size() );
            filter.setTotalRecords( ( ( long ) objectManagerDTOs.size() ) );

            List< ManageObjectDTO > filteredList = objectManagerDTOs.subList( filter.getStart(),
                    ( objectManagerDTOs.size() - filter.getStart() ) <= filter.getLength() ? objectManagerDTOs.size()
                            : ( filter.getStart() + filter.getLength() ) );

            return PaginationUtil.constructFilteredResponse( filter, filteredList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getJobDataCreatedTableUI() {

        final List< TableColumn > columns = GUIUtils.listColumns( GenericDTO.class );
        Map< String, String > nameUrlValues = new HashMap<>();
        nameUrlValues.put( ConstantsString.OBJECT_KEY, ConstantsUrlViews.DATA_OBJECT_VIEW );
        nameUrlValues.put( ConstantsString.PROJECT_KEY, ConstantsUrlViews.DATA_PROJECT_VIEW );
        GUIUtils.setLinkColumn( DataObjectDTO.DATAOBJECT_NAME, URL_TYPE, nameUrlValues, columns );
        return columns;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean permitPermissionToJob( CheckBox checkBox, UUID objectId, UUID securityId, String userIdFromGeneralHeader ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return permissionManager.permitPermissionToObject( entityManager, checkBox, objectId, securityId, userIdFromGeneralHeader );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getContextRouter( UUID userIdFromGeneralHeader, FiltersDTO filter, Class< ? > clazz, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            boolean isMultiSelect = false;
            final List< Object > selectedIds = filter.getItems();
            if ( 1 < selectedIds.size() ) {
                isMultiSelect = true;
            }
            List< ContextMenuItem > contextMenuItem = new ArrayList<>();
            if ( CollectionUtil.isNotEmpty( selectedIds ) ) {
                final Job job = getJob( entityManager, userIdFromGeneralHeader, ( String ) selectedIds.get( 0 ) );
                if ( job.getJobType() != JobTypeEnums.SYSTEM.getKey() && job.getStatus().getId() != WorkflowStatus.DISCARD.getKey() ) {
                    contextMenuItem = prepareContextMenu( entityManager, filter, clazz, selectedIds, job, token, isMultiSelect );
                    if ( job.getRunMode().equalsIgnoreCase( RunMode.LOCAL.getKey() ) ) {
                        contextMenuItem = contextMenuManager.setContextVisibilityToClientOnly( contextMenuItem );
                    }
                }
            }
            return contextMenuItem;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare context menu.
     *
     * @param entityManager
     *         the entity manager
     * @param filter
     *         the filter
     * @param clazz
     *         the clazz
     * @param selectedIds
     *         the selected ids
     * @param job
     *         the entity
     * @param token
     *         the token
     * @param isMultiSelect
     *         the is multi select
     *
     * @return the list
     */
    private List< ContextMenuItem > prepareContextMenu( EntityManager entityManager, FiltersDTO filter, Class< ? > clazz,
            final List< Object > selectedIds, final Job job, String token, boolean isMultiSelect ) {
        List< ContextMenuItem > contextMenuItem = new ArrayList<>();
        if ( !isMultiSelect ) {
            if ( ( job.getStatus().getId() == WorkflowStatus.RUNNING.getKey() ) ) {
                contextMenuItem = contextMenuManager.getRunningJobContext( job.getId() );
                if ( isWorkflowJob( job ) && OSValidator.isUnix() ) {
                    contextMenuManager.addJobPauseOption( contextMenuItem, job.getId() );
                }
            } else if ( job.getStatus().getId() == WorkflowStatus.PAUSED.getKey() ) {
                contextMenuItem = contextMenuManager.getRunningJobContext( job.getId() );
                contextMenuManager.addJobResumeOption( contextMenuItem, job.getId() );
            } else if ( ( ConstantsInteger.INTEGER_VALUE_ONE == selectedIds.size() ) && isWorkflowJob( job ) ) {
                final List< ContextMenuItem > contextMenu = contextMenuManager.getContextMenu( entityManager, PLUGIN_OBJECT, clazz,
                        filter );

                for ( final ContextMenuItem contextMenuItem2 : contextMenu ) {
                    if ( contextMenuItem2.getTitle().equals( MessageBundleFactory.getMessage( RE_RUN_JOB ) )
                            && job.getJobType() != JobTypeEnums.SCHEME.getKey() && job.getJobType() != JobTypeEnums.VARIANT.getKey() ) {
                        contextMenuItem.add( contextMenuItem2 );
                    }
                    if ( contextMenuItem2.getTitle().equals( MessageBundleFactory.getMessage( DISCARD_JOB ) )
                            && job.getStatus().getId() != WorkflowStatus.DISCARD.getKey()
                            && job.getStatus().getId() != WorkflowStatus.RUNNING.getKey() && !job.isAutoDelete() ) {
                        contextMenuItem.add( contextMenuItem2 );
                    }

                }
            }

            if ( ( job.getJobType() == JobTypeEnums.SCHEME.getKey() || job.getJobType() == JobTypeEnums.WORKFLOW.getKey()
                    || job.getJobType() == JobTypeEnums.VARIANT.getKey() ) && ( ConstantsInteger.INTEGER_VALUE_ONE
                    == selectedIds.size() ) ) {

                boolean jobTypeScheme = false;
                // adding scheme related context for job
                if ( job.getJobType() == JobTypeEnums.SCHEME.getKey() ) {
                    jobTypeScheme = true;
                    if ( !job.isAutoDelete() ) {
                        List< ContextMenuItem > contextGenerateImage = contextMenuManager.getContextGenerateImageMenu(
                                filter.getItems().get( ConstantsInteger.INTEGER_VALUE_ZERO ).toString() );
                        contextMenuItem.addAll( contextGenerateImage );
                    }
                }

                List< ContextMenuItem > contextMenuPlot = contextMenuManager.getContextJobPlotingAndJobLogDownload( PLUGIN_OBJECT,
                        DesignPlotingConfig.class, filter, token, jobTypeScheme );
                if ( !job.isAutoDelete() ) {
                    contextMenuItem.addAll( contextMenuPlot );
                }
            }
        }
        return contextMenuItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Object > getSingleJobSchemeData( String userId, String id, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                    id + ConstantsString.COLON + PermissionMatrixEnum.READ.getValue() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_READ.getKey(), OBJECT ) );
            }
            JobEntity masterJob = jobDao.getLatestNonDeletedObjectById( entityManager, UUID.fromString( id ) );
            List< Object > columnsList = new ArrayList<>();

            List< JobEntity > assemblyList = jobDao.getAllChildrenOfMasterJob( entityManager, UUID.fromString( id ) );

            for ( JobEntity jobEntity : assemblyList ) {
                Map< String, String > outResult = new HashMap<>();

                if ( jobEntity.getResultSummary() != null ) {
                    SchemeSummaryResults schemeSummaryResults = JsonUtils.jsonToObject(
                            ByteUtil.convertByteToString( jobEntity.getResultSummary() ), SchemeSummaryResults.class );

                    if ( schemeSummaryResults != null ) {
                        outResult.put( EXP_NUM,
                                ( schemeSummaryResults.getExperimentNumber() != null ? schemeSummaryResults.getExperimentNumber() : " " ) );
                    }
                    prepareOutResult( entityManager, jobEntity, outResult, schemeSummaryResults, masterJob );
                    columnsList.add( outResult );
                }
            }
            filter.setFilteredRecords( ( long ) columnsList.size() );
            filter.setTotalRecords( ( long ) columnsList.size() );

            sortExperimentsByAscendingOrder( columnsList );

            return PaginationUtil.constructFilteredResponse( filter, PaginationUtil.getPaginatedList( columnsList, filter ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare out result.
     *
     * @param entityManager
     *         the entity manager
     * @param jobEntity
     *         the job entity
     * @param outResult
     *         the out result
     * @param schemeSummaryResults
     *         the scheme summary results
     * @param masterJob
     *         the master job
     */
    private void prepareOutResult( EntityManager entityManager, JobEntity jobEntity, Map< String, String > outResult,
            SchemeSummaryResults schemeSummaryResults, JobEntity masterJob ) {
        Map< String, Object > designSummaryMap = schemeSummaryResults.getDesignSummaryMap();
        Map< String, Object > objectSummaryMap = schemeSummaryResults.getObjectSummaryMap();
        Map< String, Object > goalSummaryList = schemeSummaryResults.getGoal();
        if ( null == designSummaryMap || designSummaryMap.isEmpty() ) {
            List< DesignVariableEntity > list = variableDAO.getAllDesignVariables( entityManager,
                    jobEntity.getWorkflow().getComposedId().getId().toString(), jobEntity.getCreatedBy().getId().toString() );
            for ( DesignVariableEntity designVariableEntity : list ) {
                outResult.put( designVariableEntity.getLabel(), ConstantsString.EMPTY_STRING );
            }
        } else {
            for ( Map.Entry< String, Object > objective : designSummaryMap.entrySet() ) {
                outResult.put( objective.getKey(),
                        ( objective.getValue() != null ? objective.getValue().toString() : ConstantsString.SPACE ) );
            }
        }
        if ( null == objectSummaryMap || objectSummaryMap.isEmpty() ) {
            List< ObjectiveVariableEntity > objectiveVariableEntity = variableDAO.getAllObjectiveVariablesByWorkflowId( entityManager,
                    jobEntity.getWorkflow().getComposedId().getId().toString() );
            for ( ObjectiveVariableEntity objectiveVariable : objectiveVariableEntity ) {
                outResult.put( objectiveVariable.getLabel(), ConstantsString.EMPTY_STRING );
            }
        } else {
            for ( Map.Entry< String, Object > objective : objectSummaryMap.entrySet() ) {
                outResult.put( objective.getKey(),
                        ( objective.getValue() != null ? objective.getValue().toString() : ConstantsString.SPACE ) );
            }
        }

        if ( masterJob != null && masterJob.getJobSchemeCategory() == SchemeCategoryEnum.OPTIMIZATION.getKey() && goalSummaryList != null
                && goalSummaryList.containsKey( COMBINED_GOAL ) && goalSummaryList.get( COMBINED_GOAL ) != null ) {
            outResult.put( COMBINED_GOAL, String.valueOf( goalSummaryList.get( COMBINED_GOAL ) ) );
        }
    }

    /**
     * Sorts list by Experiment number in ascending order.
     *
     * @param list
     *         the list
     */
    private void sortExperimentsByAscendingOrder( List< Object > list ) {
        list.sort( Comparator.comparing( o -> new BigDecimal( ( ( Map< String, String > ) o ).get( EXP_NUM ) ) ) );
    }

    /**
     * Genrate CSV file by job id.
     *
     * @param id
     *         the id
     * @param token
     *         the token
     *
     * @return true, if successful
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Override
    public String generateCSVFileByJobId( String userId, String id, String token ) throws IOException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return generateCSVFileByJobId( entityManager, userId, id, token );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Genrate CSV file by job id.
     *
     * @param id
     *         the id
     * @param token
     *         the token
     *
     * @return true, if successful
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Override
    public String generateCSVFileByJobId( EntityManager entityManager, String userId, String id, String token ) throws IOException {
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                id + ConstantsString.COLON + PermissionMatrixEnum.READ.getValue() ) ) {
            throw new SusException( "You don't have permission to Read CSV file" );
        }

        List< JobEntity > assemblyList = jobDao.getAllChildrenOfMasterJob( entityManager, UUID.fromString( id ) );
        List< JobEntity > sortedChildJobs = assemblyList.stream().sorted( Comparator.comparing( JobEntity::getCreatedOn ) ).toList();
        List< List< String > > rows = new ArrayList<>();
        prepareCSVFileSummaryDataFromJob( sortedChildJobs, rows, true );
        // csv file generated in staging with job id
        File file = generateCSVFileInTempWithJobId( entityManager, id, rows );
        return file.getPath();
    }

    @Override
    public String generateAndCopyJobSummaryCSVByJobId( String userId, String userUID, String fromJobId, String toJobId )
            throws IOException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< JobEntity > assemblyList = jobDao.getAllChildrenOfMasterJob( entityManager, UUID.fromString( fromJobId ) );
            List< JobEntity > sortedChildJobs = assemblyList.stream().sorted( Comparator.comparing( JobEntity::getCreatedOn ) ).toList();
            List< List< String > > rows = new ArrayList<>();
            prepareCSVFileSummaryDataFromJob( sortedChildJobs, rows, true );
            // csv file genrated in staging with job id
            File file = generateCSVFileInStagingWithJobId( entityManager, toJobId, rows );
            return file.getPath();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the single job scheme UI.
     *
     * @param id
     *         the id
     *
     * @return the single job scheme UI
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.server.manager.JobManager#getSingleJobSchemeUI(java.lang.String)
     */
    @Override
    public List< TableColumn > getSingleJobSchemeUI( String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< TableColumn > columnsList = new ArrayList<>();
            List< JobEntity > assemblyList = jobDao.getAllChildrenOfMasterJob( entityManager, UUID.fromString( id ) );

            JobEntity job;
            if ( !assemblyList.isEmpty() ) {
                job = assemblyList.get( 0 );
            } else {
                return columnsList; // return empty when data not available
            }

            columnsList.add( prepareExpNumColumn() );
            int orderNumber = ConstantsInteger.INTEGER_VALUE_ONE;

            SchemeSummaryResults schemeSummaryResults = new SchemeSummaryResults();
            if ( job.getResultSummary() != null && 0 < job.getResultSummary().length ) {
                schemeSummaryResults = JsonUtils.jsonToObject( ByteUtil.convertByteToString( job.getResultSummary() ),
                        SchemeSummaryResults.class );
            } else {
                log.debug( "result summary is not genrated against job id: " + id );
            }

            JobEntity master = jobDao.getLatestNonDeletedObjectById( entityManager, UUID.fromString( id ) );

            if ( null != schemeSummaryResults.getDesignSummaryMap() ) {
                for ( Map.Entry< String, Object > entry : schemeSummaryResults.getDesignSummaryMap().entrySet() ) {
                    if ( !EXP_NUM.equals( entry.getKey() ) && !"id".equals( entry.getKey() ) ) {
                        orderNumber += ConstantsInteger.INTEGER_VALUE_ONE;
                        columnsList.add( prepareTable( entry.getKey(), orderNumber ) );
                    }
                }

                for ( Map.Entry< String, Object > entry : schemeSummaryResults.getObjectSummaryMap().entrySet() ) {
                    if ( !EXP_NUM.equals( entry.getKey() ) && !"id".equals( entry.getKey() ) ) {
                        orderNumber += ConstantsInteger.INTEGER_VALUE_ONE;
                        columnsList.add( prepareTable( entry.getKey(), orderNumber ) );
                    }
                }

                if ( master != null && master.getJobSchemeCategory() == SchemeCategoryEnum.OPTIMIZATION.getKey()
                        && null != schemeSummaryResults.getGoal() ) {
                    for ( Map.Entry< String, Object > entry : schemeSummaryResults.getGoal().entrySet() ) {
                        if ( !EXP_NUM.equals( entry.getKey() ) && !"id".equals( entry.getKey() ) ) {
                            orderNumber += ConstantsInteger.INTEGER_VALUE_ONE;
                            if ( entry.getKey().equals( COMBINED_GOAL ) ) {
                                columnsList.add( prepareTable( entry.getKey(), orderNumber ) );
                            }
                        }
                    }
                }
            }

            // update design summary titals
            for ( DesignVariableEntity designVariableEntity : variableDAO.getAllDesignVariables( entityManager,
                    job.getWorkflow().getComposedId().getId().toString(), master.getCreatedBy().getId().toString() ) ) {

                for ( TableColumn uiRow : columnsList ) {
                    if ( designVariableEntity.getLabel().equalsIgnoreCase( uiRow.getName() ) ) {

                        uiRow.setTitle( designVariableEntity.getLabel() );
                    }
                }
            }

            // update objective summary titals
            for ( ObjectiveVariableEntity objectiveVariableEntity : ( List< ObjectiveVariableEntity > ) variableDAO.getAllObjectiveVariablesByWorkflowId(
                    entityManager, job.getWorkflow().getComposedId().getId().toString() ) ) {

                for ( TableColumn uiRow : columnsList ) {
                    if ( objectiveVariableEntity.getName().equalsIgnoreCase( uiRow.getName() ) ) {
                        uiRow.setTitle( objectiveVariableEntity.getLabel() );
                    }
                }
            }

            return columnsList;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare exp num column.
     *
     * @return the table column
     */
    private TableColumn prepareExpNumColumn() {
        TableColumn tc = new TableColumn();
        tc.setName( EXP_NUM );
        tc.setData( EXP_NUM );
        tc.setTitle( "Exp Nr" );
        tc.setFilter( "" );
        tc.setOrderNum( ConstantsInteger.INTEGER_VALUE_ZERO );
        tc.setSortable( false );
        return tc;

    }

    /**
     * Prepare table.
     *
     * @param column
     *         the column
     * @param orderNumber
     *         the order number
     *
     * @return the table column
     */
    private TableColumn prepareTable( String column, int orderNumber ) {
        TableColumn tc = new TableColumn();
        tc.setName( column );
        tc.setData( column );
        tc.setTitle( column );
        tc.setFilter( "" );
        tc.setOrderNum( orderNumber );
        tc.setRenderer( prepareRenderer( column ) );
        tc.setSortable( false );
        return tc;
    }

    /**
     * Prepare renderer.
     *
     * @param column
     *         the column
     *
     * @return the renderer
     */
    private Renderer prepareRenderer( String column ) {
        Renderer renderer = new Renderer();
        renderer.setType( FieldTypes.TEXT.getType() );
        renderer.setData( column.toLowerCase() );
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getDataCreatedContext( String userIdFromGeneralHeader, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< Object > selectedIds = filter.getItems();
            if ( CollectionUtil.isNotEmpty( selectedIds ) && ConstantsInteger.INTEGER_VALUE_ONE == selectedIds.size() ) {
                return contextMenuManager.getJobsDataCreatedContextMenu( UUID.fromString( selectedIds.get( 0 ).toString() ), false );
            } else {
                final SelectionResponseUI selectionResponseUI = selectionManager.createSelection( entityManager, userIdFromGeneralHeader,
                        SelectionOrigins.CONTEXT, filter );
                return contextMenuManager.getJobsDataCreatedContextMenu( UUID.fromString( selectionResponseUI.getId() ), true );
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Check job childern.
     *
     * @param jobId
     *         the job id
     *
     * @return true, if successful
     */
    @Override
    public boolean checkJobChildern( UUID jobId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // counting takes less time instead of fetching all records in jpa
        // please replace me with counting
        try {
            return CollectionUtils.isNotEmpty( jobDao.getAllChildrenOfMasterJob( entityManager, jobId ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Check job childern IDs.
     *
     * @param jobId
     *         the job id
     *
     * @return true, if successful
     */
    @Override
    public List< UUID > getJobChildernIDs( UUID jobId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UUID > childrens = new ArrayList<>();
            final List< Relation > relationList = jobDao.getjobRelationsById( entityManager, jobId );
            for ( Relation relation : relationList ) {
                if ( Integer.valueOf( relation.getType() ).equals( ConstantsInteger.INTEGER_VALUE_ZERO ) ) {
                    childrens.add( relation.getChild() );
                }
            }
            return childrens;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Checks if is workflow job.
     *
     * @param job
     *         the job
     *
     * @return true, if is workflow job
     */
    private boolean isWorkflowJob( Job job ) {

        if ( job.getJobType() == JobTypeEnums.WORKFLOW.getKey() ) {
            return true;
        }
        return ( job.getWorkflowId().equals( UUID.fromString( SystemWorkflow.DELETE.getId() ) ) ) || ( job.getWorkflowId()
                .equals( UUID.fromString( SystemWorkflow.RESTORE.getId() ) ) );
    }

    /**
     * Creates the generic DTO from object entity.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the object
     */
    private GenericDTO createGenericDTOFromSusEntity( SuSEntity susEntity ) {
        GenericDTO genericDTO = null;
        if ( susEntity != null ) {
            genericDTO = new GenericDTO();
            genericDTO.setName( susEntity.getName() );
            genericDTO.setId( susEntity.getComposedId().getId() );
            genericDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
            genericDTO.setCreatedOn( susEntity.getCreatedOn() );
            genericDTO.setModifiedOn( susEntity.getModifiedOn() );
            genericDTO.setCreatedBy( prepareUserModelFromUserEntity( susEntity.getCreatedBy() ) );
            genericDTO.setModifiedBy( prepareUserModelFromUserEntity( susEntity.getModifiedBy() ) );
            if ( susEntity.getTypeId() != null ) {
                genericDTO.setLifeCycleStatus(
                        configManager.getStatusByIdandObjectType( susEntity.getTypeId(), susEntity.getLifeCycleStatus(),
                                susEntity.getConfig() ) );
                genericDTO.setType(
                        configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(), susEntity.getConfig() ).getName() );
                genericDTO.setIcon( susEntity.getIcon() );
            }
            if ( susEntity instanceof ContainerEntity ) {
                genericDTO.setUrlType( ConstantsString.PROJECT_KEY );
            } else {
                genericDTO.setUrlType( ConstantsString.OBJECT_KEY );
            }
            genericDTO.setTypeId( susEntity.getTypeId() );
            genericDTO.setSize( susEntity.getSize() != null && ConstantsInteger.INTEGER_VALUE_ZERO < susEntity.getSize()
                    ? org.apache.commons.io.FileUtils.byteCountToDisplaySize( susEntity.getSize() ) : ConstantsString.NOT_AVAILABLE );
        }
        return genericDTO;
    }

    /**
     * Prepare user model from user entity.
     *
     * @param userEntity
     *         the user entity
     *
     * @return the user DTO
     */
    private UserDTO prepareUserModelFromUserEntity( UserEntity userEntity ) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId( userEntity.getId().toString() );
        userDTO.setDescription( userEntity.getDescription() );
        userDTO.setFirstName( userEntity.getFirstName() );
        userDTO.setSurName( userEntity.getSurName() );
        userDTO.setUserUid( userEntity.getUserUid() );
        userDTO.setStatus( userEntity.isStatus() ? ConstantsStatus.ACTIVE : ConstantsStatus.INACTIVE );
        userDTO.setRestricted( userEntity.isRestricted() ? ConstantsStatus.YES : ConstantsStatus.NO );
        userDTO.setChangable( userEntity.isChangeable() );
        return userDTO;
    }

    /**
     * Prepare object view DTO.
     *
     * @param objectId
     *         the object id
     * @param viewJson
     *         the view json
     * @param viewKey
     *         the view key
     * @param isUpdateable
     *         the is updateable
     * @param viewId
     *         the view id
     *
     * @return the object view DTO
     */
    @Override
    public ObjectViewDTO prepareObjectViewDTO( String objectId, String viewJson, String viewKey, boolean isUpdateable, String viewId ) {
        final ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
        if ( !isUpdateable && !objectViewDTO.isDefaultView() ) {
            objectViewDTO.setId( null );
        }
        objectViewDTO.setObjectId( objectId );
        objectViewDTO.setObjectViewKey( viewKey );
        objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
        objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
        return objectViewDTO;
    }

    /**
     * Prepare location entity.
     *
     * @param location
     *         the location
     *
     * @return the location entity
     */
    private LocationEntity prepareLocationEntity( LocationDTO location ) {
        return new LocationEntity( location.getId(), location.getDescription(), location.getName(),
                ConstantsStatus.ACTIVE.equals( location.getStatus() ) );
    }

    /**
     * Stop server work flow.
     *
     * @param jobId
     *         the job id
     * @param userName
     *         the user name
     *
     * @return true, if successful
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.server.manager.JobManager#stopServerWorkFlow(java.lang.String)
     */
    @Override
    public boolean stopServerWorkFlow( String jobId, String userName ) {
        return workflowManager.stopServerWorkFlow( jobId, userName );
    }

    /**
     * Plot CSV file by python.
     *
     * @param jobId
     *         the job id
     * @param optionId
     *         the option id
     * @param token
     *         the token
     * @param userUID
     *         the user UID
     *
     * @return the object
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws ParseException
     *         the parse exception
     */
    @Override
    public Object plotCSVFileByPython( String userId, String jobId, String optionId, String token, String userUID )
            throws IOException, ParseException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< JobEntity > childJobs = validateJobForPlotGeneration( entityManager, userId, jobId );
            List< List< String > > rows = new ArrayList<>();
            prepareCSVFileSummaryDataFromJob( childJobs, rows, true );
            File file = generateCSVFileInTempWithJobId( entityManager, jobId, rows );
            String pythonGeneratedCSVFileAddress = generatePlotCSVFileByPython( optionId, file );
            if ( 2 > childJobs.size() && DesignPlotingConfig.getOptionNameById( Integer.parseInt( optionId ) ).toLowerCase()
                    .contains( "correlation" ) ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.AT_LEAST_2_EXPERIMENTS_REQUIRED_FOR_CORRELATION.getKey() ) );
            }

            final String susEntity = uploadFileAndCreateTraceObject( optionId, token, pythonGeneratedCSVFileAddress,
                    childJobs.get( 0 ).getName() );
            String traceObjectId = null;
            org.json.JSONObject jsonObj = new org.json.JSONObject( susEntity );
            if ( jsonObj.has( "id" ) ) {
                traceObjectId = ( String ) jsonObj.get( "id" );
            }
            String breadcrumb = PropertiesManager.getLocationURL() + File.separator + "api/breadcrumb/view/data/object/" + traceObjectId;
            SusResponseDTO breadCrumbResponse = SuSClient.getRequest( breadcrumb, prepareDownloadHeaders( token ) );
            List< BreadCrumbItemDTO > list = JsonUtils.jsonToList( JsonUtils.objectToJson( breadCrumbResponse.getData() ),
                    BreadCrumbItemDTO.class );
            return list.get( ( list.size() - 1 ) );

        } finally {
            entityManager.close();
        }
    }

    /**
     * Generate Image.
     *
     * @param jobId
     *         the job id
     * @param key
     *         the key
     * @param token
     *         the token
     *
     * @return the object
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws ParseException
     *         the parse exception
     */
    @Override
    public Object generateImage( String userId, String jobId, String key, String token ) throws IOException, ParseException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< JobEntity > childJobs = validateJobForPlotGeneration( entityManager, userId, jobId );
            List< List< String > > rows = new ArrayList<>();
            validateGenerateImageData( childJobs, key );
            prepareCSVFileSummaryDataFromJob( childJobs, rows, true );
            File file = generateCSVFileInTempWithJobId( entityManager, jobId, rows );
            String pythonGeneratedCSVFileAddress = generateCSVFileForGenerateImage( key, file );
            final String susEntity = uploadFileAndCreateTraceObject( "4", token, pythonGeneratedCSVFileAddress,
                    childJobs.get( 0 ).getName() );
            String traceObjectId = null;
            org.json.JSONObject jsonObj = new org.json.JSONObject( susEntity );
            if ( jsonObj.has( "id" ) ) {
                traceObjectId = ( String ) jsonObj.get( "id" );
            }
            String breadcrumb = PropertiesManager.getLocationURL() + File.separator + "api/breadcrumb/view/data/object/" + traceObjectId;
            SusResponseDTO breadCrumbResponse = SuSClient.getRequest( breadcrumb, prepareDownloadHeaders( token ) );
            List< BreadCrumbItemDTO > list = JsonUtils.jsonToList( JsonUtils.objectToJson( breadCrumbResponse.getData() ),
                    BreadCrumbItemDTO.class );
            return list.get( ( list.size() - 1 ) );
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_IN_SCHEME_PLOT_GENERATION.getKey(), key ) );
        } finally {
            entityManager.close();
        }
    }

    private List< JobEntity > validateJobForPlotGeneration( EntityManager entityManager, String userId, String jobId ) {
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                jobId + ConstantsString.COLON + PermissionMatrixEnum.READ.getValue() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_READ.getKey(), OBJECT ) );
        }
        JobEntity masterJob = jobDao.getLatestNonDeletedObjectById( entityManager, UUID.fromString( jobId ) );
        if ( masterJob.getStatus() != WorkflowStatus.COMPLETED.getKey() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.CAN_NOT_PLOT_FOR_INCOMPLETE_JOBS.getKey() ) );
        }
        List< JobEntity > childJobs = jobDao.getAllChildrenOfMasterJob( entityManager, UUID.fromString( jobId ) );
        if ( childJobs.isEmpty() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_CHILD_JOBS_FOUND.getKey() ) );
        }
        return childJobs;
    }

    /**
     * Validate Generate Image Data.
     *
     * @param assemblyList
     *         the assembly List
     * @param key
     *         the key
     */
    private void validateGenerateImageData( List< JobEntity > assemblyList, String key ) {
        if ( key.toLowerCase().contains( GENERATE_IMAGE_CURVES ) && 4 > assemblyList.size() ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.AT_LEAST_4_EXPERIMENTS_REQUIRED_FOR_GENERATING_CURVE.getKey() ) );
        }
        if ( key.toLowerCase().contains( GENERATE_STACK_PLOT ) && 2 > assemblyList.size() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.AT_LEAST_2_EXPERIMENTS_REQUIRED_FOR_STACK_PLOT.getKey() ) );
        }
    }

    /**
     * Prepare location headers.
     *
     * @param authToken
     *         the auth tokens
     *
     * @return the map
     */
    private Map< String, String > prepareLocationHeaders( String authToken ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, authToken );
        requestHeaders.put( ConstantRequestHeader.AUTHORIZATION, authToken );

        return requestHeaders;
    }

    /**
     * Upload file and create trace object.
     *
     * @param optionId
     *         the option id
     * @param token
     *         the token
     * @param pythonGeneratedCSVFileAddress
     *         the python generated CSV file address
     * @param masterJobName
     *         the master job name
     *
     * @return the string
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws ParseException
     *         the parse exception
     */
    private String uploadFileAndCreateTraceObject( String optionId, String token, String pythonGeneratedCSVFileAddress,
            String masterJobName ) throws IOException, ParseException {

        String objName = masterJobName + DesignPlotingConfig.getOptionNameById( Integer.parseInt( optionId ) ) + Math.random();
        String address = PropertiesManager.getLocationURL() + File.separator + API_DOCUMENT_UPLOAD;
        DocumentDTO documentDTO = SuSClient.uploadFileRequest( address, new File( pythonGeneratedCSVFileAddress ),
                prepareDownloadHeaders( token ) );
        // delete csv/image file
        deleteFile( pythonGeneratedCSVFileAddress );
        String jsonPlotAddress = pythonGeneratedCSVFileAddress.substring( 0, pythonGeneratedCSVFileAddress.lastIndexOf( '.' ) );
        String typeId;
        String jsonObjectDTO;
        if ( TRACE.equals( extractDataObjectTypeFromPath( pythonGeneratedCSVFileAddress ) ) ) {
            jsonObjectDTO = prepareTraceDto( optionId, objName, documentDTO, jsonPlotAddress );
            // delete json file in case of Trace
            deleteFile( jsonPlotAddress + ConstantsFileExtension.JSON );
            typeId = DataObjectTraceEntity.CLASS_ID.toString();
        } else {
            jsonObjectDTO = prepareImageDto( objName, documentDTO );
            typeId = SimuspaceFeaturesEnum.IMAGE_DATA_OBJECTS.getId();
        }

        String createObjectURL =
                PropertiesManager.getLocationURL() + File.separator + "api/data/object/" + ConstantsID.HIDDEN_PROJECT_ID_FOR_SCHEME_PLOTTING
                        + "/type/" + typeId + "/refresh";
        SusResponseDTO response = SuSClient.postRequest( createObjectURL, jsonObjectDTO, prepareDownloadHeaders( token ) );
        return JsonUtils.objectToJson( response.getData() );
    }

    /**
     * Prepare image dto.
     *
     * @param objName
     *         the obj name
     * @param documentDTO
     *         the document DTO
     *
     * @return the string
     */
    private String prepareImageDto( String objName, DocumentDTO documentDTO ) {
        DataObjectImageDTO image = new DataObjectImageDTO();
        image.setFile( documentDTO );
        image.setName( objName );
        image.setTypeId( UUID.fromString( "67edfe99-bc9a-4f5a-a73d-b7c7b4c53081" ) );
        image.setCreatedOn( new Date() );
        image.setHidden( true );
        return JsonUtils.objectToJson( image );
    }

    /**
     * Prepare trace dto.
     *
     * @param optionId
     *         the option id
     * @param objName
     *         the obj name
     * @param documentDTO
     *         the document DTO
     * @param jsonPlotAddress
     *         the json plot address
     *
     * @return the string
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws ParseException
     *         the parse exception
     */
    private String prepareTraceDto( String optionId, String objName, DocumentDTO documentDTO, String jsonPlotAddress )
            throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse( new FileReader( jsonPlotAddress + ConstantsFileExtension.JSON ) );

        DataObjectTraceDTO trace = new DataObjectTraceDTO();
        trace.setPlot( obj.toString() );
        trace.setFile( documentDTO );
        trace.setPlotType( DesignPlotingConfig.getOptionNameById( Integer.parseInt( optionId ) ) );
        trace.setName( objName );
        trace.setTypeId( UUID.fromString( DataObjectTraceEntity.CLASS_ID.toString() ) );
        trace.setCreatedOn( new Date() );
        trace.setHidden( true );
        return JsonUtils.objectToJson( trace );
    }

    /**
     * Extract data object type from path.
     *
     * @param pythonGeneratedCSVFileAddress
     *         the python generated CSV file address
     *
     * @return the string
     */
    private String extractDataObjectTypeFromPath( String pythonGeneratedCSVFileAddress ) {
        return ( "csv".equalsIgnoreCase( FilenameUtils.getExtension( pythonGeneratedCSVFileAddress ) ) ) ? TRACE : "Image";
    }

    /**
     * Generate plot CSV file by python.
     *
     * @param optionId
     *         the option id
     * @param file
     *         the file
     *
     * @return the string
     */
    private String generatePlotCSVFileByPython( String optionId, File file ) {
        String pythonFilePath = DesignPlotingConfig.getPythonPathById( Integer.parseInt( optionId ) );
        String pythonGeneratedCSVFileAddress;
        if ( pythonFilePath != null && !pythonFilePath.isEmpty() && new File( pythonFilePath ).isFile() ) {
            ProcessResult imageGenerationResult = LinuxUtils.runSystemCommand(
                    String.format( "%s %s %s", PropertiesManager.getPythonExecutionPathOnServer(), pythonFilePath, file.getAbsolutePath() ),
                    null );
            if ( imageGenerationResult.getExitValue() != 0 ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_IN_SCHEME_PLOT_GENERATION.getKey(),
                        DesignPlotingConfig.getOptionNameById( Integer.parseInt( optionId ) ) ) );
            }
            pythonGeneratedCSVFileAddress = imageGenerationResult.getOutputString().trim();
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), pythonFilePath ) );
        }
        return pythonGeneratedCSVFileAddress;

    }

    /**
     * Generate plot CSV file by python.
     *
     * @param key
     *         the option id
     * @param file
     *         the file
     *
     * @return the string
     */
    private String generateCSVFileForGenerateImage( String key, File file ) {
        String pythonFilePath = DesignPlotingConfig.getGenerateImagePythonFilePath( key );
        String pythonGeneratedCSVFileAddress = null;
        if ( pythonFilePath != null && !pythonFilePath.isEmpty() && new File( pythonFilePath ).isFile() ) {
            ProcessResult imageGenerationResult = LinuxUtils.runSystemCommand(
                    String.format( "%s %s %s", PropertiesManager.getPythonExecutionPathOnServer(), pythonFilePath, file.getAbsolutePath() ),
                    null );
            if ( imageGenerationResult.getExitValue() != 0 ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_IN_SCHEME_PLOT_GENERATION.getKey(), key ) );
            }
            pythonGeneratedCSVFileAddress = imageGenerationResult.getOutputString().trim();
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), pythonFilePath ) );
        }
        return pythonGeneratedCSVFileAddress;
    }

    /**
     * Python stderror.
     *
     * @param process
     *         the process
     *
     * @return the string
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private String pythonStderror( Process process ) throws IOException {
        String line;
        final StringBuilder error = new StringBuilder();
        try ( InputStream stdError = process.getErrorStream(); InputStreamReader isr = new InputStreamReader(
                stdError ); final BufferedReader brCleanUpError = new BufferedReader( isr ) ) {
            while ( ( line = brCleanUpError.readLine() ) != null ) {
                error.append( line ).append( ConstantsString.NEW_LINE );
            }
            if ( !error.isEmpty() ) {
                log.warn( "python error: " + error );
            }
        }
        return error.toString();
    }

    /**
     * Python stdout.
     *
     * @param process
     *         the process
     *
     * @return the string
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private String pythonStdout( Process process ) throws IOException {
        String pythonGenratedCSVFileAddress = null;
        try ( InputStream stdout = process.getInputStream(); InputStreamReader isr = new InputStreamReader( stdout,
                StandardCharsets.UTF_8 ); BufferedReader reader = new BufferedReader( isr ) ) {
            String line;
            while ( ( line = reader.readLine() ) != null ) {
                log.info( "stdout: " + line );
                pythonGenratedCSVFileAddress = line;
            }
        }
        return pythonGenratedCSVFileAddress;
    }

    /**
     * Prepare download headers.
     *
     * @param authToken
     *         the auth token
     *
     * @return the map
     */
    private Map< String, String > prepareDownloadHeaders( String authToken ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, authToken );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTHORIZATION, authToken );
        return requestHeaders;

    }

    /**
     * Delete file.
     *
     * @param url
     *         the url
     */
    private void deleteFile( String url ) {
        try {
            File stagingFile = new File( url );
            if ( stagingFile.exists() ) {
                FileUtils.setGlobalAllFilePermissions( stagingFile );

                if ( OSValidator.isUnix() ) {
                    String[] finalCommand2 = { "rm", "-rf", stagingFile.getPath() };
                    executeProcess( finalCommand2 );
                } else if ( OSValidator.isWindows() ) {
                    String[] finalCommand2 = { "del", stagingFile.getPath() };
                    executeProcess( finalCommand2 );
                }
                log.info( " File deleted > " + stagingFile.getName() + " : object Deleted" );
            }

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * Execute process.
     *
     * @param taskList
     *         the task list
     *
     * @return true, if successful
     */
    public static boolean executeProcess( String[] taskList ) {
        Thread t = new Thread( () -> LinuxUtils.runCommand( taskList, ConstantsString.COMMAND_KARAF_LOGGING_ON, null ) );
        t.start();

        return true;

    }

    /**
     * Generate csv file in temp with job id file.
     *
     * @param entityManager
     *         the entity manager
     * @param jobId
     *         the job id
     * @param rows
     *         the rows
     *
     * @return the file
     *
     * @throws IOException
     *         the io exception
     */
    private File generateCSVFileInTempWithJobId( EntityManager entityManager, String jobId, List< List< String > > rows )
            throws IOException {
        String jobName = jobDao.getLatestObjectById( entityManager, JobEntity.class, UUID.fromString( jobId ) ).getName();
        File file = new File( PropertiesManager.getDefaultServerTempPath() + File.separator + "CSV_Downloaded_" + jobName + ".csv" );
        if ( !file.exists() ) {
            file.createNewFile();
            FileUtils.setGlobalReadFilePermissions( file );
        }
        try ( FileWriter csvWriter = new FileWriter( file ) ) {
            for ( List< String > rowData : rows ) {
                csvWriter.append( String.join( ",", rowData ) );
                csvWriter.append( "\n" );
            }
            csvWriter.flush();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return file;
    }

    /**
     * Generate csv file in staging with job id file.
     *
     * @param entityManager
     *         the entity manager
     * @param jobId
     *         the job id
     * @param rows
     *         the rows
     *
     * @return the file
     *
     * @throws IOException
     *         the io exception
     */
    private File generateCSVFileInStagingWithJobId( EntityManager entityManager, String jobId, List< List< String > > rows )
            throws IOException {
        JobEntity jobeNT = jobDao.getLatestObjectById( entityManager, JobEntity.class, UUID.fromString( jobId ) );
        JobImpl job = prepareJob( entityManager, jobeNT );
        final String pathFile = File.separator + job.getName() + "_" + job.getJobInteger();
        String stagingPath = PropertiesManager.getUserStagingPath( job.getCreatedBy().getUserUid() ) + pathFile;

        File file = new File( stagingPath + File.separator + DESIGN_SUMMARY_CSV );
        if ( !file.exists() ) {
            file.createNewFile();
            FileUtils.setGlobalReadFilePermissions( file );
        }
        try ( FileWriter csvWriter = new FileWriter( file ) ) {
            for ( List< String > rowData : rows ) {
                csvWriter.append( String.join( ",", rowData ) );
                csvWriter.append( "\n" );
            }
            csvWriter.flush();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return file;
    }

    /**
     * Prepare CSV file summary data from job.
     *
     * @param assemblyList
     *         the assembly list
     * @param rows
     *         the rows
     * @param ran
     *         the ran
     *
     * @return true, if successful
     */
    private boolean prepareCSVFileSummaryDataFromJob( List< JobEntity > assemblyList, List< List< String > > rows, boolean ran ) {
        for ( JobEntity jobEntity : assemblyList ) {

            if ( jobEntity.getResultSummary() != null ) {

                List< String > header = new ArrayList<>();
                List< String > dataList = new ArrayList<>();

                SchemeSummaryResults schemeSummaryResults = JsonUtils.jsonToObject(
                        ByteUtil.convertByteToString( jobEntity.getResultSummary() ), SchemeSummaryResults.class );
                Map< String, Object > designSummaryMap = schemeSummaryResults.getDesignSummaryMap();
                Map< String, Object > objectSummaryMap = schemeSummaryResults.getObjectSummaryMap();

                if ( designSummaryMap != null ) {
                    for ( Map.Entry< String, Object > objective : designSummaryMap.entrySet() ) {
                        if ( "id".equals( objective.getKey() ) ) {
                            continue;
                        }
                        header.add( objective.getKey() );
                        dataList.add( ( objective.getValue() != null ? objective.getValue().toString() : " " ) );
                    }
                }
                if ( objectSummaryMap != null ) {
                    for ( Map.Entry< String, Object > objective : objectSummaryMap.entrySet() ) {

                        header.add( objective.getKey() );
                        dataList.add( ( objective.getValue() != null ? objective.getValue().toString() : " " ) );
                    }
                }

                if ( ran ) {
                    rows.add( header );
                    ran = false;
                }
                rows.add( dataList );
            }
        }
        return ran;
    }

    /**
     * Gets the job status by job id.
     *
     * @param id
     *         the id
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the job status by job id
     */
    @Override
    public String getJobStatusByJobId( String id, UUID userIdFromGeneralHeader ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            JobEntity entity = jobDao.getLatestNonDeletedObjectById( entityManager, UUID.fromString( id ) );
            if ( null != entity ) {
                return String.valueOf( WorkflowStatus.getById( entity.getStatus() ) );
            } else {
                return null;
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the job resultsummary by job id.
     *
     * @param id
     *         the id
     *
     * @return the job resultsummary by job id
     */
    @Override
    public Object getJobResultsummaryByJobId( String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            JobEntity entity = jobDao.getLatestNonDeletedObjectById( entityManager, UUID.fromString( id ) );
            SchemeSummaryResults schemeSummaryResults = new SchemeSummaryResults();
            if ( entity.getResultSummary() != null && 0 < entity.getResultSummary().length ) {
                schemeSummaryResults = JsonUtils.jsonToObject( ByteUtil.convertByteToString( entity.getResultSummary() ),
                        SchemeSummaryResults.class );
            } else {
                log.debug( "result summary is not generated against job id: " + id );
            }
            return schemeSummaryResults;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LatestWorkFlowDTO rerunJob( String userId, String jobId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Job retJob = getJob( entityManager, UUID.fromString( userId ), jobId );
            LatestWorkFlowDTO workflow = workflowManager.getNewWorkflowByIdAndVersionId( entityManager, UUID.fromString( userId ),
                    retJob.getWorkflowId().toString(), retJob.getWorkflowVersion().getId() );
            workflow.setRunsOn( retJob.getRunsOn().getId().toString() );
            workflow.setRerunJobId( jobId );
            applyPreviousAskOnRunParameters( workflow, retJob );
            workflow.setHasGlobalVariables(
                    getGlobalVariablesFromJobIdAndUserId( entityManager, UUID.fromString( jobId ), UUID.fromString( userId ) ) != null );
            return workflow;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Map< String, Object > getGlobalVariablesFromJobIdAndUserId( EntityManager entityManager, UUID jobId, UUID userId ) {
        JobGlobalVariablesEntity jobGlobalVariablesEntity = jobGlobalVariableDAO.getJobGlobalVariablesByJobEntityAndUserId( entityManager,
                jobId, userId );
        return jobGlobalVariablesEntity != null ? JsonUtils.convertStringToMapGenericValue(
                ByteUtil.convertByteToString( jobGlobalVariablesEntity.getGlobalVariables() ) ) : null;
    }

    /**
     * Apply ask on run parameters from last job run.
     *
     * @param workflow
     *         the workflow
     * @param retJob
     *         the retJob
     */
    private void applyPreviousAskOnRunParameters( LatestWorkFlowDTO workflow, Job retJob ) {
        for ( String key : workflow.getJob().keySet() ) {
            switch ( key ) {
                case RUN_ON_FIELD -> workflow.getJob().put( key, retJob.getRunsOn().getId() );
                case NAME_FIELD -> workflow.getJob().put( key, retJob.getName() );
                case MAX_EXE_TIME_FIELD -> workflow.getJob().put( key, prepareJobMaxTime( retJob ) );
                case DESCRIPTION_FIELD -> workflow.getJob().put( key, retJob.getDescription() );
            }
        }
        if ( retJob.getAskOnRunParameters() == null || retJob.getAskOnRunParameters().isEmpty() ) {
            return;
        }
        Map< String, Object > askOnRunParameters = retJob.getAskOnRunParameters();
        for ( String key : workflow.getJob().keySet() ) {
            if ( askOnRunParameters.containsKey( key ) ) {
                workflow.getJob().put( key, askOnRunParameters.get( key ) );
            }
        }
        WorkflowModel workflowModel = JsonUtils.jsonToObject( JsonUtils.toJson( workflow.getElements() ), WorkflowModel.class );
        for ( WorkflowElement element : workflowModel.getNodes() ) {
            for ( Field field : element.getData().getFields() ) {
                if ( field.getType().equals( FieldTypes.OBJECT_PARSER.getType() ) ) {
                    continue;
                } else if ( askOnRunParameters.containsKey( field.getName() ) ) {
                    field.setValue( JsonUtils.jsonToObject( JsonUtils.toJson( askOnRunParameters.get( field.getName() ) ), Field.class )
                            .getValue() );
                }
            }
        }
        // put replaced elements to wf
        workflow.setElements( JsonUtils.convertStringToMapGenericValue( JsonUtils.toJson( workflowModel ) ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateJobStatus( UUID userId, String token, String jobId, String status ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Job job = getJob( entityManager, userId, jobId );

            if ( "complete".equalsIgnoreCase( status ) || "completed".equalsIgnoreCase( status ) ) {
                job.setStatus( new Status( WorkflowStatus.COMPLETED ) );
                updateProgressBarForOptimizationJob( entityManager, job );
            } else if ( "fail".equalsIgnoreCase( status ) || "failed".equalsIgnoreCase( status ) ) {
                job.setStatus( new Status( WorkflowStatus.FAILED ) );
            } else {
                WorkflowStatus statusToUpdate = WorkflowStatus.getByValue( status );
                if ( statusToUpdate == null ) {
                    return ResponseUtils.failure( "Invalid status " + status );
                } else {
                    job.setStatus( new Status( statusToUpdate ) );
                }
            }

            updateJob( entityManager, job );

            if ( ( "complete".equalsIgnoreCase( status ) || "completed".equalsIgnoreCase( status ) )
                    && job.getJobRelationType() == JobRelationTypeEnums.MASTER.getKey() && (
                    job.getJobType() == SchemeCategoryEnum.OPTIMIZATION.getKey()
                            || job.getJobType() == SchemeCategoryEnum.DOE.getKey() ) ) {
                runPostProcessJobIfExisitsInScheme( entityManager, job, token );
            }

            return ResponseUtils.success( "Job status updated", true );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Update progress bar for optimization job.
     *
     * @param entityManager
     *         the entity manager
     * @param job
     *         the job
     */
    private void updateProgressBarForOptimizationJob( EntityManager entityManager, Job job ) {

        if ( job.getJobType() == SchemeCategoryEnum.OPTIMIZATION.getKey() && job.getProgress() != null
                && ConstantsInteger.INTEGER_VALUE_ZERO == job.getProgress().getTotal() ) {
            List< Job > childJobs = getAllChildrensOfMasterJob( entityManager, job );
            ProgressBar bar = new ProgressBar();
            bar.setTotal( childJobs != null ? childJobs.size() : ConstantsInteger.INTEGER_VALUE_ONE );
            bar.setCompleted( childJobs.stream()
                    .filter( childJob -> WorkflowStatus.COMPLETED.equals( WorkflowStatus.getById( childJob.getStatus().getId() ) ) )
                    .count() );
            job.setProgress( bar );
        }
    }

    /**
     * Run post process job if exisits in scheme.
     *
     * @param entityManager
     *         the entity manager
     * @param jobImpl
     *         the job
     * @param token
     *         the token
     */
    private void runPostProcessJobIfExisitsInScheme( EntityManager entityManager, Job jobImpl, String token ) {
        try {

            if ( jobImpl.getPostprocess() != null && jobImpl.getPostprocess().getPostProcessWorkflow() != null
                    && jobImpl.getPostprocess().getPostProcessWorkflow().getElements() != null ) {

                log.info( POST_WORKFLOW + "Starting  Preprations" );
                JobScheme jobScheme = prepareJobSchemeAsPostJson( entityManager, jobImpl );
                log.info( POST_WORKFLOW + "JobScheme prepared" );

                JobParameters jobParameters = JsonUtils.jsonToObject( jobImpl.getPostprocessParametersJson(), JobParameters.class );
                jobParameters.getWorkflow().setDummyMasterJobId( jobImpl.getId().toString() );
                jobParameters.setResultSchemeAsJson( jobScheme );
                jobParameters.setJobRelationType( JobRelationTypeEnums.MASTER.getKey() );
                jobParameters.setJobType( JobTypeEnums.WORKFLOW.getKey() );
                jobParameters.setPostprocess( jobImpl.getPostprocess() );
                jobParameters.setName( jobParameters.getWorkflow().getName() );
                jobParameters.setServer( jobImpl.getServer() );
                jobParameters.setRequestHeaders( jobImpl.getRequestHeaders() );
                jobParameters.getRequestHeaders().setToken( token );
                jobParameters.getRequestHeaders().setJobAuthToken( jobImpl.getRequestHeaders().getJobAuthToken() );
                Map< String, String > requestHeadersForJobSubmission = prepareDownloadHeaders( token );
                requestHeadersForJobSubmission.put( ConstantRequestHeader.JOB_TOKEN, jobImpl.getRequestHeaders().getJobAuthToken() );
                log.info( POST_WORKFLOW + "Submitting Job" );
                String urlRun = PropertiesManager.getLocationURL() + "/api/workflow/runjob";
                SusResponseDTO susResponseRunJob = SuSClient.postRequest( urlRun, JsonUtils.toJson( jobParameters ),
                        requestHeadersForJobSubmission );

                log.info( POST_WORKFLOW + "job submitted" + susResponseRunJob.getData() );
            }

        } catch ( Exception e ) {
            log.error( POST_WORKFLOW + "job submission failed", e );
            throw new SusException( e, getClass() );
        }
    }

    @Override
    public JobScheme prepareJobSchemeAsPostJson( EntityManager entityManager, Job jobImpl ) {

        try {
            List< JobEntity > assemblyList = jobDao.getAllChildrenOfMasterJob( entityManager, jobImpl.getId() );
            // prepare JobScheme

            List< JobSchemeExperiment > experiments = new ArrayList<>();

            for ( JobEntity jobEntity : assemblyList ) {
                if ( jobEntity.getResultSummary() != null ) {
                    SchemeSummaryResults schemeSummaryResults = JsonUtils.jsonToObject(
                            ByteUtil.convertByteToString( jobEntity.getResultSummary() ), SchemeSummaryResults.class );
                    if ( jobImpl.getJobSchemeCategory() == SchemeCategoryEnum.DOE.getKey() ) {
                        JobSchemeExperiment jobSchemeExperiment = new JobSchemeExperiment(
                                Integer.parseInt( schemeSummaryResults.getExperimentNumber() ),
                                new JobSchemeDetail( jobEntity.getName(), jobEntity.getId(),
                                        WorkflowStatus.getById( jobEntity.getStatus() ).getValue(), jobEntity.getJobDirectory() ),
                                new JobSchemeSummary( Collections.singletonList( schemeSummaryResults.getDesignSummaryMap() ),
                                        Collections.singletonList( schemeSummaryResults.getObjectSummaryMap() ) ) );

                        experiments.add( jobSchemeExperiment );
                    } else if ( jobImpl.getJobSchemeCategory() == SchemeCategoryEnum.OPTIMIZATION.getKey() ) {

                        List< Map< String, Object > > optObjectiveMap = prepareObjectiveMapFromGoalMap(
                                Collections.singletonList( schemeSummaryResults.getObjectSummaryMap() ),
                                Collections.singletonList( schemeSummaryResults.getGoal() ) );
                        JobSchemeExperiment jobSchemeExperiment = new JobSchemeExperiment(
                                Integer.parseInt( schemeSummaryResults.getExperimentNumber() ),
                                new JobSchemeDetail( jobEntity.getName(), jobEntity.getId(),
                                        WorkflowStatus.getById( jobEntity.getStatus() ).getValue(), jobEntity.getJobDirectory() ),
                                new JobSchemeSummary( Collections.singletonList( schemeSummaryResults.getDesignSummaryMap() ),
                                        optObjectiveMap ) );
                        experiments.add( jobSchemeExperiment );
                    }
                }
            }

            String fileAddress = generateCSVFileByJobId( entityManager, jobImpl.getCreatedBy().getId(), jobImpl.getId().toString(),
                    jobImpl.getRequestHeaders().getJobAuthToken() );
            File source = new File( fileAddress );
            File destination = new File(
                    jobImpl.getWorkingDir().getPath() + File.separator + source.getName().replace( "CSV_Downloaded_", "" ) );
            JobSchemeFileSummary jobSchemeFileSummary = getJobSchemeFileSummary( jobImpl, experiments, source, destination );
            Map< String, String > optionScheme = workflowManager.getOptionScheme( entityManager,
                    String.valueOf( jobImpl.getCreatedBy().getId() ), String.valueOf( jobImpl.getWorkflowId() ), TAB_KEY_SCHEME_OPTIONS,
                    jobImpl.getWorkflowVersion().getId() );
            WFSchemeEntity schemeConfigDetails = ( WFSchemeEntity ) susDAO.getLatestObjectById( entityManager, WFSchemeEntity.class,
                    UUID.fromString( optionScheme.get( SCHEME.toLowerCase() ) ) );

            JobSchemeAlgo jobSchemeAlgo = new JobSchemeAlgo( SchemeCategoryEnum.getById( jobImpl.getJobSchemeCategory() ).getValue(),
                    schemeConfigDetails != null ? schemeConfigDetails.getName() : ConstantsString.EMPTY_STRING );
            return new JobScheme( new WorkflowScheme( jobImpl.getWorkflowId(), jobImpl.getWorkflowVersion().getId() ), experiments,
                    jobSchemeFileSummary, jobSchemeAlgo );

        } catch ( Exception e ) {
            log.error( POST_WORKFLOW + "job scheme preparation failed", e );
            throw new SusException( e, getClass() );
        }
    }

    /**
     * Gets job scheme file summary.
     *
     * @param jobImpl
     *         the job
     * @param experiments
     *         the experiments
     * @param source
     *         the source
     * @param destination
     *         the destination
     *
     * @return the job scheme file summary
     */
    private static JobSchemeFileSummary getJobSchemeFileSummary( Job jobImpl, List< JobSchemeExperiment > experiments, File source,
            File destination ) {
        JobSchemeFileSummary jobSchemeFileSummary = null;
        try {
            LinuxUtils.copyFileFromSrcPathToDestPathWithImpersonation( jobImpl.getCreatedBy().getUserUid(), source.getAbsolutePath(),
                    destination.getAbsolutePath() );

            String jsonFile = destination.getParent() + File.separator + FilenameUtils.getBaseName(
                    source.getName().replace( "CSV_Downloaded_", "" ) ) + ConstantsFileExtension.JSON;

            LinuxUtils.createFileWithImpersonation( jobImpl.getCreatedBy().getUserUid(), jsonFile );
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue( Paths.get( jsonFile ).toFile(), experiments );

            jobSchemeFileSummary = new JobSchemeFileSummary( jsonFile, destination.getAbsolutePath() );

        } catch ( IOException e ) {
            log.error( "Unable to copy file : ", e );
        }
        return jobSchemeFileSummary;
    }

    /**
     * Prepare objective map from goal map list.
     *
     * @param objectiveList
     *         the objective list
     * @param goalList
     *         the goal list
     *
     * @return the list
     */
    private List< Map< String, Object > > prepareObjectiveMapFromGoalMap( List< Map< String, Object > > objectiveList,
            List< Map< String, Object > > goalList ) {
        List< Map< String, Object > > newObjectiveList = new ArrayList<>();
        try {

            objectiveList.stream().forEach( objectiveMap -> {

                Map< String, Object > newObjectiveMap = new HashMap<>();
                objectiveMap.forEach( ( key, value ) -> {
                    newObjectiveMap.put( key, value );
                    String objectiveGoalKey = key + OBJ_VAR_GOAL_POSTFIX;
                    goalList.stream().filter( goalMap -> ( goalMap != null && goalMap.containsKey( objectiveGoalKey ) ) )
                            .forEach( goalMap -> {
                                newObjectiveMap.put( GOAL, goalMap.get( objectiveGoalKey ) );
                                newObjectiveMap.put( COMBINED_GOAL, goalMap.get( COMBINED_GOAL ) );
                            } );
                } );

                newObjectiveList.add( newObjectiveMap );

            } );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e, getClass() );
        }
        return newObjectiveList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionHosts isHostEnabled( UUID id ) {
        ExecutionHosts executionHost = new ExecutionHosts();
        if ( PropertiesManager.isHostEnable() ) {
            Hosts hostList = PropertiesManager.getHosts();
            if ( hostList != null && CollectionUtils.isNotEmpty( hostList.getExcutionHosts() ) ) {
                for ( ExecutionHosts host : hostList.getExcutionHosts() ) {
                    if ( host.getId().toString().equals( id.toString() ) ) {
                        executionHost = host;
                        break;
                    }
                }
            }
        }
        return executionHost;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response discardJob( String userId, String token, String jobId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( !String.valueOf( userId ).equals( ConstantsID.SUPER_USER_ID ) && (
                    !permissionManager.isPermitted( entityManager, String.valueOf( userId ),
                            jobId + ConstantsString.COLON + PermissionMatrixEnum.DELETE.getValue() ) || !permissionManager.isPermitted(
                            entityManager, String.valueOf( userId ),
                            jobId + ConstantsString.COLON + PermissionMatrixEnum.KILL.getValue() ) ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_DISCARD.getKey(), OBJECT ) );
            }
            Job jobDto = getJob( entityManager, UUID.fromString( userId ), jobId );

            if ( jobDto.isAutoDelete() ) {
                throw new SusException( "Auto deleted job can not be discarded" );
            }

            List< String > createdObjectList = getCreatedDataObjectsIdsForJobAndChildren( entityManager, UUID.fromString( userId ), jobId );

            if ( !createdObjectList.isEmpty() ) {
                // convert list to object list
                List< Object > filterItem = Arrays.asList( createdObjectList.toArray() );

                FiltersDTO filter = new FiltersDTO();
                filter.setItems( filterItem );

                SelectionResponseUI selectionObj = selectionManager.createSelection( entityManager, userId, SelectionOrigins.CONTEXT,
                        filter );
                SusResponseDTO response = null;
                try {
                    // delete all created objects by selection id
                    String urldelete = PropertiesManager.getLocationURL() + "/api/data/object/" + selectionObj.getId() + "?mode=bulk";
                    response = SuSClient.deleteRequest( urldelete, prepareLocationHeaders( token ) );
                } catch ( Exception e ) {
                    log.error( e.getMessage(), e );
                    throw new SusException( "DataObject Deletion Failed: job can not be discarded" + e.getMessage() );
                }

                if ( response.getSuccess() ) {
                    // going to delete all stagging dir/files related to selected job
                    JobEntity entityJob = jobDao.getLatestNonDeletedObjectById( entityManager, UUID.fromString( jobId ) );
                    deletingStaggingJobFiles( entityManager, entityJob );

                    // update job status in db : remainng
                    jobDto.setStatus( new Status( WorkflowStatus.DISCARD ) );
                    updateJob( entityManager, jobDto );

                    updateStatusOfChildJobs( entityManager, jobDto.getId(), jobDto.getJobType(), userId );

                    return ResponseUtils.success( "Job is Being discarded", true );
                } else {
                    return ResponseUtils.failure( response.getMessage().toString() );
                }

            } else {

                // going to delete all stagging dir/files related to selected job
                JobEntity entityJob = jobDao.getLatestNonDeletedObjectById( entityManager, UUID.fromString( jobId ) );
                deletingStaggingJobFiles( entityManager, entityJob );
                // update job status in db : remainng
                jobDto.setStatus( new Status( WorkflowStatus.DISCARD ) );
                updateJob( entityManager, jobDto );

                updateStatusOfChildJobs( entityManager, jobDto.getId(), jobDto.getJobType(), userId );

                return ResponseUtils.success( "Job has not createdObject  : job discarded", true );
            }
        } finally {
            entityManager.close();
        }

    }

    /**
     * Update status of child jobs.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     * @param type
     *         the type
     * @param userId
     *         the user id
     */
    private void updateStatusOfChildJobs( EntityManager entityManager, UUID id, int type, String userId ) {

        // get child jobs as DTOs
        List< JobEntity > allChildren = getAllChildrensOfMasterJob( entityManager, id, type );
        List< Job > childDtos = new ArrayList<>();
        for ( JobEntity childJob : allChildren ) {
            String childid = childJob.getId().toString();
            childDtos.add( getJob( entityManager, UUID.fromString( userId ), childid ) );
        }

        // if child jobs exists then update status
        if ( !childDtos.isEmpty() ) {
            for ( Job childJob : childDtos ) {
                childJob.setStatus( new Status( WorkflowStatus.DISCARD ) );
                updateJob( entityManager, childJob );
            }
        }

    }

    /**
     * Deleting stagging job files.
     *
     * @param entityManager
     *         the entity manager
     * @param jobEntity
     *         the job entity
     */
    private void deletingStaggingJobFiles( EntityManager entityManager, JobEntity jobEntity ) {
        Integer id = getJobIdByUUID( entityManager, jobEntity.getId() );
        final String bmwSuggestion = File.separator + jobEntity.getName() + "_" + id;
        if ( jobEntity.getJobType().toString().equals( String.valueOf( JobTypeEnums.WORKFLOW.getKey() ) ) ) {

            // deleting child job directory
            String staggingBase = PropertiesManager.getUserStagingPath( jobEntity.getCreatedBy().getUserUid() );
            String dirOrFilePath = staggingBase + File.separator + bmwSuggestion;
            LinuxUtils.deleteFileOrDirByPath( jobEntity.getCreatedBy().getUserUid(), dirOrFilePath );

            // deleting wf dir where all the wf files are being uplaoded
            String dirOrFilePathWF = staggingBase + File.separator + jobEntity.getWorkflowId().toString();
            LinuxUtils.deleteFileOrDirByPath( jobEntity.getCreatedBy().getUserUid(), dirOrFilePathWF );

        } else if ( jobEntity.getJobType().toString().equals( String.valueOf( JobTypeEnums.SCHEME.getKey() ) ) ) {

            // deleting wf dir where all the wf files are being uplaoded
            String staggingBase = PropertiesManager.getUserStagingPath( jobEntity.getCreatedBy().getUserUid() );
            String dirOrFilePathWF = staggingBase + File.separator + bmwSuggestion;
            LinuxUtils.deleteFileOrDirByPath( jobEntity.getCreatedBy().getUserUid(), dirOrFilePathWF );

            // getting all childs of master job to delete Dir from staging
            List< JobEntity > childJobs = jobDao.getAllChildrenOfMasterJob( entityManager, jobEntity.getId() );

            if ( childJobs != null && !childJobs.isEmpty() ) {
                for ( var child : childJobs ) {
                    Integer childId = getJobIdByUUID( entityManager, child.getId() );
                    final String bmwChildSuggestion = File.separator + child.getName() + "_" + childId;
                    String dirOrFilePath = staggingBase + File.separator + bmwChildSuggestion;
                    LinuxUtils.deleteFileOrDirByPath( child.getCreatedBy().getUserUid(), dirOrFilePath );

                    String dirOrFilePathSCHEME = staggingBase + File.separator + child.getWorkflowId().toString();
                    LinuxUtils.deleteFileOrDirByPath( jobEntity.getCreatedBy().getUserUid(), dirOrFilePathSCHEME );

                }
            }
        } else if ( jobEntity.getJobType().toString().equals( String.valueOf( JobTypeEnums.VARIANT.getKey() ) ) ) {

            runVarientClearStaggingRecursively( entityManager, jobEntity );

        }
    }

    /**
     * Run varient clear stagging recursively.
     *
     * @param entityManager
     *         the entity manager
     * @param jobEntity
     *         the job entity
     */
    private void runVarientClearStaggingRecursively( EntityManager entityManager, JobEntity jobEntity ) {
        List< JobEntity > innerEntityList = jobDao.getAllChildrenOfMasterJob( entityManager, jobEntity.getId() );
        if ( innerEntityList != null && !innerEntityList.isEmpty() ) {
            JobEntity childEntity = innerEntityList.get( 0 );
            LinuxUtils.deleteFileOrDirByPath( childEntity.getCreatedBy().getUserUid(), childEntity.getJobDirectory() );
            runVarientClearStaggingRecursively( entityManager, childEntity );
        }
    }

    /**
     * Checks if is restricted.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     */
    private void isRestricted( EntityManager entityManager, UUID userId ) {
        UserDTO userDTO = userCommonManager.getUserById( entityManager, userId );
        if ( userDTO != null && userDTO.getRestricted().equals( RESTRICTED_USER ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_RESTRICTED_TO_SYSTEM.getKey(), userDTO.getName() ) );
        }
    }

    /**
     * Sets the job ids DAO.
     *
     * @param jobIdsDAO
     *         the new job ids DAO
     */
    public void setJobIdsDAO( JobIdsDAO jobIdsDAO ) {
        this.jobIdsDAO = jobIdsDAO;
    }

    /**
     * Generate job logs zip file.
     *
     * @param id
     *         the id
     * @param token
     *         the token
     *
     * @return the string
     */
    @Override
    public String generateJobLogsZipFile( String id, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        UserEntity user = tokenManager.getUserEntityByToken( entityManager, token );

        try {
            JobEntity masterEntity = jobDao.getLatestObjectById( entityManager, JobEntity.class, UUID.fromString( id ) );

            if ( masterEntity.isAutoDelete() ) {
                throw new SusException( "Auto deleted job can not be discarded" );
            }

            if ( ( !user.getId().toString().equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager,
                    user.getId().toString(), id + ConstantsString.COLON + PermissionMatrixEnum.READ.getValue() ) ) ) {
                throw new SusException( "You don't have permission to Download job logs" );
            }

            List< JobEntity > childJobList = jobDao.getAllChildrenOfMasterJob( entityManager, UUID.fromString( id ) );

            if ( ( childJobList == null || childJobList.isEmpty() ) && masterEntity.getJobType() != JobTypeEnums.WORKFLOW.getKey() ) {
                throw new SusException( "No job logs found to download for :" + masterEntity.getName() );
            }

            String jobNameInTemp = PropertiesManager.getDefaultServerTempPath() + File.separator + masterEntity.getCreatedBy().getUserUid()
                    + File.separator + masterEntity.getName() + "_" + new SimpleDateFormat( "h_m_s" ).format( new Date() );
            LinuxUtils.createDirectory( user.getUserUid(),
                    PropertiesManager.getDefaultServerTempPath() + File.separator + user.getUserUid() );
            LinuxUtils.createDirectory( user.getUserUid(), jobNameInTemp );
            File mkDirMasterTemp = new File( jobNameInTemp );

            List< File > dirPathList = new ArrayList<>();

            if ( masterEntity.getJobType() == JobTypeEnums.WORKFLOW.getKey() ) {
                List< JobEntity > wfList = new ArrayList<>();
                wfList.add( masterEntity );
                prepareJobFileAndDirPathsForZipFile( user.getUserUid(), wfList, mkDirMasterTemp, dirPathList );
            }
            if ( masterEntity.getJobType() == JobTypeEnums.SCHEME.getKey() ) {
                prepareJobFileAndDirPathsForZipFile( user.getUserUid(), childJobList, mkDirMasterTemp, dirPathList );
            }
            if ( masterEntity.getJobType() == JobTypeEnums.VARIANT.getKey() ) {
                addDirAndFilesTOZipLogResursively( entityManager, user.getUserUid(), childJobList, mkDirMasterTemp, dirPathList );
            }

            if ( !dirPathList.isEmpty() ) {
                String zipFilePath =
                        PropertiesManager.getDefaultServerTempPath() + File.separator + masterEntity.getCreatedBy().getUserUid()
                                + File.separator + mkDirMasterTemp.getName() + File.separator + masterEntity.getName() + ".zip";

                try {
                    ZipUtils.zip( dirPathList, zipFilePath );
                } catch ( IOException e ) {
                    log.error( "job log zip failed : " + e.getMessage(), e );
                    throw new SusException( "job log zip failed : " + e.getMessage() );
                }

                return zipFilePath;
            } else {
                throw new SusException( "Download Failed: Job Log not available in working Directory" );
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Adds the dir and files TO zip log resursively.
     *
     * @param entityManager
     *         the entity manager
     * @param userName
     *         the user name
     * @param childJobList
     *         the child job list
     * @param mkDirMasterTemp
     *         the mk dir master temp
     * @param dirPathList
     *         the dir path list
     */
    private void addDirAndFilesTOZipLogResursively( EntityManager entityManager, String userName, List< JobEntity > childJobList,
            File mkDirMasterTemp, List< File > dirPathList ) {
        prepareJobFileAndDirPathsForZipFile( userName, childJobList, mkDirMasterTemp, dirPathList );

        if ( childJobList != null && !childJobList.isEmpty() ) {

            childJobList.get( 0 ).getId();
            List< JobEntity > grandChildJobList = jobDao.getAllChildrenOfMasterJob( entityManager, childJobList.get( 0 ).getId() );

            if ( grandChildJobList != null && !grandChildJobList.isEmpty() ) {
                addDirAndFilesTOZipLogResursively( entityManager, userName, grandChildJobList, mkDirMasterTemp, dirPathList );
            }
        }
    }

    /**
     * Gets the token manager.
     *
     * @return the token manager
     */
    public UserTokenManager getTokenManager() {
        return tokenManager;
    }

    /**
     * Sets the token manager.
     *
     * @param tokenManager
     *         the new token manager
     */
    public void setTokenManager( UserTokenManager tokenManager ) {
        this.tokenManager = tokenManager;
    }

    /**
     * Gets the user manager.
     *
     * @return the user manager
     */
    public WorkflowUserManager getUserManager() {
        return userManager;
    }

    /**
     * Gets the permission manager.
     *
     * @return the permission manager
     */
    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    /**
     * Prepare job file and dir paths for zip file.
     *
     * @param userName
     *         the user name
     * @param assemblyList
     *         the assembly list
     * @param mkDirMasterTemp
     *         the mk dir master temp
     * @param dirPathList
     *         the dir path list
     */
    private void prepareJobFileAndDirPathsForZipFile( String userName, List< JobEntity > assemblyList, File mkDirMasterTemp,
            List< File > dirPathList ) {
        for ( JobEntity jobEntity : assemblyList ) {

            File mainJobDir = new File( jobEntity.getJobDirectory() );

            if ( mainJobDir.exists() ) {

                File mkDirTemp = new File( mkDirMasterTemp.getAbsolutePath() + File.separator + jobEntity.getName() );
                mkDirTemp.mkdirs();
                FileUtils.setGlobalAllFilePermissions( mkDirTemp );

                for ( File f : mainJobDir.listFiles() ) {

                    String regex = jobEntity.getName() + ".*.log$";

                    if ( Pattern.matches( regex, f.getName() ) ) {
                        LinuxUtils.copyFileFromSrcPathToDestPath( userName, f.getAbsolutePath(),
                                mkDirTemp.getAbsolutePath() + File.separator + f.getName() );
                    }
                }
                dirPathList.add( mkDirTemp );
            }
        }
    }

    @Override
    public FilteredResponse< Job > getFilteredWorkflowRelatedJobsList( UUID userId, FiltersDTO filter, String workflowId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }
            isRestricted( entityManager, userId );
            List< Job > allJobsList = new ArrayList<>();
            replaceEnumsInJobFilters( filter );

            final List< JobEntity > jobEntityList = jobDao.getAllJobsByWorkflowId( entityManager, userId, filter, workflowId );

            if ( CollectionUtil.isNotEmpty( jobEntityList ) ) {
                for ( final JobEntity jobEntity : jobEntityList ) {
                    allJobsList.add( prepareJob( entityManager, jobEntity ) );
                }
            }
            return PaginationUtil.constructFilteredResponse( filter, allJobsList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getJobInputParameters( UUID userId, String jobId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< UIFormItem > inputParameters = new ArrayList<>();
        try {
            JobEntity jobEntity = jobDao.getJob( entityManager, UUID.fromString( jobId ) );
            if ( JobTypeEnums.WORKFLOW.getKey() == jobEntity.getJobType() ) {
                getJobInputParametersForWorkflowJob( entityManager, userId, jobId, inputParameters, jobEntity );
            } else if ( JobTypeEnums.SCHEME.getKey() == jobEntity.getJobType() ) {
                // in-case of scheme, all child jobs will have the same AOR parameters. so just retrieve the parameters of first child-job
                // you find
                List< JobEntity > childJobs = jobDao.getAllChildrenOfMasterJob( entityManager, jobEntity.getId() );
                if ( CollectionUtils.isNotEmpty( childJobs ) ) {
                    getJobInputParametersForWorkflowJob( entityManager, userId, jobId, inputParameters,
                            childJobs.stream().findFirst().get() );
                }
            }

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );

        } finally {
            entityManager.close();
        }
        return GUIUtils.createFormFromItems( inputParameters );
    }

    /**
     * Gets job input parameters for workflow job.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param jobId
     *         the job id
     * @param inputParameters
     *         the input parameters
     * @param jobEntity
     *         the job entity
     */
    private void getJobInputParametersForWorkflowJob( EntityManager entityManager, UUID userId, String jobId,
            List< UIFormItem > inputParameters, JobEntity jobEntity ) {
        WorkflowDTO workflowDTO = workflowManager.getWorkflowByIdAndVersionId( entityManager, userId, jobEntity.getWorkflowId().toString(),
                jobEntity.getWorkflow().getVersionId() );
        Map< String, Object > definition = workflowDTO.getDefinition();
        // job attributes
        getJobAtributesFieldsForJob( jobEntity, inputParameters, definition );

        // Custom flag fields
        getFilledCustomFlagFieldsForJob( entityManager, userId, jobId, inputParameters, workflowDTO );

        // workflow's ask-on-run fields

        getWorkflowAORFieldsForJob( inputParameters, jobEntity, definition );

    }

    /**
     * Gets job atributes fields for job.
     *
     * @param jobEntity
     *         the job entity
     * @param inputParameters
     *         the input parameters
     * @param definition
     *         the definition
     */
    private void getJobAtributesFieldsForJob( JobEntity jobEntity, List< UIFormItem > inputParameters, Map< String, Object > definition ) {
        if ( definition != null ) {
            Map< String, Object > jobAttribs = ( Map< String, Object > ) definition.get( "job" );
            var params = jobAttribs.get( "changeOnRun" );
            if ( params instanceof String aorParameter && StringUtils.isNotNullOrEmpty( aorParameter ) ) {
                inputParameters.add( prepareJobAORFieldForJob( jobEntity, aorParameter, jobAttribs ) );
            } else if ( params instanceof List ) {
                List< String > jobAORParameters = ( List< String > ) params;
                jobAORParameters.forEach(
                        aorParameter -> inputParameters.add( prepareJobAORFieldForJob( jobEntity, aorParameter, jobAttribs ) ) );
            }
        }
    }

    /**
     * Prepare job aor field for job ui form item.
     *
     * @param jobEntity
     *         the job entity
     * @param aorParameter
     *         the aor parameter
     * @param jobAttribs
     *         the job attribs
     *
     * @return the ui form item
     */
    private UIFormItem prepareJobAORFieldForJob( JobEntity jobEntity, String aorParameter, Map< String, Object > jobAttribs ) {
        UIFormItem uiFormItem = GUIUtils.createFormItem();
        uiFormItem.setName( aorParameter );
        switch ( aorParameter ) {
            case RUN_ON_FIELD -> {
                uiFormItem.setValue( jobEntity.getRunsOn().getName() );
                uiFormItem.setLabel( "Job " + MessageBundleFactory.getMessage( "4100135x4" ) );
            }
            case NAME_FIELD -> {
                uiFormItem.setValue( jobEntity.getName() );
                uiFormItem.setLabel( "Job " + MessageBundleFactory.getMessage( "3000032x4" ) );
            }
            case MAX_EXE_TIME_FIELD -> {
                uiFormItem.setValue( prepareJobMaxTime( jobEntity ) );
                uiFormItem.setLabel( "Job " + MessageBundleFactory.getMessage( "4100136x4" ) );
            }
            case DESCRIPTION_FIELD -> {
                uiFormItem.setValue( jobEntity.getDescription() );
                uiFormItem.setLabel( "Job " + MessageBundleFactory.getMessage( "3000011x4" ) );
            }
        }
        uiFormItem.setReadonly( Boolean.TRUE );
        uiFormItem.setType( FieldTypes.TEXT.getType() );
        return uiFormItem;
    }

    /**
     * Prepare job max time.
     *
     * @param jobEntity
     *         the job entity
     *
     * @return the int
     */
    private int prepareJobMaxTime( JobEntity jobEntity ) {
        if ( null != jobEntity.getJobMaxExecutionTime() ) {
            return DateUtils.getTimeDifferenceInMinutes( jobEntity.getJobMaxExecutionTime(), jobEntity.getStartedOn() ).intValue()
                    + ConstantsInteger.INTEGER_VALUE_ONE;
        } else {
            return ConstantsInteger.UNLIMITED_TIME_FOR_ELEMENT;
        }

    }

    /**
     * Prepare job max time.
     *
     * @param job
     *         the job
     *
     * @return the int
     */
    private int prepareJobMaxTime( Job job ) {
        if ( null != job.getJobMaxExecutionTime() ) {
            return DateUtils.getTimeDifferenceInMinutes( job.getJobMaxExecutionTime(), job.getSubmitTime() ).intValue()
                    + ConstantsInteger.INTEGER_VALUE_ONE;
        } else {
            return ConstantsInteger.UNLIMITED_TIME_FOR_ELEMENT;
        }

    }

    /**
     * Gets workflow aor fields for job.
     *
     * @param inputParameters
     *         the input parameters
     * @param jobEntity
     *         the job entity
     * @param definition
     *         the definition
     */
    private void getWorkflowAORFieldsForJob( List< UIFormItem > inputParameters, JobEntity jobEntity, Map< String, Object > definition ) {
        if ( definition != null ) {
            String json;
            json = JsonUtils.toJson( definition );
            WorkflowDefinitionDTO workflowDefinitionDTO = JsonUtils.jsonToObject( json, WorkflowDefinitionDTO.class );
            LinkedHashMap< String, Object > askOnRunMap = getAskOnRunMapFromJobEntity( jobEntity );
            if ( askOnRunMap != null ) {
                workflowDefinitionDTO.getElements().getNodes().forEach( wfElement -> {
                    if ( wfElement.getData().getFields().stream().anyMatch( Field::isChangeOnRun ) ) {
                        inputParameters.add( prepareSectionFieldForElement( wfElement ) );
                        wfElement.getData().getFields().stream().filter( Field::isChangeOnRun ).forEach( field -> {
                            Map< String, Object > fieldMap = ( Map< String, Object > ) askOnRunMap.get( field.getName() );
                            fieldMap.put( TOOL_TIP,
                                    getFieldToolTipFromJson( json, wfElement.getData().getId(), fieldMap.get( NAME ).toString() ) );
                            UIFormItem paramUI = prepareSelectUIItemFromFieldAsReadOnly( fieldMap );
                            if ( FieldTypes.OBJECT_PARSER.getType().equals( String.valueOf( field.getType() ) ) && String.valueOf(
                                    field.getTemplateType() ).equalsIgnoreCase( FieldTemplates.CUSTOM_VARIABLE.getValue() ) ) {
                                paramUI.setValue( field.getValue() );
                            }
                            inputParameters.add( paramUI );
                        } );
                    }
                } );
            }
        }
    }

    /**
     * Adds the tool tip to map.
     *
     * @param json
     *         the json
     * @param targetNodeId
     *         the target node id
     * @param targetFieldName
     *         the target field name
     *
     * @return the field tool tip from json
     */
    private String getFieldToolTipFromJson( String json, String targetNodeId, String targetFieldName ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree( json );
            JsonNode nodesArray = jsonNode.path( ELEMENTS ).path( NODES );
            for ( JsonNode node : nodesArray ) {
                String nodeId = node.path( DATA ).path( ID ).asText();
                if ( nodeId.equals( targetNodeId ) ) {
                    JsonNode fields = node.path( DATA ).path( FIELDS );
                    for ( JsonNode field : fields ) {
                        String fieldName = field.path( NAME ).asText();
                        if ( fieldName.equals( targetFieldName ) ) {
                            return field.path( TOOL_TIP ).asText();
                        }
                    }
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Gets filled custom flag fields for job.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param jobId
     *         the job id
     * @param inputParameters
     *         the input parameters
     * @param workflowDTO
     *         the workflow dto
     */
    private void getFilledCustomFlagFieldsForJob( EntityManager entityManager, UUID userId, String jobId,
            List< UIFormItem > inputParameters, WorkflowDTO workflowDTO ) {
        var globalVariables = getGlobalVariablesFromJobIdAndUserId( entityManager, UUID.fromString( jobId ), userId );
        if ( globalVariables != null && workflowDTO.getCustomFlags() != null ) {
            workflowDTO.getCustomFlags()
                    .forEach( plugin -> this.addCustomFlagFieldsToInputParameters( plugin, globalVariables, inputParameters, plugin ) );
        }
    }

    /**
     * Prepare section field for element ui form item.
     *
     * @param wfElement
     *         the wf element
     *
     * @return the ui form item
     */
    private UIFormItem prepareSectionFieldForElement( WorkflowElement wfElement ) {
        SectionFormItem uiFormItem = ( SectionFormItem ) GUIUtils.createFormItem( FormItemType.SECTION );

        uiFormItem.setType( FieldTypes.SECTION.getType() );
        uiFormItem.setReadonly( Boolean.TRUE );
        uiFormItem.setTitle( wfElement.getData().getName() );
        uiFormItem.setName( wfElement.getData().getName() );
        return uiFormItem;
    }

    /**
     * Gets ask on run map from job entity.
     *
     * @param jobEntity
     *         the job entity
     *
     * @return the ask on run map from job entity
     */
    private LinkedHashMap< String, Object > getAskOnRunMapFromJobEntity( JobEntity jobEntity ) {
        LinkedHashMap< String, Object > askOnRunMap = null;
        if ( null != jobEntity.getAskOnRunParameters() ) {
            Map< String, Object > askOnRunParams = new LinkedHashMap<>();
            String askOnRunJsonString = ByteUtil.convertByteToString( jobEntity.getAskOnRunParameters() );
            if ( StringUtils.isNotNullOrEmpty( askOnRunJsonString ) && JsonUtils.isValidJSON( askOnRunJsonString ) ) {
                askOnRunMap = ( LinkedHashMap< String, Object > ) JsonUtils.jsonToMap( askOnRunJsonString, askOnRunParams );
            }
        }
        return askOnRunMap;
    }

    /**
     * Add custom flag fields to input parameters.
     *
     * @param plugin
     *         the plugin
     * @param globalVariables
     *         the global variables
     * @param inputParameters
     *         the input parameters
     * @param originalPlugin
     *         the original plugin
     */
    private void addCustomFlagFieldsToInputParameters( String plugin, Map< String, Object > globalVariables,
            List< UIFormItem > inputParameters, String originalPlugin ) {
        List< Map< String, Object > > fields = workflowManager.getFieldsFromPluginPath( plugin );
        fields.forEach( field -> {
            inputParameters.add( prepareSelectUIItemFromField( field, originalPlugin, globalVariables ) );
            if ( field.get( TYPE ).equals( FieldTypes.SELECTION.getType() ) ) {
                String varName = "{{" + plugin + ConstantsString.DOT + field.get( NAME_FIELD ).toString() + "}}";
                String newPath = plugin + ConstantsString.FORWARD_SLASH + globalVariables.get( varName );
                addCustomFlagFieldsToInputParameters( newPath, globalVariables, inputParameters, originalPlugin );
            }
        } );
    }

    /**
     * Prepare select ui item from field as read only ui form item.
     *
     * @param field
     *         the field
     *
     * @return the ui form item
     */
    private UIFormItem prepareSelectUIItemFromFieldAsReadOnly( Map< String, Object > field ) {
        UIFormItem item = workflowManager.prepareSelectUIItemFromField( field );
        if ( FieldTypes.SELECTION.getType().equals( String.valueOf( field.get( TYPE ) ) ) ) {
            var options = ( ( SelectFormItem ) item ).getOptions();
            if ( ( options != null ) && ( options.isEmpty() || options.stream().findFirst().get().getName().isEmpty() ) ) {
                SelectOptionsUI objectItem = new SelectOptionsUI();
                objectItem.setId( String.valueOf( item.getValue() ) );
                objectItem.setName( String.valueOf( item.getValue() ) );
                ( ( SelectFormItem ) item ).getOptions().add( objectItem );
            }
        }
        if ( FieldTypes.OBJECT.getType().equals( String.valueOf( field.get( TYPE ) ) ) ) {
            item.setExternal( FieldTypes.OBJECT.getType() );
        }
        item.setReadonly( Boolean.TRUE );
        return item;

    }

    /**
     * Prepare select ui item from field ui form item.
     *
     * @param field
     *         the field
     * @param plugin
     *         the plugin
     * @param globalVariables
     *         the global variables
     *
     * @return the ui form item
     */
    private UIFormItem prepareSelectUIItemFromField( Map< String, Object > field, String plugin, Map< String, Object > globalVariables ) {
        SelectFormItem item = ( SelectFormItem ) prepareSelectUIItemFromFieldAsReadOnly( field );
        Object customFlagValue = getFieldValueFromGlobalVariableMap( plugin, field, globalVariables );
        if ( customFlagValue != null ) {
            item.setValue( customFlagValue );
        }
        return item;
    }

    /**
     * Gets field value from global variable map.
     *
     * @param plugin
     *         the plugin
     * @param field
     *         the field
     * @param globalVariables
     *         the global variables
     *
     * @return the field value from global variable map
     */
    private Object getFieldValueFromGlobalVariableMap( String plugin, Map< String, Object > field, Map< String, Object > globalVariables ) {
        if ( plugin == null || plugin.isEmpty() ) {
            return null;
        }
        if ( field.containsKey( ConstantsCustomFlagFields.NAME ) ) {
            String variableName = "{{" + plugin + ConstantsString.DOT + field.get( ConstantsCustomFlagFields.NAME ) + "}}";
            if ( globalVariables != null && globalVariables.containsKey( variableName ) ) {
                return globalVariables.get( variableName );
            }
        }
        return null;
    }

    /**
     * Sets job global variable dao.
     *
     * @param jobGlobalVariableDAO
     *         the job global variable dao
     */
    public void setJobGlobalVariableDAO( JobGlobalVariableDAO jobGlobalVariableDAO ) {
        this.jobGlobalVariableDAO = jobGlobalVariableDAO;
    }

    /**
     * Sets entity manager factory.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Sets the sus DAO.
     *
     * @param jobEntityDAO
     *         the susDAO to set
     */
    public void setJobEntityDAO( SuSGenericObjectDAO< JobEntity > jobEntityDAO ) {
        this.jobEntityDAO = jobEntityDAO;
    }

    /**
     * Gets the sus DAO.
     *
     * @return the susDAO
     */
    @Override
    public SuSGenericObjectDAO< JobEntity > getJobEntityDAO() {
        return jobEntityDAO;
    }

    /**
     * Sets the permission manager.
     *
     * @param permissionManager
     *         the permissionManager to set
     */
    public void setPermissionManager( PermissionManager permissionManager ) {
        this.permissionManager = permissionManager;
    }

    /**
     * Getter for the Job Data Access Object.
     *
     * @return JobDAO Object
     */
    @Override
    public JobDAO getJobDao() {
        return jobDao;
    }

    /**
     * Sets the UserManager via dependency injection.
     *
     * @param userManager
     *         the new UserManager object
     */
    public void setUserManager( WorkflowUserManager userManager ) {
        this.userManager = userManager;
    }

    /**
     * Getter for the Workflow Manager Object.
     *
     * @return WorkflowManager Object
     */
    @Override
    public WorkflowManager getWorkflowManager() {
        return workflowManager;
    }

    /**
     * Sets the workflow manager.
     *
     * @param workflowManager
     *         the new workflow manager
     */
    public void setWorkflowManager( WorkflowManager workflowManager ) {
        this.workflowManager = workflowManager;
    }

    /**
     * Sets the job data access object.
     *
     * @param jobDao
     *         the new job data access object
     */
    public void setJobDao( JobDAO jobDao ) {
        this.jobDao = jobDao;
    }

    /**
     * Gets the context menu manager.
     *
     * @return the context menu manager
     */
    public ContextMenuManager getContextMenuManager() {
        return contextMenuManager;
    }

    /**
     * Sets the context menu manager.
     *
     * @param contextMenuManager
     *         the new context menu manager
     */
    public void setContextMenuManager( ContextMenuManager contextMenuManager ) {
        this.contextMenuManager = contextMenuManager;
    }

    /**
     * Gets the license manager.
     *
     * @return the license manager
     */
    public LicenseManager getLicenseManager() {
        return licenseManager;
    }

    /**
     * Gets the config manager.
     *
     * @return the config manager
     */
    public ObjectTypeConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Sets the config manager.
     *
     * @param configManager
     *         the new config manager
     */
    public void setConfigManager( ObjectTypeConfigManager configManager ) {
        this.configManager = configManager;
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
     * Gets the user common manager.
     *
     * @return the user common manager
     */
    public UserCommonManager getUserCommonManager() {
        return userCommonManager;
    }

    /**
     * Sets the user common manager.
     *
     * @param userCommonManager
     *         the new user common manager
     */
    public void setUserCommonManager( UserCommonManager userCommonManager ) {
        this.userCommonManager = userCommonManager;
    }

    /**
     * Gets the location manager.
     *
     * @return the location manager
     */
    public LocationManager getLocationManager() {
        return locationManager;
    }

    /**
     * Sets the location manager.
     *
     * @param locationManager
     *         the new location manager
     */
    public void setLocationManager( LocationManager locationManager ) {
        this.locationManager = locationManager;
    }

    /**
     * Gets the sus entity DAO.
     *
     * @return the sus entity DAO
     */
    public SuSGenericObjectDAO< SuSEntity > getSusDAO() {
        return susDAO;
    }

    /**
     * Sets the sus entity DAO.
     *
     * @param susDAO
     *         the new sus entity DAO
     */
    public void setSusDAO( SuSGenericObjectDAO< SuSEntity > susDAO ) {
        this.susDAO = susDAO;
    }

    /**
     * Gets the objective variable dao.
     *
     * @return the objective variable dao
     */
    @Override
    public VariableDAO getVariableDAO() {
        return variableDAO;
    }

    /**
     * Gets the selection manager.
     *
     * @return the selection manager
     */
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    /**
     * Sets the selection manager.
     *
     * @param selectionManager
     *         the new selection manager
     */
    public void setSelectionManager( SelectionManager selectionManager ) {
        this.selectionManager = selectionManager;
    }

    /**
     * Sets variable dao.
     *
     * @param variableDAO
     *         the variable dao
     */
    public void setVariableDAO( VariableDAO variableDAO ) {
        this.variableDAO = variableDAO;
    }

    /**
     * Sets document dao.
     *
     * @param documentDAO
     *         the document dao
     */
    public void setDocumentDAO( DocumentDAO documentDAO ) {
        this.documentDAO = documentDAO;
    }

}