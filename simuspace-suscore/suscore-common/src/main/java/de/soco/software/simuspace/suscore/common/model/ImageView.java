package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Agent mapping class into json.
 *
 * @author Ali Haider
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ImageView implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -1924174863028529176L;

    /**
     * The browser.
     */
    private List< DocumentDTO > browser;

    /**
     * The table.
     */
    private String table;

    /**
     * The client.
     */
    private List< DocumentDTO > client;

    /**
     * The server.
     */
    private List< DocumentDTO > server;

    /**
     * Gets the browser.
     *
     * @return the browser
     */
    public List< DocumentDTO > getBrowser() {
        return browser;
    }

    /**
     * Sets the browser.
     *
     * @param browser
     *         the new browser
     */
    public void setBrowser( List< DocumentDTO > browser ) {
        this.browser = browser;
    }

    /**
     * Gets the table.
     *
     * @return the table
     */
    public String getTable() {
        return table;
    }

    /**
     * Sets the table.
     *
     * @param table
     *         the new table
     */
    public void setTable( String table ) {
        this.table = table;
    }

    /**
     * Gets the client.
     *
     * @return the client
     */
    public List< DocumentDTO > getClient() {
        return client;
    }

    /**
     * Sets the client.
     *
     * @param client
     *         the new client
     */
    public void setClient( List< DocumentDTO > client ) {
        this.client = client;
    }

    /**
     * Gets the server.
     *
     * @return the server
     */
    public List< DocumentDTO > getServer() {
        return server;
    }

    /**
     * Sets the server.
     *
     * @param server
     *         the new server
     */
    public void setServer( List< DocumentDTO > server ) {
        this.server = server;
    }

}
