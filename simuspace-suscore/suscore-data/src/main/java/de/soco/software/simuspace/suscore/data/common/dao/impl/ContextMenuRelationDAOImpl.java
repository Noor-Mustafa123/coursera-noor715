package de.soco.software.simuspace.suscore.data.common.dao.impl;

import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.common.dao.ContextMenuRelationDAO;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

public class ContextMenuRelationDAOImpl extends AbstractGenericDAO< Relation > implements ContextMenuRelationDAO {

    /**
     * The Constant LINK.
     */
    private static final Object LINK = 1;

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

}
