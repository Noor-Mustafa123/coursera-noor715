package de.soco.software.simuspace.workflow.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.DataTransferObject;

/**
 * This represent the class for status possible proterties of any object.
 *
 * @author Nosheen.Sharif
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class StatusDetailDTO extends DataTransferObject {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Mapped status id.
     */
    private Integer id;

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
    private Integer moveOldVersionToStatus;

    /**
     * Mapped status list to which the change is possible.
     */
    private List< Integer > canMoveToStatus;

    /**
     * if object to be runnable by the user.
     */
    private boolean executable;

    /**
     * if object to be visible to the user.
     */
    private String visible;

    /**
     * Instantiates a new status detail.
     */
    public StatusDetailDTO() {
        super();
    }

    /**
     * Gets the can move to status.
     *
     * @return the can move to status
     */
    public List< Integer > getCanMoveToStatus() {
        return canMoveToStatus;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Gets the move old version to status.
     *
     * @return the move old version to status
     */
    public Integer getMoveOldVersionToStatus() {
        return moveOldVersionToStatus;
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
    public void setCanMoveToStatus( List< Integer > canMoveToStatus ) {
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
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( Integer id ) {
        this.id = id;
    }

    /**
     * Sets the move old version to status.
     *
     * @param moveOldVersionToStatus
     *         the new move old version to status
     */
    public void setMoveOldVersionToStatus( Integer moveOldVersionToStatus ) {
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

}