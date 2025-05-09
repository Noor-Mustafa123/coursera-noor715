package de.soco.software.simuspace.suscore.location.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.data.entity.LocationEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface will be responsible for all the database operations necessary for dealing with the locations.
 *
 * @author M.Nasir.Farooq
 */
public interface LocationDAO extends GenericDAO< LocationEntity > {

    /**
     * Gets the all local object list.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the all local object list
     */
    List< LocationEntity > getAllLocalObjectList( EntityManager entityManager );

    /**
     * Gets latest non deleted location by id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the latest non deleted location by id
     */
    LocationEntity getLatestNonDeletedLocationById( EntityManager entityManager, UUID id );

    /**
     * Gets all property values.
     *
     * @param entityManager
     *         the entity manager
     * @param propertyName
     *         the property name
     *
     * @return the all property values
     */
    List< Object > getAllPropertyValues( EntityManager entityManager, String propertyName );

    /**
     * Gets all filtered records for locations.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters dto
     * @param userId
     *         the user id
     * @param key
     *
     * @return the all filtered records for locations
     */
    List< LocationEntity > getAllFilteredNonDeletedLocationsWithPermissions( EntityManager entityManager,
            FiltersDTO filtersDTO, String userId, int permission );

}
