package de.soco.software.simuspace.suscore.common.client;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.FileUpload;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsAPITypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.properties.CommonProperties;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.http.model.TransferInfo;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.IOUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;

/**
 * The Class SuSClient is a communication client usable to communicate.
 *
 * @author Ahsan Khan
 */
@Log4j2
public class SuSClient {

    /**
     * The Constant KEY_CONTAINING_FILE_SIZE.
     */
    private static final String HEADER_CONTAINING_FILE_SIZE = "File-Size";

    /**
     * The Constant EOF.
     */
    private static final int EOF = -1;

    /**
     * The Constant WINDOWS_1252.
     */
    private static final String WINDOWS_1252 = "windows-1252";

    /**
     * The Constant UTF_8.
     */
    private static final String UTF_8 = "UTF-8";

    /**
     * The Constant FAILED_HTTP_ERROR_CODE.
     */
    private static final String FAILED_HTTP_ERROR_CODE = "Failed : HTTP error code : ";

    /**
     * The Constant URL_IS_INVALID.
     */
    private static final String URL_IS_INVALID = "URL is invalid";

    /**
     * This Constant contains the key holding filename.
     */
    public static final String KEY_CONTAINS_FILE_NAME = "filename";

    /**
     * The Constant TRANSFER_ABORTED.
     */
    private static final String TRANSFER_ABORTED = "Transfer aborted";

    /**
     * The Constant KEY_VALUE_PAIR_LENGTH.
     */
    public static final int KEY_VALUE_PAIR_LENGTH = 2;

    /**
     * The Constant holds the value that is key value separator.
     */
    public static final String KEY_VALUE_SEPARATOR = "=";

    /**
     * A {@code String} constant representing {@value #NEW_STRING} is to replace some old value.
     */
    public static final String NEW_STRING = "";

    /**
     * A {@code String} constant representing {@value #OPTIONAL_HEADER_CONTAINING_DATA} is optional response header. Contains some info in
     * that key i.e file name here.
     */
    public static final String OPTIONAL_HEADER_CONTAINING_DATA = "Content-Disposition";

    /**
     * This Constant has the value to replace.
     */
    public static final String STRING_TO_REPLACE = "\"";

    /**
     * The Constant for origin.
     */
    private static final String ORIGIN = "origin";

    /**
     * The Constant key of dataobject files.
     */
    private static final String DATAOBJECT_FILES_KEY = "dataobject-files";

    /**
     * The Constant for linux operating system.
     */
    private static final Object OS_LINUX_KEY = "linux";

    /**
     * The Constant for getting operating system.
     */
    private static final String OS_NAME = "os.name";

    /**
     * The Constant for document id of file.
     */
    private static final String FILE_DOC_ID = "docId";

    /**
     * The Constant for location id of file.
     */
    private static final String FILE_LOCATION_ID = "locationId";

    /**
     * The Constant for name of file.
     */
    private static final String FILE_NAME = "name";

    /**
     * The Constant for selection type of file i.e client.
     */
    private static final String FILE_SELECTION_TYPE = "selType";

    /**
     * The Constant FILES_KEY.
     */
    private static final String FILES_KEY = "files";

    /**
     * The Constant for multipart boundary parameter.
     */
    private static final String BOUNDARY_PARAM = "----";

    /**
     * The Constant holds the default string encoding.
     */
    private static final String DEFAULT_ENCODING = UTF_8;

    /**
     * The Constant JSOM_MIME_TYPE.
     */
    private static final String JSOM_MIME_TYPE = "application/json";

    /**
     * The Constant MASTER_SERVER.
     */
    private static final String MASTER_SERVER = "Jetty";

    /**
     * The Constant SERVER.
     */
    private static final Object SERVER = "Server";

    /**
     * The Constant RESPONSE_ENTITY.
     */
    private static final Object RESPONSE_ENTITY = "entity";

    /**
     * The api expiry time when karaf down.
     */
    public static long API_EXPIRY_TIME_WHEN_KARAF_DOWN = 7200 * 1000; // 7200*1000

    /**
     * The api expiry time when api not found.
     */
    public static long API_EXPIRY_TIME_WHEN_API_NOT_FOUND = 20000; // 20000

    /**
     * The api call delay between karaf down.
     */
    public static int API_CALL_DELAY_BETWEEN_KARAF_DOWN = 10000; // 10000

    public static void ignoreSSL() {
        TrustManager[] trustAllCerts = new TrustManager[]{ new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted( java.security.cert.X509Certificate[] certs, String authType ) {
            }

            public void checkServerTrusted( java.security.cert.X509Certificate[] certs, String authType ) {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance( "SSL" );
            sc.init( null, trustAllCerts, new java.security.SecureRandom() );
            HttpsURLConnection.setDefaultSSLSocketFactory( sc.getSocketFactory() );
            HttpsURLConnection.setDefaultHostnameVerifier( ( hostname, session ) -> true );
        } catch ( Exception e ) {
            log.warn( e.getMessage() );
        }
    }

    /**
     * Gets the https ssl client.
     *
     * @param url
     *         the url
     *
     * @return the https ssl client
     */
    public static CloseableHttpClient getHttpsSslClient( String url ) {

        if ( url.toLowerCase().contains( "https" ) ) {
            try {
                SSLContextBuilder builder = new SSLContextBuilder();
                SchemeRegistry registry = new SchemeRegistry();
                SSLSocketFactory socketFactory = new SSLSocketFactory( new TrustStrategy() {

                    public boolean isTrusted( final X509Certificate[] chain, String authType ) {
                        return true;
                    }
                }, org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );

                registry.register( new Scheme( "https", 443, socketFactory ) );
                ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager( registry );
                return new DefaultHttpClient( mgr, new DefaultHttpClient().getParams() );

            } catch ( Exception e ) {
                ExceptionLogger.logException( e, SuSClient.class );
            }

        }
        return HttpClients.createDefault();
    }

    /**
     * Post request.
     *
     * @param url
     *         the url
     * @param payload
     *         the payload
     * @param requestHeaders
     *         the request headers
     *
     * @return the sus response DTO
     */
    @SuppressWarnings( { "unchecked", "resource" } )
    public static SusResponseDTO postRequest( String url, String payload, Map< String, String > requestHeaders ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.POST, url ) );
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        SusResponseDTO susResponseDTO = null;
        try {

            httpClient = getHttpsSslClient( url );
            httpPost = new HttpPost( url );

            final StringEntity input = new StringEntity( payload, DEFAULT_ENCODING );
            input.setContentType( JSOM_MIME_TYPE );

            httpPost.setEntity( input );

            for ( final Entry< String, String > h : requestHeaders.entrySet() ) {
                httpPost.addHeader( h.getKey(), h.getValue() );
            }
            try {
                response = httpClient.execute( httpPost );
            } catch ( Exception e ) {
                log.warn( e.getMessage() );
            }

            if ( response != null && response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 201
                    && response.getStatusLine().getStatusCode() != 404 && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() );
            } else if ( response == null || response.getStatusLine().getStatusCode() == 404
                    || response.getStatusLine().getStatusCode() == 502 ) {
                response = retryApiCallUntillKarafUpOrExpiryTimeRunsOutPostRequest( httpPost, response );
            }

            for ( Header header : response.getAllHeaders() ) {
                if ( header.getName().equals( SERVER ) && header.getValue().contains( MASTER_SERVER ) ) {
                    susResponseDTO = JsonUtils.jsonStreamToObject( response.getEntity().getContent(), SusResponseDTO.class );
                    break;
                }
            }

            if ( susResponseDTO == null ) {
                susResponseDTO = prepareSusResponseDTO( response );
            }
        } catch ( final IOException e ) {
            ExceptionLogger.logException( e, SuSClient.class );
            throw new SusException( e, SuSClient.class );
        } catch ( Exception e1 ) {
            ExceptionLogger.logException( e1, SuSClient.class );
        } finally {
            IOUtils.safeClose( response );
            IOUtils.safeClose( httpClient );
        }
        return susResponseDTO;
    }

    /**
     * Post external request.
     *
     * @param url
     *         the url
     * @param payload
     *         the payload
     * @param requestHeaders
     *         the request headers
     *
     * @return the closeable http response
     */
    public static CloseableHttpResponse postExternalRequest( String url, String payload, Map< String, String > requestHeaders ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.POST, url ) );
        int timeout = 39;
        RequestConfig config = RequestConfig.custom().setConnectTimeout( timeout * 1000 ).setConnectionRequestTimeout( timeout * 1000 )
                .setSocketTimeout( timeout * 1000 ).build();
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {

            httpClient = HttpClientBuilder.create().setDefaultRequestConfig( config ).build();
            httpPost = new HttpPost( url );

            final StringEntity input = new StringEntity( payload, DEFAULT_ENCODING );
            input.setContentType( JSOM_MIME_TYPE );

            httpPost.setEntity( input );

            for ( final Entry< String, String > h : requestHeaders.entrySet() ) {
                httpPost.addHeader( h.getKey(), h.getValue() );
            }
            response = httpClient.execute( httpPost );

            if ( response != null && response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 404
                    && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() + " Invalid remote response" );
            }

        } catch ( final IOException e ) {
            ExceptionLogger.logException( e, SuSClient.class );
            throw new SusException( e, SuSClient.class );
        } catch ( Exception e1 ) {
            ExceptionLogger.logException( e1, SuSClient.class );
            throw new SusException( " Invalid remote response" );
        }
        return response;
    }

    /**
     * Post request without retry.
     *
     * @param url
     *         the url
     * @param payload
     *         the payload
     * @param requestHeaders
     *         the request headers
     *
     * @return the sus response DTO
     */
    @SuppressWarnings( { "unchecked", "resource" } )
    public static SusResponseDTO postRequestWithoutRetry( String url, String payload, Map< String, String > requestHeaders ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.POST, url ) );
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        SusResponseDTO susResponseDTO = null;
        try {
            httpClient = getHttpsSslClient( url );
            httpPost = new HttpPost( url );

            final StringEntity input = new StringEntity( payload, DEFAULT_ENCODING );
            input.setContentType( JSOM_MIME_TYPE );

            httpPost.setEntity( input );

            for ( final Entry< String, String > h : requestHeaders.entrySet() ) {
                httpPost.addHeader( h.getKey(), h.getValue() );
            }
            try {
                response = httpClient.execute( httpPost );
            } catch ( Exception e ) {
                log.warn( e.getMessage() );
            }

            if ( response != null && response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 404
                    && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() );
            } else if ( response == null || response.getStatusLine().getStatusCode() == 404
                    || response.getStatusLine().getStatusCode() == 502 ) {
                throw new SusException( URL_IS_INVALID );
            }

            for ( Header header : response.getAllHeaders() ) {
                if ( header.getName().equals( SERVER ) && header.getValue().contains( MASTER_SERVER ) ) {
                    susResponseDTO = JsonUtils.jsonStreamToObject( response.getEntity().getContent(), SusResponseDTO.class );
                    break;
                }
            }

            if ( susResponseDTO == null ) {
                susResponseDTO = prepareSusResponseDTO( response );
            }
        } catch ( final IOException e ) {
            ExceptionLogger.logException( e, SuSClient.class );
            throw new SusException( e, SuSClient.class );
        } catch ( Exception e1 ) {
            ExceptionLogger.logException( e1, SuSClient.class );
        } finally {
            IOUtils.safeClose( response );
            IOUtils.safeClose( httpClient );
        }
        return susResponseDTO;
    }

    /**
     * Used to request GET api of SuS-1.1.
     *
     * @param url
     *         the url of SuS-1.1 to request.
     * @param requestHeaders
     *         the request headers required to authentication request .
     *
     * @return the generic SusResponseDTO with success, message and data.
     */
    @SuppressWarnings( { "unchecked", "resource" } )
    public static SusResponseDTO getRequest( String url, Map< String, String > requestHeaders ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.GET, url ) );
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        SusResponseDTO susResponseDTO = null;
        try {

            httpClient = getHttpsSslClient( url );
            httpGet = new HttpGet( url );

            for ( final Entry< String, String > h : requestHeaders.entrySet() ) {
                httpGet.addHeader( h.getKey(), h.getValue() );
            }

            try {
                response = httpClient.execute( httpGet );
            } catch ( Exception e ) {
                log.warn( e.getMessage() );
            }

            if ( response != null && response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 404
                    && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() );
            } else if ( response == null || response.getStatusLine().getStatusCode() == 404
                    || response.getStatusLine().getStatusCode() == 502 ) {
                response = retryApiCallUntillKarafUpOrExpiryTimeRunsOutGetRequest( httpGet, response );
            }

            for ( Header header : response.getAllHeaders() ) {
                if ( header.getName().equals( SERVER ) && header.getValue().contains( MASTER_SERVER ) ) {
                    susResponseDTO = JsonUtils.jsonStreamToObject( response.getEntity().getContent(), SusResponseDTO.class );
                    break;
                }
            }

            if ( susResponseDTO == null ) {
                susResponseDTO = prepareSusResponseDTO( response );

            }
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, SuSClient.class );
        } finally {
            IOUtils.safeClose( response );
            IOUtils.safeClose( httpClient );
        }
        return susResponseDTO;
    }

    public static CloseableHttpResponse getExternalRequest( String url, Map< String, String > requestHeaders ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.GET, url ) );
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        try {

            httpClient = getHttpsSslClient( url );
            httpGet = new HttpGet( url );

            for ( final Entry< String, String > h : requestHeaders.entrySet() ) {
                httpGet.addHeader( h.getKey(), h.getValue() );
            }

            try {
                response = httpClient.execute( httpGet );
            } catch ( Exception e ) {
                log.warn( e.getMessage() );
            }

            if ( response != null && response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 204
                    && response.getStatusLine().getStatusCode() != 404 && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() );
            } else if ( response == null || response.getStatusLine().getStatusCode() == 404
                    || response.getStatusLine().getStatusCode() == 502 ) {
                response = retryApiCallUntillKarafUpOrExpiryTimeRunsOutGetRequest( httpGet, response );
            }
        } catch ( Exception e1 ) {
            ExceptionLogger.logException( e1, SuSClient.class );
        }
        return response;
    }

    /**
     * Gets external request without retry.
     *
     * @param url
     *         the url
     * @param requestHeaders
     *         the request headers
     *
     * @return the external request without retry
     */
    public static CloseableHttpResponse getExternalRequestWithoutRetry( String url, Map< String, String > requestHeaders ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.GET, url ) );
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        try {

            httpClient = getHttpsSslClient( url );
            httpGet = new HttpGet( url );

            for ( final Entry< String, String > h : requestHeaders.entrySet() ) {
                httpGet.addHeader( h.getKey(), h.getValue() );
            }

            try {
                response = httpClient.execute( httpGet );
            } catch ( Exception e ) {
                log.warn( e.getMessage() );
                throw e;
            }

            if ( response != null && response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 204
                    && response.getStatusLine().getStatusCode() != 404 && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() );
            }
        } catch ( Exception e1 ) {
            ExceptionLogger.logException( e1, SuSClient.class );
        }
        return response;
    }

    /**
     * Used to request DELETE api of SuS-1.1.
     *
     * @param url
     *         the url of SuS-1.1 to request.
     * @param requestHeaders
     *         the request headers required to authentication request .
     *
     * @return the generic SusResponseDTO with success, message and data.
     */
    @SuppressWarnings( { "unchecked", "resource" } )
    public static SusResponseDTO deleteRequest( String url, Map< String, String > requestHeaders ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.DELETE, url ) );
        CloseableHttpClient httpClient = null;
        HttpDelete httpDelete = null;
        CloseableHttpResponse response = null;
        SusResponseDTO susResponseDTO = null;
        try {
            httpClient = getHttpsSslClient( url );
            httpDelete = new HttpDelete( url );

            for ( final Entry< String, String > h : requestHeaders.entrySet() ) {
                httpDelete.addHeader( h.getKey(), h.getValue() );
            }

            try {
                response = httpClient.execute( httpDelete );
            } catch ( Exception e ) {
                log.warn( e.getMessage() );
            }

            if ( response != null && response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 404
                    && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() );
            } else if ( response == null || response.getStatusLine().getStatusCode() == 404
                    || response.getStatusLine().getStatusCode() == 502 ) {
                response = retryApiCallUntillKarafUpOrExpiryTimeRunsOutDeleteRequest( httpDelete, response );
            }

            for ( Header header : response.getAllHeaders() ) {
                if ( header.getName().equals( SERVER ) && header.getValue().contains( MASTER_SERVER ) ) {
                    susResponseDTO = JsonUtils.jsonStreamToObject( response.getEntity().getContent(), SusResponseDTO.class );
                    break;
                }
            }

            if ( susResponseDTO == null ) {
                susResponseDTO = prepareSusResponseDTO( response );
            }

        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, SuSClient.class );
        } finally {
            IOUtils.safeClose( response );
            IOUtils.safeClose( httpClient );
        }
        return susResponseDTO;
    }

    /**
     * Used to request PUT API.
     *
     * @param url
     *         the url
     * @param requestHeaders
     *         the request headers required to authentication request .
     * @param payload
     *         the payload of API to request.
     *
     * @return the sus response DTO
     */
    @SuppressWarnings( "resource" )
    public static SusResponseDTO putRequest( String url, Map< String, String > requestHeaders, String payload ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.PUT, url ) );
        CloseableHttpClient httpClient = null;
        HttpPut httpPut = null;
        CloseableHttpResponse response = null;
        SusResponseDTO susResponseDTO = null;
        try {

            httpClient = getHttpsSslClient( url );
            httpPut = new HttpPut( url );

            final StringEntity input = new StringEntity( payload, DEFAULT_ENCODING );
            input.setContentType( JSOM_MIME_TYPE );
            httpPut.setEntity( input );

            for ( final Entry< String, String > header : requestHeaders.entrySet() ) {
                httpPut.addHeader( header.getKey(), header.getValue() );
            }

            try {
                response = httpClient.execute( httpPut );
            } catch ( Exception e ) {
                log.warn( e.getMessage() );
            }

            if ( response != null && response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 404
                    && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() );
            } else if ( response == null || response.getStatusLine().getStatusCode() == 404
                    || response.getStatusLine().getStatusCode() == 502 ) {
                response = retryApiCallUntillKarafUpOrExpiryTimeRunsOutPutRequest( httpPut, response );
            }

            try {
                susResponseDTO = JsonUtils.jsonStreamToObject( response.getEntity().getContent(), SusResponseDTO.class );
            } catch ( Exception e ) {
                susResponseDTO = prepareSusResponseDTO( response );
            }

        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, SuSClient.class );
        } finally {
            IOUtils.safeClose( response );
            IOUtils.safeClose( httpClient );
        }

        return susResponseDTO;
    }

    /**
     * Post request.
     *
     * @param url
     *         the url
     * @param filePath
     *         the file path
     * @param requestHeaders
     *         the request headers
     *
     * @return the sus response dto
     *
     * @throws SusException
     *         the sus exception
     */
    @SuppressWarnings( "resource" )
    public static FileUpload uploadFileRequest( String url, String filePath, Map< String, String > requestHeaders ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.UPLOAD, url ) );
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        Map< String, Object > map = new HashMap<>();
        final FileUpload file = new FileUpload();
        try {
            httpClient = getHttpsSslClient( url );
            httpPost = new HttpPost( url );
            final String boundary = BOUNDARY_PARAM + UUID.randomUUID();
            appendContentTypes( requestHeaders, boundary );
            final File fileToUpload = new File( filePath );
            if ( !fileToUpload.exists() || !fileToUpload.isFile() ) {
                throw new SusException( MessagesUtil.getMessage( WFEMessages.FILE_TO_UPLOAD_SHOULD_EXIST ) );
            }

            httpPost.setEntity( preparePayload( fileToUpload, boundary ) );

            for ( final Entry< String, String > header : requestHeaders.entrySet() ) {
                httpPost.addHeader( header.getKey(), header.getValue() );
            }

            try {
                response = httpClient.execute( httpPost );
            } catch ( Exception e ) {
                log.warn( e.getMessage() );
            }

            if ( response != null && response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 404
                    && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() );
            } else if ( response == null || response.getStatusLine().getStatusCode() == 404
                    || response.getStatusLine().getStatusCode() == 502 ) {
                response = retryApiCallUntillKarafUpOrExpiryTimeRunsOutPostRequest( httpPost, response );
            }

            final StringBuilder responseStrBuilder = new StringBuilder();

            try ( InputStreamReader inputStreamReader = new InputStreamReader( response.getEntity().getContent(), DEFAULT_ENCODING );
                    final BufferedReader streamReader = new BufferedReader( inputStreamReader ) ) {
                String inputStr;
                while ( ( inputStr = streamReader.readLine() ) != null ) {
                    responseStrBuilder.append( inputStr );
                }
            }

            map = ( Map< String, Object > ) JsonUtils.jsonToMap( responseStrBuilder.toString(), map );

            final List< Object > uploadedDocument = ( List< Object > ) map.get( FILES_KEY );
            final Map< String, Object > document = ( Map< String, Object > ) uploadedDocument.get( 0 );

            file.setDocId( ( Integer ) document.get( FILE_DOC_ID ) );
            file.setLocationId( ( Integer ) document.get( FILE_LOCATION_ID ) );
            file.setPath( ( String ) document.get( FILE_NAME ) );
            file.setSelType( ( String ) document.get( FILE_SELECTION_TYPE ) );

        } catch ( final IOException e ) {
            ExceptionLogger.logException( e, SuSClient.class );
        } finally {
            IOUtils.safeClose( response );
            IOUtils.safeClose( httpClient );
        }

        return file;
    }

    /**
     * Upload file request.
     *
     * @param url
     *         the url
     * @param localFile
     *         the local file
     * @param requestHeaders
     *         the request headers
     *
     * @return the document DTO
     */
    @SuppressWarnings( "resource" )
    public static DocumentDTO uploadFileRequest( String url, File localFile, Map< String, String > requestHeaders ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.UPLOAD, url ) );
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        DocumentDTO documentDTO = null;
        try {

            httpClient = getHttpsSslClient( url );
            httpPost = new HttpPost( url );
            final String boundary = BOUNDARY_PARAM + UUID.randomUUID();
            appendContentTypes( requestHeaders, boundary );
            if ( !localFile.exists() || localFile.isDirectory() ) {
                throw new SusException( MessagesUtil.getMessage( WFEMessages.FILE_TO_UPLOAD_SHOULD_EXIST ) );
            }

            httpPost.setEntity( preparePayload( localFile, boundary ) );

            for ( final Entry< String, String > header : requestHeaders.entrySet() ) {
                httpPost.addHeader( header.getKey(), header.getValue() );
            }

            try {
                response = httpClient.execute( httpPost );
            } catch ( Exception e ) {
                throw new SusException( e );
            }

            if ( response != null && response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 404
                    && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() );
            } else if ( response == null || response.getStatusLine().getStatusCode() == 404
                    || response.getStatusLine().getStatusCode() == 502 ) {
                response = retryApiCallUntillKarafUpOrExpiryTimeRunsOutPostRequest( httpPost, response );
            }

            final StringBuilder responseStrBuilder = new StringBuilder();

            try ( InputStreamReader inputStreamReader = new InputStreamReader( response.getEntity().getContent(), DEFAULT_ENCODING );
                    final BufferedReader streamReader = new BufferedReader( inputStreamReader ) ) {

                String inputStr;
                while ( ( inputStr = streamReader.readLine() ) != null ) {
                    responseStrBuilder.append( inputStr );
                }
            }

            SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( responseStrBuilder.toString(), SusResponseDTO.class );
            documentDTO = JsonUtils.jsonToObject( JsonUtils.toJson( susResponseDTO.getData() ), DocumentDTO.class );

        } catch ( final IOException e ) {
            ExceptionLogger.logException( e, SuSClient.class );
        } finally {
            IOUtils.safeClose( response );
            IOUtils.safeClose( httpClient );
        }

        return documentDTO;
    }

    /**
     * Upload file request.
     *
     * @param url
     *         the url
     * @param localFile
     *         the local file
     * @param requestHeaders
     *         the request headers
     */
    @SuppressWarnings( "resource" )
    public static void uploadStagingFileRequest( String url, File localFile, Map< String, String > requestHeaders ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.UPLOAD, url ) );
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {

            httpClient = getHttpsSslClient( url );
            httpPost = new HttpPost( url );
            final String boundary = BOUNDARY_PARAM + UUID.randomUUID();
            appendContentTypes( requestHeaders, boundary );
            if ( !localFile.exists() || !localFile.isFile() ) {
                throw new SusException( MessagesUtil.getMessage( WFEMessages.FILE_TO_UPLOAD_SHOULD_EXIST ) );
            }

            httpPost.setEntity( preparePayload( localFile, boundary ) );

            for ( final Entry< String, String > header : requestHeaders.entrySet() ) {
                httpPost.addHeader( header.getKey(), header.getValue() );
            }

            response = httpClient.execute( httpPost );

            if ( response != null && response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 404
                    && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() );
            } else if ( response == null || response.getStatusLine().getStatusCode() == 404
                    || response.getStatusLine().getStatusCode() == 502 ) {
                response = retryApiCallUntillKarafUpOrExpiryTimeRunsOutPostRequest( httpPost, response );
            }

        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, SuSClient.class );
        } finally {
            IOUtils.safeClose( response );
            IOUtils.safeClose( httpClient );
        }

    }

    /**
     * Prepare multipart payload.
     *
     * @param fileToUpload
     *         the file to upload
     * @param boundary
     *         the boundary
     *
     * @return the string
     */
    private static HttpEntity preparePayload( File fileToUpload, String boundary ) {
        final StringBody origin = new StringBody( DATAOBJECT_FILES_KEY, ContentType.MULTIPART_FORM_DATA );
        final MultipartEntityBuilder builder = MultipartEntityBuilder.create().addPart( ORIGIN, origin );
        builder.setBoundary( boundary );
        builder.setMode( HttpMultipartMode.BROWSER_COMPATIBLE );
        builder.setCharset( getCharset() );
        builder.addBinaryBody( FILES_KEY, fileToUpload, ContentType.DEFAULT_BINARY, fileToUpload.getName() );

        return builder.build();
    }

    /**
     * Gets the charset.
     *
     * @return the charset
     */
    private static Charset getCharset() {
        final String OS = System.getProperty( OS_NAME ).toLowerCase();

        if ( OS.equals( OS_LINUX_KEY ) ) {
            return Charset.forName( UTF_8 );
        } else {
            return Charset.forName( WINDOWS_1252 );
        }
    }

    /**
     * Append content types.
     *
     * @param requestHeaders
     *         the request headers
     * @param boundary
     *         the boundary
     */
    private static void appendContentTypes( Map< String, String > requestHeaders, String boundary ) {

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ContentType.MULTIPART_FORM_DATA.getMimeType() + ";boundary=" + boundary );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
    }

    /**
     * This function will be used to download file from SimuSpace 1.1
     *
     * @param url
     *         the url of file to download.
     * @param destinationPath
     *         the destination file path either file or directory if it's file then file to download will download as new file if it's
     *         directory then will download as original in that directory.
     * @param requestHeaders
     *         the request headers required to authentication request .
     * @param errors
     *         the errors
     *
     * @return the sus response DTO
     */
    public static String downloadRequest( String url, String destinationPath, Map< String, String > requestHeaders, List< Error > errors ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.DOWNLOAD, url ) );
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        String filePath = "";

        try {

            httpClient = getHttpsSslClient( url );
            httpGet = new HttpGet( url );

            for ( final Entry< String, String > h : requestHeaders.entrySet() ) {
                httpGet.addHeader( h.getKey(), h.getValue() );
            }

            try {
                response = httpClient.execute( httpGet );
            } catch ( Exception e ) {
                log.warn( e.getMessage() );
            }

            if ( response != null && response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 404
                    && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() );
            } else if ( response == null || response.getStatusLine().getStatusCode() == 404
                    || response.getStatusLine().getStatusCode() == 502 ) {
                response = retryApiCallUntillKarafUpOrExpiryTimeRunsOutGetRequest( httpGet, response );
            }

            try ( final InputStream retStream = response.getEntity().getContent() ) {

                final Header header = response.getLastHeader( OPTIONAL_HEADER_CONTAINING_DATA );
                final String value = header.getValue();
                String fileName = "";
                if ( value.contains( KEY_CONTAINS_FILE_NAME ) ) {
                    final String[] a = value.split( KEY_VALUE_SEPARATOR );
                    if ( a.length == KEY_VALUE_PAIR_LENGTH ) {
                        fileName = a[ 1 ].replace( STRING_TO_REPLACE, NEW_STRING );
                    }
                }

                if ( new File( destinationPath ).isDirectory() ) {
                    filePath = destinationPath + File.separator + fileName;
                } else {
                    filePath = destinationPath;
                }

                writeStreamToFile( filePath, retStream );

            }

        } catch ( final IOException e ) {
            ExceptionLogger.logException( e, SuSClient.class );
        } finally {
            IOUtils.safeClose( response );
            IOUtils.safeClose( httpClient );

        }
        return filePath;

    }

    /**
     * Download request with progres bar.
     *
     * @param url
     *         the url
     * @param destinationPath
     *         the destination path
     * @param requestHeaders
     *         the request headers
     * @param errors
     *         the errors
     * @param transferInfo
     *         the transfer info
     *
     * @return the string
     */
    @SuppressWarnings( "resource" )
    public static String downloadRequestWithProgresBar( String url, String destinationPath, Map< String, String > requestHeaders,
            List< Error > errors, TransferInfo transferInfo ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.DOWNLOAD, url ) );
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        String filePath = "";

        try {

            httpClient = getHttpsSslClient( url );
            httpGet = new HttpGet( url );
            transferInfo.setHttpGet( httpGet );

            for ( final Entry< String, String > h : requestHeaders.entrySet() ) {
                httpGet.addHeader( h.getKey(), h.getValue() );
            }

            try {
                response = httpClient.execute( httpGet );
            } catch ( Exception e ) {
                log.warn( e.getMessage() );
            }

            if ( httpGet.isAborted() ) {
                throw new SusException( TRANSFER_ABORTED );
            } else if ( response != null && response.getStatusLine().getStatusCode() != 200
                    && response.getStatusLine().getStatusCode() != 404 && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() );
            } else if ( response == null || response.getStatusLine().getStatusCode() == 404
                    || response.getStatusLine().getStatusCode() == 502 ) {
                response = retryApiCallUntillKarafUpOrExpiryTimeRunsOutGetRequest( httpGet, response );
            }

            try ( PushbackInputStream input = new PushbackInputStream( response.getEntity().getContent() ) ) {

                final Header header = response.getLastHeader( OPTIONAL_HEADER_CONTAINING_DATA );
                final String value = header.getValue();
                String fileName = "";
                if ( value.contains( KEY_CONTAINS_FILE_NAME ) ) {
                    final String[] a = value.split( KEY_VALUE_SEPARATOR );
                    if ( a.length == KEY_VALUE_PAIR_LENGTH ) {
                        fileName = a[ 1 ].replace( STRING_TO_REPLACE, NEW_STRING );
                    }
                }

                Header sizeHeader = response.getFirstHeader( HEADER_CONTAINING_FILE_SIZE );
                int totalSize = ConstantsInteger.INTEGER_VALUE_ONE;
                if ( null != sizeHeader && NumberUtils.isNumber( sizeHeader.getValue() ) ) {
                    totalSize = Integer.parseInt( sizeHeader.getValue() );
                    if ( totalSize == 0 ) {
                        totalSize = ConstantsInteger.INTEGER_VALUE_ONE;
                    }
                }

                if ( new File( destinationPath ).isDirectory() ) {
                    filePath = destinationPath + File.separator + fileName;
                } else {
                    filePath = destinationPath;
                }

                int size = 0;
                double total = 0;
                final byte[] bytes = new byte[ 1024 ];
                double temp;
                try ( final OutputStream outputStream = new FileOutputStream( new File( filePath ) ) ) {
                    while ( ( size = input.read( bytes ) ) != EOF ) {
                        total += size;
                        temp = ( total / totalSize );
                        transferInfo.setProgess( Math.round( temp * 100 ) );
                        outputStream.write( bytes, 0, size );
                    }
                } catch ( Exception e ) {
                    ExceptionLogger.logException( e, SuSClient.class );
                }
            }
        } catch ( final IOException e ) {
            ExceptionLogger.logException( e, SuSClient.class );
        } finally {
            IOUtils.safeClose( response );
            IOUtils.safeClose( httpClient );
        }
        return filePath;

    }

    /**
     * Upload file request with progress.
     *
     * @param url
     *         the url
     * @param localFile
     *         the local file
     * @param requestHeaders
     *         the request headers
     * @param transferInfo
     *         the transfer info
     *
     * @return the document DTO
     */
    @SuppressWarnings( "resource" )
    public static DocumentDTO uploadFileRequestWithProgress( String url, File localFile, Map< String, String > requestHeaders,
            TransferInfo transferInfo ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.DOWNLOAD, url ) );
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        DocumentDTO documentDTO = null;
        try {

            httpClient = getHttpsSslClient( url );
            httpPost = new HttpPost( url );
            final String boundary = BOUNDARY_PARAM + UUID.randomUUID();
            appendContentTypes( requestHeaders, boundary );
            if ( !localFile.exists() || !localFile.isFile() ) {
                throw new SusException( MessagesUtil.getMessage( WFEMessages.FILE_TO_UPLOAD_SHOULD_EXIST ) );
            }

            HttpEntity httpEntity = preparePayload( localFile, boundary );

            ProgressEntityWrapper.ProgressListener pListener = new ProgressEntityWrapper.ProgressListener() {

                @Override
                public void progress( float percentage ) {
                    transferInfo.setProgess( ( long ) percentage );
                }
            };

            transferInfo.setHttpPost( httpPost );

            httpPost.setEntity( new ProgressEntityWrapper( httpEntity, pListener ) );

            for ( final Entry< String, String > header : requestHeaders.entrySet() ) {
                httpPost.addHeader( header.getKey(), header.getValue() );
            }

            try {
                response = httpClient.execute( httpPost );
            } catch ( Exception e ) {
                log.warn( e.getMessage() );
            }

            if ( httpPost.isAborted() ) {
                throw new SusException( TRANSFER_ABORTED );
            } else if ( response != null && response.getStatusLine().getStatusCode() != 200
                    && response.getStatusLine().getStatusCode() != 404 && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() );
            } else if ( response == null || response.getStatusLine().getStatusCode() == 404
                    || response.getStatusLine().getStatusCode() == 502 ) {
                response = retryApiCallUntillKarafUpOrExpiryTimeRunsOutPostRequest( httpPost, response );
            }

            final StringBuilder responseStrBuilder = new StringBuilder();

            try ( InputStreamReader inputStreamReader = new InputStreamReader( response.getEntity().getContent(), DEFAULT_ENCODING );
                    final BufferedReader streamReader = new BufferedReader( inputStreamReader ) ) {

                String inputStr;
                while ( ( inputStr = streamReader.readLine() ) != null ) {
                    responseStrBuilder.append( inputStr );
                }
            }
            SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( responseStrBuilder.toString(), SusResponseDTO.class );
            documentDTO = JsonUtils.jsonToObject( JsonUtils.toJson( susResponseDTO.getData() ), DocumentDTO.class );

        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, SuSClient.class );
        } finally {
            IOUtils.safeClose( response );
            IOUtils.safeClose( httpClient );
        }

        return documentDTO;
    }

    /**
     * Write stream to file.
     *
     * @param filePath
     *         the file path
     * @param retStream
     *         the ret stream
     */
    public static void writeStreamToFile( String filePath, final InputStream retStream ) {

        try ( final OutputStream outputStream = new FileOutputStream( filePath ) ) {

            int read = 0;
            final byte[] bytes = new byte[ 1024 ];

            while ( ( read = retStream.read( bytes ) ) != EOF ) {
                outputStream.write( bytes, 0, read );
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, SuSClient.class );
        }
    }

    /**
     * Checks if is time expired for api call.
     *
     * @param startTime
     *         the start time
     * @param apiRetryTimeout
     *         the api retry timeout
     *
     * @return true, if is time expired for api call
     */
    private static boolean isTimeExpiredForApiCall( LocalDateTime startTime, long apiRetryTimeout ) {
        long millisecond = ChronoUnit.SECONDS.between( startTime, LocalDateTime.now() ) * 1000;
        return millisecond > apiRetryTimeout;
    }

    /**
     * Retry api call untill karaf up or expiry time runs out get request.
     *
     * @param httpGet
     *         the http get
     * @param response
     *         the response
     *
     * @return the closeable http response
     */
    private static CloseableHttpResponse retryApiCallUntillKarafUpOrExpiryTimeRunsOutGetRequest( HttpGet httpGet,
            CloseableHttpResponse response ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.RETRY_GET,
                httpGet.getURI() ) );
        LocalDateTime apiNotFoundTime = null;
        LocalDateTime startTime = LocalDateTime.now();
        while ( !isTimeExpiredForApiCall( startTime, ( CommonProperties.getExpiryTimeWhenKarafDown() == 0 ? API_EXPIRY_TIME_WHEN_KARAF_DOWN
                : CommonProperties.getExpiryTimeWhenKarafDown() ) ) ) {
            try {
                Thread.sleep( ( CommonProperties.getDelayBetweenKarafDown() == 0 ? API_CALL_DELAY_BETWEEN_KARAF_DOWN
                        : CommonProperties.getDelayBetweenKarafDown() ) );
                CloseableHttpClient httpClients = HttpClients.createDefault();
                response = null;
                response = httpClients.execute( httpGet );
            } catch ( InterruptedException e ) {
                log.warn( MessageBundleFactory.getDefaultMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }

            if ( response != null && response.getStatusLine().getStatusCode() == 200 ) {
                break;
            } else if ( response != null && response.getStatusLine().getStatusCode() != 200
                    && response.getStatusLine().getStatusCode() != 404 && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() );
            } else if ( response == null ) {
                if ( isTimeExpiredForApiCall( startTime,
                        ( CommonProperties.getExpiryTimeWhenKarafDown() == 0 ? API_EXPIRY_TIME_WHEN_KARAF_DOWN
                                : CommonProperties.getExpiryTimeWhenKarafDown() ) ) ) {
                    break;
                }
            } else if ( response != null
                    && ( response.getStatusLine().getStatusCode() == 404 || response.getStatusLine().getStatusCode() == 502 ) ) {
                if ( apiNotFoundTime == null ) {
                    apiNotFoundTime = LocalDateTime.now();
                }
                if ( isTimeExpiredForApiCall( apiNotFoundTime, API_EXPIRY_TIME_WHEN_API_NOT_FOUND ) ) {
                    break;
                }
            }
        }
        return response;
    }

    /**
     * Retry api call untill karaf up or expiry time runs out put request.
     *
     * @param httpPut
     *         the http put
     * @param response
     *         the response
     *
     * @return the closeable http response
     */
    private static CloseableHttpResponse retryApiCallUntillKarafUpOrExpiryTimeRunsOutPutRequest( HttpPut httpPut,
            CloseableHttpResponse response ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.RETRY_PUT,
                httpPut.getURI() ) );
        LocalDateTime apiNotFoundTime = null;
        LocalDateTime startTime = LocalDateTime.now();
        while ( !isTimeExpiredForApiCall( startTime, ( CommonProperties.getExpiryTimeWhenKarafDown() == 0 ? API_EXPIRY_TIME_WHEN_KARAF_DOWN
                : CommonProperties.getExpiryTimeWhenKarafDown() ) ) ) {
            try {
                Thread.sleep( ( CommonProperties.getDelayBetweenKarafDown() == 0 ? API_CALL_DELAY_BETWEEN_KARAF_DOWN
                        : CommonProperties.getDelayBetweenKarafDown() ) );
                CloseableHttpClient httpClients = getHttpsSslClient( httpPut.getURI().toString() );
                response = null;
                response = httpClients.execute( httpPut );
            } catch ( InterruptedException e ) {
                log.warn( MessageBundleFactory.getDefaultMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
            } catch ( Exception e ) {
                log.warn( e.getMessage() );
            }

            if ( response != null && response.getStatusLine().getStatusCode() == 200 ) {
                break;
            } else if ( response != null && response.getStatusLine().getStatusCode() != 200
                    && response.getStatusLine().getStatusCode() != 404 && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() );
            } else if ( response == null ) {
                if ( isTimeExpiredForApiCall( startTime,
                        ( CommonProperties.getExpiryTimeWhenKarafDown() == 0 ? API_EXPIRY_TIME_WHEN_KARAF_DOWN
                                : CommonProperties.getExpiryTimeWhenKarafDown() ) ) ) {
                    break;
                }
            } else if ( response != null
                    && ( response.getStatusLine().getStatusCode() == 404 || response.getStatusLine().getStatusCode() == 502 ) ) {
                if ( apiNotFoundTime == null ) {
                    apiNotFoundTime = LocalDateTime.now();
                }
                if ( isTimeExpiredForApiCall( apiNotFoundTime, API_EXPIRY_TIME_WHEN_API_NOT_FOUND ) ) {
                    break;
                }
            }
        }
        return response;
    }

    /**
     * Retry api call untill karaf up or expiry time runs out post request.
     *
     * @param httpPost
     *         the http post
     * @param response
     *         the response
     *
     * @return the closeable http response
     */
    private static CloseableHttpResponse retryApiCallUntillKarafUpOrExpiryTimeRunsOutPostRequest( HttpPost httpPost,
            CloseableHttpResponse response ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.RETRY_POST,
                httpPost.getURI() ) );
        LocalDateTime apiNotFoundTime = null;
        LocalDateTime startTime = LocalDateTime.now();
        while ( !isTimeExpiredForApiCall( startTime, ( CommonProperties.getExpiryTimeWhenKarafDown() == 0 ? API_EXPIRY_TIME_WHEN_KARAF_DOWN
                : CommonProperties.getExpiryTimeWhenKarafDown() ) ) ) {
            try {
                Thread.sleep( ( CommonProperties.getDelayBetweenKarafDown() == 0 ? API_CALL_DELAY_BETWEEN_KARAF_DOWN
                        : CommonProperties.getDelayBetweenKarafDown() ) );
                CloseableHttpClient httpClients = getHttpsSslClient( httpPost.getURI().toString() );
                response = null;
                response = httpClients.execute( httpPost );
            } catch ( InterruptedException e ) {
                log.warn( MessageBundleFactory.getDefaultMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
            } catch ( Exception e ) {
                log.warn( e.getMessage() );
            }

            if ( response != null && response.getStatusLine().getStatusCode() == 200 ) {
                break;
            } else if ( response != null && response.getStatusLine().getStatusCode() != 200
                    && response.getStatusLine().getStatusCode() != 404 && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() );
            } else if ( response == null ) {
                if ( isTimeExpiredForApiCall( startTime,
                        ( CommonProperties.getExpiryTimeWhenKarafDown() == 0 ? API_EXPIRY_TIME_WHEN_KARAF_DOWN
                                : CommonProperties.getExpiryTimeWhenKarafDown() ) ) ) {
                    break;
                }
            } else if ( response != null
                    && ( response.getStatusLine().getStatusCode() == 404 || response.getStatusLine().getStatusCode() == 502 ) ) {
                if ( apiNotFoundTime == null ) {
                    apiNotFoundTime = LocalDateTime.now();
                }
                if ( isTimeExpiredForApiCall( apiNotFoundTime, API_EXPIRY_TIME_WHEN_API_NOT_FOUND ) ) {
                    break;
                }
            }
        }
        return response;
    }

    /**
     * Retry api call untill karaf up or expiry time runs out delete request.
     *
     * @param httpDelete
     *         the http delete
     * @param response
     *         the response
     *
     * @return the closeable http response
     */
    private static CloseableHttpResponse retryApiCallUntillKarafUpOrExpiryTimeRunsOutDeleteRequest( HttpDelete httpDelete,
            CloseableHttpResponse response ) {
        log.info( MessageBundleFactory.getDefaultMessage( Messages.INTERNAL_API_LOG_MESSAGE.getKey(), ConstantsAPITypes.RETRY_DELETE,
                httpDelete.getURI() ) );
        LocalDateTime apiNotFoundTime = null;
        LocalDateTime startTime = LocalDateTime.now();
        while ( !isTimeExpiredForApiCall( startTime, ( CommonProperties.getExpiryTimeWhenKarafDown() == 0 ? API_EXPIRY_TIME_WHEN_KARAF_DOWN
                : CommonProperties.getExpiryTimeWhenKarafDown() ) ) ) {
            try {
                Thread.sleep( ( CommonProperties.getDelayBetweenKarafDown() == 0 ? API_CALL_DELAY_BETWEEN_KARAF_DOWN
                        : CommonProperties.getDelayBetweenKarafDown() ) );
                CloseableHttpClient httpClients = getHttpsSslClient( httpDelete.getURI().toString() );
                response = null;
                response = httpClients.execute( httpDelete );
            } catch ( InterruptedException e ) {
                log.warn( MessageBundleFactory.getDefaultMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
            } catch ( Exception e ) {
                log.warn( e.getMessage() );
            }

            if ( response != null && response.getStatusLine().getStatusCode() == 200 ) {
                break;
            } else if ( response != null && response.getStatusLine().getStatusCode() != 200
                    && response.getStatusLine().getStatusCode() != 404 && response.getStatusLine().getStatusCode() != 502 ) {
                throw new SusException( FAILED_HTTP_ERROR_CODE + response.getStatusLine().getStatusCode() );
            } else if ( response == null ) {
                if ( isTimeExpiredForApiCall( startTime,
                        ( CommonProperties.getExpiryTimeWhenKarafDown() == 0 ? API_EXPIRY_TIME_WHEN_KARAF_DOWN
                                : CommonProperties.getExpiryTimeWhenKarafDown() ) ) ) {
                    break;
                }
            } else if ( response != null
                    && ( response.getStatusLine().getStatusCode() == 404 || response.getStatusLine().getStatusCode() == 502 ) ) {
                if ( apiNotFoundTime == null ) {
                    apiNotFoundTime = LocalDateTime.now();
                }
                if ( isTimeExpiredForApiCall( apiNotFoundTime, API_EXPIRY_TIME_WHEN_API_NOT_FOUND ) ) {
                    break;
                }
            }
        }
        return response;
    }

    /**
     * Prepare download headers.
     *
     * @param authToken
     *         the auth token
     *
     * @return the map
     */
    public static Map< String, String > prepareDownloadHeaders( String authToken ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, authToken );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTHORIZATION, authToken );
        return requestHeaders;

    }

    /**
     * Private a constructor to hide implicit one.
     */
    private SuSClient() {
        super();
    }

    /**
     * Prepare sus response DTO.
     *
     * @param response
     *         the response
     *
     * @return the sus response DTO
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private static SusResponseDTO prepareSusResponseDTO( CloseableHttpResponse response ) throws IOException {
        SusResponseDTO susResponseDTO;
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString( entity, DEFAULT_ENCODING );
        Map< String, String > map = new HashMap<>();
        try {
            susResponseDTO = JsonUtils.jsonToObject( responseString, SusResponseDTO.class );
        } catch ( Exception e ) {
            map = ( Map< String, String > ) JsonUtils.jsonToMap( responseString, map );
            susResponseDTO = JsonUtils.jsonToObject( map.get( RESPONSE_ENTITY ), SusResponseDTO.class );
        }
        return susResponseDTO;
    }

}