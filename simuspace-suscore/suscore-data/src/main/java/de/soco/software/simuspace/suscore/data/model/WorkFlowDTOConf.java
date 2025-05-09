package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;

/**
 * New Workflow Data Transfer Object for Fornt End representation of workflow.
 *
 * @author M.Nasir.Farooq
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class WorkFlowDTOConf extends SusDTO {

    private static final long serialVersionUID = -3301051704721412356L;

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
    public static final String WORKFLOW_CONFIG = "WorkflowProjectConfig.json";

    /**
     * The name.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "name", title = "3000032x4" )
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000032x4", orderNum = 1, width = 0 )
    private String name;

    /**
     * The config.
     */
    private String config;

    /**
     * The object type id.
     */
    @UIFormField( name = "typeId", title = "3000074x4", type = "hidden", isAsk = false, orderNum = 7 )
    @UIColumn( data = "typeId", name = "typeId", filter = "", renderer = "hidden", title = "3000074x4", type = "hidden", isShow = false, orderNum = 7, isSortable = false )
    private UUID typeId;

    @UIFormField( name = "type", title = "3000051x4", isAsk = false )
    @UIColumn( data = "type", name = "type", filter = "", renderer = "text", title = "3000051x4", orderNum = 5, isSortable = false )
    private String type;

    /**
     * The updated on.
     */
    @UIFormField( name = "lifeCycleStatus.name", title = "3000066x4", isAsk = false )
    @UIColumn( data = "lifeCycleStatus.name", filter = "", renderer = "text", title = "3000066x4", name = "lifeCycleStatus", orderNum = 6 )
    private StatusDTO lifeCycleStatus;

    /**
     * The link.
     */
    @UIFormField( name = "link", title = "3000121x4", isAsk = false, orderNum = 8 )
    private String link;

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
     * The comments made by user.
     */
    private String comments;

    /**
     * workflow designer token.
     */
    private String token;

    /**
     * The version Object consist of version Id and version label of workflow.
     */
    private VersionDTO version;

    /**
     * Whether this is an intractive workflow
     */
    private boolean interactive;

    /**
     * Whether this is an favorite workflow
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
    private float minZoom;

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
     * The file path in case of file run.
     */
    private String file;

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
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
        return true;
    }

    /**
     * Checks if is box selection enabled.
     *
     * @return true, if is box selection enabled
     */
    public boolean getBoxSelectionEnabled() {
        return boxSelectionEnabled;
    }

    /**
     * This method returns the comments of a work flow.
     *
     * @return the comments
     */
    public String getComments() {
        return comments;
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
     * Gets the curent sim user id.
     *
     * @return the curent sim user id
     */
    public UUID getCurentSimUserId() {
        return curentSimUserId;
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
     * Gets the file.
     *
     * @return the file
     */
    public String getFile() {
        return file;
    }

    /**
     * @return the lifeCycleStatus
     */
    public StatusDTO getLifeCycleStatus() {
        return lifeCycleStatus;
    }

    /**
     * Gets the link.
     *
     * @return the link
     */
    public String getLink() {
        return link;
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
    public float getMinZoom() {
        return minZoom;
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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ( prime * result ) + ( ( getDescription() == null ) ? 0 : getDescription().hashCode() );
        result = ( prime * result ) + ( ( getId() == null ) ? 0 : getId().hashCode() );
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

        final HashMap< String, Object > defination = new HashMap<>();

        defination.put( "elements", getElements() );
        defination.put( "style", getStyle() );
        defination.put( "zoomingEnabled", getZoomingEnabled() );
        defination.put( "userZoomingEnabled", getUserZoomingEnabled() );
        defination.put( "zoom", getZoom() );
        defination.put( "minZoom", getMinZoom() );
        defination.put( "maxZoom", getMaxZoom() );
        defination.put( "panningEnabled", getPanningEnabled() );
        defination.put( "userPanningEnabled", getUserPanningEnabled() );
        defination.put( "pan", getPan() );
        defination.put( "boxSelectionEnabled", getBoxSelectionEnabled() );
        defination.put( "renderer", getRenderer() );
        defination.put( "userSignature", getUserSignature() );

        return defination;

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
     * Sets the comments.
     *
     * @param comments
     *         the new comments
     */
    public void setComments( String comments ) {
        this.comments = comments;
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
     * Sets the curent sim user id.
     *
     * @param curentSimUserId
     *         the new curent sim user id
     */
    public void setCurentSimUserId( UUID curentSimUserId ) {
        this.curentSimUserId = curentSimUserId;
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
     *         the favorite
     */
    public void setFavorite( boolean favorite ) {
        this.favorite = favorite;
    }

    /**
     * Sets the file.
     *
     * @param file
     *         the new file
     */
    public void setFile( String file ) {
        this.file = file;
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
     * @param lifeCycleStatus
     *         the lifeCycleStatus to set
     */
    public void setLifeCycleStatus( StatusDTO lifeCycleStatus ) {
        this.lifeCycleStatus = lifeCycleStatus;
    }

    /**
     * Sets the link.
     *
     * @param link
     *         the new link
     */
    public void setLink( String link ) {
        this.link = link;
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
    public void setMinZoom( float minZoom ) {
        this.minZoom = minZoom;
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
        setMinZoom( ( float ) definition.get( "minZoom" ) );
        setMaxZoom( ( Integer ) definition.get( "maxZoom" ) );
        setPanningEnabled( ( boolean ) definition.get( "panningEnabled" ) );
        setUserPanningEnabled( ( boolean ) definition.get( "userPanningEnabled" ) );
        setPan( ( Map< String, Object > ) definition.get( "pan" ) );
        setBoxSelectionEnabled( ( boolean ) definition.get( "boxSelectionEnabled" ) );
        setRenderer( ( Map< String, Object > ) definition.get( "renderer" ) );
        setUserSignature( ( String ) definition.get( "userSignature" ) );

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

    @Override
    public String toString() {
        return "WorkFlowDTOConf [id=" + getId() + ", name=" + name + ", description=" + getDescription() + ", createdOn=" + getCreatedOn()
                + ", createdBy=" + getCreatedBy() + ", updatedOn=" + getModifiedOn() + ", updatedBy=" + getModifiedBy() + ", config="
                + config + ", typeId=" + typeId + ", type=" + type + ", lifeCycleStatus=" + lifeCycleStatus + ", link=" + link
                + ", runOnLocation=" + runOnLocation + ", locations=" + locations + ", runsOn=" + runsOn + ", comments=" + comments
                + ", token=" + token + ", version=" + version + ", interactive=" + interactive + ", favorite=" + favorite + ", executable="
                + executable + ", curentSimUserId=" + curentSimUserId + ", elements=" + elements + ", style=" + style + ", zoomingEnabled="
                + zoomingEnabled + ", userZoomingEnabled=" + userZoomingEnabled + ", zoom=" + zoom + ", minZoom=" + minZoom + ", maxZoom="
                + maxZoom + ", panningEnabled=" + panningEnabled + ", userPanningEnabled=" + userPanningEnabled + ", pan=" + pan
                + ", boxSelectionEnabled=" + boxSelectionEnabled + ", renderer=" + renderer + ", userSignature=" + userSignature + ", file="
                + file + "]";
    }

}
