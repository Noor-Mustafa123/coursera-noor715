package de.soco.software.simuspace.server.dao;

import javax.persistence.EntityManager;

import java.util.List;

import de.soco.software.simuspace.suscore.data.entity.TrainingAlgoEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface TrainingAlgoDAO.
 *
 * @author noman arshad
 */
public interface TrainingAlgoDAO extends GenericDAO< TrainingAlgoEntity > {

    /**
     * Gets the training list.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the jobs list
     */
    List< TrainingAlgoEntity > getTrainingAlgoList( EntityManager entityManager );

    /**
     * Gets the training algo entity.
     *
     * @param entityManager
     *         the entity manager
     * @param algoName
     *         the algo name
     *
     * @return the training algo entity
     */
    TrainingAlgoEntity getTrainingAlgoEntityByName( EntityManager entityManager, String algoName );

}
