package de.soco.software.simuspace.server.manager;

import javax.persistence.EntityManager;
import javax.ws.rs.core.UriInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsCustomFlagFields;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.EncryptionDecryptionDTO;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.PythonUtils;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchEntity;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.EncryptionDecryptionEntity;
import de.soco.software.simuspace.suscore.data.entity.LoadCaseEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.document.dao.DocumentDAO;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;

/**
 * This class provides methods to check role permission of a user also checks license validity.
 */
@Setter
@Log4j2
public class BaseManager extends SuSBaseService {

    /**
     * The Constant FULL_PATH.
     */
    protected static final String FULL_PATH = "fullPath";

    /**
     * The Constant FILE_SELECTOR.
     */
    protected static final Object FILE_SELECTOR = "fileSelector";

    /**
     * The Constant REGEX_OBJECT.
     */
    protected static final String REGEX_OBJECT = "regexobject";

    /**
     * The Constant TEMPLATE_OBJECT.
     */
    protected static final String TEMPLATE_OBJECT = "templateobject";

    /**
     * The Constant VALUE.
     */
    protected static final String VALUE = "value";

    /**
     * The Constant OBJECT.
     */
    protected static final String OBJECT = "object";

    /**
     * The Constant TYPE.
     */
    protected static final String TYPE = "type";

    /**
     * The Constant ID.
     */
    protected static final String ID = "id";

    /**
     * The Constant REQUIRED_CHARACTER.
     */
    protected static final int REQUIRED_CHARACTER = 2048;

    /**
     * The license manager.
     */
    protected LicenseManager licenseManager;

    /**
     * The selection manager.
     */
    protected SelectionManager selectionManager;

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    protected SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The Constant LABEL.
     */
    protected static final String LABEL = "label";

    /**
     * The Constant TITLE.
     */
    protected static final String TITLE = "title";

    /**
     * The Constant SUBTITLE.
     */
    protected static final String SUBTITLE = "subtitle";

    /**
     * The Constant NAME_FIELD.
     */
    protected static final String NAME_FIELD = "name";

    /**
     * The Constant TOOLTIP.
     */
    protected static final String TOOLTIP = "tooltip";

    /**
     * The Constant EXTERNAL.
     */
    protected static final String EXTERNAL = "external";

    /**
     * The Constant LIVESEARCH.
     */
    protected static final String LIVESEARCH = "livesearch";

    /**
     * The Constant RULES.
     */
    protected static final String RULES = "rules";

    /**
     * The Constant REQUIRED.
     */
    protected static final String REQUIRED = "required";

    /**
     * The Constant MUST_CHOSE_OPTION.
     */
    protected static final String MUST_CHOSE_OPTION = "Must Chose Option";

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    protected DocumentDAO documentDAO;

    /**
     * Instantiates a new base manager.
     */
    public BaseManager() {
        super();
    }

    /**
     * Checks if is workflow manager.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return true, if is workflow manager
     */
    public boolean isWorkflowManager( EntityManager entityManager, UUID userId ) {
        return licenseManager.isLicenseAddedAgainstUserForModule( entityManager, SimuspaceFeaturesEnum.WORKFLOW_MANAGER.getKey(),
                userId.toString() );
    }

    /**
     * Checks if is workflow user.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return true, if is workflow user
     */
    public boolean isWorkflowUser( EntityManager entityManager, UUID userId ) {
        return licenseManager.isLicenseAddedAgainstUserForModule( entityManager, SimuspaceFeaturesEnum.WORKFLOW_USER.getKey(),
                userId.toString() );
    }

    /**
     * Check manager permission.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return true, if successful
     */
    protected boolean checkManagerPermission( EntityManager entityManager, UUID userId ) {

        boolean manager;
        try {
            manager = isWorkflowManager( entityManager, userId );
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            manager = false;
        }

        return manager;
    }

    /**
     * Gets the json.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the json
     */
    protected JsonNode getJson( EntityManager entityManager, UUID id ) {
        JsonNode jsonNode = null;
        SelectionEntity selectionEntity = selectionManager.getSelectionEntityById( entityManager, id.toString() );
        if ( selectionEntity != null && null != selectionEntity.getJson() ) {
            jsonNode = JsonUtils.toJsonNode( ByteUtil.convertByteToString( selectionEntity.getJson() ) );
        }
        return jsonNode;
    }

    /**
     * Prepare encryption decryption DTO.
     *
     * @param encryptionDecryption
     *         the encryption decryption
     *
     * @return the encryption decryption DTO
     */
    protected EncryptionDecryptionDTO prepareEncryptionDecryptionDTO( EncryptionDecryptionEntity encryptionDecryption ) {

        if ( encryptionDecryption == null ) {
            return null;
        }
        return new EncryptionDecryptionDTO( encryptionDecryption.getId().toString(), encryptionDecryption.getMethod(),
                encryptionDecryption.isActive(), encryptionDecryption.getSalt() );
    }

    /**
     * Gets the document object by object id.
     *
     * @param entityManager
     *         the entity manager
     * @param objId
     *         the obj id
     *
     * @return the document object by object id
     */
    protected DocumentEntity getDocumentObjectByObjectId( EntityManager entityManager, UUID objId ) {
        DocumentEntity document = documentDAO.findById( entityManager, objId );
        if ( document == null ) {
            SuSEntity suSEntity = susDAO.getLatestNonDeletedObjectById( entityManager, objId );
            if ( suSEntity instanceof ContainerEntity ) {
                throw new SusException( "Selection object can not be a container type" );
            }
            if ( suSEntity instanceof WorkflowEntity ) {
                throw new SusException( "Selection object can not be a workflow" );
            }
            if ( suSEntity instanceof LoadCaseEntity ) {
                throw new SusException( "Selection object can not be a load case" );
            }
            if ( suSEntity instanceof DataObjectEntity dataObjectEntity ) {
                if ( dataObjectEntity.getFile() == null ) {
                    throw new SusException( "Selection object have no attached data object file" );
                }
                return dataObjectEntity.getFile();
            }
        }
        return document;
    }

    /**
     * Download CB 2 file and return path.
     *
     * @param userUID
     *         the user UID
     * @param cb2ObjectEntity
     *         the cb 2 object entity
     *
     * @return the string
     */
    protected static String downloadCB2FileAndReturnPath( String userUID, BmwCaeBenchEntity cb2ObjectEntity ) {
        String cb2DesPath = PropertiesManager.getDefaultServerTempPath();
        String cb2RequiredFile = cb2DesPath + File.separator + cb2ObjectEntity.getName();
        // delete outdated file
        FileUtils.deleteIfExists( cb2RequiredFile );
        ProcessResult commandResult = PythonUtils.downloadCB2ObjectFileByCB2Oid( userUID, cb2ObjectEntity.getOid(), cb2DesPath,
                cb2ObjectEntity.getBmwCaeDataType() );
        if ( commandResult.getExitValue() == 2 ) {
            log.error( commandResult.getErrorStreamString() );
            throw new SusException( "File not found on cb2 for " + cb2ObjectEntity.getName() );
        } else if ( commandResult.getExitValue() != 0 ) {
            log.error( commandResult.getErrorStreamString() );
            throw new SusException( "ERROR downloading CB2 File" + cb2ObjectEntity.getName() );
        }
        if ( commandResult.getErrorStreamString() != null && !commandResult.getErrorStreamString().isEmpty() ) {
            log.warn( commandResult.getErrorStreamString() );
        }
        return cb2RequiredFile;
    }

    /**
     * Prepare select UI for custom flag.
     *
     * @param uriInfo
     *         the uri info
     * @param field
     *         the field
     * @param last
     *         the last
     *
     * @return the select UI
     */
    protected SelectFormItem prepareSelectUIForCustomFlag( UriInfo uriInfo, Map< String, Object > field, boolean last ) {
        SelectFormItem item = ( SelectFormItem ) prepareSelectUIItemFromFieldBasic( field );

        if ( !last && !field.get( TYPE ).toString().equals( FieldTypes.INPUT_TABLE.getType() ) ) {
            item.setBindFrom( uriInfo.getRequestUri().getPath().replace( "/api/", ConstantsString.EMPTY_STRING )
                    + ConstantsCustomFlagFields.VALUE_POSTFIX );
        }
        return item;
    }

    /**
     * Prepare select ui item from field ui form item.
     *
     * @param field
     *         the field
     *
     * @return the ui form item
     */
    protected UIFormItem prepareSelectUIItemFromFieldBasic( Map< String, Object > field ) {

        SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        String fieldType = String.valueOf( field.get( TYPE ) );
        if ( FieldTypes.SELECTION.getType().equals( fieldType ) ) {
            item.setPicker( Collections.singletonMap( "liveSearch", Boolean.TRUE ) );
            List< SelectOptionsUI > options = new ArrayList<>();
            String optionsJson = JsonUtils.toJson( field.get( ConstantsCustomFlagFields.OPTIONS ) );
            if ( optionsJson != null ) {
                var optionsStrings = JsonUtils.jsonToList( optionsJson, String.class );
                optionsStrings.forEach( option -> {
                    SelectOptionsUI objectItem = new SelectOptionsUI();
                    objectItem.setId( option );
                    objectItem.setName( option );
                    options.add( objectItem );
                } );
            }
            item.setOptions( options );

        }

        if ( FieldTypes.INPUT_TABLE.getType().equals( fieldType ) ) {
            List optionsStrings = JsonUtils.jsonToList( JsonUtils.toJson( field.get( ConstantsCustomFlagFields.OPTIONS ) ), String.class );
            item.setOptions( optionsStrings );
        }

        var rulesJson = JsonUtils.toJson( field.get( ConstantsCustomFlagFields.RULES ) );
        if ( rulesJson != null ) {
            item.setRules( ( Map< String, Object > ) JsonUtils.jsonToMap( rulesJson, new HashMap< String, Object >() ) );
            item.setMessages( Collections.singletonMap( ConstantsCustomFlagFields.REQUIRED, ConstantsCustomFlagFields.MUST_CHOSE_OPTION ) );
        }

        item.setTitle( String.valueOf( field.get( ConstantsCustomFlagFields.TITLE ) ) );
        item.setSubtitle( String.valueOf( field.get( ConstantsCustomFlagFields.SUBTITLE ) ) );
        item.setLabel( String.valueOf( field.get( ConstantsCustomFlagFields.LABEL ) ) );
        item.setName( String.valueOf( field.get( ConstantsCustomFlagFields.NAME ) ) );
        item.setType( String.valueOf( field.get( ConstantsCustomFlagFields.TYPE ) ) );
        item.setTooltip( String.valueOf( field.get( ConstantsCustomFlagFields.TOOLTIP ) ) );
        item.setMultiple( Boolean.parseBoolean( String.valueOf( field.get( ConstantsCustomFlagFields.MULTIPLE ) ) ) );
        item.setExternal( String.valueOf( field.get( ConstantsCustomFlagFields.EXTERNAL ) ) );
        item.setValue( field.get( ConstantsCustomFlagFields.VALUE ) );
        return item;

    }

}
