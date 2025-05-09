package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;

/**
 * The Class VerificationDTO.
 */
public class VerificationDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 739179841231857660L;

    /**
     * The user name.
     */
    private String userName;

    /**
     * The profile photo url.
     */
    private String profilePhotoUrl;

    /**
     * The Directory type.
     */
    private String directoryType;

    /**
     * Instantiates a new verification DTO.
     *
     * @param userName
     *         the user name
     * @param profilePhotoUrl
     *         the profile photo url
     */
    public VerificationDTO( String userName, String profilePhotoUrl, String directoryType ) {
        this.userName = userName;
        this.profilePhotoUrl = profilePhotoUrl;
        this.directoryType = directoryType;
    }

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     *
     * @param userName
     *         the new user name
     */
    public void setUserName( String userName ) {
        this.userName = userName;
    }

    /**
     * Gets the profile photo url.
     *
     * @return the profile photo url
     */
    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    /**
     * Sets the profile photo url.
     *
     * @param profilePhotoUrl
     *         the new profile photo url
     */
    public void setProfilePhotoUrl( String profilePhotoUrl ) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    /**
     * Gets directory type.
     *
     * @return the directory type
     */
    public String getDirectoryType() {
        return directoryType;
    }

    /**
     * Sets directory type.
     *
     * @param directoryType
     *         the directory type
     */
    public void setDirectoryType( String directoryType ) {
        this.directoryType = directoryType;
    }

}
