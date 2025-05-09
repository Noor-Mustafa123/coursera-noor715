package de.soco.software.simuspace.suscore.core.location.service.rest.impl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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

import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDocument;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.enums.HTTPResponseHeaderEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.FileAlreadyExistsException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.SuSVaultUtils;
import de.soco.software.simuspace.suscore.core.location.manager.LocationCoreManager;

/**
 * A servlet for uploading various kind of supported documents to the system.
 *
 * @author Ahmar Nadeem
 *
 * A servlet for uploading various kind of supported documents to the system.
 */
@Log4j2
public class TransferServiceImpl extends HttpServlet {

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
     * The core manager.
     */
    private LocationCoreManager coreManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doOptions( HttpServletRequest request, HttpServletResponse response ) {
        setResponseHeader( response );
    }

    /**
     * Sequence for upload in SuS<br> 1. verify token <br> 2. create Document in db <br> 3. write file to Document path, and collect form
     * parameters <br> 4. verify form parameters and validate document<br> 5. cleanup temp file in case fail or pass <br> 6. send a json
     * response
     *
     * {@inheritDoc}
     *
     * @throws IOException
     */
    @Override
    public void doPost( HttpServletRequest request, HttpServletResponse response ) {

        String authToken = request.getHeader( ConstantRequestHeader.AUTHORIZATION );

        response.setContentType( ConstantsDocument.CONTENT_TYPE_JSON );
        setResponseHeader( response );
        log.debug( "Started upload" );

        try ( PrintWriter printWriter = response.getWriter() ) {
            coreManager.isValidToken( authToken );
            startUpload( request, printWriter );
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, TransferServiceImpl.class );
        }

    }

    /**
     * Start upload.
     *
     * @param request
     *         the request
     * @param user
     *         the user
     * @param printWriter
     *         the print writer
     * @param document
     *         the document
     */
    private void startUpload( HttpServletRequest request, PrintWriter printWriter ) {
        try {
            log.debug( "Started upload function" );
            uploadFile( request, printWriter );
            log.debug( "finished upload function" );
        } catch ( SusException | IOException e ) {
            ExceptionLogger.logException( e, TransferServiceImpl.class );
            fail( printWriter, e.getMessage() );
        }
    }

    /**
     * Upload file.
     *
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
    private void uploadFile( HttpServletRequest request, PrintWriter printWriter ) throws IOException {
        Map< String, String > fieldsMap = new HashMap<>();
        File uploadedFile = null;
        FileItemStream item = null;
        InputStream stream = null;
        if ( ServletFileUpload.isMultipartContent( request ) ) {
            String name;
            try {
                ServletFileUpload upload = new ServletFileUpload();
                FileItemIterator uploadItemIterator = upload.getItemIterator( request );
                while ( uploadItemIterator.hasNext() ) {
                    item = uploadItemIterator.next();
                    name = item.getFieldName();
                    stream = item.openStream();
                    if ( item.isFormField() ) {
                        fieldsMap.put( name, Streams.asString( stream ) );
                    } else {
                        if ( item.getContentType() == null ) {
                            fail( printWriter, MessageBundleFactory.getMessage( Messages.NO_CONTENT_TYPE_PROVIDED_TO_UPLOAD.getKey() ) );
                        }
                        String destPath = request.getHeader( ConstantRequestHeader.DEST_PATH );
                        uploadedFile = writeToDiskInVault( stream, destPath );
                    }
                }
            } catch ( FileAlreadyExistsException e ) {
                ExceptionLogger.logMessage( MessageBundleFactory.getMessage( Messages.FILE_ALREADY_EXIST_ON_SERVER.getKey() ) );
                pass( printWriter, new DocumentDTO() );
                return;
            } catch ( Exception e ) {
                ExceptionLogger.logException( e, TransferServiceImpl.class );
                throw new SusException( MessageBundleFactory.getMessage( Messages.DOCUMENT_UPLOAD_FAILED.getKey() ) );
            } finally {
                if ( stream != null ) {
                    stream.close();
                }
            }

            validateAndSaveDocument( uploadedFile, fieldsMap, item, printWriter );
        }

    }

    public File writeToDiskInVault( InputStream stream, String objectPath ) {
        return SuSVaultUtils.saveInRemoteVaultWithoutEncryptionCheck( stream, objectPath.substring( ConstantsInteger.INTEGER_VALUE_FOUR ),
                PropertiesManager.getVaultPath() + objectPath.substring( 0, ConstantsInteger.INTEGER_VALUE_FOUR ) );
    }

    /**
     * helper function to validate the uploaded document(s) according to the origin and save the information to the database. It also takes
     * care of the errors occurred. If there's any error occurred during the validation process, the file itself and any subsequent files
     * for which the document information could not be saved are deleted
     *
     * @param file
     *         the file
     * @param fieldsMap
     *         the fields map
     * @param item
     *         the item
     * @param user
     *         the user
     * @param printWriter
     *         the print writer
     */
    protected void validateAndSaveDocument( File file, Map< String, String > fieldsMap, FileItemStream item, PrintWriter printWriter ) {
        String errorMessage = null;
        try {
            validateMimeTypeAndPrepareDocument( file, fieldsMap.get( ConstantsDocument.DOCUMENT_AGENT ) == null
                    ? DocumentDTO.BROWSER
                    : fieldsMap.get( ConstantsDocument.DOCUMENT_AGENT ), null );
        } catch ( SusException e ) {
            errorMessage = e.getMessage();
        }
        if ( StringUtils.isNotBlank( errorMessage ) ) {
            throw new SusException( errorMessage );
        }

        pass( printWriter, new DocumentDTO() );
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
     */
    protected void validateMimeTypeAndPrepareDocument( File filePath, String agent, UUID userId ) {

        verifyIfAgentRegistered( agent );

        validateMimeTypeAndData( filePath );
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

    public String validateMimeTypeAndData( File uploadedFile ) {
        String mimeType = null;

        try {
            // use Tika Library to get mime type instead of File.probeContentType as it gives null incase file exist without extension on
            // windows operating system (no issue at linux)
            mimeType = new Tika().detect( uploadedFile );
            if ( mimeType.contains( "video" ) ) {
                mimeType = VIDEO_MP4;
            }
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, getClass() );
        }
        return mimeType;
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
     * Gets the core manager.
     *
     * @return the core manager
     */
    public LocationCoreManager getCoreManager() {
        return coreManager;
    }

    /**
     * Sets the core manager.
     *
     * @param coreManager
     *         the new core manager
     */
    public void setCoreManager( LocationCoreManager coreManager ) {
        this.coreManager = coreManager;
    }

}
