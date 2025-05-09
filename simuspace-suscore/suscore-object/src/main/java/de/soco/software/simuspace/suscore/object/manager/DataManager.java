package de.soco.software.simuspace.suscore.object.manager;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.TransferResult;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.CurveUnitDTO;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.TransferObject;
import de.soco.software.simuspace.suscore.common.ui.WfFieldsUiDTO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.TranslationEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.DataObjectCurveDTO;
import de.soco.software.simuspace.suscore.data.model.WorkflowProjectDTO;
import de.soco.software.simuspace.suscore.object.model.ChangeStatusDTO;
import de.soco.software.simuspace.suscore.object.model.DeletedObjectDTO;
import de.soco.software.simuspace.workflow.model.Job;

/**
 * The interface Data manager.
 */
public interface DataManager {

    /**
     * Update work flow project workflow project dto.
     *
     * @param token
     *         the user token
     * @param workflowProjectJson
     *         the workflow project json
     *
     * @return the workflow project dto
     */
    WorkflowProjectDTO updateWorkFlowProject( String token, String workflowProjectJson );

    /**
     * Verify permissions and get delete object list list.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     *
     * @return the list
     */
    List< SuSEntity > verifyPermissionsAndGetDeleteObjectList( EntityManager entityManager, String userId, UUID objectId );

    /**
     * Gets object view manager.
     *
     * @return the object view manager
     */
    ObjectViewManager getObjectViewManager();

    /**
     * Gets deleted object list.
     *
     * @param filter
     *         the filter
     * @param userId
     *         the user id
     *
     * @return the deleted object list
     */
    FilteredResponse< DeletedObjectDTO > getDeletedObjectList( FiltersDTO filter, String userId );

    /**
     * Gets items from selection id.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the items from selection id
     */
    List< String > getItemsFromSelectionId( UUID selectionId );

    /**
     * Add file to an object object.
     *
     * @param jobId
     *         the job id
     * @param objectId
     *         the object id
     * @param docMap
     *         the doc map
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the object
     */
    Object addFileToAnObject( String jobId, String objectId, Map< String, DocumentDTO > docMap, String userIdFromGeneralHeader );

    /**
     * Restore object by selection.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     * @param mode
     *         the mode
     */
    void restoreObjectBySelection( String userIdFromGeneralHeader, String selectionId, String mode );

    /**
     * Verify permissions and get object ids for restore list.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param selectionId
     *         the selection id
     * @param mode
     *         the mode
     *
     * @return the list
     */
    List< UUID > verifyPermissionsAndGetObjectIdsForRestore( EntityManager entityManager, String userId, String selectionId, String mode );

    /**
     * Gets context router.
     *
     * @param filter
     *         the filter
     * @param clazz
     *         the clazz
     *
     * @return the context router
     */
    List< ContextMenuItem > getContextRouter( FiltersDTO filter, Class< ? > clazz );

    /**
     * Restore deleted objects boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param job
     *         the job
     *
     * @return the boolean
     */
    boolean restoreDeletedObjects( EntityManager entityManager, String userId, UUID objectId, Job job );

    /**
     * Change status object boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the object id
     * @param changeStatusDTO
     *         the change status dto
     *
     * @return the boolean
     */
    boolean changeStatusObject( EntityManager entityManager, String userIdFromGeneralHeader, UUID objectId,
            ChangeStatusDTO changeStatusDTO );

    /**
     * Check user read permission boolean.
     *
     * @param userId
     *         the user id
     * @param id
     *         the id
     *
     * @return the boolean
     */
    boolean checkUserReadPermission( String userId, String id );

    /**
     * Check user delete permission boolean.
     *
     * @param userId
     *         the user id
     * @param id
     *         the id
     *
     * @return the boolean
     */
    boolean checkUserDeletePermission( String userId, String id );

    /**
     * Stop job execution boolean.
     *
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param jobId
     *         the job id
     * @param mode
     *         the mode
     *
     * @return the boolean
     */
    boolean stopJobExecution( String userId, String userName, String jobId, String mode );

    /**
     * Pause job execution.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param userNameFromGeneralHeader
     *         the user name from general header
     * @param jobId
     *         the job id
     */
    void pauseJobExecution( String userIdFromGeneralHeader, String userNameFromGeneralHeader, String jobId );

    /**
     * Resume job execution.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param userNameFromGeneralHeader
     *         the user name from general header
     * @param jobId
     *         the job id
     */
    void resumeJobExecution( String userIdFromGeneralHeader, String userNameFromGeneralHeader, String jobId );

    /**
     * Stop job execution boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param jobId
     *         the job id
     * @param mode
     *         the mode
     *
     * @return the boolean
     */
    boolean stopJobExecution( EntityManager entityManager, String userId, String userName, String jobId, String mode );

    /**
     * Verify permissions and get delete object list bulk list.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param selectionId
     *         the selection id
     *
     * @return the list
     */
    List< SuSEntity > verifyPermissionsAndGetDeleteObjectListBulk( EntityManager entityManager, String userId, String selectionId );

    /**
     * Delete object boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param susEntity
     *         the sus entity
     *
     * @return the boolean
     */
    boolean deleteObject( EntityManager entityManager, String userId, SuSEntity susEntity );

    /**
     * Transfer object transfer result.
     *
     * @param userId
     *         the user id
     * @param transferObject
     *         the transfer object
     *
     * @return the transfer result
     */
    TransferResult transferObject( String userId, TransferObject transferObject );

    /**
     * Transfer object transfer result.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param transferObject
     *         the transfer object
     *
     * @return the transfer result
     */
    TransferResult transferObject( EntityManager entityManager, String userId, TransferObject transferObject );

    /**
     * Export object ui map.
     *
     * @return the map
     */
    UIForm exportObjectUI();

    /**
     * Gets curve x units.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     *
     * @return the curve x units
     */
    List< SelectOptionsUI > getCurveXUnits( EntityManager entityManager, String objectId );

    /**
     * Gets curve y units.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     *
     * @return the curve y units
     */
    List< SelectOptionsUI > getCurveYUnits( EntityManager entityManager, String objectId );

    /**
     * Create generic dto from object entity generic dto.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     * @param susEntity
     *         the sus entity
     * @param translationEntities
     *         the translation entities
     * @param forList
     *         the for list
     *
     * @return the generic dto
     */
    GenericDTO createGenericDTOFromObjectEntity( EntityManager entityManager, String userId, UUID projectId, SuSEntity susEntity,
            List< TranslationEntity > translationEntities, boolean forList );

    /**
     * Create generic dto from object entity generic dto.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     * @param susEntity
     *         the sus entity
     * @param translationEntities
     *         the translation entities
     *
     * @return the generic dto
     */
    GenericDTO createGenericDTOFromObjectEntity( EntityManager entityManager, String userId, UUID projectId, SuSEntity susEntity,
            List< TranslationEntity > translationEntities );

    /**
     * Gets data object curve by selection ids.
     *
     * @param selectionId
     *         the selection id
     * @param curveUnitDTO
     *         the curve unit dto
     *
     * @return the data object curve by selection ids
     *
     * @throws IOException
     *         the io exception
     */
    List< DataObjectCurveDTO > getDataObjectCurveBySelectionIds( String selectionId, CurveUnitDTO curveUnitDTO ) throws IOException;

    /**
     * Gets curve x units by selection.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the curve x units by selection
     */
    Object getCurveXUnitsBySelection( String selectionId );

    /**
     * Gets curve y units by selection.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the curve y units by selection
     */
    Object getCurveYUnitsBySelection( String selectionId );

    /**
     * Gets hpc list by each user.
     *
     * @param userUID
     *         the user uid
     * @param solver
     *         the solver
     *
     * @return the hpc list by each user
     */
    List< WfFieldsUiDTO > getHPCListByEachUser( String userUID, String solver );

    /**
     * Delete index.
     *
     * @param entity
     *         the entity
     */
    void deleteIndex( SuSEntity entity );

    /**
     * Gets sus dao.
     *
     * @return the sus dao
     */
    SuSGenericObjectDAO< SuSEntity > getSusDAO();

    /**
     * Download document response.
     *
     * @param objectId
     *         the object id
     * @param version
     *         the version
     * @param token
     *         the token
     *
     * @return the response
     */
    Response downloadDocument( UUID objectId, int version, String token );

}
