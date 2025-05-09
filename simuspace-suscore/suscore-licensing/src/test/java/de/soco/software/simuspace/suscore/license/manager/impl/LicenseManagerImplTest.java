package de.soco.software.simuspace.suscore.license.manager.impl;

import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.model.ModuleLicenseDTO;
import de.soco.software.simuspace.suscore.common.util.BundleUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.license.manager.LicenseConfigurationManager;

/**
 * The Class is responsible to provide the public functions of class {@link LicenseManagerImpl}.
 *
 * @author M.Nasir.Farooq
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { BundleUtils.class } )
public class LicenseManagerImplTest {

    /**
     * The Constant MOCK_FUNCTION_OF_BUNDLE_UTILS_GET_USER_ID_FROM_MESSAGE.
     */
    private static final String MOCK_FUNCTION_OF_BUNDLE_UTILS_GET_USER_ID_FROM_MESSAGE = "getUserIdFromMessage";

    /**
     * The Constant YES_USER_IS_VALID.
     */
    private static final boolean YES_USER_IS_VALID = true;

    /**
     * The Constant OBJECT_INDEX.
     */
    private static final int OBJECT_INDEX = 0;

    /**
     * The Constant LICENSE_CONFIG_FILE_PATH.
     */
    private static final String LICENSE_CONFIG_FILE_PATH = "/license.json";

    /**
     * The Constant USER_ID.
     */
    private static final String USER_ID = UUID.randomUUID().toString();

    /**
     * The Constant MODULE.
     */
    private static final String MODULE = "RunSim";

    /**
     * The Constant MODULE_2.
     */
    private static final String MODULE_2 = "Search";

    /**
     * The Constant NULL_MODULE.
     */
    private static final String NULL_MODULE = null;

    /**
     * The Constant EMPTY_MODULE.
     */
    private static final String EMPTY_MODULE = "";

    /**
     * The Constant ALLOWED_USERS.
     */
    private static final int ALLOWED_USERS = 4;

    /**
     * The Constant RESTRICTED_USERS.
     */
    private static final int RESTRICTED_USERS = 4;

    /**
     * The Constant DUMMY_UID.
     */
    private static final String DUMMY_UID = "sces122";

    /**
     * The Constant DUMMY_PASSWORD.
     */
    private static final String DUMMY_PASSWORD = "abc123";

    /**
     * The Constant NULL_USER_ID.
     */
    private static final String NULL_USER_ID = null;

    /**
     * The license manager impl.
     */
    private LicenseManagerImpl licenseManagerImpl;

    /**
     * The configuration manager.
     */
    private LicenseConfigurationManager configurationManager;

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Setup.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        licenseManagerImpl = new LicenseManagerImpl();
        configurationManager = mockControl.createMock( LicenseConfigurationManager.class );
        licenseManagerImpl.setLicenseConfigManager( configurationManager );
    }

    /**
     * Fill license DTO.
     *
     * @return the licenseDTO
     */
    private ModuleLicenseDTO fillLicenseDTO() {
        ModuleLicenseDTO license = JsonUtils.jsonStreamToObject( this.getClass().getResourceAsStream( LICENSE_CONFIG_FILE_PATH ),
                ModuleLicenseDTO.class );
        return license;
    }

    private static void mockStaticFunctions() throws Exception {
        PowerMockito.spy( BundleUtils.class );
        PowerMockito.doReturn( USER_ID.toString() ).when( BundleUtils.class, MOCK_FUNCTION_OF_BUNDLE_UTILS_GET_USER_ID_FROM_MESSAGE,
                Matchers.any( org.apache.cxf.message.Message.class ) );
    }

}
