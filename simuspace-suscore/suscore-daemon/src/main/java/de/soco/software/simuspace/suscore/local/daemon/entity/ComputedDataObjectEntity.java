/*
 *
 */

package de.soco.software.simuspace.suscore.local.daemon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class ComputedDataObjectEntity for sqllite database to map entity.
 *
 * @author noman
 */

@Entity
@Table( name = "hash" )
@JsonIgnoreProperties( ignoreUnknown = true )
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
     * The dir.
     */
    @Column( name = "isExistOnServer" )
    private boolean isExistOnServer;

    /**
     * The is type updated.
     */
    @Column( name = "isTypeUpdated" )
    private boolean isTypeUpdated = false;

    /**
     * The size.
     */
    @Column
    private Long size;

    /**
     * Instantiates a new computed data object entity.
     */
    public ComputedDataObjectEntity() {
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

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

    public ComputedDataObjectEntity( String id, String name, String container, String checksum, Date modifiedOn, String path, String type,
            boolean dir, Long size, boolean isExistOnServer ) {
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
        this.isExistOnServer = isExistOnServer;
    }

    /**
     * Instantiates a new computed data object entity.
     *
     * @return the path
     */

    public String getPath() {
        return path;
    }

    /**
     * Sets the path.
     *
     * @param path
     *         the new path
     */
    public void setPath( String path ) {
        this.path = path;
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
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets the container.
     *
     * @return the container
     */
    public String getContainer() {
        return container;
    }

    /**
     * Sets the container.
     *
     * @param container
     *         the new container
     */
    public void setContainer( String container ) {
        this.container = container;
    }

    /**
     * Gets the modified on.
     *
     * @return the modified on
     */
    public Date getModifiedOn() {
        return modifiedOn;
    }

    /**
     * Sets the modified on.
     *
     * @param modifiedOn
     *         the new modified on
     */
    public void setModifiedOn( Date modifiedOn ) {
        this.modifiedOn = modifiedOn;
    }

    /**
     * Gets the checksum.
     *
     * @return the checksum
     */
    public String getChecksum() {
        return checksum;
    }

    /**
     * Sets the checksum.
     *
     * @param checksum
     *         the new checksum
     */
    public void setChecksum( String checksum ) {
        this.checksum = checksum;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
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

    /**
     * Gets the size.
     *
     * @return the size
     */
    public Long getSize() {
        return size;
    }

    /**
     * Sets the size.
     *
     * @param size
     *         the new size
     */
    public void setSize( Long size ) {
        this.size = size;
    }

    /**
     * Checks if is type updated.
     *
     * @return true, if is type updated
     */
    public boolean isTypeUpdated() {
        return isTypeUpdated;
    }

    /**
     * Sets the type updated.
     *
     * @param isTypeUpdated
     *         the new type updated
     */
    public void setTypeUpdated( boolean isTypeUpdated ) {
        this.isTypeUpdated = isTypeUpdated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ComputedDataObjectEntity [id=" + id + ", name=" + name + ", container=" + container + ", path=" + path + ", checksum="
                + checksum + ", modifiedOn=" + modifiedOn + "]";
    }

    public boolean isExistOnServer() {
        return isExistOnServer;
    }

    public void setExistOnServer( boolean isExistOnServer ) {
        this.isExistOnServer = isExistOnServer;
    }

}