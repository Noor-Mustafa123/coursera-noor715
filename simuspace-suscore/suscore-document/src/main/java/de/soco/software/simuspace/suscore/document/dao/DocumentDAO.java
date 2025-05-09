package de.soco.software.simuspace.suscore.document.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * An interface having all the basic CRUD and other methods for communicating with document table in database
 *
 * @author ahmar.nadeem
 */
public interface DocumentDAO extends GenericDAO< DocumentEntity > {

    /**
     * Delete document.
     *
     * @param entityManager
     *         the entity manager
     * @param documentId
     *         the documentId
     *
     * @return true, if successful
     */
    boolean deleteDocument( EntityManager entityManager, UUID documentId );

    /**
     * A DAO function to get filtered list of documents against a particular user
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the userId
     * @param filter
     *         the filter
     *
     * @return Filtered list of Documents owned by user
     */
    List< DocumentEntity > getFilteredListByUser( EntityManager entityManager, UUID userId, FiltersDTO filter );

    /**
     * Gets the object by composite id
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         entity name
     * @param compositId
     *         the composit id
     *
     * @return returns the object
     */
    DocumentEntity findByCompositeId( EntityManager entityManager, Class< ? > clazz, VersionPrimaryKey compositId );

    /**
     * Find by name.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param name
     *         the name
     *
     * @return the document entity
     */
    DocumentEntity findByName( EntityManager entityManager, Class< ? > clazz, String name );

}
