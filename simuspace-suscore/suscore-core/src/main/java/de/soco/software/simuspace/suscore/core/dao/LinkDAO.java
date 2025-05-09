package de.soco.software.simuspace.suscore.core.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface LinkDAO.
 */
public interface LinkDAO extends GenericDAO< Relation > {

    /**
     * Gets the relation by parent id and child id.
     *
     * @param entityManager
     *         the entity manager
     * @param parent
     *         the parent
     * @param child
     *         the child
     *
     * @return the relation by parent id and child id
     */
    Relation getRelationLinkByParentIdAndChildId( EntityManager entityManager, UUID parent, UUID child );

    /**
     * Gets linked relation by child id.
     *
     * @param entityManager
     *         the entity manager
     * @param child
     *         the child
     *
     * @return the linked relation by child id
     */
    List< Relation > getLinkedRelationByChildId( EntityManager entityManager, UUID child );

    /**
     * Gets the linked relation by child id.
     *
     * @param entityManager
     *         the entity manager
     * @param childId
     *         the Child Id
     *
     * @return the relation by child id
     */
    List< Relation > getRelationWithParentByChildId( EntityManager entityManager, UUID childId );

}