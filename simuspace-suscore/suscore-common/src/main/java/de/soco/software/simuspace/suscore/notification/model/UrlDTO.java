package de.soco.software.simuspace.suscore.notification.model;

import de.soco.software.simuspace.suscore.common.model.UserDTO;

/**
 * The Class UrlDTO is used to hold the user actions for managing ui states.
 *
 * @author Zeeshan jamal
 */
public class UrlDTO {

    /**
     * The url.
     */
    private String url;

    private String action;

    /**
     * The user DTO.
     */
    private UserDTO modifiedBy;

    public String getUrl() {
        return url;
    }

    public void setUrl( String url ) {
        this.url = url;
    }

    public UserDTO getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy( UserDTO modifiedBy ) {
        this.modifiedBy = modifiedBy;
    }

    public UrlDTO() {
        super();
    }

    public String getAction() {
        return action;
    }

    public void setAction( String action ) {
        this.action = action;
    }

    public UrlDTO( String url, String action, UserDTO modifiedBy ) {
        super();
        this.url = url;
        this.action = action;
        this.modifiedBy = modifiedBy;
    }

}
