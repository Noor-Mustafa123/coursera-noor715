package de.soco.software.simuspace.suscore.data.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This represent the class for status possible properties of any object.
 *
 * @author Nosheen.Sharif
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class StatusConfigDTO {

    /**
     * Mapped status id.
     */
    private String id;

    /**
     * Mapped status name.
     */
    private String name;

    /**
     * Mapped object to be unique or versionable.
     */
    private boolean unique;

    /**
     * Mapped status change to move old version to status id.
     */
    private String moveOldVersionToStatus;

    /**
     * Mapped status list to which the change is possible.
     */
    private List< String > canMoveToStatus;

    /**
     * if object to be runnable by the user.
     */
    private boolean executable;

    /**
     * if object to be visible to the user.
     */
    private String visible;

    /**
     * The allow check out.
     */
    private boolean allowCheckOut;

    /**
     * The allow changes.
     */
    private boolean allowChanges;

    /**
     * The update overview.
     */
    private boolean updateOverview;

    /**
     * Instantiates a new status detail.
     */
    public StatusConfigDTO() {
        super();
    }

    /**
     * Gets the can move to status.
     *
     * @return the can move to status
     */
    public List< String > getCanMoveToStatus() {
        return canMoveToStatus;
    }

    /**
     * Gets the move old version to status.
     *
     * @return the move old version to status
     */
    public String getMoveOldVersionToStatus() {
        return moveOldVersionToStatus;
    }

    /**
     * Checks if is allow check out.
     *
     * @return true, if is allow check out
     */
    public boolean isAllowCheckOut() {
        return allowCheckOut;
    }

    /**
     * Sets the allow check out.
     *
     * @param allowCheckOut
     *         the new allow check out
     */
    public void setAllowCheckOut( boolean allowCheckOut ) {
        this.allowCheckOut = allowCheckOut;
    }

    /**
     * Checks if is allow changes.
     *
     * @return true, if is allow changes
     */
    public boolean isAllowChanges() {
        return allowChanges;
    }

    /**
     * Sets the allow changes.
     *
     * @param allowChanges
     *         the new allow changes
     */
    public void setAllowChanges( boolean allowChanges ) {
        this.allowChanges = allowChanges;
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
     * Gets the visible.
     *
     * @return the visible
     */
    public String getVisible() {
        return visible;
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
     * Checks if is unique.
     *
     * @return true, if is unique
     */
    public boolean isUnique() {
        return unique;
    }

    /**
     * Sets the can move to status.
     *
     * @param canMoveToStatus
     *         the new can move to status
     */
    public void setCanMoveToStatus( List< String > canMoveToStatus ) {
        this.canMoveToStatus = canMoveToStatus;
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
     * Sets the move old version to status.
     *
     * @param moveOldVersionToStatus
     *         the new move old version to status
     */
    public void setMoveOldVersionToStatus( String moveOldVersionToStatus ) {
        this.moveOldVersionToStatus = moveOldVersionToStatus;
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
     * Sets the unique.
     *
     * @param unique
     *         the new unique
     */
    public void setUnique( boolean unique ) {
        this.unique = unique;
    }

    /**
     * Sets the visible.
     *
     * @param visible
     *         the new visible
     */
    public void setVisible( String visible ) {
        this.visible = visible;
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

    /**
     * Checks if is update overview.
     *
     * @return true, if is update overview
     */
    public boolean isUpdateOverview() {
        return updateOverview;
    }

    /**
     * Sets the update overview.
     *
     * @param updateOverview
     *         the new update overview
     */
    public void setUpdateOverview( boolean updateOverview ) {
        this.updateOverview = updateOverview;
    }

}
