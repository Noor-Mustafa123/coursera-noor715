package de.soco.software.simuspace.suscore.user.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.LanguageEntity;

/**
 * An interface to handle the database functions for user language.
 *
 * @author Zeeshan jamal
 */
public interface LanguageDAO {

    /**
     * A method used to get all languages
     *
     * @param entityManager
     *         the entity manager
     *
     * @return list of language entities
     */
    List< LanguageEntity > getAllLanguages( EntityManager entityManager );

    /**
     * A method used to get language by primary key from database
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return language entity
     */
    LanguageEntity getLanguageById( EntityManager entityManager, UUID id );

}
