package de.soco.software.simuspace.suscore.user.manager;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.model.SuSUserDirectoryDTO;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;

/**
 * The interface is responsible to provide blueprint for business logic and invoke to dao class functions to communicate with repository and
 * provide CRUD operations to it.
 *
 * @author M.Nasir.Farooq
 */
public interface DirectoryManager {

    /**
     * Creates the directory.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param model
     *         the model
     *
     * @return the su S user directory model
     *
     * @apiNote To be used in service calls only
     */
    SuSUserDirectoryDTO createDirectory( String userIdFromGeneralHeader, SuSUserDirectoryDTO model );

    /**
     * Update directory.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param token
     *         the token
     * @param model
     *         the model
     *
     * @return the su S user directory model
     *
     * @apiNote To be used in service calls only
     */
    SuSUserDirectoryDTO updateDirectory( String userIdFromGeneralHeader, String token, SuSUserDirectoryDTO model );

    /**
     * Delete directory.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param directoryId
     *         the directory id
     *
     * @return the su S user directory model
     */
    boolean deleteDirectory( EntityManager entityManager, String userIdFromGeneralHeader, UUID directoryId );

    /**
     * Read directory.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param directoryId
     *         the directory id
     *
     * @return the su S user directory model
     *
     * @apiNote To be used in service calls only
     */
    SuSUserDirectoryDTO readDirectory( String userIdFromGeneralHeader, UUID directoryId );

    /**
     * Read directory.
     *
     * @param entityManager
     *         the entity manager
     * @param directoryId
     *         the directory id
     * @param withCredentials
     *         the set credentials
     *
     * @return the su S user directory model
     */
    SuSUserDirectoryDTO readDirectory( EntityManager entityManager, UUID directoryId, boolean withCredentials );

    /**
     * Read directory.
     *
     * @param entityManager
     *         the entity manager
     * @param directoryId
     *         the directory id
     *
     * @return the su S user directory entity
     */
    SuSUserDirectoryEntity readDirectory( EntityManager entityManager, UUID directoryId );

    /**
     * Gets the update directory UI.
     *
     * @param option
     *         the option
     *
     * @return the update directory UI
     */
    UIForm getUpdateDirectoryUI( String option );

    /**
     * Gets context menu.
     *
     * @param filter
     *         the filter
     *
     * @return List<ContextMenuItem> context menu
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getContextMenu( FiltersDTO filter );

    /**
     * Checks lda or ad connection
     *
     * @param susUserDirectoryDTO
     *         the sus user directory dto
     *
     * @return boolean boolean
     *
     * @apiNote To be used in service calls only
     */
    boolean testLdapADConnection( SuSUserDirectoryDTO susUserDirectoryDTO );

    /**
     * A method to prepare directory Entity from model
     *
     * @param susUserDirectory
     *         the sus user directory
     *
     * @return SuSUserDirectoryEntity su s user directory entity
     */
    SuSUserDirectoryEntity prepareDirectoryEntityFromModel( SuSUserDirectoryDTO susUserDirectory );

    /**
     * A method to prepare directory Entity from model
     *
     * @param susUserDirectory
     *         the sus user directory
     * @param entity
     *         the entity
     *
     * @return SuSUserDirectoryEntity su s user directory entity
     */
    SuSUserDirectoryEntity prepareDirectoryEntityFromModel( SuSUserDirectoryDTO susUserDirectory, SuSUserDirectoryEntity entity );

    /**
     * prepare directory model from entity
     *
     * @param entityManager
     *         the entity manager
     * @param directory
     *         the directory
     * @param withCredentials
     *         the set credentials
     *
     * @return SuSUserDirectoryDTO su s user directory dto
     */
    SuSUserDirectoryDTO prepareDirectoryModelFromEntity( EntityManager entityManager, SuSUserDirectoryEntity directory,
            boolean withCredentials );

    /**
     * prepare directory model from entity without user
     *
     * @param directory
     *         the directory
     * @param withCredentials
     *         the with credentials
     *
     * @return SuSUserDirectoryDTO sus user directory dto
     */
    SuSUserDirectoryDTO prepareDirectoryModelFromEntityWithoutUsers( SuSUserDirectoryEntity directory, boolean withCredentials );

    /**
     * Delete directory by selection
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     * @param mode
     *         the mode
     *
     * @return the boolean
     *
     * @apiNote To be used in service calls only
     */
    boolean deleteDirectoryBySelection( String userIdFromGeneralHeader, String selectionId, String mode );

    /**
     * Gets the directory list.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param filtersDTO
     *         the filtersDTO
     *
     * @return the directory list
     *
     * @apiNote To be used in service calls only
     */
    List< SuSUserDirectoryDTO > getDirectoryList( String userIdFromGeneralHeader, FiltersDTO filtersDTO );

    /**
     * create ui form
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return UIForm item list
     *
     * @apiNote To be used in service calls only
     */
    UIForm createForm( String userIdFromGeneralHeader );

    /**
     * a method to get all directories
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return list of SusUserDirectory
     *
     * @apiNote To be used in service calls only
     */
    List< SuSUserDirectoryDTO > getAllDirectories( String userIdFromGeneralHeader );

    /**
     * Gets the directories by type.
     *
     * @param userId
     *         the user id
     * @param dirType
     *         the dir type
     *
     * @return the directories by type
     */
    List< SuSUserDirectoryDTO > getDirectoriesByType( String userId, String dirType );

    /**
     * Creates the user directory form for edit.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm createUserDirectoryFormForEdit( String userIdFromGeneralHeader, UUID id );

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    ObjectViewManager getObjectViewManager();

    /**
     * Gets the directory UI.
     *
     * @return the directory UI
     */
    List< TableColumn > getDirectoryUI();

    /**
     * Prepare directory model without user.
     *
     * @param directoryEntity
     *         the directory entity
     *
     * @return the su S user directory DTO
     */
    SuSUserDirectoryDTO prepareDirectoryModel( SuSUserDirectoryEntity directoryEntity );

    /**
     * Prepare directory model from entity for user model dto su s user directory dto.
     *
     * @param directoryEntity
     *         the directory entity
     *
     * @return the su s user directory dto
     */
    SuSUserDirectoryDTO prepareDirectoryModelFromEntityForUserModelDTO( SuSUserDirectoryEntity directoryEntity );

    /**
     * Gets all values for directory table column.
     *
     * @param columnName
     *         the column name
     * @param token
     *         the token
     *
     * @return the all values for directory table column
     */
    List< Object > getAllValuesForDirectoryTableColumn( String columnName, String token );

    /**
     * Gets list of all oauthDirectores from the db.
     *
     * @return the list of all SuSUserDirectoryDTO
     */
    public List< SuSUserDirectoryDTO > getListOfAllOAuthUserDirectories();

}
