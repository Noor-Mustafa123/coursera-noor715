package de.soco.software.simuspace.suscore.role.manager.impl;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserDirectories;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupRoleEntity;
import de.soco.software.simuspace.suscore.data.entity.RoleEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.permissions.constants.PermissionManageForm;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.permissions.model.PermissionDTO;
import de.soco.software.simuspace.suscore.role.dao.GroupRoleDAO;
import de.soco.software.simuspace.suscore.role.dao.RoleDAO;
import de.soco.software.simuspace.suscore.role.model.RoleDTO;
import de.soco.software.simuspace.suscore.user.dao.AclSecurityIdentityDAO;
import de.soco.software.simuspace.suscore.user.manager.UserGroupManager;
import de.soco.software.simuspace.suscore.user.manager.UserManager;

/**
 * This class having test cases role CRUD related (and other) operation to Dao layer.
 *
 * @author Ahsan Khan
 * @since 2.0
 */

@RunWith( PowerMockRunner.class )
@PrepareForTest( { GUIUtils.class } )
public class RoleManagerImplTest {

    /**
     * The manager.
     */
    private RoleManagerImpl manager;

    /**
     * The selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * Dummy GROUP_ID for test Cases.
     */
    private static final UUID GROUP_ID = UUID.randomUUID();

    /**
     * The Constant NAME_FIELD.
     */
    private static final String NAME_FIELD = "Name";

    /**
     * The Constant DESCRIPTION_FIELD.
     */
    private static final String DESCRIPTION_FIELD = "Description";

    /**
     * The Constant ACTIVE.
     */
    public static final String ACTIVE = "Active";

    /**
     * Dummy UserId for test Cases.
     */
    private static final String DEFAULT_USER_ID = UUID.randomUUID().toString();

    /**
     * The Constant UID.
     */
    private static final String UID = "sces120";

    /**
     * The Constant UID.
     */
    private static final String USER_NAME = "Test_User";

    /**
     * Dummy USER_ID for test Cases.
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * The Constant SINGLE.
     */
    private static final String MODE = "single";

    /**
     * The Constant BULK.
     */
    private static final String BULK = "bulk";

    /**
     * Error Message for test Case.
     */
    private static final String ERROR_MSG_FOR_ROLE_ID = "No role with id : ";

    /**
     * The Constant ROLE.
     */
    private static final String ROLE = "Role";

    /**
     * The Constant NAME.
     */
    private static final String NAME = "name";

    /**
     * The Constant TYPE.
     */
    private static final String TYPE = "text";

    /**
     * The Constant VALUE.
     */
    private static final String VALUE = null;

    /**
     * The Constant HELP.
     */
    private static final String HELP = null;

    /**
     * The Constant PLACE_HOLDER.
     */
    private static final String PLACE_HOLDER = null;

    /**
     * The dao.
     */
    private RoleDAO dao;

    /**
     * The group role DAO.
     */
    private GroupRoleDAO groupRoleDAO;

    /**
     * The user manager.
     */
    private UserManager userManager;

    /**
     * The group manager.
     */
    private UserGroupManager userGroupManager;

    /**
     * The security identity DAO.
     */
    private AclSecurityIdentityDAO securityIdentityDAO;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The context menu manager.
     */
    private ContextMenuManager contextMenuManager;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The license manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The Constant mockControl.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * Error Message for test Case.
     */
    private static final String ERROR_MSG_FOR_GRP_ROLE_LENGTH = "Value too large for Role (MaxLength=64).";

    /**
     * The Constant ERROR_MSG_FOR_GRP_DESC_LENGTH.
     */
    private static final String ERROR_MSG_FOR_GRP_DESC_LENGTH = "Value too large for Description (MaxLength=255).";

    /**
     * The Constant DESCRIPTION_WITH_GREATER_THEN_MAX.
     */
    private static final String DESCRPTION_WITH_GREATER_THEN_MAX = "ghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";

    /**
     * Error Message for test Case.
     */
    private static final String ERROR_MSG_FOR_GRP_NAME = "Role can not be null or empty";

    /**
     * The Constant ROLE_ID.
     */
    private static final UUID ROLE_ID = UUID.randomUUID();

    /**
     * The Constant RESOURCE_ID.
     */
    private static final UUID RESOURCE_ID = UUID.randomUUID();

    /**
     * The role model.
     */
    private RoleDTO roleModel = new RoleDTO();

    /**
     * The role entity.
     */
    private RoleEntity roleEntity = new RoleEntity();

    /**
     * The group role entity.
     */
    private GroupRoleEntity groupRoleEntity = new GroupRoleEntity();

    /**
     * Dummy groupEntity object for test data.
     */
    private GroupEntity groupEntity = new GroupEntity();

    /**
     * The Constant ACTIVE_ROLE.
     */
    private static final boolean ACTIVE_ROLE = true;

    /**
     * The Constant Active Group.
     */
    private static final boolean ACTIVE_GRP = true;

    /**
     * Dummy susGroupDTo object for test data.
     */
    private SuSUserGroupDTO susGroupDto = new SuSUserGroupDTO();

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * The Constant GROUP_ROLE_ID.
     */
    private static final UUID GROUP_ROLE_ID = UUID.randomUUID();

    /**
     * The Constant LICENSE_RESOURCE_ID.
     */
    private static final UUID LICENSE_RESOURCE_ID = UUID.randomUUID();

    /**
     * The Constant INDEX.
     */
    private static final int INDEX = 0;

    /**
     * The Constant NO_ROLE_EXIST.
     */
    private static final String NO_ROLE_EXIST = "No role with id : " + ROLE_ID.toString();

    /**
     * The Constant NO_ACCOUNT_FOUND_FOR_GROUP.
     */
    private static final String NO_ACCOUNT_FOUND_FOR_GROUP = "No account found for group.";

    /**
     * The Constant MODE_NOT_SUPPORTED.
     */
    private static final String MODE_NOT_SUPPORTED = "Mode is not suported: kkk";

    /**
     * The Constant SELECTION_ID.
     */
    public static final UUID SELECTION_ID = UUID.fromString( "cd18eff4-b50b-4b13-aa92-6f4f92259ddc" );

    /**
     * The Constant SUPER_USER_ID.
     */
    public static final String SUPER_USER_ID = "1e98a77c-a0be-4983-9137-d9a8acd0ea8b";

    /**
     * The Constant DIRECTORY_ID.
     */
    private static final UUID DIRECTORY_ID = UUID.fromString( "cd18eff4-b50b-4b13-aa92-6f4f92459ddc" );

    /**
     * The active account status.
     */

    public static final boolean ACCOUNT_STATUS_ACTIVE = true;

    /**
     * Setup.
     *
     * @throws Exception
     *         the exception
     */
    @Before
    public void setup() throws Exception {
        mockControl.resetToNice();
        manager = new RoleManagerImpl();
        dao = mockControl.createMock( RoleDAO.class );
        groupRoleDAO = mockControl.createMock( GroupRoleDAO.class );
        userManager = mockControl.createMock( UserManager.class );
        securityIdentityDAO = mockControl.createMock( AclSecurityIdentityDAO.class );
        userGroupManager = mockControl.createMock( UserGroupManager.class );
        permissionManager = mockControl.createMock( PermissionManager.class );
        licenseManager = mockControl.createMock( LicenseManager.class );
        setSelectionManager( mockControl.createMock( SelectionManager.class ) );
        contextMenuManager = mockControl.createMock( ContextMenuManager.class );
        userCommonManager = mockControl.createMock( UserCommonManager.class );

        manager.setContextMenuManager( contextMenuManager );
        manager.setSelectionManager( selectionManager );
        manager.setRoleDAO( dao );
        manager.setUserManager( userManager );
        manager.setUserGroupManager( userGroupManager );
        manager.setGroupRoleDAO( groupRoleDAO );
        manager.setPermissionManager( permissionManager );
        manager.setLicenseManager( licenseManager );
        manager.setUserCommonManager( userCommonManager );
        ;
    }

    /**
     * Should get null in create role when null object is given as input.
     */
    @Test
    public void shouldGetNullInCreateRoleWhenNullObjectIsGivenAsInput() {
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        RoleDTO actual = null;
        RoleDTO expected = manager.createRole( DEFAULT_USER_ID, actual, true );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should get validation error msg in create role entity when name fieldlegth is greater than 64.
     */
    @Test
    public void shouldGetValidationErrorMsgInCreateRoleEntityWhenNameFieldlegthIsGreaterThan64() {
        thrown.expect( SusException.class );
        thrown.expectMessage( ERROR_MSG_FOR_GRP_ROLE_LENGTH );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        RoleDTO actual = getRoleModel();
        actual.setName( DESCRPTION_WITH_GREATER_THEN_MAX );
        manager.createRole( DEFAULT_USER_ID, actual, true );
    }

    /**
     * Should get validation error msg in create role entity when description fieldlegth is greater than 255.
     */
    @Test
    public void shouldGetValidationErrorMsgInCreateRoleEntityWhenDescriptionFieldlegthIsGreaterThan255() {
        thrown.expect( SusException.class );
        thrown.expectMessage( ERROR_MSG_FOR_GRP_DESC_LENGTH );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        RoleDTO actual = getRoleModel();
        actual.setName( NAME );
        actual.setDescription( DESCRPTION_WITH_GREATER_THEN_MAX );
        manager.createRole( DEFAULT_USER_ID, actual, true );
    }

    /**
     * Should Get Validation Error Msg In Create Group Entity When Group Name is Empty.
     */
    @Test
    public void shouldGetValidationErrorMsgInCreateRoleEntityWhenRoleNameisEmpty() {
        thrown.expect( SusException.class );
        thrown.expectMessage( ERROR_MSG_FOR_GRP_NAME );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        RoleDTO actual = getRoleModel();
        actual.setName( StringUtils.EMPTY );
        manager.createRole( DEFAULT_USER_ID, actual, true );
    }

    /**
     * Should successfully create role when valid role id is given as input.
     */
    @Test
    public void shouldSuccessfullyCreateRoleWhenValidRoleIdIsGivenAsInput() {
        RoleEntity actualEntity = getRoleEntity();
        actualEntity.setSelectionid( SELECTION_ID.toString() );
        RoleDTO actual = getRoleModel();

        List< UUID > expectedUsers = new ArrayList<>();
        expectedUsers.add( UUID.randomUUID() );
        expectedUsers.add( UUID.randomUUID() );
        expectedUsers.add( UUID.randomUUID() );

        EasyMock.expect( dao.saveRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( RoleEntity.class ) ) )
                .andReturn( actualEntity ).anyTimes();
        EasyMock.expect( userGroupManager.readUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( getUserGrpDto() ).anyTimes();
        EasyMock.expect( groupRoleDAO.getGroupRoleByRoleId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getGroupRoleList() ).anyTimes();
        EasyMock.expect(
                        selectionManager.getSelectedIdsListBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( expectedUsers ).anyTimes();
        EasyMock.expect( userManager.getSecurityIdentityDAO() ).andReturn( securityIdentityDAO ).anyTimes();
        EasyMock.expect( securityIdentityDAO.save( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( AclSecurityIdentityEntity.class ) ) ).andReturn( fillSecurityIdentity( actualEntity ) ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( dao.readRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( actualEntity ).anyTimes();
        mockControl.replay();

        RoleDTO expected = manager.createRole( EasyMock.anyObject( EntityManager.class ), DEFAULT_USER_ID, actual, true );
        Assert.assertEquals( expected.getName(), actual.getName() );
        Assert.assertEquals( expected.getDescription(), actual.getDescription() );
        Assert.assertEquals( expected.getId(), actual.getId() );
        Assert.assertEquals( expected.getSelectionId(), actual.getSelectionId() );

    }

    /**
     * Should return all roles related to user.
     */
    @Test
    public void shouldReturnAllRolesRelatedToUser() {
        roleModel = getRoleModel();
        List< RoleDTO > actualRoleDTOs = new ArrayList<>();
        actualRoleDTOs.add( roleModel );

        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
                .andReturn( getUserEntity() ).anyTimes();
        List< GroupEntity > userGroupEntities = new ArrayList<>();
        userGroupEntities.add( getGroupEntity() );

        EasyMock.expect(
                        userGroupManager.getUserGroupsByUserId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userGroupEntities ).anyTimes();

        EasyMock.expect( groupRoleDAO.getGroupRoleByGroupId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getGroupRoleList() ).anyTimes();
        roleModel = getRoleModel();
        RoleEntity actualEntity = getRoleEntity();
        actualEntity.setGroups( getGroupRoleList() );
        EasyMock.expect( userManager.getSecurityIdentityDAO() ).andReturn( securityIdentityDAO ).anyTimes();
        EasyMock.expect( securityIdentityDAO.getSecurityIdentityBySid( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillSecurityIdentity( actualEntity ) ).anyTimes();
        EasyMock.expect(
                        dao.getLatestNonDeletedActiveObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( actualEntity ).anyTimes();
        mockControl.replay();
        List< RoleDTO > expectedRoleDTOs = manager.getRoleNamesForUser( EasyMock.anyObject( EntityManager.class ), USER_ID.toString() );

        Assert.assertEquals( expectedRoleDTOs.get( INDEX ).getName(), actualRoleDTOs.get( INDEX ).getName() );

    }

    /**
     * Should get null in update role when null object is given as input.
     */
    @Test
    public void shouldGetNullInUpdateRoleWhenNullObjectIsGivenAsInput() {
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        RoleDTO actual = null;
        RoleDTO expected = manager.updateRole( DEFAULT_USER_ID, actual );
        Assert.assertEquals( expected, actual );

    }

    /**
     * Should successfully update role when valid role id is given as input.
     */
    @Test
    public void shouldSuccessfullyUpdateRoleWhenValidRoleIdIsGivenAsInput() {
        RoleEntity actualEntity = getRoleEntity();
        actualEntity.setGroups( getGroupRoleList() );
        RoleDTO actual = getRoleModel();
        List< UUID > expectedUsers = new ArrayList<>();
        expectedUsers.add( UUID.randomUUID() );
        expectedUsers.add( UUID.randomUUID() );
        expectedUsers.add( UUID.randomUUID() );
        EasyMock.expect( dao.updateRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( RoleEntity.class ) ) )
                .andReturn( actualEntity ).anyTimes();
        EasyMock.expect( dao.readRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( actualEntity ).anyTimes();
        EasyMock.expect( groupRoleDAO.getGroupRoleByRoleId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( actualEntity.getGroups() ).anyTimes();
        EasyMock.expect( userGroupManager.readUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( getUserGrpDto() ).anyTimes();
        EasyMock.expect(
                        selectionManager.getSelectedIdsListBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( expectedUsers ).anyTimes();
        EasyMock.expect( userManager.getSecurityIdentityDAO() ).andReturn( securityIdentityDAO ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        RoleDTO expected = manager.updateRole( DEFAULT_USER_ID, actual );
        Assert.assertEquals( expected.getName(), actual.getName() );
        Assert.assertEquals( expected.getDescription(), actual.getDescription() );
        Assert.assertEquals( expected.getId(), actual.getId() );
    }

    /**
     * Should not update role if payload is empty or null.
     */
    @Test
    public void shouldNotUpdateRoleIfPayloadIsEmptyOrNull() {
        thrown.expect( SusException.class );
        thrown.expectMessage( ERROR_MSG_FOR_GRP_NAME );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        RoleEntity actualEntity = getRoleEntity();
        actualEntity.setGroups( getGroupRoleList() );
        EasyMock.expect( dao.readRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( actualEntity ).anyTimes();
        mockControl.replay();
        RoleDTO actual = getRoleModel();
        actual.setName( StringUtils.EMPTY );
        manager.updateRole( DEFAULT_USER_ID, actual );
    }

    /**
     * Should not update if role does not exist.
     */
    @Test
    public void shouldNotUpdateIfRoleDoesNotExist() {
        thrown.expect( SusException.class );
        thrown.expectMessage( NO_ROLE_EXIST );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId( ROLE_ID );
        EasyMock.expect( dao.readRole( EasyMock.anyObject( EntityManager.class ), roleDTO.getId() ) ).andReturn( null ).anyTimes();
        mockControl.replay();
        manager.updateRole( DEFAULT_USER_ID, roleDTO );
    }

    /**
     * Should successfully delete role entity when valid role id is given as input.
     */
    @Test
    public void shouldSuccessfullyDeleteRoleEntityWhenValidRoleIdIsGivenAsInput() {
        RoleEntity actualEntity = getRoleEntity();
        EasyMock.expect( userManager.getSecurityIdentityDAO() ).andReturn( securityIdentityDAO ).anyTimes();
        EasyMock.expect( securityIdentityDAO.getSecurityIdentityBySid( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillSecurityIdentity( actualEntity ) ).anyTimes();
        EasyMock.expect( dao.readRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( actualEntity ).anyTimes();
        EasyMock.expect( dao.updateRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( RoleEntity.class ) ) )
                .andReturn( actualEntity ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        boolean expected = manager.deleteRoleBySelection( DEFAULT_USER_ID, ROLE_ID.toString(), MODE );
        Assert.assertTrue( expected );
    }

    /**
     * Should successfully delete role entity in bulk when valid role id is given as input.
     */
    @Test
    public void shouldSuccessfullyDeleteRoleEntityInBulkWhenValidRoleIdIsGivenAsInput() {
        RoleEntity actualEntity = getRoleEntity();
        FiltersDTO filtersDTO = fillFilterDTO();

        EasyMock.expect( userManager.getSecurityIdentityDAO() ).andReturn( securityIdentityDAO ).anyTimes();
        EasyMock.expect( securityIdentityDAO.getSecurityIdentityBySid( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillSecurityIdentity( actualEntity ) ).anyTimes();
        EasyMock.expect( dao.readRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( actualEntity ).anyTimes();
        EasyMock.expect( dao.updateRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( RoleEntity.class ) ) )
                .andReturn( actualEntity ).anyTimes();
        EasyMock.expect( contextMenuManager.getFilterBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( filtersDTO ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        boolean expected = manager.deleteRoleBySelection( DEFAULT_USER_ID, ROLE_ID.toString(), BULK );
        Assert.assertTrue( expected );
    }

    /**
     * Fill filter DTO.
     *
     * @return the filters DTO
     */
    private FiltersDTO fillFilterDTO() {
        FiltersDTO filtersDTO = new FiltersDTO();
        UUID item = UUID.randomUUID();
        List< Object > items = new ArrayList<>();
        items.add( item );
        filtersDTO.setItems( items );
        return filtersDTO;
    }

    /**
     * Should not delete if invalid mode provided.
     */
    @Test
    public void shouldNotDeleteIfInvalidModeProvided() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MODE_NOT_SUPPORTED );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.deleteRoleBySelection( SUPER_USER_ID, ROLE_ID.toString(), "kkk" );
    }

    /**
     * Should Throw Exception In Delete Group Entity When Group Id Has No Record In DB.
     */
    @Test
    public void shouldThrowExceptionInDeleteRoleEntityWhenRoleIdHasNoRecordInDB() {
        thrown.expect( SusException.class );
        thrown.expectMessage( ERROR_MSG_FOR_ROLE_ID + ROLE_ID );
        EasyMock.expect( dao.readRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) ).andReturn( null )
                .anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.deleteRoleBySelection( SUPER_USER_ID, ROLE_ID.toString(), MODE );
    }

    /**
     * Should get empty role entity list when dao return null list.
     */
    @Test
    public void shouldGetEmptyRoleEntityListWhenDaoReturnNullList() {
        RoleEntity actualEntity = getRoleEntity();
        actualEntity.setGroups( getGroupRoleList() );
        List< RoleEntity > actual = new ArrayList<>();
        FiltersDTO filtersDTO = populateFilterDTO();
        EasyMock.expect( dao.getAllFilteredRecords( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( actual ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        FilteredResponse< RoleDTO > expected = manager.getRoleList( SUPER_USER_ID, filtersDTO );
        Assert.assertNotNull( expected );
        Assert.assertTrue( expected.getData().isEmpty() );
        Assert.assertEquals( expected.getData().size(), actual.size() );
    }

    /**
     * Should not provide role list if filter is null.
     */
    @Test
    public void shouldNotProvideRoleListIfFilterIsNull() {
        thrown.expect( SusException.class );
        thrown.expectMessage( NO_ACCOUNT_FOUND_FOR_GROUP );
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.getRoleList( SUPER_USER_ID, null );
    }

    /**
     * Should successfully get role entity list when dao return valid list.
     */
    @Test
    public void shouldSuccessfullyGetRoleEntityListWhenDaoReturnValidList() {
        RoleEntity actualEntity = getRoleEntity();
        actualEntity.setGroups( getGroupRoleList() );

        List< RoleEntity > actual = new ArrayList<>();
        actual.add( actualEntity );
        FiltersDTO filtersDTO = populateFilterDTO();
        EasyMock.expect( dao.getAllFilteredRecords( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( actual ).anyTimes();
        EasyMock.expect( groupRoleDAO.getGroupRoleByRoleId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( actualEntity.getGroups() ).anyTimes();
        EasyMock.expect( userManager.getSecurityIdentityDAO() ).andReturn( securityIdentityDAO ).anyTimes();
        EasyMock.expect( securityIdentityDAO.getSecurityIdentityBySid( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillSecurityIdentity( actualEntity ) ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        FilteredResponse< RoleDTO > expected = manager.getRoleList( DEFAULT_USER_ID, filtersDTO );
        Assert.assertNotNull( expected );
        Assert.assertEquals( expected.getData().size(), actual.size() );
    }

    /**
     * Should return all the valid permissions related to role.
     */
    @Test
    public void shouldReturnAllTheValidPermissionsRelatedToRole() {

        List< PermissionManageForm > actualResourceAccessControlDTOs = new ArrayList<>();
        actualResourceAccessControlDTOs.add( getFilledResourceAccessControlDTO() );
        FiltersDTO filtersDTO = populateFilterDTO();

        RoleEntity actualEntity = getRoleEntity();
        actualEntity.setGroups( getGroupRoleList() );
        EasyMock.expect( dao.readRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( actualEntity ).anyTimes();
        EasyMock.expect( groupRoleDAO.getGroupRoleByRoleId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( actualEntity.getGroups() ).anyTimes();
        EasyMock.expect( userManager.getSecurityIdentityDAO() ).andReturn( securityIdentityDAO ).anyTimes();
        EasyMock.expect( securityIdentityDAO.getSecurityIdentityBySid( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillSecurityIdentity( actualEntity ) ).anyTimes();
        EasyMock.expect( permissionManager.getResourceAccessControlList( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( actualResourceAccessControlDTOs ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();

        FilteredResponse< PermissionManageForm > expected = manager.getAllPermissionsByRoleId( filtersDTO, EasyMock.anyString(), ROLE_ID );
        List< PermissionManageForm > expectedResourceAccessControlDTOs = expected.getData();
        Assert.assertEquals( expectedResourceAccessControlDTOs.get( INDEX ).getName(),
                actualResourceAccessControlDTOs.get( INDEX ).getName() );

    }

    /**
     * Should return create role form.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnCreateRoleForm() throws Exception {
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        List< UIFormItem > expectedUiFormItems = new ArrayList<>();
        expectedUiFormItems.add( getUIFormItem( ROLE, NAME, TYPE, VALUE, HELP, PLACE_HOLDER ) );
        // List< UIFormItem > actualUiFormItems = manager.createRoleForm( SUPER_USER_ID );
        // Assert.assertEquals( expectedUiFormItems.get( INDEX ).getHelp(), actualUiFormItems.get( INDEX ).getHelp() );
    }

    /**
     * Should return edit rple form with selected group.
     */
    @Test
    public void shouldReturnEditRpleFormWithSelectedGroup() {
        List< UIFormItem > expectedUiFormItems = new ArrayList<>();
        expectedUiFormItems.add( getUIFormItem( ROLE, NAME, TYPE, VALUE, HELP, PLACE_HOLDER ) );
        RoleEntity actualEntity = getRoleEntity();
        actualEntity.setGroups( getGroupRoleList() );
        SuSUserGroupDTO selectedGroup = new SuSUserGroupDTO();
        selectedGroup.setId( UUID.randomUUID() );

        EasyMock.expect( dao.readRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( actualEntity ).anyTimes();
        EasyMock.expect( groupRoleDAO.getGroupRoleByRoleId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( actualEntity.getGroups() ).anyTimes();
        EasyMock.expect( userGroupManager.readUserGroup( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( selectedGroup ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        // List< UIFormItem > actualUiFormItems = manager.editRoleForm( SUPER_USER_ID, ROLE_ID );
        // Assert.assertEquals( expectedUiFormItems.get( INDEX ).getHelp(), actualUiFormItems.get( INDEX ).getHelp() );
    }

    /**
     * Should not open edit R ole form if invalid role provided.
     */
    @Test
    public void shouldNotOpenEditROleFormIfInvalidRoleProvided() {
        thrown.expect( SusException.class );
        thrown.expectMessage( NO_ROLE_EXIST );
        EasyMock.expect( dao.readRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) ).andReturn( null )
                .anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        manager.editRoleForm( SUPER_USER_ID, ROLE_ID );
    }

    /**
     * Permit allow feature permission to role.
     */
    @Test
    public void permitAllowFeaturePermissionToRole() {
        RoleEntity actualEntity = getRoleEntity();
        EasyMock.expect( dao.readRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( actualEntity ).anyTimes();
        EasyMock.expect( groupRoleDAO.getGroupRoleByRoleId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( actualEntity.getGroups() ).anyTimes();
        EasyMock.expect( userManager.getSecurityIdentityDAO() ).andReturn( securityIdentityDAO ).anyTimes();
        EasyMock.expect( securityIdentityDAO.getSecurityIdentityBySid( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillSecurityIdentity( actualEntity ) ).anyTimes();
        EasyMock.expect( permissionManager.permitFeaturesBySecurityIdentityAndResourceId( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject(), EasyMock.anyObject( UUID.class ), EasyMock.anyObject() ) ).andReturn( true ).anyTimes();
        UserEntity userEntity = prepareUserEntity();
        EasyMock.expect( userManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        mockControl.replay();
        Assert.assertTrue( manager.permitPermissionToRole( EasyMock.anyObject( EntityManager.class ), SUPER_USER_ID, new CheckBox(),
                ROLE_ID, RESOURCE_ID, true ) );
    }

    /**
     * Should return role if valid role id provided.
     */
    @Test
    public void shouldReturnRoleIfValidRoleIdProvided() {
        roleModel = getRoleModel();
        RoleEntity actualEntity = getRoleEntity();
        actualEntity.setGroups( getGroupRoleList() );
        EasyMock.expect( dao.readRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( actualEntity ).anyTimes();
        EasyMock.expect( groupRoleDAO.getGroupRoleByRoleId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( actualEntity.getGroups() ).anyTimes();
        EasyMock.expect( userManager.getSecurityIdentityDAO() ).andReturn( securityIdentityDAO ).anyTimes();
        EasyMock.expect( securityIdentityDAO.getSecurityIdentityBySid( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillSecurityIdentity( actualEntity ) ).anyTimes();
        mockControl.replay();
        RoleDTO actualRoleDTO = manager.readRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), ROLE_ID );
        Assert.assertEquals( roleModel.getName(), actualRoleDTO.getName() );
    }

    /**
     * Should throw exception if no role exist.
     */
    @Test
    public void shouldThrowExceptionIfNoRoleExist() {
        thrown.expect( SusException.class );
        thrown.expectMessage( NO_ROLE_EXIST );
        EasyMock.expect( dao.readRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) ).andReturn( null )
                .anyTimes();
        manager.readRole( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), ROLE_ID );
    }

    /**
     * Gets the filled permission DTO.
     *
     * @return the filled permission DTO
     */
    private PermissionDTO getFilledPermissionDTO() {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setManage( true );
        permissionDTO.setMatrexValue( "View" );
        permissionDTO.setMatrixKey( 2 );
        permissionDTO.setValue( 0 );
        return permissionDTO;
    }

    /**
     * Gets the filled resource access control DTO.
     *
     * @return the filled resource access control DTO
     */
    private PermissionManageForm getFilledResourceAccessControlDTO() {
        PermissionManageForm resourceAccessControlDTO = new PermissionManageForm();
        resourceAccessControlDTO.setName( "License" );
        List< PermissionDTO > permissionDTOs = new ArrayList<>();
        permissionDTOs.add( getFilledPermissionDTO() );
        resourceAccessControlDTO.setPermissionDTOs( permissionDTOs );
        return resourceAccessControlDTO;
    }

    /**
     * Populate Filter DTo.
     *
     * @return the filters DTO
     */
    private FiltersDTO populateFilterDTO() {
        FiltersDTO filterDTO = new FiltersDTO();
        filterDTO.setDraw( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setLength( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setStart( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setFilteredRecords( 10L );
        return filterDTO;
    }

    /**
     * Gets the group role list.
     *
     * @return the group role list
     */
    private List< GroupRoleEntity > getGroupRoleList() {
        List< GroupRoleEntity > list = new ArrayList<>();
        list.add( getGroupRoleEntity() );
        return list;
    }

    /**
     * Gets the group role entity.
     *
     * @return the group role entity
     */
    private GroupRoleEntity getGroupRoleEntity() {
        groupRoleEntity.setGroupEntity( getGroupEntity() );
        groupRoleEntity.setRoleEntity( getRoleEntity() );
        groupRoleEntity.setId( GROUP_ROLE_ID );
        return groupRoleEntity;
    }

    /**
     * Gets the role entity.
     *
     * @return the role entity
     */
    private RoleEntity getRoleEntity() {
        roleEntity.setId( ROLE_ID );
        roleEntity.setName( NAME_FIELD );
        roleEntity.setDescription( DESCRIPTION_FIELD );
        roleEntity.setStatus( ACTIVE_ROLE );
        roleEntity.setCreatedOn( new Date() );
        roleEntity.setModifiedOn( new Date() );
        return roleEntity;
    }

    /**
     * Get Group Entity.
     *
     * @return the group entity
     */
    private GroupEntity getGroupEntity() {
        groupEntity.setId( GROUP_ID );
        groupEntity.setName( NAME_FIELD );
        groupEntity.setDescription( DESCRIPTION_FIELD );
        groupEntity.setStatus( ACTIVE_GRP );
        Set< UserEntity > userEntities = new HashSet<>();
        userEntities.add( getUserEntity() );
        groupEntity.setUsers( userEntities );
        return groupEntity;
    }

    /**
     * Gets the role model.
     *
     * @return the role model
     */
    private RoleDTO getRoleModel() {
        roleModel.setId( ROLE_ID );
        roleModel.setName( NAME_FIELD );
        roleModel.setDescription( DESCRIPTION_FIELD );
        roleModel.setStatus( ACTIVE );
        roleModel.setSelectionId( SELECTION_ID.toString() );
        List< SuSUserGroupDTO > suSUserGroupDTOs = new ArrayList<>();
        suSUserGroupDTOs.add( getUserGrpDto() );
        roleModel.setGroups( suSUserGroupDTOs );
        return roleModel;
    }

    /**
     * Gets the user grp dto.
     *
     * @return the user grp dto
     */
    private SuSUserGroupDTO getUserGrpDto() {
        susGroupDto.setId( GROUP_ID );
        susGroupDto.setName( NAME_FIELD );
        susGroupDto.setDescription( DESCRIPTION_FIELD );
        susGroupDto.setStatus( ACTIVE );
        susGroupDto.setSelectionId( SELECTION_ID.toString() );
        List< UserDTO > users = new ArrayList<>();
        users.add( getUserModel() );
        susGroupDto.setUsers( users );
        return susGroupDto;
    }

    /**
     * Gets the user model.
     *
     * @return the user model
     */
    private UserDTO getUserModel() {
        UserDTO userEntity = new UserDTO();
        userEntity.setUserUid( UID );
        userEntity.setName( USER_NAME );
        userEntity.setId( USER_ID.toString() );
        return userEntity;
    }

    /**
     * Gets the user entity.
     *
     * @return the user entity
     */
    private UserEntity getUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId( USER_ID );
        return userEntity;
    }

    /**
     * Fill security identity.
     *
     * @param roleEntity
     *         the role entity
     *
     * @return the security identity entity
     */
    private AclSecurityIdentityEntity fillSecurityIdentity( RoleEntity roleEntity ) {
        AclSecurityIdentityEntity securityIdentityEntity = new AclSecurityIdentityEntity();
        securityIdentityEntity.setId( UUID.randomUUID() );
        securityIdentityEntity.setSid( roleEntity.getId() );
        securityIdentityEntity.setPrinciple( Boolean.FALSE );
        return securityIdentityEntity;
    }

    /**
     * Gets the UI form item.
     *
     * @param label
     *         the label
     * @param name
     *         the name
     * @param type
     *         the type
     * @param value
     *         the value
     * @param help
     *         the help
     * @param placeHolder
     *         the place holder
     *
     * @return the UI form item
     */
    private static UIFormItem getUIFormItem( String label, String name, String type, String value, String help, String placeHolder ) {
        UIFormItem uiFormItem = GUIUtils.createFormItem();
        uiFormItem.setLabel( label );
        uiFormItem.setName( name );
        uiFormItem.setType( type );
        uiFormItem.setValue( value );
        uiFormItem.setHelp( help );
        uiFormItem.setPlaceHolder( placeHolder );
        return uiFormItem;
    }

    /**
     * Gets the selection manager.
     *
     * @return the selection manager
     */
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    /**
     * Sets the selection manager.
     *
     * @param selectionManager
     *         the new selection manager
     */
    public void setSelectionManager( SelectionManager selectionManager ) {
        this.selectionManager = selectionManager;
    }

    /**
     * Prepare user entity.
     *
     * @return the user entity
     */
    private UserEntity prepareUserEntity() {

        UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( SUPER_USER_ID ) );
        userEntity.setFirstName( NAME_FIELD );
        userEntity.setSurName( NAME_FIELD );
        userEntity.setDirectory( prepareDirectoryEntity() );

        return userEntity;
    }

    /**
     * Prepare directory entity.
     *
     * @return the su S user directory entity
     */
    private SuSUserDirectoryEntity prepareDirectoryEntity() {

        SuSUserDirectoryEntity directoryEntity = new SuSUserDirectoryEntity();
        directoryEntity.setId( DIRECTORY_ID );
        directoryEntity.setType( ConstantsUserDirectories.INTERNAL_DIRECTORY );
        directoryEntity.setStatus( ACCOUNT_STATUS_ACTIVE );

        return directoryEntity;
    }

}
