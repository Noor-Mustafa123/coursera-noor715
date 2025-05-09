package de.soco.software.simuspace.suscore.executor.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.executor.model.ExecutorPool;

/**
 * The Class PropertiesManager loads properties of executor file.
 *
 * @author Noman Arshad
 */
@Log4j2
public class ExecutorPropertiesManager {

    /**
     * The executor pools.
     */
    private static ExecutorPool executorPools;

    /** The pools. */

    /**
     * Inits the read executor file.
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void init() throws IOException {
        String path = ConstantsKaraf.KARAF_HOME + File.separator + ConstantsKaraf.KARAF_CONF + File.separator
                + ConstantsFileProperties.SUSCORE_EXECUTOR_JSON;

        executorPools = JsonUtils.jsonToObject( org.apache.commons.io.FileUtils.readFileToString( getFileFromKarafConf( path ) ),
                ExecutorPool.class );
    }

    /**
     * Gets the file from karaf conf.
     *
     * @param path
     *         the path
     *
     * @return the file from karaf conf
     */
    public static File getFileFromKarafConf( String path ) {
        log.debug( "Reading executor file" );
        File file = new File( path );
        if ( !file.exists() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey() ) );
        } else {
            log.debug( "Reading success" );
            return file;
        }
    }

    /**
     * @return the executorPools
     */
    public static ExecutorPool getExecutorPools() {
        return executorPools;
    }

}
