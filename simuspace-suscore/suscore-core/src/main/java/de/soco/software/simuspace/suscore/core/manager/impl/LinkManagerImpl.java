package de.soco.software.simuspace.suscore.core.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDbOperationTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.core.dao.LinkDAO;
import de.soco.software.simuspace.suscore.core.manager.LinkManager;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.ProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;

/**
 * The Class LinkManagerImpl.
 *
 * @author Ahsan.Khan
 */
@Log4j2
public class LinkManagerImpl implements LinkManager {

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The object type config manager.
     */
    private ObjectTypeConfigManager configManager;

    /**
     * The link DAO.
     */
    private LinkDAO linkDAO;

    /**
     * The Entity manager factory reference.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Constant LINK.
     */
    private static final int LINK = 1;

    /**
     * The Constant WORKFLOW_PROJECT_CLASS_NAME.
     */
    private static final String WORKFLOW_PROJECT_CLASS_NAME = "de.soco.software.simuspace.suscore.data.model.WorkflowProjectDTO";

    /**
     * The Constant WORKFLOW_ID.
     */
    private static final String WORKFLOW_CLASS_NAME = "de.soco.software.simuspace.suscore.data.model.WorkFlowDTOConf";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean objectsLinking( UUID userId, UUID linkSrc, UUID linkTarget ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UUID > itemsTargets = selectionManager.getSelectedIdsListBySelectionId( entityManager, linkTarget.toString() );
            if ( CollectionUtils.isEmpty( itemsTargets ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.TARGET_ITEM_NOT_AVAILABE.getKey() ) );
            } else {
                log.info( "Selected items target" + linkTarget );
            }

            List< SuSEntity > objectsLinkingTo = susDAO.getLatestNonDeletedObjectsByIds( entityManager, itemsTargets );

            if ( CollectionUtils.isEmpty( objectsLinkingTo ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.TARGET_LINKING_OBJECTS_NOT_AVAILABE.getKey() ) );
            }
            SuSEntity singleObject = susDAO.getLatestNonDeletedObjectById( entityManager, linkSrc );
            if ( singleObject == null ) {
                List< UUID > itemsSrc = selectionManager.getSelectedIdsListBySelectionId( entityManager, linkSrc.toString() );
                if ( CollectionUtils.isEmpty( itemsSrc ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.SOURCE_ITEM_NOT_AVAILABE.getKey() ) );
                } else {
                    log.info( "Selected items src" + itemsSrc );
                }

                List< SuSEntity > objectsLinkingFrom = susDAO.getLatestNonDeletedObjectsByIds( entityManager, itemsSrc );
                if ( CollectionUtils.isEmpty( objectsLinkingFrom ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.SOURCE_LINKING_OBJECTS_NOT_AVAILABE.getKey() ) );
                } else {
                    for ( SuSEntity objectLinkFrom : objectsLinkingFrom ) {
                        linkObject( entityManager, userId, objectsLinkingTo, objectLinkFrom );
                    }
                }
            } else {
                log.info( "Selected item src" + linkTarget );
                linkObject( entityManager, userId, objectsLinkingTo, singleObject );
            }
        } finally {
            entityManager.close();
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeLinkObjectById( UUID userId, UUID objectId, UUID selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UUID > selectedObjects = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId.toString() );
            if ( CollectionUtils.isEmpty( selectedObjects ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.SELECTION_ITEMS_NOT_SAVED.getKey() ) );
            } else {
                for ( UUID linkedFromId : selectedObjects ) {
                    Relation relation = validateObjectOrContainer( entityManager, objectId, linkedFromId );
                    if ( relation == null ) {
                        throw new SusException(
                                MessageBundleFactory.getMessage( Messages.LINK_NOT_AVAILABLE.getKey(), linkedFromId, objectId ) );
                    }
                }
                for ( UUID linkedFromId : selectedObjects ) {
                    Relation relation = validateObjectOrContainer( entityManager, objectId, linkedFromId );
                    linkDAO.delete( entityManager, relation );
                }
            }
        } finally {
            entityManager.close();
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getLinkedRelationByChildId( UUID child ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< Relation > relation;
        try {
            relation = linkDAO.getLinkedRelationByChildId( entityManager, child );
        } finally {
            entityManager.close();
        }
        return CollectionUtils.isNotEmpty( relation );
    }

    /**
     * Validate object or container.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param linkedFromId
     *         the linked from id
     *
     * @return the relation
     */
    private Relation validateObjectOrContainer( EntityManager entityManager, UUID objectId, UUID linkedFromId ) {

        Relation relation = linkDAO.getRelationLinkByParentIdAndChildId( entityManager, objectId, linkedFromId );
        if ( relation == null ) {
            relation = linkDAO.getRelationLinkByParentIdAndChildId( entityManager, linkedFromId, objectId );
        }
        return relation;
    }

    /**
     * Sets the link from link object.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectsLinkingTo
     *         the objects linking to
     * @param objectLinkFrom
     *         the object link from
     */
    private void linkObject( EntityManager entityManager, UUID userId, List< SuSEntity > objectsLinkingTo, SuSEntity objectLinkFrom ) {
        List< String > bloodline = new ArrayList<>();
        recursivelyGetAllParents( entityManager, objectLinkFrom.getComposedId().getId(), bloodline );
        if ( !bloodline.isEmpty() ) {
            for ( SuSEntity suSEntity : objectsLinkingTo ) {
                if ( bloodline.contains( suSEntity.getComposedId().getId().toString() ) ) {
                    throw new SusException( "Linking is not allowed in parent and child" );
                }
            }
        }
        Set< String > srcContainsClassName = new HashSet<>();

        SuSObjectModel susObjectModelSrc = configManager.getObjectTypeByIdAndConfigName( objectLinkFrom.getTypeId().toString(),
                objectLinkFrom.getConfig() );

        List< SuSObjectModel > susObjectModelSrcConfigAllObjects = configManager.getObjectTypesByConfigName( objectLinkFrom.getConfig() );

        for ( String className : susObjectModelSrc.getLinks() ) {
            for ( SuSObjectModel suSObjectModel : susObjectModelSrcConfigAllObjects ) {
                if ( className.equals( suSObjectModel.getClassName() ) ) {
                    srcContainsClassName.add( suSObjectModel.getClassName() );
                }
            }
        }

        for ( SuSEntity objectLinkTo : objectsLinkingTo ) {
            validate( entityManager, susObjectModelSrc, objectLinkFrom, objectLinkTo, srcContainsClassName );
        }
        for ( SuSEntity objectLinkTo : objectsLinkingTo ) {
            Relation relationFrom = new Relation( objectLinkFrom.getComposedId().getId(), objectLinkTo.getComposedId().getId() );
            relationFrom.setType( LINK );
            relationFrom
                    .setAuditLogEntity(
                            AuditLogDTO.prepareAuditLogEntityForSusEntity(
                                    "Link from : " + relationFrom.getParent() + ConstantsString.SPACE + "Link to : "
                                            + relationFrom.getChild() + ConstantsString.SPACE,
                                    ConstantsDbOperationTypes.CREATED, userId.toString(), objectLinkFrom ) );
            linkDAO.save( entityManager, relationFrom );
        }
    }

    private void recursivelyGetAllParents( EntityManager entityManager, UUID id, List< String > bloodline ) {
        List< Relation > relation = linkDAO.getRelationWithParentByChildId( entityManager, id );
        if ( relation != null && !relation.isEmpty() && relation.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getParent() != null ) {
            bloodline.add( relation.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getParent().toString() );
            recursivelyGetAllParents( entityManager, relation.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getParent(), bloodline );
        }
    }

    /**
     * Validate.
     *
     * @param entityManager
     *         the entity manager
     * @param susObjectModelSrc
     *         the sus object model src
     * @param objectLinkFrom
     *         the object link from
     * @param objectLinkTo
     *         the object link to
     * @param srcContainsClassName
     *         the src contains class name
     */
    private void validate( EntityManager entityManager, SuSObjectModel susObjectModelSrc, SuSEntity objectLinkFrom, SuSEntity objectLinkTo,
            Set< String > srcContainsClassName ) {
        Relation alreadyAvailableRelation = linkDAO.getRelationLinkByParentIdAndChildId( entityManager,
                objectLinkFrom.getComposedId().getId(), objectLinkTo.getComposedId().getId() );

        if ( alreadyAvailableRelation != null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.LINK_ALREADY_EXIST.getKey() ) );
        }

        Relation reversedRelation = linkDAO.getRelationLinkByParentIdAndChildId( entityManager, objectLinkTo.getComposedId().getId(),
                objectLinkFrom.getComposedId().getId() );
        if ( reversedRelation != null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.REVERSE_LINK_IS_NOT_APPLICABLE.getKey() ) );
        }
        if ( objectLinkFrom instanceof ProjectEntity projectEntity && "Label".equals( projectEntity.getType() ) ) {

            throw new SusException( "Can Not Link Objects with a Label" );

        }

        if ( objectLinkFrom.getComposedId().getId().equals( objectLinkTo.getComposedId().getId() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.LINK_TO_ITSELF_NOT_ALLOWED.getKey() ) );
        }

        List< SuSEntity > sourceChildren = susDAO.getAllRecordsWithParent( entityManager, SuSEntity.class,
                objectLinkFrom.getComposedId().getId() );
        if ( CollectionUtils.isNotEmpty( sourceChildren ) ) {
            for ( SuSEntity suSEntity : sourceChildren ) {
                if ( suSEntity.getName().equals( objectLinkTo.getName() ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.LINK_NOT_POSSIBLE_WITH_SAME_NAME.getKey() ) );
                }
            }
        }

        SuSObjectModel susObjectModelTarget = configManager.getObjectTypeByIdAndConfigName( objectLinkTo.getTypeId().toString(),
                objectLinkTo.getConfig() );

        /* If target and source have same configuration **/
        if ( objectLinkFrom.getConfig().equals( objectLinkTo.getConfig() ) ) {
            validateSameConfigFIles( susObjectModelSrc, srcContainsClassName, susObjectModelTarget );
        } else {
            validateDifferentConfigFile( susObjectModelSrc, objectLinkTo, srcContainsClassName, susObjectModelTarget );
        }
    }

    /**
     * Validate different config file.
     *
     * @param susObjectModelSrc
     *         the sus object model src
     * @param objectLinkTo
     *         the object link to
     * @param srcContainsClassName
     *         the src contains class name
     */
    private void validateDifferentConfigFile( SuSObjectModel susObjectModelSrc, SuSEntity objectLinkTo, Set< String > srcContainsClassName,
            SuSObjectModel susObjectModelTarget ) {
        Set< String > targerContainsClassName = new HashSet<>();
        List< SuSObjectModel > susObjectModelTargetConfigAllObjects = configManager.getObjectTypesByConfigName( objectLinkTo.getConfig() );

        for ( String className : susObjectModelSrc.getLinks() ) {
            for ( SuSObjectModel suSObjectModel : susObjectModelTargetConfigAllObjects ) {
                if ( className.equals( suSObjectModel.getClassName() ) ) {
                    targerContainsClassName.add( suSObjectModel.getClassName() );
                }
            }
        }

        if ( CollectionUtils.isNotEmpty( targerContainsClassName ) && CollectionUtils.isNotEmpty( srcContainsClassName )
                && !srcContainsClassName.containsAll( targerContainsClassName ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SOURCE_MUST_CONTAIN_TARGET.getKey() ) );
        }
        validateSameConfigFIles( susObjectModelSrc, srcContainsClassName, susObjectModelTarget );
    }

    /**
     * Validate same config F iles.
     *
     * @param susObjectModelSrc
     *         the sus object model src
     * @param srcContainsClassName
     *         the src contains class name
     * @param susObjectModelTarget
     *         the sus object model target
     */
    private void validateSameConfigFIles( SuSObjectModel susObjectModelSrc, Set< String > srcContainsClassName,
            SuSObjectModel susObjectModelTarget ) {
        if ( CollectionUtils.isEmpty( srcContainsClassName ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SOURCE_MUST_CONTAIN_TARGET.getKey() ) );
        }
        if ( susObjectModelSrc.getClassName().equals( WORKFLOW_PROJECT_CLASS_NAME )
                || susObjectModelSrc.getClassName().equals( WORKFLOW_CLASS_NAME ) ) {
            for ( String string : srcContainsClassName ) {
                Object object = initializeObject( string );
                if ( !( object instanceof ContainerEntity ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.ONLY_CONTAINER_IS_ALLOWED.getKey() ) );
                }
            }
        } else if ( ( !srcContainsClassName.contains( susObjectModelTarget.getClassName() ) ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SOURCE_MUST_CONTAIN_TARGET.getKey() ) );
        }
    }

    /**
     * Initialize object.
     *
     * @param className
     *         the class name
     *
     * @return the object
     */
    private static Object initializeObject( String className ) {

        try {
            return Class.forName( className ).newInstance();
        } catch ( InstantiationException | IllegalAccessException | ClassNotFoundException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.CLASS_NOT_FOUND_OR_NOT_ABLE_TO_INITIALIZE.getKey(), className ), e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.CLASS_NOT_FOUND_OR_NOT_ABLE_TO_INITIALIZE.getKey(), className ) );
        }
    }

    /**
     * Gets the sus DAO.
     *
     * @return the sus DAO
     */
    public SuSGenericObjectDAO< SuSEntity > getSusDAO() {
        return susDAO;
    }

    /**
     * Sets the sus DAO.
     *
     * @param susDAO
     *         the new sus DAO
     */
    public void setSusDAO( SuSGenericObjectDAO< SuSEntity > susDAO ) {
        this.susDAO = susDAO;
    }

    /**
     * Gets the selection manager.
     *
     * @return the selection manager
     */
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    /**
     * Sets the selection manager.
     *
     * @param selectionManager
     *         the new selection manager
     */
    public void setSelectionManager( SelectionManager selectionManager ) {
        this.selectionManager = selectionManager;
    }

    /**
     * Gets the link DAO.
     *
     * @return the link DAO
     */
    public LinkDAO getLinkDAO() {
        return linkDAO;
    }

    /**
     * Sets the link DAO.
     *
     * @param linkDAO
     *         the new link DAO
     */
    public void setLinkDAO( LinkDAO linkDAO ) {
        this.linkDAO = linkDAO;
    }

    /**
     * Gets the config manager.
     *
     * @return the config manager
     */
    public ObjectTypeConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Sets the config manager.
     *
     * @param configManager
     *         the new config manager
     */
    public void setConfigManager( ObjectTypeConfigManager configManager ) {
        this.configManager = configManager;
    }

    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}