package de.soco.software.simuspace.suscore.common.entity;

import javax.persistence.Column;

import java.io.Serial;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * This entity lists all users in the system, “Security Id” (SID) is assigned to each user role or group. So SID may correspond to a granted
 * authority such as role.
 *
 * @author Ahsan Khan
 * @version 2.0
 */
@Getter
@Setter
public class AclSecurityIdentityEntity extends SystemEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -3109914693618461942L;

    /**
     * true means the SID is a user and false means SID is a granted authority (typically a role).
     */
    @Column( name = "principle" )
    private boolean principle;

    /**
     * SID stands for Security ID. It is assigned to each user, group or role. SID is attached with ACL to specify which actions can the
     * user with that SID perform on the desired objects.
     */
    @Type( type = "uuid-char" )
    @Column( name = "sid", nullable = false, unique = true )
    private UUID sid;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( principle ? 1231 : 1237 );
        result = prime * result + ( ( sid == null ) ? 0 : sid.hashCode() );
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
        AclSecurityIdentityEntity other = ( AclSecurityIdentityEntity ) obj;
        if ( principle != other.principle ) {
            return false;
        }
        if ( sid == null ) {
            if ( other.sid != null ) {
                return false;
            }
        } else if ( !sid.equals( other.sid ) ) {
            return false;
        }
        return true;
    }

}