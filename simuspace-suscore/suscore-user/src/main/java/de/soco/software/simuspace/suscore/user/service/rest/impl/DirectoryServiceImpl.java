package de.soco.software.simuspace.suscore.user.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.SuSUserDirectoryDTO;
import de.soco.software.simuspace.suscore.common.model.SusUserDirectoryAttributeDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.data.utility.ContextUtil;
import de.soco.software.simuspace.suscore.user.manager.DirectoryManager;
import de.soco.software.simuspace.suscore.user.service.rest.DirectoryService;

/**
 * Implementation of Directory Service responsible to communicate with the business lyre.
 *
 * @author M.Nasir.Farooq
 */
public class DirectoryServiceImpl extends SuSBaseService implements DirectoryService {

    /**
     * The directory manager.
     */
    private DirectoryManager directoryManager;

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.directory.utils.DirectoryService#
     * createObject(java.lang.String)
     */
    @Override
    public Response createDirectory( String objectJson ) {
        try {
            SuSUserDirectoryDTO model = JsonUtils.jsonToObject( objectJson, SuSUserDirectoryDTO.class );
            SuSUserDirectoryDTO returnModel = directoryManager.createDirectory( getUserIdFromGeneralHeader(), model );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.DIRECTORY_CREATED_SUCCESSFULLY.getKey() ),
                    returnModel );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.directory.utils.DirectoryService#
     * createObject(java.lang.String)
     */
    @Override
    public Response updateDirectory( String objectJson ) {
        try {
            SuSUserDirectoryDTO model = JsonUtils.jsonToObject( objectJson, SuSUserDirectoryDTO.class );
            SuSUserDirectoryDTO returnModel = directoryManager.updateDirectory( getUserIdFromGeneralHeader(), getTokenFromGeneralHeader(),
                    model );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.DIRECTORY_UPDATED_SUCCESSFULLY.getKey() ),
                    returnModel );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.directory.utils.DirectoryService#
     * createObject(java.lang.String)
     */
    @Override
    public Response deleteDirectory( String id, String mode ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.DIRTORY_DELETED_SUCCESSFULLY.getKey() ),
                    directoryManager.deleteDirectoryBySelection( getUserIdFromGeneralHeader(), id, mode ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.directory.utils.DirectoryService#
     * createObject(java.lang.String)
     */
    @Override
    public Response readDirectory( UUID directoryId ) {
        try {
            SuSUserDirectoryDTO returnModel = directoryManager.readDirectory( getUserIdFromGeneralHeader(), directoryId );
            return ResponseUtils.success( returnModel );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#
     * getDirectoryList()
     */
    @Override
    public Response getDirectoryList( String filtersJson ) {
        try {
            FiltersDTO filtersDTO = JsonUtils.jsonToObject( filtersJson, FiltersDTO.class );
            List< SuSUserDirectoryDTO > returnModel = directoryManager.getDirectoryList( getUserIdFromGeneralHeader(), filtersDTO );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.DIRECTORY.getKey() ) ),
                    PaginationUtil.constructFilteredResponse( filtersDTO, returnModel ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllDirectories() {
        try {
            List< SuSUserDirectoryDTO > returnModel = directoryManager.getAllDirectories( getUserIdFromGeneralHeader() );
            return ResponseUtils.success( returnModel );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDirectoriesByType( String dirType ) {
        try {
            List< SuSUserDirectoryDTO > returnModel = directoryManager.getDirectoriesByType( getUserIdFromGeneralHeader(), dirType );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DIRECTORY_SELECTED.getKey(), dirType ), returnModel );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#
     * listDirectoyUI()
     */
    @Override
    public Response listDirectoyUI() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.DIRECTORY.getKey() ) ), new TableUI( directoryManager.getDirectoryUI(),
                    directoryManager.getObjectViewManager()
                            .getUserObjectViewsByKey( ConstantsObjectViewKey.USER_DIRECTORY_TABLE_KEY, getUserIdFromGeneralHeader(),
                                    null ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#
     * listDirectoryColumn()
     */
    @Override
    public Response listDirectoryColumn() {
        try {
            List< TableColumn > columns = GUIUtils.listColumns( SuSUserDirectoryDTO.class, SusUserDirectoryAttributeDTO.class );
            return ResponseUtils.success( columns );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#
     * createDirectoryForm()
     */
    @Override
    public Response createDirectoryForm() {
        try {
            var uiForm = directoryManager.createForm( getUserIdFromGeneralHeader() );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.FORM_CREATED.getKey(),
                    MessageBundleFactory.getMessage( Messages.DIRECTORY.getKey() ) ), uiForm );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getUpdateDirectoryUI( String option ) {
        try {
            return ResponseUtils.success( directoryManager.getUpdateDirectoryUI( option ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#getContextRouter(java.lang.String)
     */
    @Override
    public Response getContextRouter( String filterJson ) {

        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.CONTEXT_MENU_FETCHED.getKey() ),
                    ContextUtil.allOrderedContext( directoryManager.getContextMenu( filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response createUserDirectoryFormForEdit( UUID id ) {
        try {
            var columns = directoryManager.createUserDirectoryFormForEdit( getUserIdFromGeneralHeader(), id );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.FORM_CREATED.getKey(),
                    MessageBundleFactory.getMessage( Messages.EDIT.getKey() ) + ConstantsString.SPACE + MessageBundleFactory.getMessage(
                            Messages.DIRECTORY.getKey() ) ), columns );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#testDirectoryConnection(java.lang.String)
     */
    @Override
    public Response testDirectoryConnection( String json ) {
        try {
            SuSUserDirectoryDTO susUserDirectoryDTO = JsonUtils.jsonToObject( json, SuSUserDirectoryDTO.class );
            boolean connected = directoryManager.testLdapADConnection( susUserDirectoryDTO );
            if ( connected ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.CONNECTION_SUCCESSFUL.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.ONLY_APPLICABLE_TO_LDAP_OR_AD.getKey() ) );
            }

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#getAllViews()
     */
    @Override
    public Response getAllViews() {
        try {
            return ResponseUtils.success( directoryManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.USER_DIRECTORY_TABLE_KEY, getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#getView(java.lang.String)
     */
    @Override
    public Response getView( String viewId ) {
        try {
            return ResponseUtils.success( directoryManager.getObjectViewManager().getObjectViewById( UUID.fromString( viewId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#saveView(java.lang.String)
     */
    @Override
    public Response saveView( String viewJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.USER_DIRECTORY_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    directoryManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#setViewAsDefault(java.lang.String)
     */
    @Override
    public Response setViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    directoryManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.USER_DIRECTORY_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#deleteView(java.lang.String)
     */
    @Override
    public Response deleteView( String viewId ) {
        try {
            if ( directoryManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#updateView(java.lang.String, java.lang.String)
     */
    @Override
    public Response updateView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.USER_DIRECTORY_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    directoryManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getAllValuesForDirectoryTableColumn( String column ) {
        try {
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.ALL_VALUES_FOR_COLUMN_RETURNED_SUCCESSFULLY.getKey(), column ),
                    directoryManager.getAllValuesForDirectoryTableColumn( column, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the directory manager.
     *
     * @return the directoryManager
     */
    public DirectoryManager getDirectoryManager() {
        return directoryManager;
    }

    /**
     * Sets the directory manager.
     *
     * @param directoryManager
     *         the directoryManager to set
     */
    public void setDirectoryManager( DirectoryManager directoryManager ) {
        this.directoryManager = directoryManager;
    }

}
