package de.soco.software.simuspace.server.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.server.dao.CategoryDAO;
import de.soco.software.simuspace.server.manager.BaseManager;
import de.soco.software.simuspace.server.manager.CategoryManager;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.CategoryEntity;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.workflow.constant.ConstantsElementProps;
import de.soco.software.simuspace.workflow.model.Category;
import de.soco.software.simuspace.workflow.model.impl.CategoryImpl;

/**
 * To manage the Category CRUD (and other) operation to Dao layer This class and related Entities are no longer in use. marked for removal
 *
 * @author Nosheen.Sharif
 */
@Deprecated( forRemoval = true )
public class CategoryManagerImpl extends BaseManager implements CategoryManager {

    /**
     * The category DAO reference for Database calls.
     */
    private CategoryDAO categoryDAO;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The constructor which instantiates an object of the class
     */
    public CategoryManagerImpl() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Category > getCategories() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final List< Category > list = new ArrayList<>();
            for ( final CategoryEntity entity : categoryDAO.getCategories( entityManager ) ) {
                list.add( prepareCategoryModel( entityManager, entity ) );
            }
            return list;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category addCategory( UUID userId, Category categoryModel ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // validates if the user has the role workflow manager, else throws exception
            isWorkflowManager( entityManager, userId );
            final Notification notification = StringUtils.validateFieldAndLength( categoryModel.getName(), ConstantsElementProps.NAME,
                    ConstantsLength.STANDARD_NAME_LENGTH, false, true );
            if ( notification.hasErrors() ) {
                throw new SusException( new Exception( notification.getErrors().toString() ), getClass() );
            }

            final CategoryEntity entity = prepareCategoryEntity( categoryModel, false );
            final CategoryEntity retrunEntity = categoryDAO.addCategory( entityManager, entity );
            return prepareCategoryModel( entityManager, retrunEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category updateCategory( UUID userId, Category categoryModel ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // validates if the user has the role workflow manager, else throws exception
            isWorkflowManager( entityManager, userId );
            if ( !StringUtils.validateUUIDString( categoryModel.getId() ) ) {
                throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.INVALID_UUID, categoryModel.getId() ) ),
                        getClass() );
            }

            final Notification notification = StringUtils.validateFieldAndLength( categoryModel.getName(), ConstantsElementProps.NAME,
                    ConstantsLength.STANDARD_NAME_LENGTH, false, true );
            if ( notification.hasErrors() ) {
                throw new SusException( new Exception( notification.getErrors().toString() ), getClass() );
            }
            // check if category exist in db
            final CategoryEntity retEntity = categoryDAO.getCategoryById( entityManager, UUID.fromString( categoryModel.getId() ) );
            if ( retEntity == null ) {
                throw new SusException(
                        new Exception( MessagesUtil.getMessage( WFEMessages.CATEGORY_DOES_NOT_EXIST, categoryModel.getName() ) ),
                        this.getClass() );
            }

            final CategoryEntity entity = prepareCategoryEntity( categoryModel, true );
            final CategoryEntity retrunEntity = categoryDAO.updateCategory( entityManager, entity );
            return prepareCategoryModel( entityManager, retrunEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteCategory( UUID userId, String categoryId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // validates if the user has the role workflow manager, else throws exception
            isWorkflowManager( entityManager, userId );

            if ( !StringUtils.validateUUIDString( categoryId ) ) {
                throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.INVALID_UUID, categoryId ) ), getClass() );
            }
            final UUID catId = UUID.fromString( categoryId );
            final CategoryEntity entity = categoryDAO.getCategoryById( entityManager, catId );
            if ( entity != null ) {
                return categoryDAO.removeCategory( entityManager, catId );
            } else {
                throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.NO_CATEGORY_EXIST_WITH_ID, categoryId ) ),
                        this.getClass() );
            }
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    @Override
    public Category getCategoryById( String categoryId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getCategoryById( entityManager, categoryId );
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category getCategoryById( EntityManager entityManager, String categoryId ) {
        if ( !StringUtils.validateUUIDString( categoryId ) ) {
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.INVALID_UUID, categoryId ) ), getClass() );
        }
        final CategoryEntity entity = categoryDAO.getCategoryById( entityManager, UUID.fromString( categoryId ) );
        if ( entity != null ) {
            return prepareCategoryModel( entityManager, entity );
        } else {
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.NO_CATEGORY_EXIST_WITH_ID, categoryId ) ),
                    this.getClass() );
        }
    }

    /**
     * Prepare category model(for UI) from categoryEntity.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return the category impl
     */
    private Category prepareCategoryModel( EntityManager entityManager, CategoryEntity entity ) {
        final Category model = new CategoryImpl();
        model.setId( entity.getId().toString() );
        model.setName( entity.getName() );
        model.setCount( categoryDAO.getWorkflowCountByCategoryId( entityManager, entity.getId() ) );
        return model;
    }

    /**
     * Prepare category entity from category model to save to databank.
     *
     * @param model
     *         the model
     * @param update
     *         the update
     *
     * @return the category entity
     */
    private CategoryEntity prepareCategoryEntity( Category model, boolean update ) {
        final CategoryEntity entity = new CategoryEntity();
        if ( update ) {
            entity.setId( UUID.fromString( model.getId() ) );
        } else {
            entity.setId( UUID.randomUUID() );
        }

        entity.setName( model.getName() );
        return entity;
    }

    /**
     * Sets the category DAO.
     *
     * @param categoryDAO
     *         the new category DAO
     */
    public void setCategoryDAO( CategoryDAO categoryDAO ) {
        this.categoryDAO = categoryDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Category > getCategoryListByWorkflowId( String workflowId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final List< Category > list = new ArrayList<>();
            if ( !StringUtils.validateUUIDString( workflowId ) ) {
                throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID, workflowId ) ),
                        this.getClass() );
            }
            for ( final CategoryEntity entity : categoryDAO.getCategoryListByWorkflowId( entityManager, UUID.fromString( workflowId ) ) ) {
                list.add( prepareCategoryModel( entityManager, entity ) );
            }

            return list;
        } finally {
            entityManager.close();
        }
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
