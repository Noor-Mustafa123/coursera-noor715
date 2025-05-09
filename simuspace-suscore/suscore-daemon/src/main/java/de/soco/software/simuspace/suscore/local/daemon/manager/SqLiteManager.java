package de.soco.software.simuspace.suscore.local.daemon.manager;

import java.util.List;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.local.daemon.entity.AllObjectEntity;
import de.soco.software.simuspace.suscore.local.daemon.entity.ComputedDataObjectEntity;
import de.soco.software.simuspace.suscore.local.daemon.entity.UserDaemonEntity;

/**
 * The Interface SqLiteManager.
 *
 * @author noman
 */
public interface SqLiteManager {

    /**
     * Gets the all users.
     *
     * @return the all users
     */
    public List< ComputedDataObjectEntity > getAllObjects();

    /**
     * Save.
     *
     * @param objects
     *         the objects
     */
    public void save( ComputedDataObjectEntity objects );

    /**
     * Delete all.
     */
    public void deleteAll();

    /**
     * Delete.
     *
     * @param object
     *         the object
     */
    public void delete( ComputedDataObjectEntity object );

    /**
     * Find by id.
     *
     * @param id
     *         the id
     *
     * @return the computed data object entity
     */
    public ComputedDataObjectEntity findById( String id );

    /**
     * Find by name.
     *
     * @param containerId
     *         the container id
     *
     * @return the computed data object entity
     */

    public List< ComputedDataObjectEntity > findBycontainerId( String containerId );

    /**
     * Find by name.
     *
     * @param name
     *         the name
     *
     * @return the computed data object entity
     */
    public ComputedDataObjectEntity findByName( String name );

    /**
     * Find by P id.
     *
     * @param id
     *         the id
     *
     * @return the computed data object entity
     */
    public ComputedDataObjectEntity findByPId( String id );

    /**
     * Find by path.
     *
     * @param path
     *         the path
     *
     * @return the computed data object entity
     */
    public ComputedDataObjectEntity findByPath( String path );

    /**
     * Save bulk data.
     *
     * @param object
     *         the object
     *
     * @return the list
     */
    public List< AllObjectEntity > saveBulkData( List< AllObjectEntity > object );

    /**
     * Save all object entity.
     *
     * @param object
     *         the object
     *
     * @return the all object entity
     */
    public AllObjectEntity saveAllObjectEntity( AllObjectEntity object );

    /**
     * List all by page.
     *
     * @param filtersDTO
     *         the filters DTO
     * @param totalRecords
     *         the total records
     * @param b
     *
     * @return the list
     */
    public List< AllObjectEntity > listAllByPage( FiltersDTO filtersDTO, int totalRecords );

    /**
     * Delete all object.
     */
    public void deleteAllObject();

    /**
     * Save user.
     *
     * @param object
     *         the object
     */
    void saveUser( UserDaemonEntity object );

    /**
     * Find user by id.
     *
     * @param id
     *         the id
     *
     * @return the user daemon entity
     */
    public UserDaemonEntity findUserById( String id );

}