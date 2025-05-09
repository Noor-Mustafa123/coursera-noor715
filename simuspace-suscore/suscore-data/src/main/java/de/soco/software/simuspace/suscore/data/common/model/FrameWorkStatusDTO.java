package de.soco.software.simuspace.suscore.data.common.model;

/**
 * The Class FrameWorkStatusDTO.
 */
public class FrameWorkStatusDTO {

    /**
     * The is up.
     */
    private boolean isUp;

    /**
     * The version.
     */
    private String version;

    /**
     * Checks if is up.
     *
     * @return true, if is up
     */
    public boolean isUp() {
        return isUp;
    }

    /**
     * Sets the up.
     *
     * @param isUp
     *         the new up
     */
    public void setUp( boolean isUp ) {
        this.isUp = isUp;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *         the new version
     */
    public void setVersion( String version ) {
        this.version = version;
    }

}
