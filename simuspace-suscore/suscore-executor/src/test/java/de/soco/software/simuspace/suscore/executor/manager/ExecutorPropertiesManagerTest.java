package de.soco.software.simuspace.suscore.executor.manager;

import java.io.File;
import java.util.HashMap;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;

/**
 * The Class ExecutorPropertiesManagerTest for testing property manager class and testing executor file
 *
 * @author noman arshad
 */
public class ExecutorPropertiesManagerTest {

    /**
     * The Constant EXECUTOR_CONFIG_PATH.
     */
    private static final String EXECUTOR_CONFIG_PATH = ConstantsString.TEST_RESOURCE_PATH + "executor.json";

    /**
     * The Constant EXECUTOR_FILE_NAME.
     */
    private static final String EXECUTOR_FILE_NAME = "executor.json";

    /**
     * The Constant WRONGE_EXECUTOR_CONFIG_PATH.
     */
    private static final String WRONGE_EXECUTOR_CONFIG_PATH = ConstantsString.TEST_RESOURCE_PATH + "sim.executerrs.cfg";

    /**
     * The Constant FILE_NOT_EXIST.
     */
    private static final String FILE_NOT_EXIST = "File path does Not Exist :{0}";

    /**
     * The thrown.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * The Constant mockControl.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The executor file.
     */
    private HashMap< String, String > executorFile = new HashMap<>();

    /**
     * Setup.
     */
    @BeforeClass
    public static void setup() {
        mockControl.resetToNice();

    }

    /**
     * Read executor file.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void readExecutorFile() throws Exception {
        File executorFile = ExecutorPropertiesManager.getFileFromKarafConf( EXECUTOR_CONFIG_PATH );
        Assert.assertEquals( EXECUTOR_FILE_NAME, executorFile.getName() );

    }

    /**
     * Should throw exception when wronge path provided.
     */
    @Test
    public void ShouldThrowexceptionWhenWrongePathProvided() {
        thrown.expect( SusException.class );
        thrown.expectMessage( FILE_NOT_EXIST );
        ExecutorPropertiesManager.getFileFromKarafConf( WRONGE_EXECUTOR_CONFIG_PATH );

    }

}
