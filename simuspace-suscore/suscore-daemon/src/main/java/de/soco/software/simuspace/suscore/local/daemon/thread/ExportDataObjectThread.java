package de.soco.software.simuspace.suscore.local.daemon.thread;

import java.io.File;

import de.soco.software.simuspace.suscore.common.base.TransferManager;
import de.soco.software.simuspace.suscore.common.http.model.TransferInfo;
import de.soco.software.simuspace.suscore.data.model.DataObjectDTO;
import de.soco.software.simuspace.suscore.local.daemon.controller.SyncController;

/**
 * The Class ExportDataObjectThread.
 *
 * @author Noman Arshad
 */
public class ExportDataObjectThread implements Runnable {

    /**
     * The Constant EXPORT_OBJECT.
     */
    private static final String EXPORT_OBJECT = "Export Object";

    /**
     * The data object DTO.
     */
    DataObjectDTO dataObjectDTO;

    /**
     * The is dir.
     */
    boolean isDir;

    /**
     * The export path.
     */
    String exportPath;

    /**
     * The auth token.
     */
    String authToken;

    /**
     * The controller.
     */
    SyncController controller;

    /**
     * The transfer manager.
     */
    TransferManager transferManager;

    /**
     * Instantiates a new export data object thread.
     */
    public ExportDataObjectThread() {
    }

    /**
     * Instantiates a new export data object thread.
     *
     * @param dataObjectDTO
     *         the data object DTO
     * @param isDir
     *         the is dir
     * @param exportPath
     *         the export path
     * @param authToken
     *         the auth token
     */
    public ExportDataObjectThread( DataObjectDTO dataObjectDTO, boolean isDir, String exportPath, String authToken,
            SyncController controller, TransferManager transferManager ) {
        super();
        this.dataObjectDTO = dataObjectDTO;
        this.isDir = isDir;
        this.exportPath = exportPath;
        this.authToken = authToken;
        this.controller = controller;
        this.transferManager = transferManager;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        controller.recursivelyDownloadStructuralObjects( dataObjectDTO, isDir, exportPath, authToken );
        transferManager.getTransfers().put( dataObjectDTO.getName(),
                new TransferInfo( 100L, EXPORT_OBJECT, exportPath + File.separator + dataObjectDTO.getName() ) );
    }

}
