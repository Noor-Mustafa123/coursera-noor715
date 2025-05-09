package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class SystemEntity is base class for all systems Entities without version.
 *
 * @author Nosheen.Sharif
 */
@Getter
@Setter
@Entity
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
@org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
public abstract class SystemEntity implements Serializable {

    /**
     * Constant serialVersionUID
     */
    @Serial
    private static final long serialVersionUID = -1630526524546214676L;

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
     * modification date of object
     */
    @Column( name = "modified_on" )
    private Date modifiedOn;

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
     * owner mapping
     */
    @OneToOne
    private UserEntity createdBy;

    /**
     * The modified by.
     */
    @OneToOne
    private UserEntity modifiedBy;

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
        result = prime * result + ( ( createdBy == null ) ? 0 : createdBy.hashCode() );
        result = prime * result + ( ( createdOn == null ) ? 0 : createdOn.hashCode() );
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        result = prime * result + ( isDelete ? 1231 : 1237 );
        result = prime * result + ( ( modifiedBy == null ) ? 0 : modifiedBy.hashCode() );
        result = prime * result + ( ( modifiedOn == null ) ? 0 : modifiedOn.hashCode() );
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
        SystemEntity other = ( SystemEntity ) obj;
        if ( createdBy == null ) {
            if ( other.createdBy != null ) {
                return false;
            }
        } else if ( !createdBy.equals( other.createdBy ) ) {
            return false;
        }
        if ( createdOn == null ) {
            if ( other.createdOn != null ) {
                return false;
            }
        } else if ( !createdOn.equals( other.createdOn ) ) {
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
        if ( modifiedBy == null ) {
            if ( other.modifiedBy != null ) {
                return false;
            }
        } else if ( !modifiedBy.equals( other.modifiedBy ) ) {
            return false;
        }
        if ( modifiedOn == null ) {
            if ( other.modifiedOn != null ) {
                return false;
            }
        } else if ( !modifiedOn.equals( other.modifiedOn ) ) {
            return false;
        }
        return true;
    }

}
