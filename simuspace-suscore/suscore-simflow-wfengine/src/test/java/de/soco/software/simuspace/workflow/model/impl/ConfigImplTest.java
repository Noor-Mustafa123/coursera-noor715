package de.soco.software.simuspace.workflow.model.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.workflow.constant.ConstantsElementKey;
import de.soco.software.simuspace.workflow.model.WorkflowConfiguration;

/*** This Class contains test cases that test all the possible scenarios in configuration validation. */

@RunWith( PowerMockRunner.class )
@PrepareForTest( WorkflowConfigurationImpl.class )
public class ConfigImplTest {

    /**
     * The Constant INDEX_0 for configuration setting.
     */
    private static final int INDEX_0 = 0;

    /**
     * The Constant INDEX_1 for configuration setting.
     */
    private static final int INDEX_1 = 1;

    /**
     * The Constant INDEX_2 for configuration setting.
     */
    private static final int INDEX_2 = 2;

    /**
     * The Constant OUTPUT.
     */
    private static final String OUTPUT = "Output";

    /**
     * The Constant DUMMY_STRING.
     */
    private static final String DUMMY_STRING = "dummy";

    /**
     * The Constant DATA.
     */
    private static final String DATA = "data";

    /**
     * The Constant WORKFLOW_CONFIG_FILE_PATH to get configuration.
     */
    private static final String WORKFLOW_CONFIG_FILE_PATH = "/WF_Config_Test.js";

    /**
     * The configuration object for validation of user work flow.
     */
    private WorkflowConfiguration config;

    /**
     * This method reads a configuration before any method of class is called. and makes config object.
     *
     * @throws SusException
     *         the simuspace exception
     */
    @Before
    public void setup() throws SusException {
        config = JsonUtils.jsonStreamToObject( this.getClass().getResourceAsStream( WORKFLOW_CONFIG_FILE_PATH ),
                WorkflowConfiguration.class );
        final Map< String, List< String > > applicable = new HashMap<>();
        for ( final WorkflowConfigElements elements : config.getElements() ) {
            applicable.put( elements.getElement().getData().getKey(), elements.getElement().getData().getAllowedConnections() );

        }
        config.setApplicable( applicable );
    }

    /**
     * This method checks that the allowed field keys must contain Input element field type.
     */
    @Test
    public void whenAllowedFieldsNotContainsFieldTypeItShouldHaveErrorInvalidFieldType() {
        boolean error = false;
        final List< String > allowedFieldKeys = new ArrayList<>();
        allowedFieldKeys.add( "data" );
        config.getElements().get( INDEX_0 ).getElement().getData().setKey( ConstantsElementKey.WFE_IO );
        config.getElements().get( INDEX_0 ).getElement().getData().setAllowedFieldKeys( allowedFieldKeys );

        final Notification notif = config.validate();

        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains( MessagesUtil.getMessage( WFEMessages.INVALID_FIELD_TYPE, DATA ) ) ) {
                error = true;
            }
        }

        assertTrue( error );
    }

    /**
     * This method checks that configuration must have applicable keys
     */
    @Test
    public void whenConfigurationHaveInvalidApplicableKeyItShouldHaveErrorInvalidAplicableKey() {
        boolean error = false;
        config.getApplicable().put( DUMMY_STRING, new ArrayList< String >() );

        final Notification notif = config.validate();

        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains( MessagesUtil.getMessage( WFEMessages.INVALID_APPLICABLE_KEY, DUMMY_STRING ) ) ) {
                error = true;
            }
        }

        assertTrue( error );
    }

    /**
     * This method checks that the Applicable value must contain WorkFlow values i.e (wfe_input, wfe_output, wfe_script).
     */
    @Test
    public void whenConfigurationHaveInvalidApplicableValueItShouldHaveErrorInvalidAplicableValue() {
        boolean error = false;
        final List< String > values = new ArrayList<>();
        values.add( DUMMY_STRING );
        config.getApplicable().put( ConstantsElementKey.WFE_IO, values );

        final Notification notif = config.validate();

        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains( MessagesUtil.getMessage( WFEMessages.INVALID_APPLICABLE_VALUE, DUMMY_STRING ) ) ) {
                error = true;
            }
        }

        assertTrue( error );
    }

    /**
     * When it validate successfully it should not return error.
     */
    @Test
    public void whenConfigurationValidateSuccessfullyItShoudNotReturnError() {
        final Notification notif = config.validate();

        assertNotNull( notif );
        assertFalse( notif.hasErrors() );
    }

    /**
     * This method checks field key is not valid.
     */
    @Test
    public void whenElementFieldKeyIsInvalidItShouldHaveErrorFieldKeyNotAllowed() {
        boolean error = false;
        config.getElements().get( INDEX_1 ).getElement().getData().getFields().get( INDEX_0 ).setType( DATA );

        final Notification notif = config.validate();

        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains( MessagesUtil.getMessage( WFEMessages.FILED_KEY_NOT_ALLOWED, DATA ) ) ) {
                error = true;
            }
        }

        assertTrue( error );
    }

    /**
     * This method checks that the mode of field is user and field type is from allowed keys of elements.
     */
    @Test
    public void whenElementFieldModeIsUserAndElementFieldTypeIsNotFromAllowedFieldKeysItShouldReturnFieldNotAllowedMessage() {
        boolean error = false;
        config.getElements().get( INDEX_1 ).getElement().getData().getFields().get( INDEX_0 ).setMode( "user" );
        config.getElements().get( INDEX_1 ).getElement().getData().getFields().get( INDEX_0 ).setType( DATA );

        final Notification notif = config.validate();

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains( MessagesUtil.getMessage( WFEMessages.FIELD_NOT_ALLOWED, DATA ) ) ) {
                error = true;
            }
        }
        assertTrue( error );
    }

    /**
     * This method checks no duplication of fields of Elements in WF_Config.js file.
     */

    @Test
    public void whenElementFieldNameIsDuplicatedItShoudHaveErrorDuplicatedField() {
        boolean error = false;
        config.getElements().get( INDEX_2 ).getElement().getData().getFields().get( INDEX_0 ).setName( "textarea" );
        config.getElements().get( INDEX_2 ).getElement().getData().getFields().get( INDEX_1 ).setName( "textarea" );

        final Notification notif = config.validate();

        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains( MessagesUtil.getMessage( WFEMessages.DUPLICATE_FIELD, "textarea",
                    config.getElements().get( INDEX_2 ).getElement().getData().getName() ) ) ) {
                error = true;
            }
        }

        assertTrue( error );
    }

    /**
     * This method checks that the Input elements must not contains null allowed Field keys.
     */
    @Test
    public void whenElementIsInputAndAllowedFieldsAreNullItShouldHaveErrorAllowedKeysCantBeNull() {
        boolean error = false;
        config.getElements().get( INDEX_0 ).getElement().getData().setKey( ConstantsElementKey.WFE_IO );
        config.getElements().get( INDEX_0 ).getElement().getData().setAllowedFieldKeys( null );

        final Notification notif = config.validate();

        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains( MessagesUtil.getMessage( WFEMessages.ALLOWED_KEYS_CANT_BE_NULL ) ) ) {
                error = true;
            }
        }
        assertTrue( error );
    }

    /**
     * This method checks that the elements key must not be null.
     */
    @Test
    public void whenElementKeyIsNullItShouldHaveErrorElementKeyCannotBeEmpty() {
        boolean error = false;
        config.getElements().get( INDEX_0 ).getElement().getData().setKey( null );

        final Notification notif = config.validate();

        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains( MessagesUtil.getMessage( WFEMessages.EMPTY_ELEMENT_KEY ) ) ) {
                error = true;
            }
        }

        assertTrue( error );
    }

    /**
     * This method checks no duplication of Elements in WF_Config.js file.
     */
    @Test
    public void whenElementsNameIsDuplicatedItShoudHaveErrorDuplicateElementName() {
        boolean error = false;
        config.getElements().get( INDEX_0 ).getElement().getData().setName( OUTPUT );
        config.getElements().get( INDEX_1 ).getElement().getData().setName( OUTPUT );

        final Notification notif = config.validate();

        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains( MessagesUtil.getMessage( WFEMessages.DUPLICATE_ELEMENT_NAME, OUTPUT ) ) ) {
                error = true;
            }
        }

        assertTrue( error );
    }

    /**
     * This method checks that the script elements must contain a valid runMode i.e (client, server, user-selected).
     */
    @Test
    public void whenRunModeOfScriptElementIsInvalidItShouldHaveErrorInvalidRunMode() {
        boolean error = false;
        config.getElements().get( INDEX_0 ).getElement().getData().setKey( ConstantsElementKey.WFE_SCRIPT );
        config.getElements().get( INDEX_0 ).getElement().getData().setRunMode( "remote" );

        final Notification notif = config.validate();

        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains( MessagesUtil.getMessage( WFEMessages.INVALID_RUN_MODE, "remote" ) ) ) {
                error = true;
            }
        }

        assertTrue( error );
    }

    /**
     * This method checks that the script elements must contain runMode i.e (client, server, user-selected).
     */
    @Test
    public void whenRunModeOfScriptElementIsNullItShouldHaveErrorRunModeIsEmpty() {
        boolean error = false;
        config.getElements().get( INDEX_0 ).getElement().getData().setKey( ConstantsElementKey.WFE_SCRIPT );
        config.getElements().get( INDEX_0 ).getElement().getData().setRunMode( null );

        final Notification notif = config.validate();

        assertNotNull( notif );

        for ( final Error err : notif.getErrors() ) {
            assertNotNull( err.getMessage() );
            if ( err.getMessage().contains( MessagesUtil.getMessage( WFEMessages.EMPTY_RUN_MODE ) ) ) {
                error = true;
            }
        }

        assertTrue( error );
    }

}