/*
 *
 */

package de.soco.software.simuspace.workflow.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.DataTransferObject;
import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.workflow.constant.ConstantsWorkFlowProps;
import de.soco.software.simuspace.workflow.model.Category;
import de.soco.software.simuspace.workflow.model.impl.ProgressBar;

/**
 * Workflow Data Transfer Object for Fornt End representation of workflow.
 *
 * @author Nosheen.Sharif
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class WorkflowDTO extends DataTransferObject implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The Constant NOT_REQUIRED_PROP.
     */
    public static final String[] NOT_REQUIRED_PROP = { "definition" };

    /**
     * The Constant WORKFLOW_NAME.
     */
    public static final String NAME = "name";

    /**
     * The actions (like status change)
     */
    private List< Action > actions;

    /**
     * The categories list attached to workflow.
     */
    private List< Category > categories;

    /**
     * user who creates the workflow. referred to sus 1.1 user
     */
    private UserDTO createdBy;

    /**
     * creation date of workflow.
     */
    private Date createdOn;

    /**
     * map consist of key like elements,connection and positions workflow model field to store in db .
     */
    private Map< String, Object > definition;

    /**
     * The description of workflow provided by User.
     */
    private String description;

    /**
     * workflow Id.
     */
    private String id;

    /**
     * Whether this is an intractive workflow
     */
    private boolean interactive;

    /**
     * The job counts .
     */
    private ProgressBar jobs;

    /**
     * User responsible for creating new version of workflow.(versionable)
     */
    private UserDTO updatedBy;

    /**
     * Modification date of worklfow.
     */
    private Date modifiedOn;

    /**
     * Name of workflow
     */
    private String name;

    /**
     * workflow designer token.
     */
    private String token;

    /**
     * The version Object consist of version Id and version label of workflow.
     */
    private VersionDTO version;

    /**
     * The versions list for a workflow
     */
    private List< WorkflowDTO > versions;

    /**
     * Whether this is a favorite workflow
     */
    private boolean favorite;

    /**
     * Flag to indicate either the workflow can execute (as job) or not.
     */
    private boolean executable;

    /**
     * The curent sim user id from 1.1.
     */
    private UUID curentSimUserId;

    /**
     * The updated on.
     */
    private String config;

    /**
     * The object type id.
     */
    private UUID typeId;

    /**
     * The type.
     */
    private String type;

    /**
     * The updated on.
     */
    private StatusDTO lifeCycleStatus;

    /**
     * The user selection id.
     */
    private String userSelectionId;

    /**
     * The group selection id.
     */
    private String groupSelectionId;

    /**
     * The run on location.
     */
    private List< LocationDTO > runOnLocation;

    /**
     * The locations.
     */
    private String locations;

    /**
     * The custom flags.
     */
    private List< String > customFlags;

    /**
     * The comments.
     */
    private String comments;

    /**
     * Instantiates a new workflow dto.
     */
    public WorkflowDTO() {
        super();
    }

    /**
     * Instantiates a new workflow dto.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param version
     *         the version
     * @param interactive
     *         the interactive
     * @param description
     *         the description
     * @param status
     *         the status
     * @param createdBy
     *         the created by
     * @param definition
     *         the definition
     * @param categoryList
     *         the category list
     */
    public WorkflowDTO( String id, String name, VersionDTO version, boolean interactive, String description, Status status,
            UserDTO createdBy, Map< String, Object > definition, List< Category > categoryList ) {
        this();
        this.id = id;
        this.name = name;
        this.version = version;
        this.interactive = interactive;
        this.createdOn = new Date();
        this.createdBy = createdBy;
        this.description = description;
        this.definition = definition;
        this.categories = categoryList;

    }

    /**
     * Instantiates a new workflow DTO.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param description
     *         the description
     * @param userId
     *         the user id
     */
    public WorkflowDTO( UUID id, String name, String description, String userId ) {
        this.id = id.toString();
        this.name = name;
        this.description = description;
        this.createdBy = new UserDTO( userId );
    }

    /**
     * Gets the actions.
     *
     * @return the actions
     */
    public List< Action > getActions() {
        return actions;
    }

    /**
     * Gets the categories.
     *
     * @return the categories
     */
    public List< Category > getCategories() {
        return categories;
    }

    /**
     * Gets the config.
     *
     * @return the config
     */
    public String getConfig() {
        return config;
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
     * Gets the created on.
     *
     * @return the created on
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * Gets the curent sim user id.
     *
     * @return the curent sim user id
     */
    public UUID getCurentSimUserId() {
        return curentSimUserId;
    }

    /**
     * Gets the definition.
     *
     * @return the definition
     */
    public Map< String, Object > getDefinition() {
        return definition;
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
     * Gets the group selection id.
     *
     * @return the group selection id
     */
    public String getGroupSelectionId() {
        return groupSelectionId;
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
     * Gets the jobs.
     *
     * @return the jobs
     */
    public ProgressBar getJobs() {
        return jobs;
    }

    /**
     * @return the lifeCycleStatus
     */
    public StatusDTO getLifeCycleStatus() {
        return lifeCycleStatus;
    }

    /**
     * Gets the locations.
     *
     * @return the locations
     */
    public String getLocations() {
        return locations;
    }

    /**
     * Gets the modified on.
     *
     * @return the modified on
     */
    public Date getModifiedOn() {
        return modifiedOn;
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
     * Gets the run on location.
     *
     * @return the run on location
     */
    public List< LocationDTO > getRunOnLocation() {
        return runOnLocation;
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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the type id.
     *
     * @return the type id
     */
    public UUID getTypeId() {
        return typeId;
    }

    /**
     * Gets the updated by.
     *
     * @return the updated by
     */
    public UserDTO getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Gets the user selection id.
     *
     * @return the user selection id
     */
    public String getUserSelectionId() {
        return userSelectionId;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public VersionDTO getVersion() {
        return version;
    }

    /**
     * Gets the versions.
     *
     * @return the versions
     */
    public List< WorkflowDTO > getVersions() {
        return versions;
    }

    /**
     * Checks if is executable.
     *
     * @return true, if is executable
     */
    public boolean isExecutable() {
        return executable;
    }

    /**
     * * Checks if is favorite.
     *
     * @return true, if is favorite
     */
    public boolean isFavorite() {
        return favorite;
    }

    /**
     * Checks if is interactive.
     *
     * @return true, if is interactive
     */
    public boolean isInteractive() {
        return interactive;
    }

    /**
     * Sets the actions.
     *
     * @param actions
     *         the new actions
     */
    public void setActions( List< Action > actions ) {
        this.actions = actions;
    }

    /**
     * Sets the categories.
     *
     * @param categories
     *         the new categories
     */
    public void setCategories( List< Category > categories ) {
        this.categories = categories;
    }

    /**
     * Sets the config.
     *
     * @param config
     *         the new config
     */
    public void setConfig( String config ) {
        this.config = config;
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
     * Sets the created on.
     *
     * @param createdOn
     *         the new created on
     */
    public void setCreatedOn( Date createdOn ) {
        this.createdOn = createdOn;
    }

    /**
     * Sets the curent sim user id.
     *
     * @param curentSimUserId
     *         the new curent sim user id
     */
    public void setCurentSimUserId( UUID curentSimUserId ) {
        this.curentSimUserId = curentSimUserId;
    }

    /**
     * Sets the definition.
     *
     * @param definition
     *         the definition
     */
    public void setDefinition( Map< String, Object > definition ) {
        this.definition = definition;
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
     * Sets the executable.
     *
     * @param executable
     *         the new executable
     */
    public void setExecutable( boolean executable ) {
        this.executable = executable;
    }

    /**
     * Sets the isFavorite.
     *
     * @param favorite
     *         the favorite
     */
    public void setFavorite( boolean favorite ) {
        this.favorite = favorite;
    }

    /**
     * Sets the group selection id.
     *
     * @param groupSelectionId
     *         the new group selection id
     */
    public void setGroupSelectionId( String groupSelectionId ) {
        this.groupSelectionId = groupSelectionId;
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
     * Sets the interactive.
     *
     * @param interactive
     *         the new interactive
     */
    public void setInteractive( boolean interactive ) {
        this.interactive = interactive;
    }

    /**
     * Sets the jobs.
     *
     * @param jobs
     *         the new jobs
     */
    public void setJobs( ProgressBar jobs ) {
        this.jobs = jobs;
    }

    /**
     * @param lifeCycleStatus
     *         the lifeCycleStatus to set
     */
    public void setLifeCycleStatus( StatusDTO lifeCycleStatus ) {
        this.lifeCycleStatus = lifeCycleStatus;
    }

    /**
     * Sets the locations.
     *
     * @param locations
     *         the new locations
     */
    public void setLocations( String locations ) {
        this.locations = locations;
    }

    /**
     * Sets the modified on.
     *
     * @param modifiedOn
     *         the new modified on
     */
    public void setModifiedOn( Date modifiedOn ) {
        this.modifiedOn = modifiedOn;
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
     * Sets the run on location.
     *
     * @param runOnLocation
     *         the new run on location
     */
    public void setRunOnLocation( List< LocationDTO > runOnLocation ) {
        this.runOnLocation = runOnLocation;
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
     * @param type
     *         the type to set
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * Sets the type id.
     *
     * @param typeId
     *         the new type id
     */
    public void setTypeId( UUID typeId ) {
        this.typeId = typeId;
    }

    /**
     * Sets the updated by.
     *
     * @param updatedBy
     *         the new updated by
     */
    public void setUpdatedBy( UserDTO updatedBy ) {
        this.updatedBy = updatedBy;
    }

    /**
     * Sets the user selection id.
     *
     * @param userSelectionId
     *         the new user selection id
     */
    public void setUserSelectionId( String userSelectionId ) {
        this.userSelectionId = userSelectionId;
    }

    /**
     * Sets the version.
     *
     * @param version
     *         the new version
     */
    public void setVersion( VersionDTO version ) {
        this.version = version;
    }

    /**
     * Sets the versions.
     *
     * @param versions
     *         the new versions
     */
    public void setVersions( List< WorkflowDTO > versions ) {
        this.versions = versions;
    }

    /**
     * Gets the custom flags.
     *
     * @return the custom flags
     */
    public List< String > getCustomFlags() {
        return customFlags;
    }

    /**
     * Sets the custom flags.
     *
     * @param customFlags
     *         the new custom flags
     */
    public void setCustomFlags( List< String > customFlags ) {
        this.customFlags = customFlags;
    }

    /**
     * Basic validation of workflow data transfer object.
     *
     * @return Error notifications list contains messages for user
     */
    public boolean validate() {
        final Notification notification = new Notification();
        if ( StringUtils.isNullOrEmpty( name ) ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.WORKFLOW_NAME_SHOULD_NOT_EMPTY ) ) );
        } else {
            notification.addNotification( StringUtils.validateFieldAndLength( getName(),
                    ConstantsWorkFlowProps.NAME + ConstantsString.STANDARD_SEPARATOR + getName(), ConstantsLength.STANDARD_NAME_LENGTH,
                    false, true ) );
        }
        if ( StringUtils.isNotNullOrEmpty( description ) ) {
            notification.addNotification( StringUtils.validateFieldAndLength( getDescription(), ConstantsWorkFlowProps.DESCRIPTION,
                    ConstantsLength.STANDARD_DESCRIPTION_LENGTH, true, false ) );
        }
        if ( StringUtils.isNotNullOrEmpty( comments ) ) {
            notification.addNotification( StringUtils.validateFieldAndLength( getComments(), ConstantsWorkFlowProps.COMMENTS,
                    ConstantsLength.STANDARD_COMMENT_LENGTH, true, false ) );
        }
        if ( !notification.getErrors().isEmpty() ) {

            throw new SusException( new Exception( notification.getErrors().toString() ), getClass() );
        }
        return true;
    }

    /**
     * Gets the comments.
     *
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the comments.
     *
     * @param comments
     *         the new comments
     */
    public void setComments( String comments ) {
        this.comments = comments;
    }

}
