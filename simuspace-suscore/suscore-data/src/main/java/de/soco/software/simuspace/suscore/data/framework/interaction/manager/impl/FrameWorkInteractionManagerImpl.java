package de.soco.software.simuspace.suscore.data.framework.interaction.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.data.activator.Activator;
import de.soco.software.simuspace.suscore.data.common.dao.AboutMenuDAO;
import de.soco.software.simuspace.suscore.data.common.model.FrameWorkStatusDTO;
import de.soco.software.simuspace.suscore.data.entity.AboutSimuspaceEntity;
import de.soco.software.simuspace.suscore.data.framework.interaction.manager.FrameWorkInteractionManager;

/**
 * The Class FrameWorkInteractionManagerImpl to get bundle status.
 */
public class FrameWorkInteractionManagerImpl implements FrameWorkInteractionManager {

    /**
     * The about menu DAO.
     */
    private AboutMenuDAO aboutMenuDAO;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public FrameWorkStatusDTO getStatus() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            FrameWorkStatusDTO frameWorkStatusDTO = new FrameWorkStatusDTO();
            AboutSimuspaceEntity aboutEntity = aboutMenuDAO.getLatestObjectById( entityManager, AboutSimuspaceEntity.class,
                    UUID.fromString( ConstantsID.ABOUT_MENU_ID ) );
            if ( null != aboutEntity ) {
                frameWorkStatusDTO.setVersion( aboutEntity.getBuildBeSimuspace() );
            }
            frameWorkStatusDTO.setUp( Activator.isLifeCycleUp() );
            return frameWorkStatusDTO;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Sets the about menu DAO.
     *
     * @param aboutMenuDAO
     *         the new about menu DAO
     */
    public void setAboutMenuDAO( AboutMenuDAO aboutMenuDAO ) {
        this.aboutMenuDAO = aboutMenuDAO;
    }

    /**
     * Sets entity manager factory.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}
