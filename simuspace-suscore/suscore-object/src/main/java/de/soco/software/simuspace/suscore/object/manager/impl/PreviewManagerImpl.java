package de.soco.software.simuspace.suscore.object.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.log4j.Log4j;

import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsImageFileTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.EncryptionDecryptionDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.EncryptAndDecryptUtils;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.ImageUtil;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.SuSVaultUtils;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.DataObject3DceetronEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectCurveEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectHtmlsEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectImageEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectMovieEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectPDFEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectPredictionModelEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.EncryptionDecryptionEntity;
import de.soco.software.simuspace.suscore.data.entity.LocationEntity;
import de.soco.software.simuspace.suscore.data.model.DataObjectCurveDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectHtmlDTO;
import de.soco.software.simuspace.suscore.document.manager.DocumentManager;
import de.soco.software.simuspace.suscore.executor.service.ThreadPoolExecutorService;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.simuspace.suscore.object.manager.PreviewManager;
import de.soco.software.simuspace.suscore.object.threads.ImageToThumbnailThread;
import de.soco.software.simuspace.suscore.object.threads.MovieffmpegThread;
import de.soco.software.simuspace.suscore.object.threads.MovieffmpegThreadForHtml;

/**
 * The type Preview manager.
 */
@Log4j
public class PreviewManagerImpl implements PreviewManager {

    /**
     * The constant VIDEO_X_MATROSKA.
     */
    private static final String VIDEO_X_MATROSKA = "application/x-matroska";

    /**
     * The constant VIDEO_MP4.
     */
    private static final String VIDEO_MP4 = "video/mp4";

    /**
     * The constant VIDEO_QUICKTIME.
     */
    private static final String VIDEO_QUICKTIME = "video/quicktime";

    /**
     * The constant WEBM_FORMAT.
     */
    private static final String WEBM_FORMAT = ".webm";

    /**
     * The Document manager.
     */
    private DocumentManager documentManager;

    /**
     * The Thread pool executor service.
     */
    private ThreadPoolExecutorService threadPoolExecutorService;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Config manager.
     */
    private ObjectTypeConfigManager configManager;

    /**
     * Create preview.
     *
     * @param entityManager
     *         the entity manager
     * @param documentDTO
     *         the document dto
     * @param userId
     *         the user id
     * @param dataObjectEntity
     *         the data object entity
     * @param documentEntity
     *         the document entity
     * @param objectDTO
     *         the object dto
     */
    @Override
    public void createPreview( EntityManager entityManager, DocumentDTO documentDTO, String userId, DataObjectEntity dataObjectEntity,
            DocumentEntity documentEntity, Object objectDTO ) {
        List< LocationEntity > locationEntityList = documentEntity.getLocations();
        writeToDiskInFETempByLocation( entityManager, documentDTO, locationEntityList );
        if ( dataObjectEntity instanceof DataObjectMovieEntity dataObjectMovieEntity && documentDTO.getType() != null
                && ( documentDTO.getType().equalsIgnoreCase( VIDEO_QUICKTIME ) || documentDTO.getType().equalsIgnoreCase( VIDEO_MP4 )
                || documentDTO.getType().equalsIgnoreCase( VIDEO_X_MATROSKA ) ) ) {
            createDataObjectMovieFilesInFeTemp( entityManager, documentDTO, userId, dataObjectMovieEntity );
        } else if ( dataObjectEntity instanceof DataObjectImageEntity && ( documentDTO.getType() != null ) ) {
            createDataObjectThumbNail( entityManager, userId, dataObjectEntity, documentDTO );
        } else if ( dataObjectEntity instanceof DataObjectCurveEntity dataObjectCurveEntity && ( documentDTO.getType() != null ) ) {
            createDataObjectCurve( entityManager, userId, dataObjectCurveEntity, documentDTO );
        } else if ( dataObjectEntity instanceof DataObjectPredictionModelEntity dataObjectPredictionModelEntity ) {
            createDataObjectPredictionModelFilesInFETemp( entityManager, dataObjectPredictionModelEntity, documentDTO );
        } else if ( dataObjectEntity instanceof DataObjectHtmlsEntity dataObjectHtmlsEntity ) {
            createDataObjectHtmlFilesInFETemp( dataObjectHtmlsEntity, documentDTO, ( DataObjectHtmlDTO ) objectDTO );
        } else if ( dataObjectEntity instanceof DataObject3DceetronEntity dataObject3DceetronEntity ) {
            createDataObjectCeetronFilesInFeCee( dataObject3DceetronEntity );
        } else if ( dataObjectEntity instanceof DataObjectPDFEntity ) {
            createDataObjectPDFPreviewInFeTemp( entityManager, documentDTO );
        }
    }

    /**
     * Write to disk in fe temp by location.
     *
     * @param entityManager
     *         the entity manager
     * @param documentDTO
     *         the document dto
     * @param locationEntityList
     *         the location entity list
     */
    private void writeToDiskInFETempByLocation( EntityManager entityManager, DocumentDTO documentDTO,
            List< LocationEntity > locationEntityList ) {
        boolean presentOnDefault = false;
        LocationEntity locationEntity = null;
        for ( LocationEntity location : locationEntityList ) {
            if ( location.getId().toString().equals( LocationsEnum.DEFAULT_LOCATION.getId() ) ) {
                presentOnDefault = true;
                break;
            } else {
                locationEntity = location;
            }
        }
        if ( documentDTO != null ) {
            if ( presentOnDefault ) {
                try {
                    documentManager.writeToDiskInFETemp( entityManager, documentDTO, PropertiesManager.getFeStaticPath() );
                } catch ( Exception e ) {
                    log.error( "Object not found in vault " + documentDTO.getName(), e );
                    throw new SusException( "Object not found in vault " + documentDTO.getName() );
                }
            } else if ( locationEntity != null ) {
                try ( InputStream ignored = readFileFromLocationIfNotInDefault( documentDTO, locationEntity ) ) {
                    SuSVaultUtils.copyFileFromTmpToVault( documentDTO.getPath() );
                } catch ( IOException e ) {
                    log.error( "Tmp To Vault copying failed", e );
                    throw new SusException( "Tmp To Vault copying failed" );
                }
                documentManager.writeToDiskInFETemp( entityManager, documentDTO, PropertiesManager.getFeStaticPath() );
            }
        }
    }

    /**
     * Read file from location if not in default input stream.
     *
     * @param documentDTO
     *         the document dto
     * @param locationEntity
     *         the location entity
     *
     * @return the input stream
     */
    private InputStream readFileFromLocationIfNotInDefault( DocumentDTO documentDTO, LocationEntity locationEntity ) {
        InputStream fileStream = null;

        try {
            fileStream = documentManager.readVaultFromDisk( documentDTO );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }

        if ( fileStream == null ) {
            String hex = CommonUtils.getHex( UUID.fromString( documentDTO.getId() ), documentDTO.getVersion().getId() );
            File hexDir = new File(
                    PropertiesManager.getDefaultServerTempPath() + File.separator + hex.substring( ConstantsInteger.INTEGER_VALUE_ZERO,
                            ConstantsInteger.INTEGER_VALUE_TWO ) );
            hexDir.mkdirs();
            try {
                SuSClient.downloadRequest(
                        locationEntity.getUrl() + "/api/core/location/download/vault/file/" + documentDTO.getId() + "/version/"
                                + documentDTO.getVersion().getId(), hexDir.getPath(),
                        CommonUtils.prepareHeadersWithAuthToken( locationEntity.getAuthToken() ), null );
                fileStream = documentManager.readDocumentFromTemp( documentDTO );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }
        }
        return fileStream;
    }

    /**
     * Create data object movie files in fe temp.
     *
     * @param entityManager
     *         the entity manager
     * @param documentDTO
     *         the document dto
     * @param userID
     *         the user id
     * @param dataObjectEntity
     *         the data object entity
     */
    @Override
    public void createDataObjectMovieFilesInFeTemp( EntityManager entityManager, DocumentDTO documentDTO, String userID,
            DataObjectMovieEntity dataObjectEntity ) {
        DocumentDTO previewDoc = createMovieThumbNail( entityManager, userID, documentDTO );
        DocumentDTO thumbDoc = createMovieThumbNail( entityManager, userID, documentDTO );

        dataObjectEntity.setPreviewImage( documentManager.prepareEntityFromDocumentDTO( previewDoc ) );
        dataObjectEntity.setThumbnail( documentManager.prepareEntityFromDocumentDTO( thumbDoc ) );

        // ffmpeg part
        String srcFilePathOnVault = PropertiesManager.getVaultPath() + documentDTO.getPath();
        String srcFilePath = srcFilePathOnVault.substring( ConstantsInteger.INTEGER_VALUE_ZERO,
                srcFilePathOnVault.lastIndexOf( File.separator ) );
        String srcfileName = srcFilePathOnVault.substring( srcFilePathOnVault.lastIndexOf( File.separator ) + 1 );

        MovieffmpegThread movieThread = new MovieffmpegThread( documentManager, documentDTO, srcFilePath, srcfileName,
                entityManagerFactory );
        threadPoolExecutorService.executeFFmpeg( movieThread );
    }

    /**
     * Create movie thumb nail document dto.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param documentDTO
     *         the document dto
     *
     * @return the document dto
     */
    private DocumentDTO createMovieThumbNail( EntityManager entityManager, String userIdFromGeneralHeader, DocumentDTO documentDTO ) {

        DocumentDTO thumbNail = new DocumentDTO();
        thumbNail.setPath( documentManager.getDocumentPathInHex( documentDTO ) );
        thumbNail.setType( ConstantsImageFileTypes.PNG );
        String srcFilePathOnVault = PropertiesManager.getVaultPath() + documentDTO.getPath();

        String srcfileName = srcFilePathOnVault.substring( srcFilePathOnVault.lastIndexOf( File.separator ) + 1 );
        String fileNameOnly = FilenameUtils.removeExtension( srcfileName );
        thumbNail.setName( fileNameOnly );

        return createDocumentForThumbNail( entityManager, userIdFromGeneralHeader, new File( fileNameOnly ), thumbNail );

    }

    /**
     * Create document for thumb nail document dto.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param thumbFile
     *         the thumb file
     * @param dataObjectDocument
     *         the data object document
     *
     * @return the document dto
     */
    private DocumentDTO createDocumentForThumbNail( EntityManager entityManager, String userId, File thumbFile,
            DocumentDTO dataObjectDocument ) {
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setId( UUID.randomUUID().toString() );
        if ( PropertiesManager.isEncrypted() ) {
            documentDTO.setEncryptionDecryption( PropertiesManager.getEncryptionDecryptionDTO() );
        }
        documentDTO.setVersion( new VersionDTO( ConstantsInteger.INTEGER_VALUE_ZERO ) );
        documentDTO.setPath( dataObjectDocument.getPath() );
        documentDTO.setUserId( ValidationUtils.validateUUIDString( userId ) ? UUID.fromString( userId ) : null );
        documentDTO.setName( thumbFile.getName() );
        documentDTO.setType( dataObjectDocument.getType() );
        documentDTO.setSize( thumbFile.length() );
        return documentManager.saveDocument( entityManager, documentDTO );
    }

    /**
     * Create data object thumb nail.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param dataObjectImageEntity
     *         the data object image entity
     * @param documentDTO
     *         the document dto
     */
    @Override
    public void createDataObjectThumbNail( EntityManager entityManager, String userId, DataObjectEntity dataObjectImageEntity,
            DocumentDTO documentDTO ) {
        if ( documentDTO.getType() != null ) {

            File fileFromVault = new File( PropertiesManager.getVaultPath() + documentDTO.getPath() );
            File thumbNailFileToCopyInfeStatic = new File( PropertiesManager.getFeStaticPath() + documentDTO.getPath() + File.separator
                    + ConstantsString.OBJECT_THUMB_NAIL_FILE_POSTFIX + documentDTO.getName() );
            File thumbNailFileToCopyInVault = new File( fileFromVault.getPath() + ConstantsString.OBJECT_THUMB_NAIL_FILE_POSTFIX );
            // Create data object image
            DocumentDTO thumbnailDocument = createDocumentForThumbNail( entityManager, userId, thumbNailFileToCopyInfeStatic, documentDTO );
            dataObjectImageEntity.getFile().setFilePath( documentDTO.getPath() );
            dataObjectImageEntity.getFile().setFileName( documentDTO.getName() );
            dataObjectImageEntity.getFile().setHash( documentDTO.getHash() );
            dataObjectImageEntity.getFile().setFileType( documentDTO.getType() );
            if ( null != documentDTO.getEncryptionDecryption() ) {
                EncryptionDecryptionEntity encryptionDecryptionEntity = new EncryptionDecryptionEntity(
                        documentDTO.getEncryptionDecryption().getMethod(), documentDTO.getEncryptionDecryption().getSalt(),
                        documentDTO.getEncryptionDecryption().isActive() );
                if ( null != documentDTO.getEncryptionDecryption().getId() ) {
                    encryptionDecryptionEntity.setId( UUID.fromString( documentDTO.getEncryptionDecryption().getId() ) );
                } else {
                    encryptionDecryptionEntity.setId( UUID.randomUUID() );
                }
                dataObjectImageEntity.getFile().setEncryptionDecryption( encryptionDecryptionEntity );

            }
            dataObjectImageEntity.getFile().setEncrypted( documentDTO.isEncrypted() );
            ( ( DataObjectImageEntity ) dataObjectImageEntity ).setThumbNail(
                    documentManager.prepareEntityFromDocumentDTO( thumbnailDocument ) );
            if ( dataObjectImageEntity.getFile() != null ) {
                // creating thumbNail file in feTemp so resized image buffered can be created
                createThumbnailQueues( fileFromVault, thumbNailFileToCopyInfeStatic, thumbNailFileToCopyInVault,
                        documentDTO.getEncryptionDecryption() );
            }
        }
    }

    /**
     * Create thumbnail queues.
     *
     * @param fileFromVault
     *         the file from vault
     * @param fileToCopyInFeTemp
     *         the file to copy in fe temp
     * @param fileToCopyInVault
     *         the file to copy in vault
     * @param encDec
     *         the enc dec
     */
    private void createThumbnailQueues( File fileFromVault, File fileToCopyInFeTemp, File fileToCopyInVault,
            EncryptionDecryptionDTO encDec ) {
        ImageToThumbnailThread thumbNailRunnable = new ImageToThumbnailThread( fileFromVault, fileToCopyInFeTemp, fileToCopyInVault,
                encDec );
        threadPoolExecutorService.thumbnailExecute( thumbNailRunnable );
    }

    /**
     * Create data object curve.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param dataObjectCurve
     *         the data object curve
     * @param documentDTO
     *         the document dto
     */
    @Override
    public void createDataObjectCurve( EntityManager entityManager, String userId, DataObjectCurveEntity dataObjectCurve,
            DocumentDTO documentDTO ) {
        if ( documentDTO.getType() != null ) {
            try {
                DataObjectCurveDTO dataObjectCurveDTO;
                File fileFromVault = new File( PropertiesManager.getVaultPath() + documentDTO.getPath() );

                try ( InputStream decryptedStreamFromVault = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream( fileFromVault,
                        documentDTO.getEncryptionDecryption() ) ) {
                    dataObjectCurveDTO = JsonUtils.jsonToObject( decryptedStreamFromVault, DataObjectCurveDTO.class );
                }

                List< double[] > xyCoordinates = dataObjectCurveDTO.getCurve();
                String xUnits = dataObjectCurveDTO.getxUnit();
                String yUnits = dataObjectCurveDTO.getyUnit();
                String xQuantityType = dataObjectCurveDTO.getxDimension();
                String yQuantityType = dataObjectCurveDTO.getyDimension();
                String xAxisLable = xQuantityType + " (" + xUnits + ")";
                String yAxisLable = yQuantityType + " (" + yUnits + ")";

                String strChartLabel = dataObjectCurve.getName();
                String strFileFormate = "jpeg";
                Integer heigth = 1920;
                Integer width = 1080;

                byte[] byteArrCureImage = ImageUtil.createImageByXyChart( strChartLabel, xAxisLable, yAxisLable, strFileFormate, heigth,
                        width, xyCoordinates );
                File thumbNailFileToCopyInFeStatic = new File(
                        PropertiesManager.getFeStaticPath() + documentDTO.getPath() + File.separator + documentDTO.getName() + ".jpeg" );
                // Create data object image
                DocumentDTO thumbnailDocument = createDocumentForThumbNail( entityManager, userId, thumbNailFileToCopyInFeStatic,
                        documentDTO );
                dataObjectCurve.setCurvethumbNail( documentManager.prepareEntityFromDocumentDTO( thumbnailDocument ) );
                // Copying original data object image from vault to feTemp for previews
                documentManager.writeToDiskInFETemp( entityManager, documentDTO, PropertiesManager.getFeStaticPath() );
                try ( ByteArrayInputStream bais = new ByteArrayInputStream( byteArrCureImage );
                        FileOutputStream fos = new FileOutputStream( thumbNailFileToCopyInFeStatic ) ) {
                    IOUtils.copy( bais, fos );
                }
            } catch ( IOException e ) {
                log.error( e.getMessage(), e );
                throw new SusException( e.getMessage() );
            }
        }

    }

    /**
     * Create data object prediction model files in fe temp.
     *
     * @param entityManager
     *         the entity manager
     * @param dataObjectPredictionEntity
     *         the data object prediction entity
     * @param documentDTO
     *         the document dto
     */
    @Override
    public void createDataObjectPredictionModelFilesInFETemp( EntityManager entityManager,
            DataObjectPredictionModelEntity dataObjectPredictionEntity, DocumentDTO documentDTO ) {
        if ( documentDTO == null || dataObjectPredictionEntity.getJsonFile() == null || dataObjectPredictionEntity.getBinFile() == null ) {
            return;
        }

        DocumentDTO jsonDoc = documentManager.prepareDocumentDTO( dataObjectPredictionEntity.getJsonFile() );
        DocumentDTO binDoc = documentManager.prepareDocumentDTO( dataObjectPredictionEntity.getBinFile() );

        String hex = CommonUtils.getHex( UUID.fromString( jsonDoc.getId() ), jsonDoc.getVersion().getId() );

        // copying all prediction model files in same FE static directory as per requirements
        documentManager.writeToDiskInFETemp( entityManager, jsonDoc, PropertiesManager.getFeStaticPath(), hex );
        documentManager.writeToDiskInFETemp( entityManager, binDoc, PropertiesManager.getFeStaticPath(), hex );
    }

    /**
     * Create data object html files in fe temp.
     *
     * @param dataObjectHtmlsEntity
     *         the data object htmls entity
     * @param documentDTO
     *         the document dto
     * @param objectDTO
     *         the object dto
     */
    @Override
    public void createDataObjectHtmlFilesInFETemp( DataObjectHtmlsEntity dataObjectHtmlsEntity, DocumentDTO documentDTO,
            DataObjectHtmlDTO objectDTO ) {
        DataObjectHtmlDTO dataObjectHtmlDTO = prepareHtmlDtoAndExtractZipFile( documentDTO, objectDTO );
        dataObjectHtmlsEntity.setHtmlJson( ByteUtil.convertStringToByte( JsonUtils.toJson( dataObjectHtmlDTO ) ) );

    }

    /**
     * Prepare html dto and extract zip file data object html dto.
     *
     * @param documentDTO
     *         the document dto
     * @param objectDTO
     *         the object dto
     *
     * @return the data object html dto
     */
    private DataObjectHtmlDTO prepareHtmlDtoAndExtractZipFile( DocumentDTO documentDTO, DataObjectHtmlDTO objectDTO ) {
        File htmlJsonPath = new File( PropertiesManager.getVaultPath() + objectDTO.getFile().getPath() );
        DataObjectHtmlDTO dataObjectHtmlDTO = readHtmlJsonFromHtmlFile( htmlJsonPath, documentDTO );
        if ( objectDTO.getZipFile() != null && objectDTO.getZipFile().getPath() != null && dataObjectHtmlDTO != null ) {

            String urlFe = objectDTO.getFile().getPath() + File.separator;
            prepareHtmlAndZipContentAndCopyFilesToFeStatic( dataObjectHtmlDTO, documentDTO, objectDTO, urlFe );
            dataObjectHtmlDTO.setBaseurl( urlFe );
            if ( StringUtils.isNotEmpty( dataObjectHtmlDTO.getHtml_index() ) ) {
                dataObjectHtmlDTO.setHtml_index( urlFe + dataObjectHtmlDTO.getHtml_index() );
            }
            if ( StringUtils.isNotEmpty( dataObjectHtmlDTO.getJs_index() ) ) {
                dataObjectHtmlDTO.setJs_index( urlFe + dataObjectHtmlDTO.getJs_index() );
            }
        }

        return dataObjectHtmlDTO;
    }

    /**
     * Read html json from html file data object html dto.
     *
     * @param htmlJsonPath
     *         the html json path
     * @param documentDTO
     *         the document dto
     *
     * @return the data object html dto
     */
    private static DataObjectHtmlDTO readHtmlJsonFromHtmlFile( File htmlJsonPath, DocumentDTO documentDTO ) {
        if ( htmlJsonPath.exists() ) {
            try ( InputStream decryptStream = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream( htmlJsonPath,
                    documentDTO.getEncryptionDecryption() ) ) {
                byte[] byteArray = IOUtils.toByteArray( decryptStream );
                try ( InputStream inputStream = new ByteArrayInputStream( byteArray ) ) {
                    return JsonUtils.jsonToObject( inputStream, DataObjectHtmlDTO.class );
                } catch ( Exception e ) {
                    log.error( "Unable to read Html file", e );
                    String html = new String( byteArray, StandardCharsets.UTF_8 );
                    DataObjectHtmlDTO dataObjectHtmlDTO = new DataObjectHtmlDTO();
                    dataObjectHtmlDTO.setHtml( html );
                }
            } catch ( IOException e ) {
                log.error( "Unable to read Html file", e );
            }
        }
        return null;
    }

    /**
     * Prepare html and zip content and copy files to fe static.
     *
     * @param dataObjectHtmlDTO
     *         the data object html dto
     * @param document
     *         the document
     * @param fileDestination
     *         the file destination
     * @param srcZipFile
     *         the src zip file
     * @param urlFe
     *         the url fe
     * @param urlCee
     *         the url cee
     * @param htmlObjectId
     *         the html object id
     */
    private void prepareHtmlAndZipContentAndCopyFilesToFeStatic( DataObjectHtmlDTO dataObjectHtmlDTO, DocumentDTO document,
            DataObjectHtmlDTO objectDTO, String urlFe ) {
        File fileDestination = new File( PropertiesManager.getFeStaticPath() + objectDTO.getFile().getPath() );
        File srcZipFile = new File( PropertiesManager.getVaultPath() + objectDTO.getZipFile().getPath() );
        String urlCee = PropertiesManager.getCeetronServerUrl().asText() + File.separator;
        if ( dataObjectHtmlDTO.getAttachments() != null ) {
            if ( !fileDestination.exists() ) {
                fileDestination.mkdirs();
                FileUtils.setGlobalExecuteFilePermissions( fileDestination.toPath() );
            }
            List< Map< String, String > > updateAttachmentList = new ArrayList<>();
            if ( srcZipFile.exists() ) {
                try {
                    String zipDest3D = PropertiesManager.getCeetronOutputPath().asText() + ConstantsString.PREFIX_3D_MODEL_FOLDER;

                    File decryptedFile = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndSave( document, srcZipFile,
                            PropertiesManager.getDefaultServerTempPath() + File.separator + srcZipFile.getName() );

                    FileUtils.extractZipFile( decryptedFile.getAbsolutePath(), fileDestination.getAbsolutePath() );
                    for ( Map< String, String > attFiles : dataObjectHtmlDTO.getAttachments() ) {
                        Map< String, String > updateAttachment = new HashMap<>();
                        updateAttachment.put( "name", attFiles.get( "name" ) );
                        updateAttachment.put( "type", attFiles.get( "type" ) );
                        if ( "movie".equalsIgnoreCase( attFiles.get( "type" ) ) ) {
                            updateMovieAttachmentInHtml( fileDestination, urlFe, updateAttachmentList, attFiles, updateAttachment );

                        } else if ( "ceetron".equalsIgnoreCase( attFiles.get( "type" ) ) ) {
                            updateCeetronAttachmentInHtml( fileDestination, urlCee, new File( objectDTO.getFile().getPath() ).getName(),
                                    updateAttachmentList, zipDest3D, attFiles,
                                    updateAttachment );
                        } else {
                            updateAttachment.put( "url", urlFe + attFiles.get( "name" ).replace( "\\", "/" ) );
                            updateAttachmentList.add( updateAttachment );
                        }
                    }
                    Files.deleteIfExists( decryptedFile.toPath() );
                } catch ( IOException e ) {
                    log.error( "HTML Zip file copy operation Failed :" + e.getMessage(), e );
                }
            }
            dataObjectHtmlDTO.setAttachments( updateAttachmentList );
        }
    }

    /**
     * Update ceetron attachment in html.
     *
     * @param fileDestination
     *         the file destination
     * @param urlCee
     *         the url cee
     * @param htmlObjectId
     *         the html object id
     * @param updateAttachmentList
     *         the update attachment list
     * @param zipDest3D
     *         the zip dest 3 d
     * @param attFiles
     *         the att files
     * @param updateAttachment
     *         the update attachment
     */
    private void updateCeetronAttachmentInHtml( File fileDestination, String urlCee, String htmlObjectId,
            List< Map< String, String > > updateAttachmentList, String zipDest3D, Map< String, String > attFiles,
            Map< String, String > updateAttachment ) {
        String innerCeePath = attFiles.get( "name" ).replace( "\\", "/" );
        File ceeFile = new File( innerCeePath );
        String ceeFileName = ceeFile.getName().contains( ConstantsString.DOT ) ? ceeFile.getName()
                .split( "\\." )[ ConstantsInteger.INTEGER_VALUE_ZERO ] : ceeFile.getName();
        updateAttachment.put( "url", urlCee + ceeFileName + htmlObjectId );
        updateAttachmentList.add( updateAttachment );
        String ceeZipSrc = fileDestination.getAbsolutePath() + ConstantsString.FORWARD_SLASH + innerCeePath;
        String ceeDestination = zipDest3D + ceeFileName + htmlObjectId + File.separator;

        Runnable extractZip = () -> {
            try {
                File dest = new File( ceeDestination );
                if ( dest.exists() ) {
                    org.apache.commons.io.FileUtils.forceDelete( dest );
                }
                FileUtils.extractZipFile( ceeZipSrc, ceeDestination );
                log.info( "ceetron files Extracted to: " + ceeDestination );
            } catch ( IOException e ) {
                log.error( "Ceetron Zip File extraction Failed: ", e );
                throw new SusException( "Ceetron Zip File extraction Failed: " + e.getMessage() );
            }
        };

        threadPoolExecutorService.archiveExecute( extractZip );
    }

    /**
     * Update movie attachment in html.
     *
     * @param fileDestination
     *         the file destination
     * @param urlFe
     *         the url fe
     * @param updateAttachmentList
     *         the update attachment list
     * @param attFiles
     *         the att files
     * @param updateAttachment
     *         the update attachment
     */
    private void updateMovieAttachmentInHtml( File fileDestination, String urlFe, List< Map< String, String > > updateAttachmentList,
            Map< String, String > attFiles, Map< String, String > updateAttachment ) {
        File file = new File( attFiles.get( "name" ).replace( "\\", "/" ) );
        String fileName = file.getName();
        updateAttachment.put( "url",
                urlFe + file.getParent() + "/" + ( fileName.contains( ConstantsString.DOT ) ? fileName.split( "\\." )[ 0 ] : fileName )
                        + WEBM_FORMAT );
        updateAttachmentList.add( updateAttachment );
        String tempFilePath =
                fileDestination.getAbsolutePath() + ConstantsString.FORWARD_SLASH + attFiles.get( "name" ).replace( "\\", "/" );
        String name = tempFilePath.substring( ConstantsInteger.INTEGER_VALUE_ZERO,
                tempFilePath.lastIndexOf( ConstantsString.FORWARD_SLASH ) );
        MovieffmpegThreadForHtml movieThread = new MovieffmpegThreadForHtml( name, FilenameUtils.getName( tempFilePath ) );
        threadPoolExecutorService.executeFFmpeg( movieThread );
    }

    /**
     * Create data object ceetron files in fe cee.
     *
     * @param dataObject3DceetronEntity
     *         the data object 3 dceetron entity
     */
    @Override
    public void createDataObjectCeetronFilesInFeCee( DataObject3DceetronEntity dataObject3DceetronEntity ) {
        String configPath = getConfigPath( dataObject3DceetronEntity );
        String inputFile = getSelectFile( dataObject3DceetronEntity );
        String outputFile = PropertiesManager.getCeetronOutputPath().asText() + ConstantsString.PREFIX_3D_MODEL_FOLDER
                + dataObject3DceetronEntity.getName() + dataObject3DceetronEntity.getComposedId().getId();
        new Thread( () -> {
            String cmd = PropertiesManager.getPythonExecutionPathOnServer() + ConstantsString.SPACE
                    + PropertiesManager.getCeetronConversionCommand();
            cmd = cmd.replace( "[input]", inputFile );
            cmd = cmd.replace( "[ouput]", outputFile );
            cmd = cmd.replace( "[configFile]", configPath );

            LinuxUtils.runCommand( cmd.split( " " ), ConstantsString.COMMAND_KARAF_LOGGING_ON, null );

            try {
                Files.delete( Paths.get( configPath ) );
                Files.delete( Paths.get( inputFile ) );
            } catch ( IOException e ) {
                log.error( e.getMessage(), e );
            }

        } ).start();
    }

    /**
     * Gets select file.
     *
     * @param dataObject3DceetronEntity
     *         the data object 3 dceetron entity
     *
     * @return the select file
     */
    private String getSelectFile( DataObject3DceetronEntity dataObject3DceetronEntity ) {
        File file;
        if ( null != dataObject3DceetronEntity.getFile() ) {
            String nameFile =
                    PropertiesManager.getDefaultServerTempPath() + File.separator + dataObject3DceetronEntity.getFile().getFileName();
            file = new File( nameFile );
            if ( !file.exists() ) {
                try {
                    file.createNewFile();
                } catch ( IOException e ) {
                    log.error( e.getMessage(), e );
                    throw new SusException( e );
                }
            }

            DocumentDTO jsonDoc = documentManager.prepareDocumentDTO( dataObject3DceetronEntity.getFile() );
            try ( FileOutputStream fos = new FileOutputStream( file.getAbsolutePath() );
                    InputStream fileStream = documentManager.readVaultFromDisk( jsonDoc ) ) {
                Streams.copy( fileStream, fos, true );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
                throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_WRITE_FILE.getKey() ) );
            }
        } else {
            throw new SusException( "Please provide Select File!" );
        }
        return file.getAbsolutePath();
    }

    /**
     * Gets config path.
     *
     * @param dataObject3DceetronEntity
     *         the data object 3 dceetron entity
     *
     * @return the config path
     */
    private String getConfigPath( DataObject3DceetronEntity dataObject3DceetronEntity ) {
        String configPath;
        SuSObjectModel susModel = configManager.getObjectTypeByIdAndConfigName( dataObject3DceetronEntity.getTypeId().toString(),
                dataObject3DceetronEntity.getConfig() );
        String nameFile = PropertiesManager.getDefaultServerTempPath() + File.separator + susModel.getName() + "_config";
        if ( null != dataObject3DceetronEntity.getConfigUpload() ) {
            File file;
            file = new File( nameFile );
            if ( !file.exists() ) {
                try {
                    file.createNewFile();
                } catch ( IOException e ) {
                    log.error( e.getMessage(), e );
                    throw new SusException( e );
                }
            }
            DocumentDTO jsonDoc = documentManager.prepareDocumentDTO( dataObject3DceetronEntity.getConfigUpload() );
            try ( FileOutputStream fos = new FileOutputStream( file.getAbsolutePath() );
                    InputStream fileStream = documentManager.readVaultFromDisk( jsonDoc ) ) {
                Streams.copy( fileStream, fos, true );
            } catch ( Exception e ) {
                log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_WRITE_FILE.getKey() ), e );
                throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_WRITE_FILE.getKey() ) );
            }
            configPath = file.getAbsolutePath();
        } else {
            configPath = prepareConfigPath( susModel.getName() );
        }
        return configPath;
    }

    /**
     * Prepare config path string.
     *
     * @param name
     *         the name
     *
     * @return the string
     */
    private String prepareConfigPath( String name ) {
        JsonNode jsonNode = PropertiesManager.getCeetronConfigByObjectType( name );
        String nameFile = PropertiesManager.getDefaultServerTempPath() + File.separator + name;

        File file = new File( nameFile );
        if ( !file.exists() ) {
            try {
                file.createNewFile();
            } catch ( IOException e ) {
                log.error( e.getMessage(), e );
                throw new SusException( e );
            }
        }

        try ( FileOutputStream fos = new FileOutputStream( file.getAbsolutePath() );
                InputStream inputStream = new ByteArrayInputStream( jsonNode.toString().getBytes() ) ) {
            Streams.copy( inputStream, fos, true );
        } catch ( Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_WRITE_FILE.getKey() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_WRITE_FILE.getKey() ) );
        }
        return file.getAbsolutePath();
    }

    /**
     * Create data object pdf preview in fe temp.
     *
     * @param entityManager
     *         the entity manager
     * @param documentDTO
     *         the document dto
     */
    @Override
    public void createDataObjectPDFPreviewInFeTemp( EntityManager entityManager, DocumentDTO documentDTO ) {
        documentManager.writeToDiskInFETemp( entityManager, documentDTO, PropertiesManager.getFeStaticPath() );
    }

    /**
     * Sets document manager.
     *
     * @param documentManager
     *         the document manager
     */
    public void setDocumentManager( DocumentManager documentManager ) {
        this.documentManager = documentManager;
    }

    /**
     * Sets thread pool executor service.
     *
     * @param threadPoolExecutorService
     *         the thread pool executor service
     */
    public void setThreadPoolExecutorService(
            ThreadPoolExecutorService threadPoolExecutorService ) {
        this.threadPoolExecutorService = threadPoolExecutorService;
    }

    /**
     * Sets entity manager factory.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Sets config manager.
     *
     * @param configManager
     *         the config manager
     */
    public void setConfigManager( ObjectTypeConfigManager configManager ) {
        this.configManager = configManager;
    }

}
