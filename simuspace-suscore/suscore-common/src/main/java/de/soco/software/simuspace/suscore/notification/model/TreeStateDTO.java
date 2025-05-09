package de.soco.software.simuspace.suscore.notification.model;

import de.soco.software.simuspace.suscore.common.model.UserDTO;

/**
 * The Class TreeStateDTO.
 */
public class TreeStateDTO {

    /**
     * The user DTO.
     */
    private UserDTO modifiedBy;

    private TreeChangeDTO change;

    /**
     * Instantiates a new tree state DTO.
     */
    public TreeStateDTO() {

    }

    public UserDTO getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy( UserDTO modifiedBy ) {
        this.modifiedBy = modifiedBy;
    }

    public TreeChangeDTO getChange() {
        return change;
    }

    public void setChange( TreeChangeDTO change ) {
        this.change = change;
    }

    public TreeStateDTO( UserDTO modifiedBy, TreeChangeDTO change ) {
        super();
        this.modifiedBy = modifiedBy;
        this.change = change;
    }

}
