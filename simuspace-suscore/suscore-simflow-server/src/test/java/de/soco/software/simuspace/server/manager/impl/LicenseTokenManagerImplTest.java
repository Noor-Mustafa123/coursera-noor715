package de.soco.software.simuspace.server.manager.impl;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import javax.persistence.EntityManager;

import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.exceptions.SusException;

/***
 * Is the test class for{@link LicenseTokenManagerImpl}.
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( LicenseTokenManagerImpl.class )
public class LicenseTokenManagerImplTest {

    /**
     * The Constant for manager license token limit.
     */
    private static final String MANAGER_TOKE_LIMIT = "2";

    /**
     * The Constant for user License token limit.
     */
    private static final String USER_TOKEN_LIMIT = "3";

    /**
     * The Constant for manager token.
     */
    private static final String MANAGER_TOKEN_1 = "Manager-token-1";

    /**
     * The Constant for manager token.
     */
    private static final String MANAGER_TOKEN_2 = "Manager-token-2";

    /**
     * The Constant for manager token.
     */
    private static final String MANAGER_TOKEN_3 = "Manager-token-3";

    /**
     * The Constant for user token.
     */
    private static final String USER_TOKEN_1 = "User-token-1";

    /**
     * The Constant for user token.
     */
    private static final String USER_TOKEN_2 = "User-token-2";

    /**
     * The Constant for user token.
     */
    private static final String USER_TOKEN_3 = "User-token-3";

    /**
     * The Constant MANAGER_TOKEN_4.
     */
    private static final String MANAGER_TOKEN_4 = "User-token-4";

    /**
     * The Constant for IPAddress.
     */
    private static final String IP_ADDRESS_1 = "IP-1";

    /**
     * The Constant for number of tokens from a single IP.
     */
    private static final int TOKENS_FROM_SINGLE_IP = 1;

    /**
     * The Constant for feature work flow.
     */
    private static final String WORKFLOW_FEATURE_KEY = "Workflow";

    /**
     * The Constant for manager component of feature work flow.
     */
    private static final String WORKFLOW_MANAGER_COMPONENT_KEY = "workflow.manager";

    /**
     * The Constant for user component of feature work flow.
     */
    private static final String WORKFLOW_USER_COMPONENT_KEY = "workflow.user";

    /**
     * The Constant for id of manager.
     */
    private static final UUID MANAGER_ID = UUID.randomUUID();

    /**
     * The Constant for id of user.
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * The Constant for uid of user.
     */
    private static final String UID = "uid";

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The license token manager impl.
     */
    private static LicenseTokenManagerImpl licenseTokenManagerImpl;

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * Sets the objects and mocks the dependencies.
     */
    @BeforeClass
    public static void setUp() throws SusException {
        licenseTokenManagerImpl = new LicenseTokenManagerImpl();

        licenseTokenManagerImpl = spy( licenseTokenManagerImpl );
        doReturn( true ).when( licenseTokenManagerImpl ).isWorkflowManager( EasyMock.anyObject( EntityManager.class ), MANAGER_ID );
        doReturn( false ).when( licenseTokenManagerImpl ).isWorkflowManager( EasyMock.anyObject( EntityManager.class ), USER_ID );
        doReturn( true ).when( licenseTokenManagerImpl ).isWorkflowUser( EasyMock.anyObject( EntityManager.class ), USER_ID );
        doReturn( false ).when( licenseTokenManagerImpl ).isWorkflowUser( EasyMock.anyObject( EntityManager.class ), MANAGER_ID );

    }

}
