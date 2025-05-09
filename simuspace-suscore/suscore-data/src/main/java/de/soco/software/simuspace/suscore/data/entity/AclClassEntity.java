package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This entity allow us to uniquely identify any domain object class in the system.
 *
 * @author Ahsan Khan
 * @version 2.0
 */
@Getter
@Setter
@ToString
@Entity
@Table( name = "acl_class" )
public class AclClassEntity {

    /**
     * name for object .
     */
    @Column( name = "name", nullable = false )
    private String name;

    /**
     * Fully qualified class name for domain objects .
     */
    @Column( name = "qualified_name", nullable = false )
    private String qualifiedName;

    /**
     * The description.
     */
    @Column( name = "description" )
    private String description;

    /**
     * The Constant FIELD_NAME_MODIFIED_ON.
     */
    public static final String FIELD_NAME_MODIFIED_ON = "modifiedOn";

    /**
     * The primary key.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * Creation date of object
     */
    @Column( name = "created_on" )
    private Date createdOn;

    /**
     * isDeleted
     */
    @Column( name = "is_delete" )
    private boolean isDelete;

    /**
     * auditLogEntity mapping
     */
    @ManyToOne( cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private AuditLogEntity auditLogEntity;

    /**
     * @return the isDelete
     */
    public boolean isDelete() {
        return isDelete;
    }

    /**
     * @param isDelete
     *         the isDelete to set
     */
    public void setDelete( boolean isDelete ) {
        this.isDelete = isDelete;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( auditLogEntity == null ) ? 0 : auditLogEntity.hashCode() );
        result = prime * result + ( ( createdOn == null ) ? 0 : createdOn.hashCode() );
        result = prime * result + ( ( description == null ) ? 0 : description.hashCode() );
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        result = prime * result + ( isDelete ? 1231 : 1237 );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        result = prime * result + ( ( qualifiedName == null ) ? 0 : qualifiedName.hashCode() );
        return result;
    }

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
        AclClassEntity other = ( AclClassEntity ) obj;
        if ( auditLogEntity == null ) {
            if ( other.auditLogEntity != null ) {
                return false;
            }
        } else if ( !auditLogEntity.equals( other.auditLogEntity ) ) {
            return false;
        }
        if ( createdOn == null ) {
            if ( other.createdOn != null ) {
                return false;
            }
        } else if ( !createdOn.equals( other.createdOn ) ) {
            return false;
        }
        if ( description == null ) {
            if ( other.description != null ) {
                return false;
            }
        } else if ( !description.equals( other.description ) ) {
            return false;
        }
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals( other.id ) ) {
            return false;
        }
        if ( isDelete != other.isDelete ) {
            return false;
        }
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals( other.name ) ) {
            return false;
        }
        if ( qualifiedName == null ) {
            if ( other.qualifiedName != null ) {
                return false;
            }
        } else if ( !qualifiedName.equals( other.qualifiedName ) ) {
            return false;
        }
        return true;
    }

}