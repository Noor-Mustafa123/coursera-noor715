package de.soco.software.simuspace.suscore.data.common.dao.impl;

import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.common.dao.ObjectViewDAO;
import de.soco.software.simuspace.suscore.data.entity.ObjectViewEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * The Class will be responsible for all the database operations necessary for dealing with the object's views.
 *
 * @author Zeeshan jamal
 */
public class ObjectViewDAOImpl extends AbstractGenericDAO< ObjectViewEntity > implements ObjectViewDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectViewEntity > getUserObjectViewsByKey( EntityManager entityManager, UUID userId, String objectViewKey,
            String objectId ) {
        return getListByPropertiesDescendingOrder( entityManager, fillCommonProperties( userId, objectViewKey, objectId ),
                ObjectViewEntity.class, ConstantsDAO.MODIFIED_ON );
    }

    @Override
    public ObjectViewEntity getUserDefaultObjectViewByKey( EntityManager entityManager, UUID userId, String objectViewKey,
            String objectId ) {
        Map< String, Object > properties = fillCommonProperties( userId, objectViewKey, objectId );
        properties.put( ConstantsDAO.DEFAULT_VIEW, true );
        return getUniqueObjectByProperties( entityManager, properties, ObjectViewEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewEntity getUserDefaultObjectViewByKeyAndConfig( EntityManager entityManager, UUID userId, String objectViewKey,
            String objectId, String config ) {
        Map< String, Object > properties = fillCommonProperties( userId, objectViewKey, objectId );
        properties.put( ConstantsDAO.CONFIG, config );
        properties.put( ConstantsDAO.DEFAULT_VIEW, true );
        return getUniqueObjectByProperties( entityManager, properties, ObjectViewEntity.class );
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.data.common.dao.ObjectViewDAO#isObjectViewAlreadyExists(java.lang.String, java.lang.String, de.soco.software.simuspace.suscore.data.entity.UserEntity, java.lang.String)
     */
    @Override
    public boolean isObjectViewAlreadyExists( EntityManager entityManager, String objectViewName, String objectViewKey, UUID userId,
            String objectId ) {
        Map< String, Object > properties = fillCommonProperties( userId, objectViewKey, objectId );
        properties.put( ConstantsDAO.OBJECT_VIEW_NAME, objectViewName );
        return isObjectAlreadyExistsByProperties( entityManager, properties, ObjectViewEntity.class );
    }

    /**
     * Fill common properties.
     *
     * @param userId
     *         the user id
     * @param objectViewKey
     *         the object view key
     * @param objectId
     *         the object id
     *
     * @return the map
     */
    private Map< String, Object > fillCommonProperties( UUID userId, String objectViewKey, String objectId ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.CREATED_BY_ID, userId );
        properties.put( ConstantsDAO.OBJECT_VIEW_KEY, objectViewKey );
        properties.put( ConstantsDAO.IS_DELETE, false );
        if ( ValidationUtils.validateUUIDString( objectId ) ) {
            properties.put( ConstantsDAO.OBJECT_ID, UUID.fromString( objectId ) );
        }
        return properties;
    }

    /**
     * Fill common properties.
     *
     * @param objectViewKey
     *         the object view key
     *
     * @return the map
     */
    private Map< String, Object > fillCommonProperties( String objectViewKey ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.OBJECT_VIEW_KEY, objectViewKey );
        properties.put( ConstantsDAO.IS_DELETE, false );
        return properties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectViewEntity > getUserObjectViewsByKeyAndConfig( EntityManager entityManager, UUID userId, String objectViewkey,
            String config ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.CREATED_BY_ID, userId );
        properties.put( ConstantsDAO.OBJECT_VIEW_KEY, objectViewkey );
        properties.put( ConstantsDAO.CONFIG, config );
        properties.put( ConstantsDAO.IS_DELETE, false );
        return getListByPropertiesDescendingOrder( entityManager, properties, ObjectViewEntity.class, ConstantsDAO.MODIFIED_ON );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectViewEntity > getUserObjectViewsByKeyAndName( EntityManager entityManager, UUID userId, String objectViewkey,
            String name ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.CREATED_BY_ID, userId );
        properties.put( ConstantsDAO.OBJECT_VIEW_KEY, objectViewkey );
        properties.put( ConstantsDAO.OBJECT_VIEW_NAME, name );
        properties.put( ConstantsDAO.IS_DELETE, false );
        return getListByPropertiesDescendingOrder( entityManager, properties, ObjectViewEntity.class, ConstantsDAO.MODIFIED_ON );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectViewEntity > getAllObjectViewsByKey( EntityManager entityManager, String objectViewKey ) {
        return getListByPropertiesDescendingOrder( entityManager, fillCommonProperties( objectViewKey ), ObjectViewEntity.class,
                ConstantsDAO.MODIFIED_ON );
    }

}
