package de.soco.software.simuspace.workflow.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.DataTransferObject;
import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.ElementKeys;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldModes;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.workflow.constant.ConstantsElementProps;
import de.soco.software.simuspace.workflow.model.User;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.UserWorkFlow;
import de.soco.software.simuspace.workflow.model.WorkflowConfiguration;

/**
 * This Class contains the properties of a user Workflow
 *
 * @author M.Nasir.Farooq
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class UserWorkflowImpl extends DataTransferObject implements UserWorkFlow {

    private static final int FIRST_INDEX = 0;

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The connections.
     */
    private List< NodeEdge > edges = new ArrayList<>();

    /**
     * This is a directory to create a log file
     */
    private String directory;

    /**
     * The list of elements of user work flow.
     */
    private List< UserWFElement > nodes;

    /**
     * The id of work flow.
     */
    private String id;

    /**
     * The interactive. is a workflow is interactive. Interactive means here either user input is required or not it returns true if a
     * workflow in interactive and false if not
     */
    private boolean interactive;

    /**
     * The name.
     */
    private String name;

    /**
     * The owner.
     */
    private User owner;

    /**
     * {@inheritDoc}
     */
    @Override
    public void addElement( UserWFElement element ) {
        if ( nodes == null ) {
            nodes = new ArrayList<>();
        }
        nodes.add( element );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDirectory() {
        return directory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< NodeEdge > getEdges() {
        return edges;
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
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings( "unchecked" )
    public List< UserWFElement > getNodes() {
        ArrayList< UserWFElement > newArrayList = new ArrayList<>();
        if ( nodes != null ) {
            newArrayList = ( ArrayList< UserWFElement > ) ( ( ArrayList< UserWFElement > ) nodes ).clone();
        }

        return newArrayList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getOwner() {
        return owner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInteractive() {
        return interactive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDirectory( String directory ) {
        this.directory = directory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEdges( List< NodeEdge > connections ) {
        edges = connections;
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
    public void setInteractive( boolean interactive ) {
        this.interactive = interactive;
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
    public void setNodes( List< UserWFElement > elements ) {
        nodes = elements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOwner( User owner ) {
        this.owner = owner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "UserWorkflowImpl [connections='" + edges + "', directory='" + directory + "', elements='" + nodes + "', id='" + id
                + "', interactive='" + interactive + "', name='" + name + "', owner='" + owner + "']";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Notification validate( WorkflowConfiguration config ) {
        final Notification notification = new Notification();

        final HashSet< String > elementNameSet = new HashSet<>();
        if ( !getNodes().isEmpty() ) {
            for ( final UserWFElement element : getNodes() ) {
                notification.addNotification( this.validateUserCommonFields( element, true, false ) );
                // to check the element name uniqueness
                if ( !elementNameSet.add( element.getName() ) ) {

                    notification.addError(
                            new Error( MessagesUtil.getMessage( WFEMessages.DUPLICATE_ELEMENT_NAME_IN_WORKFLOW, element.getName() ) ) );
                }
            }
            // now validate connections with config applicable
            if ( CollectionUtils.isNotEmpty( getEdges() ) ) {
                notification.addNotification( validateConnections( getEdges(), config ) );
            } else {
                setEdges( List.of( new NodeEdge( new ElementConnectionImpl( getNodes().get( FIRST_INDEX ).getId(), "" ) ) ) );
            }
        } else {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.NO_ELEMENT_IN_WORKFLOW ) ) );
        }
        return notification;

    }

    /**
     * validateConnections. this method is specifically written to validate Connections. A WorkFlow can contain a list of connections
     * Connections are connection contains two keys., source and target.
     *
     * @param edges
     *         the edges
     * @param config
     *         the config
     *
     * @return the notification
     */
    private Notification validateConnections( List< NodeEdge > edges, WorkflowConfiguration config ) {
        final Notification notif = new Notification();
        final HashMap< String, String > elementMap = new HashMap<>();
        final Set< String > connectionSet = new HashSet<>();

        for ( final NodeEdge elementConn : edges ) {
            connectionSet.add( elementConn.getData().getSource() );
            connectionSet.add( elementConn.getData().getTarget() );
        }

        // check for the Node with no connection
        for ( final UserWFElement userWFElement : getNodes() ) {
            elementMap.put( userWFElement.getId(), userWFElement.getKey() );
            if ( !connectionSet.contains( userWFElement.getId() ) ) {
                notif.addError(
                        new Error( MessagesUtil.getMessage( WFEMessages.NO_CONNECTION_INFO_FOR_ELEMENT, userWFElement.getName() ) ) );
            }

        }

        // if there are no element connections provided it will create a notification for use

        for ( final NodeEdge elementConn : edges ) {
            // get target

            String targetElementkey = null;
            String sourceElementKey = null;
            if ( ( elementMap.get( elementConn.getData().getTarget() ) != null ) && ( elementMap.get( elementConn.getData().getSource() )
                    != null ) ) {
                targetElementkey = elementMap.get( elementConn.getData().getTarget() );
                sourceElementKey = elementMap.get( elementConn.getData().getSource() );
            } else {
                // not valid ids
                if ( elementMap.get( elementConn.getData().getTarget() ) == null ) {

                    notif.addError( new Error(
                            MessagesUtil.getMessage( WFEMessages.TARGET_CONNECTION_NOT_VALID, elementConn.getData().getTarget() ) ) );
                } else {

                    notif.addError( new Error(
                            MessagesUtil.getMessage( WFEMessages.SOURCE_CONNECTION_NOT_VALID, elementConn.getData().getSource() ) ) );
                }
            }
            // get applicable by target key
            if ( config.getApplicable().get( sourceElementKey ) != null ) {
                final List< String > valideSourceList = config.getApplicable().get( sourceElementKey );
                if ( !CollectionUtil.isEmpty( valideSourceList ) && !valideSourceList.contains( targetElementkey ) && !isLoopElementKey(
                        targetElementkey ) ) {
                    notif.addError( new Error(
                            MessagesUtil.getMessage( WFEMessages.CONNECTION_NOT_APPLICABLE, targetElementkey, sourceElementKey ) ) );
                }
            }
        }

        return notif;

    }

    /**
     * Checks if element key is loop element
     *
     * @param elementkey
     *         the elementkey
     *
     * @return the boolean
     */
    private boolean isLoopElementKey( String elementkey ) {
        return ElementKeys.WFE_FOREACHLOOP.getKey().equals( elementkey ) || ElementKeys.WFE_WHILELOOP.getKey().equals( elementkey )
                || ElementKeys.WFE_UNTILLOOP.getKey().equals( elementkey );
    }

    /**
     * {@inheritDoc}
     *
     * @param element
     *         the User WorkFlow element
     *
     * @return throws exception if any field is invalid, else returns
     */
    @Override
    public Notification validateUserCommonFields( UserWFElement element, boolean runtimeValidation, boolean isImportWorkflow ) {

        final Notification notif = new Notification();

        notif.addNotification( StringUtils.validateFieldAndLength( element.getName(),
                ConstantsElementProps.NAME + ConstantsString.STANDARD_SEPARATOR + element.getName(), ConstantsLength.STANDARD_NAME_LENGTH,
                false, false ) );
        notif.addNotification(
                StringUtils.validateFieldAndLength( element.getKey(), ConstantsElementProps.KEY, ConstantsLength.STANDARD_NAME_LENGTH,
                        false, false ) );
        notif.addNotification( StringUtils.validateFieldAndLength( element.getComments(), ConstantsElementProps.COMMENTS,
                ConstantsLength.STANDARD_COMMENT_LENGTH, true, false ) );
        notif.addNotification( StringUtils.validateFieldAndLength( element.getDescription(), ConstantsElementProps.DESCRIPTION,
                ConstantsLength.STANDARD_DESCRIPTION_LENGTH, true, false ) );
        if ( element.getInfo() != null ) {
            notif.addNotification( StringUtils.validateFieldAndLength( element.getInfo().getVersion(), ConstantsElementProps.VERSION,
                    ConstantsLength.STANDARD_NAME_LENGTH, false, false ) );
        }

        // to check Field Names to be unique with in element
        final HashSet< String > fieldNameset = new HashSet<>();
        if ( element.getFields() != null ) {
            for ( final Field< ? > field : element.getFields() ) {

                if ( !isImportWorkflow ) {
                    notif.addNotification( field.validateField() );

                } else {
                    // do not validate file paths on import workflow
                    if ( !field.getType().contains( FieldTypes.FILE.getType() ) && !field.getType()
                            .contains( FieldTypes.DIRECTORY.getType() ) ) {
                        notif.addNotification( field.validateField() );
                    }
                }

                if ( field.getMode().equals( FieldModes.USER.getType() ) ) {

                    notif.addNotification( StringUtils.validateFieldAndLength( field.getName(),
                            ConstantsElementProps.NAME + ConstantsString.STANDARD_SEPARATOR + field.getName(),
                            ConstantsLength.STANDARD_NAME_LENGTH, false, false ) );

                }

                if ( !fieldNameset.add( field.getName() ) ) {

                    notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.DUPLICATE_FIELD_IN_WORKFLOW_ELEMENT ) ) );

                }
            }
        }

        if ( element.getExceptions() != null ) {
            for ( final Field< ? > field : element.getExceptions() ) {
                if ( field.getName().equals( ConstantsString.MAXIMUM_EXECUTION_TIME ) ) {
                    try {
                        Double.parseDouble( field.getValue().toString() );
                    } catch ( final NumberFormatException ex ) {
                        notif.addError( new Error(
                                MessagesUtil.getMessage( WFEMessages.ELEMENT_EXECUTION_TIME_IS_NOT_VALID, element.getName() ) ) );
                    }

                }

            }

        }

        return notif;
    }

}