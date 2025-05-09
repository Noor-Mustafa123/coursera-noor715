package de.soco.software.simuspace.suscore.data.manager.base;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;

/**
 * An interface for holding business logic for audit logs and CRUD operations.
 *
 * @author Zeeshan jamal
 */
public interface AuditLogManager {

    /**
     * A method for searching an audit log
     *
     * @param userId
     *         the userId
     * @param filtersDTO
     *         the filtersDTO
     *
     * @return list filtered response
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< AuditLogDTO > searchAuditLog( String userId, FiltersDTO filtersDTO );

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    ObjectViewManager getObjectViewManager();

    /**
     * Gets the log list by data object.
     *
     * @param objectId
     *         the object id
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the log list by data object
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< AuditLogDTO > getLogListByDataObject( UUID objectId, FiltersDTO filtersDTO );

    /**
     * Gets the log list by data object and version id.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the log list by data object and version id
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< AuditLogDTO > getLogListByObjectAndVersionId( UUID objectId, int versionId, FiltersDTO filtersDTO );

    /**
     * Gets all values for audit log table column.
     *
     * @param columnName
     *         the column name
     * @param token
     *         the token
     *
     * @return the all values for audit log table column
     */
    List< Object > getAllValuesForAuditLogTableColumn( String columnName, String token );

}
