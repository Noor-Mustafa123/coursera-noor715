package de.soco.software.simuspace.suscore.object.threads;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;

/**
 * The Life cycle policy delete selection thread to delete extra selections
 */
@Log4j2
public class LifeCyclePolicyDeleteSelectionThread extends UserThread {

    /**
     * The context selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Constant ORIGIN_TO_DELETE.
     */
    private static final String ORIGIN_TO_DELETE = "context";

    /**
     * Instantiates a new Life cycle policy delete selection thread.
     *
     * @param selectionManager
     *         the selection manager
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public LifeCyclePolicyDeleteSelectionThread( SelectionManager selectionManager, EntityManagerFactory entityManagerFactory ) {
        super();
        this.selectionManager = selectionManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.common.base.UserThread#run()
     */
    @Override
    public void run() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            log.info( "Deletion of useless selections" );
            selectionManager.deleteAllSelectionsWithOrigin( entityManager, ORIGIN_TO_DELETE );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets selection manager.
     *
     * @return the selection manager
     */
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    /**
     * Sets selection manager.
     *
     * @param selectionManager
     *         the selection manager
     */
    public void setSelectionManager( SelectionManager selectionManager ) {
        this.selectionManager = selectionManager;
    }

}
