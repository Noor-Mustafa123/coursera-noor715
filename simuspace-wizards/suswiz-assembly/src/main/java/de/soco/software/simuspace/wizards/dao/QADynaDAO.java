package de.soco.software.simuspace.wizards.dao;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.QADynaFormEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface SchemeSchemaDAO.
 */
public interface QADynaDAO extends GenericDAO< QADynaFormEntity > {

    /**
     * Gets by user and project.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     * @param versionId
     *         the version id
     *
     * @return the by user and project
     */
    QADynaFormEntity getByUserAndProject( EntityManager entityManager, UUID userId, UUID projectId, int versionId );

}
