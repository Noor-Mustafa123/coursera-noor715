package de.soco.software.simuspace.workflow.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.DataTransferObject;

/**
 * Mapping Class for status Configration js file
 *
 * @author Nosheen.Sharif
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class StatusConfig extends DataTransferObject {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The status configuration reference for list of status details .
     */
    private List< StatusDetailDTO > statusConfiguration;

    /**
     * The applicable of status changes.
     */
    private List< StatusObjectDetail > applicable;

    /**
     * Gets the applicable.
     *
     * @return the applicable
     */
    public List< StatusObjectDetail > getApplicable() {
        return applicable;
    }

    /**
     * Gets the status configuration.
     *
     * @return the status configuration
     */
    public List< StatusDetailDTO > getStatusConfiguration() {
        return statusConfiguration;
    }

    /**
     * Sets the applicable.
     *
     * @param applicable
     *         the new applicable
     */
    public void setApplicable( List< StatusObjectDetail > applicable ) {
        this.applicable = applicable;
    }

    /**
     * Sets the status configuration.
     *
     * @param statusConfiguration
     *         the new status configuration
     */
    public void setStatusConfiguration( List< StatusDetailDTO > statusConfiguration ) {
        this.statusConfiguration = statusConfiguration;
    }

}
