package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serial;

import lombok.Getter;
import lombok.Setter;

/**
 * This entity stores each and every domain object instance in the system. So there is a one-to-many relation between
 * {@link de.soco.software.simuspace.suscore.data.entity.AclClassEntity ClassEntity} and
 * {@link de.soco.software.simuspace.suscore.data.entity.AclObjectIdentityEntity ObjectIdentityEntity}. Each object must have an owner and
 * the owner’s SID (user, group or role)
 *
 * @author Ahsan Khan
 * @version 2.0
 */
@Getter
@Setter
@Entity
@Table( name = "acl_object_identity" )
public class AclObjectIdentityEntity extends SystemEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 8508114517239886020L;

    /**
     * The parent object.
     */
    @ManyToOne
    @JoinColumn( name = "parent_id", referencedColumnName = "id" )
    private AclObjectIdentityEntity parentObject;

    /**
     * The class entity.
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "acl_class_id", referencedColumnName = "id", nullable = false )
    private AclClassEntity classEntity;

    /**
     * The owner sid.
     */
    @ManyToOne
    @JoinColumn( name = "owner_sid", referencedColumnName = "id", nullable = false )
    private AclSecurityIdentityEntity ownerSid;

    /**
     * The inherit.
     */
    @Column( name = "entries_inheriting" )
    private boolean inherit;

    /**
     * The final parent object.
     */
    @ManyToOne
    @JoinColumn( name = "final_acl_permission", referencedColumnName = "id" )
    private AclObjectIdentityEntity finalParentObject;

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.data.entity.SystemEntity#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( classEntity == null ) ? 0 : classEntity.hashCode() );
        result = prime * result + ( inherit ? 1231 : 1237 );
        result = prime * result + ( ( ownerSid == null ) ? 0 : ownerSid.hashCode() );
        result = prime * result + ( ( parentObject == null ) ? 0 : parentObject.hashCode() );
        return result;
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.data.entity.SystemEntity#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( !super.equals( obj ) ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        AclObjectIdentityEntity other = ( AclObjectIdentityEntity ) obj;
        if ( classEntity == null ) {
            if ( other.classEntity != null ) {
                return false;
            }
        } else if ( !classEntity.equals( other.classEntity ) ) {
            return false;
        }
        if ( inherit != other.inherit ) {
            return false;
        }
        if ( ownerSid == null ) {
            if ( other.ownerSid != null ) {
                return false;
            }
        } else if ( !ownerSid.equals( other.ownerSid ) ) {
            return false;
        }
        if ( parentObject == null ) {
            if ( other.parentObject != null ) {
                return false;
            }
        } else if ( !parentObject.equals( other.parentObject ) ) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AclObjectIdentityEntity [parentObject=" + parentObject + ", classEntity=" + classEntity + ", ownerSid=" + ownerSid
                + ", inherit=" + inherit + "]";
    }

}