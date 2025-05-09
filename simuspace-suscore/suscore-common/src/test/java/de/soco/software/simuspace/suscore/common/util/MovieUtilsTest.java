package de.soco.software.simuspace.suscore.common.util;

import java.io.File;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * Test Class for MovieUtilsTest .
 *
 * @author Nosheen.Sharif
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { PropertiesManager.class } )
public class MovieUtilsTest {

    /**
     * The Constant DUMMY_OUT_PUT_FILE.
     */
    private static final String DUMMY_OUT_PUT_FILE = "/src/test/resources/movie";

    /**
     * The Constant DUMMY_VAULT_PATH.
     */
    private static final String DUMMY_VAULT_PATH = "/src/test/resources/movie";

    /**
     * The Constant DUMMYPATH.
     */
    private static final String DUMMYPATH = "/src/test/resources/movie";

    /**
     * The Constant mockControl.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The thrown.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * The srcfile name.
     */
    private String srcfileName = "movie";

    /**
     * Setup.
     *
     * @throws Exception
     *         the exception
     */
    @Before
    public void setup() throws Exception {
        mockControl.resetToNice();

        PowerMockito.spy( PropertiesManager.class );
        PowerMockito.doReturn( ConstantsKaraf.KARAF_HOME ).when( PropertiesManager.class, "getKarafPath" );

        mockControl.replay();
    }

    /**
     * Tear.
     */
    @After
    public void tear() {
        File f = new File( DUMMY_OUT_PUT_FILE );
        if ( f.exists() ) {
            f.delete();
        }
    }

    /**
     * Should successfully prepare thumb nail file for movie.
     */
    @Test
    public void shouldSuccesfullyPrepareThumbNailFileForMovie() {

        String actual = MovieUtils.prepareThumbnailFromMovieFile( DUMMYPATH, srcfileName, DUMMY_OUT_PUT_FILE );
        Assert.assertTrue( actual.contains( DUMMYPATH ) );
    }

}
