package de.soco.software.simuspace.suscore.document.service.rest.impl;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDocument;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMessageHeaders;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.HTTPResponseHeaderEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.EncryptionDecryptionDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.EncryptAndDecryptUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.common.dao.UserCommonDAO;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.EncryptionDecryptionEntity;
import de.soco.software.simuspace.suscore.data.entity.JobTokenEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.UserTokenEntity;
import de.soco.software.simuspace.suscore.document.manager.DocumentManager;
import de.soco.software.simuspace.suscore.interceptors.dao.JobTokenDAO;
import de.soco.software.simuspace.suscore.interceptors.dao.UserTokenDAO;

/**
 * A servlet for uploading various kind of supported documents to the system.
 *
 * @author Ahmar Nadeem
 */
@Log4j2
public class UploadServiceImpl extends HttpServlet {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -1L;

    /**
     * http response error.
     */
    private static final String ERROR = "ERROR";

    /**
     * http response type.
     */
    private static final String TYPE = "type";

    /**
     * http response content.
     */
    private static final String CONTENT = "content";

    /**
     * http response message.
     */
    private static final String MESSAGE = "message";

    /**
     * http response expired.
     */
    private static final String EXPIRED = "expired";

    /**
     * http response success.
     */
    private static final String SUCCESS = "success";

    /**
     * http response files.
     */
    private static final String DATA = "data";

    /**
     * The Constant VIDEO_MP4.
     */
    private static final String VIDEO_MP4 = "video/mp4";

    /**
     * A reference to the document DAO.
     */
    private DocumentManager documentManager;

    /**
     * User token dao object for token management.
     */
    private UserTokenDAO userTokenDAO;

    private JobTokenDAO jobTokenDAO;

    private UserCommonDAO userCommonDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doOptions( HttpServletRequest request, HttpServletResponse response ) throws IOException {
        setResponseHeader( response );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doPost( HttpServletRequest request, HttpServletResponse response ) {
        EntityManager entityManager = documentManager.getEntityManagerFactory().createEntityManager();
        UserEntity user = validateTokenAndGetUserInfo( entityManager, request );
        response.setContentType( ConstantsDocument.CONTENT_TYPE_JSON );
        setResponseHeader( response );
        log.debug( "Started upload" );
        try ( PrintWriter printWriter = response.getWriter() ) {
            DocumentDTO document = new DocumentDTO();
            document.setId( UUID.randomUUID().toString() );
            if ( PropertiesManager.isEncrypted() ) {
                document.setEncrypted( true );
                document.setEncryptionDecryption( PropertiesManager.getEncryptionDecryptionDTO() );
            } else {
                document.setEncrypted( false );
                document.setEncryptionDecryption( null );
            }
            document.setVersion( new VersionDTO( ConstantsInteger.INTEGER_VALUE_ONE ) );
            document.setUserId( user.getId() );
            uploadFile( entityManager, request, printWriter, document, user );
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, UploadServiceImpl.class );
        } finally {
            entityManager.close();
        }

    }

    /**
     * Upload file.
     *
     * @param entityManager
     *         the entity manager
     * @param request
     *         the request
     * @param printWriter
     *         the print writer
     * @param document
     *         the document
     * @param user
     *         the user
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private void uploadFile( EntityManager entityManager, HttpServletRequest request, PrintWriter printWriter, DocumentDTO document,
            UserEntity user ) throws IOException {
        InputStream stream = null;
        try {
            log.debug( "Starting upload function" );
            Map< String, String > fieldsMap = new HashMap<>();
            File uploadedFile = null;
            FileItemStream item = null;
            boolean isMultipart = ServletFileUpload.isMultipartContent( request );
            if ( isMultipart ) {
                String name = ConstantsString.EMPTY_STRING;
                FileItemIterator iter = null;
                ServletFileUpload upload = new ServletFileUpload();
                iter = upload.getItemIterator( request );
                while ( iter.hasNext() ) {
                    item = iter.next();
                    name = item.getFieldName();
                    stream = item.openStream();
                    if ( item.isFormField() ) {
                        fieldsMap.put( name, Streams.asString( stream ) );
                    } else {
                        if ( item.getContentType() == null ) {
                            fail( printWriter, MessageBundleFactory.getMessage( Messages.NO_CONTENT_TYPE_PROVIDED_TO_UPLOAD.getKey() ) );
                        } else {
                            document.setType( item.getContentType() );
                        }

                        document.setName( item.getName() );
                        uploadedFile = documentManager.writeToDiskInVault( request.getHeader( ConstantsString.USER_AGENT_HEADER ), document,
                                stream );
                        document.setSize( uploadedFile.length() );
                        document.setPath( File.separator + new File( PropertiesManager.getVaultPath() ).toURI()
                                .relativize( new File( uploadedFile.getAbsolutePath() ).toURI() ).getPath() );
                    }
                }
                validateAndSaveDocument( entityManager, document, uploadedFile, fieldsMap, user, printWriter );
            }
            log.debug( "Upload function finished" );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, UploadServiceImpl.class );
            fail( printWriter, e.getMessage() );
        } finally {
            try {
                if ( stream != null ) {
                    stream.close();
                }
            } catch ( Exception e2 ) {
                log.warn( e2.getMessage(), e2 );
            }
        }
    }

    /**
     * helper function to validate the uploaded document(s) according to the origin and save the information to the database. It also takes
     * care of the errors occurred. If there's any error occurred during the validation process, the file itself and any subsequent files
     * for which the document information could not be saved are deleted
     *
     * @param entityManager
     *         the entity manager
     * @param document
     *         the document
     * @param file
     *         the file
     * @param fieldsMap
     *         the fields map
     * @param user
     *         the user
     * @param printWriter
     *         the print writer
     */
    protected void validateAndSaveDocument( EntityManager entityManager, DocumentDTO document, File file, Map< String, String > fieldsMap,
            UserEntity user, PrintWriter printWriter ) {
        try {
            document = validateMimeTypeAndPrepareDocument( document, file,
                    fieldsMap.get( ConstantsDocument.DOCUMENT_AGENT ) == null ? DocumentDTO.BROWSER
                            : fieldsMap.get( ConstantsDocument.DOCUMENT_AGENT ),
                    user.getId() );
        } catch ( Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.DOCUMENT_VALIDATION_FAILED.getKey() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.DOCUMENT_VALIDATION_FAILED.getKey() ) );
        }
        try {
            document.setCreatedOn( new Date() );

            DocumentEntity documentEntity = prepareEntityFromDocumentDTO( document );
            documentEntity.setOwner( user );
            DocumentDTO savedDocument = documentManager.saveDocument( entityManager, document );
            document.setHash( savedDocument.getHash() );
        } catch ( Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FAILED_TO_SAVE_DOCUMENT.getKey() ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_SAVE_DOCUMENT.getKey() ) );
        }

        pass( printWriter, document );
    }

    /**
     * helper function to validate the request and get user information.
     *
     * @param entityManager
     *         the entity manager
     * @param request
     *         the request
     *
     * @return the user entity
     */
    private UserEntity validateTokenAndGetUserInfo( EntityManager entityManager, HttpServletRequest request ) {
        String token = request.getHeader( ConstantsMessageHeaders.X_AUTH_TOKEN );
        String jobToken = request.getHeader( ConstantsMessageHeaders.X_JOB_TOKEN );
        if ( token == null && jobToken == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_TOKEN_PROVIDED.getKey() ) );
        }
        if ( StringUtils.isNotEmpty( token ) ) {
            UserTokenEntity userToken = userTokenDAO.getUserTokenEntityByActiveToken( entityManager, token );
            if ( userToken == null || userToken.isExpired() ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_OR_EXPIRED_TOKEN.getKey() ) );
            }
            return userToken.getUserEntity();
        }
        if ( StringUtils.isNotEmpty( jobToken ) ) {
            JobTokenEntity jobTokenEntity = jobTokenDAO.getJobTokenEntityByJobToken( entityManager, jobToken );
            if ( jobTokenEntity == null || jobTokenEntity.isExpired() ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_OR_EXPIRED_TOKEN.getKey() ) );
            }
            return userCommonDAO.readUser( entityManager, UUID.fromString( jobTokenEntity.getUserId() ) );
        }
        return null;
    }

    /**
     * A utility function to validate the document type to be uploaded according to the system rules.
     *
     * @param document
     *         the document
     * @param filePath
     *         the file path
     * @param agent
     *         the agent
     * @param userId
     *         the user id
     *
     * @return the document DTO
     */
    protected DocumentDTO validateMimeTypeAndPrepareDocument( DocumentDTO document, File filePath, String agent, UUID userId ) {

        verifyIfAgentRegistered( agent );
        document.setUserId( userId );
        document.setAgent( agent );
        document.setIsTemp( false );
        validateMimeTypeAndData( filePath, document );
        document.validate();
        return document;
    }

    /**
     * A helper function to verify if the origin is one of the registered origins.
     *
     * @param agent
     *         the agent
     */
    private void verifyIfAgentRegistered( String agent ) {
        boolean isAgentRegistered = false;

        for ( String regAgent : DocumentDTO.REGISTERED_AGENTS ) {
            if ( agent.contentEquals( regAgent ) ) {
                isAgentRegistered = true;
                break;
            }
        }
        if ( !isAgentRegistered ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DOCUMENT_NOT_REGISTERED_AGENT.getKey(), agent ) );
        }
    }

    /**
     * utility function to validate mime type and also the data.
     *
     * @param uploadedFile
     *         the uploaded file
     * @param document
     *         the document
     *
     * @return the string
     */

    public String validateMimeTypeAndData( File uploadedFile, DocumentDTO document ) {
        String mimeType = null;
        document.setSize( uploadedFile.length() );

        try ( InputStream decryptedstream = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream( uploadedFile,
                document.getEncryptionDecryption() ) ) {
            // use Tika Library to get mime type instead of File.probeContentType as it gives null incase file exist without extension on
            // windows operating system (no issue at linux)
            mimeType = new Tika().detect( decryptedstream );
            if ( mimeType.contains( "video" ) ) {
                mimeType = VIDEO_MP4;
            }
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, UploadStagingServiceImpl.class );
        }
        document.setType( mimeType );
        return mimeType;
    }

    /**
     * utility function to prepare document entity from the passed DTO.
     *
     * @param documentDTO
     *         the document DTO
     *
     * @return the document entity
     */
    private DocumentEntity prepareEntityFromDocumentDTO( DocumentDTO documentDTO ) {
        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setId( UUID.fromString( documentDTO.getId() ) );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( documentDTO.getUserId() );
        documentEntity.setOwner( userEntity );
        documentEntity.setFileName( documentDTO.getName() );
        documentEntity.setFileType( documentDTO.getType() );
        documentEntity.setIsTemp( documentDTO.getIsTemp() );
        documentEntity.setProperties( documentDTO.getProperties() );
        documentEntity.setExpiry( documentDTO.getExpiry() );
        documentEntity.setCreatedOn( documentDTO.getCreatedOn() );
        if ( null != documentDTO.getEncryptionDecryption() ) {
            EncryptionDecryptionEntity encryptionDecryptionEntity = prepareEncDecEntity( documentDTO.getEncryptionDecryption() );
            if ( null != documentDTO.getEncryptionDecryption().getId() ) {
                encryptionDecryptionEntity.setId( UUID.fromString( documentDTO.getEncryptionDecryption().getId() ) );
            } else {
                encryptionDecryptionEntity.setId( UUID.randomUUID() );
            }
            documentEntity.setEncryptionDecryption( encryptionDecryptionEntity );
        }
        documentEntity.setEncrypted( documentDTO.isEncrypted() );

        return documentEntity;
    }

    /**
     * Prepare enc dec entity.
     *
     * @param encryptionDecryption
     *         the encryption decryption
     *
     * @return the encryption decription entity
     */
    public EncryptionDecryptionEntity prepareEncDecEntity( EncryptionDecryptionDTO encryptionDecryption ) {
        return new EncryptionDecryptionEntity( encryptionDecryption.getMethod(), encryptionDecryption.getSalt(),
                encryptionDecryption.isActive() );
    }

    /**
     * A utility function to set response header.
     *
     * @param response
     *         the new response header
     */
    private void setResponseHeader( HttpServletResponse response ) {
        response.setHeader( HTTPResponseHeaderEnum.SERVER.getKey(), HTTPResponseHeaderEnum.SERVER.getValue() );
        response.setHeader( HTTPResponseHeaderEnum.ALLOW.getKey(), HTTPResponseHeaderEnum.ALLOW.getValue() );
        response.setHeader( HTTPResponseHeaderEnum.ACCESS_CONTROL_ALLOW_HEADER.getKey(),
                HTTPResponseHeaderEnum.ACCESS_CONTROL_ALLOW_HEADER.getValue() );
        response.setHeader( HTTPResponseHeaderEnum.ACCESS_CONTROL_ALLOW_METHOD.getKey(),
                HTTPResponseHeaderEnum.ACCESS_CONTROL_ALLOW_METHOD.getValue() );
        response.setHeader( HTTPResponseHeaderEnum.ACCESS_CONTROL_ALLOW_ORIGIN.getKey(),
                HTTPResponseHeaderEnum.ACCESS_CONTROL_ALLOW_ORIGIN.getValue() );
    }

    /**
     * print the failure message.
     *
     * @param out
     *         the out
     * @param message
     *         the message
     */
    private void fail( PrintWriter out, String message ) {
        Map< String, String > messageMap = new HashMap<>();
        messageMap.put( CONTENT, message );
        messageMap.put( TYPE, ERROR );

        Map< String, Object > responseMap = new HashMap<>();
        responseMap.put( SUCCESS, Boolean.FALSE );
        responseMap.put( EXPIRED, Boolean.FALSE );
        responseMap.put( MESSAGE, messageMap );

        ObjectMapper mapper = new ObjectMapper();

        try {
            out.print( mapper.writeValueAsString( responseMap ) );
        } catch ( Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.SERIALIZATION_ERROR.getKey() ), e );
            out.print( MessageBundleFactory.getMessage( Messages.SERIALIZATION_ERROR.getKey() ) );
        }

        out.flush();
    }

    /**
     * print the success response.
     *
     * @param out
     *         the out
     * @param data
     *         the data
     */
    private void pass( PrintWriter out, Object data ) {
        Map< String, Object > responseMap = new HashMap<>();
        responseMap.put( SUCCESS, Boolean.TRUE );
        responseMap.put( DATA, data );
        out.print( JsonUtils.convertMapToStringGernericValue( responseMap ) );
        out.flush();
    }

    /**
     * Gets the document manager.
     *
     * @return the document manager
     */
    public DocumentManager getDocumentManager() {
        return documentManager;
    }

    /**
     * Sets the document manager.
     *
     * @param documentManager
     *         the new document manager
     */
    public void setDocumentManager( DocumentManager documentManager ) {
        this.documentManager = documentManager;
    }

    /**
     * Gets the user token DAO.
     *
     * @return the userTokenDAO
     */
    public UserTokenDAO getUserTokenDAO() {
        return userTokenDAO;
    }

    /**
     * Sets the user token DAO.
     *
     * @param userTokenDAO
     *         the userTokenDAO to set
     */
    public void setUserTokenDAO( UserTokenDAO userTokenDAO ) {
        this.userTokenDAO = userTokenDAO;
    }

    public JobTokenDAO getJobTokenDAO() {
        return jobTokenDAO;
    }

    public void setJobTokenDAO( JobTokenDAO jobTokenDAO ) {
        this.jobTokenDAO = jobTokenDAO;
    }

    public UserCommonDAO getUserCommonDAO() {
        return userCommonDAO;
    }

    public void setUserCommonDAO( UserCommonDAO userCommonDAO ) {
        this.userCommonDAO = userCommonDAO;
    }

}
