package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class BmwCaeBenchSubModelDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class BmwCaeBenchSubModelDTO extends BmwCaeBenchDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1579393170468732040L;

    /**
     * The label.
     */
    @UIFormField( name = "label", title = "3000169x4", orderNum = 2 )
    @UIColumn( data = "label", filter = "text", renderer = "text", title = "3000169x4", name = "label", orderNum = 2 )
    private String label;

    /**
     * The type.
     */
    @UIFormField( name = "type", title = "3000173x4", orderNum = 3 )
    @UIColumn( data = "type", filter = "text", renderer = "text", title = "3000173x4", name = "type", orderNum = 3 )
    private String type;

    /**
     * The car project.
     */
    @UIFormField( name = "carProject", title = "3000166x4", orderNum = 4 )
    @UIColumn( data = "carProject", filter = "text", renderer = "text", title = "3000166x4", name = "carProject", orderNum = 4 )
    private String carProject;

    /**
     * The format.
     */
    @UIFormField( name = "disciplineContext", title = "3000185x4", orderNum = 7 )
    @UIColumn( data = "disciplineContext", filter = "text", renderer = "text", title = "3000185x4", name = "disciplineContext", orderNum = 7 )
    private String disciplineContext;

    /**
     * The model def label.
     */
    @UIFormField( name = "modelDefLabel", title = "3000153x4", orderNum = 8 )
    @UIColumn( data = "modelDefLabel", filter = "text", renderer = "text", title = "3000153x4", name = "modelDefLabel", orderNum = 8 )
    private String modelDefLabel;

    /**
     * The project.
     */
    @UIFormField( name = "project", title = "3000172x4", isAsk = false, orderNum = 9 )
    @UIColumn( data = "project", filter = "text", renderer = "text", title = "3000166x4", name = "project", isShow = false, orderNum = 9 )
    private String project;

    /**
     * The variant label.
     */
    @UIFormField( name = "variantLabel", title = "3000151x4", orderNum = 10 )
    @UIColumn( data = "variantLabel", filter = "text", renderer = "text", title = "3000151x4", name = "variantLabel", orderNum = 10 )
    private String variantLabel;

    /**
     * The model def label.
     */
    @UIFormField( name = "modelState", title = "3000152x4", orderNum = 11 )
    @UIColumn( data = "modelState", filter = "text", renderer = "text", title = "3000152x4", name = "modelState", orderNum = 11 )
    private String modelState;

    /**
     * The format.
     */
    @UIFormField( name = "format", title = "3000171x4", orderNum = 12 )
    @UIColumn( data = "format", filter = "text", renderer = "text", title = "3000171x4", name = "format", orderNum = 12 )
    private String format;

    /**
     * The assemble type.
     */
    @UIFormField( name = "assembleType", title = "3000145x4", orderNum = 13 )
    @UIColumn( data = "assembleType", filter = "text", renderer = "text", title = "3000145x4", name = "assembleType", orderNum = 13 )
    private String assembleType;

    /**
     * Instantiates a new bmw cae bench sub model DTO.
     */
    public BmwCaeBenchSubModelDTO() {
    }

    /**
     * Gets the assemble type.
     *
     * @return the assemble type
     */
    public String getAssembleType() {
        return assembleType;
    }

    /**
     * Sets the assemble type.
     *
     * @param assembleType
     *         the new assemble type
     */
    public void setAssembleType( String assembleType ) {
        this.assembleType = assembleType;
    }

    /**
     * Gets the car project.
     *
     * @return the car project
     */
    public String getCarProject() {
        return carProject;
    }

    /**
     * Sets the car project.
     *
     * @param carProject
     *         the new car project
     */
    public void setCarProject( String carProject ) {
        this.carProject = carProject;
    }

    /**
     * Gets the project.
     *
     * @return the project
     */
    public String getProject() {
        return project;
    }

    /**
     * Sets the project.
     *
     * @param project
     *         the new project
     */
    public void setProject( String project ) {
        this.project = project;
    }

    /**
     * Gets the model def label.
     *
     * @return the model def label
     */
    public String getModelDefLabel() {
        return modelDefLabel;
    }

    /**
     * Sets the model def label.
     *
     * @param modelDefLabel
     *         the new model def label
     */
    public void setModelDefLabel( String modelDefLabel ) {
        this.modelDefLabel = modelDefLabel;
    }

    /**
     * Gets the variant label.
     *
     * @return the variant label
     */
    public String getVariantLabel() {
        return variantLabel;
    }

    /**
     * Sets the variant label.
     *
     * @param variantLabel
     *         the new variant label
     */
    public void setVariantLabel( String variantLabel ) {
        this.variantLabel = variantLabel;
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

    /**
     * Gets the model state.
     *
     * @return the model state
     */
    public String getModelState() {
        return modelState;
    }

    /**
     * Sets the model state.
     *
     * @param modelState
     *         the new model state
     */
    public void setModelState( String modelState ) {
        this.modelState = modelState;
    }

    /**
     * Gets the format.
     *
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the format.
     *
     * @param format
     *         the new format
     */
    public void setFormat( String format ) {
        this.format = format;
    }

    /**
     * Gets the discipline context.
     *
     * @return the discipline context
     */
    public String getDisciplineContext() {
        return disciplineContext;
    }

    /**
     * Sets the discipline context.
     *
     * @param disciplineContext
     *         the new discipline context
     */
    public void setDisciplineContext( String disciplineContext ) {
        this.disciplineContext = disciplineContext;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel( String label ) {
        this.label = label;
    }

}
