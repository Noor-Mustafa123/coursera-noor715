package de.soco.software.simuspace.suscore.search.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class ELSQueryObject.
 */
public class ELSQueryObject {

    /**
     * The Query object.
     */
    Query query;

    /**
     * The from.
     */
    private float from;

    /**
     * The size.
     */
    private float size;

    /**
     * The sort.
     */
    private List< Object > sort = new ArrayList<>();

    /**
     * Instantiates a new ELS query object.
     */
    public ELSQueryObject() {
        super();
    }

    /**
     * Instantiates a new ELS query object.
     *
     * @param queryObject
     *         the query object
     * @param from
     *         the from
     * @param size
     *         the size
     * @param sort
     *         the sort
     */
    public ELSQueryObject( Query queryObject, float from, float size, List< Object > sort ) {
        super();
        query = queryObject;
        this.from = from;
        this.size = size;
        this.sort = sort;
    }

    /**
     * Gets the query.
     *
     * @return the query
     */
    public Query getQuery() {
        return query;
    }

    /**
     * Gets the from.
     *
     * @return the from
     */
    public float getFrom() {
        return from;
    }

    /**
     * Gets the size.
     *
     * @return the size
     */
    public float getSize() {
        return size;
    }

    /**
     * Sets the query.
     *
     * @param query
     *         the new query
     */
    public void setQuery( Query query ) {
        this.query = query;
    }

    /**
     * Sets the from.
     *
     * @param from
     *         the new from
     */
    public void setFrom( float from ) {
        this.from = from;
    }

    /**
     * Sets the size.
     *
     * @param size
     *         the new size
     */
    public void setSize( float size ) {
        this.size = size;
    }

    /**
     * Gets the sort.
     *
     * @return the sort
     */
    public List< Object > getSort() {
        return sort;
    }

    /**
     * Sets the sort.
     *
     * @param sort
     *         the new sort
     */
    public void setSort( List< Object > sort ) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "ELSQueryObject [query=" + query + ", from=" + from + ", size=" + size + ", sort=" + sort + "]";
    }

}