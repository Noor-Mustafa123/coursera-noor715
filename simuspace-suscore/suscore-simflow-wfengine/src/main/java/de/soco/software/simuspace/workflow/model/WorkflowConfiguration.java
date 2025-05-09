package de.soco.software.simuspace.workflow.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.workflow.model.impl.WorkflowConfigElements;
import de.soco.software.simuspace.workflow.model.impl.WorkflowConfigurationImpl;

/**
 * The Interface Config. This contains properties of a configuration of a work flow
 */
@JsonDeserialize( as = WorkflowConfigurationImpl.class )
public interface WorkflowConfiguration {

    /**
     * Gets the applicable. Applicable means an element as key can accept incoming connection from which other elements
     *
     * @return the applicable
     */
    Map< String, List< String > > getApplicable();

    /**
     * Gets the elements.
     *
     * @return the elements
     */
    List< WorkflowConfigElements > getElements();

    /**
     * Sets the applicable.
     *
     * @param applicable
     *         the applicable
     */
    void setApplicable( Map< String, List< String > > applicable );

    /**
     * Sets the elements.
     *
     * @param elements
     *         the new elements
     */
    void setElements( List< WorkflowConfigElements > elements );

    /**
     * this method validates a configuration.
     *
     * @return the notification
     */
    Notification validate();

}
