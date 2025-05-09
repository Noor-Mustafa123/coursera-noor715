package de.soco.software.simuspace.suscore.data.common.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface BmwCaeBenchDAO.
 *
 * @author noman arshad
 */
public interface BmwCaeBenchDAO extends GenericDAO< BmwCaeBenchEntity > {

    /**
     * Gets the latest non deleted objects by list of ids.
     *
     * @param entityManager
     *         the entity manager
     * @param childs
     *         the childs
     *
     * @return the latest non deleted objects by list of ids
     */
    List< BmwCaeBenchEntity > getLatestNonDeletedObjectsByListOfIds( EntityManager entityManager, List< UUID > childs );

    /**
     * Gets the all bmw filtered records by property.
     *
     * @param entityManager
     *         the entity manager
     * @param node
     *         the node
     * @param filter
     *         the filter
     * @param caeBenchType
     *         the cae bench type
     *
     * @return the all bmw filtered records by property
     */
    Map< String, UUID > getAllBmwFilteredRecordsByProperty( EntityManager entityManager, String node, FiltersDTO filter,
            String caeBenchType );

    /**
     * Gets all bmw filtered records by property without node.
     *
     * @param entityManager
     *         the entity manager
     * @param filter
     *         the filter
     * @param caeBenchType
     *         the cae bench type
     *
     * @return the all bmw filtered records by property without node
     */
    List< BmwCaeBenchEntity > getAllBmwFilteredRecordsByPropertyWithoutNode( EntityManager entityManager, FiltersDTO filter,
            String caeBenchType );

    Map< String, UUID > getAllBmwFilteredRecordsSetByPropertyWithoutNode( EntityManager entityManager, FiltersDTO filter,
            String caeBenchType );

}
