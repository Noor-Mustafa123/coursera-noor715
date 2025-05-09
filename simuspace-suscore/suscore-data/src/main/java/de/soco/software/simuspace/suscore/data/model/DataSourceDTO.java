package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.data.entity.DataDashboardEntity;
import de.soco.software.simuspace.suscore.data.entity.DataSourceEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;

/**
 * The type Data source settings dto.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties( ignoreUnknown = true )
public abstract class DataSourceDTO implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 3218361285738515341L;

    /**
     * The Id.
     */
    private String id;

    /**
     * The Version id.
     */
    private Integer versionId;

    /**
     * The Name.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "sourceName", title = "3000245x4", orderNum = 10, section = "Data Source Settings" )
    private String sourceName;

    /**
     * The Data source.
     */
    @UIFormField( title = "3000246x4", type = "select", name = "sourceType", orderNum = 11, section = "Data Source Settings" )
    private String sourceType;

    /**
     * The Created by.
     */
    private UserDTO createdBy;

    /**
     * The Modified by.
     */
    private UserDTO modifiedBy;

    /**
     * The Created on.
     */
    private Date createdOn;

    /**
     * The Modified on.
     */
    private Date modifiedOn;

    /**
     * The Dashboard.
     */
    private DataDashboardDTO dashboard;

    /**
     * The Is delete.
     */
    private boolean isDelete;

    /**
     * The Deleted on.
     */
    private Date deletedOn;

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
    public DataSourceEntity prepareEntity( DataDashboardEntity dashboardEntity, UserEntity userEntity ) {
        var entity = new DataSourceEntity();
        prepareEntity( dashboardEntity, userEntity, entity );
        return entity;
    }

    /**
     * Prepare entity.
     *
     * @param dashboardEntity
     *         the dashboard entity
     * @param userEntity
     *         the user entity
     * @param entity
     *         the entity
     */
    public void prepareEntity( DataDashboardEntity dashboardEntity, UserEntity userEntity, DataSourceEntity entity ) {
        entity.setDashboard( dashboardEntity );
        entity.setCreatedOn( new Date() );
        entity.setModifiedOn( new Date() );
        entity.setModifiedBy( userEntity );
        entity.setCreatedBy( userEntity );
        entity.setName( getSourceName() );
        entity.setSourceType( getSourceType() );
    }

}