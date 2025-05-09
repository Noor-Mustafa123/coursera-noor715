package de.soco.software.simuspace.wizards.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.manager.BaseManager;
import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.server.manager.WorkflowManager;
import de.soco.software.simuspace.suscore.authentication.manager.AuthManager;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.CB2Client;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.BmwCaeBenchEnums;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchInputDeckDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchSubModelDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.SubTabsUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.PythonUtils;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.BmwCaeBenchDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchEntity;
import de.soco.software.simuspace.suscore.data.entity.ProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.QADynaFormEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionItemEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.data.model.ProjectDTO;
import de.soco.software.simuspace.suscore.data.model.SsfeSelectionDTO;
import de.soco.software.simuspace.suscore.location.manager.LocationManager;
import de.soco.software.simuspace.suscore.object.manager.DataProjectManager;
import de.soco.software.simuspace.wizards.base.utils.CB2FormUtils;
import de.soco.software.simuspace.wizards.dao.QADynaDAO;
import de.soco.software.simuspace.wizards.manager.QaDynaManager;
import de.soco.software.simuspace.wizards.manager.WizardsManager;
import de.soco.software.simuspace.wizards.model.QADyna.QADynaBaseForm;
import de.soco.software.simuspace.wizards.model.QADyna.QADynaCombinationDTO;
import de.soco.software.simuspace.wizards.model.QADyna.QADynaCombinationDeckDTO;
import de.soco.software.simuspace.wizards.model.QADyna.QADynaCombinationObjectDTO;
import de.soco.software.simuspace.wizards.model.QADyna.QADynaFormDTO;
import de.soco.software.simuspace.wizards.model.QADyna.QADynaInputForm;
import de.soco.software.simuspace.wizards.model.QADyna.QADynaPPOForm;
import de.soco.software.simuspace.wizards.model.QADyna.QADynaParameterForm;
import de.soco.software.simuspace.wizards.model.run.LoadcaseWFModel;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.dto.WorkflowElement;
import de.soco.software.simuspace.workflow.dto.WorkflowModel;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.JobParametersImpl;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;

/**
 * The Class QaDynaManagerImpl.
 */
@Getter
@Setter
@Log4j2
public class QaDynaManagerImpl extends BaseManager implements QaDynaManager {

    /**
     * The constant SELECTION_ORIGIN.
     */
    private static final SelectionOrigins SELECTION_ORIGIN = SelectionOrigins.QA_DYNA;

    /**
     * The constant VERSIONS.
     */
    private static final String VERSION_STRING = "version";

    /**
     * The constant CB2_QUERY_STRING.
     */
    private static final String CB2_QUERY_STRING = "CB2 Query  :::::::::: ";

    /**
     * The constant SERVER_MCFILE_ID.
     */
    private static final String SERVER_MCFILE_ID = "_server_mcfileId";

    /**
     * The constant CB2_MCFILE_ID.
     */
    private static final String CB2_MCFILE_ID = "_cb2_mcfileId";

    /**
     * The constant SERVER_MATDB_ID.
     */
    private static final String SERVER_MATDB_ID = "_server_matdbId";

    /**
     * The constant CB2_MATDB_ID.
     */
    private static final String CB2_MATDB_ID = "_cb2_matdbId";

    /**
     * The constant SERVER_MATDBQS_ID.
     */
    private static final String SERVER_MATDBQS_ID = "_server_matdbqsId";

    /**
     * The constant CB2_MATDBQS_ID.
     */
    private static final String CB2_MATDBQS_ID = "_cb2_matdbqsId";

    /**
     * The Data manager. -- SETTER -- Sets data manager.
     */
    private DataProjectManager dataProjectManager;

    /**
     * The Qa dyna dao. -- SETTER -- Sets qa dyna dao.
     */
    private QADynaDAO qaDynaDAO;

    /**
     * The Entity Manager Factory -- SETTER -- Sets entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The bmw cae bench DAO. -- SETTER -- Sets the bmw cae bench DAO.
     */
    private BmwCaeBenchDAO bmwCaeBenchDAO;

    /**
     * The User common manager. -- SETTER -- Sets user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The location manager. -- SETTER -- Sets location manager.
     */
    private LocationManager locationManager;

    /**
     * The Job manager. -- SETTER -- Sets job manager.
     */
    private JobManager jobManager;

    /**
     * The Workflow manager. -- SETTER -- Sets workflow manager.
     */
    private WorkflowManager workflowManager;

    /**
     * The Auth manager. -- SETTER -- Sets auth manager.
     */
    private AuthManager authManager;

    /**
     * The Wizards manager. -- SETTER -- Sets wizards manager.
     */
    private WizardsManager wizardsManager;

    /**
     * The Constant MUST_CHOSE_OPTION.
     */
    private static final String MUST_CHOSE_OPTION = "Must Chose Option";

    /**
     * The Constant REQUIRED.
     */
    private static final String REQUIRED = "required";

    /**
     * The constant EXISTING_DYNA_OPTION.
     */
    private static final String EXISTING_DYNA_OPTION = "0";

    /**
     * The constant CB2_ERROR_KEY.
     */
    private static final String CB2_ERROR_KEY = "CB2 Error: ";

    /**
     * The constant SIM_DEFINITION_KEY.
     */
    private static final String SIM_DEFINITION_KEY = "simDef";

    /**
     * The constant MASTER_CONTROL_FILE_KEY.
     */
    private static final String MASTER_CONTROL_FILE_KEY = "mcfile";

    /**
     * The constant MAT_DB_KEY.
     */
    private static final String MAT_DB_KEY = "matdb";

    /**
     * The constant MAT_DB_QS_KEY.
     */
    private static final String MAT_DB_QS_KEY = "matdbqs";

    /**
     * The constant SELECTION_ID.
     */
    private static final String SELECTION_ID = "{selectionId}";

    /**
     * The constant PROJECT_ID.
     */
    private static final String PROJECT_ID = "{projectId}";

    /**
     * The constant SET_WITHIN_BRACKETS.
     */
    private static final String SET_WITHIN_BRACKETS = "{set}";

    /**
     * The constant SET.
     */
    private static final String SET = "set";

    /**
     * The constant WHICH_FILE.
     */
    private static final String WHICH_FILE = "{whichfile}";

    /**
     * The constant BIND_FROM_URL_FOR_DYNA.
     */
    public static final String BIND_FROM_URL_FOR_DYNA_OPTION = "/qadyna/project/{projectId}/parameter/selection/{selectionId}/set/{set}/dynaopt/{__value__}";

    /**
     * The constant BIND_FROM_URL_FOR_DYNA_FILE_SELECTION.
     */
    public static final String BIND_FROM_URL_FOR_DYNA_FILE_SELECTION = "/qadyna/project/{projectId}/parameter/selection/{selectionId}/set/{set}/file/{whichfile}/option/{__value__}";

    /**
     * The constant BIND_FROM_EZ_TYPE_FOR_DYNA.
     */
    public static final String BIND_FROM_EZ_TYPE_FOR_DYNA = "/qadyna/eztype/{__value__}";

    @Override
    public SubTabsItem getQaDynaTabsUI( String userId, String projectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( !licenseManager.isFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.CB2_CONNECTOR.getKey(), userId ) ) {
                throw new SusException( MessageBundleFactory.getMessage( "1300033x4" ) );
            }
            final SubTabsItem subTabs = new SubTabsItem();
            subTabs.setTitle( MessageBundleFactory.getMessage( "4100146x4" ) );
            subTabs.setTabs( prepareQaDynaTabs() );
            return subTabs;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UIFormItem > prepareBaseTabUI( String userId, String userName, String token, String projectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< UIFormItem > result = new ArrayList<>();
        try {
            ProjectDTO project = dataProjectManager.getProject( entityManager, UUID.fromString( projectId ) );
            QADynaFormEntity entity = qaDynaDAO.getByUserAndProject( entityManager, UUID.fromString( userId ), project.getId(),
                    project.getVersion().getId() );

            UIFormItem sectionField = GUIUtils.createFormItem();
            sectionField.setLabel( "Base Input Decks" );
            sectionField.setTitle( "Base Input Decks" );
            sectionField.setName( "inputDecks" );
            sectionField.setType( FieldTypes.SECTION.getType() );
            result.add( sectionField );

            SelectFormItem cb2Loadcase = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            cb2Loadcase.setLabel( "Select Base InputDecks" );
            cb2Loadcase.setName( "base" );
            cb2Loadcase.setExternal( BmwCaeBenchEnums.CB2_INPUT_DECK.getKey() );
            cb2Loadcase.setMultiple( true );
            cb2Loadcase.setType( FieldTypes.CB2_OBJECTS.getType() );

            FiltersDTO filter = new FiltersDTO();
            filter.setStart( ConstantsInteger.INTEGER_VALUE_ZERO );
            filter.setLength( ConstantsInteger.INTEGER_VALUE_HUNDRED );
            List< BmwCaeBenchDTO > inputDecks = wizardsManager.getBmwCaeBenchInputDeckForQADyna( userId, userName, token, filter,
                    BmwCaeBenchEnums.CB2_INPUT_DECK.getValue() );
            filter.setItems( inputDecks.stream().map( BmwCaeBenchDTO::getId ).collect( Collectors.toList() ) );
            if ( null != entity && null != entity.getContent() ) {
                QADynaFormDTO form = JsonUtils.jsonToObject( entity.getContent(), QADynaFormDTO.class );
                if ( null != form.getBase() ) {
                    removeOldAddNewSelection( entityManager, form, filter );
                    cb2Loadcase.setValue( form.getBase().getSelectionId() );
                }
            } else {
                SelectionResponseUI response = selectionManager.createSelection( entityManager, userId, SELECTION_ORIGIN, filter );
                cb2Loadcase.setValue( response.getId() );
            }
            setRulesAndMessageOnUI( cb2Loadcase );
            result.add( cb2Loadcase );
        } finally {
            entityManager.close();
        }
        return result;
    }

    /**
     * Remove old add new selection.
     *
     * @param entityManager
     *         the entity manager
     * @param form
     *         the form
     * @param filter
     *         the filter
     */
    private void removeOldAddNewSelection( EntityManager entityManager, QADynaFormDTO form, FiltersDTO filter ) {
        selectionManager.removeAllSelectionItems( entityManager, form.getBase().getSelectionId() );
        selectionManager.addSelectionItemsInExistingSelection( entityManager, form.getBase().getSelectionId(), filter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QADynaFormDTO saveBaseFields( String userId, String projectId, String selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SelectionEntity entitySelect = selectionManager.getSelectionDAO()
                    .getLatestObjectById( entityManager, SelectionEntity.class, UUID.fromString( selectionId ) );
            Set< SelectionItemEntity > selectedEntityIds = entitySelect.getItems();
            ProjectDTO project = dataProjectManager.getProject( entityManager, UUID.fromString( projectId ) );
            QADynaFormEntity entity = qaDynaDAO.getByUserAndProject( entityManager, UUID.fromString( userId ), project.getId(),
                    project.getVersion().getId() );
            QADynaFormDTO form;
            if ( null != entity ) {
                form = JsonUtils.jsonToObject( entity.getContent(), QADynaFormDTO.class );
            } else {
                form = new QADynaFormDTO();
                entity = new QADynaFormEntity();
                entity.setId( UUID.randomUUID() );
                entity.setUserId( UUID.fromString( userId ) );
                ProjectEntity projectEntity = entityManager.getReference( ProjectEntity.class,
                        new VersionPrimaryKey( project.getId(), project.getVersion().getId() ) );
                entity.setProject( projectEntity );
            }
            entity.setContent( JsonUtils.toJson( prepareQaDynaForm( selectionId, selectedEntityIds, entityManager, form ) ) );
            return prepareQADynaFormDTO( qaDynaDAO.saveOrUpdate( entityManager, entity ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UIFormItem > prepareParamTabUI( String userId, String userName, String projectId, String selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            QADynaFormDTO form = new QADynaFormDTO();
            ProjectDTO project = dataProjectManager.getProject( entityManager, UUID.fromString( projectId ) );
            QADynaFormEntity entity = qaDynaDAO.getByUserAndProject( entityManager, UUID.fromString( userId ), project.getId(),
                    project.getVersion().getId() );
            if ( null != entity && null != entity.getContent() ) {
                form = JsonUtils.jsonToObject( entity.getContent(), QADynaFormDTO.class );
            }

            List< UIFormItem > uiFormList = new ArrayList<>();
            JSONObject jsonQueryResponse = CB2Client.PostCB2( userName, getCB2QueryPayload( selectionId, entityManager ) );
            String simDef = extractSimulationDefFromCB2Resp( jsonQueryResponse ).get( ConstantsInteger.INTEGER_VALUE_ZERO )
                    .get( SIM_DEFINITION_KEY );
            String study = extractStudyFromCB2Resp( userName, jsonQueryResponse );
            UIFormItem sectionField = GUIUtils.createFormItem();
            sectionField.setLabel( "Common Parameters" );
            sectionField.setTitle( "Common Parameters" );
            sectionField.setName( "commonParams" );
            sectionField.setType( FieldTypes.SECTION.getType() );
            uiFormList.add( sectionField );
            uiFormList.addAll( getSolverSettingsFormFromCB2( userName, study, simDef, form, "param" ) );
            uiFormList.addAll( getEZTypeField( form ) );
            uiFormList.addAll( getDynaFieldsSet1( projectId, selectionId, form ) );
            uiFormList.addAll( getDynaFieldsSet2( projectId, selectionId, form ) );
            return uiFormList;
        } catch ( final Exception e ) {
            log.error( CB2_ERROR_KEY + e.getMessage(), e );
            throw new SusException( CB2_ERROR_KEY + e.getMessage() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QADynaFormDTO saveParamFields( String userId, String projectId, String selectionId, QADynaParameterForm dynaParamForm ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ProjectDTO project = dataProjectManager.getProject( userId, UUID.fromString( projectId ) );
            QADynaFormEntity entity = qaDynaDAO.getByUserAndProject( entityManager, UUID.fromString( userId ), project.getId(),
                    project.getVersion().getId() );
            QADynaFormDTO form = JsonUtils.jsonToObject( entity.getContent(), QADynaFormDTO.class );
            prepareDynaParamForm( entityManager, dynaParamForm );
            form.setParameters( dynaParamForm );
            entity.setContent( JsonUtils.toJson( form ) );
            return prepareQADynaFormDTO( qaDynaDAO.saveOrUpdate( entityManager, entity ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UIFormItem > qaDynaOptionsUI( String userId, String userName, String projectId, String selectionId, String setNumber,
            String option ) {
        List< UIFormItem > result = new ArrayList<>();
        if ( EXISTING_DYNA_OPTION.contentEquals( option ) ) {
            result.addAll( getDynaVersionFormFile( userId, userName, projectId, selectionId, setNumber ) );
        } else {
            result.add( prepareParamServerFileFields( "Upload Dyna", "set" + setNumber + ConstantsString.UNDERSCORE + "uploadDynaId",
                    FieldTypes.SERVER_FILE_EXPLORER.getType(), null ) );
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UIFormItem > qaDynaFileSelectionUI( String userId, String projectId, String selectionId, String setNumber,
            String whichFile, String option ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< UIFormItem > result = new ArrayList<>();
        try {
            QADynaFormDTO form = null;
            ProjectDTO project = dataProjectManager.getProject( entityManager, UUID.fromString( projectId ) );
            QADynaFormEntity entity = qaDynaDAO.getByUserAndProject( entityManager, UUID.fromString( userId ), project.getId(),
                    project.getVersion().getId() );
            if ( null != entity && null != entity.getContent() ) {
                form = JsonUtils.jsonToObject( entity.getContent(), QADynaFormDTO.class );
            }
            if ( String.valueOf( ConstantsInteger.INTEGER_VALUE_ONE ).contentEquals( option ) ) {
                if ( whichFile.equalsIgnoreCase( MASTER_CONTROL_FILE_KEY ) ) {
                    result.add( prepareParamServerFileFields( "Master Control File",
                            SET + setNumber + ConstantsString.UNDERSCORE + "server" + ConstantsString.UNDERSCORE + "mcfileId",
                            FieldTypes.SERVER_FILE_EXPLORER.getType(), form ) );
                } else if ( whichFile.equalsIgnoreCase( MAT_DB_KEY ) ) {
                    result.add( prepareParamServerFileFields( "MatDB",
                            SET + setNumber + ConstantsString.UNDERSCORE + "server" + ConstantsString.UNDERSCORE + "matdbId",
                            FieldTypes.SERVER_FILE_EXPLORER.getType(), form ) );
                } else if ( whichFile.equalsIgnoreCase( MAT_DB_QS_KEY ) ) {
                    result.add( prepareParamServerFileFields( "MatDBQS",
                            SET + setNumber + ConstantsString.UNDERSCORE + "server" + ConstantsString.UNDERSCORE + "matdbqsId",
                            FieldTypes.SERVER_FILE_EXPLORER.getType(), form ) );
                }
            } else {
                if ( whichFile.equalsIgnoreCase( MASTER_CONTROL_FILE_KEY ) ) {
                    SelectFormItem selectUI = prepareParamServerFileFields( "Master Control File",
                            SET + setNumber + ConstantsString.UNDERSCORE + "cb2" + ConstantsString.UNDERSCORE + "mcfileId",
                            FieldTypes.CB2_OBJECTS.getType(), form );
                    selectUI.setExternal( BmwCaeBenchEnums.CB2_SUBMODEL.getKey() );
                    result.add( selectUI );
                } else if ( whichFile.equalsIgnoreCase( MAT_DB_KEY ) ) {
                    SelectFormItem selectUI = prepareParamServerFileFields( "MatDB",
                            SET + setNumber + ConstantsString.UNDERSCORE + "cb2" + ConstantsString.UNDERSCORE + "matdbId",
                            FieldTypes.CB2_OBJECTS.getType(), form );
                    selectUI.setExternal( BmwCaeBenchEnums.CB2_SUBMODEL.getKey() );
                    result.add( selectUI );
                } else if ( whichFile.equalsIgnoreCase( MAT_DB_QS_KEY ) ) {
                    SelectFormItem selectUI = prepareParamServerFileFields( "MatDBQS",
                            SET + setNumber + ConstantsString.UNDERSCORE + "cb2" + ConstantsString.UNDERSCORE + "matdbqsId",
                            FieldTypes.CB2_OBJECTS.getType(), form );
                    selectUI.setExternal( BmwCaeBenchEnums.CB2_SUBMODEL.getKey() );
                    result.add( selectUI );
                }
            }
        } finally {
            entityManager.close();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UIFormItem > prepareEZFieldUI( String ezTypeName ) {
        String value = null;
        List< UIFormItem > result = new ArrayList<>();
        String qaDynaConfigPath = PropertiesManager.getQADynaJson();
        if ( new File( qaDynaConfigPath ).exists() ) {
            var dynamicProperties = getDynamicProperties( qaDynaConfigPath, ezTypeName );
            if ( dynamicProperties != null ) {
                value = dynamicProperties.asText();
            }
        }
        result.add( prepareEZListFormField( value ) );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UIFormItem > qaDynaReviewUi( String userId, String projectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< UIFormItem > result = new ArrayList<>();
        try {
            QADynaFormDTO form = new QADynaFormDTO();
            ProjectDTO project = dataProjectManager.getProject( entityManager, UUID.fromString( projectId ) );
            QADynaFormEntity entity = qaDynaDAO.getByUserAndProject( entityManager, UUID.fromString( userId ), project.getId(),
                    project.getVersion().getId() );
            if ( null != entity && null != entity.getContent() ) {
                form = JsonUtils.jsonToObject( entity.getContent(), QADynaFormDTO.class );
            }
            // Review Inputs section
            result.add( getSectionField( "Review Inputs", "Review Inputs", "reviewInput" ) );

            SelectFormItem reviewTable = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            reviewTable.setLabel( "Review Variants" );
            reviewTable.setName( "combinations" );
            reviewTable.setType( "table" );
            result.add( reviewTable );

            // Output Settings section
            result.add( getSectionField( "Output Settings", "Output Settings", "outputSettings" ) );
            UIFormItem htmlNameField = GUIUtils.createFormItem();
            htmlNameField.setLabel( "HTML Report Name" );
            htmlNameField.setName( "htmlName" );
            setRulesAndMessageOnUI( htmlNameField );
            htmlNameField.setType( FieldTypes.TEXT.getType() );
            htmlNameField.setValue( null != form.getInputs() ? form.getInputs().getHtmlName() : ConstantsString.EMPTY_STRING );
            result.add( htmlNameField );

            List< UIFormItem > qaDynaInput = GUIUtils.prepareForm( false, new QADynaInputForm() );
            SelectFormItem qaDynaWorkflowItem = ( SelectFormItem ) findWorkflowById( qaDynaInput );
            List< SelectOptionsUI > dynaWorkflowOption = prepareDummyWorkFlows( entityManager );
            if ( !dynaWorkflowOption.isEmpty() ) {
                qaDynaWorkflowItem.setOptions( dynaWorkflowOption );
                qaDynaWorkflowItem.setValue( dynaWorkflowOption.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getId() );
            }
            qaDynaWorkflowItem.setType( "select-workflow" );
            setRulesAndMessageOnUI( qaDynaWorkflowItem );
            qaDynaWorkflowItem.setCanBeEmpty( false );
            result.add( qaDynaWorkflowItem );
        } finally {
            entityManager.close();
        }
        return result;
    }

    /**
     * Gets section field.
     *
     * @param label
     *         the label
     * @param title
     *         the title
     * @param name
     *         the name
     *
     * @return the section field
     */
    private UIFormItem getSectionField( String label, String title, String name ) {
        UIFormItem reviewInputSection = GUIUtils.createFormItem( FormItemType.SECTION );
        reviewInputSection.setLabel( label );
        reviewInputSection.setTitle( title );
        reviewInputSection.setName( name );
        reviewInputSection.setType( FieldTypes.SECTION.getType() );
        return reviewInputSection;
    }

    /**
     * Prepare dummy work flows list.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the list
     */
    private List< SelectOptionsUI > prepareDummyWorkFlows( EntityManager entityManager ) {
        List< SelectOptionsUI > options = new ArrayList<>();
        String workflowId = getQADynaWorkflow();
        if ( null == workflowId ) {
            return new ArrayList<>();
        }
        try {
            SuSEntity workflow = susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( workflowId ) );
            if ( workflow != null ) {
                SelectOptionsUI selectOptionsUI = new SelectOptionsUI();
                selectOptionsUI.setId( workflow.getComposedId().getId().toString() );
                selectOptionsUI.setName( workflow.getName() );
                options.add( selectOptionsUI );
            } else {
                throw new SusException( workflowId + ". Please correct the configuration." );
            }
        } catch ( Exception e ) {
            log.debug( "workflow id do not exists" + e.getMessage(), e );
        }
        return options;
    }

    /**
     * Find workflow by id ui form item.
     *
     * @param result
     *         the result
     *
     * @return the ui form item
     */
    private UIFormItem findWorkflowById( List< UIFormItem > result ) {
        for ( UIFormItem uiFormItem : result ) {
            if ( uiFormItem.getName().equalsIgnoreCase( "qaDynaWorkflow.id" ) ) {
                return uiFormItem;
            }
        }
        return GUIUtils.createFormItem();
    }

    /**
     * Gets workflow.
     *
     * @param qaDynaWorkflow
     *         the qa dyna workflow
     *
     * @return the workflow
     */
    private String getQADynaWorkflow() {
        String qaDynaConfigPath = PropertiesManager.getQADynaJson();
        if ( new File( qaDynaConfigPath ).exists() ) {
            var dynamicProperties = getDynamicProperties( qaDynaConfigPath, "qaDynaWorkflow" );
            if ( dynamicProperties != null ) {
                return dynamicProperties.asText();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > qaDynaReviewTableUI( String userId ) {
        return GUIUtils.listColumns( QADynaCombinationDTO.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< QADynaCombinationDTO > getQaDynaInputTable( String userId, String projectId, Map< String, Object > map ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ProjectDTO project = dataProjectManager.getProject( userId, UUID.fromString( projectId ) );
            QADynaFormEntity entity = qaDynaDAO.getByUserAndProject( entityManager, UUID.fromString( userId ), project.getId(),
                    project.getVersion().getId() );
            List< QADynaCombinationDTO > combination = new ArrayList<>();
            if ( null != entity ) {
                QADynaFormDTO form = JsonUtils.jsonToObject( entity.getContent(), QADynaFormDTO.class );
                UserDTO user = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );
                Path outputFilePath = prepareCombinationsForQADYNA( user, form );
                FileUtils.setGlobalAllFilePermissions( outputFilePath );
                combination.addAll( prepareListFromOutput( outputFilePath ) );
                FileUtils.deleteIfExists( outputFilePath );
            }
            return combination;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QADynaFormDTO saveInputReviewFields( String userId, String token, String projectId, QADynaInputForm dynaInputForm ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ProjectDTO project = dataProjectManager.getProject( userId, UUID.fromString( projectId ) );
            QADynaFormEntity entity = qaDynaDAO.getByUserAndProject( entityManager, UUID.fromString( userId ), project.getId(),
                    project.getVersion().getId() );
            QADynaFormDTO form = JsonUtils.jsonToObject( entity.getContent(), QADynaFormDTO.class );
            form.setInputs( dynaInputForm );
            entity.setContent( JsonUtils.toJson( form ) );
            return prepareQADynaFormDTO( qaDynaDAO.saveOrUpdate( entityManager, entity ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UIFormItem > preparePPOTabUI( String userId, String userName, String projectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > result = new ArrayList<>();
            result.add( getSectionField( "PPO", "PPO", "ppo" ) );
            result.addAll( getPPOField() );
            return result;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void savePPOFieldAndSubmit( String userId, String token, String projectId, QADynaPPOForm dynaPPOForm ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UserDTO user = TokenizedLicenseUtil.getNotNullUser( token );
            ProjectDTO project = dataProjectManager.getProject( userId, UUID.fromString( projectId ) );
            QADynaFormEntity entity = qaDynaDAO.getByUserAndProject( entityManager, UUID.fromString( userId ), project.getId(),
                    project.getVersion().getId() );
            QADynaFormDTO form = JsonUtils.jsonToObject( entity.getContent(), QADynaFormDTO.class );
            form.setPpo( dynaPPOForm );
            entity.setContent( JsonUtils.toJson( form ) );
            QADynaFormDTO qaDynaFormDTO = prepareQADynaFormDTO( qaDynaDAO.saveOrUpdate( entityManager, entity ) );

            JobParameters job = prepareJobAndSubmit( entityManager, user, token, qaDynaFormDTO.getInputs().getQaDynaWorkflow() );
            qaDynaFormDTO.setJobPath( job.getWorkingDir().getPath() );
            prepareJobFileAndSubmit( user, qaDynaFormDTO );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare job and submit.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param token
     *         the token
     * @param qaDynaWorkflow
     *         the qa dyna workflow
     *
     * @return the job parameters
     */
    private JobParameters prepareJobAndSubmit( EntityManager entityManager, UserDTO user, String token, LoadcaseWFModel qaDynaWorkflow ) {
        JobParameters jobImpl = new JobParametersImpl();
        jobImpl.setId( UUID.randomUUID().toString() );
        try {
            jobImpl.setDescription( ConstantsString.EMPTY_STRING );
            jobImpl.setName( "Run_QADYNA" );
            jobImpl.setWorkingDir( new EngineFile( PropertiesManager.getUserStagingPath( user.getUserUid() ) ) );
            jobImpl.setJobType( JobTypeEnums.QADYNA.getKey() );
            Integer id = jobManager.saveJobIds( entityManager, UUID.fromString( jobImpl.getId() ) );
            if ( id != null ) {
                jobImpl.setJobInteger( id );
            }
            jobImpl.setWorkflow( getWorkflow( entityManager, user.getId(), qaDynaWorkflow ) );
            jobImpl.setRunsOn( jobImpl.getWorkflow().getJob().get( "runsOn" ).toString() );
            RestAPI server = getServerByLocationId( entityManager, LocationsEnum.DEFAULT_LOCATION.getId() );
            jobImpl.setServer( server );
            RequestHeaders requestHeader = new RequestHeaders( token,
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36" );
            String jobToken = prepareJobToken( user.getId(), user.getUserUid(), jobImpl.getId(), server, requestHeader );
            requestHeader.setJobAuthToken( jobToken );
            requestHeader.setToken( null );
            jobImpl.setRequestHeaders( requestHeader );
            jobImpl.setWorkingDir( new EngineFile(
                    PropertiesManager.getUserStagingPath( user.getUserUid() ) + File.separator + "Run_QADYNA" + "_"
                            + jobImpl.getJobInteger() ) );
            UserEntity userEntity = userCommonManager.getUserEntityById( entityManager, UUID.fromString( user.getId() ) );
            log.info( "Monitoring job with id: " + jobImpl.getId() );
            Runnable runnable = () -> {
                log.info( ">>submitQADyna Workflow : " );
                submitQADynaThread( userEntity, jobImpl );
            };
            Thread t = new Thread( runnable );
            t.start();
            return jobImpl;
        } catch ( Exception e ) {
            log.debug( "QA Dyna Failed : ", e );
            updateJobWithStatus( entityManager, UUID.fromString( user.getId() ), UUID.fromString( jobImpl.getId() ),
                    new Status( WorkflowStatus.FAILED ) );
            authManager.expireJobToken( entityManager, jobImpl.getRequestHeaders().getJobAuthToken() );
        }
        return jobImpl;
    }

    /**
     * Submit qa dyna thread.
     *
     * @param userEntity
     *         the user entity
     * @param jobImpl
     *         the job
     */
    private void submitQADynaThread( UserEntity userEntity, JobParameters jobImpl ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            workflowManager.runServerJob( entityManager, userEntity, jobImpl, null );
        } finally {
            entityManager.close();
        }

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
     */
    private void updateJobWithStatus( EntityManager entityManager, UUID userId, UUID jobId, Status jobStatus ) {
        Job job = jobManager.getJob( entityManager, userId, jobId.toString() );
        List< LogRecord > jobLog = new ArrayList<>();
        jobLog.add( new LogRecord( ConstantsMessageTypes.INFO, "job Completed", new Date() ) );
        job.setLog( jobLog );
        job.setStatus( jobStatus );
        jobManager.updateJob( entityManager, job );
    }

    /**
     * Gets the workflow.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param lcWFModel
     *         the lc WF model
     *
     * @return the workflow
     */
    private LatestWorkFlowDTO getWorkflow( EntityManager entityManager, String userIdFromGeneralHeader, LoadcaseWFModel lcWFModel ) {
        UUID userId = UUID.fromString( userIdFromGeneralHeader );
        LatestWorkFlowDTO wf = getLatestWorkFlowDTO( entityManager, userId, lcWFModel );
        WorkflowModel workflowModel = JsonUtils.jsonToObject( JsonUtils.toJson( wf.getElements() ), WorkflowModel.class );
        if ( lcWFModel.getElements() != null ) {
            updateWorkflowModelElements( lcWFModel, workflowModel );
        }
        wf.setElements( JsonUtils.convertStringToMapGenericValue( JsonUtils.toJson( workflowModel ) ) );
        return wf;
    }

    /**
     * Gets latest work flow dto.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param lcWFModel
     *         the lc wf model
     *
     * @return the latest work flow dto
     */
    private LatestWorkFlowDTO getLatestWorkFlowDTO( EntityManager entityManager, UUID userId, LoadcaseWFModel lcWFModel ) {
        int versionId = workflowManager.getWorkflowById( entityManager, userId, lcWFModel.getId() ).getVersion().getId();
        return workflowManager.getNewWorkflowByIdAndVersionId( entityManager, userId, lcWFModel.getId(), versionId );
    }

    /**
     * Update workflow model elements.
     *
     * @param lcWFModel
     *         the lc wf model
     * @param workflowModel
     *         the workflow model
     */
    private void updateWorkflowModelElements( LoadcaseWFModel lcWFModel, WorkflowModel workflowModel ) {
        lcWFModel.getElements().forEach( ( elementId, fieldsMap ) -> workflowModel.getNodes().stream()
                .filter( element -> element.getData().getId().equals( elementId ) )
                .forEach( element -> updateElementFields( element, fieldsMap ) ) );
    }

    /**
     * Update element fields.
     *
     * @param element
     *         the element
     * @param fieldsMap
     *         the fields map
     */
    private void updateElementFields( WorkflowElement element, Map< String, Object > fieldsMap ) {
        for ( Field field : element.getData().getFields() ) {
            if ( fieldsMap.containsKey( field.getName() ) ) {
                field.setValue( fieldsMap.get( field.getName() ) );
            }
        }
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
        String token;
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
     * Prepare job file and submit string.
     *
     * @param user
     *         the user
     * @param qaDynaFormDTO
     *         the qa dyna form dto
     */
    private void prepareJobFileAndSubmit( UserDTO user, QADynaFormDTO qaDynaFormDTO ) {
        String inputFilePath = null;
        try {
            inputFilePath = PropertiesManager.getStagingPath() + File.separator + user.getUserUid() + File.separator + "qadyna.json";
            Path outputFile = Path.of( inputFilePath );
            Files.deleteIfExists( outputFile );
            Files.createFile( outputFile );
            FileUtils.setGlobalAllFilePermissions( new File( inputFilePath ).toPath() );
            try ( final FileWriter fileWriter = new FileWriter( inputFilePath, false ) ) {
                fileWriter.write( ( qaDynaFormDTO == null ? "{}" : JsonUtils.toJson( qaDynaFormDTO ) ) );
                fileWriter.flush();
            } catch ( final Exception e ) {
                log.error( e.getMessage(), e );
            }
        } catch ( IOException e ) {
            FileUtils.deleteIfExists( inputFilePath );
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_CREATED.getKey(), "qadyna.json" ) );
        }
    }

    /**
     * Prepare qa dyna form qa dyna form dto.
     *
     * @param selectionId
     *         the selection id
     * @param selectedEntityIds
     *         the selected entity ids
     * @param entityManager
     *         the entity manager
     * @param form
     *         the form
     *
     * @return the qa dyna form dto
     */
    private QADynaFormDTO prepareQaDynaForm( String selectionId, Set< SelectionItemEntity > selectedEntityIds, EntityManager entityManager,
            QADynaFormDTO form ) {
        QADynaBaseForm baseForm = new QADynaBaseForm();
        baseForm.setSelectionId( selectionId );
        if ( CollectionUtil.isNotEmpty( selectedEntityIds ) ) {
            List< BmwCaeBenchInputDeckDTO > list = selectedEntityIds.stream()
                    .map( selectionItemEntity -> bmwCaeBenchDAO.getUniqueObjectByProperty( entityManager, BmwCaeBenchEntity.class, "id",
                            UUID.fromString( selectionItemEntity.getItem() ) ) ).filter( Objects::nonNull )
                    .map( this::prepareBmwCaeBenchInputDeckByEntity ).toList();
            baseForm.setInputDeck( list );
        }
        form.setBase( baseForm );
        return form;
    }

    /**
     * Prepare dyna param form.
     *
     * @param entityManager
     *         the entity manager
     * @param dynaParamForm
     *         the dyna param form
     */
    private void prepareDynaParamForm( EntityManager entityManager, QADynaParameterForm dynaParamForm ) {
        processUploadDyna( dynaParamForm.getSet1_uploadDynaId(), dynaParamForm::setSet1_uploadDyna );
        processUploadDyna( dynaParamForm.getSet2_uploadDynaId(), dynaParamForm::setSet2_uploadDyna );

        processMcfileOptionSet1( entityManager, dynaParamForm.getSet1_mcfileOption(), dynaParamForm, dynaParamForm::setSet1_mcfile,
                dynaParamForm::setSet1_mcfileCB2 );
        processMcfileOptionSet2( entityManager, dynaParamForm.getSet2_mcfileOption(), dynaParamForm, dynaParamForm::setSet2_mcfile,
                dynaParamForm::setSet2_mcfileCB2 );

        processMatdbOptionSet1( entityManager, dynaParamForm.getSet1_matdbOption(), dynaParamForm, dynaParamForm::setSet1_matdb,
                dynaParamForm::setSet1_matdbCB2 );
        processMatdbOptionSet2( entityManager, dynaParamForm.getSet2_matdbOption(), dynaParamForm, dynaParamForm::setSet2_matdb,
                dynaParamForm::setSet2_matdbCB2 );

        processMatdbqsOptionSet1( entityManager, dynaParamForm.getSet1_matdbqsOption(), dynaParamForm, dynaParamForm::setSet1_matdbqs,
                dynaParamForm::setSet1_matdbqsCB2 );
        processMatdbqsOptionSet2( entityManager, dynaParamForm.getSet2_matdbqsOption(), dynaParamForm, dynaParamForm::setSet2_matdbqs,
                dynaParamForm::setSet2_matdbqsCB2 );
    }

    /**
     * Process upload dyna.
     *
     * @param uploadDynaId
     *         the upload dyna id
     * @param setter
     *         the setter
     */
    private void processUploadDyna( String uploadDynaId, Consumer< SsfeSelectionDTO > setter ) {
        if ( uploadDynaId != null ) {
            setter.accept( prepareObjectFromSelectionId( uploadDynaId ) );
        }
    }

    /**
     * Process mcfile option.
     *
     * @param entityManager
     *         the entity manager
     * @param option
     *         the option
     * @param dynaParamForm
     *         the dyna param form
     * @param mcfileSetter
     *         the mcfile setter
     * @param mcfileCB2Setter
     *         the mcfile cb 2 setter
     */
    private void processMcfileOptionSet1( EntityManager entityManager, String option, QADynaParameterForm dynaParamForm,
            Consumer< SsfeSelectionDTO > mcfileSetter, Consumer< BmwCaeBenchSubModelDTO > mcfileCB2Setter ) {
        if ( StringUtils.isNotNullOrEmpty( option ) ) {
            if ( Integer.parseInt( option ) == ConstantsInteger.INTEGER_VALUE_ZERO ) {
                mcfileCB2Setter.accept( prepareCB2ObjectFromSelectionId( entityManager, dynaParamForm.getSet1_cb2_mcfileId() ) );
            } else {
                mcfileSetter.accept( prepareObjectFromSelectionId( dynaParamForm.getSet1_server_mcfileId() ) );
            }
        }
    }

    /**
     * Process mcfile option.
     *
     * @param entityManager
     *         the entity manager
     * @param option
     *         the option
     * @param dynaParamForm
     *         the dyna param form
     * @param mcfileSetter
     *         the mcfile setter
     * @param mcfileCB2Setter
     *         the mcfile cb 2 setter
     */
    private void processMcfileOptionSet2( EntityManager entityManager, String option, QADynaParameterForm dynaParamForm,
            Consumer< SsfeSelectionDTO > mcfileSetter, Consumer< BmwCaeBenchSubModelDTO > mcfileCB2Setter ) {
        if ( StringUtils.isNotNullOrEmpty( option ) ) {
            if ( Integer.parseInt( option ) == ConstantsInteger.INTEGER_VALUE_ZERO ) {
                mcfileCB2Setter.accept( prepareCB2ObjectFromSelectionId( entityManager, dynaParamForm.getSet2_cb2_mcfileId() ) );
            } else {
                mcfileSetter.accept( prepareObjectFromSelectionId( dynaParamForm.getSet2_server_mcfileId() ) );
            }
        }
    }

    /**
     * Process matdb option.
     *
     * @param entityManager
     *         the entity manager
     * @param option
     *         the option
     * @param dynaParamForm
     *         the dyna param form
     * @param matdbSetter
     *         the matdb setter
     * @param matdbCB2Setter
     *         the matdb cb 2 setter
     */
    private void processMatdbOptionSet1( EntityManager entityManager, String option, QADynaParameterForm dynaParamForm,
            Consumer< SsfeSelectionDTO > matdbSetter, Consumer< BmwCaeBenchSubModelDTO > matdbCB2Setter ) {
        if ( StringUtils.isNotNullOrEmpty( option ) ) {
            if ( Integer.parseInt( option ) == ConstantsInteger.INTEGER_VALUE_ZERO ) {
                matdbCB2Setter.accept( prepareCB2ObjectFromSelectionId( entityManager, dynaParamForm.getSet1_cb2_matdbId() ) );
            } else {
                matdbSetter.accept( prepareObjectFromSelectionId( dynaParamForm.getSet1_server_matdbId() ) );
            }
        }
    }

    /**
     * Process matdb option set 2.
     *
     * @param entityManager
     *         the entity manager
     * @param option
     *         the option
     * @param dynaParamForm
     *         the dyna param form
     * @param matdbSetter
     *         the matdb setter
     * @param matdbCB2Setter
     *         the matdb cb 2 setter
     */
    private void processMatdbOptionSet2( EntityManager entityManager, String option, QADynaParameterForm dynaParamForm,
            Consumer< SsfeSelectionDTO > matdbSetter, Consumer< BmwCaeBenchSubModelDTO > matdbCB2Setter ) {
        if ( StringUtils.isNotNullOrEmpty( option ) ) {
            if ( Integer.parseInt( option ) == ConstantsInteger.INTEGER_VALUE_ZERO ) {
                matdbCB2Setter.accept( prepareCB2ObjectFromSelectionId( entityManager, dynaParamForm.getSet2_cb2_matdbId() ) );
            } else {
                matdbSetter.accept( prepareObjectFromSelectionId( dynaParamForm.getSet2_server_matdbId() ) );
            }
        }
    }

    /**
     * Process matdbqs option.
     *
     * @param entityManager
     *         the entity manager
     * @param option
     *         the option
     * @param dynaParamForm
     *         the dyna param form
     * @param matdbqsSetter
     *         the matdbqs setter
     * @param matdbqsCB2Setter
     *         the matdbqs cb 2 setter
     */
    private void processMatdbqsOptionSet1( EntityManager entityManager, String option, QADynaParameterForm dynaParamForm,
            Consumer< SsfeSelectionDTO > matdbqsSetter, Consumer< BmwCaeBenchSubModelDTO > matdbqsCB2Setter ) {
        if ( StringUtils.isNotNullOrEmpty( option ) ) {
            if ( Integer.parseInt( option ) == ConstantsInteger.INTEGER_VALUE_ZERO ) {
                matdbqsCB2Setter.accept( prepareCB2ObjectFromSelectionId( entityManager, dynaParamForm.getSet1_cb2_matdbqsId() ) );
            } else {
                matdbqsSetter.accept( prepareObjectFromSelectionId( dynaParamForm.getSet1_server_matdbqsId() ) );
            }
        }
    }

    /**
     * Process matdbqs option set 2.
     *
     * @param entityManager
     *         the entity manager
     * @param option
     *         the option
     * @param dynaParamForm
     *         the dyna param form
     * @param matdbqsSetter
     *         the matdbqs setter
     * @param matdbqsCB2Setter
     *         the matdbqs cb 2 setter
     */
    private void processMatdbqsOptionSet2( EntityManager entityManager, String option, QADynaParameterForm dynaParamForm,
            Consumer< SsfeSelectionDTO > matdbqsSetter, Consumer< BmwCaeBenchSubModelDTO > matdbqsCB2Setter ) {
        if ( StringUtils.isNotNullOrEmpty( option ) ) {
            if ( Integer.parseInt( option ) == ConstantsInteger.INTEGER_VALUE_ZERO ) {
                matdbqsCB2Setter.accept( prepareCB2ObjectFromSelectionId( entityManager, dynaParamForm.getSet2_cb2_matdbqsId() ) );
            } else {
                matdbqsSetter.accept( prepareObjectFromSelectionId( dynaParamForm.getSet2_server_matdbqsId() ) );
            }
        }
    }

    /**
     * Prepare object from selection id ssfe selection dto.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the ssfe selection dto
     */
    private SsfeSelectionDTO prepareObjectFromSelectionId( String selectionId ) {
        return ( SsfeSelectionDTO ) selectionManager.getSelectedIdsListBySelectionId( selectionId )
                .get( ConstantsInteger.INTEGER_VALUE_ZERO );
    }

    /**
     * Prepare cb 2 object from selection id bmw cae bench sub model dto.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selection id
     *
     * @return the bmw cae bench sub model dto
     */
    private BmwCaeBenchSubModelDTO prepareCB2ObjectFromSelectionId( EntityManager entityManager, String selectionId ) {
        SelectionEntity entitySelect = selectionManager.getSelectionDAO()
                .getLatestObjectById( entityManager, SelectionEntity.class, UUID.fromString( selectionId ) );
        Set< SelectionItemEntity > selectedEntityIds = entitySelect.getItems();
        List< BmwCaeBenchSubModelDTO > list = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( selectedEntityIds ) ) {
            for ( SelectionItemEntity selectionItemEntity : selectedEntityIds ) {
                BmwCaeBenchEntity entityBmw = bmwCaeBenchDAO.getUniqueObjectByProperty( entityManager, BmwCaeBenchEntity.class, "id",
                        UUID.fromString( selectionItemEntity.getItem() ) );
                if ( null != entityBmw ) {
                    BmwCaeBenchSubModelDTO bmwCaeDTO = prepareBmwCaeBenchSubModelByEntity( entityBmw );
                    list.add( bmwCaeDTO );
                }
            }
        }
        return list.get( ConstantsInteger.INTEGER_VALUE_ZERO );
    }

    /**
     * Prepare list.
     *
     * @param outputFilePath
     *         the output file path
     *
     * @return the list
     */
    private List< QADynaCombinationDTO > prepareListFromOutput( Path outputFilePath ) {
        List< QADynaCombinationDTO > combinationList = new ArrayList<>();
        if ( Files.exists( outputFilePath ) ) {
            try ( BufferedReader outputFile = Files.newBufferedReader( outputFilePath ) ) {
                JSONParser parser = new JSONParser();
                org.json.simple.JSONArray outputJson = ( org.json.simple.JSONArray ) parser.parse( outputFile );
                for ( org.json.simple.JSONObject jsonObj : ( Iterable< org.json.simple.JSONObject > ) outputJson ) {
                    combinationList.add( prepareCombinationList( jsonObj ) );
                }
            } catch ( IOException | ParseException e ) {
                log.error( e.getMessage(), e );
            }
        }
        return combinationList;
    }

    /**
     * Prepare combination list qa dyna combination dto.
     *
     * @param jsonObj
     *         the json obj
     *
     * @return the qa dyna combination dto
     */
    private QADynaCombinationDTO prepareCombinationList( org.json.simple.JSONObject jsonObj ) {
        QADynaCombinationDTO qaDynaCombinationDTO = new QADynaCombinationDTO();
        qaDynaCombinationDTO.setInputDeck( prepareInputDeckObject( ( org.json.simple.JSONObject ) jsonObj.get( "inputDeck" ) ) );
        qaDynaCombinationDTO.setCpu( ( String ) jsonObj.get( "cpu" ) );
        qaDynaCombinationDTO.setEz( ( String ) jsonObj.get( "ez" ) );
        qaDynaCombinationDTO.setDynaVersion( prepareObject( ( org.json.simple.JSONObject ) jsonObj.get( "dynaVersion" ) ) );
        if ( !isNotEmpty( jsonObj.get( MASTER_CONTROL_FILE_KEY ) ) ) {
            qaDynaCombinationDTO.setMcfile( prepareObject( ( org.json.simple.JSONObject ) jsonObj.get( MASTER_CONTROL_FILE_KEY ) ) );
        }
        if ( !isNotEmpty( jsonObj.get( MAT_DB_KEY ) ) ) {
            qaDynaCombinationDTO.setMatdb( prepareObject( ( org.json.simple.JSONObject ) jsonObj.get( MAT_DB_KEY ) ) );
        }
        if ( !isNotEmpty( jsonObj.get( MAT_DB_QS_KEY ) ) ) {
            qaDynaCombinationDTO.setMatdbqs( prepareObject( ( org.json.simple.JSONObject ) jsonObj.get( MAT_DB_QS_KEY ) ) );
        }
        return qaDynaCombinationDTO;
    }

    /**
     * Is not empty boolean.
     *
     * @param obj
     *         the obj
     *
     * @return the boolean
     */
    private boolean isNotEmpty( Object obj ) {
        if ( obj == null ) {
            return true;
        }
        if ( obj instanceof String string ) {
            return string.isEmpty();
        }
        if ( obj instanceof JSONObject jsonObj ) {
            return jsonObj.isEmpty();
        }
        if ( obj instanceof JSONArray jsonArray ) {
            return jsonArray.isEmpty();
        }
        return false;
    }

    /**
     * Prepare object qa dyna combination object dto.
     *
     * @param jsonObject
     *         the input deck
     *
     * @return the qa dyna combination object dto
     */
    private QADynaCombinationObjectDTO prepareObject( org.json.simple.JSONObject jsonObject ) {
        return new QADynaCombinationObjectDTO( ( String ) jsonObject.get( "selectionId" ), ( String ) jsonObject.get( "name" ),
                ( String ) jsonObject.get( "fullPath" ), ( String ) jsonObject.get( VERSION_STRING ) );
    }

    /**
     * Prepare input deck object qa dyna combination deck dto.
     *
     * @param jsonObject
     *         the json object
     *
     * @return the qa dyna combination deck dto
     */
    private QADynaCombinationDeckDTO prepareInputDeckObject( org.json.simple.JSONObject jsonObject ) {
        return new QADynaCombinationDeckDTO( ( String ) jsonObject.get( "id" ), ( String ) jsonObject.get( "name" ),
                ( String ) jsonObject.get( "fullPath" ), ( String ) jsonObject.get( VERSION_STRING ) );
    }

    /**
     * Prepare qa dyna form dto qa dyna form dto.
     *
     * @param qaDynaFormEntity
     *         the qa dyna form entity
     *
     * @return the qa dyna form dto
     */
    private QADynaFormDTO prepareQADynaFormDTO( QADynaFormEntity qaDynaFormEntity ) {
        return JsonUtils.jsonToObject( qaDynaFormEntity.getContent(), QADynaFormDTO.class );
    }

    /**
     * Prepare bmw cae bench input deck by entity bmw cae bench input deck dto.
     *
     * @param bmwCaeBenchEntity
     *         the bmw cae bench entity
     *
     * @return the bmw cae bench input deck dto
     */
    private BmwCaeBenchInputDeckDTO prepareBmwCaeBenchInputDeckByEntity( BmwCaeBenchEntity bmwCaeBenchEntity ) {
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
     * Prepare qa dyna tabs list.
     *
     * @return the list
     */
    private List< SubTabsUI > prepareQaDynaTabs() {
        List< SubTabsUI > subTabsUIs = new ArrayList<>();
        subTabsUIs.add( new SubTabsUI( null, "base", MessageBundleFactory.getMessage( Messages.BASE.getKey() ) ) );
        subTabsUIs.add( new SubTabsUI( null, "parameter", MessageBundleFactory.getMessage( Messages.PARAMETERS.getKey() ) ) );
        subTabsUIs.add( new SubTabsUI( null, "inputs", MessageBundleFactory.getMessage( Messages.REVIEW_INPUTS.getKey() ) ) );
        subTabsUIs.add( new SubTabsUI( null, "ppo", MessageBundleFactory.getMessage( Messages.RESULTS.getKey() ) ) );
        return subTabsUIs;
    }

    /**
     * Sets rules and message on ui.
     *
     * @param uiFormItem
     *         the ui form item
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
     * Gets ez type field.
     *
     * @param form
     *         the form
     *
     * @return the ez type field
     */
    private List< UIFormItem > getEZTypeField( QADynaFormDTO form ) {
        QADynaParameterForm param = new QADynaParameterForm();
        if ( null != form.getParameters() ) {
            param = form.getParameters();
        }
        List< String > eztypeList = new ArrayList<>();
        String qaDynaConfigPath = PropertiesManager.getQADynaJson();
        if ( new File( qaDynaConfigPath ).exists() ) {
            var dynamicProperties = getDynamicProperties( qaDynaConfigPath, "ezType" );
            if ( dynamicProperties != null ) {
                eztypeList = ( List< String > ) JsonUtils.jsonToList( JsonUtils.toJsonString( dynamicProperties ), eztypeList );
            }
        }
        SelectFormItem item = prepareSelectUIItemFromField( "EZ List Type", "ezType", "Select the EZ List type for HPC" );
        item.setValue( param.getEzType() );
        List< SelectOptionsUI > options = new ArrayList<>();
        eztypeList.forEach( type -> options.add( new SelectOptionsUI( type, type ) ) );
        item.setOptions( options );
        item.setBindFrom( BIND_FROM_EZ_TYPE_FOR_DYNA );
        List< UIFormItem > result = new ArrayList<>();
        result.add( item );
        return result;
    }

    /**
     * Gets ppo field.
     *
     * @return the ppo field
     */
    private List< UIFormItem > getPPOField() {

        List< String > ppoList = new ArrayList<>();
        String qaDynaConfigPath = PropertiesManager.getQADynaJson();
        if ( new File( qaDynaConfigPath ).exists() ) {
            var dynamicProperties = getDynamicProperties( qaDynaConfigPath, "ppo" );
            if ( dynamicProperties != null ) {
                ppoList = ( List< String > ) JsonUtils.jsonToList( JsonUtils.toJsonString( dynamicProperties ), ppoList );
            }
        }
        SelectFormItem item = prepareSelectUIItemFromField( "KeyResultCurves", "ppoNames", "Select the PPO's" );
        List< SelectOptionsUI > options = new ArrayList<>();
        ppoList.forEach( type -> options.add( new SelectOptionsUI( type, type ) ) );
        item.setOptions( options );
        item.setMultiple( true );
        return new ArrayList<>( List.of( item ) );
    }

    /**
     * Gets dynamic properties.
     *
     * @param path
     *         the path
     * @param key
     *         the key
     *
     * @return the dynamic properties
     */
    public JsonNode getDynamicProperties( String path, String key ) {
        JsonNode jsonDynamicPathObject;
        try ( InputStream dummyFileStream = new FileInputStream( path ) ) {
            jsonDynamicPathObject = JsonUtils.convertInputStreamToJsonNode( dummyFileStream );
        } catch ( FileNotFoundException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), path ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), path ) );
        } catch ( IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_FILE.getKey(), path ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_FILE.getKey(), path ) );
        }
        return jsonDynamicPathObject != null ? jsonDynamicPathObject.get( key ) : null;
    }

    /**
     * Prepare select ui item from field select ui.
     *
     * @param label
     *         the label
     * @param name
     *         the name
     * @param tooltip
     *         the tooltip
     *
     * @return the select ui
     */
    public SelectFormItem prepareSelectUIItemFromField( String label, String name, String tooltip ) {
        SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        item.setType( FieldTypes.SELECTION.getType() );
        setRulesAndMessageOnUI( item );
        item.setLabel( label );
        item.setName( name );
        item.setTooltip( tooltip );
        return item;
    }

    /**
     * Prepare ez list form field select ui.
     *
     * @param value
     *         the value
     *
     * @return the select ui
     */
    private SelectFormItem prepareEZListFormField( String value ) {
        SelectFormItem ezField = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        ezField.setType( FieldTypes.TEXT.getType() );
        ezField.setName( "ezList" );
        ezField.setLabel( "EZ List" );
        ezField.setTooltip( "Provide the EZ List" );
        ezField.setValue( value );
        setRulesAndMessageOnUI( ezField );
        return ezField;
    }

    /**
     * Create input file string.
     *
     * @param user
     *         the user
     * @param form
     *         the form
     *
     * @return the string
     */
    private Path prepareCombinationsForQADYNA( UserDTO user, QADynaFormDTO form ) {
        String inputFilePath = null;
        try {
            File inputFile = new File( PropertiesManager.getDefaultServerTempPath() );
            inputFile = File.createTempFile( "input", ".json", inputFile );
            FileUtils.setGlobalAllFilePermissions( Path.of( inputFile.getAbsolutePath() ) );
            inputFilePath = inputFile.getAbsolutePath();

            try ( final FileWriter fileWriter = new FileWriter( inputFile, false ) ) {
                fileWriter.write( ( form == null ? "{}" : JsonUtils.toJson( form ) ) );
                fileWriter.flush();
            } catch ( final Exception e ) {
                log.error( e.getMessage(), e );
            }

            File outputFile = new File( PropertiesManager.getUserStagingPath( user.getUserUid() ) + "/qadynaOutput.json" );
            // call python script here
            PythonUtils.callGenerateQADYNACombinations( getPythonScriptPath(), inputFile.getAbsolutePath(), outputFile.getAbsolutePath() );

            FileUtils.deleteIfExists( inputFilePath );
            return outputFile.toPath();
        } catch ( IOException e ) {
            FileUtils.deleteIfExists( inputFilePath );
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_CREATED.getKey(), "input.json" ) );
        }
    }

    /**
     * Gets python script path.
     *
     * @param key
     *         the key
     *
     * @return the python script path
     */
    private String getPythonScriptPath() {
        String pythonPath = null;
        String qaDynaConfigPath = PropertiesManager.getQADynaJson();
        if ( new File( qaDynaConfigPath ).exists() ) {
            var dynamicProperties = getDynamicProperties( qaDynaConfigPath, "qaDynaCombinationScript" );
            if ( dynamicProperties != null ) {
                pythonPath = dynamicProperties.asText()
                        .replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, PropertiesManager.getScriptsPath() );
            }
        }
        return pythonPath;
    }

    /**
     * Gets dyna fields set 1.
     *
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     * @param form
     *         the form
     *
     * @return the dyna fields set 1
     */
    private List< UIFormItem > getDynaFieldsSet1( String projectId, String selectionId, QADynaFormDTO form ) {
        List< UIFormItem > result = new ArrayList<>();
        UIFormItem sectionField = GUIUtils.createFormItem();
        sectionField.setLabel( "Set 1 Parameters" );
        sectionField.setTitle( "Set 1 Parameters" );
        sectionField.setName( "set1" );
        sectionField.setType( FieldTypes.SECTION.getType() );
        result.add( sectionField );

        SelectFormItem dynaOpt = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        dynaOpt.setLabel( "LS-DYNA Options" );
        dynaOpt.setName( "set1_DynaOptions" );
        dynaOpt.setType( FieldTypes.SELECTION.getType() );
        dynaOpt.setBindFrom( BIND_FROM_URL_FOR_DYNA_OPTION.replace( PROJECT_ID, projectId ).replace( SELECTION_ID, selectionId )
                .replace( SET_WITHIN_BRACKETS, String.valueOf( ConstantsInteger.INTEGER_VALUE_ONE ) ) );
        dynaOpt.setValue( String.valueOf( ConstantsInteger.INTEGER_VALUE_ZERO ) );
        setRulesAndMessageOnUI( dynaOpt );
        List< SelectOptionsUI > options = new ArrayList<>();
        options.add( new SelectOptionsUI( String.valueOf( ConstantsInteger.INTEGER_VALUE_ZERO ), "Existing LS-DYNA Versions" ) );
        options.add( new SelectOptionsUI( String.valueOf( ConstantsInteger.INTEGER_VALUE_ONE ), "Upload LS-DYNA Executable" ) );
        dynaOpt.setOptions( options );

        result.add( dynaOpt );
        result.addAll( getQaDynaFilesUI( projectId, selectionId, String.valueOf( ConstantsInteger.INTEGER_VALUE_ONE ), form ) );
        return result;
    }

    /**
     * Gets dyna fields set 2.
     *
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     * @param form
     *         the form
     *
     * @return the dyna fields set 2
     */
    private List< UIFormItem > getDynaFieldsSet2( String projectId, String selectionId, QADynaFormDTO form ) {
        List< UIFormItem > result = new ArrayList<>();

        UIFormItem sectionField = GUIUtils.createFormItem();
        sectionField.setLabel( "Set 2 Parameters" );
        sectionField.setTitle( "Set 2 Parameters" );
        sectionField.setName( "set2" );
        sectionField.setType( FieldTypes.SECTION.getType() );
        result.add( sectionField );

        SelectFormItem dynaOpt = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        dynaOpt.setLabel( "LS-DYNA Options" );
        dynaOpt.setName( "set2_DynaOptions" );
        dynaOpt.setType( FieldTypes.SELECTION.getType() );
        dynaOpt.setBindFrom( BIND_FROM_URL_FOR_DYNA_OPTION.replace( PROJECT_ID, projectId ).replace( SELECTION_ID, selectionId )
                .replace( SET_WITHIN_BRACKETS, String.valueOf( ConstantsInteger.INTEGER_VALUE_TWO ) ) );
        dynaOpt.setValue( String.valueOf( ConstantsInteger.INTEGER_VALUE_ZERO ) );
        setRulesAndMessageOnUI( dynaOpt );
        List< SelectOptionsUI > options = new ArrayList<>();
        options.add( new SelectOptionsUI( String.valueOf( ConstantsInteger.INTEGER_VALUE_ZERO ), "Existing LS-DYNA Versions" ) );
        options.add( new SelectOptionsUI( String.valueOf( ConstantsInteger.INTEGER_VALUE_ONE ), "Upload LS-DYNA Exe" ) );
        dynaOpt.setOptions( options );

        result.add( dynaOpt );
        result.addAll( getQaDynaFilesUI( projectId, selectionId, String.valueOf( ConstantsInteger.INTEGER_VALUE_TWO ), form ) );
        return result;
    }

    /**
     * Extract study from cb 2 resp string.
     *
     * @param userName
     *         the username
     * @param jsonQueryResponse
     *         the json query response
     *
     * @return the string
     */
    private String extractStudyFromCB2Resp( String userName, JSONObject jsonQueryResponse ) {
        try {
            List< NameValuePair > cb2QueryPayload = new ArrayList<>();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "Variant" ) );
            cb2QueryPayload.add(
                    new BasicNameValuePair( "expr", "[objectId=='" + getVariantIdFromResponse( jsonQueryResponse ) + "'].studiedIn" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );
            if ( log.isDebugEnabled() ) {
                log.debug( CB2_QUERY_STRING + cb2QueryPayload );
            }
            return extractNameOidFromCB2Resp( CB2Client.PostCB2( userName, cb2QueryPayload ) ).get( ConstantsInteger.INTEGER_VALUE_ZERO )
                    .get( "id" );
        } catch ( final Exception e ) {
            log.error( CB2_ERROR_KEY + e.getMessage(), e );
            throw new SusException( CB2_ERROR_KEY + e.getMessage() );
        }
    }

    /**
     * Gets variant id from response.
     *
     * @param jsonQueryResponse
     *         the json query response
     *
     * @return the variant id from response
     */
    private String getVariantIdFromResponse( JSONObject jsonQueryResponse ) {
        List< Map< String, String > > output = new ArrayList<>();
        if ( jsonQueryResponse.has( "objects" ) ) {
            JSONArray objects = jsonQueryResponse.getJSONArray( "objects" );
            for ( int i = ConstantsInteger.INTEGER_VALUE_ZERO; i < objects.length(); i++ ) {
                JSONObject subObject = objects.getJSONObject( i );
                Map< String, String > cb2objects = new HashMap<>();
                cb2objects.put( "variant", String.valueOf( subObject.getJSONObject( "attrs" ).getJSONObject( "variant" ).get( "oid" ) ) );
                output.add( cb2objects );
            }
        }
        return output.get( ConstantsInteger.INTEGER_VALUE_ZERO ).get( "variant" )
                .split( ConstantsString.COLON )[ ConstantsInteger.INTEGER_VALUE_ZERO ];
    }

    /**
     * Extract name oid from cb 2 resp list.
     *
     * @param jsonQueryResponseObj
     *         the json query response obj
     *
     * @return the list
     */
    private List< Map< String, String > > extractNameOidFromCB2Resp( JSONObject jsonQueryResponseObj ) {
        List< Map< String, String > > output = new ArrayList<>();
        if ( jsonQueryResponseObj.has( "objects" ) ) {
            JSONArray objects = jsonQueryResponseObj.getJSONArray( "objects" );
            for ( int i = ConstantsInteger.INTEGER_VALUE_ZERO; i < objects.length(); i++ ) {
                JSONObject subObject = objects.getJSONObject( i );
                Map< String, String > cb2objects = new HashMap<>();
                cb2objects.put( "name", String.valueOf( subObject.getJSONObject( "attrs" ).getJSONObject( "name" ).get( "text" ) ) );
                cb2objects.put( "id", String.valueOf( subObject.get( "oid" ) ) );
                output.add( cb2objects );
            }
        }
        return output;
    }

    /**
     * Gets solver settings form from cb 2.
     *
     * @param userName
     *         the username
     * @param studyOid
     *         the study oid
     * @param simulationDefOid
     *         the simulation def oid
     * @param savedValues
     *         the saved values
     * @param tabName
     *         the tab name
     *
     * @return the solver settings form from cb 2
     */
    private List< UIFormItem > getSolverSettingsFormFromCB2( String userName, String studyOid, String simulationDefOid,
            QADynaFormDTO savedValues, String tabName ) {
        try {
            StringBuilder buildFilterQuery = new StringBuilder();
            buildFilterQuery.append( "study=" ).append( studyOid );
            buildFilterQuery.append( "&simulationDef=" ).append( simulationDefOid );
            buildFilterQuery.append( "&getDefaults=False" );
            buildFilterQuery.append( "&format=json" );
            log.info( CB2_QUERY_STRING + buildFilterQuery );
            JSONObject jsonQueryResponseObj = CB2Client.GetCB2( userName, "/cb2/servlet/rest/simgen/GetParametersOfSimulation?",
                    buildFilterQuery.toString() );
            return extractSolverSettingsFormFromResponse( jsonQueryResponseObj, savedValues, tabName );
        } catch ( final Exception e ) {
            log.error( CB2_ERROR_KEY + e.getMessage(), e );
            throw new SusException( CB2_ERROR_KEY + e.getMessage() );
        }
    }

    /**
     * Gets qa dyna files ui.
     *
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     * @param setNumber
     *         the set number
     * @param form
     *         the form
     *
     * @return the qa dyna files ui
     */
    private List< UIFormItem > getQaDynaFilesUI( String projectId, String selectionId, String setNumber, QADynaFormDTO form ) {
        List< UIFormItem > result = new ArrayList<>();

        UIFormItem mcFileUI = prepareSelectFileOptionField( "Select Master Control from",
                "set" + setNumber + ConstantsString.UNDERSCORE + "mcfileOption", projectId, selectionId, setNumber,
                MASTER_CONTROL_FILE_KEY );
        mcFileUI.setValue( Integer.parseInt( setNumber ) == ConstantsInteger.INTEGER_VALUE_ONE ? form.getParameters().getSet1_mcfileOption()
                : form.getParameters().getSet2_mcfileOption() );
        result.add( mcFileUI );

        UIFormItem matdbUI = prepareSelectFileOptionField( "Select Mat DB from",
                "set" + setNumber + ConstantsString.UNDERSCORE + "matdbOption", projectId, selectionId, setNumber, MAT_DB_KEY );
        matdbUI.setValue( Integer.parseInt( setNumber ) == ConstantsInteger.INTEGER_VALUE_ONE ? form.getParameters().getSet1_matdbOption()
                : form.getParameters().getSet2_matdbOption() );
        result.add( matdbUI );

        UIFormItem matdbqsUI = prepareSelectFileOptionField( "Select MatDBQS from",
                "set" + setNumber + ConstantsString.UNDERSCORE + "matdbqsOption", projectId, selectionId, setNumber, MAT_DB_QS_KEY );
        matdbqsUI.setValue(
                Integer.parseInt( setNumber ) == ConstantsInteger.INTEGER_VALUE_ONE ? form.getParameters().getSet1_matdbqsOption()
                        : form.getParameters().getSet2_matdbqsOption() );
        result.add( matdbqsUI );
        return result;
    }

    /**
     * Prepare select file option field ui form item.
     *
     * @param label
     *         the label
     * @param name
     *         the name
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     * @param setNumber
     *         the set number
     * @param whichFile
     *         the which file
     *
     * @return the ui form item
     */
    private UIFormItem prepareSelectFileOptionField( String label, String name, String projectId, String selectionId, String setNumber,
            String whichFile ) {
        SelectFormItem fileOption = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        fileOption.setLabel( label );
        fileOption.setName( name );
        fileOption.setType( FieldTypes.SELECTION.getType() );
        fileOption.setBindFrom( BIND_FROM_URL_FOR_DYNA_FILE_SELECTION.replace( PROJECT_ID, projectId ).replace( SELECTION_ID, selectionId )
                .replace( SET_WITHIN_BRACKETS, setNumber ).replace( WHICH_FILE, whichFile ) );
        List< SelectOptionsUI > options = new ArrayList<>();
        options.add( new SelectOptionsUI( String.valueOf( ConstantsInteger.INTEGER_VALUE_ZERO ), "CB2 File" ) );
        options.add( new SelectOptionsUI( String.valueOf( ConstantsInteger.INTEGER_VALUE_ONE ), "Server File" ) );
        fileOption.setOptions( options );
        return fileOption;
    }

    /**
     * Prepare param fields select ui.
     *
     * @param label
     *         the label
     * @param name
     *         the name
     * @param type
     *         the type
     * @param param
     *         the param
     *
     * @return the select ui
     */
    private SelectFormItem prepareParamServerFileFields( String label, String name, String type, QADynaFormDTO param ) {
        SelectFormItem field = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        field.setLabel( label );
        field.setName( name );
        field.setType( type );
        if ( null != param ) {
            field.setValue( getValueFromSavedFields( name, param ) );
        }
        return field;
    }

    /**
     * Gets value from saved fields.
     *
     * @param name
     *         the name
     * @param form
     *         the form
     *
     * @return the value from saved fields
     */
    private Object getValueFromSavedFields( String name, QADynaFormDTO form ) {
        QADynaParameterForm param = new QADynaParameterForm();
        if ( null != form.getParameters() ) {
            param = form.getParameters();
        }
        if ( name.contains( "1" + SERVER_MCFILE_ID ) ) {
            return param.getSet1_server_mcfileId();
        } else if ( name.contains( "1" + CB2_MCFILE_ID ) ) {
            return param.getSet1_cb2_mcfileId();
        } else if ( name.contains( "2" + SERVER_MCFILE_ID ) ) {
            return param.getSet2_server_mcfileId();
        } else if ( name.contains( "2" + CB2_MCFILE_ID ) ) {
            return param.getSet2_cb2_mcfileId();
        } else if ( name.contains( "1" + SERVER_MATDB_ID ) ) {
            return param.getSet1_server_matdbId();
        } else if ( name.contains( "1" + CB2_MATDB_ID ) ) {
            return param.getSet1_cb2_matdbId();
        } else if ( name.contains( "2" + SERVER_MATDB_ID ) ) {
            return param.getSet2_server_matdbId();
        } else if ( name.contains( "2" + CB2_MATDB_ID ) ) {
            return param.getSet2_cb2_matdbId();
        } else if ( name.contains( "1" + SERVER_MATDBQS_ID ) ) {
            return param.getSet1_server_matdbqsId();
        } else if ( name.contains( "1" + CB2_MATDBQS_ID ) ) {
            return param.getSet1_cb2_matdbqsId();
        } else if ( name.contains( "2" + SERVER_MATDBQS_ID ) ) {
            return param.getSet2_server_matdbqsId();
        } else if ( name.contains( "2" + CB2_MATDBQS_ID ) ) {
            return param.getSet2_cb2_matdbqsId();
        }
        return null;
    }

    /**
     * Gets dyna version form filed.
     *
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     * @param setNumber
     *         the set number
     *
     * @return the dyna version form filed
     */
    private List< UIFormItem > getDynaVersionFormFile( String userId, String userName, String projectId, String selectionId,
            String setNumber ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            QADynaFormDTO form = new QADynaFormDTO();
            ProjectDTO project = dataProjectManager.getProject( entityManager, UUID.fromString( projectId ) );
            QADynaFormEntity entity = qaDynaDAO.getByUserAndProject( entityManager, UUID.fromString( userId ), project.getId(),
                    project.getVersion().getId() );
            if ( null != entity && null != entity.getContent() ) {
                form = JsonUtils.jsonToObject( entity.getContent(), QADynaFormDTO.class );
            }
            JSONObject jsonQueryResponse = CB2Client.PostCB2( userName, getCB2QueryPayload( selectionId, entityManager ) );
            String simDef = extractSimulationDefFromCB2Resp( jsonQueryResponse ).get( ConstantsInteger.INTEGER_VALUE_ZERO )
                    .get( SIM_DEFINITION_KEY );
            String study = extractStudyFromCB2Resp( userName, jsonQueryResponse );
            return getSolverSettingsFormFromCB2( userName, study, simDef, form,
                    SET + setNumber + ConstantsString.UNDERSCORE + "dynaversion" );
        } catch ( final Exception e ) {
            log.error( CB2_ERROR_KEY + e.getMessage(), e );
            throw new SusException( CB2_ERROR_KEY + e.getMessage() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets cb 2 query payload.
     *
     * @param selectionId
     *         the selection id
     * @param entityManager
     *         the entity manager
     *
     * @return the cb 2 query payload
     */
    private List< NameValuePair > getCB2QueryPayload( String selectionId, EntityManager entityManager ) {
        String inputDeckId;
        List< String > selectedList = selectionManager.getSelectedIdsStringListBySelectionId( entityManager, selectionId );
        BmwCaeBenchEntity entityBmw = bmwCaeBenchDAO.getUniqueObjectByProperty( entityManager, BmwCaeBenchEntity.class, ID,
                UUID.fromString( selectedList.get( ConstantsInteger.INTEGER_VALUE_ZERO ) ) );
        if ( null != entityBmw ) {
            inputDeckId = entityBmw.getOid().split( ConstantsString.COLON )[ ConstantsInteger.INTEGER_VALUE_ZERO ];
        } else {
            throw new SusException( "Error extracting selected CB2 Entry against " + selectionId );
        }
        List< NameValuePair > cb2QueryPayload = new ArrayList<>();
        cb2QueryPayload.add( new BasicNameValuePair( "type", "InputDeck" ) );
        cb2QueryPayload.add( new BasicNameValuePair( "expr", "[objectId=='" + inputDeckId + "']" ) );
        cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );
        if ( log.isDebugEnabled() ) {
            log.debug( CB2_QUERY_STRING + cb2QueryPayload );
        }
        return cb2QueryPayload;
    }

    /**
     * Extract simulation def from cb 2 resp list.
     *
     * @param jsonQueryResponseObj
     *         the json query response obj
     *
     * @return the list
     */
    private List< Map< String, String > > extractSimulationDefFromCB2Resp( JSONObject jsonQueryResponseObj ) {
        List< Map< String, String > > output = new ArrayList<>();
        if ( jsonQueryResponseObj.has( "objects" ) ) {
            JSONArray objects = jsonQueryResponseObj.getJSONArray( "objects" );
            for ( int i = 0; i < objects.length(); i++ ) {
                JSONObject subObject = objects.getJSONObject( i );
                Map< String, String > cb2objects = new HashMap<>();
                cb2objects.put( SIM_DEFINITION_KEY,
                        String.valueOf( subObject.getJSONObject( "attrs" ).getJSONObject( "simulationDef" ).get( "oid" ) ) );
                output.add( cb2objects );
            }
        }
        return output;
    }

    /**
     * Extract solver settings form from response list.
     *
     * @param jsonQueryResponseObj
     *         the json query response obj
     * @param savedValues
     *         the saved values
     * @param tabName
     *         the tab name
     *
     * @return the list
     */
    private List< UIFormItem > extractSolverSettingsFormFromResponse( JSONObject jsonQueryResponseObj, QADynaFormDTO savedValues,
            String tabName ) {
        List< UIFormItem > fieldList = new ArrayList<>();
        JsonNode root = JsonUtils.convertStingToJsonNode( jsonQueryResponseObj.toString() );
        // Extract values from JsonNode
        JsonNode inputList = root.at( "/parameters/ui/tabs/SOLVER1/inputList" );

        // Iterate over the elements in the inputList
        if ( tabName.equalsIgnoreCase( "param" ) ) {
            getCPUFieldForParamTab( inputList, root, fieldList, savedValues );
        } else if ( tabName.equalsIgnoreCase( "simSetup" ) ) {
            UIFormItem sectionField = GUIUtils.createFormItem();
            sectionField.setLabel( "Solver Settings" );
            sectionField.setTitle( "Solver Settings" );
            sectionField.setName( "solverSettings" );
            sectionField.setType( FieldTypes.SECTION.getType() );
            fieldList.add( sectionField );
            prepareSimSetupFields( inputList, root, fieldList );
        } else if ( tabName.contains( VERSION_STRING ) ) {
            prepareDynaVersionField( inputList, root, fieldList, tabName.split( ConstantsString.UNDERSCORE )[ 0 ], savedValues );
        }
        return fieldList;
    }

    /**
     * Prepare sim setup fields.
     *
     * @param inputList
     *         the input list
     * @param root
     *         the root
     * @param fieldList
     *         the field list
     */
    private void prepareSimSetupFields( JsonNode inputList, JsonNode root, List< UIFormItem > fieldList ) {
        for ( JsonNode input : inputList ) {
            String fieldKey = input.asText();
            JsonNode fieldNode = root.at( "/parameters/inputs/" + fieldKey );
            String actualFieldName = fieldKey.substring(
                    fieldKey.lastIndexOf( ConstantsString.UNDERSCORE ) + ConstantsInteger.INTEGER_VALUE_ONE ).trim();
            if ( actualFieldName.equalsIgnoreCase( "hpcEZ" ) || actualFieldName.equalsIgnoreCase( VERSION_STRING ) ) {
                continue;
            }
            UIFormItem item = prepareFormItemFromCb2FieldJson( fieldNode );
            if ( item != null ) {
                String fieldName = item.getName()
                        .substring( item.getName().lastIndexOf( ConstantsString.UNDERSCORE ) + ConstantsInteger.INTEGER_VALUE_ONE ).trim();
                if ( !fieldName.equalsIgnoreCase( "ncpu" ) ) {
                    fieldList.add( item );
                }
            }
        }
    }

    /**
     * Prepare dyna version field.
     *
     * @param inputList
     *         the input list
     * @param root
     *         the root
     * @param fieldList
     *         the field list
     * @param setNumber
     *         the set number
     * @param savedValues
     *         the saved values
     */
    private void prepareDynaVersionField( JsonNode inputList, JsonNode root, List< UIFormItem > fieldList, String setNumber,
            QADynaFormDTO savedValues ) {
        QADynaParameterForm paramForm = new QADynaParameterForm();
        if ( null != savedValues.getParameters() ) {
            paramForm = savedValues.getParameters();
        }
        for ( JsonNode input : inputList ) {
            String fieldKey = input.asText();
            JsonNode fieldNode = root.at( "/parameters/inputs/" + fieldKey );
            String actualFieldName = fieldKey.substring(
                    fieldKey.lastIndexOf( ConstantsString.UNDERSCORE ) + ConstantsInteger.INTEGER_VALUE_ONE ).trim();
            if ( actualFieldName.equalsIgnoreCase( VERSION_STRING ) ) {
                UIFormItem item = prepareFormItemFromCb2FieldJson( fieldNode );
                if ( item != null ) {
                    item.setName( setNumber + ConstantsString.UNDERSCORE + actualFieldName );
                    item.setLabel( "LS-DYNA Version" );
                    item.setValue( setNumber.contains( String.valueOf( ConstantsInteger.INTEGER_VALUE_ONE ) ) ? paramForm.getSet1_version()
                            : paramForm.getSet2_version() );
                    fieldList.add( item );
                    break;
                }
            }
        }
    }

    /**
     * Gets cpu field for param tab.
     *
     * @param inputList
     *         the input list
     * @param root
     *         the root
     * @param fieldList
     *         the field list
     * @param savedValues
     *         the saved values
     */
    private void getCPUFieldForParamTab( JsonNode inputList, JsonNode root, List< UIFormItem > fieldList, QADynaFormDTO savedValues ) {
        QADynaParameterForm paramForm = new QADynaParameterForm();
        if ( null != savedValues.getParameters() ) {
            paramForm = savedValues.getParameters();
        }
        for ( JsonNode input : inputList ) {
            String fieldKey = input.asText();
            JsonNode fieldNode = root.at( "/parameters/inputs/" + fieldKey );
            String actualFieldName = fieldKey.substring(
                    fieldKey.lastIndexOf( ConstantsString.UNDERSCORE ) + ConstantsInteger.INTEGER_VALUE_ONE ).trim();
            if ( actualFieldName.equalsIgnoreCase( "ncpu" ) ) {
                UIFormItem item = prepareFormItemFromCb2FieldJson( fieldNode );
                if ( item != null ) {
                    item.setName( actualFieldName );
                    item.setValue( paramForm.getNcpu() );
                    item.setMultiple( true );
                    fieldList.add( item );
                    break;
                }
            }
        }
    }

    /**
     * Prepare form item from cb 2 field json ui form item.
     *
     * @param fieldNode
     *         the field node
     *
     * @return the ui form item
     */
    private UIFormItem prepareFormItemFromCb2FieldJson( JsonNode fieldNode ) {
        String widgetId = fieldNode.has( "widgetId" ) ? fieldNode.get( "widgetId" ).asText() : null;
        if ( widgetId == null ) {
            return null;
        }
        return switch ( widgetId ) {
            case "drop-down" -> CB2FormUtils.prepareSelectItemFromDropDown( fieldNode );
            case "check-box" -> CB2FormUtils.prepareBooleanItemFromCheckBox( fieldNode );
            case "one-ref" -> CB2FormUtils.prepareFormItemFromOneRef( fieldNode );
            case "text-field" -> CB2FormUtils.prepareFormItemFromTextField( fieldNode );
            default -> null;
        };

    }

    /**
     * Gets selection manager.
     *
     * @return the selection manager
     */
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    /**
     * Sets selection manager.
     *
     * @param selectionManager
     *         the selection manager
     */
    public void setSelectionManager( SelectionManager selectionManager ) {
        this.selectionManager = selectionManager;
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
     * Sets data project manager.
     *
     * @param dataProjectManager
     *         the data project manager
     */
    public void setDataProjectManager( DataProjectManager dataProjectManager ) {
        this.dataProjectManager = dataProjectManager;
    }

}
