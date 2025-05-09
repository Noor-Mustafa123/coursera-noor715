package de.soco.software.simuspace.suscore.common.client;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * The Class CB2Client to hit BMW bench apis.
 *
 * @author noman arshad
 * @since 2.0
 */
@Log4j2
public class CB2Client {

    /* move this to a config file -> CB2 recommends 8443 or 7443 for REST calls */
    private static final int CB2_PORT = PropertiesManager.cb2Port();

    private static final String CB2_IP = PropertiesManager.cb2Ip();

    /**
     * Post CB 2.
     *
     * @param userName
     *         the user name
     * @param cb2QueryPayload
     *         the cb 2 query payload
     *
     * @return the JSON object
     *
     * @throws NoSuchAlgorithmException
     *         the no such algorithm exception
     * @throws KeyStoreException
     *         the key store exception
     * @throws KeyManagementException
     *         the key management exception
     * @throws UnsupportedEncodingException
     *         the unsupported encoding exception
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws ClientProtocolException
     *         the client protocol exception
     */
    public static JSONObject PostCB2( String userName, List< NameValuePair > cb2QueryPayload )
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {

        CookieStore cookieStore = readCookiesFromSessionFileWithImpersonation( userName );
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore( cookieStore );

        final SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial( null, new TrustSelfSignedStrategy() );
        final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory( builder.build(),
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore( cookieStore ).setSSLSocketFactory( sslsf ).build();
        HttpHost targetHost = new HttpHost( CB2_IP, CB2_PORT, "https" );
        HttpPost requestCb2QueryMain = new HttpPost( "/cb2/servlet/rest/query" );
        requestCb2QueryMain.setEntity( new UrlEncodedFormEntity( cb2QueryPayload, "UTF-8" ) );
        if ( log.isDebugEnabled() ) {
            log.debug(
                    "cb2 query main:" + requestCb2QueryMain + " " + requestCb2QueryMain.getEntity() + " " + requestCb2QueryMain.getURI() );
            log.debug( "cb2 targetHost:" + targetHost );
        }
        CloseableHttpResponse cb2QueryResponse = httpclient.execute( targetHost, requestCb2QueryMain, context );
        JSONObject jsonQueryResponseObj = new JSONObject( EntityUtils.toString( cb2QueryResponse.getEntity() ) );
        if ( log.isDebugEnabled() ) {
            log.debug( "CB2 POST Query response :::::::::: " + jsonQueryResponseObj );
        }
        if ( jsonQueryResponseObj != null && jsonQueryResponseObj.has( "code" )
                && !jsonQueryResponseObj.get( "code" ).toString().equals( "200" ) ) {

            throw new SusException( MessageBundleFactory.getMessage( Messages.CB2_API_ATTEMPT_FAILED.getKey(),
                    ( jsonQueryResponseObj.has( "msg" ) ? jsonQueryResponseObj.get( "msg" ) : "" ) ) );
        }
        return jsonQueryResponseObj;
    }

    /**
     * Gets the CB 2.
     *
     * @param userName
     *         the user name
     * @param buildFilterQuery
     *         the build filter query
     *
     * @return the JSON object
     *
     * @throws NoSuchAlgorithmException
     *         the no such algorithm exception
     * @throws KeyStoreException
     *         the key store exception
     * @throws KeyManagementException
     *         the key management exception
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws ClientProtocolException
     *         the client protocol exception
     */
    public static JSONObject GetCB2( String userName, String buildFilterQuery )
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        JSONObject jsonObject = GetCB2( userName, "/cb2/servlet/rest/query?", buildFilterQuery );
        if ( log.isDebugEnabled() ) {
            log.debug( "CB2 GET response :::::::::: " + jsonObject );
        }
        return jsonObject;
    }

    public static JSONObject GetCB2( String userName, String servlet, String buildFilterQuery )
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        // cb2 cookies
        CookieStore cookieStore = readCookiesFromSessionFileWithImpersonation( userName );
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore( cookieStore );

        final SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial( null, new TrustSelfSignedStrategy() );
        final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory( builder.build(),
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore( cookieStore ).setSSLSocketFactory( sslsf ).build();
        HttpHost targetHost = new HttpHost( CB2_IP, CB2_PORT, "https" );
        HttpGet getRequestCb2QueryMain = new HttpGet( servlet + buildFilterQuery );
        CloseableHttpResponse cb2QueryResponse = httpclient.execute( targetHost, getRequestCb2QueryMain, context );
        JSONObject jsonQueryResponseObj = new JSONObject( EntityUtils.toString( cb2QueryResponse.getEntity() ) );

        if ( jsonQueryResponseObj != null && jsonQueryResponseObj.has( "code" )
                && !jsonQueryResponseObj.get( "code" ).toString().equals( "200" ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.CB2_API_ATTEMPT_FAILED.getKey(),
                    ( jsonQueryResponseObj.has( "msg" ) ? jsonQueryResponseObj.get( "msg" ) : "" ) ) );
        }
        return jsonQueryResponseObj;
    }

    public static String GetCb2FileOrDownlaod( String userName, String cb2QueryPayload, String destinationPath )
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        // cb2 cookies
        CookieStore cookieStore = readCookiesFromSessionFileWithImpersonation( userName );
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore( cookieStore );

        final SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial( null, new TrustSelfSignedStrategy() );
        final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory( builder.build(),
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore( cookieStore ).setSSLSocketFactory( sslsf ).build();
        HttpHost targetHost = new HttpHost( CB2_IP, CB2_PORT, "https" );
        HttpGet getRequestCb2QueryMain = new HttpGet( "/cb2/servlet/rest" + cb2QueryPayload );
        CloseableHttpResponse cb2QueryResponse = httpclient.execute( targetHost, getRequestCb2QueryMain, context );
        if ( log.isDebugEnabled() ) {
            log.debug( "CB2 FILE response :::::::::: " + cb2QueryResponse );
        }
        /*
         * try { JSONObject jsonQueryResponseObj = new
         * JSONObject(EntityUtils.toString(cb2QueryResponse.getEntity()));
         *
         * if (jsonQueryResponseObj != null && jsonQueryResponseObj.has("code") &&
         * !jsonQueryResponseObj.get("code").toString().equals("200")) { throw new
         * SusException(MessageBundleFactory.getMessage(Messages.CB2_API_ATTEMPT_FAILED.
         * getKey(), (jsonQueryResponseObj.has("msg") ? jsonQueryResponseObj.get("msg")
         * : ""))); } } catch (Exception e) { throw new SusException(e.getMessage()); }
         */

        String filePath = "";
        try ( final InputStream retStream = cb2QueryResponse.getEntity().getContent() ) {

            final Header header = cb2QueryResponse.getLastHeader( SuSClient.OPTIONAL_HEADER_CONTAINING_DATA );
            final String value = header.getValue();
            String fileName = "";
            if ( value.contains( SuSClient.KEY_CONTAINS_FILE_NAME ) ) {
                final String[] a = value.split( SuSClient.KEY_VALUE_SEPARATOR );
                if ( a.length == SuSClient.KEY_VALUE_PAIR_LENGTH ) {
                    fileName = a[ 1 ].replace( SuSClient.STRING_TO_REPLACE, SuSClient.NEW_STRING );
                }
            }

            if ( new File( destinationPath ).isDirectory() ) {
                filePath = destinationPath + File.separator + fileName;
            } else {
                filePath = destinationPath;
            }
            SuSClient.writeStreamToFile( filePath, retStream );
        }

        return filePath;
    }

    public static InputStream GetCb2FileOrDownlaod( String userName, String cb2QueryPayload )
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        // cb2 cookies
        CookieStore cookieStore = readCookiesFromSessionFileWithImpersonation( userName );
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore( cookieStore );

        final SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial( null, new TrustSelfSignedStrategy() );
        final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory( builder.build(),
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore( cookieStore ).setSSLSocketFactory( sslsf ).build();
        HttpHost targetHost = new HttpHost( CB2_IP, CB2_PORT, "https" );
        HttpGet getRequestCb2QueryMain = new HttpGet( "/cb2/servlet/rest" + cb2QueryPayload );
        CloseableHttpResponse cb2QueryResponse = httpclient.execute( targetHost, getRequestCb2QueryMain, context );
        if ( log.isDebugEnabled() ) {
            log.debug( "CB2 FILE response :::::::::: " + cb2QueryResponse );
        }
        return cb2QueryResponse.getEntity().getContent();
    }

    /**
     * Read cookies from session file with impersonation.
     *
     * @param userName
     *         the user name
     *
     * @return the cookie store
     */
    public static CookieStore readCookiesFromSessionFileWithImpersonation( String userName ) {
        try {
            String srcFileCookie = "/home/" + userName + "/.sus/cookies.txt";
            String destFileCookie = PropertiesManager.getDefaultServerTempPath() + File.separator + "cookie_" + UUID.randomUUID() + ".txt";
            LinuxUtils.copyFileFromSrcPathToDestPathWithImpersonation( userName, srcFileCookie, destFileCookie );
            CookieStore cookieStore = new BasicCookieStore();
            File cookieFile = new File( destFileCookie );
            FileUtils.setGlobalReadFilePermissions( cookieFile );
            BasicClientCookie cookie = null;
            JSONParser parser = new JSONParser();
            Object obj = null;
            try {
                obj = parser.parse( new FileReader( cookieFile ) );
            } catch ( Exception e1 ) {
                throw new SusException( "No CB2 session file exists for this user" );
            }
            Map< String, String > map = JsonUtils.convertStringToMap( JsonUtils.toJson( obj ) );

            // load cookies form cookie file json
            for ( Entry< String, String > entry : map.entrySet() ) {
                cookie = new BasicClientCookie( entry.getKey(), entry.getValue() );
            }
            // delete cookie file : IMPORTENT
            cookieFile.delete();

            // set basic cookies
            cookie.setDomain( CB2_IP );
            cookie.setPath( "/cb2" );
            cookie.setVersion( 0 );
            cookieStore.addCookie( cookie );
            return cookieStore;
        } catch ( final Exception e ) {
            throw new SusException( e, CB2Client.class );
        }
    }

}
