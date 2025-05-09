package de.soco.software.simuspace.suscore.document.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.EncryptionDecryptionDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.EncryptAndDecryptUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.InternalExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MovieUtils;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.SuSVaultUtils;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.EncryptionDecryptionEntity;
import de.soco.software.simuspace.suscore.data.entity.LocationEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.document.dao.DocumentDAO;
import de.soco.software.simuspace.suscore.document.manager.DocumentManager;

/**
 * The implementation of the document manager to handle document operations
 *
 * @author ahmar.nadeem
 */
public class DocumentManagerImpl implements DocumentManager {

    /**
     * The document DAO object.
     */
    private DocumentDAO documentDAO;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< DocumentDTO > getDocumentList( FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< DocumentDTO > documentsList;
        try {
            documentsList = prepareDocumentDTOList( documentDAO.getAllFilteredRecords( entityManager, DocumentEntity.class, filter ) );
        } finally {
            entityManager.close();
        }
        return PaginationUtil.constructFilteredResponse( filter, documentsList );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< DocumentDTO > getDocumentListByUserId( UUID userId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< DocumentDTO > documentsList;
        try {
            documentsList = prepareDocumentDTOList( documentDAO.getFilteredListByUser( entityManager, userId, filter ) );
        } finally {
            entityManager.close();
        }
        return PaginationUtil.constructFilteredResponse( filter, documentsList );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentDTO getDocumentById( UUID id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        DocumentDTO documentDTO;
        try {
            documentDTO = getDocumentById( entityManager, id );
        } finally {
            entityManager.close();
        }
        return documentDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentDTO getDocumentById( EntityManager entityManager, UUID id ) {
        DocumentEntity entity = documentDAO.findById( entityManager, id );
        if ( entity == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DOCUMENT_DOES_NOT_EXIST.getKey(), id ) );
        }
        return prepareDocumentDTO( entity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentEntity getDocumentEntityById( EntityManager entityManager, UUID id ) {
        DocumentEntity entity = documentDAO.findById( entityManager, id );
        if ( entity == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DOCUMENT_DOES_NOT_EXIST.getKey(), id ) );
        }
        return entity;
    }

    @Override
    public List< DocumentDTO > getDocumentsByIds( EntityManager entityManager, List< UUID > ids ) {
        List< DocumentEntity > entities = documentDAO.getLatestNonDeletedObjectsByIds( entityManager, ids );
        List< DocumentDTO > dtoList = new ArrayList<>();
        if ( null != entities && !entities.isEmpty() ) {
            for ( DocumentEntity documentEntity : entities ) {
                dtoList.add( prepareDocumentDTO( documentEntity ) );
            }
        }
        return dtoList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentDTO getDocumentByName( EntityManager entityManager, String name ) {
        DocumentEntity entity = documentDAO.findByName( entityManager, DocumentEntity.class, name );
        if ( entity == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DOCUMENT_DOES_NOT_EXIST_WITH_NAME.getKey(), name ) );
        }
        return prepareDocumentDTO( entity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentDTO updateDocument( EntityManager entityManager, DocumentDTO document ) {
        if ( documentDAO.findById( entityManager, UUID.fromString( document.getId() ) ) == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DOCUMENT_DOES_NOT_EXIST.getKey(),
                    UUID.fromString( document.getId() ), document.getVersion().getId() ) );
        }
        DocumentEntity entity = prepareEntityFromDocumentDTO( document );
        return prepareDocumentDTO( saveOrUpdateDocument( entityManager, entity ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentEntity saveOrUpdateDocument( EntityManager entityManager, DocumentEntity entity ) {
        return documentDAO.saveOrUpdate( entityManager, entity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteDocument( UUID documentId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean isDeleted;
        try {
            isDeleted = deleteDocument( entityManager, documentId );
        } finally {
            entityManager.close();
        }
        return isDeleted;
    }

    private boolean deleteDocument( EntityManager entityManager, UUID documentId ) {
        boolean isDeleted = documentDAO.deleteDocument( entityManager, documentId );
        if ( isDeleted ) {
            SuSVaultUtils.deleteFileFromVault( documentId, ConstantsInteger.INTEGER_VALUE_ONE, PropertiesManager.getVaultPath() );
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DOCUMENT_DOES_NOT_EXIST.getKey(), documentId,
                    ConstantsInteger.INTEGER_VALUE_ONE ) );
        }
        return isDeleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentDTO saveDocument( EntityManager entityManager, DocumentDTO document ) {
        DocumentEntity entity = prepareEntityFromDocumentDTO( document );
        LocationEntity defaultLocation = new LocationEntity();
        defaultLocation.setId( UUID.fromString( LocationsEnum.DEFAULT_LOCATION.getId() ) );

        List< LocationEntity > locationEntities = new ArrayList<>();
        locationEntities.add( defaultLocation );
        entity.setLocations( locationEntities );

        return prepareDocumentDTO( saveOrUpdateDocument( entityManager, entity ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File writeToDiskInVault( String browserDetails, DocumentDTO document, InputStream stream ) {
        String hex = CommonUtils.getHex( UUID.fromString( document.getId() ), document.getVersion().getId() );
        return SuSVaultUtils.saveInVault( stream, PropertiesManager.getVaultPath(), hex,
                CommonUtils.getEncodedName( new File( hex.substring( ConstantsInteger.INTEGER_VALUE_TWO ) ).getName(), browserDetails ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream readVaultFromDisk( DocumentDTO document ) {
        return SuSVaultUtils.getDecryptionSteamFromPath( UUID.fromString( document.getId() ), document.getVersion().getId(),
                PropertiesManager.getVaultPath(), document.getEncryptionDecryption() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream readDocumentFromTemp( DocumentDTO document ) {
        return SuSVaultUtils.getDecryptionSteamFromPath( UUID.fromString( document.getId() ), document.getVersion().getId(),
                PropertiesManager.getDefaultServerTempPath(), document.getEncryptionDecryption() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDocumentPathInHex( DocumentDTO document ) {
        String hex = CommonUtils.getHex( UUID.fromString( document.getId() ), document.getVersion().getId() );
        return File.separator + hex.substring( ConstantsInteger.INTEGER_VALUE_ZERO, ConstantsInteger.INTEGER_VALUE_TWO ) + File.separator
                + hex.substring( ConstantsInteger.INTEGER_VALUE_TWO );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String writeToDiskInFETemp( EntityManager entityManager, DocumentDTO document, String tempPath ) {
        DocumentDTO doc = getDocumentById( entityManager, UUID.fromString( document.getId() ) );
        String hex = CommonUtils.getHex( UUID.fromString( doc.getId() ), doc.getVersion().getId() );
        String fileName = null;
        try {
            fileName = new String( doc.getName().getBytes( Charset.forName( ConstantsString.UTF8.toUpperCase() ) ),
                    ConstantsString.UTF8.toUpperCase() );
        } catch ( UnsupportedEncodingException e ) {
            InternalExceptionLogger.logException( e, DocumentManagerImpl.class );
        }
        String filePath = tempPath + File.separator
                + hex.substring( ConstantsInteger.INTEGER_VALUE_ZERO, ConstantsInteger.INTEGER_VALUE_TWO ) + File.separator
                + hex.substring( ConstantsInteger.INTEGER_VALUE_TWO );
        String vaultFile = PropertiesManager.getVaultPath() + document.getPath();

        File file = new File( filePath );
        File requiredFolder = Paths.get( filePath ).toFile();
        if ( requiredFolder.exists() || requiredFolder.mkdirs() ) {
            file = new File( requiredFolder, fileName );

            try ( FileOutputStream fos = new FileOutputStream( file );
                    InputStream decryptedStreamFromVault = EncryptAndDecryptUtils
                            .decryptFileIfEncpEnabledAndReturnStream( new File( vaultFile ), document.getEncryptionDecryption() ) ) {
                Streams.copy( decryptedStreamFromVault, fos, true );
            } catch ( Exception e ) {
                ExceptionLogger.logException( e, DocumentManagerImpl.class );
                throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_WRITE_FILE.getKey() ) );
            }

        }
        return File.separator + hex.substring( ConstantsInteger.INTEGER_VALUE_ZERO, ConstantsInteger.INTEGER_VALUE_TWO ) + File.separator
                + hex.substring( ConstantsInteger.INTEGER_VALUE_TWO ) + File.separator + doc.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String writeToDiskInFETemp( EntityManager entityManager, DocumentDTO document, String tempPath, String hex ) {
        DocumentDTO doc = getDocumentById( entityManager, UUID.fromString( document.getId() ) );
        String fileName = null;
        try {
            fileName = new String( doc.getName().getBytes( Charset.forName( ConstantsString.UTF8.toUpperCase() ) ),
                    ConstantsString.UTF8.toUpperCase() );
        } catch ( UnsupportedEncodingException e ) {
            InternalExceptionLogger.logException( e, DocumentManagerImpl.class );
        }
        String filePath = tempPath + File.separator
                + hex.substring( ConstantsInteger.INTEGER_VALUE_ZERO, ConstantsInteger.INTEGER_VALUE_TWO ) + File.separator
                + hex.substring( ConstantsInteger.INTEGER_VALUE_TWO );
        String vaultFile = PropertiesManager.getVaultPath() + document.getPath();
        File file = new File( filePath );
        File requiredFolder = Paths.get( filePath ).toFile();
        if ( requiredFolder.exists() || requiredFolder.mkdirs() ) {
            file = new File( requiredFolder, fileName );
            try ( FileOutputStream fos = new FileOutputStream( file ); InputStream vaultFileStream = new FileInputStream( vaultFile ) ) {
                Streams.copy( vaultFileStream, fos, true );
            } catch ( Exception e ) {
                ExceptionLogger.logException( e, DocumentManagerImpl.class );
                throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_WRITE_FILE.getKey() ) );
            }
        }
        return File.separator + hex.substring( ConstantsInteger.INTEGER_VALUE_ZERO, ConstantsInteger.INTEGER_VALUE_TWO ) + File.separator
                + hex.substring( ConstantsInteger.INTEGER_VALUE_TWO ) + File.separator + doc.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String writeAllFileToDiskInFETemp( EntityManager entityManager, DocumentDTO document, String tempPath, String fileExtension ) {
        String sourcePath = null;
        DocumentDTO doc = getDocumentById( entityManager, UUID.fromString( document.getId() ) );
        String hex = CommonUtils.getHex( UUID.fromString( doc.getId() ), doc.getVersion().getId() );

        String filePath = tempPath + File.separator
                + hex.substring( ConstantsInteger.INTEGER_VALUE_ZERO, ConstantsInteger.INTEGER_VALUE_TWO ) + File.separator
                + hex.substring( ConstantsInteger.INTEGER_VALUE_TWO );
        String vaultFile = PropertiesManager.getVaultPath() + document.getPath();

        if ( StringUtils.isNoneEmpty( vaultFile ) ) {
            sourcePath = vaultFile.substring( ConstantsInteger.INTEGER_VALUE_ZERO, vaultFile.lastIndexOf( ConstantsString.FORWARD_SLASH ) );
        }
        File destinationFile = new File( filePath );
        File requiredFolder = Paths.get( filePath ).toFile();

        if ( requiredFolder.exists() || requiredFolder.mkdirs() ) {

            File sourceDir = new File( sourcePath );

            try {
                // convert file in formates which described in ffmpeg.json file and copy it to particular destination
                if ( fileExtension.equalsIgnoreCase( ConstantsFileProperties.AVI ) ) {
                    MovieUtils.makeAVIFile( vaultFile, sourceDir, destinationFile );
                } else if ( fileExtension.equalsIgnoreCase( ConstantsFileProperties.WEBM ) ) {
                    MovieUtils.makeWEBMFile( vaultFile, sourceDir, destinationFile );
                } else if ( fileExtension.equalsIgnoreCase( ConstantsFileProperties.MP4 ) ) {
                    MovieUtils.makeMP4File( "", vaultFile, sourceDir, destinationFile );
                } else if ( fileExtension.equalsIgnoreCase( ConstantsFileProperties.MKV ) ) {
                    MovieUtils.makeMKVFile( vaultFile, sourceDir, destinationFile );
                } else if ( fileExtension.equalsIgnoreCase( ConstantsFileProperties.MOV ) ) {
                    MovieUtils.makeMOVFile( vaultFile, sourceDir, destinationFile );
                } else {
                    // file format is unknown or not supported just copy it as it is
                    FileUtils.copyDirectory( sourceDir, destinationFile );
                }

            } catch ( Exception e ) {
                ExceptionLogger.logException( e, DocumentManagerImpl.class );
                throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_WRITE_FILE.getKey() ) );

            }

        }

        return File.separator + hex.substring( ConstantsInteger.INTEGER_VALUE_ZERO, ConstantsInteger.INTEGER_VALUE_TWO ) + File.separator
                + hex.substring( ConstantsInteger.INTEGER_VALUE_TWO ) + File.separator + doc.getName();
    }

    /**
     * utility function to prepare document entity from the passed DTO.
     *
     * @param documentDTO
     *         the documentDTO
     *
     * @return the document Entity
     */
    @Override
    public DocumentEntity prepareEntityFromDocumentDTO( DocumentDTO documentDTO ) {
        return prepareEntityFromDocumentDTO( documentDTO, null );
    }

    /**
     * utility function to prepare document entity from the passed DTO.
     *
     * @param documentDTO
     *         the documentDTO
     *
     * @return the document Entity
     */
    @Override
    public DocumentEntity prepareEntityFromDocumentDTO( DocumentDTO documentDTO, DocumentEntity existingEntity ) {
        DocumentEntity documentEntity = ( existingEntity == null ) ? new DocumentEntity() : existingEntity;

        if ( ValidationUtils.validateUUIDString( documentDTO.getId() ) ) {
            documentEntity.setId( UUID.fromString( documentDTO.getId() ) );
        } else {
            documentEntity.setId( UUID.randomUUID() );
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setId( documentDTO.getUserId() );
        documentEntity.setOwner( userEntity );
        documentEntity.setFileName( documentDTO.getName() );
        documentEntity.setFileType( documentDTO.getType() );
        documentEntity.setIsTemp( documentDTO.getIsTemp() );
        documentEntity.setProperties( documentDTO.getProperties() );
        documentEntity.setExpiry( documentDTO.getExpiry() );
        documentEntity.setFilePath( documentDTO.getPath() );
        if ( null != documentDTO.getEncryptionDecryption() ) {
            EncryptionDecryptionEntity encryptionDecryptionEntity = new EncryptionDecryptionEntity(
                    documentDTO.getEncryptionDecryption().getMethod(), documentDTO.getEncryptionDecryption().getSalt(),
                    documentDTO.getEncryptionDecryption().isActive() );
            if ( null != documentDTO.getEncryptionDecryption().getId() ) {
                encryptionDecryptionEntity.setId( UUID.fromString( documentDTO.getEncryptionDecryption().getId() ) );
            } else {
                encryptionDecryptionEntity.setId( UUID.randomUUID() );
            }
            documentEntity.setEncryptionDecryption( encryptionDecryptionEntity );
        }
        documentEntity.setEncrypted( documentDTO.isEncrypted() );
        documentEntity.setFileSize( documentDTO.getSize() );
        documentEntity.setAgent( documentDTO.getAgent() );
        documentEntity.setCreatedOn( new Date() );
        documentEntity.setModifiedOn( new Date() );
        File file = new File( PropertiesManager.getVaultPath() + documentDTO.getPath() );

        documentEntity.setHash( de.soco.software.simuspace.suscore.common.util.FileUtils.getAdler32CheckSumForVaultFile( file,
                documentDTO.getEncryptionDecryption() ) );

        return documentEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentDTO prepareDocumentDTO( DocumentEntity entity ) {
        DocumentDTO document = new DocumentDTO();
        document.setId( entity.getId().toString() );
        if ( null != entity.getEncryptionDecryption() ) {
            EncryptionDecryptionDTO prepareEncDecDTO = new EncryptionDecryptionDTO( entity.getEncryptionDecryption().getId().toString(),
                    entity.getEncryptionDecryption().getMethod(), entity.getEncryptionDecryption().isActive(),
                    entity.getEncryptionDecryption().getSalt() );
            document.setEncryptionDecryption( prepareEncDecDTO );
        }
        document.setEncrypted( entity.isEncrypted() );
        document.setVersion( new VersionDTO( ConstantsInteger.INTEGER_VALUE_ONE ) );
        document.setUserId( entity.getOwner() != null ? entity.getOwner().getId() : null );
        document.setName( entity.getFileName() );
        document.setType( entity.getFileType() );
        document.setIsTemp( entity.getIsTemp() );
        document.setProperties( entity.getProperties() );
        document.setExpiry( entity.getExpiry() );
        document.setPath( entity.getFilePath() );
        document.setCreatedOn( entity.getCreatedOn() );
        document.setSize( entity.getFileSize() );

        document.setEncoding( entity.getEncoding() );
        document.setHash( entity.getHash() );

        return document;
    }

    /**
     * Prepare document DTO's list.
     *
     * @param entities
     *         the entities
     *
     * @return the document
     */
    private List< DocumentDTO > prepareDocumentDTOList( List< DocumentEntity > entities ) {
        List< DocumentDTO > documentsList = new ArrayList<>();
        DocumentDTO document;
        for ( DocumentEntity documentEntity : entities ) {
            document = prepareDocumentDTO( documentEntity );
            documentsList.add( document );
        }
        return documentsList;
    }

    /**
     * @param documentDAO
     *         the documentDAO to set
     */
    public void setDocumentDAO( DocumentDAO documentDAO ) {
        this.documentDAO = documentDAO;
    }

    /**
     * Gets the document DAO.
     *
     * @return the document DAO
     */
    @Override
    public DocumentDAO getDocumentDAO() {
        return documentDAO;
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

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

}
