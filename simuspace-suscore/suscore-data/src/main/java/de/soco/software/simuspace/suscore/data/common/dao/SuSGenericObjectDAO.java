package de.soco.software.simuspace.suscore.data.common.dao;

import javax.persistence.EntityManager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.data.entity.Category;
import de.soco.software.simuspace.suscore.data.entity.CustomAttributeEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.MetaDataFile;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;
import de.soco.software.simuspace.suscore.data.model.ObjectTreeViewDTO;

/**
 * This interface is provides the general API function from SuS.Core, which can be used by configured types, or external plugins. Any
 * configured types use the same base functions.It also provides the basic CRUD operations on different object types.
 *
 * @param <T>
 *         the generic type
 *
 * @author fahad
 */
public interface SuSGenericObjectDAO< T extends Serializable > extends GenericDAO< T > {

    /**
     * It creates an object and returns the created one . It is A general API function from SuS.Core, which can be used by configured types,
     * or external plugins. Any configured types use the same base functions.
     *
     * @param entityManager
     *         the entity manager
     * @param t
     *         the t
     *
     * @return object t
     */
    T createAnObject( EntityManager entityManager, T t );

    /**
     * It creates an object with new version and returns the created one . It is A general API function from SuS.Core, which can be used by
     * configured types, or external plugins. Any configured types use the same base functions.
     *
     * @param t
     *         the t
     *
     * @return object
     */
    T updateAnObject( EntityManager entityManager, T t );

    /**
     * Creates the relation.
     *
     * @param entityManager
     *         the entity manager
     * @param r
     *         the r
     *
     * @return the relation
     */
    Relation createRelation( EntityManager entityManager, Relation r );

    /**
     * Gets the list by property desc.
     *
     * @param entityManager
     *         the entity manager
     * @param propertyName
     *         the property name
     * @param value
     *         the value
     *
     * @return the list by property desc
     */
    List< Relation > getListByPropertyDesc( EntityManager entityManager, String propertyName, Object value );

    /**
     * Gets the parents.
     *
     * @param entityManager
     *         the entity manager
     * @param t
     *         the t
     *
     * @return the parents
     */
    List< T > getParents( EntityManager entityManager, T t );

    /**
     * Gets the deleted parents.
     *
     * @param entityManager
     *         the entity manager
     * @param t
     *         the t
     *
     * @return the deleted parents
     */
    List< T > getDeletedParents( EntityManager entityManager, T t );

    /**
     * get list of all children of particular object passed.
     *
     * @param entityManager
     *         the entity manager
     * @param t
     *         the t
     *
     * @return the children
     */
    List< T > getChildren( EntityManager entityManager, T t );

    /**
     * get all parents of particular object with filter passed.
     *
     * @param entityManager
     *         the entity manager
     * @param t
     *         the t
     * @param filter
     *         the filter
     *
     * @return List parents
     */
    List< T > getParents( EntityManager entityManager, T t, ObjectTreeViewDTO filter );

    /**
     * get all children of particular object with filter passed.
     *
     * @param entityManager
     *         the entity manager
     * @param t
     *         the t
     * @param filter
     *         the filter
     *
     * @return the children
     */
    List< T > getChildren( EntityManager entityManager, T t, ObjectTreeViewDTO filter );

    /**
     * It create or update an object and returns the created or updated one . It is A general API function from SuS.Core, which can be used
     * by configured types, or external plugins. Any configured types use the same base functions.
     *
     * @param entityManager
     *         the entity manager
     * @param t
     *         the t
     *
     * @return object t
     */
    T saveOrUpdateObject( EntityManager entityManager, T t );

    /**
     * It creates a container and returned the created one. It basically change the object tree container .It is A general API function from
     * SuS.Core, which can be used by configured types, or external plugins. Any configured types use the same base functions.
     *
     * @param t
     *         the t
     *
     * @return the t
     */
    T addContainer( T t );

    /**
     * Removes the object from the tree structure . It is A general API function from SuS.Core, which can be used by configured types, or
     * external plugins. Any configured types use the same base functions
     *
     * @param t
     *         the t
     *
     * @return boolean
     */
    boolean removeObject( T t );

    /**
     * Removes the container from the tree structure . It is A general API function from SuS.Core, which can be used by configured types, or
     * external plugins. Any configured types use the same base functions
     *
     * @param t
     *         the t
     *
     * @return boolean
     */
    boolean removeContainer( T t );

    /**
     * Add the object to a container specified. After successfull adding it will return the successfull container.
     *
     * @param object
     *         the object
     * @param container
     *         the container
     *
     * @return object
     */
    T addObjectToContainer( T object, T container );

    /**
     * Add the object to more then one containers A general API function , which can link an object to many other containers
     * Example:Loadcase Configuration can add a link to a Loadcase. Loadcase is defined globally and can have own structure.
     *
     * @param object
     *         the object
     * @param multipleContainer
     *         the multiple container
     *
     * @return the t
     */
    T addObjectToMultipleContainer( T object, List< T > multipleContainer );

    /**
     * Change the attribute of an object and returns the updated object. A general API function from SuS.Core, which can be used by
     * configured types, or external plugins. Any configured types use the same base functions.
     *
     * @param object
     *         the object
     *
     * @return the t
     */

    T changeAttributeOfAnObject( T object );

    /**
     * It will change the metadata of an object. A general API function from SuS.Core, which can be used by configured types, or external
     * plugins. Any configured types use the same base functions.
     *
     * @param object
     *         the object
     *
     * @return object
     */
    T changeMetadataOfAnObject( T object );

    /**
     * Add the custom attributes to an object. And return the updated Object A general API function from SuS.Core, which can be used by
     * configured types, or external plugins. Any configured types use the same base functions. Use keys to define the attributes so that
     * renaming is easy Custom attribute values can follow rules e.g. integer, text etc.. like i/o element In V2.0 custom attribute values
     * are text or dropdown only, no support for integer, textarea, radio etc
     *
     * @param object
     *         the object
     * @param customAttributeEntity
     *         the custom attribute entity
     *
     * @return the t
     */
    T addCustomAttributeToObject( T object, CustomAttributeEntity customAttributeEntity );

    /**
     * Remove the custom attributes from an object A general API function from SuS.Core, which can read the updated configuration, and keep
     * custom attributes from all instances of this type, but not show it
     *
     * @param object
     *         the object
     * @param customAttributeEntity
     *         the custom attribute entity
     *
     * @return the t
     */
    T removeCustomAttributeFromObject( T object, CustomAttributeEntity customAttributeEntity );

    /**
     * Change the custom attribute from an object A general API function from SuS.Core, which can be used by configured types, or external
     * plugins. Any configured types use the same base functions. Use keys to define the attributes so that renaming is easy Custom
     * attribute values can follow rules e.g. integer, text etc.. like i/o element In V2.0 custom attribute values are text or dropdown
     * only, no support for integer, textarea, radio etc"
     *
     * @param object
     *         return object
     * @param customAttributeEntity
     *         the custom attribute entity
     *
     * @return the t
     */
    T changeCustomAttributeOfAnObject( T object, CustomAttributeEntity customAttributeEntity );

    /**
     * Gets all property values by parent id.
     *
     * @param entityManager
     *         the entity manager
     * @param uuid
     *         the uuid
     * @param propertyName
     *         the property name
     * @param language
     *         the language
     *
     * @return the all property values by parent id
     */
    List< Object > getAllPropertyValuesByParentId( EntityManager entityManager, UUID uuid, String propertyName, String language );

    /**
     * UI Implmentation, with json configuration to implment tree structure.
     *
     * @return the the tree structure from JSON at boostrap
     */

    List< T > getTheTreeStructureFromJSONAtBoostrap();

    /**
     * created a new version of an object.
     *
     * @param object
     *         the object
     *
     * @return the t
     */
    T createNewVersionOfObject( T object );

    /**
     * Get all the project/containers
     *
     * @return List
     */
    List< T > getAllprojects();

    /**
     * Get all the versions of a specified project. return the list of all versions project has.
     *
     * @param project
     *         the project
     *
     * @return list
     */
    List< T > getAllVersionsOfProject( T project );

    /**
     * Get all the object type the container/project has.
     *
     * @param project
     *         the project
     *
     * @return the all object type in project
     */
    List< T > getAllObjectTypeInProject( T project );

    /**
     * Get the meta data file of an object.
     *
     * @return metadatfile
     */

    MetaDataFile getMetadataOfAnObject();

    /**
     * Get all the Data Objects in the container/project.
     *
     * @return the list of data objects in container
     */
    List< DataObjectEntity > getListOfDataObjectsInContainer();

    /**
     * Get all the custom attributes of an object.
     *
     * @return list
     */
    List< CustomAttributeEntity > getAllCustomAttributesOfObject();

    /**
     * Get the category of an project.
     *
     * @return Category
     */

    Category getTheCategoryOfTheProject();

    /**
     * Move a container in tree structure.
     *
     * @return container
     */

    T moveContainer();

    /**
     * Change an attribute value of an object.
     */
    void changeAttributeValueOfAnObject();

    /**
     * Change metadata of an object.
     */
    void changeMetadataOfObject();

    /**
     * Get List of Object by ObjecT Type with latest version of each object.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     *
     * @return the list of object by type
     */
    List< SuSEntity > getListOfObjectByType( EntityManager entityManager, Class< ? > clazz );

    /**
     * Get Object By Composite key.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param id
     *         the id
     * @param versionId
     *         the version id
     *
     * @return the object by composite id
     */
    T getObjectByCompositeId( EntityManager entityManager, Class< ? > clazz, UUID id, Integer versionId );

    /**
     * Get Object Version List By Id.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param id
     *         the id
     *
     * @return the object version list by id
     */
    List< T > getObjectVersionListById( EntityManager entityManager, Class< ? > clazz, UUID id );

    /**
     * Gets latest object by type and id.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param id
     *         the id
     *
     * @return the latest object by type and id
     */
    SuSEntity getLatestObjectByTypeAndId( EntityManager entityManager, Class< ? > clazz, UUID id );

    /**
     * Get Object by Id and Version Id.
     *
     * @param entityManager
     *         the entity manager
     * @param objectTypeClass
     *         the object type class
     * @param fromString
     *         the from string
     * @param version
     *         the version
     *
     * @return the object by id and version
     */
    SuSEntity getObjectByIdAndVersion( EntityManager entityManager, Class< ? > objectTypeClass, UUID fromString, int version );

    /**
     * Provides siblings with same name, ignoring case. Important for generating folder structures on filesystems.
     *
     * @param entityManager
     *         the entity manager
     * @param name
     *         to check against siblings
     * @param updateEntity
     *         to skip check for current entity if being updated, can be null
     * @param parent
     *         container to get siblings
     *
     * @return list of siblings with same name, ignoring case
     */
    List< SuSEntity > getSiblingsBySameIName( EntityManager entityManager, String name, SuSEntity updateEntity, SuSEntity parent );

    /**
     * Delete relation.
     *
     * @param entityManager
     *         the entity manager
     * @param dsf
     *         the dsf
     *
     * @return true, if successful
     */
    boolean deleteRelation( EntityManager entityManager, List< Relation > dsf );

    /**
     * Gets the non deleted object list by property.
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
     * @return the non deleted object list by property
     */
    List< SuSEntity > getNonDeletedObjectListByProperty( EntityManager entityManager, Class< ? > clazz, String propertyName, Object value );

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
    List< SuSEntity > getLatestNonDeletedObjectsByListOfIds( EntityManager entityManager, List< UUID > childs );

    List< T > getLatestNonDeletedObjectsByListOfIdsAndFilter( EntityManager entityManager, List< UUID > selectedIdUUID,
            FiltersDTO filter );

    /**
     * Gets the objects by list of ids.
     *
     * @param entityManager
     *         the entity manager
     * @param childs
     *         the childs
     *
     * @return the objects by list of ids
     */
    List< SuSEntity > getObjectsByListOfIds( EntityManager entityManager, List< UUID > childs );

    /**
     * get list of entities by property map and subClass of susEntity
     *
     * @param propertyMap
     *         the propertyMap
     * @param clazz
     *         the clazz
     *
     * @return List of entities
     */
    List< ? > getListByPropertyMapAndClass( EntityManager entityManager, Map< String, Object > propertyMap, Class< ? > clazz );

    /**
     * Gets all records count with parent.
     *
     * @param entityManager
     *         the entity manager
     * @param parentId
     *         the parent id
     *
     * @return the all records count with parent
     */
    Long getAllRecordsCountWithParent( EntityManager entityManager, UUID parentId );

}
