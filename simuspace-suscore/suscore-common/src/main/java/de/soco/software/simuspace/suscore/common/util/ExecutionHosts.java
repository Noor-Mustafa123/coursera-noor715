package de.soco.software.simuspace.suscore.common.util;

import java.io.Serializable;
import java.util.UUID;

/**
 * The Class ExecutionHosts.
 */
public class ExecutionHosts implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -6924836496982046865L;

    /**
     * The id.
     */
    private UUID id;

    /**
     * The name.
     */
    private String name;

    /**
     * The address.
     */
    private String address;

    /**
     * The engine dir.
     */
    private String engineDir;

    /**
     * The enabled.
     */
    private Boolean enabled;

    /**
     * The core pool size.
     */
    private String corePoolSize;

    /**
     * The core max size.
     */
    private String coreMaxSize;

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
     * Gets the address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address.
     *
     * @param address
     *         the new address
     */
    public void setAddress( String address ) {
        this.address = address;
    }

    /**
     * Gets the engine dir.
     *
     * @return the engine dir
     */
    public String getEngineDir() {
        return engineDir;
    }

    /**
     * Sets the engine dir.
     *
     * @param engineDir
     *         the new engine dir
     */
    public void setEngineDir( String engineDir ) {
        this.engineDir = engineDir;
    }

    /**
     * Gets the enabled.
     *
     * @return the enabled
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled.
     *
     * @param enabled
     *         the new enabled
     */
    public void setEnabled( Boolean enabled ) {
        this.enabled = enabled;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( UUID id ) {
        this.id = id;
    }

    /**
     * Gets the core pool size.
     *
     * @return the core pool size
     */
    public String getCorePoolSize() {
        return corePoolSize;
    }

    /**
     * Sets the core pool size.
     *
     * @param corePoolSize
     *         the new core pool size
     */
    public void setCorePoolSize( String corePoolSize ) {
        this.corePoolSize = corePoolSize;
    }

    /**
     * Gets the core max size.
     *
     * @return the core max size
     */
    public String getCoreMaxSize() {
        return coreMaxSize;
    }

    /**
     * Sets the core max size.
     *
     * @param coreMaxSize
     *         the new core max size
     */
    public void setCoreMaxSize( String coreMaxSize ) {
        this.coreMaxSize = coreMaxSize;
    }

}
