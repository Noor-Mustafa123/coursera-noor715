package de.soco.software.simuspace.server.manager;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.workflow.model.Category;

/**
 * To manage the Category CRUD (and other) operation to Dao layer
 *
 * @author Nosheen.Sharif
 */
@Deprecated( forRemoval = true )
public interface CategoryManager {

    /**
     * Gets the categories.
     *
     * @return the categories
     */
    List< Category > getCategories();

    /**
     * Gets the category by id.
     *
     * @param categoryId
     *         the category id
     *
     * @return the category by id
     */
    @Deprecated
    Category getCategoryById( String categoryId );

    /**
     * Gets the category by id.
     *
     * @param entityManager
     *         the entity manager
     * @param categoryId
     *         the category id
     *
     * @return the category by id
     */
    Category getCategoryById( EntityManager entityManager, String categoryId );

    /**
     * Adds the category.
     *
     * @param userId
     *         the user id
     * @param categoryDto
     *         the category dto
     *
     * @return the category
     */
    Category addCategory( UUID userId, Category categoryDto );

    /**
     * Update category (update name of category).
     *
     * @param userId
     *         the user id
     * @param categoryDto
     *         the category dto
     *
     * @return the category
     */
    Category updateCategory( UUID userId, Category categoryDto );

    /**
     * Delete category.
     *
     * @param userId
     *         the user id
     * @param categoryId
     *         the category id
     *
     * @return true, if successful
     */
    boolean deleteCategory( UUID userId, String categoryId );

    /**
     * Gets the category list by workflow id.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the category list by workflow id
     */
    List< Category > getCategoryListByWorkflowId( String workflowId );

}
