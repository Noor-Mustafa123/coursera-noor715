package de.soco.software.simuspace.suscore.lifecycle.model;

import java.util.List;

import de.soco.software.simuspace.suscore.data.common.model.StatusConfigDTO;
import de.soco.software.simuspace.suscore.data.model.StatusApplicableDTO;

/**
 * This class is responsible of mapping Lifecycle object.
 *
 * @author Nosheen.Sharif
 */
public class LifeCycleDTO {

    /**
     * The id.
     */
    private String id;

    /**
     * The name.
     */
    private String name;

    /**
     * The status configuration reference for list of status details .
     */
    private List< StatusConfigDTO > statusConfiguration;

    /**
     * The applicable of status changes.
     */
    private StatusApplicableDTO applicable;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( String id ) {
        this.id = id;
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
     * Gets the status configuration.
     *
     * @return the status configuration
     */
    public List< StatusConfigDTO > getStatusConfiguration() {
        return statusConfiguration;
    }

    /**
     * Sets the status configuration.
     *
     * @param statusConfiguration
     *         the new status configuration
     */
    public void setStatusConfiguration( List< StatusConfigDTO > statusConfiguration ) {
        this.statusConfiguration = statusConfiguration;
    }

    /**
     * Gets the applicable.
     *
     * @return the applicable
     */
    public StatusApplicableDTO getApplicable() {
        return applicable;
    }

    /**
     * Sets the applicable.
     *
     * @param applicable
     *         the new applicable
     */
    public void setApplicable( StatusApplicableDTO applicable ) {
        this.applicable = applicable;
    }

}
