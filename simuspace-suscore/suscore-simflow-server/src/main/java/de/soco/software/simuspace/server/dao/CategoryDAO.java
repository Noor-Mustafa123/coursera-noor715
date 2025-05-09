package de.soco.software.simuspace.server.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.simflow.server.entity.Categorizable;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.CategoryEntity;

/**
 * This interface perform all CRUD and other operations of Category
 *
 * @author Nosheen.Sharif
 */
public interface CategoryDAO extends Categorizable {

    /**
     * Gets the category by id.
     *
     * @param entityManager
     *         the entity manager
     * @param categoryId
     *         the category id
     *
     * @return the category by id and null if not exist
     */
    CategoryEntity getCategoryById( EntityManager entityManager, UUID categoryId );

    /**
     * Gets the category list by workflow id.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     *
     * @return the category list by workflow id if exist, otherwise null will be returned
     */
    List< CategoryEntity > getCategoryListByWorkflowId( EntityManager entityManager, UUID workflowId );

    /**
     * Gets the workflow count by category id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the workflow count by category id
     */
    int getWorkflowCountByCategoryId( EntityManager entityManager, UUID id );

    /**
     * {@inheritDoc}
     */
    @Override
    boolean removeCategory( EntityManager entityManager, UUID categoryId );

}
