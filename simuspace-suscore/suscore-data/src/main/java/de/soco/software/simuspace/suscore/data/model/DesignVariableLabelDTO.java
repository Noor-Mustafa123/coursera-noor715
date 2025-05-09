package de.soco.software.simuspace.suscore.data.model;

import java.util.List;

public class DesignVariableLabelDTO {

    private List< LabelDTO > labels;

    private String expression;

    public List< LabelDTO > getLabels() {
        return labels;
    }

    public void setLabels( List< LabelDTO > labels ) {
        this.labels = labels;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression( String expression ) {
        this.expression = expression;
    }

}
