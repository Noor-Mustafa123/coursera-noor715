package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serial;

import lombok.Getter;
import lombok.Setter;

/**
 * An entity class that would be mapped to the group related to roles and responsible for managing Group Roles.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
@Getter
@Setter
@Entity
@Table( name = "group_roles" )
public class GroupRoleEntity extends SystemEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -8912262381534252820L;

    /**
     * The user entity reference .
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "role_id", referencedColumnName = "id" )
    private RoleEntity roleEntity;

    /**
     * The user entity reference .
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "group_id", referencedColumnName = "id" )
    private GroupEntity groupEntity;

}