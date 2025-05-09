package de.soco.software.simuspace.suscore.local.daemon.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class LocalProjectConfig.
 *
 * @author Ali Haider
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class LocalProjectConfig {

    /**
     * The local dir.
     */
    private String local_sync_dir;

    /**
     * The file types.
     */
    private Map< String, List< String > > configuration;

    public String getLocal_sync_dir() {
        return local_sync_dir;
    }

    public void setLocal_sync_dir( String local_sync_dir ) {
        this.local_sync_dir = local_sync_dir;
    }

    public Map< String, List< String > > getConfiguration() {
        return configuration;
    }

    public void setConfiguration( Map< String, List< String > > configuration ) {
        this.configuration = configuration;
    }

}
