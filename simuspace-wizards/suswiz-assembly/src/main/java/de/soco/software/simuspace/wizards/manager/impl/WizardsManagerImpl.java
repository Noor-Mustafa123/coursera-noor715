package de.soco.software.simuspace.wizards.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.server.manager.WorkflowManager;
import de.soco.software.simuspace.suscore.authentication.manager.AuthManager;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.cb2.model.BmwCaeBenchScenerioTreeDTO;
import de.soco.software.simuspace.suscore.common.cb2.model.BmwCaeBenchTreeDTO;
import de.soco.software.simuspace.suscore.common.cb2.model.Cb2ScenerioTreeChildrenDTO;
import de.soco.software.simuspace.suscore.common.cb2.model.Cb2TreeChildrenDTO;
import de.soco.software.simuspace.suscore.common.client.CB2Client;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.AuditTrailRelationType;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.enums.BmwCaeBenchEnums;
import de.soco.software.simuspace.suscore.common.enums.Cb2OperationEnum;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.VariableDropDownEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobRelationTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SectionFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.formitem.impl.TableFormItem;
import de.soco.software.simuspace.suscore.common.model.BindTo;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchInputDeckDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchKeyResultsDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchProjectsDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchReportDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchResultDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchSenarioDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchStoryBoardDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchSubModelDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchSubModelTableDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchVariantDTO;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.DummyTypeDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectsSubModelDTO;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.VariantSubModelDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.model.WorkFlowAdditionalAttributeDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.Renderer;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.SubTabsUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.EncryptAndDecryptUtils;
import de.soco.software.simuspace.suscore.common.util.ExecutionHosts;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.Hosts;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.OSValidator;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.PasswordUtils;
import de.soco.software.simuspace.suscore.common.util.PythonUtils;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.core.dao.LinkDAO;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.BmwCaeBenchDAO;
import de.soco.software.simuspace.suscore.data.common.dao.BmwCaeBenchNodeDAO;
import de.soco.software.simuspace.suscore.data.common.dao.ContextMenuRelationDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.AdditionalFiles;
import de.soco.software.simuspace.suscore.data.common.model.Cb2VariantWizardDTO;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchEntity;
import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchNodeEntity;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.DummyTypeEntity;
import de.soco.software.simuspace.suscore.data.entity.LoadCaseEntity;
import de.soco.software.simuspace.suscore.data.entity.LoadCaseWizardEntity;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionItemEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.SubmitLoadcaseEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VariantEntity;
import de.soco.software.simuspace.suscore.data.entity.VariantWizardEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ClickManager;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.DataObjectDTO;
import de.soco.software.simuspace.suscore.data.model.LoadCaseDTO;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.data.model.VariantDTO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.simuspace.suscore.location.manager.LocationManager;
import de.soco.software.simuspace.suscore.object.dao.SubmitLoadcaseDAO;
import de.soco.software.simuspace.suscore.object.manager.DataObjectManager;
import de.soco.software.simuspace.suscore.object.manager.SuSObjectTreeManager;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.wizards.constants.WizardsFeaturesEnum;
import de.soco.software.simuspace.wizards.dao.DummyTypeDAO;
import de.soco.software.simuspace.wizards.dao.LoadCaseDAO;
import de.soco.software.simuspace.wizards.dao.LoadcaseWizDAO;
import de.soco.software.simuspace.wizards.dao.WizDAO;
import de.soco.software.simuspace.wizards.manager.Cb2DummyManager;
import de.soco.software.simuspace.wizards.manager.LoadcaseManager;
import de.soco.software.simuspace.wizards.manager.WizardsManager;
import de.soco.software.simuspace.wizards.model.DummyVariantDTO;
import de.soco.software.simuspace.wizards.model.LoadcaseSubmitDTO;
import de.soco.software.simuspace.wizards.model.LoadcaseWizardDTO;
import de.soco.software.simuspace.wizards.model.VariantLoadcaseDTO;
import de.soco.software.simuspace.wizards.model.VariantObjectDTO;
import de.soco.software.simuspace.wizards.model.VariantSubmitDTO;
import de.soco.software.simuspace.wizards.model.VariantWizardDTO;
import de.soco.software.simuspace.wizards.model.run.LoadcaseWFModel;
import de.soco.software.simuspace.wizards.model.run.SubmitLoadcase;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.dto.WorkflowDefinitionDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowElement;
import de.soco.software.simuspace.workflow.dto.WorkflowModel;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.SystemWorkflow;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.JobParametersImpl;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.ProgressBar;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;

/**
 * The Class WizardsManagerImpl.
 */
@Log4j2
public class WizardsManagerImpl implements WizardsManager, ClickManager {

    /**
     * The constant NAME.
     */
    private static final String NAME = "name";

    /**
     * The constant LABEL.
     */
    private static final String LABEL = "label";

    /**
     * The constant SUBMODELS_JSON_FILE_PATH.
     */
    private static final String SUBMODELS_JSON_FILE_PATH = PropertiesManager.getUserDefaultServerTempPath( "%s" ) + "/submodelsTree.json";

    /**
     * The constant SCENARIO_JSON_FILE_PATH.
     */
    private static final String SCENARIO_JSON_FILE_PATH =
            ConstantsKaraf.KARAF_HOME + File.separator + ConstantsKaraf.KARAF_CONF + "/scenerioTress.json";

    /**
     * The Constant SELECT_WORKFLOW.
     */
    private static final String SELECT_WORKFLOW = "select-workflow";

    /**
     * The Constant GET_CUSTOM_ATTRIBUTES.
     */
    private static final String GET_CUSTOM_ATTRIBUTES = "getCustomAttributes";

    /**
     * The Constant GET_VERSION.
     */
    private static final String GET_VERSION = "getVersion";

    /**
     * The Constant GET_ID.
     */
    private static final String GET_ID = "getId";

    /**
     * The Constant GET_DESCRIPTION.
     */
    private static final String GET_DESCRIPTION = "getDescription";

    /**
     * The Constant GET_NAME.
     */
    private static final String GET_NAME = "getName";

    /**
     * The Constant LINK_TYPE_YES.
     */
    private static final String LINK_TYPE_YES = "Yes";

    /**
     * The Constant LINK_TYPE_NO.
     */
    private static final String LINK_TYPE_NO = "No";

    /**
     * The Constant RUN_MODE.
     */
    private static final String RUN_MODE = "server";

    /**
     * The Constant EXECUTION.
     */
    private static final String EXECUTION = "execution";

    /**
     * The Constant HOST.
     */
    private static final String HOST = "host";

    /**
     * The Constant MUST_CHOSE_OPTION.
     */
    private static final String MUST_CHOSE_OPTION = "Must Chose Option";

    /**
     * The Constant REQUIRED.
     */
    private static final String REQUIRED = "required";

    /**
     * The Constant LIVESEARCH.
     */
    private static final String LIVESEARCH = "liveSearch";

    /**
     * The Constant SELECT.
     */
    private static final String SELECT = "select";

    /**
     * The Constant INFO.
     */
    private static final String INFO = "Info";

    /**
     * The url.
     */
    private String url = null;

    /**
     * The Node id.
     */
    private String nodeId = null;

    /**
     * The tree manager.
     */
    private SuSObjectTreeManager treeManager;

    /**
     * The data manager.
     */
    private DataObjectManager dataObjectManager;

    /**
     * The sus DAO.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The wiz DAO.
     */
    private WizDAO wizDAO;

    /**
     * The loadcase wiz DAO.
     */
    private LoadcaseWizDAO loadcaseWizDAO;

    /**
     * The load case DAO.
     */
    private LoadCaseDAO loadCaseDAO;

    /**
     * The selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The loadcase manager.
     */
    private LoadcaseManager loadcaseManager;

    /**
     * The workflow manager.
     */
    private WorkflowManager workflowManager;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The location manager.
     */
    private LocationManager locationManager;

    /**
     * The job manager.
     */
    private JobManager jobManager;

    /**
     * The relation DAO.
     */
    private ContextMenuRelationDAO relationDAO;

    /**
     * The relation DAO.
     */
    private Cb2DummyManager cb2DummyManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The dummy type DAO.
     */
    private DummyTypeDAO dummyTypeDAO;

    /**
     * The bmw cae bench DAO.
     */
    private BmwCaeBenchDAO bmwCaeBenchDAO;

    /**
     * The bmw cae bench node DAO.
     */
    private BmwCaeBenchNodeDAO bmwCaeBenchNodeDAO;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The object type config manager.
     */
    private ObjectTypeConfigManager configManager;

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private LinkDAO linkDao;

    /**
     * The common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The submit loadcase DAO.
     */
    private SubmitLoadcaseDAO submitLoadcaseDAO;

    /**
     * The auth manager.
     */
    private AuthManager authManager;

    /**
     * The Entity Manager Factory
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The User token manager
     */
    private UserTokenManager tokenManager;

    /**
     * Sets the config manager.
     *
     * @param configManager
     *         the configManager to set
     */
    public void setConfigManager( ObjectTypeConfigManager configManager ) {
        this.configManager = configManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubTabsItem getTabsUI( String userId, String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< SubTabsUI > subTabsUIs = new ArrayList<>();
            subTabsUIs.add( new SubTabsUI( "General" ) );
            subTabsUIs.add( new SubTabsUI( "Objects" ) );
            subTabsUIs.add( new SubTabsUI( "Assembly" ) );
            subTabsUIs.add( new SubTabsUI( "Solver" ) );
            subTabsUIs.add( new SubTabsUI( "Post" ) );
            subTabsUIs.add( new SubTabsUI( "Submit" ) );
            SuSEntity varEntity = susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( id ) );
            return new SubTabsItem( id, varEntity.getName(), subTabsUIs, varEntity.getIcon() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Inits the.
     */
    public void init() {
        // initiate pending job submission
        log.debug( "initiate pending job submission" );
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            runAllPendingJobs( entityManager );

            treeManager.persistSystemContainer( entityManager, UUID.fromString( WizardsFeaturesEnum.CONFIGURATION.getId() ),
                    WizardsFeaturesEnum.CONFIGURATION.getKey(), UUID.fromString( SimuspaceFeaturesEnum.ROOT.getId() ),
                    "auto_transmission" );

            treeManager.persistSystemContainer( entityManager, UUID.fromString( WizardsFeaturesEnum.LOADCASES.getId() ),
                    WizardsFeaturesEnum.LOADCASES.getKey(), UUID.fromString( WizardsFeaturesEnum.CONFIGURATION.getId() ), "assignment" );

            treeManager.persistSystemContainer( entityManager, UUID.fromString( WizardsFeaturesEnum.WFSCHEMES.getId() ),
                    WizardsFeaturesEnum.WFSCHEMES.getKey(), UUID.fromString( WizardsFeaturesEnum.CONFIGURATION.getId() ), "rule_settings" );

            treeManager.persistSystemContainer( entityManager, UUID.fromString( WizardsFeaturesEnum.TRAINING_ALGO.getId() ),
                    WizardsFeaturesEnum.TRAINING_ALGO.getKey(), UUID.fromString( WizardsFeaturesEnum.CONFIGURATION.getId() ),
                    "model_training" );

            permissionManager.addFeatures( entityManager,
                    permissionManager.prepareResourceAccessControlDTO( UUID.fromString( WizardsFeaturesEnum.LOADCASES.getId() ),
                            WizardsFeaturesEnum.LOADCASES.getKey(), WizardsFeaturesEnum.LOADCASES.getType(),
                            "Loadcases provided as a feature on system startup." ) );

            permissionManager.addFeatures( entityManager,
                    permissionManager.prepareResourceAccessControlDTO( UUID.fromString( WizardsFeaturesEnum.WFSCHEMES.getId() ),
                            WizardsFeaturesEnum.WFSCHEMES.getKey(), WizardsFeaturesEnum.WFSCHEMES.getType(),
                            "WF Schemes provided as a feature on system startup." ) );

            permissionManager.addFeatures( entityManager,
                    permissionManager.prepareResourceAccessControlDTO( UUID.fromString( WizardsFeaturesEnum.TRAINING_ALGO.getId() ),
                            WizardsFeaturesEnum.TRAINING_ALGO.getKey(), WizardsFeaturesEnum.TRAINING_ALGO.getType(),
                            "Training Algo provided as a feature on system startup." ) );
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getGeneralTabUI( String userId, String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var result = dataObjectManager.editDataObjectForm( entityManager, objectId, userId );
            VariantWizardDTO variantWizardDTO = getVariant( entityManager, userId, objectId );
            UIFormItem refItem = GUIUtils.createFormItem( "Reference", "referenceId", variantWizardDTO.getReferenceId() );
            refItem.setType( "object" );
            result.get( "default" ).add( refItem );
            SelectFormItem executionHostDropDownTypeItem = executionHostDropDown( variantWizardDTO.getExecutionHostId() );
            result.get( "default" ).add( executionHostDropDownTypeItem );
            SelectFormItem copyItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            copyItem.setLabel( "Copy Submodels" );
            copyItem.setName( "copy" );
            copyItem.setValue( variantWizardDTO.getCopy() );
            copyItem.setType( SELECT );
            List< SelectOptionsUI > options = new ArrayList<>();
            options.add( new SelectOptionsUI( "yes", "Yes" ) );
            options.add( new SelectOptionsUI( "no", "No" ) );
            copyItem.setOptions( options );
            result.get( "default" ).add( copyItem );
            result.get( "default" ).add( getJobDescriptionField( variantWizardDTO.getJobDescription() ) );
            return result;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getCb2GeneralDropDown( String userId, String userName, String varId, String sid ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > result = new ArrayList<>();
            Map< String, String > payloadListItems = new HashMap<>();
            List< String > inputDecListIds = selectionManager.getSelectedIdsStringListBySelectionId( entityManager, sid );
            if ( inputDecListIds != null && !inputDecListIds.isEmpty() ) {
                BmwCaeBenchEntity shouldBeProject = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager,
                        UUID.fromString( inputDecListIds.get( 0 ) ) );
                BmwCaeBenchProjectsDTO bmwProject = prepareBmwCaeBenchProjectsByEntity( shouldBeProject );
                JSONObject jsonQueryResponseObj = cb2DummyManager.getCb2RunGeneralProjectItemsDropDown( userName, bmwProject.getName() );
                payloadListItems = prepareDropDownListByColName( jsonQueryResponseObj, LABEL );
            }
            Cb2VariantWizardDTO variantWizardDTO = getCb2Variant( entityManager, userId, varId );
            // Copy Simulation Generator Settings added
            SelectFormItem copyItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            copyItem.setLabel( "Items" );
            copyItem.setName( "generalItem" );
            copyItem.setValue( variantWizardDTO.getGeneralItem() );
            copyItem.setType( "select" );
            List< SelectOptionsUI > options1 = new ArrayList<>();
            for ( Entry< String, String > itemsMap : payloadListItems.entrySet() ) {
                options1.add( new SelectOptionsUI( itemsMap.getKey(), itemsMap.getValue() ) );
            }
            copyItem.setOptions( options1 );
            copyItem.setBindFrom( "/wizards/cb2/run/general/" + varId + "/item/{__value__}/dropdown/" + sid );
            result.add( copyItem );
            return GUIUtils.createFormFromItems( result );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getCb2GeneralDefinitionDropDown( String userId, String userName, String varId, String sid, String itemId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > result = new ArrayList<>();
            Map< String, String > payloadListVariantDefinition = new HashMap<>();
            Map< String, String > disciplineContextMap = new HashMap<>();
            Map< String, String > variantDefiMap = new HashMap<>();
            Map< String, String > objTypeMap = new HashMap<>();
            List< String > inputDecListIds = selectionManager.getSelectedIdsStringListBySelectionId( entityManager, sid );
            if ( inputDecListIds != null && !inputDecListIds.isEmpty() ) {
                BmwCaeBenchEntity shouldBeProject = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager,
                        UUID.fromString( inputDecListIds.get( 0 ) ) );
                BmwCaeBenchProjectsDTO inputDecDtoShouldBeProject = prepareBmwCaeBenchProjectsByEntity( shouldBeProject );
                JSONObject jsonQueryResponseObj = cb2DummyManager.getCb2RunGeneralProjectItemsDropDown( userName,
                        inputDecDtoShouldBeProject.getName() );
                payloadListVariantDefinition = prepareDropDownDefListByColId( jsonQueryResponseObj, "definition", itemId );
                disciplineContextMap = prepareDropDownDefListByColId( jsonQueryResponseObj, "disciplineContext", itemId );
                variantDefiMap = prepareDropDownDefListByColId( jsonQueryResponseObj, "definition.variantDefs[0]", itemId );
                String disciplineContextName = null;
                for ( Entry< String, String > disciplineContextNameMap : disciplineContextMap.entrySet() ) {
                    disciplineContextName = disciplineContextNameMap.getValue();
                }
                JSONObject jsonQueryResponseObjType = cb2DummyManager.getCb2RunGeneralTabObjectTypeDropDown( userName,
                        disciplineContextName );
                objTypeMap = prepareDropDownListByColId( jsonQueryResponseObjType, "name" );
            }
            Cb2VariantWizardDTO variantWizardDTO = getCb2Variant( entityManager, userId, varId );
            // Copy SimulationVariant Definition added
            SelectFormItem definition = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            definition.setLabel( "Variant Definition" );
            definition.setName( "generalVariantDefination" );
            definition.setValue( variantWizardDTO.getGeneralVariantDefination() );
            definition.setType( "select" );
            List< SelectOptionsUI > optionsdefinition = new ArrayList<>();
            for ( Entry< String, String > definitionMap : payloadListVariantDefinition.entrySet() ) {
                for ( Entry< String, String > variantDefinitionIdMap : variantDefiMap.entrySet() ) {
                    optionsdefinition.add( new SelectOptionsUI( variantDefinitionIdMap.getKey(), definitionMap.getValue() ) );
                    break;
                }
            }
            definition.setOptions( optionsdefinition );
            result.add( definition );
            // Copy Simulation Variant Type added
            SelectFormItem disciplineContext = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            disciplineContext.setLabel( "Discipline Context" );
            disciplineContext.setName( "generalDisciplineContext" );
            disciplineContext.setValue( variantWizardDTO.getGeneralDisciplineContext() );
            disciplineContext.setType( "select" );
            List< SelectOptionsUI > optionsdisciplineContext = new ArrayList<>();
            for ( Entry< String, String > variantTypeMap : disciplineContextMap.entrySet() ) {
                optionsdisciplineContext.add( new SelectOptionsUI( variantTypeMap.getKey(), variantTypeMap.getValue() ) );
            }
            disciplineContext.setOptions( optionsdisciplineContext );
            result.add( disciplineContext );
            SelectFormItem generalVariantType = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            generalVariantType.setLabel( "Variant Type" );
            generalVariantType.setName( "generalVariantType" );
            generalVariantType.setValue( variantWizardDTO.getGeneralVariantType() );
            generalVariantType.setType( "select" );
            List< SelectOptionsUI > generalVariantTypeMap = new ArrayList<>();
            for ( Entry< String, String > variantTypeMap : objTypeMap.entrySet() ) {
                generalVariantTypeMap.add( new SelectOptionsUI( variantTypeMap.getKey(), variantTypeMap.getValue() ) );
            }
            generalVariantType.setOptions( generalVariantTypeMap );
            result.add( generalVariantType );
            return GUIUtils.createFormFromItems( result );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getCb2GeneralTabUI( String userId, String userName, String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var result = dataObjectManager.editDataObjectForm( entityManager, objectId, userId );
            Cb2VariantWizardDTO variantWizardDTO = getCb2Variant( entityManager, userId, objectId );
            JSONObject projectPhaseJson = cb2DummyManager.getCb2RunVariantProjectPhaseDropDown( userName );
            Map< String, String > projectPhaseList = prepareDropDownListByColName( projectPhaseJson, NAME );
            // cb2 project added
            TableFormItem cb2AdditionalFiles = ( TableFormItem ) GUIUtils.createFormItem( FormItemType.TABLE );
            cb2AdditionalFiles.setLabel( "Cb2 Project" );
            cb2AdditionalFiles.setMultiple( false );
            cb2AdditionalFiles.setSelectable( null );
            cb2AdditionalFiles.setType( "externalObject" );
            cb2AdditionalFiles.setName( "generalProjectSelectionId" );
            cb2AdditionalFiles.setExternal( BmwCaeBenchEnums.CB2_PROJECT.getKey() );
            cb2AdditionalFiles.setValue( variantWizardDTO.getGeneralProjectSelectionId() );
            cb2AdditionalFiles.setBindFrom( "/wizards/cb2/run/general/" + objectId + "/dropdown/{__value__}" );
            cb2AdditionalFiles.setTriggerModifiedOnInit( true );
            Map< String, Object > rules14 = new HashMap<>();
            Map< String, Object > message43 = new HashMap<>();
            rules14.put( REQUIRED, true );
            message43.put( REQUIRED, MUST_CHOSE_OPTION );
            cb2AdditionalFiles.setRules( rules14 );
            cb2AdditionalFiles.setMessages( message43 );
            result.get( "default" ).add( cb2AdditionalFiles );
            // generalDerivedFrom added
            TableFormItem derivedFrom = ( TableFormItem ) GUIUtils.createFormItem( FormItemType.TABLE );
            derivedFrom.setLabel( "Derived From" );
            derivedFrom.setMultiple( false );
            derivedFrom.setSelectable( null );
            derivedFrom.setType( "externalObject" );
            derivedFrom.setName( "generalDerivedFrom" );
            derivedFrom.setExternal( "bmw-cb2-variant" );
            derivedFrom.setValue( variantWizardDTO.getGeneralDerivedFrom() );
            result.get( "default" ).add( derivedFrom );
            // projectPhase added
            SelectFormItem projectPhase = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            projectPhase.setLabel( "Project Phase" );
            projectPhase.setName( "generalProjectPhase" );
            projectPhase.setValue( variantWizardDTO.getGeneralProjectPhase() );
            projectPhase.setType( "select" );
            List< SelectOptionsUI > optionsprojectPhase = new ArrayList<>();
            for ( Entry< String, String > projPhasMap : projectPhaseList.entrySet() ) {
                optionsprojectPhase.add( new SelectOptionsUI( projPhasMap.getKey(), projPhasMap.getValue() ) );
            }
            projectPhase.setOptions( optionsprojectPhase );
            result.get( "default" ).add( projectPhase );
            // Copy Simulation Generator Settings added
            SelectFormItem copyItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            copyItem.setLabel( "Copy Simulation Generator Settings" );
            copyItem.setName( "generalSimulationGeneratorSettings" );
            copyItem.setValue( variantWizardDTO.getGeneralSimulationGeneratorSettings() );
            copyItem.setType( "select" );
            List< SelectOptionsUI > options = new ArrayList<>();
            options.add( new SelectOptionsUI( "0", "True" ) );
            options.add( new SelectOptionsUI( "1", "False" ) );
            copyItem.setOptions( options );
            result.get( "default" ).add( copyItem );
            // addAdditionalFiles
            UIFormItem addAdditionalFiles = GUIUtils.createFormItem();
            addAdditionalFiles.setType( FieldTypes.SERVER_FILE_EXPLORER.getType() );
            addAdditionalFiles.setName( "generalVariantOverview" );
            addAdditionalFiles.setShow( "all" );
            addAdditionalFiles.setMultiple( true );
            addAdditionalFiles.setSortable( true );
            addAdditionalFiles.setCustomAttributes( prepareAdditionalFileMap() );
            addAdditionalFiles.setLabel( "Variant Overview Image File" );
            addAdditionalFiles.setValue( variantWizardDTO.getGeneralVariantOverview() );
            result.get( "default" ).add( addAdditionalFiles );
            return result;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the job description field.
     *
     * @param value
     *         the value
     *
     * @return the job description field
     */
    private UIFormItem getJobDescriptionField( String value ) {
        UIFormItem jobDescriptionField = GUIUtils.createFormItem();
        jobDescriptionField.setLabel( "Job Description" );
        jobDescriptionField.setName( "jobDescription" );
        jobDescriptionField.setValue( value );
        jobDescriptionField.setType( "textarea" );
        Map< String, Object > rules = new HashMap<>();
        rules.put( "maxlength", 255 );
        jobDescriptionField.setRules( rules );
        return jobDescriptionField;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VariantWizardDTO getVariant( String userIdFromGeneralHeader, String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getVariant( entityManager, userIdFromGeneralHeader, id );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets variant.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param id
     *         the id
     *
     * @return the variant
     */
    private VariantWizardDTO getVariant( EntityManager entityManager, String userId, String id ) {
        Object object = dataObjectManager.getDataObject( entityManager, userId, UUID.fromString( id ) );
        VariantWizardDTO wizDTO = new VariantWizardDTO();
        wizDTO.setName( ( String ) ReflectionUtils.invokeMethod( GET_NAME, object ) );
        wizDTO.setDescription( ( String ) ReflectionUtils.invokeMethod( GET_DESCRIPTION, object ) );
        wizDTO.setId( ( UUID ) ReflectionUtils.invokeMethod( GET_ID, object ) );
        wizDTO.setVersion( ( VersionDTO ) ReflectionUtils.invokeMethod( GET_VERSION, object ) );
        wizDTO.setCustomAttributes( ( Map< String, Object > ) ReflectionUtils.invokeMethod( GET_CUSTOM_ATTRIBUTES, object ) );

        VariantWizardEntity wizEntity = wizDAO.findById( entityManager, UUID.fromString( id ) );
        if ( wizEntity != null ) {
            return createVaraintWizardDTOFromEntity( entityManager, wizEntity );
        }
        return wizDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cb2VariantWizardDTO getCb2Variant( String userIdFromGeneralHeader, String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getCb2Variant( entityManager, userIdFromGeneralHeader, id );
        } finally {
            entityManager.close();
        }

    }

    /**
     * Gets cb 2 variant.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     *
     * @return the cb 2 variant
     */
    private Cb2VariantWizardDTO getCb2Variant( EntityManager entityManager, String userIdFromGeneralHeader, String id ) {
        Object object = dataObjectManager.getDataObject( entityManager, userIdFromGeneralHeader, UUID.fromString( id ) );
        Cb2VariantWizardDTO wizDTO = new Cb2VariantWizardDTO();
        wizDTO.setName( ( String ) ReflectionUtils.invokeMethod( GET_NAME, object ) );
        wizDTO.setDescription( ( String ) ReflectionUtils.invokeMethod( GET_DESCRIPTION, object ) );
        wizDTO.setId( ( UUID ) ReflectionUtils.invokeMethod( GET_ID, object ) );
        wizDTO.setVersion( ( VersionDTO ) ReflectionUtils.invokeMethod( GET_VERSION, object ) );
        wizDTO.setCustomAttributes( ( Map< String, Object > ) ReflectionUtils.invokeMethod( GET_CUSTOM_ATTRIBUTES, object ) );

        VariantWizardEntity wizEntity = wizDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( id ) );
        if ( wizEntity != null ) {
            return createCb2VaraintWizardDTOFromEntity( entityManager, userIdFromGeneralHeader, wizEntity );
        }
        return wizDTO;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object saveVariant( String token, String userIdFromGeneralHeader, String id, VariantWizardDTO variantWizardDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            VariantWizardEntity entity = createVariantEntityFromVariantWizardDTO( entityManager, id, variantWizardDTO );
            if ( entity.isFrozen() ) {
                throw new SusException( "Variant Frozen now" );
            }
            wizDAO.saveOrUpdate( entityManager, entity );
            return createVaraintWizardDTOFromEntity( entityManager, entity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object saveCb2Variant( String token, String userIdFromGeneralHeader, String id, Cb2VariantWizardDTO variantWizardDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // VariantWizardEntity entity = new VariantWizardEntity();
            VariantWizardEntity entity = createCb2VariantEntityFromVariantWizardDTO( entityManager, id, variantWizardDTO );
            if ( entity.isFrozen() ) {
                throw new SusException( "Variant Frozen now" );
            }
            wizDAO.saveOrUpdate( entityManager, entity );
            return createCb2VaraintWizardDTOFromEntity( entityManager, userIdFromGeneralHeader, entity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * validate CB2 session.
     *
     * @param userEntity
     *         the user entity
     * @param token
     *         the token
     */
    private void validateCB2session( UserEntity userEntity, String token ) {
        ValidationUtils.validateCB2AccessWithWENLoginByToken( token );
        log.info( "validating CB2 session" );
        String password = PasswordUtils.getPasswordById( userEntity.getId().toString() );
        if ( authManager.isUserAuthenticateInLdap( userEntity.getUserUid(), password ) ) {
            ProcessResult processResult = PythonUtils.CB2LoginByPython( userEntity.getUserUid(), password,
                    String.valueOf( Cb2OperationEnum.CB2_LOGIN_OR_KEEP_ALIVE_SESSION.getKey() ),
                    tokenManager.getRefreshTokenFromCurrentSessionUsingUid( userEntity.getUserUid() ) );
            if ( processResult.getExitValue() != 0 && processResult.getErrorStreamString() != null && !processResult.getErrorStreamString()
                    .equals( ConstantsString.EMPTY_STRING ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.CB2_SESSION_INACTIVE_LOGIN_TO_CB2_AGAIN.getKey() ) );
            }
        } else {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.USER_COULD_NOT_BE_AUTHENTICATED_IN_LDAP.getKey(), userEntity.getUserUid() ) );
        }
    }

    /**
     * Prepare job model.
     *
     * @param entityManager
     *         the entity manager
     * @param jobName
     *         the job name
     * @param description
     *         the description
     * @param workflow
     *         the workflow
     *
     * @return the job
     */
    private Job prepareJobModel( EntityManager entityManager, String jobName, String description, SystemWorkflow workflow ) {
        Status status = new Status( WorkflowStatus.RUNNING );
        final Job jobImpl = new JobImpl();
        final LocationDTO locationDTO = locationManager.getLocation( entityManager, LocationsEnum.DEFAULT_LOCATION.getId() );
        jobImpl.setRunsOn( locationDTO );
        jobImpl.setDescription( description );
        jobImpl.setName( jobName );
        jobImpl.setWorkflowId( UUID.fromString( workflow.getId() ) );
        jobImpl.setWorkflowName( workflow.getName() );
        jobImpl.setOs( OSValidator.getOperationSystemName() );
        try {
            jobImpl.setMachine( InetAddress.getLocalHost().getHostName() );
        } catch ( final UnknownHostException e ) {
            log.error( "Machine Name Error: " + e.getMessage(), e );
        }
        jobImpl.setStatus( status );
        jobImpl.setWorkflowVersion( new VersionDTO( SusConstantObject.DEFAULT_VERSION_NO ) );
        return jobImpl;
    }

    /**
     * Gets the worfklow.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param lcwfModel
     *         the lcwf model
     *
     * @return the worfklow
     */
    private LatestWorkFlowDTO getworfklow( EntityManager entityManager, String userIdFromGeneralHeader, LoadcaseWFModel lcwfModel ) {
        LatestWorkFlowDTO wf = workflowManager.getNewWorkflowByIdAndVersionId( entityManager, UUID.fromString( userIdFromGeneralHeader ),
                lcwfModel.getId(),
                workflowManager.getWorkflowById( entityManager, UUID.fromString( userIdFromGeneralHeader ), lcwfModel.getId() ).getVersion()
                        .getId() );

        WorkflowModel workflowModel = JsonUtils.jsonToObject( JsonUtils.toJson( wf.getElements() ), WorkflowModel.class );
        if ( null != lcwfModel.getElements() ) {
            for ( String elementId : lcwfModel.getElements().keySet() ) {
                for ( WorkflowElement element : workflowModel.getNodes() ) {
                    if ( element.getData().getId().equals( elementId ) ) {
                        for ( Field f : element.getData().getFields() ) {
                            if ( lcwfModel.getElements().get( elementId ).containsKey( f.getName() ) ) {
                                // if field found, replace value
                                f.setValue( lcwfModel.getElements().get( elementId ).get( f.getName() ) );
                            }
                        }
                    }
                }
            }
        }
        // put replaced elements to wf
        wf.setElements( JsonUtils.convertStringToMapGenericValue( JsonUtils.toJson( workflowModel ) ) );
        return wf;
    }

    /**
     * Submit loadcase.
     *
     * @param entityManager
     *         the entity manager
     * @param selectedLoadcaseDto
     *         the selected loadcase dto
     * @param runItem
     *         the run item
     * @param userEntity
     *         the user entity
     * @param token
     *         the token
     * @param id
     *         the id
     * @param submitLoadcase
     *         the submit loadcase
     * @param masterId
     *         the master id
     * @param isRunDUmmy
     *         the is run Dummy
     * @param selectedLoadcaseCount
     *         the selected loadcase count
     * @param executedLoadcase
     *         the executed loadcase
     * @param hostId
     *         the host id
     */
    private void submitLoadcase( EntityManager entityManager, LoadCaseDTO selectedLoadcaseDto, VariantWizardDTO runItem,
            UserEntity userEntity, String token, String id, SubmitLoadcase submitLoadcase, UUID masterId, Boolean isRunDUmmy,
            int selectedLoadcaseCount, int executedLoadcase, String hostId ) {
        boolean assembleSuccess = false;
        if ( !submitLoadcase.isAssemble() ) {
            return;
        }
        // create loadcase in selected varient
        UUID lId = createLoadCaseInSelectedContainer( entityManager, runItem, userEntity, id, selectedLoadcaseDto.getName(), masterId );

        RestAPI server = getServerByLocationId( entityManager, LocationsEnum.DEFAULT_LOCATION.getId() );
        String scriptsPath = getNodeFromDummyConfig( "1", "scriptsPath" ).asText();

        Map< String, Object > vars = null;
        try {
            vars = prepareAssembleVariables( entityManager, selectedLoadcaseDto, userEntity.getId().toString(), id, runItem, lId,
                    isRunDUmmy, selectedLoadcaseCount, executedLoadcase, scriptsPath );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        JobParameters jobParametersA = prepareJobParameter( entityManager, runItem.getName() + "_Assembly", userEntity.getId().toString(),
                token, submitLoadcase.getOassemble(), server, vars, hostId, selectedLoadcaseDto.getName() );
        jobParametersA.setCreateChild( submitLoadcase.isSolve() );
        jobParametersA.setJobRelationType( JobRelationTypeEnums.CHILD.getKey() );
        jobParametersA.setJobType( JobTypeEnums.VARIANT.getKey() );
        jobParametersA.getWorkflow().setDummyMasterJobId( String.valueOf( masterId ) );

        // create folder in staging and copy additional files there
        Set< String > allAdditionalFilePath = copyAdditnalFilesInStaging( entityManager, userEntity, runItem, jobParametersA,
                selectedLoadcaseDto.getName() );
        writeAdditionalFilesName( entityManager, allAdditionalFilePath, runItem, userEntity.getUserUid(),
                jobParametersA.getWorkflow().getName(), jobParametersA.getJobInteger().toString(), selectedLoadcaseDto.getName() );

        workflowManager.runServerJob( entityManager, userEntity, jobParametersA, selectedLoadcaseDto.getName() );
        log.info( "Running Assembly job: " + jobParametersA.getName() );
        log.info( "Monitoring job with id: " + jobParametersA.getId() );

        relationDAO.save( entityManager, new Relation( masterId, UUID.fromString( jobParametersA.getId() ) ) );
        assembleSuccess = monitorJob( entityManager, jobParametersA.getId() );
        // check status, then run solve, or next steps
        if ( !assembleSuccess ) {
            return;
        }

        // List< SuSEntity > assemblyCreatedObjects =
        // jobManager.getJobDataCreatedObjectsList( userEntity.getId(),
        // jobParametersA.getId() );
        // for ( SuSEntity deckEntity : assemblyCreatedObjects ) {
        // hack start - avoid using hard coded typeId, instead get from
        // config
        // if ( deckEntity.getTypeId().equals( UUID.fromString(
        // "d187b628-4e8f-4f5c-8b39-9bbb9410df82" ) ) ) {
        // hack end
        new Thread( () -> {
            // log.info( ">>submitSolve deckEntity: " + deckEntity.getName() );
            EntityManager threadEntityManager = entityManagerFactory.createEntityManager();
            try {
                submitSolve( threadEntityManager, String.valueOf( masterId ), selectedLoadcaseDto.getName(), runItem, userEntity, token, id,
                        submitLoadcase, server, "", UUID.fromString( jobParametersA.getId() ), lId, selectedLoadcaseCount, executedLoadcase,
                        hostId, scriptsPath );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            } finally {
                threadEntityManager.close();
            }
        } ).start();

    }

    /**
     * Copy additnal files in staging.
     *
     * @param entityManager
     *         the entity manager
     * @param userEntity
     *         the user entity
     * @param runItem
     *         the run item
     * @param jobParametersA
     *         the job parameters A
     * @param loadcaseName
     *         the loadcase name
     *
     * @return the sets the
     */
    private Set< String > copyAdditnalFilesInStaging( EntityManager entityManager, UserEntity userEntity, VariantWizardDTO runItem,
            JobParameters jobParametersA, String loadcaseName ) {
        log.info( "Additional Files Is Being Copied" );
        Set< String > filePaths = new HashSet<>();
        List< AdditionalFiles > selectedFilesList = runItem.getAdditionalFiles();
        List< DocumentDTO > selectedFilesListLocal = runItem.getAdditionalFilesLocal();
        String selectionIdSus = runItem.getAdditionalFilesSus();

        if ( ( selectedFilesList != null && !selectedFilesList.isEmpty() ) || ( selectedFilesListLocal != null
                && !selectedFilesListLocal.isEmpty() ) || ( selectionIdSus != null && !selectionIdSus.isEmpty() ) || (
                runItem.getCb2projtree() != null ) ) {
            String bmwSuggestion = loadcaseName + "_" + jobParametersA.getWorkflow().getName() + "_" + jobParametersA.getJobInteger();
            File desFolder = new File( PropertiesManager.getUserStagingPath( userEntity.getUserUid() ) + File.separator + bmwSuggestion );
            if ( !desFolder.exists() ) {
                LinuxUtils.createDirectory( userEntity.getUserUid(), desFolder.getAbsolutePath() );
            }

            // server files copy
            if ( selectedFilesList != null && !selectedFilesList.isEmpty() ) {
                filePaths.addAll( copyServerAdditionalFilesToStaging( userEntity.getUserUid(), runItem.getAdditionalFiles(),
                        desFolder.getAbsolutePath() ) );
            }

            // local files copy
            if ( selectedFilesListLocal != null && !selectedFilesListLocal.isEmpty() ) {
                filePaths.addAll( copyLocalAdditionalFilesToStaging( userEntity.getUserUid(), runItem.getAdditionalFilesLocal(),
                        desFolder.getAbsolutePath() ) );
            }

            // sus files copy
            if ( selectionIdSus != null && !selectionIdSus.isEmpty() ) {
                filePaths.addAll( copySusAdditionalFilesToStaging( entityManager, userEntity.getUserUid(), runItem.getAdditionalFilesSus(),
                        desFolder.getAbsolutePath() ) );
            }

            if ( runItem.getCb2projtree() != null && !runItem.getCb2projtree().isEmpty() ) {

                List< String > cb2OidList = selectionManager.getSelectedIdsStringListBySelectionId( entityManager,
                        runItem.getCb2projtree() );

                if ( cb2OidList != null && !cb2OidList.isEmpty() ) {

                    StringBuilder bufferStoreOID = new StringBuilder();
                    int size = cb2OidList.size();
                    for ( int i = 0; i < cb2OidList.size(); i++ ) {
                        BmwCaeBenchEntity cb2ObjectEntity = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager,
                                UUID.fromString( cb2OidList.get( i ) ) );
                        bufferStoreOID.append( cb2ObjectEntity.getOid() );
                        if ( i != size - 1 ) {
                            bufferStoreOID.append( "#" );
                        }
                        FileUtils.deleteIfExists(
                                PropertiesManager.getDefaultServerTempPath() + File.separator + cb2ObjectEntity.getName() );
                    }

                    ProcessResult processResult = PythonUtils.downloadCB2ObjectFileByCB2Oid( userEntity.getUserUid(),
                            bufferStoreOID.toString(), desFolder.getAbsolutePath(), BmwCaeBenchEnums.CB2_VARIANT.getValue() );

                    if ( processResult.getExitValue() != 0 ) {
                        log.error( processResult.getErrorStreamString() );
                        log.error( "ERROR downloading additional CB2 Files" );
                    }
                    if ( processResult.getErrorStreamString() != null && !processResult.getErrorStreamString().isEmpty() ) {
                        log.warn( processResult.getErrorStreamString() );
                    }
                }
            }
        }
        return filePaths;
    }

    /**
     * Copy Server Additional Files to staging.
     *
     * @param userUid
     *         the user uid
     * @param selectedFilesList
     *         the selectedFilesList
     * @param destPath
     *         the dest path
     *
     * @return the server additional files path
     */
    private Set< String > copyServerAdditionalFilesToStaging( String userUid, List< AdditionalFiles > selectedFilesList, String destPath ) {
        Set< String > filePaths = new HashSet<>();

        for ( AdditionalFiles additionalFiles : selectedFilesList ) {
            File srcFile = new File( additionalFiles.getFullPath() );
            if ( srcFile.isFile() ) {

                File destFile = new File( destPath + File.separator + srcFile.getName() );
                try {
                    LinuxUtils.copyFileFromSrcPathToDestPath( userUid, srcFile.getAbsolutePath(), destFile.getAbsolutePath() );
                    filePaths.add( destFile.getAbsolutePath() );
                } catch ( Exception e ) {
                    log.error( e.getMessage(), e );
                }
            }
        }

        return filePaths;
    }

    /**
     * Get Local Additional Files Path.
     *
     * @param userUid
     *         the user uid
     * @param selectedFilesListLocal
     *         the selectedFilesListLocal
     * @param destPath
     *         the dest path
     *
     * @return the local additional files path
     */
    private Set< String > copyLocalAdditionalFilesToStaging( String userUid, List< DocumentDTO > selectedFilesListLocal, String destPath ) {
        Set< String > filePaths = new HashSet<>();

        for ( DocumentDTO additionalFiles : selectedFilesListLocal ) {
            File srcFile = new File( PropertiesManager.getVaultPath() + additionalFiles.getPath() );
            if ( srcFile.isFile() ) {
                File destFile = new File( destPath + File.separator + additionalFiles.getName() );
                try {
                    if ( PropertiesManager.isImpersonated() ) {
                        File decryptedTempFile = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndSave( additionalFiles, srcFile,
                                PropertiesManager.getDefaultServerTempPath() + File.separator + srcFile.getName() );

                        FileUtils.setGlobalReadFilePermissions( decryptedTempFile );
                        LinuxUtils.copyFileFromSrcPathToDestPath( userUid, decryptedTempFile.getAbsolutePath(),
                                destFile.getAbsolutePath() );

                        if ( decryptedTempFile.exists() ) {
                            decryptedTempFile.delete();
                        }
                    } else {
                        EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndSave( additionalFiles, srcFile, destFile.getAbsolutePath() );
                    }

                    filePaths.add( destFile.getAbsolutePath() );
                } catch ( Exception e ) {
                    log.error( e.getMessage(), e );
                }
            }
        }

        return filePaths;
    }

    /**
     * Copy sus additional files to staging set.
     *
     * @param entityManager
     *         the entity manager
     * @param userUid
     *         the user uid
     * @param selectionId
     *         the selection id
     * @param destPath
     *         the dest path
     *
     * @return the set
     */
    private Set< String > copySusAdditionalFilesToStaging( EntityManager entityManager, String userUid, String selectionId,
            String destPath ) {
        Set< String > filePaths = new HashSet<>();
        List< DataObjectDTO > allData = ( List< DataObjectDTO > ) ( Object ) dataObjectManager.getContainerOrChildsById( entityManager,
                selectionId );

        if ( allData != null ) {
            for ( DataObjectDTO data : allData ) {
                if ( data.getFile() != null && data.getFile().getPath() != null ) {
                    File srcFile = new File( PropertiesManager.getVaultPath() + data.getFile().getPath() );
                    File destFile = new File( destPath + File.separator + data.getFile().getName() );
                    try {
                        if ( PropertiesManager.isImpersonated() ) {
                            File decryptedTempFile = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndSave( data.getFile(), srcFile,
                                    PropertiesManager.getDefaultServerTempPath() + File.separator + srcFile.getName() );

                            FileUtils.setGlobalReadFilePermissions( decryptedTempFile );
                            LinuxUtils.copyFileFromSrcPathToDestPath( userUid, decryptedTempFile.getAbsolutePath(),
                                    destFile.getAbsolutePath() );

                            if ( decryptedTempFile.exists() ) {
                                decryptedTempFile.delete();
                            }
                        } else {
                            EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndSave( data.getFile(), srcFile, destFile.getAbsolutePath() );
                        }

                        filePaths.add( destFile.getAbsolutePath() );
                    } catch ( Exception e ) {
                        log.error( e.getMessage(), e );
                    }
                }
            }
        }

        return filePaths;
    }

    /**
     * Creates the load case in selected container.
     *
     * @param entityManager
     *         the entity manager
     * @param runItem
     *         the run item
     * @param userEntity
     *         the user entity
     * @param id
     *         the id
     * @param selectedloadName
     *         the selectedload name
     * @param masterJobId
     *         the master job id
     *
     * @return the uuid
     */
    private UUID createLoadCaseInSelectedContainer( EntityManager entityManager, VariantWizardDTO runItem, UserEntity userEntity, String id,
            String selectedloadName, UUID masterJobId ) {
        JSONObject obj = new JSONObject();
        obj.put( "createOption", ConstantsID.LOADCASE_TYPE_ID_FOR_BMW_CONFIG );
        obj.put( "parentId", id );
        obj.put( "typeId", ConstantsID.LOADCASE_TYPE_ID_FOR_BMW_CONFIG );
        obj.put( NAME, selectedloadName );
        obj.put( "jobId", masterJobId );
        Object lEntity = dataObjectManager.createObject( entityManager, userEntity, id, ConstantsID.LOADCASE_TYPE_ID_FOR_BMW_CONFIG,
                obj.toString(), false, null );
        JSONObject reader = new JSONObject( lEntity );
        return UUID.fromString( reader.get( "id" ).toString() );
    }

    /**
     * Gets the server by location id.
     *
     * @param entityManager
     *         the entity manager
     * @param locationId
     *         the location id
     *
     * @return the server by location id
     */
    private RestAPI getServerByLocationId( EntityManager entityManager, String locationId ) {
        LocationDTO location = locationManager.getLocation( entityManager, locationId );
        URL url = null;
        try {
            url = new URL( location.getUrl() );
        } catch ( MalformedURLException e ) {
            log.error( e.getMessage(), e );
        }
        return new RestAPI( url.getProtocol() + "://", url.getHost(), url.getPort() + "" );
    }

    /**
     * Prepare job parameter.
     *
     * @param entityManager
     *         the entity manager
     * @param runItem
     *         the run item
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param token
     *         the token
     * @param loadcaseWFModel
     *         the loadcase WF model
     * @param server
     *         the server
     * @param vars
     *         the vars
     * @param runsOn
     *         the runs on
     * @param loadcaseName
     *         the loadcase name
     *
     * @return the job parameters
     */
    private JobParameters prepareJobParameter( EntityManager entityManager, String runItem, String userIdFromGeneralHeader, String token,
            LoadcaseWFModel loadcaseWFModel, RestAPI server, Map< String, Object > vars, String runsOn, String loadcaseName ) {
        UserDTO user = userCommonManager.getUserById( entityManager, UUID.fromString( userIdFromGeneralHeader ) );

        JobParameters jobParameters = new JobParametersImpl();
        jobParameters.setName( runItem );

        if ( loadcaseWFModel != null ) {
            jobParameters.setWorkflow( getworfklow( entityManager, userIdFromGeneralHeader, loadcaseWFModel ) );
        }
        jobParameters.setRequestHeaders( new RequestHeaders( token,
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36" ) );
        jobParameters.setServer( server );

        if ( null != runsOn ) {
            jobParameters.setRunsOn( runsOn );
        } else {
            jobParameters.setRunsOn( LocationsEnum.DEFAULT_LOCATION.getId() );
        }
        if ( null != vars ) {
            jobParameters.setGlobalVariables( vars );
        }
        jobParameters.setId( UUID.randomUUID().toString() );

        Integer id = jobManager.saveJobIds( entityManager, UUID.fromString( jobParameters.getId() ) );
        if ( id != null ) {
            jobParameters.setJobInteger( id );
        }
        jobParameters.setWorkingDir( new EngineFile(
                PropertiesManager.getUserStagingPath( user.getUserUid() ) + File.separator + loadcaseName + "_"
                        + jobParameters.getWorkflow().getName() + "_" + jobParameters.getJobInteger() ) );

        return jobParameters;
    }

    /**
     * Submit solve.
     *
     * @param entityManager
     *         the entity manager
     * @param _masterId
     *         the master id
     * @param selectedLoadcaseName
     *         the selected loadcase name
     * @param runItem
     *         the run item
     * @param userEntity
     *         the user entity
     * @param token
     *         the token
     * @param id
     *         the id
     * @param submitLoadcase
     *         the submit loadcase
     * @param server
     *         the server
     * @param deckEntity
     *         the deck entity
     * @param masterId
     *         the master id
     * @param lId
     *         the l id
     * @param selectedLoadcaseCount
     *         the selected loadcase count
     * @param executedLoadcase
     *         the executed loadcase
     * @param hostId
     *         the host id
     * @param scriptsPath
     *         the scripts path
     */
    private void submitSolve( EntityManager entityManager, String _masterId, String selectedLoadcaseName, VariantWizardDTO runItem,
            UserEntity userEntity, String token, String id, SubmitLoadcase submitLoadcase, RestAPI server, String deckEntity, UUID masterId,
            UUID lId, int selectedLoadcaseCount, int executedLoadcase, String hostId, String scriptsPath ) {

        boolean solveSuccess = false;
        if ( !submitLoadcase.isSolve() ) {
            return;
        }
        Map< String, Object > varsS = null;
        try {
            varsS = prepareSolveVariables( entityManager, deckEntity, selectedLoadcaseName, userEntity.getId().toString(), id, runItem, lId,
                    scriptsPath );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        JobParameters jobParametersS = prepareJobParameter( entityManager, runItem.getName() + "_Solver", userEntity.getId().toString(),
                token, submitLoadcase.getOsolve(), server, varsS, hostId, selectedLoadcaseName );
        jobParametersS.setJobRelationType( JobRelationTypeEnums.CHILD.getKey() );
        jobParametersS.setCreateChild( submitLoadcase.isPost() );
        jobParametersS.setJobType( JobTypeEnums.VARIANT.getKey() );
        jobParametersS.getWorkflow().setDummyMasterJobId( _masterId );
        // create folder in staging and copy additional files there
        Set< String > allAdditionalFilePath = copyAdditnalFilesInStaging( entityManager, userEntity, runItem, jobParametersS,
                selectedLoadcaseName );
        writeAdditionalFilesName( entityManager, allAdditionalFilePath, runItem, userEntity.getUserUid(),
                jobParametersS.getWorkflow().getName(), jobParametersS.getJobInteger().toString(), selectedLoadcaseName );

        workflowManager.runServerJob( entityManager, userEntity, jobParametersS, selectedLoadcaseName );
        log.info( "Running Solver job: " + jobParametersS.getName() );

        relationDAO.save( entityManager, new Relation( masterId, UUID.fromString( jobParametersS.getId() ) ) );
        log.info( ">>relation between Assembly : " + masterId + " and Solver " + jobParametersS.getId() );
        solveSuccess = monitorJob( entityManager, jobParametersS.getId() );

        if ( !solveSuccess ) {
            return;
        }
        if ( !submitLoadcase.isPost() ) {
            return;
        }
        Map< String, Object > varsP = null;
        try {
            varsP = preparePostVariables( entityManager, jobParametersS.getId(), selectedLoadcaseName, userEntity.getId().toString(), id,
                    runItem, lId, selectedLoadcaseCount, executedLoadcase, scriptsPath );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }

        JobParameters jobParametersP = prepareJobParameter( entityManager, runItem.getName() + "_Post", userEntity.getId().toString(),
                token, submitLoadcase.getOpost(), server, varsP, hostId, selectedLoadcaseName );
        jobParametersP.setJobRelationType( JobRelationTypeEnums.CHILD.getKey() );
        jobParametersP.setCreateChild( false );
        jobParametersS.setJobType( JobTypeEnums.VARIANT.getKey() );
        jobParametersP.getWorkflow().setDummyMasterJobId( _masterId );

        // create folder in staging and copy additional files there
        copyAdditnalFilesInStaging( entityManager, userEntity, runItem, jobParametersP, selectedLoadcaseName );
        writeAdditionalFilesName( entityManager, allAdditionalFilePath, runItem, userEntity.getUserUid(),
                jobParametersP.getWorkflow().getName(), jobParametersP.getJobInteger().toString(), selectedLoadcaseName );

        workflowManager.runServerJob( entityManager, userEntity, jobParametersP, selectedLoadcaseName );
        log.info( "Running Post job: " + jobParametersP.getName() );

        relationDAO.save( entityManager,
                new Relation( UUID.fromString( jobParametersS.getId() ), UUID.fromString( jobParametersP.getId() ) ) );

        log.info( ">>relation between Solver : " + jobParametersS.getId() + " and Post " + jobParametersP.getId() );

        monitorJob( entityManager, jobParametersP.getId() );
    }

    /**
     * Monitor job.
     *
     * @param entityManager
     *         the entity manager
     * @param jobId
     *         the job id
     *
     * @return true, if successful
     */
    private boolean monitorJob( EntityManager entityManager, String jobId ) {
        boolean result = false;
        long jobMonitoringTime = Integer.valueOf( PropertiesManager.getJobMonitoringTimeSeconds() ) * 1000;
        while ( true ) {
            log.info( ">> while true" );
            JobEntity job = jobManager.getJobById( entityManager, UUID.fromString( jobId ) );
            log.info( "Monitoring job with id: " + jobId );
            if ( job != null ) {
                log.info( "Monitoring job with id: " + jobId + " with status: " + job.getStatus() );
                if ( job.getStatus() == WorkflowStatus.QUEUED.getKey() || job.getStatus() == WorkflowStatus.RUNNING.getKey() ) {
                    try {
                        log.info( "Job Monitor sleep for millisec : " + jobMonitoringTime );
                        Thread.sleep( jobMonitoringTime );
                    } catch ( InterruptedException e ) {
                        log.info( e.getMessage(), e );
                    }
                    continue;
                } else if ( job.getStatus() == WorkflowStatus.COMPLETED.getKey() ) {
                    result = true;
                    break;
                } else {
                    break;
                }
            }

            try {
                Thread.sleep( jobMonitoringTime );
            } catch ( InterruptedException e ) {
                log.info( e.getMessage(), e );
            }

        }
        return result;
    }

    /**
     * Prepare assemble variables.
     *
     * @param entityManager
     *         the entity manager
     * @param selectedLoadcaseDto
     *         the selected loadcase dto
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     * @param variantWizardDTO
     *         the variant wizard DTO
     * @param lId
     *         the l id
     * @param isRunDummy
     *         the is run dummy
     * @param selectedLoadcaseCount
     *         the selected loadcase count
     * @param executedLoadcase
     *         the executed loadcase
     * @param scriptsPath
     *         the scripts path
     *
     * @return the map
     */
    private Map< String, Object > prepareAssembleVariables( EntityManager entityManager, LoadCaseDTO selectedLoadcaseDto,
            String userIdFromGeneralHeader, String id, VariantWizardDTO variantWizardDTO, UUID lId, Boolean isRunDummy,
            int selectedLoadcaseCount, int executedLoadcase, String scriptsPath ) {
        log.info( ">>prepareAssembleVariables selectedLoadcaseName: " + selectedLoadcaseDto.getName() );
        Map< String, Object > map = new HashMap<>();
        createVariantEntityFromVariantWizardDTO( entityManager, id, variantWizardDTO );
        // for submodels, create a new ordered selectionId of items existing in
        // object_loadcase_json according to order in object_selection
        // pass new selectionId as variable
        // workflow should handle Flat export according to new selection Id
        // workflow should also get the order of submodels via export
        // systemoutput
        String lcSubmodelsSelectionId = "";
        StringBuilder sepratedIds = new StringBuilder();
        if ( isRunDummy ) {
            List< String > dummFilesSelectionList;
            if ( null != variantWizardDTO.getObjectSelectionId() ) {
                dummFilesSelectionList = selectionManager.getSelectedIdsStringListBySelectionId( entityManager,
                        variantWizardDTO.getObjectSelectionId().toString() );
                for ( String ID : dummFilesSelectionList ) {
                    BmwCaeBenchEntity dummyFileFromDb = null;
                    dummyFileFromDb = bmwCaeBenchDAO.getLatestObjectById( entityManager, BmwCaeBenchEntity.class, UUID.fromString( ID ) );
                    if ( sepratedIds.length() != 0 ) {
                        sepratedIds.append( "," );
                    }
                    sepratedIds.append( dummyFileFromDb.getOid() );
                }
            }
        } else {
            lcSubmodelsSelectionId = createSubmodelsSelectionId( entityManager, selectedLoadcaseDto, userIdFromGeneralHeader, id,
                    variantWizardDTO );
        }

        map.put( "{{Assemble.cb2submodels}}", sepratedIds );
        map.put( "{{Assemble.submodels}}", lcSubmodelsSelectionId );
        map.put( "{{Assemble.variantName}}", variantWizardDTO.getName() );
        map.put( "{{Assemble.variantID}}", variantWizardDTO.getId().toString() );
        map.put( "{{Assemble.loadcaseName}}", selectedLoadcaseDto.getName() );
        map.put( "{{Assemble.currentLoadCaseCount}}", executedLoadcase );
        map.put( "{{Assemble.totalLoadCaseCount}}", selectedLoadcaseCount );
        map.put( "{{SuS.scripts}}", scriptsPath );
        FiltersDTO filtersDTO = new FiltersDTO();
        List< Object > objects = new ArrayList<>();
        objects.add( lId );
        filtersDTO.setItems( objects );
        map.put( "{{Assemble.variantImportId}}",
                selectionManager.createSelection( entityManager, userIdFromGeneralHeader, SelectionOrigins.RUN_DUMMY, filtersDTO )
                        .getId() );
        log.info( "<<prepareAssembleVariables selectedLoadcaseId: " + selectedLoadcaseDto.getId() );
        return map;
    }

    /**
     * Prepare solve variables.
     *
     * @param entityManager
     *         the entity manager
     * @param deckEntity
     *         the deck entity
     * @param selectedLoadcaseName
     *         the selected loadcase name
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     * @param variantWizardDTO
     *         the variant wizard DTO
     * @param lId
     *         the l id
     * @param scriptsPath
     *         the scripts path
     *
     * @return the map
     */
    private Map< String, Object > prepareSolveVariables( EntityManager entityManager, String deckEntity, String selectedLoadcaseName,
            String userIdFromGeneralHeader, String id, VariantWizardDTO variantWizardDTO, UUID lId, String scriptsPath ) {
        log.info( ">>prepareSolveVariables selectedLoadcaseName: " + selectedLoadcaseName );
        Map< String, Object > map = new HashMap<>();
        // for submodels, create a new ordered selectionId of items existing in
        // object_loadcase_json according to order in object_selection
        // pass new selectionId as variable
        // workflow should handle Flat export according to new selection Id
        // workflow should also get the order of submodels via export
        // systemoutput

        String lcInputDeckSelectionId = "";
        String lcInputDeckName = "";
        // hack start - avoid using hard coded typeId, instead get from config
        // if ( deckEntity.getTypeId().equals( UUID.fromString(
        // "d187b628-4e8f-4f5c-8b39-9bbb9410df82" ) ) ) {
        // // hack start
        // FiltersDTO filtersDTO = new FiltersDTO();
        // List< Object > objects = new ArrayList<>();
        // objects.add( deckEntity.getComposedId().getId() );
        // filtersDTO.setItems( objects );
        // SelectionResponseUI selection = selectionManager.createSelection(
        // userIdFromGeneralHeader, "", filtersDTO );
        //
        // lcInputDeckName = deckEntity.getName();
        //
        // lcInputDeckSelectionId = selection.getId();
        // }
        map.put( "{{Solve.inputDeckId}}", lcInputDeckSelectionId );
        map.put( "{{Solve.inputDeckName}}", lcInputDeckName );

        String resultName = "";
        if ( lcInputDeckName.contains( "." ) ) {
            resultName = lcInputDeckName.split( "\\." )[ 0 ];
        } else {
            resultName = lcInputDeckName;
        }

        map.put( "{{Solve.resultName}}", resultName );
        map.put( "{{Solve.variantName}}", variantWizardDTO.getName() );
        map.put( "{{Solve.variantID}}", variantWizardDTO.getId().toString() );
        map.put( "{{Solve.loadcaseName}}", selectedLoadcaseName );
        map.put( "{{SuS.scripts}}", scriptsPath );
        FiltersDTO filtersDTO = new FiltersDTO();
        List< Object > objects = new ArrayList<>();
        objects.add( lId );
        filtersDTO.setItems( objects );
        map.put( "{{Solve.variantImportId}}",
                selectionManager.createSelection( entityManager, userIdFromGeneralHeader, SelectionOrigins.RUN_DUMMY, filtersDTO )
                        .getId() );
        log.info( "<<prepareSolveVariables selectedLoadcaseName: " + selectedLoadcaseName );
        return map;
    }

    /**
     * Prepare post variables.
     *
     * @param entityManager
     *         the entity manager
     * @param solveJobId
     *         the solve job id
     * @param selectedLoadcaseName
     *         the selected loadcase name
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     * @param variantWizardDTO
     *         the variant wizard DTO
     * @param lID
     *         the l ID
     * @param selectedLoadcaseCount
     *         the selected loadcase count
     * @param executedLoadcase
     *         the executed loadcase
     * @param scriptsPath
     *         the scripts path
     *
     * @return the map
     */
    private Map< String, Object > preparePostVariables( EntityManager entityManager, String solveJobId, String selectedLoadcaseName,
            String userIdFromGeneralHeader, String id, VariantWizardDTO variantWizardDTO, UUID lID, int selectedLoadcaseCount,
            int executedLoadcase, String scriptsPath ) {
        log.info( ">>preparePostVariables selectedLoadcaseName: " + selectedLoadcaseName );
        Map< String, Object > map = new HashMap<>();
        createVariantEntityFromVariantWizardDTO( entityManager, id, variantWizardDTO );

        List< SuSEntity > solveCreatedObjects = jobManager.getJobDataCreatedObjectsList( entityManager,
                UUID.fromString( userIdFromGeneralHeader ), solveJobId );
        String lcResultSelectionId = "";
        String lcPostResultName = "";
        for ( SuSEntity suSEntity : solveCreatedObjects ) {
            // hack start - avoid using hard coded typeId, instead get from
            // config
            if ( suSEntity.getTypeId().equals( UUID.fromString( "da20bdbb-b540-41bc-b88d-ca93b65fd0d8" ) ) ) {
                // hack start
                FiltersDTO filtersDTO = new FiltersDTO();
                List< Object > objects = new ArrayList<>();
                objects.add( suSEntity.getComposedId().getId() );
                filtersDTO.setItems( objects );
                SelectionResponseUI selection = selectionManager.createSelection( entityManager, userIdFromGeneralHeader,
                        SelectionOrigins.RUN_DUMMY, filtersDTO );

                lcPostResultName = suSEntity.getName() + "_PPO";

                lcResultSelectionId = selection.getId();
            }
        }

        map.put( "{{Post.resultId}}", lcResultSelectionId );
        map.put( "{{Post.resultName}}", lcPostResultName );
        map.put( "{{Post.variantName}}", variantWizardDTO.getName() );
        map.put( "{{Post.variantID}}", variantWizardDTO.getId().toString() );
        map.put( "{{Post.loadcaseName}}", selectedLoadcaseName );
        map.put( "{{Post.currentLoadCaseCount}}", executedLoadcase );
        map.put( "{{Post.totalLoadCaseCount}}", selectedLoadcaseCount );
        map.put( "{{SuS.scripts}}", scriptsPath );
        FiltersDTO filtersDTO = new FiltersDTO();
        List< Object > objects = new ArrayList<>();
        objects.add( lID );
        filtersDTO.setItems( objects );
        map.put( "{{Post.variantImportId}}",
                selectionManager.createSelection( entityManager, userIdFromGeneralHeader, SelectionOrigins.RUN_DUMMY, filtersDTO )
                        .getId() );
        log.info( "<<preparePostVariables selectedLoadcaseName: " + selectedLoadcaseName );
        return map;
    }

    /**
     * Creates the submodels selection id.
     *
     * @param entityManager
     *         the entity manager
     * @param selectedLoadcaseId
     *         the selected loadcase id
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     * @param variantWizardDTO
     *         the variant wizard DTO
     *
     * @return the string
     */
    private String createSubmodelsSelectionId( EntityManager entityManager, LoadCaseDTO selectedLoadcaseId, String userIdFromGeneralHeader,
            String id, VariantWizardDTO variantWizardDTO ) {
        List< String > lcSubmodelIds = new ArrayList<>();
        Map< String, Integer > loadcaseValues = new HashMap<>();

        List< String > selectedObjectIds = new ArrayList<>();
        if ( variantWizardDTO.getObjectSelectionId() != null ) {
            selectedObjectIds = selectionManager.getSelectedIdsStringListBySelectionId( entityManager,
                    variantWizardDTO.getObjectSelectionId().toString() );
        }

        if ( !selectedObjectIds.isEmpty() ) {
            for ( String submodelId : selectedObjectIds ) {
                if ( variantWizardDTO.getObjectLoadcaseRelation() != null && variantWizardDTO.getObjectLoadcaseRelation()
                        .containsKey( submodelId ) ) {
                    loadcaseValues = ( Map< String, Integer > ) JsonUtils.jsonToMap(
                            JsonUtils.objectToJson( variantWizardDTO.getObjectLoadcaseRelation().get( submodelId ) ), loadcaseValues );
                    if ( null != loadcaseValues && loadcaseValues.containsKey( selectedLoadcaseId.getId().toString() )
                            && loadcaseValues.get( selectedLoadcaseId.getId().toString() ) == 1 ) {
                        lcSubmodelIds.add( submodelId );
                    }
                }
            }
        }
        FiltersDTO filtersDTO = new FiltersDTO();
        List< Object > objects = new ArrayList<>();
        for ( String uuid : lcSubmodelIds ) {
            objects.add( uuid );
        }
        filtersDTO.setItems( objects );
        SelectionResponseUI selection = selectionManager.createSelection( entityManager, userIdFromGeneralHeader,
                SelectionOrigins.RUN_DUMMY, filtersDTO );

        return selection.getId();

    }

    /**
     * Creates the variant entity from variant wizard DTO.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     * @param variantWizardDTO
     *         the variant wizard DTO
     *
     * @return the variant wizard entity
     */
    private VariantWizardEntity createVariantEntityFromVariantWizardDTO( EntityManager entityManager, String id,
            VariantWizardDTO variantWizardDTO ) {
        boolean isCopy = true;
        log.info( ">>saveVariant variantWizardDTO:" + variantWizardDTO );
        if ( variantWizardDTO.getCopy().equalsIgnoreCase( "No" ) ) {
            isCopy = false;
        }
        VariantWizardEntity entity = wizDAO.findById( entityManager, UUID.fromString( id ) );
        if ( entity == null ) {
            entity = new VariantWizardEntity();
            entity.setId( UUID.fromString( id ) );
        }
        entity.setCopy( isCopy );
        // missing setting loadcaseSubmit amd objectLoadcaseRelation, as they
        // are
        // controlled by checkbox APIs
        if ( null != variantWizardDTO.getObjectSelectionId() ) {
            entity.setObjectSelectionId( variantWizardDTO.getObjectSelectionId() );
        }
        if ( null != variantWizardDTO.getLoadcaseSelectionId() ) {
            entity.setLoadcaseSelectionId( variantWizardDTO.getLoadcaseSelectionId() );
        }
        if ( null != variantWizardDTO.getReferenceId() ) {
            entity.setReferenceId( variantWizardDTO.getReferenceId() );
        }
        if ( null != variantWizardDTO.getAssemble() ) {
            entity.setAssemble( JsonUtils.toJson( variantWizardDTO.getAssemble() ) );
        }
        if ( null != variantWizardDTO.getSolve() ) {
            entity.setSolve( JsonUtils.toJson( variantWizardDTO.getSolve() ) );
        }
        if ( null != variantWizardDTO.getPost() ) {
            entity.setPost( JsonUtils.toJson( variantWizardDTO.getPost() ) );
        }
        if ( null != variantWizardDTO.getSolverType() ) {
            entity.setSolverType( variantWizardDTO.getSolverType() );
        }

        entity.setJobDescription( variantWizardDTO.getJobDescription() );

        return entity;
    }

    /**
     * Create cb 2 variant entity from variant wizard dto variant wizard entity.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     * @param variantWizardDTO
     *         the variant wizard dto
     *
     * @return the variant wizard entity
     */
    private VariantWizardEntity createCb2VariantEntityFromVariantWizardDTO( EntityManager entityManager, String id,
            Cb2VariantWizardDTO variantWizardDTO ) {

        log.info( ">>saveVariant cb2variantWizardDTO:" + variantWizardDTO );

        VariantWizardEntity entity = wizDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( id ) );
        if ( entity == null ) {
            entity = new VariantWizardEntity();
            entity.setId( UUID.fromString( id ) );
        }

        entity.setObjectSelectionId( variantWizardDTO.getObjectSelectionId() );
        entity.setLoadcaseSelectionId( variantWizardDTO.getLoadcaseSelectionId() );

        if ( null != variantWizardDTO.getAssemble() ) {
            entity.setAssemble( JsonUtils.toJson( variantWizardDTO.getAssemble() ) );
        } else {
            entity.setAssemble( null );
        }

        if ( null != variantWizardDTO.getSolve() ) {
            entity.setSolve( JsonUtils.toJson( variantWizardDTO.getSolve() ) );
        } else {
            entity.setSolve( null );
        }

        if ( null != variantWizardDTO.getPost() ) {
            entity.setPost( JsonUtils.toJson( variantWizardDTO.getPost() ) );
        } else {
            entity.setPost( null );
        }
        if ( null != variantWizardDTO.getGeneralVariantOverview() ) {
            entity.setGeneralVariantOverview( JsonUtils.toJson( variantWizardDTO.getGeneralVariantOverview() ) );
        } else {
            entity.setGeneralVariantOverview( null );
        }

        entity.setJobDescription( variantWizardDTO.getJobDescription() );
        entity.setGeneralProjectSelectionId( variantWizardDTO.getGeneralProjectSelectionId() );
        entity.setGeneralItem( variantWizardDTO.getGeneralItem() );
        entity.setGeneralVariantDefination( variantWizardDTO.getGeneralVariantDefination() );
        entity.setGeneralVariantType( variantWizardDTO.getGeneralVariantType() );
        entity.setGeneralDerivedFrom( variantWizardDTO.getGeneralDerivedFrom() );
        entity.setGeneralProjectPhase( variantWizardDTO.getGeneralProjectPhase() );
        entity.setGeneralSimulationGeneratorSettings( variantWizardDTO.getGeneralSimulationGeneratorSettings() );
        entity.setGeneralDisciplineContext( variantWizardDTO.getGeneralDisciplineContext() );

        return entity;
    }

    /**
     * Creates the varaint wizard DTO from entity.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return the variant wizard DTO
     */
    private VariantWizardDTO createVaraintWizardDTOFromEntity( EntityManager entityManager, VariantWizardEntity entity ) {
        VariantWizardDTO wizDTO = new VariantWizardDTO();
        VariantDTO variant = ( VariantDTO ) dataObjectManager.createDTOFromSusEntity( entityManager, null,
                susDAO.getLatestNonDeletedObjectById( entityManager, entity.getId() ), true );
        wizDTO.setName( variant.getName() );
        wizDTO.setId( variant.getId() );
        wizDTO.setVersion( variant.getVersion() );
        wizDTO.setCustomAttributes( variant.getCustomAttributes() );

        wizDTO.setJobDescription( entity.getJobDescription() );
        wizDTO.setLoadcaseSelectionId( entity.getLoadcaseSelectionId() );
        wizDTO.setObjectSelectionId( entity.getObjectSelectionId() );

        if ( null != entity.getSolverType() ) {
            wizDTO.setSolverType( entity.getSolverType() );
        }

        if ( entity.isCopy() ) {
            wizDTO.setCopy( "Yes" );
        } else {
            wizDTO.setCopy( "No" );
        }
        wizDTO.setReferenceId( entity.getReferenceId() );
        if ( entity.getLoadcaseSubmit() != null ) {
            Map< String, Object > loadcaseSubmit = new HashMap<>();
            loadcaseSubmit = ( Map< String, Object > ) JsonUtils.jsonToMap( entity.getLoadcaseSubmit(), loadcaseSubmit );
            wizDTO.setLoadcaseSubmit( loadcaseSubmit );
        }
        if ( entity.getObjectLoadcaseRelation() != null ) {
            Map< String, Object > objectToLoadcaseRelations = new HashMap<>();
            objectToLoadcaseRelations = ( Map< String, Object > ) JsonUtils.jsonToMap( entity.getObjectLoadcaseRelation(),
                    objectToLoadcaseRelations );
            wizDTO.setObjectLoadcaseRelation( objectToLoadcaseRelations );
        }
        if ( entity.getAssemble() != null ) {
            List< LoadcaseWFModel > list = JsonUtils.jsonToList( entity.getAssemble(), LoadcaseWFModel.class );
            wizDTO.setAssemble( list );
        }
        if ( entity.getSolve() != null ) {
            List< LoadcaseWFModel > list = JsonUtils.jsonToList( entity.getSolve(), LoadcaseWFModel.class );
            wizDTO.setSolve( list );
        }
        if ( entity.getPost() != null ) {
            List< LoadcaseWFModel > list = JsonUtils.jsonToList( entity.getPost(), LoadcaseWFModel.class );
            wizDTO.setPost( list );
        }
        return wizDTO;
    }

    /**
     * Create cb 2 varaint wizard dto from entity cb 2 variant wizard dto.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param entity
     *         the entity
     *
     * @return the cb 2 variant wizard dto
     */
    private Cb2VariantWizardDTO createCb2VaraintWizardDTOFromEntity( EntityManager entityManager, String userIdFromGeneralHeader,
            VariantWizardEntity entity ) {
        Cb2VariantWizardDTO wizDTO = new Cb2VariantWizardDTO();
        VariantDTO variant = ( VariantDTO ) dataObjectManager.createDTOFromSusEntity( entityManager, null,
                susDAO.getLatestNonDeletedObjectById( entityManager, entity.getId() ), true );
        wizDTO.setName( variant.getName() );
        wizDTO.setId( variant.getId() );
        wizDTO.setVersion( variant.getVersion() );
        wizDTO.setCustomAttributes( variant.getCustomAttributes() );
        wizDTO.setDescription( variant.getDescription() );
        wizDTO.setJobDescription( entity.getJobDescription() );
        wizDTO.setLoadcaseSelectionId( entity.getLoadcaseSelectionId() );
        wizDTO.setObjectSelectionId( entity.getObjectSelectionId() );

        if ( entity.getLoadcaseSubmit() != null ) {
            Map< String, Object > loadcaseSubmit = new HashMap<>();
            loadcaseSubmit = ( Map< String, Object > ) JsonUtils.jsonToMap( entity.getLoadcaseSubmit(), loadcaseSubmit );
            wizDTO.setLoadcaseSubmit( loadcaseSubmit );
        }
        if ( entity.getObjectLoadcaseRelation() != null ) {
            Map< String, Object > objectToLoadcaseRelations = new HashMap<>();
            objectToLoadcaseRelations = ( Map< String, Object > ) JsonUtils.jsonToMap( entity.getObjectLoadcaseRelation(),
                    objectToLoadcaseRelations );
            wizDTO.setObjectLoadcaseRelation( objectToLoadcaseRelations );
        }
        if ( entity.getAssemble() != null ) {
            List< Map< String, String > > list = new ArrayList<>();
            list = ( List< Map< String, String > > ) JsonUtils.jsonToList( entity.getAssemble(), list );
            wizDTO.setAssemble( list );
        }
        if ( entity.getSolve() != null ) {
            List< Map< String, String > > list = new ArrayList<>();
            list = ( List< Map< String, String > > ) JsonUtils.jsonToList( entity.getSolve(), list );
            wizDTO.setSolve( list );
        }
        if ( entity.getPost() != null ) {
            List< Map< String, String > > list = new ArrayList<>();
            list = ( List< Map< String, String > > ) JsonUtils.jsonToList( entity.getPost(), list );
            wizDTO.setPost( list );
        }
        if ( entity.getGeneralVariantOverview() != null ) {
            List< AdditionalFiles > list = JsonUtils.jsonToList( entity.getGeneralVariantOverview(), AdditionalFiles.class );
            wizDTO.setGeneralVariantOverview( list );
        }

        wizDTO.setGeneralProjectSelectionId( entity.getGeneralProjectSelectionId() );
        wizDTO.setGeneralItem( entity.getGeneralItem() );
        wizDTO.setGeneralVariantDefination( entity.getGeneralVariantDefination() );
        wizDTO.setGeneralVariantType( entity.getGeneralVariantType() );
        wizDTO.setGeneralDerivedFrom( entity.getGeneralDerivedFrom() );
        wizDTO.setGeneralProjectPhase( entity.getGeneralProjectPhase() );
        wizDTO.setGeneralSimulationGeneratorSettings( entity.getGeneralSimulationGeneratorSettings() );
        wizDTO.setGeneralDisciplineContext( entity.getGeneralDisciplineContext() );

        return wizDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > findMenu( SuSEntity e ) {
        /*
         * if ( e.getComposedId().getId().toString().equals(
         * WizardsFeaturesEnum.LOADCASES.getId() ) ) { ContextMenuItem containerCMI =
         * new ContextMenuItem(); containerCMI.setUrl( "url" ); containerCMI.setTitle(
         * "Plugin Click" ); return Arrays.asList( containerCMI ); }
         */
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getObjectsTabUI( String userIdFromGeneralHeader, String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< TableColumn > columns = new ArrayList<>();
            int count = 0;
            VariantWizardDTO runItem = getVariant( entityManager, userIdFromGeneralHeader, id );
            if ( runItem.getLoadcaseSelectionId() != null ) {
                getRunVariantLoadcaseColumns( columns, count, runItem, entityManager );
            }
            if ( runItem.getReferenceId() != null ) {
                VariantWizardDTO refVariant = getReferenceVariant( entityManager, runItem.getReferenceId(), userIdFromGeneralHeader );
                if ( null != refVariant ) {
                    getRunVariantLoadcaseColumns( columns, count, refVariant, entityManager );
                }
            }
            columns.addAll( GUIUtils.listColumns( BmwCaeBenchDTO.class ) );
            return columns;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the reference variant.
     *
     * @param entityManager
     *         the entity manager
     * @param referenceVariantId
     *         the reference variant id
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the reference variant
     */
    private VariantWizardDTO getReferenceVariant( EntityManager entityManager, UUID referenceVariantId, String userIdFromGeneralHeader ) {
        List< UUID > referenceIds = selectionManager.getSelectedIdsListBySelectionId( entityManager, referenceVariantId.toString() );
        return !referenceIds.isEmpty() ? getVariant( entityManager, userIdFromGeneralHeader,
                referenceIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ).toString() ) : null;
    }

    /**
     * Gets the run variant loadcase columns.
     *
     * @param columns
     *         the columns
     * @param count
     *         the count
     * @param runVariant
     *         the run item
     * @param entityManager
     *         the entity manager
     *
     * @return the run variant loadcase columns
     */
    private void getRunVariantLoadcaseColumns( List< TableColumn > columns, int count, VariantWizardDTO runVariant,
            EntityManager entityManager ) {
        List< UUID > selectedLoadcaseIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                runVariant.getLoadcaseSelectionId().toString() );
        for ( UUID selectedLoadcaseId : selectedLoadcaseIds ) {
            TableColumn column = new TableColumn();
            column.setData( "loadcases." + count + ".value" );
            column.setName( "loadcases." + selectedLoadcaseId );
            column.setRotated( true );
            Renderer renderer = new Renderer();
            renderer.setType( "checkbox" );
            renderer.setUrl( "url" );
            renderer.setData( null );
            renderer.setManage( true );
            column.setTitle( susDAO.getLatestNonDeletedObjectById( entityManager, selectedLoadcaseId ).getName() );
            column.setRenderer( renderer );
            count += 1;
            columns.add( column );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getCb2ObjectsTabUI( String userIdFromGeneralHeader, String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< TableColumn > columns = new ArrayList<>();
            int count = 0;

            Cb2VariantWizardDTO runItem = getCb2Variant( entityManager, userIdFromGeneralHeader, id );
            if ( runItem.getLoadcaseSelectionId() != null ) {
                List< UUID > selectedLoadcaseIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                        runItem.getLoadcaseSelectionId().toString() );
                for ( UUID selectedLoadcaseId : selectedLoadcaseIds ) {
                    TableColumn column = new TableColumn();
                    column.setData( "loadcases." + count + ".value" );
                    column.setName( "loadcases." + selectedLoadcaseId );
                    column.setRotated( true );
                    Renderer renderer = new Renderer();
                    renderer.setType( "checkbox" );
                    renderer.setUrl( "url" );
                    renderer.setData( null );
                    renderer.setManage( true );
                    column.setTitle( bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager, selectedLoadcaseId ).getName() );
                    column.setRenderer( renderer );
                    count += 1;
                    columns.add( column );
                }
            }
            columns.addAll( GUIUtils.listColumns( BmwCaeBenchSubModelDTO.class ) );
            return columns;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Object > getObjectsTabData( String userIdFromGeneralHeader, String id, FiltersDTO filterDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            VariantWizardDTO runItem = getVariant( entityManager, userIdFromGeneralHeader, id );
            List< Object > objectDTOList = new ArrayList<>();

            if ( runItem.getReferenceId() != null ) {
                VariantWizardDTO refVariant = getReferenceVariant( entityManager, runItem.getReferenceId(), userIdFromGeneralHeader );
                if ( null != refVariant ) {
                    getRunVariantObjectList( entityManager, refVariant, filterDTO, userIdFromGeneralHeader, objectDTOList );
                }
            }

            getRunVariantObjectList( entityManager, runItem, filterDTO, userIdFromGeneralHeader, objectDTOList );

            return PaginationUtil.constructFilteredResponse( filterDTO, objectDTOList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the run variant object list.
     *
     * @param entityManager
     *         the entity manager
     * @param runVariant
     *         the run variant
     * @param filterDTO
     *         the filter DTO
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the run variant object list
     */
    private void getRunVariantObjectList( EntityManager entityManager, VariantWizardDTO runVariant, FiltersDTO filterDTO,
            String userIdFromGeneralHeader, List< Object > objectDTOList ) {
        List< UUID > selectedLoadcaseIds = new ArrayList<>();
        if ( runVariant.getLoadcaseSelectionId() != null ) {
            selectedLoadcaseIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                    runVariant.getLoadcaseSelectionId().toString() );
        }
        List< UUID > selectedObjectIds = new ArrayList<>();
        if ( runVariant.getObjectSelectionId() != null ) {
            selectedObjectIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                    runVariant.getObjectSelectionId().toString() );
        }

        List< SuSEntity > unSortedList = susDAO.getAllFilteredRecordsByObjectIds( entityManager, SuSEntity.class, selectedObjectIds,
                filterDTO, configManager.getAllTypesFromConfiguration() );

        List< SuSEntity > sortedList = new ArrayList<>();
        for ( UUID objSids : selectedObjectIds ) {
            for ( SuSEntity suSEntity : unSortedList ) {
                if ( suSEntity.getComposedId().getId().equals( objSids ) ) {
                    sortedList.add( suSEntity );
                }
            }
        }

        for ( SuSEntity suSEntity : sortedList ) {
            objectDTOList.add( createVariantObjectDTOFromEntity( entityManager, userIdFromGeneralHeader, suSEntity, selectedLoadcaseIds,
                    runVariant.getObjectLoadcaseRelation() ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Object > getCb2ObjectsTabData( String userIdFromGeneralHeader, String id, FiltersDTO filterDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filterDTO.getTotalRecords() == null ) {
                filterDTO.setTotalRecords( Long.valueOf( ConstantsInteger.INTEGER_VALUE_ZERO ) );
            }
            Cb2VariantWizardDTO runItem = getCb2Variant( entityManager, userIdFromGeneralHeader, id );
            List< UUID > selectedLoadcaseIds = new ArrayList<>();
            if ( runItem.getLoadcaseSelectionId() != null ) {
                selectedLoadcaseIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                        runItem.getLoadcaseSelectionId().toString() );
            }
            List< UUID > selectedObjectIds = new ArrayList<>();
            if ( runItem.getObjectSelectionId() != null ) {
                selectedObjectIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                        runItem.getObjectSelectionId().toString() );
            }

            List< BmwCaeBenchEntity > sortedList = new ArrayList<>();
            for ( UUID objSids : selectedObjectIds ) {
                BmwCaeBenchEntity bmwOnj = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager, objSids );
                if ( bmwOnj != null ) {
                    sortedList.add( bmwOnj );
                }
            }

            List< Object > objectDTOList = new ArrayList<>();
            for ( BmwCaeBenchEntity bmwCaeBenchEntity : sortedList ) {
                objectDTOList.add( createObjectsSubModelDTOFromEntity( entityManager, bmwCaeBenchEntity, selectedLoadcaseIds,
                        runItem.getObjectLoadcaseRelation() ) );
            }
            return PaginationUtil.constructFilteredResponse( filterDTO, objectDTOList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Creates the variant object DTO from entity.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param susEntity
     *         the sus entity
     * @param selectedLoadcaseIds
     *         the selected loadcase ids
     * @param objectLoadcaseRelations
     *         the object loadcase relations
     *
     * @return the object
     */
    private Object createVariantObjectDTOFromEntity( EntityManager entityManager, String userIdFromGeneralHeader, SuSEntity susEntity,
            List< UUID > selectedLoadcaseIds, Map< String, Object > objectLoadcaseRelations ) {
        GenericDTO objectDTO = null;
        if ( susEntity != null ) {
            VariantObjectDTO genericDTO = new VariantObjectDTO();
            genericDTO.setName( susEntity.getName() );
            genericDTO.setId( susEntity.getComposedId().getId() );
            genericDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
            genericDTO.setCreatedOn( susEntity.getCreatedOn() );
            genericDTO.setModifiedOn( susEntity.getModifiedOn() );
            genericDTO.setDescription( susEntity.getDescription() );
            if ( susEntity.getTypeId() != null ) {
                genericDTO.setTypeId( susEntity.getTypeId() );
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
            if ( null != susEntity.getCreatedBy() ) {
                genericDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getCreatedBy() ) );
            }
            if ( null != susEntity.getModifiedBy() ) {
                genericDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getModifiedBy() ) );
            }
            genericDTO.setSize( susEntity.getSize() != null && susEntity.getSize() > ConstantsInteger.INTEGER_VALUE_ZERO
                    ? org.apache.commons.io.FileUtils.byteCountToDisplaySize( susEntity.getSize() ) : ConstantsString.NOT_AVAILABLE );
            List< VariantLoadcaseDTO > loadcases = new ArrayList<>();
            for ( UUID loadCaseId : selectedLoadcaseIds ) {
                VariantLoadcaseDTO loadcaseDTO = loadcaseManager.getVaraintLoadcase( entityManager, userIdFromGeneralHeader, loadCaseId );
                loadcaseDTO.setValue( 0 );
                if ( null != objectLoadcaseRelations && objectLoadcaseRelations.containsKey(
                        susEntity.getComposedId().getId().toString() ) ) {
                    Map< String, Integer > loadcaseValues = new HashMap<>();

                    loadcaseValues = ( Map< String, Integer > ) JsonUtils.jsonToMap(
                            JsonUtils.objectToJson( objectLoadcaseRelations.get( susEntity.getComposedId().getId().toString() ) ),
                            loadcaseValues );
                    if ( null != loadcaseValues && loadcaseValues.containsKey( loadCaseId.toString() ) ) {
                        loadcaseDTO.setValue( loadcaseValues.get( loadCaseId.toString() ) );
                    }

                }
                loadcases.add( loadcaseDTO );
            }

            List< Relation > relation = linkDao.getLinkedRelationByChildId( entityManager, susEntity.getComposedId().getId() );
            if ( CollectionUtils.isNotEmpty( relation ) ) {
                genericDTO.setLink( LINK_TYPE_YES );
            } else {
                genericDTO.setLink( LINK_TYPE_NO );
            }

            genericDTO.setLoadcases( loadcases );

            objectDTO = genericDTO;
        }
        return objectDTO;
    }

    /**
     * Create objects sub model dto from entity objects sub model dto.
     *
     * @param entityManager
     *         the entity manager
     * @param bmwCaeBenchEntity
     *         the bmw cae bench entity
     * @param selectedLoadcaseIds
     *         the selected loadcase ids
     * @param objectLoadcaseRelations
     *         the object loadcase relations
     *
     * @return the objects sub model dto
     */
    private ObjectsSubModelDTO createObjectsSubModelDTOFromEntity( EntityManager entityManager, BmwCaeBenchEntity bmwCaeBenchEntity,
            List< UUID > selectedLoadcaseIds, Map< String, Object > objectLoadcaseRelations ) {

        ObjectsSubModelDTO submodelDTO = new ObjectsSubModelDTO();

        submodelDTO.setAssembleType( bmwCaeBenchEntity.getAssembleType() );
        submodelDTO.setCarProject( bmwCaeBenchEntity.getCarProject() );
        submodelDTO.setCreatedAt( bmwCaeBenchEntity.getCreatedAt() );
        submodelDTO.setCreatedBy( bmwCaeBenchEntity.getCreatedBy() );
        submodelDTO.setDescription( bmwCaeBenchEntity.getDescription() );
        submodelDTO.setDisciplineContext( bmwCaeBenchEntity.getDisciplineContext() );
        submodelDTO.setFormat( bmwCaeBenchEntity.getFormat() );
        submodelDTO.setId( bmwCaeBenchEntity.getId() );
        submodelDTO.setLabel( bmwCaeBenchEntity.getLabel() );
        submodelDTO.setModelDefLabel( bmwCaeBenchEntity.getModelDefLabel() );
        submodelDTO.setModelState( bmwCaeBenchEntity.getModelState() );
        submodelDTO.setName( bmwCaeBenchEntity.getName() );
        submodelDTO.setOid( bmwCaeBenchEntity.getOid() );
        submodelDTO.setOwner( bmwCaeBenchEntity.getOwner() );
        submodelDTO.setProject( bmwCaeBenchEntity.getProject() );
        submodelDTO.setType( bmwCaeBenchEntity.getType() );
        submodelDTO.setVariantLabel( bmwCaeBenchEntity.getVariantLabel() );

        List< VariantSubModelDTO > loadcases = new ArrayList<>();
        for ( UUID senarioId : selectedLoadcaseIds ) {

            BmwCaeBenchEntity entiry = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager, senarioId );

            VariantSubModelDTO submodlDto = prepareVariantSubModelDTOByEntity( entiry );
            submodlDto.setValue( 0 );

            if ( null != submodlDto && objectLoadcaseRelations != null && objectLoadcaseRelations.containsKey(
                    bmwCaeBenchEntity.getId().toString() ) ) {

                Map< String, Integer > loadcaseValues = new HashMap<>();

                loadcaseValues = ( Map< String, Integer > ) JsonUtils.jsonToMap(
                        JsonUtils.objectToJson( objectLoadcaseRelations.get( bmwCaeBenchEntity.getId().toString() ) ), loadcaseValues );
                if ( null != loadcaseValues && loadcaseValues.containsKey( senarioId.toString() ) ) {
                    submodlDto.setValue( loadcaseValues.get( senarioId.toString() ) );
                }

            }
            loadcases.add( submodlDto );
        }

        submodelDTO.setLoadcases( loadcases );

        List< Relation > relation = linkDao.getLinkedRelationByChildId( entityManager, bmwCaeBenchEntity.getId() );
        if ( CollectionUtils.isNotEmpty( relation ) ) {
            submodelDTO.setLink( LINK_TYPE_YES );
        } else {
            submodelDTO.setLink( LINK_TYPE_NO );
        }
        return submodelDTO;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getAssemblyTabUI( String userIdFromGeneralHeader, String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > formItems = new ArrayList<>();

            VariantWizardDTO runItem = getVariant( entityManager, userIdFromGeneralHeader, id );

            if ( runItem.getReferenceId() != null ) {
                VariantWizardDTO refVariant = getReferenceVariant( entityManager, runItem.getReferenceId(), userIdFromGeneralHeader );
                if ( null != refVariant ) {
                    getLoadcaseFormItemsForAsseblyTab( formItems, entityManager, refVariant );
                }
            }

            if ( runItem.getLoadcaseSelectionId() != null ) {
                getLoadcaseFormItemsForAsseblyTab( formItems, entityManager, runItem );
            }

            return GUIUtils.createFormFromItems( formItems );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the loadcase form items for assebly tab.
     *
     * @param formItems
     *         the form items
     * @param entityManager
     *         the entity manager
     * @param runVariant
     *         the run variant
     *
     * @return the loadcase form items for assebly tab
     */
    private void getLoadcaseFormItemsForAsseblyTab( List< UIFormItem > formItems, EntityManager entityManager,
            VariantWizardDTO runVariant ) {
        List< UUID > selectedLoadcaseIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                runVariant.getLoadcaseSelectionId().toString() );
        int counter = 0;
        for ( UUID selectedLoadcaseId : selectedLoadcaseIds ) {

            LoadCaseEntity loadCase = ( LoadCaseEntity ) susDAO.getLatestObjectById( entityManager, LoadCaseEntity.class,
                    selectedLoadcaseId );
            if ( loadCase != null ) {
                SectionFormItem sectionFormItem = ( SectionFormItem ) GUIUtils.createFormItem( FormItemType.SECTION );
                sectionFormItem.setTitle( loadCase.getName() );
                sectionFormItem.setType( "section" );
                formItems.add( sectionFormItem );

                List< SelectOptionsUI > options = new ArrayList<>();
                LoadCaseWizardEntity loadCaseWizardEntity = loadcaseWizDAO.findById( entityManager, selectedLoadcaseId );
                SelectFormItem item = prepareAssemblyDropDwon( entityManager, "assemble[" + counter + "].id", loadCaseWizardEntity,
                        options );
                if ( runVariant.getAssemble() != null && runVariant.getAssemble().size() > counter
                        && runVariant.getAssemble().get( counter ) != null ) {
                    item.setValue( runVariant.getAssemble().get( counter ).getId() );
                }

                formItems.add( item );
                counter += 1;
            } else {
                throw new SusException( "Selected Item(s) is not a Loadcase." );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getCb2AssemblyTabUI( String userIdFromGeneralHeader, String userName, String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > result = new ArrayList<>();
            Cb2VariantWizardDTO runItem = getCb2Variant( entityManager, userIdFromGeneralHeader, id );

            if ( runItem.getLoadcaseSelectionId() == null ) {
                log.info( "Loadcase/Senario Selection do not exist" );
                return GUIUtils.createFormFromItems( result );
            }
            List< UUID > selectedLoadcaseIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                    runItem.getLoadcaseSelectionId().toString() );
            if ( selectedLoadcaseIds == null || selectedLoadcaseIds.isEmpty() ) {
                log.info( "Project Selection Item do not exist" );
                return GUIUtils.createFormFromItems( result );
            }

            if ( runItem.getGeneralProjectSelectionId() == null || runItem.getGeneralProjectSelectionId().isEmpty() ) {
                log.info( "Project Selection do not exist" );
                return GUIUtils.createFormFromItems( result );
            }

            List< UUID > generalProjectSelectionList = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                    runItem.getGeneralProjectSelectionId() );
            if ( generalProjectSelectionList == null || generalProjectSelectionList.isEmpty() ) {
                log.info( "Project Selection Item do not exist" );
                return GUIUtils.createFormFromItems( result );
            }

            int counter = 0;
            for ( UUID selectedLoadcaseId : selectedLoadcaseIds ) {
                BmwCaeBenchEntity bmwSenario = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager, selectedLoadcaseId );
                if ( bmwSenario != null ) {
                    BmwCaeBenchSenarioDTO bmwSenarioLoadcase = prepareBmwCaeBenchSenarioByEntity( bmwSenario );

                    // TODO pass Default's simDefObjectId from its loadcase
                    JSONObject assembleJsonOptions = cb2DummyManager.getCb2RunVariantAssembleFormXML( userName, "" );
                    Map< String, Map< String, String > > cb2AssembleForm = cb2DummyManager.downloadPimByOidAndReadPim( userName,
                            assembleJsonOptions );
                    String pimTypeName = extractPimType( assembleJsonOptions );
                    prepareCb2AssemblyForm2( "assemble[" + counter + "].", result, runItem, cb2AssembleForm, bmwSenarioLoadcase );

                    addExtraHiddenFields( "assemble[" + counter + "].", result, pimTypeName, "assemblePimType", "Assemble Pim Type" );
                } else {
                    throw new SusException( "Selected Item(s) Do not Exisits" );
                }
                counter += 1;
            }

            return GUIUtils.createFormFromItems( result );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare cb 2 assembly form 2.
     *
     * @param idAndSepratorObject
     *         the id and seprator object
     * @param result
     *         the result
     * @param runItem
     *         the run item
     * @param cb2OptionsMap
     *         the cb 2 options map
     * @param bmwSenarioLoadcase
     *         the bmw senario loadcase
     */
    private void prepareCb2AssemblyForm2( String idAndSepratorObject, List< UIFormItem > result, Cb2VariantWizardDTO runItem,
            Map< String, Map< String, String > > cb2OptionsMap, BmwCaeBenchSenarioDTO bmwSenarioLoadcase ) {

        Map< String, String > assmblyFormObj = extractVariantFormFormByLoadcaseId( runItem.getAssemble(),
                bmwSenarioLoadcase.getId().toString() );

        SectionFormItem sectionFormItem = ( SectionFormItem ) GUIUtils.createFormItem( FormItemType.SECTION );
        sectionFormItem.setTitle( bmwSenarioLoadcase.getName() );
        sectionFormItem.setType( "section" );
        result.add( sectionFormItem );
        // loadcase OR senario Name name added
        SelectFormItem loadcaseId = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        loadcaseId.setCanBeEmpty( false );
        loadcaseId.setDuplicate( false );
        loadcaseId.setReadonly( false );
        loadcaseId.setLabel( "Assembly loadcase id" );
        loadcaseId.setName( idAndSepratorObject + "id" );
        // loadcaseId.setName( idAndSepratorObject + "assemblyLoadcaseId" );
        loadcaseId.setValue( bmwSenarioLoadcase.getId().toString() );
        loadcaseId.setType( "text" );
        Map< String, Object > rules14 = new HashMap<>();
        Map< String, Object > message43 = new HashMap<>();
        rules14.put( REQUIRED, false );
        message43.put( REQUIRED, MUST_CHOSE_OPTION );
        loadcaseId.setRules( rules14 );
        loadcaseId.setMessages( message43 );
        result.add( loadcaseId );

        if ( cb2OptionsMap != null && !cb2OptionsMap.isEmpty() ) {
            for ( Entry< String, Map< String, String > > pimiFormItem : cb2OptionsMap.entrySet() ) {
                if ( pimiFormItem.getValue().containsKey( "validValues" ) ) {
                    result.add( prepareOptionFormField( idAndSepratorObject, pimiFormItem, assmblyFormObj ) );
                } else {
                    result.add( prepareInputTextField( idAndSepratorObject, pimiFormItem, assmblyFormObj ) );
                }
            }
        }
    }

    /**
     * Add extra hidden fields.
     *
     * @param idAndSepratorObject
     *         the id and seprator object
     * @param result
     *         the result
     * @param value
     *         the value
     * @param name
     *         the name
     * @param label
     *         the label
     */
    private void addExtraHiddenFields( String idAndSepratorObject, List< UIFormItem > result, String value, String name, String label ) {
        SelectFormItem loadcaseId = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        loadcaseId.setCanBeEmpty( false );
        loadcaseId.setDuplicate( false );
        loadcaseId.setReadonly( false );
        loadcaseId.setLabel( label );
        loadcaseId.setName( idAndSepratorObject + name );
        loadcaseId.setValue( value );
        loadcaseId.setType( "text" );
        Map< String, Object > rules14 = new HashMap<>();
        Map< String, Object > message43 = new HashMap<>();
        rules14.put( REQUIRED, false );
        message43.put( REQUIRED, MUST_CHOSE_OPTION );
        loadcaseId.setRules( rules14 );
        loadcaseId.setMessages( message43 );
        result.add( loadcaseId );
    }

    /**
     * Prepare input text field select ui.
     *
     * @param idAndSepratorObject
     *         the id and seprator object
     * @param pimiFormItem
     *         the pimi form item
     * @param respectiveSavedForm
     *         the respective saved form
     *
     * @return the select ui
     */
    private SelectFormItem prepareInputTextField( String idAndSepratorObject, Entry< String, Map< String, String > > pimiFormItem,
            Map< String, String > respectiveSavedForm ) {
        SelectFormItem loadcaseId = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        loadcaseId.setCanBeEmpty( false );
        loadcaseId.setDuplicate( false );
        loadcaseId.setReadonly( false );
        loadcaseId.setLabel( getCapitalLetterNameFromKey( pimiFormItem.getKey() ) );
        loadcaseId.setName( idAndSepratorObject + pimiFormItem.getKey() );
        // loadcaseId.setName( idAndSepratorObject + "assemblyLoadcaseId" );
        loadcaseId.setValue( extractDefaultValueForDynamicForm( pimiFormItem.getValue().get( "defaultValue" ), respectiveSavedForm,
                pimiFormItem.getKey() ) );
        loadcaseId.setType( "text" );
        Map< String, Object > rules14 = new HashMap<>();
        Map< String, Object > message43 = new HashMap<>();
        rules14.put( REQUIRED, false );
        message43.put( REQUIRED, MUST_CHOSE_OPTION );
        loadcaseId.setRules( rules14 );
        loadcaseId.setMessages( message43 );
        return loadcaseId;
    }

    /**
     * Prepare option form field select ui.
     *
     * @param idAndSepratorObject
     *         the id and seprator object
     * @param pimiFormItem
     *         the pimi form item
     * @param respectiveSavedForm
     *         the respective saved form
     *
     * @return the select ui
     */
    private SelectFormItem prepareOptionFormField( String idAndSepratorObject, Entry< String, Map< String, String > > pimiFormItem,
            Map< String, String > respectiveSavedForm ) {

        SelectFormItem optionForm = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        optionForm.setLabel( getCapitalLetterNameFromKey( pimiFormItem.getKey() ) );
        optionForm.setName( idAndSepratorObject + pimiFormItem.getKey() );
        optionForm.setType( "select" );
        List< SelectOptionsUI > options5 = new ArrayList<>();
        for ( String splitterOptions : pimiFormItem.getValue().get( "validValues" ).split( ";" ) ) {
            String value = splitterOptions;
            if ( splitterOptions.contains( "|" ) ) {
                String[] splitedVal = splitterOptions.split( "\\|" );
                value = splitedVal[ 1 ];
            }
            options5.add( new SelectOptionsUI( value, splitterOptions ) );
        }
        optionForm.setValue( extractDefaultValueForDynamicForm( pimiFormItem.getValue().get( "defaultValue" ), respectiveSavedForm,
                pimiFormItem.getKey() ) );
        optionForm.setOptions( options5 );
        return optionForm;
    }

    /**
     * Extract default value for dynamic form string.
     *
     * @param defaultPimValue
     *         the default pim value
     * @param respectiveSavedForm
     *         the respective saved form
     * @param key
     *         the key
     *
     * @return the string
     */
    private String extractDefaultValueForDynamicForm( String defaultPimValue, Map< String, String > respectiveSavedForm, String key ) {
        if ( respectiveSavedForm == null || respectiveSavedForm.isEmpty() ) {
            return defaultPimValue;
        } else {
            return respectiveSavedForm.get( key );
        }
    }

    /**
     * Extract variant form form by loadcase id map.
     *
     * @param listMapCb2Var
     *         the list map cb 2 var
     * @param scenariosId
     *         the scenarios id
     *
     * @return the map
     */
    private Map< String, String > extractVariantFormFormByLoadcaseId( List< Map< String, String > > listMapCb2Var, String scenariosId ) {
        if ( listMapCb2Var != null ) {
            for ( Map< String, String > map : listMapCb2Var ) {
                if ( map.get( "id" ).equals( scenariosId ) ) {
                    return map;
                }
            }
        }
        return new HashMap<>();
    }

    /**
     * Prepare assembly drop dwon.
     *
     * @param entityManager
     *         the entity manager
     * @param name
     *         the name
     * @param loadCaseWizardEntity
     *         the load case wizard entity
     * @param options
     *         the options
     *
     * @return the select UI
     */
    private SelectFormItem prepareAssemblyDropDwon( EntityManager entityManager, String name, LoadCaseWizardEntity loadCaseWizardEntity,
            List< SelectOptionsUI > options ) {

        if ( loadCaseWizardEntity != null && loadCaseWizardEntity.getAssemblySelectionId() != null ) {
            for ( UUID workflowId : selectionManager.getSelectedIdsListBySelectionId( entityManager,
                    loadCaseWizardEntity.getAssemblySelectionId().toString() ) ) {
                SelectOptionsUI project = new SelectOptionsUI();
                project.setId( workflowId.toString() );
                SuSEntity workflow = susDAO.getLatestObjectById( entityManager, WorkflowEntity.class, workflowId );
                project.setName( workflow.getName() );
                options.add( project );
            }
        }

        return prepareAssemblySelectUI( ConstantsString.ASSEMBLY_METHOD, name, options );
    }

    /**
     * Prepare assembly select UI.
     *
     * @param label
     *         the label
     * @param name
     *         the name
     * @param options
     *         the options
     *
     * @return the select UI
     */
    private SelectFormItem prepareAssemblySelectUI( String label, String name, List< SelectOptionsUI > options ) {
        SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        item.setLabel( label );
        item.setOptions( options );
        item.setName( name );
        item.setType( SELECT_WORKFLOW );
        item.setReadonly( false );
        item.setCanBeEmpty( true );
        return item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getSolverTabUI( String userIdFromGeneralHeader, String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > formItems = new ArrayList<>();
            VariantWizardDTO runItem = getVariant( entityManager, userIdFromGeneralHeader, id );

            if ( runItem.getReferenceId() != null ) {
                VariantWizardDTO refVariant = getReferenceVariant( entityManager, runItem.getReferenceId(), userIdFromGeneralHeader );
                if ( null != refVariant ) {
                    getLoadcaseFormItemsForSolverTab( formItems, entityManager, refVariant );
                }
            }

            if ( runItem.getLoadcaseSelectionId() != null ) {
                getLoadcaseFormItemsForSolverTab( formItems, entityManager, runItem );
            }

            return GUIUtils.createFormFromItems( formItems );
        } finally {
            entityManager.close();
        }
    }

    private void getLoadcaseFormItemsForSolverTab( List< UIFormItem > formItems, EntityManager entityManager,
            VariantWizardDTO runVariant ) {
        List< UUID > selectedLoadcaseIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                runVariant.getLoadcaseSelectionId().toString() );
        int counter = 0;
        for ( UUID selectedLoadcaseId : selectedLoadcaseIds ) {

            LoadCaseEntity loadCase = ( LoadCaseEntity ) susDAO.getLatestObjectById( entityManager, LoadCaseEntity.class,
                    selectedLoadcaseId );
            if ( loadCase != null ) {
                SectionFormItem sectionFormItem = ( SectionFormItem ) GUIUtils.createFormItem( FormItemType.SECTION );
                sectionFormItem.setTitle( loadCase.getName() );
                sectionFormItem.setType( "section" );
                formItems.add( sectionFormItem );
                LoadCaseWizardEntity loadCaseWizardEntity = loadcaseWizDAO.findById( entityManager, selectedLoadcaseId );
                SelectFormItem item = prepareSolverDropDown( entityManager, "solve[" + counter + "].id", loadCaseWizardEntity,
                        new ArrayList<>() );
                if ( runVariant.getSolve() != null && runVariant.getSolve().size() > counter
                        && runVariant.getSolve().get( counter ) != null ) {
                    item.setValue( runVariant.getSolve().get( counter ).getId() );
                }
                formItems.add( item );
                counter += 1;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getCb2SolverTabUI( String userIdFromGeneralHeader, String userName, String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > result = new ArrayList<>();
            Cb2VariantWizardDTO runItem = getCb2Variant( entityManager, userIdFromGeneralHeader, id );
            if ( runItem.getLoadcaseSelectionId() == null ) {
                log.info( "Loadcase/Senario Selection do not exist" );
                return GUIUtils.createFormFromItems( result );
            }
            List< UUID > selectedLoadcaseIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                    runItem.getLoadcaseSelectionId().toString() );
            if ( selectedLoadcaseIds == null || selectedLoadcaseIds.isEmpty() ) {
                log.info( "Project Selection Item do not exist" );
                return GUIUtils.createFormFromItems( result );
            }
            if ( runItem.getGeneralProjectSelectionId() == null || runItem.getGeneralProjectSelectionId().isEmpty() ) {
                log.info( "Project Selection do not exist" );
                return GUIUtils.createFormFromItems( result );
            }
            List< UUID > generalProjectSelectionList = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                    runItem.getGeneralProjectSelectionId() );
            if ( generalProjectSelectionList == null || generalProjectSelectionList.isEmpty() ) {
                log.info( "Project Selection Item do not exist" );
                return GUIUtils.createFormFromItems( result );
            }
            int counter = 0;
            for ( UUID selectedLoadcaseId : selectedLoadcaseIds ) {
                BmwCaeBenchEntity bmwSenario = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager, selectedLoadcaseId );
                if ( bmwSenario != null ) {
                    BmwCaeBenchSenarioDTO bmwSenarioLoadcase = prepareBmwCaeBenchSenarioByEntity( bmwSenario );
                    // TODO pass Default's simDefObjectId from its loadcase
                    JSONObject solveJsonOptions = cb2DummyManager.getCb2RunVariantSolverFormXML( userName, "" );
                    Map< String, Map< String, String > > cb2SolverForm = cb2DummyManager.downloadPimByOidAndReadPim( userName,
                            solveJsonOptions );
                    String pimTypeName = extractPimType( solveJsonOptions );
                    prepareCb2VariantSolverForm( "solve[" + counter + "].", result, bmwSenarioLoadcase, cb2SolverForm, runItem );
                    addExtraHiddenFields( "solve[" + counter + "].", result, pimTypeName, "solvePimType", "Solver Pim Type" );
                } else {
                    throw new SusException( "Selected Item(s) Do not Exisits" );
                }
                counter++;
            }
            return GUIUtils.createFormFromItems( result );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare cb 2 variant solver form.
     *
     * @param idAndSepratorObject
     *         the id and seprator object
     * @param result
     *         the result
     * @param bmwSenarioLoadcase
     *         the bmw senario loadcase
     * @param cb2OptionsMap
     *         the cb 2 options map
     * @param runItem
     *         the run item
     */
    private void prepareCb2VariantSolverForm( String idAndSepratorObject, List< UIFormItem > result,
            BmwCaeBenchSenarioDTO bmwSenarioLoadcase, Map< String, Map< String, String > > cb2OptionsMap, Cb2VariantWizardDTO runItem ) {

        Map< String, String > solveFormObjs = extractVariantFormFormByLoadcaseId( runItem.getSolve(),
                bmwSenarioLoadcase.getId().toString() );

        // add section space
        SectionFormItem sectionFormItem = ( SectionFormItem ) GUIUtils.createFormItem( FormItemType.SECTION );
        sectionFormItem.setTitle( bmwSenarioLoadcase.getName() );
        sectionFormItem.setType( "section" );
        result.add( sectionFormItem );

        // loadcase OR senario Name name added
        SelectFormItem loadcaseId = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        loadcaseId.setCanBeEmpty( false );
        loadcaseId.setDuplicate( false );
        loadcaseId.setReadonly( false );
        loadcaseId.setLabel( "Solver loadcase id" );
        loadcaseId.setName( idAndSepratorObject + "id" );
        loadcaseId.setValue( bmwSenarioLoadcase.getId().toString() );
        loadcaseId.setType( "text" );
        Map< String, Object > rules14 = new HashMap<>();
        Map< String, Object > message43 = new HashMap<>();
        rules14.put( REQUIRED, false );
        message43.put( REQUIRED, MUST_CHOSE_OPTION );
        loadcaseId.setRules( rules14 );
        loadcaseId.setMessages( message43 );
        result.add( loadcaseId );

        if ( cb2OptionsMap != null && !cb2OptionsMap.isEmpty() ) {
            for ( Entry< String, Map< String, String > > pimiFormItem : cb2OptionsMap.entrySet() ) {
                if ( pimiFormItem.getValue().containsKey( "validValues" ) ) {
                    result.add( prepareOptionFormField( idAndSepratorObject, pimiFormItem, solveFormObjs ) );
                } else {
                    result.add( prepareInputTextField( idAndSepratorObject, pimiFormItem, solveFormObjs ) );
                }
            }
        }
    }

    /**
     * Gets capital letter name from key.
     *
     * @param key
     *         the key
     *
     * @return the capital letter name from key
     */
    private static String getCapitalLetterNameFromKey( String key ) {
        StringBuilder sb = new StringBuilder();
        String[] camelCaseWords = key.split( "(?=[A-Z])" );
        for ( String part : camelCaseWords ) {
            sb.append( part );
            sb.append( " " );
        }
        return org.apache.commons.lang3.StringUtils.capitalize( sb.toString() );
    }

    /**
     * Prepare solver drop down.
     *
     * @param name
     *         the name
     * @param loadCaseWizardEntity
     *         the load case wizard entity
     * @param options
     *         the options
     *
     * @return the select UI
     */
    private SelectFormItem prepareSolverDropDown( EntityManager entityManager, String name, LoadCaseWizardEntity loadCaseWizardEntity,
            List< SelectOptionsUI > options ) {
        if ( loadCaseWizardEntity != null && loadCaseWizardEntity.getSolverSelectionId() != null ) {
            for ( UUID workflowId : selectionManager.getSelectedIdsListBySelectionId( entityManager,
                    loadCaseWizardEntity.getSolverSelectionId().toString() ) ) {
                SelectOptionsUI project = new SelectOptionsUI();
                project.setId( workflowId.toString() );
                SuSEntity workflow = susDAO.getLatestObjectById( entityManager, WorkflowEntity.class, workflowId );
                project.setName( workflow.getName() );
                options.add( project );
            }
        }
        SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        item.setLabel( "Solver Method" );
        item.setOptions( options );
        item.setName( name );
        item.setType( SELECT_WORKFLOW );
        item.setReadonly( false );
        item.setCanBeEmpty( true );
        return item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getPostTabUI( String userIdFromGeneralHeader, String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > formItems = new ArrayList<>();
            VariantWizardDTO runItem = getVariant( entityManager, userIdFromGeneralHeader, id );

            if ( runItem.getReferenceId() != null ) {
                VariantWizardDTO refVariant = getReferenceVariant( entityManager, runItem.getReferenceId(), userIdFromGeneralHeader );
                if ( null != refVariant ) {
                    getLoadcaseFormItemsForPostTab( formItems, entityManager, refVariant );
                }
            }

            if ( runItem.getLoadcaseSelectionId() != null ) {
                getLoadcaseFormItemsForPostTab( formItems, entityManager, runItem );
            }

            return GUIUtils.createFormFromItems( formItems );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the loadcase form items for post tab.
     *
     * @param formItems
     *         the form items
     * @param entityManager
     *         the entity manager
     * @param runVariant
     *         the run variant
     *
     * @return the loadcase form items for post tab
     */
    private void getLoadcaseFormItemsForPostTab( List< UIFormItem > formItems, EntityManager entityManager, VariantWizardDTO runVariant ) {
        List< UUID > selectedLoadcaseIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                runVariant.getLoadcaseSelectionId().toString() );
        int counter = 0;
        for ( UUID selectedLoadcaseId : selectedLoadcaseIds ) {

            LoadCaseEntity loadCase = ( LoadCaseEntity ) susDAO.getLatestObjectById( entityManager, LoadCaseEntity.class,
                    selectedLoadcaseId );
            if ( loadCase != null ) {
                SectionFormItem sectionFormItem = ( SectionFormItem ) GUIUtils.createFormItem( FormItemType.SECTION );
                sectionFormItem.setTitle( loadCase.getName() );
                sectionFormItem.setType( "section" );
                formItems.add( sectionFormItem );

                List< SelectOptionsUI > options = new ArrayList<>();
                LoadCaseWizardEntity loadCaseWizardEntity = loadcaseWizDAO.findById( entityManager, selectedLoadcaseId );
                SelectFormItem item = preparePostDropDown( entityManager, "post[" + counter + "].id", loadCaseWizardEntity, options );
                if ( runVariant.getPost() != null && runVariant.getPost().size() > counter
                        && runVariant.getPost().get( counter ) != null ) {
                    item.setValue( runVariant.getPost().get( counter ).getId() );
                }
                formItems.add( item );
                counter += 1;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getCb2PostTabUI( String userIdFromGeneralHeader, String id, String userName ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > result = new ArrayList<>();
            Cb2VariantWizardDTO runItem = getCb2Variant( entityManager, userIdFromGeneralHeader, id );
            if ( runItem.getLoadcaseSelectionId() == null ) {
                log.info( "Loadcase/Senario Selection do not exist" );
                return GUIUtils.createFormFromItems( result );
            }
            List< UUID > selectedLoadcaseIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                    runItem.getLoadcaseSelectionId().toString() );
            if ( selectedLoadcaseIds == null || selectedLoadcaseIds.isEmpty() ) {
                log.info( "Project Selection Item do not exist" );
                return GUIUtils.createFormFromItems( result );
            }
            if ( runItem.getGeneralProjectSelectionId() == null || runItem.getGeneralProjectSelectionId().isEmpty() ) {
                log.info( "Project Selection do not exist" );
                return GUIUtils.createFormFromItems( result );
            }
            List< UUID > generalProjectSelectionList = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                    runItem.getGeneralProjectSelectionId() );
            if ( generalProjectSelectionList == null || generalProjectSelectionList.isEmpty() ) {
                log.info( "Project Selection Item do not exist" );
                return GUIUtils.createFormFromItems( result );
            }
            int counter = 0;
            for ( UUID selectedLoadcaseId : selectedLoadcaseIds ) {
                BmwCaeBenchEntity bmwSenario = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager, selectedLoadcaseId );
                if ( bmwSenario != null ) {
                    BmwCaeBenchSenarioDTO bmwSenarioLoadcase = prepareBmwCaeBenchSenarioByEntity( bmwSenario );
                    // TODO pass Default's simDefObjectId from its loadcase
                    JSONObject assembleJsonOPrions = cb2DummyManager.getCb2RunVariantPostFormXML( userName, "" );
                    Map< String, Map< String, String > > cb2PostForm = cb2DummyManager.downloadPimByOidAndReadPim( userName,
                            assembleJsonOPrions );
                    String pimTypeName = extractPimType( assembleJsonOPrions );
                    prepareCb2PostForm( "post[" + counter + "].", result, bmwSenarioLoadcase, cb2PostForm, runItem );
                    addExtraHiddenFields( "post[" + counter + "].", result, pimTypeName, "postPimType", "Post Pim Type" );
                } else {
                    throw new SusException( "Selected Item(s) Do not Exisits" );
                }
                counter++;
            }
            return GUIUtils.createFormFromItems( result );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare cb 2 post form.
     *
     * @param idAndSepratorObject
     *         the id and seprator object
     * @param result
     *         the result
     * @param bmwSenarioLoadcase
     *         the bmw senario loadcase
     * @param cb2OptionsMap
     *         the cb 2 options map
     * @param runItem
     *         the run item
     */
    private void prepareCb2PostForm( String idAndSepratorObject, List< UIFormItem > result, BmwCaeBenchSenarioDTO bmwSenarioLoadcase,
            Map< String, Map< String, String > > cb2OptionsMap, Cb2VariantWizardDTO runItem ) {

        Map< String, String > postFormObj = extractVariantFormFormByLoadcaseId( runItem.getPost(), bmwSenarioLoadcase.getId().toString() );

        SectionFormItem sectionFormItem = ( SectionFormItem ) GUIUtils.createFormItem( FormItemType.SECTION );
        sectionFormItem.setTitle( bmwSenarioLoadcase.getName() );
        sectionFormItem.setType( "section" );
        result.add( sectionFormItem );

        // loadcase OR senario Name name added
        SelectFormItem loadcaseId = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        loadcaseId.setCanBeEmpty( false );
        loadcaseId.setDuplicate( false );
        loadcaseId.setReadonly( false );
        loadcaseId.setLabel( "Solve loadcase id" );
        loadcaseId.setName( idAndSepratorObject + "id" );
        loadcaseId.setValue( bmwSenarioLoadcase.getId().toString() );
        loadcaseId.setType( "text" );
        Map< String, Object > rules14 = new HashMap<>();
        Map< String, Object > message43 = new HashMap<>();
        rules14.put( REQUIRED, false );
        message43.put( REQUIRED, MUST_CHOSE_OPTION );
        loadcaseId.setRules( rules14 );
        loadcaseId.setMessages( message43 );
        result.add( loadcaseId );

        if ( cb2OptionsMap != null && !cb2OptionsMap.isEmpty() ) {
            for ( Entry< String, Map< String, String > > pimiFormItem : cb2OptionsMap.entrySet() ) {
                if ( pimiFormItem.getValue().containsKey( "validValues" ) ) {
                    result.add( prepareOptionFormField( idAndSepratorObject, pimiFormItem, postFormObj ) );
                } else {
                    result.add( prepareInputTextField( idAndSepratorObject, pimiFormItem, postFormObj ) );
                }
            }
        }

    }

    /**
     * Extract pim type string.
     *
     * @param jsonQueryResponseObjPim
     *         the json query response obj pim
     *
     * @return the string
     */
    private String extractPimType( JSONObject jsonQueryResponseObjPim ) {
        if ( jsonQueryResponseObjPim.has( "objects" ) ) {
            JSONArray objects = jsonQueryResponseObjPim.getJSONArray( "objects" );
            for ( int i = 0; i < objects.length(); i++ ) {
                JSONObject subObjects = objects.getJSONObject( i );
                if ( subObjects.has( "type" ) ) {
                    // JSONObject attrs = subObjects.getJSONObject( "attrs" );
                    // JSONObject displayConfigObj = attrs.getJSONObject( "displayConfig.files" );
                    // JSONArray filesArray = displayConfigObj.getJSONArray( "files" );
                    // JSONObject pimName = filesArray.getJSONObject( 0 );
                    return String.valueOf( subObjects.get( "type" ) );

                }
            }
        }
        return null;
    }

    /**
     * Prepare post drop down.
     *
     * @param name
     *         the name
     * @param loadCaseWizardEntity
     *         the load case wizard entity
     * @param options
     *         the options
     *
     * @return the select UI
     */
    private SelectFormItem preparePostDropDown( EntityManager entityManager, String name, LoadCaseWizardEntity loadCaseWizardEntity,
            List< SelectOptionsUI > options ) {
        if ( loadCaseWizardEntity != null && loadCaseWizardEntity.getPostSelectionId() != null ) {
            for ( UUID workflowId : selectionManager.getSelectedIdsListBySelectionId( entityManager,
                    loadCaseWizardEntity.getPostSelectionId().toString() ) ) {
                SelectOptionsUI project = new SelectOptionsUI();
                project.setId( workflowId.toString() );
                SuSEntity workflow = susDAO.getLatestObjectById( entityManager, WorkflowEntity.class, workflowId );
                project.setName( workflow.getName() );
                options.add( project );
            }
        }
        SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        item.setLabel( "Post Method" );
        item.setOptions( options );
        item.setName( name );
        item.setType( SELECT_WORKFLOW );
        item.setReadonly( false );
        item.setCanBeEmpty( true );
        return item;

    }

    /**
     * Sets the loadcase wiz DAO.
     *
     * @param loadcaseWizDAO
     *         the new loadcase wiz DAO
     */
    public void setLoadcaseWizDAO( LoadcaseWizDAO loadcaseWizDAO ) {
        this.loadcaseWizDAO = loadcaseWizDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getWorkflowRunFields( String userIdFromGeneralHeader, String workflowId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > formItems = new ArrayList<>();
            LatestWorkFlowDTO latestWorkFlowDTO = workflowManager.getNewWorkflowById( entityManager,
                    UUID.fromString( userIdFromGeneralHeader ), workflowId );
            final Map< String, Object > map = latestWorkFlowDTO.prepareDefinition();
            String json = null;
            if ( map != null ) {
                json = JsonUtils.toJson( map );
            }
            final WorkflowDefinitionDTO workflowDefinitionDTO = JsonUtils.jsonToObject( json, WorkflowDefinitionDTO.class );

            if ( null != workflowDefinitionDTO.getElements() && null != workflowDefinitionDTO.getElements().getNodes() ) {
                for ( WorkflowElement element : workflowDefinitionDTO.getElements().getNodes() ) {
                    if ( null != element.getData().getFields() ) {
                        for ( Field< ? > field : element.getData().getFields() ) {
                            if ( field.getType().contentEquals( "select" ) ) {
                                SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
                                item.setLabel( field.getLabel() );
                                item.setName( field.getName() );
                                item.setType( field.getType() );
                                List< SelectOptionsUI > options = new ArrayList<>();
                                for ( Object value : field.getOptions() ) {
                                    String realVal = "";
                                    try {
                                        realVal = value.toString();
                                    } catch ( Exception e ) {
                                        log.error( e.getMessage(), e );
                                    }
                                    SelectOptionsUI selectOptionsUI = new SelectOptionsUI();
                                    selectOptionsUI.setId( realVal );
                                    selectOptionsUI.setName( realVal );

                                    options.add( selectOptionsUI );
                                }
                                item.setOptions( options );
                                formItems.add( item );
                            } else {
                                UIFormItem formItem = GUIUtils.createFormItem();
                                formItem.setLabel( field.getLabel() );
                                formItem.setName( field.getName() );
                                formItem.setType( field.getType() );

                                formItems.add( formItem );
                            }
                        }
                    }
                }
            }

            return GUIUtils.createFormFromItems( formItems );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveObjectsOrder( String userIdFromGeneralHeader, String id, List< UUID > jsonToList ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            VariantWizardEntity wizEntity = wizDAO.findById( entityManager, UUID.fromString( id ) );
            if ( wizEntity.isFrozen() ) {
                throw new SusException( "Variant Frozen now" );
            }
            FiltersDTO filtersDTO = new FiltersDTO();
            List< Object > objects = new ArrayList<>( jsonToList );
            filtersDTO.setItems( objects );
            selectionManager.removeSelectionItemsInExistingSelection( entityManager, wizEntity.getObjectSelectionId().toString(),
                    filtersDTO );
            selectionManager.addSelectionItemsInExistingSelection( entityManager, wizEntity.getObjectSelectionId().toString(), filtersDTO );
            return true;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getSubmitTabUI( String userIdFromGeneralHeader, String id ) {
        return GUIUtils.listColumns( VariantSubmitDTO.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Object > getSubmitTabData( String userIdFromGeneralHeader, String id, FiltersDTO filterDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< Object > objectDTOList = new ArrayList<>();

            VariantWizardDTO runItem = getVariant( entityManager, userIdFromGeneralHeader, id );
            getSelectedLoadcaseForSubmitTab( objectDTOList, runItem, entityManager );

            if ( runItem.getReferenceId() != null ) {
                VariantWizardDTO refVariant = getReferenceVariant( entityManager, runItem.getReferenceId(), userIdFromGeneralHeader );
                if ( null != refVariant ) {
                    getSelectedLoadcaseForSubmitTab( objectDTOList, refVariant, entityManager );
                }
            }

            filterDTO.setTotalRecords( Long.valueOf( objectDTOList.size() ) );
            filterDTO.setFilteredRecords( Long.valueOf( objectDTOList.size() ) );

            return PaginationUtil.constructFilteredResponse( filterDTO, objectDTOList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the selected loadcase for submit tab.
     *
     * @param objectDTOList
     *         the object DTO list
     * @param runVariant
     *         the run item
     * @param entityManager
     *         the entity manager
     *
     * @return the selected loadcase for submit tab
     */
    private void getSelectedLoadcaseForSubmitTab( List< Object > objectDTOList, VariantWizardDTO runVariant, EntityManager entityManager ) {
        List< UUID > selectedLoadcaseIds = new ArrayList<>();
        if ( runVariant.getLoadcaseSelectionId() != null ) {
            selectedLoadcaseIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                    runVariant.getLoadcaseSelectionId().toString() );
        }
        if ( !selectedLoadcaseIds.isEmpty() ) {
            for ( UUID uuid : selectedLoadcaseIds ) {
                SuSEntity e = susDAO.getLatestNonDeletedObjectById( entityManager, uuid );
                VariantSubmitDTO submitItem = new VariantSubmitDTO();
                submitItem.setId( e.getComposedId().getId() );
                submitItem.setLoadcase( e.getName() );

                if ( null != runVariant.getLoadcaseSubmit() && runVariant.getLoadcaseSubmit()
                        .containsKey( e.getComposedId().getId().toString() ) ) {
                    Map< String, Integer > loadcaseSubmitValues = new HashMap<>();

                    loadcaseSubmitValues = ( Map< String, Integer > ) JsonUtils.jsonToMap(
                            JsonUtils.objectToJson( runVariant.getLoadcaseSubmit().get( e.getComposedId().getId().toString() ) ),
                            loadcaseSubmitValues );
                    if ( null != loadcaseSubmitValues ) {
                        if ( loadcaseSubmitValues.containsKey( "assemble" ) ) {
                            submitItem.setAssemble( loadcaseSubmitValues.get( "assemble" ) );
                        }
                        if ( loadcaseSubmitValues.containsKey( "solve" ) ) {
                            submitItem.setSolve( loadcaseSubmitValues.get( "solve" ) );
                        }
                        if ( loadcaseSubmitValues.containsKey( "post" ) ) {
                            submitItem.setPost( loadcaseSubmitValues.get( "post" ) );
                        }
                        if ( loadcaseSubmitValues.containsKey( "submit" ) ) {
                            submitItem.setSubmit( loadcaseSubmitValues.get( "submit" ) );
                        }
                    }

                }
                objectDTOList.add( submitItem );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Object > getCb2SubmitTabData( String userIdFromGeneralHeader, String id, FiltersDTO filterDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Cb2VariantWizardDTO runItem = getCb2Variant( entityManager, userIdFromGeneralHeader, id );
            List< UUID > selectedSenariosIds = new ArrayList<>();
            if ( runItem.getLoadcaseSelectionId() != null ) {
                selectedSenariosIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                        runItem.getLoadcaseSelectionId().toString() );
            }
            List< Object > objectDTOList = new ArrayList<>();
            if ( !selectedSenariosIds.isEmpty() ) {
                for ( UUID uuid : selectedSenariosIds ) {
                    // SuSEntity e = susDAO.getLatestNonDeletedObjectById( uuid );
                    BmwCaeBenchEntity bmwCaeBenchEntity = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager, uuid );
                    VariantSubmitDTO submitItem = new VariantSubmitDTO();
                    submitItem.setId( bmwCaeBenchEntity.getId() );
                    submitItem.setLoadcase( bmwCaeBenchEntity.getName() );
                    if ( null != runItem.getLoadcaseSubmit() && runItem.getLoadcaseSubmit()
                            .containsKey( bmwCaeBenchEntity.getId().toString() ) ) {
                        Map< String, Integer > loadcaseSubmitValues = new HashMap<>();
                        loadcaseSubmitValues = ( Map< String, Integer > ) JsonUtils.jsonToMap(
                                JsonUtils.objectToJson( runItem.getLoadcaseSubmit().get( bmwCaeBenchEntity.getId().toString() ) ),
                                loadcaseSubmitValues );
                        if ( null != loadcaseSubmitValues ) {
                            if ( loadcaseSubmitValues.containsKey( "assemble" ) ) {
                                submitItem.setAssemble( loadcaseSubmitValues.get( "assemble" ) );
                            }
                            if ( loadcaseSubmitValues.containsKey( "solve" ) ) {
                                submitItem.setSolve( loadcaseSubmitValues.get( "solve" ) );
                            }
                            if ( loadcaseSubmitValues.containsKey( "post" ) ) {
                                submitItem.setPost( loadcaseSubmitValues.get( "post" ) );
                            }
                            if ( loadcaseSubmitValues.containsKey( "submit" ) ) {
                                submitItem.setSubmit( loadcaseSubmitValues.get( "submit" ) );
                            }
                        }
                    }
                    objectDTOList.add( submitItem );
                }
            }
            filterDTO.setTotalRecords( Long.valueOf( selectedSenariosIds.size() ) );
            filterDTO.setFilteredRecords( Long.valueOf( selectedSenariosIds.size() ) );
            return PaginationUtil.constructFilteredResponse( filterDTO, objectDTOList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean changeObjectToLoadcaseRelation( String userIdFromGeneralHeader, String variantId, String objectId, UUID loadcaseId,
            Integer value ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            VariantWizardEntity wizEntity = wizDAO.findById( entityManager, UUID.fromString( variantId ) );
            if ( wizEntity.isFrozen() ) {
                throw new SusException( "Variant Frozen now" );
            }
            Map< String, Object > objectToLoadcaseRelations = new HashMap<>();
            if ( null != wizEntity.getObjectLoadcaseRelation() ) {
                objectToLoadcaseRelations = ( Map< String, Object > ) JsonUtils.jsonToMap( wizEntity.getObjectLoadcaseRelation(),
                        objectToLoadcaseRelations );
            }

            Map< String, Integer > loadcaseValues = new HashMap<>();

            if ( objectToLoadcaseRelations.containsKey( objectId ) ) {
                loadcaseValues = ( Map< String, Integer > ) JsonUtils.jsonToMap(
                        JsonUtils.objectToJson( objectToLoadcaseRelations.get( objectId ) ), loadcaseValues );
            }
            loadcaseValues.put( loadcaseId.toString(), value );
            objectToLoadcaseRelations.put( objectId, loadcaseValues );

            wizEntity.setObjectLoadcaseRelation( JsonUtils.objectToJson( objectToLoadcaseRelations ) );
            wizDAO.saveOrUpdate( entityManager, wizEntity );

            return true;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean changeLoadcaseSubmit( String userIdFromGeneralHeader, String variantId, String loadcaseId, String key, Integer value ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            VariantWizardEntity wizEntity = wizDAO.findById( entityManager, UUID.fromString( variantId ) );
            if ( wizEntity.isFrozen() ) {
                throw new SusException( "Variant Frozen now" );
            }
            Map< String, Object > loadcaseSubmit = new HashMap<>();
            if ( null != wizEntity.getLoadcaseSubmit() ) {
                loadcaseSubmit = ( Map< String, Object > ) JsonUtils.jsonToMap( wizEntity.getLoadcaseSubmit(), loadcaseSubmit );
            }

            Map< String, Integer > submitValues = new HashMap<>();

            if ( loadcaseSubmit.containsKey( loadcaseId ) ) {
                submitValues = ( Map< String, Integer > ) JsonUtils.jsonToMap( JsonUtils.objectToJson( loadcaseSubmit.get( loadcaseId ) ),
                        submitValues );
            }
            submitValues.put( key, value );
            loadcaseSubmit.put( loadcaseId, submitValues );

            wizEntity.setLoadcaseSubmit( JsonUtils.objectToJson( loadcaseSubmit ) );

            wizDAO.saveOrUpdate( entityManager, wizEntity );

            return true;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getCopyVairnatUI( String userIdFromGeneralHeader, String refId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var result = dataObjectManager.editDataObjectForm( entityManager, refId, userIdFromGeneralHeader );
            VariantWizardDTO variantWizardDTO = getVariant( entityManager, userIdFromGeneralHeader, refId );
            UIFormItem refItem = GUIUtils.createFormItem( "Reference", "referenceId", variantWizardDTO.getReferenceId() );
            refItem.setType( "object" );
            result.get( "default" ).add( refItem );
            SelectFormItem copyItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            copyItem.setLabel( "Copy Submodels" );
            copyItem.setName( "copy" );
            copyItem.setValue( variantWizardDTO.getCopy() );
            copyItem.setType( "select" );
            List< SelectOptionsUI > options = new ArrayList<>();
            options.add( new SelectOptionsUI( "yes", "Yes" ) );
            options.add( new SelectOptionsUI( "no", "No" ) );
            copyItem.setOptions( options );
            result.get( "default" ).add( copyItem );
            return result;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object createVariantCopy( String userIdFromGeneralHeader, String refId, VariantWizardDTO variantWizardDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity refVariantEntity = susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( refId ) );
            SuSEntity refVarientParentEntity = susDAO.getParents( entityManager, refVariantEntity ).get( 0 );
            Object createdVariant = dataObjectManager.createSuSObject( entityManager, userIdFromGeneralHeader,
                    refVarientParentEntity.getComposedId().getId().toString(), variantWizardDTO.getTypeId().toString(),
                    JsonUtils.toJson( variantWizardDTO ), Boolean.TRUE, null );

            VariantWizardDTO var = getVariant( entityManager, userIdFromGeneralHeader, refId );

            VariantWizardEntity entity = createVariantEntityFromVariantWizardDTO( entityManager, refId, var );
            entity.setId( ( ( VariantDTO ) createdVariant ).getId() );

            if ( entity.getObjectSelectionId() != null ) {
                SelectionResponseUI newObjectSelectionId = selectionManager.createSelection( entityManager, userIdFromGeneralHeader,
                        SelectionOrigins.VARIANT_COPY, createFiltersDTOfromUUIDs(
                                selectionManager.getSelectedIdsListBySelectionId( entityManager,
                                        entity.getObjectSelectionId().toString() ) ) );
                entity.setObjectSelectionId( UUID.fromString( newObjectSelectionId.getId() ) );
            }

            if ( entity.getLoadcaseSelectionId() != null ) {
                SelectionResponseUI newLoadcaseSelectionId = selectionManager.createSelection( entityManager, userIdFromGeneralHeader,
                        SelectionOrigins.VARIANT_COPY, createFiltersDTOfromUUIDs(
                                selectionManager.getSelectedIdsListBySelectionId( entityManager,
                                        entity.getLoadcaseSelectionId().toString() ) ) );
                entity.setLoadcaseSelectionId( UUID.fromString( newLoadcaseSelectionId.getId() ) );
            }
            wizDAO.saveOrUpdate( entityManager, entity );
            return createVaraintWizardDTOFromEntity( entityManager, entity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the dummy option form.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param solverType
     *         the solver type
     * @param variantReference
     *         the variant reference
     *
     * @return the dummy option form
     */
    private List< UIFormItem > getDummyOptionForm( EntityManager entityManager, String userId, UUID parentId, String solverType,
            String variantReference ) {
        List< UIFormItem > result = GUIUtils.prepareForm( true, new DummyVariantDTO() );
        UIFormItem nameItem = findItemByName( result, NAME );
        nameItem.setValue( prepareDummyName( entityManager, parentId ) );

        // prepare list if execution host property is true
        if ( PropertiesManager.isHostEnable() ) {
            List< SelectOptionsUI > executionHostList = new ArrayList<>();
            SelectOptionsUI defaultLocationItem = new SelectOptionsUI();
            defaultLocationItem.setId( ( LocationsEnum.DEFAULT_LOCATION.getId() ) );
            defaultLocationItem.setName( LocationsEnum.DEFAULT_LOCATION.getName() );
            executionHostList.add( defaultLocationItem );
            Hosts hosts = PropertiesManager.getHosts();
            if ( hosts != null && CollectionUtils.isNotEmpty( hosts.getExcutionHosts() ) ) {
                for ( ExecutionHosts executionHosts : hosts.getExcutionHosts() ) {
                    SelectOptionsUI hostItem = new SelectOptionsUI();
                    hostItem.setId( ( executionHosts.getId().toString() ) );
                    hostItem.setName( executionHosts.getName() );
                    executionHostList.add( hostItem );
                }
            }
            SelectFormItem typeItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            typeItem.setOptions( executionHostList );
            typeItem.setCanBeEmpty( false );
            typeItem.setDuplicate( false );
            typeItem.setLabel( "Job runs on" );
            typeItem.setName( "execution.host" );
            typeItem.setType( "select" );
            Map< String, Object > rules = new HashMap<>();
            Map< String, Object > message = new HashMap<>();

            rules.put( REQUIRED, true );
            message.put( REQUIRED, MUST_CHOSE_OPTION );
            typeItem.setRules( rules );
            typeItem.setMessages( message );

            result.add( typeItem );
        }

        result.add( getJobDescriptionField( "" ) );

        SelectFormItem typeItem = ( SelectFormItem ) findItemByName( result, "dummyType" );
        typeItem.setOptions( prepareDummyTypeOptionFromConfiguration( solverType ) );
        typeItem.setBindFrom( "/wizards/dummy/bind/solvertype/" + solverType + "/refvar/" + variantReference + "/dummytype/{__value__}" );

        SelectFormItem assemblyItem = ( SelectFormItem ) findItemByName( result, "assembly.id" );
        if ( assemblyItem != null ) {
            List< SelectOptionsUI > optionsAssembly = prepareDummyWorkFlows( entityManager, "assembly", solverType );
            assemblyItem.setOptions( optionsAssembly );
            assemblyItem.setType( SELECT_WORKFLOW );
            assemblyItem.setCanBeEmpty( true );
        }

        SelectFormItem solverItem = ( SelectFormItem ) findItemByName( result, "solver.id" );
        if ( assemblyItem != null ) {
            List< SelectOptionsUI > optionsSolver = prepareDummyWorkFlows( entityManager, "solver", solverType );
            solverItem.setOptions( optionsSolver );
            solverItem.setType( SELECT_WORKFLOW );
            solverItem.setCanBeEmpty( true );
        }

        SelectFormItem postItem = ( SelectFormItem ) findItemByName( result, "post.id" );
        if ( assemblyItem != null ) {
            List< SelectOptionsUI > optionsPost = prepareDummyWorkFlows( entityManager, "post", solverType );
            postItem.setOptions( optionsPost );
            postItem.setType( SELECT_WORKFLOW );
            postItem.setCanBeEmpty( true );
        }

        return result;
    }

    /**
     * Gets the dummy option form.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param variantId
     *         the variant id
     * @param dummyVariant
     *         the dummy variant
     * @param solverType
     *         the solver type
     * @param variantReference
     *         the variant reference
     *
     * @return the dummy option form
     */
    private List< UIFormItem > getDummyOptionForm( EntityManager entityManager, String userId, String variantId,
            DummyVariantDTO dummyVariant, String solverType, String variantReference ) {

        List< UIFormItem > result = GUIUtils.prepareForm( true, new DummyVariantDTO() );
        UIFormItem nameItem = findItemByName( result, NAME );
        nameItem.setValue( prepareDummyName( entityManager, UUID.fromString( variantId ) ) );

        // prepare list if execution host property is true
        SelectFormItem executionHostDropDownTypeItem = executionHostDropDown( dummyVariant.getExecutionHostId() );
        result.add( executionHostDropDownTypeItem );

        // typeItem
        SelectFormItem typeItem = ( SelectFormItem ) findItemByName( result, "dummyType" );
        typeItem.setOptions( prepareDummyTypeOptionFromConfiguration( solverType ) );
        typeItem.setBindFrom( "/wizards/dummy/bind/solvertype/" + solverType + "/refvar/" + variantReference + "/dummytype/{__value__}" );
        typeItem.setValue( dummyVariant.getDummyType() );

        // assemblyItem
        SelectFormItem assemblyItem = ( SelectFormItem ) findItemByName( result, "assembly.id" );
        List< SelectOptionsUI > optionsAssembly = prepareDummyWorkFlows( entityManager, "assembly", solverType );
        assemblyItem.setOptions( optionsAssembly );
        assemblyItem.setType( SELECT_WORKFLOW );
        assemblyItem.setCanBeEmpty( true );
        if ( dummyVariant.getAssembly() != null && isWfExistsAndNonDeleted( entityManager, dummyVariant.getAssembly().getId() ) ) {
            assemblyItem.setValue( dummyVariant.getAssembly().getId() );
        }

        // solverItem
        SelectFormItem solverItem = ( SelectFormItem ) findItemByName( result, "solver.id" );
        List< SelectOptionsUI > optionsSolver = prepareDummyWorkFlows( entityManager, "solver", solverType );
        solverItem.setOptions( optionsSolver );
        solverItem.setType( SELECT_WORKFLOW );
        solverItem.setCanBeEmpty( true );
        if ( dummyVariant.getSolver() != null && isWfExistsAndNonDeleted( entityManager, dummyVariant.getSolver().getId() ) ) {
            solverItem.setValue( dummyVariant.getSolver().getId() );
        }

        // postItem
        SelectFormItem postItem = ( SelectFormItem ) findItemByName( result, "post.id" );
        List< SelectOptionsUI > optionsPost = prepareDummyWorkFlows( entityManager, "post", solverType );
        postItem.setOptions( optionsPost );
        postItem.setType( SELECT_WORKFLOW );
        postItem.setCanBeEmpty( true );
        if ( dummyVariant.getPost() != null && isWfExistsAndNonDeleted( entityManager, dummyVariant.getPost().getId() ) ) {
            postItem.setValue( dummyVariant.getPost().getId() );
        }

        result.add( getJobDescriptionField( "" ) );

        String[] formUI = { "dummyType", "dummy_files_bmw", "additionalFiles", "cb2projtree", "additionalFilesSus", "additionalFilesLocal",
                "loadCases", "assembly.id", "solver.id", "post.id", "parentId", "typeId", NAME, "description" };
        List< UIFormItem > resulSequesnced = new ArrayList<>();
        for ( String uiOption : formUI ) {
            try {

                ListIterator< UIFormItem > iterator = result.listIterator();
                while ( iterator.hasNext() ) {
                    UIFormItem uiFormItem = iterator.next();
                    if ( uiFormItem != null && uiFormItem.getName() != null && uiOption.equalsIgnoreCase( uiFormItem.getName() ) ) {
                        resulSequesnced.add( uiFormItem );
                        iterator.remove();
                        break;
                    }
                }
            } catch ( Exception e ) {
                log.error( "Recent varient GUI rearengments failed", e );
            }

        }

        if ( !result.isEmpty() ) {
            for ( UIFormItem uiFormItem : result ) {
                if ( uiFormItem != null ) {
                    resulSequesnced.add( uiFormItem );
                }
            }
        }

        return resulSequesnced;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getDummyOptionFormForReferenceSelection( String userId, String variantId, String referenceId,
            DummyVariantDTO dummyVariant, String executedSolverType, String toExecuteSolverType, String variantReference ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > result = GUIUtils.prepareForm( true, new DummyVariantDTO() );
            UIFormItem nameItem = findItemByName( result, NAME );
            nameItem.setValue( prepareDummyName( entityManager, UUID.fromString( variantId ) ) );
            // prepare list if execution host property is true
            SelectFormItem executionHostDropDownTypeItem = executionHostDropDown( dummyVariant.getExecutionHostId() );
            result.add( executionHostDropDownTypeItem );
            // typeItem
            SelectFormItem typeItem = ( SelectFormItem ) findItemByName( result, "dummyType" );
            typeItem.setOptions( prepareDummyTypeOptionFromConfiguration( toExecuteSolverType ) );
            typeItem.setBindFrom( "/wizards/dummy/bind/solvertype/" + toExecuteSolverType + "/refvar/" + variantReference
                    + "/dummytype/{__value__}/reference/" + referenceId );
            typeItem.setValue( dummyVariant.getDummyType() );
            // assemblyItem
            SelectFormItem assemblyItem = ( SelectFormItem ) findItemByName( result, "assembly.id" );
            List< SelectOptionsUI > optionsAssembly = prepareDummyWorkFlows( entityManager, "assembly", toExecuteSolverType );
            assemblyItem.setOptions( optionsAssembly );
            assemblyItem.setType( SELECT_WORKFLOW );
            assemblyItem.setCanBeEmpty( true );
            if ( dummyVariant.getAssembly() != null && isWfExistsAndNonDeleted( entityManager, dummyVariant.getAssembly().getId() ) ) {
                assemblyItem.setValue( dummyVariant.getAssembly().getId() );
            }
            // solverItem
            SelectFormItem solverItem = ( SelectFormItem ) findItemByName( result, "solver.id" );
            List< SelectOptionsUI > optionsSolver = prepareDummyWorkFlows( entityManager, "solver", toExecuteSolverType );
            solverItem.setOptions( optionsSolver );
            solverItem.setType( SELECT_WORKFLOW );
            solverItem.setCanBeEmpty( true );
            if ( dummyVariant.getSolver() != null && isWfExistsAndNonDeleted( entityManager, dummyVariant.getSolver().getId() ) ) {
                solverItem.setValue( dummyVariant.getSolver().getId() );
            }
            // postItem
            SelectFormItem postItem = ( SelectFormItem ) findItemByName( result, "post.id" );
            List< SelectOptionsUI > optionsPost = prepareDummyWorkFlows( entityManager, "post", toExecuteSolverType );
            postItem.setOptions( optionsPost );
            postItem.setType( SELECT_WORKFLOW );
            postItem.setCanBeEmpty( true );
            if ( dummyVariant.getPost() != null && isWfExistsAndNonDeleted( entityManager, dummyVariant.getPost().getId() ) ) {
                postItem.setValue( dummyVariant.getPost().getId() );
            }
            result.add( getJobDescriptionField( "" ) );
            String[] formUI = { "dummyType", "dummy_files_bmw", "additionalFiles", "cb2projtree", "additionalFilesSus",
                    "additionalFilesLocal", "loadCases", "assembly.id", "solver.id", "post.id", "parentId", "typeId", NAME, "description" };
            List< UIFormItem > resulSequesnced = new ArrayList<>();
            for ( String uiOption : formUI ) {
                try {
                    ListIterator< UIFormItem > iterator = result.listIterator();
                    while ( iterator.hasNext() ) {
                        UIFormItem uiFormItem = iterator.next();
                        if ( uiFormItem != null && uiFormItem.getName() != null && uiOption.equalsIgnoreCase( uiFormItem.getName() ) ) {
                            resulSequesnced.add( uiFormItem );
                            iterator.remove();
                            break;
                        }
                    }
                } catch ( Exception e ) {
                    log.error( "Recent varient GUI rearengments failed", e );
                }
            }
            if ( !result.isEmpty() ) {
                for ( UIFormItem uiFormItem : result ) {
                    if ( uiFormItem != null ) {
                        resulSequesnced.add( uiFormItem );
                    }
                }
            }
            return GUIUtils.createFormFromItems( resulSequesnced );
        } finally {
            entityManager.close();
        }

    }

    /**
     * Prepare additional file map.
     *
     * @return the map
     */
    private List< Map< String, Object > > prepareAdditionalFileMap() {
        List< Map< String, Object > > retList = new ArrayList<>();
        List< JsonNode > dumyTypesList = getServerFileAttributes( "customAttributes" );
        if ( CollectionUtil.isNotEmpty( dumyTypesList ) ) {
            for ( JsonNode atrrib : dumyTypesList ) {
                Map< String, Object > map = new HashMap<>();
                map.put( LABEL, atrrib.get( LABEL ).asText() );
                map.put( NAME, atrrib.get( NAME ).asText() );
                map.put( "type", atrrib.get( "type" ).asText() );
                map.put( "canBeEmpty", atrrib.get( "canBeEmpty" ).asBoolean() );
                List< JsonNode > loadCases = JsonUtils.jsonToList( JsonUtils.toJsonString( atrrib.get( "options" ) ), JsonNode.class );
                List< String > optList = new ArrayList<>();
                for ( JsonNode loadcase : loadCases ) {
                    optList.add( loadcase.asText() );
                }
                map.put( "options", optList );
                retList.add( map );
            }
        }
        return retList;
    }

    /**
     * Gets the server file attributes.
     *
     * @param node
     *         the node
     *
     * @return the server file attributes
     */
    private List< JsonNode > getServerFileAttributes( String node ) {
        final String propFileName =
                ConstantsKaraf.KARAF_HOME + File.separator + ConstantsKaraf.KARAF_CONF + File.separator + "sus.serverfiles.json";
        JsonNode jsonJobSystemObject = null;
        try ( InputStream propFileNameStream = new FileInputStream( propFileName ) ) {
            jsonJobSystemObject = JsonUtils.convertInputStreamToJsonNode( propFileNameStream );
        } catch ( final IOException e ) {
            log.error( e.getLocalizedMessage(), e );
        }
        return JsonUtils.jsonToList( JsonUtils.toJsonString( jsonJobSystemObject.get( node ) ), JsonNode.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getDummyRefrenceOptionForm( String variantId ) {
        List< UIFormItem > parserForm = new ArrayList<>();
        parserForm.add( prepareObjectHiddenForm( variantId ) );
        parserForm.add( prepareSolverTypeOptions( variantId ) );
        return GUIUtils.createFormFromItems( parserForm );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getDummyRefrenceVariantForm( String solverType, String variantId ) {
        List< UIFormItem > parserForm = new ArrayList<>();
        parserForm.add( prepareVariantReferenceOptions( solverType, variantId ) );
        return GUIUtils.createFormFromItems( parserForm );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getDummyRefrenceVariantSelectionForm( String userId, String variantId, String solverType, String refVariant ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( refVariant.equalsIgnoreCase( "1" ) ) {
                return GUIUtils.createFormFromItems(
                        getDummyOptionForm( entityManager, userId, UUID.fromString( variantId ), solverType, refVariant ) );
            } else if ( refVariant.equalsIgnoreCase( "2" ) ) {
                VariantWizardEntity variant = wizDAO.getRecentVariantWizardByProperties( entityManager, userId, solverType );
                if ( variant == null ) {
                    return GUIUtils.createFormFromItems(
                            getDummyOptionForm( entityManager, userId, UUID.fromString( variantId ), solverType, refVariant ) );
                }
                DummyVariantDTO dummyVariant = JsonUtils.jsonToObject( variant.getFormJson(), DummyVariantDTO.class );
                return GUIUtils.createFormFromItems(
                        getDummyOptionForm( entityManager, userId, variantId, dummyVariant, solverType, refVariant ) );
            } else if ( refVariant.equalsIgnoreCase( "3" ) ) {
                return GUIUtils.createFormFromItems( prepareVariantSelectorForm( variantId, solverType ) );
            }
            return null;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare variant selector form.
     *
     * @param variantid
     *         the variantid
     * @param solverType
     *         the solver type
     *
     * @return the list
     */
    private List< UIFormItem > prepareVariantSelectorForm( String variantid, String solverType ) {
        List< UIFormItem > result = new ArrayList<>();

        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( "object.referenceselection" );
        selectFormItem.setLabel( MessageBundleFactory.getMessage( "4100143x4" ) );
        selectFormItem.setValue( null );
        selectFormItem.setType( "object" );

        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();

        rules.put( REQUIRED, true );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        selectFormItem.setRules( rules );
        selectFormItem.setMessages( message );
        selectFormItem.setMultiple( Boolean.FALSE );
        selectFormItem.setBindFrom( "/wizards/dummy/create/variant/" + variantid + "/solverType/" + solverType + "/selection/{__value__}" );
        result.add( selectFormItem );

        return result;
    }

    /**
     * Prepare variant reference options.
     *
     * @param variantId
     *         the id
     *
     * @return the UI form item
     */
    public UIFormItem prepareSolverTypeOptions( String variantId ) {
        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( "object.solvertype" );
        selectFormItem.setLabel( MessageBundleFactory.getMessage( "4100141x4" ) );
        selectFormItem.setValue( null );
        selectFormItem.setType( "select" );
        List< SelectOptionsUI > options = new ArrayList<>();
        options.add( new SelectOptionsUI( "1", "Abaqus" ) );
        options.add( new SelectOptionsUI( "2", "LsDyna" ) );
        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();
        rules.put( REQUIRED, true );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        selectFormItem.setRules( rules );
        selectFormItem.setMessages( message );
        selectFormItem.setMultiple( Boolean.FALSE );
        selectFormItem.setValue( "" );
        selectFormItem.setOptions( options );
        selectFormItem.setBindFrom( "/wizards/dummy/reference/variant/" + variantId + "/solvertype/{__value__}" );
        return selectFormItem;
    }

    /**
     * Prepare variant reference options.
     *
     * @param solverType
     *         the id
     * @param variantId
     *         the variant id
     *
     * @return the UI form item
     */
    public UIFormItem prepareVariantReferenceOptions( String solverType, String variantId ) {
        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( "object.referencevariant" );
        selectFormItem.setLabel( MessageBundleFactory.getMessage( "4100142x4" ) );
        selectFormItem.setValue( null );
        selectFormItem.setType( "select" );
        List< SelectOptionsUI > options = new ArrayList<>();
        options.add( new SelectOptionsUI( "1", "New Variant" ) );
        options.add( new SelectOptionsUI( "2", "Recent Variant" ) );
        options.add( new SelectOptionsUI( "3", "Reference Variant" ) );
        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();
        rules.put( REQUIRED, true );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        selectFormItem.setRules( rules );
        selectFormItem.setMessages( message );
        selectFormItem.setMultiple( Boolean.FALSE );
        selectFormItem.setValue( "1" );
        selectFormItem.setOptions( options );
        selectFormItem.setBindFrom( "/wizards/dummy/reference/variant/" + variantId + "/solvertype/" + solverType + "/option/{__value__}" );
        return selectFormItem;
    }

    /**
     * Prepare object hidden form.
     *
     * @param selection
     *         the selection
     *
     * @return the UI form item
     */
    private UIFormItem prepareObjectHiddenForm( String selection ) {

        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( "id" );
        selectFormItem.setLabel( "id" );
        selectFormItem.setValue( selection );
        selectFormItem.setType( "hidden" );

        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();

        rules.put( REQUIRED, false );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        selectFormItem.setRules( rules );
        selectFormItem.setMessages( message );
        selectFormItem.setMultiple( Boolean.FALSE );
        return selectFormItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getDummyFilesUI() {
        List< TableColumn > columnsList = new ArrayList<>();
        TableColumn tableColumn = new TableColumn();
        tableColumn.setOrderNum( 1 );
        tableColumn.setData( "dummyFiles" );
        tableColumn.setFilter( "text" );
        tableColumn.setName( "dummyFiles" );
        tableColumn.setSortable( true );
        tableColumn.setTitle( "Dummy Files" );
        tableColumn.setVisible( true );
        Renderer renderer = new Renderer();
        renderer.setData( "dummyFiles" );
        renderer.setManage( Boolean.TRUE );
        renderer.setType( "text" );
        renderer.setTooltip( "" );
        renderer.setUrl( "" );
        tableColumn.setRenderer( renderer );
        columnsList.add( tableColumn );
        return columnsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< LoadCaseDTO > getLoadCaseSelectedDummyTypeList( String userId, String dummyType, String solverType,
            FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< LoadCaseDTO > objectDTOList = new ArrayList<>();
            if ( filter.getColumns().get( 0 ).getFilters() != null || filter.getColumns().get( 1 ).getFilters() != null
                    || filter.getColumns().get( 2 ).getFilters() != null || filter.getColumns().get( 3 ).getFilters() != null
                    || filter.getColumns().get( 4 ).getFilters() != null || filter.getColumns().get( 5 ).getFilters() != null
                    || filter.getColumns().get( 6 ).getFilters() != null || filter.getColumns().get( 7 ).getFilters() != null ) {
                getLoadCases( entityManager, dummyType, filter, objectDTOList, solverType );
            } else {
                final Runnable task = () -> prepareDummyLoadCaseAsOptionThread( userId, dummyType, solverType );
                final Thread thread = new Thread( task );
                thread.start();
                getLoadCases( entityManager, dummyType, filter, objectDTOList, solverType );
            }
            return PaginationUtil.constructFilteredResponse( filter, objectDTOList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the load cases.
     *
     * @param entityManager
     *         the entity manager
     * @param dummyType
     *         the dummy type
     * @param filter
     *         the filter
     * @param objectDTOList
     *         the object DTO list
     * @param solverType
     *         the solver type
     */
    private void getLoadCases( EntityManager entityManager, String dummyType, FiltersDTO filter, List< LoadCaseDTO > objectDTOList,
            String solverType ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( "dummyTypeEntity.dummyTypeName", dummyType );
        properties.put( "dummyTypeEntity.solverType", solverType );
        List< LoadCaseEntity > loadCaseEntities = loadCaseDAO.getAllFilteredRecordsByProperties( entityManager, LoadCaseEntity.class,
                properties, filter );
        for ( LoadCaseEntity loadCaseEntity : loadCaseEntities ) {
            objectDTOList.add( loadcaseManager.createLoadcaseDTOFromEntity( loadCaseEntity ) );
        }
    }

    /**
     * Prepare bmw cae bench option from bmw cae bench 2.
     *
     * @param url
     *         the url
     * @param userId
     *         the user id
     * @param dumyTypes
     *         the dumy types
     * @param node
     *         the node
     * @param caeBenchType
     *         the cae bench type
     * @param filter
     *         the filter
     * @param userName
     *         the username
     *
     * @return the string
     */
    private String prepareBmwCaeBenchOptionFromBmwCaeBench2( String url, String userId, List< JsonNode > dumyTypes, String node,
            String caeBenchType, FiltersDTO filter, String userName ) {
        String payload = "";
        if ( dumyTypes != null ) {
            for ( JsonNode dumyType : dumyTypes ) {
                if ( dumyType.get( NAME ).asText().equals( node ) ) {
                    String query = JsonUtils.toJsonString( dumyType.get( "query" ) );
                    payload = "{\"query\":" + query + "}";
                    break;
                }
            }
        } else {
            payload = "{\"query\":\"" + url + "\"}";
        }
        try {
            return cb2DummyManager.getListOfCb2Objects( payload, userId, caeBenchType, filter, node, userName );
        } catch ( KeyManagementException | NoSuchAlgorithmException | KeyStoreException | IOException e ) {
            throw new SusException( "Unable to get response from CAE-Bench", e );
        }
    }

    /**
     * Find item by name.
     *
     * @param result
     *         the result
     * @param string
     *         the string
     *
     * @return the UI form item
     */
    private UIFormItem findItemByName( List< UIFormItem > result, String string ) {
        for ( UIFormItem uiFormItem : result ) {
            if ( uiFormItem.getName().equalsIgnoreCase( string ) ) {
                return uiFormItem;
            }
        }
        return null;
    }

    /**
     * Prepare dummy work flows.
     *
     * @param entityManager
     *         the entity manager
     * @param node
     *         the node
     * @param solverType
     *         the solver type
     *
     * @return the select UI
     */
    private List< SelectOptionsUI > prepareDummyWorkFlows( EntityManager entityManager, String node, String solverType ) {
        List< SelectOptionsUI > options = new ArrayList<>();
        List< JsonNode > workflows = getDummyNodes( solverType, node );
        for ( JsonNode workflowId : workflows ) {
            try {
                SuSEntity workflow = susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( workflowId.asText() ) );
                if ( workflow != null ) {
                    SelectOptionsUI selectOptionsUI = new SelectOptionsUI();
                    selectOptionsUI.setId( workflow.getComposedId().getId().toString() );
                    selectOptionsUI.setName( workflow.getName() );
                    options.add( selectOptionsUI );
                } else {
                    throw new SusException( workflowId.asText() + ". Please correct the configuration." );
                }
            } catch ( Exception e ) {
                log.debug( "conf id do not exists" + e.getMessage(), e );
            }
        }
        return options;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getDummyTypeForm( String userId, String solverType, String dummyType, String refVariant ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            VariantWizardEntity variant = wizDAO.getRecentVariantWizardByProperties( entityManager, userId, solverType );
            entityManager.close();
            DummyVariantDTO dummyVariant = null;
            if ( null != variant ) {
                dummyVariant = JsonUtils.jsonToObject( variant.getFormJson(), DummyVariantDTO.class );
            }
            List< UIFormItem > listDummyTypeForm = new ArrayList<>();
            Map< String, String > optional = new HashMap<>();
            optional.put( "dummy_type", dummyType );
            optional.put( "solver_type", solverType );
            TableFormItem dummyFile = ( TableFormItem ) GUIUtils.createFormItem( FormItemType.TABLE );
            dummyFile.setLabel( MessageBundleFactory.getMessage( "3000144x4" ) );
            dummyFile.setMultiple( true );
            dummyFile.setSortable( true );
            dummyFile.setSelectable( null );
            dummyFile.setType( "externalObject" );
            dummyFile.setName( "dummy_files_bmw" );
            dummyFile.setExternal( "bmw-dummyfiles" );
            Map< String, Object > rules = new HashMap<>();
            Map< String, Object > message = new HashMap<>();
            rules.put( "required", false );
            message.put( "required", "Must Chose Option" );
            dummyFile.setRules( rules );
            dummyFile.setMessages( message );
            dummyFile.setExtendTree( optional );
            if ( null != dummyVariant && dummyType.equals( dummyVariant.getDummyType() ) && !refVariant.equalsIgnoreCase( "1" ) ) {
                dummyFile.setValue( dummyVariant.getDummy_files_bmw() );
            }
            listDummyTypeForm.add( dummyFile );
            UIFormItem addAdditionalFiles = GUIUtils.createFormItem();
            addAdditionalFiles.setType( "fileExplorer" );
            addAdditionalFiles.setName( "additionalFiles" );
            addAdditionalFiles.setSortable( true );
            addAdditionalFiles.setCustomAttributes( prepareAdditionalFileMap() );
            addAdditionalFiles.setShow( "all" );
            addAdditionalFiles.setMultiple( true );
            addAdditionalFiles.setLabel( MessageBundleFactory.getMessage( "4100139x4" ) );
            if ( null != dummyVariant && dummyType.equals( dummyVariant.getDummyType() ) && !refVariant.equalsIgnoreCase( "1" ) ) {
                addAdditionalFiles.setValue( dummyVariant.getAdditionalFiles() );
            }
            listDummyTypeForm.add( addAdditionalFiles );
            listDummyTypeForm.add( prepareCB2Selector( solverType ) );
            UIFormItem addAdditionalFilesSus = GUIUtils.createFormItem();
            addAdditionalFilesSus.setType( "object" );
            addAdditionalFilesSus.setName( "additionalFilesSus" );
            addAdditionalFilesSus.setMultiple( true );
            addAdditionalFilesSus.setLabel( MessageBundleFactory.getMessage( "4100138x4" ) );
            if ( null != dummyVariant && dummyType.equals( dummyVariant.getDummyType() ) && !refVariant.equalsIgnoreCase( "1" ) ) {
                addAdditionalFilesSus.setValue( dummyVariant.getAdditionalFilesSus() );
            }
            listDummyTypeForm.add( addAdditionalFilesSus );
            UIFormItem addAdditionalFilesLocal = GUIUtils.createFormItem();
            addAdditionalFilesLocal.setType( "file-upload" );
            addAdditionalFilesLocal.setName( "additionalFilesLocal" );
            addAdditionalFilesLocal.setMultiple( true );
            addAdditionalFilesLocal.setLabel( MessageBundleFactory.getMessage( "4100137x4" ) );
            if ( null != dummyVariant && dummyType.equals( dummyVariant.getDummyType() ) && !refVariant.equalsIgnoreCase( "1" ) ) {
                addAdditionalFilesLocal.setValue( dummyVariant.getAdditionalFilesLocal() );
            }
            listDummyTypeForm.add( addAdditionalFilesLocal );
            TableFormItem loadCase = ( TableFormItem ) GUIUtils.createFormItem( FormItemType.TABLE );
            loadCase.setLabel( MessageBundleFactory.getMessage( "4000020x4" ) );
            loadCase.setMultiple( true );
            loadCase.setSelectable( null );
            loadCase.setType( "externalObject" );
            loadCase.setName( "loadCases" );
            loadCase.setExternal( "bmw-loadcases" );
            Map< String, Object > loadcaseRules = new HashMap<>();
            loadcaseRules.put( "required", true );
            loadCase.setRules( loadcaseRules );
            loadCase.setExtendTree( optional );
            loadCase.setMessages( message );
            if ( null != dummyVariant && dummyType.equals( dummyVariant.getDummyType() ) && !refVariant.equalsIgnoreCase( "1" ) ) {
                loadCase.setValue( dummyVariant.getLoadCases() );
            }
            listDummyTypeForm.add( loadCase );
            return GUIUtils.createFormFromItems( listDummyTypeForm );
        } finally {
            if ( entityManager.isOpen() ) {
                entityManager.close();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getDummyTypeFormForReferenceSelection( String userId, String solverType, String dummyType, String refVariant,
            String referenceId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            VariantWizardEntity variant = wizDAO.findById( entityManager, UUID.fromString( referenceId ) );
            entityManager.close();
            DummyVariantDTO dummyVariant = null;
            if ( null != variant ) {
                dummyVariant = JsonUtils.jsonToObject( variant.getFormJson(), DummyVariantDTO.class );
            }
            List< UIFormItem > listDummyTypeForm = new ArrayList<>();
            Map< String, String > optional = new HashMap<>();
            optional.put( "dummy_type", dummyType );
            optional.put( "solver_type", solverType );
            TableFormItem dummyFile = ( TableFormItem ) GUIUtils.createFormItem( FormItemType.TABLE );
            dummyFile.setLabel( MessageBundleFactory.getMessage( "3000144x4" ) );
            dummyFile.setMultiple( true );
            dummyFile.setSortable( true );
            dummyFile.setSelectable( null );
            dummyFile.setType( "externalObject" );
            dummyFile.setName( "dummy_files_bmw" );
            dummyFile.setExternal( "bmw-dummyfiles" );
            Map< String, Object > rules = new HashMap<>();
            Map< String, Object > message = new HashMap<>();
            rules.put( "required", false );
            message.put( "required", "Must Chose Option" );
            dummyFile.setRules( rules );
            dummyFile.setMessages( message );
            dummyFile.setExtendTree( optional );
            if ( null != dummyVariant && dummyType.equals( dummyVariant.getDummyType() ) && !refVariant.equalsIgnoreCase( "1" ) ) {
                dummyFile.setValue( dummyVariant.getDummy_files_bmw() );
            }
            listDummyTypeForm.add( dummyFile );
            UIFormItem addAdditionalFiles = GUIUtils.createFormItem();
            addAdditionalFiles.setType( "fileExplorer" );
            addAdditionalFiles.setName( "additionalFiles" );
            addAdditionalFiles.setSortable( true );
            addAdditionalFiles.setCustomAttributes( prepareAdditionalFileMap() );
            addAdditionalFiles.setShow( "all" );
            addAdditionalFiles.setMultiple( true );
            addAdditionalFiles.setLabel( MessageBundleFactory.getMessage( "4100139x4" ) );
            if ( null != dummyVariant && dummyType.equals( dummyVariant.getDummyType() ) && !refVariant.equalsIgnoreCase( "1" ) ) {
                addAdditionalFiles.setValue( dummyVariant.getAdditionalFiles() );
            }
            listDummyTypeForm.add( addAdditionalFiles );
            listDummyTypeForm.add( prepareCB2Selector( solverType ) );
            UIFormItem addAdditionalFilesSus = GUIUtils.createFormItem();
            addAdditionalFilesSus.setType( "object" );
            addAdditionalFilesSus.setName( "additionalFilesSus" );
            addAdditionalFilesSus.setMultiple( true );
            addAdditionalFilesSus.setLabel( MessageBundleFactory.getMessage( "4100138x4" ) );
            if ( null != dummyVariant && dummyType.equals( dummyVariant.getDummyType() ) && !refVariant.equalsIgnoreCase( "1" ) ) {
                addAdditionalFilesSus.setValue( dummyVariant.getAdditionalFilesSus() );
            }
            listDummyTypeForm.add( addAdditionalFilesSus );
            UIFormItem addAdditionalFilesLocal = GUIUtils.createFormItem();
            addAdditionalFilesLocal.setType( "file-upload" );
            addAdditionalFilesLocal.setName( "additionalFilesLocal" );
            addAdditionalFilesLocal.setMultiple( true );
            addAdditionalFilesLocal.setLabel( MessageBundleFactory.getMessage( "4100137x4" ) );
            if ( null != dummyVariant && dummyType.equals( dummyVariant.getDummyType() ) && !refVariant.equalsIgnoreCase( "1" ) ) {
                addAdditionalFilesLocal.setValue( dummyVariant.getAdditionalFilesLocal() );
            }
            listDummyTypeForm.add( addAdditionalFilesLocal );
            TableFormItem loadCase = ( TableFormItem ) GUIUtils.createFormItem( FormItemType.TABLE );
            loadCase.setLabel( MessageBundleFactory.getMessage( "4000020x4" ) );
            loadCase.setMultiple( true );
            loadCase.setSelectable( null );
            loadCase.setType( "externalObject" );
            loadCase.setName( "loadCases" );
            loadCase.setExternal( "bmw-loadcases" );
            Map< String, Object > loadcaseRules = new HashMap<>();
            loadcaseRules.put( "required", true );
            loadCase.setRules( loadcaseRules );
            loadCase.setExtendTree( optional );
            loadCase.setMessages( message );
            if ( null != dummyVariant && dummyType.equals( dummyVariant.getDummyType() ) && !refVariant.equalsIgnoreCase( "1" ) ) {
                loadCase.setValue( dummyVariant.getLoadCases() );
            }
            listDummyTypeForm.add( loadCase );
            return GUIUtils.createFormFromItems( listDummyTypeForm );
        } finally {
            if ( entityManager.isOpen() ) {
                entityManager.close();
            }
        }
    }

    /**
     * Prepare CB 2 selector.
     *
     * @param solverType
     *         the solver type
     *
     * @return the select UI table
     */
    private TableFormItem prepareCB2Selector( String solverType ) {
        TableFormItem loadCase = ( TableFormItem ) GUIUtils.createFormItem( FormItemType.TABLE );
        loadCase.setLabel( MessageBundleFactory.getMessage( "4100140x4" ) );
        loadCase.setMultiple( true );
        loadCase.setSortable( true );
        loadCase.setSelectable( null );
        loadCase.setType( "externalObject" );
        loadCase.setName( "cb2projtree" );
        loadCase.setExternal( "bmw-cb2-project-tree" );
        Map< String, Object > rules = new HashMap<>();
        rules.put( "required", false );
        loadCase.setRules( rules );
        Map< String, String > optional = new HashMap<>();
        optional.put( "solver_type", solverType );
        loadCase.setExtendTree( optional );
        Map< String, Object > message = new HashMap<>();
        message.put( "required", "Must Chose Option" );
        loadCase.setMessages( message );
        return loadCase;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getDummyLoadCaseForm( String userIdFromGeneralHeader, UUID loadCaseId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > listLoadCaseForm = new ArrayList<>();
            LoadCaseWizardEntity loadCaseWizardEntity = loadcaseWizDAO.findById( entityManager, loadCaseId );
            if ( loadCaseWizardEntity != null ) {
                SelectFormItem assemblyItem = prepareAssemblyDropDwon( entityManager, "assemblyWFId", loadCaseWizardEntity,
                        new ArrayList<>() );
                listLoadCaseForm.add( assemblyItem );
                SelectFormItem solverItem = prepareSolverDropDown( entityManager, "solveWFId", loadCaseWizardEntity, new ArrayList<>() );
                listLoadCaseForm.add( solverItem );
                SelectFormItem postItem = preparePostDropDown( entityManager, "postWFId", loadCaseWizardEntity, new ArrayList<>() );
                listLoadCaseForm.add( postItem );
            } else {
                throw new SusException( "Invalid load case. Please correct the configuration." );
            }
            return GUIUtils.createFormFromItems( listLoadCaseForm );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare dummy load case as option.
     *
     * @param userId
     *         the user id
     * @param selected
     *         the selected
     * @param solverType
     *         the solver type
     *
     * @return the list
     *
     * @implNote To be used in thread calls only
     */
    private synchronized List< LoadCaseDTO > prepareDummyLoadCaseAsOptionThread( String userId, String selected, String solverType ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return prepareDummyLoadCaseAsOption( entityManager, userId, selected, solverType );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare dummy load case as option.
     *
     * @param userId
     *         the user id
     * @param selected
     *         the selected
     * @param solverType
     *         the solver type
     *
     * @return the list
     */
    private synchronized List< LoadCaseDTO > prepareDummyLoadCaseAsOption( EntityManager entityManager, String userId, String selected,
            String solverType ) {
        List< JsonNode > dumyTypesList = getDummyNodes( solverType, "dummyTypes" );
        List< LoadCaseDTO > loadCaseDTOs = new ArrayList<>();
        DummyTypeEntity dummyTypeFromDB = dummyTypeDAO.getDummyTypeByName( entityManager, selected, solverType );
        if ( dummyTypeFromDB == null ) {
            dummyTypeFromDB = new DummyTypeEntity();
            dummyTypeFromDB.setId( UUID.randomUUID() );
            dummyTypeFromDB.setDummyTypeName( selected );
            dummyTypeFromDB.setSolverType( solverType );
            dummyTypeFromDB = dummyTypeDAO.save( entityManager, dummyTypeFromDB );
        }
        List< LoadCaseEntity > loadCaseEntityList = loadCaseDAO.getNonDeletedObjectListByProperty( entityManager, dummyTypeFromDB.getId() );

        HashMap< String, LoadCaseEntity > loadCaseNameFromDB = new HashMap<>();
        for ( LoadCaseEntity loadCaseEntity : loadCaseEntityList ) {
            loadCaseNameFromDB.put( loadCaseEntity.getName(), loadCaseEntity );
        }
        for ( JsonNode dumyType : dumyTypesList ) {
            if ( dumyType.get( NAME ).asText().equals( selected ) ) {
                List< JsonNode > loadCases = JsonUtils.jsonToList( JsonUtils.toJsonString( dumyType.get( "loadcases" ) ), JsonNode.class );
                for ( JsonNode loadcase : loadCases ) {
                    prepareLoadcaseDtoList( entityManager, userId, loadCaseDTOs, dummyTypeFromDB, loadCaseNameFromDB, loadcase );
                }
                removeDummyUnusedLoadcasesFromDatabase( entityManager, loadCases, loadCaseEntityList );
            }
        }
        return loadCaseDTOs;
    }

    /**
     * Removes unused loadcases from database.
     *
     * @param entityManager
     *         the entity manager
     * @param configurationLoadcaseNamesList
     *         the configurationLoadcaseList
     * @param databaseLoadcaseList
     *         the databaseLoadcaseList
     */
    private void removeDummyUnusedLoadcasesFromDatabase( EntityManager entityManager, List< JsonNode > configurationLoadcaseNamesList,
            List< LoadCaseEntity > databaseLoadcaseList ) {
        List< SuSEntity > unusedLoadcases = new ArrayList<>();

        for ( LoadCaseEntity loadcaseEntity : databaseLoadcaseList ) {
            boolean loadcaseExistInConfig = configurationLoadcaseNamesList.stream()
                    .anyMatch( configName -> loadcaseEntity.getName().equals( configName.asText() ) );

            if ( !loadcaseExistInConfig ) { // if dummy loadcase not in config, delete from db
                loadcaseEntity.setDelete( Boolean.TRUE );
                loadcaseEntity.setDeletedOn( new Date() );
                unusedLoadcases.add( loadcaseEntity );
            }
        }

        if ( !unusedLoadcases.isEmpty() ) {
            susDAO.saveOrUpdateBulk( entityManager, unusedLoadcases );
        }
    }

    /**
     * Prepare loadcase dto list.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param loadCaseDTOs
     *         the load case DT os
     * @param dummyTypeFromDB
     *         the dummy type from DB
     * @param loadCaseNameFromDB
     *         the load case name from DB
     * @param loadcase
     *         the loadcase
     */
    private void prepareLoadcaseDtoList( EntityManager entityManager, String userId, List< LoadCaseDTO > loadCaseDTOs,
            DummyTypeEntity dummyTypeFromDB, HashMap< String, LoadCaseEntity > loadCaseNameFromDB, JsonNode loadcase ) {
        if ( loadCaseNameFromDB.containsKey( loadcase.asText() ) ) {
            loadCaseDTOs.add( loadcaseManager.createLoadcaseDTOFromEntity( loadCaseNameFromDB.get( loadcase.asText() ) ) );
        } else {
            loadCaseDTOs.add( prapareLoadCaseForDummyVariant( entityManager, userId, loadcase, dummyTypeFromDB ) );
        }
    }

    /**
     * Prapare load case for dummy variant.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param loadcase
     *         the loadcase
     * @param dummyTypeFromDB
     *         the dummy type from DB
     *
     * @return the select object UI
     */
    private LoadCaseDTO prapareLoadCaseForDummyVariant( EntityManager entityManager, String userId, JsonNode loadcase,
            DummyTypeEntity dummyTypeFromDB ) {
        LoadCaseDTO loadCaseDTO = null;
        LoadcaseWizardDTO createloadcaseWizardDTO = new LoadcaseWizardDTO();
        createloadcaseWizardDTO.setId( UUID.randomUUID() );
        createloadcaseWizardDTO.setName( loadcase.asText() );
        createloadcaseWizardDTO.setDescription( "Load case created by configuration" );
        DummyTypeDTO dummyTypeDTO = new DummyTypeDTO();
        dummyTypeDTO.setId( dummyTypeFromDB.getId() );
        dummyTypeDTO.setDummyTypeName( dummyTypeFromDB.getDummyTypeName() );
        dummyTypeDTO.setSolverType( dummyTypeFromDB.getSolverType() );
        createloadcaseWizardDTO.setDummyTypeDTO( dummyTypeDTO );
        LoadcaseWizardDTO returnloadcaseWizardDTO = loadcaseManager.createLoadcase( entityManager, userId, createloadcaseWizardDTO,
                dummyTypeFromDB );
        if ( returnloadcaseWizardDTO != null ) {
            loadCaseDTO = loadcaseManager.getLoadcase( entityManager, userId, returnloadcaseWizardDTO.getId() );
        }
        return loadCaseDTO;
    }

    /**
     * Prepare dummy type.
     *
     * @param solverType
     *         the solver type
     *
     * @return the select UI
     */
    private List< SelectOptionsUI > prepareDummyTypeOptionFromConfiguration( String solverType ) {
        List< SelectOptionsUI > options = new ArrayList<>();
        List< JsonNode > dumyTypes = getDummyNodes( solverType, "dummyTypes" );

        for ( JsonNode dumyType : dumyTypes ) {
            String name = dumyType.get( NAME ).asText();
            SelectOptionsUI opt = new SelectOptionsUI();
            opt.setId( name );
            opt.setName( name );
            options.add( opt );
        }
        return options;
    }

    /**
     * Gets the dummy nodes.
     *
     * @param solverType
     *         the solver type
     * @param node
     *         the node
     *
     * @return the dummy nodes
     */
    private List< JsonNode > getDummyNodes( String solverType, String node ) {
        return JsonUtils.jsonToList( JsonUtils.toJsonString( getNodeFromDummyConfig( solverType, node ) ), JsonNode.class );
    }

    /**
     * Gets the dummy nodes.
     *
     * @param node
     *         the node
     * @param selectedNode
     *         the selected node
     * @param solverType
     *         the solver type
     *
     * @return the dummy nodes
     */
    private List< JsonNode > getDummyNodes( String node, String selectedNode, String solverType, String userUID ) {
        List< JsonNode > nodes = JsonUtils.jsonToList( JsonUtils.toJsonString( getNodeFromDummyConfig( solverType, node ) ),
                JsonNode.class );
        String query = getQueryFromSubModelTreeIfQueryNotAvailableInDummy( selectedNode, nodes, userUID );
        JsonNode jsonJobSystemObject = null;
        if ( StringUtils.isNotNullOrEmpty( query ) ) {
            jsonJobSystemObject = getDummyConfig();
            ( ( ObjectNode ) jsonJobSystemObject ).put( node, JsonUtils.convertObjectToJsonNode( nodes ) );
            final String propFileName =
                    ConstantsKaraf.KARAF_HOME + File.separator + ConstantsKaraf.KARAF_CONF + File.separator + "dummy_config.json";
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer( new DefaultPrettyPrinter() );
            try {
                writer.writeValue( new File( propFileName ), jsonJobSystemObject );
            } catch ( IOException e ) {
                throw new SusException( "Parsing issue from Submodel tree to Dummy Config.", e );
            }
        }
        return nodes;
    }

    /**
     * Gets senario tree node.
     *
     * @param selectedNode
     *         the selected node
     *
     * @return the senario tree node
     */
    private Cb2ScenerioTreeChildrenDTO getSenarioTreeNode( String selectedNode ) {

        try {
            File cb2TreeFile = new File( SCENARIO_JSON_FILE_PATH );
            FileUtils.setGlobalReadFilePermissions( cb2TreeFile );

            try ( InputStream is = new FileInputStream( cb2TreeFile ) ) {
                BmwCaeBenchScenerioTreeDTO cb2 = JsonUtils.jsonToObject( is, BmwCaeBenchScenerioTreeDTO.class );

                return findSenarioNodeById( selectedNode, cb2.getData() );

            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return null;

    }

    /**
     * Find senario node by id cb 2 scenerio tree children dto.
     *
     * @param selectedNode
     *         the selected node
     * @param data
     *         the data
     *
     * @return the cb 2 scenerio tree children dto
     */
    private Cb2ScenerioTreeChildrenDTO findSenarioNodeById( String selectedNode, List< Cb2ScenerioTreeChildrenDTO > data ) {

        Cb2ScenerioTreeChildrenDTO cb2Senarios = null;

        for ( Cb2ScenerioTreeChildrenDTO cb2ScenerioChild : data ) {
            if ( cb2ScenerioChild.getId().equalsIgnoreCase( selectedNode ) ) {
                /*
                 * cb2Senarios = new Cb2ScenerioTreeChildrenDTO( cb2ScenerioChild.getId(),
                 * cb2ScenerioChild.getTitle(), cb2ScenerioChild.getPath(),
                 * cb2ScenerioChild.getUrl(), cb2ScenerioChild.getChildren() );
                 */
                cb2Senarios = cb2ScenerioChild;
                break;
            } else if ( cb2ScenerioChild.getChildren() != null ) {
                cb2Senarios = findSenarioNodeById( selectedNode, cb2ScenerioChild.getChildren() );
                if ( cb2Senarios != null ) {
                    return cb2Senarios;
                }
            }
        }
        return cb2Senarios;
    }

    /**
     * Gets the query from sub model tree if query not availabe in dummy.
     *
     * @param selectedNode
     *         the selected node
     * @param nodes
     *         the nodes
     *
     * @return the query from sub model tree if query not available in dummy
     */
    private String getQueryFromSubModelTreeIfQueryNotAvailableInDummy( String selectedNode, List< JsonNode > nodes, String userUid ) {
        String query = null;
        for ( JsonNode dumyType : nodes ) {
            if ( dumyType.get( NAME ).asText().equals( selectedNode ) ) {
                query = dumyType.get( "query" ).asText();
                if ( StringUtils.isNullOrEmpty( query ) ) {
                    getQueryFromSubmodelTree(
                            readTreeJsonFileWithImpersonation( String.format( SUBMODELS_JSON_FILE_PATH, userUid ) ).getData().get( 0 ),
                            selectedNode );
                    query = this.url;
                    this.url = null;
                    query = query + ";subprojects{SP}.sdmObjects:" + BmwCaeBenchEnums.CB2_SUBMODEL.getValue();
                    ( ( ObjectNode ) dumyType ).put( "query", query );
                } else {
                    query = null;
                }
                break;
            }
        }
        return query;
    }

    /**
     * Gets the query from submodel tree.
     *
     * @param cb2TreeChildrenDTO
     *         the cb 2 tree children DTO
     * @param title
     *         the title
     */
    private void getQueryFromSubmodelTree( Cb2TreeChildrenDTO cb2TreeChildrenDTO, String title ) {
        if ( cb2TreeChildrenDTO.getTitle().equals( title ) ) {
            this.url = cb2TreeChildrenDTO.getUrl();
        } else {
            if ( CollectionUtil.isNotEmpty( cb2TreeChildrenDTO.getChildren() ) ) {
                for ( Cb2TreeChildrenDTO child : cb2TreeChildrenDTO.getChildren() ) {
                    getQueryFromSubmodelTree( child, title );
                }
            }
        }
    }

    /**
     * Gets node from sub model.
     *
     * @param userUid
     *         the user uid
     *
     * @return the node from sub model
     */
    private BmwCaeBenchTreeDTO getNodeFromSubModel( String userUid ) {
        return readTreeJsonFileWithImpersonation( String.format( SUBMODELS_JSON_FILE_PATH, userUid ) );
    }

    /**
     * Extract node url string.
     *
     * @param node
     *         the node
     * @param bmwCaeBenchTreeDTO
     *         the bmw cae bench tree dto
     *
     * @return the string
     */
    private String extractNodeUrl( String node, BmwCaeBenchTreeDTO bmwCaeBenchTreeDTO ) {
        Cb2TreeChildrenDTO rootNode = bmwCaeBenchTreeDTO.getData().get( 0 );
        getUrlById( rootNode, node );
        String urlLocal = this.url;
        this.url = null;
        return urlLocal;
    }

    /**
     * Extract node id by path string.
     *
     * @param path
     *         the path
     * @param bmwCaeBenchTreeDTO
     *         the bmw cae bench tree dto
     *
     * @return the string
     */
    private String extractNodeIdByPath( String path, BmwCaeBenchTreeDTO bmwCaeBenchTreeDTO ) {
        Cb2TreeChildrenDTO rootNode = bmwCaeBenchTreeDTO.getData().get( ConstantsInteger.INTEGER_VALUE_ZERO );
        getNodeIdByPath( rootNode, path );
        String nodeIdLocal = this.nodeId;
        this.nodeId = null;
        return nodeIdLocal;
    }

    /**
     * Is filter exists boolean.
     *
     * @param filter
     *         the filter
     *
     * @return the boolean
     */
    private boolean isFilterExists( FiltersDTO filter ) {
        if ( filter.getSearch() != null && !filter.getSearch().isEmpty() ) {
            return true;
        }
        if ( filter.getColumns() != null ) {
            for ( FilterColumn colm : filter.getColumns() ) {
                if ( colm.getFilters() != null && !colm.getFilters().isEmpty() ) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the url by id.
     *
     * @param cb2TreeChildrenDTO
     *         the cb 2 tree children DTO
     * @param id
     *         the id
     */
    private void getUrlById( Cb2TreeChildrenDTO cb2TreeChildrenDTO, String id ) {
        if ( cb2TreeChildrenDTO.getId().equals( id ) ) {
            url = cb2TreeChildrenDTO.getUrl();
        } else {
            if ( CollectionUtils.isNotEmpty( cb2TreeChildrenDTO.getChildren() ) ) {
                for ( Cb2TreeChildrenDTO cb2TreeChildrenDTO2 : cb2TreeChildrenDTO.getChildren() ) {
                    getUrlById( cb2TreeChildrenDTO2, id );
                }
            }
        }
    }

    /**
     * Gets node id by path.
     *
     * @param cb2TreeChildrenDTO
     *         the cb2 tree children dto
     * @param path
     *         the path
     */
    private void getNodeIdByPath( Cb2TreeChildrenDTO cb2TreeChildrenDTO, String path ) {
        if ( cb2TreeChildrenDTO.getPath().equals( path ) ) {
            nodeId = cb2TreeChildrenDTO.getId();
        } else {
            if ( CollectionUtils.isNotEmpty( cb2TreeChildrenDTO.getChildren() ) ) {
                cb2TreeChildrenDTO.getChildren().forEach( cb2TreeChildrenDTO2 -> getNodeIdByPath( cb2TreeChildrenDTO2, path ) );
            }
        }
    }

    /**
     * Gets the dummy config.
     *
     * @return the dummy config
     */
    private JsonNode getDummyConfig() {
        final String propFileName =
                ConstantsKaraf.KARAF_HOME + File.separator + ConstantsKaraf.KARAF_CONF + File.separator + "dummy_config.json";
        JsonNode jsonJobSystemObject = null;
        try ( InputStream dummyFileStream = new FileInputStream( propFileName ) ) {
            jsonJobSystemObject = JsonUtils.convertInputStreamToJsonNode( dummyFileStream );
        } catch ( final IOException e ) {
            log.error( e.getLocalizedMessage(), e );
        }
        return jsonJobSystemObject;
    }

    /**
     * Gets the node from dummy config.
     *
     * @param solverType
     *         the solver type
     * @param node
     *         the node
     *
     * @return the node from dummy config
     */
    private JsonNode getNodeFromDummyConfig( String solverType, String node ) {
        JsonNode jsonNode = getDummyConfig();
        if ( Integer.valueOf( solverType ) == ConstantsInteger.INTEGER_VALUE_ONE ) {
            return jsonNode.get( "Abaqus" ).get( node );
        } else {
            return jsonNode.get( "LsDyna" ).get( node );
        }

    }

    /**
     * Prepare dummy name.
     *
     * @param parentId
     *         the parent id
     *
     * @return the UI form item
     */
    private String prepareDummyName( EntityManager entityManager, UUID parentId ) {
        String name = "";
        VariantEntity variantEntity = ( VariantEntity ) susDAO.getLatestChildByParent( entityManager, VariantEntity.class, parentId );
        if ( variantEntity == null ) {
            name = "dummyVariant_1";
        } else {
            String[] nameWithVersion = variantEntity.getName().split( "_" );
            try {
                int newValue = Integer.parseInt( nameWithVersion[ 1 ] );
                name = variantEntity.getName().replace( nameWithVersion[ 1 ], newValue + 1 + "" );
            } catch ( Exception e ) {
                name = variantEntity.getName() + "_1";
            }
        }
        return name;
    }

    /**
     * Creates the filters DT ofrom UUI ds.
     *
     * @param selectedIdsListBySelectionId
     *         the selected ids list by selection id
     *
     * @return the filters DTO
     */
    private FiltersDTO createFiltersDTOfromUUIDs( List< UUID > selectedIdsListBySelectionId ) {
        FiltersDTO filtersDTO = new FiltersDTO();
        List< Object > objects = new ArrayList<>();
        for ( UUID uuid : selectedIdsListBySelectionId ) {
            objects.add( uuid );
        }
        filtersDTO.setItems( objects );
        return filtersDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object createAndRunDummyVariant( String token, String userIdFromGeneralHeader, String uid, String parentId, String json ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DummyVariantDTO dummyVariant = JsonUtils.jsonToObject( json, DummyVariantDTO.class );
            if ( dummyVariant.getAssembly() != null && dummyVariant.getAssembly().getId() != null && !dummyVariant.getAssembly().getId()
                    .isEmpty() ) {
                licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.ASSEMBLY.getKey() );
            }
            if ( dummyVariant.getPost() != null && dummyVariant.getPost().getId() != null && !dummyVariant.getPost().getId().isEmpty() ) {
                licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.POST.getKey() );
            }
            UserEntity userEntity = userCommonManager.getUserEntityById( entityManager, UUID.fromString( userIdFromGeneralHeader ) );
            if ( licenseManager.isFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.CB2_CONNECTOR.getKey(),
                    userEntity.getId().toString() ) ) {
                validateCB2session( userEntity, token );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( "1300033x4" ) );
            }

            String executionHostId = getExecutionHostId( json );
            String solverType = getSolverType( json );

            // 1. create the variant
            dummyVariant.setTypeId( UUID.fromString( ConstantsID.VARIANT_TYPE_ID ) );
            dummyVariant.setExecutionHostId( executionHostId );
            // set null pointers to empty selections
            if ( dummyVariant.getAssembly() == null || dummyVariant.getAssembly().getId().isEmpty() || dummyVariant.getAssembly().getId()
                    .equals( "" ) ) {
                dummyVariant.setAssembly( null );
            }
            if ( dummyVariant.getPost() == null || dummyVariant.getPost().getId().isEmpty() || dummyVariant.getPost().getId()
                    .equals( "" ) ) {
                dummyVariant.setPost( null );
            }
            if ( dummyVariant.getSolver() == null || dummyVariant.getSolver().getId().isEmpty() || dummyVariant.getSolver().getId()
                    .equals( "" ) ) {
                dummyVariant.setSolver( null );
            }

            json = JsonUtils.toJson( dummyVariant );

            // FE should not allow user to press submit button if Form is not loaded
            if ( dummyVariant.getAssembly() == null || dummyVariant.getDummyType() == null || dummyVariant.getLoadCases() == null ) {
                throw new SusException( "Can not submit Empty Dummy" );
            }

            VariantDTO createdVariant = ( VariantDTO ) dataObjectManager.createSuSObject( entityManager, userIdFromGeneralHeader, parentId,
                    ConstantsID.VARIANT_TYPE_ID, json, Boolean.TRUE, token );

            // 2. create VariantWizardEntity
            VariantWizardEntity entity = createNewVariantWizardEntity( entityManager, userIdFromGeneralHeader, createdVariant, dummyVariant,
                    solverType );
            wizDAO.saveOrUpdate( entityManager, entity );

            // 3. Run the Run Variant
            VariantWizardDTO variantWizardDTO = createVaraintWizardDTOFromEntity( entityManager, entity );
            variantWizardDTO.setAdditionalFiles( dummyVariant.getAdditionalFiles() );
            variantWizardDTO.setAdditionalFilesSus( dummyVariant.getAdditionalFilesSus() );
            variantWizardDTO.setAdditionalFilesLocal( dummyVariant.getAdditionalFilesLocal() );
            variantWizardDTO.setCb2projtree( dummyVariant.getCb2projtree() );
            if ( variantWizardDTO.getLoadcaseSelectionId() != null ) {
                Runnable runnable = () -> runLoadcases2Thread( variantWizardDTO, userIdFromGeneralHeader, token, uid,
                        variantWizardDTO.getId().toString(), executionHostId, true );
                Thread t = new Thread( runnable );
                t.start();
            }
            return createVaraintWizardDTOFromEntity( entityManager, entity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the solver type.
     *
     * @param json
     *         the json
     *
     * @return the solver type
     */
    private String getSolverType( String json ) {
        String solverType = null;
        Map< String, Object > data = new HashMap<>();
        data = ( Map< String, Object > ) JsonUtils.jsonToMap( json, data );
        try {
            for ( Map.Entry< String, Object > me : data.entrySet() ) {
                if ( me.getKey().equals( "object" ) ) {
                    solverType = JsonUtils.getValue( JsonUtils.toJson( me.getValue() ), "solvertype" );
                }
            }
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        }
        return solverType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object runVariant( String token, String userIdFromGeneralHeader, String uid, String id, VariantWizardDTO variantWizardDTO,
            String json ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.ASSEMBLY.getKey() );
            licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.POST.getKey() );
            VariantWizardEntity entity = createVariantEntityFromVariantWizardDTO( entityManager, id, variantWizardDTO );
            if ( entity.isFrozen() ) {
                throw new SusException( "Variant Frozen now" );
            } // entity.setFrozen( true );
            String executionHostId = getExecutionHostId( json );
            if ( executionHostId.isEmpty() ) {
                throw new SusException( "Please select job runs on location" );
            }
            variantWizardDTO.setExecutionHostId( executionHostId );
            if ( variantWizardDTO.getLoadcaseSelectionId() != null ) {
                runLoadcases2( entityManager, variantWizardDTO, userIdFromGeneralHeader, token, uid, id, executionHostId, false );
            } else {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.JOB_NOT_SUBMITTED_PARAM_NOT_SELECTED.getKey(), "Loadcase" ) );
            }
            wizDAO.saveOrUpdate( entityManager, entity );
            return null;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object runCb2Variant( String token, String userId, String uid, String variantId, Cb2VariantWizardDTO variantWizardDTO,
            String json ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.ASSEMBLY.getKey() );
            licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.POST.getKey() );
            VariantWizardEntity entity = createCb2VariantEntityFromVariantWizardDTO( entityManager, variantId, variantWizardDTO );
            if ( entity.isFrozen() ) {
                throw new SusException( "Cb2 Variant Frozen now" );
            } // entity.setFrozen( true );
            Cb2VariantWizardDTO runVariant = getCb2Variant( entityManager, userId, variantId );
            ValidateCb2RunVariant( entityManager, variantWizardDTO, runVariant );
            wizDAO.saveOrUpdate( entityManager, entity );
            // Thread Execution starts
            List< Cb2VariantWizardDTO > cb2VariantWizardDTOList = prepareAllCb2VariantsForLoadcases( entityManager, userId, uid,
                    runVariant );
            runCb2Variant( cb2VariantWizardDTOList );
            return variantWizardDTO;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Run cb 2 variant.
     *
     * @param cb2VariantWizardDTOList
     *         the cb 2 variant wizard dto list
     */
    private void runCb2Variant( List< Cb2VariantWizardDTO > cb2VariantWizardDTOList ) {

        for ( Cb2VariantWizardDTO cb2VariantWizardDTO : cb2VariantWizardDTOList ) {
            String payloadCb2VariantWizardDTO = JsonUtils.toJson( cb2VariantWizardDTO );
            String jsonFilePath =
                    PropertiesManager.getFeStaticPath() + File.separator + cb2VariantWizardDTO.getName() + "_" + new Date().getTime()
                            + "_.json";
            writeJsonToGivenPath( payloadCb2VariantWizardDTO, jsonFilePath );

            String[] command = { PropertiesManager.getPythonExecutionPathOnServer(), PropertiesManager.cb2RunVariant(), jsonFilePath,
                    PropertiesManager.getFeStaticPath() + File.separator + cb2VariantWizardDTO.getName() + "_" + new Date().getTime()
                            + "_.txt" };
            ProcessResult runProcessResult = LinuxUtils.runCommand( command, true, null );
            log.debug( runProcessResult.toString() );

        }
    }

    /**
     * Write json to given path.
     *
     * @param payloadCb2VariantWizardDTO
     *         the payload cb 2 variant wizard dto
     * @param jsonFilePath
     *         the json file path
     */
    private void writeJsonToGivenPath( String payloadCb2VariantWizardDTO, String jsonFilePath ) {
        File filesSave = new File( jsonFilePath );
        if ( filesSave.exists() ) {
            filesSave.delete();
        }
        try {
            FileWriter fw = new FileWriter( jsonFilePath );
            fw.write( payloadCb2VariantWizardDTO );
            fw.close();
        } catch ( Exception e ) {
            log.error( "Cb2 Run Variant Failed : " + e.getMessage(), e );
        }
    }

    /**
     * Prepare all cb 2 variants for loadcases list.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param uid
     *         the uid
     * @param runVariant
     *         the run variant
     *
     * @return the list
     */
    private List< Cb2VariantWizardDTO > prepareAllCb2VariantsForLoadcases( EntityManager entityManager, String userId, String uid,
            Cb2VariantWizardDTO runVariant ) {
        BmwCaeBenchProjectsDTO cb2GeneralTabProject = null;
        List< Cb2VariantWizardDTO > cb2VariantWizardDTOList = new ArrayList<>();
        VariantWizardEntity cb2RunVariantEntity = wizDAO.findById( entityManager, runVariant.getId() );
        // ****** Project extraction
        List< String > selectionProject = selectionManager.getSelectedIdsStringListBySelectionId( entityManager,
                runVariant.getGeneralProjectSelectionId() );
        cb2GeneralTabProject = prepareBmwCaeBenchProjectsByEntity(
                bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( selectionProject.get( 0 ) ) ) );
        // ****** Includes Selection Extraction
        List< BmwCaeBenchEntity > allOrderedIncludesList = selectionManager.getSortedSelection( entityManager,
                runVariant.getObjectSelectionId().toString() );
        Map< String, Object > relationMap = runVariant.getObjectLoadcaseRelation();
        // ****** Loadcase/Senarios Extraction
        List< String > scenariosLoadcaseIdsList = selectionManager.getSelectedIdsStringListBySelectionId( entityManager,
                runVariant.getLoadcaseSelectionId().toString() );
        for ( String senarioId : scenariosLoadcaseIdsList ) {
            // ***** populate Cb2VariantWizardDTO Distinct values Relating Loadcase/Senario
            Cb2VariantWizardDTO cb2VariantWizardDTO = prepareScenarioListToRun( entityManager, runVariant, relationMap, senarioId, uid,
                    allOrderedIncludesList, cb2RunVariantEntity, cb2GeneralTabProject );
            cb2VariantWizardDTOList.add( cb2VariantWizardDTO );
        }
        log.debug( JsonUtils.toJson( cb2VariantWizardDTOList ) );

        return cb2VariantWizardDTOList;
    }

    /**
     * Prepare scenario list to run cb 2 variant wizard dto.
     *
     * @param entityManager
     *         the entity manager
     * @param runVar
     *         the run var
     * @param relationMap
     *         the relation map
     * @param senarioId
     *         the senario id
     * @param userName
     *         the user name
     * @param allIncludesIdsList
     *         the all includes ids list
     * @param cb2RunVariantEntity
     *         the cb 2 run variant entity
     * @param cb2GeneralTabProject
     *         the cb 2 general tab project
     *
     * @return the cb 2 variant wizard dto
     */
    private Cb2VariantWizardDTO prepareScenarioListToRun( EntityManager entityManager, Cb2VariantWizardDTO runVar,
            Map< String, Object > relationMap, String senarioId, String userName, List< BmwCaeBenchEntity > allIncludesIdsList,
            VariantWizardEntity cb2RunVariantEntity, BmwCaeBenchProjectsDTO cb2GeneralTabProject ) {
        // initialize and prepare Cb2VariantWizardDTO to run cb2 variant
        Cb2VariantWizardDTO prepareCb2RunVariant = new Cb2VariantWizardDTO();
        getSenarioOrLoadcaseSubmitValue( prepareCb2RunVariant, senarioId, cb2RunVariantEntity );

        // get senario loadcase by id
        BmwCaeBenchEntity scenarioLoadcaseObj = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( senarioId ) );
        List< Object > includesOrderedList = prepareOrderedIncludesForLoadcase( entityManager, relationMap, senarioId, allIncludesIdsList );
        prepareCb2RunVariant.setDescription( runVar.getDescription() );
        prepareCb2RunVariant.setLoadcaseObject(
                prepareBmwAllCustomDtoByEntity( BmwCaeBenchEnums.CB2_SCENARIO.getValue(), scenarioLoadcaseObj ) );
        prepareCb2RunVariant.setName( runVar.getName() );
        // includeIdList includes added
        prepareCb2RunVariant.setIncludesList( includesOrderedList );
        // assemble form data added
        prepareCb2RunVariant.setAssemble( Arrays.asList( extractVariantFormFormByLoadcaseId( runVar.getAssemble(), senarioId ) ) );
        // solver form data added
        prepareCb2RunVariant.setSolve( Arrays.asList( extractVariantFormFormByLoadcaseId( runVar.getSolve(), senarioId ) ) );
        prepareCb2RunVariant.setPost( Arrays.asList( extractVariantFormFormByLoadcaseId( runVar.getPost(), senarioId ) ) );
        // set general tab data
        prepareCb2RunVariant.setGeneralDerivedFrom( runVar.getGeneralDerivedFrom() );
        if ( runVar.getGeneralDerivedFrom() != null && !runVar.getGeneralDerivedFrom().isEmpty() ) {
            List< String > selectionGeneralDerivedFrom = selectionManager.getSelectedIdsStringListBySelectionId( entityManager,
                    runVar.getGeneralDerivedFrom() );
            if ( selectionGeneralDerivedFrom != null && !selectionGeneralDerivedFrom.isEmpty() ) {
                prepareCb2RunVariant.setGeneralDerivedFromObject( prepareBmwAllCustomDtoByEntity( BmwCaeBenchEnums.CB2_VARIANT.getValue(),
                        bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager,
                                UUID.fromString( selectionGeneralDerivedFrom.get( 0 ) ) ) ) );
            }
        }
        prepareCb2RunVariant.setGeneralItem( runVar.getGeneralItem() );
        prepareCb2RunVariant.setGeneralProjectPhase( runVar.getGeneralProjectPhase() );
        prepareCb2RunVariant.setGeneralProjectSelectionId( runVar.getGeneralProjectSelectionId() );
        prepareCb2RunVariant.setGeneralDisciplineContext( runVar.getGeneralDisciplineContext() );
        prepareCb2RunVariant.setGeneralProjectSelectionObject( cb2GeneralTabProject );
        prepareCb2RunVariant.setGeneralSimulationGeneratorSettings( runVar.getGeneralSimulationGeneratorSettings() );
        prepareCb2RunVariant.setGeneralVariantDefination( runVar.getGeneralVariantDefination() );
        prepareCb2RunVariant.setGeneralVariantOverview( runVar.getGeneralVariantOverview() );
        prepareCb2RunVariant.setGeneralVariantType( runVar.getGeneralVariantType() );
        String projectPath = cb2GeneralTabProject.getName();
        prepareCb2RunVariant.setAssembleParams( cb2DummyManager.getCb2RunVariantAssembleAppParam( userName, projectPath ) );
        prepareCb2RunVariant.setSolverParams( cb2DummyManager.getCb2RunVariantSolveAppParam( userName, projectPath ) );
        prepareCb2RunVariant.setPostProcessingParams( cb2DummyManager.getCb2RunVariantPostProcessingAppParam( userName, projectPath ) );
        prepareCb2RunVariant.setSimulationDefination( null );
        prepareCb2RunVariant.setHpc( "abaqusexplicit.cb2test" );
        prepareCb2RunVariant.setJobDir(
                PropertiesManager.getStagingPath() + File.separator + userName + File.separator + "cb2RunTest" + File.separator );
        return prepareCb2RunVariant;
    }

    /**
     * Prepare ordered includes for loadcase list.
     *
     * @param entityManager
     *         the entity manager
     * @param relationMap
     *         the relation map
     * @param senarioId
     *         the senario id
     * @param allIncludesIdsList
     *         the all includes ids list
     *
     * @return the list
     */
    private List< Object > prepareOrderedIncludesForLoadcase( EntityManager entityManager, Map< String, Object > relationMap,
            String senarioId, List< BmwCaeBenchEntity > allIncludesIdsList ) {
        List< String > unOrderedIdList = new ArrayList<>();
        // prepare selected cb2 object ids
        for ( Entry< String, Object > mapRelation : relationMap.entrySet() ) {
            Map< String, Object > innerRelationMap = JsonUtils.convertStringToMapGenericValue( JsonUtils.toJson( mapRelation.getValue() ) );
            for ( Entry< String, Object > innerRelation : innerRelationMap.entrySet() ) {
                innerRelation.getKey();
                innerRelation.getValue();
                if ( innerRelation.getKey().contains( senarioId ) && innerRelation.getValue().toString().equalsIgnoreCase( "1" ) ) {
                    unOrderedIdList.add( mapRelation.getKey() );
                }
            }
        }

        List< Object > orderedSelectedIncludesList = new ArrayList<>();
        for ( BmwCaeBenchEntity orderedObj : allIncludesIdsList ) {
            for ( String unOrderedId : unOrderedIdList ) {
                if ( unOrderedId.equalsIgnoreCase( orderedObj.getId().toString() ) ) {
                    BmwCaeBenchEntity includesEntity = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager, orderedObj.getId() );
                    orderedSelectedIncludesList.add(
                            prepareBmwAllCustomDtoByEntity( BmwCaeBenchEnums.CB2_SUBMODEL.getValue(), includesEntity ) );
                }
            }
        }
        return orderedSelectedIncludesList;
    }

    /**
     * Gets senario or loadcase submit value.
     *
     * @param prepareCb2RunVariant
     *         the prepare cb 2 run variant
     * @param loadcaseId
     *         the loadcase id
     * @param cb2VariantEntity
     *         the cb 2 variant entity
     */
    public void getSenarioOrLoadcaseSubmitValue( Cb2VariantWizardDTO prepareCb2RunVariant, String loadcaseId,
            VariantWizardEntity cb2VariantEntity ) {
        // ***** add is submit || is post | is assemble |is solve **** //
        Map< String, Object > loadcaseSubmit = new HashMap<>();
        if ( null != cb2VariantEntity.getLoadcaseSubmit() ) {
            loadcaseSubmit = ( Map< String, Object > ) JsonUtils.jsonToMap( cb2VariantEntity.getLoadcaseSubmit(), loadcaseSubmit );
            for ( Entry< String, Object > loadcaseSubmitEntry : loadcaseSubmit.entrySet() ) {
                if ( loadcaseSubmitEntry.getKey().contains( loadcaseId ) ) {
                    Map< String, String > innerSubmit = JsonUtils.convertStringToMap( JsonUtils.toJson( loadcaseSubmitEntry.getValue() ) );
                    prepareCb2RunVariant.setAssembleIs( String.valueOf( innerSubmit.get( "assemble" ) ).equalsIgnoreCase( "1" ) );
                    prepareCb2RunVariant.setSolveIs( String.valueOf( innerSubmit.get( "solve" ) ).equalsIgnoreCase( "1" ) );
                    prepareCb2RunVariant.setPostIs( String.valueOf( innerSubmit.get( "post" ) ).equalsIgnoreCase( "1" ) );
                    prepareCb2RunVariant.setSubmitIs( String.valueOf( innerSubmit.get( "submit" ) ).equalsIgnoreCase( "1" ) );
                    break;
                }
            }
        }
    }

    /**
     * Validate cb 2 run variant.
     *
     * @param entityManager
     *         the entity manager
     * @param variantWizardDTO
     *         the variant wizard dto
     * @param runVariant
     *         the run variant
     */
    private void ValidateCb2RunVariant( EntityManager entityManager, Cb2VariantWizardDTO variantWizardDTO,
            Cb2VariantWizardDTO runVariant ) {
        if ( runVariant.getObjectSelectionId() == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.JOB_NOT_SUBMITTED_PARAM_NOT_SELECTED.getKey(), "Includes" ) );
        } else {
            List< String > objectSelectionId = selectionManager.getSelectedIdsStringListBySelectionId( entityManager,
                    runVariant.getObjectSelectionId().toString() );
            if ( objectSelectionId.size() < 1 ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.JOB_NOT_SUBMITTED_PARAM_NOT_SELECTED.getKey(), "Includes" ) );
            }
        }
        if ( runVariant.getGeneralProjectSelectionId() == null || runVariant.getGeneralProjectSelectionId().isEmpty() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.JOB_NOT_SUBMITTED_PARAM_NOT_SELECTED.getKey(), "Project" ) );
        } else {
            List< String > projectSelectionId = selectionManager.getSelectedIdsStringListBySelectionId( entityManager,
                    runVariant.getGeneralProjectSelectionId() );
            if ( projectSelectionId.size() < 1 ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.JOB_NOT_SUBMITTED_PARAM_NOT_SELECTED.getKey(), "Project" ) );
            }
        }

        if ( variantWizardDTO.getLoadcaseSelectionId() == null ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.JOB_NOT_SUBMITTED_PARAM_NOT_SELECTED.getKey(), "Senarios/Loadcase" ) );
        } else {
            List< String > scenariosLoadcaseIdsList = selectionManager.getSelectedIdsStringListBySelectionId( entityManager,
                    runVariant.getLoadcaseSelectionId().toString() );
            if ( scenariosLoadcaseIdsList.size() < 1 ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.JOB_NOT_SUBMITTED_PARAM_NOT_SELECTED.getKey(), "Senarios/Loadcase" ) );
            }
        }
    }

    /**
     * Gets the execution host id.
     *
     * @param json
     *         the json
     *
     * @return the execution host id
     */
    private String getExecutionHostId( String json ) {
        String executionHostId = null;
        Map< String, Object > data = new HashMap<>();
        data = ( Map< String, Object > ) JsonUtils.jsonToMap( json, data );
        try {
            for ( Map.Entry< String, Object > me : data.entrySet() ) {
                if ( me.getKey().equals( EXECUTION ) ) {
                    executionHostId = JsonUtils.getValue( JsonUtils.toJson( me.getValue() ), HOST );
                }
            }
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        }
        return executionHostId;
    }

    /**
     * Creates the new variant wizard entity.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param createdVariant
     *         the created variant
     * @param dummyVariant
     *         the dummy variant
     * @param solverType
     *         the solver type
     *
     * @return the variant wizard entity
     */
    private VariantWizardEntity createNewVariantWizardEntity( EntityManager entityManager, String userIdFromGeneralHeader,
            VariantDTO createdVariant, DummyVariantDTO dummyVariant, String solverType ) {
        VariantWizardEntity entity = new VariantWizardEntity();
        entity.setId( createdVariant.getId() );
        entity.setSolverType( solverType );

        List< LoadcaseWFModel > assemble = new ArrayList<>();
        List< LoadcaseWFModel > post = new ArrayList<>();
        List< LoadcaseWFModel > solve = new ArrayList<>();

        populateLoadcaseWFModelListWithDummy( entityManager, assemble, post, solve, dummyVariant );

        entity.setFormJson( JsonUtils.toJson( dummyVariant ) );
        entity.setUserId( userIdFromGeneralHeader );
        entity.setCreatedOn( new Date() );
        entity.setJobDescription( dummyVariant.getJobDescription() );

        if ( !assemble.isEmpty() ) {
            entity.setAssemble( JsonUtils.toJson( assemble ) );
        }

        if ( !post.isEmpty() ) {
            entity.setPost( JsonUtils.toJson( post ) );
        }

        if ( !solve.isEmpty() ) {
            entity.setSolve( JsonUtils.toJson( solve ) );
        }

        List< UUID > selectionIdsLoadcases = selectionManager.getSelectedIdsListBySelectionId( entityManager, dummyVariant.getLoadCases() );
        List< String > selectionIdsDummyFiles = new ArrayList<>();
        if ( null != dummyVariant.getDummy_files_bmw() && !dummyVariant.getDummy_files_bmw().isEmpty() ) {
            selectionIdsDummyFiles = selectionManager.getSelectedIdsStringListBySelectionId( entityManager,
                    dummyVariant.getDummy_files_bmw() );
        }

        if ( dummyVariant.getDummy_files_bmw() != null && !dummyVariant.getDummy_files_bmw().isEmpty() ) {
            entity.setObjectSelectionId( UUID.fromString( dummyVariant.getDummy_files_bmw() ) );
        }

        if ( dummyVariant.getLoadCases() != null ) {

            entity.setLoadcaseSelectionId( UUID.fromString( dummyVariant.getLoadCases() ) );
        }
        Map< String, Object > loadcaseSubmit = new HashMap<>();
        for ( UUID loadcaseId : selectionIdsLoadcases ) {
            Map< String, Integer > submitValues = new HashMap<>();
            submitValues.put( "assemble", 1 );
            submitValues.put( "solve", 1 );
            submitValues.put( "post", 1 );
            submitValues.put( "submit", 1 );
            loadcaseSubmit.put( loadcaseId.toString(), submitValues );
        }
        entity.setLoadcaseSubmit( JsonUtils.objectToJson( loadcaseSubmit ) );

        Map< String, Object > objectMap = new HashMap<>();
        for ( String objectId : selectionIdsDummyFiles ) {
            Map< String, Integer > lcMap = new HashMap<>();
            for ( UUID loadcaseId : selectionIdsLoadcases ) {
                lcMap.put( loadcaseId.toString(), 1 );
            }
            objectMap.put( objectId, lcMap );
        }
        entity.setObjectLoadcaseRelation( JsonUtils.toJson( objectMap ) );
        entity.setFrozen( true );
        return entity;
    }

    /**
     * Populate assemble, post and solve list with dummyVariant.
     *
     * @param entityManager
     *         the entity manager
     * @param assemble
     *         the assemble list
     * @param post
     *         the post list
     * @param solve
     *         the solve list
     * @param dummyVariant
     *         the dummy variant
     */
    private void populateLoadcaseWFModelListWithDummy( EntityManager entityManager, List< LoadcaseWFModel > assemble,
            List< LoadcaseWFModel > post, List< LoadcaseWFModel > solve, DummyVariantDTO dummyVariant ) {
        if ( dummyVariant.getLoadCases() != null ) {
            List< UUID > selectionIdsLoadcases = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                    dummyVariant.getLoadCases() );
            for ( int i = 0; i < selectionIdsLoadcases.size(); i++ ) {

                if ( dummyVariant.getAssembly() != null ) {
                    assemble.add( dummyVariant.getAssembly() );
                }

                if ( dummyVariant.getPost() != null ) {
                    post.add( dummyVariant.getPost() );
                }

                if ( dummyVariant.getSolver() != null ) {
                    solve.add( dummyVariant.getSolver() );
                }
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< BmwCaeBenchDTO > getBmwCaeBenchObjectSelection( String selectionId, String filterJson, String externalFlag ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            List< SelectionItemEntity > selectedUserIds = selectionManager.getUserSelectionsBySelectionIds( entityManager, selectionId,
                    filter );
            List< Object > notFoundObjects = new ArrayList<>();
            List< BmwCaeBenchDTO > bmwList = new ArrayList<>();
            if ( CollectionUtil.isNotEmpty( selectedUserIds ) ) {
                for ( SelectionItemEntity user : selectedUserIds ) {
                    BmwCaeBenchEntity entityBmw = bmwCaeBenchDAO.getUniqueObjectByProperty( entityManager, BmwCaeBenchEntity.class, "id",
                            UUID.fromString( user.getItem() ) );
                    if ( null != entityBmw ) {
                        bmwList.add( prepareBmwAllCustomDtoByEntity( externalFlag, entityBmw ) );
                    } else {
                        notFoundObjects.add( user.getItem() );
                    }
                }
            }

            FiltersDTO removeId = new FiltersDTO();
            removeId.setItems( notFoundObjects );
            selectionManager.removeSelectionItemsInExistingSelection( entityManager, selectionId, removeId );
            return PaginationUtil.constructFilteredResponse( filter, bmwList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< BmwCaeBenchDTO > getBmwCaeBenchObjetBySelection( String selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getBmwCaeBenchObjetBySelection( entityManager, selectionId );
        } finally {
            entityManager.close();
        }
    }

    private List< BmwCaeBenchDTO > getBmwCaeBenchObjetBySelection( EntityManager entityManager, String selectionId ) {
        List< UUID > selectionList = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );

        List< BmwCaeBenchDTO > bmwList = new ArrayList<>();
        if ( selectionList != null && CollectionUtil.isNotEmpty( selectionList ) ) {
            for ( UUID objId : selectionList ) {
                BmwCaeBenchEntity entityBmw = bmwCaeBenchDAO.getUniqueObjectByProperty( entityManager, BmwCaeBenchEntity.class, "id",
                        objId );
                if ( null != entityBmw ) {
                    bmwList.add( prepareBmwAllCustomDtoByEntity( BmwCaeBenchEnums.CB2_VARIANT.getValue(), entityBmw ) );
                }
            }
        }

        return bmwList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< ? > getBmwCaeBenchObjectSelectionWithAttributes( String selectionId, String filterJson, String externalFlag ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            SelectionEntity entitySelect = selectionManager.getSelectionDAO()
                    .getLatestObjectById( entityManager, SelectionEntity.class, UUID.fromString( selectionId ) );
            List< WorkFlowAdditionalAttributeDTO > listAttrib = null;
            if ( null != entitySelect.getAdditionalAttributesJson() ) {
                try {
                    listAttrib = JsonUtils.jsonToList( entitySelect.getAdditionalAttributesJson(), WorkFlowAdditionalAttributeDTO.class );
                } catch ( Exception e ) {
                    throw new SusException( "Additional attribute load json failed" + e.getMessage(), e );
                }
            }
            Set< SelectionItemEntity > selectedEntityIds = entitySelect.getItems();

            List< Object > notFoundObjects = new ArrayList<>();
            List< Object > bmwList = new ArrayList<>();
            if ( CollectionUtil.isNotEmpty( selectedEntityIds ) ) {
                for ( SelectionItemEntity selectionItemEntity : selectedEntityIds ) {

                    List< WorkFlowAdditionalAttributeDTO > updateAttributeMap = new ArrayList<>();

                    BmwCaeBenchEntity entityBmw = bmwCaeBenchDAO.getUniqueObjectByProperty( entityManager, BmwCaeBenchEntity.class, "id",
                            UUID.fromString( selectionItemEntity.getItem() ) );
                    if ( null != entityBmw ) {
                        BmwCaeBenchDTO bmwCaeDTO = prepareBmwAllCustomDtoByEntity( externalFlag, entityBmw );
                        Map< String, Object > map = new HashMap<>();
                        map = ( Map< String, Object > ) JsonUtils.jsonToMap( JsonUtils.toJson( bmwCaeDTO ), map );

                        if ( listAttrib != null ) {
                            List< WorkFlowAdditionalAttributeDTO > selectionItemAttributeList = getSelectionItemsAdditionalAttributes(
                                    selectionItemEntity );

                            for ( WorkFlowAdditionalAttributeDTO attrib : listAttrib ) {
                                AtomicInteger isChanged = new AtomicInteger( ConstantsInteger.INTEGER_VALUE_ZERO );
                                WorkFlowAdditionalAttributeDTO selectionItemAttributeObj = selectionManager.getAdditionalAttributeManager()
                                        .getWorkFlowAdditionalAttributeDtoByName( selectionItemAttributeList, attrib, isChanged,
                                                entitySelect.getItems() );
                                map.put( attrib.getName(), selectionItemAttributeObj );
                                updateAttributeMap.add( selectionItemAttributeObj );
                            }
                            selectionItemEntity.setAdditionalAttributesJson( JsonUtils.toJson( updateAttributeMap ) );
                        }
                        bmwList.add( map );
                    } else {
                        notFoundObjects.add( selectionItemEntity.getItem() );
                    }
                }
            }

            if ( selectedEntityIds != null && !selectedEntityIds.isEmpty() ) {
                entitySelect.setItems( selectedEntityIds );
                selectionManager.getSelectionDAO().saveOrUpdate( entityManager, entitySelect );
            }

            filter.setFilteredRecords( Long.valueOf( bmwList.size() ) );
            filter.setTotalRecords( Long.valueOf( bmwList.size() ) );

            FiltersDTO removeId = new FiltersDTO();
            removeId.setItems( notFoundObjects );
            selectionManager.removeSelectionItemsInExistingSelection( entityManager, selectionId, removeId );
            return PaginationUtil.constructFilteredResponse( filter, bmwList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the selection items additional attributes.
     *
     * @param selectionItemEntity
     *         the selection item entity
     *
     * @return the selection items additional attributes
     */
    private List< WorkFlowAdditionalAttributeDTO > getSelectionItemsAdditionalAttributes( SelectionItemEntity selectionItemEntity ) {
        if ( selectionItemEntity.getAdditionalAttributesJson() != null && !selectionItemEntity.getAdditionalAttributesJson().isEmpty() ) {

            return JsonUtils.jsonToList( selectionItemEntity.getAdditionalAttributesJson(), WorkFlowAdditionalAttributeDTO.class );
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< LoadCaseDTO > getLoadCaseSelection( String selectionId, String filterJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            List< SelectionItemEntity > selectedUserIds = selectionManager.getUserSelectionsBySelectionIds( entityManager, selectionId,
                    filter );
            List< Object > notFoundObjects = new ArrayList<>();
            List< LoadCaseDTO > loadCaseDTOs = new ArrayList<>();
            if ( CollectionUtil.isNotEmpty( selectedUserIds ) ) {
                for ( SelectionItemEntity user : selectedUserIds ) {
                    LoadCaseEntity loadCaseEntity = loadCaseDAO.getLatestNonDeletedObjectById( entityManager,
                            UUID.fromString( user.getItem() ) );
                    if ( null != loadCaseEntity ) {
                        loadCaseDTOs.add( loadcaseManager.createLoadcaseDTOFromEntity( loadCaseEntity ) );
                    } else {
                        notFoundObjects.add( user.getItem() );
                    }
                }
            }

            FiltersDTO removeId = new FiltersDTO();
            removeId.setItems( notFoundObjects );
            selectionManager.removeSelectionItemsInExistingSelection( entityManager, selectionId, removeId );
            return PaginationUtil.constructFilteredResponse( filter, loadCaseDTOs );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Run loadcases 2.
     *
     * @param runItem
     *         the run item
     * @param userId
     *         the user id
     * @param token
     *         the token
     * @param uid
     *         the uid
     * @param variantId
     *         the variant id
     * @param hostId
     *         the host id
     * @param isRunDummy
     *         the is run Dummy
     *
     * @implNote To be used in thread calls only
     */
    private void runLoadcases2Thread( VariantWizardDTO runItem, String userId, String token, String uid, String variantId, String hostId,
            Boolean isRunDummy ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            runLoadcases2( entityManager, runItem, userId, token, uid, variantId, hostId, isRunDummy );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Run loadcases 2.
     *
     * @param entityManager
     *         the entity manager
     * @param runItem
     *         the run item
     * @param userId
     *         the user id
     * @param token
     *         the token
     * @param uid
     *         the uid
     * @param variantId
     *         the variant id
     * @param hostId
     *         the host id
     * @param isRunDUmmy
     *         the is run D ummy
     */
    private void runLoadcases2( EntityManager entityManager, VariantWizardDTO runItem, String userId, String token, String uid,
            String variantId, String hostId, Boolean isRunDUmmy ) {
        List< LoadcaseSubmitDTO > preparedLoadcaseList = Collections.synchronizedList( new ArrayList<>() );
        int index = 0;
        AtomicInteger executedLoadcase = new AtomicInteger( ConstantsInteger.INTEGER_VALUE_ZERO );
        List< UUID > selectedLoadcases = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                runItem.getLoadcaseSelectionId().toString() );
        int selectedLoadcaseCount = selectedLoadcases.size();
        for ( UUID loadcaseSubmitId : selectedLoadcases ) {
            log.info( ">>runLoadcases loadcaseSubmitId: " + loadcaseSubmitId );
            Map< String, Integer > loadcaseSubmitValues = new HashMap<>();
            loadcaseSubmitValues = ( Map< String, Integer > ) JsonUtils.jsonToMap(
                    JsonUtils.objectToJson( runItem.getLoadcaseSubmit().get( loadcaseSubmitId.toString() ) ), loadcaseSubmitValues );
            if ( null != loadcaseSubmitValues && loadcaseSubmitValues.containsKey( "submit" )
                    && loadcaseSubmitValues.get( "submit" ) == 1 ) {
                SubmitLoadcase submitItem = new SubmitLoadcase();
                if ( loadcaseSubmitValues.containsKey( "assemble" ) && runItem.getAssemble() != null
                        && runItem.getAssemble().get( index ) != null ) {
                    submitItem.setAssemble( loadcaseSubmitValues.get( "assemble" ) == 1 );
                    submitItem.setOassemble( runItem.getAssemble().get( index ) );
                } else {
                    submitItem.setAssemble( false );
                }
                if ( loadcaseSubmitValues.containsKey( "solve" ) && runItem.getSolve() != null
                        && runItem.getSolve().get( index ) != null ) {
                    submitItem.setSolve( loadcaseSubmitValues.get( "solve" ) == 1 );
                    submitItem.setOsolve( runItem.getSolve().get( index ) );
                } else {
                    submitItem.setSolve( false );
                }
                if ( loadcaseSubmitValues.containsKey( "post" ) && runItem.getPost() != null && runItem.getPost().get( index ) != null ) {
                    submitItem.setPost( loadcaseSubmitValues.get( "post" ) == 1 );
                    submitItem.setOpost( runItem.getPost().get( index ) );
                } else {
                    submitItem.setPost( false );
                }
                LoadcaseSubmitDTO submitloadcase = new LoadcaseSubmitDTO( runItem, UUID.fromString( userId ), token, uid,
                        UUID.fromString( variantId ), ( ( hostId != null ? UUID.fromString( hostId ) : null ) ), isRunDUmmy, submitItem,
                        selectedLoadcaseCount, loadcaseSubmitId );
                submitloadcase.setUserId( UUID.fromString( userId ) );
                submitloadcase.setDummyIterationCompleted( false );

                synchronized ( UUID.randomUUID() ) {
                    preparedLoadcaseList.add( submitloadcase );
                }
            }
            index += 1;
            executedLoadcase.getAndIncrement();
        }
        // save iteration in db
        SubmitLoadcaseEntity submitLoadcaseEntity = new SubmitLoadcaseEntity();
        submitLoadcaseEntity.setId( UUID.randomUUID() );
        submitLoadcaseEntity.setJobCompleted( false );
        String jsonn = JsonUtils.convertListToJson( preparedLoadcaseList );
        submitLoadcaseEntity.setLoadcaseListjson( jsonn );

        SubmitLoadcaseEntity savedEntiry = submitLoadcaseDAO.save( entityManager, submitLoadcaseEntity );
        entityManager.detach( savedEntiry );
        runPendingJobs( entityManager, preparedLoadcaseList, savedEntiry.getId() );

    }

    /**
     * Prepare job token string.
     *
     * @param userId
     *         the user id
     * @param uid
     *         the uid
     * @param id
     *         the id
     * @param server
     *         the server
     * @param requestHeaders
     *         the request headers
     *
     * @return the string
     */
    private String prepareJobToken( String userId, String uid, String id, RestAPI server, RequestHeaders requestHeaders ) {
        final String url = prepareURL( "/api/auth/prepareJobToken/" + userId + "/" + uid + "/" + id + "/" + requestHeaders.getToken(),
                server );
        SusResponseDTO susResponseDTO = SuSClient.getRequest( url, prepareHeaders( requestHeaders ) );
        String token = null;
        if ( susResponseDTO != null && !susResponseDTO.getSuccess() ) {
            throw new SusException( new Exception( susResponseDTO.getMessage().getContent() ), getClass() );
        } else {
            token = susResponseDTO.getData().toString();
        }
        return token;
    }

    /**
     * Prepare url string.
     *
     * @param url
     *         the url
     * @param server
     *         the server
     *
     * @return the string
     */
    private String prepareURL( String url, RestAPI server ) {
        if ( server != null ) {
            return server.getProtocol() + server.getHostname() + ConstantsString.COLON + server.getPort() + url;
        } else {
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.CONFIG_NOT_PROVIDED ) ) );
        }
    }

    /**
     * Prepare headers map.
     *
     * @param headers
     *         the headers
     *
     * @return the map
     */
    private Map< String, String > prepareHeaders( RequestHeaders headers ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, headers.getToken() );
        requestHeaders.put( ConstantRequestHeader.JOB_TOKEN, headers.getJobAuthToken() );
        return requestHeaders;
    }

    /**
     * Run all pending jobs.
     *
     * @param entityManager
     *         the entity manager
     */
    public void runAllPendingJobs( EntityManager entityManager ) {

        try {

            log.debug( "Running all pending jobs" );
            List< SubmitLoadcaseEntity > pendingJobsList = submitLoadcaseDAO.getAllPendingJobs( entityManager );

            if ( pendingJobsList != null && !pendingJobsList.isEmpty() ) {
                log.debug( "All runing job count : " + pendingJobsList.size() );

                for ( SubmitLoadcaseEntity submitLoadcaseEntity : pendingJobsList ) {
                    entityManager.detach( submitLoadcaseEntity );
                    if ( !submitLoadcaseEntity.getJobCompleted() ) {

                        String loadcaseListjson = submitLoadcaseEntity.getLoadcaseListjson();

                        List< LoadcaseSubmitDTO > preparedLoadcaseList = JsonUtils.jsonToList( loadcaseListjson, LoadcaseSubmitDTO.class );
                        log.debug( "submiting pending Dummy job" );
                        log.debug( "*********************************************" );
                        log.debug( preparedLoadcaseList );
                        log.debug( "*********************************************" );

                        runPendingJobs( entityManager, preparedLoadcaseList, submitLoadcaseEntity.getId() );
                    }
                }
            }

        } catch ( Exception e ) {
            log.error( "EXCEPTION" );
            log.error( "runAllPendingJobs FAILED : ", e );
        }
    }

    /**
     * Run pending jobs.
     *
     * @param entityManager
     *         the entity manager
     * @param preparedLoadcaseList
     *         the prepared loadcase list
     * @param submitLoadcaseEntityId
     *         the submit loadcase entity id
     */
    public void runPendingJobs( EntityManager entityManager, List< LoadcaseSubmitDTO > preparedLoadcaseList, UUID submitLoadcaseEntityId ) {

        AtomicInteger executedLoadcase = new AtomicInteger( ConstantsInteger.INTEGER_VALUE_ZERO );

        for ( LoadcaseSubmitDTO currentloadcaseSubmitDTO : preparedLoadcaseList ) {

            try {

                if ( currentloadcaseSubmitDTO.isDummyIterationCompleted() ) {
                    continue;
                }

                LoadCaseDTO loadcaseDto = loadcaseManager.getLoadcase( entityManager, currentloadcaseSubmitDTO.getUserId().toString(),
                        currentloadcaseSubmitDTO.getLoadcaseSubmitId() );
                Job jobParametersMaster = prepareJobModel( entityManager,
                        currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getName() + "_" + loadcaseDto.getName() + "_Master", "",
                        SystemWorkflow.MASTER );
                jobParametersMaster.setId( UUID.randomUUID() );
                if ( null != currentloadcaseSubmitDTO.getHostId() ) {
                    Hosts hosts = PropertiesManager.getHosts();
                    if ( hosts != null && CollectionUtils.isNotEmpty( hosts.getExcutionHosts() ) ) {
                        for ( ExecutionHosts executionHosts : hosts.getExcutionHosts() ) {
                            if ( executionHosts.getId().equals( currentloadcaseSubmitDTO.getHostId() ) ) {
                                jobParametersMaster.setMachine( executionHosts.getName() );
                            }
                        }
                    }
                }
                Job savedMasterJob = null;
                log.debug( "saving master job **********************************" );
                if ( currentloadcaseSubmitDTO.getMasterJobId() == null ) {
                    List< LogRecord > jobLog = new ArrayList<>();
                    jobLog.add( new LogRecord( INFO, MessageBundleFactory.getMessage( Messages.GOING_TO_MAKE_MASTER_JOB.getKey() ),
                            new Date() ) );
                    jobParametersMaster.setLog( jobLog );
                    jobParametersMaster.setDescription( currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getJobDescription() );
                    jobParametersMaster.setCreatedBy(
                            userCommonManager.getUserById( entityManager, currentloadcaseSubmitDTO.getUserId() ) );
                    jobParametersMaster.setProgress( new ProgressBar( Long.valueOf( 0 ), Long.valueOf( 1 ) ) );
                    jobParametersMaster.setJobRelationType( JobRelationTypeEnums.MASTER.getKey() );
                    jobParametersMaster.setJobType( JobTypeEnums.VARIANT.getKey() );
                    jobParametersMaster.setRunMode( RUN_MODE );
                    jobParametersMaster.setOs( OSValidator.getOperationSystemName() );
                    RestAPI server = getServerByLocationId( entityManager, LocationsEnum.DEFAULT_LOCATION.getId() );
                    RequestHeaders requestHeader = new RequestHeaders( currentloadcaseSubmitDTO.getToken(),
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36" );
                    String jobToken = prepareJobToken( currentloadcaseSubmitDTO.getUserId().toString(), currentloadcaseSubmitDTO.getUid(),
                            jobParametersMaster.getId().toString(), server, requestHeader );
                    currentloadcaseSubmitDTO.setToken( jobToken );
                    requestHeader.setJobAuthToken( jobToken );
                    requestHeader.setToken( null );
                    jobParametersMaster.setRequestHeaders( requestHeader );
                    Integer id = workflowManager.getJobManager().saveJobIds( entityManager, jobParametersMaster.getId() );
                    if ( id != null ) {
                        jobParametersMaster.setJobInteger( id );
                    }
                    savedMasterJob = jobManager.createJob( entityManager, currentloadcaseSubmitDTO.getUserId(), jobParametersMaster );
                    currentloadcaseSubmitDTO.setMasterJobId( savedMasterJob.getId() );

                    // save master job in SubmitLoadcaseEntity
                    updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );
                } else {
                    RequestHeaders requestHeader = new RequestHeaders();
                    requestHeader.setUserAgent(
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36" );
                    requestHeader.setJobAuthToken( currentloadcaseSubmitDTO.getToken() );
                    jobParametersMaster.setRequestHeaders( requestHeader );
                    savedMasterJob = jobManager.getJob( entityManager, currentloadcaseSubmitDTO.getUserId(),
                            currentloadcaseSubmitDTO.getMasterJobId().toString() );
                }

                currentloadcaseSubmitDTO.setMasterJobId( savedMasterJob.getId() );
                log.info( "Monitoring job with id: " + savedMasterJob.getId() );
                Runnable runnable = () -> {
                    log.info( ">>submitAssembly loadcaseSubmitId: " + currentloadcaseSubmitDTO.getLoadcaseSubmitId() );
                    submitAssemblyThread( loadcaseDto, executedLoadcase.intValue(), currentloadcaseSubmitDTO, preparedLoadcaseList,
                            submitLoadcaseEntityId, currentloadcaseSubmitDTO.getMasterJobId(),
                            jobParametersMaster.getRequestHeaders().getJobAuthToken() );
                };
                Thread t = new Thread( runnable );
                t.start();
                executedLoadcase.getAndIncrement();
            } catch ( Exception e ) {
                log.debug( "pending Dummy iteration Failed : ", e );
            }
        }

    }

    /**
     * Submit assembly.
     *
     * @param loadcaseDto
     *         the loadcase dto
     * @param executedLoadcaseintValue
     *         the executed loadcaseint value
     * @param currentloadcaseSubmitDTO
     *         the currentloadcase submit DTO
     * @param preparedLoadcaseList
     *         the prepared loadcase list
     * @param submitLoadcaseEntityId
     *         the submit loadcase entity id
     * @param masterJobId
     *         the master job id
     * @param masterToken
     *         the master token
     *
     * @implNote To be used in thread calls only
     */
    private void submitAssemblyThread( LoadCaseDTO loadcaseDto, int executedLoadcaseintValue, LoadcaseSubmitDTO currentloadcaseSubmitDTO,
            List< LoadcaseSubmitDTO > preparedLoadcaseList, UUID submitLoadcaseEntityId, UUID masterJobId, String masterToken ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            submitAssembly( entityManager, loadcaseDto, executedLoadcaseintValue, currentloadcaseSubmitDTO, preparedLoadcaseList,
                    submitLoadcaseEntityId, masterJobId, masterToken );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Submit assembly.
     *
     * @param entityManager
     *         the entity manager
     * @param loadcaseDto
     *         the loadcase dto
     * @param executedLoadcaseintValue
     *         the executed loadcaseint value
     * @param currentloadcaseSubmitDTO
     *         the currentloadcase submit DTO
     * @param preparedLoadcaseList
     *         the prepared loadcase list
     * @param submitLoadcaseEntityId
     *         the submit loadcase entity id
     * @param masterJobId
     *         the master job id
     * @param masterToken
     *         the master token
     */
    private void submitAssembly( EntityManager entityManager, LoadCaseDTO loadcaseDto, int executedLoadcaseintValue,
            LoadcaseSubmitDTO currentloadcaseSubmitDTO, List< LoadcaseSubmitDTO > preparedLoadcaseList, UUID submitLoadcaseEntityId,
            UUID masterJobId, String masterToken ) {
        try {
            boolean assembleSuccess = false;
            if ( !currentloadcaseSubmitDTO.getSubmitItem().isAssemble() ) {
                currentloadcaseSubmitDTO.setDummyIterationCompleted( true );
                updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );
                updateJobWithStatus( entityManager, currentloadcaseSubmitDTO.getUserId(), currentloadcaseSubmitDTO.getMasterJobId(),
                        new Status( WorkflowStatus.COMPLETED ) );
                authManager.expireJobToken( entityManager, masterToken );
                return;
            }
            UserEntity userEntity = userCommonManager.getUserEntityById( entityManager, currentloadcaseSubmitDTO.getUserId() );
            UUID loadcaseContainerId = null;
            if ( currentloadcaseSubmitDTO.getLoadcaseContainerId() == null ) {
                // create loadcase in selected varient
                loadcaseContainerId = createLoadCaseInSelectedContainer( entityManager,
                        currentloadcaseSubmitDTO.getOrignalVariantWizardDTO(), userEntity,
                        currentloadcaseSubmitDTO.getVariantId().toString(), loadcaseDto.getName(), masterJobId );
                // save variant relation with loadcase relation for trail tab
                relationDAO.save( entityManager,
                        new Relation( currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getId(), loadcaseContainerId,
                                AuditTrailRelationType.RELATION_CREATED ) );
                currentloadcaseSubmitDTO.setLoadcaseContainerId( loadcaseContainerId );
                updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );
            } else {
                loadcaseContainerId = currentloadcaseSubmitDTO.getLoadcaseContainerId();
            }
            RestAPI server = getServerByLocationId( entityManager, LocationsEnum.DEFAULT_LOCATION.getId() );
            String scriptsPath = getNodeFromDummyConfig( "1", "scriptsPath" ).asText();
            Map< String, Object > vars = null;
            try {
                vars = prepareAssembleVariables( entityManager, loadcaseDto, currentloadcaseSubmitDTO.getUserId().toString(),
                        currentloadcaseSubmitDTO.getVariantId().toString(), currentloadcaseSubmitDTO.getOrignalVariantWizardDTO(),
                        loadcaseContainerId, currentloadcaseSubmitDTO.getIsRunDUmmy(), currentloadcaseSubmitDTO.getSelectedLoadcaseCount(),
                        executedLoadcaseintValue, scriptsPath );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }
            String _masterId = String.valueOf( currentloadcaseSubmitDTO.getMasterJobId() );
            JobParameters jobParametersA = prepareJobParameter( entityManager,
                    currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getName() + "_Assembly",
                    currentloadcaseSubmitDTO.getUserId().toString(), currentloadcaseSubmitDTO.getToken(),
                    currentloadcaseSubmitDTO.getSubmitItem().getOassemble(), server, vars,
                    ( ( currentloadcaseSubmitDTO.getHostId() != null ? currentloadcaseSubmitDTO.getHostId().toString() : null ) ),
                    currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getName() + "_" + loadcaseDto.getName() );
            jobParametersA.setJobRelationType( JobRelationTypeEnums.CHILD.getKey() );
            jobParametersA.setJobType( JobTypeEnums.VARIANT.getKey() );
            jobParametersA.setJobRunByUserId( currentloadcaseSubmitDTO.getUserId() );
            jobParametersA.setJobRunByUserUID( userEntity.getUserUid() );
            jobParametersA.setCreateChild( currentloadcaseSubmitDTO.getSubmitItem().isSolve() );
            jobParametersA.getWorkflow().setDummyMasterJobId( _masterId );
            jobParametersA.getRequestHeaders().setToken( null );
            jobParametersA.getRequestHeaders().setJobAuthToken( masterToken );
            // check if job is created against saved job id
            boolean submitWf = false;
            if ( currentloadcaseSubmitDTO.getAssemblyJobId() != null ) {
                JobEntity job = null;
                job = jobManager.getJobById( entityManager, currentloadcaseSubmitDTO.getAssemblyJobId() );
                if ( job == null ) {
                    submitWf = true;
                }
            }
            // if job id is saved but job is not yet created Submit job again
            if ( currentloadcaseSubmitDTO.getAssemblyJobId() == null || ( currentloadcaseSubmitDTO.getAssemblyJobId() != null
                    && submitWf ) ) {
                // create folder in staging and copy additional files there
                Set< String > allAdditionalFilePath = copyAdditnalFilesInStaging( entityManager, userEntity,
                        currentloadcaseSubmitDTO.getOrignalVariantWizardDTO(), jobParametersA,
                        currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getName() + "_" + loadcaseDto.getName() );
                writeAdditionalFilesName( entityManager, allAdditionalFilePath, currentloadcaseSubmitDTO.getOrignalVariantWizardDTO(),
                        userEntity.getUserUid(), jobParametersA.getWorkflow().getName(), jobParametersA.getJobInteger().toString(),
                        currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getName() + "_" + loadcaseDto.getName() );
                // currentloadcaseSubmitDTO.getOrignalVariantWizardDTO()
                synchronized ( UUID.randomUUID() ) {
                    workflowManager.runServerJob( entityManager, userEntity, jobParametersA,
                            currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getName() + "_" + loadcaseDto.getName() );
                }
                log.info( "Running Assembly job: " + jobParametersA.getName() );
                log.info( "Monitoring job with id: " + jobParametersA.getId() );
                relationDAO.save( entityManager,
                        new Relation( currentloadcaseSubmitDTO.getMasterJobId(), UUID.fromString( jobParametersA.getId() ) ) );
                currentloadcaseSubmitDTO.setAssemblyJobId( UUID.fromString( jobParametersA.getId() ) );
                // save master job in SubmitLoadcaseEntity
                updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );
                // check status, then run solve, or next steps
                // save master job in SubmitLoadcaseEntity
            } else {
                jobParametersA.setId( currentloadcaseSubmitDTO.getAssemblyJobId().toString() );
                // check status, then run solve, or next steps
                // save master job in SubmitLoadcaseEntity
            }
            assembleSuccess = monitorJob( entityManager, jobParametersA.getId() );
            if ( !assembleSuccess ) {
                currentloadcaseSubmitDTO.setDummyIterationCompleted( true );
                updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );
                authManager.expireJobToken( entityManager, masterToken );
                return;
            }
            currentloadcaseSubmitDTO.setAssemblyComplete( true );
            updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );
            new Thread( () -> submitSolve2Thread( _masterId, loadcaseDto, userEntity, server, "", scriptsPath, executedLoadcaseintValue,
                    currentloadcaseSubmitDTO, preparedLoadcaseList, submitLoadcaseEntityId, masterToken ) ).start();
        } catch ( Exception e ) {
            currentloadcaseSubmitDTO.setDummyIterationCompleted( true );
            updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );
            checkAndSetJobStatusFailed( entityManager, currentloadcaseSubmitDTO.getUserId(), currentloadcaseSubmitDTO.getMasterJobId(),
                    e.getMessage() );
            authManager.expireJobToken( entityManager, masterToken );
        }
    }

    /**
     * Submit solve 2 thread.
     *
     * @param _masterId
     *         the master id
     * @param loadcaseDto
     *         the loadcase dto
     * @param userEntity
     *         the user entity
     * @param server
     *         the server
     * @param deckEntity
     *         the deckEntity
     * @param scriptsPath
     *         the scripts path
     * @param executedLoadcaseintValue
     *         the executed loadcaseint value
     * @param currentloadcaseSubmitDTO
     *         the currentloadcase submit dto
     * @param preparedLoadcaseList
     *         the prepared loadcase list
     * @param submitLoadcaseEntityId
     *         the submit loadcase entity id
     * @param masterToken
     *         the master token
     *
     * @implNote To be used in thread calls only
     */
    private void submitSolve2Thread( String _masterId, LoadCaseDTO loadcaseDto, UserEntity userEntity, RestAPI server, String deckEntity,
            String scriptsPath, int executedLoadcaseintValue, LoadcaseSubmitDTO currentloadcaseSubmitDTO,
            List< LoadcaseSubmitDTO > preparedLoadcaseList, UUID submitLoadcaseEntityId, String masterToken ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            submitSolve2( entityManager, _masterId, loadcaseDto, userEntity, server, deckEntity, scriptsPath, executedLoadcaseintValue,
                    currentloadcaseSubmitDTO, preparedLoadcaseList, submitLoadcaseEntityId, masterToken );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Submit solve 2.
     *
     * @param entityManager
     *         the entity manager
     * @param _masterId
     *         the master id
     * @param loadcaseDto
     *         the loadcase dto
     * @param userEntity
     *         the user entity
     * @param server
     *         the server
     * @param deckEntity
     *         the deck entity
     * @param scriptsPath
     *         the scripts path
     * @param executedLoadcaseintValue
     *         the executed loadcaseint value
     * @param currentloadcaseSubmitDTO
     *         the currentloadcase submit DTO
     * @param preparedLoadcaseList
     *         the prepared loadcase list
     * @param submitLoadcaseEntityId
     *         the submit loadcase entity id
     * @param masterToken
     *         the master token
     */
    private void submitSolve2( EntityManager entityManager, String _masterId, LoadCaseDTO loadcaseDto, UserEntity userEntity,
            RestAPI server, String deckEntity, String scriptsPath, int executedLoadcaseintValue, LoadcaseSubmitDTO currentloadcaseSubmitDTO,
            List< LoadcaseSubmitDTO > preparedLoadcaseList, UUID submitLoadcaseEntityId, String masterToken ) {
        try {
            boolean solveSuccess = false;
            if ( !currentloadcaseSubmitDTO.getSubmitItem().isSolve() ) {
                currentloadcaseSubmitDTO.setDummyIterationCompleted( true );
                updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );
                updateJobWithStatus( entityManager, currentloadcaseSubmitDTO.getUserId(), currentloadcaseSubmitDTO.getMasterJobId(),
                        new Status( WorkflowStatus.COMPLETED ) );
                authManager.expireJobToken( entityManager, masterToken );
                return;
            }
            Map< String, Object > varsS = null;
            try {
                varsS = prepareSolveVariables( entityManager, "", loadcaseDto.getName(), userEntity.getId().toString(),
                        currentloadcaseSubmitDTO.getVariantId().toString(), currentloadcaseSubmitDTO.getOrignalVariantWizardDTO(),
                        currentloadcaseSubmitDTO.getLoadcaseContainerId(), scriptsPath );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }
            JobParameters jobParametersSolve = prepareJobParameter( entityManager,
                    currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getName() + "_Solver", userEntity.getId().toString(),
                    currentloadcaseSubmitDTO.getToken(), currentloadcaseSubmitDTO.getSubmitItem().getOsolve(), server, varsS,
                    currentloadcaseSubmitDTO.getHostId() != null ? currentloadcaseSubmitDTO.getHostId().toString() : null,
                    currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getName() + "_" + loadcaseDto.getName() );
            jobParametersSolve.setJobRelationType( JobRelationTypeEnums.CHILD.getKey() );
            jobParametersSolve.setJobType( JobTypeEnums.VARIANT.getKey() );
            jobParametersSolve.setJobRunByUserId( currentloadcaseSubmitDTO.getUserId() );
            jobParametersSolve.setJobRunByUserUID( userEntity.getUserUid() );
            jobParametersSolve.setCreateChild( currentloadcaseSubmitDTO.getSubmitItem().isPost() );
            jobParametersSolve.getWorkflow().setDummyMasterJobId( _masterId );
            jobParametersSolve.getRequestHeaders().setToken( null );
            jobParametersSolve.getRequestHeaders().setJobAuthToken( masterToken );
            // check if job is created against saved job id
            boolean submitWf = false;
            if ( currentloadcaseSubmitDTO.getSolveJobId() != null ) {
                JobEntity job = jobManager.getJobById( entityManager, currentloadcaseSubmitDTO.getSolveJobId() );
                if ( job == null ) {
                    submitWf = true;
                }
            }
            // if job id is saved but job is not yet created Submit job again
            // incase of failer set dummy as completed so that it can not be run again
            if ( currentloadcaseSubmitDTO.getSolveJobId() == null || ( currentloadcaseSubmitDTO.getSolveJobId() != null && submitWf ) ) {
                // create folder in staging and copy additional files there
                Set< String > allAdditionalFilePath = copyAdditnalFilesInStaging( entityManager, userEntity,
                        currentloadcaseSubmitDTO.getOrignalVariantWizardDTO(), jobParametersSolve,
                        currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getName() + "_" + loadcaseDto.getName() );
                writeAdditionalFilesName( entityManager, allAdditionalFilePath, currentloadcaseSubmitDTO.getOrignalVariantWizardDTO(),
                        userEntity.getUserUid(), jobParametersSolve.getWorkflow().getName(), jobParametersSolve.getJobInteger().toString(),
                        currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getName() + "_" + loadcaseDto.getName() );
                synchronized ( UUID.randomUUID() ) {
                    workflowManager.runServerJob( entityManager, userEntity, jobParametersSolve,
                            currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getName() + "_" + loadcaseDto.getName() );
                }
                log.info( "Running Solver job: " + jobParametersSolve.getName() );
                relationDAO.save( entityManager,
                        new Relation( currentloadcaseSubmitDTO.getAssemblyJobId(), UUID.fromString( jobParametersSolve.getId() ) ) );
                log.info( ">>relation between Assembly : " + currentloadcaseSubmitDTO.getAssemblyJobId() + " and Solver "
                        + jobParametersSolve.getId() );
                currentloadcaseSubmitDTO.setSolveJobId( UUID.fromString( jobParametersSolve.getId() ) );
                // save master job in SubmitLoadcaseEntity
                updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );
                // save master job in SubmitLoadcaseEntity
            } else {
                jobParametersSolve.setId( currentloadcaseSubmitDTO.getSolveJobId().toString() );
                // save master job in SubmitLoadcaseEntity
            }
            solveSuccess = monitorJob( entityManager, jobParametersSolve.getId() );
            if ( !solveSuccess ) {
                // incase of failer set dummy as completed so that it can not be run again
                currentloadcaseSubmitDTO.setDummyIterationCompleted( true );
                updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );
                authManager.expireJobToken( entityManager, masterToken );
                return;
            }
            currentloadcaseSubmitDTO.setSolverComplete( true );
            updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );
            // calling post method
            submitPost( entityManager, _masterId, loadcaseDto, userEntity, server, scriptsPath, executedLoadcaseintValue,
                    currentloadcaseSubmitDTO, preparedLoadcaseList, submitLoadcaseEntityId, jobParametersSolve, masterToken );

        } catch ( Exception e ) {
            currentloadcaseSubmitDTO.setDummyIterationCompleted( true );
            updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );
            checkAndSetJobStatusFailed( entityManager, currentloadcaseSubmitDTO.getUserId(), currentloadcaseSubmitDTO.getMasterJobId(),
                    e.getMessage() );
            authManager.expireJobToken( entityManager, masterToken );
        }

    }

    /**
     * Submit post.
     *
     * @param entityManager
     *         the entity manager
     * @param _masterId
     *         the master id
     * @param loadcaseDto
     *         the loadcase dto
     * @param userEntity
     *         the user entity
     * @param server
     *         the server
     * @param scriptsPath
     *         the scripts path
     * @param executedLoadcaseintValue
     *         the executed loadcaseint value
     * @param currentloadcaseSubmitDTO
     *         the currentloadcase submit DTO
     * @param preparedLoadcaseList
     *         the prepared loadcase list
     * @param submitLoadcaseEntityId
     *         the submit loadcase entity id
     * @param jobParametersSolve
     *         the job parameters solve
     * @param masterToken
     *         the master token
     */
    private void submitPost( EntityManager entityManager, String _masterId, LoadCaseDTO loadcaseDto, UserEntity userEntity, RestAPI server,
            String scriptsPath, int executedLoadcaseintValue, LoadcaseSubmitDTO currentloadcaseSubmitDTO,
            List< LoadcaseSubmitDTO > preparedLoadcaseList, UUID submitLoadcaseEntityId, JobParameters jobParametersSolve,
            String masterToken ) {
        log.debug( "Post running ********" );

        try {
            // call post here
            if ( !currentloadcaseSubmitDTO.getSubmitItem().isPost() ) {
                currentloadcaseSubmitDTO.setDummyIterationCompleted( true );
                updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );
                updateJobWithStatus( entityManager, currentloadcaseSubmitDTO.getUserId(), currentloadcaseSubmitDTO.getMasterJobId(),
                        new Status( WorkflowStatus.COMPLETED ) );
                authManager.expireJobToken( entityManager, masterToken );
                return;
            }

            Map< String, Object > varsP = null;
            try {
                varsP = preparePostVariables( entityManager, jobParametersSolve.getId(), loadcaseDto.getName(),
                        userEntity.getId().toString(), currentloadcaseSubmitDTO.getVariantId().toString(),
                        currentloadcaseSubmitDTO.getOrignalVariantWizardDTO(), currentloadcaseSubmitDTO.getLoadcaseContainerId(),
                        currentloadcaseSubmitDTO.getSelectedLoadcaseCount(), executedLoadcaseintValue, scriptsPath );

            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }

            JobParameters jobParametersP = prepareJobParameter( entityManager,
                    currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getName() + "_Post", userEntity.getId().toString(),
                    currentloadcaseSubmitDTO.getToken(), currentloadcaseSubmitDTO.getSubmitItem().getOpost(), server, varsP,
                    ( ( currentloadcaseSubmitDTO.getHostId() != null ? currentloadcaseSubmitDTO.getHostId().toString() : null ) ),
                    currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getName() + "_" + loadcaseDto.getName() );

            jobParametersP.setJobRelationType( JobRelationTypeEnums.CHILD.getKey() );
            jobParametersP.setJobType( JobTypeEnums.VARIANT.getKey() );
            jobParametersP.setJobRunByUserId( currentloadcaseSubmitDTO.getUserId() );
            jobParametersP.setJobRunByUserUID( userEntity.getUserUid() );
            jobParametersP.getWorkflow().setDummyMasterJobId( _masterId );
            jobParametersP.getRequestHeaders().setToken( null );
            jobParametersP.getRequestHeaders().setJobAuthToken( masterToken );

            // check if job is created against saved job id
            boolean submitWf2 = false;
            if ( currentloadcaseSubmitDTO.getPostJobId() != null ) {
                JobEntity job = jobManager.getJobById( entityManager, currentloadcaseSubmitDTO.getPostJobId() );
                if ( job == null ) {
                    submitWf2 = true;
                }
            }

            // if job id is saved but job is not yet created Submit job again
            if ( currentloadcaseSubmitDTO.getPostJobId() == null || ( currentloadcaseSubmitDTO.getPostJobId() != null && submitWf2 ) ) {
                // create folder in staging and copy additional files there
                Set< String > allAdditionalFilePath = copyAdditnalFilesInStaging( entityManager, userEntity,
                        currentloadcaseSubmitDTO.getOrignalVariantWizardDTO(), jobParametersP,
                        currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getName() + "_" + loadcaseDto.getName() );
                writeAdditionalFilesName( entityManager, allAdditionalFilePath, currentloadcaseSubmitDTO.getOrignalVariantWizardDTO(),
                        userEntity.getUserUid(), jobParametersP.getWorkflow().getName(), jobParametersP.getJobInteger().toString(),
                        currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getName() + "_" + loadcaseDto.getName() );
                synchronized ( UUID.randomUUID() ) {
                    workflowManager.runServerJob( entityManager, userEntity, jobParametersP,
                            currentloadcaseSubmitDTO.getOrignalVariantWizardDTO().getName() + "_" + loadcaseDto.getName() );
                }

                log.info( "Running Post job: " + jobParametersP.getName() );

                relationDAO.save( entityManager,
                        new Relation( UUID.fromString( jobParametersSolve.getId() ), UUID.fromString( jobParametersP.getId() ) ) );

                log.info( ">>relation between Solver : " + jobParametersSolve.getId() + " and Post " + jobParametersP.getId() );

                currentloadcaseSubmitDTO.setPostJobId( UUID.fromString( jobParametersP.getId() ) );
                // save master job in SubmitLoadcaseEntity
                updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );

                boolean postSuccess = monitorJob( entityManager, jobParametersP.getId() );

                if ( !postSuccess ) {
                    // incase of failer set dummy as completed so that it can not be run again
                    currentloadcaseSubmitDTO.setDummyIterationCompleted( true );
                    updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );
                    authManager.expireJobToken( entityManager, masterToken );
                    return;
                }

                // save master job in SubmitLoadcaseEntity

            } else {
                jobParametersP.setId( currentloadcaseSubmitDTO.getPostJobId().toString() );

                boolean postSuccess = monitorJob( entityManager, jobParametersP.getId() );
                if ( !postSuccess ) {
                    // incase of failer set dummy as completed so that it can not be run again
                    currentloadcaseSubmitDTO.setDummyIterationCompleted( true );
                    updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );
                    authManager.expireJobToken( entityManager, masterToken );
                    return;
                }

                // save master job in SubmitLoadcaseEntity

            }
            currentloadcaseSubmitDTO.setPostComplete( true );
            currentloadcaseSubmitDTO.setDummyIterationCompleted( true );
            updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );
        } catch ( Exception e ) {
            currentloadcaseSubmitDTO.setDummyIterationCompleted( true );
            updateSubmitLoadcaseEntityById( entityManager, submitLoadcaseEntityId, preparedLoadcaseList );
            checkAndSetJobStatusFailed( entityManager, currentloadcaseSubmitDTO.getUserId(), currentloadcaseSubmitDTO.getMasterJobId(),
                    e.getMessage() );
            authManager.expireJobToken( entityManager, masterToken );
        }
    }

    /**
     * Update submit loadcase entity by id.
     *
     * @param entityManager
     *         the entity manager
     * @param submitLoadcaseEntityId
     *         the submit loadcase entity id
     * @param preparedLoadcaseList
     *         the prepared loadcase list
     */
    private void updateSubmitLoadcaseEntityById( EntityManager entityManager, UUID submitLoadcaseEntityId,
            List< LoadcaseSubmitDTO > preparedLoadcaseList ) {
        var entity = submitLoadcaseDAO.update( entityManager,
                new SubmitLoadcaseEntity( submitLoadcaseEntityId, JsonUtils.toJson( preparedLoadcaseList ), false ) );
        entityManager.detach( entity );
    }

    /**
     * Write additional files name.
     *
     * @param entityManager
     *         the entity manager
     * @param allAdditionalFilePath
     *         the all additional file path
     * @param runItem
     *         the run item
     * @param userUid
     *         the user uid
     * @param jobName
     *         the job name
     * @param jobInteger
     *         the job integer
     * @param loadcaseName
     *         the loadcase name
     */
    private void writeAdditionalFilesName( EntityManager entityManager, Set< String > allAdditionalFilePath, VariantWizardDTO runItem,
            String userUid, String jobName, String jobInteger, String loadcaseName ) {
        try {
            StringBuilder bufferStore = new StringBuilder();

            // additional files names
            if ( null != runItem.getAdditionalFiles() ) {
                for ( AdditionalFiles file : runItem.getAdditionalFiles() ) {
                    for ( String path : allAdditionalFilePath ) {
                        String name = new File( path ).getName();
                        if ( name.equals( file.getTitle() ) ) {
                            if ( null != file.getCustomAttributes() ) {
                                String assembleMethod = file.getCustomAttributes().toString()
                                        .replace( ConstantsString.CURLY_LEFT_BRACKET, ConstantsString.EMPTY_STRING )
                                        .replace( ConstantsString.CURLY_RIGHT_BRACKET, ConstantsString.EMPTY_STRING );
                                if ( !assembleMethod.endsWith( ConstantsString.EQUALS_OPERATOR ) ) {
                                    bufferStore.append( name + ConstantsString.COMMA + assembleMethod + ConstantsString.NEW_LINE );
                                } else {
                                    getDefaultAssemblyMethod( bufferStore, name );
                                }
                            } else {
                                getDefaultAssemblyMethod( bufferStore, name );
                            }
                        }
                    }
                }
            }

            // cb2 files names
            for ( BmwCaeBenchDTO bmwCaeBenchDTOX : getSortedSelection( entityManager, runItem.getCb2projtree(),
                    BmwCaeBenchEnums.CB2_SUBMODEL.getValue() ) ) {
                BmwCaeBenchSubModelDTO submodels = ( BmwCaeBenchSubModelDTO ) bmwCaeBenchDTOX;

                if ( null != submodels.getAssembleType() && !submodels.getAssembleType().isEmpty() ) {
                    bufferStore.append( submodels.getName() + ConstantsString.COMMA + "assemblymethod=" + submodels.getAssembleType()
                            + ConstantsString.NEW_LINE );
                } else {
                    bufferStore.append( submodels.getName() + ConstantsString.NEW_LINE );
                }
            }

            // additional local files names
            if ( runItem.getAdditionalFilesLocal() != null ) {
                for ( DocumentDTO doc : runItem.getAdditionalFilesLocal() ) {
                    bufferStore.append( doc.getName() + ConstantsString.COMMA + "assemblymethod=" + ConstantsString.NEW_LINE );
                }
            }

            // additional sus files names
            String susSelection = runItem.getAdditionalFilesSus();
            if ( susSelection != null && !susSelection.isEmpty() ) {
                List< DataObjectDTO > allData = ( List< DataObjectDTO > ) ( Object ) dataObjectManager.getContainerOrChildsById(
                        entityManager, susSelection );

                for ( DataObjectDTO dataObjectDTO : allData ) {
                    if ( dataObjectDTO.getFile() != null && dataObjectDTO.getFile().getName() != null ) {
                        dataObjectDTO.getFile().getName();
                        dataObjectDTO.getCustomAttributes();

                        String method = "";
                        if ( dataObjectDTO.getCustomAttributes() != null ) {
                            for ( Entry< String, Object > mapAtrib : dataObjectDTO.getCustomAttributes().entrySet() ) {
                                method = mapAtrib.getValue().toString();
                            }
                        }
                        bufferStore.append( dataObjectDTO.getFile().getName() + ConstantsString.COMMA + "assemblymethod=" + method
                                + ConstantsString.NEW_LINE );
                    }
                }

            }

            String bmwSuggestion = loadcaseName + "_" + jobName + "_" + jobInteger;

            String jobDirPath = PropertiesManager.getUserStagingPath( userUid ) + File.separator + bmwSuggestion;
            String jobFilePath = jobDirPath + File.separator + "AdditionalFiles.txt";
            log.debug( "writing additional files names in job dir " + jobFilePath );

            LinuxUtils.createDirectory( userUid, jobDirPath );
            LinuxUtils.writeFile( userUid, jobFilePath, bufferStore.toString() );

        } catch ( Exception e ) {
            log.debug( "Error writing AdditionalFiles.txt  names files in job Dir " + e.getMessage(), e );
        }
    }

    /**
     * Gets the default assembly method.
     *
     * @param bufferStore
     *         the buffer store
     * @param name
     *         the name
     */
    private void getDefaultAssemblyMethod( StringBuilder bufferStore, String name ) {
        List< JsonNode > dumyTypesList = getServerFileAttributes( "customAttributes" );
        if ( CollectionUtil.isNotEmpty( dumyTypesList ) ) {
            for ( JsonNode atrrib : dumyTypesList ) {
                if ( atrrib.get( LABEL ).asText().equals( "Assembly Method" ) ) {
                    bufferStore.append( name + ConstantsString.COMMA + "assemblymethod=" + atrrib.get( "defaultValue" ).asText()
                            + ConstantsString.NEW_LINE );
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BmwCaeBenchTreeDTO getBmwCaeBenchTreeData( String userId ) {
        BmwCaeBenchTreeDTO cb2Tree = readTreeJsonFileWithImpersonation(
                String.format( SUBMODELS_JSON_FILE_PATH, TokenizedLicenseUtil.getUserUID( userId ) ) );
        if ( cb2Tree == null || cb2Tree.getData() == null ) {
            throw new SusException( "submodelsTree.json not loaded or empty" );
        }
        return cb2Tree;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BmwCaeBenchScenerioTreeDTO readScenerioTreeJsonFile() {

        try {
            File cb2TreeFile = new File( SCENARIO_JSON_FILE_PATH );
            FileUtils.setGlobalReadFilePermissions( cb2TreeFile );

            try ( InputStream is = new FileInputStream( cb2TreeFile ) ) {
                return JsonUtils.jsonToObject( is, BmwCaeBenchScenerioTreeDTO.class );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }
            return null;

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return new BmwCaeBenchScenerioTreeDTO();
    }

    /**
     * Read tree json file with impersonation.
     *
     * @param jsonPath
     *         the json path
     *
     * @return the cb 2 tree DTO
     */
    private static BmwCaeBenchTreeDTO readTreeJsonFileWithImpersonation( String jsonPath ) {

        try {
            File cb2TreeFile = new File( jsonPath );
            FileUtils.setGlobalReadFilePermissions( cb2TreeFile );
            try ( InputStream is = new FileInputStream( cb2TreeFile ) ) {
                return JsonUtils.jsonToObject( is, BmwCaeBenchTreeDTO.class );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }
            return null;

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< BmwCaeBenchSubModelTableDTO > getBmwCaeBenchVariantSubModelObjectListFromCB2( String userId, String userName, String token,
            String selectionId, String caeBenchType, String simdefOid ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< BmwCaeBenchSubModelTableDTO > bmwCaeBenchDtolist = new ArrayList<>();
            // String cb2JsonList = null;
            String cb2SearchModle;
            try {
                List< String > selectedList = selectionManager.getSelectedIdsStringListBySelectionId( entityManager, selectionId );
                String node = selectedList.get( 0 );
                BmwCaeBenchEntity entityBmw = bmwCaeBenchDAO.getUniqueObjectByProperty( entityManager, BmwCaeBenchEntity.class, "id",
                        UUID.fromString( node ) );
                if ( null != entityBmw ) {
                    cb2SearchModle = entityBmw.getOid().split( ":" )[ 0 ];
                } else {
                    throw new SusException( "Error extracting selected CB2 Entry against " + selectionId );
                }
            } catch ( Exception e ) {
                throw new SusException( "Error extracting selected CB2 Entry" + e.getMessage(), e );
            }
            try {
                if ( cb2SearchModle != null ) {
                    bmwCaeBenchDtolist = cb2DummyManager.getVariantSubModelsListOfCb2Objects( cb2SearchModle,
                            BmwCaeBenchEnums.CB2_SUBMODEL.getValue(), userName, simdefOid );
                } else {
                    return bmwCaeBenchDtolist;
                }
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }

            return bmwCaeBenchDtolist;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm addBmwCaeBenchVariantSubModelFormUI( String userId, String selectionId, String userName, String simdefOid ) {
        List< UIFormItem > listUserDirectory = new ArrayList<>();
        log.debug( "entered in addBmwCaeBenchVariantSubModelObjectList method" );

        // Object Type field added
        SelectFormItem selectUItwo = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectUItwo.setName( "selection_type" );
        selectUItwo.setLabel( "Selection Type" );
        selectUItwo.setType( SELECT );
        setRequired( selectUItwo, true );
        List< SelectOptionsUI > optionsTwo = new ArrayList<>();
        optionsTwo.add( new SelectOptionsUI( VariableDropDownEnum.INTERNAL.getId(), VariableDropDownEnum.INTERNAL.getName() ) );
        optionsTwo.add( new SelectOptionsUI( VariableDropDownEnum.CB2.getId(), VariableDropDownEnum.CB2.getName() ) );
        optionsTwo.add( new SelectOptionsUI( VariableDropDownEnum.SERVER.getId(), VariableDropDownEnum.SERVER.getName() ) );
        optionsTwo.add( new SelectOptionsUI( VariableDropDownEnum.LOCAL.getId(), VariableDropDownEnum.LOCAL.getName() ) );
        optionsTwo.add( new SelectOptionsUI( VariableDropDownEnum.VARIABLE.getId(), VariableDropDownEnum.VARIABLE.getName() ) );
        selectUItwo.setMultiple( Boolean.FALSE );
        selectUItwo.setOptions( optionsTwo );
        selectUItwo.setValue( VariableDropDownEnum.SERVER.getId() );
        selectUItwo.setBindFrom( "/external/bmw/variant/" + selectionId + "/simdef/" + simdefOid + "/submodels/options/{__value__}" );
        listUserDirectory.add( selectUItwo );

        return GUIUtils.createFormFromItems( listUserDirectory );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getSubModelTypeOptions( String userId, String userName, String selectionId, String dropDown, String auth,
            String simdefOid ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            BmwCaeBenchEntity entityBmw;
            try {
                List< String > selectedList = selectionManager.getSelectedIdsStringListBySelectionId( entityManager, selectionId );
                String node = selectedList.get( 0 );
                entityBmw = bmwCaeBenchDAO.getUniqueObjectByProperty( entityManager, BmwCaeBenchEntity.class, "id",
                        UUID.fromString( node ) );
            } catch ( Exception e ) {
                throw new SusException( "Error extracting selected CB2 Entry" + e.getMessage(), e );
            }
            List< UIFormItem > form = new ArrayList<>();
            if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.INTERNAL.getId() ) ) {
                form.add( prepareObjectSelectorForm( VariableDropDownEnum.getNameById( dropDown ), null ) );
            } else if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.CB2.getId() ) ) {
                form.add( prepareCB2Selector() );
            } else if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.SERVER.getId() ) ) {
                form.add( prepareFileExplorerForm( VariableDropDownEnum.getNameById( dropDown ), null ) );
            } else if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.LOCAL.getId() ) ) {
                form.add( prepareLocalFileSelectForm( VariableDropDownEnum.getNameById( dropDown ), null ) );
            } else if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.VARIABLE.getId() ) ) {

            }
            addSubModelOptionsToForm( userName, form, entityBmw );
            return form;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Add sub model options to form.
     *
     * @param userName
     *         the user name
     * @param form
     *         the form
     * @param entityBmw
     *         the entity bmw
     */
    private void addSubModelOptionsToForm( String userName, List< UIFormItem > form, BmwCaeBenchEntity entityBmw ) {
        SelectFormItem subModelTypeField = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        addOptionsForForm( userName, subModelTypeField, "name=='*'", "ItemDef" );
        subModelTypeField.setCanBeEmpty( false );
        subModelTypeField.setDuplicate( false );
        subModelTypeField.setLabel( "SubModel Type" );
        subModelTypeField.setName( "itemDefinition" );
        subModelTypeField.setType( SELECT );
        subModelTypeField.setConvert( "json" );
        setRequired( subModelTypeField, false );
        setLiveSearch( subModelTypeField );
        form.add( subModelTypeField );

        SelectFormItem projectField = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        projectField.setCanBeEmpty( false );
        projectField.setDuplicate( false );
        projectField.setLabel( "Project Path" );
        projectField.setName( "project" );
        projectField.setType( "text" );
        projectField.setValue( entityBmw != null ? entityBmw.getProject() : "" );
        setRequired( projectField, true );
        form.add( projectField );

        SelectFormItem projectItemField = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        projectItemField.setCanBeEmpty( false );
        projectItemField.setDuplicate( false );
        projectItemField.setLabel( "Project Item" );
        projectItemField.setName( "item" );
        projectItemField.setType( SELECT );
        projectItemField.setBindTo( getProjectItemBindToSetting() );
        projectItemField.setConvert( "json" );
        setRequired( projectItemField, true );
        setLiveSearch( projectItemField );
        form.add( projectItemField );

        SelectFormItem phaseField = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        phaseField.setCanBeEmpty( false );
        phaseField.setDuplicate( false );
        phaseField.setLabel( "Phase" );
        phaseField.setName( "projectPhase" );
        phaseField.setType( SELECT );
        phaseField.setBindTo( getProjectPhaseBindToSetting() );
        phaseField.setConvert( "json" );
        setRequired( phaseField, true );
        setLiveSearch( phaseField );
        form.add( phaseField );

        SelectFormItem nameField = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        nameField.setCanBeEmpty( false );
        nameField.setDuplicate( false );
        nameField.setLabel( "Name" );
        nameField.setName( NAME );
        nameField.setType( "text" );
        setRequired( nameField, true );
        form.add( nameField );

        SelectFormItem descriptionField = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        descriptionField.setCanBeEmpty( false );
        descriptionField.setDuplicate( false );
        descriptionField.setLabel( "Description" );
        descriptionField.setName( "description" );
        descriptionField.setType( "textarea" );
        setRequired( descriptionField, false );
        form.add( descriptionField );

        SelectFormItem solverTypeField = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        addOptionsForForm( userName, solverTypeField, "name=='*'", "Format" );
        solverTypeField.setCanBeEmpty( false );
        solverTypeField.setDuplicate( false );
        solverTypeField.setLabel( "Solver Type" );
        solverTypeField.setName( "format" );
        solverTypeField.setType( SELECT );
        solverTypeField.setConvert( "json" );
        setRequired( solverTypeField, false );
        setLiveSearch( solverTypeField );
        form.add( solverTypeField );

        SelectFormItem assembyTypeField = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        addOptionsForForm( userName, assembyTypeField, "name=='*'", "AssembleMethod" );
        assembyTypeField.setCanBeEmpty( false );
        assembyTypeField.setDuplicate( false );
        assembyTypeField.setLabel( "Assembly Type" );
        assembyTypeField.setName( "assembleType" );
        assembyTypeField.setType( SELECT );
        assembyTypeField.setConvert( "json" );
        setRequired( assembyTypeField, false );
        setLiveSearch( assembyTypeField );
        form.add( assembyTypeField );

        SelectFormItem departmentField = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        addOptionsForForm( userName, departmentField, "name=='*'", "Department" );
        departmentField.setCanBeEmpty( false );
        departmentField.setDuplicate( false );
        departmentField.setLabel( "Department" );
        departmentField.setName( "department" );
        departmentField.setType( SELECT );
        departmentField.setConvert( "json" );
        setRequired( departmentField, false );
        setLiveSearch( departmentField );
        form.add( departmentField );

        autoFillFieldsValues( projectItemField, phaseField, solverTypeField, assembyTypeField, departmentField, userName,
                entityBmw.getProject() );
    }

    /**
     * Prepare CB 2 selector.
     *
     * @return the select UI table
     */
    private TableFormItem prepareCB2Selector() {
        TableFormItem cb2Form = ( TableFormItem ) GUIUtils.createFormItem( FormItemType.TABLE );
        cb2Form.setName( "submodel_value" );
        cb2Form.setLabel( VariableDropDownEnum.CB2.getName() );
        cb2Form.setMultiple( false );
        cb2Form.setSelectable( null );
        cb2Form.setType( "externalObject" );

        cb2Form.setExternal( "bmw-cb2-project-tree" );

        Map< String, String > optional = new HashMap<>();
        Map< String, Object > rules = new HashMap<>();

        rules.put( REQUIRED, true );
        cb2Form.setRules( rules );
        cb2Form.setExtendTree( optional );
        Map< String, Object > message = new HashMap<>();
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        cb2Form.setMessages( message );
        return cb2Form;
    }

    /**
     * Prepare object selector form.
     *
     * @param type
     *         the type
     * @param formValue
     *         the form value
     *
     * @return the UI form item
     */
    private UIFormItem prepareObjectSelectorForm( String type, Object formValue ) {

        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( "submodel_value" );
        selectFormItem.setLabel( type + " " + "Selection" );
        selectFormItem.setValue( formValue );
        selectFormItem.setType( "object" );

        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();

        rules.put( REQUIRED, true );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        selectFormItem.setRules( rules );
        selectFormItem.setMessages( message );
        selectFormItem.setMultiple( Boolean.FALSE );
        return selectFormItem;
    }

    /**
     * Prepare file explorer form.
     *
     * @param type
     *         the type
     * @param formValue
     *         the form value
     *
     * @return the UI form item
     */
    private UIFormItem prepareFileExplorerForm( String type, Object formValue ) {

        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( "submodel_value" );
        selectFormItem.setLabel( type + " " + "Selection" );
        selectFormItem.setValue( formValue );
        selectFormItem.setType( FieldTypes.SERVER_FILE_EXPLORER.getType() );
        selectFormItem.setShow( "all" );
        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();

        rules.put( REQUIRED, true );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        selectFormItem.setRules( rules );
        selectFormItem.setMessages( message );
        selectFormItem.setMultiple( Boolean.FALSE );
        return selectFormItem;
    }

    /**
     * Prepare local file select form.
     *
     * @param type
     *         the type
     * @param formValue
     *         the form value
     *
     * @return the UI form item
     */
    private UIFormItem prepareLocalFileSelectForm( String type, Object formValue ) {

        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( "submodel_value" );
        selectFormItem.setLabel( type + " " + "Selection" );
        selectFormItem.setValue( formValue );
        selectFormItem.setType( "file-upload" );

        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();

        rules.put( REQUIRED, true );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        selectFormItem.setRules( rules );
        selectFormItem.setMessages( message );
        selectFormItem.setMultiple( Boolean.FALSE );
        return selectFormItem;
    }

    /**
     * Auto fill fields values.
     *
     * @param projectItemField
     *         the project item field
     * @param phaseField
     *         the phase field
     * @param solverTypeField
     *         the solver type field
     * @param assembyTypeField
     *         the assemby type field
     * @param departmentField
     *         the department field
     * @param userName
     *         the user name
     * @param projectPath
     *         the project path
     */
    private void autoFillFieldsValues( SelectFormItem projectItemField, SelectFormItem phaseField, SelectFormItem solverTypeField,
            SelectFormItem assembyTypeField, SelectFormItem departmentField, String userName, String projectPath ) {
        List< SelectOptionsUI > items = getProjectItemOptions( userName, projectPath );
        SelectOptionsUI projectItem = items.get( 0 );

        projectItemField.setValue( projectItem.getId() );

        fillFieldsFromCB2( phaseField, solverTypeField, assembyTypeField, departmentField, userName, projectPath );
    }

    /**
     * Fill fields values from cb2.
     *
     * @param phaseField
     *         the phase field
     * @param solverTypeField
     *         the solver type field
     * @param assembyTypeField
     *         the assemby type field
     * @param departmentField
     *         the department field
     * @param userName
     *         the user name
     * @param projectPathVariant
     *         the project path variant
     */
    private void fillFieldsFromCB2( SelectFormItem phaseField, SelectFormItem solverTypeField, SelectFormItem assembyTypeField,
            SelectFormItem departmentField, String userName, String projectPathVariant ) {
        // String projectPathVariant = "SE10_30p_EA30V003_DT_SOCO_par01";
        StringBuilder buildFilterQuery = new StringBuilder();
        buildFilterQuery.append( "type=" + BmwCaeBenchEnums.CB2_VARIANT.getValue() );
        buildFilterQuery.append( "&expr=" + prepareExprForSubVariant( projectPathVariant ) );
        addFieldsForCb2Filters( buildFilterQuery, BmwCaeBenchEnums.CB2_SUBMODEL.getValue() );
        buildFilterQuery.append( "&pageSize=1" );
        buildFilterQuery.append( "&format=json" );

        try {
            JSONObject jsonQueryResponseObj = CB2Client.GetCB2( userName, buildFilterQuery.toString() );
            if ( jsonQueryResponseObj.has( "objects" ) ) {
                JSONArray objects = jsonQueryResponseObj.getJSONArray( "objects" );
                JSONObject subObjects = objects.getJSONObject( 0 ); // taking the first cb2includes from list

                if ( subObjects.has( "attrs" ) ) {
                    JSONObject attrs = subObjects.getJSONObject( "attrs" );
                    if ( attrs.has( "projectPhase" ) ) {
                        JSONObject lable = attrs.getJSONObject( "projectPhase" );
                        if ( !lable.getString( "oid" ).isEmpty() ) {
                            Map< String, String > defMap = new HashMap<>();
                            defMap.put( NAME, lable.getString( "text" ) );
                            defMap.put( "id", lable.getString( "oid" ) );

                            phaseField.setValue( JsonUtils.convertMapToString( defMap ) );
                        }
                    }

                    if ( attrs.has( "format" ) ) {
                        JSONObject lable = attrs.getJSONObject( "format" );
                        if ( !lable.getString( "oid" ).isEmpty() ) {
                            Map< String, String > defMap = new HashMap<>();
                            defMap.put( NAME, lable.getString( "text" ) );
                            defMap.put( "id", lable.getString( "oid" ) );

                            solverTypeField.setValue( JsonUtils.convertMapToString( defMap ) );
                        }
                    }

                    if ( attrs.has( "assembleType" ) ) {
                        JSONObject lable = attrs.getJSONObject( "assembleType" );
                        if ( !lable.getString( "oid" ).isEmpty() ) {
                            Map< String, String > defMap = new HashMap<>();
                            defMap.put( NAME, lable.getString( "text" ) );
                            defMap.put( "id", lable.getString( "oid" ) );

                            assembyTypeField.setValue( JsonUtils.convertMapToString( defMap ) );
                        }
                    }

                    if ( attrs.has( "department" ) ) {
                        JSONObject lable = attrs.getJSONObject( "department" );
                        if ( !lable.getString( "oid" ).isEmpty() ) {
                            Map< String, String > defMap = new HashMap<>();
                            defMap.put( NAME, lable.getString( "text" ) );
                            defMap.put( "id", lable.getString( "oid" ) );

                            departmentField.setValue( JsonUtils.convertMapToString( defMap ) );
                        }
                    }
                }

            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * Prepare expr for sub variant.
     *
     * @param projectPath
     *         the project path
     *
     * @return the string
     */
    private String prepareExprForSubVariant( String projectPath ) {

        StringBuilder buildFilterQuery = new StringBuilder();
        buildFilterQuery.append( "name==" + "'" + projectPath + "'" );
        return "[" + buildFilterQuery + "]" + ".components.component:SubModelVariant.models:SubModel";
    }

    /**
     * Adds the fields for cb 2 filters.
     *
     * @param buildFilterQuery
     *         the build filter query
     * @param bmwCaeBenchJsonType
     *         the bmw cae bench json type
     */
    private void addFieldsForCb2Filters( StringBuilder buildFilterQuery, String bmwCaeBenchJsonType ) {
        List< TableColumn > columns;
        if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_VARIANT.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchVariantDTO.class );
        } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_SUBMODEL.getValue() ) || bmwCaeBenchJsonType.contains(
                BmwCaeBenchEnums.CB2_OBJECT_TREE.getValue() ) || bmwCaeBenchJsonType.contains(
                BmwCaeBenchEnums.CB2_DUMMY_TEMP_TREE.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchSubModelTableDTO.class );
        } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_INPUT_DECK.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchInputDeckDTO.class );
        } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_KEYRESULTS.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchKeyResultsDTO.class );
        } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_RESULT.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchResultDTO.class );
        } else {
            columns = GUIUtils.listColumns( BmwCaeBenchSubModelDTO.class );
        }

        for ( TableColumn tableColumn : columns ) {
            buildFilterQuery.append( "&f=" + tableColumn.getData() );
        }
    }

    /**
     * Gets project item bind to setting.
     *
     * @return the project item bind to setting
     */
    private BindTo getProjectItemBindToSetting() {
        BindTo bindTo = new BindTo();
        bindTo.setName( "project" );

        Map< String, String > params = new HashMap<>();
        params.put( "path", "{__value__}" );
        bindTo.setParams( params );

        bindTo.setUrl( "external/bmw/variant/projectItem/options" );

        return bindTo;
    }

    /**
     * Gets project phase bind to setting.
     *
     * @return the project phase bind to setting
     */
    private BindTo getProjectPhaseBindToSetting() {
        BindTo bindTo = new BindTo();
        bindTo.setName( "project" );

        Map< String, String > params = new HashMap<>();
        params.put( "path", "{__value__}" );
        bindTo.setParams( params );

        bindTo.setUrl( "external/bmw/variant/projectPhase/options" );

        return bindTo;
    }

    public List< SelectOptionsUI > getProjectItemOptions( String userName, String projectPath ) {
        try {
            SelectFormItem projectItemField = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            addOptionsForForm( userName, projectItemField, "project.name=='" + projectPath + "*'", "Item" );
            return projectItemField.getOptions();
        } catch ( Exception e ) {
            return new ArrayList<>();
        }
    }

    public List< SelectOptionsUI > getProjectPhaseOptions( String userName, String projectPath ) {
        try {
            SelectFormItem phaseField = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            addOptionsForForm( userName, phaseField, "name=='*'", "ProjectPhase" );
            return phaseField.getOptions();
        } catch ( Exception e ) {
            return new ArrayList<>();
        }
    }

    /**
     * Sets the liveSearch to true.
     *
     * @param selectFormItemItem
     *         the selectUiItem
     */
    private void setLiveSearch( SelectFormItem selectFormItemItem ) {
        Map< String, Object > liveSearch = new HashMap<>();
        liveSearch.put( LIVESEARCH, true );
        selectFormItemItem.setPicker( liveSearch );
    }

    /**
     * Adds the options.
     *
     * @param userName
     *         the user name
     * @param selectFormItem
     *         the select UI
     * @param searchQuery
     *         the search query
     * @param bmwCaeBenchJsonType
     *         the bmw cae bench json type
     */
    private void addOptions( String userName, SelectFormItem selectFormItem, String searchQuery, String bmwCaeBenchJsonType ) {
        List< SelectOptionsUI > optionsList = new ArrayList<>();
        try {
            List< BmwCaeBenchEntity > cb2List = cb2DummyManager.getCb2Objects( searchQuery, bmwCaeBenchJsonType, userName );
            for ( BmwCaeBenchEntity bmwCaeBenchEntity : cb2List ) {
                optionsList.add( new SelectOptionsUI( bmwCaeBenchEntity.getOid(), bmwCaeBenchEntity.getName() ) );
            }
            selectFormItem.setOptions( optionsList );
        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    /**
     * Adds the definition options.
     *
     * @param userName
     *         the user name
     * @param selectFormItem
     *         the select UI
     * @param searchQuery
     *         the search query
     * @param bmwCaeBenchJsonType
     *         the bmw cae bench json type
     */
    private void addOptionsForForm( String userName, SelectFormItem selectFormItem, String searchQuery, String bmwCaeBenchJsonType ) {
        addOptions( userName, selectFormItem, searchQuery, bmwCaeBenchJsonType );
        Map< String, String > defMap = new HashMap<>();
        List< SelectOptionsUI > optionsList = selectFormItem.getOptions();
        for ( SelectOptionsUI option : optionsList ) {
            defMap.put( NAME, option.getName() );
            defMap.put( "id", option.getId() );
            option.setId( JsonUtils.convertMapToString( defMap ) );
        }
    }

    /**
     * Sets the required.
     *
     * @param projectField
     *         the project field
     * @param bool
     *         the bool
     */
    private void setRequired( SelectFormItem projectField, boolean bool ) {
        Map< String, Object > rules = new HashMap<>();
        rules.put( REQUIRED, bool );
        projectField.setRules( rules );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< ? > getBmwCaeBenchObjectListFromCB2( String userId, String userName, String token, String node,
            FiltersDTO filter, String caeBenchType, String solverType ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_USER.getKey() ) );
            }
            UserEntity userEntity = userCommonManager.getUserEntityById( entityManager, UUID.fromString( userId ) );
            if ( licenseManager.isFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.CB2_CONNECTOR.getKey(),
                    userEntity.getId().toString() ) ) {
                validateCB2session( userEntity, token );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( "1300033x4" ) );
            }
            String cb2JsonList;
            Cb2ScenerioTreeChildrenDTO senarioNode = null;
            List< BmwCaeBenchDTO > bmwCaeBenchDtolist = new ArrayList<>();
            String urlCb2 = null;
            List< JsonNode > dumyTypesList = null;
            FiltersDTO filterTemp = new FiltersDTO();
            filterTemp.setStart( filter.getStart() );
            filterTemp.setLength( filter.getLength() );
            if ( caeBenchType.contains( BmwCaeBenchEnums.CB2_DUMMY_TEMP_TREE.getKey() ) ) {
                dumyTypesList = getDummyNodes( "dummyTypes", node, solverType, userEntity.getUserUid() );
                caeBenchType = BmwCaeBenchEnums.CB2_DUMMY_TEMP_TREE.getValue();
            }
            if ( caeBenchType.contains( BmwCaeBenchEnums.CB2_SCENARIO.getValue() ) ) {
                senarioNode = getSenarioTreeNode( node );
            } else {
                BmwCaeBenchTreeDTO nodes = getNodeFromSubModel( userEntity.getUserUid() );
                if ( nodes.getData().get( 0 ).getId().equalsIgnoreCase( node ) && !isFilterExists( filter ) ) {
                    // if selected node is root node show empty table
                    return PaginationUtil.constructFilteredResponse( filter, bmwCaeBenchDtolist );
                }
                urlCb2 = extractNodeUrl( node, nodes );
                if ( BmwCaeBenchEnums.CB2_PROJECT.getValue().contains( caeBenchType ) ) {
                    urlCb2 = urlCb2 + ";children";
                } else {
                    urlCb2 = urlCb2 + ";subprojects{SP}.sdmObjects:" + caeBenchType;
                }
            }

            if ( caeBenchType.contains( BmwCaeBenchEnums.CB2_SCENARIO.getValue() ) ) {
                cb2JsonList = cb2DummyManager.getSenarioListOfCb2Objects( userName, senarioNode.getPath(), filter );
            } else {
                cb2JsonList = prepareBmwCaeBenchOptionFromBmwCaeBench2( urlCb2, userId, dumyTypesList, node, caeBenchType, filter,
                        userName );
            }
            if ( cb2JsonList != null ) {
                List< BmwCaeBenchEntity > listFromCB2 = JsonUtils.jsonToList( cb2JsonList, BmwCaeBenchEntity.class );

                /*
                 * this is old/optimized approach to compare and prepare bmw records but its
                 * commented because of search results selection do not get highlighted because of
                 * different UUID in Different node(containers) to fix that we are getting all
                 * records against all nodes instead of particular node so that uuid can be
                 * matched and highlighted List< BmwCaeBenchEntity > listFromDbAllRecords =
                 * bmwCaeBenchDAO.getAllBmwFilteredRecordsByProperty( node, filterTemp,
                 * caeBenchType );
                 */

                Map< String, UUID > mapFromDbAllRecords = bmwCaeBenchDAO.getAllBmwFilteredRecordsSetByPropertyWithoutNode( entityManager,
                        filterTemp, caeBenchType );
                BmwCaeBenchNodeEntity bmwCaeBenchNodeEntity = bmwCaeBenchNodeDAO.getBmwCaeBenchNodeByName( entityManager, node );
                if ( null == mapFromDbAllRecords || mapFromDbAllRecords.isEmpty() || bmwCaeBenchNodeEntity == null ) {
                    // save BmwCaeBenchEntity with nodes
                    if ( bmwCaeBenchNodeEntity == null ) {
                        bmwCaeBenchNodeEntity = saveBmwCaeBenchNodeEntity( entityManager, node, caeBenchType );
                    }
                    for ( BmwCaeBenchEntity bmwCaeBenchObjectEntity : listFromCB2 ) {
                        saveOrUpdateBmwEntity( entityManager, caeBenchType, bmwCaeBenchNodeEntity, bmwCaeBenchObjectEntity );
                    }
                } else {
                    List< BmwCaeBenchEntity > bmwDifferenceList = compareCB2FilesWithDbFiles( listFromCB2, mapFromDbAllRecords.keySet() );

                    if ( CollectionUtil.isNotEmpty( bmwDifferenceList ) ) {
                        // if cb2 has extra files save them
                        for ( BmwCaeBenchEntity cb2ObjectEntity : bmwDifferenceList ) {
                            saveOrUpdateBmwEntity( entityManager, caeBenchType, bmwCaeBenchNodeEntity, cb2ObjectEntity );
                        }
                        mapFromDbAllRecords = bmwCaeBenchDAO.getAllBmwFilteredRecordsByProperty( entityManager, node, filterTemp,
                                caeBenchType );
                    }
                    listFromCB2 = getPreparedCb2ObjectList( listFromCB2, mapFromDbAllRecords );
                }
                if ( listFromCB2 != null && !listFromCB2.isEmpty() ) {
                    prepareBmwDtoListByBmwEntityList( caeBenchType, bmwCaeBenchDtolist, listFromCB2 );
                }
            }

            return PaginationUtil.constructFilteredResponse( filter, bmwCaeBenchDtolist );
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< BmwCaeBenchDTO > getBmwCaeBenchInputDeckForQADyna( String userId, String userName, String token, FiltersDTO filter,
            String caeBenchType ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UserEntity userEntity = userCommonManager.getUserEntityById( entityManager, UUID.fromString( userId ) );
            if ( licenseManager.isFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.CB2_CONNECTOR.getKey(),
                    userEntity.getId().toString() ) ) {
                validateCB2session( userEntity, token );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( "1300033x4" ) );
            }
            String cb2JsonList;
            List< BmwCaeBenchDTO > bmwCaeBenchDtolist = new ArrayList<>();
            FiltersDTO filterTemp = new FiltersDTO();
            filterTemp.setStart( filter.getStart() );
            filterTemp.setLength( filter.getLength() );
            String inputDeckNodeId = extractNodeIdByPath( PropertiesManager.qaDynaBaseInputProjectPath(),
                    readTreeJsonFileWithImpersonation( String.format( SUBMODELS_JSON_FILE_PATH, userEntity.getUserUid() ) ) );
            cb2JsonList = cb2DummyManager.getListOfCb2InputDecks( userId, userName, inputDeckNodeId, caeBenchType, filter );
            if ( cb2JsonList != null ) {
                List< BmwCaeBenchEntity > listFromCB2 = JsonUtils.jsonToList( cb2JsonList, BmwCaeBenchEntity.class );
                Map< String, UUID > mapFromDbAllRecords = bmwCaeBenchDAO.getAllBmwFilteredRecordsSetByPropertyWithoutNode( entityManager,
                        filterTemp, caeBenchType );
                BmwCaeBenchNodeEntity bmwCaeBenchNodeEntity = bmwCaeBenchNodeDAO.getBmwCaeBenchNodeByName( entityManager, inputDeckNodeId );
                if ( null == mapFromDbAllRecords || mapFromDbAllRecords.isEmpty() || bmwCaeBenchNodeEntity == null ) {
                    // save BmwCaeBenchEntity with nodes
                    if ( bmwCaeBenchNodeEntity == null ) {
                        bmwCaeBenchNodeEntity = saveBmwCaeBenchNodeEntity( entityManager, inputDeckNodeId, caeBenchType );
                    }
                    for ( BmwCaeBenchEntity bmwCaeBenchObjectEntity : listFromCB2 ) {
                        saveOrUpdateBmwEntity( entityManager, caeBenchType, bmwCaeBenchNodeEntity, bmwCaeBenchObjectEntity );
                    }
                } else {
                    List< BmwCaeBenchEntity > bmwDifferenceList = compareCB2FilesWithDbFiles( listFromCB2, mapFromDbAllRecords.keySet() );
                    if ( CollectionUtil.isNotEmpty( bmwDifferenceList ) ) {
                        // if cb2 has extra files save them
                        for ( BmwCaeBenchEntity cb2ObjectEntity : bmwDifferenceList ) {
                            saveOrUpdateBmwEntity( entityManager, caeBenchType, bmwCaeBenchNodeEntity, cb2ObjectEntity );
                        }
                        mapFromDbAllRecords = bmwCaeBenchDAO.getAllBmwFilteredRecordsByProperty( entityManager, inputDeckNodeId, filterTemp,
                                caeBenchType );
                    }
                    listFromCB2 = getPreparedCb2ObjectList( listFromCB2, mapFromDbAllRecords );
                }
                if ( !listFromCB2.isEmpty() ) {
                    prepareBmwDtoListByBmwEntityList( caeBenchType, bmwCaeBenchDtolist, listFromCB2 );
                }
            }

            return bmwCaeBenchDtolist;
        } finally {
            entityManager.close();
        }

    }

    /**
     * Compare CB 2 files with db files.
     *
     * @param listFromCB2
     *         the list from CB 2
     * @param existingOids
     *         the list from db all records 2
     *
     * @return the list
     */
    private List< BmwCaeBenchEntity > compareCB2FilesWithDbFiles( List< BmwCaeBenchEntity > listFromCB2, Set< String > existingOids ) {
        return listFromCB2.stream().filter( entry -> !existingOids.contains( entry.getOid() ) ) // O(1) lookup in HashSet
                .toList();
    }

    /**
     * Save bmw cae bench node entity.
     *
     * @param entityManager
     *         the entity manager
     * @param node
     *         the node
     * @param caeBenchType
     *         the cae bench type
     *
     * @return the bmw cae bench node entity
     */
    private BmwCaeBenchNodeEntity saveBmwCaeBenchNodeEntity( EntityManager entityManager, String node, String caeBenchType ) {
        BmwCaeBenchNodeEntity bmwCaeBenchNodeEntity;
        bmwCaeBenchNodeEntity = new BmwCaeBenchNodeEntity();
        bmwCaeBenchNodeEntity.setId( UUID.randomUUID() );
        bmwCaeBenchNodeEntity.setType( caeBenchType );
        bmwCaeBenchNodeEntity.setNode( node );
        bmwCaeBenchNodeDAO.saveOrUpdate( entityManager, bmwCaeBenchNodeEntity );
        return bmwCaeBenchNodeEntity;
    }

    /**
     * Prepare bmw dto list by bmw entity list.
     *
     * @param caeBenchType
     *         the cae bench type
     * @param bmwCaeBenchDtolist
     *         the bmw cae bench dtolist
     * @param dbBmwCaeBench
     *         the db bmw cae bench
     */
    private void prepareBmwDtoListByBmwEntityList( String caeBenchType, List< BmwCaeBenchDTO > bmwCaeBenchDtolist,
            List< BmwCaeBenchEntity > dbBmwCaeBench ) {
        if ( caeBenchType.contains( BmwCaeBenchEnums.CB2_VARIANT.getValue() ) ) {
            for ( BmwCaeBenchEntity bmwCaeBenchEntity : dbBmwCaeBench ) {
                bmwCaeBenchDtolist.add( prepareBmwCaeBenchVariantByEntity( bmwCaeBenchEntity ) );
            }
        } else if ( caeBenchType.contains( BmwCaeBenchEnums.CB2_SUBMODEL.getValue() ) || caeBenchType.contains(
                BmwCaeBenchEnums.CB2_OBJECT_TREE.getValue() ) || caeBenchType.contains(
                BmwCaeBenchEnums.CB2_DUMMY_TEMP_TREE.getValue() ) ) {
            for ( BmwCaeBenchEntity bmwCaeBenchEntity : dbBmwCaeBench ) {
                bmwCaeBenchDtolist.add( prepareBmwCaeBenchSubModelByEntity( bmwCaeBenchEntity ) );
            }
        } else if ( caeBenchType.contains( BmwCaeBenchEnums.CB2_INPUT_DECK.getValue() ) ) {
            for ( BmwCaeBenchEntity bmwCaeBenchEntity : dbBmwCaeBench ) {
                bmwCaeBenchDtolist.add( prepareBmwCaeBenchInputDeckByEntity( bmwCaeBenchEntity ) );
            }
        } else if ( caeBenchType.contains( BmwCaeBenchEnums.CB2_KEYRESULTS.getValue() ) ) {
            for ( BmwCaeBenchEntity bmwCaeBenchEntity : dbBmwCaeBench ) {
                bmwCaeBenchDtolist.add( prepareBmwCaeBenchKeyResultByEntity( bmwCaeBenchEntity ) );
            }
        } else if ( caeBenchType.contains( BmwCaeBenchEnums.CB2_RESULT.getValue() ) ) {
            for ( BmwCaeBenchEntity bmwCaeBenchEntity : dbBmwCaeBench ) {
                bmwCaeBenchDtolist.add( prepareBmwCaeBenchCb2ResultByEntity( bmwCaeBenchEntity ) );
            }
        } else if ( caeBenchType.contains( BmwCaeBenchEnums.CB2_REPORT.getValue() ) ) {
            for ( BmwCaeBenchEntity bmwCaeBenchEntity : dbBmwCaeBench ) {
                bmwCaeBenchDtolist.add( prepareBmwCaeBenchReportByEntity( bmwCaeBenchEntity ) );
            }
        } else if ( caeBenchType.contains( BmwCaeBenchEnums.CB2_STORY_BOARD.getValue() ) ) {
            for ( BmwCaeBenchEntity bmwCaeBenchEntity : dbBmwCaeBench ) {
                bmwCaeBenchDtolist.add( prepareBmwCaeBenchStoryBoardByEntity( bmwCaeBenchEntity ) );
            }
        } else if ( caeBenchType.contains( BmwCaeBenchEnums.CB2_SCENARIO.getValue() ) ) {
            for ( BmwCaeBenchEntity bmwCaeBenchEntity : dbBmwCaeBench ) {
                bmwCaeBenchDtolist.add( prepareBmwCaeBenchSenarioByEntity( bmwCaeBenchEntity ) );
            }
        } else if ( caeBenchType.contains( BmwCaeBenchEnums.CB2_PROJECT.getValue() ) ) {
            for ( BmwCaeBenchEntity bmwCaeBenchEntity : dbBmwCaeBench ) {
                bmwCaeBenchDtolist.add( prepareBmwCaeBenchProjectsByEntity( bmwCaeBenchEntity ) );
            }
        }
    }

    /**
     * Save or update bmw entity.
     *
     * @param entityManager
     *         the entity manager
     * @param caeBenchType
     *         the cae bench type
     * @param bmwCaeBenchEntityNode
     *         the bmw cae bench entity node
     * @param bmwCaeBenchObjectEntity
     *         the bmw cae benchbject entity
     */
    private void saveOrUpdateBmwEntity( EntityManager entityManager, String caeBenchType, BmwCaeBenchNodeEntity bmwCaeBenchEntityNode,
            BmwCaeBenchEntity bmwCaeBenchObjectEntity ) {
        bmwCaeBenchObjectEntity.setDelete( false );
        bmwCaeBenchObjectEntity.setBmwCaeDataType( caeBenchType );
        bmwCaeBenchObjectEntity.setId( UUID.randomUUID() );
        bmwCaeBenchObjectEntity.setBmwCaeBenchNodeEntity( bmwCaeBenchEntityNode );
        bmwCaeBenchDAO.saveOrUpdate( entityManager, bmwCaeBenchObjectEntity );
    }

    /**
     * Gets the prepared cb 2 object list.
     *
     * @param listFromBmwCaeBench
     *         the list from bmw cae bench
     * @param dbBmwCaeBench
     *         the db bmw cae bench
     *
     * @return the prepared cb 2 object list
     */
    private List< BmwCaeBenchEntity > getPreparedCb2ObjectList( List< BmwCaeBenchEntity > listFromBmwCaeBench,
            Map< String, UUID > dbBmwCaeBench ) {
        List< BmwCaeBenchEntity > bmwCaeBenchEntitylist = new ArrayList<>();
        if ( listFromBmwCaeBench != null && !listFromBmwCaeBench.isEmpty() ) {
            listFromBmwCaeBench.stream().forEach( cb2List -> {
                for ( Entry< String, UUID > bmwCaeBenchEntity : dbBmwCaeBench.entrySet() ) {
                    if ( cb2List.getOid().equals( bmwCaeBenchEntity.getKey() ) ) {
                        cb2List.setId( bmwCaeBenchEntity.getValue() );
                        bmwCaeBenchEntitylist.add( cb2List );
                        break;
                    }
                }
            } );
        }
        listFromBmwCaeBench = null;
        return bmwCaeBenchEntitylist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< BmwCaeBenchDTO > getSortedSelection( String sId, String external ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getSortedSelection( entityManager, sId, external );
        } finally {
            entityManager.close();
        }
    }

    private List< BmwCaeBenchDTO > getSortedSelection( EntityManager entityManager, String sId, String external ) {
        if ( null != sId && !sId.isEmpty() ) {
            return extractBmwCaeBenchObjectsFromSelectionItemList( entityManager, selectionManager.getSelectionItemDAO()
                    .getObjectListByProperty( entityManager, ConstantsDAO.SELECTION_ENTITY_ID, UUID.fromString( sId ) ), external );
        }
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< BmwCaeBenchDTO > getBmwCaeBenchSortedSelection( String sId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( null != sId && !sId.isEmpty() ) {
                return extractBmwCaeBenchDataFromSelectionItemList( entityManager, selectionManager.getSelectionItemDAO()
                        .getObjectListByProperty( entityManager, ConstantsDAO.SELECTION_ENTITY_ID, UUID.fromString( sId ) ) );
            }
            return new ArrayList<>();
        } finally {
            entityManager.close();
        }

    }

    /**
     * Extract CB 2 files from selection item list.
     *
     * @param entityManager
     *         the entity manager
     * @param objectListByProperty
     *         the object list by property
     * @param external
     *         the external
     *
     * @return the list
     */
    private List< BmwCaeBenchDTO > extractBmwCaeBenchObjectsFromSelectionItemList( EntityManager entityManager,
            List< SelectionItemEntity > objectListByProperty, String external ) {
        List< UUID > selectedIdUUID = new ArrayList<>();

        List< BmwCaeBenchDTO > BmwCaeBenchDTOXList = new ArrayList<>();

        for ( SelectionItemEntity selectionEntity : objectListByProperty ) {
            selectedIdUUID.add( UUID.fromString( selectionEntity.getItem() ) );
        }
        List< BmwCaeBenchEntity > bmwCaeBenchEntityList = bmwCaeBenchDAO.getLatestNonDeletedObjectsByListOfIds( entityManager,
                selectedIdUUID );
        for ( BmwCaeBenchEntity entity : bmwCaeBenchEntityList ) {
            if ( entity != null ) {
                BmwCaeBenchDTOXList.add( prepareBmwAllCustomDtoByEntity( external, entity ) );
            }
        }
        return BmwCaeBenchDTOXList;
    }

    /**
     * Extract bmw cae bench data from selection item list.
     *
     * @param entityManager
     *         the entity manager
     * @param objectListByProperty
     *         the object list by property
     *
     * @return the list
     */
    private List< BmwCaeBenchDTO > extractBmwCaeBenchDataFromSelectionItemList( EntityManager entityManager,
            List< SelectionItemEntity > objectListByProperty ) {
        List< UUID > selectedIdUUID = new ArrayList<>();
        List< BmwCaeBenchDTO > cb2FileDTOs = new ArrayList<>();
        for ( SelectionItemEntity selectionEntity : objectListByProperty ) {
            selectedIdUUID.add( UUID.fromString( selectionEntity.getItem() ) );
        }
        List< BmwCaeBenchEntity > cb2FileEntities = bmwCaeBenchDAO.getLatestNonDeletedObjectsByListOfIds( entityManager, selectedIdUUID );
        for ( BmwCaeBenchEntity cb2FileEntity : cb2FileEntities ) {
            if ( cb2FileEntity != null ) {
                cb2FileDTOs.add( prepareBmwCaeBenchSubModelDtoFromEntity( cb2FileEntity ) );
            }
        }
        return cb2FileDTOs;
    }

    /**
     * Prepare bmw cae bench sub model dto from entity.
     *
     * @param bmwEntity
     *         the bmw entity
     *
     * @return the bmw cae bench sub model DTO
     */
    private BmwCaeBenchSubModelDTO prepareBmwCaeBenchSubModelDtoFromEntity( BmwCaeBenchEntity bmwEntity ) {
        BmwCaeBenchSubModelDTO bmwCaeBenchSubModelDTO = new BmwCaeBenchSubModelDTO();
        bmwCaeBenchSubModelDTO.setName( bmwEntity.getName() );
        bmwCaeBenchSubModelDTO.setOid( bmwEntity.getOid() );
        bmwCaeBenchSubModelDTO.setId( bmwEntity.getId() );
        bmwCaeBenchSubModelDTO.setAssembleType( bmwEntity.getAssembleType() );
        bmwCaeBenchSubModelDTO.setCarProject( bmwEntity.getCarProject() );
        bmwCaeBenchSubModelDTO.setOwner( bmwEntity.getOwner() );
        bmwCaeBenchSubModelDTO.setCreatedAt( bmwEntity.getCreatedAt() );
        bmwCaeBenchSubModelDTO.setModelDefLabel( bmwEntity.getModelDefLabel() );
        bmwCaeBenchSubModelDTO.setDescription( bmwEntity.getDescription() );
        return bmwCaeBenchSubModelDTO;
    }

    /**
     * Prepare bmw cae bench variant by entity.
     *
     * @param bmwCaeBenchEntity
     *         the bmw cae bench entity
     *
     * @return the bmw cae bench variant DTO
     */
    public BmwCaeBenchVariantDTO prepareBmwCaeBenchVariantByEntity( BmwCaeBenchEntity bmwCaeBenchEntity ) {
        BmwCaeBenchVariantDTO bmwCaeBenchDTO = new BmwCaeBenchVariantDTO();
        bmwCaeBenchDTO.setName( bmwCaeBenchEntity.getName() );
        bmwCaeBenchDTO.setOid( bmwCaeBenchEntity.getOid() );
        bmwCaeBenchDTO.setId( bmwCaeBenchEntity.getId() );
        bmwCaeBenchDTO.setCreatedAt( bmwCaeBenchEntity.getCreatedAt() );
        bmwCaeBenchDTO.setDescription( bmwCaeBenchEntity.getDescription() );
        bmwCaeBenchDTO.setInputDecks( bmwCaeBenchEntity.getInputDecks() );
        bmwCaeBenchDTO.setPhase( bmwCaeBenchEntity.getPhase() );
        bmwCaeBenchDTO.setProject( bmwCaeBenchEntity.getProject() );
        bmwCaeBenchDTO.setReference( bmwCaeBenchEntity.getReference() );
        bmwCaeBenchDTO.setReleaseLevelLabel( bmwCaeBenchEntity.getReleaseLevelLabel() );
        bmwCaeBenchDTO.setLabel( bmwCaeBenchEntity.getLabel() );
        bmwCaeBenchDTO.setType( bmwCaeBenchEntity.getType() );
        bmwCaeBenchDTO.setDisciplineContext( bmwCaeBenchEntity.getDisciplineContext() );
        bmwCaeBenchDTO.setDerivedFrom( bmwCaeBenchEntity.getDerivedFrom() );
        bmwCaeBenchDTO.setOwner( bmwCaeBenchEntity.getOwner() );
        bmwCaeBenchDTO.setCreatedBy( bmwCaeBenchEntity.getCreatedBy() );
        return bmwCaeBenchDTO;
    }

    /**
     * Prepare bmw all custom dto by entity.
     *
     * @param externalFlag
     *         the external flag
     * @param bmwCaeBenchEntity
     *         the bmw cae bench entity
     *
     * @return the bmw cae bench DTO
     */
    private BmwCaeBenchDTO prepareBmwAllCustomDtoByEntity( String externalFlag, BmwCaeBenchEntity bmwCaeBenchEntity ) {
        if ( externalFlag.contains( BmwCaeBenchEnums.CB2_VARIANT.getValue() ) ) {
            return prepareBmwCaeBenchVariantByEntity( bmwCaeBenchEntity );
        } else if ( externalFlag.contains( BmwCaeBenchEnums.CB2_SUBMODEL.getValue() ) || externalFlag.contains(
                BmwCaeBenchEnums.CB2_OBJECT_TREE.getValue() ) || externalFlag.contains(
                BmwCaeBenchEnums.CB2_DUMMY_TEMP_TREE.getValue() ) ) {
            return prepareBmwCaeBenchSubModelByEntity( bmwCaeBenchEntity );
        } else if ( externalFlag.contains( BmwCaeBenchEnums.CB2_INPUT_DECK.getValue() ) ) {
            return prepareBmwCaeBenchInputDeckByEntity( bmwCaeBenchEntity );
        } else if ( externalFlag.contains( BmwCaeBenchEnums.CB2_KEYRESULTS.getValue() ) ) {
            return prepareBmwCaeBenchKeyResultByEntity( bmwCaeBenchEntity );
        } else if ( externalFlag.contains( BmwCaeBenchEnums.CB2_RESULT.getValue() ) ) {
            return prepareBmwCaeBenchCb2ResultByEntity( bmwCaeBenchEntity );
        } else if ( externalFlag.contains( BmwCaeBenchEnums.CB2_REPORT.getValue() ) ) {
            return prepareBmwCaeBenchReportByEntity( bmwCaeBenchEntity );
        } else if ( externalFlag.contains( BmwCaeBenchEnums.CB2_STORY_BOARD.getValue() ) ) {
            return prepareBmwCaeBenchStoryBoardByEntity( bmwCaeBenchEntity );
        } else if ( externalFlag.contains( BmwCaeBenchEnums.CB2_SCENARIO.getValue() ) ) {
            return prepareBmwCaeBenchSenarioByEntity( bmwCaeBenchEntity );
        } else if ( externalFlag.contains( BmwCaeBenchEnums.CB2_PROJECT.getValue() ) ) {
            return prepareBmwCaeBenchProjectsByEntity( bmwCaeBenchEntity );
        }
        return null;
    }

    /**
     * Prepare bmw cae bench input deck by entity.
     *
     * @param bmwCaeBenchEntity
     *         the bmw cae bench entity
     *
     * @return the bmw cae bench input deck DTO
     */
    public BmwCaeBenchInputDeckDTO prepareBmwCaeBenchInputDeckByEntity( BmwCaeBenchEntity bmwCaeBenchEntity ) {
        BmwCaeBenchInputDeckDTO bmwCaeBenchDTO = new BmwCaeBenchInputDeckDTO();
        bmwCaeBenchDTO.setName( bmwCaeBenchEntity.getName() );
        bmwCaeBenchDTO.setOid( bmwCaeBenchEntity.getOid() );
        bmwCaeBenchDTO.setId( bmwCaeBenchEntity.getId() );
        bmwCaeBenchDTO.setCreatedAt( bmwCaeBenchEntity.getCreatedAt() );
        bmwCaeBenchDTO.setDescription( bmwCaeBenchEntity.getDescription() );
        bmwCaeBenchDTO.setLabel( bmwCaeBenchEntity.getLabel() );
        bmwCaeBenchDTO.setFormat( bmwCaeBenchEntity.getFormat() );
        bmwCaeBenchDTO.setProjectPhase( bmwCaeBenchEntity.getProjectPhase() );
        bmwCaeBenchDTO.setType( bmwCaeBenchEntity.getType() );
        bmwCaeBenchDTO.setVariant( bmwCaeBenchEntity.getVariant() );
        bmwCaeBenchDTO.setSimPostProcessStatus( bmwCaeBenchEntity.getSimPostProcessStatus() );
        bmwCaeBenchDTO.setSimProcessStatus( bmwCaeBenchEntity.getSimProcessStatus() );
        bmwCaeBenchDTO.setProject( bmwCaeBenchEntity.getProject() );
        bmwCaeBenchDTO.setOwner( bmwCaeBenchEntity.getOwner() );
        bmwCaeBenchDTO.setDisciplineContext( bmwCaeBenchEntity.getDisciplineContext() );
        bmwCaeBenchDTO.setReference( bmwCaeBenchEntity.getReference() );
        bmwCaeBenchDTO.setCreatedBy( bmwCaeBenchEntity.getCreatedBy() );
        return bmwCaeBenchDTO;
    }

    /**
     * Prepare bmw cae bench cb 2 result by entity.
     *
     * @param bmwCaeBenchEntity
     *         the bmw cae bench entity
     *
     * @return the bmw cae bench result DTO
     */
    public BmwCaeBenchResultDTO prepareBmwCaeBenchCb2ResultByEntity( BmwCaeBenchEntity bmwCaeBenchEntity ) {
        BmwCaeBenchResultDTO bmwCaeBenchDTO = new BmwCaeBenchResultDTO();
        bmwCaeBenchDTO.setName( bmwCaeBenchEntity.getName() );
        bmwCaeBenchDTO.setOid( bmwCaeBenchEntity.getOid() );
        bmwCaeBenchDTO.setId( bmwCaeBenchEntity.getId() );
        bmwCaeBenchDTO.setCreatedAt( bmwCaeBenchEntity.getCreatedAt() );
        bmwCaeBenchDTO.setDescription( bmwCaeBenchEntity.getDescription() );
        bmwCaeBenchDTO.setType( bmwCaeBenchEntity.getType() );
        bmwCaeBenchDTO.setDisciplineContext( bmwCaeBenchEntity.getDisciplineContext() );
        bmwCaeBenchDTO.setOwner( bmwCaeBenchEntity.getOwner() );
        bmwCaeBenchDTO.setCreatedBy( bmwCaeBenchEntity.getCreatedBy() );
        bmwCaeBenchDTO.setProject( bmwCaeBenchEntity.getProject() );
        return bmwCaeBenchDTO;
    }

    /**
     * Prepare bmw cae bench key result by entity.
     *
     * @param bmwCaeBenchEntity
     *         the bmw cae bench entity
     *
     * @return the bmw cae bench key results DTO
     */
    public BmwCaeBenchKeyResultsDTO prepareBmwCaeBenchKeyResultByEntity( BmwCaeBenchEntity bmwCaeBenchEntity ) {
        BmwCaeBenchKeyResultsDTO bmwCaeBenchDTO = new BmwCaeBenchKeyResultsDTO();
        bmwCaeBenchDTO.setName( bmwCaeBenchEntity.getName() );
        bmwCaeBenchDTO.setOid( bmwCaeBenchEntity.getOid() );
        bmwCaeBenchDTO.setId( bmwCaeBenchEntity.getId() );
        bmwCaeBenchDTO.setCreatedAt( bmwCaeBenchEntity.getCreatedAt() );
        bmwCaeBenchDTO.setDescription( bmwCaeBenchEntity.getDescription() );
        bmwCaeBenchDTO.setReleaseLevelLabel( bmwCaeBenchEntity.getReleaseLevelLabel() );
        bmwCaeBenchDTO.setCreatedBy( bmwCaeBenchEntity.getCreatedBy() );
        bmwCaeBenchDTO.setType( bmwCaeBenchEntity.getType() );
        bmwCaeBenchDTO.setLabel( bmwCaeBenchEntity.getLabel() );
        bmwCaeBenchDTO.setOwner( bmwCaeBenchEntity.getOwner() );
        bmwCaeBenchDTO.setProject( bmwCaeBenchEntity.getProject() );
        bmwCaeBenchDTO.setInputDeck( bmwCaeBenchEntity.getInputDeck() );
        return bmwCaeBenchDTO;
    }

    /**
     * Prepare bmw cae bench sub model by entity.
     *
     * @param bmwCaeBenchEntity
     *         the bmw cae bench entity
     *
     * @return the bmw cae bench sub model DTO
     */
    private VariantSubModelDTO prepareVariantSubModelDTOByEntity( BmwCaeBenchEntity bmwCaeBenchEntity ) {
        VariantSubModelDTO bmwCaeBenchDTO = new VariantSubModelDTO();
        bmwCaeBenchDTO.setName( bmwCaeBenchEntity.getName() );
        bmwCaeBenchDTO.setOid( bmwCaeBenchEntity.getOid() );
        bmwCaeBenchDTO.setId( bmwCaeBenchEntity.getId() );
        bmwCaeBenchDTO.setAssembleType( bmwCaeBenchEntity.getAssembleType() );
        bmwCaeBenchDTO.setCarProject( bmwCaeBenchEntity.getCarProject() );
        bmwCaeBenchDTO.setOwner( bmwCaeBenchEntity.getOwner() );
        bmwCaeBenchDTO.setCreatedAt( bmwCaeBenchEntity.getCreatedAt() );
        bmwCaeBenchDTO.setVariantLabel( bmwCaeBenchEntity.getVariantLabel() );
        bmwCaeBenchDTO.setModelDefLabel( bmwCaeBenchEntity.getModelDefLabel() );
        bmwCaeBenchDTO.setDescription( bmwCaeBenchEntity.getDescription() );
        bmwCaeBenchDTO.setLabel( bmwCaeBenchEntity.getLabel() );
        bmwCaeBenchDTO.setFormat( bmwCaeBenchEntity.getFormat() );
        bmwCaeBenchDTO.setModelState( bmwCaeBenchEntity.getModelState() );
        bmwCaeBenchDTO.setCreatedBy( bmwCaeBenchEntity.getCreatedBy() );
        bmwCaeBenchDTO.setDisciplineContext( bmwCaeBenchEntity.getDisciplineContext() );
        bmwCaeBenchDTO.setType( bmwCaeBenchEntity.getType() );
        return bmwCaeBenchDTO;
    }

    /**
     * Prepare bmw cae bench sub model by entity bmw cae bench sub model dto.
     *
     * @param bmwCaeBenchEntity
     *         the bmw cae bench entity
     *
     * @return the bmw cae bench sub model dto
     */
    private BmwCaeBenchSubModelDTO prepareBmwCaeBenchSubModelByEntity( BmwCaeBenchEntity bmwCaeBenchEntity ) {
        BmwCaeBenchSubModelDTO bmwCaeBenchDTO = new BmwCaeBenchSubModelDTO();
        bmwCaeBenchDTO.setName( bmwCaeBenchEntity.getName() );
        bmwCaeBenchDTO.setOid( bmwCaeBenchEntity.getOid() );
        bmwCaeBenchDTO.setId( bmwCaeBenchEntity.getId() );
        bmwCaeBenchDTO.setAssembleType( bmwCaeBenchEntity.getAssembleType() );
        bmwCaeBenchDTO.setCarProject( bmwCaeBenchEntity.getCarProject() );
        bmwCaeBenchDTO.setOwner( bmwCaeBenchEntity.getOwner() );
        bmwCaeBenchDTO.setCreatedAt( bmwCaeBenchEntity.getCreatedAt() );
        bmwCaeBenchDTO.setVariantLabel( bmwCaeBenchEntity.getVariantLabel() );
        bmwCaeBenchDTO.setModelDefLabel( bmwCaeBenchEntity.getModelDefLabel() );
        bmwCaeBenchDTO.setDescription( bmwCaeBenchEntity.getDescription() );
        bmwCaeBenchDTO.setLabel( bmwCaeBenchEntity.getLabel() );
        bmwCaeBenchDTO.setFormat( bmwCaeBenchEntity.getFormat() );
        bmwCaeBenchDTO.setModelState( bmwCaeBenchEntity.getModelState() );
        bmwCaeBenchDTO.setCreatedBy( bmwCaeBenchEntity.getCreatedBy() );
        bmwCaeBenchDTO.setDisciplineContext( bmwCaeBenchEntity.getDisciplineContext() );
        bmwCaeBenchDTO.setType( bmwCaeBenchEntity.getType() );
        return bmwCaeBenchDTO;
    }

    /**
     * Prepare bmw cae bench report by entity.
     *
     * @param bmwCaeBenchEntity
     *         the bmw cae bench entity
     *
     * @return the bmw cae bench report DTO
     */
    public BmwCaeBenchReportDTO prepareBmwCaeBenchReportByEntity( BmwCaeBenchEntity bmwCaeBenchEntity ) {
        BmwCaeBenchReportDTO bmwCaeBenchDTO = new BmwCaeBenchReportDTO();
        bmwCaeBenchDTO.setName( bmwCaeBenchEntity.getName() );
        bmwCaeBenchDTO.setOid( bmwCaeBenchEntity.getOid() );
        bmwCaeBenchDTO.setId( bmwCaeBenchEntity.getId() );
        bmwCaeBenchDTO.setCreatedAt( bmwCaeBenchEntity.getCreatedAt() );
        bmwCaeBenchDTO.setDescription( bmwCaeBenchEntity.getDescription() );
        bmwCaeBenchDTO.setCreatedBy( bmwCaeBenchEntity.getCreatedBy() );
        bmwCaeBenchDTO.setType( bmwCaeBenchEntity.getType() );
        bmwCaeBenchDTO.setOwner( bmwCaeBenchEntity.getOwner() );
        bmwCaeBenchDTO.setProject( bmwCaeBenchEntity.getProject() );
        bmwCaeBenchDTO.setDisciplineContext( bmwCaeBenchEntity.getDisciplineContext() );
        bmwCaeBenchDTO.setSeedType( bmwCaeBenchEntity.getSeeds() );
        return bmwCaeBenchDTO;
    }

    /**
     * Prepare bmw cae bench story board by entity.
     *
     * @param bmwCaeBenchEntity
     *         the bmw cae bench entity
     *
     * @return the bmw cae bench story board DTO
     */
    public BmwCaeBenchStoryBoardDTO prepareBmwCaeBenchStoryBoardByEntity( BmwCaeBenchEntity bmwCaeBenchEntity ) {
        BmwCaeBenchStoryBoardDTO bmwCaeBenchDTO = new BmwCaeBenchStoryBoardDTO();
        bmwCaeBenchDTO.setName( bmwCaeBenchEntity.getName() );
        bmwCaeBenchDTO.setOid( bmwCaeBenchEntity.getOid() );
        bmwCaeBenchDTO.setId( bmwCaeBenchEntity.getId() );
        bmwCaeBenchDTO.setCreatedAt( bmwCaeBenchEntity.getCreatedAt() );
        bmwCaeBenchDTO.setDescription( bmwCaeBenchEntity.getDescription() );
        bmwCaeBenchDTO.setCreatedBy( bmwCaeBenchEntity.getCreatedBy() );
        bmwCaeBenchDTO.setType( bmwCaeBenchEntity.getType() );
        bmwCaeBenchDTO.setOwner( bmwCaeBenchEntity.getOwner() );
        bmwCaeBenchDTO.setProject( bmwCaeBenchEntity.getProject() );
        bmwCaeBenchDTO.setDisciplineContext( bmwCaeBenchEntity.getDisciplineContext() );
        return bmwCaeBenchDTO;
    }

    /**
     * Prepare bmw cae bench senario by entity bmw cae bench senario dto.
     *
     * @param bmwCaeBenchEntity
     *         the bmw cae bench entity
     *
     * @return the bmw cae bench senario dto
     */
    private BmwCaeBenchSenarioDTO prepareBmwCaeBenchSenarioByEntity( BmwCaeBenchEntity bmwCaeBenchEntity ) {
        BmwCaeBenchSenarioDTO bmwCaeBenchDTO = new BmwCaeBenchSenarioDTO();
        bmwCaeBenchDTO.setName( bmwCaeBenchEntity.getName() );
        bmwCaeBenchDTO.setLabel( bmwCaeBenchEntity.getLabel() );
        bmwCaeBenchDTO.setOid( bmwCaeBenchEntity.getOid() );
        bmwCaeBenchDTO.setId( bmwCaeBenchEntity.getId() );
        bmwCaeBenchDTO.setCreatedAt( bmwCaeBenchEntity.getCreatedAt() );
        bmwCaeBenchDTO.setDescription( bmwCaeBenchEntity.getDescription() );
        bmwCaeBenchDTO.setDisciplineContext( bmwCaeBenchEntity.getDisciplineContext() );
        bmwCaeBenchDTO.setSimulationType( bmwCaeBenchEntity.getSimulationType() );
        bmwCaeBenchDTO.setType( bmwCaeBenchEntity.getType() );
        return bmwCaeBenchDTO;
    }

    /**
     * Prepare bmw cae bench projects by entity bmw cae bench projects dto.
     *
     * @param bmwCaeBenchEntity
     *         the bmw cae bench entity
     *
     * @return the bmw cae bench projects dto
     */
    private BmwCaeBenchProjectsDTO prepareBmwCaeBenchProjectsByEntity( BmwCaeBenchEntity bmwCaeBenchEntity ) {
        BmwCaeBenchProjectsDTO bmwCaeBenchDTO = new BmwCaeBenchProjectsDTO();
        bmwCaeBenchDTO.setName( bmwCaeBenchEntity.getName() );
        bmwCaeBenchDTO.setOid( bmwCaeBenchEntity.getOid() );
        bmwCaeBenchDTO.setId( bmwCaeBenchEntity.getId() );
        bmwCaeBenchDTO.setCreatedAt( bmwCaeBenchEntity.getCreatedAt() );
        bmwCaeBenchDTO.setDescription( bmwCaeBenchEntity.getDescription() );
        bmwCaeBenchDTO.setType( bmwCaeBenchEntity.getType() );
        bmwCaeBenchDTO.setShortName( bmwCaeBenchEntity.getShortName() );
        return bmwCaeBenchDTO;
    }

    /**
     * Checks if is wf exists and non deleted.
     *
     * @param entityManager
     *         the entity manager
     * @param wfId
     *         the wf id
     *
     * @return true, if is wf exists and non deleted
     */
    private boolean isWfExistsAndNonDeleted( EntityManager entityManager, String wfId ) {
        try {
            SuSEntity susWfEntity = susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( wfId ) );
            if ( susWfEntity != null ) {
                return true;
            }
        } catch ( Exception e ) {
            return false;
        }
        return false;
    }

    /**
     * Check and set job status failed.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param masterJobId
     *         the master job id
     * @param errorMsg
     *         the error msg
     *
     * @return true, if successful
     */
    private boolean checkAndSetJobStatusFailed( EntityManager entityManager, UUID userId, UUID masterJobId, String errorMsg ) {
        Job job = jobManager.getJob( entityManager, userId, masterJobId.toString() );
        List< LogRecord > jobLog = new ArrayList<>();
        if ( null != job.getLog() ) {
            jobLog = job.getLog();
        }
        jobLog.add( new LogRecord( ConstantsMessageTypes.ERROR, errorMsg, new Date() ) );
        job.setLog( jobLog );
        job.setStatus( new Status( WorkflowStatus.FAILED ) );
        jobManager.updateJob( entityManager, job );
        return false;
    }

    /**
     * Update job with status.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param jobId
     *         the job id
     * @param jobStatus
     *         the job status
     *
     * @return true, if successful
     */
    private boolean updateJobWithStatus( EntityManager entityManager, UUID userId, UUID jobId, Status jobStatus ) {
        Job job = jobManager.getJob( entityManager, userId, jobId.toString() );
        List< LogRecord > jobLog = new ArrayList<>();
        jobLog.add( new LogRecord( ConstantsMessageTypes.INFO, "job Completed", new Date() ) );
        job.setLog( jobLog );
        job.setStatus( new Status( WorkflowStatus.COMPLETED ) );
        jobManager.updateJob( entityManager, job );
        return true;
    }

    /**
     * Execution host drop down.
     *
     * @param hostId
     *         the host id
     *
     * @return the select UI
     */
    private SelectFormItem executionHostDropDown( String hostId ) {
        if ( PropertiesManager.isHostEnable() ) {
            List< SelectOptionsUI > executionHostList = new ArrayList<>();
            SelectOptionsUI defaultLocationItem = new SelectOptionsUI();
            defaultLocationItem.setId( ( LocationsEnum.DEFAULT_LOCATION.getId() ) );
            defaultLocationItem.setName( LocationsEnum.DEFAULT_LOCATION.getName() );
            executionHostList.add( defaultLocationItem );
            Hosts hosts = PropertiesManager.getHosts();
            if ( hosts != null && CollectionUtils.isNotEmpty( hosts.getExcutionHosts() ) ) {
                for ( ExecutionHosts executionHosts : hosts.getExcutionHosts() ) {
                    SelectOptionsUI hostItem = new SelectOptionsUI();
                    hostItem.setId( ( executionHosts.getId().toString() ) );
                    hostItem.setName( executionHosts.getName() );
                    executionHostList.add( hostItem );
                }
            }
            SelectFormItem typeItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            typeItem.setOptions( executionHostList );
            typeItem.setCanBeEmpty( false );
            typeItem.setDuplicate( false );
            typeItem.setLabel( "Job runs on" );
            typeItem.setName( "execution.host" );
            typeItem.setType( "select" );
            if ( hostId != null ) {
                typeItem.setValue( hostId );
            }
            Map< String, Object > rules = new HashMap<>();
            Map< String, Object > message = new HashMap<>();
            rules.put( REQUIRED, true );
            message.put( REQUIRED, MUST_CHOSE_OPTION );
            typeItem.setRules( rules );
            typeItem.setMessages( message );
            return typeItem;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO updateSelectionView( String userId, String selectionId, List< TableColumn > updateColumns ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ObjectViewDTO objectViewDTO = new ObjectViewDTO();
            List< TableColumn > viewColumnsList = updateColumns.stream().sorted( Comparator.comparing( TableColumn::getReorder ) ).toList();
            List< FilterColumn > filterColumns = new ArrayList<>();
            for ( TableColumn viewColumn : viewColumnsList ) {
                FilterColumn filterColumn = new FilterColumn();
                filterColumn.setName( viewColumn.getName() );
                filterColumn.setVisible( viewColumn.isVisible() );
                filterColumn.setReorder( viewColumn.getReorder() );
                filterColumns.add( filterColumn );
            }
            FiltersDTO viewFilters = new FiltersDTO();
            viewFilters.setColumns( filterColumns );
            List< ObjectViewDTO > views = objectViewManager.getAllObjectViewsByKey( entityManager, selectionId );

            if ( views != null && !views.isEmpty() ) {
                objectViewDTO = views.get( 0 ); // only one view is saved for this table
            }
            objectViewDTO.setDefaultView( true );
            objectViewDTO.setName( selectionId );
            objectViewDTO.setObjectViewKey( selectionId );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( viewFilters ) );
            return objectViewManager.saveOrUpdateObjectView( entityManager, objectViewDTO, userId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > applyViewToSelectionColumns( String selectionId, List< TableColumn > listColumns ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< ObjectViewDTO > views = objectViewManager.getAllObjectViewsByKey( entityManager, selectionId );
            if ( views != null && !views.isEmpty() ) {
                ObjectViewDTO objectViewDTO = views.get( 0 ); // only one view is saved for this table
                FiltersDTO viewFilters = JsonUtils.jsonToObject( objectViewDTO.getObjectViewJson(), FiltersDTO.class );
                List< FilterColumn > viewColumnsList = viewFilters.getColumns();
                for ( TableColumn tableColumn : listColumns ) {
                    for ( FilterColumn viewColumn : viewColumnsList ) {
                        if ( tableColumn.getName().equals( viewColumn.getName() ) ) {
                            tableColumn.setVisible( viewColumn.isVisible() );
                            tableColumn.setOrderNum( viewColumn.getReorder() );
                            tableColumn.setReorder( viewColumn.getReorder() );
                        }
                    }
                }
                return listColumns.stream().sorted( Comparator.comparing( TableColumn::getOrderNum ) ).toList();
            } else {
                return listColumns;
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getDummyRefrenceCb2OptionForm( String variantId ) {
        List< UIFormItem > parserForm = new ArrayList<>();
        parserForm.add( prepareObjectHiddenForm( variantId ) );
        parserForm.add( prepareSolverTypeOptions( variantId ) );
        return GUIUtils.createFormFromItems( parserForm );
    }

    /**
     * Prepare drop down list by col name map.
     *
     * @param jsonQueryResponseObj
     *         the json query response obj
     * @param colName
     *         the col name
     *
     * @return the map
     */
    private Map< String, String > prepareDropDownListByColName( JSONObject jsonQueryResponseObj, String colName ) {
        Map< String, String > payloadList = new HashMap<>();
        if ( jsonQueryResponseObj.has( "objects" ) ) {
            JSONArray objects = jsonQueryResponseObj.getJSONArray( "objects" );
            for ( int i = 0; i < objects.length(); i++ ) {
                JSONObject subObjects = objects.getJSONObject( i );
                String lableValue = "";
                if ( subObjects.has( "attrs" ) ) {
                    JSONObject attrs = subObjects.getJSONObject( "attrs" );
                    if ( attrs.has( colName ) ) {
                        JSONObject lable = attrs.getJSONObject( colName );
                        if ( lable.has( "text" ) ) {
                            lableValue = lable.getString( "text" );
                        }
                    }
                }
                if ( subObjects.has( "oid" ) ) {
                    payloadList.put( subObjects.getString( "oid" ), lableValue );
                }
            }
        }

        return payloadList;
    }

    /**
     * Prepare drop down list by col id map.
     *
     * @param jsonQueryResponseObj
     *         the json query response obj
     * @param colName
     *         the col name
     *
     * @return the map
     */
    private Map< String, String > prepareDropDownListByColId( JSONObject jsonQueryResponseObj, String colName ) {
        Map< String, String > payloadList = new HashMap<>();

        if ( jsonQueryResponseObj.has( "objects" ) ) {
            JSONArray objects = jsonQueryResponseObj.getJSONArray( "objects" );
            for ( int i = 0; i < objects.length(); i++ ) {
                JSONObject subObjects = objects.getJSONObject( i );
                // Map<String, String> payloadMap = new HashMap<String, String>();
                if ( subObjects.has( "oid" ) ) {
                    String lableValue = "";
                    if ( subObjects.has( "attrs" ) ) {
                        JSONObject attrs = subObjects.getJSONObject( "attrs" );
                        if ( attrs.has( colName ) ) {
                            JSONObject lable = attrs.getJSONObject( colName );

                            if ( lable.has( "text" ) ) {
                                lableValue = lable.getString( "text" );
                            }
                        }
                    }
                    payloadList.put( subObjects.getString( "oid" ), lableValue );
                }
            }
        }

        return payloadList;
    }

    /**
     * Prepare drop down def list by col id map.
     *
     * @param jsonQueryResponseObj
     *         the json query response obj
     * @param colName
     *         the col name
     * @param colId
     *         the col id
     *
     * @return the map
     */
    private Map< String, String > prepareDropDownDefListByColId( JSONObject jsonQueryResponseObj, String colName, String colId ) {
        Map< String, String > payloadList = new HashMap<>();

        if ( jsonQueryResponseObj.has( "objects" ) ) {
            JSONArray objects = jsonQueryResponseObj.getJSONArray( "objects" );
            for ( int i = 0; i < objects.length(); i++ ) {
                JSONObject subObjects = objects.getJSONObject( i );
                // Map<String, String> payloadMap = new HashMap<String, String>();
                if ( subObjects.has( "oid" ) && subObjects.get( "oid" ).toString().equalsIgnoreCase( colId ) ) {
                    String lableValue = "";
                    if ( subObjects.has( "attrs" ) ) {
                        JSONObject attrs = subObjects.getJSONObject( "attrs" );
                        if ( attrs.has( colName ) ) {
                            JSONObject lable = attrs.getJSONObject( colName );
                            if ( lable.has( "text" ) ) {
                                payloadList.put( lable.getString( "oid" ), lable.getString( "text" ) );
                            }
                        }
                    }
                }
            }
        }
        return payloadList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getCb2ParamsBySetup( String userIdFromGeneralHeader, String userName, String assembleSolvePost, String dynamicValKeyName,
            String setupOid ) {
        List< String > options = new ArrayList<>();
        JSONObject setupOidObject = new JSONObject( "{\"objects\":[{\"oid\":\"" + setupOid + "\"}]}" );
        Map< String, Map< String, String > > cb2solveForm = cb2DummyManager.downloadPimByOidAndReadPim( userName, setupOidObject );
        Map< String, String > postprocversionMap = cb2solveForm.get( dynamicValKeyName );
        if ( null == postprocversionMap ) {
            // dyna = ncpu, abaqus = numberOfCpus
            if ( dynamicValKeyName.equals( "ncpu" ) ) {
                postprocversionMap = cb2solveForm.get( "numberOfCpus" );
            }
            // dyna = postversions, abaqus = postprocversion
            if ( dynamicValKeyName.equals( "postversions" ) ) {
                postprocversionMap = cb2solveForm.get( "postprocversion" );
            }
        }
        // log.info( "CB2 postprocversionMap: " + postprocversionMap );
        if ( null != postprocversionMap && !postprocversionMap.isEmpty() ) {
            String[] validValuesOption = postprocversionMap.get( "validValues" ).split( ";" );
            for ( String option : validValuesOption ) {
                options.add( option );
            }
        }

        return options;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Map< String, String > > getCb2StudyDefsByVariant( String userIdFromGeneralHeader, String userName, String variantSId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< Map< String, String > > options = new ArrayList<>();
            try {
                UUID.fromString( variantSId );
            } catch ( IllegalArgumentException exception ) {
                return options;
            }
            List< BmwCaeBenchDTO > variants = getBmwCaeBenchObjetBySelection( entityManager, variantSId );
            if ( variants.size() == 0 ) {
                return options;
            }
            options = cb2DummyManager.getCb2StudyDefsByVariant( userName, variants.get( 0 ).getOid() );
            return options;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Map< String, String > > getCb2SimDefsByScenario( String userIdFromGeneralHeader, String userName,
            String variantAndScenarioOid ) {
        List< Map< String, String > > options;
        log.info( "variantAndScenarioOid" + variantAndScenarioOid );

        options = cb2DummyManager.getCb2SimDefsByScenario( userName, variantAndScenarioOid );

        return options;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Map< String, String > > getCb2ScenariosByVariant( String userIdFromGeneralHeader, String userName, String variantSId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< Map< String, String > > options = new ArrayList<>();
            try {
                UUID.fromString( variantSId );
            } catch ( IllegalArgumentException exception ) {
                return options;
            }
            List< BmwCaeBenchDTO > variants = getBmwCaeBenchObjetBySelection( entityManager, variantSId );
            if ( variants.size() == 0 ) {
                return options;
            }
            options = cb2DummyManager.getCb2ScenariosByVariant( userName, variants.get( 0 ).getOid() );
            return options;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Map< String, String > > getCb2SetupByStudyDef( String userIdFromGeneralHeader, String userName, String assembleSolvePost,
            String studydefOid ) {
        List< Map< String, String > > options = new ArrayList<>();
        options = cb2DummyManager.getCb2SetupByStudyDef( userName, assembleSolvePost, studydefOid );
        return options;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< ? > getGenericDTOListBySelection( String userId, String selectionId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return selectionManager.getGenericDTOList( entityManager, userId, selectionId, filter );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkIfLicenseFeatureAllowedToUser( String licenseFeature ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return licenseManager.checkIfFeatureAllowedToUser( entityManager, licenseFeature );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UUID > getSelectedIdsListBySelectionId( String sId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return selectionManager.getSelectedIdsListBySelectionId( entityManager, sId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VariantWizardEntity getVariantWizardEntityById( UUID id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return wizDAO.findById( entityManager, id );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Sets the tree manager.
     *
     * @param treeManager
     *         the new tree manager
     */
    public void setTreeManager( SuSObjectTreeManager treeManager ) {
        this.treeManager = treeManager;
    }

    /**
     * Sets the sus DAO.
     *
     * @param susDAO
     *         the new sus DAO
     */
    public void setSusDAO( SuSGenericObjectDAO< SuSEntity > susDAO ) {
        this.susDAO = susDAO;
    }

    /**
     * Sets the wiz DAO.
     *
     * @param wizDAO
     *         the new wiz DAO
     */
    public void setWizDAO( WizDAO wizDAO ) {
        this.wizDAO = wizDAO;
    }

    /**
     * Sets the cb 2 dummy manager.
     *
     * @param cb2DummyManager
     *         the new cb 2 dummy manager
     */
    public void setCb2DummyManager( Cb2DummyManager cb2DummyManager ) {
        this.cb2DummyManager = cb2DummyManager;
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
     * Sets the loadcase manager.
     *
     * @param loadcaseManager
     *         the new loadcase manager
     */
    public void setLoadcaseManager( LoadcaseManager loadcaseManager ) {
        this.loadcaseManager = loadcaseManager;
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
     * Sets the permission manager.
     *
     * @param permissionManager
     *         the new permission manager
     */
    public void setPermissionManager( PermissionManager permissionManager ) {
        this.permissionManager = permissionManager;
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
     * Sets the job manager.
     *
     * @param jobManager
     *         the new job manager
     */
    public void setJobManager( JobManager jobManager ) {
        this.jobManager = jobManager;
    }

    /**
     * Sets the dummy type DAO.
     *
     * @param dummyTypeDAO
     *         the new dummy type DAO
     */
    public void setDummyTypeDAO( DummyTypeDAO dummyTypeDAO ) {
        this.dummyTypeDAO = dummyTypeDAO;
    }

    /**
     * Sets the relation DAO.
     *
     * @param relationDAO
     *         the new relation DAO
     */
    public void setRelationDAO( ContextMenuRelationDAO relationDAO ) {
        this.relationDAO = relationDAO;
    }

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.wizards.manager.WizardsManager#
     * getObjectViewManager()
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
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.wizards.manager.WizardsManager#
     * setObjectViewManager(de.soco.software.simuspace.suscore.data.manager.base
     * .ObjectViewManager)
     */
    public void setObjectViewManager( ObjectViewManager objectViewManager ) {
        this.objectViewManager = objectViewManager;
    }

    /**
     * Sets the submit loadcase DAO.
     *
     * @param submitLoadcaseDAO
     *         the new submit loadcase DAO
     */
    public void setSubmitLoadcaseDAO( SubmitLoadcaseDAO submitLoadcaseDAO ) {
        this.submitLoadcaseDAO = submitLoadcaseDAO;
    }

    /**
     * Sets the bmw cae bench DAO.
     *
     * @param bmwCaeBenchDAO
     *         the new bmw cae bench DAO
     */
    public void setBmwCaeBenchDAO( BmwCaeBenchDAO bmwCaeBenchDAO ) {
        this.bmwCaeBenchDAO = bmwCaeBenchDAO;
    }

    /**
     * Sets the bmw cae bench node DAO.
     *
     * @param bmwCaeBenchNodeDAO
     *         the new bmw cae bench node DAO
     */
    public void setBmwCaeBenchNodeDAO( BmwCaeBenchNodeDAO bmwCaeBenchNodeDAO ) {
        this.bmwCaeBenchNodeDAO = bmwCaeBenchNodeDAO;
    }

    /**
     * Sets the auth manager.
     *
     * @param authManager
     *         the new auth manager
     */
    public void setAuthManager( AuthManager authManager ) {
        this.authManager = authManager;
    }

    /**
     * Sets the license manager.
     *
     * @param licenseManager
     *         the new license manager
     */
    public void setLicenseManager( LicenseManager licenseManager ) {
        this.licenseManager = licenseManager;
    }

    /**
     * Sets the load case DAO.
     *
     * @param loadCaseDAO
     *         the new load case DAO
     */
    public void setLoadCaseDAO( LoadCaseDAO loadCaseDAO ) {
        this.loadCaseDAO = loadCaseDAO;
    }

    /**
     * Sets the link dao.
     *
     * @param linkDao
     *         the new link dao
     */
    public void setLinkDao( LinkDAO linkDao ) {
        this.linkDao = linkDao;
    }

    /**
     * Sets the user common manager.
     *
     * @param userCommonManager
     *         the userCommonManager to set
     */
    public void setUserCommonManager( UserCommonManager userCommonManager ) {
        this.userCommonManager = userCommonManager;
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

    public void setDataObjectManager( DataObjectManager dataObjectManager ) {
        this.dataObjectManager = dataObjectManager;
    }

    /**
     * Gets token manager.
     *
     * @return the token manager
     */
    public UserTokenManager getTokenManager() {
        return tokenManager;
    }

    /**
     * Sets token manager.
     *
     * @param tokenManager
     *         the token manager
     */
    public void setTokenManager( UserTokenManager tokenManager ) {
        this.tokenManager = tokenManager;
    }

}
