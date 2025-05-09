package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serial;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 * This entity stores individual permission to each recipient. In this entity we specify what action can be performed on each domain object
 * instance by the desired user, group or role.
 *
 * @author Ahsan.Khan
 * @version 2.0
 */
@Getter
@Setter
@Entity
@Table( name = "acl_entry" )
public class AclEntryEntity extends SystemEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 2270436998454585869L;

    /**
     * The object identity entity.
     */
    @ManyToOne
    @JoinColumn( name = "acl_object_identity_id", referencedColumnName = "id" )
    private AclObjectIdentityEntity objectIdentityEntity;

    /**
     * The security identity entity.
     */
    @ManyToOne
    @JoinColumn( name = "acl_sid_id", referencedColumnName = "id" )
    private AclSecurityIdentityEntity securityIdentityEntity;

    /**
     * The mask.
     */
    @Column( name = "mask" )
    private int mask;

    /**
     * Instantiates a new acl entry entity.
     */
    public AclEntryEntity() {
    }

    /**
     * Instantiates a new acl entry entity.
     *
     * @param objectIdentityEntity
     *         the object identity entity
     * @param securityIdentityEntity
     *         the security identity entity
     * @param mask
     *         the mask
     */
    public AclEntryEntity( AclObjectIdentityEntity objectIdentityEntity, AclSecurityIdentityEntity securityIdentityEntity, int mask ) {
        setId( UUID.randomUUID() );
        setDelete( false );
        this.objectIdentityEntity = objectIdentityEntity;
        this.securityIdentityEntity = securityIdentityEntity;
        this.mask = mask;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + mask;
        result = prime * result + ( ( objectIdentityEntity == null ) ? 0 : objectIdentityEntity.hashCode() );
        result = prime * result + ( ( securityIdentityEntity == null ) ? 0 : securityIdentityEntity.hashCode() );
        return result;
    }

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
        AclEntryEntity other = ( AclEntryEntity ) obj;
        if ( mask != other.mask ) {
            return false;
        }
        if ( objectIdentityEntity == null ) {
            if ( other.objectIdentityEntity != null ) {
                return false;
            }
        } else if ( !objectIdentityEntity.equals( other.objectIdentityEntity ) ) {
            return false;
        }
        if ( securityIdentityEntity == null ) {
            if ( other.securityIdentityEntity != null ) {
                return false;
            }
        } else if ( !securityIdentityEntity.equals( other.securityIdentityEntity ) ) {
            return false;
        }
        return true;
    }

}