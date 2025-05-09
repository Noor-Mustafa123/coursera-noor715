package de.soco.software.simuspace.suscore.permissions.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.AclObjectIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * This interface stores each and every domain object instance in the system. Each object must have an owner and the owner’s SID (user,
 * group or role)
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public interface AclObjectIdentityDAO extends GenericDAO< AclObjectIdentityEntity > {

    /**
     * Gets the acl object identity by class entity id.
     *
     * @param id
     *         the id
     *
     * @return the acl object identity by class entity id
     */
    AclObjectIdentityEntity getAclObjectIdentityByClassEntityId( EntityManager entityManager, UUID id );

    /**
     * Gets the acl object identity by class entity id and by security identity entity id.
     *
     * @param entityManager
     *         the entity manager
     * @param classId
     *         the class id
     * @param sidId
     *         the sid id
     *
     * @return the acl object identity by class entity id and by security identity entity id
     */
    AclObjectIdentityEntity getAclObjectIdentityByClassEntityIdAndBySecurityIdentityEntityId( EntityManager entityManager, UUID classId,
            UUID sidId );

    List< AclObjectIdentityEntity > getAclObjectIdentitiesByClassId( EntityManager entityManager, UUID classId );

    /**
     * Gets the child acl object identities by id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the child acl object identities by id
     */
    List< AclObjectIdentityEntity > getChildAclObjectIdentitiesById( EntityManager entityManager, UUID id );

    /**
     * Gets All child of acl tree from parent node..
     *
     * @param entityManager
     *         the entity manager
     * @param parendId
     *         the parend id
     *
     * @return the child acl object identities by id
     */
    List< AclObjectIdentityEntity > getAclObjectChildTreeFromParentId( EntityManager entityManager, UUID parendId );

    /**
     * Gets the acl object entity I ds by inherit false and null final permission.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the acl object entity I ds by inherit false and null final permission
     */
    List< AclObjectIdentityEntity > getAclObjectEntityIDsByInheritFalseAndNullFinalPermission( EntityManager entityManager );

    AclObjectIdentityEntity getAclObjectIdentityEntityById( EntityManager entityManager, UUID id );

}
