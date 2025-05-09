package de.soco.software.simuspace.wizards.model.QADyna;

import java.util.List;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.wizards.model.run.LoadcaseWFModel;

/**
 * The type Qa dyna input review form.
 */
public class QADynaInputForm {

    /**
     * The Html name.
     */
    private String htmlName;

    /**
     * The Combinations.
     */
    private List< QADynaCombinationDTO > combinations;

    @UIFormField( name = "qaDynaWorkflow.id", title = "9800014x4", orderNum = 8, type = "select", multiple = false )
    @UIColumn( data = "qaDynaWorkflow.id", name = "qaDynaWorkflow", filter = "text", renderer = "text", title = "9800014x4", orderNum = 8, type = "select" )
    private LoadcaseWFModel qaDynaWorkflow;

    /**
     * Gets html name.
     *
     * @return the html name
     */
    public String getHtmlName() {
        return htmlName;
    }

    /**
     * Sets html name.
     *
     * @param htmlName
     *         the html name
     */
    public void setHtmlName( String htmlName ) {
        this.htmlName = htmlName;
    }

    /**
     * Gets combinations.
     *
     * @return the combinations
     */
    public List< QADynaCombinationDTO > getCombinations() {
        return combinations;
    }

    /**
     * Sets combinations.
     *
     * @param combinations
     *         the combinations
     */
    public void setCombinations( List< QADynaCombinationDTO > combinations ) {
        this.combinations = combinations;
    }

    /**
     * Gets qa dyna workflow.
     *
     * @return the qa dyna workflow
     */
    public LoadcaseWFModel getQaDynaWorkflow() {
        return qaDynaWorkflow;
    }

    /**
     * Sets qa dyna workflow.
     *
     * @param qaDynaWorkflow
     *         the qa dyna workflow
     */
    public void setQaDynaWorkflow( LoadcaseWFModel qaDynaWorkflow ) {
        this.qaDynaWorkflow = qaDynaWorkflow;
    }

}
