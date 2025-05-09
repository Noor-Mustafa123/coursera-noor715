package de.soco.software.simuspace.suscore.common.formitem.impl;

import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;

/**
 * The Class ImageFormItem.
 *
 * @author Ali Haider
 */
public class ImageFormItem extends UIFormItem {

    /**
     * The agents.
     */
    private AgentsDTO agents;

    /**
     * Instantiates a new Image form item.
     */
    ImageFormItem() {

    }

    /**
     * Sets agents.
     *
     * @param acceptedFiles
     *         the accepted files
     * @param maxFiles
     *         the max files
     */
    public void setAgents( String acceptedFiles, Integer maxFiles ) {
        this.setAgents( new AgentsDTO( acceptedFiles, maxFiles ) );
    }

    /**
     * Gets the agents.
     *
     * @return the agents
     */
    public AgentsDTO getAgents() {
        return agents;
    }

    /**
     * Sets the agents.
     *
     * @param agents
     *         the new agents
     */
    public void setAgents( AgentsDTO agents ) {
        this.agents = agents;
    }

    /**
     * {@inheritDoc}
     *
     * @param obj
     *         the obj
     *
     * @return the boolean
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        return getClass() == obj.getClass();
    }

    /**
     * {@inheritDoc}
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * The Class AgentsDTO.
     */
    public class AgentsDTO {

        /**
         * The client.
         */
        private boolean client;

        /**
         * The server.
         */
        private boolean server;

        /**
         * The browser.
         */
        private Browser browser;

        /**
         * The table.
         */
        private boolean table;

        /**
         * Instantiates a new agents DTO.
         *
         * @param acceptedFiles
         *         the accepted files
         * @param maxFiles
         *         the max files
         */
        public AgentsDTO( String acceptedFiles, Integer maxFiles ) {
            this.browser = new Browser( acceptedFiles, maxFiles );
            this.client = false;
            this.server = false;
            this.table = false;

        }

        /**
         * Checks if is client.
         *
         * @return true, if is client
         */
        public boolean isClient() {
            return client;
        }

        /**
         * Sets the client.
         *
         * @param client
         *         the new client
         */
        public void setClient( boolean client ) {
            this.client = client;
        }

        /**
         * Checks if is server.
         *
         * @return true, if is server
         */
        public boolean isServer() {
            return server;
        }

        /**
         * Sets the server.
         *
         * @param server
         *         the new server
         */
        public void setServer( boolean server ) {
            this.server = server;
        }

        /**
         * Gets the browser.
         *
         * @return the browser
         */
        public Browser getBrowser() {
            return browser;
        }

        /**
         * Sets the browser.
         *
         * @param browser
         *         the new browser
         */
        public void setBrowser( Browser browser ) {
            this.browser = browser;
        }

        /**
         * Checks if is table.
         *
         * @return true, if is table
         */
        public boolean isTable() {
            return table;
        }

        /**
         * Sets the table.
         *
         * @param table
         *         the new table
         */
        public void setTable( boolean table ) {
            this.table = table;
        }

    }

    /**
     * The Class Browser.
     */
    private class Browser {

        /**
         * The accepted files.
         */
        private String acceptedFiles;

        /**
         * The max files.
         */
        private Integer maxFiles;

        /**
         * Instantiates a new browser.
         *
         * @param acceptedFiles
         *         the accepted files
         * @param maxFiles
         *         the max files
         */
        public Browser( String acceptedFiles, Integer maxFiles ) {
            super();
            this.acceptedFiles = acceptedFiles;
            this.maxFiles = maxFiles;
        }

        /**
         * Gets the accepted files.
         *
         * @return the accepted files
         */
        public String getAcceptedFiles() {
            return acceptedFiles;
        }

        /**
         * Sets the accepted files.
         *
         * @param acceptedFiles
         *         the new accepted files
         */
        public void setAcceptedFiles( String acceptedFiles ) {
            this.acceptedFiles = acceptedFiles;
        }

        /**
         * Gets the max files.
         *
         * @return the max files
         */
        public Integer getMaxFiles() {
            return maxFiles;
        }

        /**
         * Sets the max files.
         *
         * @param maxFiles
         *         the new max files
         */
        public void setMaxFiles( Integer maxFiles ) {
            this.maxFiles = maxFiles;
        }

    }

}
