package de.soco.software.simuspace.server.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.data.entity.RegexEntity;

/**
 * The interface Regex dao.
 */
public interface RegexDAO {

    /**
     * Save regex regex entity.
     *
     * @param entityManager
     *         the entity manager
     * @param regexEntity
     *         the regex entity
     *
     * @return the regex entity
     */
    RegexEntity saveRegex( EntityManager entityManager, RegexEntity regexEntity );

    /**
     * Gets regex.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the regex
     */
    RegexEntity getRegex( EntityManager entityManager, UUID id );

    /**
     * Gets regex list by selection selectionId.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selectionId
     * @param filtersDTO
     *         the filters dto
     *
     * @return the regex list by selection selectionId
     */
    List< RegexEntity > getRegexListBySelectionId( EntityManager entityManager, UUID selectionId, FiltersDTO filtersDTO );

    /**
     * Gets regex list by selection id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the regex list by selection id
     */
    List< RegexEntity > getRegexListBySelectionId( EntityManager entityManager, UUID id );

    /**
     * Update regex regex entity.
     *
     * @param entityManager
     *         the entity manager
     * @param regexEntity
     *         the regex entity
     *
     * @return the regex entity
     */
    RegexEntity updateRegex( EntityManager entityManager, RegexEntity regexEntity );

    /**
     * Delete regex boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param regexEntity
     *         the regex entity
     *
     * @return the boolean
     */
    Boolean deleteRegex( EntityManager entityManager, RegexEntity regexEntity );

}
