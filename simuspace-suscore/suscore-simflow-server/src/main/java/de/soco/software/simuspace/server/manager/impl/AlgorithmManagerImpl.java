package de.soco.software.simuspace.server.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.server.dao.TrainingAlgoDAO;
import de.soco.software.simuspace.server.dao.WFSchemeDAO;
import de.soco.software.simuspace.server.manager.AlgorithmManager;
import de.soco.software.simuspace.server.manager.BaseManager;
import de.soco.software.simuspace.server.model.AlgorithmDTO;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.SchemeCategoryEnum;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.entity.TrainingAlgoEntity;
import de.soco.software.simuspace.suscore.data.entity.WFSchemeEntity;
import de.soco.software.simuspace.suscore.user.manager.UserManager;

/**
 * The type Algorithm manager.
 */
public class AlgorithmManagerImpl extends BaseManager implements AlgorithmManager {

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Training algo dao.
     */
    private TrainingAlgoDAO trainingAlgoDAO;

    /**
     * The Wf scheme dao.
     */
    private WFSchemeDAO wFSchemeDAO;

    /**
     * The User manager.
     */
    private UserManager userManager;

    @Override
    public AlgorithmDTO getAlgorithmsDataCount( String userIdFromGeneralHeader ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getAlgorithmsDataCount( entityManager, userIdFromGeneralHeader );
        } finally {
            entityManager.close();
        }

    }

    /**
     * Gets algorithms data count.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the algorithms data count
     */
    private AlgorithmDTO getAlgorithmsDataCount( EntityManager entityManager, String userIdFromGeneralHeader ) {
        userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.TRAINING_ALGO.getId(),
                PermissionMatrixEnum.VIEW.getValue(), Messages.NO_RIGHTS_TO_READ.getKey(), "TrainingAlgos" );
        userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.WFSCHEMES.getId(),
                PermissionMatrixEnum.VIEW.getValue(), Messages.NO_RIGHTS_TO_READ.getKey(), "WFSchemes" );
        List< TrainingAlgoEntity > trainingAlgoEntities = trainingAlgoDAO.getTrainingAlgoList( entityManager );
        List< WFSchemeEntity > wfSchemeEntities = wFSchemeDAO.getLatestNonDeletedWFSchemeList( entityManager );

        long trainingAlgoCount = trainingAlgoEntities.size();
        long wfSchemeCount = wfSchemeEntities.size();
        long categoryZeroCount = wfSchemeEntities.stream().filter( item -> item.getCategory() == 0 ).count();
        long categoryOneCount = wfSchemeEntities.stream().filter( item -> item.getCategory() == 1 ).count();
        Map< String, Long > wfSchemeMap = getStringLongMap( categoryZeroCount, categoryOneCount );

        AlgorithmDTO algorithmsDTO = new AlgorithmDTO();
        Map< String, Object > trainingAlgosData = new HashMap<>();
        trainingAlgosData.put( MessageBundleFactory.getMessage( Messages.TOTAL_TRAINING_ALGOS.getKey() ), trainingAlgoCount );

        Map< String, Object > wfSchemeData = new HashMap<>();
        wfSchemeData.put( MessageBundleFactory.getMessage( Messages.TOTAL_WFSCHEMES.getKey() ), wfSchemeCount );
        wfSchemeData.put( MessageBundleFactory.getMessage( Messages.CATEGORIES.getKey() ), wfSchemeMap );

        algorithmsDTO.setTrainingAlgosList( trainingAlgosData );
        algorithmsDTO.setWfSchemesList( wfSchemeData );

        return algorithmsDTO;
    }

    /**
     * Gets string long map.
     *
     * @param categoryZeroCount
     *         the category zero count
     * @param categoryOneCount
     *         the category one count
     *
     * @return the string long map
     */
    private static Map< String, Long > getStringLongMap( long categoryZeroCount, long categoryOneCount ) {
        Map< String, Long > wfSchemeMap = new HashMap<>();
        wfSchemeMap.put( SchemeCategoryEnum.DOE.getValue(), categoryZeroCount );
        wfSchemeMap.put( SchemeCategoryEnum.OPTIMIZATION.getValue(), categoryOneCount );
        return wfSchemeMap;
    }

    /**
     * Gets entity manager factory.
     *
     * @return the entity manager factory
     */
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    /**
     * Sets entity manager factory.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Gets user manager.
     *
     * @return the user manager
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Sets user manager.
     *
     * @param userManager
     *         the user manager
     */
    public void setUserManager( UserManager userManager ) {
        this.userManager = userManager;
    }

    /**
     * Gets training algo dao.
     *
     * @return the training algo dao
     */
    public TrainingAlgoDAO getTrainingAlgoDAO() {
        return trainingAlgoDAO;
    }

    /**
     * Sets training algo dao.
     *
     * @param trainingAlgoDAO
     *         the training algo dao
     */
    public void setTrainingAlgoDAO( TrainingAlgoDAO trainingAlgoDAO ) {
        this.trainingAlgoDAO = trainingAlgoDAO;
    }

    /**
     * Gets wf scheme dao.
     *
     * @return the wf scheme dao
     */
    public WFSchemeDAO getwFSchemeDAO() {
        return wFSchemeDAO;
    }

    /**
     * Sets f scheme dao.
     *
     * @param wFSchemeDAO
     *         the w f scheme dao
     */
    public void setwFSchemeDAO( WFSchemeDAO wFSchemeDAO ) {
        this.wFSchemeDAO = wFSchemeDAO;
    }

}
