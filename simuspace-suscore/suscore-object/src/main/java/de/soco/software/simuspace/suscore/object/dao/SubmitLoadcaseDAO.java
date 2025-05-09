package de.soco.software.simuspace.suscore.object.dao;

import javax.persistence.EntityManager;

import java.util.List;

import de.soco.software.simuspace.suscore.data.entity.SubmitLoadcaseEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface SubmitLoadcaseDAO.
 *
 * @author noman arshad
 */
public interface SubmitLoadcaseDAO extends GenericDAO< SubmitLoadcaseEntity > {

    /**
     * Gets the all pending jobs.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the all pending jobs
     */
    List< SubmitLoadcaseEntity > getAllPendingJobs( EntityManager entityManager );

}