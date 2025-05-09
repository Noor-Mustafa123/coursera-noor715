package de.soco.software.simuspace.workflow.dexecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.workflow.util.WorkflowOutput;

/**
 * The Class Decision class used for making decision either which element to execute after which element and true/false path after after an
 * element execution.
 */
public class DecisionObject {

    /**
     * The result. Of an element execution
     */
    private boolean result;

    /**
     * The element type.
     */
    private String elementType;

    /**
     * The element name.
     */
    private String elementName;

    /**
     * The loop start element id.
     */
    private String loopStartElementId;

    /**
     * The loop end element id.
     */
    private String loopEndElementId;

    /**
     * The true path elements.
     */
    private List< String > truePathElementIds = new ArrayList<>();

    /**
     * The false path element ids.
     */
    private List< String > falsePathElementIds = new ArrayList<>();

    /**
     * The incoming and outgoing parameters of work flow elements.
     */
    private Map< String, Object > parameters = new HashMap<>();

    /**
     * The system output.
     */
    private WorkflowOutput workflowOutput;

    /**
     * The is pass to next element.
     */
    private boolean isPassToNextElement;

    /**
     * Instantiates a new decision object.
     *
     * @param result
     *         the result
     * @param elementType
     *         the element type
     */
    public DecisionObject( boolean result, String elementType, String elementName ) {
        super();
        this.result = result;
        this.elementType = elementType;
        this.elementName = elementName;
    }

    /**
     * Instantiates a new decision object.
     *
     * @param result
     *         the result
     * @param isPassToNextElement
     *         the is pass to next element
     * @param elementType
     *         the element type
     * @param elementName
     *         the element name
     * @param parameters
     *         the parameters
     */
    public DecisionObject( boolean result, boolean isPassToNextElement, String elementType, String elementName,
            Map< String, Object > parameters ) {
        this( result, elementType, elementName );
        this.parameters = parameters;
        this.isPassToNextElement = isPassToNextElement;
    }

    /**
     * Instantiates a new decision object.
     *
     * @param result
     *         the result
     * @param elementType
     *         the element type
     * @param elementName
     *         the element name
     * @param parameters
     *         the parameters
     * @param loopStartId
     *         the loop start id
     * @param loopEndId
     *         the loop end id
     */
    public DecisionObject( boolean result, String elementType, String elementName, Map< String, Object > parameters, String loopStartId,
            String loopEndId ) {
        this( result, elementType, elementName );
        this.parameters = parameters;
        this.loopStartElementId = loopStartId;
        this.loopEndElementId = loopEndId;
    }

    /**
     * Instantiates a new decision object.
     *
     * @param result
     *         the result
     * @param elementType
     *         the element type
     * @param truePathElementIds
     *         the true path element ids
     * @param falsePathElementIds
     *         the false path element ids
     */
    public DecisionObject( boolean result, String elementType, String elementName, List< String > truePathElementIds,
            List< String > falsePathElementIds ) {
        this( result, elementType, elementName );
        this.truePathElementIds = truePathElementIds;
        this.falsePathElementIds = falsePathElementIds;
    }

    /**
     * Instantiates a new decision object.
     *
     * @param result
     *         the result
     * @param elementType
     *         the element type
     * @param parameters
     *         the parameters
     */
    public DecisionObject( boolean result, String elementType, Map< String, Object > parameters ) {
        this.result = result;
        this.elementType = elementType;
        this.parameters = parameters;

    }

    /**
     * Instantiates a new decision object.
     *
     * @param result
     *         the result
     * @param elementType
     *         the element type
     * @param parameters
     *         the parameters
     * @param workflowOutput
     *         the workflow output
     */
    public DecisionObject( boolean result, String elementType, Map< String, Object > parameters, WorkflowOutput workflowOutput ) {
        this.result = result;
        this.elementType = elementType;
        this.parameters = parameters;
        this.workflowOutput = workflowOutput;
    }

    /**
     * Instantiates a new decision object.
     *
     * @param result
     *         the result
     * @param elementType
     *         the element type
     * @param parameters
     *         the parameters
     * @param workflowOutput
     *         the workflow output
     * @param elementName
     *         the element name
     */
    public DecisionObject( boolean result, String elementType, Map< String, Object > parameters, WorkflowOutput workflowOutput,
            String elementName ) {
        this( result, elementType, elementName );
        this.parameters = parameters;
        this.workflowOutput = workflowOutput;
    }

    /**
     * Instantiates a new decision object.
     *
     * @param result
     *         the result
     * @param elementType
     *         the element type
     * @param workflowOutput
     *         the workflow output
     */
    public DecisionObject( boolean result, String elementType, WorkflowOutput workflowOutput ) {
        super();
        this.result = result;
        this.elementType = elementType;
        this.workflowOutput = workflowOutput;
    }

    /**
     * Gets the element name.
     *
     * @return the element name
     */
    public String getElementName() {
        return elementName;
    }

    /**
     * Gets the element type.
     *
     * @return the elementType
     */
    public String getElementType() {
        return elementType;
    }

    /**
     * Gets the false path element ids.
     *
     * @return the falsePathElementIds
     */
    public List< String > getFalsePathElementIds() {
        return falsePathElementIds;
    }

    /**
     * Gets the parameters.
     *
     * @return the parameters
     */
    public Map< String, Object > getParameters() {
        return parameters;
    }

    /**
     * Gets the true path element ids.
     *
     * @return the truePathElementIds
     */
    public List< String > getTruePathElementIds() {
        return truePathElementIds;
    }

    /**
     * Gets the workflow output.
     *
     * @return the workflow output
     */
    public WorkflowOutput getWorkflowOutput() {
        return workflowOutput;
    }

    /**
     * Checks if is pass to next element.
     *
     * @return true, if is pass to next element
     */
    public boolean isPassToNextElement() {
        return isPassToNextElement;
    }

    /**
     * Checks if is result.
     *
     * @return the result
     */
    public boolean isResult() {
        return result;
    }

    /**
     * Sets the element name.
     *
     * @param elementName
     *         the new element name
     */
    public void setElementName( String elementName ) {
        this.elementName = elementName;
    }

    /**
     * Sets the element type.
     *
     * @param elementType
     *         the elementType to set
     */
    public void setElementType( String elementType ) {
        this.elementType = elementType;
    }

    /**
     * Sets the false path element ids.
     *
     * @param falsePathElementIds
     *         the falsePathElementIds to set
     */
    public void setFalsePathElementIds( List< String > falsePathElementIds ) {
        this.falsePathElementIds = falsePathElementIds;
    }

    /**
     * Sets the parameters.
     *
     * @param parameters
     *         the parameters
     */
    public void setParameters( Map< String, Object > parameters ) {
        this.parameters = parameters;
    }

    /**
     * Sets the pass to next element.
     *
     * @param isPassToNextElement
     *         the new pass to next element
     */
    public void setPassToNextElement( boolean isPassToNextElement ) {
        this.isPassToNextElement = isPassToNextElement;
    }

    /**
     * Sets the result.
     *
     * @param result
     *         the result to set
     */
    public void setResult( boolean result ) {
        this.result = result;
    }

    /**
     * Sets the true path element ids.
     *
     * @param truePathElementIds
     *         the truePathElementIds to set
     */
    public void setTruePathElementIds( List< String > truePathElementIds ) {
        this.truePathElementIds = truePathElementIds;
    }

    /**
     * Sets the workflow output.
     *
     * @param workflowOutput
     *         the new workflow output
     */
    public void setWorkflowOutput( WorkflowOutput workflowOutput ) {
        this.workflowOutput = workflowOutput;
    }

    /**
     * Gets the loop start element id.
     *
     * @return the loop start element id
     */
    public String getLoopStartElementId() {
        return loopStartElementId;
    }

    /**
     * Sets the loop start element id.
     *
     * @param loopStartElementId
     *         the new loop start element id
     */
    public void setLoopStartElementId( String loopStartElementId ) {
        this.loopStartElementId = loopStartElementId;
    }

    /**
     * Gets the loop end element id.
     *
     * @return the loop end element id
     */
    public String getLoopEndElementId() {
        return loopEndElementId;
    }

    /**
     * Sets the loop end element id.
     *
     * @param loopEndElementId
     *         the new loop end element id
     */
    public void setLoopEndElementId( String loopEndElementId ) {
        this.loopEndElementId = loopEndElementId;
    }

}
