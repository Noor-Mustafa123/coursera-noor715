package de.soco.software.simuspace.workflow.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.workflow.util.ImportWorkFlowFile;

/**
 * ImportWorkFlowDTO.
 */

@JsonIgnoreProperties( ignoreUnknown = true )
public class ImportWorkFlowDTO {

    /**
     * The parent id.
     */
    @UIFormField( name = "parentId", title = "3000041x4", type = "hidden" )
    @UIColumn( data = "parentId", name = "parentId", filter = "uuid", renderer = "hidden", title = "3000041x4", type = "hidden", isShow = false )
    private UUID parentId;

    /**
     * The id.
     */
    @UIFormField( name = "id", title = "3000021x4", isAsk = false, type = "hidden")
    @UIColumn( data = "id", name = "id", filter = "uuid", renderer = "text", title = "3000021x4", isShow = false, orderNum = 9 )
    private UUID id;

    /**
     * The name.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "name", title = "3000032x4" )
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000032x4", orderNum = 1, width = 0 )
    private String name;

    /**
     * The description.
     */
    @UIFormField( name = "description", title = "3000011x4", type = "textarea")
    @UIColumn( data = "description", name = "description", filter = "text", renderer = "text", title = "3000011x4", orderNum = 2 )
    private String description;

    /**
     * The file path in case of file run.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "file", title = "3000105x4" )
    @UIColumn( data = "file", name = "file", filter = "text", renderer = "os-file", title = "3000105x4", orderNum = 3 )
    private ImportWorkFlowFile file;

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
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
        final ImportWorkFlowDTO other = ( ImportWorkFlowDTO ) obj;
        if ( ( parentId == null ) && ( other.parentId != null ) ) {
            return false;

        }
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals( other.id ) ) {
            return false;
        }
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals( other.name ) ) {
            return false;
        }

        return true;
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
     * Gets the file.
     *
     * @return the file
     */
    public ImportWorkFlowFile getFile() {
        return file;
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
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the parent id.
     *
     * @return the parent id
     */
    public UUID getParentId() {
        return parentId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ( prime * result ) + ( ( parentId == null ) ? 0 : parentId.hashCode() );
        result = ( prime * result ) + ( ( description == null ) ? 0 : description.hashCode() );
        result = ( prime * result ) + ( ( id == null ) ? 0 : id.hashCode() );
        result = ( prime * result ) + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
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
     * Sets the file.
     *
     * @param file
     *         the new file
     */
    public void setFile( ImportWorkFlowFile file ) {
        this.file = file;
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
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Sets the parent id.
     *
     * @param parentId
     *         the new parent id
     */
    public void setParentId( UUID parentId ) {
        this.parentId = parentId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ImportWorkFlowDTO [parentId=" + parentId + ", id=" + id + ", name=" + name + ", description=" + description + ", file="
                + file + "]";
    }

}
