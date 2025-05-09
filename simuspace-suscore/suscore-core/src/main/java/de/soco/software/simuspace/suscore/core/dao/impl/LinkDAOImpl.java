package de.soco.software.simuspace.suscore.core.dao.impl;

import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.core.dao.LinkDAO;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * The Class LinkDAOImpl.
 */
public class LinkDAOImpl extends AbstractGenericDAO< Relation > implements LinkDAO {

    /**
     * The Constant LINK.
     */
    private static final Object LINK = 1;

    /**
     * The Constant RELATION.
     */
    private static final Object RELATION = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    public Relation getRelationLinkByParentIdAndChildId( EntityManager entityManager, UUID parent, UUID child ) {

        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.PARENT, parent );
        properties.put( ConstantsDAO.CHILD, child );
        properties.put( ConstantsDAO.TYPE, LINK );
        return getUniqueObjectByProperties( entityManager, properties, Relation.class );
    }

    @Override
    public List< Relation > getLinkedRelationByChildId( EntityManager entityManager, UUID child ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.CHILD, child );
        properties.put( ConstantsDAO.TYPE, LINK );
        return getListByPropertiesJpa( entityManager, properties, Relation.class, true );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Relation > getRelationWithParentByChildId( EntityManager entityManager, UUID childId ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.CHILD, childId );
        properties.put( ConstantsDAO.TYPE, RELATION );
        return getListByPropertiesJpa( entityManager, properties, Relation.class, true );
    }

}