package de.soco.software.simuspace.suscore.common.enums.simflow;

/**
 * The enum Field templates.
 */
public enum FieldTemplates {

    /**
     * Design variable field template.
     */
    DESIGN_VARIABLE( "designVariable" ),

    /**
     * Objective variable field template.
     */
    OBJECTIVE_VARIABLE( "objectiveVariable" ),

    /**
     * Custom variable field template.
     */
    CUSTOM_VARIABLE( "custom" );

    /**
     * Instantiates a new Field template.
     *
     * @param value
     *         the value
     */
    FieldTemplates( String value ) {
        this.value = value;
    }

    /**
     * The Value.
     */
    private final String value;

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return this.value;
    }
}
