package de.soco.software.simuspace.suscore.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class EncryptionConfiguration.
 *
 * @author Ali Haider
 */

@JsonIgnoreProperties( ignoreUnknown = true )
public class EncryptionConfiguration {

    /**
     * The active.
     */
    private boolean active;

    /**
     * The available methods.
     */
    private List< EncryptionDecryptionDTO > availableMethods;

    /**
     * Checks if is active.
     *
     * @return true, if is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active.
     *
     * @param active
     *         the new active
     */
    public void setActive( boolean active ) {
        this.active = active;
    }

    /**
     * Gets the available methods.
     *
     * @return the available methods
     */
    public List< EncryptionDecryptionDTO > getAvailableMethods() {
        return availableMethods;
    }

    /**
     * Sets the available methods.
     *
     * @param availableMethods
     *         the new available methods
     */
    public void setAvailableMethods( List< EncryptionDecryptionDTO > availableMethods ) {
        this.availableMethods = availableMethods;
    }

}
