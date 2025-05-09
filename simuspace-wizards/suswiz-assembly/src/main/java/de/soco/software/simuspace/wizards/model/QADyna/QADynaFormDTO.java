package de.soco.software.simuspace.wizards.model.QADyna;

/**
 * The type Qa dyna form dto.
 */
public class QADynaFormDTO {

    /**
     * The Base.
     */
    private QADynaBaseForm base;

    /**
     * The Parameters.
     */
    private QADynaParameterForm parameters;

    /**
     * The Inputs.
     */
    private QADynaInputForm inputs;

    /**
     * The Ppo.
     */
    private QADynaPPOForm ppo;

    /**
     * The Job path.
     */
    private String jobPath;

    /**
     * Gets base.
     *
     * @return the base
     */
    public QADynaBaseForm getBase() {
        return base;
    }

    /**
     * Sets base.
     *
     * @param base
     *         the base
     */
    public void setBase( QADynaBaseForm base ) {
        this.base = base;
    }

    /**
     * Gets parameters.
     *
     * @return the parameters
     */
    public QADynaParameterForm getParameters() {
        return parameters;
    }

    /**
     * Sets parameters.
     *
     * @param parameters
     *         the parameters
     */
    public void setParameters( QADynaParameterForm parameters ) {
        this.parameters = parameters;
    }

    /**
     * Gets inputs.
     *
     * @return the inputs
     */
    public QADynaInputForm getInputs() {
        return inputs;
    }

    /**
     * Sets inputs.
     *
     * @param inputs
     *         the inputs
     */
    public void setInputs( QADynaInputForm inputs ) {
        this.inputs = inputs;
    }

    /**
     * Gets job path.
     *
     * @return the job path
     */
    public String getJobPath() {
        return jobPath;
    }

    /**
     * Sets job path.
     *
     * @param jobPath
     *         the job path
     */
    public void setJobPath( String jobPath ) {
        this.jobPath = jobPath;
    }

    /**
     * Gets ppo.
     *
     * @return the ppo
     */
    public QADynaPPOForm getPpo() {
        return ppo;
    }

    /**
     * Sets ppo.
     *
     * @param ppo
     *         the ppo
     */
    public void setPpo( QADynaPPOForm ppo ) {
        this.ppo = ppo;
    }

}
