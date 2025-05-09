package de.soco.software.simuspace.suscore.common.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import lombok.extern.log4j.Log4j2;

/**
 * The Class HttpUtils only deals with HTTP client for utility purposes available globally in the system.
 *
 * @author Ahsan Khan
 */
@Log4j2
public class HttpUtils {

    /**
     * The Constant HTTPS.
     */
    private static final CharSequence HTTPS = "https://";

    /**
     * The Constant HTTP.
     */
    private static final CharSequence HTTP = "http://";

    /**
     * The Constant TIME_OUT_LIMIT for pinging address.
     */
    private static final int TIME_OUT_LIMIT = 1000 * 10;

    /**
     * The Constant SSL.
     */
    private static final String SSL = "SSL";

    /**
     * Ping url.
     *
     * @param address
     *         the address
     *
     * @return true, if successful
     */
    public static boolean pingUrl( final String address ) {
        try {
            final URL url = new URL( address );
            if ( address.contains( HTTPS ) ) {
                return isUpForHttps( url );
            } else if ( address.contains( HTTP ) ) {
                return isUpForHttp( url );
            }
        } catch ( final IOException e ) {
            log.warn( e.getMessage(), e );
            return false;
        }
        return false;
    }

    /**
     * Checks if is up for https.
     *
     * @param url
     *         the url
     *
     * @return true, if is up for https
     */
    private static boolean isUpForHttps( final URL url ) {
        boolean isUp = true;
        HttpsURLConnection httpsUrlConn;
        try {
            httpsUrlConn = ( HttpsURLConnection ) url.openConnection();
            HttpUtils.setupHttpsConncetion();
            httpsUrlConn.setConnectTimeout( TIME_OUT_LIMIT );
            httpsUrlConn.getResponseCode();
        } catch ( Exception e ) {
            isUp = false;
        }
        return isUp;
    }

    /**
     * Checks if is up for http.
     *
     * @param url
     *         the url
     *
     * @return true, if is up for http
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public static boolean isUpForHttp( final URL url ) throws IOException {
        boolean isUp = true;
        HttpURLConnection httpUrlConn;
        httpUrlConn = ( HttpURLConnection ) url.openConnection();
        httpUrlConn.setConnectTimeout( TIME_OUT_LIMIT );
        httpUrlConn.getResponseCode();
        return isUp;
    }

    /**
     * Setup https conncetion.
     *
     * @throws NoSuchAlgorithmException
     *         the no such algorithm exception
     * @throws KeyManagementException
     *         the key management exception
     */
    private static void setupHttpsConncetion() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{ new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted( X509Certificate[] certs, String authType ) {
            }

            public void checkServerTrusted( X509Certificate[] certs, String authType ) {
            }

            public void checkClientTrusted( java.security.cert.X509Certificate[] arg0, String arg1 ) {
            }

            public void checkServerTrusted( java.security.cert.X509Certificate[] arg0, String arg1 ) {
            }
        } };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance( SSL );
        sc.init( null, trustAllCerts, new java.security.SecureRandom() );
        HttpsURLConnection.setDefaultSSLSocketFactory( sc.getSocketFactory() );
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {

            public boolean verify( String hostname, SSLSession session ) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier( allHostsValid );
    }

}