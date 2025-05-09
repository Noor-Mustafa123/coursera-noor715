package de.soco.software.simuspace.suscore.data.simflow.server.entity;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.CategoryEntity;

/**
 * This interface defines if objects can have categories in the system. Some entities in the system can implement this interface to have
 * one-to-many relationship to objects in database.
 *
 * @author Nosheen.Sharif
 */
public interface Categorizable {

    /**
     * Gets the categories.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the categories
     */
    List< CategoryEntity > getCategories( EntityManager entityManager );

    /**
     * Adds the category.
     *
     * @param entityManager
     *         the entity manager
     * @param categoryEntity
     *         the category entity
     *
     * @return the category entity
     */
    CategoryEntity addCategory( EntityManager entityManager, CategoryEntity categoryEntity );

    /**
     * Update category( category name)
     *
     * @param entityManager
     *         the entity manager
     * @param categoryEntity
     *         the category entity
     *
     * @return the category entity
     */
    CategoryEntity updateCategory( EntityManager entityManager, CategoryEntity categoryEntity );

    /**
     * Removes the category.
     *
     * @param entityManager
     *         the entity manager
     * @param categoryId
     *         the category id
     *
     * @return true, if successful
     */
    boolean removeCategory( EntityManager entityManager, UUID categoryId );

}
