package de.soco.software.simuspace.suscore.data.common.dao;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

public interface ContextMenuRelationDAO extends GenericDAO< Relation > {

    /**
     * Gets relation link by parent id and child id.
     *
     * @param entityManager
     *         the entity manager
     * @param parent
     *         the parent
     * @param child
     *         the child
     *
     * @return the relation link by parent id and child id
     */
    Relation getRelationLinkByParentIdAndChildId( EntityManager entityManager, UUID parent, UUID child );

}
