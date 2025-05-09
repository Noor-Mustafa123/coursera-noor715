/*
 *
 */

package de.soco.software.simuspace.workflow.model.impl;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.util.JsonUtils;

/**
 * This class contains an object of a file.
 */

@JsonIgnoreProperties( ignoreUnknown = true )
public class EngineFile extends TemplateFile {

    /**
     * The agent.
     */
    private String agent;

    /**
     * The doc id.
     */
    private int docId;

    /**
     * The location.
     */
    private int location;

    /**
     * The path.
     */
    private String path;

    /**
     * The type.
     */
    private String type;

    /**
     * The items.
     */
    private List< String > items;

    /**
     * Instantiates a new file.
     */
    public EngineFile() {
        super();
    }

    /**
     * Instantiates a new file.
     *
     * @param path
     *         the path
     */
    public EngineFile( String path ) {
        this();
        this.path = path;
    }

    /**
     * Instantiates a new file.
     *
     * @param agent
     *         the agent
     * @param type
     *         the type
     * @param path
     *         the path
     * @param docId
     *         the doc id
     * @param location
     *         the location
     */
    public EngineFile( String agent, String type, String path, int docId, int location ) {
        this();
        this.agent = agent;
        this.type = type;
        this.path = path;
        this.docId = docId;
        this.location = location;
    }

    /**
     * Gets the agent.
     *
     * @return the agent
     */
    public String getAgent() {
        return agent;
    }

    /**
     * Gets the doc id.
     *
     * @return the doc id
     */
    public int getDocId() {
        return docId;
    }

    /**
     * Gets the items.
     *
     * @return the items
     */
    public List< String > getItems() {
        return items;
    }

    /**
     * Gets the location.
     *
     * @return the location
     */
    public int getLocation() {
        return location;
    }

    public String getPath() {
        String pathStr;
        if ( path == null && ( null != items && !items.isEmpty() ) ) {
            pathStr = items.get( 0 );
        } else if ( path == null && ( null != getFile() || !getFile().isEmpty() ) ) {
            pathStr = getFile() != null && !getFile().isEmpty() ? JsonUtils.toJson( new TemplateFile( getFile(), getVariables() ) ) : null;
        } else {
            pathStr = path;
        }
        return pathStr;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the agent.
     *
     * @param agent
     *         the new agent
     */
    public void setAgent( String agent ) {
        this.agent = agent;
    }

    /**
     * Sets the doc id.
     *
     * @param docId
     *         the new doc id
     */
    public void setDocId( int docId ) {
        this.docId = docId;
    }

    /**
     * Sets the items.
     *
     * @param items
     *         the new items
     */
    public void setItems( List< String > items ) {
        this.items = items;
    }

    /**
     * Sets the location.
     *
     * @param location
     *         the new location
     */
    public void setLocation( int location ) {
        this.location = location;
    }

    /**
     * Sets the path.
     *
     * @param path
     *         the new path
     */
    public void setPath( String path ) {
        this.path = path;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "File [agent='" + agent + "', type='" + type + "', path='" + path + "', docId='" + docId + "', location='" + location + "']";
    }

}
