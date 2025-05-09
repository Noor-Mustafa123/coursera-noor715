package de.soco.software.simuspace.suscore.role.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.json.simple.parser.ParseException;

import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.data.utility.ContextUtil;
import de.soco.software.simuspace.suscore.permissions.constants.PermissionManageForm;
import de.soco.software.simuspace.suscore.role.constants.ConstantsPermissions;
import de.soco.software.simuspace.suscore.role.manager.RoleManager;
import de.soco.software.simuspace.suscore.role.model.RoleDTO;
import de.soco.software.simuspace.suscore.role.service.rest.RoleService;

/**
 * This Class provides the Roles API implementation for managing roles and assigning permission to role.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public class RoleServiceImpl extends SuSBaseService implements RoleService {

    /**
     * The role manager.
     */
    private RoleManager roleManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response manageRolePermissionUI( UUID roleId ) {
        try {
            return ResponseUtils.success( new TableUI( roleManager.managePermissionTableUI( getUserIdFromGeneralHeader() ),
                    roleManager.getUserObjectViewsByKey( ConstantsObjectViewKey.MANAGE_ROLE_TABLE_KEY, getUserIdFromGeneralHeader(),
                            null ) ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllPermissionsByRoleId( String filterJsonStr, UUID roleId ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJsonStr, FiltersDTO.class );
            FilteredResponse< PermissionManageForm > response = roleManager.getAllPermissionsByRoleId( filter, getUserIdFromGeneralHeader(),
                    roleId );
            return ResponseUtils.success( response );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response permitPermissionToRole( String permissionRole, UUID roleId, UUID resourceId ) {
        boolean isPermitted;
        try {
            Map< String, Object > checkBoxStateMap = new HashMap<>();
            checkBoxStateMap = ( Map< String, Object > ) JsonUtils.jsonToMap( permissionRole, checkBoxStateMap );
            CheckBox checkBoxState = null;
            for ( Entry< String, Object > entry : checkBoxStateMap.entrySet() ) {
                if ( "level".equals( entry.getKey() ) ) {
                    int valueOfLevel = PermissionMatrixEnum.getKeyByValue( entry.getValue().toString() );
                    checkBoxState = new CheckBox( null, entry.getKey(), valueOfLevel );

                } else {
                    checkBoxState = new CheckBox( null, entry.getKey().substring( ConstantsPermissions.TOTAL_CHECKBOX_COUNT ),
                            Integer.parseInt( entry.getValue().toString() ) );
                }
            }
            isPermitted = roleManager.permitPermissionToRole( getUserIdFromGeneralHeader(), checkBoxState, roleId, resourceId, true );
            if ( isPermitted ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PERMISSION_APPLIED_SUCCESSFULLY.getKey() ), true );
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
    public Response readRole( UUID roleId ) {
        try {
            RoleDTO returnModel = roleManager.readRole( getUserIdFromGeneralHeader(), roleId );
            return ResponseUtils.success( returnModel );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ParseException
     */
    @Override
    public Response createRole( String roleJson ) throws ParseException {
        RoleDTO role;
        try {
            role = JsonUtils.jsonToObject( JsonUtils.jsonObjectFixForRole( roleJson ).toJSONString(), RoleDTO.class );
            role.setUpdate( Boolean.FALSE );
            role = roleManager.createRole( getUserIdFromGeneralHeader(), role, true );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.ROLE_CREATED_SUCCESSFULLY.getKey() ), role );
        } catch ( final SusException e ) {
            return ResponseUtils.failure( e.getLocalizedMessage(), e.getStatusCode() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createRoleForm() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.FORM_CREATED.getKey(),
                            MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_ROLE.getKey() ) ),
                    roleManager.createRoleForm( getUserIdFromGeneralHeader() ) );
        } catch ( final SusException e ) {
            return ResponseUtils.failure( e.getLocalizedMessage(), e.getStatusCode() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateRole( String roleJson ) {
        try {
            RoleDTO model = JsonUtils.jsonToObject( JsonUtils.jsonObjectFixForRole( roleJson ).toJSONString(), RoleDTO.class );
            model.setUpdate( Boolean.TRUE );
            RoleDTO returnModel = roleManager.updateRole( getUserIdFromGeneralHeader(), model );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.ROLE_UPDATED_SUCCESSFULLY.getKey() ), returnModel );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteRole( String groupId, String mode ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.ROLE_DELETE_SUCCESSFULLY.getKey() ),
                    roleManager.deleteRoleBySelection( getUserIdFromGeneralHeader(), groupId, mode ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editRoleForm( UUID id ) {
        try {
            return ResponseUtils.success( roleManager.editRoleForm( getUserIdFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listRoleUI() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_ROLE.getKey() ) ),
                    new TableUI( GUIUtils.listColumns( RoleDTO.class ),
                            roleManager.getUserObjectViewsByKey( ConstantsObjectViewKey.ROLE_TABLE_KEY, getUserIdFromGeneralHeader(),
                                    null ) ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getRoleList( String userJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( userJson, FiltersDTO.class );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_ROLE.getKey() ) ),
                    roleManager.getRoleList( getUserIdFromGeneralHeader(), filter ) );
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
            return ResponseUtils.success( ContextUtil.allOrderedContext( roleManager.getContextMenu( filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.UserService#getAllViews()
     */
    @Override
    public Response getAllViews() {
        try {
            return ResponseUtils.success(
                    roleManager.getUserObjectViewsByKey( ConstantsObjectViewKey.ROLE_TABLE_KEY, getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#getView(java.lang.String)
     */
    @Override
    public Response getView( String viewId ) {
        try {
            return ResponseUtils.success( roleManager.getObjectViewById( UUID.fromString( viewId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#saveView(java.lang.String)
     */
    @Override
    public Response saveView( String viewJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.ROLE_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    roleManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#setViewAsDefault(java.lang.String)
     */
    @Override
    public Response setViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    roleManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.ROLE_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#deleteView(java.lang.String)
     */
    @Override
    public Response deleteView( String viewId ) {
        try {
            if ( roleManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#updateView(java.lang.String, java.lang.String)
     */
    @Override
    public Response updateView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.ROLE_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    roleManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.role.service.rest.RoleService#getAllManageRoleViews(java.lang.String)
     */
    @Override
    public Response getAllManageRoleViews( String id ) {
        try {
            return ResponseUtils.success(
                    roleManager.getUserObjectViewsByKey( ConstantsObjectViewKey.MANAGE_ROLE_TABLE_KEY, getUserIdFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.role.service.rest.RoleService#saveManageRoleView(java.lang.String, java.lang.String)
     */
    @Override
    public Response saveManageRoleView( String id, String viewJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
            objectViewDTO.setId( null );
            objectViewDTO.setObjectId( id );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.MANAGE_ROLE_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    roleManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.role.service.rest.RoleService#setManageRoleViewAsDefault(java.lang.String, java.lang.String)
     */
    @Override
    public Response setManageRoleViewAsDefault( String id, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    roleManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.MANAGE_ROLE_TABLE_KEY, id ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.role.service.rest.RoleService#deleteManageRoleView(java.lang.String, java.lang.String)
     */
    @Override
    public Response deleteManageRoleView( String id, String viewId ) {
        try {
            if ( roleManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.role.service.rest.RoleService#updateManageRoleView(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public Response updateManageRoleView( String id, String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectId( id );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.MANAGE_ROLE_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    roleManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getAllValuesForRoleTableColumn( String column ) {
        try {
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.ALL_VALUES_FOR_COLUMN_RETURNED_SUCCESSFULLY.getKey(), column ),
                    roleManager.getAllValuesForRoleTableColumn( column, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listGroupsFromRoleIdUI( UUID roleId ) {
        try {
            return ResponseUtils.success( roleManager.listGroupsFromRoleIdUI( getUserIdFromGeneralHeader() ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listGroupsFromRoleId( UUID roleId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( roleManager.listGroupsFromRoleId( getUserIdFromGeneralHeader(), roleId, filter ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getContextRouterForRoleGroups( UUID roleId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext( roleManager.getContextRouterForRoleGroups( filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Sets the role manager.
     *
     * @param roleManager
     *         the new role manager
     */
    public void setRoleManager( RoleManager roleManager ) {
        this.roleManager = roleManager;
    }

}
