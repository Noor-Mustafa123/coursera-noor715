package de.soco.software.simuspace.suscore.object.dao.impl;

import javax.persistence.EntityManager;

import java.util.List;

import de.soco.software.simuspace.suscore.data.entity.SubmitLoadcaseEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.object.dao.SubmitLoadcaseDAO;

/**
 * The Class SubmitLoadcaseDAOImpl.
 *
 * @author noman arshad
 */
public class SubmitLoadcaseDAOImpl extends AbstractGenericDAO< SubmitLoadcaseEntity > implements SubmitLoadcaseDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SubmitLoadcaseEntity > getAllPendingJobs( EntityManager entityManager ) {
        return getObjectListByProperty( entityManager, "jobCompleted", false );
    }

}