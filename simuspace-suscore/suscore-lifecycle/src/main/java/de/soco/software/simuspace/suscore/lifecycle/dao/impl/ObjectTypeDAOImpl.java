package de.soco.software.simuspace.suscore.lifecycle.dao.impl;

import javax.persistence.EntityManager;

import de.soco.software.simuspace.suscore.data.entity.ObjectJsonSchemaEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.lifecycle.dao.ObjectTypeDAO;

/**
 * @author Ahmar Nadeem
 *
 * An implementation of ObjectTypeDAO for the crud operations of Object Type Configuration.
 */
public class ObjectTypeDAOImpl extends AbstractGenericDAO< ObjectJsonSchemaEntity > implements ObjectTypeDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectJsonSchemaEntity loadLastJsonSchemaConfiguration( EntityManager entityManager, String schemaName ) {

        StringBuilder query = new StringBuilder( "SELECT ojs FROM ObjectJsonSchemaEntity ojs WHERE ojs.parentSchema = null AND ojs.name = '"
                + schemaName
                + "' AND ojs.createdOn = (SELECT MAX(ojsInner.createdOn) FROM ObjectJsonSchemaEntity ojsInner WHERE ojsInner.name = '"
                + schemaName + "')" );

        return queryUniqueObject( entityManager, query.toString(), null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectJsonSchemaEntity saveNewConfiguration( EntityManager entityManager, ObjectJsonSchemaEntity masterEntity ) {
        return save( entityManager, masterEntity );
    }

}
