package de.soco.software.simuspace.server.dao;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.SchemeSchemaEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface SchemeSchemaDAO.
 */
public interface SchemeSchemaDAO extends GenericDAO< SchemeSchemaEntity > {

    /**
     * Gets the scheme schema by workflow id and name.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param versionId
     *         the version id
     * @param name
     *         the name
     * @param userId
     *         the user id
     *
     * @return the scheme schema by workflow id and name
     */
    SchemeSchemaEntity getSchemeSchemaByWorkflowIdAndName( EntityManager entityManager, UUID workflowId, int versionId, String name,
            UUID userId );

}
