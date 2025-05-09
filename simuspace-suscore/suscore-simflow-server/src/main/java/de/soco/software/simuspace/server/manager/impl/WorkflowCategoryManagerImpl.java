package de.soco.software.simuspace.server.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.server.dao.WorkflowCategoryDAO;
import de.soco.software.simuspace.server.manager.BaseManager;
import de.soco.software.simuspace.server.manager.CategoryManager;
import de.soco.software.simuspace.server.manager.WorkflowCategoryManager;
import de.soco.software.simuspace.server.manager.WorkflowManager;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.CategoryEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowCategoryEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.workflow.dto.WorkflowDTO;
import de.soco.software.simuspace.workflow.model.Category;

/**
 * To manage the Category and workflow Relation operation to Dao layer Feature no longer in use. Marked for removal
 *
 * @author Nosheen.Sharif
 */
@Deprecated( forRemoval = true )
public class WorkflowCategoryManagerImpl extends BaseManager implements WorkflowCategoryManager {

    /**
     * The workflow manager reference.
     */
    private WorkflowManager workflowManager;

    /**
     * The category manager reference.
     */
    private CategoryManager categoryManager;

    /**
     * The workflow cateogory DAO reference.
     */
    private WorkflowCategoryDAO workflowCategoryDao;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The constructor which instantiates an object of the class
     */
    public WorkflowCategoryManagerImpl() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean assignCategoriesToWorkflow( UUID userId, String workflowId, List< String > categoryIdsList ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // validates if the user has the role workflow manager, else throws exception
        try {
            isWorkflowManager( entityManager, userId );

            final List< WorkflowCategoryEntity > list = new ArrayList<>();
            final WorkflowDTO workflowDto = workflowManager.getWorkflowById( entityManager, userId, workflowId );
            if ( workflowDto != null ) {
                if ( categoryIdsList != null ) {
                    for ( final String id : categoryIdsList ) {

                        final Category category = categoryManager.getCategoryById( entityManager, id );
                        if ( category != null ) {
                            list.add( prepareWorkflowCategoryEntity( workflowDto, category, true ) );
                        }
                    }
                }
            } else {
                throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID, workflowId ) ),
                        getClass() );
            }
            return workflowCategoryDao.assignCategoriesToWorkflow( entityManager, list, UUID.fromString( workflowDto.getId() ) );
        } finally {
            entityManager.close();
        }

    }

    /**
     * Prepare workflow category entity.
     *
     * @param workflow
     *         the workflow
     * @param category
     *         the category
     * @param isAssign
     *         the boolean
     *
     * @return the workflow category entity
     */
    private WorkflowCategoryEntity prepareWorkflowCategoryEntity( WorkflowDTO workflow, Category category, boolean isAssign ) {
        final WorkflowCategoryEntity returnEntity = new WorkflowCategoryEntity();
        if ( isAssign ) {
            returnEntity.setId( UUID.randomUUID() );
        }
        final WorkflowEntity entity = new WorkflowEntity();

        entity.setComposedId( new VersionPrimaryKey( UUID.fromString( workflow.getId() ), workflow.getVersion().getId() ) );

        returnEntity.setWorkflow( entity );

        returnEntity.setCategory( new CategoryEntity( UUID.fromString( category.getId() ) ) );

        return returnEntity;
    }

    /**
     * Gets the workflow manager.
     *
     * @return the workflow manager
     */
    public WorkflowManager getWorkflowManager() {
        return workflowManager;
    }

    /**
     * Sets the workflow manager.
     *
     * @param workflowManager
     *         the new workflow manager
     */
    public void setWorkflowManager( WorkflowManager workflowManager ) {
        this.workflowManager = workflowManager;
    }

    /**
     * Gets the category manager.
     *
     * @return the category manager
     */
    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    /**
     * Sets the category manager.
     *
     * @param categoryManager
     *         the new category manager
     */
    public void setCategoryManager( CategoryManager categoryManager ) {
        this.categoryManager = categoryManager;
    }

    /**
     * Gets the workflow category dao.
     *
     * @return the workflow category dao
     */
    public WorkflowCategoryDAO getWorkflowCategoryDao() {
        return workflowCategoryDao;
    }

    /**
     * Sets the workflow category dao.
     *
     * @param workflowCategoryDao
     *         the new workflow category dao
     */
    public void setWorkflowCategoryDao( WorkflowCategoryDAO workflowCategoryDao ) {
        this.workflowCategoryDao = workflowCategoryDao;
    }

    /**
     * Gets the license manager.
     *
     * @return the license manager
     */
    public LicenseManager getLicenseManager() {
        return licenseManager;
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