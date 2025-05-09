package de.soco.software.simuspace.suscore.interceptors.dao.impl;

import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.JobTokenEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.interceptors.dao.JobTokenDAO;

/**
 * An implementation class of UserTokenDAO. It is responsible for the CRUD operation related to user tokens.
 *
 * @author Zeeshan jamal
 */
public class JobTokenDAOImpl extends AbstractGenericDAO< JobTokenEntity > implements JobTokenDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public JobTokenEntity saveJobToken( EntityManager entityManager, JobTokenEntity jobTokenEntity ) {
        return save( entityManager, jobTokenEntity );
    }

    @Override
    public JobTokenEntity getJobTokenEntityByJobToken( EntityManager entityManager, String token ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.TOKEN, token );
        properties.put( ConstantsDAO.EXPIRED, false );
        return getUniqueObjectByProperties( entityManager, properties, JobTokenEntity.class );
    }

    @Override
    public List< JobTokenEntity > getJobTokenEntityByAuthToken( EntityManager entityManager, String authtoken ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.AUTH_TOKEN, authtoken );
        properties.put( ConstantsDAO.EXPIRED, false );
        return getListByProperties( entityManager, properties, JobTokenEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< JobTokenEntity > getJobTokenEntityByAuthTokenExpireAndNonExpire( EntityManager entityManager, String authtoken ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.AUTH_TOKEN, authtoken );
        return getListByProperties( entityManager, properties, JobTokenEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateJobTokenEntity( EntityManager entityManager, JobTokenEntity jobTokenEntity ) {
        saveOrUpdate( entityManager, jobTokenEntity );
    }

}
