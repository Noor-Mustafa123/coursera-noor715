/*
 *
 */

package de.soco.software.simuspace.suscore.license.manager;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.ModuleLicenseDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.UserTokenDTO;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.data.common.dao.LicenseConfigurationDAO;
import de.soco.software.simuspace.suscore.data.common.model.MonitorLicenseCurveDTO;
import de.soco.software.simuspace.suscore.data.entity.LicenseEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.license.model.UserLicensesDTO;
import de.soco.software.suscore.jsonschema.model.HistoryMap;

/**
 * The interface is responsible for providing license feature. Which will be consume able for services and for other managers if they are
 * coupled through dependency injection.
 *
 * @author M.Nasir.Farooq
 */
public interface LicenseConfigurationManager {

    /**
     * Adds the license.
     *
     * @param userId
     *         the user id
     * @param license
     *         the license
     *
     * @return the license
     *
     * @apiNote To be used in service calls only
     */
    ModuleLicenseDTO addLicense( String userId, ModuleLicenseDTO license );

    /**
     * Gets the module license.
     *
     * @param entityManager
     *         the entity manager
     * @param module
     *         the module
     *
     * @return the module license
     */
    ModuleLicenseDTO getModuleLicense( EntityManager entityManager, String module );

    /**
     * Gets the module license list.
     *
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     *
     * @return the module license list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< ModuleLicenseDTO > getModuleLicenseList( String userId, FiltersDTO filter );

    /**
     * Gets the modules by user id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the modules by user id
     */
    List< ModuleLicenseDTO > getModulesByUserId( EntityManager entityManager, String userId );

    /**
     * Delete module license by id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     * @param userId
     *         the user id
     */
    void deleteModuleLicenseById( EntityManager entityManager, UUID id, String userId );

    /**
     * Delete module license by selection.
     *
     * @param userId
     *         the user id
     * @param id
     *         the id
     * @param mode
     *         the mode
     *
     * @apiNote To be used in service calls only
     */
    void deleteModuleLicenseBySelection( String userId, String id, String mode );

    /**
     * Check if feature allowed to user boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param feature
     *         the feature
     * @param userId
     *         the user id
     *
     * @return the boolean
     */
    boolean checkIfFeatureAllowedToUser( EntityManager entityManager, String feature, String userId );

    /**
     * Checks if is user valid for module.
     *
     * @param entityManager
     *         the entity manager
     * @param module
     *         the module
     * @param userId
     *         the user id
     *
     * @return true, if is user valid for module
     */
    boolean isUserValidForModule( EntityManager entityManager, String module, String userId );

    /**
     * Check is feature allowed to user.
     *
     * @param feature
     *         the feature
     *
     * @return the string
     *
     * @apiNote To be used in service calls only
     */
    boolean checkIsFeatureAllowedToUser( String feature );

    /**
     * Gets the module license list.
     *
     * @return the module license list
     *
     * @apiNote To be used in service calls only
     */
    List< ModuleLicenseDTO > getModuleLicenseList();

    /**
     * Gets module license entity list.
     *
     * @return the module license entity list
     *
     * @apiNote To be used in service calls only
     */
    List< LicenseEntity > getModuleLicenseEntityList();

    /**
     * Gets the context router.
     *
     * @param filter
     *         the filter
     *
     * @return the context router
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getContextMenu( FiltersDTO filter );

    /**
     * Creates the license form for edit.
     *
     * @param userId
     *         the user id
     * @param id
     *         the id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm createLicenseFormForEdit( String userId, UUID id );

    /**
     * Manage license table UI.
     *
     * @param userId
     *         the user id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    List< TableColumn > manageLicenseTableUI( String userId );

    /**
     * Gets the all user licenses.
     *
     * @param filter
     *         the filter
     *
     * @return the all user licenses
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< UserLicensesDTO > getAllUserLicenses( FiltersDTO filter );

    /**
     * Manage user license.
     *
     * @param licenseAssignedBy
     *         the license assigned by
     * @param licenseAssignedTo
     *         the license assigned to
     * @param checkBox
     *         the check box
     *
     * @apiNote To be used in service calls only
     */
    void manageUserLicense( String licenseAssignedBy, UUID licenseAssignedTo, CheckBox checkBox );

    /**
     * Checks if is feature allowed to user.
     *
     * @param entityManager
     *         the entity manager
     * @param feature
     *         the feature
     * @param userId
     *         the user id
     *
     * @return true, if is feature allowed to user
     */
    boolean isFeatureAllowedToUser( EntityManager entityManager, String feature, String userId );

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    ObjectViewManager getObjectViewManager();

    /**
     * Creates the license form.
     *
     * @param userId
     *         the user id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm createLicenseForm( String userId );

    /**
     * Gets the user from id.
     *
     * @param userId
     *         the user id
     *
     * @return the user from id
     *
     * @apiNote To be used in service calls only
     */
    UserDTO getUserFromId( String userId );

    /**
     * Gets the allowed locations to user.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the allowed locations to user
     */
    int getAllowedLocationsToUser( EntityManager entityManager, String userId );

    /**
     * Checks if is token based license exists.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return true, if is token based license exists
     */
    boolean isTokenBasedLicenseExists( EntityManager entityManager );

    /**
     * Gets the license config dao.
     *
     * @return the license config dao
     */
    LicenseConfigurationDAO getLicenseConfigDao();

    /**
     * Gets the license user entity list by user id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the license user entity list by user id
     */
    List< ModuleLicenseDTO > getLicenseUserEntityListByUserId( EntityManager entityManager, String userId );

    /**
     * Check if feature allowed to user.
     *
     * @param entityManager
     *         the entity manager
     * @param feature
     *         the feature
     * @param userId
     *         the user id
     * @param operationUserId
     *         the operation user id
     *
     * @return true, if successful
     */
    boolean checkIfFeatureAllowedToUser( EntityManager entityManager, String feature, String userId, UUID operationUserId );

    /**
     * Update default view for user data tree.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @apiNote To be used in service calls only
     */
    void updateDefaultViewForUserDataTree( String userIdFromGeneralHeader );

    /**
     * Gets the monitor license manager.
     *
     * @return the monitor license manager
     */
    MonitorLicenseManager getMonitorLicenseManager();

    /**
     * Gets the licese ui.
     *
     * @param userId
     *         the user id
     *
     * @return the licese ui
     *
     * @apiNote To be used in service calls only
     */
    TableUI getLiceseUi( String userId );

    /**
     * Gets the all active users UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the all active users UI
     *
     * @apiNote To be used in service calls only
     */
    TableUI getAllActiveUsersUI( String userIdFromGeneralHeader );

    /**
     * Gets the all active users list.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param filter
     *         the filter
     *
     * @return the all active users list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< UserTokenDTO > getAllActiveUsersList( String userIdFromGeneralHeader, FiltersDTO filter );

    /**
     * Gets the active user context.
     *
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     *
     * @return the active user context
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getActiveUserContext( String userId, FiltersDTO filter );

    /**
     * Free license by expiring tokens.
     *
     * @param sid
     *         the sid
     * @param actionByUserId
     *         the action by user id
     * @param token
     *         the token
     *
     * @return true, if successful
     */
    boolean freeLicenseByExpiringTokens( String sid, String actionByUserId, String token );

    /**
     * Gets the licesnse history.
     *
     * @param json
     *         the json
     * @param userId
     *         the user id
     *
     * @return the licesnse history
     *
     * @apiNote To be used in service calls only
     */
    List< MonitorLicenseCurveDTO > getLicesnseHistory( HistoryMap json, String userId );

    /**
     * Gets the license module options.
     *
     * @param userId
     *         the user id
     *
     * @return the license module options
     *
     * @apiNote To be used in service calls only
     */
    List< SelectOptionsUI > getLicenseModuleOptions( String userId );

    /**
     * Delete object view.
     *
     * @param viewId
     *         the view id
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean deleteObjectView( UUID viewId );

    /**
     * Gets the object view by id.
     *
     * @param viewId
     *         the view id
     *
     * @return the object view by id
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO getObjectViewById( UUID viewId );

    /**
     * Gets the user object views by key.
     *
     * @param key
     *         the key
     * @param userId
     *         the user id
     * @param objectId
     *         the object id (set null if its not for any other object)
     *
     * @return the user object views by key
     *
     * @apiNote To be used in service calls only
     */
    List< ObjectViewDTO > getUserObjectViewsByKey( String key, String userId, String objectId );

    /**
     * Save default object view.
     *
     * @param viewId
     *         the view id
     * @param userId
     *         the user id
     * @param objectViewKey
     *         the object view key
     * @param objectId
     *         the object id (set null if its not for any other object)
     *
     * @return the object view DTO
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO saveDefaultObjectView( UUID viewId, String userId, String objectViewKey, String objectId );

    /**
     * Save or update object view.
     *
     * @param viewDTO
     *         the view DTO
     * @param userId
     *         the user id
     *
     * @return the object view DTO
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO saveOrUpdateObjectView( ObjectViewDTO viewDTO, String userId );

    /**
     * Gets the all active tokens.
     *
     * @return the all active tokens
     *
     * @apiNote To be used in service calls only
     */
    List< UserTokenDTO > getAllActiveTokens();

    /**
     * Gets all values for license module table column.
     *
     * @param columnName
     *         the column name
     * @param token
     *         the token
     *
     * @return the all values for license module table column
     */
    List< Object > getAllValuesForLicenseModuleTableColumn( String columnName, String token );

    /**
     * Gets all values for license user table column.
     *
     * @param columnName
     *         the column name
     * @param token
     *         the token
     *
     * @return the all values for license user table column
     */
    List< Object > getAllValuesForLicenseUserTableColumn( String columnName, String token );

}