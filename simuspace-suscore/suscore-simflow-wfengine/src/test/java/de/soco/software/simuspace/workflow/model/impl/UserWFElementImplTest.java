/*
 *
 */

package de.soco.software.simuspace.workflow.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;

/**
 * The Class UserWFElementImplTest for testing {@link UserWFElementImpl} methods.
 *
 * @author zeeshan jamal
 */
public class UserWFElementImplTest {

    /**
     * The job impl.
     */
    private UserWFElementImpl userWFElementImpl;

    /**
     * Prepare exceptions.
     *
     * @return
     */
    private List< Field< ? > > prepareField() {
        final List< Field< ? > > fields = new ArrayList<>();
        final Field< String > stopOnWorkflowField = new Field<>();
        stopOnWorkflowField.setName( ConstantsString.STOP_ON_ERROR );
        stopOnWorkflowField.setValue( ConstantsString.DEFAULT_VALUE_FOR_WORKFLOW_STOP_ON_ERROR );
        fields.add( stopOnWorkflowField );

        final Field< Integer > executionTimeField = new Field<>();
        executionTimeField.setName( ConstantsString.MAXIMUM_EXECUTION_TIME );
        executionTimeField.setValue( ConstantsInteger.UNLIMITED_TIME_FOR_ELEMENT );
        fields.add( executionTimeField );
        return fields;
    }

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        userWFElementImpl = new UserWFElementImpl();
        userWFElementImpl.setExceptions( prepareField() );

    }

    /**
     * Should successfully return default stop on error option when valid input is provided.
     */
    @Test
    public void shouldSuccessfullyReturnDefaultStopOnErrorOptionWhenValidInputIsProvided() {

        final String expected = ConstantsString.DEFAULT_VALUE_FOR_WORKFLOW_STOP_ON_ERROR;
        final String actual = userWFElementImpl.getStopOnWorkFlowOption();
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should successfully return element execution time when valid input is provided.
     */
    @Test
    public void shouldSuccessfullyReturnElementExecutionTimeWhenValidInputIsProvided() {
        final int expected = ConstantsInteger.UNLIMITED_TIME_FOR_ELEMENT;
        final int actual = userWFElementImpl.getExecutionValue();
        Assert.assertEquals( expected, actual );
    }

}
