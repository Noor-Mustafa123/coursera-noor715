package de.soco.software.simuspace.suscore.data.common.model;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UserDTO;

/**
 * The Class AuditTrailDetailDTO.
 *
 * @author Noman Arshad
 */
public class AuditTrailDetailDTO {

    /**
     * The name.
     */
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000032x4", orderNum = 1, width = 0 )
    private String name;

    /**
     * The created by.
     */
    @UIColumn( data = "createdBy.userUid", name = "createdBy", filter = "text", url = "system/user/{createdBy.id}", renderer = "link", title = "3000064x4", tooltip = "{createdBy.userName}", orderNum = 4 )
    private UserDTO createdBy;

    /**
     * The created on.
     */
    @UIColumn( data = "createdOn", name = "createdOn", filter = "dateRange", renderer = "date", title = "3000008x4", orderNum = 3 )
    private String createdOn;

    /**
     * The description.
     */
    @UIColumn( data = "description", name = "description", filter = "text", renderer = "text", title = "3000011x4", orderNum = 2 )
    private String description;

    public AuditTrailDetailDTO() {
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
     * Gets the created by.
     *
     * @return the created by
     */
    public UserDTO getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the created by.
     *
     * @param createdBy
     *         the new created by
     */
    public void setCreatedBy( UserDTO createdBy ) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the created on.
     *
     * @return the created on
     */
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the created on.
     *
     * @param createdOn
     *         the new created on
     */
    public void setCreatedOn( String createdOn ) {
        this.createdOn = createdOn;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description
     *         the new description
     */
    public void setDescription( String description ) {
        this.description = description;
    }

}
