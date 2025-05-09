package de.soco.software.simuspace.suscore.authentication.activator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

@Log4j2
public class Activator implements BundleActivator {

    private static final String WEB_EAM_WELL_KNOWN_URLS = "web.eam.well_known_url";

    private static final String WEB_EAM_IS_ENABLED = "web.eam.enabled";

    public static final JsonNode WELL_KNOWNS_URL = oauth2WellKnownUrls();

    @Override
    public void start( BundleContext context ) throws Exception {
    }

    @Override
    public void stop( BundleContext context ) throws Exception {
    }

    private static JsonNode oauth2WellKnownUrls() {
        JsonNode wellKnownUrlsLocal = null;
        Properties oauthProperties = getOauth2PropertiesFile();
        if ( Boolean.parseBoolean( oauthProperties.getProperty( WEB_EAM_IS_ENABLED ) ) ) {
            final Map< String, String > requestHeaders = new HashMap<>();
            requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
            String url = oauthProperties.getProperty( ConstantsFileProperties.WEB_EAM_ROOT ) + "/"
                    + oauthProperties.getProperty( ConstantsFileProperties.WEB_EAM_REALM )
                    + oauthProperties.getProperty( WEB_EAM_WELL_KNOWN_URLS );
            CloseableHttpResponse closeableHttpResponse = SuSClient.getExternalRequest( url, requestHeaders );
            try {
                HttpEntity entity = closeableHttpResponse.getEntity();
                String responseString = EntityUtils.toString( entity, "UTF-8" );
                log.info( responseString );
                wellKnownUrlsLocal = JsonUtils.toJsonNode( responseString );
            } catch ( Exception e ) {
                log.info( "OAuth not configured " + e.getMessage() );
            }
        }
        return wellKnownUrlsLocal;
    }

    private static Properties getOauth2PropertiesFile() {
        Properties propertiesLocal = new Properties();
        final String hibernate_cfg = ConstantsKaraf.KARAF_HOME + File.separator + ConstantsKaraf.KARAF_CONF + File.separator
                + ConstantsString.WEN_CFG;
        try ( InputStream hibernateCfgStream = new FileInputStream( hibernate_cfg ) ) {
            propertiesLocal.load( hibernateCfgStream );
        } catch ( final IOException e ) {
            ExceptionLogger.logMessage( e.getMessage() );
            throw new SusDataBaseException(
                    ( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), ConstantsString.HIBERNATE_CFG ) ) );
        }
        return propertiesLocal;
    }

}
