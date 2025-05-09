package de.soco.software.simuspace.server.manager.impl;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.dao.TemplateDAO;
import de.soco.software.simuspace.server.manager.BaseManager;
import de.soco.software.simuspace.server.manager.TemplateManager;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.enums.VariableDropDownEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.ObjectVariableDTO;
import de.soco.software.simuspace.suscore.common.model.ScanFileDTO;
import de.soco.software.simuspace.suscore.common.model.ScanResponseDTO;
import de.soco.software.simuspace.suscore.common.model.TemplateScanDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.EncryptAndDecryptUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.BmwCaeBenchDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.TemplateEntity;
import de.soco.software.simuspace.suscore.document.dao.DocumentDAO;

/**
 * The Class TemplateManagerImpl.
 *
 * @author Fahad Rafi
 */
@Log4j2
public class TemplateManagerImpl extends BaseManager implements TemplateManager {

    /**
     * The template DAO.
     */
    private TemplateDAO templateDAO;

    /**
     * The bmw cae bench DAO.
     */
    private BmwCaeBenchDAO bmwCaeBenchDAO;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Constant SCHEME_TEMPLATE_FILE_SIZE.
     */
    public static final String SCHEME_TEMPLATE_FILE_SIZE = PropertiesManager.getSchemeTemplateFileSize();

    /**
     * The Constant TEMPLATE_OBJECT.
     */
    public static final String REGEX_OBJECT = "regexobject";

    /**
     * The Constant VALUE.
     */
    public static final String VALUE = "value";

    /**
     * The Constant ID.
     */
    public static final String ID = "id";

    /**
     * The Constant HIDDEND_PARAM.
     */
    public static final String HIDDEND_PARAM = "hidden";

    /**
     * The Constant REQUIRED_PARAM.
     */
    public static final String REQUIRED_PARAM = "required";

    /**
     * The Constant TEMPLATE_SELECTION.
     */
    public static final String TEMPLATE_SELECTION = "Template selection";

    /**
     * The Constant SELECTION_INVALID.
     */
    public static final String SELECTION_INVALID = "Selection is not a valid UUID";

    /**
     * The Constant FILE_SIZE_ERROR.
     */
    public static final String FILE_SIZE_ERROR = " size exceeds defined limit ";

    /**
     * The Constant BYTES.
     */
    public static final String BYTES = " Bytes";

    /**
     * The Constant OBJECT_UNAVAILABLE.
     */
    public static final String OBJECT_UNAVAILABLE = "Object not available";

    /**
     * The Constant SELECTION_REQUIRED.
     */
    public static final String SELECTION_REQUIRED = "Must select Option";

    /**
     * The Constant TEMPLATE_OBJECT_TYPE.
     */
    public static final String REGEX_OBJECT_TYPE = "regexobject.type";

    /**
     * The Constant OBJECT_SELECTOR.
     */
    public static final String OBJECT_SELECTOR = "Object Selector";

    /**
     * The Constant SELECT.
     */
    public static final String SELECT = "select";

    /**
     * The Constant MATCH.
     */
    public static final String MATCH = "match";

    /**
     * The Constant UTF_8.
     */
    public static final String UTF_8 = "UTF-8";

    /**
     * The Constant CB2_PARSING_ERROR.
     */
    public static final String CB2_PARSING_ERROR = "ERROR parsing CB2 File";

    /**
     * Gets the template selector model.
     *
     * @param userIdStringFromGeneralHeader
     *         the user id string from general header
     * @param userNameFromGeneralHeader
     *         the user name from general header
     * @param jsonToBeUpdated
     *         the json to be updated
     *
     * @return the template selector model
     */
    @Override
    public Object getTemplateSelectorModel( String userIdStringFromGeneralHeader, String userNameFromGeneralHeader,
            String jsonToBeUpdated ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String id = null;
            if ( jsonToBeUpdated != null ) {
                JsonNode jsonNode = JsonUtils.toJsonNode( jsonToBeUpdated );
                if ( jsonNode.has( ID ) ) {
                    id = jsonNode.get( ID ).asText();
                }
            }
            if ( id != null ) {
                SelectionEntity selectionEntities = selectionManager.getSelectionDAO()
                        .getLatestObjectById( entityManager, SelectionEntity.class, UUID.fromString( id ) );
                String oldJson = ByteUtil.convertByteToString( selectionEntities.getJson() );
                selectionEntities.setJson( ByteUtil.convertStringToByte( jsonToBeUpdated ) );
                selectionManager.getSelectionDAO().saveOrUpdate( entityManager, selectionEntities );
                SelectionResponseUI genratedSelection = new SelectionResponseUI();
                genratedSelection.setId( selectionEntities.getId().toString() );
                if ( oldJson != null ) {
                    JsonNode jsonNodeOld = JsonUtils.toJsonNode( oldJson );
                    JsonNode jsonNodeNew = JsonUtils.toJsonNode( jsonToBeUpdated );
                    if ( jsonNodeOld != null && jsonNodeOld.has( TEMPLATE_OBJECT ) && !( jsonNodeOld.get( TEMPLATE_OBJECT ).get( VALUE )
                            .toString().equals( jsonNodeNew.get( TEMPLATE_OBJECT ).get( VALUE ).toString() ) ) ) {
                        List< TemplateEntity > templateEntities = templateDAO.getTemplateListBySelectionId( entityManager,
                                UUID.fromString( id ) );
                        deleteTemplate( entityManager,
                                templateEntities.stream().map( TemplateEntity::getId ).map( UUID::toString ).toList() );
                    }
                }
                return genratedSelection;
            } else {
                SelectionResponseUI selectionid = createSelection( entityManager, userIdStringFromGeneralHeader, jsonToBeUpdated );
                SelectionResponseUI genratedSelection = new SelectionResponseUI();
                genratedSelection.setId( selectionid.getId() );
                return genratedSelection;
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Delete template.
     *
     * @param ids
     *         the ids
     *
     * @return the boolean
     */
    @Override
    public Boolean deleteTemplate( List< String > ids ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return deleteTemplate( entityManager, ids );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Delete template boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param ids
     *         the ids
     *
     * @return the boolean
     */
    private Boolean deleteTemplate( EntityManager entityManager, List< String > ids ) {
        ids.forEach( id -> templateDAO.deleteTemplate( entityManager, templateDAO.getTemplate( entityManager, UUID.fromString( id ) ) ) );
        return Boolean.TRUE;
    }

    /**
     * Creates the selection.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdStringFromGeneralHeader
     *         the user id string from general header
     * @param json
     *         the json
     *
     * @return the selection response UI
     */
    private SelectionResponseUI createSelection( EntityManager entityManager, String userIdStringFromGeneralHeader, String json ) {
        FiltersDTO filtersDTO = new FiltersDTO();
        List< Object > items = new ArrayList<>();
        items.add( UUID.randomUUID() );
        filtersDTO.setItems( items );

        SelectionResponseUI selectionid = selectionManager.createSelection( entityManager, userIdStringFromGeneralHeader,
                SelectionOrigins.TEMPLATE_SELECTION, filtersDTO );

        SelectionEntity selectionEntis = selectionManager.getSelectionDAO()
                .getLatestObjectById( entityManager, SelectionEntity.class, UUID.fromString( selectionid.getId() ) );
        selectionEntis.setJson( ByteUtil.convertStringToByte( json ) );
        selectionManager.getSelectionDAO().saveOrUpdate( entityManager, selectionEntis );
        return selectionid;
    }

    /**
     * Gets the template ui.
     *
     * @param id
     *         the id
     *
     * @return the template ui
     */
    @Override
    public List< TableColumn > getTemplateUi( String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            try {
                UUID.fromString( id );
            } catch ( IllegalArgumentException exception ) {
                throw new SusException( SELECTION_INVALID );
            }

            SelectionEntity selectionEntis = selectionManager.getSelectionDAO()
                    .getLatestObjectById( entityManager, SelectionEntity.class, UUID.fromString( id ) );
            if ( selectionEntis == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.SELECTION_NOT_AVAILIABLE.getKey() ) );
            }

            return GUIUtils.listColumns( TemplateScanDTO.class );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the template list.
     *
     * @param selectionId
     *         the id
     * @param filter
     *         the filter
     *
     * @return the template list
     */
    @Override
    public FilteredResponse< TemplateScanDTO > getTemplateList( UUID selectionId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< TemplateScanDTO > templateScanDTOs;
            List< TemplateEntity > templateEntities = templateDAO.getTemplateListBySelectionId( entityManager, selectionId, filter );
            templateScanDTOs = new ArrayList<>();
            if ( CollectionUtils.isNotEmpty( templateEntities ) ) {
                for ( TemplateEntity templateEntity : templateEntities ) {
                    templateScanDTOs.add( prepareTemplateScanDTO( templateEntity ) );
                }
            }
            return PaginationUtil.constructFilteredResponse( filter, templateScanDTOs );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare template scan DTO.
     *
     * @param templateEntity
     *         the template entity
     *
     * @return the template scan DTO
     */
    private TemplateScanDTO prepareTemplateScanDTO( TemplateEntity templateEntity ) {
        TemplateScanDTO templateScanDTO = null;
        if ( templateEntity != null ) {
            templateScanDTO = new TemplateScanDTO();
            templateScanDTO.setId( templateEntity.getId() );
            templateScanDTO.setVariableName( templateEntity.getVariableName() );
            templateScanDTO.setLineNumber( templateEntity.getLineNumber() );
            templateScanDTO.setStart( templateEntity.getStart() );
            templateScanDTO.setEnd( templateEntity.getEnd() );
            templateScanDTO.setMatch( templateEntity.getMatch() );
        }
        return templateScanDTO;
    }

    /**
     * Gets the template edit.
     *
     * @param userId
     *         the user id
     * @param templateId
     *         the template id
     *
     * @return the template edit
     */
    @Override
    public Object getTemplateEdit( String userId, String templateId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String selectorValue = null;

            try {
                UUID.fromString( templateId );
            } catch ( IllegalArgumentException exception ) {
                throw new SusException( SELECTION_INVALID );
            }

            SelectionEntity selectionEntis = selectionManager.getSelectionDAO()
                    .getLatestObjectById( entityManager, SelectionEntity.class, UUID.fromString( templateId ) );
            if ( selectionEntis == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.SELECTION_NOT_AVAILIABLE.getKey() ) );
            } else {

                Map< String, Object > parameter = new HashMap<>();
                parameter = ( Map< String, Object > ) JsonUtils.jsonToMap( ByteUtil.convertByteToString( selectionEntis.getJson() ),
                        parameter );

                Map< String, Object > parameterObject = new HashMap<>();
                if ( parameter.containsKey( REGEX_OBJECT ) ) {
                    parameterObject = ( Map< String, Object > ) JsonUtils.jsonToMap( JsonUtils.toJson( parameter.get( REGEX_OBJECT ) ),
                            parameterObject );
                }

                if ( parameterObject.get( TYPE ) != null ) {
                    String parameterType = String.valueOf( parameterObject.get( TYPE ) );
                    if ( parameterType.equalsIgnoreCase( VariableDropDownEnum.SERVER.getId() ) || parameterType.equalsIgnoreCase(
                            VariableDropDownEnum.INTERNAL.getId() ) || parameterType.equalsIgnoreCase( VariableDropDownEnum.CB2.getId() )
                            || parameterType.equalsIgnoreCase( VariableDropDownEnum.LOCAL.getId() ) ) {
                        selectorValue = parameterObject.get( TYPE ).toString();
                    }
                }

            }
            List< UIFormItem > result = new ArrayList<>();
            result.add( prepareObjectHiddenForm( templateId ) );
            result.add( prepareObjectTypeForm( templateId, selectorValue ) );
            return result;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare object hidden form.
     *
     * @param wfId
     *         the wf id
     *
     * @return the UI form item
     */
    private UIFormItem prepareObjectHiddenForm( String wfId ) {
        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( ID );
        selectFormItem.setLabel( "ID" );
        selectFormItem.setValue( wfId );
        selectFormItem.setType( HIDDEND_PARAM );

        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();

        rules.put( REQUIRED_PARAM, false );
        message.put( REQUIRED_PARAM, SELECTION_REQUIRED );
        selectFormItem.setRules( rules );
        selectFormItem.setMessages( message );
        selectFormItem.setMultiple( Boolean.FALSE );
        return selectFormItem;
    }

    /**
     * Prepare object type form.
     *
     * @param wfId
     *         the wf id
     * @param value
     *         the value
     *
     * @return the UI form item
     */
    private UIFormItem prepareObjectTypeForm( String wfId, String value ) {
        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( REGEX_OBJECT_TYPE );
        if ( value == null ) {
            selectFormItem.setValue( VariableDropDownEnum.SERVER.getId() );
        } else {
            selectFormItem.setValue( value );
        }

        selectFormItem.setLabel( OBJECT_SELECTOR );
        selectFormItem.setType( SELECT );

        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();

        rules.put( REQUIRED_PARAM, true );
        message.put( REQUIRED_PARAM, SELECTION_REQUIRED );
        selectFormItem.setRules( rules );
        selectFormItem.setMessages( message );

        List< SelectOptionsUI > options = new ArrayList<>();
        options.add( new SelectOptionsUI( VariableDropDownEnum.INTERNAL.getId(), VariableDropDownEnum.INTERNAL.getName() ) );
        options.add( new SelectOptionsUI( VariableDropDownEnum.CB2.getId(), VariableDropDownEnum.CB2.getName() ) );
        options.add( new SelectOptionsUI( VariableDropDownEnum.SERVER.getId(), VariableDropDownEnum.SERVER.getName() ) );
        options.add( new SelectOptionsUI( VariableDropDownEnum.LOCAL.getId(), VariableDropDownEnum.LOCAL.getName() ) );
        selectFormItem.setMultiple( Boolean.FALSE );
        selectFormItem.setOptions( options );
        // same as regex dropdown
        selectFormItem.setBindFrom( "/workflow/scheme/regex/wf/" + wfId + "/dropdown/{__value__}" );
        return selectFormItem;
    }

    /**
     * Removes the scanned value from ui.
     *
     * @param UiList
     *         the ui list
     */
    private List< UIFormItem > removeScannedValueFromUi( List< UIFormItem > UiList ) {
        List< UIFormItem > matchLess = new ArrayList<>();
        UiList.stream().filter( uiItem -> !uiItem.getName().equalsIgnoreCase( MATCH ) ).forEach( matchLess::add );
        return matchLess;
    }

    /**
     * Creates the template UI form.
     *
     * @param id
     *         the id
     *
     * @return the list
     */
    @Override
    public UIForm createTemplateUIForm( String id ) {
        List< UIFormItem > createUiList = GUIUtils.prepareForm( true, new TemplateScanDTO() );
        return GUIUtils.createFormFromItems( removeScannedValueFromUi( createUiList ) );
    }

    /**
     * Find matched value.
     *
     * @param entityManager
     *         the entity manager
     * @param lineNumber
     *         the line number
     * @param start
     *         the start
     * @param end
     *         the end
     * @param selectionId
     *         the workflow id
     *
     * @return the string
     */
    private String findMatchedValue( EntityManager entityManager, String lineNumber, String start, String end, UUID selectionId ) {
        String scannedValue;
        File file = new File( getFullPathContent( entityManager, getUserNameFromGeneralHeader(), selectionId ) );

        try ( BufferedReader reader = Files.newBufferedReader( Paths.get( file.getAbsolutePath() ), StandardCharsets.UTF_8 ) ) {
            String line = reader.lines().skip( Integer.parseInt( lineNumber ) - 1 ).limit( 1 ).collect( Collectors.joining() );
            scannedValue = line.substring( Integer.parseInt( start ), Integer.parseInt( end ) );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( "Please provide correct Values in fields" );
        }

        return scannedValue;
    }

    /**
     * Creates the template.
     *
     * @param selectionId
     *         the workflow id
     * @param templateScanDTO
     *         the template scan DTO
     *
     * @return the template scan DTO
     */
    @Override
    public TemplateScanDTO createTemplate( UUID selectionId, TemplateScanDTO templateScanDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            templateScanDTO.setMatch(
                    findMatchedValue( entityManager, templateScanDTO.getLineNumber(), templateScanDTO.getStart(), templateScanDTO.getEnd(),
                            selectionId ) );
            TemplateEntity templateEntity = prepareTemplateEntityToCreate( selectionId, templateScanDTO );
            TemplateEntity saveTemplateEntity = templateDAO.saveTemplate( entityManager, templateEntity );
            return prepareTemplateScanDTO( saveTemplateEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare template entity to create.
     *
     * @param selectionId
     *         the workflow id
     * @param templateScanDTO
     *         the template scan DTO
     *
     * @return the template entity
     */
    private TemplateEntity prepareTemplateEntityToCreate( UUID selectionId, TemplateScanDTO templateScanDTO ) {
        TemplateEntity templateEntity = null;
        if ( templateScanDTO != null ) {
            templateEntity = prepareTemplateEntity( UUID.randomUUID(), templateScanDTO.getVariableName(), templateScanDTO.getLineNumber(),
                    templateScanDTO.getStart(), templateScanDTO.getEnd(), templateScanDTO.getMatch(), selectionId );
        }
        return templateEntity;
    }

    /**
     * Prepare template entity.
     *
     * @param id
     *         the id
     * @param variableName
     *         the variable name
     * @param lineNumber
     *         the line number
     * @param start
     *         the start
     * @param end
     *         the end
     * @param match
     *         the match
     * @param selectionId
     *         the workflow id
     *
     * @return the template entity
     */
    private TemplateEntity prepareTemplateEntity( UUID id, String variableName, String lineNumber, String start, String end, String match,
            UUID selectionId ) {
        TemplateEntity templateEntity = new TemplateEntity();
        templateEntity.setId( id );
        templateEntity.setVariableName( variableName );
        templateEntity.setLineNumber( lineNumber );
        templateEntity.setStart( start );
        templateEntity.setEnd( end );
        templateEntity.setMatch( match );
        templateEntity.setSelectionId( selectionId );
        return templateEntity;
    }

    /**
     * Gets the file content.
     *
     * @param userId
     *         the user id
     * @param userUID
     *         the user UID
     * @param id
     *         the id
     *
     * @return the file content
     */
    @Override
    public Object getFileContent( String userId, String userUID, UUID id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< Object > fileScannedContent = new ArrayList<>();

            List< TemplateEntity > templateEntities = templateDAO.getTemplateListBySelectionId( entityManager, id );
            File file = new File( getFullPathContent( entityManager, userUID, id ) );

            List< ObjectVariableDTO > objectVariableDTOs;
            ScanFileDTO scanFileDTO;

            List< ScanResponseDTO > scanResponseDTOsIndexed = new ArrayList<>();

            if ( CollectionUtils.isNotEmpty( templateEntities ) ) {
                objectVariableDTOs = new ArrayList<>();
                for ( TemplateEntity templateEntity : templateEntities ) {
                    ScanResponseDTO scanResponseDTO = new ScanResponseDTO();

                    objectVariableDTOs.add( prepareObjectVariableDTO( templateEntity ) );

                    scanResponseDTO.setEnd( templateEntity.getEnd() );
                    scanResponseDTO.setLineNumber( templateEntity.getLineNumber() );
                    scanResponseDTO.setMatch( templateEntity.getMatch() );
                    scanResponseDTO.setStart( templateEntity.getStart() );
                    scanResponseDTO.setLineNumberIndexed( templateEntity.getLineNumber() );

                    scanResponseDTOsIndexed.add( scanResponseDTO );
                }
                scanFileDTO = new ScanFileDTO();
                scanFileDTO.setFile( file.getAbsolutePath() );
                scanFileDTO.setVariables( objectVariableDTOs );

                if ( CollectionUtils.isNotEmpty( scanResponseDTOsIndexed ) ) {
                    for ( int i = 0; i < scanResponseDTOsIndexed.size(); i++ ) {
                        ScanResponseDTO indexedObj = scanResponseDTOsIndexed.get( i );
                        ObjectVariableDTO indexedMap = objectVariableDTOs.get( i );
                        TemplateEntity entity = templateDAO.getTemplate( entityManager, indexedMap.getId() );
                        entity.setMatch( indexedObj.getMatch() );
                        entity.setEnd( indexedObj.getEnd() );
                        entity.setStart( indexedObj.getStart() );
                        templateDAO.updateTemplate( entityManager, entity );
                    }

                    fileScannedContent = prepareScanContent( scanFileDTO, scanResponseDTOsIndexed.stream().collect( collectingAndThen(
                            toCollection( () -> new TreeSet<>(
                                    Comparator.comparing( ScanResponseDTO::getStart, Comparator.nullsLast( Comparator.naturalOrder() ) )
                                            .thenComparing( ScanResponseDTO::getEnd, Comparator.nullsLast( Comparator.naturalOrder() ) )
                                            .thenComparing( ScanResponseDTO::getLineNumber,
                                                    Comparator.nullsLast( Comparator.naturalOrder() ) ) ) ), ArrayList::new ) ) );
                    if ( CollectionUtils.isEmpty( fileScannedContent ) ) {
                        fileScannedContent = getRequiredCharIfOnlyFileSelected( file );
                    }
                }

            } else if ( file != null ) {
                if ( file.length() <= Long.parseLong( SCHEME_TEMPLATE_FILE_SIZE ) ) {
                    fileScannedContent = getRequiredCharIfOnlyFileSelected( file );
                } else {
                    throw new SusException( file.getName() + FILE_SIZE_ERROR + SCHEME_TEMPLATE_FILE_SIZE + BYTES );
                }
            }
            return fileScannedContent;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare object variable DTO.
     *
     * @param templateEntity
     *         the template entity
     *
     * @return the object variable DTO
     */
    private ObjectVariableDTO prepareObjectVariableDTO( TemplateEntity templateEntity ) {
        ObjectVariableDTO objectVariableDTO = null;
        if ( templateEntity != null ) {
            objectVariableDTO = new ObjectVariableDTO();
            objectVariableDTO.setId( templateEntity.getId() );
            objectVariableDTO.setVariableName( templateEntity.getVariableName() );

            ScanResponseDTO ScanResponseDTO = new ScanResponseDTO();
            ScanResponseDTO.setLineNumber( templateEntity.getLineNumber() );
            ScanResponseDTO.setMatch( templateEntity.getMatch() );
            ScanResponseDTO.setStart( templateEntity.getStart() );
            ScanResponseDTO.setEnd( templateEntity.getEnd() );
            objectVariableDTO.setHighlight( ScanResponseDTO );
        }
        return objectVariableDTO;
    }

    /**
     * Gets the scanned lines.
     *
     * @param contents
     *         the contents
     * @param scanResponseDTOs
     *         the scan response DT os
     *
     * @return the scanned lines
     */
    private List< Integer > getScannedLines( List< String > contents, List< ScanResponseDTO > scanResponseDTOs ) {
        List< Integer > scannedLine = new ArrayList<>();

        for ( int scannerLine = 0; scannerLine <= contents.size(); scannerLine++ ) {
            for ( ScanResponseDTO responseDTO : scanResponseDTOs ) {
                if ( null != responseDTO.getStart() && scannerLine == Integer.parseInt( responseDTO.getLineNumberIndexed() ) ) {
                    scannedLine.add( scannerLine - ConstantsInteger.INTEGER_VALUE_ONE );
                }
            }
        }
        return scannedLine;
    }

    /**
     * Gets the content from file.
     *
     * @param path
     *         the path
     *
     * @return the content from file
     */
    private List< String > getContentFromFile( String path ) {
        List< String > contents = new ArrayList<>();
        try {
            contents = org.apache.commons.io.FileUtils.readLines( new File( path ), UTF_8 );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        }
        return contents;
    }

    /**
     * Prepare scan content.
     *
     * @param scanFileDTO
     *         the scan file DTO
     * @param scanResponseDTOs
     *         the scan response DT os
     *
     * @return the list
     */
    private List< Object > prepareScanContent( ScanFileDTO scanFileDTO, List< ScanResponseDTO > scanResponseDTOs ) {
        List< Object > fileScannedContent = new ArrayList<>();
        List< String > contents = getContentFromFile( scanFileDTO.getFile() );
        List< Integer > scannedLine = getScannedLines( contents, scanResponseDTOs );
        if ( scannedLine.isEmpty() ) {
            return getRequiredCharIfOnlyFileSelected( new File( scanFileDTO.getFile() ) );
        }

        for ( int index = 0; index < contents.size(); index++ ) {
            List< Object > objects = new ArrayList<>();
            objects.add( contents.get( index ) );
            List< ScanResponseDTO > responseDTOs = new ArrayList<>();
            for ( ScanResponseDTO responseDTO : scanResponseDTOs ) {
                if ( null != responseDTO.getStart() && scannedLine.contains( index ) ) {
                    responseDTO.setLineNumber( responseDTO.getLineNumberIndexed() );
                    responseDTOs.add( responseDTO );
                }
            }
            objects.add( responseDTOs );
            objects.add( index + 1 );
            Collections.reverse( objects );
            fileScannedContent.add( objects );
        }
        return fileScannedContent;
    }

    /**
     * Gets the full path content.
     *
     * @param entityManager
     *         the entity manager
     * @param userUID
     *         the user UID
     * @param selectionId
     *         the selectionId
     *
     * @return the full path content
     */
    private String getFullPathContent( EntityManager entityManager, String userUID, UUID selectionId ) {
        JsonNode selectedJson = getJson( entityManager, selectionId );
        JsonNode objectNode = selectedJson.get( REGEX_OBJECT );
        String fullPath = null;
        JsonNode selectedValue = null;
        if ( objectNode != null ) {
            if ( objectNode.has( VALUE ) ) {
                selectedValue = objectNode.get( VALUE );
            }
            if ( objectNode.get( TYPE ).asText().equalsIgnoreCase( VariableDropDownEnum.LOCAL.getId() ) ) {
                fullPath = getFilePathContent( entityManager, UUID.fromString( selectedValue.get( ID ).asText() ) );
            } else if ( objectNode.get( TYPE ).asText().equalsIgnoreCase( VariableDropDownEnum.INTERNAL.getId() ) ) {
                List< UUID > selectedItemsList = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectedValue.asText() );
                if ( selectedItemsList != null && !selectedItemsList.isEmpty() ) {
                    fullPath = getFilePathContent( entityManager, selectedItemsList.get( 0 ) );
                }
            } else if ( objectNode.get( TYPE ).asText().equalsIgnoreCase( VariableDropDownEnum.SERVER.getId() ) ) {
                if ( selectedValue.isArray() ) {
                    for ( JsonNode jsonNode : selectedValue ) {
                        fullPath = jsonNode.get( FULL_PATH ).asText();
                    }
                }
            } else if ( objectNode.get( TYPE ).asText().equalsIgnoreCase( VariableDropDownEnum.CB2.getId() ) ) {
                List< String > cb2OidList = selectionManager.getSelectedIdsStringListBySelectionId( entityManager, selectedValue.asText() );
                if ( cb2OidList != null && !cb2OidList.isEmpty() ) {
                    String cb2OID = cb2OidList.get( 0 );
                    BmwCaeBenchEntity cb2ObjectEntity = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager,
                            UUID.fromString( cb2OID ) );
                    fullPath = downloadCB2FileAndReturnPath( userUID, cb2ObjectEntity );
                }
            }
        }
        return fullPath;
    }

    /**
     * Gets the file path content.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the file path content
     */
    private String getFilePathContent( EntityManager entityManager, UUID id ) {
        DocumentEntity documentEntity = getDocumentObjectByObjectId( entityManager, id );
        if ( documentEntity == null ) {
            throw new SusException( OBJECT_UNAVAILABLE );
        }
        String selectedFilePath = PropertiesManager.getVaultPath() + documentEntity.getFilePath();
        File selectedFileObj = new File(
                PropertiesManager.getDefaultServerTempPath() + File.separator + UUID.randomUUID() + documentEntity.getFileName()
                        .replaceAll( "\\s", "_" ) );
        try ( InputStream decritedStreamDromVault = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream(
                new File( selectedFilePath ), prepareEncryptionDecryptionDTO( documentEntity.getEncryptionDecryption() ) ) ) {
            Files.copy( decritedStreamDromVault, selectedFileObj.toPath() );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        }
        return selectedFileObj.getAbsolutePath();
    }

    /**
     * Gets the required char if only file selected.
     *
     * @param file
     *         the file
     *
     * @return the required char if only file selected
     */
    private List< Object > getRequiredCharIfOnlyFileSelected( File file ) {
        List< Object > fileScannedContent = new ArrayList<>();
        Integer scannerLine = 0;

        try ( FileReader fileReader = new FileReader( file.getAbsolutePath() ); BufferedReader bufferedReader = new BufferedReader(
                fileReader ) ) {
            String readLine;
            while ( ( readLine = bufferedReader.readLine() ) != null ) {
                List< Object > linesObject = new ArrayList<>();
                linesObject.add( readLine );
                List< ScanResponseDTO > scanResponseDTOs = new ArrayList<>();
                linesObject.add( scanResponseDTOs );
                linesObject.add( ( scannerLine + 1 ) );
                Collections.reverse( linesObject );
                fileScannedContent.add( linesObject );
                scannerLine++;
            }
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
        return fileScannedContent;
    }

    /**
     * Edits the template UI form.
     *
     * @param id
     *         the id
     *
     * @return the list
     */
    @Override
    public UIForm editTemplateUIForm( UUID id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > editUiList = GUIUtils.prepareForm( false, getTemplate( entityManager, id ) );
            return GUIUtils.createFormFromItems( removeScannedValueFromUi( editUiList ) );
        } finally {
            entityManager.close();
        }
    }

    private TemplateScanDTO getTemplate( EntityManager entityManager, UUID id ) {
        return prepareTemplateScanDTO( templateDAO.getTemplate( entityManager, id ) );
    }

    /**
     * Update template.
     *
     * @param id
     *         the id
     * @param templateScanDTO
     *         the template scan DTO
     *
     * @return the template scan DTO
     */
    @Override
    public TemplateScanDTO updateTemplate( UUID id, TemplateScanDTO templateScanDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Notification notification = templateScanDTO.validate();
            if ( notification.hasErrors() ) {
                throw new SusException( notification.getErrors().toString() );
            }

            TemplateEntity templateEntity = templateDAO.getTemplate( entityManager, id );

            UUID selectionId = templateEntity.getSelectionId();
            templateScanDTO.setMatch(
                    findMatchedValue( entityManager, templateScanDTO.getLineNumber(), templateScanDTO.getStart(), templateScanDTO.getEnd(),
                            selectionId ) );
            templateEntity = prepareTemplateEntityToUpdate( templateEntity, templateScanDTO );
            templateEntity = templateDAO.updateTemplate( entityManager, templateEntity );
            return prepareTemplateScanDTO( templateEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare template entity to update.
     *
     * @param templateEntityFromDB
     *         the template entity from DB
     * @param templateScanDTO
     *         the template scan DTO
     *
     * @return the template entity
     */
    private TemplateEntity prepareTemplateEntityToUpdate( TemplateEntity templateEntityFromDB, TemplateScanDTO templateScanDTO ) {
        if ( templateEntityFromDB != null ) {
            if ( templateScanDTO.getVariableName() != null ) {
                templateEntityFromDB.setVariableName( templateScanDTO.getVariableName() );
            }
            if ( templateScanDTO.getLineNumber() != null ) {
                templateEntityFromDB.setLineNumber( templateScanDTO.getLineNumber() );
            }
            if ( templateScanDTO.getMatch() != null ) {
                templateEntityFromDB.setMatch( templateScanDTO.getMatch() );
            }
            if ( templateScanDTO.getStart() != null ) {
                templateEntityFromDB.setStart( templateScanDTO.getStart() );
            }
            if ( templateScanDTO.getEnd() != null ) {
                templateEntityFromDB.setEnd( templateScanDTO.getEnd() );
            }
        }
        return templateEntityFromDB;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TemplateEntity > duplicateTemplateEntitiesBySelectionId( UUID oldSelectionId, UUID newSelectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< TemplateEntity > regexEntities = templateDAO.getTemplateListBySelectionId( entityManager, oldSelectionId );
            List< TemplateEntity > newEntities = new ArrayList<>();
            if ( CollectionUtils.isEmpty( regexEntities ) ) {
                return newEntities;
            }
            regexEntities.forEach( oldEntity -> {
                TemplateEntity newEntity = oldEntity.copy();
                newEntity.setSelectionId( newSelectionId );
                newEntities.add( templateDAO.saveTemplate( entityManager, newEntity ) );
            } );
            return newEntities;
        } finally {
            entityManager.close();
        }
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
     * Gets the bmw cae bench DAO.
     *
     * @return the bmw cae bench DAO
     */
    public BmwCaeBenchDAO getBmwCaeBenchDAO() {
        return bmwCaeBenchDAO;
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
     * Gets the template DAO.
     *
     * @return the template DAO
     */
    @Override
    public TemplateDAO getTemplateDAO() {
        return templateDAO;
    }

    /**
     * Sets the template DAO.
     *
     * @param templateDAO
     *         the new template DAO
     */
    public void setTemplateDAO( TemplateDAO templateDAO ) {
        this.templateDAO = templateDAO;
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
     * Sets document dao.
     *
     * @param documentDAO
     *         the document dao
     */
    public void setDocumentDAO( DocumentDAO documentDAO ) {
        this.documentDAO = documentDAO;
    }

}
