package de.soco.software.simuspace.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.soco.software.simuspace.workflow.model.impl.ElementConnectionImpl;

/**
 * The Interface ElementConnections. Element connections tells us how two work flow elements are connected to each other. It contains a
 * source and target element id's
 *
 * @author M.Nasir.Farooq
 */
@JsonDeserialize( as = ElementConnectionImpl.class )
@JsonIgnoreProperties( ignoreUnknown = true )
public interface ElementConnection {

    /**
     * Gets the source.
     *
     * @return the source
     */
    String getSource();

    /**
     * Gets the target.
     *
     * @return the target
     */
    String getTarget();

    /**
     * Sets the source.
     *
     * @param source
     *         the new source
     */
    void setSource( String source );

    /**
     * Sets the target.
     *
     * @param target
     *         the new target
     */

    void setTarget( String target );

}