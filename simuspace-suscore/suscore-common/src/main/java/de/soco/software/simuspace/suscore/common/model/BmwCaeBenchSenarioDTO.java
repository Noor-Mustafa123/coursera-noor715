package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class BmwCaeBenchDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class BmwCaeBenchSenarioDTO extends BmwCaeBenchDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 7856221825118191385L;

    /**
     * The label.
     */
    @UIFormField( name = "label", title = "3000169x4", orderNum = 4 )
    @UIColumn( data = "label", filter = "text", renderer = "text", title = "3000169x4", name = "label", orderNum = 4 )
    private String label;

    /**
     * The type.
     */
    @UIFormField( name = "type", title = "3000173x4", orderNum = 5 )
    @UIColumn( data = "type", filter = "text", renderer = "text", title = "3000173x4", name = "type", orderNum = 5 )
    private String type;

    @UIFormField( name = "simulationType", title = "3000194x4", orderNum = 7 )
    @UIColumn( data = "simulationType", filter = "text", renderer = "text", title = "3000194x4", name = "simulationType", orderNum = 7 )
    private String simulationType;

    @UIFormField( name = "disciplineContext", title = "3000193x4", orderNum = 10 )
    @UIColumn( data = "disciplineContext", filter = "text", renderer = "text", title = "3000193x4", name = "disciplineContext", orderNum = 10 )
    private String disciplineContext;

    /**
     * Instantiates a new bmw cae bench senario DTO.
     */
    public BmwCaeBenchSenarioDTO() {
        super();
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label.
     *
     * @param label
     *         the new label
     */
    public void setLabel( String label ) {
        this.label = label;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
    }

    public String getDisciplineContext() {
        return disciplineContext;
    }

    public void setDisciplineContext( String disciplineContext ) {
        this.disciplineContext = disciplineContext;
    }

    public String getSimulationType() {
        return simulationType;
    }

    public void setSimulationType( String simulationType ) {
        this.simulationType = simulationType;
    }

}
