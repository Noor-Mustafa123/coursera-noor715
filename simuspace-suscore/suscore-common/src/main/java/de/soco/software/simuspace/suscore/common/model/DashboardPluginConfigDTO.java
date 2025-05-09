package de.soco.software.simuspace.suscore.common.model;

import java.util.List;
import java.util.Map;

/**
 * The Class DashboardPluginConfigDTO.
 */
@Deprecated( since = "soco/2.3.1/release", forRemoval = true )
public class DashboardPluginConfigDTO {

    /**
     * The name.
     */
    private String name;

    /**
     * The Allow multiple.
     */
    private Boolean allowMultiple;

    /**
     * The commands.
     */
    private List< Map< String, String > > commands;

    /**
     * The fields.
     */
    private List< Map< String, Object > > fields;

    /**
     * The properties.
     */
    private List< Map< String, Object > > properties;

    /**
     * Gets allow multiple.
     *
     * @return the allow multiple
     */
    public Boolean getAllowMultiple() {
        return allowMultiple;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets the commands.
     *
     * @return the commands
     */
    public List< Map< String, String > > getCommands() {
        return commands;
    }

    /**
     * Sets the commands.
     *
     * @param commands
     *         the commands
     */
    public void setCommands( List< Map< String, String > > commands ) {
        this.commands = commands;
    }

    /**
     * Gets the fields.
     *
     * @return the fields
     */
    public List< Map< String, Object > > getFields() {
        return fields;
    }

    /**
     * Sets the fields.
     *
     * @param fields
     *         the fields
     */
    public void setFields( List< Map< String, Object > > fields ) {
        this.fields = fields;
    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public List< Map< String, Object > > getProperties() {
        return properties;
    }

    /**
     * Sets the properties.
     *
     * @param properties
     *         the properties
     */
    public void setProperties( List< Map< String, Object > > properties ) {
        this.properties = properties;
    }

    /**
     * Sets allow multiple.
     *
     * @param allowMultiple
     *         the allow multiple
     */
    public void setAllowMultiple( Boolean allowMultiple ) {
        this.allowMultiple = allowMultiple;
    }

}
