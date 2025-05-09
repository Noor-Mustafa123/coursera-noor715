package de.soco.software.simuspace.server.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.UUID;

import de.soco.software.simuspace.server.dao.JobIdsDAO;
import de.soco.software.simuspace.suscore.data.entity.JobIdsEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

public class JobIdsDAOImpl extends AbstractGenericDAO< JobIdsEntity > implements JobIdsDAO {

    @Override
    public JobIdsEntity saveJobIds( EntityManager entityManager, JobIdsEntity jobIdsEntity ) {
        return save( entityManager, jobIdsEntity );
    }

    @Override
    public JobIdsEntity getJobIdsEntityByUUID( EntityManager entityManager, UUID uuid ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< JobIdsEntity > criteria = cb.createQuery( JobIdsEntity.class );
        Root< JobIdsEntity > root = criteria.from( JobIdsEntity.class );
        Predicate predicate = cb.equal( root.get( "uuid" ), uuid );
        criteria.where( predicate );
        return entityManager.createQuery( criteria ).getResultList().stream().findFirst().orElse( null );
    }

}
