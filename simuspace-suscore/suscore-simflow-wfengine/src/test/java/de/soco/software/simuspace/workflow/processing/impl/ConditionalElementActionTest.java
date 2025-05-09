/*
 *
 */

package de.soco.software.simuspace.workflow.processing.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.workflow.exception.UserWFParseException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.UserWFElementImpl;

/**
 * This is test class for {@link ConditionalElementAction } will test public function with valid and invalid expressions.
 *
 * @author Nasir.farooq
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( ConditionalElementAction.class )
public class ConditionalElementActionTest {

    /**
     * The Constant parameters.
     */
    private final static Map< String, Object > parameters = new HashMap<>();

    /**
     * The Constant CONDITION_TEST_FILE.
     */
    private final static String CONDITION_TEST_FILE = "src/test/resources/conditional_test.txt";

    /**
     * The Constant INPUT_FILE mapping pattern.
     */
    private static final String INPUT_FILE = "{{Input.file}}";

    /**
     * The Constant INPUT_TEXT mapping pattern.
     */
    private static final String INPUT_TEXT = "{{Input.text}}";

    /**
     * The Constant soco for test String input.
     */
    private static final String soco = "soco";

    /**
     * The Constant expressionStr to test and evaluate condition.
     */
    private static final String expressionStr = "exists('{{Input.file}}') && isFile('{{Input.file}}') && !isDirectory('{{Input.file}}') AND contains('{{Input.file}}','{{Input.text}}')";

    /**
     * The Constant trueExpressionStr to test and evaluate condition.
     */
    private static final String trueExpressionStr = "1 === 1";

    /**
     * The Constant logicalOpExpressionStr to test and evaluate logicalo operators
     */
    private static final String logicalOpExpressionStr = "2 > 1 && 2 < 3";

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * This method set up the commands and parameters for user work flow. This method executes before class execution
     *
     * @throws JsonSerializationException
     * @throws UserWFParseException
     * @throws FileNotFoundException
     */
    @BeforeClass
    public static void setup() throws FileNotFoundException, UserWFParseException, JsonSerializationException {

        final EngineFile file = new EngineFile();
        file.setPath( CONDITION_TEST_FILE );

        parameters.put( INPUT_FILE, file );
        parameters.put( INPUT_TEXT, soco );
    }

    /**
     * The work flow element action.
     */
    private ConditionalElementAction conditionalElementAction;

    /**
     * The user WF element reference .
     */
    private UserWFElement userWFElemen;

    /**
     * The expected exception from message.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Prepare job impl.
     *
     * @return the job
     */
    public Job prepareJobImpl() {
        final Job job = new JobImpl();
        job.setId( UUID.randomUUID() );
        return job;
    }

    /**
     * Should add error in notification when invalid expression.
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void shouldAddErrorInNotificationWhenInvalidExpression() throws JsonSerializationException {
        userWFElemen = new UserWFElementImpl();
        conditionalElementAction = new ConditionalElementAction( prepareJobImpl(), userWFElemen, parameters, null, null, null );

        final List< Field< ? > > fields = new ArrayList<>();
        final Field< String > expression = new Field<>();
        expression.setType( FieldTypes.TEXT.getType() );
        expression.setValue( trueExpressionStr );
        fields.add( expression );
        userWFElemen.setFields( fields );

        conditionalElementAction.doAction();
        final Notification notification = new Notification();
        conditionalElementAction.evaluateExpression( notification );
        assertFalse( notification.getErrors().isEmpty() );
        assertEquals( MessagesUtil.getMessage( WFEMessages.MESSAGE_PROVIDED_EXPRESSION_IS_INVALID, trueExpressionStr ),
                notification.getErrors().get( FIRST_INDEX ).getMessage() );

    }

    /**
     * Should add error in notification when invalid functions in expression.
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void shouldAddErrorInNotificationWhenInvalidFunctionsInExpression() throws JsonSerializationException {

        userWFElemen = new UserWFElementImpl();
        conditionalElementAction = new ConditionalElementAction( prepareJobImpl(), userWFElemen, parameters, null, null, null );
        final String inValidExpression = "inValidFunction('src/test/resources/conditional_test.txt')";
        final List< Field< ? > > fields = new ArrayList<>();
        final Field< String > expression = new Field<>();
        expression.setType( FieldTypes.TEXT.getType() );
        expression.setValue( inValidExpression );
        fields.add( expression );
        userWFElemen.setFields( fields );

        conditionalElementAction.doAction();
        final Notification notification = new Notification();
        conditionalElementAction.evaluateExpression( notification );
        assertFalse( notification.getErrors().isEmpty() );
        assertTrue( notification.getErrors().get( FIRST_INDEX ).getMessage()
                .endsWith( MessagesUtil.getMessage( WFEMessages.MESSAGE_IN_VALID_CUSTOM_METHOD_SUFIX ) ) );

    }

    /**
     * Replaces the parameter variables and then evaluates the given expression having custom functions along with common conditional
     * expression.
     *
     * @throws JsonSerializationException
     */
    @Test
    public void shouldWorkCustomExistsFunctionCominedWithBasicExpressions() throws JsonSerializationException {

        userWFElemen = new UserWFElementImpl();
        conditionalElementAction = new ConditionalElementAction( prepareJobImpl(), userWFElemen, parameters, null, null, null );
        final String expressionStr = "exists('{{Input.file}}') && 3 > 1";
        final List< Field< ? > > fields = new ArrayList<>();
        final Field< String > expression = new Field<>();
        expression.setType( FieldTypes.TEXT.getType() );
        expression.setValue( expressionStr );
        fields.add( expression );
        userWFElemen.setFields( fields );
        conditionalElementAction.doAction();
        final Notification notification = new Notification();
        final boolean resultTrue = conditionalElementAction.evaluateExpression( notification );
        assertTrue( notification.getErrors().isEmpty() );
        assertTrue( resultTrue );

    }

    /**
     * Replaces the parameter variables and then evaluates the given expression having custom functions in it.
     *
     * @throws JsonSerializationException
     */
    @Test
    public void shouldWorkCutsomFunctionExistsUsedInExpressionWithValidPath() throws JsonSerializationException {

        userWFElemen = new UserWFElementImpl();
        conditionalElementAction = new ConditionalElementAction( prepareJobImpl(), userWFElemen, parameters, null, null, null );
        final String expressionStr = "exists('{{Input.file}}')";
        final List< Field< ? > > fields = new ArrayList<>();
        final Field< String > expression = new Field<>();
        expression.setType( FieldTypes.TEXT.getType() );
        expression.setValue( expressionStr );
        fields.add( expression );
        userWFElemen.setFields( fields );
        conditionalElementAction.doAction();
        final Notification notification = new Notification();
        final boolean resultTrue = conditionalElementAction.evaluateExpression( notification );
        assertTrue( notification.getErrors().isEmpty() );
        assertTrue( resultTrue );

    }

    /**
     * This will test all custom functions in expression like exists(), isFile(), isDircectory() and contains('filePath','text') functions.
     *
     * @throws JsonSerializationException
     */
    @Test
    public void shouldWorkCutsomFunctionsInExpressionForFiles() throws JsonSerializationException {

        userWFElemen = new UserWFElementImpl();
        conditionalElementAction = new ConditionalElementAction( prepareJobImpl(), userWFElemen, parameters, null, null, null );

        final List< Field< ? > > fields = new ArrayList<>();
        final Field< String > expression = new Field<>();
        expression.setType( FieldTypes.TEXT.getType() );
        expression.setValue( expressionStr );
        fields.add( expression );
        userWFElemen.setFields( fields );
        conditionalElementAction.doAction();

        final Notification notification = new Notification();
        final boolean resultTrue = conditionalElementAction.evaluateExpression( notification );
        assertTrue( notification.getErrors().isEmpty() );
        assertTrue( resultTrue );
    }

    /**
     * Should work with basic operational expression.
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void shouldWorkWithBasicOperationalExpression() throws JsonSerializationException {

        userWFElemen = new UserWFElementImpl();
        conditionalElementAction = new ConditionalElementAction( prepareJobImpl(), userWFElemen, parameters, null, null, null );

        final List< Field< ? > > fields = new ArrayList<>();
        final Field< String > expression = new Field<>();
        expression.setType( FieldTypes.TEXT.getType() );
        expression.setValue( logicalOpExpressionStr );
        fields.add( expression );
        userWFElemen.setFields( fields );
        conditionalElementAction.doAction();
        final Notification notification = new Notification();
        final boolean resultTrue = conditionalElementAction.evaluateExpression( notification );
        assertTrue( notification.getErrors().isEmpty() );
        assertTrue( resultTrue );

    }

}
