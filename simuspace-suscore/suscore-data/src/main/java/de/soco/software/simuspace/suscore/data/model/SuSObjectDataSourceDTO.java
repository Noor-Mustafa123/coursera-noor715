package de.soco.software.simuspace.suscore.data.model;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.DATA_SOURCE_FIELDS;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import de.soco.software.simuspace.suscore.common.enums.DashboardEnums;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.data.entity.DataDashboardEntity;
import de.soco.software.simuspace.suscore.data.entity.DataSourceEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSObjectDataSourceEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The type SuS object data source dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode( callSuper = true )
@JsonIgnoreProperties( ignoreUnknown = true )
public class SuSObjectDataSourceDTO extends DataSourceDTO {

    /**
     * The File selection.
     */
    @UIFormField( name = DATA_SOURCE_FIELDS.PROJECT_SELECTION, title = "3000373x4", type = "object", orderNum = 20, section = "Data Source Settings" )
    private String projectSelection;

    /**
     * The Update interval.
     */
    @UIFormField( name = DATA_SOURCE_FIELDS.UPDATE_INTERVAL, title = "3000374x4", type = "integer", orderNum = 21, section = "Data Source Settings" )
    private String updateInterval;

    /**
     * The Traversal depth.
     */
    @UIFormField( name = DATA_SOURCE_FIELDS.TRAVERSAL_DEPTH, title = "3000375x4", type = "integer", orderNum = 22, section = "Data Source Settings" )
    private String traversalDepth;

    /**
     * The Update cache.
     */
    @UIFormField( name = DATA_SOURCE_FIELDS.UPDATE_CACHE, title = "3000376x4", type = "button", orderNum = 23 )
    private String updateCache;

    @UIFormField( name = DATA_SOURCE_FIELDS.CACHE_UPDATED_AT, title = "3000379x4", orderNum = 24, readonly = true )
    private String cacheUpdatedAt = "N/A";

    /**
     * The Project id.
     */
    private String projectId;

    /**
     * Prepare entity data source entity.
     *
     * @param dashboardEntity
     *         the dashboard entity
     * @param userEntity
     *         the user entity
     *
     * @return the data source entity
     */
    @Override
    public DataSourceEntity prepareEntity( DataDashboardEntity dashboardEntity, UserEntity userEntity ) {
        if ( DashboardEnums.DashboardDataSourceOptions.SUS_OBJECT.getId().equalsIgnoreCase( getSourceType() ) ) {
            SuSObjectDataSourceEntity suSObjectDataSourceEntity = new SuSObjectDataSourceEntity();
            suSObjectDataSourceEntity.setCompositeId(
                    new VersionPrimaryKey( UUID.randomUUID(), dashboardEntity.getComposedId().getVersionId() ) );
            SelectionEntity selectionEntity = new SelectionEntity();
            selectionEntity.setId( UUID.fromString( getProjectSelection() ) );
            suSObjectDataSourceEntity.setSelectionEntity( selectionEntity );
            suSObjectDataSourceEntity.setUpdateInterval( getUpdateInterval() );
            suSObjectDataSourceEntity.setTraversalDepth( getTraversalDepth() );
            suSObjectDataSourceEntity.setCacheUpdatedAt( getCacheUpdatedAt() );
            super.prepareEntity( dashboardEntity, userEntity, suSObjectDataSourceEntity );
            return suSObjectDataSourceEntity;
        }
        return null;
    }

}
