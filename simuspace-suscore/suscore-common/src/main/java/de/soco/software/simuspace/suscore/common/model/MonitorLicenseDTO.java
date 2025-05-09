package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class MonitorLicenseDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class MonitorLicenseDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 4775171936176605507L;

    /**
     * The id.
     */
    @UIFormField( name = "id", type = "hidden", title = "3000021x4", orderNum = 1 )
    @UIColumn( data = "id", name = "id", filter = "text", renderer = "hidden", title = "3000021x4", orderNum = 1 )
    private String id;

    /**
     * The token.
     */
    @UIFormField( name = "token", title = "3000118x4", orderNum = 2 )
    @UIColumn( data = "token", name = "token", filter = "text", renderer = "text", title = "3000118x4", orderNum = 2 )
    private String token;

    /**
     * The assigned to user id.
     */
    @UIFormField( name = "assignedToUserId", title = "3000021x4", orderNum = 3, type = "hidden")
    @UIColumn( data = "assignedToUserId", name = "assignedToUserId", filter = "text", renderer = "hidden", title = "3000021x4", orderNum = 3 )
    private String assignedToUserId;

    /**
     * The assigned to user name.
     */
    @UIFormField( name = "assignedToUserName", title = "9900005x4", orderNum = 4 )
    @UIColumn( data = "assignedToUserName", name = "assignedToUserName", filter = "text", renderer = "text", title = "9900005x4", orderNum = 4 )
    private String assignedToUserName;

    /**
     * The assigned by user id.
     */
    @UIFormField( name = "assignedByUserId", title = "3000021x4", orderNum = 5, type = "hidden" )
    @UIColumn( data = "assignedByUserId", name = "assignedByUserId", filter = "text", renderer = "hidden", title = "3000021x4", orderNum = 5 )
    private String assignedByUserId;

    /**
     * The assigned by user name.
     */
    @UIFormField( name = "assignedByUserName", title = "9900006x4", orderNum = 6 )
    @UIColumn( data = "assignedByUserName", name = "assignedByUserName", filter = "text", renderer = "text", title = "9900006x4", orderNum = 6 )
    private String assignedByUserName;

    /**
     * The description.
     */
    @UIFormField( name = "description", title = "3000011x4", orderNum = 7 )
    @UIColumn( data = "description", name = "description", filter = "text", renderer = "text", title = "3000011x4", orderNum = 7 )
    private String description;

    /**
     * The created on.
     */
    @UIFormField( name = "createdOn", title = "3000008x4", orderNum = 8 )
    @UIColumn( data = "createdOn", name = "createdOn", filter = "text", renderer = "text", title = "3000008x4", orderNum = 8 )
    private String createdOn;

    /**
     * The license type.
     */
    @UIFormField( name = "licenseType", title = "3000027x4", orderNum = 9 )
    @UIColumn( data = "licenseType", name = "licenseType", filter = "text", renderer = "text", title = "3000027x4", orderNum = 9 )
    private String licenseType;

    /**
     * The license module.
     */
    @UIFormField( name = "licenseModule", title = "3000030x4", orderNum = 10 )
    @UIColumn( data = "licenseModule", name = "licenseModule", filter = "text", renderer = "text", title = "3000030x4", orderNum = 10 )
    private String licenseModule;

    /**
     * The is active.
     */
    @UIFormField( name = "isActive", title = "9900007x4", orderNum = 11 )
    @UIColumn( data = "isActive", name = "isActive", filter = "text", renderer = "text", title = "9900007x4", orderNum = 11 )
    private String isActive;

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
     * Gets the token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token.
     *
     * @param token
     *         the new token
     */
    public void setToken( String token ) {
        this.token = token;
    }

    /**
     * Gets the assigned to user id.
     *
     * @return the assigned to user id
     */
    public String getAssignedToUserId() {
        return assignedToUserId;
    }

    /**
     * Sets the assigned to user id.
     *
     * @param assignedToUserId
     *         the new assigned to user id
     */
    public void setAssignedToUserId( String assignedToUserId ) {
        this.assignedToUserId = assignedToUserId;
    }

    /**
     * Gets the assigned by user id.
     *
     * @return the assigned by user id
     */
    public String getAssignedByUserId() {
        return assignedByUserId;
    }

    /**
     * Sets the assigned by user id.
     *
     * @param assignedByUserId
     *         the new assigned by user id
     */
    public void setAssignedByUserId( String assignedByUserId ) {
        this.assignedByUserId = assignedByUserId;
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
     * Gets the license type.
     *
     * @return the license type
     */
    public String getLicenseType() {
        return licenseType;
    }

    /**
     * Sets the license type.
     *
     * @param licenseType
     *         the new license type
     */
    public void setLicenseType( String licenseType ) {
        this.licenseType = licenseType;
    }

    /**
     * Gets the license module.
     *
     * @return the license module
     */
    public String getLicenseModule() {
        return licenseModule;
    }

    /**
     * Sets the license module.
     *
     * @param licenseModule
     *         the new license module
     */
    public void setLicenseModule( String licenseModule ) {
        this.licenseModule = licenseModule;
    }

    /**
     * Gets the checks if is active.
     *
     * @return the checks if is active
     */
    public String getIsActive() {
        return isActive;
    }

    /**
     * Sets the checks if is active.
     *
     * @param isActive
     *         the new checks if is active
     */
    public void setIsActive( String isActive ) {
        this.isActive = isActive;
    }

    /**
     * Gets the assigned to user name.
     *
     * @return the assigned to user name
     */
    public String getAssignedToUserName() {
        return assignedToUserName;
    }

    /**
     * Sets the assigned to user name.
     *
     * @param assignedToUserName
     *         the new assigned to user name
     */
    public void setAssignedToUserName( String assignedToUserName ) {
        this.assignedToUserName = assignedToUserName;
    }

    /**
     * Gets the assigned by user name.
     *
     * @return the assigned by user name
     */
    public String getAssignedByUserName() {
        return assignedByUserName;
    }

    /**
     * Sets the assigned by user name.
     *
     * @param assignedByUserName
     *         the new assigned by user name
     */
    public void setAssignedByUserName( String assignedByUserName ) {
        this.assignedByUserName = assignedByUserName;
    }

}
