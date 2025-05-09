package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class EncryptionDecriptionDTO.
 *
 * @author noman arshad, Ali Haider
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class EncryptionDecryptionDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -239387214494803817L;

    /**
     * The id.
     */
    private String id;

    /**
     * The method.
     */
    private String method;

    /**
     * The active.
     */
    private boolean active;

    /**
     * The salt.
     */
    private String salt;

    /**
     * Instantiates a new encryption decryption DTO.
     */
    public EncryptionDecryptionDTO() {
    }

    /**
     * Instantiates a new encryption decryption DTO.
     *
     * @param method
     *         the method
     * @param active
     *         the active
     * @param salt
     *         the salt
     */
    public EncryptionDecryptionDTO( String id, String method, boolean active, String salt ) {
        super();
        this.id = id;
        this.method = method;
        this.active = active;
        this.salt = salt;
    }

    /**
     * Gets the method.
     *
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the method.
     *
     * @param method
     *         the new method
     */
    public void setMethod( String method ) {
        this.method = method;
    }

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
     * Gets the salt.
     *
     * @return the salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Sets the salt.
     *
     * @param salt
     *         the new salt
     */
    public void setSalt( String salt ) {
        this.salt = salt;
    }

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

}
