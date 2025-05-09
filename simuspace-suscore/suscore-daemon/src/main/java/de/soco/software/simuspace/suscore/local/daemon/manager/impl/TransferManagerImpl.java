package de.soco.software.simuspace.suscore.local.daemon.manager.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.base.TransferManager;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.enums.TransferOperationType;
import de.soco.software.simuspace.suscore.common.http.model.TransferInfo;
import de.soco.software.simuspace.suscore.common.model.BreadCrumbItemDTO;
import de.soco.software.simuspace.suscore.common.model.CommonLocationDTO;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.FileInfo;
import de.soco.software.simuspace.suscore.common.model.TransferLocationObject;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.local.daemon.manager.SuscoreDaemonManager;
import de.soco.software.simuspace.workflow.model.impl.ObjectDTO;
import de.soco.software.simuspace.workflow.model.impl.ObjectFile;
import de.soco.software.simuspace.workflow.model.impl.ObjectVersion;

/**
 * The Class TransferManagerImpl. Used for downloading data objects and uploading
 */
@Component
public class TransferManagerImpl implements TransferManager {

    /**
     * The Constant TYPE.
     */
    private static final String TYPE = "/type/";

    /**
     * The Constant FILE_PATH.
     */
    private static final String FILE_PATH = "/file";

    /**
     * The Constant DATA_OBJECT.
     */
    private static final String DATA_OBJECT = "data/object/";

    /**
     * The Constant FILE_SEPERATOR.
     */
    private static final String FILE_SEPERATOR = "/";

    /**
     * The Constant BREADCRUMB_VIEW_DATA_PROJECT.
     */
    private static final String BREADCRUMB_VIEW_DATA_PROJECT = "breadcrumb/view/data/project/";

    /**
     * The Constant DEFAULT.
     */
    private static final String DEFAULT = "Default";

    /**
     * The Constant FILE_TYPE_HTML.
     */
    private static final String FILE_TYPE_HTML = "Html";

    /**
     * The daemon manager.
     */
    @Autowired
    private SuscoreDaemonManager daemonManager;

    /**
     * The Constant logger to log error or exceptions of work flow wait element.
     */
    private static final Logger logger = Logger.getLogger( TransferManagerImpl.class.getName() );

    /**
     * The transfers.
     */
    private Map< String, TransferInfo > transfers = new HashMap<>();

    /**
     * Download data object.
     *
     * @param parentId
     *         the parent id
     * @param file
     *         the file
     * @param authToken
     *         the auth token
     *
     * @return true, if successful
     */
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.local.daemon.manager.TransferManager#
     * downloadDataObject(java.lang.String,
     * de.soco.software.simuspace.suscore.common.model.FileInfo, java.lang.String)
     */
    @Override
    public boolean downloadDataObject( String parentId, FileInfo file, String authToken ) {

        Runnable myRunnable = new Runnable() {

            public void run() {
                logger.debug( "Runnable running" );

                try {
                    TransferInfo transferInfo = new TransferInfo();
                    if ( transfers.containsKey( file.getName() ) ) {
                        transferInfo = transfers.get( file.getName() );
                    }

                    String relativeLocalSyncPath = computeLocalSyncPath(
                            SuSClient.getRequest( daemonManager.getServerAPIBase() + BREADCRUMB_VIEW_DATA_PROJECT + parentId,
                                    CommonUtils.prepareHeadersWithAuthToken( authToken ) ) );

                    File projectLocalDir = new File( daemonManager.localSyncDirectory().getLocal_sync_dir() + relativeLocalSyncPath );
                    projectLocalDir.mkdirs();

                    SuSClient.downloadRequestWithProgresBar(
                            daemonManager.getServerAPIBase() + "document/" + file.getFile().getId() + "/version/"
                                    + file.getFile().getVersion().getId() + "/download",
                            projectLocalDir + FILE_SEPERATOR + file.getName(), prepareDownloadHeaders( authToken ), null, transferInfo );
                    logger.debug( "downloaded :" + file.getName() );

                } catch ( Exception e ) {
                    ExceptionLogger.logException( e, getClass() );
                }
            }
        };

        Thread thread = new Thread( myRunnable );
        thread.start();
        try {
            thread.join();
        } catch ( InterruptedException e ) {
            ExceptionLogger.logException( e, getClass() );
        }
        return true;

    }

    /**
     * Download data object from location.
     *
     * @param parentId
     *         the parent id
     * @param file
     *         the file
     * @param authToken
     *         the auth token
     * @param locations
     *         the locations
     *
     * @return true, if successful
     */
    @Override
    public boolean downloadDataObjectFromLocation( String parentId, FileInfo file, String authToken, List< CommonLocationDTO > locations ) {

        Runnable myRunnable = new Runnable() {

            public void run() {
                logger.debug( "Runnable running" );

                try {
                    TransferInfo transferInfo = new TransferInfo();
                    if ( transfers.containsKey( file.getName() ) ) {
                        transferInfo = transfers.get( file.getName() );
                    }

                    String relativeLocalSyncPath = computeLocalSyncPath(
                            SuSClient.getRequest( daemonManager.getServerAPIBase() + BREADCRUMB_VIEW_DATA_PROJECT + parentId,
                                    CommonUtils.prepareHeadersWithAuthToken( authToken ) ) );

                    File projectLocalDir = new File( daemonManager.localSyncDirectory().getLocal_sync_dir() + relativeLocalSyncPath );
                    projectLocalDir.mkdirs();

                    for ( CommonLocationDTO commonLocationDTO : locations ) {
                        try {
                            // check if location is up
                            SusResponseDTO request1 = SuSClient.getRequest( daemonManager.getServerAPIBase() + "core/location/isup",
                                    CommonUtils.prepareHeadersWithAuthToken( commonLocationDTO.getAuthToken() ) );
                            String json = JsonUtils.toJson( request1.getData() );
                            String responseServerIsUp = JsonUtils.jsonToObject( json, String.class );
                            // if location up then begin downloading file
                            if ( responseServerIsUp != null && !responseServerIsUp.isEmpty() ) {
                                String filePath = "";
                                // downloading file from other location to this location vault
                                if ( file.getType().toString().equalsIgnoreCase( FILE_TYPE_HTML ) ) {
                                    File htmlProjectLocalDir = new File( daemonManager.localSyncDirectory().getLocal_sync_dir()
                                            + relativeLocalSyncPath + FILE_SEPERATOR + file.getName() );
                                    htmlProjectLocalDir.mkdirs();

                                    if ( file.getFile().getType().toString().equalsIgnoreCase( "text/plain" ) ) {
                                        filePath = htmlProjectLocalDir + FILE_SEPERATOR + file.getName() + ".json";
                                    } else if ( file.getFile().getType().toString().equalsIgnoreCase( "application/zip" ) ) {
                                        filePath = htmlProjectLocalDir + FILE_SEPERATOR + file.getName() + ".zip";
                                    }

                                } else {
                                    filePath = projectLocalDir + FILE_SEPERATOR + file.getName();
                                }
                                copyFileInToVaultIfFileExistsOnAnyOtherLocation( file, commonLocationDTO,
                                        daemonManager.getServerAPIBase().split( "/api", 5 ), filePath );

                                // download documet from vault to local sycnh path
                                SuSClient.downloadRequestWithProgresBar(
                                        daemonManager.getServerAPIBase() + "document/" + file.getFile().getId() + "/version/"
                                                + file.getFile().getVersion().getId() + "/download",
                                        filePath, prepareDownloadHeaders( authToken ), null, transferInfo );
                                downloadAdditionalFilesOfObject( file, commonLocationDTO, projectLocalDir, transferInfo, authToken );
                                logger.debug( "downloaded :" + file.getName() );
                                // break location downloading when complete
                                break;
                            }

                        } catch ( Exception e ) {
                        }

                    }

                } catch ( Exception e ) {
                    ExceptionLogger.logException( e, getClass() );
                }
            }
        };

        Thread thread = new Thread( myRunnable );
        thread.start();
        try {
            thread.join();
        } catch ( InterruptedException e ) {
            ExceptionLogger.logException( e, getClass() );
        }
        return true;

    }

    /**
     * Download additional files.
     *
     * @param file
     *         the file
     * @param commonLocationDTO
     *         the common location DTO
     * @param projectLocal
     *         Dir the projectLocal Dir
     */
    private void downloadAdditionalFilesOfObject( FileInfo file, CommonLocationDTO commonLocationDTO, File projectLocalDir,
            TransferInfo transferInfo, String authToken ) {
        if ( file.getAdditionalFiles() != null && !file.getAdditionalFiles().isEmpty() ) {
            for ( DocumentDTO document : file.getAdditionalFiles() ) {
                try {
                    copyFileInToVaultIfFileExistsOnAnyOtherLocation( document, commonLocationDTO,
                            daemonManager.getServerAPIBase().split( "/api", 5 ), projectLocalDir + FILE_SEPERATOR + document.getName() );

                    // download documet from vault to local sycnh path
                    SuSClient.downloadRequestWithProgresBar(
                            daemonManager.getServerAPIBase() + "document/" + document.getId() + "/version/" + document.getVersion().getId()
                                    + "/download",
                            projectLocalDir + FILE_SEPERATOR + document.getName(), prepareDownloadHeaders( authToken ), null,
                            transferInfo );
                } catch ( Exception e ) {
                }
            }
        }
    }

    /**
     * Copy file in to vault if file exists on any other location.
     *
     * @param file
     *         the file
     * @param commonLocationDTO
     *         the common location DTO
     * @param serverAdress
     *         the server adress
     * @param synchPath
     *         the synch path
     */
    private void copyFileInToVaultIfFileExistsOnAnyOtherLocation( FileInfo file, CommonLocationDTO commonLocationDTO, String[] serverAdress,
            String synchPath ) {
        TransferLocationObject transferLocationObject = new TransferLocationObject( file.getFile().getPath(), serverAdress[ 0 ],
                commonLocationDTO.getAuthToken(), TransferOperationType.COPY );
        final String jsonObjectDTO = JsonUtils.toJsonString( transferLocationObject );
        SuSClient.downloadRequest(
                commonLocationDTO.getUrl() + "/api/core/location/download/vault/file/" + file.getFile().getId() + "/version/"
                        + file.getFile().getVersion().getId(),
                synchPath, CommonUtils.prepareHeadersWithAuthToken( commonLocationDTO.getAuthToken() ), null );
    }

    /**
     * Copy file in to vault if file exists on any other location.
     *
     * @param file
     *         the file
     * @param commonLocationDTO
     *         the common location DTO
     * @param serverAdress
     *         the server adress
     * @param synchPath
     *         the synch path
     */
    private void copyFileInToVaultIfFileExistsOnAnyOtherLocation( DocumentDTO document, CommonLocationDTO commonLocationDTO,
            String[] serverAdress, String synchPath ) {
        TransferLocationObject transferLocationObject = new TransferLocationObject( document.getPath(), serverAdress[ 0 ],
                commonLocationDTO.getAuthToken(), TransferOperationType.COPY );
        final String jsonObjectDTO = JsonUtils.toJsonString( transferLocationObject );
        SuSClient.downloadRequest(
                commonLocationDTO.getUrl() + "/api/core/location/download/vault/file/" + document.getId() + "/version/"
                        + document.getVersion().getId(),
                synchPath, CommonUtils.prepareHeadersWithAuthToken( commonLocationDTO.getAuthToken() ), null );
    }

    /**
     * Gets the server vault path.
     *
     * @param authToken
     *         the auth token
     *
     * @return the server vault path
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private String getServerVaultPath( String authToken ) throws IOException {
        SusResponseDTO dataResponse = SuSClient.getRequest( daemonManager.getServerAPIBase() + "data/project/propertymanager/vault",
                CommonUtils.prepareHeadersWithAuthToken( authToken ) );
        return JsonUtils.jsonToObject( JsonUtils.toJson( dataResponse.getData() ), String.class );
    }

    /**
     * Prepare download headers.
     *
     * @param authToken
     *         the auth token
     *
     * @return the map
     */
    private Map< String, String > prepareDownloadHeaders( String authToken ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, authToken );

        return requestHeaders;

    }

    /**
     * Compute local sync path.
     *
     * @param request
     *         the request
     *
     * @return the string
     */
    private String computeLocalSyncPath( SusResponseDTO request ) {
        StringBuilder path = new StringBuilder();
        String json = JsonUtils.toJson( request.getData() );
        List< BreadCrumbItemDTO > bcList = JsonUtils.jsonToList( json, BreadCrumbItemDTO.class );
        for ( BreadCrumbItemDTO bc : bcList ) {
            path.append( FILE_SEPERATOR );
            path.append( bc.getName() );
        }
        String root = null;
        for ( Path path2 : new File( path.toString() ).toPath() ) {
            root = path2.toString();
            break;
        }
        return path.toString().replace( root, "Data" );
    }

    /**
     * Gets the transfers.
     *
     * @return the transfers
     */
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.local.daemon.manager.TransferManager#
     * getTransfers()
     */
    public Map< String, TransferInfo > getTransfers() {
        return transfers;
    }

    /**
     * Gets the daemon manager.
     *
     * @return the daemon manager
     */
    public SuscoreDaemonManager getDaemonManager() {
        return daemonManager;
    }

    /**
     * Sets the daemon manager.
     *
     * @param daemonManager
     *         the new daemon manager
     */
    public void setDaemonManager( SuscoreDaemonManager daemonManager ) {
        this.daemonManager = daemonManager;
    }

    /**
     * Upload data object.
     *
     * @param containerId
     *         the container id
     * @param fileName
     *         the file name
     * @param authToken
     *         the auth token
     * @param objectId
     *         the object id
     * @param typeId
     *         the type id
     * @param size
     *         the size
     *
     * @return true, if successful
     */
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.local.daemon.manager.TransferManager#
     * uploadDataObject(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public boolean uploadDataObject( String containerId, String fileName, String authToken, String objectId, String typeId, String size ) {

        try {
            TransferInfo transferInfo = new TransferInfo();
            if ( transfers.containsKey( fileName ) ) {
                transferInfo = transfers.get( fileName );
            }
            String relativeLocalSyncPath = computeLocalSyncPath(
                    SuSClient.getRequest( daemonManager.getServerAPIBase() + BREADCRUMB_VIEW_DATA_PROJECT + containerId,
                            CommonUtils.prepareHeadersWithAuthToken( authToken ) ) );

            File projectLocalDir = new File( daemonManager.localSyncDirectory().getLocal_sync_dir() + relativeLocalSyncPath );

            String address = daemonManager.getServerAPIBase() + "document/upload";
            File file = new File( projectLocalDir + FILE_SEPERATOR + fileName );

            DocumentDTO documentDTO = SuSClient.uploadFileRequestWithProgress( address, file, prepareDownloadHeaders( authToken ),
                    transferInfo );
            logger.debug( "updoaded :" + fileName );
            if ( objectId == null ) {
                // create dataobject
                ObjectDTO objectDTO = new ObjectDTO( fileName, new ObjectFile( documentDTO.getId(), new ObjectVersion( 1 ), size ), typeId,
                        size );
                String jsonObjectDTO = JsonUtils.toJsonString( objectDTO );
                SuSClient.postRequest( daemonManager.getServerAPIBase() + DATA_OBJECT + containerId + TYPE + typeId + "/refresh",
                        jsonObjectDTO, CommonUtils.prepareHeadersWithAuthToken( authToken ) );

            } else {
                // update existing dataobject
                Map< String, DocumentDTO > docMap = new HashMap< String, DocumentDTO >();
                docMap.put( "file", documentDTO );
                String jsonObjectDTO = JsonUtils.toJsonString( docMap );
                SuSClient.putRequest( daemonManager.getServerAPIBase() + DATA_OBJECT + objectId + FILE_PATH,
                        CommonUtils.prepareHeadersWithAuthToken( authToken ), jsonObjectDTO );
            }

        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return false;
        }

        return true;

    }

    /**
     * Upload data object to locations.
     *
     * @param containerId
     *         the container id
     * @param fileName
     *         the file name
     * @param authToken
     *         the auth token
     * @param objectId
     *         the object id
     * @param typeId
     *         the type id
     * @param size
     *         the size
     * @param locations
     *         the locations
     *
     * @return true, if successful
     */
    @Override
    public boolean uploadDataObjectToLocations( String containerId, String fileName, String authToken, String objectId, String typeId,
            String size, List< CommonLocationDTO > locations ) {
        try {
            TransferInfo transferInfo = new TransferInfo();
            if ( transfers.containsKey( fileName ) ) {
                transferInfo = transfers.get( fileName );
            }
            String relativeLocalSyncPath = computeLocalSyncPath(
                    SuSClient.getRequest( daemonManager.getServerAPIBase() + BREADCRUMB_VIEW_DATA_PROJECT + containerId,
                            CommonUtils.prepareHeadersWithAuthToken( authToken ) ) );
            File projectLocalDir = new File( daemonManager.localSyncDirectory().getLocal_sync_dir() + relativeLocalSyncPath );
            String address = daemonManager.getServerAPIBase() + "document/upload";

            if ( typeId.equalsIgnoreCase( FILE_TYPE_HTML ) ) {
                Map< String, DocumentDTO > docMap = new HashMap< String, DocumentDTO >();

                File htmlJsonFile = new File( projectLocalDir + FILE_SEPERATOR + fileName + FILE_SEPERATOR + fileName + ".json" );
                DocumentDTO htmlJsonDocumentDTO = null;
                if ( htmlJsonFile.exists() ) {
                    htmlJsonDocumentDTO = SuSClient.uploadFileRequestWithProgress( address, htmlJsonFile,
                            prepareDownloadHeaders( authToken ), transferInfo );
                    htmlJsonDocumentDTO.setHash(
                            de.soco.software.simuspace.suscore.common.util.FileUtils.getAdler32CheckSumForLocalFile( htmlJsonFile ) );
                    docMap.put( "file", htmlJsonDocumentDTO );
                    logger.debug( "Uploaded :" + htmlJsonFile.getName() );
                }

                File htmlZipFile = new File( projectLocalDir + FILE_SEPERATOR + fileName + FILE_SEPERATOR + fileName + ".zip" );
                DocumentDTO htmlZipDocumentDTO = null;
                if ( htmlZipFile.exists() ) {
                    htmlZipDocumentDTO = SuSClient.uploadFileRequestWithProgress( address, htmlZipFile, prepareDownloadHeaders( authToken ),
                            transferInfo );
                    htmlZipDocumentDTO.setHash(
                            de.soco.software.simuspace.suscore.common.util.FileUtils.getAdler32CheckSumForLocalFile( htmlZipFile ) );
                    docMap.put( "zipFile", htmlZipDocumentDTO );
                    logger.debug( "Uploaded :" + htmlZipFile.getName() );
                }
                String jsonObjectDTO = JsonUtils.toJsonString( docMap );
                SuSClient.putRequest( daemonManager.getServerAPIBase() + DATA_OBJECT + objectId + FILE_PATH,
                        CommonUtils.prepareHeadersWithAuthToken( authToken ), jsonObjectDTO );

            } else {
                File file = new File( projectLocalDir + FILE_SEPERATOR + fileName );

                DocumentDTO documentDTO = SuSClient.uploadFileRequestWithProgress( address, file, prepareDownloadHeaders( authToken ),
                        transferInfo );
                documentDTO.setHash( de.soco.software.simuspace.suscore.common.util.FileUtils.getAdler32CheckSumForLocalFile( file ) );

                if ( locations != null ) {

                    for ( CommonLocationDTO commonLocationDTO : locations ) {
                        logger.debug( "preparedFile.getLocation() " + commonLocationDTO.getName() );
                        try {

                            if ( !StringUtils.areEqual( commonLocationDTO.getName(), DEFAULT ) ) {

                                // check if location is up
                                SusResponseDTO request1 = SuSClient.getRequest( daemonManager.getServerAPIBase() + "core/location/isup",
                                        CommonUtils.prepareHeadersWithAuthToken( commonLocationDTO.getAuthToken() ) );
                                String json = JsonUtils.toJson( request1.getData() );
                                String responseServerIsUp = JsonUtils.jsonToObject( json, String.class );

                                // if location up then begin uploading file
                                if ( responseServerIsUp != null && !responseServerIsUp.isEmpty() ) {
                                    // uploading file to target locations
                                    TransferLocationObject transferLocationObject = new TransferLocationObject( documentDTO.getPath(),
                                            commonLocationDTO.getUrl(), commonLocationDTO.getAuthToken(), TransferOperationType.COPY );
                                    SuSClient.postRequest( daemonManager.getServerAPIBase() + "core/location/export/vault/file",
                                            JsonUtils.toJsonString( transferLocationObject ),
                                            CommonUtils.prepareHeadersWithAuthToken( commonLocationDTO.getAuthToken() ) );

                                }
                            }

                        } catch ( Exception e ) {
                        }
                    }
                }

                logger.debug( "updoaded :" + fileName );
                if ( objectId == null ) {
                    // create dataobject
                    ObjectDTO objectDTO = new ObjectDTO( fileName, new ObjectFile( documentDTO.getId(), new ObjectVersion( 1 ), size ),
                            typeId, size );
                    String jsonObjectDTO = JsonUtils.toJsonString( objectDTO );
                    SuSClient.postRequest( daemonManager.getServerAPIBase() + DATA_OBJECT + containerId + TYPE + typeId + "/refresh",
                            jsonObjectDTO, CommonUtils.prepareHeadersWithAuthToken( authToken ) );

                } else {
                    // update existing dataobject
                    Map< String, DocumentDTO > docMap = new HashMap< String, DocumentDTO >();
                    docMap.put( "file", documentDTO );
                    String jsonObjectDTO = JsonUtils.toJsonString( docMap );
                    SuSClient.putRequest( daemonManager.getServerAPIBase() + DATA_OBJECT + objectId + FILE_PATH,
                            CommonUtils.prepareHeadersWithAuthToken( authToken ), jsonObjectDTO );
                }
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return false;
        }

        return true;

    }

    /**
     * Upload directory.
     *
     * @param containerId
     *         the container id
     * @param fileName
     *         the file name
     * @param authToken
     *         the auth token
     * @param objectId
     *         the object id
     * @param objectType
     *         the object type
     *
     * @return true, if successful
     */
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.local.daemon.manager.TransferManager#
     * uploadDirectory(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public boolean uploadDirectory( String containerId, String fileName, String authToken, String objectId, String objectType ) {
        try {

            if ( objectId == null ) {
                // create dataobject
                ObjectDTO objectDTO = new ObjectDTO();
                objectDTO.setName( fileName );
                objectDTO.setTypeId( objectType );
                objectDTO.setParentId( containerId );

                String jsonObjectDTO = JsonUtils.toJsonString( objectDTO );
                SuSClient.postRequest( daemonManager.getServerAPIBase() + DATA_OBJECT + containerId + TYPE + objectType, jsonObjectDTO,
                        CommonUtils.prepareHeadersWithAuthToken( authToken ) );

            } else {
                // update existing dataobject
                Map< String, DocumentDTO > docMap = new HashMap< String, DocumentDTO >();
                docMap.put( "file", new DocumentDTO() );
                String jsonObjectDTO = JsonUtils.toJsonString( docMap );
                SuSClient.putRequest( daemonManager.getServerAPIBase() + DATA_OBJECT + objectId + FILE_PATH,
                        CommonUtils.prepareHeadersWithAuthToken( authToken ), jsonObjectDTO );
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            return false;
        }
        return true;
    }

}