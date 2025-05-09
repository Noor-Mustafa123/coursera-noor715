package de.soco.software.simuspace.suscore.data.simflow.server.entity.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serializable;

import org.hibernate.annotations.Type;

/**
 * This class represents the custom attributes which can be added to other database elements(if needed). Currenty only workflow is mapped we
 * have other as per requirements
 *
 * @author Nosheen.Sharif
 */
@Entity
@Table( name = "wf_custom_attributes" )
public class CustomAttributes implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The id of object.Table primary key
     */
    @Id
    @Column( name = "id" )
    @Type( type = "uuid-char" )
    private int id;

    /**
     * workflow can have custom attributes.
     */
    @ManyToOne( optional = false )
    @JoinColumns( value = { @JoinColumn( name = "workflow_id", referencedColumnName = "id" ),
            @JoinColumn( name = "workflow_version_id", referencedColumnName = "version_id" ) } )
    private WorkflowEntity workflow;

    /**
     * The value assign to any key
     */
    @Column( name = "value_attribute" )
    @Lob
    private byte[] value;

    /**
     * Instantiates a new custom attributes.
     */
    public CustomAttributes() {
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
     * Gets the value.
     *
     * @return the value
     */
    public byte[] getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *         the new value
     */
    public void setValue( byte[] value ) {
        this.value = value;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( int id ) {
        this.id = id;
    }

}
