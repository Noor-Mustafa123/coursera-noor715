package de.soco.software.simuspace.suscore.data.common.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.TranslationEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface TranslationDAO.
 */
public interface TranslationDAO extends GenericDAO< TranslationEntity > {

    /**
     * Gets the all translations by list of ids and clazz.
     *
     * @param entityManager
     *         the entity manager
     * @param listOfIds
     *         the list of ids
     *
     * @return the all translations by list of ids
     */
    List< TranslationEntity > getAllTranslationsByListOfIds( EntityManager entityManager, List< VersionPrimaryKey > listOfIds );

    /**
     * Gets the all translations by list of UUID.
     *
     * @param entityManager
     *         the entity manager
     * @param listOfIds
     *         the list of ids
     *
     * @return the all translations by list of UUID
     */
    List< TranslationEntity > getAllTranslationsByListOfUUID( EntityManager entityManager, List< UUID > listOfIds );

}
