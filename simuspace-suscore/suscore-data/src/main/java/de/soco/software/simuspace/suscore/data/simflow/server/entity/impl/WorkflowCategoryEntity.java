package de.soco.software.simuspace.suscore.data.simflow.server.entity.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;

/**
 * This Class related the workflows to categories and mapped to database as Entity
 *
 * @author Nosheen.Sharif
 */
@Entity
@Table( name = "workflow_category" )
@Deprecated( forRemoval = true )
public class WorkflowCategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The id of workflow_category .Database table primary key
     */
    @Id
    @Column( name = "id" )
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * workflow related to category
     */
    @ManyToOne( optional = false )
    @JoinColumns( value = { @JoinColumn( name = "workflow_id", referencedColumnName = "id" ),
            @JoinColumn( name = "workflow_version_id", referencedColumnName = "version_id" ) } )
    private WorkflowEntity workflow;

    /**
     * category reference
     */
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "category_id", nullable = false )
    private CategoryEntity category;

    /**
     * Instantiates a new workflow category entity.
     */
    public WorkflowCategoryEntity() {
        super();
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
     * Gets the category.
     *
     * @return the category
     */
    public CategoryEntity getCategory() {
        return category;
    }

    /**
     * Sets the category.
     *
     * @param category
     *         the new category
     */
    public void setCategory( CategoryEntity category ) {
        this.category = category;
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
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( UUID id ) {
        this.id = id;
    }

}
