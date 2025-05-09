package de.soco.software.simuspace.suscore.object.manager;

import java.util.List;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.data.common.model.AuditTrailDTO;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;

/**
 * The Interface AuditTrailManager.
 *
 * @author Noman Arshad
 */
public interface AuditTrailManager {

    /**
     * Gets the audit trail.
     *
     * @param objectId
     *         t he object id
     * @param userId
     *         the user id
     *
     * @return the audit trail
     *
     * @apiNote To be used in service calls only
     */
    AuditTrailDTO getAuditTrailByObjectIdOuts( String objectId, String userId );

    /**
     * Gets the audit trail table UI.
     *
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     *
     * @return the audit trail table UI
     *
     * @apiNote To be used in service calls only
     */
    TableUI getAuditTrailTableUI( String objectId, String userId );

    /**
     * Gets object view manager.
     *
     * @return the object view manager
     */
    ObjectViewManager getObjectViewManager();

    /**
     * Gets the data table context.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the object id
     * @param filter
     *         the filter
     * @param token
     *         the token
     *
     * @return the data table context
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getDataTableContext( String userIdFromGeneralHeader, String objectId, FiltersDTO filter, String token );

    /**
     * Gets the audit trail data by id list ins.
     *
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     *
     * @return the audit trail data by id list ins
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Object > getAuditTrailDataByIdListIns( String objectId, String userId, FiltersDTO filter );

    /**
     * Gets the audit trail data by id list outs.
     *
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     *
     * @return the audit trail data by id list outs
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Object > getAuditTrailDataByIdListOuts( String objectId, String userId, FiltersDTO filter );

    /**
     * Gets the audit trail by object id ins.
     *
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     *
     * @return the audit trail by object id ins
     *
     * @apiNote To be used in service calls only
     */
    AuditTrailDTO getAuditTrailByObjectIdIns( String objectId, String userId );

    /**
     * Gets the audit trail table UI.
     *
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     *
     * @return the audit trail table UI
     *
     * @apiNote To be used in service calls only
     */
    List< ObjectViewDTO > getAuditTrailTableUIView( String objectId, String userId );

    /**
     * Save or update object view.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param viewJson
     *         the view json
     * @param isUpdateable
     *         the is updateable
     *
     * @return the object view DTO
     */
    ObjectViewDTO saveObjectView( String userId, String objectId, String viewJson, boolean isUpdateable );

    /**
     * Save or update object view.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param viewJson
     *         the view json
     * @param isUpdateable
     *         the is updateable
     *
     * @return the object view DTO
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO saveOrUpdateObjectView( String userId, String objectId, String viewJson, boolean isUpdateable );

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
    boolean deleteObjectView( String viewId );

}
