package de.soco.software.simuspace.suscore.local.daemon.manager;

import java.io.IOException;

import de.soco.software.simuspace.suscore.local.daemon.model.FileExtension;
import de.soco.software.simuspace.suscore.local.daemon.model.LocalProjectConfig;
import de.soco.software.simuspace.suscore.local.daemon.model.SyncJson;

/**
 * The Interface of the business class responsible for communications with susclient engine and process accordingly.
 *
 * @author Nasir.Farooq
 */
public interface SuscoreDaemonManager {

    /**
     * Local sync directory.
     *
     * @return the map
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public LocalProjectConfig localSyncDirectory() throws IOException;

    /**
     * Local sync config.
     *
     * @return the local sync config
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public SyncJson localSyncConfig() throws IOException;

    /**
     * Gets the server API base.
     *
     * @return the server API base
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    String getServerAPIBase() throws IOException;

    /**
     * Local extension file config.
     *
     * @return the file extension
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    FileExtension localExtensionFileConfig() throws IOException;

}