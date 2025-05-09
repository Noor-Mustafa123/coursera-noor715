package de.soco.software.simuspace.suscore.license.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ModuleLicenseDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.license.manager.LicenseConfigurationManager;
import de.soco.software.simuspace.suscore.license.model.LicenseForm;
import de.soco.software.simuspace.suscore.license.model.UserLicensesDTO;
import de.soco.software.simuspace.suscore.license.service.rest.LicenseConfigurationService;
import de.soco.software.suscore.jsonschema.model.HistoryMap;

/**
 * The class responsible to provide implementation of license end points and responsible to communicate to business layer class
 * {@link LicenseConfigurationManager}.
 *
 * @author M.Nasir.Farooq
 */
public class LicenseConfigurationServiceImpl extends SuSBaseService implements LicenseConfigurationService {

    /**
     * The configuration manager.
     */
    private LicenseConfigurationManager configurationManager;

    /**
     * Adds the license.
     *
     * @param licenseJson
     *         the license json
     *
     * @return the response
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.license.service.rest.impl.LicenseConfigurationService#addLicense(java.lang.String)
     */
    @Override
    public Response addLicense( String licenseJson ) {
        try {
            LicenseForm licenseForm = JsonUtils.jsonToObject( licenseJson, LicenseForm.class );

            ModuleLicenseDTO license = JsonUtils.jsonToObject( CommonUtils.removeSlashes( licenseForm.getLicense() ),
                    ModuleLicenseDTO.class );

            ModuleLicenseDTO returnLicense = configurationManager.addLicense( getUserIdFromGeneralHeader(), license );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.LICENSE_GENERATED_SUCCESSFULLY.getKey() ),
                    returnLicense );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Update license.
     *
     * @param strLicense
     *         the str license
     *
     * @return the response
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.license.service.rest.LicenseConfigurationService#updateLicense(java.lang.String)
     */
    @Override
    public Response updateLicense( String strLicense ) {
        try {
            LicenseForm licenseForm = JsonUtils.jsonToObject( strLicense, LicenseForm.class );

            ModuleLicenseDTO license = JsonUtils.jsonToObject( CommonUtils.removeSlashes( licenseForm.getLicense() ),
                    ModuleLicenseDTO.class );
            ModuleLicenseDTO returnLicense = configurationManager.addLicense( getUserIdFromGeneralHeader(), license );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.LICENSE_UPDATED_SUCCESSFULLY.getKey() ),
                    returnLicense );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the module license list.
     *
     * @param filterJsonStr
     *         the filter json str
     *
     * @return the module license list
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.license.service.rest.LicenseConfigurationService#getModuleLicenseList()
     */
    @Override
    public Response getModuleLicenseList( String filterJsonStr ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJsonStr, FiltersDTO.class );

            final FilteredResponse< ModuleLicenseDTO > filteredList = configurationManager.getModuleLicenseList(
                    getUserIdFromGeneralHeader(), filter );

            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.LICENSE_MODULE.getKey() ) ), filteredList );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Delete module license.
     *
     * @param selectionId
     *         the selection id
     * @param mode
     *         the mode
     *
     * @return the response
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.license.service.rest.LicenseConfigurationService#deleteModuleLicense(java.lang.String)
     */
    @Override
    public Response deleteModuleLicense( String selectionId, String mode ) {
        try {
            configurationManager.deleteModuleLicenseBySelection( getUserIdFromGeneralHeader(), selectionId, mode );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.MODULE_LICENSE_IS_SUCCESSFULLY_DELETED.getKey() ),
                    true );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Checks if is feature allowed to user.
     *
     * @param feature
     *         the feature
     *
     * @return the response
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.license.service.rest.LicenseConfigurationService#isFeatureAllowedToUser(java.lang.String)
     */
    @Override
    public Response isFeatureAllowedToUser( String feature ) {
        try {
            return ResponseUtils.success( configurationManager.checkIsFeatureAllowedToUser( feature ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * List license UI.
     *
     * @return the response
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.license.service.rest.LicenseConfigurationService#listLicenseUI()
     */
    @Override
    public Response listLicenseUI() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.LICENSE_MODULE.getKey() ) ),
                    configurationManager.getLiceseUi( getUserIdFromGeneralHeader() ) );

        } catch ( final SusException e ) {
            return handleException( e );
        }

    }

    /**
     * Creates the license form.
     *
     * @return the response
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.license.service.rest.LicenseConfigurationService#createLicenseForm()
     */
    @Override
    public Response createLicenseForm() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.CREATE_LICENSE.getKey() ) ),
                    configurationManager.createLicenseForm( getUserIdFromGeneralHeader() ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * Creates the license form for edit.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.license.service.rest.LicenseConfigurationService#createLicenseFormForEdit(java.util.UUID)
     */
    @Override
    public Response createLicenseFormForEdit( UUID id ) {
        try {
            var columns = configurationManager.createLicenseFormForEdit( getUserIdFromGeneralHeader(), id );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.FORM_CREATED.getKey(),
                    MessageBundleFactory.getMessage( Messages.EDIT_LICENSE.getKey() ) ), columns );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the configuration manager.
     *
     * @return the configuration manager
     */
    public LicenseConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    /**
     * Sets the configuration manager.
     *
     * @param configurationManager
     *         the new configuration manager
     */
    public void setConfigurationManager( LicenseConfigurationManager configurationManager ) {
        this.configurationManager = configurationManager;
    }

    /**
     * Gets the module license list.
     *
     * @return the module license list
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.license.service.rest.LicenseConfigurationService#getModuleLicenseList()
     */
    @Override
    public Response getModuleLicenseList() {
        try {

            final List< ModuleLicenseDTO > filteredList = configurationManager.getModuleLicenseList();

            return ResponseUtils.success( filteredList );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the context router.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the context router
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.license.service.rest.LicenseConfigurationService#getContextRouter(java.lang.String)
     */
    @Override
    public Response getContextRouter( String filterJson ) {

        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            List< ContextMenuItem > contextMenuItems = configurationManager.getContextMenu( filter );

            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.CONTEXT_MENU_FETCHED.getKey() ), contextMenuItems );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response manageLicenseUI() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.MANAGE_LICENSE.getKey() ) ),
                    new TableUI( configurationManager.manageLicenseTableUI( getUserIdFromGeneralHeader() ),
                            configurationManager.getUserObjectViewsByKey( ConstantsObjectViewKey.MANAGE_LICENSE_TABLE_KEY,
                                    getUserIdFromGeneralHeader(), null ) ) );

        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllUserLicenses( String filterJsonStr ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJsonStr, FiltersDTO.class );

            FilteredResponse< UserLicensesDTO > filteredList = configurationManager.getAllUserLicenses( filter );

            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.MANAGE_LICENSE.getKey() ) ), filteredList );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response manageUserLicense( UUID licenseAssignedTo, String checkBoxStr ) {
        try {

            String licenseAssignedBy = getUserIdFromGeneralHeader();

            Map< String, Object > checkBoxStateMap = new HashMap<>();
            checkBoxStateMap = ( Map< String, Object > ) JsonUtils.jsonToMap( checkBoxStr, checkBoxStateMap );
            CheckBox checkBoxState = null;

            for ( Entry< String, Object > entry : checkBoxStateMap.entrySet() ) {
                checkBoxState = new CheckBox( null, entry.getKey(), Integer.valueOf( entry.getValue().toString() ) );
            }
            String licenseAssignedToName = configurationManager.getUserFromId( licenseAssignedTo.toString() ).getName();
            configurationManager.manageUserLicense( licenseAssignedBy, licenseAssignedTo, checkBoxState );

            configurationManager.updateDefaultViewForUserDataTree( licenseAssignedTo.toString() );

            if ( checkBoxState != null && checkBoxState.getValue() == ConstantsInteger.INTEGER_VALUE_ONE ) {

                return ResponseUtils.success(
                        MessageBundleFactory.getMessage( Messages.LICENSE_ASSIGN_TO_USER_SUCCESSFULLY.getKey(), licenseAssignedToName ),
                        true );
            } else {
                return ResponseUtils.success(
                        MessageBundleFactory.getMessage( Messages.LICENSE_UNASSIGN_FROM_USER_SUCCESSFULLY.getKey(), licenseAssignedToName ),
                        true );
            }

        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /**
     * Gets the all license views.
     *
     * @return the all license views
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.UserService#getAllLicenseViews()
     */
    @Override
    public Response getAllLicenseViews() {
        try {
            return ResponseUtils.success(
                    configurationManager.getUserObjectViewsByKey( ConstantsObjectViewKey.LICENSE_TABLE_KEY, getUserIdFromGeneralHeader(),
                            null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the license view.
     *
     * @param viewId
     *         the view id
     *
     * @return the license view
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#getLicenseView(java.lang.String)
     */
    @Override
    public Response getLicenseView( String viewId ) {
        try {
            return ResponseUtils.success( configurationManager.getObjectViewById( UUID.fromString( viewId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Save license view.
     *
     * @param viewJson
     *         the view json
     *
     * @return the response
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#saveLicenseView(java.lang.String)
     */
    @Override
    public Response saveLicenseView( String viewJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.LICENSE_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    configurationManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Sets the license view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#setLicenseViewAsDefault(java.lang.String)
     */
    @Override
    public Response setLicenseViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    configurationManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.LICENSE_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Delete license view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#deleteLicenseView(java.lang.String)
     */
    @Override
    public Response deleteLicenseView( String viewId ) {
        try {
            if ( configurationManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Update license view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#updateLicenseView(java.lang.String)
     */
    @Override
    public Response updateLicenseView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.LICENSE_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    configurationManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the all manage license views.
     *
     * @return the all manage license views
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.UserService#getAllManageLicenseViews()
     */
    @Override
    public Response getAllManageLicenseViews() {
        try {
            return ResponseUtils.success( configurationManager.getUserObjectViewsByKey( ConstantsObjectViewKey.MANAGE_LICENSE_TABLE_KEY,
                    getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the manage license view.
     *
     * @param viewId
     *         the view id
     *
     * @return the manage license view
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#getManageLicenseView(java.lang.String)
     */
    @Override
    public Response getManageLicenseView( String viewId ) {
        try {
            return ResponseUtils.success( configurationManager.getObjectViewById( UUID.fromString( viewId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Save manage license view.
     *
     * @param viewJson
     *         the view json
     *
     * @return the response
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#saveManageLicenseView(java.lang.String)
     */
    @Override
    public Response saveManageLicenseView( String viewJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
            objectViewDTO.setId( null );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.MANAGE_LICENSE_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    configurationManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Sets the manage license view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#setManageLicenseViewAsDefault(java.lang.String)
     */
    @Override
    public Response setManageLicenseViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    configurationManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.MANAGE_LICENSE_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Delete manage license view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#deleteLicenseView(java.lang.String)
     */
    @Override
    public Response deleteManageLicenseView( String viewId ) {
        try {
            if ( configurationManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Update manage license view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#updateManageLicenseView(java.lang.String)
     */
    @Override
    public Response updateManageLicenseView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.MANAGE_LICENSE_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    configurationManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the active users ui.
     *
     * @param viewId
     *         the view id
     *
     * @return the active users ui
     */
    @Override
    public Response getActiveUsersUi( String viewId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.ACTIVE_USERS.getKey() ) ),
                    configurationManager.getAllActiveUsersUI( getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the active users list.
     *
     * @param filterJsonStr
     *         the filter json str
     *
     * @return the active users list
     */
    @Override
    public Response getActiveUsersList( String filterJsonStr ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJsonStr, FiltersDTO.class );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.ACTIVE_USERS.getKey() ) ),
                    configurationManager.getAllActiveUsersList( getUserIdFromGeneralHeader(), filter ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the all active user context.
     *
     * @param json
     *         the json
     *
     * @return the all active user context
     */
    @Override
    public Response getAllActiveUserContext( String json ) {
        try {
            final FiltersDTO filterDto = JsonUtils.jsonToObject( json, FiltersDTO.class );
            return ResponseUtils.success( configurationManager.getActiveUserContext( getUserIdFromGeneralHeader(), filterDto ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Free licesnse.
     *
     * @param id
     *         the id
     * @param mode
     *         the mode
     *
     * @return the response
     */
    @Override
    public Response freeLicesnse( String id, String mode ) {
        try {
            return ResponseUtils.success(
                    configurationManager.freeLicenseByExpiringTokens( id, getUserIdFromGeneralHeader(), getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the license module options.
     *
     * @return the license module options
     */
    @Override
    public Response getLicenseModuleOptions() {
        try {
            return ResponseUtils.success( configurationManager.getLicenseModuleOptions( getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the licesnse history.
     *
     * @param json
     *         the json
     *
     * @return the licesnse history
     */
    @Override
    public Response getLicesnseHistory( String json ) {
        try {
            HistoryMap historyJsonObj = JsonUtils.jsonToObject( json, HistoryMap.class );
            return ResponseUtils.success( configurationManager.getLicesnseHistory( historyJsonObj, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the all active user views.
     *
     * @return the all active user views
     */
    @Override
    public Response getAllActiveUserViews() {
        try {
            return ResponseUtils.success(
                    configurationManager.getUserObjectViewsByKey( ConstantsObjectViewKey.LICENSE_TABLE_ACTIVE_USER_KEY,
                            getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the active user view.
     *
     * @param viewId
     *         the view id
     *
     * @return the active user view
     */
    @Override
    public Response getActiveUserView( String viewId ) {
        try {
            return ResponseUtils.success( configurationManager.getObjectViewById( UUID.fromString( viewId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Save active user view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @Override
    public Response saveActiveUserView( String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.LICENSE_TABLE_ACTIVE_USER_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    configurationManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Sets the active user view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @Override
    public Response setActiveUserViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    configurationManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.LICENSE_TABLE_ACTIVE_USER_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Delete active user view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @Override
    public Response deleteActiveUserView( String viewId ) {
        try {
            if ( configurationManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Update active user view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @Override
    public Response updateActiveUserView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.LICENSE_TABLE_ACTIVE_USER_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    configurationManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getAllValuesForLicenseModuleTableColumn( String column ) {
        try {
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.ALL_VALUES_FOR_COLUMN_RETURNED_SUCCESSFULLY.getKey(), column ),
                    configurationManager.getAllValuesForLicenseModuleTableColumn( column, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getAllValuesForLicenseUserTableColumn( String column ) {
        try {
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.ALL_VALUES_FOR_COLUMN_RETURNED_SUCCESSFULLY.getKey(), column ),
                    configurationManager.getAllValuesForLicenseUserTableColumn( column, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

}