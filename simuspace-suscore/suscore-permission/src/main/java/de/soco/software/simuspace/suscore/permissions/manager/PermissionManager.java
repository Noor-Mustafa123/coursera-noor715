package de.soco.software.simuspace.suscore.permissions.manager;

import javax.persistence.EntityManager;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.AclObjectIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.permissions.constants.PermissionManageForm;
import de.soco.software.simuspace.suscore.permissions.dao.AclEntryDAO;
import de.soco.software.simuspace.suscore.permissions.dao.AclObjectIdentityDAO;
import de.soco.software.simuspace.suscore.permissions.model.ManageObjectDTO;
import de.soco.software.simuspace.suscore.permissions.model.ResourceAccessControlDTO;

/**
 * The Interface PermissionManager manages the permission operations to Dao layer.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public interface PermissionManager {

    /**
     * Checks if is permitted.
     *
     * @param userId
     *         the user id to check against
     * @param resourcePermissionSet
     *         the resource permission set
     *
     * @return true, if is permitted
     *
     * @apiNote To be used in service calls only
     */
    boolean isPermitted( String userId, String resourcePermissionSet );

    /**
     * Is permitted boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param resourcePermissionSet
     *         the resource permission set
     *
     * @return the boolean
     */
    boolean isPermitted( EntityManager entityManager, String userId, String resourcePermissionSet );

    /**
     * Permit by security identity and resource id.
     *
     * @param entityManager
     *         the entity manager
     * @param checkBox
     *         the check box
     * @param resourceId
     *         the resource id
     * @param securityIdentityEntity
     *         the security identity entity
     *
     * @return true, if successful
     */
    boolean permitFeaturesBySecurityIdentityAndResourceId( EntityManager entityManager, CheckBox checkBox, UUID resourceId,
            AclSecurityIdentityEntity securityIdentityEntity );

    /**
     * Manage permission table UI.
     *
     * @return the list
     */
    List< TableColumn > managePermissionTableUI();

    /**
     * Gets the resource access control list.
     *
     * @param entityManager
     *         the entity manager
     * @param securityIdentity
     *         the security identity
     *
     * @return the resource access control list
     */
    List< PermissionManageForm > getResourceAccessControlList( EntityManager entityManager, UUID securityIdentity );

    /**
     * Gets the resource access control.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the resource access control
     */
    ResourceAccessControlDTO getResourceAccessControl( EntityManager entityManager, UUID id );

    /**
     * Adds the tree node to acl.
     *
     * @param entityManager
     *         the entity manager
     * @param resourceAccessControlDTO
     *         the resource access control DTO
     *
     * @return true, if successful
     */
    boolean addTreeNodeToAcl( EntityManager entityManager, ResourceAccessControlDTO resourceAccessControlDTO );

    /**
     * Gets the entry DAO.
     *
     * @return the entry DAO
     */
    AclEntryDAO getEntryDAO();

    /**
     * Gets the object identity DAO.
     *
     * @return the object identity DAO
     */
    AclObjectIdentityDAO getObjectIdentityDAO();

    /**
     * Adds the object to acl.
     *
     * @param entityManager
     *         the entity manager
     * @param resourceAccessControlDTO
     *         the resource access control DTO
     *
     * @return true, if successful
     */
    boolean addObjectToAcl( EntityManager entityManager, ResourceAccessControlDTO resourceAccessControlDTO );

    /**
     * Extract column list.
     *
     * @param attributes
     *         the attributes
     *
     * @return the list
     */
    List< TableColumn > extractColumnList( Field[] attributes );

    /**
     * Checks if is permittedto read.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     *
     * @return true, if is permittedto read
     */
    boolean isReadable( EntityManager entityManager, UUID userId, UUID objectId );

    /**
     * Checks if is writable.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     *
     * @return true, if is writable
     */
    boolean isWritable( EntityManager entityManager, UUID userId, UUID objectId );

    /**
     * Checks if is manageable.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     *
     * @return true, if is manageable
     */
    boolean isManageable( EntityManager entityManager, UUID userId, UUID objectId );

    /**
     * Prepare object manager DT os.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param isWriteAble
     *         the is write able
     * @param filter
     *         the filter
     *
     * @return the list
     */
    List< ManageObjectDTO > prepareObjectManagerDTOs( EntityManager entityManager, UUID objectId, boolean isWriteAble, FiltersDTO filter );

    /**
     * Gets object user ids.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     *
     * @return the list
     */
    List< String > getObjectUserIds( EntityManager entityManager, UUID objectId );

    /**
     * Gets object group ids.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     *
     * @return the list
     */
    List< String > getObjectGroupIds( EntityManager entityManager, UUID objectId );

    /**
     * Permit permission to object.
     *
     * @param entityManager
     *         the entity manager
     * @param checkBox
     *         the check box
     * @param objectId
     *         the object id
     * @param securityId
     *         the security id
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return true, if successful
     */
    boolean permitPermissionToObject( EntityManager entityManager, CheckBox checkBox, UUID objectId, UUID securityId,
            String userIdFromGeneralHeader );

    /**
     * Save selection for new entity.
     *
     * @param userId
     *         the user id
     * @param createdEntity
     *         the created entity
     * @param parentEntity
     *         the parent entity
     */
    void saveSelectionForNewEntity( EntityManager entityManager, String userId, SuSEntity createdEntity, SuSEntity parentEntity );

    /**
     * Save selection for new entity.
     *
     * @param userId
     *         the user id
     * @param createdEntity
     *         the created entity
     * @param parentEntity
     *         the parent entity
     */
    void saveSelectionForNewJob( EntityManager entityManager, String userId, JobEntity createdEntity, SuSEntity parentEntity );

    /**
     * Prepare resource access control DTO from object.
     *
     * @param entityManager
     *         the entity manager
     * @param createdEntity
     *         the created entity
     * @param parentEntity
     *         the parent entity
     *
     * @return the resource access control DTO
     */
    ResourceAccessControlDTO prepareResourceAccessControlDTOFromObject( EntityManager entityManager, SuSEntity createdEntity,
            SuSEntity parentEntity );

    /**
     * Prepare resource access control DTO from object.
     *
     * @param entityManager
     *         the entity manager
     * @param createdEntity
     *         the created entity
     * @param parentEntity
     *         the parent entity
     *
     * @return the resource access control DTO
     */
    ResourceAccessControlDTO prepareResourceAccessControlDTOFromJob( EntityManager entityManager, JobEntity createdEntity,
            SuSEntity parentEntity );

    /**
     * Sets the children final ACL recusively.
     *
     * @param entityManager
     *         the entity manager
     * @param aclObjectIdentityEntity
     *         the new children final ACL recusively
     */
    void setChildrenFinalACLRecusively( EntityManager entityManager, AclObjectIdentityEntity aclObjectIdentityEntity );

    /**
     * Sets the children final ACL Improved version of setChildrenFinalACLRecusively; Gets all childrens in one db call instead of
     * recursive.
     *
     * @param aclObjectIdentityEntity
     *         the new children final ACL recusively
     */
    void setChildrenFinalACL( AclObjectIdentityEntity aclObjectIdentityEntity );

    /**
     * Adds the features.
     *
     * @param entityManager
     *         the entity manager
     * @param resourceAccessControlDTO
     *         the resource access control DTO
     *
     * @return true, if successful
     */
    boolean addFeatures( EntityManager entityManager, ResourceAccessControlDTO resourceAccessControlDTO );

    /**
     * Prepare resource access control DTO.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param qualifiedName
     *         the qualified name
     * @param description
     *         the description
     *
     * @return the resource access control DTO
     */
    ResourceAccessControlDTO prepareResourceAccessControlDTO( UUID id, String name, String qualifiedName, String description );

    /**
     * Checks if is permitted.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param premissionQuery
     *         the premission type
     * @param message
     *         the message
     * @param name
     *         the name
     */
    void isPermitted( EntityManager entityManager, String userIdFromGeneralHeader, String premissionQuery, String message, String name );

    /**
     * Save selection items.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param origin
     *         the origin
     * @param selectionIds
     *         the selection ids
     *
     * @return the selection response UI
     */
    SelectionResponseUI saveSelectionItems( EntityManager entityManager, String userId, String origin, List< UUID > selectionIds );

    SuSGenericObjectDAO< SuSEntity > getSusDAO();

    /**
     * Update parent in acl entity.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param newParentId
     *         the new parent id
     */
    void updateParentInAclEntity( EntityManager entityManager, String userId, UUID objectId, UUID newParentId );

}
