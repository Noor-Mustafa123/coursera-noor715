package de.soco.software.simuspace.suscore.authentication;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SuSFeaturesEnum;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.data.entity.AclEntryEntity;
import de.soco.software.simuspace.suscore.data.entity.AclObjectIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.role.manager.RoleManager;
import de.soco.software.simuspace.suscore.role.model.RoleDTO;
import de.soco.software.simuspace.suscore.user.dao.UserDAO;
import de.soco.software.simuspace.suscore.user.dao.UserDetailDAO;
import de.soco.software.simuspace.suscore.user.dao.UserLoginAttemptDAO;
import de.soco.software.simuspace.suscore.user.manager.UserGroupManager;
import de.soco.software.simuspace.suscore.user.manager.UserManager;

/**
 * Custom shiro realm class for authenticating user
 *
 * @author Zeeshan
 */
@Log4j2
public class CustomAuthorizingRealm extends JdbcRealm {

    /**
     * The Constant IS_LOCATION.
     */
    private static final String IS_LOCATION = "isLocation";

    /**
     * The Constant COLON.
     */
    private static final String COLON = ":";

    /**
     * The userManager reference .
     */
    private UserManager userManager;

    /**
     * The permissionManager reference .
     */
    private PermissionManager permissionManager;

    /**
     * The user group manager.
     */
    private UserGroupManager userGroupManager;

    /**
     * The roleManager reference .
     */
    private RoleManager roleManager;

    /**
     * The userLoginAttemptDAO reference .
     */
    private UserLoginAttemptDAO userLoginAttemptDAO;

    /**
     * The userDetailDAO reference .
     */
    private UserDetailDAO userDetailDAO;

    /**
     * The userDAO reference .
     */
    private UserDAO userDAO;

    /**
     * The Entity manager factory reference.
     */
    private EntityManagerFactory entityManagerFactory;

    @Override
    public boolean isPermitted( PrincipalCollection principals, String permission ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UserDTO user = ( UserDTO ) principals.getPrimaryPrincipal();
            log.info( ">>isPermitted user " + user.getUserUid() );
            String[] permSplit = permission.split( COLON );
            String objectId = permSplit[ 0 ];
            log.info( ">>isPermitted objectId " + objectId );
            int mask = PermissionMatrixEnum.getKeyByValue( permSplit[ 1 ] );
            log.info( ">>isPermitted mask " + mask );
            String isLocationFeature = "";
            if ( 2 < permSplit.length ) {
                isLocationFeature = permSplit[ 2 ];
            }

            boolean result = false;
            if ( verifyFeature( objectId, isLocationFeature ) ) {
                result = checkFeature( entityManager, getEffectiveRoleSidsOfUser( entityManager, user ), permissionManager
                        .getObjectIdentityDAO().getAclObjectIdentitiesByClassId( entityManager, UUID.fromString( objectId ) ), mask );

            } else {
                List< UUID > effectiveSidsOfUser = getEffectiveSidsOfUser( entityManager, user );
                result = checkRecursive( entityManager, effectiveSidsOfUser, UUID.fromString( objectId ), mask );
            }

            log.info( "<<isPermitted result " + result );
            return result;
        } finally {
            entityManager.close();
        }
    }

    private boolean checkFeature( EntityManager entityManager, List< UUID > effectiveRoleSidsOfUser,
            List< AclObjectIdentityEntity > objectIds, int mask ) {
        boolean result = false;
        if ( effectiveRoleSidsOfUser.isEmpty() ) {
            return false;
        }
        for ( AclObjectIdentityEntity o : objectIds ) {
            List< AclEntryEntity > entries = permissionManager.getEntryDAO()
                    .getAclEntryEntityByObjectIdAndMaskAndGivenSidList( entityManager, o.getId(), effectiveRoleSidsOfUser, mask );
            if ( !entries.isEmpty() ) {
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean checkRecursive( EntityManager entityManager, List< UUID > effectiveSidsOfUser, UUID objectId, int mask ) {
        AclObjectIdentityEntity objectEntity = permissionManager.getObjectIdentityDAO().getAclObjectIdentityEntityById( entityManager,
                objectId );
        if ( objectEntity != null ) {
            if ( log.isDebugEnabled() ) {
                log.debug( ">>checkRecursive objectEntity " + objectEntity );
            }

            // 1. check if obj_id inherit=true, then check parent for it. (recursive)

            if ( objectEntity.isInherit() ) {
                return checkRecursive( entityManager, effectiveSidsOfUser, objectEntity.getParentObject().getId(), mask );
            } else {
                // 2. check if obj_id inherit=false, then check for user/group/role.
                if ( log.isDebugEnabled() ) {
                    log.debug( ">>isPermitted effectiveSidsOfUser " + effectiveSidsOfUser );
                }
                List< AclEntryEntity > entries = permissionManager.getEntryDAO()
                        .getAclEntryEntityByObjectIdAndMaskAndGivenSidList( entityManager, objectId, effectiveSidsOfUser, mask );
                return !entries.isEmpty();
            }
        }
        return false;

    }

    private List< UUID > getEffectiveSidsOfUser( EntityManager entityManager, UserDTO user ) {

        List< UUID > effectiveSidsOfUser = new ArrayList<>();
        effectiveSidsOfUser.add(
                userManager.getSecurityIdentityDAO().getSecurityIdentityBySid( entityManager, UUID.fromString( user.getId() ) ).getId() );
        List< GroupEntity > groupEntities = userGroupManager.getUserGroupsByUserId( entityManager, UUID.fromString( user.getId() ) );
        if ( groupEntities != null ) {
            for ( GroupEntity groupEntity : groupEntities ) {
                AclSecurityIdentityEntity gAclSecurityIdentityEntity = userManager.getSecurityIdentityDAO()
                        .getSecurityIdentityBySid( entityManager, groupEntity.getId() );

                effectiveSidsOfUser.add( gAclSecurityIdentityEntity.getId() );
            }
        }
        return effectiveSidsOfUser;
    }

    private List< UUID > getEffectiveRoleSidsOfUser( EntityManager entityManager, UserDTO user ) {

        List< UUID > effectiveSidsOfUser = new ArrayList<>();
        List< RoleDTO > roleModels = roleManager.getRoleNamesForUser( entityManager, user.getId() );
        for ( RoleDTO roleModel : roleModels ) {
            if ( roleModel != null ) {
                effectiveSidsOfUser.add( roleModel.getSecurityIdentity() );
            }
        }
        return effectiveSidsOfUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthorizationInfo doGetAuthorizationInfo( PrincipalCollection principals ) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        UserDTO user = ( UserDTO ) principals.getPrimaryPrincipal();
        log.info( ">>doGetAuthorizationInfo:user:" + user.getUserUid() );
        return authorizationInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthenticationInfo doGetAuthenticationInfo( AuthenticationToken token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UserDTO user = null;
            UsernamePasswordToken upToken = ( UsernamePasswordToken ) token;
            String username = upToken.getUsername();
            String password = new String( upToken.getPassword() );
            user = getAuthenticatedUser( entityManager, username, password );
            super.doClearCache( new SimplePrincipalCollection( username, this.getName() ) );
            return new SimpleAuthenticationInfo( user, upToken.getPassword(), getName() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearCachedAuthorizationInfo( PrincipalCollection principals ) {
        super.clearCachedAuthorizationInfo( principals );
    }

    /**
     * Enable cache.
     */
    public void enableCache() {
        setCachingEnabled( true );
    }

    @Override
    public void setDataSource( DataSource dataSource ) {
        super.setDataSource( dataSource );
    }

    @Override
    public void setPermissionsLookupEnabled( boolean permissionsLookupEnabled ) {
        super.setPermissionsLookupEnabled( permissionsLookupEnabled );
    }

    @Override
    public void setAuthenticationQuery( String authenticationQuery ) {
        super.setAuthenticationQuery( authenticationQuery );
    }

    /**
     * Gets the authenticated user.
     *
     * @param entityManager
     *         the entity manager
     * @param username
     *         the username
     * @param password
     *         the password
     *
     * @return the authenticated user
     */
    private UserDTO getAuthenticatedUser( EntityManager entityManager, String username, String password ) {
        UserDTO user;
        user = new UserDTO();
        user.setUserUid( username );
        user.setPassword( password );
        user = userManager.authenticate( entityManager, user, Boolean.FALSE );
        String token = userManager.prepareUserToken( entityManager, user );
        user.setToken( token );
        user.setPassword( password );
        return user;
    }

    /**
     * Verify feature.
     *
     * @param id
     *         the id
     * @param isLocationFeature
     *         the isLocationFeature
     *
     * @return true, if successful
     */
    private boolean verifyFeature( String id, String isLocationFeature ) {

        if ( StringUtils.isNotNullOrEmpty( isLocationFeature ) && isLocationFeature.equals( IS_LOCATION ) ) {
            return true;
        }

        for ( SuSFeaturesEnum featuresEnumFullyQualified : SuSFeaturesEnum.values() ) {
            if ( featuresEnumFullyQualified.getId().equals( id ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets user manager.
     *
     * @return the userManager
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Sets user manager.
     *
     * @param userManager
     *         the userManager
     */
    public void setUserManager( UserManager userManager ) {
        this.userManager = userManager;
    }

    /**
     * Gets permission manager.
     *
     * @return the permissionManager
     */
    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    /**
     * Sets permission manager.
     *
     * @param permissionManager
     *         the permissionManager
     */
    public void setPermissionManager( PermissionManager permissionManager ) {
        this.permissionManager = permissionManager;
    }

    /**
     * Gets role manager.
     *
     * @return the roleManager
     */
    public RoleManager getRoleManager() {
        return roleManager;
    }

    /**
     * Sets role manager.
     *
     * @param roleManager
     *         the roleManager
     */
    public void setRoleManager( RoleManager roleManager ) {
        this.roleManager = roleManager;
    }

    /**
     * Gets user login attempt dao.
     *
     * @return the userLoginAttemptDAO
     */
    public UserLoginAttemptDAO getUserLoginAttemptDAO() {
        return userLoginAttemptDAO;
    }

    /**
     * Sets user login attempt dao.
     *
     * @param userLoginAttemptDAO
     *         the userLoginAttemptDAO
     */
    public void setUserLoginAttemptDAO( UserLoginAttemptDAO userLoginAttemptDAO ) {
        this.userLoginAttemptDAO = userLoginAttemptDAO;
    }

    /**
     * Gets user detail dao.
     *
     * @return the userDetailDAO
     */
    public UserDetailDAO getUserDetailDAO() {
        return userDetailDAO;
    }

    /**
     * Sets user detail dao.
     *
     * @param userDetailDAO
     *         the userDetailDAO
     */
    public void setUserDetailDAO( UserDetailDAO userDetailDAO ) {
        this.userDetailDAO = userDetailDAO;
    }

    /**
     * Gets user dao.
     *
     * @return the userDAO
     */
    public UserDAO getUserDAO() {
        return userDAO;
    }

    /**
     * Sets user dao.
     *
     * @param userDAO
     *         the userDAO
     */
    public void setUserDAO( UserDAO userDAO ) {
        this.userDAO = userDAO;
    }

    /**
     * Gets the user group manager.
     *
     * @return the user group manager
     */
    public UserGroupManager getUserGroupManager() {
        return userGroupManager;
    }

    /**
     * Sets the user group manager.
     *
     * @param userGroupManager
     *         the new user group manager
     */
    public void setUserGroupManager( UserGroupManager userGroupManager ) {
        this.userGroupManager = userGroupManager;
    }

    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}
