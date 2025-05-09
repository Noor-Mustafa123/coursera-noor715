package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.NotNull;

import java.io.Serial;
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
import de.soco.software.simuspace.suscore.data.entity.DatabaseDataSourceEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The type Dashboard data source dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode( callSuper = true )
@JsonIgnoreProperties( ignoreUnknown = true )
public class DatabaseDataSourceDTO extends DataSourceDTO {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 873269659543625540L;

    /**
     * The Preconfigured.
     */
    private boolean preconfigured;

    /**
     * The Type.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "databaseType", title = "3000235x4", type = "select", orderNum = 20, section = "Data Source Settings" )
    private String databaseType;

    /**
     * The Url.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "host", title = "3000231x4", orderNum = 21, section = "Database Connection Parameters" )
    private String host;

    /**
     * The Url.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "port", title = "3000236x4", type = "integer", orderNum = 22, section = "Database Connection Parameters" )
    private Integer port;

    /**
     * The Db name.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "dbName", title = "3000232x4", orderNum = 23, section = "Database Connection Parameters" )
    private String dbName;

    /**
     * The User name.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "userName", title = "3000233x4", orderNum = 24, section = "Database Connection Parameters" )
    private String userName;

    /**
     * The Password.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "password", title = "3000234x4", orderNum = 25, section = "Database Connection Parameters", type = "password")
    private String password;

    /**
     * The Test button.
     */
    @UIFormField( name = "testButton", title = "3000229x4", type = "button" )
    private String testButton;

    /**
     * The Preview button.
     */
    @UIFormField( name = "previewButton", title = "3000237x4", type = "button" )
    private String previewButton;

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
        if ( DashboardEnums.DashboardDataSourceOptions.DATABASE.getId().equalsIgnoreCase( getSourceType() ) ) {
            DatabaseDataSourceEntity dataSourceDatabase = new DatabaseDataSourceEntity();
            dataSourceDatabase.setCompositeId( new VersionPrimaryKey( UUID.randomUUID(), dashboardEntity.getComposedId().getVersionId() ) );
            dataSourceDatabase.setHost( getHost() );
            dataSourceDatabase.setPort( getPort() );
            dataSourceDatabase.setPassword( getPassword() );
            dataSourceDatabase.setDbName( getDbName() );
            dataSourceDatabase.setUserName( getUserName() );
            dataSourceDatabase.setDatabaseType( getDatabaseType() );
            super.prepareEntity( dashboardEntity, userEntity, dataSourceDatabase );
            return dataSourceDatabase;
        }
        return null;
    }

}