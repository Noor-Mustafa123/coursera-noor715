package de.soco.software.simuspace.wizards.model.run;

/**
 * The Class SubmitLoadcase.
 */
public class SubmitLoadcase {

    /**
     * The assemble.
     */
    private boolean assemble = false;

    /**
     * The solve.
     */
    private boolean solve = false;

    /**
     * The post.
     */
    private boolean post = false;

    /**
     * The oassemble.
     */
    private LoadcaseWFModel oassemble;

    /**
     * The osolve.
     */
    private LoadcaseWFModel osolve;

    /**
     * The opost.
     */
    private LoadcaseWFModel opost;

    /**
     * Checks if is assemble.
     *
     * @return true, if is assemble
     */
    public boolean isAssemble() {
        return assemble;
    }

    /**
     * Sets the assemble.
     *
     * @param assemble
     *         the new assemble
     */
    public void setAssemble( boolean assemble ) {
        this.assemble = assemble;
    }

    /**
     * Checks if is solve.
     *
     * @return true, if is solve
     */
    public boolean isSolve() {
        return solve;
    }

    /**
     * Sets the solve.
     *
     * @param solve
     *         the new solve
     */
    public void setSolve( boolean solve ) {
        this.solve = solve;
    }

    /**
     * Checks if is post.
     *
     * @return true, if is post
     */
    public boolean isPost() {
        return post;
    }

    /**
     * Sets the post.
     *
     * @param post
     *         the new post
     */
    public void setPost( boolean post ) {
        this.post = post;
    }

    /**
     * Instantiates a new submit loadcase.
     */
    public SubmitLoadcase() {
        super();
    }

    /**
     * Gets the oassemble.
     *
     * @return the oassemble
     */
    public LoadcaseWFModel getOassemble() {
        return oassemble;
    }

    /**
     * Sets the oassemble.
     *
     * @param oassemble
     *         the new oassemble
     */
    public void setOassemble( LoadcaseWFModel oassemble ) {
        this.oassemble = oassemble;
    }

    /**
     * Gets the osolve.
     *
     * @return the osolve
     */
    public LoadcaseWFModel getOsolve() {
        return osolve;
    }

    /**
     * Sets the osolve.
     *
     * @param osolve
     *         the new osolve
     */
    public void setOsolve( LoadcaseWFModel osolve ) {
        this.osolve = osolve;
    }

    /**
     * Gets the opost.
     *
     * @return the opost
     */
    public LoadcaseWFModel getOpost() {
        return opost;
    }

    /**
     * Sets the opost.
     *
     * @param opost
     *         the new opost
     */
    public void setOpost( LoadcaseWFModel opost ) {
        this.opost = opost;
    }

}
