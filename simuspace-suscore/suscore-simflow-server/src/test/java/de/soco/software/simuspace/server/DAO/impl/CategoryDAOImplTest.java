package de.soco.software.simuspace.server.DAO.impl;

import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.server.dao.impl.CategoryDAOImpl;
import de.soco.software.simuspace.suscore.data.activator.Activator;

/**
 * Test Case for CategoryDAOImpl Class.
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { Activator.class } )
public class CategoryDAOImplTest {

    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The category dao refernce .
     */
    private CategoryDAOImpl categoryDao;

    /**
     * The Constant CATEGORY_ID.
     */
    private static final UUID CATEGORY_ID = UUID.randomUUID();

    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * Sets the up. Called before each method
     *
     * @throws Exception
     *         the exception
     */

    @Before
    public void setUp() throws Exception {
        HibernateTestConfigration.setUp();
        PowerMockito.spy( Activator.class );
        PowerMockito.when( Activator.getSession() ).thenReturn( HibernateTestConfigration.getSession() );
        categoryDao = new CategoryDAOImpl();

    }

    /**
     * Replay configration.
     */
    private void replayConfigration() {
        HibernateTestConfigration.mockControl().replay();

    }

}
