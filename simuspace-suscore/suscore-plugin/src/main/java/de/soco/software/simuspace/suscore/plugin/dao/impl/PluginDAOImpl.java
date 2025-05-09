package de.soco.software.simuspace.suscore.plugin.dao.impl;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.PluginEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.plugin.dao.PluginDAO;

/**
 * Implementation of PluginDAO Interface to communicate with database bank
 *
 * @author Nosheen.Sharif
 */
public class PluginDAOImpl extends AbstractGenericDAO< PluginEntity > implements PluginDAO {

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.plugin.dao.PluginDAO#savePlugin(de.soco.software.simuspace.suscore.data.entity.PluginEntity)
     */
    @Override
    public PluginEntity savePlugin( EntityManager entityManager, PluginEntity entity ) {
        return save( entityManager, entity );

    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.plugin.dao.PluginDAO#getPluginById(java.util.UUID)
     */
    @Override
    public PluginEntity getPluginById( EntityManager entityManager, UUID id ) {
        return findById( entityManager, id );
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.plugin.dao.PluginDAO#updatePlugin(de.soco.software.simuspace.suscore.data.entity.PluginEntity)
     */
    @Override
    public void updatePlugin( EntityManager entityManager, PluginEntity entity ) {
        saveOrUpdate( entityManager, entity );
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.plugin.dao.PluginDAO#getPluginList()
     */
    @Override
    public List< PluginEntity > getPluginList( EntityManager entityManager ) {
        return findAll( entityManager );
    }

}
