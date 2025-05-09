package de.soco.software.simuspace.workflow.model.impl;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class BindVisibility implements Serializable {

    private static final long serialVersionUID = 9036771566786324237L;

    /**
     * The name.
     */
    private String name;

    /**
     * The value.
     */
    private List< BindValue > values;

    private String operation;

    public BindVisibility() {
        super();
    }

    public BindVisibility( String name, List< BindValue > values ) {
        super();
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public String getOperation() {
        return operation;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public List< BindValue > getValues() {
        return values;
    }

    public void setValues( List< BindValue > values ) {
        this.values = values;
    }

    public void setOperation( String operation ) {
        this.operation = operation;
    }

}
