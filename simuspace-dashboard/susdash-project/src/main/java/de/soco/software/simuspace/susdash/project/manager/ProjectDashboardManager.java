package de.soco.software.simuspace.susdash.project.manager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.DataObjectDashboardEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.susdash.core.model.ProjectDashboardDTO;
import de.soco.software.simuspace.susdash.project.model.LifecycleTableDTO;
import de.soco.software.simuspace.susdash.project.model.ProjectStructureTableCell;

/**
 * The Interface ProjectDashboardManager.
 */
public interface ProjectDashboardManager {

    /**
     * Gets the project structure chart.
     *
     * @param objectId
     *         the object id
     * @param projectDashboardDTO
     *         the project dashboard DTO
     * @param tokenFromGeneralHeader
     *         the token from general header
     *
     * @return the project structure chart
     */
    Map< String, Object > getProjectStructureChart( String objectId, ProjectDashboardDTO projectDashboardDTO,
            String tokenFromGeneralHeader );

    /**
     * Gets materials project tree in table form.
     *
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     * @param tokenFromGeneralHeader
     *         the token from general header
     *
     * @return the materials project tree in table form
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Map< String, ProjectStructureTableCell > > getProjectStructureTable( String objectId, String userId,
            FiltersDTO filter, String tokenFromGeneralHeader );

    /**
     * Gets materials project table ui.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param tokenFromGeneralHeader
     *         the token from general header
     *
     * @return the materials project table ui
     *
     * @apiNote To be used in service calls only
     */
    TableUI getProjectStructureTableUI( String userId, String objectId, String tokenFromGeneralHeader );

    /**
     * Gets all project dashboards.
     *
     * @return the all project dashboards
     */
    List< DataObjectDashboardEntity > getAllProjectDashboards();

    /**
     * Gets dashboard by id.
     *
     * @param objectId
     *         the object id
     *
     * @return the dashboard by id
     */
    DataObjectDashboardEntity getDashboardById( String objectId );

    /**
     * Prepare object view object view dto.
     *
     * @param objectJson
     *         the object json
     * @param key
     *         the key
     * @param save
     *         the save
     *
     * @return the object view dto
     */
    ObjectViewDTO prepareObjectView( String objectJson, String key, boolean save );

    /**
     * Gets user materials views.
     *
     * @param userId
     *         the user id
     *
     * @return the user materials views
     *
     * @apiNote To be used in service calls only
     */
    List< ObjectViewDTO > getUserProjectStructureViews( String userId );

    /**
     * Save or update materials view object view dto.
     *
     * @param objectJson
     *         the object json
     * @param userId
     *         the user id
     * @param save
     *         the save
     *
     * @return the object view dto
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO saveOrUpdateProjectStructureView( String objectJson, String userId, boolean save );

    /**
     * Delete materials view boolean.
     *
     * @param viewId
     *         the view id
     *
     * @return the boolean
     *
     * @apiNote To be used in service calls only
     */
    boolean deleteProjectStructureView( String viewId );

    /**
     * Gets Project lifecycle table ui.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     *
     * @return the lifecycle table ui
     */
    TableUI getProjectLifecycleTableUI( String userId, String objectId );

    /**
     * Gets materials project tree in table form.
     *
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     * @param tokenFromGeneralHeader
     *         the token from general header
     *
     * @return the materials project tree in table form
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< LifecycleTableDTO > getProjectLifecycleTable( String objectId, String userId, FiltersDTO filter,
            String tokenFromGeneralHeader );

    /**
     * Gets user project lifecycle views.
     *
     * @param userId
     *         the user id
     *
     * @return the user project lifecycle views
     */
    List< ObjectViewDTO > getUserProjectLifecycleViews( String userId );

    /**
     * Save or update project lifecycle view object view dto.
     *
     * @param objectJson
     *         the object json
     * @param userId
     *         the user id
     * @param save
     *         the save
     *
     * @return the object view dto
     */
    ObjectViewDTO saveOrUpdateProjectLifecycleView( String objectJson, String userId, boolean save );

    /**
     * Delete project lifecycle view boolean.
     *
     * @param viewId
     *         the view id
     *
     * @return the boolean
     */
    boolean deleteProjectLifecycleView( String viewId );

    /**
     * Update dashboard cache.
     *
     * @param objectId
     *         the object id
     */
    void updateDashboardCache( String objectId );

    /**
     * Gets dashboard last modified.
     *
     * @param objectId
     *         the object id
     * @param tokenFromGeneralHeader
     *         the token from general header
     *
     * @return the dashboard last modified
     */
    Date getDashboardLastModified( String objectId, String tokenFromGeneralHeader );

    /**
     * Gets project lifecycle context.
     *
     * @param objectId
     *         the object id
     * @param filter
     *         the filter
     * @param token
     *         the token
     *
     * @return the project lifecycle context
     */
    List< ContextMenuItem > getProjectLifecycleContext( String objectId, FiltersDTO filter, String token );

    /**
     * Change project lifecycle boolean.
     *
     * @param objectId
     *         the object id
     * @param selectionId
     *         the selection id
     * @param statusId
     *         the status id
     * @param tokenFromGeneralHeader
     *         the token from general header
     *
     * @return the boolean
     */
    Boolean changeProjectLifecycle( String objectId, String selectionId, String statusId, String tokenFromGeneralHeader );

    /**
     * Gets list of parent names.
     *
     * @param entityManager
     *         the entity manager
     * @param suSEntity
     *         the su s entity
     * @param finalParentId
     *         the final parent id
     *
     * @return the list of parent names
     */
    List< String > getListOfParentNames( EntityManager entityManager, SuSEntity suSEntity, String finalParentId );

    /**
     * Gets sus dao.
     *
     * @return the sus dao
     */
    SuSGenericObjectDAO< SuSEntity > getSusDAO();

    /**
     * Gets selection manager.
     *
     * @return the selection manager
     */
    SelectionManager getSelectionManager();

    /**
     * Gets entity manager factory.
     *
     * @return the entity manager factory
     */
    EntityManagerFactory getEntityManagerFactory();

}
