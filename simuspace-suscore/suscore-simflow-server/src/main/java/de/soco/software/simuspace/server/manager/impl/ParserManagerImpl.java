package de.soco.software.simuspace.server.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.manager.BaseManager;
import de.soco.software.simuspace.server.manager.ParserManager;
import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.BmwCaeBenchEnums;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.VariableDropDownEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.formitem.impl.TableFormItem;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.parser.model.ParserDTO;
import de.soco.software.simuspace.suscore.common.parser.model.ParserPartDTO;
import de.soco.software.simuspace.suscore.common.parser.model.ParserPropertiesDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.EncryptAndDecryptUtils;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.PythonUtils;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.BmwCaeBenchDAO;
import de.soco.software.simuspace.suscore.data.common.dao.ParserSchemaDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.ParserVariableDTO;
import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.ParserEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.CustomVariableDTO;
import de.soco.software.simuspace.suscore.document.dao.DocumentDAO;

/**
 * The Class ParserManagerImpl.
 *
 * @author noman arshad
 * @since 2.0
 */
@Log4j2
public class ParserManagerImpl extends BaseManager implements ParserManager {

    /**
     * The Constant FULL_PATH.
     */
    private static final String FULL_PATH = "fullPath";

    /**
     * The Constant FILTER_ALL.
     */
    private static final String FILTER_ALL = "{\"draw\":2,\"start\":0,\"length\":1000000,\"search\":\"\",\"columns\":[{\"name\":\"id\",\"visible\":false,\"dir\":null,\"filters\":null,\"reorder\":0},{\"name\":\"variableName\",\"visible\":false,\"dir\":null,\"filters\":null,\"reorder\":1},{\"name\":\"name\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":2},{\"name\":\"type\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":3},{\"name\":\"scannedValue\",\"visible\":true,\"dir\":null,\"filters\":null,\"reorder\":4}]}";

    /**
     * The Constant OBJECT_DOT.
     */
    private static final String OBJECT_DOT = "object.";

    /**
     * The Constant FILE_EXPLORER_FORM.
     */
    private static final String FILE_EXPLORER_FORM = "fileExplorer";

    /**
     * The Constant DOES_NOT_CONTAIN.
     */
    private static final String DOES_NOT_CONTAIN = "Does Not Contain";

    /**
     * The Constant CONTAINS.
     */
    private static final String CONTAINS = "Contains";

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
     * The Constant PARSER2.
     */
    private static final String PARSER2 = "parser";

    /**
     * The Constant SELECTION.
     */
    private static final String SELECTION = "selection";

    /**
     * The Constant HIDDEN.
     */
    private static final String HIDDEN = "hidden";

    /**
     * The Constant OBJECT_TYPE2.
     */
    private static final String OBJECT_TYPE2 = "Object Type";

    /**
     * The Constant OBJECT_TYPE.
     */
    private static final String OBJECT_TYPE = "object.type";

    /**
     * The Constant WORKFLOW_PARSER_OBJECTTYPE_VALUE.
     */
    private static final String WORKFLOW_PARSER_OBJECTTYPE_VALUE = "/workflow/parser/{parserId}/objecttype/{__value__}";

    /**
     * The Constant SELECT.
     */
    private static final String SELECT = "select";

    /**
     * The Constant PARSER_IDENTIFIER.
     */
    private static final String PARSER_IDENTIFIER = "{parserId}";

    /**
     * The Constant OBJECT_PARSER2.
     */
    private static final String OBJECT_PARSER2 = "Object Parser";

    /**
     * The Constant OBJECT_PARSER.
     */
    private static final String OBJECT_PARSER = "object.parser";

    /**
     * The Constant OBJECT.
     */
    private static final String OBJECT = "object";

    /**
     * The Constant SELECTION_TITAL.
     */
    private static final String SELECTION_TITAL = "Selection";

    /**
     * The Constant ADD.
     */
    private static final String ADD = "4100054x4";

    /**
     * The Constant EDIT.
     */
    private static final String EDIT = "4100014x4";

    /**
     * The Constant ID.
     */
    private static final String ID = "id";

    /**
     * The Constant SCANNED_VALUE.
     */
    private static final String SCANNED_VALUE = "scannedValue";

    /**
     * The Constant VARIABLE_NAME.
     */
    private static final String VARIABLE_NAME = "variableName";

    /**
     * The Constant TYPE.
     */
    private static final String TYPE = "type";

    /**
     * The Constant STATIC.
     */
    private static final String STATIC = "static";

    /**
     * The Constant NAME.
     */
    private static final String NAME = "name";

    /**
     * The Constant REQUIRED.
     */
    private static final String REQUIRED = "required";

    /**
     * The Constant MUST_CHOSE_OPTION.
     */
    private static final String MUST_CHOSE_OPTION = "Must Chose Option";

    /**
     * The Constant ERROR_PARSING_CB2_FILE.
     */
    private static final String ERROR_PARSING_CB2_FILE = "ERROR parsing CB2 File";

    /**
     * The Constant EXTERNAL.
     */
    private static final String EXTERNAL = "external";

    /**
     * The parser schema DAO.
     */
    private ParserSchemaDAO parserSchemaDAO;

    /**
     * The selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The context menu manager.
     */
    private ContextMenuManager contextMenuManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The bmw cae bench DAO.
     */
    private BmwCaeBenchDAO bmwCaeBenchDAO;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Gets the parser object.
     *
     * @param userId
     *         the user id
     * @param userUID
     *         the user UID
     * @param json
     *         the json
     *
     * @return the parser object
     */
    @Override
    public Object getParserObject( String userId, String userUID, String json ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Map< String, String > formMap = getFormDataFromJson( json );
            if ( formMap == null ) {
                ParserEntity saved = parserSchemaDAO.save( entityManager, new ParserEntity( UUID.randomUUID(), "Abaqus", null, null ) );
                SelectionResponseUI genratedSelection = new SelectionResponseUI();
                genratedSelection.setId( saved.getId().toString() );
                return genratedSelection;
            }

            String objectParserIdMain = formMap.get( ID );
            String parserType = formMap.get( TYPE );
            String parserParsser = formMap.get( PARSER2 );
            String selectionObject = formMap.get( SELECTION );

            File selectedFileObj = null;

            if ( VariableDropDownEnum.INTERNAL.getId().equalsIgnoreCase( parserType ) || VariableDropDownEnum.LOCAL.getId()
                    .equalsIgnoreCase( parserType ) ) {

                DocumentEntity selectedFileDoc = getSelectedInternalObjectFilePath( entityManager, parserType, selectionObject );
                if ( selectedFileDoc == null ) {
                    log.error( "Object not available" );
                    throw new SusException( "Object not available" );
                }
                String selectedFilePath = PropertiesManager.getVaultPath() + selectedFileDoc.getFilePath();

                selectedFileObj = new File(
                        PropertiesManager.getDefaultServerTempPath() + File.separator + UUID.randomUUID() + selectedFileDoc.getFileName()
                                .replaceAll( "\\s", "_" ) );
                try ( InputStream decritedStreamDromVault = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream(
                        new File( selectedFilePath ), prepareEncryptionDecryptionDTO( selectedFileDoc.getEncryptionDecryption() ) ) ) {
                    Files.copy( decritedStreamDromVault, selectedFileObj.toPath() );
                } catch ( IOException e ) {
                    log.error( "For ParserFile copy Failed :", e );
                }

            } else if ( VariableDropDownEnum.SERVER.getId().equalsIgnoreCase( parserType ) ) {
                selectedFileObj = new File( selectionObject );
            } else if ( VariableDropDownEnum.CB2.getId().equalsIgnoreCase( parserType ) ) {

                List< String > cb2OidList = selectionManager.getSelectedIdsStringListBySelectionId( entityManager, selectionObject );
                if ( cb2OidList != null && !cb2OidList.isEmpty() ) {
                    String cb2OID = cb2OidList.get( 0 );
                    BmwCaeBenchEntity cb2ObjectEntity = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager,
                            UUID.fromString( cb2OID ) );
                    selectedFileObj = new File( downloadCB2FileAndReturnPath( userUID, cb2ObjectEntity ) );
                }
            }

            String outputFile =
                    PropertiesManager.getDefaultServerTempPath() + File.separator + UUID.randomUUID() + new Date().getSeconds() + ".txt";
            FileUtils.setGlobalReadFilePermissions( selectedFileObj );

            PythonUtils.callParserExtractionPythonFile( userUID, selectedFileObj.getAbsolutePath(), outputFile, parserParsser );
            ParserDTO parserAvailableList = new ParserDTO();
            File myObj = new File( outputFile );
            try ( InputStream targetConfigStream = new FileInputStream( myObj ) ) {
                parserAvailableList = JsonUtils.jsonToObject( targetConfigStream, ParserDTO.class );
            } catch ( IOException e ) {
                log.error( "Abaqus File reading ERROR", e );
            }

            ParserEntity saved = saveParserEntity( entityManager, json, objectParserIdMain, parserType, parserParsser,
                    parserAvailableList );

            SelectionResponseUI genratedSelection = new SelectionResponseUI();
            genratedSelection.setId( saved.getId().toString() );

            return genratedSelection;
        } finally {
            entityManager.close();
        }

    }

    /**
     * Save parser entity.
     *
     * @param entityManager
     *         the entity manager
     * @param json
     *         the json
     * @param objectParserIdMain
     *         the object parser id main
     * @param parserType
     *         the parser type
     * @param parserParsser
     *         the parser parsser
     * @param parserAvailableList
     *         the parser available list
     *
     * @return the parser entity
     */
    private ParserEntity saveParserEntity( EntityManager entityManager, String json, String objectParserIdMain, String parserType,
            String parserParsser, ParserDTO parserAvailableList ) {
        ParserEntity parserEntity = new ParserEntity();
        parserEntity.setId( UUID.fromString( objectParserIdMain ) );
        parserEntity.setName( VariableDropDownEnum.getNameById( parserType ) );
        parserEntity.setType( parserParsser );
        parserEntity.setJsonSchema( JsonUtils.toJson( parserAvailableList ) );
        parserEntity.setFormJson( json );
        return parserSchemaDAO.saveOrUpdate( entityManager, parserEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentEntity getSelectedInternalObjectFilePath( EntityManager entityManager, String parserType, String selectionObject ) {
        if ( parserType.equalsIgnoreCase( VariableDropDownEnum.INTERNAL.getName() ) || parserType.equalsIgnoreCase(
                VariableDropDownEnum.INTERNAL.getId() ) ) {
            List< UUID > selectedItemsList = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionObject );
            if ( selectedItemsList != null && !selectedItemsList.isEmpty() ) {
                return getDocumentObjectByObjectId( entityManager, selectedItemsList.get( 0 ) );
            }
        } else if ( parserType.equalsIgnoreCase( VariableDropDownEnum.LOCAL.getName() ) || parserType.equalsIgnoreCase(
                VariableDropDownEnum.LOCAL.getId() ) ) {
            return getDocumentObjectByObjectId( entityManager, UUID.fromString( selectionObject ) );
        } else if ( parserType.equalsIgnoreCase( VariableDropDownEnum.SERVER.getName() ) || parserType.equalsIgnoreCase(
                VariableDropDownEnum.SERVER.getId() ) ) {
            return null;
        }
        return null;
    }

    /**
     * Gets the form data from json.
     *
     * @param json
     *         the json
     *
     * @return the form data from json
     */
    @Override
    public Map< String, String > getFormDataFromJson( String json ) {
        Map< String, String > mapForm = new HashMap<>();
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = ( JSONObject ) parser.parse( json );
            if ( !jsonObject.containsKey( ID ) && jsonObject.get( ID ) == null ) {
                return null;
            }
            mapForm.put( ID, ( String ) jsonObject.get( ID ) );
            JSONObject parserObject = ( JSONObject ) jsonObject.get( OBJECT );
            mapForm.put( PARSER2, ( String ) parserObject.get( PARSER2 ) );
            String objectType = ( String ) parserObject.get( TYPE );
            mapForm.put( TYPE, objectType );
            String external = ( String ) jsonObject.get( EXTERNAL );
            mapForm.put( EXTERNAL, external );
            String selectionKey = VariableDropDownEnum.getNameById( objectType );

            if ( parserObject.containsKey( selectionKey ) ) {
                if ( VariableDropDownEnum.INTERNAL.getName().equalsIgnoreCase( selectionKey ) ) {
                    // internal selection added
                    mapForm.put( SELECTION, ( String ) parserObject.get( selectionKey ) );

                } else if ( VariableDropDownEnum.SERVER.getName().equalsIgnoreCase( selectionKey ) ) {
                    // Server selection added
                    List< Object > serverFiles = new ArrayList<>();
                    serverFiles = ( List< Object > ) JsonUtils.jsonToList( JsonUtils.toJson( parserObject.get( selectionKey ) ),
                            serverFiles );
                    Map< String, String > serverFileDetails = new HashMap<>();
                    serverFileDetails = ( Map< String, String > ) JsonUtils.jsonToMap( JsonUtils.toJson( serverFiles.get( 0 ) ),
                            serverFileDetails );
                    String fullFilePath = serverFileDetails.get( FULL_PATH );
                    mapForm.put( SELECTION, fullFilePath );
                } else if ( VariableDropDownEnum.CB2.getName().equalsIgnoreCase( selectionKey ) ) {
                    // CB2 selection added
                    mapForm.put( SELECTION, ( String ) parserObject.get( selectionKey ) );

                } else if ( VariableDropDownEnum.LOCAL.getName().equalsIgnoreCase( selectionKey ) ) {
                    // Local selection added
                    DocumentDTO docSelected = JsonUtils.jsonToObject( JsonUtils.toJson( parserObject.get( selectionKey ) ),
                            DocumentDTO.class );
                    mapForm.put( SELECTION, docSelected.getId() );
                }
            }

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return mapForm;
    }

    /**
     * Gets the form submitted data by type.
     *
     * @param json
     *         the json
     *
     * @return the form submitted data by type
     */
    public Object getFormSubmittedDataByType( String json ) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = ( JSONObject ) parser.parse( json );
            if ( !jsonObject.containsKey( ID ) && jsonObject.get( ID ) == null ) {
                return null;
            }
            JSONObject parserObject = ( JSONObject ) jsonObject.get( OBJECT );
            String objectType = ( String ) parserObject.get( TYPE );
            String selectionKey = VariableDropDownEnum.getNameById( objectType );

            if ( parserObject.containsKey( selectionKey ) && ( VariableDropDownEnum.INTERNAL.getName().equalsIgnoreCase( selectionKey )
                    || VariableDropDownEnum.SERVER.getName().equalsIgnoreCase( selectionKey ) || VariableDropDownEnum.CB2.getName()
                    .equalsIgnoreCase( selectionKey ) || VariableDropDownEnum.LOCAL.getName().equalsIgnoreCase( selectionKey ) ) ) {
                return parserObject.get( selectionKey );
            }

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return ConstantsString.EMPTY_STRING;
    }

    /**
     * Gets the server file selection object by json.
     *
     * @param json
     *         the json
     *
     * @return the server file selection object by json
     */
    @Override
    public Map< String, String > getServerFileSelectionObjectByJson( String json ) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = ( JSONObject ) parser.parse( json );
            if ( !jsonObject.containsKey( ID ) && jsonObject.get( ID ) == null ) {
                return null;
            }
            JSONObject parserObject = ( JSONObject ) jsonObject.get( OBJECT );
            String objectType = ( String ) parserObject.get( TYPE );
            String selectionKey = VariableDropDownEnum.getNameById( objectType );

            if ( parserObject.containsKey( selectionKey ) && ( VariableDropDownEnum.SERVER.getName().equalsIgnoreCase( selectionKey ) ) ) {
                // Server selection added
                ArrayList< Object > serverFile = new ArrayList<>();
                serverFile = ( ArrayList< Object > ) JsonUtils.jsonToList( JsonUtils.toJson( parserObject.get( selectionKey ) ),
                        serverFile );
                Map< String, String > serverFileDetails = new HashMap<>();
                serverFileDetails = ( Map< String, String > ) JsonUtils.jsonToMap( JsonUtils.toJson( serverFile.get( 0 ) ),
                        serverFileDetails );
                return serverFileDetails;
            }

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return null;
    }

    /**
     * Gets the parser available ui.
     *
     * @param userId
     *         the user id
     * @param parserId
     *         the parser id
     *
     * @return the parser available ui
     */
    @Override
    public List< TableColumn > getParserAvailableUi( String userId, String parserId ) {
        List< TableColumn > list = GUIUtils.listColumns( ParserVariableDTO.class );
        for ( TableColumn tableColumn : list ) {
            if ( tableColumn.getName().equalsIgnoreCase( VARIABLE_NAME ) ) {
                tableColumn.setVisible( false );
            }
        }
        return list;
    }

    /**
     * Gets the parser selected ui.
     *
     * @param userId
     *         the user id
     * @param parserId
     *         the parser id
     *
     * @return the parser selected ui
     */
    @Override
    public List< TableColumn > getParserSelectedUi( String userId, String parserId ) {
        return GUIUtils.listColumns( ParserVariableDTO.class );
    }

    /**
     * Gets the parser edit.
     *
     * @param userId
     *         the user id
     * @param parserId
     *         the parser id
     *
     * @return the parser edit
     */
    @Override
    public Object getParserEdit( String userId, String parserId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ParserEntity parserEntity = parserSchemaDAO.getLatestObjectById( entityManager, ParserEntity.class,
                    UUID.fromString( parserId ) );
            List< UIFormItem > parserForm = new ArrayList<>();
            parserForm.add( prepareObjectHiddenForm( parserId ) );
            parserForm.add( prepareObjectTypeForm( ( parserEntity != null ? parserEntity.getName() : "" ), parserId ) );
            parserForm.add( prepareObjectParserForm( ( parserEntity != null ? parserEntity.getType() : "" ) ) );
            return parserForm;
        } finally {
            entityManager.close();
        }
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
        selectFormItem.setName( ID );
        selectFormItem.setLabel( ID );
        selectFormItem.setValue( selection );
        selectFormItem.setType( HIDDEN );

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
     * Prepare object type form.
     *
     * @param value
     *         the value
     * @param parserId
     *         the parser id
     *
     * @return the UI form item
     */

    private UIFormItem prepareObjectTypeForm( String value, String parserId ) {

        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( OBJECT_TYPE );
        selectFormItem.setValue( VariableDropDownEnum.getIdByName( value ) );
        selectFormItem.setLabel( OBJECT_TYPE2 );
        selectFormItem.setType( SELECT );

        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();

        rules.put( REQUIRED, true );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        selectFormItem.setRules( rules );
        selectFormItem.setMessages( message );

        List< SelectOptionsUI > options = new ArrayList<>();
        options.add( new SelectOptionsUI( VariableDropDownEnum.INTERNAL.getId(), VariableDropDownEnum.INTERNAL.getName() ) );
        options.add( new SelectOptionsUI( VariableDropDownEnum.CB2.getId(), VariableDropDownEnum.CB2.getName() ) );
        options.add( new SelectOptionsUI( VariableDropDownEnum.SERVER.getId(), VariableDropDownEnum.SERVER.getName() ) );
        options.add( new SelectOptionsUI( VariableDropDownEnum.LOCAL.getId(), VariableDropDownEnum.LOCAL.getName() ) );
        selectFormItem.setMultiple( Boolean.FALSE );
        selectFormItem.setOptions( options );
        selectFormItem.setBindFrom( WORKFLOW_PARSER_OBJECTTYPE_VALUE.replace( PARSER_IDENTIFIER, parserId ) );
        return selectFormItem;
    }

    /**
     * Prepare object parser form.
     *
     * @param value
     *         the value
     *
     * @return the UI form item
     */
    private UIFormItem prepareObjectParserForm( String value ) {

        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( OBJECT_PARSER );
        selectFormItem.setLabel( OBJECT_PARSER2 );
        selectFormItem.setValue( value );
        selectFormItem.setType( SELECT );

        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();

        rules.put( REQUIRED, true );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        selectFormItem.setRules( rules );
        selectFormItem.setMessages( message );

        List< SelectOptionsUI > options = new ArrayList<>();
        Set< String > parserKeySet = PropertiesManager.getParserKeySet();

        for ( String parserKey : parserKeySet ) {
            String extract = PropertiesManager.getParserPathsByKeyAndIdentifierKey( parserKey, "extract" );
            options.add( new SelectOptionsUI( extract, parserKey ) );
        }

        selectFormItem.setMultiple( Boolean.FALSE );
        selectFormItem.setOptions( options );
        return selectFormItem;
    }

    /**
     * Gets the parser available list.
     *
     * @param userId
     *         the user id
     * @param parserId
     *         the parser id
     * @param filter
     *         the filter
     *
     * @return the parser available list
     */
    @Override
    public FilteredResponse< ParserVariableDTO > getParserAvailableList( String userId, String parserId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ParserEntity parserEntity;
        try {
            parserEntity = parserSchemaDAO.getLatestObjectById( entityManager, ParserEntity.class, UUID.fromString( parserId ) );
        } finally {
            entityManager.close();
        }
        if ( parserEntity == null || parserEntity.getJsonSchemaAsString() == null ) {
            long totalRec = 0;
            filter.setTotalRecords( totalRec );
            filter.setFilteredRecords( totalRec );
            return PaginationUtil.constructFilteredResponse( filter, new ArrayList<>() );
        }

        ParserDTO parserAvailableList = JsonUtils.jsonToObject( parserEntity.getJsonSchemaAsString(), ParserDTO.class );
        List< ParserVariableDTO > parserPartList = new ArrayList<>();
        List< ParserVariableDTO > parserParameterList = new ArrayList<>();
        if ( parserAvailableList != null ) {
            parserPartList = convertParserPartDTOListToParserVariableDTO( parserAvailableList.getPart() );
            parserParameterList = convertParserParametersMapToParserVariableDTO( parserAvailableList.getParameter() );

        }
        parserPartList.addAll( parserParameterList );
        List< ParserVariableDTO > filteredList = prepareParserVariableDtoListWithFilter( filter, parserPartList );
        return PaginationUtil.constructFilteredResponse( filter, filteredList );

    }

    /**
     * Union list.
     *
     * @param nameList
     *         the name list
     * @param typeList
     *         the type list
     *
     * @return the list
     */
    public static List< ParserVariableDTO > commonList( List< ParserVariableDTO > nameList, List< ParserVariableDTO > typeList ) {
        return nameList.stream().distinct().filter( typeList::contains ).toList();
    }

    /**
     * Convert parser part DTO list to parser variable DTO.
     *
     * @param allPartsList
     *         the all parts list
     *
     * @return the list
     */
    private List< ParserVariableDTO > convertParserPartDTOListToParserVariableDTO( List< ParserPartDTO > allPartsList ) {
        List< ParserVariableDTO > preparIndexedList;
        preparIndexedList = new ArrayList<>();
        if ( allPartsList != null && !allPartsList.isEmpty() ) {
            for ( int i = 0; i < allPartsList.size(); i++ ) {
                ParserPartDTO listPart = allPartsList.get( i );

                if ( listPart.isPart() ) {
                    String[] varSplitArray = null;
                    if ( listPart.getVariableName() != null && !listPart.getVariableName().isEmpty() ) {
                        varSplitArray = listPart.getVariableName().split( Pattern.quote( "%" ) );
                    }
                    if ( listPart.getProperties() != null && !listPart.getProperties().isEmpty() ) {
                        prepareIndexListFromPropertiesList( preparIndexedList, i, listPart, varSplitArray );
                    }

                } else {
                    ParserVariableDTO indexPart = new ParserVariableDTO();
                    indexPart.setInfo( null );
                    indexPart.setId( listPart.getName() );
                    indexPart.setFullIndex( listPart.getName() );
                    indexPart.setName( listPart.getName() );
                    indexPart.setType( "Parameter" );
                    indexPart.setPart( false );
                    indexPart.setScannedValue( listPart.getParameterValue() );
                    indexPart.setVariableName( listPart.getVariableName() );
                    preparIndexedList.add( indexPart );
                }
            }
        }
        return preparIndexedList;
    }

    /**
     * Prepare index list from properties list.
     *
     * @param preparIndexedList
     *         the prepar indexed list
     * @param i
     *         the i
     * @param listPart
     *         the list part
     * @param varSplitArray
     *         the var split array
     */
    private void prepareIndexListFromPropertiesList( List< ParserVariableDTO > preparIndexedList, int i, ParserPartDTO listPart,
            String[] varSplitArray ) {
        List< ParserPropertiesDTO > propertiesList = listPart.getProperties();
        for ( int k = 0; k < propertiesList.size(); k++ ) {
            ParserPropertiesDTO property = propertiesList.get( k );
            ParserVariableDTO indexPart = new ParserVariableDTO();
            indexPart.setInfo( listPart.getInfo() );
            indexPart.setName( listPart.getName().trim().replace( ConstantsString.SPACE, ConstantsString.UNDERSCORE ) );
            indexPart.setType( property.getName() );
            indexPart.setPart( true );
            indexPart.setScannedValue( property.getValue() );
            try {
                indexPart.setVariableName( varSplitArray[ k ] );
            } catch ( Exception e ) {
                indexPart.setVariableName( "dv-" + i + "-" + k );
            }

            if ( listPart.getFullIndex() != null && !listPart.getFullIndex().isEmpty() ) {
                indexPart.setFullIndex( listPart.getFullIndex() );
            } else {
                indexPart.setFullIndex( i + "." + k );
            }

            indexPart.setId( i + "." + k );
            preparIndexedList.add( indexPart );
        }
    }

    /**
     * Convert parser parameters map to parser variable DTO.
     *
     * @param parameter
     *         the parameter
     *
     * @return the list
     */
    private List< ParserVariableDTO > convertParserParametersMapToParserVariableDTO( Map< String, String > parameter ) {
        List< ParserVariableDTO > preparIndexedList = new ArrayList<>();
        int count = 0;
        if ( parameter != null && !parameter.isEmpty() ) {
            for ( Entry< String, String > param : parameter.entrySet() ) {
                ParserVariableDTO indexedParameter = new ParserVariableDTO();
                indexedParameter.setInfo( "" );
                indexedParameter.setName( param.getKey() );
                indexedParameter.setFullIndex( param.getKey() );
                indexedParameter.setType( "Parameter" );
                indexedParameter.setScannedValue( param.getValue() );
                indexedParameter.setVariableName( "dvp-" + count + "-" + count );
                indexedParameter.setId( param.getKey() );
                indexedParameter.setPart( false );
                preparIndexedList.add( indexedParameter );
                count++;
            }
        }
        return preparIndexedList;
    }

    /**
     * Convert parser variable DTO list to parser part DTO.
     *
     * @param allVariableList
     *         the all variable list
     *
     * @return the list
     */
    @Override
    public List< ParserPartDTO > convertParserVariableDTOListToParserPartDTO( List< ParserVariableDTO > allVariableList ) {
        List< ParserPartDTO > preparIndexedList = new ArrayList<>();

        for ( ParserVariableDTO parserVarDTO : allVariableList ) {
            if ( parserVarDTO.isPart() ) {
                // parser Part list handling
                String[] splitedIndex = parserVarDTO.getId().split( Pattern.quote( "." ) );
                String index = splitedIndex[ 0 ];
                StringBuilder variableName = new StringBuilder();
                List< ParserPropertiesDTO > properties = new ArrayList<>();

                if ( parserVarDTO.getVariableName() != null && !parserVarDTO.getVariableName().isEmpty() ) {
                    variableName.append( parserVarDTO.getVariableName() );
                    variableName.append( "%" );
                }
                properties.add( new ParserPropertiesDTO( parserVarDTO.getType(), parserVarDTO.getScannedValue() ) );
                ParserPartDTO newObject = new ParserPartDTO( parserVarDTO.getName(), properties, String.valueOf( index ),
                        variableName.toString() );
                newObject.setInfo( parserVarDTO.getInfo() );
                newObject.setFullIndex( parserVarDTO.getFullIndex() );
                newObject.setPart( true );
                preparIndexedList.add( newObject );
            } else {
                // parser Parameter list handling
                ParserPartDTO newObject = new ParserPartDTO();
                newObject.setIndex( parserVarDTO.getId() );
                newObject.setName( parserVarDTO.getId() );
                newObject.setParameterValue( parserVarDTO.getScannedValue() );
                newObject.setPart( false );
                newObject.setVariableName( parserVarDTO.getVariableName() );
                preparIndexedList.add( newObject );
            }
        }
        return preparIndexedList;
    }

    /**
     * Prepare part DTO from parser part DTO list.
     *
     * @param parserPartDTOList
     *         the parser part DTO list
     *
     * @return the parser DTO
     */
    @Override
    public ParserDTO preparePartDTOFromParserPartDTOList( List< ParserPartDTO > parserPartDTOList ) {
        ParserDTO selectedFinal = new ParserDTO();

        List< ParserPartDTO > part = new ArrayList<>();

        Map< String, String > parameter = new HashMap<>();

        for ( ParserPartDTO parserPartDTO : parserPartDTOList ) {
            if ( parserPartDTO.isPart() ) {
                part.add( parserPartDTO );
            } else {
                parameter.put( parserPartDTO.getIndex(), parserPartDTO.getParameterValue() );
            }
        }

        selectedFinal.setPart( part );
        selectedFinal.setParameter( parameter );
        return selectedFinal;
    }

    /**
     * Gets the parser selected list.
     *
     * @param userId
     *         the user id
     * @param parserId
     *         the parser id
     * @param filter
     *         the filter
     *
     * @return the parser selected list
     */
    @Override
    public FilteredResponse< ParserVariableDTO > getParserSelectedList( String userId, String parserId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getParserSelectedList( entityManager, userId, parserId, filter );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the parser selected list.
     *
     * @param userId
     *         the user id
     * @param parserId
     *         the parser id
     * @param filter
     *         the filter
     *
     * @return the parser selected list
     */
    private FilteredResponse< ParserVariableDTO > getParserSelectedList( EntityManager entityManager, String userId, String parserId,
            FiltersDTO filter ) {
        ParserEntity parserEntity;
        try {
            parserEntity = parserSchemaDAO.getLatestObjectById( entityManager, ParserEntity.class, UUID.fromString( parserId ) );
        } finally {
            entityManager.close();
        }
        if ( parserEntity == null || parserEntity.getJsonSchemaAsString() == null ) {
            long totalRec = 0;
            filter.setTotalRecords( totalRec );
            filter.setFilteredRecords( totalRec );
            return PaginationUtil.constructFilteredResponse( filter, new ArrayList<>() );
        }

        ParserDTO parserAvailableList = JsonUtils.jsonToObject( parserEntity.getJsonSchemaAsString(), ParserDTO.class );
        List< ParserVariableDTO > parserList = new ArrayList<>();
        if ( parserAvailableList != null ) {
            parserList = convertParserPartDTOListToParserVariableDTO( parserAvailableList.getSelectedEntries() );
        }
        if ( filter.getStart() >= parserList.size() ) {
            filter.setStart( 0 );
        }

        List< ParserVariableDTO > filteredList = prepareParserVariableDtoListWithFilter( filter, parserList );

        return PaginationUtil.constructFilteredResponse( filter, filteredList );
    }

    /**
     * Prepare parser variable dto list with filter.
     *
     * @param filter
     *         the filter
     * @param parserList
     *         the parser list
     *
     * @return the list
     */
    private List< ParserVariableDTO > prepareParserVariableDtoListWithFilter( FiltersDTO filter, List< ParserVariableDTO > parserList ) {
        List< ParserVariableDTO > nameList = new ArrayList<>();
        List< ParserVariableDTO > typeList = new ArrayList<>();
        boolean isFilterExists = false;

        filter.setTotalRecords( ( long ) 0 );
        filter.setFilteredRecords( ( long ) 0 );

        if ( StringUtils.isNotBlank( filter.getSearch() ) ) {
            nameList = parserList.stream().filter( line -> StringUtils.containsIgnoreCase( line.getName(), filter.getSearch() ) ).toList();
            filter.setTotalRecords( ( long ) nameList.size() );
            filter.setFilteredRecords( ( long ) nameList.size() );
            return nameList;
        } else {
            for ( FilterColumn colm : filter.getColumns() ) {
                if ( colm.getName().equalsIgnoreCase( NAME ) && colm.getFilters() != null && !colm.getFilters().isEmpty() ) {

                    boolean isCondition = false;
                    for ( Filter ftl : colm.getFilters() ) {
                        if ( ftl.getOperator().equalsIgnoreCase( CONTAINS ) ) {
                            if ( isCondition ) {
                                nameList = parserList.stream().filter( line -> ( line.getName().contains( ftl.getValue() ) ) )
                                        .collect( Collectors.toList() );
                                isCondition = true;
                            }
                        } else if ( ftl.getOperator().equalsIgnoreCase( DOES_NOT_CONTAIN ) ) {
                            if ( isCondition ) {
                                nameList = parserList.stream().filter( line -> !( line.getName().contains( ftl.getValue() ) ) )
                                        .collect( Collectors.toList() );
                                isCondition = true;
                            } else {
                                if ( ftl.getCondition().equals( "AND" ) ) {
                                    nameList = parserList.stream().filter( line -> !( line.getName().contains( ftl.getValue() ) ) )
                                            .collect( Collectors.toList() );
                                } else if ( ftl.getCondition().equals( "OR" ) ) {
                                    nameList.addAll(
                                            parserList.stream().filter( line -> !( line.getName().contains( ftl.getValue() ) ) ).toList() );
                                }
                            }

                        } else if ( ftl.getOperator().equalsIgnoreCase( EQUALS ) ) {
                            nameList = parserList.stream().filter( line -> ( line.getName().equals( ftl.getValue() ) ) )
                                    .collect( Collectors.toList() );
                        } else if ( ftl.getOperator().equalsIgnoreCase( NOT_EQUALS ) ) {
                            nameList = parserList.stream().filter( line -> !( line.getName().equals( ftl.getValue() ) ) )
                                    .collect( Collectors.toList() );
                        } else if ( ftl.getOperator().equalsIgnoreCase( BEGINS_WITH ) ) {
                            nameList = parserList.stream().filter( line -> ( line.getName().startsWith( ftl.getValue() ) ) )
                                    .collect( Collectors.toList() );
                        } else if ( ftl.getOperator().equalsIgnoreCase( ENDS_WITH ) ) {
                            nameList = parserList.stream().filter( line -> ( line.getName().endsWith( ftl.getValue() ) ) )
                                    .collect( Collectors.toList() );
                        }
                        isFilterExists = true;
                    }
                }

                if ( colm.getName().equalsIgnoreCase( TYPE ) && colm.getFilters() != null && !colm.getFilters().isEmpty() ) {
                    for ( Filter ftl : colm.getFilters() ) {
                        if ( ftl.getOperator().equalsIgnoreCase( CONTAINS ) ) {
                            typeList = parserList.stream().filter( line -> ( line.getType().contains( ftl.getValue() ) ) ).toList();
                        } else if ( ftl.getOperator().equalsIgnoreCase( DOES_NOT_CONTAIN ) ) {
                            typeList = parserList.stream().filter( line -> !( line.getType().contains( ftl.getValue() ) ) ).toList();
                        } else if ( ftl.getOperator().equalsIgnoreCase( EQUALS ) ) {
                            typeList = parserList.stream().filter( line -> ( line.getType().equals( ftl.getValue() ) ) ).toList();
                        } else if ( ftl.getOperator().equalsIgnoreCase( NOT_EQUALS ) ) {
                            typeList = parserList.stream().filter( line -> !( line.getType().equals( ftl.getValue() ) ) ).toList();
                        } else if ( ftl.getOperator().equalsIgnoreCase( BEGINS_WITH ) ) {
                            typeList = parserList.stream().filter( line -> ( line.getType().startsWith( ftl.getValue() ) ) ).toList();
                        } else if ( ftl.getOperator().equalsIgnoreCase( ENDS_WITH ) ) {
                            typeList = parserList.stream().filter( line -> ( line.getType().endsWith( ftl.getValue() ) ) ).toList();
                        }
                        isFilterExists = true;
                    }
                }
            }

            List< ParserVariableDTO > retList;
            List< ParserVariableDTO > filteredList = new ArrayList<>();

            if ( nameList.isEmpty() && typeList.isEmpty() ) {
                if ( !isFilterExists ) {
                    filter.setTotalRecords( ( long ) parserList.size() );
                    filter.setFilteredRecords( ( long ) parserList.size() );
                    // filter list with given FilterDTO
                    filteredList = parserList.subList( filter.getStart(),
                            ( parserList.size() - filter.getStart() ) <= filter.getLength() ? parserList.size()
                                    : ( filter.getStart() + filter.getLength() ) );
                }
            } else {
                if ( nameList.isEmpty() ) {
                    retList = typeList;
                } else if ( typeList.isEmpty() ) {
                    retList = nameList;
                } else {
                    retList = commonList( nameList, typeList );
                }
                filter.setTotalRecords( ( long ) retList.size() );
                filter.setFilteredRecords( ( long ) retList.size() );
                // filter list with given FilterDTO
                filteredList = retList.subList( filter.getStart(),
                        ( retList.size() - filter.getStart() ) <= filter.getLength() ? retList.size()
                                : ( filter.getStart() + filter.getLength() ) );
            }
            for ( FilterColumn colm : filter.getColumns() ) {
                if ( null != colm.getDir() ) {
                    sortList( filteredList, colm );
                }
            }

            return filteredList;
        }
    }

    /**
     * Sort list.
     *
     * @param filteredList
     *         the filtered list
     * @param colm
     *         the colm
     */
    private void sortList( List< ParserVariableDTO > filteredList, FilterColumn colm ) {
        if ( colm.getName().equalsIgnoreCase( NAME ) ) {
            if ( colm.getDir().equalsIgnoreCase( "asc" ) ) {
                filteredList.sort( Comparator.comparing( ParserVariableDTO::getName ) );
            } else {
                filteredList.sort( Comparator.comparing( ParserVariableDTO::getName ).reversed() );
            }
        } else if ( colm.getName().equalsIgnoreCase( TYPE ) ) {
            if ( colm.getDir().equalsIgnoreCase( "asc" ) ) {
                filteredList.sort( Comparator.comparing( ParserVariableDTO::getType ) );
            } else {
                filteredList.sort( Comparator.comparing( ParserVariableDTO::getType ).reversed() );
            }
        }
    }

    /**
     * Gets the parser ui.
     *
     * @param parserId
     *         the parser id
     *
     * @return the parser ui
     */
    @Override
    public List< TableColumn > getParserUi( String parserId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ParserEntity parserEntity = parserSchemaDAO.getLatestObjectById( entityManager, ParserEntity.class,
                    UUID.fromString( parserId ) );
            if ( parserEntity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.SELECTION_NOT_AVAILIABLE.getKey() ) );
            }
            return GUIUtils.listColumns( ParserVariableDTO.class );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the parser list.
     *
     * @param userId
     *         the user id
     * @param parserId
     *         the parser id
     * @param filter
     *         the filter
     *
     * @return the parser list
     */
    @Override
    public FilteredResponse< ParserVariableDTO > getParserList( String userId, String parserId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getParserSelectedList( entityManager, userId, parserId, filter );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the parser object selected UI.
     *
     * @param userId
     *         the user id
     * @param dropDown
     *         the drop down
     * @param parserId
     *         the parser id
     *
     * @return the parser object selected UI
     */
    @Override
    public UIForm getParserObjectSelectedUI( String userId, String dropDown, String parserId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ParserEntity parserEntity;
        try {
            parserEntity = parserSchemaDAO.getLatestObjectById( entityManager, ParserEntity.class, UUID.fromString( parserId ) );
        } finally {
            entityManager.close();
        }

        Object selectedValue = null;
        if ( parserEntity != null ) {
            Object formValue = null;
            if ( parserEntity.getFormJsonAsString() != null ) {
                getFormDataFromJson( parserEntity.getFormJsonAsString() );
                formValue = getFormSubmittedDataByType( parserEntity.getFormJsonAsString() );

            }
            selectedValue = ( VariableDropDownEnum.getNameById( dropDown ).equalsIgnoreCase( parserEntity.getName() ) ? formValue : null );
        }

        List< UIFormItem > parserForm = new ArrayList<>();

        if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.INTERNAL.getId() ) ) {
            parserForm.add( prepareObjectSelectorForm( VariableDropDownEnum.getNameById( dropDown ), selectedValue ) );
        } else if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.CB2.getId() ) ) {
            if ( StringUtils.equals( userId, ConstantsID.SUPER_USER_ID ) || licenseManager.isFeatureAllowedToUser(
                    SimuspaceFeaturesEnum.CB2_CONNECTOR.getKey(), userId ) ) {
                ValidationUtils.validateCB2AccessWithWENLogin();
                parserForm.add( prepareCB2TypeList( parserId ) );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.FEATURE_NOT_ALLOWED_TO_USER.getKey(),
                        SimuspaceFeaturesEnum.CB2_CONNECTOR.getKey() ) );
            }
        } else if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.SERVER.getId() ) ) {
            parserForm.add( prepareFileExplorerForm( VariableDropDownEnum.getNameById( dropDown ), selectedValue ) );
        } else if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.LOCAL.getId() ) ) {
            parserForm.add( prepareLocalFileSelectForm( VariableDropDownEnum.getNameById( dropDown ), selectedValue ) );
        }

        return GUIUtils.createFormFromItems( parserForm );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getParserObjectCB2SelectorUI( String userId, String typeSelected, String parserId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > parserForm = new ArrayList<>();
            ParserEntity parserEntity = parserSchemaDAO.getLatestObjectById( entityManager, ParserEntity.class,
                    UUID.fromString( parserId ) );
            Object formValue = null;
            if ( parserEntity != null ) {
                var formJson = parserEntity.getFormJsonAsString();
                if ( formJson != null ) {
                    var map = getFormDataFromJson( formJson );
                    if ( typeSelected.equals( map.get( EXTERNAL ) ) ) {
                        formValue = getFormSubmittedDataByType( formJson );
                    }
                }
            }
            parserForm.add( prepareCB2Selector( typeSelected, formValue ) );
            return GUIUtils.createFormFromItems( parserForm );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare cb 2 type list ui form item.
     *
     * @param parserId
     *         the parser id
     *
     * @return the ui form item
     */
    private UIFormItem prepareCB2TypeList( String parserId ) {
        SelectFormItem cb2TypeSelect = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        cb2TypeSelect.setType( FieldTypes.SELECTION.getType() );
        cb2TypeSelect.setName( EXTERNAL );
        cb2TypeSelect.setLabel( OBJECT_TYPE2 );
        cb2TypeSelect.setValue( BmwCaeBenchEnums.CB2_SUBMODEL.getKey() );
        List< SelectOptionsUI > options = new ArrayList<>();
        options.add( new SelectOptionsUI( BmwCaeBenchEnums.CB2_SUBMODEL.getKey(), "Submodel" ) );
        options.add( new SelectOptionsUI( BmwCaeBenchEnums.CB2_VARIANT.getKey(), "Variant" ) );
        options.add( new SelectOptionsUI( BmwCaeBenchEnums.CB2_INPUT_DECK.getKey(), "InputDeck" ) );
        options.add( new SelectOptionsUI( BmwCaeBenchEnums.CB2_RESULT.getKey(), "Result" ) );
        options.add( new SelectOptionsUI( BmwCaeBenchEnums.CB2_KEYRESULTS.getKey(), "KeyResults" ) );
        options.add( new SelectOptionsUI( BmwCaeBenchEnums.CB2_STORY_BOARD.getKey(), "StoryBoard" ) );
        options.add( new SelectOptionsUI( BmwCaeBenchEnums.CB2_REPORT.getKey(), "Report" ) );
        cb2TypeSelect.setOptions( options );
        cb2TypeSelect.setBindFrom( "workflow/parser/" + parserId + "/cb2type/{__value__}" );
        return cb2TypeSelect;

    }

    @Override
    public Object getParserObjectSelectedUIForCB2Element( String userId, String dropDown ) {

        List< UIFormItem > parserForm = new ArrayList<>();

        if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.INTERNAL.getId() ) ) {
            parserForm.add( prepareObjectSelectorForm( VariableDropDownEnum.getNameById( dropDown ), null ) );
        } else if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.CB2.getId() ) ) {
            parserForm.add( prepareCB2Selector() );
        } else if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.SERVER.getId() ) ) {
            parserForm.add( prepareFileExplorerForm( VariableDropDownEnum.getNameById( dropDown ), null ) );
        } else if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.LOCAL.getId() ) ) {
            parserForm.add( prepareLocalFileSelectForm( VariableDropDownEnum.getNameById( dropDown ), null ) );
        } else if ( dropDown.equalsIgnoreCase( VariableDropDownEnum.VARIABLE.getId() ) ) {

            SelectFormItem typeItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            typeItem.setCanBeEmpty( false );
            typeItem.setDuplicate( false );
            typeItem.setLabel( "Value" );
            typeItem.setName( "value" );
            typeItem.setType( "text" );
            Map< String, Object > rules = new HashMap<>();
            Map< String, Object > message = new HashMap<>();
            rules.put( REQUIRED, true );
            message.put( REQUIRED, MUST_CHOSE_OPTION );
            typeItem.setRules( rules );
            typeItem.setMessages( message );

            parserForm.add( typeItem );

        }

        return parserForm;
    }

    /**
     * Prepare CB 2 selector.
     *
     * @return the select UI table
     */
    private TableFormItem prepareCB2Selector() {
        return prepareCB2Selector( "bmw-cb2-project-tree", null );
    }

    /**
     * Prepare CB 2 selector.
     *
     * @return the select UI table
     */
    private TableFormItem prepareCB2Selector( String external, Object selectedValue ) {
        TableFormItem cb2Form = ( TableFormItem ) GUIUtils.createFormItem( FormItemType.TABLE );
        cb2Form.setName( OBJECT_DOT + VariableDropDownEnum.CB2.getName() );
        cb2Form.setLabel( VariableDropDownEnum.CB2.getName() );
        cb2Form.setMultiple( false );
        cb2Form.setSelectable( null );
        cb2Form.setType( "externalObject" );

        cb2Form.setExternal( external );

        Map< String, String > optional = new HashMap<>();
        Map< String, Object > rules = new HashMap<>();

        rules.put( REQUIRED, true );
        cb2Form.setRules( rules );
        cb2Form.setExtendTree( optional );
        Map< String, Object > message = new HashMap<>();
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        cb2Form.setMessages( message );
        cb2Form.setValue( selectedValue );
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
        selectFormItem.setName( OBJECT_DOT + type );
        selectFormItem.setLabel( type + " " + SELECTION_TITAL );
        selectFormItem.setValue( formValue );
        selectFormItem.setType( OBJECT );

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
        selectFormItem.setName( OBJECT_DOT + type );
        selectFormItem.setLabel( type + " " + SELECTION_TITAL );
        selectFormItem.setValue( formValue );
        selectFormItem.setType( FILE_EXPLORER_FORM );
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
        selectFormItem.setName( OBJECT_DOT + type );
        selectFormItem.setLabel( type + " " + SELECTION_TITAL );
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
     * Gets the parser schema DAO.
     *
     * @return the parser schema DAO
     */
    @Override
    public ParserSchemaDAO getParserSchemaDAO() {
        return parserSchemaDAO;
    }

    /**
     * Sets the parser schema DAO.
     *
     * @param parserSchemaDAO
     *         the new parser schema DAO
     */
    public void setParserSchemaDAO( ParserSchemaDAO parserSchemaDAO ) {
        this.parserSchemaDAO = parserSchemaDAO;
    }

    /**
     * Gets the parser object available context.
     *
     * @param userId
     *         the user id
     * @param parserId
     *         the parser id
     * @param filter
     *         the filter
     *
     * @return the parser object available context
     */
    @Override
    public Object getParserObjectAvailableContext( String userId, String parserId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // case of select all from data table
        try {
            if ( filter.getItems().isEmpty() && filter.getLength().toString().equalsIgnoreCase( "-1" ) ) {

                ParserEntity parserEntity = parserSchemaDAO.getLatestObjectById( entityManager, ParserEntity.class,
                        UUID.fromString( parserId ) );

                ParserDTO parserAvailableList = JsonUtils.jsonToObject( parserEntity.getJsonSchemaAsString(), ParserDTO.class );
                List< ParserVariableDTO > parserList = new ArrayList<>();
                if ( parserAvailableList != null ) {
                    parserList = convertParserPartDTOListToParserVariableDTO( parserAvailableList.getPart() );
                }

                List< Object > itemsList = new ArrayList<>();
                parserList.stream().forEach( parserVariableDTO -> itemsList.add( parserVariableDTO.getId() ) );
                filter.setItems( itemsList );
            }

            final SelectionResponseUI selectionResponseUI = selectionManager.createSelection( entityManager, userId,
                    SelectionOrigins.CONTEXT, filter );
            List< Object > items = new ArrayList<>();
            items.add( selectionResponseUI.getId() );
            filter.setItems( items );
            // show add context only
            List< ContextMenuItem > addContext = new ArrayList<>();
            for ( ContextMenuItem contextMenuItem : contextMenuManager.getParserTableContext( parserId, filter ) ) {
                if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( ADD ) ) ) {
                    addContext.add( contextMenuItem );
                }
            }
            return addContext;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the parser object selected context.
     *
     * @param userId
     *         the user id
     * @param parserId
     *         the parser id
     * @param filter
     *         the filter
     *
     * @return the parser object selected context
     */
    @Override
    public Object getParserObjectSelectedContext( String userId, String parserId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // case of select all from data table
            if ( filter.getItems().isEmpty() && filter.getLength().toString().equalsIgnoreCase( "-1" ) ) {

                ParserEntity parserEntity = parserSchemaDAO.getLatestObjectById( entityManager, ParserEntity.class,
                        UUID.fromString( parserId ) );

                ParserDTO parserAvailableList = JsonUtils.jsonToObject( parserEntity.getJsonSchemaAsString(), ParserDTO.class );
                List< ParserVariableDTO > parserList = new ArrayList<>();
                if ( parserAvailableList != null ) {
                    parserList = convertParserPartDTOListToParserVariableDTO( parserAvailableList.getSelectedEntries() );
                }

                List< Object > itemsList = new ArrayList<>();
                parserList.stream().forEach( parserVariableDTO -> itemsList.add( parserVariableDTO.getId() ) );
                filter.setItems( itemsList );
            }

            int itemSize = filter.getItems().size();
            final SelectionResponseUI selectionResponseUI = selectionManager.createSelection( entityManager, userId,
                    SelectionOrigins.CONTEXT, filter );
            List< Object > items = new ArrayList<>();
            items.add( selectionResponseUI.getId() );
            filter.setItems( items );
            // show remove and edit context only
            List< ContextMenuItem > addContext = new ArrayList<>();
            for ( ContextMenuItem contextMenuItem : contextMenuManager.getParserTableContext( parserId, filter ) ) {
                if ( !contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( ADD ) ) && ( itemSize <= 1
                        || !contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( EDIT ) ) ) ) {
                    addContext.add( contextMenuItem );
                }
            }
            return addContext;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Adds the entry to parser selected list.
     *
     * @param string
     *         the string
     * @param parserId
     *         the parser id
     * @param selectionId
     *         the selection id
     *
     * @return the object
     */
    @Override
    public Object addEntryToParserSelectedList( String string, String parserId, String selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< String > itemsSelected = selectionManager.getSelectedIdsStringListBySelectionId( entityManager, selectionId );

            ParserEntity parserEntity = parserSchemaDAO.getLatestObjectById( entityManager, ParserEntity.class,
                    UUID.fromString( parserId ) );

            ParserDTO parserAvailableList = JsonUtils.jsonToObject( parserEntity.getJsonSchemaAsString(), ParserDTO.class );
            List< ParserPartDTO > partsList = parserAvailableList.getPart();
            Map< String, String > parameterMap = parserAvailableList.getParameter();

            List< ParserPartDTO > selected = new ArrayList<>();
            if ( parserAvailableList.getSelectedEntries() != null ) {
                selected = parserAvailableList.getSelectedEntries();
            }
            List< ParserVariableDTO > parserList;

            List< ParserVariableDTO > parserSelectedList = convertParserPartDTOListToParserVariableDTO( selected );
            parserList = convertParserPartDTOListToParserVariableDTO( partsList );
            parserList.addAll( convertParserParametersMapToParserVariableDTO( parameterMap ) );

            if ( !itemsSelected.isEmpty() ) {
                for ( String index : itemsSelected ) {
                    if ( !isIndexExistInParserVariableDTOList( parserSelectedList, index ) ) {
                        for ( ParserVariableDTO parserVariableDTO : parserList ) {
                            if ( parserVariableDTO.getId().equalsIgnoreCase( index ) || parserVariableDTO.getFullIndex()
                                    .equalsIgnoreCase( index ) ) {
                                parserSelectedList.add( parserVariableDTO );
                                break;
                            }
                        }
                    }
                }

                selected.clear();
                selected = convertParserVariableDTOListToParserPartDTO( parserSelectedList );
            } else {
                // itemsSelected is empty then select all
                selected.clear();
                selected = convertParserVariableDTOListToParserPartDTO( parserList );
            }

            parserAvailableList.setSelectedEntries( selected );
            parserEntity.setJsonSchema( JsonUtils.toJson( parserAvailableList ) );
            return parserSchemaDAO.saveOrUpdate( entityManager, parserEntity );
        } finally {
            entityManager.close();
        }

    }

    /**
     * Checks if is index exist in parser variable DTO list.
     *
     * @param parserList
     *         the parser list
     * @param index
     *         the index
     *
     * @return true, if is index exist in parser variable DTO list
     */
    private boolean isIndexExistInParserVariableDTOList( List< ParserVariableDTO > parserList, String index ) {
        if ( !parserList.isEmpty() ) {
            for ( ParserVariableDTO parserVariableDTO : parserList ) {
                if ( index.equalsIgnoreCase( parserVariableDTO.getFullIndex() ) ) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Save entry to parser available and selected list.
     *
     * @param userId
     *         the user id
     * @param parserId
     *         the parser id
     * @param parserVariableDTO
     *         the parser variable DTO
     *
     * @return the object
     */
    @Override
    public Object saveEntryToParserAvailableAndSelectedList( String userId, String parserId, ParserVariableDTO parserVariableDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ParserEntity parserEntity = parserSchemaDAO.getLatestObjectById( entityManager, ParserEntity.class,
                    UUID.fromString( parserId ) );

            ParserDTO parserAvailableList = JsonUtils.jsonToObject( parserEntity.getJsonSchemaAsString(), ParserDTO.class );

            List< ParserPartDTO > selected = new ArrayList<>();
            if ( parserAvailableList.getSelectedEntries() != null ) {
                selected = parserAvailableList.getSelectedEntries();
            }

            List< ParserVariableDTO > parserSelectedList = convertParserPartDTOListToParserVariableDTO( selected );

            for ( ParserVariableDTO parserVariableDTO2 : parserSelectedList ) {
                if ( parserVariableDTO2.getVariableName().equalsIgnoreCase( parserVariableDTO.getVariableName() )
                        && !parserVariableDTO.getId().equalsIgnoreCase( parserVariableDTO2.getId() ) ) {
                    throw new SusException( parserVariableDTO.getVariableName() + " Variable Name Already exsits." );
                }
            }

            for ( ParserVariableDTO parserVariableDTO2 : parserSelectedList ) {
                if ( parserVariableDTO2.getId().equalsIgnoreCase( parserVariableDTO.getId() ) ) {
                    parserVariableDTO2.setVariableName( parserVariableDTO.getVariableName() );
                    parserVariableDTO2.setScannedValue( parserVariableDTO.getScannedValue() );
                    break;
                }
            }

            selected.clear();
            selected = convertParserVariableDTOListToParserPartDTO( parserSelectedList );
            parserAvailableList.setSelectedEntries( selected );
            parserEntity.setJsonSchema( JsonUtils.toJson( parserAvailableList ) );
            return parserSchemaDAO.saveOrUpdate( entityManager, parserEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the edits the form for selected list.
     *
     * @param string
     *         the string
     * @param parserId
     *         the parser id
     * @param selectionId
     *         the selection id
     *
     * @return the edits the form for selected list
     */
    @Override
    public Object getEditFormForSelectedList( String string, String parserId, String selectionId ) {
        List< String > itemsSelected;
        ParserEntity parserEntity;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            itemsSelected = selectionManager.getSelectedIdsStringListBySelectionId( entityManager, selectionId );

            parserEntity = parserSchemaDAO.getLatestObjectById( entityManager, ParserEntity.class, UUID.fromString( parserId ) );
        } finally {
            entityManager.close();
        }

        ParserDTO parserAvailableList = JsonUtils.jsonToObject( parserEntity.getJsonSchemaAsString(), ParserDTO.class );
        List< ParserVariableDTO > parserSelectedList = convertParserPartDTOListToParserVariableDTO(
                parserAvailableList.getSelectedEntries() );

        ParserVariableDTO parserPartDTOSelected = new ParserVariableDTO();
        for ( ParserVariableDTO parserVariableDTO : parserSelectedList ) {
            for ( String item : itemsSelected ) {
                if ( parserVariableDTO.getId().equalsIgnoreCase( item ) ) {
                    parserPartDTOSelected = parserVariableDTO;
                    break;
                }
            }
        }

        List< UIFormItem > formUI = GUIUtils.prepareForm( true, new ParserVariableDTO() );
        for ( UIFormItem uIForm : formUI ) {

            if ( uIForm.getName().equalsIgnoreCase( NAME ) ) {
                uIForm.setType( STATIC );
                uIForm.setValue( parserPartDTOSelected.getName() );
            } else if ( uIForm.getName().equalsIgnoreCase( TYPE ) ) {
                uIForm.setType( STATIC );
                uIForm.setValue( parserPartDTOSelected.getType() );
            } else if ( uIForm.getName().equalsIgnoreCase( VARIABLE_NAME ) ) {
                uIForm.setValue( parserPartDTOSelected.getVariableName() );

            } else if ( uIForm.getName().equalsIgnoreCase( SCANNED_VALUE ) ) {
                uIForm.setValue( parserPartDTOSelected.getScannedValue() );
            } else if ( uIForm.getName().equalsIgnoreCase( ID ) ) {
                uIForm.setValue( parserPartDTOSelected.getId() );
            }
        }
        return formUI;
    }

    /**
     * Removes the entry from parser selected list.
     *
     * @param string
     *         the string
     * @param parserId
     *         the parser id
     * @param selectionId
     *         the selection id
     *
     * @return the object
     */
    @Override
    public Object removeEntryFromParserSelectedList( String string, String parserId, String selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< String > itemsSelected = selectionManager.getSelectedIdsStringListBySelectionId( entityManager, selectionId );

            ParserEntity parserEntity = parserSchemaDAO.getLatestObjectById( entityManager, ParserEntity.class,
                    UUID.fromString( parserId ) );

            ParserDTO parserAvailableList = JsonUtils.jsonToObject( parserEntity.getJsonSchemaAsString(), ParserDTO.class );

            List< ParserPartDTO > selected = new ArrayList<>();
            if ( parserAvailableList.getSelectedEntries() != null ) {
                selected = parserAvailableList.getSelectedEntries();
            }

            List< ParserVariableDTO > parserSelectedList;
            parserSelectedList = convertParserPartDTOListToParserVariableDTO( selected );

            for ( String index : itemsSelected ) {
                parserSelectedList.removeIf( parserVariable -> parserVariable.getId().equalsIgnoreCase( index ) );
            }
            selected.clear();
            selected = convertParserVariableDTOListToParserPartDTO( parserSelectedList );

            parserAvailableList.setSelectedEntries( selected );
            parserEntity.setJsonSchema( JsonUtils.toJson( parserAvailableList ) );
            return parserSchemaDAO.saveOrUpdate( entityManager, parserEntity );
        } finally {
            entityManager.close();
        }
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
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
    public List< ParserVariableDTO > getSelectedParserEntriesListByParserId( EntityManager entityManager, String parserId ) {
        if ( null == parserId || parserId.isEmpty() ) {
            return new ArrayList<>();
        }
        ParserEntity parserEntity = parserSchemaDAO.getLatestObjectById( entityManager, ParserEntity.class, UUID.fromString( parserId ) );

        ParserDTO parserAvailableList = JsonUtils.jsonToObject( parserEntity.getJsonSchemaAsString(), ParserDTO.class );
        List< ParserPartDTO > selected = new ArrayList<>();
        if ( parserAvailableList.getSelectedEntries() != null ) {
            selected = parserAvailableList.getSelectedEntries();
        }
        return convertParserPartDTOListToParserVariableDTO( selected );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ParserVariableDTO > getSelectedParserEntriesList( List< ParserEntity > parserEntities ) {
        List< ParserPartDTO > selected = new ArrayList<>();
        for ( ParserEntity parserEntity : parserEntities ) {
            ParserDTO parserAvailableList = JsonUtils.jsonToObject( parserEntity.getJsonSchemaAsString(), ParserDTO.class );

            if ( parserAvailableList.getSelectedEntries() != null ) {
                selected.addAll( parserAvailableList.getSelectedEntries() );
            }
        }
        return convertParserPartDTOListToParserVariableDTO( selected );
    }

    /**
     * Gets the selected parser entries list custom variables.
     *
     * @param parserEntity
     *         the parser entitiy
     *
     * @return the selected parser entries list custom variables
     */
    @Override
    public List< CustomVariableDTO > getSelectedParserEntriesListCustomVariables( ParserEntity parserEntity ) {
        List< ParserPartDTO > selected = new ArrayList<>();
        ParserDTO parserAvailableList = JsonUtils.jsonToObject( parserEntity.getJsonSchemaAsString(), ParserDTO.class );

        if ( parserAvailableList.getSelectedEntries() != null ) {
            selected.addAll( parserAvailableList.getSelectedEntries() );
        }
        return convertParserPartDTOListToCustomVariableDTO( selected );
    }

    /**
     * Convert parser part DTO list to custom variable DTO.
     *
     * @param allPartsList
     *         the all parts list
     *
     * @return the list
     */
    private List< CustomVariableDTO > convertParserPartDTOListToCustomVariableDTO( List< ParserPartDTO > allPartsList ) {
        List< CustomVariableDTO > preparIndexedList = new ArrayList<>();
        if ( allPartsList != null && !allPartsList.isEmpty() ) {
            for ( int i = 0; i < allPartsList.size(); i++ ) {
                ParserPartDTO listPart = allPartsList.get( i );

                if ( listPart.isPart() ) {
                    String[] varSplitArray = null;
                    if ( listPart.getVariableName() != null && !listPart.getVariableName().isEmpty() ) {
                        varSplitArray = listPart.getVariableName().split( Pattern.quote( "%" ) );
                    }
                    if ( listPart.getProperties() != null && !listPart.getProperties().isEmpty() ) {
                        prepareIndexListFromPropertiesListCustom( preparIndexedList, i, listPart, varSplitArray );
                    }

                } else {
                    CustomVariableDTO indexPart = new CustomVariableDTO();
                    indexPart.setName( listPart.getName() );
                    indexPart.setType( "Parameter" );
                    indexPart.setValue( listPart.getParameterValue() );
                    indexPart.setLabel( listPart.getVariableName() );
                    preparIndexedList.add( indexPart );
                }
            }
        }
        return preparIndexedList;
    }

    /**
     * Prepare index list from properties list custom.
     *
     * @param preparIndexedList
     *         the prepar indexed list
     * @param i
     *         the i
     * @param listPart
     *         the list part
     * @param varSplitArray
     *         the var split array
     */
    private void prepareIndexListFromPropertiesListCustom( List< CustomVariableDTO > preparIndexedList, int i, ParserPartDTO listPart,
            String[] varSplitArray ) {
        List< ParserPropertiesDTO > propertiesList = listPart.getProperties();
        for ( int k = 0; k < propertiesList.size(); k++ ) {
            ParserPropertiesDTO property = propertiesList.get( k );
            CustomVariableDTO indexPart = new CustomVariableDTO();
            indexPart.setName( listPart.getName().trim().replace( ConstantsString.SPACE, ConstantsString.UNDERSCORE ) );
            indexPart.setType( property.getName() );
            indexPart.setValue( property.getValue() );
            try {
                indexPart.setLabel( varSplitArray[ k ] );
            } catch ( Exception e ) {
                indexPart.setLabel( "dv-" + i + "-" + k );
            }
            preparIndexedList.add( indexPart );
        }
    }

    /**
     * Prepare objectivevariable for engine.
     *
     * @param userId
     *         the user id
     * @param userUID
     *         the user UID
     * @param parserId
     *         the parser id
     * @param filepath
     *         the filepath
     *
     * @return the list
     */
    @Override
    public List< ParserVariableDTO > prepareObjectivevariableForEngine( String userId, String userUID, String parserId, String filepath ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ParserEntity parserEntity;
        try {
            parserEntity = parserSchemaDAO.getLatestObjectById( entityManager, ParserEntity.class, UUID.fromString( parserId ) );
        } finally {
            entityManager.close();
        }

        filepath = StringUtils.strip( filepath, "\"" );
        Map< String, String > formMap = getFormDataFromJson( parserEntity.getFormJsonAsString() );

        String parserType = formMap.get( TYPE );
        String parserParsser = formMap.get( PARSER2 );
        String selectionObject = formMap.get( SELECTION );

        String readFileOutput = filepath + File.separator + "ParserOutputFile.json";
        String readFileInput = null;

        if ( VariableDropDownEnum.INTERNAL.getId().equalsIgnoreCase( parserType ) || VariableDropDownEnum.LOCAL.getId()
                .equalsIgnoreCase( parserType ) ) {
            DocumentEntity selectedDocumet = getSelectedInternalObjectFilePath( entityManager, parserType, selectionObject );
            readFileInput = filepath + File.separator + selectedDocumet.getFileName();
        } else if ( VariableDropDownEnum.SERVER.getId().equalsIgnoreCase( parserType ) ) {
            Map< String, String > severFileSelectedMap = getServerFileSelectionObjectByJson( parserEntity.getFormJsonAsString() );
            String selectedFileName = severFileSelectedMap.get( "title" );
            readFileInput = filepath + File.separator + selectedFileName;
        }

        PythonUtils.callParserExtractionPythonFile( userUID, readFileInput, readFileOutput, parserParsser );
        ParserDTO parserAvailableListNew = new ParserDTO();
        File myObj = new File( readFileOutput );
        try ( InputStream targetConfigStream = new FileInputStream( myObj ) ) {
            parserAvailableListNew = JsonUtils.jsonToObject( targetConfigStream, ParserDTO.class );
        } catch ( IOException e ) {
            log.error( "Abaqus File reading ERROR", e );
        }

        final FiltersDTO filter = JsonUtils.jsonToObject( FILTER_ALL, FiltersDTO.class );

        ParserDTO parserAvailableList = JsonUtils.jsonToObject( parserEntity.getJsonSchemaAsString(), ParserDTO.class );
        List< ParserVariableDTO > parserSelectedList = new ArrayList<>();
        if ( parserAvailableList != null ) {
            parserSelectedList = convertParserPartDTOListToParserVariableDTO( parserAvailableList.getSelectedEntries() );
        }

        // previous selected row
        List< ParserVariableDTO > filteredSelectedList = prepareParserVariableDtoListWithFilter( filter, parserSelectedList );

        // prepare new Parser File available list
        List< ParserVariableDTO > parserPartList = new ArrayList<>();
        List< ParserVariableDTO > parserParameterList = new ArrayList<>();
        if ( parserAvailableListNew != null ) {
            parserPartList = convertParserPartDTOListToParserVariableDTO( parserAvailableListNew.getPart() );
            parserParameterList = convertParserParametersMapToParserVariableDTO( parserAvailableListNew.getParameter() );
        }
        parserPartList.addAll( parserParameterList );
        List< ParserVariableDTO > filteredAvailableList = prepareParserVariableDtoListWithFilter( filter, parserPartList );

        List< ParserVariableDTO > finalSelectedList = new ArrayList<>();

        for ( ParserVariableDTO selected : filteredSelectedList ) {
            for ( ParserVariableDTO available : filteredAvailableList ) {
                if ( ( selected.isPart() && selected.getFullIndex().equalsIgnoreCase( available.getFullIndex() ) ) || ( !selected.isPart()
                        && selected.getId().equalsIgnoreCase( available.getId() ) ) ) {
                    available.setVariableName( selected.getVariableName() );
                    finalSelectedList.add( available );
                }
            }
        }

        return finalSelectedList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParserSelectedFilePathByParserId( String userUID, String parserId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ParserEntity parserEntity = parserSchemaDAO.getLatestObjectById( entityManager, ParserEntity.class,
                    UUID.fromString( parserId ) );
            Map< String, String > formMap = getFormDataFromJson( parserEntity.getFormJsonAsString() );
            String parserType = formMap.get( TYPE );
            String selectionObject = formMap.get( SELECTION );
            String selectedFilePath = null;
            if ( VariableDropDownEnum.INTERNAL.getId().equalsIgnoreCase( parserType ) || VariableDropDownEnum.LOCAL.getId()
                    .equalsIgnoreCase( parserType ) ) {
                DocumentEntity selectedFileDoc = getSelectedInternalObjectFilePath( entityManager, parserType, selectionObject );
                selectedFilePath =
                        PropertiesManager.getVaultPath() + selectedFileDoc.getFilePath() + File.separator + selectedFileDoc.getFileName();
            } else if ( VariableDropDownEnum.SERVER.getId().equalsIgnoreCase( parserType ) ) {
                File file = new File( selectionObject );
                selectedFilePath = file.getPath();
            } else if ( VariableDropDownEnum.CB2.getId().equalsIgnoreCase( parserType ) ) {

                List< String > cb2OidList = selectionManager.getSelectedIdsStringListBySelectionId( entityManager, selectionObject );
                if ( cb2OidList != null && !cb2OidList.isEmpty() ) {

                    String cb2OID = cb2OidList.get( 0 );
                    BmwCaeBenchEntity cb2ObjectEntity = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager,
                            UUID.fromString( cb2OID ) );
                    selectedFilePath = downloadCB2FileAndReturnPath( userUID, cb2ObjectEntity );
                }
            }
            return selectedFilePath;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParserEntity duplicateParserEntity( EntityManager entityManager, String parserId ) {
        ParserEntity sourceEntity = parserSchemaDAO.getLatestObjectById( entityManager, ParserEntity.class, UUID.fromString( parserId ) );
        if ( sourceEntity == null ) {
            return null;
        }
        ParserEntity duplicate = sourceEntity.copy();
        return parserSchemaDAO.save( entityManager, duplicate );
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
     * Sets the entity manager factory.
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
