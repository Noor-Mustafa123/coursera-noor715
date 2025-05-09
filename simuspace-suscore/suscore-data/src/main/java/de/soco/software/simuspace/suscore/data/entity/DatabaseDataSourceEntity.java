package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The type Dashboard data source database.
 */
@Getter
@Setter
@ToString
@Entity
public class DatabaseDataSourceEntity extends DataSourceEntity {

    /**
     * The Url.
     */
    private String host;

    /**
     * The Port.
     */
    private Integer port;

    /**
     * The Db name.
     */
    private String dbName;

    /**
     * The User name.
     */
    private String userName;

    /**
     * The Password.
     */
    private String password;

    /**
     * The Database type.
     */
    private String databaseType;

}



