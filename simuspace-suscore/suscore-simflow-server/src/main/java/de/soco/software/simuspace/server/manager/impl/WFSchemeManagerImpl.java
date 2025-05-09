/*
 *
 */

package de.soco.software.simuspace.server.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.dao.DesignVariableLabelDAO;
import de.soco.software.simuspace.server.dao.RegexDAO;
import de.soco.software.simuspace.server.dao.SchemeSchemaDAO;
import de.soco.software.simuspace.server.dao.VariableDAO;
import de.soco.software.simuspace.server.dao.WFSchemeDAO;
import de.soco.software.simuspace.server.dao.WorkflowDAO;
import de.soco.software.simuspace.server.manager.ParserManager;
import de.soco.software.simuspace.server.manager.TemplateManager;
import de.soco.software.simuspace.server.manager.WFSchemeManager;
import de.soco.software.simuspace.server.manager.WorkflowManager;
import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMode;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.AlgoTypeEnum;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.SuSFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.ElementKeys;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTemplates;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.SchemeCategoryEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.EncryptionDecryptionDTO;
import de.soco.software.simuspace.suscore.common.model.MatchedLine;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.model.ScanFileDTO;
import de.soco.software.simuspace.suscore.common.model.ScanResponseDTO;
import de.soco.software.simuspace.suscore.common.model.SchemeCategoryDTO;
import de.soco.software.simuspace.suscore.common.model.SchemeOptionSchemaDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.properties.ScanObjectDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.Renderer;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.SubTabsUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.EncryptAndDecryptUtils;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.PythonUtils;
import de.soco.software.simuspace.suscore.common.util.RegexUtil;
import de.soco.software.simuspace.suscore.common.util.ScanObjectUtil;
import de.soco.software.simuspace.suscore.common.util.SuSVaultUtils;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.ParserVariableDTO;
import de.soco.software.simuspace.suscore.data.entity.DesignVariableEntity;
import de.soco.software.simuspace.suscore.data.entity.DesignVariableLabelEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.EncryptionDecryptionEntity;
import de.soco.software.simuspace.suscore.data.entity.ObjectiveVariableEntity;
import de.soco.software.simuspace.suscore.data.entity.ParserEntity;
import de.soco.software.simuspace.suscore.data.entity.RegexEntity;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.SchemeSchemaEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.TemplateEntity;
import de.soco.software.simuspace.suscore.data.entity.VariableEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.entity.WFSchemeEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.AlgoSchemeDTO;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.DefaultDesignVariableDTO;
import de.soco.software.simuspace.suscore.data.model.DesignVariableDTO;
import de.soco.software.simuspace.suscore.data.model.DesignVariableLabelDTO;
import de.soco.software.simuspace.suscore.data.model.LEVELDesignVariableDTO;
import de.soco.software.simuspace.suscore.data.model.LabelDTO;
import de.soco.software.simuspace.suscore.data.model.MMDesignVariableDTO;
import de.soco.software.simuspace.suscore.data.model.MMLDesignVariableDTO;
import de.soco.software.simuspace.suscore.data.model.MMSDesignVariableDTO;
import de.soco.software.simuspace.suscore.data.model.ObjectiveVariableDTO;
import de.soco.software.simuspace.suscore.data.model.SSIDesignVariableDTO;
import de.soco.software.simuspace.suscore.data.model.VALDesignVariableDTO;
import de.soco.software.simuspace.suscore.data.model.WFSchemeDTO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.document.dao.DocumentDAO;
import de.soco.software.simuspace.suscore.document.manager.DocumentManager;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.permissions.model.ResourceAccessControlDTO;
import de.soco.software.simuspace.suscore.user.manager.UserManager;
import de.soco.software.simuspace.workflow.dto.WorkflowDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowDefinitionDTO;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.UserWorkFlow;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.JobParametersImpl;
import de.soco.software.simuspace.workflow.model.impl.UserWorkflowImpl;
import de.soco.software.simuspace.workflow.util.VariableUtil;
import de.soco.software.simuspace.workflow.util.WorkflowDefinitionUtil;

/**
 * The Class WFSchemeManagerImpl.
 */
@Log4j2
public class WFSchemeManagerImpl implements WFSchemeManager {

    /**
     * The sus DAO.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The user manager.
     */
    private UserManager userManager;

    /**
     * The document manager.
     */
    private DocumentManager documentManager;

    /**
     * The selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The context menu manager.
     */
    private ContextMenuManager contextMenuManager;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The design variable dao.
     */
    private VariableDAO variableDAO;

    /**
     * The workflow manager.
     */
    private WorkflowManager workflowManager;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The scheme schema DAO.
     */
    private SchemeSchemaDAO schemeSchemaDAO;

    /**
     * The parser manager.
     */
    private ParserManager parserManager;

    /**
     * The workflow dao.
     */
    private WorkflowDAO workflowDao;

    /**
     * The regex DAO.
     */
    private RegexDAO regexDAO;

    /**
     * The WorkFlow scheme DAO.
     */
    private WFSchemeDAO wFSchemeDAO;

    /**
     * The design variable label DAO.
     */
    private DesignVariableLabelDAO designVariableLabelDAO;

    /**
     * The template manager.
     */
    private TemplateManager templateManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Document dao.
     */
    private DocumentDAO documentDAO;

    /**
     * The Constant WFSCHEME.
     */
    private static final String WFSCHEME = "WFScheme";

    /**
     * The Constant OBJECT.
     */
    private static final String OBJECT = "Object";

    /**
     * The Constant SELECT_CATEGORY_LABEL.
     */
    private static final String SELECT_CATEGORY_LABEL = "Select Category";

    /**
     * The Constant SELECT_SCHEME_LABEL.
     */
    private static final String SELECT_SCHEME_LABEL = "Select Scheme";

    /**
     * The Constant SCHEME_LABEL.
     */
    private static final String SCHEME_LABEL = "scheme";

    /**
     * The Constant CATEGORY.
     */
    private static final String CATEGORY = "category";

    /**
     * The Constant FIELD_TYPE_SELECT.
     */
    public static final String FIELD_TYPE_SELECT = "select";

    /**
     * The Constant PARAM_WORKFLOW_ID.
     */
    public static final String PARAM_WORKFLOW_ID = "{workflowId}";

    /**
     * The Constant BIND_FROM_CATAGORY_URL_FOR_RUN_SCHEME_UI_FORM.
     */
    public static final String BIND_FROM_CATAGORY_URL_FOR_RUN_SCHEME_UI_FORM = "/config/workflowscheme/ui/create/{workflowId}/category/{__value__}";

    /**
     * The Constant BIND_FROM_SCHEME_URL_FOR_RUN_SCHEME_UI_FORM.
     */
    public static final String BIND_FROM_SCHEME_URL_FOR_RUN_SCHEME_UI_FORM = "/config/workflowscheme/ui/create/{workflowId}/scheme/{__value__}";

    /**
     * The Constant TAB_KEY_GENERAL.
     */
    private static final String TAB_KEY_GENERAL = "General";

    /**
     * The Constant TAB_KEY_SCHEME_OPTIONS.
     */
    private static final String TAB_KEY_SCHEME_OPTIONS = "Scheme Options";

    /**
     * The Constant TAB_KEY_DESIGN_VARIABLES.
     */
    private static final String TAB_KEY_DESIGN_VARIABLES = "Design Variables";

    /**
     * The Constant TAB_KEY_DESIGN_SUMMARY.
     */
    private static final String TAB_KEY_DESIGN_SUMMARY = "Design Summary";

    /**
     * The Constant TAB_KEY_OBJECT_VARIABLES.
     */
    private static final String TAB_KEY_OBJECT_VARIABLES = "Object variables";

    /**
     * The Constant TAB_KEY_POST_PROCESS.
     */
    private static final String TAB_KEY_POST_PROCESS = "Post Process";

    /**
     * The Constant REQUIRED.
     */
    private static final String REQUIRED = "required";

    /**
     * The Constant MUST_CHOSE_OPTION.
     */
    private static final String MUST_CHOSE_OPTION = "Must Chose Option";

    /**
     * The Constant REGEX.
     */
    public static final String REGEX = "^(.*)([a-z]*)$";

    /**
     * The Constant FLAG_I.
     */
    private static final String FLAG_I = "i";

    /**
     * The Constant OBJECT_PLUGIN_NAME.
     */
    private static final String OBJECT_PLUGIN_NAME = "plugin_object";

    /**
     * The Constant FILE_NOT_EXISTS_IN_VAULT.
     */
    private static final String FILE_NOT_EXISTS_IN_VAULT = "WFScheme File does not exist in vault.";

    /**
     * The Constant OPTIONS.
     */
    private static final String OPTIONS = "options";

    /**
     * The Constant EXP_NUM.
     */
    private static final String EXP_NUM = "expNum";

    /**
     * The Constant ID.
     */
    private static final String ID = "id";

    /**
     * The Constant OUTPUT_JSON.
     */
    private static final String OUTPUT_JSON = "/output.json";

    /**
     * The Constant MIN_FIELD_NAME.
     */
    private static final String MIN_FIELD_NAME = "min";

    /**
     * The Constant MAX_FIELD_NAME.
     */
    private static final String MAX_FIELD_NAME = "max";

    /**
     * The Constant STEP_FIELD_NAME.
     */
    private static final String STEP_FIELD_NAME = "step";

    /**
     * The Constant VALUES_FIELD_NAME.
     */
    private static final String VALUES_FIELD_NAME = "values";

    /**
     * The Constant LEVELS_FIELD_NAME.
     */
    private static final String LEVELS_FIELD_NAME = "levels";

    /**
     * The Constant OPTIONAL_FIELD_NAME.
     */
    private static final String OPTIONAL_FIELD_NAME = "optional";

    /**
     * The Constant LIVESEARCH.
     */
    private static final String LIVESEARCH = "liveSearch";

    /**
     * The Constant ELEMENT.
     */
    private static final String ELEMENT = "elements";

    /**
     * The Constant COMMAND.
     */
    private static final String COMMAND = "command";

    /**
     * The Constant VALUE.
     */
    private static final String VALUE = "value";

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getWFSchemeUI( String userIdFromGeneralHeader ) {
        return GUIUtils.listColumns( WFSchemeDTO.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobParameters runWfScheme( String userIdFromGeneralHeader, String uid, String tokenFromGeneralHeader, String workflowId,
            String objectFilterJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            validateWFRunSchemeByWorkflowId( entityManager, userIdFromGeneralHeader, workflowId );
            JobParameters jobParameters = prepareJobParamatersForSchemeJob( userIdFromGeneralHeader, uid, tokenFromGeneralHeader,
                    objectFilterJson );
            Map< String, String > options = getOptionScheme( entityManager, userIdFromGeneralHeader, workflowId, TAB_KEY_SCHEME_OPTIONS );
            WFSchemeEntity schemeEntity = validateSelectedSchemeOptions( entityManager, options );
            int category = options.containsKey( "category" ) ? Integer.parseInt( options.get( "category" ) ) : 0;
            workflowManager.runScheme( entityManager, workflowId, userIdFromGeneralHeader, uid, jobParameters, category, schemeEntity );
            return jobParameters;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare job paramaters for scheme job job parameters.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param uid
     *         the uid
     * @param tokenFromGeneralHeader
     *         the token from general header
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the job parameters
     */
    private static JobParameters prepareJobParamatersForSchemeJob( String userIdFromGeneralHeader, String uid,
            String tokenFromGeneralHeader, String objectFilterJson ) {
        JobParameters jobParameters = JsonUtils.jsonToObject( objectFilterJson, JobParametersImpl.class );
        jobParameters.setId( jobParameters.getId() == null ? UUID.randomUUID().toString() : jobParameters.getId() );
        jobParameters.getRequestHeaders().setToken( tokenFromGeneralHeader );
        jobParameters.getWorkflow().setToken( tokenFromGeneralHeader );

        jobParameters.setJobRunByUserId( UUID.fromString( userIdFromGeneralHeader ) );

        jobParameters.setJobRunByUserUID( uid );

        if ( null != jobParameters.getWorkflow().getJob() && !jobParameters.getWorkflow().getJob().isEmpty() ) {
            jobParameters.setRunsOn( jobParameters.getWorkflow().getJob().get( "runsOn" ).toString() );
            jobParameters.setDescription( jobParameters.getWorkflow().getJob().get( "description" ).toString() );
            jobParameters.setName( jobParameters.getWorkflow().getJob().get( "name" ).toString() );
            jobParameters.setRunsOn( jobParameters.getWorkflow().getJob().get( "runsOn" ).toString() );
            jobParameters.setWorkingDir(
                    JsonUtils.jsonToObject( JsonUtils.objectToJson( jobParameters.getWorkflow().getJob().get( "workingDir" ) ),
                            EngineFile.class ) );
        } else {
            jobParameters.setName( jobParameters.getWorkflow().getName() + "_run" );
            jobParameters.setRunsOn( LocationsEnum.DEFAULT_LOCATION.getId() );
        }
        return jobParameters;
    }

    /**
     * Validate selected scheme options wf scheme entity.
     *
     * @param entityManager
     *         the entity manager
     * @param options
     *         the options
     *
     * @return the wf scheme entity
     */
    private WFSchemeEntity validateSelectedSchemeOptions( EntityManager entityManager, Map< String, String > options ) {
        String schemeSchemaId = options.getOrDefault( "scheme", null );
        if ( schemeSchemaId == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SCHEME_OPTIONS_HAVE_NOT_BEEN_SELECTED.getKey() ) );
        }
        WFSchemeEntity schemeEntity = ( WFSchemeEntity ) susDAO.getLatestObjectById( entityManager, WFSchemeEntity.class,
                UUID.fromString( schemeSchemaId ) );
        if ( schemeEntity == null || schemeEntity.getAlgoConfig() == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SCHEME_OPTIONS_HAVE_NOT_BEEN_SELECTED.getKey() ) );
        }
        return schemeEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateUserSchemeLicense( String userIdFromGeneralHeader, String workflowId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            int category = workflowManager.getSchemeCategory( entityManager, workflowId, userIdFromGeneralHeader );

            if ( category == ConstantsInteger.INTEGER_VALUE_ZERO && !licenseManager.isFeatureAllowedToUser( entityManager,
                    SimuspaceFeaturesEnum.DOE.getKey(), userIdFromGeneralHeader ) ) {
                throw new SusException( "User does not have DOE scheme license" );
            } else if ( category == ConstantsInteger.INTEGER_VALUE_ONE && !licenseManager.isFeatureAllowedToUser( entityManager,
                    SimuspaceFeaturesEnum.OPTIMIZATION.getKey(), userIdFromGeneralHeader ) ) {
                throw new SusException( "User does not have OPTIMIZATION scheme license" );
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Object > getWFSchemeData( String userIdFromGeneralHeader, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.WFSCHEMES.getId(),
                    PermissionMatrixEnum.READ.getValue(), Messages.NO_RIGHTS_TO_READ.getKey(), WFSCHEME );

            List< SuSEntity > suSEntities = susDAO.getAllFilteredRecords( entityManager, WFSchemeEntity.class, filter );
            List< Object > objectDTOList = new ArrayList<>();
            for ( SuSEntity e : suSEntities ) {
                objectDTOList.add( createSchemeDTOFromEntity( ( WFSchemeEntity ) e ) );
            }
            return PaginationUtil.constructFilteredResponse( filter, objectDTOList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm createWFSchemeUI( String userIdFromGeneralHeader ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > uiFormItemList = GUIUtils.prepareForm( true, new WFSchemeDTO() );

            for ( UIFormItem uiFormItem : uiFormItemList ) {
                if ( uiFormItem.getName().equals( WFSchemeDTO.UI_COLUMN_NAME_CATEGORY ) ) {
                    populateCategoryDropdownOptions( entityManager, uiFormItem, null );
                    setRulesAndMessageOnUI( uiFormItem );
                }
            }
            return GUIUtils.createFormFromItems( uiFormItemList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WFSchemeDTO createWFScheme( String userId, WFSchemeDTO schemeDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.WFSCHEMES.getId(),
                    PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(), Messages.NO_RIGHTS_TO_CREATE.getKey(), WFSCHEME );
            WFSchemeEntity schemeEntity = createWFSchemeEntityFromDTO( schemeDTO );
            // perform validation that uploaded algo file infact correct
            validateAlgoFile( schemeEntity );
            schemeEntity.setCreatedBy( userCommonManager.getUserEntityById( entityManager, UUID.fromString( userId ) ) );
            SuSEntity updatedSchemeEntity = susDAO.saveOrUpdate( entityManager, schemeEntity );
            susDAO.createRelation( entityManager, new Relation( UUID.fromString( SimuspaceFeaturesEnum.WFSCHEMES.getId() ),
                    updatedSchemeEntity.getComposedId().getId() ) );
            addToAcl( entityManager, userId, updatedSchemeEntity,
                    susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( SuSFeaturesEnum.WFSCHEMES.getId() ) ) );
            return createSchemeDTOFromEntity( ( WFSchemeEntity ) updatedSchemeEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Validate algo file.
     *
     * @param schemeEntity
     *         the scheme entity
     */
    private void validateAlgoFile( WFSchemeEntity schemeEntity ) {
        try ( InputStream decriptStreamn = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream(
                new File( PropertiesManager.getVaultPath() + schemeEntity.getAlgoConfig().getFilePath() ),
                prepareEncryptionDecryptionDTO( schemeEntity.getAlgoConfig().getEncryptionDecryption() ) ) ) {
            JSONObject jsonAlgoConfig = new JSONObject();
            JSONParser parser = new JSONParser();
            jsonAlgoConfig = ( JSONObject ) parser.parse( new InputStreamReader( decriptStreamn ) );
            if ( !jsonAlgoConfig.containsKey( COMMAND ) ) {
                log.error( "Wrong File: Algo file do not contain 'command' key" );
                throw new SusException( "Wrong File: Algo file do not contain 'command' key" );
            }
        } catch ( ParseException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( "Wrong File: Please provide valid json file." );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getWFSchemeContextMenu( String userIdFromGeneralHeader, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // case of select all from data table
            if ( filter.getItems().isEmpty() && filter.getLength().toString().equalsIgnoreCase( "-1" ) ) {

                Long maxResults = susDAO.getAllFilteredRecordCountWithParentId( entityManager, WFSchemeEntity.class, filter,
                        UUID.fromString( SimuspaceFeaturesEnum.WFSCHEMES.getId() ) );
                filter.setLength( Integer.valueOf( maxResults.toString() ) );
                List< SuSEntity > allObjectsList = susDAO.getAllFilteredRecordsWithParent( entityManager, WFSchemeEntity.class, filter,
                        UUID.fromString( SimuspaceFeaturesEnum.WFSCHEMES.getId() ) );
                List< Object > itemsList = new ArrayList<>();
                allObjectsList.stream().forEach( susEntity -> itemsList.add( susEntity.getComposedId().getId() ) );

                filter.setItems( itemsList );
            }

            List< Object > selectedIds = filter.getItems();
            if ( CollectionUtil.isNotEmpty( selectedIds ) && selectedIds.size() == ConstantsInteger.INTEGER_VALUE_ONE ) {
                return contextMenuManager.getSchemeContextMenu( UUID.fromString( selectedIds.get( 0 ).toString() ), false );
            } else {
                final SelectionResponseUI selectionResponseUI = selectionManager.createSelection( entityManager, userIdFromGeneralHeader,
                        SelectionOrigins.CONTEXT, filter );
                return contextMenuManager.getSchemeContextMenu( UUID.fromString( selectionResponseUI.getId() ), true );
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm editWFSchemeUI( String userIdFromGeneralHeader, String schemeId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > uiFormItemList = GUIUtils.prepareForm( false, getWFScheme( entityManager, UUID.fromString( schemeId ) ) );
            for ( UIFormItem uiFormItem : uiFormItemList ) {
                if ( uiFormItem.getName().equals( WFSchemeDTO.UI_COLUMN_NAME_CATEGORY ) ) {
                    populateCategoryDropdownOptions( entityManager, uiFormItem, schemeId );
                    setRulesAndMessageOnUI( uiFormItem );
                }
            }

            return GUIUtils.createFormFromItems( uiFormItemList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WFSchemeDTO updateWFScheme( String userIdFromGeneralHeader, String schemeId, WFSchemeDTO schemeDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.WFSCHEMES.getId(),
                    PermissionMatrixEnum.WRITE.getValue(), Messages.NO_RIGHTS_TO_WRITE.getKey(), WFSCHEME );

            WFSchemeEntity schemeEntity = createWFSchemeEntityFromDTO( schemeDTO );
            schemeEntity.setModifiedBy( userCommonManager.getUserEntityById( entityManager, UUID.fromString( userIdFromGeneralHeader ) ) );
            schemeEntity.setComposedId( new VersionPrimaryKey( UUID.fromString( schemeId ), schemeEntity.getVersionId() ) );
            SuSEntity updatedEntity = susDAO.saveOrUpdate( entityManager, schemeEntity );
            return createSchemeDTOFromEntity( ( WFSchemeEntity ) updatedEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteWFScheme( String userIdFromGeneralHeader, String selectionId, String mode ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.WFSCHEMES.getId(),
                    PermissionMatrixEnum.DELETE.getValue(), Messages.NO_RIGHTS_TO_DELETE.getKey(), WFSCHEME );

            if ( mode.equals( ConstantsMode.SINGLE ) ) {
                List< SuSEntity > deletedSchemeVersions = prepareSchemeVersionsForDelete( entityManager, userIdFromGeneralHeader,
                        UUID.fromString( selectionId ) );
                susDAO.saveOrUpdateBulk( entityManager, deletedSchemeVersions );
            } else {
                List< UUID > selectedObjects = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );
                for ( UUID uuid : selectedObjects ) {
                    List< SuSEntity > deletedSchemeVersions = prepareSchemeVersionsForDelete( entityManager, userIdFromGeneralHeader,
                            uuid );
                    susDAO.saveOrUpdateBulk( entityManager, deletedSchemeVersions );
                }
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubTabsItem listSchemeTabsUI( String userId, String workflowId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            isPermittedtoRead( entityManager, userId, workflowId );
            final WorkflowEntity entity = ( WorkflowEntity ) susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                    UUID.fromString( workflowId ) );
            final List< SubTabsUI > subTabsUIs = new ArrayList<>();
            subTabsUIs.add( new SubTabsUI( null, "general", TAB_KEY_GENERAL ) );
            subTabsUIs.add( new SubTabsUI( null, "schemeoptions", TAB_KEY_SCHEME_OPTIONS ) );
            subTabsUIs.add( new SubTabsUI( null, "designvariables", TAB_KEY_DESIGN_VARIABLES ) );
            subTabsUIs.add( new SubTabsUI( null, "designsummary", TAB_KEY_DESIGN_SUMMARY ) );
            subTabsUIs.add( new SubTabsUI( null, "objectivevariables", TAB_KEY_OBJECT_VARIABLES ) );
            subTabsUIs.add( new SubTabsUI( null, "postprocess", TAB_KEY_POST_PROCESS ) );

            return new SubTabsItem( entity.getComposedId().getId().toString(), entity.getName(), entity.getVersionId(), subTabsUIs,
                    "fa fa-snowflake-o font-black" );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getCategoryOptionForm( String userId, String workflowId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > listUserDirectory = new ArrayList<>();
            log.debug( "entered in getCategroyOptionForm method" );
            List< SelectOptionsUI > options = new ArrayList<>();

            List< String > categories = SchemeCategoryEnum.getValues();

            for ( String schemeCategory : categories ) {
                SelectOptionsUI objectItem = new SelectOptionsUI();
                objectItem.setId( String.valueOf( SchemeCategoryEnum.getByValue( schemeCategory ).getKey() ) );
                objectItem.setName( schemeCategory );
                options.add( objectItem );
            }

            SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            item.setLabel( SELECT_CATEGORY_LABEL );
            item.setOptions( options );
            item.setName( CATEGORY );
            item.setType( FIELD_TYPE_SELECT );
            item.setBindFrom( BIND_FROM_CATAGORY_URL_FOR_RUN_SCHEME_UI_FORM.replace( PARAM_WORKFLOW_ID, "" + workflowId ) );
            Map< String, String > optionScheme = getOptionScheme( entityManager, userId, workflowId, TAB_KEY_SCHEME_OPTIONS );
            item.setValue( optionScheme.isEmpty() ? "" : optionScheme.get( CATEGORY ) );

            setRulesAndMessageOnUI( item );
            listUserDirectory.add( item );
            log.debug( "leaving getObjectOptionForm method" );

            return GUIUtils.createFormFromItems( listUserDirectory );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the option scheme.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param schemeOptions
     *         the scheme options
     *
     * @return the option scheme
     */
    private Map< String, String > getOptionScheme( EntityManager entityManager, String userId, String workflowId, String schemeOptions ) {
        WorkflowEntity workflowEntity = workflowManager.prepareWorkflowEntity(
                workflowManager.getWorkflowById( entityManager, UUID.fromString( userId ), workflowId ) );
        SchemeSchemaEntity entity = schemeSchemaDAO.getSchemeSchemaByWorkflowIdAndName( entityManager, UUID.fromString( workflowId ),
                workflowEntity.getVersionId(), schemeOptions, UUID.fromString( userId ) );
        Map< String, String > optionScheme = new HashMap<>();
        if ( null != entity ) {
            optionScheme = ( Map< String, String > ) JsonUtils.jsonToMap( entity.getContent(), optionScheme );
        }
        return optionScheme;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getSchemeOptionForm( String userId, String workflowId, int categoryId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Notification notify = validateIds( workflowId, String.valueOf( categoryId ) );
            if ( notify.hasErrors() ) {
                throw new SusException( notify.getErrors().toString() );
            }
            List< UIFormItem > uiFormItemList = new ArrayList<>();
            uiFormItemList.add( prepareSchemeDropDown( entityManager, userId, workflowId, categoryId ) );
            return GUIUtils.createFormFromItems( uiFormItemList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getSchemeOptionFormUI( String userId, String workflowId, String schemeId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Notification notify = validateIds( workflowId, schemeId );
            if ( notify.hasErrors() ) {
                throw new SusException( notify.getErrors().toString() );
            }
            return prepareOptions( entityManager, userId, workflowId, schemeId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ScanResponseDTO > scanObjectiveFile( String userId, List< ScanObjectDTO > scanObject ) {
        List< ScanResponseDTO > retList = new ArrayList<>();
        for ( Object scanObjectDTO : scanObject ) {
            ScanObjectDTO scanObj = JsonUtils.jsonToObject( JsonUtils.toJson( ( LinkedHashMap< String, String > ) scanObjectDTO ),
                    ScanObjectDTO.class );

            if ( null != scanObj.getVariableRegex() && !scanObj.getVariableRegex().isEmpty() ) {
                Pattern pattern;
                Pattern regexPattern = Pattern.compile( REGEX );
                Matcher regexMatcher = regexPattern.matcher( scanObj.getVariableRegex() );
                if ( regexMatcher.find() ) {
                    if ( regexMatcher.group( ConstantsInteger.INTEGER_VALUE_TWO ).contains( FLAG_I ) ) {
                        pattern = RegexUtil.preparePattern(
                                RegexUtil.prepareRegex( regexMatcher.group( ConstantsInteger.INTEGER_VALUE_ONE ) ), false );
                    } else {
                        pattern = RegexUtil.preparePattern(
                                RegexUtil.prepareRegex( regexMatcher.group( ConstantsInteger.INTEGER_VALUE_ONE ) ), true );
                    }
                    Matcher matcher = pattern.matcher( scanObj.getLine() );
                    Matcher matcherAllCount = pattern.matcher( scanObj.getLine() );
                    retList.add( prepareResponse( scanObj, matcher, matcherAllCount ) );
                } else {
                    ScanResponseDTO scanResponse = new ScanResponseDTO();
                    scanResponse.setLineNumber( scanObj.getLineNumber() );
                    retList.add( scanResponse );
                }
            } else {
                retList.add( new ScanResponseDTO() );
            }
        }
        return retList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ScanResponseDTO > scanObjectiveFileFromPath( String userIdFromGeneralHeader, ScanFileDTO scanFile ) {
        List< MatchedLine > matchedLines = ScanObjectUtil.getMatchingLinesFromFile( scanFile.getFile(), scanFile.getVariables() );
        List< ScanObjectDTO > scanList = ScanObjectUtil.prepareScanObjectList( scanFile.getVariables(), matchedLines );
        return scanObjectiveFile( userIdFromGeneralHeader, JsonUtils.jsonToObject( JsonUtils.toJson( scanList ), List.class ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getDesignvariablesUI( String wfId, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String algoType;
        try {
            algoType = getSchemeAlgoTypeByUserAndWorkflowId( entityManager, userId, wfId );
        } finally {
            entityManager.close();
        }
        if ( algoType != null ) {
            if ( algoType.equalsIgnoreCase( AlgoTypeEnum.DEFAULT.getKey() ) ) {
                return GUIUtils.listColumns( DefaultDesignVariableDTO.class );
            } else if ( algoType.equalsIgnoreCase( AlgoTypeEnum.MMS.getKey() ) ) {
                return GUIUtils.listColumns( MMSDesignVariableDTO.class );
            } else if ( algoType.equalsIgnoreCase( AlgoTypeEnum.MML.getKey() ) ) {
                return GUIUtils.listColumns( MMLDesignVariableDTO.class );
            } else if ( algoType.equalsIgnoreCase( AlgoTypeEnum.VAL.getKey() ) ) {
                return GUIUtils.listColumns( VALDesignVariableDTO.class );
            } else if ( algoType.equalsIgnoreCase( AlgoTypeEnum.LEVELS.getKey() ) ) {
                return GUIUtils.listColumns( LEVELDesignVariableDTO.class );
            } else if ( algoType.equalsIgnoreCase( AlgoTypeEnum.SSI.getKey() ) ) {
                return GUIUtils.listColumns( SSIDesignVariableDTO.class );
            } else if ( algoType.equalsIgnoreCase( AlgoTypeEnum.MM.getKey() ) ) {
                return GUIUtils.listColumns( MMDesignVariableDTO.class );
            }
        }
        return GUIUtils.listColumns( DesignVariableDTO.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getObjectiveVariablesUI() {
        return GUIUtils.listColumns( ObjectiveVariableDTO.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getDesignSummaryUI( String workflowId, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< TableColumn > columnsList = new ArrayList<>();
            columnsList.add( prepareExpNumColumn() );
            int orderNumber = ConstantsInteger.INTEGER_VALUE_ONE;

            List< DesignVariableEntity > designVariableEntityList = variableDAO.getAllDesignVariables( entityManager, workflowId, userId );
            if ( designVariableEntityList == null || designVariableEntityList.isEmpty() ) {
                WorkflowEntity workflow = ( WorkflowEntity ) susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                        UUID.fromString( workflowId ) );

                designVariableEntityList = variableDAO.getAllDesignVariables( entityManager, workflowId,
                        workflow.getCreatedBy().getId().toString() );
            }

            for ( DesignVariableEntity designVariableEntity : designVariableEntityList ) {
                orderNumber += ConstantsInteger.INTEGER_VALUE_ONE;
                columnsList.add( prepareTable(
                        ( designVariableEntity.getLabel() != null ? designVariableEntity.getLabel() : designVariableEntity.getName() ),
                        orderNumber ) );
            }

            return columnsList;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Object > getDesignVariableData( String userId, String workflowId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.WFSCHEMES.getId(), PermissionMatrixEnum.READ.getValue(),
                    Messages.NO_RIGHTS_TO_READ.getKey(), WFSCHEME );

            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_USER.getKey(),
                        SimuspaceFeaturesEnum.USERS.getKey() ) );
            }

            List< DesignVariableEntity > designVariableEntities = variableDAO.getAllFilteredDesignVariables( entityManager, workflowId,
                    userId, filter );

            WorkflowEntity workflow = ( WorkflowEntity ) susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                    UUID.fromString( workflowId ) );
            if ( designVariableEntities == null || designVariableEntities.isEmpty() ) {
                extractDesignVarFromWorkflowAndUpdateEntity( entityManager, userId, workflowId, workflow );
                designVariableEntities = variableDAO.getAllFilteredDesignVariables( entityManager, workflowId, userId, filter );
            } else {
                DesignVariableEntity designVar = designVariableEntities.get( 0 );
                if ( !designVar.getWorkflow().getComposedId().getId().toString().equals( workflowId )
                        || designVar.getWorkflow().getVersionId() != workflow.getVersionId() ) {
                    extractDesignVarFromWorkflowAndUpdateEntity( entityManager, userId, workflowId, workflow );
                    designVariableEntities = variableDAO.getAllFilteredDesignVariables( entityManager, workflowId, userId, filter );
                }
            }

            String algoType = getSchemeAlgoTypeByUserAndWorkflowId( entityManager, userId, workflowId );

            List< Object > designVariables = new ArrayList<>();
            if ( CollectionUtil.isNotEmpty( designVariableEntities ) ) {
                for ( DesignVariableEntity designVariableEntity : designVariableEntities ) {
                    if ( algoType != null && ( designVariableEntity.getAlgoType() == null || designVariableEntity.getAlgoType()
                            .isEmpty() ) ) {
                        designVariableEntity.setAlgoType( algoType );
                        variableDAO.update( entityManager, designVariableEntity );
                    } else if ( algoType != null && designVariableEntity.getAlgoType() != null && !algoType.equalsIgnoreCase(
                            designVariableEntity.getAlgoType() ) ) {
                        designVariableEntity.setAlgoType( algoType );
                        variableDAO.update( entityManager, designVariableEntity );
                    }
                    Object objDto = prepareDesignVariableDTOFromEntity( designVariableEntity );
                    designVariables.add( objDto != null ? objDto : prepareDefaultDesignVariableDTO( designVariableEntity ) );
                }
            }
            return PaginationUtil.constructFilteredResponse( filter, designVariables );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the scheme algo type by user and workflow id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return the scheme algo type by user and workflow id
     */
    private String getSchemeAlgoTypeByUserAndWorkflowId( EntityManager entityManager, String userId, String workflowId ) {

        String algoType = null;
        Map< String, String > optionScheme = getOptionScheme( entityManager, userId, workflowId, TAB_KEY_SCHEME_OPTIONS );
        String schId = optionScheme.get( SCHEME_LABEL );
        if ( schId != null ) {

            SuSEntity suSEntity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( schId ) );
            if ( suSEntity != null ) {

                WFSchemeEntity wFSchemeEntit = ( WFSchemeEntity ) suSEntity;
                JSONObject jsonAlgoConfig = new JSONObject();
                JSONParser parser = new JSONParser();
                try ( InputStream decriptStreamn = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream(
                        new File( PropertiesManager.getVaultPath() + wFSchemeEntit.getAlgoConfig().getFilePath() ),
                        prepareEncryptionDecryptionDTO( wFSchemeEntit.getAlgoConfig().getEncryptionDecryption() ) ) ) {
                    jsonAlgoConfig = ( JSONObject ) parser.parse( new InputStreamReader( decriptStreamn ) );
                    if ( jsonAlgoConfig.containsKey( "type" ) ) {
                        algoType = ( String ) jsonAlgoConfig.get( "type" );
                    } else {
                        log.error( "Algo file do not contain 'type' key" );
                        algoType = AlgoTypeEnum.DEFAULT.getKey();
                    }
                } catch ( IOException | ParseException e ) {
                    log.error( e.getMessage(), e );
                    throw new SusException( e );
                }
            }
        }
        return algoType;
    }

    /**
     * Prepare encryption decryption DTO.
     *
     * @param encryptionDecryption
     *         the encryption decryption
     *
     * @return the encryption decryption DTO
     */
    private EncryptionDecryptionDTO prepareEncryptionDecryptionDTO( EncryptionDecryptionEntity encryptionDecryption ) {
        if ( encryptionDecryption == null ) {
            return null;
        }
        return new EncryptionDecryptionDTO( encryptionDecryption.getId().toString(), encryptionDecryption.getMethod(),
                encryptionDecryption.isActive(), encryptionDecryption.getSalt() );
    }

    /**
     * Extract design var from workflow and update entity.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param workflow
     *         the workflow
     */
    private void extractDesignVarFromWorkflowAndUpdateEntity( EntityManager entityManager, String userId, String workflowId,
            WorkflowEntity workflow ) {

        final WorkflowDefinitionDTO workflowDefinitionDTO = getWorkflowDefinitionFromUserIdAndWorkflowId( entityManager, userId,
                workflowId );
        final UserWorkFlow userWorkFlow = new UserWorkflowImpl();
        if ( null != workflowDefinitionDTO.getElements() ) {
            userWorkFlow.setNodes( WorkflowDefinitionUtil.prepareWorkflowElements( workflowDefinitionDTO ) );
        }

        List< DesignVariableEntity > designVariableEntities = variableDAO.getAllDesignVariables( entityManager, workflowId,
                workflow.getCreatedBy().getId().toString() );

        String algoType = getSchemeAlgoTypeByUserAndWorkflowId( entityManager, userId, workflowId );

        for ( DesignVariableEntity existingEntity : designVariableEntities ) {
            if ( !isVariableAskOnRun( existingEntity.getName(), userWorkFlow ) && !StringUtils.equals( userId,
                    String.valueOf( existingEntity.getUserId() ) ) ) {
                DesignVariableEntity newEntity = new DesignVariableEntity();
                newEntity.setStep( "" );
                newEntity.setDesignVariableMax( "" );
                newEntity.setDesignVariableMin( "" );
                newEntity.setDesignVariableValues( "" );
                newEntity.setOptional( "" );
                newEntity.setId( UUID.randomUUID() );
                newEntity.setUserId( UUID.fromString( userId ) );
                newEntity.setAlgoType( algoType );
                newEntity.setWorkflow( existingEntity.getWorkflow() );
                newEntity.setLabel( existingEntity.getLabel() );
                newEntity.setName( existingEntity.getName() );
                newEntity.setType( existingEntity.getType() );
                newEntity.setCreatedOn( new Date() );
                newEntity.setLevel( "" );
                newEntity.setNominalValue( existingEntity.getNominalValue() );
                variableDAO.saveOrUpdate( entityManager, newEntity );
            }
        }
    }

    /**
     * Gets the design variable data as label.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return the design variable data as label
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.server.manager.WFSchemeManager#getDesignVariableDataAsLabel(java.lang.String, java.util.UUID)
     */
    @Override
    public DesignVariableLabelDTO getDesignVariableDataAsLabel( String userId, UUID workflowId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getDesignVariableDataAsLabel( entityManager, userId, workflowId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the design variable data as label.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return the design variable data as label
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.server.manager.WFSchemeManager#getDesignVariableDataAsLabel(java.lang.String, java.util.UUID)
     */
    private DesignVariableLabelDTO getDesignVariableDataAsLabel( EntityManager entityManager, String userId, UUID workflowId ) {
        userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.WFSCHEMES.getId(), PermissionMatrixEnum.READ.getValue(),
                Messages.NO_RIGHTS_TO_READ.getKey(), WFSCHEME );
        DesignVariableLabelDTO designVariableLabelDTO = new DesignVariableLabelDTO();
        DesignVariableLabelEntity designVariableLabelEntity = designVariableLabelDAO.getDesignVariableLabelByWorkflowId( entityManager,
                workflowId );

        if ( designVariableLabelEntity != null ) {
            designVariableLabelDTO = prepareDesignVariableLabelDTO( entityManager, designVariableLabelEntity, userId );
        } else {
            List< DesignVariableEntity > designVariableEntities = variableDAO.getAllDesignVariables( entityManager,
                    String.valueOf( workflowId ), userId );
            if ( CollectionUtil.isNotEmpty( designVariableEntities ) ) {
                designVariableLabelDTO = new DesignVariableLabelDTO();
                designVariableLabelDTO.setExpression( "" );
                List< LabelDTO > labelDTOs = new ArrayList<>();
                for ( DesignVariableEntity designVariableEntity : designVariableEntities ) {
                    labelDTOs.add( prepareLabelDTOFromEntity( designVariableEntity ) );
                }
                designVariableLabelDTO.setLabels( labelDTOs );
            }
        }
        return designVariableLabelDTO;
    }

    /**
     * Update design variables relation with expression.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param expression
     *         the expression
     *
     * @return the design variable label DTO
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.server.manager.WFSchemeManager#updateDesignVariablesRelationWithExpression(java.lang.String, java.util.UUID, java.lang.String)
     */
    @Override
    public DesignVariableLabelDTO updateDesignVariablesRelationWithExpression( String userId, UUID workflowId, String expression ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DesignVariableLabelDTO designVariableLabelDTO;
            DesignVariableLabelEntity designVariableLabelEntityFromDB = designVariableLabelDAO.getDesignVariableLabelByWorkflowId(
                    entityManager, workflowId );
            if ( designVariableLabelEntityFromDB == null ) {
                DesignVariableLabelEntity designVariableLabelEntity = new DesignVariableLabelEntity();
                designVariableLabelEntity.setId( UUID.randomUUID() );
                designVariableLabelEntity.setWorkflow(
                        ( WorkflowEntity ) susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, workflowId ) );
                designVariableLabelEntity.setExpression( expression );
                DesignVariableLabelEntity savedDesignVariableLabelEntity = designVariableLabelDAO.saveOrUpdate( entityManager,
                        designVariableLabelEntity );
                designVariableLabelDTO = prepareDesignVariableLabelDTO( entityManager, savedDesignVariableLabelEntity, userId );
            } else {
                designVariableLabelEntityFromDB.setExpression( expression );
                DesignVariableLabelEntity savedDesignVariableLabelEntity = designVariableLabelDAO.saveOrUpdate( entityManager,
                        designVariableLabelEntityFromDB );
                designVariableLabelDTO = prepareDesignVariableLabelDTO( entityManager, savedDesignVariableLabelEntity, userId );
            }
            return designVariableLabelDTO;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare design variable label DTO.
     *
     * @param entityManager
     *         the entity manager
     * @param designVariableLabelEntity
     *         the design variable label entity
     * @param userId
     *         the user id
     *
     * @return the design variable label DTO
     */
    private DesignVariableLabelDTO prepareDesignVariableLabelDTO( EntityManager entityManager,
            DesignVariableLabelEntity designVariableLabelEntity, String userId ) {
        DesignVariableLabelDTO designVariableLabelDTO = null;
        if ( designVariableLabelEntity != null ) {
            designVariableLabelDTO = new DesignVariableLabelDTO();
            designVariableLabelDTO.setExpression( designVariableLabelEntity.getExpression() );

            List< DesignVariableEntity > designVariableEntities = variableDAO.getAllDesignVariables( entityManager,
                    designVariableLabelEntity.getWorkflow().getComposedId().getId().toString(), userId );
            if ( CollectionUtil.isNotEmpty( designVariableEntities ) ) {
                List< LabelDTO > labelDTOs = new ArrayList<>();
                for ( DesignVariableEntity designVariableEntity : designVariableEntities ) {
                    labelDTOs.add( prepareLabelDTOFromEntity( designVariableEntity ) );
                }
                designVariableLabelDTO.setLabels( labelDTOs );
            }
        }
        return designVariableLabelDTO;
    }

    /**
     * Gets the design variable all data.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return the design variable all data
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.server.manager.WFSchemeManager#getDesignVariableAllData(java.lang.String, java.lang.String)
     */
    @Override
    public List< Object > getDesignVariableAllData( String userId, String workflowId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.WFSCHEMES.getId(), PermissionMatrixEnum.READ.getValue(),
                    Messages.NO_RIGHTS_TO_READ.getKey(), WFSCHEME );

            List< Object > designVariables = new ArrayList<>();
            List< DesignVariableEntity > designVariableEntities = variableDAO.getAllDesignVariables( entityManager, workflowId, userId );
            if ( CollectionUtil.isNotEmpty( designVariableEntities ) ) {
                for ( DesignVariableEntity designVariableEntity : designVariableEntities ) {
                    designVariables.add( prepareDesignVariableDTOFromEntity( designVariableEntity ) );
                }
            }
            return designVariables;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< ObjectiveVariableDTO > getObjectiveVariableData( String userId, String workflowId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.WFSCHEMES.getId(), PermissionMatrixEnum.READ.getValue(),
                    Messages.NO_RIGHTS_TO_READ.getKey(), WFSCHEME );

            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_USER.getKey(),
                        SimuspaceFeaturesEnum.USERS.getKey() ) );
            }
            WorkflowDTO workflowDTO = workflowManager.getWorkflowById( entityManager, UUID.fromString( userId ), workflowId );
            String definitionJson = JsonUtils.toJson( workflowDTO.getDefinition() );
            final WorkflowDefinitionDTO workflowDefinitionDTO = JsonUtils.jsonToObject( definitionJson, WorkflowDefinitionDTO.class );
            final UserWorkFlow userWorkFlow = new UserWorkflowImpl();
            if ( null != workflowDefinitionDTO.getElements() ) {
                userWorkFlow.setNodes( WorkflowDefinitionUtil.prepareWorkflowElements( workflowDefinitionDTO ) );
            }

            List< ObjectiveVariableDTO > objectiveVariables = new ArrayList<>();
            List< ObjectiveVariableEntity > objectiveVariableEntities = variableDAO.getAllFilteredObjectiveVariables( entityManager,
                    workflowId, userId, filter );

            if ( objectiveVariableEntities == null || objectiveVariableEntities.isEmpty() ) {
                objectiveVariableEntities = cloneObjectiveVariablesFromWorkflowOwner( userId, workflowDTO, filter, entityManager,
                        userWorkFlow );
            }

            if ( CollectionUtil.isNotEmpty( objectiveVariableEntities ) ) {
                for ( ObjectiveVariableEntity objectiveVariableEntity : objectiveVariableEntities ) {
                    objectiveVariables.add( prepareObjectiveVariableModelFromEntity( objectiveVariableEntity ) );
                }
            }
            return PaginationUtil.constructFilteredResponse( filter, objectiveVariables );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Clone objective variables from wrokflow owner list.
     *
     * @param userId
     *         the user id
     * @param workflowDTO
     *         the workflow dto
     * @param filter
     *         the filter
     * @param entityManager
     *         the entity manager
     * @param userWorkFlow
     *         the user work flow
     *
     * @return the list
     */
    private List< ObjectiveVariableEntity > cloneObjectiveVariablesFromWorkflowOwner( String userId, WorkflowDTO workflowDTO,
            FiltersDTO filter, EntityManager entityManager, UserWorkFlow userWorkFlow ) {
        List< ObjectiveVariableEntity > newObjectiveVaraibles = new ArrayList<>();
        List< ObjectiveVariableEntity > ownerObjectiveVariableEntities = variableDAO.getAllFilteredObjectiveVariables( entityManager,
                workflowDTO.getId(), workflowDTO.getCreatedBy().getId(), filter );
        if ( ownerObjectiveVariableEntities == null || ownerObjectiveVariableEntities.isEmpty() ) {
            return newObjectiveVaraibles;
        }
        for ( ObjectiveVariableEntity existingVariable : ownerObjectiveVariableEntities ) {
            if ( !isVariableAskOnRun( existingVariable.getName(), userWorkFlow ) ) {
                ObjectiveVariableEntity newVariable = new ObjectiveVariableEntity();
                newVariable.setGoal( null );
                newVariable.setOptions( null );
                newVariable.setUserId( UUID.fromString( userId ) );
                newVariable.setCreatedOn( new Date() );
                newVariable.setId( UUID.randomUUID() );
                newVariable.setNominalValue( existingVariable.getNominalValue() );
                newVariable.setLabel( existingVariable.getLabel() );
                newVariable.setName( existingVariable.getName() );
                newVariable.setWorkflow( existingVariable.getWorkflow() );
                newObjectiveVaraibles.add( ( ObjectiveVariableEntity ) variableDAO.saveOrUpdate( entityManager, newVariable ) );
            }
        }
        return newObjectiveVaraibles;
    }

    /**
     * Checks if is variable ask on run.
     *
     * @param variableName
     *         the variable name
     * @param userWorkFlow
     *         the user work flow
     *
     * @return true, if is variable ask on run
     */
    private boolean isVariableAskOnRun( String variableName, UserWorkFlow userWorkFlow ) {

        boolean isAskOnRun = false;
        for ( final UserWFElement element : userWorkFlow.getNodes() ) {
            if ( element.getKey().equals( ElementKeys.IO.getKey() ) && null != element.getFields() ) {
                for ( final Field< ? > field : element.getFields() ) {
                    if ( variableName.contains( element.getName() ) && variableName.contains( field.getName() ) && field.isChangeOnRun() ) {
                        isAskOnRun = true;
                    }
                }
            }
        }

        return isAskOnRun;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Map< String, Object > > getDesignSummaryData( String userId, String workflowId, FiltersDTO filter,
            boolean refreshOriginal ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( isSchemeCategoryOptimization( entityManager, userId, workflowId ) ) {
                filter.setTotalRecords( Long.valueOf( "0" ) );
                filter.setFilteredRecords( Long.valueOf( "0" ) );
                return PaginationUtil.constructFilteredResponse( filter, new ArrayList<>() );
            }

            WorkflowEntity workflowEntity = workflowManager.prepareWorkflowEntity(
                    workflowManager.getWorkflowById( entityManager, UUID.fromString( userId ), workflowId ) );

            List< Map< String, Object > > mapList = new ArrayList<>();

            SchemeSchemaEntity entity = schemeSchemaDAO.getSchemeSchemaByWorkflowIdAndName( entityManager, UUID.fromString( workflowId ),
                    workflowEntity.getVersionId(), TAB_KEY_DESIGN_SUMMARY, UUID.fromString( userId ) );
            if ( null != entity && null != entity.getContent() ) {
                mapList = ( List< Map< String, Object > > ) JsonUtils.jsonToList( entity.getContent(), mapList );
                if ( !mapList.isEmpty() ) {
                    List< String > keyListFromScheme = mapList.stream()
                            .flatMap( map -> map.keySet().stream().filter( key -> !key.equals( EXP_NUM ) && !key.equals( "id" ) ) )
                            .toList();
                    List< String > ketListFromVariabvles = getDesignVariableDataAsLabel( entityManager, userId,
                            workflowEntity.getComposedId().getId() ).getLabels().parallelStream().map( LabelDTO::getName ).toList();
                    boolean isEqual = keyListFromScheme.stream().allMatch( ketListFromVariabvles::contains );
                    if ( !isEqual ) {
                        if ( refreshOriginal ) {
                            mapList = prepareDesignSummary( entityManager, workflowEntity, userId );
                        } else {
                            mapList = new ArrayList<>();
                        }
                    }
                }
            }

            UserDTO user = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );
            filter.setFilteredRecords( ( long ) mapList.size() );
            filter.setTotalRecords( ( long ) mapList.size() );
            FileUtils.deleteFile( PropertiesManager.getUserStagingPath( user.getUserUid() ) + OUTPUT_JSON ); // Remove Temporary output json
            if ( mapList.isEmpty() ) {
                return PaginationUtil.constructFilteredResponse( filter, mapList );
            }
            return PaginationUtil.constructFilteredResponse( filter, mapList.subList( filter.getStart(),
                    filter.getStart() + filter.getLength() < mapList.size() ? filter.getStart() + filter.getLength()
                            : filter.getStart() + ( mapList.size() - filter.getStart() ) ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Checks if is scheme category optimization.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return true, if is scheme category optimization
     */
    private boolean isSchemeCategoryOptimization( EntityManager entityManager, String userId, String workflowId ) {
        int category = workflowManager.getSchemeCategory( entityManager, workflowId, userId );
        return category == ConstantsInteger.INTEGER_VALUE_ONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getGenerateDesignSummaryData( String userId, String workflowId, boolean refreshOriginal ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // check if all design variable have min max defined
            // then prepare design summary method
            boolean prepareDesignsumary = checkIfAllDesignVariablesAreValidated( entityManager, userId, UUID.fromString( workflowId ) );
            if ( !prepareDesignsumary ) {
                throw new SusException( "Summary Not Genrated: Design variables are not filled" );
            }

            WorkflowEntity workflowEntity = workflowManager.prepareWorkflowEntity(
                    workflowManager.getWorkflowById( entityManager, UUID.fromString( userId ), workflowId ) );

            return prepareDesignSummary( entityManager, workflowEntity, userId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String downloadDesignSummaryDataFileCSV( String userId, String workflowId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            if ( isSchemeCategoryOptimization( entityManager, userId, workflowId ) ) {
                throw new SusException( "Optimization/workflow does not have Design Summary Option" );
            }

            WorkflowEntity workflowEntity = workflowManager.prepareWorkflowEntity(
                    workflowManager.getWorkflowById( entityManager, UUID.fromString( userId ), workflowId ) );
            SchemeSchemaEntity entity = schemeSchemaDAO.getSchemeSchemaByWorkflowIdAndName( entityManager, UUID.fromString( workflowId ),
                    workflowEntity.getVersionId(), TAB_KEY_DESIGN_SUMMARY, UUID.fromString( userId ) );
            List< Map< String, Object > > mapList = new ArrayList<>();

            File csvFile;
            try {
                csvFile = new File( PropertiesManager.getDefaultServerTempPath() + File.separator + workflowEntity.getName() + "_"
                        + new Date().getTime() + ".csv" );
                csvFile.createNewFile();
                FileUtils.setGlobalReadFilePermissions( csvFile );
            } catch ( IOException e ) {
                log.error( e.getMessage(), e );
                throw new SusException( "Csv file download Failed : " + e.getMessage() );
            }

            if ( null != entity ) {
                mapList = ( List< Map< String, Object > > ) JsonUtils.jsonToList( entity.getContent(), mapList );
            } else {
                return csvFile.getAbsolutePath();
            }

            List< DesignVariableEntity > designVariableEntityList = variableDAO.getAllDesignVariables( entityManager, workflowId, userId );

            List< String > headerList = new ArrayList<>();

            for ( DesignVariableEntity designVariableEntity : designVariableEntityList ) {
                headerList.add(
                        ( designVariableEntity.getLabel() != null ? designVariableEntity.getLabel() : designVariableEntity.getName() ) );
            }

            try ( FileWriter csvWriter = new FileWriter( csvFile ) ) {

                // writing csv headers in 1st row
                int last = 1;
                for ( String head : headerList ) {
                    if ( last == headerList.size() ) {
                        csvWriter.append( head );
                        csvWriter.append( "\n" );
                    } else {
                        csvWriter.append( head );
                        csvWriter.append( "," );
                        last++;
                    }
                }

                // writing all the experiment data in csv file
                for ( Map< String, Object > headerMap : mapList ) {
                    last = 1;
                    for ( String headx : headerList ) {
                        Object header = headerMap.get( headx );
                        if ( last == headerList.size() ) {
                            csvWriter.append( header.toString() );
                            csvWriter.append( "\n" );
                        } else {
                            csvWriter.append( header.toString() );
                            csvWriter.append( "," );
                            last++;
                        }
                    }
                }

                csvWriter.flush();
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
                throw new SusException( "Csv file download Failed : " + e.getMessage() );
            }

            return csvFile.getAbsolutePath();
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getDesignVariableContextMenu( FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return contextMenuManager.getContextMenu( entityManager, OBJECT_PLUGIN_NAME, DesignVariableDTO.class, filter );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getObjectiveVariableContextMenu( FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return contextMenuManager.getContextMenu( entityManager, OBJECT_PLUGIN_NAME, ObjectiveVariableDTO.class, filter );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateWFRunSchemeByWorkflowId( String userId, String workflowId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            validateWFRunSchemeByWorkflowId( entityManager, userId, workflowId );
        } finally {
            entityManager.close();
        }
    }

    private void validateWFRunSchemeByWorkflowId( EntityManager entityManager, String userId, String workflowId ) {
        WorkflowEntity workflowEntity = workflowManager.prepareWorkflowEntity(
                workflowManager.getWorkflowById( entityManager, UUID.fromString( userId ), workflowId ) );
        SchemeSchemaEntity options = schemeSchemaDAO.getSchemeSchemaByWorkflowIdAndName( entityManager, UUID.fromString( workflowId ),
                workflowEntity.getVersionId(), TAB_KEY_SCHEME_OPTIONS, UUID.fromString( userId ) );
        SchemeSchemaEntity designSummary = schemeSchemaDAO.getSchemeSchemaByWorkflowIdAndName( entityManager, UUID.fromString( workflowId ),
                workflowEntity.getVersionId(), TAB_KEY_DESIGN_SUMMARY, UUID.fromString( userId ) );
        if ( options == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SCHEME_OPTIONS_HAVE_NOT_BEEN_SELECTED.getKey() ) );
        }
        if ( ( null == designSummary || designSummary.getContent().equals( "[]" ) )
                && workflowManager.getSchemeCategory( entityManager, workflowId, userId ) != ConstantsInteger.INTEGER_VALUE_ONE ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DESIGN_SUMMARY_IS_EMPTY.getKey() ) );
        }
        validateDesignVariables( entityManager, workflowId, userId );
    }

    /**
     * Validate design variables.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param userId
     *         the user id
     */
    private void validateDesignVariables( EntityManager entityManager, String workflowId, String userId ) {
        if ( !checkIfAllDesignVariablesAreValidated( entityManager, userId, UUID.fromString( workflowId ) ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DESIGN_VARIABLES_REQUIRED_FIELD_IS_MISSING.getKey() ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm editDesignVariableUI( String userIdFromGeneralHeader, String designvariableId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Object designVariable;
        try {
            designVariable = getDesignVariable( entityManager, designvariableId );
        } finally {
            entityManager.close();
        }
        if ( designVariable == null ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.VARIABLE_NOT_FOUND_REFRESH_LIST_OR_OPEN_SUBMIT_FORM_AGAIN.getKey() ) );
        }
        if ( designVariable instanceof DefaultDesignVariableDTO defaultDesignVariableDTO ) {
            List< UIFormItem > editVariablesForm = GUIUtils.prepareForm( false, defaultDesignVariableDTO );
            addTooltipsToVariablesForm( editVariablesForm, defaultDesignVariableDTO.getType() );
            return GUIUtils.createFormFromItems( editVariablesForm );
        } else if ( designVariable instanceof MMSDesignVariableDTO defaultDesignVariableDTO ) {
            List< UIFormItem > editVariablesForm = GUIUtils.prepareForm( false, defaultDesignVariableDTO );
            addTooltipsToVariablesForm( editVariablesForm, defaultDesignVariableDTO.getType() );
            return GUIUtils.createFormFromItems( editVariablesForm );
        } else if ( designVariable instanceof MMLDesignVariableDTO defaultDesignVariableDTO ) {
            List< UIFormItem > editVariablesForm = GUIUtils.prepareForm( false, defaultDesignVariableDTO );
            addTooltipsToVariablesForm( editVariablesForm, defaultDesignVariableDTO.getType() );
            return GUIUtils.createFormFromItems( editVariablesForm );
        } else if ( designVariable instanceof VALDesignVariableDTO defaultDesignVariableDTO ) {
            List< UIFormItem > editVariablesForm = GUIUtils.prepareForm( false, defaultDesignVariableDTO );
            addTooltipsToVariablesForm( editVariablesForm, defaultDesignVariableDTO.getType() );
            return GUIUtils.createFormFromItems( editVariablesForm );
        } else if ( designVariable instanceof LEVELDesignVariableDTO levelDesignVariableDTO ) {
            List< UIFormItem > editVariablesForm = GUIUtils.prepareForm( false, levelDesignVariableDTO );
            addTooltipsToVariablesForm( editVariablesForm, levelDesignVariableDTO.getType() );
            return GUIUtils.createFormFromItems( editVariablesForm );
        } else if ( designVariable instanceof MMDesignVariableDTO mmDesignVariableDTO ) {
            List< UIFormItem > editVariablesForm = GUIUtils.prepareForm( false, mmDesignVariableDTO );
            addTooltipsToVariablesForm( editVariablesForm, mmDesignVariableDTO.getType() );
            return GUIUtils.createFormFromItems( editVariablesForm );
        } else if ( designVariable instanceof SSIDesignVariableDTO defaultDesignVariableDTO ) {
            List< UIFormItem > editVariablesForm = GUIUtils.prepareForm( false, defaultDesignVariableDTO );
            addTooltipsToVariablesForm( editVariablesForm, defaultDesignVariableDTO.getType() );
            return GUIUtils.createFormFromItems( editVariablesForm );
        }
        return new UIForm();
    }

    /**
     * Adds the tooltips to variables form.
     *
     * @param editVariablesForm
     *         the edit variables form
     * @param type
     *         the type
     */
    private void addTooltipsToVariablesForm( List< UIFormItem > editVariablesForm, String type ) {
        if ( type.equalsIgnoreCase( ConstantsString.INTEGER_TYPE ) || type.equalsIgnoreCase( ConstantsString.FLOAT_TYPE ) ) {
            addTooltipsToVariablesForNumericType( editVariablesForm );
        }

        if ( type.equalsIgnoreCase( ConstantsString.STRING_TYPE ) ) {
            addTooltipsToVariablesForStringType( editVariablesForm );
        }
    }

    /**
     * Adds the tooltips to variables for numeric type.
     *
     * @param editVariablesForm
     *         the edit variables form
     */
    private void addTooltipsToVariablesForNumericType( List< UIFormItem > editVariablesForm ) {
        for ( UIFormItem variable : editVariablesForm ) {
            switch ( variable.getName() ) {
                case MIN_FIELD_NAME -> variable.setTooltip(
                        MessageBundleFactory.getMessage( Messages.DTO_UI_SCHEME_MINIMUM_VALUE_TOOLTIP_MESSAGE.getKey() ) );
                case MAX_FIELD_NAME ->
                        variable.setTooltip( MessageBundleFactory.getMessage( Messages.DTO_UI_SCHEME_MAX_VALUE_TOOLTIP_MESSAGE.getKey() ) );
                case STEP_FIELD_NAME ->
                        variable.setTooltip( MessageBundleFactory.getMessage( Messages.DTO_UI_SCHEME_STEP_SIZE_TOOLTIP_MESSAGE.getKey() ) );
                case OPTIONAL_FIELD_NAME ->
                        variable.setTooltip( MessageBundleFactory.getMessage( Messages.DTO_UI_SCHEME_OPTIONAL_TOOLTIP_MESSAGE.getKey() ) );
                case VALUES_FIELD_NAME ->
                        variable.setTooltip( MessageBundleFactory.getMessage( Messages.DTO_UI_SCHEME_VALUES_TOOLTIP_MESSAGE.getKey() ) );
                case LEVELS_FIELD_NAME ->
                        variable.setTooltip( MessageBundleFactory.getMessage( Messages.DTO_UI_SCHEME_LEVELS_TOOLTIP_MESSAGE.getKey() ) );
                default -> {
                    // do notthing
                }
            }
        }
    }

    /**
     * Adds the tooltips to variables for string type.
     *
     * @param editVariablesForm
     *         the edit variables form
     */
    private void addTooltipsToVariablesForStringType( List< UIFormItem > editVariablesForm ) {
        for ( UIFormItem variable : editVariablesForm ) {
            if ( variable.getName().equals( VALUES_FIELD_NAME ) ) {
                variable.setTooltip( MessageBundleFactory.getMessage( Messages.DTO_UI_SCHEME_VALUES_TOOLTIP_MESSAGE.getKey() ) );
            } else if ( variable.getName().equals( OPTIONAL_FIELD_NAME ) ) {
                variable.setTooltip( MessageBundleFactory.getMessage( Messages.DTO_UI_SCHEME_OPTIONAL_TOOLTIP_MESSAGE.getKey() ) );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm editObjectiveVariableUI( String userIdFromGeneralHeader, String objectiveVariableId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > uiFormItemList = GUIUtils.prepareForm( false, prepareObjectiveVariableModelFromEntity(
                    variableDAO.readObjectiveVariable( entityManager, UUID.fromString( objectiveVariableId ) ) ) );

            for ( UIFormItem uiFormItem : uiFormItemList ) {
                if ( uiFormItem.getName().equalsIgnoreCase( "Goal" ) ) {
                    setRulesAndMessageOnUI( uiFormItem ); // set goal field mandatory
                }
            }
            return GUIUtils.createFormFromItems( uiFormItemList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm addDesignSummaryUI( String userId, String workflowId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            WorkflowEntity workflowEntity = workflowManager.prepareWorkflowEntity(
                    workflowManager.getWorkflowById( entityManager, UUID.fromString( userId ), workflowId ) );
            SchemeSchemaEntity entity = schemeSchemaDAO.getSchemeSchemaByWorkflowIdAndName( entityManager, UUID.fromString( workflowId ),
                    workflowEntity.getVersionId(), TAB_KEY_DESIGN_SUMMARY, UUID.fromString( userId ) );
            List< UIFormItem > retList = new ArrayList<>();
            List< Map< String, Object > > mapList = new ArrayList<>();
            mapList = ( List< Map< String, Object > > ) JsonUtils.jsonToList( entity.getContent(), mapList );

            Map< String, Object > mp = mapList.get( 0 );
            for ( Entry< String, Object > entry : mp.entrySet() ) {
                if ( !entry.getKey().equals( EXP_NUM ) && !entry.getKey().equals( ID ) ) {
                    retList.add( prepareUIFormItem( entry, false ) );
                }
            }
            return GUIUtils.createFormFromItems( retList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Map< String, Object > > addDesignSummary( String userId, String workflowId, Map< String, Object > newValue ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            WorkflowEntity workflowEntity = workflowManager.prepareWorkflowEntity(
                    workflowManager.getWorkflowById( entityManager, UUID.fromString( userId ), workflowId ) );
            SchemeSchemaEntity entity = schemeSchemaDAO.getSchemeSchemaByWorkflowIdAndName( entityManager, UUID.fromString( workflowId ),
                    workflowEntity.getVersionId(), TAB_KEY_DESIGN_SUMMARY, UUID.fromString( userId ) );
            List< Map< String, Object > > mapList = new ArrayList<>();
            mapList = ( List< Map< String, Object > > ) JsonUtils.jsonToList( entity.getContent(), mapList );
            Map< String, Object > lastElement = mapList.get( mapList.size() - 1 );
            newValue.put( EXP_NUM, Integer.parseInt( lastElement.get( EXP_NUM ).toString() ) + ConstantsInteger.INTEGER_VALUE_ONE );
            newValue.put( ID, Integer.parseInt( lastElement.get( ID ).toString() ) + ConstantsInteger.INTEGER_VALUE_ONE );
            mapList.add( newValue );
            entity.setContent( JsonUtils.toJson( mapList ) );
            entity.setUserId( UUID.fromString( userId ) );
            schemeSchemaDAO.saveOrUpdate( entityManager, entity );
            return mapList;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm editDesignSummaryUI( String userId, String workflowId, String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            WorkflowEntity workflowEntity = workflowManager.prepareWorkflowEntity(
                    workflowManager.getWorkflowById( entityManager, UUID.fromString( userId ), workflowId ) );
            SchemeSchemaEntity entity = schemeSchemaDAO.getSchemeSchemaByWorkflowIdAndName( entityManager, UUID.fromString( workflowId ),
                    workflowEntity.getVersionId(), TAB_KEY_DESIGN_SUMMARY, UUID.fromString( userId ) );
            List< UIFormItem > retList = new ArrayList<>();
            List< Map< String, Object > > mapList = new ArrayList<>();
            mapList = ( List< Map< String, Object > > ) JsonUtils.jsonToList( entity.getContent(), mapList );

            for ( Map< String, Object > map2 : mapList ) {
                if ( map2.get( EXP_NUM ).toString().equals( id ) ) {
                    for ( Entry< String, Object > entry : map2.entrySet() ) {
                        if ( !entry.getKey().equals( EXP_NUM ) && !entry.getKey().equals( ID ) ) {
                            retList.add( prepareUIFormItem( entry, true ) );
                        }
                    }
                }
            }
            return GUIUtils.createFormFromItems( retList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Map< String, Object > > updateDesignSummary( String userId, String workflowId, String id,
            Map< String, Object > updateMap ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            WorkflowEntity workflowEntity = workflowManager.prepareWorkflowEntity(
                    workflowManager.getWorkflowById( entityManager, UUID.fromString( userId ), workflowId ) );
            SchemeSchemaEntity entity = schemeSchemaDAO.getSchemeSchemaByWorkflowIdAndName( entityManager, UUID.fromString( workflowId ),
                    workflowEntity.getVersionId(), TAB_KEY_DESIGN_SUMMARY, UUID.fromString( userId ) );
            List< Map< String, Object > > mapList = new ArrayList<>();
            mapList = ( List< Map< String, Object > > ) JsonUtils.jsonToList( entity.getContent(), mapList );
            for ( Map< String, Object > map : mapList ) {
                if ( map.get( EXP_NUM ).toString().equals( id ) ) {
                    for ( Entry< String, Object > entry : map.entrySet() ) {
                        updateEntry( entry, updateMap );
                    }
                }
            }
            entity.setContent( mapList.isEmpty() ? mapList.toString() : JsonUtils.toJson( mapList ) );
            entity.setUserId( UUID.fromString( userId ) );
            schemeSchemaDAO.saveOrUpdate( entityManager, entity );
            return mapList;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteDesignSummary( String userId, String workflowId, FiltersDTO filters ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            WorkflowEntity workflowEntity = workflowManager.prepareWorkflowEntity(
                    workflowManager.getWorkflowById( entityManager, UUID.fromString( userId ), workflowId ) );
            SchemeSchemaEntity entity = schemeSchemaDAO.getSchemeSchemaByWorkflowIdAndName( entityManager, UUID.fromString( workflowId ),
                    workflowEntity.getVersionId(), TAB_KEY_DESIGN_SUMMARY, UUID.fromString( userId ) );
            if ( filters.getItems().isEmpty() ) {
                entity.setContent( new ArrayList<>().toString() );
            } else {
                List< Map< String, Object > > updatedMapList = new ArrayList<>();
                List< Map< String, Object > > mapList = new ArrayList<>();
                mapList = ( List< Map< String, Object > > ) JsonUtils.jsonToList( entity.getContent(), mapList );
                List< Object > ids = filters.getItems();
                List< String > strings = new ArrayList<>( ids.size() );
                for ( Object object : ids ) {
                    strings.add( object != null ? object.toString() : null );
                }
                for ( Map< String, Object > object : mapList ) {
                    if ( !strings.contains( object.get( EXP_NUM ).toString() ) ) {
                        updatedMapList.add( object );
                    }
                }
                entity.setContent( updatedMapList.isEmpty() ? updatedMapList.toString() : JsonUtils.toJson( updatedMapList ) );
            }
            entity.setUserId( UUID.fromString( userId ) );
            schemeSchemaDAO.saveOrUpdate( entityManager, entity );
            return true;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Update entry.
     *
     * @param entry
     *         the entry
     * @param updateMap
     *         the update map
     */
    private static void updateEntry( Entry< String, Object > entry, Map< String, Object > updateMap ) {
        for ( Entry< String, Object > updateEntry : updateMap.entrySet() ) {
            if ( updateEntry.getKey().equals( entry.getKey() ) ) {
                entry.setValue( updateEntry.getValue() );
            }
        }
    }

    /**
     * Prepare UI form item.
     *
     * @param entry
     *         the entry
     * @param setValue
     *         the set value
     *
     * @return the UI form item
     */
    private UIFormItem prepareUIFormItem( Entry< String, Object > entry, boolean setValue ) {
        UIFormItem ui = GUIUtils.createFormItem();
        ui.setDuplicate( false );
        ui.setLabel( entry.getKey() );
        ui.setName( entry.getKey() );
        ui.setType( "text" );
        if ( setValue ) {
            ui.setValue( entry.getValue() );
        }
        ui.setReadonly( false );
        ui.setMultiple( false );

        Map< String, Object > rules = new HashMap<>();
        rules.put( REQUIRED, true );
        ui.setRules( rules );

        return ui;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object updateDesignVariable( String userId, String designVariableId, String json ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            DesignVariableEntity designEntity = variableDAO.readDesignVariable( entityManager, UUID.fromString( designVariableId ) );
            if ( designEntity == null ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.NO_DESIGN_VARIABLE_WITH_ID_EXIST.getKey(), designVariableId ) );
            }

            if ( designEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.DEFAULT.getKey() ) ) {
                DefaultDesignVariableDTO designVariableDTO = JsonUtils.jsonToObject( json, DefaultDesignVariableDTO.class );
                designVariableDTO.setId( UUID.fromString( designVariableId ) );
                designEntity.setStep( designVariableDTO.getStep() );
                designEntity.setOptional( designVariableDTO.getOptional() );
                designEntity.setDesignVariableValues( designVariableDTO.getValues() );
                designEntity.setDesignVariableMax( designVariableDTO.getMax() );
                designEntity.setDesignVariableMin( designVariableDTO.getMin() );
                designEntity.setCreatedOn( new Date() );
                designEntity.setUserId( UUID.fromString( userId ) );
                validateValues( designEntity );
                designEntity = ( DesignVariableEntity ) variableDAO.saveOrUpdate( entityManager, designEntity );
                return prepareDesignVariableDTOFromEntity( designEntity );
            } else if ( designEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.MMS.getKey() ) ) {
                MMSDesignVariableDTO designVariableDTO = JsonUtils.jsonToObject( json, MMSDesignVariableDTO.class );
                designVariableDTO.setId( UUID.fromString( designVariableId ) );
                designEntity.setDesignVariableMax( designVariableDTO.getMax() );
                designEntity.setDesignVariableMin( designVariableDTO.getMin() );
                designEntity.setStep( designVariableDTO.getStep() );
                designEntity.setCreatedOn( new Date() );
                designEntity.setUserId( UUID.fromString( userId ) );
                validateValues( designEntity );
                designEntity = ( DesignVariableEntity ) variableDAO.saveOrUpdate( entityManager, designEntity );
                return prepareDesignVariableDTOFromEntity( designEntity );
            } else if ( designEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.MML.getKey() ) ) {
                MMLDesignVariableDTO designVariableDTO = JsonUtils.jsonToObject( json, MMLDesignVariableDTO.class );
                designVariableDTO.setId( UUID.fromString( designVariableId ) );
                designEntity.setDesignVariableMax( designVariableDTO.getMax() );
                designEntity.setDesignVariableMin( designVariableDTO.getMin() );
                designEntity.setLevel( designVariableDTO.getLevel() );
                designEntity.setCreatedOn( new Date() );
                designEntity.setUserId( UUID.fromString( userId ) );
                validateValues( designEntity );
                designEntity = ( DesignVariableEntity ) variableDAO.saveOrUpdate( entityManager, designEntity );
                return prepareDesignVariableDTOFromEntity( designEntity );
            } else if ( designEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.VAL.getKey() ) ) {
                VALDesignVariableDTO designVariableDTO = JsonUtils.jsonToObject( json, VALDesignVariableDTO.class );
                designVariableDTO.setId( UUID.fromString( designVariableId ) );
                designEntity.setDesignVariableValues( designVariableDTO.getValues() );
                designEntity.setCreatedOn( new Date() );
                designEntity.setUserId( UUID.fromString( userId ) );
                validateValues( designEntity );
                designEntity = ( DesignVariableEntity ) variableDAO.saveOrUpdate( entityManager, designEntity );
                return prepareDesignVariableDTOFromEntity( designEntity );
            } else if ( designEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.LEVELS.getKey() ) ) {
                LEVELDesignVariableDTO designVariableDTO = JsonUtils.jsonToObject( json, LEVELDesignVariableDTO.class );
                designVariableDTO.setId( UUID.fromString( designVariableId ) );
                designEntity.setLevel( designVariableDTO.getLevels() );
                designEntity.setCreatedOn( new Date() );
                designEntity.setUserId( UUID.fromString( userId ) );
                validateValues( designEntity );
                designEntity = ( DesignVariableEntity ) variableDAO.saveOrUpdate( entityManager, designEntity );
                return prepareDesignVariableDTOFromEntity( designEntity );
            } else if ( designEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.SSI.getKey() ) ) {
                SSIDesignVariableDTO designVariableDTO = JsonUtils.jsonToObject( json, SSIDesignVariableDTO.class );
                designVariableDTO.setId( UUID.fromString( designVariableId ) );
                designVariableDTO.setStart( designVariableDTO.getStart() );
                designVariableDTO.setStop( designVariableDTO.getStop() );
                designVariableDTO.setInterval( designVariableDTO.getInterval() );

                designEntity.setCreatedOn( new Date() );
                designEntity.setUserId( UUID.fromString( userId ) );
                validateValues( designEntity );
                designEntity = ( DesignVariableEntity ) variableDAO.saveOrUpdate( entityManager, designEntity );
                return prepareDesignVariableDTOFromEntity( designEntity );
            } else if ( designEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.MM.getKey() ) ) {
                MMDesignVariableDTO designVariableDTO = JsonUtils.jsonToObject( json, MMDesignVariableDTO.class );
                designVariableDTO.setId( UUID.fromString( designVariableId ) );
                designEntity.setDesignVariableMin( designVariableDTO.getMin() );
                designEntity.setDesignVariableMax( designVariableDTO.getMax() );
                designEntity.setCreatedOn( new Date() );
                designEntity.setUserId( UUID.fromString( userId ) );
                validateValues( designEntity );
                designEntity = ( DesignVariableEntity ) variableDAO.saveOrUpdate( entityManager, designEntity );
                return prepareDesignVariableDTOFromEntity( designEntity );
            }

            return new DesignVariableDTO();
        } finally {
            entityManager.close();
        }

    }

    /**
     * Check if all design variables are validated.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param wfId
     *         the wf id
     *
     * @return true, if successful
     */
    private boolean checkIfAllDesignVariablesAreValidated( EntityManager entityManager, String userId, UUID wfId ) {
        List< DesignVariableEntity > designVariableEntities = variableDAO.getAllDesignVariables( entityManager, wfId.toString(), userId );
        return validateDesignVariableEntityList( designVariableEntities );
    }

    /**
     * Validate design variable entity list.
     *
     * @param designVariableEntities
     *         the design variable entities
     *
     * @return true, if successful
     */
    private static boolean validateDesignVariableEntityList( List< DesignVariableEntity > designVariableEntities ) {
        boolean prepareDesignsumary = false;
        if ( designVariableEntities != null && !designVariableEntities.isEmpty() ) {
            for ( DesignVariableEntity designVariableEntity : designVariableEntities ) {

                if ( designVariableEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.DEFAULT.getKey() )
                        && designVariableEntity.getDesignVariableMin() != null && !designVariableEntity.getDesignVariableMin().isEmpty()
                        && designVariableEntity.getDesignVariableMax() != null && !designVariableEntity.getDesignVariableMax().isEmpty()
                        && designVariableEntity.getStep() != null && !designVariableEntity.getStep().isEmpty() ) {
                    prepareDesignsumary = true;
                } else if ( designVariableEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.MML.getKey() )
                        && designVariableEntity.getDesignVariableMin() != null && !designVariableEntity.getDesignVariableMin().isEmpty()
                        && designVariableEntity.getDesignVariableMax() != null && !designVariableEntity.getDesignVariableMax().isEmpty()
                        && designVariableEntity.getLevel() != null && !designVariableEntity.getLevel().isEmpty() ) {
                    prepareDesignsumary = true;
                } else if ( designVariableEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.MMS.getKey() )
                        && designVariableEntity.getDesignVariableMin() != null && !designVariableEntity.getDesignVariableMin().isEmpty()
                        && designVariableEntity.getDesignVariableMax() != null && !designVariableEntity.getDesignVariableMax().isEmpty()
                        && designVariableEntity.getStep() != null && !designVariableEntity.getStep().isEmpty() ) {
                    prepareDesignsumary = true;
                } else if ( designVariableEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.VAL.getKey() )
                        && designVariableEntity.getDesignVariableValues() != null && !designVariableEntity.getDesignVariableValues()
                        .isEmpty() ) {
                    prepareDesignsumary = true;
                } else if ( designVariableEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.LEVELS.getKey() )
                        && designVariableEntity.getLevel() != null && !designVariableEntity.getLevel().isEmpty() ) {
                    prepareDesignsumary = true;
                } else if ( designVariableEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.SSI.getKey() )
                        && designVariableEntity.getStart() != null && !designVariableEntity.getStart().isEmpty()
                        && designVariableEntity.getStop() != null && !designVariableEntity.getStop().isEmpty()
                        && designVariableEntity.getInterval() != null && !designVariableEntity.getInterval().isEmpty() ) {
                    prepareDesignsumary = true;
                } else if ( designVariableEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.MM.getKey() )
                        && designVariableEntity.getDesignVariableMin() != null && !designVariableEntity.getDesignVariableMin().isEmpty()
                        && designVariableEntity.getDesignVariableMax() != null && !designVariableEntity.getDesignVariableMax().isEmpty() ) {
                    prepareDesignsumary = true;
                } else {
                    prepareDesignsumary = false;
                    break;
                }
            }
        }
        return prepareDesignsumary;
    }

    /**
     * Prepare design summary.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowEntity
     *         the workflow entity
     * @param userId
     *         the user id
     *
     * @return the list
     */
    private List< Map< String, Object > > prepareDesignSummary( EntityManager entityManager, WorkflowEntity workflowEntity,
            String userId ) {

        SchemeSchemaEntity schemeOptions = schemeSchemaDAO.getSchemeSchemaByWorkflowIdAndName( entityManager,
                workflowEntity.getComposedId().getId(), workflowEntity.getVersionId(), TAB_KEY_SCHEME_OPTIONS, UUID.fromString( userId ) );

        List< Map< String, Object > > mapList = new ArrayList<>();
        UserDTO user = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );
        if ( null != schemeOptions ) {
            return calculateDesignSummary( entityManager, userId, workflowEntity, mapList, user, schemeOptions );
        } else {
            throw new SusException( "genrate Summary Stoped: Scheme options are not selected" );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectiveVariableDTO updateObjectiveVariable( String userId, ObjectiveVariableDTO objectiveVariableDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ObjectiveVariableEntity entity = null;
            if ( null != objectiveVariableDTO ) {
                entity = variableDAO.readObjectiveVariable( entityManager, objectiveVariableDTO.getId() );
                if ( entity == null ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTIVE_VARIABLE_WITH_ID_EXIST.getKey(),
                            objectiveVariableDTO.getId() ) );
                }
                entity = prepareObjectiveVariableEntityFromDTO( objectiveVariableDTO, entity );
                entity.setUserId( UUID.fromString( userId ) );

                entity = ( ObjectiveVariableEntity ) variableDAO.update( entityManager, entity );
                selectionManager.sendEvent( entityManager, userId, entity.getId().toString(), entity.getWorkflow().getComposedId().getId(),
                        "update" );
            }
            return prepareObjectiveVariableModelFromEntity( entity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SchemeOptionSchemaDTO saveOrUpdateSchemeOptionForm( String userId, UUID workflowId, String json ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            WorkflowEntity workflowEntity = workflowManager.prepareWorkflowEntity(
                    workflowManager.getWorkflowById( entityManager, UUID.fromString( userId ), workflowId.toString() ) );

            SchemeSchemaEntity entity = schemeSchemaDAO.getSchemeSchemaByWorkflowIdAndName( entityManager, workflowId,
                    workflowEntity.getVersionId(), TAB_KEY_SCHEME_OPTIONS, UUID.fromString( userId ) );

            if ( null == entity ) {
                entity = new SchemeSchemaEntity();
                entity.setId( UUID.randomUUID() );
                entity.setContent( json );
                entity.setWorkflow( workflowEntity );
                entity.setName( TAB_KEY_SCHEME_OPTIONS );
                entity.setUserId( UUID.fromString( userId ) );
            } else {
                entity.setContent( json );
            }
            SchemeSchemaEntity updatedEntity = schemeSchemaDAO.saveOrUpdate( entityManager, entity );
            return prepareDTOFromEntity( updatedEntity );
        } finally {
            entityManager.close();
        }

    }

    /**
     * Replace karaf path.
     *
     * @param algoSchemeDTO
     *         the algo scheme DTO
     *
     * @return the algo scheme DTO
     */
    private AlgoSchemeDTO replaceKarafPath( AlgoSchemeDTO algoSchemeDTO ) {
        if ( algoSchemeDTO.getGenerateInput() != null ) {
            algoSchemeDTO.setGenerateInput( algoSchemeDTO.getGenerateInput()
                    .replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, PropertiesManager.getScriptsPath() ) );
        }
        if ( algoSchemeDTO.getFilterOutput() != null ) {
            algoSchemeDTO.setFilterOutput( algoSchemeDTO.getFilterOutput()
                    .replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, PropertiesManager.getScriptsPath() ) );
        }
        if ( algoSchemeDTO.getCommand() != null && algoSchemeDTO.getCommand().getExecute() != null ) {
            algoSchemeDTO.getCommand().setExecute( algoSchemeDTO.getCommand().getExecute()
                    .replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, PropertiesManager.getScriptsPath() ) );
        }
        return algoSchemeDTO;
    }

    /**
     * Calculate design summary.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowEntity
     *         the workflow entity
     * @param mapList
     *         the map list
     * @param user
     *         the user
     * @param schemeOptions
     *         the scheme options
     *
     * @return the list
     */
    private List< Map< String, Object > > calculateDesignSummary( EntityManager entityManager, String userId, WorkflowEntity workflowEntity,
            List< Map< String, Object > > mapList, UserDTO user, SchemeSchemaEntity schemeOptions ) {
        AlgoSchemeDTO algoSchemeDTO;
        validateUserStagingPath( user.getUserUid() );
        File outputFilePath = new File( PropertiesManager.getUserStagingPath( user.getUserUid() ) + OUTPUT_JSON );
        try {

            algoSchemeDTO = getAlgoConfigFromSchemeOptions( entityManager, schemeOptions );
            replaceKarafPath( algoSchemeDTO );

            prepareAndExecuteAlgoConfig( entityManager, userId, user.getUserUid(), workflowEntity, algoSchemeDTO );

            if ( null != algoSchemeDTO.getFilterOutput() && !algoSchemeDTO.getFilterOutput().isEmpty() ) {
                DesignVariableLabelDTO dataDto = getDesignVariableDataAsLabel( entityManager, userId,
                        workflowEntity.getComposedId().getId() );
                if ( dataDto.getExpression() != null && !dataDto.getExpression().equals( "" ) && outputFilePath.exists() ) {
                    FileUtils.setGlobalAllFilePermissions( outputFilePath );
                    ProcessResult processResult = PythonUtils.callFilterOutput( user.getUserUid(), algoSchemeDTO.getFilterOutput(),
                            "filter", dataDto.getExpression(), outputFilePath.getAbsolutePath() );
                    if ( processResult.getErrorStreamString() != null && !processResult.getErrorStreamString().isEmpty() ) {
                        throw new SusException( "run command filterOutput ERROR :" + processResult.getErrorStreamString() );
                    }
                } else {
                    log.debug( "expression is empty. Skipping call to filterOutput for design summary" );
                }
            }
            if ( outputFilePath.exists() ) {
                try ( BufferedReader outputFile = new BufferedReader( new FileReader( outputFilePath ) ) ) {
                    JSONParser parser = new JSONParser();
                    JSONArray outputJson = ( JSONArray ) parser.parse( outputFile );
                    List< DesignVariableEntity > designVariablesEntities = variableDAO.getAllDesignVariables( entityManager,
                            workflowEntity.getComposedId().getId().toString(), userId );
                    int expNum = 0;
                    for ( JSONObject jsonObj : ( Iterable< JSONObject > ) outputJson ) {
                        Map< String, Object > map = new HashMap<>();
                        boolean addIteration = true;
                        int num = ++expNum;
                        map.put( "id", num );
                        map.put( EXP_NUM, num );
                        for ( DesignVariableEntity designVariableEntity : designVariablesEntities ) {
                            if ( null != jsonObj.get( designVariableEntity.getLabel() ) ) {
                                String valueString = String.valueOf( jsonObj.get( designVariableEntity.getLabel() ) );
                                map.put( designVariableEntity.getLabel(), valueString );
                            } else {
                                addIteration = false;
                            }
                        }
                        if ( !map.isEmpty() && addIteration ) {
                            mapList.add( map );
                        }
                    }
                } catch ( IOException | ParseException e ) {
                    log.error( e.getMessage(), e );
                }
            }

            deleteFileIfExists( outputFilePath.getAbsolutePath() );

            SchemeSchemaEntity oldEntity = schemeSchemaDAO.getSchemeSchemaByWorkflowIdAndName( entityManager,
                    workflowEntity.getComposedId().getId(), workflowEntity.getVersionId(), TAB_KEY_DESIGN_SUMMARY,
                    UUID.fromString( userId ) );
            if ( oldEntity != null ) {
                schemeSchemaDAO.delete( entityManager, oldEntity );
            }
            SchemeSchemaEntity newEntity = new SchemeSchemaEntity();
            newEntity.setId( UUID.randomUUID() );

            newEntity.setContent( mapList.isEmpty() ? mapList.toString() : JsonUtils.toJson( mapList ) );
            newEntity.setWorkflow( workflowEntity );
            newEntity.setName( TAB_KEY_DESIGN_SUMMARY );
            // every user should have their own scheme summary
            newEntity.setUserId( UUID.fromString( userId ) );

            schemeSchemaDAO.saveOrUpdate( entityManager, newEntity );
            return mapList;

        } catch ( Exception e ) {
            deleteFileIfExists( outputFilePath.getAbsolutePath() );
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }

    /**
     * Validate user staging path.
     *
     * @param userUid
     *         the user uid
     */
    private void validateUserStagingPath( String userUid ) {
        if ( userUid == null || !Files.exists( Path.of( PropertiesManager.getUserStagingPath( userUid ) ) ) ) {
            throw new SusException( "User " + userUid + " does not have a staging directory" );
        }
    }

    /**
     * Prepare scheme versions for delete.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param schemeId
     *         the scheme id
     *
     * @return the list
     */
    private List< SuSEntity > prepareSchemeVersionsForDelete( EntityManager entityManager, String userIdFromGeneralHeader, UUID schemeId ) {
        List< SuSEntity > schemeVersions = susDAO.getObjectVersionListById( entityManager, WFSchemeEntity.class, schemeId );
        for ( SuSEntity entity : schemeVersions ) {
            entity.setDelete( Boolean.TRUE );
            entity.setDeletedOn( new Date() );
            entity.setModifiedOn( new Date() );
            entity.setDeletedBy(
                    userCommonManager.getUserCommonDAO().findById( entityManager, UUID.fromString( userIdFromGeneralHeader ) ) );
        }
        return schemeVersions;
    }

    /**
     * Prepare and execute algo config.
     *
     * @param userId
     *         the user id
     * @param userUID
     *         the user UID
     * @param workflowEntity
     *         the workflow entity
     * @param algoSchemeDTO
     *         the algo scheme DTO
     */
    private void prepareAndExecuteAlgoConfig( EntityManager entityManager, String userId, String userUID, WorkflowEntity workflowEntity,
            AlgoSchemeDTO algoSchemeDTO ) {
        String inputPath = null;
        try {
            inputPath = createInputFile( entityManager, userUID, userId, workflowEntity.getComposedId().getId().toString(), algoSchemeDTO );
            if ( inputPath != null && new File( inputPath ).exists() ) {
                String[] executeCommand = prepareExecuteCommand( entityManager, userId, algoSchemeDTO, inputPath );
                Process p = Runtime.getRuntime().exec( executeCommand );
                String error = LinuxUtils.stdError( p );
                p.waitFor();
                deleteFileIfExists( inputPath ); // Remove Temporary input json
                if ( error != null && !error.isEmpty() ) {
                    if ( p.exitValue() != 0 ) {
                        log.error( "*****Linux.Utill Command Run ERROR : " + error );
                        throw new SusException( "algo conf call Erorr : " + error );
                    } else {
                        log.warn( "*****Linux.Utill Command Run WARNING : " + error );
                    }
                }
            } else {
                log.debug( "No design variable available " );
            }
        } catch ( IOException e ) {
            deleteFileIfExists( inputPath );
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.PLEASE_CORRECT_ALGO_CONFIG_OF_SCHEME.getKey() ) );
        } catch ( InterruptedException e ) {
            deleteFileIfExists( inputPath );
            log.error( e.getMessage(), e );
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Delete file if exists.
     *
     * @param inputPath
     *         the input path
     */
    private void deleteFileIfExists( String inputPath ) {
        if ( inputPath != null ) {
            File file = new File( inputPath );
            if ( file.exists() ) {
                file.delete();
            }
        }
    }

    /**
     * Prepare execute command.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param algoSchemeDTO
     *         the algo scheme DTO
     * @param inputPath
     *         the input path
     *
     * @return the string[]
     */
    private String[] prepareExecuteCommand( EntityManager entityManager, String userId, AlgoSchemeDTO algoSchemeDTO, String inputPath ) {
        JSONParser parser = new JSONParser();
        Map< String, String > optionsValues = new HashMap<>();
        try {
            for ( Object object : algoSchemeDTO.getOptions() ) {
                JSONObject jsonObj = ( JSONObject ) parser.parse( JsonUtils.toJson( object ) );
                if ( jsonObj.get( "name" ) != null && jsonObj.get( VALUE ) != null ) {
                    optionsValues.put( jsonObj.get( "name" ).toString(), jsonObj.get( VALUE ).toString() );
                }
            }

        } catch ( ParseException e ) {
            log.error( e.getMessage(), e );
        }

        UserDTO user = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );

        String[] cmdForReplacement = algoSchemeDTO.getCommand().getExecute().split( ConstantsString.SPACE );

        for ( int i = 0; i < cmdForReplacement.length; i++ ) {
            String cmd = cmdForReplacement[ i ];
            Matcher matcher = Pattern.compile( "(?<=\\$)\\w+" ).matcher( cmd );
            while ( matcher.find() ) {
                String variable = "{$" + matcher.group() + "}";
                if ( matcher.group().equalsIgnoreCase( "Input" ) ) {
                    cmd = cmd.replace( variable, inputPath );
                    cmdForReplacement[ i ] = cmd;
                }

                if ( matcher.group().equalsIgnoreCase( "output" ) ) {
                    cmd = cmd.replace( variable, PropertiesManager.getUserStagingPath( user.getUserUid() ) + OUTPUT_JSON );
                    cmdForReplacement[ i ] = cmd;
                }

                if ( optionsValues.containsKey( matcher.group() ) ) {
                    cmd = cmd.replace( variable, optionsValues.get( matcher.group() ) );
                    cmdForReplacement[ i ] = cmd;
                }
            }

        }
        return cmdForReplacement;
    }

    /**
     * Gets the algo config from scheme options.
     *
     * @param schemeOptions
     *         the scheme options
     *
     * @return the algo config from scheme options
     */
    private AlgoSchemeDTO getAlgoConfigFromSchemeOptions( EntityManager entityManager, SchemeSchemaEntity schemeOptions ) {
        AlgoSchemeDTO algoSchemeDTO = null;
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonOptions = ( JSONObject ) parser.parse( schemeOptions.getContent() );

            String schemeId = ( String ) jsonOptions.get( SCHEME_LABEL );
            WFSchemeEntity schemeEntity = ( WFSchemeEntity ) susDAO.getLatestObjectById( entityManager, WFSchemeEntity.class,
                    UUID.fromString( schemeId ) );

            try ( InputStream decriptStreamn = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream(
                    new File( PropertiesManager.getVaultPath() + schemeEntity.getAlgoConfig().getFilePath() ),
                    prepareEncryptionDecryptionDTO( schemeEntity.getAlgoConfig().getEncryptionDecryption() ) ) ) {
                algoSchemeDTO = JsonUtils.jsonToObject( decriptStreamn, AlgoSchemeDTO.class );
            }

            addUserValuesToJsonAlgoConfig( jsonOptions, algoSchemeDTO );
        } catch ( IOException | ParseException e ) {
            log.error( e.getMessage(), e );
        }
        return algoSchemeDTO;
    }

    /**
     * Adds the user values to json algo config.
     *
     * @param jsonOptions
     *         the json options
     * @param algoSchemeDTO
     *         the algo scheme DTO
     */
    private void addUserValuesToJsonAlgoConfig( JSONObject jsonOptions, AlgoSchemeDTO algoSchemeDTO ) {
        List< Object > finalOption = new ArrayList<>();
        for ( Object option : algoSchemeDTO.getOptions() ) {
            ObjectMapper mapper = new ObjectMapper();
            Map< String, Object > optionMap = mapper.convertValue( option, Map.class );
            String name = ( String ) optionMap.get( "name" );
            if ( jsonOptions.containsKey( name ) ) {
                optionMap.put( VALUE, jsonOptions.get( name ) );
            }
            finalOption.add( optionMap );
        }
        algoSchemeDTO.setOptions( finalOption );
    }

    /**
     * Validate values.
     *
     * @param entity
     *         the entity
     */
    private void validateValues( DesignVariableEntity entity ) {
        if ( entity != null && null != entity.getType() && entity.getType().equals( "String" ) ) {
            String[] allValues = entity.getDesignVariableValues().split( "," );
            boolean isValidValue = false;
            for ( String value : allValues ) {
                if ( entity.getNominalValue().equalsIgnoreCase( value ) ) {
                    isValidValue = true;
                    break;
                }
            }

            if ( !isValidValue ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NOMINAL_VALUE_IS_MISSING_IN_VALUES.getKey() ) );
            }
        }
    }

    /**
     * Prepare DTO from entity.
     *
     * @param entity
     *         the entity
     *
     * @return the scheme option schema DTO
     */
    private SchemeOptionSchemaDTO prepareDTOFromEntity( SchemeSchemaEntity entity ) {
        return new SchemeOptionSchemaDTO( entity.getId(), entity.getContent(), entity.getWorkflow().getComposedId().getId() );
    }

    /**
     * Creates the input file.
     *
     * @param entityManager
     *         the entity manager
     * @param userUID
     *         the user UID
     * @param userid
     *         the userid
     * @param workflowId
     *         the workflow id
     * @param algoSchemeDTO
     *         the algo scheme DTO
     *
     * @return the string
     */
    private String createInputFile( EntityManager entityManager, String userUID, String userid, String workflowId,
            AlgoSchemeDTO algoSchemeDTO ) {
        List< DesignVariableEntity > designVariables = variableDAO.getAllDesignVariables( entityManager, workflowId, userid );
        if ( !validateDesignVariableEntityList( designVariables ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DESIGN_VARIABLES_REQUIRED_FIELD_IS_MISSING.getKey() ) );
        }
        String filePath = null;
        if ( CollectionUtil.isEmpty( designVariables ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DESIGN_VARIABLES_UNDEFINED_IN_WORKFLOW.getKey() ) );
        }

        try {
            List< Object > designVariablesDTO = new ArrayList<>();
            if ( CollectionUtil.isNotEmpty( designVariables ) ) {
                for ( DesignVariableEntity designVariableEntity : designVariables ) {
                    designVariablesDTO.add( prepareDesignVariableDTOFromEntity( designVariableEntity ) );
                }
            }

            if ( !designVariablesDTO.isEmpty() && null != algoSchemeDTO.getGenerateInput() ) {
                File inputFile = new File( PropertiesManager.getDefaultServerTempPath() );
                inputFile = File.createTempFile( "input", ".csv", inputFile );
                FileUtils.setGlobalAllFilePermissions( inputFile );
                filePath = inputFile.getAbsolutePath();

                PythonUtils.callGenerateInputPythonFile( userUID, algoSchemeDTO.getGenerateInput(), filePath,
                        JsonUtils.toJson( designVariablesDTO ) );
            }
            return filePath;
        } catch ( IOException e ) {
            deleteFileIfExists( filePath );
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_CREATED.getKey(), "input.csv" ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkSyntax( String userId, String workflowId, String expression ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UserDTO user = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );
            List< Object > designVariables = new ArrayList<>();
            List< DesignVariableEntity > designVariableEntities = variableDAO.getAllDesignVariables( entityManager, workflowId, userId );
            if ( CollectionUtil.isNotEmpty( designVariableEntities ) ) {
                for ( DesignVariableEntity designVariableEntity : designVariableEntities ) {
                    designVariables.add( prepareDesignVariableDTOFromEntity( designVariableEntity ) );
                }
            }

            WorkflowEntity workflowEntity = workflowManager.prepareWorkflowEntity(
                    workflowManager.getWorkflowById( entityManager, UUID.fromString( userId ), workflowId ) );
            SchemeSchemaEntity schemeOptions = schemeSchemaDAO.getSchemeSchemaByWorkflowIdAndName( entityManager,
                    UUID.fromString( workflowId ), workflowEntity.getVersionId(), TAB_KEY_SCHEME_OPTIONS, UUID.fromString( userId ) );
            if ( schemeOptions == null ) {
                return false;
            }
            AlgoSchemeDTO jsonAlgoConfig = getAlgoConfigFromSchemeOptions( entityManager, schemeOptions );
            String prepareInputFromPythonPath = jsonAlgoConfig != null ? replaceKarafPath( jsonAlgoConfig ).getFilterOutput() : null;
            if ( !designVariables.isEmpty() && null != prepareInputFromPythonPath ) {
                ProcessResult result = PythonUtils.callFilterOutput( user.getUserUid(), prepareInputFromPythonPath, "check", expression,
                        JsonUtils.toJson( designVariables ) );
                if ( null != result.getErrorStreamString() ) {
                    throw new SusException( result.getErrorStreamString() );
                }
            }
            return true;
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
     * Prepare objective variable model from entity.
     *
     * @param objectiveVariableEntity
     *         the objective variable entity
     *
     * @return the objective variable DTO
     */
    private ObjectiveVariableDTO prepareObjectiveVariableModelFromEntity( ObjectiveVariableEntity objectiveVariableEntity ) {
        ObjectiveVariableDTO dto = null;
        if ( objectiveVariableEntity != null ) {
            dto = new ObjectiveVariableDTO( objectiveVariableEntity.getId(), objectiveVariableEntity.getLabel(),
                    objectiveVariableEntity.getName(), objectiveVariableEntity.getNominalValue(),
                    objectiveVariableEntity.getWorkflow().getComposedId().getId() );
            dto.setGoal( objectiveVariableEntity.getGoal() );
            dto.setOptions( objectiveVariableEntity.getOptions() );
        }
        return dto;
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
     * Prepare design variable DTO from entity.
     *
     * @param designVariableEntity
     *         the design variable entity
     *
     * @return the object
     */
    private Object prepareDesignVariableDTOFromEntity( DesignVariableEntity designVariableEntity ) {

        if ( null != designVariableEntity && designVariableEntity.getAlgoType() != null ) {
            if ( designVariableEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.DEFAULT.getKey() ) ) {
                return prepareDefaultDesignVariableDTO( designVariableEntity );
            } else if ( designVariableEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.MMS.getKey() ) ) {
                return prepareMMSDesignVariableDTO( designVariableEntity );
            } else if ( designVariableEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.MML.getKey() ) ) {
                return prepareMMLDesignVariableDTO( designVariableEntity );
            } else if ( designVariableEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.VAL.getKey() ) ) {
                return prepareVALDesignVariableDTO( designVariableEntity );
            } else if ( designVariableEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.LEVELS.getKey() ) ) {
                return prepareLEVELDesignVariableDTO( designVariableEntity );
            } else if ( designVariableEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.SSI.getKey() ) ) {
                return prepareSSIDesignVariableDTO( designVariableEntity );
            } else if ( designVariableEntity.getAlgoType().equalsIgnoreCase( AlgoTypeEnum.MM.getKey() ) ) {
                return prepareMMDesignVariableDTO( designVariableEntity );
            }
        }
        return null;

    }

    // prepareMMDesignVariableDTO

    /**
     * Prepare MM design variable DTO.
     *
     * @param designVariableEntity
     *         the design variable entity
     *
     * @return the object
     */
    private Object prepareMMDesignVariableDTO( DesignVariableEntity designVariableEntity ) {
        MMDesignVariableDTO mmDesignVariableDTO = new MMDesignVariableDTO();

        mmDesignVariableDTO.setMin( designVariableEntity.getDesignVariableMin() );
        mmDesignVariableDTO.setMax( designVariableEntity.getDesignVariableMax() );
        mmDesignVariableDTO.setLabel( designVariableEntity.getLabel() );
        mmDesignVariableDTO.setName( designVariableEntity.getName() );
        mmDesignVariableDTO.setType( designVariableEntity.getType() );
        mmDesignVariableDTO.setNominalValue( designVariableEntity.getNominalValue() );
        mmDesignVariableDTO.setWorkflowId( designVariableEntity.getWorkflow().getComposedId().getId() );
        mmDesignVariableDTO.setId( designVariableEntity.getId() );
        mmDesignVariableDTO.setIndex( designVariableEntity.getIndexNumber() );
        mmDesignVariableDTO.setCreatedOn( designVariableEntity.getCreatedOn() );
        mmDesignVariableDTO.setAlgoType( designVariableEntity.getAlgoType() );
        return mmDesignVariableDTO;
    }

    /**
     * Prepare SSI design variable DTO.
     *
     * @param designVariableEntity
     *         the design variable entity
     *
     * @return the object
     */
    private Object prepareSSIDesignVariableDTO( DesignVariableEntity designVariableEntity ) {
        SSIDesignVariableDTO multiLevelDesignVariableDTO = new SSIDesignVariableDTO();

        multiLevelDesignVariableDTO.setStart( designVariableEntity.getStart() );
        multiLevelDesignVariableDTO.setStop( designVariableEntity.getStop() );
        multiLevelDesignVariableDTO.setInterval( designVariableEntity.getInterval() );
        multiLevelDesignVariableDTO.setLabel( designVariableEntity.getLabel() );
        multiLevelDesignVariableDTO.setName( designVariableEntity.getName() );
        multiLevelDesignVariableDTO.setType( designVariableEntity.getType() );
        multiLevelDesignVariableDTO.setNominalValue( designVariableEntity.getNominalValue() );
        multiLevelDesignVariableDTO.setWorkflowId( designVariableEntity.getWorkflow().getComposedId().getId() );
        multiLevelDesignVariableDTO.setId( designVariableEntity.getId() );
        multiLevelDesignVariableDTO.setIndex( designVariableEntity.getIndexNumber() );
        multiLevelDesignVariableDTO.setCreatedOn( designVariableEntity.getCreatedOn() );
        multiLevelDesignVariableDTO.setAlgoType( designVariableEntity.getAlgoType() );
        return multiLevelDesignVariableDTO;
    }

    /**
     * Prepare VAL design variable DTO.
     *
     * @param designVariableEntity
     *         the design variable entity
     *
     * @return the object
     */
    private Object prepareVALDesignVariableDTO( DesignVariableEntity designVariableEntity ) {
        VALDesignVariableDTO multiLevelDesignVariableDTO = new VALDesignVariableDTO();

        multiLevelDesignVariableDTO.setValues( designVariableEntity.getDesignVariableValues() );
        multiLevelDesignVariableDTO.setLabel( designVariableEntity.getLabel() );
        multiLevelDesignVariableDTO.setName( designVariableEntity.getName() );
        multiLevelDesignVariableDTO.setType( designVariableEntity.getType() );
        multiLevelDesignVariableDTO.setNominalValue( designVariableEntity.getNominalValue() );
        multiLevelDesignVariableDTO.setWorkflowId( designVariableEntity.getWorkflow().getComposedId().getId() );
        multiLevelDesignVariableDTO.setId( designVariableEntity.getId() );
        multiLevelDesignVariableDTO.setIndex( designVariableEntity.getIndexNumber() );
        multiLevelDesignVariableDTO.setCreatedOn( designVariableEntity.getCreatedOn() );
        multiLevelDesignVariableDTO.setAlgoType( designVariableEntity.getAlgoType() );
        return multiLevelDesignVariableDTO;
    }

    // prepareLEVELDesignVariableDTO

    /**
     * Prepare LEVEL design variable DTO.
     *
     * @param designVariableEntity
     *         the design variable entity
     *
     * @return the object
     */
    private Object prepareLEVELDesignVariableDTO( DesignVariableEntity designVariableEntity ) {
        LEVELDesignVariableDTO levelDesignVariableDTO = new LEVELDesignVariableDTO();

        levelDesignVariableDTO.setLevels( designVariableEntity.getLevel() );
        levelDesignVariableDTO.setLabel( designVariableEntity.getLabel() );
        levelDesignVariableDTO.setName( designVariableEntity.getName() );
        levelDesignVariableDTO.setType( designVariableEntity.getType() );
        levelDesignVariableDTO.setNominalValue( designVariableEntity.getNominalValue() );
        levelDesignVariableDTO.setWorkflowId( designVariableEntity.getWorkflow().getComposedId().getId() );
        levelDesignVariableDTO.setId( designVariableEntity.getId() );
        levelDesignVariableDTO.setIndex( designVariableEntity.getIndexNumber() );
        levelDesignVariableDTO.setCreatedOn( designVariableEntity.getCreatedOn() );
        levelDesignVariableDTO.setAlgoType( designVariableEntity.getAlgoType() );
        return levelDesignVariableDTO;
    }

    /**
     * Prepare MML design variable DTO.
     *
     * @param designVariableEntity
     *         the design variable entity
     *
     * @return the object
     */
    private Object prepareMMLDesignVariableDTO( DesignVariableEntity designVariableEntity ) {
        MMLDesignVariableDTO multiLevelDesignVariableDTO = new MMLDesignVariableDTO();
        multiLevelDesignVariableDTO.setMax( designVariableEntity.getDesignVariableMax() );
        multiLevelDesignVariableDTO.setMin( designVariableEntity.getDesignVariableMin() );
        multiLevelDesignVariableDTO.setLevel( designVariableEntity.getLevel() );
        multiLevelDesignVariableDTO.setLabel( designVariableEntity.getLabel() );
        multiLevelDesignVariableDTO.setName( designVariableEntity.getName() );
        multiLevelDesignVariableDTO.setType( designVariableEntity.getType() );
        multiLevelDesignVariableDTO.setNominalValue( designVariableEntity.getNominalValue() );
        multiLevelDesignVariableDTO.setWorkflowId( designVariableEntity.getWorkflow().getComposedId().getId() );
        multiLevelDesignVariableDTO.setId( designVariableEntity.getId() );
        multiLevelDesignVariableDTO.setIndex( designVariableEntity.getIndexNumber() );
        multiLevelDesignVariableDTO.setCreatedOn( designVariableEntity.getCreatedOn() );
        multiLevelDesignVariableDTO.setAlgoType( designVariableEntity.getAlgoType() );
        return multiLevelDesignVariableDTO;
    }

    /**
     * Prepare MMS design variable DTO.
     *
     * @param designVariableEntity
     *         the design variable entity
     *
     * @return the object
     */
    private Object prepareMMSDesignVariableDTO( DesignVariableEntity designVariableEntity ) {
        MMSDesignVariableDTO multiLevelDesignVariableDTO = new MMSDesignVariableDTO();
        multiLevelDesignVariableDTO.setMax( designVariableEntity.getDesignVariableMax() );
        multiLevelDesignVariableDTO.setMin( designVariableEntity.getDesignVariableMin() );
        multiLevelDesignVariableDTO.setStep( designVariableEntity.getStep() );
        multiLevelDesignVariableDTO.setLabel( designVariableEntity.getLabel() );
        multiLevelDesignVariableDTO.setName( designVariableEntity.getName() );
        multiLevelDesignVariableDTO.setType( designVariableEntity.getType() );
        multiLevelDesignVariableDTO.setNominalValue( designVariableEntity.getNominalValue() );
        multiLevelDesignVariableDTO.setWorkflowId( designVariableEntity.getWorkflow().getComposedId().getId() );
        multiLevelDesignVariableDTO.setId( designVariableEntity.getId() );
        multiLevelDesignVariableDTO.setIndex( designVariableEntity.getIndexNumber() );
        multiLevelDesignVariableDTO.setCreatedOn( designVariableEntity.getCreatedOn() );
        multiLevelDesignVariableDTO.setAlgoType( designVariableEntity.getAlgoType() );
        return multiLevelDesignVariableDTO;
    }

    /**
     * Prepare default design variable DTO.
     *
     * @param designVariableEntity
     *         the design variable entity
     *
     * @return the object
     */
    private Object prepareDefaultDesignVariableDTO( DesignVariableEntity designVariableEntity ) {
        DefaultDesignVariableDTO defaultDesignVariableDTO = new DefaultDesignVariableDTO();
        defaultDesignVariableDTO.setMax( designVariableEntity.getDesignVariableMax() );
        defaultDesignVariableDTO.setMin( designVariableEntity.getDesignVariableMin() );
        defaultDesignVariableDTO.setStep( designVariableEntity.getStep() );
        defaultDesignVariableDTO.setValues( designVariableEntity.getDesignVariableValues() );
        defaultDesignVariableDTO.setOptional( designVariableEntity.getOptional() );
        defaultDesignVariableDTO.setLabel( designVariableEntity.getLabel() );
        defaultDesignVariableDTO.setName( designVariableEntity.getName() );
        defaultDesignVariableDTO.setType( designVariableEntity.getType() );
        defaultDesignVariableDTO.setNominalValue( designVariableEntity.getNominalValue() );
        defaultDesignVariableDTO.setWorkflowId( designVariableEntity.getWorkflow().getComposedId().getId() );
        defaultDesignVariableDTO.setId( designVariableEntity.getId() );
        defaultDesignVariableDTO.setIndex( designVariableEntity.getIndexNumber() );
        defaultDesignVariableDTO.setCreatedOn( designVariableEntity.getCreatedOn() );
        defaultDesignVariableDTO.setAlgoType( designVariableEntity.getAlgoType() );
        return defaultDesignVariableDTO;
    }

    /**
     * Prepare label DTO from entity.
     *
     * @param designVariableEntity
     *         the design variable entity
     *
     * @return the label DTO
     */
    private LabelDTO prepareLabelDTOFromEntity( DesignVariableEntity designVariableEntity ) {
        LabelDTO labelDTO = null;
        if ( null != designVariableEntity ) {
            labelDTO = new LabelDTO();
            labelDTO.setId( designVariableEntity.getId() );
            labelDTO.setName( designVariableEntity.getLabel() );
        }
        return labelDTO;
    }

    /**
     * Prepare objective variable entity from DTO.
     *
     * @param objectiveVariableDTO
     *         the objective variable DTO
     *
     * @return the objective variable entity
     */
    private ObjectiveVariableEntity prepareObjectiveVariableEntityFromDTO( ObjectiveVariableDTO objectiveVariableDTO ) {
        ObjectiveVariableEntity entity = null;
        if ( null != objectiveVariableDTO ) {
            entity = new ObjectiveVariableEntity( objectiveVariableDTO.getLabel(), objectiveVariableDTO.getName(),
                    objectiveVariableDTO.getNominalValue() );
            entity.setId( objectiveVariableDTO.getId() );
            entity.setGoal( objectiveVariableDTO.getGoal() );
            entity.setOptions( objectiveVariableDTO.getOptions() );
        }
        return entity;
    }

    /**
     * Prepare objective variable entity from DTO.
     *
     * @param dto
     *         the objective variable DTO
     * @param entity
     *         the entity
     *
     * @return the objective variable entity
     */
    private ObjectiveVariableEntity prepareObjectiveVariableEntityFromDTO( ObjectiveVariableDTO dto, ObjectiveVariableEntity entity ) {
        if ( dto != null && entity != null ) {
            entity.setGoal( dto.getGoal() );
            entity.setOptions( dto.getOptions() );
        }
        return entity;
    }

    /**
     * Gets the design variable.
     *
     * @param entityManager
     *         the entity manager
     * @param designVariableId
     *         the design variable id
     *
     * @return the design variable
     */
    private Object getDesignVariable( EntityManager entityManager, String designVariableId ) {
        return prepareDesignVariableDTOFromEntity( variableDAO.readDesignVariable( entityManager, UUID.fromString( designVariableId ) ) );
    }

    /**
     * Prepare response.
     *
     * @param scanObj
     *         the scan obj
     * @param matcher
     *         the matcher
     * @param matcherAllCount
     *         the matcher all count
     *
     * @return the scan response DTO
     */
    private ScanResponseDTO prepareResponse( ScanObjectDTO scanObj, Matcher matcher, Matcher matcherAllCount ) {
        ScanResponseDTO response = new ScanResponseDTO();
        int objectiveMatch = Integer.parseInt( scanObj.getVariableMatch() );
        int objectiveGroup = Integer.parseInt( scanObj.getVariableGroup() );
        int count = 1;
        int matchNumber = ScanObjectUtil.getObjectMatch( matcherAllCount ) + ( objectiveMatch + ConstantsInteger.INTEGER_VALUE_ONE );

        while ( matcher.find() ) {
            if ( scanObj.getVariableMatch().isEmpty() || ConstantsInteger.INTEGER_VALUE_ZERO == objectiveMatch ) {
                ScanObjectUtil.setFirstMatch( scanObj, matcher, response );
                break;
            } else if ( objectiveMatch < 0 && matchNumber == count ) {
                // Objective Match is a negative number i.e -1 = get last match
                prepareMatchResponse( matcher, matchNumber, response, objectiveGroup );
                response.setLineNumber( scanObj.getLineNumber() );
                response.setLineNumberIndexed( String.valueOf( Integer.parseInt( scanObj.getLineNumber() ) + 1 ) );
                break;
            } else {
                if ( objectiveMatch == count ) {
                    try {
                        response.setMatch( matcher.group( objectiveGroup ) );
                        response.setStart( Integer.toString( matcher.start( objectiveGroup ) ) );
                        response.setEnd( Integer.toString( matcher.end( objectiveGroup ) ) );
                    } catch ( Exception e ) {
                        log.warn( e.getMessage() );
                        response.setMatch( matcher.group() );
                        response.setStart( Integer.toString( matcher.start() ) );
                        response.setEnd( Integer.toString( matcher.end() ) );
                    }
                    response.setLineNumber( scanObj.getLineNumber() );
                    response.setLineNumberIndexed( String.valueOf( Integer.parseInt( scanObj.getLineNumber() ) + 1 ) );
                }
            }
            count++;
        }
        return response;
    }

    /**
     * Prepare match response.
     *
     * @param matcher
     *         the matcher
     * @param matchNumber
     *         the match number
     * @param response
     *         the response
     * @param objectiveGroup
     *         the objective group
     */
    private void prepareMatchResponse( Matcher matcher, int matchNumber, ScanResponseDTO response, int objectiveGroup ) {
        response.setMatch( matcher.group( matchNumber ) );
        try {
            response.setStart( Integer.toString( matcher.start( objectiveGroup ) ) );
            response.setEnd( Integer.toString( matcher.end( objectiveGroup ) ) );
        } catch ( Exception e ) {
            log.warn( e.getMessage() );
            response.setStart( Integer.toString( matcher.start() ) );
            response.setEnd( Integer.toString( matcher.end() ) );
        }
    }

    /**
     * Prepare scheme drop down.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param categoryId
     *         the category id
     *
     * @return the select UI
     */
    private SelectFormItem prepareSchemeDropDown( EntityManager entityManager, String userId, String workflowId, int categoryId ) {
        List< SelectOptionsUI > options = new ArrayList<>();
        userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.WFSCHEMES.getId(), PermissionMatrixEnum.READ.getValue(),
                Messages.NO_RIGHTS_TO_READ.getKey(), WFSCHEME );

        List< WFSchemeEntity > schemeEntities = wFSchemeDAO.getLatestNonDeletedWFSchemeListByProperty( entityManager, CATEGORY,
                categoryId );
        for ( SuSEntity suSEntity : schemeEntities ) {
            SelectOptionsUI objectItem = new SelectOptionsUI();
            objectItem.setId( suSEntity.getComposedId().getId().toString() );
            objectItem.setName( suSEntity.getName() );
            options.add( objectItem );
        }
        SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        item.setLabel( SELECT_SCHEME_LABEL );
        item.setOptions( options );
        item.setName( SCHEME_LABEL );
        item.setType( FIELD_TYPE_SELECT );
        item.setBindFrom(
                BIND_FROM_SCHEME_URL_FOR_RUN_SCHEME_UI_FORM.replace( PARAM_WORKFLOW_ID, ConstantsString.EMPTY_STRING + workflowId ) );
        Map< String, String > optionScheme = getOptionScheme( entityManager, userId, workflowId, TAB_KEY_SCHEME_OPTIONS );
        item.setValue( optionScheme.isEmpty() ? ConstantsString.EMPTY_STRING : optionScheme.get( SCHEME_LABEL ) );
        setRulesAndMessageOnUI( item );
        setLiveSearch( item );
        return item;
    }

    /**
     * Sets the live search.
     *
     * @param selectFormItemItem
     *         the new live search
     */
    private void setLiveSearch( SelectFormItem selectFormItemItem ) {
        Map< String, Object > liveSearch = new HashMap<>();
        liveSearch.put( LIVESEARCH, true );
        selectFormItemItem.setPicker( liveSearch );
    }

    /**
     * Prepare options.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param schemeId
     *         the scheme id
     *
     * @return the list
     */
    public UIForm prepareOptions( EntityManager entityManager, String userId, String workflowId, String schemeId ) {
        Map< String, Object > algoConfig = new HashMap<>();
        WFSchemeEntity schemeEntity = ( WFSchemeEntity ) susDAO.getLatestObjectById( entityManager, WFSchemeEntity.class,
                UUID.fromString( schemeId ) );
        DocumentEntity config = schemeEntity.getAlgoConfig();
        File file = new File( PropertiesManager.getVaultPath() + config.getFilePath() );
        if ( !file.exists() ) {
            throw new SusException( FILE_NOT_EXISTS_IN_VAULT );
        }

        try ( InputStream decritedStreamDromVault = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream( file,
                prepareEncryptionDecryptionDTO( config.getEncryptionDecryption() ) ) ) {
            JSONParser jsonParser = new JSONParser();
            Object objFile = jsonParser.parse( new InputStreamReader( decritedStreamDromVault ) );
            algoConfig = ( Map< String, Object > ) JsonUtils.jsonToMap( JsonUtils.toJson( objFile ), algoConfig );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }

        List< UIFormItem > retList = new ArrayList<>();
        Map< String, String > optionScheme = getOptionScheme( entityManager, userId, workflowId, TAB_KEY_SCHEME_OPTIONS );
        for ( UIFormItem uiFormItem : JsonUtils.jsonToList( JsonUtils.objectToJson( algoConfig.get( OPTIONS ) ), SelectFormItem.class ) ) {
            Object savedSchemeOption = optionScheme.get( uiFormItem.getName() );
            if ( null != savedSchemeOption ) {
                uiFormItem.setValue( optionScheme.isEmpty() ? "" : savedSchemeOption );
            }

            retList.add( uiFormItem );

        }
        return GUIUtils.createFormFromItems( retList );
    }

    /**
     * Validate ids.
     *
     * @param workflowId
     *         the workflow id
     * @param objectId
     *         the object id
     *
     * @return the notification
     */
    private Notification validateIds( String workflowId, String objectId ) {
        Notification notify = new Notification();
        if ( StringUtils.isBlank( workflowId ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.PARENT_ID_CANNOT_BE_EMPTY.getKey() ) ) );
        } else if ( !StringUtils.isBlank( workflowId ) && !ValidationUtils.validateUUIDString( workflowId ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.PARENT_ID_IS_NOT_VALID.getKey(), workflowId ) ) );
        }
        if ( StringUtils.isBlank( objectId ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.OBJECT_ID_CANNOT_EMPTY.getKey() ) ) );
        }
        return notify;
    }

    /**
     * Checks if is permittedto read.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param id
     *         the id
     */
    private void isPermittedtoRead( EntityManager entityManager, String userId, String id ) {
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                id + ConstantsString.COLON + PermissionMatrixEnum.READ.getValue() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_READ.getKey(), OBJECT ) );
        }
    }

    /**
     * Gets the WF scheme.
     *
     * @param entityManager
     *         the entity manager
     * @param schemeId
     *         the scheme id
     *
     * @return the WF scheme
     */
    private Object getWFScheme( EntityManager entityManager, UUID schemeId ) {
        return createSchemeDTOFromEntity( ( WFSchemeEntity ) susDAO.getLatestNonDeletedObjectById( entityManager, schemeId ) );
    }

    /**
     * Creates the WF scheme entity from DTO.
     *
     * @param schemeDTO
     *         the scheme DTO
     *
     * @return the WF scheme entity
     */
    private WFSchemeEntity createWFSchemeEntityFromDTO( WFSchemeDTO schemeDTO ) {
        WFSchemeEntity schemeEntity = new WFSchemeEntity();
        schemeEntity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), 1 ) );
        schemeEntity.setName( schemeDTO.getName() );
        schemeEntity.setDescription( schemeDTO.getDescription() );
        schemeEntity.setCategory( Integer.parseInt( schemeDTO.getCategoryId() ) );
        schemeEntity.setAlgoConfig( documentManager.prepareEntityFromDocumentDTO( schemeDTO.getAlgoConfig() ) );
        schemeEntity.setCreatedOn( new Date() );
        schemeEntity.setModifiedOn( new Date() );
        return schemeEntity;
    }

    /**
     * Creates the scheme DTO from entity.
     *
     * @param schemeEntity
     *         the scheme entity
     *
     * @return the WF scheme DTO
     */
    private WFSchemeDTO createSchemeDTOFromEntity( WFSchemeEntity schemeEntity ) {
        WFSchemeDTO wfSchemeDTO = new WFSchemeDTO();
        wfSchemeDTO.setId( schemeEntity.getComposedId().getId() );
        wfSchemeDTO.setName( schemeEntity.getName() );
        wfSchemeDTO.setDescription( schemeEntity.getDescription() );
        SchemeCategoryDTO schemeCategoryDTO = new SchemeCategoryDTO();
        schemeCategoryDTO.setId( schemeEntity.getCategory() );
        schemeCategoryDTO.setName( SchemeCategoryEnum.getById( schemeEntity.getCategory() ).getValue() );
        wfSchemeDTO.setCategory( schemeCategoryDTO );
        wfSchemeDTO.setAlgoConfig( documentManager.prepareDocumentDTO( schemeEntity.getAlgoConfig() ) );
        return wfSchemeDTO;
    }

    /**
     * Sets the rules and message on UI.
     *
     * @param uiFormItem
     *         the new rules and message on UI
     */
    private void setRulesAndMessageOnUI( UIFormItem uiFormItem ) {
        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();
        rules.put( REQUIRED, true );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        uiFormItem.setRules( rules );
        uiFormItem.setMessages( message );
    }

    /**
     * Populate category dropdown options.
     *
     * @param entityManager
     *         the entity manager
     * @param uiFormItem
     *         the ui form item
     * @param schemeId
     *         the scheme id
     */
    private void populateCategoryDropdownOptions( EntityManager entityManager, UIFormItem uiFormItem, String schemeId ) {

        List< String > allCategories = SchemeCategoryEnum.getValues();
        WFSchemeEntity entity = null;
        if ( null != schemeId ) {
            entity = ( WFSchemeEntity ) susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( schemeId ) );
        }
        List< SelectOptionsUI > options = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( allCategories ) ) {
            for ( String category : allCategories ) {
                SelectOptionsUI categoryOption = new SelectOptionsUI();
                categoryOption.setId( String.valueOf( SchemeCategoryEnum.getByValue( category ).getKey() ) );
                categoryOption.setName( category );
                options.add( categoryOption );
            }
            GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, options,
                    null == entity ? null : String.valueOf( entity.getCategory() ), false );
        }
    }

    /**
     * Adds the to acl.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param createdEntity
     *         the created entity
     * @param parentEntity
     *         the parent entity
     */
    private void addToAcl( EntityManager entityManager, String userId, SuSEntity createdEntity, SuSEntity parentEntity ) {
        log.info( MessageBundleFactory.getMessage( Messages.SAVING_OBJECT_AS_RESOURCE.getKey() ) );
        ResourceAccessControlDTO resourceAccessControlDTO = permissionManager.prepareResourceAccessControlDTOFromObject( entityManager,
                createdEntity, parentEntity );
        if ( resourceAccessControlDTO != null ) {
            resourceAccessControlDTO.setSecurityIdentity(
                    userCommonManager.getAclCommonSecurityIdentityDAO().getSecurityIdentityBySid( entityManager, UUID.fromString( userId ) )
                            .getId() );
            permissionManager.addObjectToAcl( entityManager, resourceAccessControlDTO );
            permissionManager.saveSelectionForNewEntity( entityManager, userId, createdEntity, parentEntity );
        }
    }

    /**
     * Gets the sus DAO.
     *
     * @return the sus DAO
     */
    public SuSGenericObjectDAO< SuSEntity > getSusDAO() {
        return susDAO;
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
     * Gets the user manager.
     *
     * @return the user manager
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Sets the user manager.
     *
     * @param userManager
     *         the new user manager
     */
    public void setUserManager( UserManager userManager ) {
        this.userManager = userManager;
    }

    /**
     * Gets the document manager.
     *
     * @return the document manager
     */
    public DocumentManager getDocumentManager() {
        return documentManager;
    }

    /**
     * Sets the document manager.
     *
     * @param documentManager
     *         the new document manager
     */
    public void setDocumentManager( DocumentManager documentManager ) {
        this.documentManager = documentManager;
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
     * Gets the permission manager.
     *
     * @return the permission manager
     */
    public PermissionManager getPermissionManager() {
        return permissionManager;
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
     * Sets the design variable dao.
     *
     * @param variableDAO
     *         the new design variable dao
     */
    public void setVariableDAO( VariableDAO variableDAO ) {
        this.variableDAO = variableDAO;
    }

    /**
     * {@inheritDoc}
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
     * Gets the scheme schema DAO.
     *
     * @return the scheme schema DAO
     */
    public SchemeSchemaDAO getSchemeSchemaDAO() {
        return schemeSchemaDAO;
    }

    /**
     * Sets the scheme schema DAO.
     *
     * @param schemeSchemaDAO
     *         the new scheme schema DAO
     */
    public void setSchemeSchemaDAO( SchemeSchemaDAO schemeSchemaDAO ) {
        this.schemeSchemaDAO = schemeSchemaDAO;
    }

    /**
     * Gets the design variable label DAO.
     *
     * @return the design variable label DAO
     */
    public DesignVariableLabelDAO getDesignVariableLabelDAO() {
        return designVariableLabelDAO;
    }

    /**
     * Sets the design variable label DAO.
     *
     * @param designVariableLabelDAO
     *         the new design variable label DAO
     */
    public void setDesignVariableLabelDAO( DesignVariableLabelDAO designVariableLabelDAO ) {
        this.designVariableLabelDAO = designVariableLabelDAO;
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
     * Sets the license manager.
     *
     * @param licenseManager
     *         the new license manager
     */
    public void setLicenseManager( LicenseManager licenseManager ) {
        this.licenseManager = licenseManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDesignSummaryImportUI( String workflowId ) {
        List< UIFormItem > parserForm = new ArrayList<>();
        parserForm.add( prepareLocalFileSelectForm() );
        return parserForm;
    }

    /**
     * Prepare local file select form.
     *
     * @return the UI form item
     */
    private UIFormItem prepareLocalFileSelectForm() {

        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( "designsummary" );
        selectFormItem.setLabel( "Import design summary" );
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
     * {@inheritDoc}
     */
    @Override
    public Object updateDesignSummaryWithImportedCSV( String workflowId, String docId, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< Map< String, Object > > mapList = new ArrayList<>();
        try {
            WorkflowEntity workflowEntity = workflowManager.prepareWorkflowEntity(
                    workflowManager.getWorkflowById( entityManager, UUID.fromString( userId ), workflowId ) );

            SchemeSchemaEntity entity = schemeSchemaDAO.getSchemeSchemaByWorkflowIdAndName( entityManager, UUID.fromString( workflowId ),
                    workflowEntity.getVersionId(), TAB_KEY_DESIGN_SUMMARY, UUID.fromString( userId ) );

            List< DesignVariableEntity > designVariableEntityList = variableDAO.getAllDesignVariables( entityManager, workflowId,
                    workflowEntity.getCreatedBy().getId().toString() );

            List< String > exisitingHeadList = prepareExistingHeadersOfDesignSummary( designVariableEntityList );

            InputStream decryptedFileStream = SuSVaultUtils.getDecryptionSteamFromPath( UUID.fromString( docId ), 1,
                    PropertiesManager.getVaultPath(), PropertiesManager.getEncryptionDecryptionDTO() );
            BufferedReader csvReader = new BufferedReader( new InputStreamReader( decryptedFileStream, StandardCharsets.UTF_8 ) );

            List< String[] > csvDatalist = new ArrayList<>();
            String row;
            while ( ( row = csvReader.readLine() ) != null ) {
                String[] data = row.split( "," );
                csvDatalist.add( data );
            }
            csvReader.close();

            // extract header from csv data
            List< String > myHeader = new ArrayList<>();
            if ( !csvDatalist.isEmpty() ) {
                for ( String head : csvDatalist.get( 0 ) ) {
                    myHeader.add( head.trim() );
                }
                csvDatalist.remove( 0 );
            }

            compareExistingHeadersToCsvHeaders( exisitingHeadList, myHeader );

            // extract data and prepare map
            int exp = 1;
            for ( String[] dataArray : csvDatalist ) {
                Map< String, Object > map = new HashMap<>();
                for ( int i = 0; i < dataArray.length; i++ ) {
                    String val = dataArray[ i ];
                    String head = myHeader.get( i );
                    map.put( head, val.trim() );
                }
                map.put( EXP_NUM, exp );
                map.put( "id", exp );
                mapList.add( map );
                exp++;
            }
            String csvJson = JsonUtils.toJson( mapList );

            if ( entity != null ) {
                entity.setContent( csvJson );
            } else {
                entity = new SchemeSchemaEntity();
                entity.setId( UUID.randomUUID() );

                entity.setContent( csvJson );
                entity.setWorkflow( workflowEntity );
                entity.setName( TAB_KEY_DESIGN_SUMMARY );
                // every user should have their own scheme summary
                entity.setUserId( UUID.fromString( userId ) );
            }

            schemeSchemaDAO.saveOrUpdate( entityManager, entity );

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        } finally {
            entityManager.close();
            // delete imported document from Vault after import
            SuSVaultUtils.deleteFileFromVault( UUID.fromString( docId ), 1, PropertiesManager.getVaultPath() );
        }

        return mapList;
    }

    /**
     * Prepare existing headers of design summary.
     *
     * @param designVariableEntityList
     *         the design variable entity list
     *
     * @return the list
     */
    private List< String > prepareExistingHeadersOfDesignSummary( List< DesignVariableEntity > designVariableEntityList ) {
        List< String > exisitingHeadList = new ArrayList<>();
        for ( DesignVariableEntity designVariableEntity : designVariableEntityList ) {
            if ( !designVariableEntity.getLabel().equals( EXP_NUM ) && !designVariableEntity.getLabel().equals( "id" ) ) {
                exisitingHeadList.add( designVariableEntity.getLabel() );
            }
        }
        return exisitingHeadList;
    }

    /**
     * Compare existing headers to csv headers.
     *
     * @param exisitingHeadList
     *         the exisiting head list
     * @param myHeader
     *         the my header
     */
    private void compareExistingHeadersToCsvHeaders( List< String > exisitingHeadList, List< String > myHeader ) {
        exisitingHeadList.removeAll( myHeader );
        if ( !exisitingHeadList.isEmpty() ) {
            throw new SusException( "Import Failed :Wrong File OR Changed CSV Headers :" + exisitingHeadList );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getPostProcessForm( String userIdFromGeneralHeader, String workflowId ) {
        List< UIFormItem > listUserDirectory = new ArrayList<>();
        listUserDirectory.add( prepareObjectSelectorForm() );
        return GUIUtils.createFormFromItems( listUserDirectory );
    }

    /**
     * Prepare object selector form.
     *
     * @return the UI form item
     */
    private UIFormItem prepareObjectSelectorForm() {

        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( "postProcessWorkflow.id" );
        selectFormItem.setLabel( "PostProcessWorkflow" );
        selectFormItem.setType( "object-workflow" );

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
    public Object getWorkflowFromPostProcessForm( String userIdFromGeneralHeader, String sWf ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UUID > ids = selectionManager.getSelectedIdsListBySelectionId( entityManager, sWf );
            if ( ids != null && !ids.isEmpty() ) {
                return workflowManager.getNewWorkflowById( entityManager, UUID.fromString( userIdFromGeneralHeader ),
                        ids.get( 0 ).toString() );
            }
            return null;
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAskOnRunVariables( String userId, String workflowId, String json ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< DesignVariableDTO > designVariables = new ArrayList<>();
            List< VariableEntity > variableEntities = new ArrayList<>();

            List< ObjectiveVariableDTO > objectiveVariables = new ArrayList<>();

            final WorkflowDefinitionDTO workflowDefinitionDTO = getWorkflowDefinitionFromUserIdAndWorkflowId( entityManager, userId,
                    workflowId );
            final UserWorkFlow userWorkFlow = new UserWorkflowImpl();
            if ( null != workflowDefinitionDTO.getElements() ) {
                userWorkFlow.setNodes( WorkflowDefinitionUtil.prepareWorkflowElements( workflowDefinitionDTO ) );
                getDesignAndObjectiveVariablesFromWorkflow( entityManager, workflowId, userWorkFlow, designVariables, objectiveVariables,
                        json );
            }
            String algoType = getSchemeAlgoTypeByUserAndWorkflowId( entityManager, userId, workflowId );

            for ( DesignVariableDTO designVariableDTO : designVariables ) {
                variableEntities.add( prepareDesignvariableEntity( entityManager, designVariableDTO, userId, algoType ) );
            }

            for ( ObjectiveVariableDTO objectiveVariableDTO : objectiveVariables ) {
                variableEntities.add( prepareObjectiveVariableEntity( entityManager, objectiveVariableDTO, userId ) );
            }

            List< VariableEntity > allVariables = ( List< VariableEntity > ) variableDAO.getAllVariables( entityManager, workflowId, userId,
                    VariableEntity.class );

            if ( CollectionUtils.isNotEmpty( allVariables ) ) {
                allVariables.stream().filter( variableEntity -> isVariableAskOnRun(
                                null != variableEntity.getName() ? variableEntity.getName() : ConstantsString.EMPTY_STRING, userWorkFlow ) )
                        .forEach( variableEntity -> variableDAO.delete( entityManager, variableEntity ) );
            }
            variableDAO.saveAll( entityManager, variableEntities );

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the workflow definition from user id and workflow id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return the workflow definition from user id and workflow id
     */
    private WorkflowDefinitionDTO getWorkflowDefinitionFromUserIdAndWorkflowId( EntityManager entityManager, String userId,
            String workflowId ) {
        WorkflowDTO workflowDTO = workflowManager.getWorkflowById( entityManager, UUID.fromString( userId ), workflowId );
        Map< String, Object > definition = workflowDTO.getDefinition();

        String definitionJson = null;
        if ( definition != null ) {
            definitionJson = JsonUtils.toJson( definition );
        }
        return JsonUtils.jsonToObject( definitionJson, WorkflowDefinitionDTO.class );
    }

    /**
     * Gets the design and objective variables from workflow.
     *
     * @param workflowId
     *         the workflow id
     * @param userWorkFlow
     *         the user work flow
     * @param designVariables
     *         the design variables
     * @param objectiveVariables
     *         the objective variables
     * @param json
     *         the json
     */
    private void getDesignAndObjectiveVariablesFromWorkflow( EntityManager entityManager, String workflowId, UserWorkFlow userWorkFlow,
            List< DesignVariableDTO > designVariables, List< ObjectiveVariableDTO > objectiveVariables, String json ) {
        for ( final UserWFElement element : userWorkFlow.getNodes() ) {
            if ( element.getKey().equals( ElementKeys.IO.getKey() ) && null != element.getFields() ) {
                for ( final Field< ? > field : element.getFields() ) {
                    if ( field.getType().equals( FieldTypes.REGEX_SCAN_SERVER.getType() ) && field.isChangeOnRun() ) {
                        prepareRegexScannedDesignAndObjectVariables( entityManager, workflowId, designVariables, objectiveVariables,
                                element, field, json );
                    } else if ( field.getType().equals( FieldTypes.OBJECT_PARSER.getType() ) && field.isChangeOnRun() ) {
                        prepareParserDesignAndObjectiveVariable( entityManager, workflowId, designVariables, objectiveVariables, element,
                                field, json );
                    } else if ( field.getType().equals( FieldTypes.TEMPLATE_SCAN_SERVER.getType() ) && field.isChangeOnRun() ) {
                        prepareTemplateScannedDesignAndObjectVariables( entityManager, workflowId, designVariables, objectiveVariables,
                                element, field, json );
                    }
                }
            }
        }
    }

    /**
     * Prepare parser design and objective variable.
     *
     * @param workflowId
     *         the workflow id
     * @param designVariables
     *         the design variables
     * @param objectiveVariables
     *         the objective variables
     * @param element
     *         the element
     * @param field
     *         the field
     * @param json
     *         the json
     */
    private void prepareParserDesignAndObjectiveVariable( EntityManager entityManager, String workflowId,
            List< DesignVariableDTO > designVariables, List< ObjectiveVariableDTO > objectiveVariables, UserWFElement element,
            Field< ? > field, String json ) {
        JSONObject parameters = getScannedValuesSelectionId( json );

        try {
            String parserId;
            if ( parameters != null && ( parameters.containsKey( field.getName() ) ) && ( parameters.get( field.getName() ) != null )
                    && !( parameters.get( field.getName() ).toString().isEmpty() ) ) {
                parserId = ( String ) parameters.get( field.getName() );
                ParserEntity parserEntity = parserManager.getParserSchemaDAO().findById( entityManager, UUID.fromString( parserId ) );
                if ( parserEntity != null ) {
                    List< ParserVariableDTO > list = parserManager.getSelectedParserEntriesListByParserId( entityManager, parserId );
                    String namePrefix = element.getName() + ConstantsString.DOT + field.getName() + ConstantsString.DOT;
                    if ( field.getTemplateType().equalsIgnoreCase( FieldTemplates.DESIGN_VARIABLE.getValue() ) ) {
                        list.stream().map( selectedDesigns -> VariableUtil.prepareDesignVariableDTOFromParserVariableDTO( selectedDesigns,
                                namePrefix, UUID.fromString( workflowId ) ) ).forEach( designVariables::add );
                    } else if ( field.getTemplateType().equalsIgnoreCase( FieldTemplates.OBJECTIVE_VARIABLE.getValue() ) ) {
                        list.stream()
                                .map( selectedDesigns -> VariableUtil.prepareObjectiveVariableDTOFromParserVariableDTO( selectedDesigns,
                                        namePrefix, UUID.fromString( workflowId ) ) ).forEach( objectiveVariables::add );
                    }
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }

    }

    /**
     * Gets the scanned values selection id.
     *
     * @param json
     *         the json
     *
     * @return the scanned values selection id
     */
    private JSONObject getScannedValuesSelectionId( String json ) {
        JSONObject parameters = null;
        JsonNode jsonNodes = JsonUtils.toJsonNode( json );
        JsonNode jsonNodeElement = jsonNodes.get( ELEMENT );
        Iterator< String > fieldNames = jsonNodeElement.fieldNames();

        boolean isChecked = false;

        while ( fieldNames.hasNext() ) {
            String fieldName = fieldNames.next();
            JsonNode fieldValue = jsonNodeElement.get( fieldName );
            Iterator< Entry< String, JsonNode > > fields = fieldValue.fields();
            while ( fields.hasNext() ) {
                Entry< String, JsonNode > field = fields.next();
                try {
                    UUID.fromString( field.getValue().asText() );
                    isChecked = true;
                    parameters = parseJsonNodeToJsonObject( fieldValue );
                } catch ( IllegalArgumentException ignored ) {
                }

                if ( isChecked ) {
                    break;
                }
            }
            if ( isChecked ) {
                break;
            }
        }
        return parameters;
    }

    /**
     * Parses the json node to json object.
     *
     * @param fieldValue
     *         the field value
     *
     * @return the JSON object
     */
    private JSONObject parseJsonNodeToJsonObject( JsonNode fieldValue ) {
        JSONObject parameters;
        JSONParser jsonParser = new JSONParser();
        try {
            Object object = jsonParser.parse( fieldValue.toString() );
            parameters = ( JSONObject ) object;
        } catch ( ParseException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
        return parameters;
    }

    /**
     * Prepare regex scanned design and object variables.
     *
     * @param workflowId
     *         the workflow id
     * @param designVariables
     *         the design variables
     * @param objectiveVariables
     *         the objective variables
     * @param element
     *         the element
     * @param field
     *         the field
     * @param json
     *         the json
     */
    private void prepareRegexScannedDesignAndObjectVariables( EntityManager entityManager, String workflowId,
            List< DesignVariableDTO > designVariables, List< ObjectiveVariableDTO > objectiveVariables, UserWFElement element,
            Field< ? > field, String json ) {
        JSONObject parameters = getScannedValuesSelectionId( json );
        try {
            String regexSelectionId;
            if ( parameters != null && ( parameters.containsKey( field.getName() ) ) && ( parameters.get( field.getName() ) != null )
                    && !( parameters.get( field.getName() ).toString().isEmpty() ) ) {
                regexSelectionId = ( String ) parameters.get( field.getName() );
                List< RegexEntity > regexEntities = regexDAO.getRegexListBySelectionId( entityManager,
                        UUID.fromString( regexSelectionId ) );
                if ( ( regexEntities != null ) ) {
                    String namePrefix = element.getName() + ConstantsString.DOT + field.getName() + ConstantsString.DOT;
                    if ( field.getTemplateType().equalsIgnoreCase( FieldTemplates.DESIGN_VARIABLE.getValue() ) ) {
                        regexEntities.stream()
                                .map( regexEntity -> VariableUtil.prepareDesignVariableDTOFromRegexEntity( regexEntity, namePrefix,
                                        UUID.fromString( workflowId ) ) ).forEach( designVariable -> {
                                    designVariable.setCreatedOn( new Date() );
                                    designVariables.add( designVariable );
                                } );
                    } else if ( field.getTemplateType().equalsIgnoreCase( FieldTemplates.OBJECTIVE_VARIABLE.getValue() ) ) {
                        regexEntities.stream()
                                .map( regexEntity -> VariableUtil.prepareObjectiveVariableDTOFromRegexEntity( regexEntity, namePrefix,
                                        UUID.fromString( workflowId ) ) ).forEach( objectiveVariable -> {
                                    objectiveVariable.setCreatedOn( new Date() );
                                    objectiveVariables.add( objectiveVariable );
                                } );
                    }
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }

    }

    /**
     * Prepare template scanned design and object variables.
     *
     * @param workflowId
     *         the workflow id
     * @param designVariables
     *         the design variables
     * @param objectiveVariables
     *         the objective variables
     * @param element
     *         the element
     * @param field
     *         the field
     * @param json
     *         the json
     */
    private void prepareTemplateScannedDesignAndObjectVariables( EntityManager entityManager, String workflowId,
            List< DesignVariableDTO > designVariables, List< ObjectiveVariableDTO > objectiveVariables, UserWFElement element,
            Field< ? > field, String json ) {
        JSONObject parameters = getScannedValuesSelectionId( json );
        UUID selectionId = UUID.fromString( parameters.get( field.getName() ).toString() );
        List< TemplateEntity > templateEntities = templateManager.getTemplateDAO()
                .getTemplateListBySelectionId( entityManager, selectionId );
        String namePrefix = element.getName() + ConstantsString.DOT + field.getName() + ConstantsString.DOT;
        if ( field.getTemplateType().equals( FieldTemplates.DESIGN_VARIABLE.getValue() ) && CollectionUtils.isNotEmpty(
                templateEntities ) ) {
            templateEntities.forEach( templateEntity -> designVariables.add(
                    VariableUtil.prepareDesignVariableDTOFromTemplateEntity( templateEntity, namePrefix,
                            UUID.fromString( workflowId ) ) ) );
        } else if ( field.getTemplateType().equals( FieldTemplates.OBJECTIVE_VARIABLE.getValue() ) && CollectionUtils.isNotEmpty(
                templateEntities ) ) {
            templateEntities.stream().map( templateEntity -> VariableUtil.prepareObjectiveVariableDTOFromTemplateEntity( templateEntity,
                    ConstantsString.EMPTY_STRING, UUID.fromString( workflowId ) ) ).forEach( objectiveVariables::add );
        }
    }

    /**
     * Prepare objective variable entity.
     *
     * @param entityManager
     *         the entity manager
     * @param objectiveVariableDTO
     *         the objective variable DTO
     * @param userId
     *         the user id
     *
     * @return the objective variable entity
     */
    private ObjectiveVariableEntity prepareObjectiveVariableEntity( EntityManager entityManager, ObjectiveVariableDTO objectiveVariableDTO,
            String userId ) {
        ObjectiveVariableEntity entity = new ObjectiveVariableEntity();
        entity.setId( UUID.randomUUID() );
        entity.setLabel( objectiveVariableDTO.getLabel() );
        entity.setName( objectiveVariableDTO.getName() );
        entity.setNominalValue( objectiveVariableDTO.getNominalValue() );
        entity.setCreatedOn( objectiveVariableDTO.getCreatedOn() );
        entity.setWorkflow( workflowDao.getLatestNonDeletedObjectById( entityManager, objectiveVariableDTO.getWorkflowId() ) );
        entity.setCreatedOn( ( objectiveVariableDTO.getCreatedOn() == null ? new Date() : objectiveVariableDTO.getCreatedOn() ) );
        entity.setUserId( UUID.fromString( userId ) );
        return entity;
    }

    /**
     * Prepare designvariable entity.
     *
     * @param entityManager
     *         the entity manager
     * @param designVariableDTO
     *         the design variable DTO
     * @param userId
     *         the user id
     * @param algoType
     *         the algo type
     *
     * @return the design variable entity
     */
    private DesignVariableEntity prepareDesignvariableEntity( EntityManager entityManager, DesignVariableDTO designVariableDTO,
            String userId, String algoType ) {
        DesignVariableEntity entity = new DesignVariableEntity();
        entity.setId( UUID.randomUUID() );
        entity.setLabel( designVariableDTO.getLabel() );
        entity.setName( designVariableDTO.getName() );
        entity.setNominalValue( designVariableDTO.getNominalValue() );
        entity.setType( designVariableDTO.getType() );
        entity.setWorkflow( workflowDao.getLatestNonDeletedObjectById( entityManager, designVariableDTO.getWorkflowId() ) );
        entity.setCreatedOn( ( designVariableDTO.getCreatedOn() == null ? new Date() : designVariableDTO.getCreatedOn() ) );
        entity.setIndexNumber( designVariableDTO.getIndex() );
        entity.setUserId( UUID.fromString( userId ) );
        entity.setAlgoType( algoType );

        return entity;
    }

    /**
     * Gets the WorkFlow scheme DAO.
     *
     * @return the WorkFlow scheme DAO
     */
    public WFSchemeDAO getwFSchemeDAO() {
        return wFSchemeDAO;
    }

    /**
     * Sets the WorkFlow scheme DAO.
     *
     * @param wFSchemeDAO
     *         the new WorkFlow scheme DAO
     */
    public void setwFSchemeDAO( WFSchemeDAO wFSchemeDAO ) {
        this.wFSchemeDAO = wFSchemeDAO;
    }

    /**
     * Gets the parser manager.
     *
     * @return the parser manager
     */
    public ParserManager getParserManager() {
        return parserManager;
    }

    /**
     * Sets the parser manager.
     *
     * @param parserManager
     *         the new parser manager
     */
    public void setParserManager( ParserManager parserManager ) {
        this.parserManager = parserManager;
    }

    /**
     * Gets the workflow dao.
     *
     * @return the workflow dao
     */
    public WorkflowDAO getWorkflowDao() {
        return workflowDao;
    }

    /**
     * Sets the workflow dao.
     *
     * @param workflowDao
     *         the new workflow dao
     */
    public void setWorkflowDao( WorkflowDAO workflowDao ) {
        this.workflowDao = workflowDao;
    }

    /**
     * Gets the regex DAO.
     *
     * @return the regex DAO
     */
    public RegexDAO getRegexDAO() {
        return regexDAO;
    }

    /**
     * Sets the regex DAO.
     *
     * @param regexDAO
     *         the new regex DAO
     */
    public void setRegexDAO( RegexDAO regexDAO ) {
        this.regexDAO = regexDAO;
    }

    /**
     * Gets the template manager.
     *
     * @return the template manager
     */
    public TemplateManager getTemplateManager() {
        return templateManager;
    }

    /**
     * Sets the template manager.
     *
     * @param templateManager
     *         the new template manager
     */
    public void setTemplateManager( TemplateManager templateManager ) {
        this.templateManager = templateManager;
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
     * Sets document dao.
     *
     * @param documentDAO
     *         the document dao
     */
    public void setDocumentDAO( DocumentDAO documentDAO ) {
        this.documentDAO = documentDAO;
    }

}