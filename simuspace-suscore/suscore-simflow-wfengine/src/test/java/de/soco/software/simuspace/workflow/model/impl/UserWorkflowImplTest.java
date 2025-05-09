package de.soco.software.simuspace.workflow.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.enums.simflow.ElementKeys;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowDefinitionDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowElement;
import de.soco.software.simuspace.workflow.model.ElementConnection;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.UserWorkFlow;
import de.soco.software.simuspace.workflow.model.WorkflowConfiguration;

/**
 * This class is specifically written to test a Work flow validations<br> It tests all the public methods of WorkflowImpl.java
 *
 * @author Ali Haider
 */
@PrepareForTest( UserWorkflowImpl.class )
public class UserWorkflowImplTest {

    /**
     * The Constant NULL_VALUE_IN_MESSAGE.
     */
    private static final String NULL_VALUE_IN_MESSAGE = "null";

    /**
     * The Constant DUMMY_INTPUT_ID.
     */
    private static final String DUMMY_INTPUT_ID = "9b020560-9c4b-11e6-99ae-f31918567553";

    /**
     * The Constant OUTPUT_ID.
     */
    private static final String DUMMY_OUTPUT_ID = "9c5fc0a0-9c4b-11e6-99ae-f31918567553";

    /**
     * The Constant SCRIPT_ID.
     */
    private static final String DUMMY_SCRIPT_ID = "9d8750b0-9c4b-11e6-99ae-f31918567553";

    /**
     * The Constant FIELD_TEXTAREA.
     */
    private static final String FIELD_TEXTAREA = "textarea";

    /**
     * The Constant INDEX_0.
     */
    private static final int INDEX_0 = 0;

    /**
     * The Constant INDEX_1.
     */
    private static final int INDEX_1 = 1;

    /**
     * The Constant INDEX_2.
     */
    private static final int INDEX_2 = 2;

    /**
     * The Constant OUTPUT.
     */
    private static final String OUTPUT = "output";

    /**
     * The Constant WORKFLOW_FILE_PATH to get work flow.
     */
    private static final String USER_WORKFLOW_FILE_PATH = "/WF_User_Test.js";

    /**
     * The Constant WORKFLOW_CONFIG_FILE_PATH to get configuration.
     */
    private static final String WORKFLOW_CONFIG_FILE_PATH = "/WF_Config_Test.js";

    /**
     * The configuration object for validation of user work flow.
     */
    private WorkflowConfiguration config;

    /**
     * The user work flow data transfer object.
     */
    private LatestWorkFlowDTO workFlowDTO;

    /**
     * It prepare userWorkflow Sets workflowDefinition , connections and elements in it. from config appended with url provided .
     *
     * @param WorkflowDTO
     *
     * @return UserWorkFlow
     */
    private UserWorkFlow prepareWorkflowModel( LatestWorkFlowDTO dto ) {

        final UserWorkFlow model = setWorkflowOwner( dto );

        String json = null;
        final Map< String, Object > map = dto.prepareDefinition();
        if ( map != null ) {
            json = JsonUtils.toJson( map );
        }
        final WorkflowDefinitionDTO workflowDefinitionDTO = JsonUtils.jsonToObject( json, WorkflowDefinitionDTO.class );

        model.setEdges( workflowDefinitionDTO.getElements().getEdges() );

        model.setNodes( setWorkflowElements( workflowDefinitionDTO ) );
        return model;
    }

    /**
     * This method reads a configuration before any method of class is called. and makes config object.
     */
    @Before
    public void setUp() {
        workFlowDTO = JsonUtils.jsonStreamToObject( this.getClass().getResourceAsStream( USER_WORKFLOW_FILE_PATH ),
                LatestWorkFlowDTO.class );
        config = JsonUtils.jsonStreamToObject( this.getClass().getResourceAsStream( WORKFLOW_CONFIG_FILE_PATH ),
                WorkflowConfiguration.class );
        final Map< String, List< String > > applicable = new HashMap<>();
        for ( final WorkflowConfigElements elements : config.getElements() ) {
            applicable.put( elements.getElement().getData().getKey(), elements.getElement().getData().getAllowedConnections() );

        }
        config.setApplicable( applicable );
    }

    /**
     * gets workflow elements from WorkflowDefinitionDTO
     *
     * @param WorkflowDefinitionDTO
     *
     * @return List<UserWFElement>
     */
    private List< UserWFElement > setWorkflowElements( WorkflowDefinitionDTO def ) {
        final List< UserWFElement > userElements = new ArrayList<>();
        for ( final WorkflowElement workflowElement : def.getElements().getNodes() ) {
            final UserWFElement element = workflowElement.getData();
            if ( ElementKeys.getkeys().contains( element.getKey() ) ) {
                userElements.add( element );
            }
        }
        return userElements;

    }

    // *************************************************************************
    //
    // Test under method addElement(element)
    //
    // *************************************************************************

    /**
     * sets workflow owner in workflow model.
     *
     * @param WorkflowDTO
     *
     * @return UserWorkFlow
     */
    private UserWorkFlow setWorkflowOwner( LatestWorkFlowDTO dto ) {
        final UserWorkFlow model = new UserWorkflowImpl();
        model.setId( dto.getId().toString() );
        model.setName( dto.getName() );

        return model;

    }

    // *************************************************************************
    //
    // Test under method validate(config)
    //
    // *************************************************************************

    /**
     * This method test if element added in the list or not.
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void shouldAddElementInTheList() throws JsonSerializationException {
        final UserWorkFlow userWorkFlow = prepareWorkflowModel( workFlowDTO );
        final int length = userWorkFlow.getNodes().size();
        final UserWFElement element = userWorkFlow.getNodes().get( INDEX_0 );

        userWorkFlow.addElement( element );

        assertNotNull( userWorkFlow.getNodes() );
        assertFalse( userWorkFlow.getNodes().isEmpty() );
        assertTrue( userWorkFlow.getNodes().size() == ( length + 1 ) );
        assertEquals( userWorkFlow.getNodes().get( length ), userWorkFlow.getNodes().get( INDEX_0 ) );
    }

    /**
     * This method checks that connections should follow the applicable rule defined in config.js.
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void whenConnectionNotFollowConfigurationRulesItShouldHaveErrorConnectionNotApplicable() throws JsonSerializationException {
        boolean error = false;
        final HashMap< String, String > elementMap = new HashMap<>();
        final UserWorkFlow userWorkFlow = prepareWorkflowModel( workFlowDTO );
        final ElementConnection connection = new ElementConnectionImpl();
        connection.setSource( DUMMY_OUTPUT_ID );
        connection.setTarget( DUMMY_INTPUT_ID );
        userWorkFlow.getEdges().get( INDEX_0 ).setData( connection );
        userWorkFlow.getEdges().get( INDEX_1 ).setData( connection );

        for ( final UserWFElement userWFElement : userWorkFlow.getNodes() ) {
            elementMap.put( userWFElement.getId(), userWFElement.getKey() );
        }

        final Notification notif = userWorkFlow.validate( config );
        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );

            if ( err.getMessage().contains( MessagesUtil.getMessage( WFEMessages.CONNECTION_NOT_APPLICABLE,
                    elementMap.get( userWorkFlow.getEdges().get( INDEX_0 ).getData().getTarget() ),
                    elementMap.get( userWorkFlow.getEdges().get( INDEX_0 ).getData().getSource() ) ) ) ) {
                error = true;
            }
        }
        assertTrue( error );
    }

    /**
     * This method checks no duplication of Elements in WF_Config.js file.
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void whenElementsAreDuplicatedItShouldReturnErrorDuplicatedElementName() throws JsonSerializationException {
        boolean error = false;
        final UserWorkFlow userWorkFlow = prepareWorkflowModel( workFlowDTO );
        userWorkFlow.getNodes().get( INDEX_0 ).setName( OUTPUT );
        userWorkFlow.getNodes().get( INDEX_1 ).setName( OUTPUT );

        final ElementConnection connection = new ElementConnectionImpl();
        connection.setSource( DUMMY_OUTPUT_ID );
        connection.setTarget( DUMMY_INTPUT_ID );
        userWorkFlow.setEdges( Arrays.asList( new NodeEdge( connection ) ) );

        final Notification notif = userWorkFlow.validate( config );
        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains( MessagesUtil.getMessage( WFEMessages.DUPLICATE_ELEMENT_NAME_IN_WORKFLOW, OUTPUT ) ) ) {
                error = true;
            }
        }
        assertTrue( error );
    }

    /**
     * This method checks that null elements are not allowed.
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void whenElementsAreNullItShouldReturnErrorWorkFlowHasNoElements() throws JsonSerializationException {
        boolean error = false;
        final UserWorkFlow userWorkFlow = prepareWorkflowModel( workFlowDTO );
        userWorkFlow.setNodes( null );

        final Notification notif = userWorkFlow.validate( config );
        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains( MessagesUtil.getMessage( WFEMessages.NO_ELEMENT_IN_WORKFLOW ) ) ) {
                error = true;
            }
        }
        assertTrue( error );
    }

    /**
     * This method checks no duplication of fields of Elements in user work flow.
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void whenFieldsAreDuplicatedInElementItShouldReturnErrorDuplicatedFieldName() throws JsonSerializationException {
        boolean error = false;
        final UserWorkFlow userWorkFlow = prepareWorkflowModel( workFlowDTO );
        userWorkFlow.getNodes().get( INDEX_2 ).getFields().get( INDEX_0 ).setName( FIELD_TEXTAREA );
        userWorkFlow.getNodes().get( INDEX_2 ).getFields().get( INDEX_1 ).setName( FIELD_TEXTAREA );

        final Notification notif = userWorkFlow.validate( config );
        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains( MessagesUtil.getMessage( WFEMessages.DUPLICATE_FIELD_IN_WORKFLOW_ELEMENT ) ) ) {
                error = true;
            }
        }
        assertTrue( error );
    }

    /**
     * This method checks that target connections cannot be empty in User Work Flow.
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void whenOutputElementKeyIsNullItShouldHaveErrorTargetConnectionNotValid() throws JsonSerializationException {
        boolean error = false;
        final UserWorkFlow userWorkFlow = prepareWorkflowModel( workFlowDTO );
        userWorkFlow.getNodes().get( INDEX_1 ).setId( DUMMY_OUTPUT_ID );

        final ElementConnection connection = new ElementConnectionImpl();
        connection.setSource( DUMMY_OUTPUT_ID );
        connection.setTarget( null );
        userWorkFlow.setEdges( Arrays.asList( new NodeEdge( connection ) ) );
        final Notification notif = userWorkFlow.validate( config );
        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains( MessagesUtil.getMessage( WFEMessages.TARGET_CONNECTION_NOT_VALID, NULL_VALUE_IN_MESSAGE ) ) ) {
                error = true;
            }
        }
        assertTrue( error );
    }

    /**
     * This method checks that source connections cannot be empty in User Work Flow.
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void whenScriptElementKeyIsNullItShouldHaveErrorSourceConnectionNotValid() throws JsonSerializationException {
        boolean error = false;
        final UserWorkFlow userWorkFlow = prepareWorkflowModel( workFlowDTO );
        userWorkFlow.getNodes().get( INDEX_2 ).setKey( null );
        userWorkFlow.getNodes().get( INDEX_2 ).setId( DUMMY_SCRIPT_ID );
        final Notification notif = userWorkFlow.validate( config );
        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains(
                    MessagesUtil.getMessage( WFEMessages.SOURCE_CONNECTION_NOT_VALID, userWorkFlow.getNodes().get( INDEX_2 ).getId() ) ) ) {
                error = true;
            }
        }
        assertTrue( error );
    }

    /**
     * This method checks that connections cannot be null in User Work Flow.
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void whenWorkFlowConnectionIsNullItShouldReturnErrorNoConnectionInformation() throws JsonSerializationException {
        boolean error = false;
        final UserWorkFlow userWorkFlow = prepareWorkflowModel( workFlowDTO );
        userWorkFlow.setEdges( new ArrayList<>() );
        userWorkFlow.setNodes( new ArrayList<>() );

        final Notification notif = userWorkFlow.validate( config );
        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            error = true;
        }
        assertTrue( error );
    }

}