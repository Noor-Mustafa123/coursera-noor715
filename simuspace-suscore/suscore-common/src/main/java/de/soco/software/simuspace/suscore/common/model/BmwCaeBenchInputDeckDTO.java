package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class BmwCaeBenchInputDeckDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class BmwCaeBenchInputDeckDTO extends BmwCaeBenchDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -8497042326869773993L;

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
     * The discipline context.
     */
    @UIFormField( name = "disciplineContext", title = "3000185x4", orderNum = 4 )
    @UIColumn( data = "disciplineContext", filter = "text", renderer = "text", title = "3000185x4", name = "disciplineContext", orderNum = 4 )
    private String disciplineContext;

    /**
     * The variant.
     */
    @UIFormField( name = "variant", title = "3000176x4", orderNum = 5 )
    @UIColumn( data = "variant", filter = "text", renderer = "text", title = "3000176x4", name = "variant", orderNum = 5 )
    private String variant;

    /**
     * The reference.
     */
    @UIFormField( name = "reference", title = "3000163x4", orderNum = 6 )
    @UIColumn( data = "reference", filter = "text", renderer = "text", title = "3000163x4", name = "reference", orderNum = 6 )
    private String reference;

    /**
     * The format.
     */
    @UIFormField( name = "format", title = "3000171x4", orderNum = 7 )
    @UIColumn( data = "format", filter = "text", renderer = "text", title = "3000171x4", name = "format", orderNum = 7 )
    private String format;

    /**
     * The project phase.
     */
    @UIFormField( name = "project", title = "3000172x4", orderNum = 8 )
    @UIColumn( data = "project", filter = "text", renderer = "text", title = "3000166x4", name = "project", orderNum = 8 )
    private String project;

    /**
     * The project phase.
     */
    @UIFormField( name = "projectPhase", title = "3000172x4", orderNum = 9 )
    @UIColumn( data = "projectPhase", filter = "text", renderer = "text", title = "3000172x4", name = "projectPhase", orderNum = 9 )
    private String projectPhase;

    /**
     * The sim process status.
     */
    @UIFormField( name = "simProcessStatus", title = "3000180x4", orderNum = 11 )
    @UIColumn( data = "simProcessStatus", filter = "text", renderer = "text", title = "3000180x4", name = "simProcessStatus", orderNum = 11 )
    private String simProcessStatus;

    /**
     * The sim post process status.
     */
    @UIFormField( name = "simPostProcessStatus", title = "3000181x4", orderNum = 12 )
    @UIColumn( data = "simPostProcessStatus", filter = "text", renderer = "text", title = "3000181x4", name = "simPostProcessStatus", orderNum = 12 )
    private String simPostProcessStatus;

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "BmwCaeBenchInputDeckDTO [label=" + label + ", format=" + format + ", type=" + type + ", project=" + project
                + ", projectPhase=" + projectPhase + ", variant=" + variant + ", simProcessStatus=" + simProcessStatus
                + ", simPostProcessStatus=" + simPostProcessStatus + "]";
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
     * Gets the project phase.
     *
     * @return the project phase
     */
    public String getProjectPhase() {
        return projectPhase;
    }

    /**
     * Sets the project phase.
     *
     * @param projectPhase
     *         the new project phase
     */
    public void setProjectPhase( String projectPhase ) {
        this.projectPhase = projectPhase;
    }

    /**
     * Gets the variant.
     *
     * @return the variant
     */
    public String getVariant() {
        return variant;
    }

    /**
     * Sets the variant.
     *
     * @param variant
     *         the new variant
     */
    public void setVariant( String variant ) {
        this.variant = variant;
    }

    /**
     * Gets the sim process status.
     *
     * @return the sim process status
     */
    public String getSimProcessStatus() {
        return simProcessStatus;
    }

    /**
     * Sets the sim process status.
     *
     * @param simProcessStatus
     *         the new sim process status
     */
    public void setSimProcessStatus( String simProcessStatus ) {
        this.simProcessStatus = simProcessStatus;
    }

    /**
     * Gets the sim post process status.
     *
     * @return the sim post process status
     */
    public String getSimPostProcessStatus() {
        return simPostProcessStatus;
    }

    /**
     * Sets the sim post process status.
     *
     * @param simPostProcessStatus
     *         the new sim post process status
     */
    public void setSimPostProcessStatus( String simPostProcessStatus ) {
        this.simPostProcessStatus = simPostProcessStatus;
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

    /**
     * Gets the reference.
     *
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the reference.
     *
     * @param reference
     *         the new reference
     */
    public void setReference( String reference ) {
        this.reference = reference;
    }

}
