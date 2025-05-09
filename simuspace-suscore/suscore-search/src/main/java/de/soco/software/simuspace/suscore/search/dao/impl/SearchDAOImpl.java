package de.soco.software.simuspace.suscore.search.dao.impl;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.search.dao.SearchDAO;

/**
 * The Class will be responsible for all the database operations necessary for dealing with the Search.
 *
 * @author Ali Haider
 * @since 2.1
 */
public class SearchDAOImpl extends AbstractGenericDAO< SuSEntity > implements SearchDAO {

    @Override
    public List< UUID > getGroupsByUserId( EntityManager entityManager, String userId ) {
        return getGroupsByUserId( entityManager, UUID.fromString( userId ) ).stream().map( GroupEntity::getId ).toList();
    }

}