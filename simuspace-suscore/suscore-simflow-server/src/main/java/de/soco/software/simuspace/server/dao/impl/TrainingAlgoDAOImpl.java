package de.soco.software.simuspace.server.dao.impl;

import javax.persistence.EntityManager;

import java.util.List;

import de.soco.software.simuspace.server.dao.TrainingAlgoDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.TrainingAlgoEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * The class TrainingAlgoDAOImpl.
 *
 * @author noman arshad
 */
public class TrainingAlgoDAOImpl extends AbstractGenericDAO< TrainingAlgoEntity > implements TrainingAlgoDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TrainingAlgoEntity > getTrainingAlgoList( EntityManager entityManager ) {
        return getAllRecordsWithDescOrder( entityManager, TrainingAlgoEntity.class, ConstantsDAO.CREATED_ON );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingAlgoEntity getTrainingAlgoEntityByName( EntityManager entityManager, String algoName ) {
        return getLatestNonDeletedObjectByProperty( entityManager, TrainingAlgoEntity.class, ConstantsDAO.NAME, algoName );
    }

}
