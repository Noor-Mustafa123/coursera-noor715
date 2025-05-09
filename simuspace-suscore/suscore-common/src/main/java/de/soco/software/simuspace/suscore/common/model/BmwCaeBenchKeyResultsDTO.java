package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class BmwCaeBenchKeyResultsDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class BmwCaeBenchKeyResultsDTO extends BmwCaeBenchDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 7315038673298149968L;

    /**
     * The release level label.
     */
    @UIFormField( name = "releaseLevelLabel", title = "3000162x4", orderNum = 2 )
    @UIColumn( data = "releaseLevelLabel", filter = "text", renderer = "text", title = "3000162x4", name = "releaseLevelLabel", orderNum = 2 )
    private String releaseLevelLabel;

    /**
     * The label.
     */
    @UIFormField( name = "label", title = "3000169x4", orderNum = 3 )
    @UIColumn( data = "label", filter = "text", renderer = "text", title = "3000169x4", name = "label", orderNum = 3 )
    private String label;

    /**
     * The type.
     */
    @UIFormField( name = "type", title = "3000173x4", orderNum = 4 )
    @UIColumn( data = "type", filter = "text", renderer = "text", title = "3000173x4", name = "type", orderNum = 4 )
    private String type;

    /**
     * The project.
     */
    @UIFormField( name = "project", title = "3000166x4", orderNum = 5 )
    @UIColumn( data = "project", filter = "text", renderer = "text", title = "3000166x4", name = "project", orderNum = 5 )
    private String project;

    /**
     * The inputDeck.
     */
    @UIFormField( name = "inputDeck", title = "3000164x4", orderNum = 5 )
    @UIColumn( data = "inputDeck", filter = "text", renderer = "text", title = "3000164x4", name = "inputDeck", orderNum = 5 )
    private String inputDeck;

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
     * Gets input deck.
     *
     * @return the input deck
     */
    public String getInputDeck() {
        return inputDeck;
    }

    /**
     * Sets input deck.
     *
     * @param inputDeck
     *         the input deck
     */
    public void setInputDeck( String inputDeck ) {
        this.inputDeck = inputDeck;
    }

}
