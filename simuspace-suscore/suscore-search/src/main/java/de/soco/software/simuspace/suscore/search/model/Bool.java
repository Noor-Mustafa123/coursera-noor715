package de.soco.software.simuspace.suscore.search.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Bool.
 */
public class Bool {

    /**
     * The must.
     */
    private List< Object > must = new ArrayList<>();

    /**
     * The must not.
     */
    private List< Object > mustNot = new ArrayList<>();

    /**
     * The should.
     */
    private List< Object > should = new ArrayList<>();

    /**
     * Instantiates a new bool.
     */
    public Bool() {
        super();
    }

    /**
     * Instantiates a new bool.
     *
     * @param must
     *         the must
     * @param mustNot
     *         the must not
     * @param should
     *         the should
     */
    public Bool( List< Object > must, List< Object > mustNot, List< Object > should ) {
        super();
        this.must = must;
        this.mustNot = mustNot;
        this.should = should;
    }

    /**
     * Gets the should.
     *
     * @return the should
     */
    public List< Object > getShould() {
        return should;
    }

    /**
     * Sets the should.
     *
     * @param should
     *         the new should
     */
    public void setShould( List< Object > should ) {
        this.should = should;
    }

    /**
     * Gets the must not.
     *
     * @return the must not
     */
    public List< Object > getMustNot() {
        return mustNot;
    }

    /**
     * Sets the must not.
     *
     * @param mustNot
     *         the new must not
     */
    public void setMustNot( List< Object > mustNot ) {
        this.mustNot = mustNot;
    }

    /**
     * Gets the must.
     *
     * @return the must
     */
    public List< Object > getMust() {
        return must;
    }

    /**
     * Sets the must.
     *
     * @param must
     *         the new must
     */
    public void setMust( List< Object > must ) {
        this.must = must;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Bool [must=" + must + ", must_not=" + mustNot + ", should=" + should + "]";
    }

}
