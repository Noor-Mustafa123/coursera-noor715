package de.soco.software.simuspace.suscore.user.dao.impl;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.LanguageEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.user.dao.LanguageDAO;

/**
 * A implementation class of LanguageDAO which is used to handle the database functions for user language preference.
 *
 * @author Zeeshan jamal
 */
public class LanguageDAOImpl extends AbstractGenericDAO< LanguageEntity > implements LanguageDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List< LanguageEntity > getAllLanguages( EntityManager entityManager ) {
        return findAll( entityManager );
    }

    /**
     * {@inheritDoc}
     */
    @Override

    public LanguageEntity getLanguageById( EntityManager entityManager, UUID id ) {
        return findById( entityManager, id );
    }

}
