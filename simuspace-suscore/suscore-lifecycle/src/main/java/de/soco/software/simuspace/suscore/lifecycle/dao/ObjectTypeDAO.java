package de.soco.software.simuspace.suscore.lifecycle.dao;

import javax.persistence.EntityManager;

import de.soco.software.simuspace.suscore.data.entity.ObjectJsonSchemaEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * @author Ahmar Nadeem
 *
 * An interface for crud operations regarding object type configuration
 */
public interface ObjectTypeDAO extends GenericDAO< ObjectJsonSchemaEntity > {

    /**
     * Load last json schema configuration.
     *
     * @param entityManager
     *         the entity manager
     * @param schemaName
     *         the schema name
     *
     * @return the object json schema entity
     */
    ObjectJsonSchemaEntity loadLastJsonSchemaConfiguration( EntityManager entityManager, String schemaName );

    /**
     * Save new configuration object json schema entity.
     *
     * @param entityManager
     *         the entity manager
     * @param masterEntity
     *         Function to store the new configuration to the database for future reference.
     *
     * @return the object json schema entity
     */
    ObjectJsonSchemaEntity saveNewConfiguration( EntityManager entityManager, ObjectJsonSchemaEntity masterEntity );

}
