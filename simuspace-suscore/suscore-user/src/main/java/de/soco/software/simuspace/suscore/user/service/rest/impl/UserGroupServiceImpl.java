/**
 *
 */

package de.soco.software.simuspace.suscore.user.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.Collections;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.core.service.SelectionReadService;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.data.utility.ContextUtil;
import de.soco.software.simuspace.suscore.user.manager.UserGroupManager;
import de.soco.software.simuspace.suscore.user.service.rest.UserGroupService;

/**
 * Implementation of UserGroupService Interface to communicate with the bussiness layer
 *
 * @author Nosheen.Sharif
 */
public class UserGroupServiceImpl extends SuSBaseService implements UserGroupService, SelectionReadService {

    /**
     * The userGroupManager reference.
     */
    private UserGroupManager userGroupManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getUserGroupsList( String userGrpFilterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( userGrpFilterJson, FiltersDTO.class );

            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_USER.getKey() ) + ConstantsString.SPACE
                                    + MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_GROUPS.getKey() ) ),
                    userGroupManager.getUserGroupsList( getUserIdFromGeneralHeader(), filter ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createUserGroup( String objectJson ) {
        try {
            SuSUserGroupDTO model = JsonUtils.jsonToObject( JsonUtils.jsonObjectFixForGroup( objectJson ).toJSONString(),
                    SuSUserGroupDTO.class );
            model.setUpdate( Boolean.FALSE );
            SuSUserGroupDTO returnModel = userGroupManager.createUserGroup( getUserIdFromGeneralHeader(), model );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.GROUP_CREATED_SUCCESSFULLY.getKey() ), returnModel );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateUserGroup( String objectJson ) {
        try {
            SuSUserGroupDTO model = JsonUtils.jsonToObject( JsonUtils.jsonObjectFixForGroup( objectJson ).toJSONString(),
                    SuSUserGroupDTO.class );
            model.setUpdate( Boolean.TRUE );
            SuSUserGroupDTO returnModel = userGroupManager.updateUserGroup( getUserIdFromGeneralHeader(), model );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.GROUP_UPDATED_SUCCESSFULLY.getKey() ), returnModel );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteUserGroup( String groupId, String mode ) {
        try {

            boolean returnModel = userGroupManager.deleteUserGroupBySelection( getUserIdFromGeneralHeader(), groupId, mode );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.GROUP_DELETE_SUCCESSFULLY.getKey() ), returnModel );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response readUserGroup( UUID groupId ) {
        try {

            SuSUserGroupDTO returnModel = userGroupManager.readUserGroup( getUserIdFromGeneralHeader(), groupId );
            return ResponseUtils.success( returnModel );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listUsersGroupColumns() {
        try {

            return ResponseUtils.success( GUIUtils.listColumns( SuSUserGroupDTO.class ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listUsersGroupUI() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_USER.getKey() ) + ConstantsString.SPACE
                                    + MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_GROUPS.getKey() ) ),
                    new TableUI( GUIUtils.listColumns( SuSUserGroupDTO.class ), userGroupManager.getObjectViewManager()
                            .getUserObjectViewsByKey( ConstantsObjectViewKey.GROUP_TABLE_KEY, getUserIdFromGeneralHeader(), null ) ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listUsersFromGroupIdUI( UUID groupId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.USERS_FROM_GROUP_ID.getKey() ) + groupId ),
                    userGroupManager.listUsersFromGroupIdUI( getUserIdFromGeneralHeader(), groupId ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listUsersFromGroupId( UUID groupId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.USERS_FROM_GROUP_ID.getKey() ) + groupId ),
                    userGroupManager.listUsersFromGroupId( getUserIdFromGeneralHeader(), groupId, filter ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createUserGroupForm() {
        try {

            return ResponseUtils.success( userGroupManager.createUserGroupForm( getUserIdFromGeneralHeader() ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editUserGroupForm( UUID id ) {
        try {
            return ResponseUtils.success( userGroupManager.editUserGroupForm( getUserIdFromGeneralHeader(), id ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getContextRouter( String filterJson ) {

        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext( userGroupManager.getContextMenu( filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getContextRouterForSingleGroup( UUID groupId ) {
        try {
            FiltersDTO filter = new FiltersDTO();
            filter.setItems( Collections.singletonList( groupId ) );
            return ResponseUtils.success( ContextUtil.allOrderedContext( userGroupManager.getContextMenu( filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getContextRouterForUser( String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext( userGroupManager.getContextRouter( filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllViews() {
        try {
            return ResponseUtils.success( userGroupManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.GROUP_TABLE_KEY, getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getView( String viewId ) {
        try {
            return ResponseUtils.success( userGroupManager.getObjectViewManager().getObjectViewById( UUID.fromString( viewId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveView( String viewJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.GROUP_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    userGroupManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    userGroupManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.GROUP_TABLE_KEY, null ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteView( String viewId ) {
        try {
            if ( userGroupManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
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
    public Response updateView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.GROUP_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    userGroupManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getGroupUsersViews() {
        try {
            return ResponseUtils.success( userGroupManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.USER_TABLE_KEY, getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveGroupUsersView( String viewJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.USER_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    userGroupManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setGroupUsersViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    userGroupManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.USER_TABLE_KEY, null ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteGroupUsersView( String viewId ) {
        try {
            if ( userGroupManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
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
    public Response updateGroupUsersView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.USER_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    userGroupManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllSelectionsWithPagination( String selectionId, String filterJson ) {
        try {
            return ResponseUtils.success( userGroupManager.getAllGroupSelections( selectionId, getUserIdFromGeneralHeader(),
                    JsonUtils.jsonToObject( filterJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getAllValuesForGroupsTableColumn( String column ) {
        try {
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.ALL_VALUES_FOR_COLUMN_RETURNED_SUCCESSFULLY.getKey(), column ),
                    userGroupManager.getAllValuesForGroupsTableColumn( column, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllSelectionsWithoutPagination( String selectionId ) {
        try {
            return ResponseUtils.success( userGroupManager.getAllGroupsBySelectionId( selectionId, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * @return the userGroupManager
     */
    public UserGroupManager getUserGroupManager() {
        return userGroupManager;
    }

    /**
     * @param userGroupManager
     *         the userGroupManager to set
     */
    public void setUserGroupManager( UserGroupManager userGroupManager ) {
        this.userGroupManager = userGroupManager;
    }

}
