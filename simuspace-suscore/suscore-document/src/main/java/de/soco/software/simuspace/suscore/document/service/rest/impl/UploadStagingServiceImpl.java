package de.soco.software.simuspace.suscore.document.service.rest.impl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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
import de.soco.software.simuspace.suscore.common.enums.HTTPResponseHeaderEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.OSValidator;
import de.soco.software.simuspace.suscore.common.util.SuSStagingUtils;
import de.soco.software.simuspace.suscore.interceptors.dao.UserTokenDAO;

/**
 * The Class UploadStagingServiceImpl.
 */
@Log4j2
public class UploadStagingServiceImpl extends HttpServlet {

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
     * User token dao object for token management.
     */
    private UserTokenDAO userTokenDAO;

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

        validateToken( request.getHeader( ConstantRequestHeader.AUTHORIZATION ) );
        response.setContentType( ConstantsDocument.CONTENT_TYPE_JSON );
        setResponseHeader( response );
        log.debug( "Started upload" );

        try ( PrintWriter printWriter = response.getWriter() ) {
            startUpload( request, printWriter );
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, UploadStagingServiceImpl.class );
        }

    }

    /**
     * Start upload.
     *
     * @param request
     *         the request
     * @param printWriter
     *         the print writer
     */
    private void startUpload( HttpServletRequest request, PrintWriter printWriter ) {
        try {
            log.debug( "Started upload function" );
            uploadFile( request, printWriter );
            log.debug( "finished upload function" );
        } catch ( SusException | IOException e ) {
            ExceptionLogger.logException( e, UploadStagingServiceImpl.class );
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
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private void uploadFile( HttpServletRequest request, PrintWriter printWriter ) throws IOException {
        Map< String, String > fieldsMap = new HashMap<>();
        FileItemStream item;
        if ( ServletFileUpload.isMultipartContent( request ) ) {
            FileItemIterator iter;
            InputStream stream = null;
            try {
                ServletFileUpload upload = new ServletFileUpload();
                iter = upload.getItemIterator( request );
                String name;
                while ( iter.hasNext() ) {
                    item = iter.next();
                    name = item.getFieldName();
                    stream = item.openStream();
                    if ( item.isFormField() ) {
                        fieldsMap.put( name, Streams.asString( stream ) );
                    } else {
                        if ( item.getContentType() == null ) {
                            fail( printWriter, MessageBundleFactory.getMessage( Messages.NO_CONTENT_TYPE_PROVIDED_TO_UPLOAD.getKey() ) );
                        }
                        String destPath = request.getHeader( ConstantRequestHeader.DEST_PATH );
                        destPath = OSValidator.convertPathToRelitiveOS( destPath );

                        File uploadedFile = writeToDiskInStaging( request.getHeader( ConstantRequestHeader.USER_UID ), stream, destPath );
                        FileUtils.setGlobalAllFilePermissions( uploadedFile );
                    }
                }
            } catch ( Exception e ) {
                ExceptionLogger.logException( e, UploadStagingServiceImpl.class );
                throw new SusException( MessageBundleFactory.getMessage( Messages.DOCUMENT_UPLOAD_FAILED.getKey() ) );
            } finally {
                if ( stream != null ) {
                    stream.close();
                }
            }
        }

    }

    /**
     * Write to disk in staging.
     *
     * @param userUid
     *         the user uid
     * @param stream
     *         the stream
     * @param destPath
     *         the dest path
     *
     * @return the file
     */
    public File writeToDiskInStaging( String userUid, InputStream stream, String destPath ) {
        return SuSStagingUtils.saveInStaging( stream, PropertiesManager.getUserStagingPath( userUid ) + destPath, userUid );
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
     * @param printWriter
     *         the print writer
     */
    protected void validateAndSaveDocument( File file, Map< String, String > fieldsMap, PrintWriter printWriter ) {
        String errorMessage = null;
        try {
            validateMimeTypeAndPrepareDocument( file, fieldsMap.get( ConstantsDocument.DOCUMENT_AGENT ) == null ? DocumentDTO.BROWSER
                    : fieldsMap.get( ConstantsDocument.DOCUMENT_AGENT ) );
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
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
     * @param filePath
     *         the file path
     * @param agent
     *         the agent
     */
    protected void validateMimeTypeAndPrepareDocument( File filePath, String agent ) {

        verifyIfAgentRegistered( agent );

        validateMimeTypeAndData( filePath );
    }

    /**
     * Validate Token.
     *
     * @param authToken
     *         the authToken
     */
    protected void validateToken( String authToken ) {
        if ( StringUtils.isEmpty( authToken ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_TOKEN_PROVIDED.getKey() ) );
        }
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
            ExceptionLogger.logException( e, UploadStagingServiceImpl.class );
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
     * Gets the user token DAO.
     *
     * @return the user token DAO
     */
    public UserTokenDAO getUserTokenDAO() {
        return userTokenDAO;
    }

    /**
     * Sets the user token DAO.
     *
     * @param userTokenDAO
     *         the new user token DAO
     */
    public void setUserTokenDAO( UserTokenDAO userTokenDAO ) {
        this.userTokenDAO = userTokenDAO;
    }

}
