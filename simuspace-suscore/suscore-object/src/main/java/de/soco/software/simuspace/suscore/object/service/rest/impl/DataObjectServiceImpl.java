package de.soco.software.simuspace.suscore.object.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMode;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.model.CurveUnitDTO;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.MoveDTO;
import de.soco.software.simuspace.suscore.common.model.ValueUnitDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.model.ModelFilesDTO;
import de.soco.software.simuspace.suscore.data.model.ProjectDTO;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.data.utility.ContextUtil;
import de.soco.software.simuspace.suscore.object.manager.DataObjectManager;
import de.soco.software.simuspace.suscore.object.model.ChangeStatusDTO;
import de.soco.software.simuspace.suscore.object.model.ObjectMetaDataDTO;
import de.soco.software.simuspace.suscore.object.service.rest.DataObjectService;

public class DataObjectServiceImpl extends SuSBaseService implements DataObjectService {

    private DataObjectManager dataObjectManager;

    /**
     * The Constant OBJECT.
     */
    private static final String OBJECT = "Object";

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createObject( String parentId, String typeId, String objectJson ) {
        Object object;
        boolean autoRefresh = true;
        try {

            object = dataObjectManager.createSuSObject( getUserIdFromGeneralHeader(), parentId, typeId, objectJson, autoRefresh,
                    getTokenFromGeneralHeader() );

            if ( object == null ) {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.UNABLE_TO_CREATE_OBJECT.getKey() ) );
            } else if ( object instanceof ProjectDTO ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PROJECT_CREATED_SUCCESSFULLY.getKey() ), object );
            } else {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.OBJECT_CREATED_SUCCESSFULLY.getKey() ), object );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createObjectForm( String parentId, String typeId ) {
        try {
            return ResponseUtils.success( dataObjectManager.createObjectForm( getTokenFromGeneralHeader(), parentId, typeId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createTranslationForm( String parentId, String typeId, String translations ) {
        try {
            return ResponseUtils.success(
                    dataObjectManager.createTranslationForm( getUserIdFromGeneralHeader(), parentId, typeId, translations ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateTranslationForm( String parentId, String typeId, String translations ) {
        try {
            String translatedText = getTranslatedLanguages( translations );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.TRANSLATION_SELECTED.getKey(), translatedText ),
                    dataObjectManager.updateTranslationForm( getUserIdFromGeneralHeader(), parentId, typeId, translations ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    private String getTranslatedLanguages( String translations ) {
        if ( translations == null || translations.isEmpty() ) {
            return "Unknown";
        }

        return Arrays.stream( translations.split( ConstantsString.COMMA ) ).map( PropertiesManager.getRequiredlanguages()::get )
                .filter( Objects::nonNull ).collect( Collectors.joining( ", " ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated( since = "soco/2.3.1/release", forRemoval = true )
    public Response createObjectFormDashboardPlugin( String parentId, String typeId, String plugin ) {
        try {
            return ResponseUtils.success(
                    dataObjectManager.createObjectFormDashboardPlugin( getUserIdFromGeneralHeader(), parentId, typeId, plugin ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getConfigFromObjectAndPlugin( String objectId, String plugin ) {
        try {
            return ResponseUtils.success(
                    dataObjectManager.getConfigFromObjectAndPlugin( getUserIdFromGeneralHeader(), objectId, plugin ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getConfigOptionsFromObjectAndPluginAndConfig( String objectId, String plugin, String config ) {
        try {
            return ResponseUtils.success(
                    dataObjectManager.getConfigOptionsFromObjectAndPluginAndConfig( getUserIdFromGeneralHeader(), objectId, plugin,
                            config ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createObjectFormDashboardPluginAndConfig( String parentId, String typeId, String plugin, String config ) {
        try {
            return ResponseUtils.success(
                    dataObjectManager.createObjectFormDashboardPluginAndConfig( getUserIdFromGeneralHeader(), parentId, typeId, plugin,
                            config ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response moveObject( String filterJson ) {
        try {
            final MoveDTO filter = JsonUtils.jsonToObject( filterJson, MoveDTO.class );
            return ResponseUtils.success(
                    dataObjectManager.moveObject( getUserIdFromGeneralHeader(), UUID.fromString( filter.getSrcSelectionId() ),
                            UUID.fromString( filter.getTargetId() ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectByIdAndVersion( String objectId, int versionId ) {
        try {
            Object object = dataObjectManager.getObjectByIdAndVersion( getUserIdFromGeneralHeader(),
                    new VersionPrimaryKey( UUID.fromString( objectId ), versionId ) );
            if ( object != null ) {
                return ResponseUtils.success( object );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllItemsByObjectId( String objectId, String objectFilterJson ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.LINKED_TO.getKey() ) + ConstantsString.SPACE
                                    + MessageBundleFactory.getMessage( Messages.ITEMS.getKey() ) ),
                    dataObjectManager.getAllItemsObjects( getUserIdFromGeneralHeader(), UUID.fromString( objectId ),
                            JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ), true ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllFromItemsByObjectId( String objectId, String objectFilterJson ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.LINKED_FROM.getKey() ) + ConstantsString.SPACE
                                    + MessageBundleFactory.getMessage( Messages.ITEMS.getKey() ) ),
                    dataObjectManager.getAllItemsObjects( getUserIdFromGeneralHeader(), UUID.fromString( objectId ),
                            JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ), false ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectProperties( String objectId ) {
        try {
            Object object = dataObjectManager.getDataObjectProperties( getUserIdFromGeneralHeader(), UUID.fromString( objectId ) );
            if ( object != null ) {
                return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                        MessageBundleFactory.getMessage( Messages.SINGLE_DATA_OBJECT_PROPERTIES.getKey() ) ), object );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObject( String objectId ) {
        try {
            Object object = dataObjectManager.getDataObject( getUserIdFromGeneralHeader(), UUID.fromString( objectId ) );
            if ( object != null ) {
                return ResponseUtils.success( object );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteObjectById( String objectId, String mode ) {
        try {

            if ( mode.equals( ConstantsMode.SINGLE ) ) {
                dataObjectManager.deleteObjectVersionsAndRelations( getUserIdFromGeneralHeader(), UUID.fromString( objectId ) );
            } else {
                dataObjectManager.deleteObjectVersionsAndRelationsBulk( getUserIdFromGeneralHeader(), objectId );
            }

            return ResponseUtils.success( MessageBundleFactory.getMessage(
                    Messages.SELECTED_ITEMS_AND_DEPENDENCIES_STARTED_DELETION_PROCESS_SUCCESSFULLY.getKey() ), true );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSingleDataObjectPropertiesUI( String objectId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.SINGLE_DATA_OBJECT_PROPERTIES.getKey() ) ),
                    dataObjectManager.getObjectSingleUI( objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectVersionPropertiesUI( String objectId, int version ) {
        try {
            return ResponseUtils.success( dataObjectManager.getObjectVersionUI( objectId, version ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectVersionAuditLogUI( String objectId, int version ) {
        try {
            return ResponseUtils.success( new TableUI( GUIUtils.listColumns( AuditLogDTO.class ), dataObjectManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.GENERAL_AUDIT_TABLE_KEY, getUserIdFromGeneralHeader(), null ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSingleDataObjectVersionUI( String objectId, int versionId ) {
        try {
            return ResponseUtils.success(
                    dataObjectManager.getTabsViewDataObjectVersionUI( getUserIdFromGeneralHeader(), objectId, versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectVersionsUI( String objectId ) {
        try {
            return ResponseUtils.success( new TableUI( dataObjectManager.getDataObjectVersionsUI( objectId ),
                    dataObjectManager.getObjectViewManager()
                            .getUserObjectViewsByKey( ConstantsObjectViewKey.VERSION_TABLE_KEY, getUserIdFromGeneralHeader(),
                                    objectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectVersionsContext( String objectId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success(
                    ContextUtil.allOrderedContext( dataObjectManager.getDataObjectVersionContext( objectId, filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFilteredObjectVersionsList( String objectId, String objectFilterJson ) {
        try {
            return ResponseUtils.success( dataObjectManager.getObjectVersions( getUserIdFromGeneralHeader(), UUID.fromString( objectId ),
                    JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getTabsViewDataObjectUI( String objectId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getTabsViewDataObjectUI( getTokenFromGeneralHeader(), objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response permissionObjectOptionsForm( String objectId ) {
        try {
            return ResponseUtils.success( dataObjectManager.permissionObjectOptionsForm( getUserIdFromGeneralHeader(), objectId, OBJECT ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response changeObjectPermissions( String objectId, String objectFilterJson ) {
        try {
            Map< String, String > map = JsonUtils.convertStringToMap( objectFilterJson );
            return ResponseUtils.success( dataObjectManager.changeObjectPermissions( getUserIdFromGeneralHeader(), objectId, map ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response objectPermissionTableUI( UUID objectId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.PERMISSIONS.getKey() ) ),
                    dataObjectManager.objectPermissionTableUI( getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response permitPermissionToObject( String permissionJson, UUID objectId, UUID sidId ) {
        boolean isPermitted = false;
        try {
            Map< String, Object > checkBoxStateMap = new HashMap<>();
            checkBoxStateMap = ( Map< String, Object > ) JsonUtils.jsonToMap( permissionJson, checkBoxStateMap );
            CheckBox checkBoxState = null;
            for ( Map.Entry< String, Object > entry : checkBoxStateMap.entrySet() ) {
                if ( entry.getKey().equals( "level" ) ) {
                    String value = entry.getValue().toString();
                    if ( StringUtils.isNotBlank( entry.getValue().toString() ) ) {
                        int valueOfLevel = PermissionMatrixEnum.getKeyByValue( value );
                        checkBoxState = new CheckBox( null, entry.getKey(), valueOfLevel );
                    }

                } else {
                    checkBoxState = new CheckBox( null, entry.getKey().substring( 15 ), Integer.valueOf( entry.getValue().toString() ) );
                }
            }
            isPermitted = dataObjectManager.permitPermissionToObject( checkBoxState, objectId, sidId, getUserIdFromGeneralHeader() );
            dataObjectManager.removeElsObjectIfPermissionNone( checkBoxState, objectId );
            if ( isPermitted ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PERMISSION_APPLIED_SUCCESSFULLY.getKey() ),
                        isPermitted );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.PERMISSION_NOT_APPLIED_SUCCESSFULLY.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response showPermittedUsersAndGroupsForObject( UUID objectId, String objectFilterJson ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.PERMISSIONS.getKey() ) ),
                    dataObjectManager.showPermittedUsersAndGroupsForObject( JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ),
                            objectId, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectMetaDataTableUI( String objectId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.METADATA.getKey() ) ),
                    new TableUI( dataObjectManager.getObjectMetaDataTableUI( objectId ), dataObjectManager.getObjectViewManager()
                            .getUserObjectViewsByKey( ConstantsObjectViewKey.META_DATA_TABLE_KEY, getUserIdFromGeneralHeader(),
                                    objectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response addMetaDataToAnObject( String objectId, String json ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.METADATA_ADDED.getKey() ),
                    dataObjectManager.addMetaDataToAnObject( objectId, JsonUtils.jsonToObject( json, ObjectMetaDataDTO.class ),
                            getUserIdFromGeneralHeader(), true ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectMetaDatalist( String objectId, String objectFilterJson ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.METADATA.getKey() ) ),
                    dataObjectManager.getObjectMetaDataList( getUserIdFromGeneralHeader(), objectId,
                            JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectMetaDatalistByVersion( String objectId, int versionId, String objectFilterJson ) {
        try {
            return ResponseUtils.success(
                    dataObjectManager.getObjectMetaDataListByVersion( getUserIdFromGeneralHeader(), objectId, versionId,
                            JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createMetaDataForm( String objectId ) {
        try {
            var creatForm = dataObjectManager.prepareMetaDataFormForCreate( getUserIdFromGeneralHeader(), objectId );
            return ResponseUtils.success( creatForm );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectMetaDataContext( String objectId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext( dataObjectManager.getMetaDataContextRouter( objectId, filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectItemContext( String objectId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext(
                    dataObjectManager.getObjectItemsContextRouter( getUserIdFromGeneralHeader(), objectId, filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectItemFromContext( String objectId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext(
                    dataObjectManager.getObjectItemsContextRouter( getUserIdFromGeneralHeader(), objectId, filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteMetaDataBySelection( String objectId, String key, String mode ) {
        try {
            dataObjectManager.deleteMetaDataBySelection( getUserIdFromGeneralHeader(), objectId, key, mode );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.RECORD_DELETED.getKey() ), true );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createMetaDataFormForEdit( String objectId, String key ) {
        try {
            return ResponseUtils.success( dataObjectManager.prepareMetaDataFormForEdit( getUserIdFromGeneralHeader(), objectId, key ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateMetaDataToAnObject( String objectId, String json ) {
        try {

            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.MATADATA_UPDATED_SUCCESSFULLY.getKey() ),
                    dataObjectManager.addMetaDataToAnObject( objectId, JsonUtils.jsonToObject( json, ObjectMetaDataDTO.class ),
                            getUserIdFromGeneralHeader(), false ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getUpdateObjectPermissionUI( String objectId, String option ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.MANAGE_PERMISSIONS.getKey() ),
                    dataObjectManager.getUpdateObjectPermissionUI( getUserIdFromGeneralHeader(), objectId, option ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateFileToAnObject( String objectId, String json ) {
        try {
            Map< String, DocumentDTO > map = new HashMap<>();
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.FILE_UPDATED_SUCCESSFULLY.getKey() ),
                    dataObjectManager.addFileToAnObject( null, objectId, ( Map< String, DocumentDTO > ) JsonUtils.jsonToMap( json, map ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectVersionMetaDataTableUI( String objectId, int versionId ) {
        try {
            return ResponseUtils.success( new TableUI( dataObjectManager.getObjectMetaDataTableUI( objectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createEditDataObjectForm( String objectId ) {
        try {
            return ResponseUtils.success( dataObjectManager.editDataObjectForm( objectId, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateDataObject( String objectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.OBJECT_UPDATED_SUCCESSFULLY.getKey() ),
                    dataObjectManager.updateDataObject( getUserIdFromGeneralHeader(), objectId, objectJson ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listObjectItemUI( String objectId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.LINKED_TO.getKey() ) + ConstantsString.SPACE
                                    + MessageBundleFactory.getMessage( Messages.ITEMS.getKey() ) ),
                    dataObjectManager.getListOfObjectsUITableColumns( getUserIdFromGeneralHeader(), objectId, GenericDTO.GENERIC_DTO_TYPE,
                            dataObjectManager.getObjectViewManager()
                                    .getUserObjectViewsByKey( ConstantsObjectViewKey.LINKED_TO_ITEMS_TABLE_KEY,
                                            getUserIdFromGeneralHeader(), objectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listObjectItemFromUI( String objectId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.LINKED_FROM.getKey() ) + ConstantsString.SPACE
                                    + MessageBundleFactory.getMessage( Messages.ITEMS.getKey() ) ),
                    dataObjectManager.getListOfObjectsUITableColumns( getUserIdFromGeneralHeader(), objectId, GenericDTO.GENERIC_DTO_TYPE,
                            dataObjectManager.getObjectViewManager()
                                    .getUserObjectViewsByKey( ConstantsObjectViewKey.LINKED_FROM_ITEMS_TABLE_KEY,
                                            getUserIdFromGeneralHeader(), objectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response changeStatusObjectOptionForm( String objectId ) {
        try {
            return ResponseUtils.success(
                    dataObjectManager.changeStatusObjectOptionForm( getUserIdFromGeneralHeader(), UUID.fromString( objectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response changeStatusObjectVersionOptionForm( String objectId, int versionId ) {
        try {
            return ResponseUtils.success(
                    dataObjectManager.changeStatusObjectVersionOptionForm( getUserIdFromGeneralHeader(), UUID.fromString( objectId ),
                            versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response changeStatusObject( String objectId, String json ) {
        try {
            boolean statusObject = dataObjectManager.changeStatusObject( getUserIdFromGeneralHeader(), UUID.fromString( objectId ),
                    JsonUtils.jsonToObject( json, ChangeStatusDTO.class ) );
            if ( statusObject ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.STATUS_CHANGED_SUCCESSFULLY.getKey() ),
                        statusObject );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.FAILED_TO_CHANGE_STATUS.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response changeStatusObjectVersion( String objectId, String json, int versionId ) {
        try {
            return ResponseUtils.success(
                    dataObjectManager.changeStatusObjectVersion( getUserIdFromGeneralHeader(), UUID.fromString( objectId ),
                            JsonUtils.jsonToObject( json, ChangeStatusDTO.class ), versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectPreview( String objectId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getDataObjectPreview( UUID.fromString( objectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectCurve( String objectId, String untJson ) {
        try {
            return ResponseUtils.success( dataObjectManager.getDataObjectCurve( UUID.fromString( objectId ),
                    JsonUtils.jsonToObject( untJson, CurveUnitDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectVersionCurve( String objectId, int versionId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getDataObjectVersionCurve( UUID.fromString( objectId ), versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectVersionHtml( String objectId, int versionId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getDataObjectVersionHtml( UUID.fromString( objectId ), versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectVersionPreview( String objectId, int versionId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getDataObjectVersionPreview( UUID.fromString( getUserIdFromGeneralHeader() ),
                    UUID.fromString( objectId ), versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectMovie( String objectId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getDataObjectMovie( UUID.fromString( objectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectVersionMovie( String objectId, int versionId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getDataObjectVersionMovie( UUID.fromString( objectId ), versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllObjectPermissionViewsByObjectId( String objectId, String viewId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.PERMISSION_TABLE_KEY, getUserIdFromGeneralHeader(), objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveObjectPermissionView( String objectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    dataObjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( objectId, objectJson, ConstantsObjectViewKey.PERMISSION_TABLE_KEY, false, null ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setObjectPermissionViewAsDefault( String objectId, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    dataObjectManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.PERMISSION_TABLE_KEY, objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteObjectPermissionView( String objectId, String viewId ) {
        try {
            if ( dataObjectManager.deleteObjectView( viewId ) ) {
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
    public Response updateObjectPermissionView( String objectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    dataObjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( objectId, objectJson, ConstantsObjectViewKey.PERMISSION_TABLE_KEY, true, viewId ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllObjectMetaDataViewsByObjectId( String objectId, String viewId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.META_DATA_TABLE_KEY, getUserIdFromGeneralHeader(), objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveObjectMetaDataView( String objectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    dataObjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( objectId, objectJson, ConstantsObjectViewKey.META_DATA_TABLE_KEY, false, null ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setObjectMetaDataViewAsDefault( String objectId, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    dataObjectManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.META_DATA_TABLE_KEY, objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteObjectMetaDataView( String objectId, String viewId ) {
        try {
            if ( dataObjectManager.deleteObjectView( viewId ) ) {
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
    public Response updateObjectMetaDataView( String objectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    dataObjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( objectId, objectJson, ConstantsObjectViewKey.META_DATA_TABLE_KEY, true, viewId ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllDataObjectVersionViewsByObjectId( String objectId, String viewId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.VERSION_TABLE_KEY, getUserIdFromGeneralHeader(), objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveDataObjectVersionView( String objectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    dataObjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( objectId, objectJson, ConstantsObjectViewKey.VERSION_TABLE_KEY, false, null ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setDataObjectVersionViewAsDefault( String objectId, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    dataObjectManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.VERSION_TABLE_KEY, objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteDataObjectVersionView( String objectId, String viewId ) {
        try {
            if ( dataObjectManager.deleteObjectView( viewId ) ) {
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
    public Response updateDataObjectVersionView( String objectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    dataObjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( objectId, objectJson, ConstantsObjectViewKey.VERSION_TABLE_KEY, true, viewId ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response auditLogForDataObjectView( String objectId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.AUDIT.getKey() ) ), new TableUI( GUIUtils.listColumns( AuditLogDTO.class ),
                    dataObjectManager.getObjectViewManager()
                            .getUserObjectViewsByKey( ConstantsObjectViewKey.GENERAL_AUDIT_TABLE_KEY, getUserIdFromGeneralHeader(),
                                    objectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getLogListByDataObject( String objectId, String objectJson ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.AUDIT.getKey() ) ), dataObjectManager.getAuditLogManager()
                    .getLogListByDataObject( UUID.fromString( objectId ), JsonUtils.jsonToObject( objectJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getModelFilesUI( String objectId ) {
        try {
            return ResponseUtils.success( new TableUI( GUIUtils.listColumns( ModelFilesDTO.class ), dataObjectManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.MODEL_FILES_TABLE_KEY, getUserIdFromGeneralHeader(), objectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getModelFilesData( String objectId, String objectJson ) {
        try {
            return ResponseUtils.success( dataObjectManager.getModelFilesData( UUID.fromString( objectId ),
                    JsonUtils.jsonToObject( objectJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getModelFilesContext( String objectId, String objectJson ) {
        try {
            return ResponseUtils.success( new ArrayList<>() );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectFileInfo( String objectId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getFileInfo( getUserIdFromGeneralHeader(), objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectVersionAuditLogList( String objectId, int versionId, String objectJson ) {
        try {
            return ResponseUtils.success( dataObjectManager.getAuditLogManager()
                    .getLogListByObjectAndVersionId( UUID.fromString( objectId ), versionId,
                            JsonUtils.jsonToObject( objectJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllObjectAuditViewsByObjectId( String objectId, String viewId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.GENERAL_AUDIT_TABLE_KEY, getUserIdFromGeneralHeader(), objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveObjectAuditView( String objectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    dataObjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( objectId, objectJson, ConstantsObjectViewKey.GENERAL_AUDIT_TABLE_KEY, false, null ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setAuditViewAsDefault( String objectId, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    dataObjectManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.GENERAL_AUDIT_TABLE_KEY, objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteObjectAuditView( String objectId, String viewId ) {
        try {
            if ( dataObjectManager.deleteObjectView( viewId ) ) {
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
    public Response updateObjectAuditView( String objectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    dataObjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( objectId, objectJson, ConstantsObjectViewKey.GENERAL_AUDIT_TABLE_KEY, true, viewId ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getCeetron3DObjectModel( UUID objectId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getCeetron3DObjectModel( getUserIdFromGeneralHeader(), objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createObjectWithoutRefresingTable( String parentId, String typeId, String objectJson ) {
        Object object;
        boolean autoRefresh = false;
        try {

            object = dataObjectManager.createSuSObject( getUserIdFromGeneralHeader(), parentId, typeId, objectJson, autoRefresh,
                    getTokenFromGeneralHeader() );

            if ( object == null ) {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.UNABLE_TO_CREATE_OBJECT.getKey() ) );
            } else if ( object instanceof ProjectDTO ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PROJECT_CREATED_SUCCESSFULLY.getKey() ), object );
            } else {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.OBJECT_CREATED_SUCCESSFULLY.getKey() ), object );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getCurveXUnits( String objectId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getCurveXUnits( objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getCurveYUnits( String objectId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getCurveYUnits( objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectValueUnits( String objectId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getDataObjectValueUnits( objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectValue( String objectId, String untJson ) {
        try {
            return ResponseUtils.success( dataObjectManager.getDataObjectValue( UUID.fromString( objectId ),
                    JsonUtils.jsonToObject( untJson, ValueUnitDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getTraceListByObjectId( String objectId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getDataObjectTraceByObjectId( objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getHtmlObjectByObjectId( String objectId, String language ) {
        try {
            return ResponseUtils.success(
                    dataObjectManager.getHtmlObjectByObjectId( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(),
                            getTokenFromGeneralHeader(), objectId, language ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getModelByObjectId( String objectId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getModelByObjectId( objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getTraceListByVersionAndObjectId( String objectId, int versionId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getDataObjectTraceByVersionAndObjectId( objectId, versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectVersionCurveY( String objectId, int versionId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getDataObjectVersionCurveY( UUID.fromString( objectId ), versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectVersionCurveX( String objectId, int versionId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getDataObjectVersionCurveX( UUID.fromString( objectId ), versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listObjectItemFromVersionUI( String objectId, int versionId ) {
        try {
            return ResponseUtils.success(
                    dataObjectManager.getListOfObjectsUITableColumnsByVersions( getUserIdFromGeneralHeader(), objectId,
                            GenericDTO.GENERIC_DTO_TYPE, versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listObjectItemVersionUI( String objectId, int versionId ) {
        try {
            return ResponseUtils.success(
                    dataObjectManager.getListOfObjectsUITableColumnsByVersions( getUserIdFromGeneralHeader(), objectId,
                            GenericDTO.GENERIC_DTO_TYPE, versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllItemsByObjectIdAndVersion( String objectId, String objectFilterJson, int versionId ) {
        try {
            return ResponseUtils.success(
                    dataObjectManager.getAllItemsObjectsByVersion( getUserIdFromGeneralHeader(), UUID.fromString( objectId ),
                            JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ), true, versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllFromItemsByObjectIdAndVersion( String objectId, String objectFilterJson, int versionId ) {
        try {
            return ResponseUtils.success(
                    dataObjectManager.getAllItemsObjectsByVersion( getUserIdFromGeneralHeader(), UUID.fromString( objectId ),
                            JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ), true, versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectCurveByVersion( String objectId, String unitJson, int versionId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getDataObjectCurveByVersion( UUID.fromString( objectId ),
                    JsonUtils.jsonToObject( unitJson, CurveUnitDTO.class ), versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectValueUnitsbyVersion( String objectId, int versionId ) {
        try {
            // noman chane it to version
            return ResponseUtils.success( dataObjectManager.getDataObjectValueUnitsByVersion( objectId, versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectValueByVersion( String objectId, String unitJson, int versionId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getDataObjectValueByVersion( UUID.fromString( objectId ),
                    JsonUtils.jsonToObject( unitJson, ValueUnitDTO.class ), versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getPlotListByObjectId( String objectId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getDataObjectTracePlotByObjectId( objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getPlotListByObjectIdAndVersionId( String objectId, int versionId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getDataObjectTracePlotByObjectIdAndVersionId( objectId, versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response savePlotListByObjectId( String objectId, String objectJson ) {
        try {
            return ResponseUtils.success( dataObjectManager.saveDataObjectTracePlotByObjectId( objectId, objectJson ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAuditContext( String objectId ) {
        try {
            return ResponseUtils.success( new ArrayList<>() );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllObjectmodelfilesViewsByObjectId( String objectId, String viewId ) {
        try {
            return ResponseUtils.success( dataObjectManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.MODEL_FILES_TABLE_KEY, getUserIdFromGeneralHeader(), objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveObjectmodelfilesView( String objectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    dataObjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( objectId, objectJson, ConstantsObjectViewKey.MODEL_FILES_TABLE_KEY, false, null ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setmodelfilesViewAsDefault( String objectId, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    dataObjectManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.MODEL_FILES_TABLE_KEY, objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteObjectmodelfilesView( String objectId, String viewId ) {
        try {
            if ( dataObjectManager.deleteObjectView( viewId ) ) {
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
    public Response updateObjectmodelfilesView( String objectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    dataObjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( objectId, objectJson, ConstantsObjectViewKey.MODEL_FILES_TABLE_KEY, true, viewId ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    public void setDataObjectManager( DataObjectManager dataObjectManager ) {
        this.dataObjectManager = dataObjectManager;
    }

}
