package de.soco.software.simuspace.suscore.permissions.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.AclEntryEntity;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * This interface stores individual permission to each recipient. In this entity we specify what action can be performed on each domain
 * object instance by the desired user, group or role.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public interface AclEntryDAO extends GenericDAO< AclEntryEntity > {

    /**
     * Gets the acl entry entity by object id and mask and sid id.
     *
     * @param entityManager
     *         the entity manager
     * @param objectIdentityId
     *         the object identity id
     * @param sidId
     *         the sid id
     * @param mask
     *         the mask
     *
     * @return the acl entry entity by object id and mask and sid id
     */
    AclEntryEntity getAclEntryEntityByObjectIdAndMaskAndSidId( EntityManager entityManager, UUID objectIdentityId, UUID sidId, int mask );

    /**
     * Gets the acl entry list by object id and sid id.
     *
     * @param objectIdentityId
     *         the object identity id
     * @param sidId
     *         the sid id
     *
     * @return the acl entry list by object id and sid id
     */
    List< AclEntryEntity > getAclEntryListByObjectIdAndSidId( EntityManager entityManager, UUID objectIdentityId, UUID sidId );

    /**
     * Gets the acl entry list by object id.
     *
     * @param objectIdentityId
     *         the object identity id
     *
     * @return the acl entry list by object id
     */
    List< AclEntryEntity > getAclEntryListByObjectId( EntityManager entityManager, UUID objectIdentityId );

    /**
     * Gets acl security identity entity ids by object id.
     *
     * @param entityManager
     *         the entity manager
     * @param objectIdentityId
     *         the object identity id
     *
     * @return the acl security identity entity list by object id
     */
    List< UUID > getAclSecurityIdentityIdsByObjectId( EntityManager entityManager, UUID objectIdentityId );

    /**
     * Gets acl security identity sids by object id.
     *
     * @param entityManager
     *         the entity manager
     * @param objectIdentityId
     *         the object identity id
     *
     * @return the acl security identity ids by object id
     */
    List< UUID > getAclSecurityIdentitySidsByObjectId( EntityManager entityManager, UUID objectIdentityId );

    /**
     * Delete ac eby acl security identity entity.
     *
     * @param entityManager
     *         the entity manager
     * @param objectIdentityId
     *         the object identity id
     * @param aclSecurityIdentityEntity
     *         the acl security identity entity
     */
    void deleteACEbyAclSecurityIdentityEntity( EntityManager entityManager, UUID objectIdentityId,
            AclSecurityIdentityEntity aclSecurityIdentityEntity );

    /**
     * Gets acl entry entity by object id and mask and given sid list.
     *
     * @param entityManager
     *         the entity manager
     * @param objectIdentityId
     *         the object identity id
     * @param sidList
     *         the sid list
     * @param mask
     *         the mask
     *
     * @return the acl entry entity by object id and mask and given sid list
     */
    List< AclEntryEntity > getAclEntryEntityByObjectIdAndMaskAndGivenSidList( EntityManager entityManager, UUID objectIdentityId,
            List< UUID > sidList, int mask );

}
