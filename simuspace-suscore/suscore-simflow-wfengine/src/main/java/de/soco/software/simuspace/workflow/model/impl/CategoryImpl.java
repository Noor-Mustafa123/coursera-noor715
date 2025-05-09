package de.soco.software.simuspace.workflow.model.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.workflow.model.Category;

/**
 * This Category is an workflow element type An element can be of [element,composed,custom] category/type
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class CategoryImpl implements Category {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -7411911678078662062L;

    /**
     * Number of workflows in category count
     */
    private int count;

    /**
     * The id.
     */
    private String id;

    /**
     * The name.
     */
    private String name;

    /**
     * Instantiates a new Category impl.
     */
    public CategoryImpl() {
        super();
    }

    /**
     * Instantiates a new category impl.
     *
     * @param name
     *         the name of catagory
     * @param count
     *         the count of catagory
     */
    public CategoryImpl( String name, int count ) {
        this();
        this.name = name;
        this.count = count;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCount( int count ) {
        this.count = count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId( String id ) {
        this.id = id;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "CategoryImpl [id='" + id + "',name='" + name + "', count='" + count + "']";
    }

}
