package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 * An entity class that would be mapped to the roles and responsible for managing roles.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
@Getter
@Setter
@Entity
@Table( name = "roles" )
public class RoleEntity extends SystemEntity implements Serializable {

    /**
     * The serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The list of user Groups.
     */
    @OneToMany( fetch = FetchType.LAZY, mappedBy = "roleEntity" )
    private List< GroupRoleEntity > groups;

    /**
     * The description.
     */
    @Column( name = "description" )
    private String description;

    /**
     * The name.
     */
    @Column( name = "name" )
    private String name;

    /**
     * The status.
     */
    @Column( name = "status" )
    private Boolean status;

    /**
     * The selection id.
     */
    @Column( name = "selection_id" )
    private String selectionid;

    /**
     * The security identity entity.
     */
    @OneToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    private AclSecurityIdentityEntity securityIdentityEntity;

    /**
     * Instantiates a new role entity.
     */
    public RoleEntity() {
        super();
    }

    /**
     * Instantiates a new role entity.
     *
     * @param roleUuid
     *         the role uuid
     * @param createdOn
     *         the created on
     * @param description
     *         the description
     * @param name
     *         the name
     * @param status
     *         the status
     * @param updatedOn
     *         the updated on
     * @param groupRoleEntities
     *         the group role entities
     */
    public RoleEntity( UUID roleUuid, Date createdOn, String description, String name, Boolean status, Date updatedOn,
            List< GroupRoleEntity > groupRoleEntities ) {
        super();
        this.description = description;
        this.name = name;
        this.status = status;
        this.groups = groupRoleEntities;
    }

    /**
     * Checks if is status.
     *
     * @return true, if is status
     */
    public Boolean isStatus() {
        return status;
    }

}