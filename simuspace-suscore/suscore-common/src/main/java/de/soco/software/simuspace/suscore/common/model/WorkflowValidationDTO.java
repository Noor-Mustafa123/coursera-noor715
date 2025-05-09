package de.soco.software.simuspace.suscore.common.model;

/**
 * The Class WorkflowValidationDTO.
 *
 * @author noman arshad
 */
public class WorkflowValidationDTO {

    /**
     * The validate.
     */
    private boolean validate;

    /**
     * The info.
     */
    Object info;

    /**
     * Gets the validate.
     *
     * @return the validate
     */
    public boolean getValidate() {
        return validate;
    }

    /**
     * Sets the validate.
     *
     * @param validate
     *         the new validate
     */
    public void setValidate( boolean validate ) {
        this.validate = validate;
    }

    /**
     * Gets the info.
     *
     * @return the info
     */
    public Object getInfo() {
        return info;
    }

    /**
     * Sets the info.
     *
     * @param info
     *         the new info
     */
    public void setInfo( Object info ) {
        this.info = info;
    }

    /**
     * Instantiates a new workflow validation DTO.
     *
     * @param validate
     *         the validate
     * @param info
     *         the info
     */
    public WorkflowValidationDTO( boolean validate, Object info ) {
        super();
        this.validate = validate;
        this.info = info;
    }

    /**
     * Instantiates a new workflow validation DTO.
     */
    public WorkflowValidationDTO() {

    }

}
