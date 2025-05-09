package de.soco.software.simuspace.workflow.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.workflow.model.impl.NodeEdge;
import de.soco.software.simuspace.workflow.model.impl.UserWorkflowImpl;

/**
 * This Interface is designed to contain all the properties that a workflow have.
 *
 * @author M.Nasir.Farooq
 */
@JsonDeserialize( as = UserWorkflowImpl.class )
@JsonIgnoreProperties( ignoreUnknown = true )
public interface UserWorkFlow {

    /**
     * Add a new element is UserWFElement list.
     *
     * @param element
     *         new element to be added
     */

    void addElement( UserWFElement element );

    /**
     * Gets the directory.
     *
     * @return the directory
     */
    String getDirectory();

    /**
     * Gets the edges.
     *
     * @return the edges
     */
    List< NodeEdge > getEdges();

    /**
     * Gets the id.
     *
     * @return the id
     */
    String getId();

    /**
     * Gets the name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the nodes.
     *
     * @return the nodes
     */
    List< UserWFElement > getNodes();

    /**
     * Gets the owner.
     *
     * @return the owner
     */
    User getOwner();

    /**
     * Gets the interactive.
     *
     * @return the interactive
     */
    boolean isInteractive();

    /**
     * Sets the directory.
     *
     * @param dir
     *         the new directory
     */
    void setDirectory( String dir );

    /**
     * Sets the edges.
     *
     * @param edges
     *         the new edges
     */
    void setEdges( List< NodeEdge > edges );

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    void setId( String id );

    /**
     * Sets the interactive.
     *
     * @param interactive
     *         the new interactive
     */
    void setInteractive( boolean interactive );

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    void setName( String name );

    /**
     * Sets the nodes.
     *
     * @param nodes
     *         the new nodes
     */
    void setNodes( List< UserWFElement > nodes );

    /**
     * Sets the owner.
     *
     * @param owner
     *         the new owner
     */
    void setOwner( User owner );

    /**
     * Validate that User Work Flow properties are valid.
     *
     * @param config
     *         the config
     *
     * @return Notification if validation done successfully
     */
    Notification validate( WorkflowConfiguration config );

    /**
     * Validate User WorkFlow element's common fields. This method is specifically<br> written to Validate All the common Fields i.e, name,
     * key ,version, comments more fields can be added
     *
     * @param element
     *         the element
     * @param runtimeValidation
     *         It is true if validation occur on runtime.
     *
     * @return the notification
     */
    Notification validateUserCommonFields( UserWFElement element, boolean runtimeValidation, boolean isImportWorkflow );

}