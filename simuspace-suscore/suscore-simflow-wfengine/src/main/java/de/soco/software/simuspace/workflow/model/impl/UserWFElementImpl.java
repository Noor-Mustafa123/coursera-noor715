package de.soco.software.simuspace.workflow.model.impl;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.workflow.model.UserWFElement;

/**
 * This is a model class for containing properties for a workflow element. It implemets UserWFElement that is holding common properties that
 * all workflow elements will have
 *
 * @author M.Nasir.Farooq
 */
public class UserWFElementImpl implements UserWFElement, Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The comments.
     */
    private String comments;

    /**
     * The description of work flow element. It is optional.
     */
    private String description;

    /**
     * The elements Fields. This is the list of fields that an element have. it could be a header , file etc
     */
    private List< Field< ? > > fields;

    /**
     * The elements exceptions. This is the list of fields that a element have.
     */
    private List< Field< ? > > exceptions;

    /**
     * The elements exceptions. This is the list of fields that an element have.
     */
    private List< Field< ? > > output;

    /**
     * The id.
     */
    private String id;

    /**
     * The information of elements containing comments, description and version etc.
     */
    private ElementInfo info;

    /**
     * The key.
     */
    private String key;

    /**
     * The name.
     */
    private String name;

    /**
     * can be [client,server, user-selected, null] either a workflow is running on client side or server side.
     */
    private String runMode;

    public UserWFElementImpl() {
    }

    /**
     * Equals.
     *
     * @param obj
     *         the obj
     *
     * @return true, if successful
     */
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final UserWFElementImpl other = ( UserWFElementImpl ) obj;
        return ( ( ( id == null ) && ( other.id != null ) ) || ( ( id != null ) && ( id.contentEquals( other.id ) ) ) );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getComments() {
        return comments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Field< ? > > getExceptions() {
        return exceptions;
    }

    /**
     * Gets the execution value.
     *
     * @return the execution value
     */
    @JsonIgnore
    @Override
    public int getExecutionValue() {
        int executionTime = ConstantsInteger.UNLIMITED_TIME_FOR_ELEMENT;
        if ( null != getExceptions() ) {
            for ( final Field< ? > elementField : getExceptions() ) {
                if ( elementField.getName().equals( ConstantsString.MAXIMUM_EXECUTION_TIME ) ) {
                    executionTime = ( int ) Double.parseDouble( elementField.getValue().toString() );
                    if ( executionTime < ConstantsInteger.UNLIMITED_TIME_FOR_ELEMENT ) {
                        executionTime = ConstantsInteger.UNLIMITED_TIME_FOR_ELEMENT;
                    }
                    break;
                }
            }
        }
        return executionTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Field< ? > > getFields() {
        return fields;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ElementInfo getInfo() {
        return info;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey() {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return ( name != null ) ? name.trim() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Field< ? > > getOutput() {
        return output;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRunMode() {
        return runMode;
    }

    /**
     * Gets the stop on work flow option.
     *
     * @return the stop on work flow option
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.workflow.model.UserWFElement#getStopOnWorkFlowOption()
     */
    @JsonIgnore
    @Override
    public String getStopOnWorkFlowOption() {
        String selectedStopOnErrorOption = ConstantsString.DEFAULT_VALUE_FOR_WORKFLOW_STOP_ON_ERROR;
        if ( null != getExceptions() ) {
            for ( final Field< ? > elementField : getExceptions() ) {
                if ( elementField.getName().equals( ConstantsString.STOP_ON_ERROR ) ) {
                    selectedStopOnErrorOption = elementField.getValue().toString();
                    break;
                }
            }
        }
        return selectedStopOnErrorOption;
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ( prime * result ) + ( ( id == null ) ? 0 : id.hashCode() );
        result = ( prime * result ) + ( ( runMode == null ) ? 0 : runMode.hashCode() );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setComments( String comments ) {
        this.comments = comments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDescription( String description ) {
        this.description = description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setExceptions( List< Field< ? > > exceptions ) {
        this.exceptions = exceptions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFields( List< Field< ? > > fields ) {
        this.fields = fields;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInfo( ElementInfo info ) {
        this.info = info;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setKey( String key ) {
        this.key = key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOutput( List< Field< ? > > output ) {
        this.output = output;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRunMode( String runMode ) {
        this.runMode = runMode;
    }

    /**
     * Validate exception.
     *
     * @return the notification
     */
    @Override
    public Notification validateException() {
        final Notification notif = new Notification();
        if ( getExecutionValue() == ConstantsInteger.INTEGER_VALUE_ZERO ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_NOT_EXECUTED, ConstantsInteger.INTEGER_VALUE_ZERO ) ) );
        }
        return notif;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "UserWFElementImpl [comments=" + comments + ", description=" + description + ", fields=" + fields + ", exceptions="
                + exceptions + ", output=" + output + ", id=" + id + ", info=" + info + ", key=" + key + ", name=" + name + ", runMode="
                + runMode + "]";
    }

}
