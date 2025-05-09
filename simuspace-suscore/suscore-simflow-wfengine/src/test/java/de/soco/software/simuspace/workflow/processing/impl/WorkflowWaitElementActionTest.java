package de.soco.software.simuspace.workflow.processing.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.workflow.exception.WorkFlowExecutionException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.UserWFElementImpl;

/**
 * This Class designed to test and execute a wait element.
 */
@PrepareForTest( WorkflowWaitElementAction.class )
public class WorkflowWaitElementActionTest {

    /**
     * The job impl.
     */
    private static Job jobImpl;

    /**
     * The incoming and outgoing parameters of work flow elements.
     */
    private final static Map< String, Object > parameters = new HashMap<>();

    /**
     * The test lock file.
     */
    private final static String FILE = "copy.txt";

    /**
     * The Constant LOCKED_FILE.
     */
    private final static String LOCKED_FILE = getFile();

    private static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    /**
     * Gets the file.
     *
     * @return the file
     */
    private static String getFile() {
        final File pathToResourcesFolder = new File( WorkflowWaitElementActionTest.class.getClassLoader().getResource( FILE ).getFile() );
        return pathToResourcesFolder.toString();
    }

    /**
     * The date.
     */
    private final String DATE = "date";

    /**
     * The date formate.
     */
    private final String DATE_FORMATE = "yyyy-MM-dd HH:mm:ss";

    /**
     * The notif.
     */
    private Notification notif = null;

    /**
     * The wait element action.
     */
    private WorkflowWaitElementAction waitElementAction;

    /**
     * The element.
     */
    private UserWFElement element;

    /**
     * The field.
     */
    private Field< String > fieldArea;

    /**
     * The field select.
     */
    private Field< String > fieldSelect;

    /**
     * The fields area.
     */
    private List< Field< ? > > fields;

    /**
     * The duration in sec.
     */
    private final String DURATION_IN_SEC = "1";

    /**
     * The exponential duration in sec.
     */
    private final String EXPONENTIAL_DURATION_IN_SEC = "1e0";

    /**
     * The invalid duration in sec.
     */
    private final String INVALID_DURATION_IN_SEC = "-3";

    /**
     * The invalid data in sec.
     */
    private final String INVALID_DATA_IN_SEC = "1es";

    /**
     * The duration in date.
     */
    private final String DURATION_IN_DATE = getCurrentDateWithSomeSecondAddition( Integer.parseInt( DURATION_IN_SEC ) );

    /**
     * The wrong date.
     */
    private final String WRONG_DATE = "2017-02-04 11:20:03";

    /**
     * The wrong date format.
     */
    private final String WRONG_DATE_FORMAT = "02-2017-55";

    /**
     * The time in sec.
     */
    private final String SELECT_TIME_IN_SEC = "time";

    /**
     * The due date to expire.
     */
    private final String DUE_DATE_TO_EXPIRE = "date";

    /**
     * The path file lock.
     */
    private final String PATH_FILE_LOCK = "path_file_lock";

    /**
     * The selection.
     */
    private final String SELECTION = "select";

    /**
     * The text.
     */
    private final String INTERGER = "integer";

    /**
     * The seconds conversion.
     */
    private final int SECONDS_CONVERSION = 1000;

    /**
     * The first index.
     */
    private final int FIRST_INDEX = 0;

    /**
     * Gets the current date with some second addition.
     *
     * @param sec
     *         the sec
     *
     * @return the current date with some second addition
     */
    private String getCurrentDateWithSomeSecondAddition( int sec ) {
        final long dateInMili = System.currentTimeMillis() + ( sec * SECONDS_CONVERSION );
        final SimpleDateFormat sdfDate = new SimpleDateFormat( ISO_8601_DATE_FORMAT );
        return sdfDate.format( dateInMili );
    }

    /**
     * Sets the elements field.
     */
    private void setElementsField( String selectType, String select, String areaType, String area ) {
        element = new UserWFElementImpl();
        setListOfField( selectType, select, areaType, area );
        element.setFields( fields );
    }

    /**
     * Sets the text area value.
     *
     * @param value
     *         the new text area value
     */
    private void setFieldArea( String areaType, String value ) {
        fieldArea = new Field<>();
        fieldArea.setType( areaType );
        fieldArea.setValue( value );
    }

    /**
     * Sets the field select.
     *
     * @param value
     *         the new field select
     */
    private void setFieldSelect( String selectType, String value ) {
        fieldSelect = new Field<>();
        fieldSelect.setType( selectType );
        fieldSelect.setValue( value );
    }

    /**
     * This method set up notification object. This method executes before each test execution
     */
    @Before
    public void setInput() {
        notif = new Notification();
    }

    /**
     * Sets the job impl.
     */
    private void setJobImpl() {
        jobImpl = new JobImpl();
        jobImpl.setId( UUID.randomUUID() );
        final de.soco.software.simuspace.workflow.model.impl.EngineFile file = new de.soco.software.simuspace.workflow.model.impl.EngineFile();
        file.setPath( this.getClass().getClassLoader().getResource( ConstantsString.EMPTY_STRING ).getFile().trim() );
        jobImpl.setWorkingDir( file );
    }

    /**
     * Sets the list of field.
     *
     * @param value
     *         the new list of field
     */
    private void setListOfField( String selectType, String select, String areaType, String area ) {
        setFieldSelect( selectType, select );
        setFieldArea( areaType, area );
        fields = new ArrayList<>();
        fields.add( fieldArea );
        fields.add( fieldSelect );
    }

    /**
     * When wait element have locked file.
     *
     * @throws WorkFlowExecutionException
     *         the work flow execution exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void whenWaitElementHaveLockedFileAchievedThenItProceed() throws WorkFlowExecutionException, JsonSerializationException {
        setJobImpl();
        setElementsField( SELECTION, PATH_FILE_LOCK, INTERGER, LOCKED_FILE );
        waitElementAction = new WorkflowWaitElementAction( jobImpl, parameters, element );
        notif = waitElementAction.doAction();
        assertFalse( notif.hasErrors() );
    }

    /**
     * When wait element have specific wait for some date but invalid seconds provided.
     *
     * @throws WorkFlowExecutionException
     *         the work flow execution exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void whenWaitElementHaveProvidedSpecificWaitForSomeDateButInvalidSecondsProvidedThenItNotifyError()
            throws WorkFlowExecutionException, JsonSerializationException {
        setJobImpl();
        setElementsField( SELECTION, SELECT_TIME_IN_SEC, INTERGER, INVALID_DURATION_IN_SEC );
        waitElementAction = new WorkflowWaitElementAction( jobImpl, parameters, element );
        notif = waitElementAction.doAction();
        assertTrue( notif.hasErrors() );
        assertEquals( notif.getErrors().get( FIRST_INDEX ).getMessage(),
                MessagesUtil.getMessage( WFEMessages.INAVLID_SECONDS_PROVIDED, INVALID_DURATION_IN_SEC ) );

    }

    /**
     * When wait element have specific wait for some date but invalid data is provided in seconds.
     *
     * @throws WorkFlowExecutionException
     *         the work flow execution exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void whenWaitElementHaveProvidedSpecificWaitForSomeSecondsButInvalidDataIsProvidedInSecondsThenItNotifyError()
            throws WorkFlowExecutionException, JsonSerializationException {
        setJobImpl();
        setElementsField( SELECTION, SELECT_TIME_IN_SEC, INTERGER, INVALID_DATA_IN_SEC );
        waitElementAction = new WorkflowWaitElementAction( jobImpl, parameters, element );
        notif = waitElementAction.doAction();
        assertTrue( notif.hasErrors() );
        assertEquals( notif.getErrors().get( FIRST_INDEX ).getMessage(),
                MessagesUtil.getMessage( WFEMessages.INAVLID_SECONDS_PROVIDED, INVALID_DATA_IN_SEC ) );

    }

    /**
     * When wait element have specific wait for some date.
     *
     * @throws WorkFlowExecutionException
     *         the work flow execution exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void whenWaitElementHaveSpecificWaitForSomeDateThenItProceed() throws WorkFlowExecutionException, JsonSerializationException {
        setJobImpl();
        setElementsField( SELECTION, SELECT_TIME_IN_SEC, INTERGER, DURATION_IN_SEC );
        waitElementAction = new WorkflowWaitElementAction( jobImpl, parameters, element );
        notif = waitElementAction.doAction();
        assertFalse( notif.hasErrors() );
    }

    /**
     * When wait element have specific wait for some time in exponential value.
     *
     * @throws WorkFlowExecutionException
     *         the work flow execution exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void whenWaitElementHaveSpecificWaitForTimeInExponentialValueThenItProceed()
            throws WorkFlowExecutionException, JsonSerializationException {
        setJobImpl();
        setElementsField( SELECTION, SELECT_TIME_IN_SEC, INTERGER, EXPONENTIAL_DURATION_IN_SEC );
        waitElementAction = new WorkflowWaitElementAction( jobImpl, parameters, element );
        notif = waitElementAction.doAction();
        assertFalse( notif.hasErrors() );
    }

    /**
     * When wait element have specific wait in due date.
     *
     * @throws WorkFlowExecutionException
     *         the work flow execution exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void whenWaitElementHaveSpecificWaitInDueDateThenItProceed() throws WorkFlowExecutionException, JsonSerializationException {
        setJobImpl();
        setElementsField( SELECTION, DUE_DATE_TO_EXPIRE, DATE, DURATION_IN_DATE );
        waitElementAction = new WorkflowWaitElementAction( jobImpl, parameters, element );
        notif = waitElementAction.doAction();
        assertFalse( notif.hasErrors() );
    }

    /**
     * When wait element have specific wait in sec but the element is null.
     *
     * @throws WorkFlowExecutionException
     *         the work flow execution exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void whenWaitElementHaveSpecificWaitInSecButTheElementIsNULL() throws WorkFlowExecutionException, JsonSerializationException {
        setJobImpl();
        waitElementAction = new WorkflowWaitElementAction( jobImpl, parameters, element );
        notif = waitElementAction.doAction();
        assertTrue( notif.hasErrors() );
        assertEquals( notif.getErrors().get( FIRST_INDEX ).getMessage(), MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) );
    }

    /**
     * When wait element have specific wait in wrong due date format.
     *
     * @throws WorkFlowExecutionException
     *         the work flow execution exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void whenWaitElementHaveSpecificWaitInWrongDueDateFormatThenItNotifyError()
            throws WorkFlowExecutionException, JsonSerializationException {
        setJobImpl();
        setElementsField( SELECTION, DUE_DATE_TO_EXPIRE, DATE, WRONG_DATE_FORMAT );
        waitElementAction = new WorkflowWaitElementAction( jobImpl, parameters, element );
        notif = waitElementAction.doAction();
        assertTrue( notif.hasErrors() );
        assertEquals( MessagesUtil.getMessage( WFEMessages.PLEASE_PROVIDE_IN_MILISECONDS, DATE_FORMATE ),
                notif.getErrors().get( FIRST_INDEX ).getMessage() );
    }

    /**
     * When wait element have specific wait in wrong due date.
     *
     * @throws WorkFlowExecutionException
     *         the work flow execution exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void whenWaitElementHaveSpecificWaitInWrongDueDateThenItNotifyError()
            throws WorkFlowExecutionException, JsonSerializationException {
        setJobImpl();
        setElementsField( SELECTION, DUE_DATE_TO_EXPIRE, DATE, WRONG_DATE );
        waitElementAction = new WorkflowWaitElementAction( jobImpl, parameters, element );
        notif = waitElementAction.doAction();
        assertTrue( notif.hasErrors() );
        assertEquals( notif.getErrors().get( FIRST_INDEX ).getMessage(),
                MessagesUtil.getMessage( WFEMessages.PLEASE_PROVIDE_IN_MILISECONDS ) );
    }

}
