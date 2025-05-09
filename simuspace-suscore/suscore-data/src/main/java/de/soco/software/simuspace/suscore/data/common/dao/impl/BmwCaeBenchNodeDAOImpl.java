package de.soco.software.simuspace.suscore.data.common.dao.impl;

import javax.persistence.EntityManager;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.common.dao.BmwCaeBenchNodeDAO;
import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchNodeEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * The Class BmwCaeBenchNodeDAOImpl.
 *
 * @author noman arshad
 */
public class BmwCaeBenchNodeDAOImpl extends AbstractGenericDAO< BmwCaeBenchNodeEntity > implements BmwCaeBenchNodeDAO {

    /**
     * Gets the bmw cae bench node by name.
     *
     * @param node
     *         the node
     *
     * @return the bmw cae bench node by name
     */
    @Override
    public BmwCaeBenchNodeEntity getBmwCaeBenchNodeByName( EntityManager entityManager, String node ) {
        return getObjectByProperty( entityManager, BmwCaeBenchNodeEntity.class, ConstantsDAO.NODE, node );
    }

}
