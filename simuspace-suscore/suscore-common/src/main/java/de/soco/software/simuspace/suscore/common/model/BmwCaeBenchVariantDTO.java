package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class BmwCaeBenchVariantDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class BmwCaeBenchVariantDTO extends BmwCaeBenchDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -836148646414915797L;

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
    @UIColumn( data = "disciplineContext", filter = "text", renderer = "text", title = "3000185x4", name = "disciplineContext.name", orderNum = 4 )
    private String disciplineContext;

    /**
     * The reference.
     */
    @UIFormField( name = "reference", title = "3000163x4", orderNum = 5 )
    @UIColumn( data = "reference", filter = "text", renderer = "text", title = "3000163x4", name = "reference", orderNum = 5 )
    private String reference;

    /**
     * The reference.
     */
    @UIFormField( name = "derivedFrom", title = "3000168x4", orderNum = 6 )
    @UIColumn( data = "derivedFrom", filter = "text", renderer = "text", title = "3000168x4", name = "derivedFrom", orderNum = 6 )
    private String derivedFrom;

    /**
     * The phase.
     */
    @UIFormField( name = "phase", title = "3000165x4", orderNum = 7 )
    @UIColumn( data = "phase", filter = "text", renderer = "text", title = "3000165x4", name = "phase", orderNum = 7 )
    private String phase;

    /**
     * The project.
     */
    @UIFormField( name = "project", title = "3000166x4", orderNum = 8 )
    @UIColumn( data = "project", filter = "text", renderer = "text", title = "3000166x4", name = "project", orderNum = 8 )
    private String project;

    /**
     * The release level label.
     */
    @UIFormField( name = "releaseLevelLabel", title = "3000162x4", orderNum = 10 )
    @UIColumn( data = "releaseLevelLabel", filter = "text", renderer = "text", title = "3000162x4", name = "releaseLevelLabel", orderNum = 10 )
    private String releaseLevelLabel;

    /**
     * The input decks.
     */
    @UIFormField( name = "inputDecks", title = "3000164x4", orderNum = 11 )
    @UIColumn( data = "inputDecks", filter = "text", renderer = "text", title = "3000164x4", name = "inputDecks", orderNum = 11 )
    private String inputDecks;

    /**
     * Gets the release level label.
     *
     * @return the release level label
     */
    public String getReleaseLevelLabel() {
        return releaseLevelLabel;
    }

    /**
     * Sets the release level label.
     *
     * @param releaseLevelLabel
     *         the new release level label
     */
    public void setReleaseLevelLabel( String releaseLevelLabel ) {
        this.releaseLevelLabel = releaseLevelLabel;
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

    /**
     * Gets the input decks.
     *
     * @return the input decks
     */
    public String getInputDecks() {
        return inputDecks;
    }

    /**
     * Sets the input decks.
     *
     * @param inputDecks
     *         the new input decks
     */
    public void setInputDecks( String inputDecks ) {
        this.inputDecks = inputDecks;
    }

    /**
     * Gets the phase.
     *
     * @return the phase
     */
    public String getPhase() {
        return phase;
    }

    /**
     * Sets the phase.
     *
     * @param phase
     *         the new phase
     */
    public void setPhase( String phase ) {
        this.phase = phase;
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
     * Gets the derived from.
     *
     * @return the derived from
     */
    public String getDerivedFrom() {
        return derivedFrom;
    }

    /**
     * Sets the derived from.
     *
     * @param derivedFrom
     *         the new derived from
     */
    public void setDerivedFrom( String derivedFrom ) {
        this.derivedFrom = derivedFrom;
    }

}
