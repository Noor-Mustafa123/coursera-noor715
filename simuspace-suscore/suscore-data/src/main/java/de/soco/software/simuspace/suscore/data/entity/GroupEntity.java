package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.io.Serial;
import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;

/**
 * * Database Entity Mapping Class Groups for users.
 *
 * @author Nosheen.Sharif
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table( name = "groups" )
public class GroupEntity extends SystemEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -8883122483713349657L;

    /**
     * The name.
     */
    @Column( name = "name" )
    private String name;

    /**
     * The description.
     */
    @Column( name = "description" )
    private String description;

    /**
     * The status.
     */
    @Column( name = "status" )
    private Boolean status;

    /**
     * The selection id.
     */
    @Column( name = "Selection_id" )
    private String selectionId;

    /**
     * The users.
     */
    @ManyToMany( fetch = FetchType.LAZY )
    @JoinTable( name = "user_groups", joinColumns = { @JoinColumn( name = "group_Id" ) }, inverseJoinColumns = {
            @JoinColumn( name = "user_id", referencedColumnName = "id" ) } )
    private Set< UserEntity > users;

    /**
     * The security identity entity.
     */
    @OneToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    private AclSecurityIdentityEntity securityIdentityEntity;

    /**
     * Instantiates a new group entity.
     *
     * @param id
     *         the id
     */
    public GroupEntity( UUID id ) {
        this.setId( id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;

        int result = ConstantsInteger.INTEGER_VALUE_ONE;

        result = prime * result + ( ( getId() == null ) ? 0 : getId().hashCode() );
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
        GroupEntity other = ( GroupEntity ) obj;
        if ( getId() == null ) {
            if ( other.getId() != null ) {
                return false;
            }
        } else if ( !getId().equals( other.getId() ) ) {
            return false;
        }

        return true;
    }

}
