package de.soco.software.simuspace.suscore.data.entity.base;

import javax.persistence.EntityManager;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectType;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Interface GenericDAO.
 *
 * @param <E>
 *         the element type
 */
public interface GenericDAO< E > {

    /**
     * Save.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return the e
     */
    E save( EntityManager entityManager, E entity );

    /**
     * Save.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     * @param logs
     *         the logs
     *
     * @return the e
     */
    E save( EntityManager entityManager, E entity, List< ? > logs );

    /**
     * Save all.
     *
     * @param entityManager
     *         the entity manager
     * @param entities
     *         the entities
     */
    void saveAll( EntityManager entityManager, List< E > entities );

    /**
     * Save or update.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return the e
     */
    E saveOrUpdate( EntityManager entityManager, E entity );

    /**
     * Save or update bulk.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return true, if successful
     */
    boolean saveOrUpdateBulk( EntityManager entityManager, List< E > entity );

    /**
     * Merge.
     *
     * @param entity
     *         the entity
     *
     * @return the e
     */
    E merge( EntityManager entityManager, E entity );

    /**
     * Delete.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     */
    void delete( EntityManager entityManager, E entity );

    /**
     * Delete bulk.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     */
    void deleteBulk( EntityManager entityManager, List< E > entity );

    /**
     * Find all.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the list
     */
    List< E > findAll( EntityManager entityManager );

    /**
     * Find by id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the e
     */
    E findById( EntityManager entityManager, Serializable id );

    /**
     * Gets the latest object by id.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param id
     *         the id
     *
     * @return the latest object by id
     */
    E getLatestObjectById( EntityManager entityManager, Class< ? > clazz, UUID id );

    /**
     * Gets the latest non deleted object by id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the latest non deleted object by id
     */
    E getLatestNonDeletedObjectById( EntityManager entityManager, UUID id );

    /**
     * Gets the latest object by id with life cycle.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     *
     * @return the latest object by id with life cycle
     */
    E getLatestObjectByIdWithLifeCycle( EntityManager entityManager, UUID id, UUID userId, List< String > ownerVisibleStatus,
            List< String > anyVisibleStatus );

    /**
     * Gets the latest non deleted active object by id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the latest non deleted active object by id
     */
    E getLatestNonDeletedActiveObjectById( EntityManager entityManager, UUID id );

    /**
     * Gets the latest non deleted objects by ids.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the latest non deleted objects by ids
     */
    List< E > getLatestNonDeletedObjectsByIds( EntityManager entityManager, List< UUID > id );

    /**
     * Gets the all latest objects by list of ids and clazz.
     *
     * @param entityManager
     *         the entity manager
     * @param listOfIds
     *         the list of ids
     * @param clazz
     *         the clazz
     *
     * @return the all latest objects by list of ids and clazz
     */
    List< E > getAllLatestObjectsByListOfIdsAndClazz( EntityManager entityManager, List< UUID > listOfIds, Class< ? > clazz );

    /**
     * Query.
     *
     * @param entityManager
     *         the entity manager
     * @param hsql
     *         the hsql
     * @param params
     *         the params
     *
     * @return the list
     */
    List< ? > query( EntityManager entityManager, String hsql, Map< String, Object > params );

    /**
     * Query unique object.
     *
     * @param entityManager
     *         the entity manager
     * @param hsql
     *         the hsql
     * @param params
     *         the params
     *
     * @return the e
     */
    E queryUniqueObject( EntityManager entityManager, String hsql, Map< String, Object > params );

    /**
     * Gets the all filtered records.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the all filtered records
     */
    List< E > getAllFilteredRecords( EntityManager entityManager, Class< ? > entityClazz, FiltersDTO filtersDTO );

    /**
     * Gets all filtered records.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters dto
     * @param isCacheEnabled
     *         the is cache enabled
     *
     * @return the all filtered records
     */
    List< E > getAllFilteredRecords( EntityManager entityManager, Class< ? > entityClazz, FiltersDTO filtersDTO, Boolean isCacheEnabled );

    /**
     * Gets the all filtered records by properties.
     *
     * @param entityClazz
     *         the entity clazz
     * @param properties
     *         the properties
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the all filtered records by properties
     */
    List< E > getAllFilteredRecordsByProperties( EntityManager entityManager, Class< ? > entityClazz, Map< String, Object > properties,
            FiltersDTO filtersDTO );

    /**
     * Gets the all filtered records by object id.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param objectId
     *         the object id
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the all filtered records by object id
     */
    List< E > getAllFilteredRecordsByObjectId( EntityManager entityManager, Class< ? > entityClazz, UUID objectId, FiltersDTO filtersDTO );

    /**
     * Gets the all filtered records by object ids.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param objectIds
     *         the object ids
     * @param filtersDTO
     *         the filters DTO
     * @param objectTypeList
     *         the object type list
     *
     * @return the all filtered records by object ids
     */
    List< E > getAllFilteredRecordsByObjectIds( EntityManager entityManager, Class< ? > entityClazz, List< UUID > objectIds,
            FiltersDTO filtersDTO, List< ObjectType > objectTypeList );

    /**
     * Gets the all filtered records by object id and version id.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the all filtered records by object id and version id
     */
    List< E > getAllFilteredRecordsByObjectIdAndVersionId( EntityManager entityManager, Class< ? > entityClazz, UUID objectId,
            int versionId, FiltersDTO filtersDTO );

    /**
     * Gets the all filtered records with parent and life cycle.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters DTO
     * @param parentId
     *         the parent id
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     * @param objectTypes
     *         the object types
     *
     * @return the all filtered records with parent and life cycle
     */
    List< E > getAllFilteredRecordsWithParentAndLifeCycle( EntityManager entityManager, Class< ? > entityClazz, FiltersDTO filtersDTO,
            UUID parentId, String userId, List< String > ownerVisibleStatus, List< String > anyVisibleStatus,
            List< ObjectType > objectTypes );

    /**
     * Gets all filtered records with parent and life cycle and permission.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters dto
     * @param parentId
     *         the parent id
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     * @param objectTypes
     *         the object types
     * @param permission
     *         the permission
     *
     * @return the all filtered records with parent and life cycle and permission
     */
    List< E > getAllFilteredRecordsWithParentAndLifeCycleAndPermission( EntityManager entityManager, Class< ? > entityClazz,
            FiltersDTO filtersDTO, UUID parentId, String userId, List< String > ownerVisibleStatus, List< String > anyVisibleStatus,
            List< ObjectType > objectTypes, int permission );

    /**
     * Gets all filtered records with parent and life cycle and permission without count.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters dto
     * @param parentId
     *         the parent id
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     * @param permission
     *         the permission
     * @param objectTypes
     *         the object types
     *
     * @return the all filtered records with parent and life cycle and permission without count
     */
    List< E > getAllFilteredRecordsWithParentAndLifeCycleAndPermissionWithoutCount( EntityManager entityManager, Class< ? > entityClazz,
            FiltersDTO filtersDTO, UUID parentId, String userId, List< String > ownerVisibleStatus, List< String > anyVisibleStatus,
            int permission, List< ObjectType > objectTypes );

    /**
     * Gets the all filtered linked items with parent and life cycle.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters DTO
     * @param parentId
     *         the parent id
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     * @param permission
     *         the permission
     * @param objectTypes
     *         the object types
     * @param isLink
     *         the is link
     *
     * @return the all filtered linked items with parent and life cycle
     */
    List< E > getAllFilteredLinkedItemsWithParentAndLifeCycle( EntityManager entityManager, Class< ? > entityClazz, FiltersDTO filtersDTO,
            UUID parentId, String userId, List< String > ownerVisibleStatus, List< String > anyVisibleStatus, int permission,
            List< ObjectType > objectTypes, boolean isLink );

    /**
     * Gets the all filtered records with parent.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters DTO
     * @param parentId
     *         the parent id
     *
     * @return the all filtered records with parent
     */
    List< E > getAllFilteredRecordsWithParent( EntityManager entityManager, Class< ? > entityClazz, FiltersDTO filtersDTO, UUID parentId );

    /**
     * Gets the object list by property.
     *
     * @param entityManager
     *         the entity manager
     * @param propertyName
     *         the property name
     * @param value
     *         the value
     *
     * @return the object list by property
     */
    List< E > getObjectListByProperty( EntityManager entityManager, String propertyName, Object value );

    /**
     * Gets the count.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param isDeleted
     *         the is deleted
     *
     * @return the count
     */
    Long getCount( EntityManager entityManager, Class< ? > clazz, boolean isDeleted );

    /**
     * Gets the object by composite key.
     *
     * @param entityManager
     *         the entity manager
     * @param versionPrimaryKey
     *         the version primary key
     *
     * @return the object by composite key
     */
    E getObjectByCompositeKey( EntityManager entityManager, VersionPrimaryKey versionPrimaryKey );

    /**
     * Update.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return the e
     */
    E update( EntityManager entityManager, E entity );

    /**
     * Checks if is hibernate entity.
     *
     * @param clazz
     *         the clazz
     *
     * @return true, if is hibernate entity
     */
    boolean isHibernateEntity( Class< ? > clazz );

    /**
     * Gets the all object list.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the all object list
     */
    List< E > getAllObjectList( EntityManager entityManager );

    /**
     * Gets the all filtered versions by id.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param id
     *         the id
     * @param filtersDTO
     *         the filters DTO
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     *
     * @return the all filtered versions by id
     */
    List< E > getAllFilteredVersionsById( EntityManager entityManager, Class< ? > entityClazz, UUID id, FiltersDTO filtersDTO, UUID userId,
            List< String > ownerVisibleStatus, List< String > anyVisibleStatus );

    /**
     * Gets the latest object by version and status.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param id
     *         the id
     * @param versionId
     *         the version id
     * @param status
     *         the status
     *
     * @return the latest object by version and status
     */
    E getLatestObjectByVersionAndStatus( EntityManager entityManager, Class< ? > clazz, UUID id, int versionId, String status );

    /**
     * Gets the all versions of object by id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the all versions of object by id
     */
    List< E > getAllVersionsOfObjectById( EntityManager entityManager, UUID id );

    /**
     * Gets the all records with parent.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param parentId
     *         the parent id
     *
     * @return the all records with parent
     */
    List< E > getAllRecordsWithParent( EntityManager entityManager, Class< ? > entityClazz, UUID parentId );

    /**
     * Gets the latest child by parent.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param parentId
     *         the parent id
     *
     * @return the latest child by parent
     */
    E getLatestChildByParent( EntityManager entityManager, Class< ? > entityClazz, UUID parentId );

    /**
     * Gets the all filtered records by property.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param hibernatePropertyName
     *         the hibernate property name
     * @param dtoPropertyName
     *         the dto property name
     * @param propertyValue
     *         the property value
     * @param filtersDTO
     *         the filters DTO
     * @param isAlias
     *         the is alias
     * @param objectTypeList
     *         the object type list
     *
     * @return the all filtered records by property
     */
    List< E > getAllFilteredRecordsByProperty( EntityManager entityManager, Class< ? > entityClazz, String hibernatePropertyName,
            String dtoPropertyName, Object propertyValue, FiltersDTO filtersDTO, boolean isAlias, List< ObjectType > objectTypeList );

    /**
     * Gets the count with parent id.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param parentId
     *         the parent id
     *
     * @return the count with parent id
     */
    Long getCountWithParentId( EntityManager entityManager, Class< ? > clazz, UUID parentId );

    /**
     * Gets the count with parent id.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param parentId
     *         the parent id
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     * @param userSecurityIDs
     *         the user security I ds
     * @param permission
     *         the permission
     *
     * @return the count with parent id
     */
    Long getCountWithParentId( EntityManager entityManager, Class< ? > clazz, UUID parentId, String userId,
            List< String > ownerVisibleStatus, List< String > anyVisibleStatus, String userSecurityIDs, int permission );

    /**
     * Gets the user security I ds.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the user security I ds
     */
    String getUserSecurityIDs( EntityManager entityManager, UUID id );

    /**
     * Gets the all filtered linked items with parent and life cycle for audit trail.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters DTO
     * @param parentId
     *         the parent id
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     * @param permission
     *         the permission
     * @param objectTypeList
     *         the object type list
     * @param isContainer
     *         the is container
     *
     * @return the all filtered linked items with parent and life cycle for audit trail
     */
    List< E > getAllFilteredLinkedItemsWithParentAndLifeCycleForAuditTrail( EntityManager entityManager, Class< ? > entityClazz,
            FiltersDTO filtersDTO, UUID parentId, String userId, List< String > ownerVisibleStatus, List< String > anyVisibleStatus,
            int permission, List< ObjectType > objectTypeList, boolean isContainer );

    /**
     * Gets the latest non deleted object by property.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param propertyName
     *         the property name
     * @param value
     *         the value
     *
     * @return the latest non deleted object by property
     */
    E getLatestNonDeletedObjectByProperty( EntityManager entityManager, Class< ? > clazz, String propertyName, Object value );

    /**
     * Gets the all records.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the all records
     */
    List< E > getAllRecords( EntityManager entityManager );

    /**
     * Gets unique object by property.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param propertyName
     *         the property name
     * @param value
     *         the value
     * @param cache
     *         the cache
     *
     * @return the unique object by property
     */
    E getUniqueObjectByProperty( EntityManager entityManager, Class< ? > clazz, String propertyName, Object value, boolean cache );

    /**
     * Gets unique object by property.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param propertyName
     *         the property name
     * @param value
     *         the value
     *
     * @return the unique object by property
     */
    E getUniqueObjectByProperty( EntityManager entityManager, Class< ? > clazz, String propertyName, Object value );

    /**
     * Gets the all jobs with created on and properties.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param minDateProperty
     *         the min date property
     * @param propertyKey
     *         the property key
     * @param properties
     *         the properties
     *
     * @return the all jobs with created on and properties
     */
    List< E > getAllJobsWithCreatedOnAndProperties( EntityManager entityManager, Class< ? > clazz, Date minDateProperty, String propertyKey,
            List< Object > properties );

    /**
     * Removes the.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     */
    void remove( EntityManager entityManager, E entity );

    /**
     * Gets the all filtered records with permissions by jpa.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters DTO
     * @param userId
     *         the user id
     * @param permission
     *         the permission
     * @param getUserJobsOnly
     *         the get user jobs only
     *
     * @return the all filtered records with permissions by jpa
     */
    List< E > getAllFilteredRecordsWithPermissionsByJpa( EntityManager entityManager, Class< ? > entityClazz, FiltersDTO filtersDTO,
            UUID userId, int permission, Boolean getUserJobsOnly );

    /**
     * Gets the all records with parent by jpa.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param parentId
     *         the parent id
     *
     * @return the all records with parent by jpa
     */
    List< E > getAllRecordsWithParentByJpa( EntityManager entityManager, Class< ? > entityClazz, UUID parentId );

    /**
     * Gets the latest object list by type and name.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param name
     *         the name
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     *
     * @return the latest object list by type and name
     */
    List< ContainerEntity > getContainersForTreeSearchWithLanguage( EntityManager entityManager, String name, UUID userId,
            List< String > ownerVisibleStatus, List< String > anyVisibleStatus, String language );

    /**
     * Gets the all deleted objects.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param userId
     *         the user id
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the all deleted objects
     */
    List< E > getAllDeletedObjects( EntityManager entityManager, Class< ? > entityClazz, UUID userId, FiltersDTO filtersDTO );

    /**
     * Gets the object by property.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param propertyName
     *         the property name
     * @param value
     *         the value
     *
     * @return the object by property
     */
    E getObjectByProperty( EntityManager entityManager, Class< ? > clazz, String propertyName, Object value );

    /**
     * Gets the all life cycle status ids.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the all life cycle status ids
     */
    List< String > getAllLifeCycleStatusIds( EntityManager entityManager );

    /**
     * Gets the all filtered record count with parent id.
     *
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters DTO
     * @param parentId
     *         the parent id
     *
     * @return the all filtered record count with parent id
     */
    Long getAllFilteredRecordCountWithParentId( EntityManager entityManager, Class< ? > entityClazz, FiltersDTO filtersDTO, UUID parentId );

    /**
     * Gets the all filtered records count.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the all filtered records count
     */
    Long getAllFilteredRecordsCount( EntityManager entityManager, Class< ? > clazz, FiltersDTO filtersDTO );

    /**
     * Gets the all records between dates.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param params
     *         the params
     * @param dateFrom
     *         the date from
     * @param dateTo
     *         the date to
     *
     * @return the all records between dates
     */
    List< E > getAllRecordsBetweenDates( EntityManager entityManager, Class< ? > entityClazz, Map< String, Object > params, Date dateFrom,
            Date dateTo );

    /**
     * Gets the records for fe static thread.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param latestDateProperty
     *         the latest date property
     * @param previousLatestDateProperty
     *         the previous latest date property
     * @param lifeCycleProperties
     *         the life cycle properties
     *
     * @return the records for fe static thread
     */
    List< E > getRecordsForFeStaticThread( EntityManager entityManager, Class< ? > clazz, Date latestDateProperty,
            Date previousLatestDateProperty, List< Object > lifeCycleProperties );

    /**
     * Gets the all previous records with created on and properties.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param minDateProperty
     *         the min date property
     * @param propertyKey
     *         the property key
     * @param properties
     *         the properties
     * @param jobExtract
     *         the job extract
     *
     * @return the all previous records with created on and properties
     */
    List< E > getAllPreviousRecordsWithCreatedOnAndProperties( EntityManager entityManager, Class< ? > clazz, Date minDateProperty,
            String propertyKey, List< Object > properties, boolean jobExtract );

    /**
     * Gets the latest non deleted object list by property.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param propertyName
     *         the property name
     * @param value
     *         the value
     *
     * @return the latest non deleted object list by property
     */
    List< E > getLatestNonDeletedObjectListByProperty( EntityManager entityManager, Class< ? > clazz, String propertyName, Object value );

    /**
     * Gets the latest non deleted object list by not null property.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param propertyName
     *         the property name
     *
     * @return the latest non deleted object list by not null property
     */
    List< E > getLatestNonDeletedObjectListByNotNullProperty( EntityManager entityManager, Class< ? > clazz, String propertyName );

    /**
     * Gets the non deleted object list.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the non deleted object list
     */
    List< E > getNonDeletedObjectList( EntityManager entityManager );

    /**
     * Gets all children by parent id with permission.
     *
     * @param entityManager
     *         the entity manager
     * @param suSEntityClass
     *         the su s entity class
     * @param id
     *         the id
     * @param userId
     *         the user id
     * @param permission
     *         the permission
     *
     * @return the all children by parent id with permission
     */
    List< E > getAllChildrenByParentIdWithPermission( EntityManager entityManager, Class< ? > suSEntityClass, UUID id, UUID userId,
            int permission );

    List< ? > getAllFilteredRecordsWithParentAndLifeCycleAndLanguageAndPermissionForList( EntityManager entityManager,
            Class< ? > entityClazz, FiltersDTO filtersDTO, UUID parentId, String userId, List< String > ownerVisibleStatus,
            List< String > anyVisibleStatus, List< ObjectType > objectTypes, String language, int permission );

    /**
     * Gets the all filtered records with parent and life cycle.
     *
     * @param entityManager
     *         the entity manager
     * @param filtersDTO
     *         the filters DTO
     * @param parentId
     *         the parent id
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     * @param objectTypes
     *         the object types
     *
     * @return the all filtered records with parent and life cycle
     */
    List< ? > getAllFilteredRecordsWithParentAndLifeCycleWithLanguageAndPermissionWithoutCount( EntityManager entityManager,
            Class< ? > entityClazz, FiltersDTO filtersDTO, UUID parentId, String userId, List< String > ownerVisibleStatus,
            List< String > anyVisibleStatus, List< ObjectType > objectTypes, String language, int permission );

}
