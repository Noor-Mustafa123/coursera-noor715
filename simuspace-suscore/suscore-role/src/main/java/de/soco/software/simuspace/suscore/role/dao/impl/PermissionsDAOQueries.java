package de.soco.software.simuspace.suscore.role.dao.impl;

/**
 * The Class will be responsible for all the database queries necessary for dealing with the Roles and Permissions
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public class PermissionsDAOQueries {

    /**
     * The Constant GET_ROLE_BY_ID.
     */
    public static final String GET_ROLE_BY_ID = "from RoleEntity r where r.roleUuid = :roleUuid and r.status='active'";

    /**
     * The GET_PERMISSIONS.
     */
    public static final String GET_PERMISSIONS = "select distinct p from AccessControlEntity p join p.roles r join r.userEntities ue where ue.uid=:uid";

    /**
     * The GET_ROLES.
     */
    public static final String GET_ROLES = "select r from RoleEntity r join r.userEntities ue where ue.uid=:uid";

    /**
     * The Constant GET_RESOURCE.
     */
    public static final String GET_RESOURCE = "select r from ResourceEntity r where r.uuid=:uuid";

    /**
     * The Constant GET_ACCESS_CONTROL_BY_RESOURCE.
     */
    public static final String GET_ACCESS_CONTROL_BY_RESOURCE = "from AccessControlEntity acl where acl.resource.uuid=:uuid";

    /**
     * DELETE_RESOURCE_PERMISSIONS_HQL query
     */
    public static final String DELETE_RESOURCE_PERMISSIONS_HQL = "DELETE from AccessControlEntity acl where acl.resource.uuid=:uuid";

    /**
     * The Constant GET_ROLE_BY_NAME.
     */
    public static final String GET_ROLE_BY_NAME = "from RoleEntity r where r.name = :name";

    /**
     * The Constant DELETE_ACCESS_CONTROL.
     */
    public static final String DELETE_ACCESS_CONTROL = "DELETE AccessControlEntity where accessControlUuid = :accessControlUuid";

}
