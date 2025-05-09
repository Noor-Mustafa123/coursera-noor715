package de.soco.software.simuspace.workflow.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mvel2.MVEL;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.enums.simflow.ElementKeys;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.UserWorkFlow;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.SectionWorkflow;
import de.soco.software.simuspace.workflow.processing.impl.WorkFlowLoopElementAction;

public class WhileUntilLoopElementDTO extends WorkFlowLoopElementAction {

    /**
     * The Constant CONDITION_FIELD.
     */
    private static final String CONDITION_FIELD_NAME = "condition";

    /**
     * The WHILE_LOOP_MODE.
     */
    public static final int WHILE_LOOP_MODE_KEY = 0;

    /**
     * The UNTIL_LOOP_MODE.
     */
    public static final int UNTIL_LOOP_MODE_KEY = 1;

    private String conditionField;

    private int loopMode;

    /**
     * Constructor for while and until loops dto.
     *
     * @param loopElement
     *         the loop element
     *
     * @return the WhileUntilLoopElementDTO object
     */
    public WhileUntilLoopElementDTO( UserWFElement loopElement, UserWorkFlow userWorkFlow, Job job ) {
        super( job, loopElement );

        setLoopName( loopElement.getName() );

        final List< Field< ? > > elementFields = loopElement.getFields();
        for ( final Field< ? > field : elementFields ) {
            if ( field.getName().equalsIgnoreCase( CONDITION_FIELD_NAME ) ) {
                setConditionField( field.getValue().toString() );
            } else if ( field.getName().equalsIgnoreCase( LOOP_START_FIELD_NAME ) ) {
                String value = field.getValue().toString().replaceAll( "^\\[|]$", "" );
                setLoopStartElementId( value );
            } else if ( field.getName().equalsIgnoreCase( LOOP_END_FIELD_NAME ) ) {
                String value = field.getValue().toString().replaceAll( "^\\[|]$", "" );
                setLoopEndElementId( value );
            }
        }

        setLoopStartAndEndElementNames( userWorkFlow );

        if ( loopElement.getKey().equals( ElementKeys.WFE_WHILELOOP.getKey() ) ) {
            loopMode = WHILE_LOOP_MODE_KEY;
        } else {
            loopMode = UNTIL_LOOP_MODE_KEY;
        }
    }

    /**
     * Decides whether to continue the while and until loop or not.
     *
     * @param loopNumber
     *         the loop number
     * @param jobWorkingPath
     *         the job working path
     *
     * @return the should continue loop
     */
    public boolean shouldContinueLoop( int loopNumber, String jobWorkingPath ) {
        boolean shouldContinue = false;

        try {
            String conditionData = replaceAllVariableValuesInText( getConditionField(), new Notification() );
            shouldContinue = ( boolean ) MVEL.eval( conditionData );
        } catch ( Exception e ) {
            return false;
        }

        if ( loopMode == WHILE_LOOP_MODE_KEY ) {
            return shouldContinue;
        } else {
            // since until loop is opposite of while loop, hence invert the result for until loop
            return !shouldContinue;
        }
    }

    /**
     * Adds loop parameters to global variables so it can be used all elements.
     *
     * @param loopName
     *         the loop name
     * @param value
     *         the loop value
     * @param job
     *         the job
     */
    public void addLoopVariablesToJob( Job job, List< UserWFElement > allElements ) {
        Map< String, Object > globalVariables = new HashMap<>();

        if ( job.getGlobalVariables() != null ) {
            globalVariables = job.getGlobalVariables();
        }

        globalVariables.put( getLoopStartVariableKey(), getLoopStartElementName() );
        globalVariables.put( getLoopEndVariableKey(), getLoopEndElementName() );

        if ( job.getResultParameters() != null ) {
            // Setting previous elements result parameters as global so loop elements can use it
            globalVariables.putAll( job.getResultParameters() );
        }

        parameters = globalVariables;

        globalVariables.put( getConditionVariableKey(), getConditionField() );

        job.setGlobalVariables( globalVariables );
    }

    /**
     * Adds loop iteration to jobs progress bar.
     *
     * @param job
     *         the job
     */
    public void addLoopIterationToJobProgressBar( Job job, SectionWorkflow iterationWorkFlow, int loopNumber ) {
        if ( loopNumber > 1 ) {
            job.getProgress().setTotal( job.getProgress().getTotal() + iterationWorkFlow.getNodes().size() );
        }
    }

    public String getConditionField() {
        return conditionField;
    }

    public void setConditionField( String conditionField ) {
        this.conditionField = conditionField;
    }

    public String getConditionVariableKey() {
        return ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + getLoopName()
                + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + CONDITION_FIELD_NAME
                + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES;
    }

}
