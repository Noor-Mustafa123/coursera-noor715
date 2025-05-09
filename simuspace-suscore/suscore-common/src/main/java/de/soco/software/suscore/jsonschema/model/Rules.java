package de.soco.software.suscore.jsonschema.model;

/**
 * The Class Rules.
 *
 * @author noman arshad
 */
public class Rules {

    /**
     * The required.
     */
    private boolean required;

    /**
     * The maxlength.
     */
    private int maxlength = 255;

    /**
     * The alpha numeric.
     */
    private boolean alphaNumeric;

    /**
     * The select options.
     */
    private boolean selectOptions;

    /**
     * The pattern.
     */
    String pattern;

    /**
     * Instantiates a new rules.
     */
    public Rules() {
    }

    /**
     * Instantiates a new rules.
     *
     * @param required
     *         the required
     * @param maxlength
     *         the maxlength
     * @param alphaNumeric
     *         the alpha numeric
     * @param selectOptions
     *         the select options
     * @param pattern
     *         the pattern
     */
    public Rules( boolean required, int maxlength, boolean alphaNumeric, boolean selectOptions, String pattern ) {
        super();
        this.required = required;
        this.maxlength = maxlength;
        this.alphaNumeric = alphaNumeric;
        this.selectOptions = selectOptions;
        this.pattern = pattern;
    }

    // Getter Methods

    /**
     * Gets the required.
     *
     * @return the required
     */
    public boolean getRequired() {
        return required;
    }

    // Setter Methods

    /**
     * Sets the required.
     *
     * @param required
     *         the new required
     */
    public void setRequired( boolean required ) {
        this.required = required;
    }

    /**
     * Gets the maxlength.
     *
     * @return the maxlength
     */
    public int getMaxlength() {
        return maxlength;
    }

    /**
     * Sets the maxlength.
     *
     * @param maxlength
     *         the new maxlength
     */
    public void setMaxlength( int maxlength ) {
        this.maxlength = maxlength;
    }

    /**
     * Checks if is alpha numeric.
     *
     * @return true, if is alpha numeric
     */
    public boolean isAlphaNumeric() {
        return alphaNumeric;
    }

    /**
     * Sets the alpha numeric.
     *
     * @param alphaNumeric
     *         the new alpha numeric
     */
    public void setAlphaNumeric( boolean alphaNumeric ) {
        this.alphaNumeric = alphaNumeric;
    }

    /**
     * Checks if is select options.
     *
     * @return true, if is select options
     */
    public boolean isSelectOptions() {
        return selectOptions;
    }

    /**
     * Sets the select options.
     *
     * @param selectOptions
     *         the new select options
     */
    public void setSelectOptions( boolean selectOptions ) {
        this.selectOptions = selectOptions;
    }

    /**
     * Gets the pattern.
     *
     * @return the pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Sets the pattern.
     *
     * @param pattern
     *         the new pattern
     */
    public void setPattern( String pattern ) {
        this.pattern = pattern;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Rules [required=" + required + ", maxlength=" + maxlength + ", alphaNumeric=" + alphaNumeric + ", selectOptions="
                + selectOptions + ", pattern=" + pattern + "]";
    }

}
