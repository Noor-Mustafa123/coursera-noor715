/*
 *
 */

package de.soco.software.simuspace.workflow.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.workflow.model.impl.ElementInfo;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.UserWFElementImpl;

/**
 * The Interface UserWFElement. UserWFElement is any work flow element that is added in a work flow it could be an i.e, input , output , or
 * script element a userWorkflow can contain as many UserWorkflow elements as need an element can further have fields that can be added in
 * an element i.e, textarea, dropdown
 *
 * @author M.Nasir.Farooq
 */
@JsonDeserialize( as = UserWFElementImpl.class )
@JsonIgnoreProperties( ignoreUnknown = true )
public interface UserWFElement extends BaseElement {

    /**
     * Gets the exceptions.
     *
     * @return the exceptions
     */
    List< Field< ? > > getExceptions();

    /**
     * Gets the execution value.
     *
     * @return the execution value
     */
    int getExecutionValue();

    /**
     * Gets the fields.
     *
     * @return the fields
     */
    List< Field< ? > > getFields();

    /**
     * Gets the id.
     *
     * @return the id
     */
    String getId();

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.workflow.model.BaseElement#getInfo()
     */
    @Override
    ElementInfo getInfo();

    /**
     * Gets the output.
     *
     * @return the output
     */
    List< Field< ? > > getOutput();

    /**
     * Gets the run mode.
     *
     * @return the run mode can be [client,server, user-selected, null] a runmode is users way of running a workflow either a workflow is
     * running on client side or server side
     */
    String getRunMode();

    /**
     * Gets the stop on work flow option.
     *
     * @return the stop on work flow option
     */
    String getStopOnWorkFlowOption();

    /**
     * Sets the exceptions.
     *
     * @param exceptions
     *         the new exceptions
     */
    void setExceptions( List< Field< ? > > exceptions );

    /**
     * Sets the fields.
     *
     * @param fields
     *         the new fields
     */
    void setFields( List< Field< ? > > fields );

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    void setId( String id );

    /**
     * Sets the info.
     *
     * @param info
     *         the new info
     */
    void setInfo( ElementInfo info );

    /**
     * Sets the output.
     *
     * @param output
     *         the new output
     */
    void setOutput( List< Field< ? > > output );

    /**
     * Sets the run runMode.
     *
     * @param runMode
     *         the new runMode
     */
    void setRunMode( String runMode );

    /**
     * Validate exception.
     *
     * @return the notification
     */
    Notification validateException();

}
