package de.soco.software.simuspace.suscore.data.manager.base;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;

/**
 * An interface responsible for managing context menu operations.
 *
 * @author M.Nasir.Farooq
 */
public interface ContextMenuManager {

    /**
     * Gets the filter by selection id.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selection id
     *
     * @return the filter by selection id
     */
    FiltersDTO getFilterBySelectionId( EntityManager entityManager, String selectionId );

    /**
     * Gets the context menu.
     *
     * @param puginName
     *         the pugin name
     * @param moduleDTOClazz
     *         the module DTO clazz
     * @param filter
     *         the filter
     *
     * @return the context menu
     */
    List< ContextMenuItem > getContextMenu( EntityManager entityManager, String puginName, Class< ? > moduleDTOClazz, FiltersDTO filter );

    /**
     * Save selection object.
     *
     * @param clazz
     *         the clazz
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the uuid
     */
    UUID saveSelectionFilter( EntityManager entityManager, Class< ? > clazz, FiltersDTO filtersDTO );

    /**
     * Gets the context menu.
     *
     * @param entityManager
     *         the entity manager
     * @param pluginName
     *         the pugin name
     * @param objectId
     *         the objectId
     * @param moduleDTOClazz
     *         the module DTO clazz
     * @param filter
     *         the filter
     *
     * @return the context menu
     */
    List< ContextMenuItem > getContextMenu( EntityManager entityManager, String pluginName, String objectId, Class< ? > moduleDTOClazz,
            FiltersDTO filter );

    /**
     * Gets the sync context menu.
     *
     * @param puginName
     *         the pugin name
     * @param projectId
     *         the project id
     * @param moduleDTOClazz
     *         the module DTO clazz
     * @param filter
     *         the filter
     * @param documentId
     *         the document id
     * @param token
     *         the token
     * @param size
     *         the size
     *
     * @return the sync context menu
     */
    List< ContextMenuItem > getSyncContextMenu( String puginName, String projectId, Class< ? > moduleDTOClazz, FiltersDTO filter,
            String documentId, String token, int size );

    /**
     * Gets the data context menu.
     *
     * @param entityManager
     *         the entity manager
     * @param pluginName
     *         the pugin name
     * @param projectId
     *         the project id
     * @param moduleDTOClazz
     *         the module DTO clazz
     * @param filter
     *         the filter
     * @param isContainer
     *         the is container
     * @param isObject
     *         the is object
     * @param containerId
     *         the container id
     * @param selectionId
     *         the selection id
     *
     * @return the data context menu
     */
    List< ContextMenuItem > getDataContextMenu( EntityManager entityManager, String pluginName, String projectId, Class< ? > moduleDTOClazz,
            FiltersDTO filter, boolean isContainer, boolean isObject, UUID containerId, String selectionId );

    /**
     * Gets the data remove link context menu.
     *
     * @param puginName
     *         the pugin name
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     *
     * @return the data remove link context menu
     */
    List< ContextMenuItem > getDataRemoveLinkContextMenu( String puginName, String projectId, String selectionId );

    /**
     * Gets the data context menu bulk delete.
     *
     * @param puginName
     *         the pugin name
     * @param projectId
     *         the project id
     * @param moduleDTOClazz
     *         the module DTO clazz
     * @param filter
     *         the filter
     * @param isObject
     *         the is object
     *
     * @return the data context menu bulk delete
     */
    List< ContextMenuItem > getDataContextMenuBulk( String puginName, String projectId, Class< ? > moduleDTOClazz, FiltersDTO filter,
            boolean isObject );

    /**
     * Gets the data context menu for report.
     *
     * @param puginName
     *         the pugin name
     * @param projectId
     *         the project id
     * @param moduleDTOClazz
     *         the module DTO clazz
     * @param filter
     *         the filter
     *
     * @return the data context menu bulk delete
     */
    List< ContextMenuItem > getDataContextMenuForReport( String puginName, String projectId, Class< ? > moduleDTOClazz, FiltersDTO filter );

    /**
     * Gets the scheme context menu.
     *
     * @param selectionId
     *         the selectionId
     * @param isBulk
     *         the is bulk
     *
     * @return the scheme context menu bulk delete
     */
    List< ContextMenuItem > getSchemeContextMenu( UUID selectionId, boolean isBulk );

    /**
     * Gets the scheme category context menu.
     *
     * @param selectionId
     *         the selectionId
     * @param isBulk
     *         the is bulk
     *
     * @return the category context menu bulk delete
     */
    List< ContextMenuItem > getSchemeCategoryContextMenu( UUID selectionId, boolean isBulk );

    /**
     * Gets the jobs data created context menu.
     *
     * @param selectionId
     *         the selectionId
     * @param isBulk
     *         the is bulk
     *
     * @return the category context menu bulk delete
     */
    List< ContextMenuItem > getJobsDataCreatedContextMenu( UUID selectionId, boolean isBulk );

    /**
     * Gets the workflow context menu.
     *
     * @param puginName
     *         the pugin name
     * @param projectId
     *         the project id
     * @param moduleDTOClazz
     *         the module DTO clazz
     * @param filter
     *         the filter
     *
     * @return the workflow context menu
     */
    List< ContextMenuItem > getWorkflowContextMenu( String puginName, String projectId, Class< ? > moduleDTOClazz, FiltersDTO filter );

    /**
     * Gets the sync container context menu.
     *
     * @param entityManager
     *         the entity manager
     * @param pluginName
     *         the plugin name
     * @param projectId
     *         the project id
     * @param moduleDTOClazz
     *         the module DTO clazz
     * @param filter
     *         the filter
     *
     * @return the sync container context menu
     */
    List< ContextMenuItem > getSyncContainerContextMenu( EntityManager entityManager, String pluginName, String projectId,
            Class< ? > moduleDTOClazz, FiltersDTO filter );

    /**
     * Gets the workflow context menu.
     *
     * @param filter
     *         the filter
     * @param isWorkFlow
     *         the is work flow
     *
     * @return the workflow context menu
     */
    List< ContextMenuItem > getWorkflowContextMenu( FiltersDTO filter, boolean isWorkFlow );

    /**
     * Gets the workflow context menu bulk.
     *
     * @param filter
     *         the filter
     *
     * @return the workflow context menu bulk
     */
    List< ContextMenuItem > getWorkflowContextMenuBulk( FiltersDTO filter );

    /**
     * Gets the Running job context menu.
     *
     * @param jobId
     *         the job id
     *
     * @return the Running job context menu
     */
    List< ContextMenuItem > getRunningJobContext( UUID jobId );

    /**
     * Gets the context scheme generate image menu.
     *
     * @param jobId
     *         the job id
     *
     * @return the context scheme ploting menu
     */
    List< ContextMenuItem > getContextGenerateImageMenu( String jobId );

    /**
     * Sets the context visibility to client only.
     *
     * @param context
     *         the context
     *
     * @return context visibility set to client only
     */
    List< ContextMenuItem > setContextVisibilityToClientOnly( List< ContextMenuItem > context );

    /**
     * Gets the parser table context.
     *
     * @param parserId
     *         the parser id
     * @param filter
     *         the filter
     *
     * @return the parser table context
     */
    List< ContextMenuItem > getParserTableContext( String parserId, FiltersDTO filter );

    /**
     * Gets the context job ploting and job log download.
     *
     * @param puginName
     *         the pugin name
     * @param moduleDTOClazz
     *         the module DTO clazz
     * @param filter
     *         the filter
     * @param token
     *         the token
     * @param jobTypeSchemeOrWf
     *         the job type scheme or wf
     *
     * @return the context job ploting and job log download
     */
    List< ContextMenuItem > getContextJobPlotingAndJobLogDownload( String puginName, Class< ? > moduleDTOClazz, FiltersDTO filter,
            String token, boolean jobTypeSchemeOrWf );

    /**
     * Gets the training algo context menu.
     *
     * @param selectionId
     *         the selection id
     * @param isBulk
     *         the is bulk
     *
     * @return the training algo context menu
     */
    List< ContextMenuItem > getTrainingAlgoContextMenu( UUID selectionId, boolean isBulk );

    /**
     * Adds the job pause option.
     *
     * @param contextMenuItem
     *         the context menu item
     * @param jobId
     *         the job id
     */
    void addJobPauseOption( List< ContextMenuItem > contextMenuItem, UUID jobId );

    /**
     * Adds the job resume option.
     *
     * @param contextMenuItem
     *         the context menu item
     * @param jobId
     *         the job id
     */
    void addJobResumeOption( List< ContextMenuItem > contextMenuItem, UUID jobId );

}