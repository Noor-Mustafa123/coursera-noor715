package de.soco.software.simuspace.suscore.search.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface will be responsible for all the database operations necessary for dealing with the Search.
 *
 * @author Ali Haider
 * @since 2.1
 */
public interface SearchDAO extends GenericDAO< SuSEntity > {

    /**
     * Gets the groups by user id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the groups by user id
     */
    List< UUID > getGroupsByUserId( EntityManager entityManager, String userId );

}