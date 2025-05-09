/*
 *
 */

package de.soco.software.simuspace.suscore.common.entity;

import javax.persistence.Column;
import javax.persistence.Id;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class ComputedDataObjectEntity for sqllite database to map entity.
 *
 * @author noman
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ComputedDataObjectEntity {

    /**
     * The id.
     */
    @Id
    @Column( name = "id" )
    private String id;

    /**
     * The name.
     */
    @Column( name = "name" )
    private String name;

    /**
     * The container id.
     */
    @Column( name = "container" )
    private String container;

    /**
     * The path.
     */
    @Column( name = "path" )
    private String path;

    /**
     * The checksum.
     */
    @Column( name = "checksum" )
    private String checksum;

    /**
     * The modified on.
     */
    @Column( name = "modifiedOn" )
    private Date modifiedOn;

    /**
     * The type.
     */
    @Column( name = "type" )
    private String type;

    /**
     * The dir.
     */
    @Column( name = "isDir" )
    private boolean isDir;

    /**
     * The size.
     */
    @Column
    private Long size;

    /**
     * Instantiates a new computed data object entity.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param container
     *         the container
     * @param checksum
     *         the checksum
     * @param modifiedOn
     *         the modified on
     * @param path
     *         the path
     * @param type
     *         the type
     * @param dir
     *         the dir
     * @param size
     *         the size
     */
    public ComputedDataObjectEntity( String id, String name, String container, String checksum, Date modifiedOn, String path, String type,
            boolean dir, Long size ) {
        super();
        this.id = id;
        this.name = name;
        this.container = container;
        this.checksum = checksum;
        this.modifiedOn = modifiedOn;
        this.path = path;
        this.type = type;
        this.isDir = dir;
        this.size = size;
    }

    /**
     * Instantiates a new computed data object entity.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     */
    public ComputedDataObjectEntity( String id, String name ) {
        super();
        this.name = name;
        this.id = id;
    }

    /**
     * Instantiates a new computed data object entity.
     *
     * @param name
     *         the name
     * @param checksum
     *         the checksum
     * @param modifiedOn
     *         the modified on
     */
    public ComputedDataObjectEntity( String name, String checksum, Date modifiedOn ) {
        super();
        this.name = name;
        this.checksum = checksum;
        this.modifiedOn = modifiedOn;
    }

    /**
     * Instantiates a new computed data object entity.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param checksum
     *         the checksum
     * @param container
     *         the container
     * @param modifiedOn
     *         the modified on
     * @param path
     *         the path
     */
    public ComputedDataObjectEntity( String id, String name, String checksum, String container, Date modifiedOn, String path ) {
        super();
        this.id = id;
        this.name = name;
        this.checksum = checksum;
        this.modifiedOn = modifiedOn;
        this.container = container;
        this.path = path;
    }

    /**
     * Instantiates a new computed data object entity.
     *
     * @param name
     *         the name
     * @param checksum
     *         the checksum
     * @param lastModified
     *         the last modified
     * @param containerId
     *         the container id
     * @param path
     *         the path
     */
    public ComputedDataObjectEntity( String name, String checksum, Date lastModified, String containerId, String path ) {
        this.name = name;
        this.checksum = checksum;
        this.modifiedOn = lastModified;
        this.container = containerId;
        this.path = path;

    }

    /**
     * Checks if is dir.
     *
     * @return true, if is dir
     */
    public boolean isDir() {
        return isDir;
    }

    /**
     * Sets the dir.
     *
     * @param isDir
     *         the new dir
     */
    public void setDir( boolean isDir ) {
        this.isDir = isDir;
    }

}