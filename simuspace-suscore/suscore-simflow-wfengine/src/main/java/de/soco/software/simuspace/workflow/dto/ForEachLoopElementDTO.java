package de.soco.software.simuspace.workflow.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.UserWorkFlow;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.processing.impl.WorkFlowLoopElementAction;

/**
 * The Class ForEachLoopElementDTO.
 */
public class ForEachLoopElementDTO extends WorkFlowLoopElementAction {

    /**
     * The Constant VALUE_FIELD.
     */
    private static final String VALUE = "VALUE";

    /**
     * The Constant LIST_FIELD.
     */
    private static final String LIST_FIELD_NAME = "listforeach";

    /**
     * The Constant SPLITTER_FIELD.
     */
    private static final String SPLITTER_FIELD_NAME = "splitter";

    /**
     * The Constant RUN_CONCURRENTLY_FIELD.
     */
    private static final String RUN_CONCURRENTLY_FIELD_NAME = "runConcurrently";

    /**
     * The values string.
     */
    private String valuesString;

    /**
     * The values list.
     */
    private String[] valuesList;

    /**
     * The splitter.
     */
    private String splitter;

    /**
     * The run cocurrently.
     */
    private boolean runCocurrently;

    /**
     * Instantiates a new for each loop element DTO.
     *
     * @param loopElement
     *         the loop element
     * @param userWorkFlow
     *         the user work flow
     * @param job
     *         the job
     */
    public ForEachLoopElementDTO( UserWFElement loopElement, UserWorkFlow userWorkFlow, Job job ) {
        super( job, loopElement );

        setLoopName( loopElement.getName() );

        final List< Field< ? > > elementFields = loopElement.getFields();
        for ( final Field< ? > field : elementFields ) {
            if ( field.getName().equalsIgnoreCase( LIST_FIELD_NAME ) ) {
                setValuesString( field.getValue().toString() );
            } else if ( field.getName().equalsIgnoreCase( SPLITTER_FIELD_NAME ) ) {
                setSplitter( field.getValue().toString() );
            } else if ( field.getName().equalsIgnoreCase( LOOP_START_FIELD_NAME ) ) {
                String value = field.getValue().toString().replaceAll( "^\\[|]$", "" );
                setLoopStartElementId( value );
            } else if ( field.getName().equalsIgnoreCase( LOOP_END_FIELD_NAME ) ) {
                String value = field.getValue().toString().replaceAll( "^\\[|]$", "" );
                setLoopEndElementId( value );
            } else if ( field.getName().equalsIgnoreCase( RUN_CONCURRENTLY_FIELD_NAME ) ) {
                setRunCocurrently( Boolean.parseBoolean( field.getValue().toString() ) );
            }
        }

        setLoopStartAndEndElementNames( userWorkFlow );
    }

    /**
     * Gets the values string.
     *
     * @return the values string
     */
    public String getValuesString() {
        return valuesString;
    }

    /**
     * Sets the values string.
     *
     * @param valuesString
     *         the new values string
     */
    public void setValuesString( String valuesString ) {
        this.valuesString = valuesString;
    }

    /**
     * Gets the values list.
     *
     * @return the values list
     */
    public String[] getValuesList() {
        return valuesList;
    }

    /**
     * Sets the values list.
     *
     * @param valueList
     *         the new values list
     */
    public void setValuesList( String[] valueList ) {
        this.valuesList = valueList;
    }

    /**
     * Gets the splitter.
     *
     * @return the splitter
     */
    public String getSplitter() {
        return splitter;
    }

    /**
     * Sets the splitter.
     *
     * @param splitter
     *         the new splitter
     */
    public void setSplitter( String splitter ) {
        this.splitter = splitter;
    }

    /**
     * Checks if is run cocurrently.
     *
     * @return true, if is run cocurrently
     */
    public boolean isRunCocurrently() {
        return runCocurrently;
    }

    /**
     * Sets the run cocurrently.
     *
     * @param runCocurrently
     *         the new run cocurrently
     */
    public void setRunCocurrently( boolean runCocurrently ) {
        this.runCocurrently = runCocurrently;
    }

    /**
     * Gets the value variable key.
     *
     * @return the value variable key
     */
    public String getValueVariableKey() {
        return ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + getLoopName()
                + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + VALUE
                + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES;
    }

    /**
     * Gets the list variable key.
     *
     * @return the list variable key
     */
    public String getListVariableKey() {
        return ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + getLoopName()
                + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + LIST_FIELD_NAME
                + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES;
    }

    /**
     * Gets the splitter variable key.
     *
     * @return the splitter variable key
     */
    public String getSplitterVariableKey() {
        return ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + getLoopName()
                + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + SPLITTER_FIELD_NAME
                + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES;
    }

    /**
     * Adds loop parameters to global variables so it can be used all elements.
     *
     * @param job
     *         the job
     * @param allElements
     *         the all elements
     */
    public void addLoopVariablesToJob( Job job ) {
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
        String valuesData = replaceAllVariableValuesInText( getValuesString(), new Notification() );
        setValuesString( valuesData );
        String splitterData = replaceAllVariableValuesInText( getSplitter(), new Notification() );
        setSplitter( splitterData );
        setValuesList( getValuesString().split( getSplitter() ) );
        globalVariables.put( getListVariableKey(), getValuesString() );
        globalVariables.put( getSplitterVariableKey(), getSplitter() );
        job.setGlobalVariables( globalVariables );
    }

}
