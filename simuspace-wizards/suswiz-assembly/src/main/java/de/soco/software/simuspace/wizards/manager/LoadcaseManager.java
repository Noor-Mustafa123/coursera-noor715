package de.soco.software.simuspace.wizards.manager;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.data.entity.DummyTypeEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.LoadCaseDTO;
import de.soco.software.simuspace.wizards.model.LoadcaseWizardDTO;
import de.soco.software.simuspace.wizards.model.VariantLoadcaseDTO;

/**
 * The Interface LoadcaseManager.
 */
public interface LoadcaseManager {

    /**
     * Gets the loadcase UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the loadcase UI
     */
    List< TableColumn > getLoadcaseUI( String userIdFromGeneralHeader );

    /**
     * Gets the loadcase data.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param filters
     *         the filters
     *
     * @return the loadcase data
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Object > getLoadcaseData( String userIdFromGeneralHeader, FiltersDTO filters );

    /**
     * Creates the loadcase UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the list
     */
    UIForm createLoadcaseUI( String userIdFromGeneralHeader );

    /**
     * Creates the loadcase.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param loadcaseWizardDTO
     *         the loadcase wizard DTO
     *
     * @return the loadcase wizard DTO
     *
     * @apiNote To be used in service calls only
     */
    LoadcaseWizardDTO createLoadcase( String userIdFromGeneralHeader, LoadcaseWizardDTO loadcaseWizardDTO );

    /**
     * Creates the loadcase.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param loadcaseWizardDTO
     *         the loadcase wizard DTO
     *
     * @return the loadcase wizard DTO
     */
    LoadcaseWizardDTO createLoadcase( EntityManager entityManager, String userIdFromGeneralHeader, LoadcaseWizardDTO loadcaseWizardDTO,
            DummyTypeEntity dummyTypeEntity );

    /**
     * Creates the loadcase DTO from entity.
     *
     * @param e
     *         the e
     *
     * @return the load case DTO
     */
    LoadCaseDTO createLoadcaseDTOFromEntity( SuSEntity e );

    /**
     * Gets the loadcase.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param loadCaseId
     *         the load case id
     *
     * @return the loadcase
     */
    LoadCaseDTO getLoadcase( EntityManager entityManager, String userIdFromGeneralHeader, UUID loadCaseId );

    /**
     * Gets the varaint loadcase.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param loadCaseId
     *         the load case id
     *
     * @return the varaint loadcase
     */
    VariantLoadcaseDTO getVaraintLoadcase( EntityManager entityManager, String userIdFromGeneralHeader, UUID loadCaseId );

    /**
     * Gets the loadcase context menu.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param filter
     *         the filter
     *
     * @return the loadcase context menu
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getLoadcaseContextMenu( String userIdFromGeneralHeader, FiltersDTO filter );

    /**
     * Edits the loadcase UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param loadcaseId
     *         the loadcase id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm editLoadcaseUI( String userIdFromGeneralHeader, String loadcaseId );

    /**
     * Update loadcase.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param loadcaseId
     *         the loadcase id
     * @param loadcaseWizardDTO
     *         the loadcase wizard DTO
     *
     * @return the loadcase wizard DTO
     *
     * @apiNote To be used in service calls only
     */
    LoadcaseWizardDTO updateLoadcase( String userIdFromGeneralHeader, String loadcaseId, LoadcaseWizardDTO loadcaseWizardDTO );

    /**
     * Delete loadcase.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param loadcaseId
     *         the loadcase id
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean deleteLoadcase( String userIdFromGeneralHeader, UUID loadcaseId );

    /**
     * Delete loadcase.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param loadcaseId
     *         the loadcase id
     *
     * @return true, if successful
     */
    boolean deleteLoadcase( EntityManager entityManager, String userIdFromGeneralHeader, UUID loadcaseId );

    /**
     * Delete loadcase in bulk.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     *
     * @return the int
     */
    int deleteLoadcaseInBulk( String userIdFromGeneralHeader, String selectionId );

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    ObjectViewManager getObjectViewManager();

    /**
     * Sets the object view manager.
     *
     * @param objectViewManager
     *         the new object view manager
     */
    void setObjectViewManager( ObjectViewManager objectViewManager );

}
