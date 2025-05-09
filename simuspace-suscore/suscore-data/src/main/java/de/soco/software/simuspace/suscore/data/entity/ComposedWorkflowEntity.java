package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import java.io.Serial;

/**
 * The Class ComposedworkflowEntity.
 */
@Entity
public class ComposedWorkflowEntity extends WorkflowTemplateEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The is system workflow.
     */
    @Column( name = "issystem_workflow" )
    private String isSystemWorkflow;

    /**
     * Gets the checks if is system workflow.
     *
     * @return the checks if is system workflow
     */
    public String getIsSystemWorkflow() {
        return isSystemWorkflow;
    }

    /**
     * Sets the checks if is system workflow.
     *
     * @param isSystemWorkflow
     *         the new checks if is system workflow
     */
    public void setIsSystemWorkflow( String isSystemWorkflow ) {
        this.isSystemWorkflow = isSystemWorkflow;
    }

}
