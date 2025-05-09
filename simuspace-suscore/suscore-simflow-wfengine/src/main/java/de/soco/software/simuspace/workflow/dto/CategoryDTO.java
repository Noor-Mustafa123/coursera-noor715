package de.soco.software.simuspace.workflow.dto;

import de.soco.software.simuspace.suscore.common.base.DataTransferObject;

/**
 * Category representation for FE
 *
 * @author Nosheen.Sharif
 */
public class CategoryDTO extends DataTransferObject {

    /**
     *
     */
    private static final long serialVersionUID = -6486423530150629975L;

    /**
     * The id of category.
     */
    private String id;

    /**
     * The name of category.
     */
    private String name;

    /**
     * The workflow count against a category.
     */
    private int workflowCount;

    /**
     * Instantiates a new category.
     */
    public CategoryDTO() {
        super();

    }

    /**
     * Gets the id of category.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name of category.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the workflow count of category.
     *
     * @return the workflow count
     */
    public int getWorkflowCount() {
        return workflowCount;
    }

    /**
     * Sets the id of category.
     *
     * @param id
     *         the new id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * Sets the name of category.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Sets the workflow count of category.
     *
     * @param workflowCount
     *         the new workflow count
     */
    public void setWorkflowCount( int workflowCount ) {
        this.workflowCount = workflowCount;
    }

}
