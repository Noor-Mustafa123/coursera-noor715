package de.soco.software.simuspace.workflow.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.workflow.model.Category;
import de.soco.software.simuspace.workflow.model.impl.ProgressBar;

/**
 * New Workflow Data Transfer Object for Fornt End representation of workflow.
 *
 * @author M.Nasir.Farooq
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class LatestWorkFlowDTO {

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final WorkflowEntity ENTITY_CLASS = new WorkflowEntity();

    /**
     * The Constant TYPE_ID.
     */
    public static final UUID TYPE_ID = UUID.fromString( ConstantsID.WORKFLOW_DTO_TYPE_ID );

    /**
     * The Constant WORKFLOW_CONFIG.
     */
    public static final String WORKFLOW_CONFIG = "Workflow Project Config";

    /**
     * The id.
     */
    @UIFormField( name = "id", title = "3000021x4", isAsk = false, type = "hidden" )
    @UIColumn( data = "id", name = "composedId.id", filter = "uuid", renderer = "text", title = "3000021x4" )
    private UUID id;

    /**
     * The name.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "name", title = "3000032x4", orderNum = 1 )
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000032x4", orderNum = 1, width = 0 )
    private String name;

    /**
     * The description.
     */
    @UIFormField( name = "description", title = "3000011x4", orderNum = 2, type = "textarea" )
    @UIColumn( data = "description", name = "description", filter = "text", renderer = "text", title = "3000011x4", orderNum = 2 )
    private String description;

    /**
     * The type.
     */
    @UIFormField( name = "type", title = "3000051x4", isAsk = false, orderNum = 3 )
    @UIColumn( data = "type", name = "type", filter = "", renderer = "text", title = "3000051x4", isSortable = false, orderNum = 3 )
    private String type;

    /**
     * The updated on.
     */
    @UIFormField( name = "lifeCycleStatus.name", title = "3000066x4", isAsk = false, orderNum = 4 )
    @UIColumn( data = "lifeCycleStatus.name", filter = "", renderer = "text", title = "3000066x4", name = "lifeCycleStatus", orderNum = 4 )
    private StatusDTO lifeCycleStatus;

    /**
     * The config
     */
    private String config;

    /**
     * The created on.
     */
    @UIFormField( name = "createdOn", title = "3000008x4", isAsk = false, orderNum = 12, type = "date" )
    @UIColumn( data = "createdOn", filter = "dateRange", renderer = "date", title = "3000008x4", name = "createdOn", orderNum = 12 )
    private Date createdOn;

    /**
     * The createdBy.
     */
    @UIFormField( name = "createdBy.userUid", title = "3000064x4", isAsk = false, orderNum = 13 )
    @UIColumn( data = "createdBy.userUid", tooltip = "{createdBy.userName}", filter = "text", url = "system/user/{createdBy.id}", renderer = "link", title = "3000064x4", name = "createdBy.userUid", orderNum = 13 )
    private UserDTO createdBy;

    /**
     * The updated on.
     */
    @UIFormField( name = "modifiedOn", title = "3000053x4", isAsk = false, orderNum = 14, type = "date" )
    @UIColumn( data = "modifiedOn", filter = "dateRange", renderer = "date", title = "3000053x4", name = "modifiedOn", orderNum = 14 )
    private Date modifiedOn;

    /**
     * The updated on.
     */
    @UIFormField( name = "modifiedBy.userUid", title = "3000065x4", isAsk = false, orderNum = 15 )
    @UIColumn( data = "modifiedBy.userUid", tooltip = "{modifiedBy.userName}", name = "modifiedBy.userUid", filter = "text", url = "system/user/{modifiedBy.id}", renderer = "link", title = "3000065x4", orderNum = 15 )
    private UserDTO modifiedBy;

    /**
     * The object type id.
     */
    @UIFormField( name = "typeId", title = "3000074x4", type = "hidden", isAsk = false, orderNum = 16 )
    @UIColumn( data = "typeId", name = "typeId", filter = "uuid", renderer = "hidden", title = "3000074x4", type = "hidden", isShow = false, orderNum = 16 )
    private UUID typeId;

    /**
     * The job params.
     */
    private Map< String, Object > job;

    /**
     * The location.
     */
    private List< LocationDTO > runOnLocation;

    /**
     * The locations.
     */
    private String locations;

    /**
     * The runs on.
     */
    private String runsOn;

    /**
     * workflow designer token.
     */
    private String token;

    /**
     * The version Object consist of version Id and version label of workflow.
     */
    private VersionDTO version;

    /**
     * The versions list for a workflow.
     */
    private List< WorkflowDTO > versions;

    /**
     * Whether this is an intractive workflow.
     */
    private boolean interactive;

    /**
     * The job counts .
     */
    private ProgressBar jobs;

    /**
     * Whether this is an favorite workflow.
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
     * The elements.
     */
    private Map< String, Object > elements;

    /**
     * The style.
     */
    private List< Object > style;

    /**
     * The zooming enabled.
     */
    private boolean zoomingEnabled;

    /**
     * The user zooming enabled.
     */
    private boolean userZoomingEnabled;

    /**
     * The zoom.
     */
    private double zoom;

    /**
     * The min zoom.
     */
    private double minZoom;

    /**
     * The max zoom.
     */
    private Integer maxZoom;

    /**
     * The panning enabled.
     */
    private boolean panningEnabled;

    /**
     * The user panning enabled.
     */
    private boolean userPanningEnabled;

    /**
     * The pan.
     */
    private Map< String, Object > pan;

    /**
     * The box selection enabled.
     */
    private boolean boxSelectionEnabled;

    /**
     * The renderer.
     */
    private Map< String, Object > renderer;

    /**
     * The user signature.
     */
    private String userSignature;

    /**
     * The actions (like status change).
     */
    private List< Action > actions;

    /**
     * The categories list attached to workflow.
     */
    private List< Category > categories;

    /**
     * The workflow type.
     */
    private int workflowType = 0;

    /**
     * The master job id.
     */
    private String masterJobId;

    /**
     * The dummy master job id.
     */
    private String dummyMasterJobId;

    /**
     * The custom flags.
     */
    private List< String > customFlags;

    private boolean hasGlobalVariables;

    /**
     * The rerun job id.
     */
    private String rerunJobId;

    /**
     * The comments.
     */
    private String comments;

    public LatestWorkFlowDTO() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final LatestWorkFlowDTO other = ( LatestWorkFlowDTO ) obj;
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals( other.id ) ) {
            return false;
        }
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals( other.name ) ) {
            return false;
        }
        if ( version == null ) {
            if ( other.version != null ) {
                return false;
            }
        } else if ( !version.equals( other.version ) ) {
            return false;
        }
        return true;
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
     * Checks if is box selection enabled.
     *
     * @return true, if is box selection enabled
     */
    public boolean getBoxSelectionEnabled() {
        return boxSelectionEnabled;
    }

    public void setModifiedOn( Date modifiedOn ) {
        this.modifiedOn = modifiedOn;
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
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the elements.
     *
     * @return the elements
     */
    public Map< String, Object > getElements() {
        return elements;
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
     * Gets the jobs.
     *
     * @return the jobs
     */
    public ProgressBar getJobs() {
        return jobs;
    }

    /**
     * Gets the life cycle status.
     *
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
     * Gets the max zoom.
     *
     * @return the max zoom
     */
    public Integer getMaxZoom() {
        return maxZoom;
    }

    /**
     * Gets the min zoom.
     *
     * @return the min zoom
     */
    public double getMinZoom() {
        return minZoom;
    }

    /**
     * Gets the updated by.
     *
     * @return the updated by
     */
    public UserDTO getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Gets the updated on.
     *
     * @return the updated on
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
     * Gets the pan.
     *
     * @return the pan
     */
    public Map< String, Object > getPan() {
        return pan;
    }

    /**
     * Checks if is panning enabled.
     *
     * @return true, if is panning enabled
     */
    public boolean getPanningEnabled() {
        return panningEnabled;
    }

    /**
     * Gets the renderer.
     *
     * @return the renderer
     */
    public Map< String, Object > getRenderer() {
        return renderer;
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
     * Gets the runs on.
     *
     * @return the runs on
     */
    public String getRunsOn() {
        return runsOn;
    }

    /**
     * Gets the style.
     *
     * @return the style
     */
    public List< Object > getStyle() {
        return style;
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
     * Gets the type.
     *
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
     * Checks if is user panning enabled.
     *
     * @return true, if is user panning enabled
     */
    public boolean getUserPanningEnabled() {
        return userPanningEnabled;
    }

    /**
     * Gets the user signature.
     *
     * @return the user signature
     */
    public String getUserSignature() {
        return userSignature;
    }

    /**
     * Checks if is user zooming enabled.
     *
     * @return true, if is user zooming enabled
     */
    public boolean getUserZoomingEnabled() {
        return userZoomingEnabled;
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
     * Gets the zoom.
     *
     * @return the zoom
     */
    public double getZoom() {
        return zoom;
    }

    /**
     * Checks if is zooming enabled.
     *
     * @return true, if is zooming enabled
     */
    public boolean getZoomingEnabled() {
        return zoomingEnabled;
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ( prime * result ) + ( ( description == null ) ? 0 : description.hashCode() );
        result = ( prime * result ) + ( ( id == null ) ? 0 : id.hashCode() );
        result = ( prime * result ) + ( ( name == null ) ? 0 : name.hashCode() );
        result = ( prime * result ) + ( ( version == null ) ? 0 : version.hashCode() );
        return result;
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
     * Checks if is favorite.
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
     * Gets the definition.
     *
     * @return the definition
     */
    public Map< String, Object > prepareDefinition() {
        final HashMap< String, Object > definition = new HashMap<>();
        definition.put( "elements", getElements() );
        definition.put( "style", getStyle() );
        definition.put( "zoomingEnabled", getZoomingEnabled() );
        definition.put( "userZoomingEnabled", getUserZoomingEnabled() );
        definition.put( "zoom", getZoom() );
        definition.put( "minZoom", getMinZoom() );
        definition.put( "maxZoom", getMaxZoom() );
        definition.put( "panningEnabled", getPanningEnabled() );
        definition.put( "userPanningEnabled", getUserPanningEnabled() );
        definition.put( "pan", getPan() );
        definition.put( "boxSelectionEnabled", getBoxSelectionEnabled() );
        definition.put( "renderer", getRenderer() );
        definition.put( "userSignature", getUserSignature() );
        if ( null != getJob() && !getJob().isEmpty() ) {
            definition.put( "job", getJob() );
        } else {
            definition.put( "job", setBasicJobParams( definition ) );
        }
        return definition;
    }

    /**
     * Sets the basic job params.
     *
     * @param defination
     *         the defination
     */
    private Map< String, Object > setBasicJobParams( HashMap< String, Object > defination ) {
        Map< String, Object > jobMap = new HashMap<>();
        jobMap.put( "name", getNameWithUnderscore() + "_run" );
        jobMap.put( "header", ConstantsString.EMPTY_STRING );
        jobMap.put( "runsOn", LocationsEnum.DEFAULT_LOCATION.getId() );
        jobMap.put( "workingDir", ConstantsString.EMPTY_STRING );
        jobMap.put( "comments", ConstantsString.EMPTY_STRING );
        jobMap.put( "description", ConstantsString.EMPTY_STRING );
        jobMap.put( "changeOnRun", ConstantsString.EMPTY_STRING );
        return jobMap;
    }

    private String getNameWithUnderscore() {
        String localName = getName();
        if ( localName.contains( " " ) ) {
            localName = localName.replace( " ", "_" );
        }
        return localName;
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
     * Sets the box selection enabled.
     *
     * @param boxSelectionEnabled
     *         the new box selection enabled
     */
    public void setBoxSelectionEnabled( boolean boxSelectionEnabled ) {
        this.boxSelectionEnabled = boxSelectionEnabled;
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
     * Sets the description.
     *
     * @param description
     *         the new description
     */
    public void setDescription( String description ) {
        this.description = description;
    }

    /**
     * Sets the elements.
     *
     * @param elements
     *         the elements
     */
    public void setElements( Map< String, Object > elements ) {
        this.elements = elements;
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
     *         the new favorite
     */
    public void setFavorite( boolean favorite ) {
        this.favorite = favorite;
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
     * Sets the life cycle status.
     *
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
     * Sets the max zoom.
     *
     * @param maxZoom
     *         the new max zoom
     */
    public void setMaxZoom( Integer maxZoom ) {
        this.maxZoom = maxZoom;
    }

    /**
     * Sets the min zoom.
     *
     * @param minZoom
     *         the new min zoom
     */
    public void setMinZoom( double minZoom ) {
        this.minZoom = minZoom;
    }

    /**
     * Sets the updated by.
     *
     * @param updatedBy
     *         the new updated by
     */
    public void setModifiedBy( UserDTO updatedBy ) {
        this.modifiedBy = updatedBy;
    }

    /**
     * Sets the updated on.
     *
     * @param updatedOn
     *         the new updated on
     *
     * @return the modified on
     */
    public void getModifiedOn( Date updatedOn ) {
        this.modifiedOn = updatedOn;
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
     * Sets the pan.
     *
     * @param pan
     *         the pan
     */
    public void setPan( Map< String, Object > pan ) {
        this.pan = pan;
    }

    /**
     * Sets the panning enabled.
     *
     * @param panningEnabled
     *         the new panning enabled
     */
    public void setPanningEnabled( boolean panningEnabled ) {
        this.panningEnabled = panningEnabled;
    }

    /**
     * Sets the renderer.
     *
     * @param renderer
     *         the renderer
     */
    public void setRenderer( Map< String, Object > renderer ) {
        this.renderer = renderer;
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
     * Sets the runs on.
     *
     * @param runsOn
     *         the new runs on
     */
    public void setRunsOn( String runsOn ) {
        this.runsOn = runsOn;
    }

    /**
     * Sets the style.
     *
     * @param style
     *         the new style
     */
    public void setStyle( List< Object > style ) {
        this.style = style;
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
     * Sets the type.
     *
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
     * Sets the user panning enabled.
     *
     * @param userPanningEnabled
     *         the new user panning enabled
     */
    public void setUserPanningEnabled( boolean userPanningEnabled ) {
        this.userPanningEnabled = userPanningEnabled;
    }

    /**
     * Sets the user signature.
     *
     * @param userSignature
     *         the new user signature
     */
    public void setUserSignature( String userSignature ) {
        this.userSignature = userSignature;
    }

    /**
     * Sets the user zooming enabled.
     *
     * @param userZoomingEnabled
     *         the new user zooming enabled
     */
    public void setUserZoomingEnabled( boolean userZoomingEnabled ) {
        this.userZoomingEnabled = userZoomingEnabled;
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
     * Sets the definition.
     *
     * @param definition
     *         the definition
     */
    public void setWithDefinition( Map< String, Object > definition ) {

        setElements( ( Map< String, Object > ) definition.get( "elements" ) );
        setStyle( ( List< Object > ) definition.get( "style" ) );
        setZoomingEnabled( ( boolean ) definition.get( "zoomingEnabled" ) );
        setUserZoomingEnabled( ( boolean ) definition.get( "userZoomingEnabled" ) );
        setZoom( ( double ) definition.get( "zoom" ) );
        Object minZoom = definition.get( "minZoom" );
        if ( minZoom instanceof Double dbl ) {
            setMinZoom( dbl );
        } else if ( minZoom instanceof Float flt ) {
            setMinZoom( flt );
        }
        setMaxZoom( ( Integer ) definition.get( "maxZoom" ) );
        setPanningEnabled( ( boolean ) definition.get( "panningEnabled" ) );
        setUserPanningEnabled( ( boolean ) definition.get( "userPanningEnabled" ) );
        setPan( ( Map< String, Object > ) definition.get( "pan" ) );
        setBoxSelectionEnabled( ( boolean ) definition.get( "boxSelectionEnabled" ) );
        setRenderer( ( Map< String, Object > ) definition.get( "renderer" ) );
        setUserSignature( ( String ) definition.get( "userSignature" ) );
        setJob( ( Map< String, Object > ) definition.get( "job" ) );
    }

    /**
     * Sets the zoom.
     *
     * @param zoom
     *         the new zoom
     */
    public void setZoom( double zoom ) {
        this.zoom = zoom;
    }

    /**
     * Sets the zooming enabled.
     *
     * @param zoomingEnabled
     *         the new zooming enabled
     */
    public void setZoomingEnabled( boolean zoomingEnabled ) {
        this.zoomingEnabled = zoomingEnabled;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "LatestWorkFlowDTO [id=" + id + ", name=" + name + ", description=" + description + ", createdOn=" + createdOn
                + ", createdBy=" + createdBy + ", modifiedOn=" + modifiedOn + ", modifiedBy=" + modifiedBy + ", config=" + config
                + ", typeId=" + typeId + ", type=" + type + ", lifeCycleStatus=" + lifeCycleStatus + ", token=" + token + ", version="
                + version + ", versions=" + versions + ", interactive=" + interactive + ", jobs=" + jobs + ", favorite=" + favorite
                + ", executable=" + executable + ", curentSimUserId=" + curentSimUserId + ", elements=" + elements + ", style=" + style
                + ", zoomingEnabled=" + zoomingEnabled + ", userZoomingEnabled=" + userZoomingEnabled + ", zoom=" + zoom + ", minZoom="
                + minZoom + ", maxZoom=" + maxZoom + ", panningEnabled=" + panningEnabled + ", userPanningEnabled=" + userPanningEnabled
                + ", pan=" + pan + ", boxSelectionEnabled=" + boxSelectionEnabled + ", renderer=" + renderer + ", userSignature="
                + userSignature + ", actions=" + actions + ", categories=" + categories + "]";
    }

    /**
     * Gets the workflow type.
     *
     * @return the workflow type
     */
    public int getWorkflowType() {
        return workflowType;
    }

    /**
     * Sets the workflow type.
     *
     * @param workflowType
     *         the new workflow type
     */
    public void setWorkflowType( int workflowType ) {
        this.workflowType = workflowType;
    }

    /**
     * Gets the master job id.
     *
     * @return the master job id
     */
    public String getMasterJobId() {
        return masterJobId;
    }

    /**
     * Sets the master job id.
     *
     * @param masterJobId
     *         the new master job id
     */
    public void setMasterJobId( String masterJobId ) {
        this.masterJobId = masterJobId;
    }

    /**
     * Gets the dummy master job id.
     *
     * @return the dummy master job id
     */
    public String getDummyMasterJobId() {
        return dummyMasterJobId;
    }

    /**
     * Sets the dummy master job id.
     *
     * @param dummyMasterJobId
     *         the new dummy master job id
     */
    public void setDummyMasterJobId( String dummyMasterJobId ) {
        this.dummyMasterJobId = dummyMasterJobId;
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
     * Gets the job.
     *
     * @return the job
     */
    public Map< String, Object > getJob() {
        return job;
    }

    /**
     * Sets the job.
     *
     * @param job
     *         the job
     */
    public void setJob( Map< String, Object > job ) {
        this.job = job;
    }

    /**
     * Gets the checks for global variables.
     *
     * @return the checks for global variables
     */
    public boolean getHasGlobalVariables() {
        return hasGlobalVariables;
    }

    /**
     * Sets the checks for global variables.
     *
     * @param hasGlobalVariables
     *         the new checks for global variables
     */
    public void setHasGlobalVariables( boolean hasGlobalVariables ) {
        this.hasGlobalVariables = hasGlobalVariables;
    }

    /**
     * Gets the rerun job id.
     *
     * @return the rerun job id
     */
    public String getRerunJobId() {
        return rerunJobId;
    }

    /**
     * Sets the rerun job id.
     *
     * @param rerunJobId
     *         the new rerun job id
     */
    public void setRerunJobId( String rerunJobId ) {
        this.rerunJobId = rerunJobId;
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

    /**
     * Validate html fields.
     */
    public void validateHtmlFields() {
        if ( !ValidationUtils.isValidHtml( getDescription() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.UTILS_INVALID_VALUE.getKey(),
                    MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_DESCRIPTION.getKey() ) ) );
        }
        if ( !ValidationUtils.isValidHtml( getComments() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.UTILS_INVALID_VALUE.getKey(),
                    MessageBundleFactory.getMessage( "3000082x4" ) ) );
        }
        Map< String, Object > job = getJob();
        if ( job != null ) {
            String header = ( String ) job.get( "header" );
            if ( Objects.nonNull( header ) && !ValidationUtils.isValidHtml( header ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.UTILS_INVALID_VALUE.getKey(),
                        MessageBundleFactory.getMessage( "3000329x4" ) ) );
            }
        }

    }

}
