package de.soco.software.simuspace.suscore.search.model;

/**
 * The Class Query.
 */
public class Query {

    /**
     * The Bool object.
     */
    Bool bool;

    /**
     * Instantiates a new query.
     */
    public Query() {
        super();
    }

    /**
     * Instantiates a new query.
     *
     * @param bool
     *         the bool
     */
    public Query( Bool bool ) {
        this.bool = bool;
    }

    /**
     * Gets the bool.
     *
     * @return the bool
     */
    public Bool getBool() {
        return bool;
    }

    /**
     * Sets the bool.
     *
     * @param bool
     *         the new bool
     */
    public void setBool( Bool bool ) {
        this.bool = bool;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Query [bool=" + bool + "]";
    }

}
