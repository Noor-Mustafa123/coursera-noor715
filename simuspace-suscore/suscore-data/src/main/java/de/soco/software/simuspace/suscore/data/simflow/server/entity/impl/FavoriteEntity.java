package de.soco.software.simuspace.suscore.data.simflow.server.entity.impl;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serializable;

import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.base.AbstractPersistentObject;

/**
 * This Class handles the mapping of addition of favorites.
 *
 * @author Zeeshan jamal
 */
@Entity
@Table( name = "favorite" )
public class FavoriteEntity extends AbstractPersistentObject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * workflow can have custom attributes.
     */
    @ManyToOne( optional = false, fetch = FetchType.EAGER )
    @JoinColumns( value = { @JoinColumn( name = "workflow_id", referencedColumnName = "id" ),
            @JoinColumn( name = "workflow_version_id", referencedColumnName = "version_id" ) } )
    private WorkflowEntity workflow;

    /**
     * workflow can have custom attributes.
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "favorite_by", nullable = false )
    private UserEntity favoriteBy;

    /**
     * Instantiates a new favorite entity.
     */
    public FavoriteEntity() {
        super();
    }

    /**
     * @param workflow
     *         the workflow
     * @param favoriteBy
     *         the favoriteBy
     */
    public FavoriteEntity( WorkflowEntity workflow, UserEntity favoriteBy ) {
        super();
        this.favoriteBy = favoriteBy;
        this.workflow = workflow;

    }

    /**
     * Gets the workflow.
     *
     * @return the workflow
     */
    public WorkflowEntity getWorkflow() {
        return workflow;
    }

    /**
     * Sets the workflow.
     *
     * @param workflow
     *         the new workflow
     */
    public void setWorkflow( WorkflowEntity workflow ) {
        this.workflow = workflow;
    }

    /**
     * @return the favoriteBy
     */
    public UserEntity getFavoriteBy() {
        return favoriteBy;
    }

    /**
     * @param favoriteBy
     *         the favoriteBy
     */
    public void setFavoriteBy( UserEntity favoriteBy ) {
        this.favoriteBy = favoriteBy;
    }

}
