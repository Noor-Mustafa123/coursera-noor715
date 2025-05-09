package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

public class Location {

    /**
     * The id.
     */
    @UIFormField( name = "id", title = "3000021x4", isAsk = false, type = "hidden")
    @UIColumn( data = "id", name = "id", filter = "uuid", renderer = "html", title = "3000021x4", isShow = false )
    private UUID id;

    /**
     * The name.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    // @NotNull ( message = "3100003x4" )
    @UIFormField( name = "name", title = "3000032x4", isAsk = true, orderNum = 1 )
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000032x4", orderNum = 1, width = 0 )
    private String name;

    /**
     * The description.
     */
    @Max( value = 255, message = "3100002x4" )
    @UIFormField( name = "description", type = "textarea", title = "3000011x4", isAsk = true, orderNum = 2 )
    @UIColumn( data = "description", name = "description", filter = "text", renderer = "text", title = "3000011x4", orderNum = 2 )
    private String description;

    /**
     * The status.
     */
    @UIFormField( name = "status", title = "3000049x4", type = "select", orderNum = 4 )
    @UIColumn( data = "status", filter = "select", renderer = "text", title = "3000049x4", type = "select", name = "status", isSortable = false, filterOptions = {
            "Active", "Inactive" }, orderNum = 4 )
    private String status;

    /**
     * The type.
     */
    @UIFormField( name = "type", title = "3000051x4", type = "select", orderNum = 3 )
    @UIColumn( data = "type", filter = "text", renderer = "text", title = "3000051x4", type = "select", name = "type", orderNum = 3 )
    private String type;

    /**
     * The priority.
     */
    @UIFormField( name = "priority", title = "3000119x4", orderNum = 5 )
    @UIColumn( data = "priority", filter = "uuid", renderer = "html", title = "3000119x4", name = "priority", orderNum = 5 )
    private int priority;

    /**
     * The vault.
     */
    @UIFormField( name = "vault", title = "3000115x4", orderNum = 6 )
    @UIColumn( data = "vault", filter = "text", renderer = "text", title = "3000115x4", name = "vault", orderNum = 6 )
    private String vault;

    /**
     * The staging.
     */
    @UIFormField( name = "staging", title = "3000116x4", orderNum = 7 )
    @UIColumn( data = "staging", filter = "text", renderer = "text", title = "3000116x4", name = "staging", orderNum = 7 )
    private String staging;

    /**
     * The hpc.
     */
    @UIFormField( name = "url", title = "3000120x4", orderNum = 8 )
    @UIColumn( data = "url", filter = "text", renderer = "text", title = "3000120x4", name = "url", orderNum = 8 )
    private String url;

    /**
     * The is internal.
     */
    private boolean isInternal;

    /**
     * Instantiates a new location DTO.
     */
    public Location() {
    }

    /**
     * Instantiates a new location.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     */
    public Location( UUID id, String name ) {
        this.id = id;
        this.name = name;
    }

    /**
     * Instantiates a new location DTO.
     *
     * @param id
     *         the id
     */
    public Location( UUID id ) {
        this.id = id;
    }

    /**
     * Instantiates a new location DTO.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param vaultPath
     *         the vault path
     * @param stagingPath
     *         the staging path
     * @param url
     *         the url
     * @param authToken
     *         the auth token
     */
    public Location( UUID id, String name, String vaultPath, String stagingPath, String url ) {
        this.id = id;
        this.name = name;
        this.vault = vaultPath;
        this.staging = stagingPath;
        this.url = url;

    }

    /**
     * Instantiates a new location.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param description
     *         the description
     * @param status
     *         the status
     * @param type
     *         the type
     * @param priority
     *         the priority
     * @param vault
     *         the vault
     * @param staging
     *         the staging
     * @param url
     *         the url
     * @param authToken
     *         the auth token
     * @param isInternal
     *         the is internal
     */
    public Location( UUID id, String name, String description, String status, String type, int priority, String vault, String staging,
            String url, boolean isInternal ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
        this.priority = priority;
        this.vault = vault;
        this.staging = staging;
        this.url = url;
        this.isInternal = isInternal;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( UUID id ) {
        this.id = id;
    }

    /**
     * Gets the vault.
     *
     * @return the vault
     */
    public String getVault() {
        return vault;
    }

    /**
     * Sets the vault.
     *
     * @param vault
     *         the new vault
     */
    public void setVault( String vault ) {
        this.vault = vault;
    }

    /**
     * Gets the staging.
     *
     * @return the staging
     */
    public String getStaging() {
        return staging;
    }

    /**
     * Sets the staging.
     *
     * @param staging
     *         the new staging
     */
    public void setStaging( String staging ) {
        this.staging = staging;
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
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description
     *         the new description
     */
    public void setDescription( String description ) {
        this.description = description;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *         the new status
     */
    public void setStatus( String status ) {
        this.status = status;
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
     * Gets the priority.
     *
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the priority.
     *
     * @param priority
     *         the new priority
     */
    public void setPriority( int priority ) {
        this.priority = priority;
    }

    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url.
     *
     * @param url
     *         the new url
     */
    public void setUrl( String url ) {
        this.url = url;
    }

    /**
     * Checks if is internal.
     *
     * @return true, if is internal
     */
    public boolean isInternal() {
        return isInternal;
    }

    /**
     * Sets the internal.
     *
     * @param isInternal
     *         the new internal
     */
    public void setInternal( boolean isInternal ) {
        this.isInternal = isInternal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        Location other = ( Location ) obj;
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals( other.id ) ) {
            return false;
        }
        return true;
    }

}
