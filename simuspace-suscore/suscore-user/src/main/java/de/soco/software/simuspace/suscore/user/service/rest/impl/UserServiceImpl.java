package de.soco.software.simuspace.suscore.user.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.SuSUserDirectoryDTO;
import de.soco.software.simuspace.suscore.common.model.SusUserDirectoryAttributeDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.UserDetail;
import de.soco.software.simuspace.suscore.common.model.UserPasswordDTO;
import de.soco.software.simuspace.suscore.common.model.UserProfileDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.core.service.SelectionReadService;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.data.utility.ContextUtil;
import de.soco.software.simuspace.suscore.user.manager.UserManager;
import de.soco.software.simuspace.suscore.user.service.rest.UserService;

/**
 * This Class is responsible for all the operation Of User.
 *
 * @author Ahsan Khan
 */
public class UserServiceImpl extends SuSBaseService implements UserService, SelectionReadService {

    private static final String LDAP_FIRST_NAME = "ldapFirstName";

    private static final String LDAP_SUR_NAME = "ldapSurName";

    private static final String USER_DIRECTORY_ATTRIBUTE = "userDirectoryAttribute";

    private static final String SUS_USER_DIRECTORY_DTO = "susUserDirectoryDTO";

    private static final String DIRECTORY = "directory";

    /**
     * The Constant USER_NOT_PROVIDER.
     */
    private static final String USER_NOT_PROVIDER = "User not provided.";

    /**
     * The user manager.
     */
    private UserManager userManager;

    /**
     * {@inheritDoc}
     *
     * @throws JsonSerializationException
     */
    @Override
    public Response createUser( UUID dirID, String userJson ) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = ( JSONObject ) parser.parse( userJson );
            json.remove( DIRECTORY );

            UserDTO userModel = JsonUtils.jsonToObject( json.toJSONString(), UserDTO.class );
            SuSUserDirectoryDTO directory = new SuSUserDirectoryDTO();
            SusUserDirectoryAttributeDTO susDirAttrDTO = new SusUserDirectoryAttributeDTO();
            directory.setUserDirectoryAttribute( susDirAttrDTO );
            directory.setId( dirID );

            JSONObject object = ( JSONObject ) json.get( SUS_USER_DIRECTORY_DTO );
            if ( object != null ) {
                HashMap< ?, ? > dto = ( HashMap< ?, ? > ) object.get( USER_DIRECTORY_ATTRIBUTE );
                directory.getUserDirectoryAttribute().setLdapFirstName( dto.get( LDAP_SUR_NAME ).toString() );
                directory.getUserDirectoryAttribute().setLdapSurName( dto.get( LDAP_FIRST_NAME ).toString() );
            }

            userModel.setSusUserDirectoryDTO( directory );
            UserDTO userDtoToReturn = userManager.createUser( getUserIdFromGeneralHeader(), userModel );
            if ( userDtoToReturn != null ) {
                return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.USER_CREATED_SUCCESSFULLY.getKey() ),
                        userDtoToReturn );
            } else {
                return ResponseUtils.failure( USER_NOT_PROVIDER );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createUserForm( String dirId, String userUid ) {
        try {
            UUID dirUUID = UUID.fromString( dirId );
            var uiForm = userManager.createUserForm( getUserIdFromGeneralHeader(), dirUUID, userUid );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.NEW_USER_FORM_CREATED.getKey() ), uiForm );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getUserById( UUID userId ) {
        try {
            UserDTO user = userManager.getUserById( getUserIdFromGeneralHeader(), userId );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PROJECT_DOES_NOT_EXIST.getKey(),
                    MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_USER.getKey() ) ), user );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCurrentUser() {
        try {
            return ResponseUtils.success(
                    userManager.getUserById( getUserIdFromGeneralHeader(), UUID.fromString( getUserIdFromGeneralHeader() ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCurrentUserUid() {
        try {
            return ResponseUtils.success( getUserNameFromGeneralHeader() );
        } catch ( final Exception e ) {
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
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.CONTEXT_MENU_FETCHED.getKey() ),
                    ContextUtil.allOrderedContext( userManager.getContextRouter( filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateUser( String json ) {
        try {
            UserDTO user = JsonUtils.jsonToObject( json, UserDTO.class );
            user = userManager.updateUser( getUserIdFromGeneralHeader(), user );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.USER_UPDATED_SUCCESSFULLY.getKey() ), user );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateUserProfile( String json ) {
        try {
            UserProfileDTO user = JsonUtils.jsonToObject( json, UserProfileDTO.class );
            user.setUserPasswordDto( new UserPasswordDTO( user.getOldPassword(), user.getNewPassword(), user.getConfirmPassword() ) );
            user = userManager.updateUserProfile( getUserIdFromGeneralHeader(), user );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.USER_PROFILE_UPDATED_SUCCESSFULLY.getKey() ), user );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editUserForm( UUID userId ) {
        try {
            var columns = userManager.editUserForm( getUserIdFromGeneralHeader(), userId );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.FORM_CREATED.getKey(),
                    MessageBundleFactory.getMessage( Messages.EDIT.getKey() ) + ConstantsString.SPACE + MessageBundleFactory.getMessage(
                            Messages.DTO_UI_TITLE_USER.getKey() ) ), columns );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editUserProfileForm( UUID userId ) {
        try {
            UIForm columns = userManager.editUserProfileFormSections( userId );
            return ResponseUtils.success( columns );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listUsersColumns() {
        try {
            List< TableColumn > columns = GUIUtils.listColumns( UserDTO.class, UserDetail.class );
            return ResponseUtils.success( columns );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllLanguages() {
        try {
            return ResponseUtils.success( userManager.getAllLanguages() );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteUser( String id, String mode ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.USER_DELETED_SUCCESSFULLY.getKey() ),
                    userManager.deleteUserBySelection( getUserIdFromGeneralHeader(), id, mode ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listUsersUI() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_USERS.getKey() ) ), new TableUI( userManager.listUsersUI(),
                    userManager.getObjectViewManager()
                            .getUserObjectViewsByKey( ConstantsObjectViewKey.USER_TABLE_KEY, getUserIdFromGeneralHeader(), null ) ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllUsers( String userFilterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( userFilterJson, FiltersDTO.class );
            final FilteredResponse< UserDTO > filteredList = userManager.getAllUsers( getUserIdFromGeneralHeader(), filter );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_USERS.getKey() ) ), filteredList );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response singleUserUI() {

        try {
            List< TableColumn > columns = userManager.singleUserUI();
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.SINGLE.getKey() ) + ConstantsString.SPACE + MessageBundleFactory.getMessage(
                            Messages.DTO_UI_TITLE_USER.getKey() ) ), columns );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllViews() {
        try {
            return ResponseUtils.success( userManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.USER_TABLE_KEY, getUserIdFromGeneralHeader(), null ) );
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
            return ResponseUtils.success( userManager.getObjectViewManager().getObjectViewById( UUID.fromString( viewId ) ) );
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
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.USER_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    userManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
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
                    userManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.USER_TABLE_KEY, null ) );
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
            if ( userManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
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
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.USER_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    userManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
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
            return ResponseUtils.success( userManager.getAllUserTableSelection( selectionId, getUserIdFromGeneralHeader(),
                    JsonUtils.jsonToObject( filterJson, FiltersDTO.class ) ) );
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
            return ResponseUtils.success( userManager.getAllUsersBySelectionId( selectionId, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getUserTokenMap() {
        try {
            return ResponseUtils.success( userManager.getUserTokenMap( getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response updateUserProperty( String userId, String propertyKey, String propertyValue ) {
        try {
            return ResponseUtils.success(
                    userManager.updateUserProperty( userId, propertyKey, propertyValue, getUserIdFromGeneralHeader() ) );
        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getUserPropertyField( String propertyKey ) {
        try {
            return ResponseUtils.success( userManager.getUserPropertyField( propertyKey, getTokenFromGeneralHeader() ) );
        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    public Response getAllValuesForUserTableColumn( String column ) {
        try {
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.ALL_VALUES_FOR_COLUMN_RETURNED_SUCCESSFULLY.getKey(), column ),
                    userManager.getAllValuesForUserTableColumn( column, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listGroupsFromUserIdUI( UUID userId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.GROUPS_FROM_USER_ID.getKey() ) + userId ),
                    userManager.listGroupsFromUserIdUI( getUserIdFromGeneralHeader() ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listGroupsFromUserId( UUID userId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.GROUPS_FROM_USER_ID.getKey() ) + userId ),
                    userManager.listGroupsFromUserId( getUserIdFromGeneralHeader(), userId, filter ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getContextRouterForUserGroups( UUID userId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext( userManager.getContextRouterForUserGroups( filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
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

}
