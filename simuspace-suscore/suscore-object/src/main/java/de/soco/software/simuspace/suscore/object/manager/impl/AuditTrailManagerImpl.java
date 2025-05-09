package de.soco.software.simuspace.suscore.object.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.cxf.phase.PhaseInterceptorChain;

import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.AuditTrailRelationType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUrlViews;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.BundleUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.dao.TranslationDAO;
import de.soco.software.simuspace.suscore.data.common.model.AuditTrailDTO;
import de.soco.software.simuspace.suscore.data.common.model.Elements;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.common.model.NodeIdNameData;
import de.soco.software.simuspace.suscore.data.common.model.NodeIdNameDefination;
import de.soco.software.simuspace.suscore.data.common.model.SourceTargetData;
import de.soco.software.simuspace.suscore.data.common.model.SourceTargetDefination;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectImageEntity;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.TranslationEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.DataObjectDTO;
import de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.simuspace.suscore.object.manager.AuditTrailManager;
import de.soco.software.simuspace.suscore.object.manager.DataObjectManager;

/**
 * The Class AuditTrailManagerImpl.
 */
public class AuditTrailManagerImpl implements AuditTrailManager {

    /**
     * The Constant URL_TYPE.
     */
    private static final String URL_TYPE = "urlType";

    /**
     * The sus DAO.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The data manager.
     */
    private DataObjectManager dataObjectManager;

    /**
     * The job manager.
     */
    private JobManager jobManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The life cycle manager.
     */
    private LifeCycleManager lifeCycleManager;

    /**
     * The config manager.
     */
    private ObjectTypeConfigManager configManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Translation dao.
     */
    private TranslationDAO translationDAO;

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

    /**
     * Gets the life cycle manager.
     *
     * @return the life cycle manager
     */
    public LifeCycleManager getLifeCycleManager() {
        return lifeCycleManager;
    }

    /**
     * Sets the life cycle manager.
     *
     * @param lifeCycleManager
     *         the new life cycle manager
     */
    public void setLifeCycleManager( LifeCycleManager lifeCycleManager ) {
        this.lifeCycleManager = lifeCycleManager;
    }

    /**
     * Sets the data manager.
     *
     * @param dataObjectManager
     *         the new data manager
     */
    public void setDataObjectManager( DataObjectManager dataObjectManager ) {
        this.dataObjectManager = dataObjectManager;
    }

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    @Override
    public ObjectViewManager getObjectViewManager() {
        return objectViewManager;
    }

    /**
     * Sets the object view manager.
     *
     * @param objectViewManager
     *         the new object view manager
     */
    public void setObjectViewManager( ObjectViewManager objectViewManager ) {
        this.objectViewManager = objectViewManager;
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
     * Gets the job manager.
     *
     * @return the job manager
     */
    public JobManager getJobManager() {
        return jobManager;
    }

    /**
     * Sets the job manager.
     *
     * @param jobManager
     *         the new job manager
     */
    public void setJobManager( JobManager jobManager ) {
        this.jobManager = jobManager;
    }

    /**
     * Adds the edge.
     *
     * @param srcObjectId
     *         the src object id
     * @param edges
     *         the edges
     * @param targetId
     *         the target id
     */
    private void addEdge( String srcObjectId, ArrayList< SourceTargetData > edges, String targetId ) {
        SourceTargetData sourceTargetData = new SourceTargetData();
        SourceTargetDefination sourceTargetDefinition = new SourceTargetDefination();
        sourceTargetDefinition.setSource( srcObjectId );
        sourceTargetDefinition.setTarget( targetId );
        sourceTargetData.setData( sourceTargetDefinition );
        sourceTargetData.setClasses( AuditTrailRelationType.DECENDENT );
        edges.add( sourceTargetData );
    }

    /**
     * Checks if is node list has base class.
     *
     * @param nodes
     *         the nodes
     * @param baseClass
     *         the base class
     *
     * @return true, if is node list has base class
     */
    private boolean isNodeListHasBaseClass( ArrayList< NodeIdNameData > nodes, SuSEntity baseClass ) {
        String searchName2 = "";
        if ( baseClass instanceof ContainerEntity ) {
            searchName2 = baseClass.getComposedId().getId().toString();
        }
        for ( NodeIdNameData nodeIdNameData : nodes ) {
            if ( nodeIdNameData.getData().getId().equals( searchName2 ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the base class node data.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param typeId
     *         the type id
     * @param classes
     *         the classes
     * @param nodes
     *         the nodes
     * @param view
     *         the view
     */
    private void addBaseClassNodeData( String id, String name, String typeId, String classes, ArrayList< NodeIdNameData > nodes,
            String view ) {
        NodeIdNameData nodeElement = new NodeIdNameData();
        NodeIdNameDefination nodeIdNameDefination1 = new NodeIdNameDefination();
        nodeIdNameDefination1.setId( id );
        nodeIdNameDefination1.setName( name );
        nodeIdNameDefination1.setTypeId( typeId );
        nodeIdNameDefination1.setView( view );
        nodeElement.setData( nodeIdNameDefination1 );
        nodeElement.setClasses( classes );
        nodes.add( nodeElement );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI getAuditTrailTableUI( String objectId, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity susObj = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            if ( susObj != null ) {
                TableUI ui = dataObjectManager.getListOfObjectsUITableColumns( entityManager, userId, objectId,
                        susObj.getTypeId().toString() );
                for ( TableColumn tableColumn : ui.getColumns() ) {
                    if ( tableColumn.getName() != null && "thumnailImage".equals( tableColumn.getName() ) ) {
                        tableColumn.setVisible( false );
                    }
                    if ( tableColumn.getName() != null && "lifeCycleStatus".equals( tableColumn.getName() ) ) {
                        tableColumn.setFilter( null );
                    }
                }
                List< TableColumn > columns = ui.getColumns();
                Map< String, String > nameUrlValues = new HashMap<>();
                nameUrlValues.put( ConstantsString.OBJECT_KEY, ConstantsUrlViews.DATA_OBJECT_VIEW );
                nameUrlValues.put( ConstantsString.PROJECT_KEY, ConstantsUrlViews.DATA_PROJECT_VIEW );
                GUIUtils.setLinkColumn( DataObjectDTO.DATAOBJECT_NAME, URL_TYPE, nameUrlValues, columns );

                return new TableUI( columns,
                        dataObjectManager.getObjectViewList( entityManager, susObj, userId, GenericDTO.GENERIC_DTO_TYPE ) );
            } else if ( jobManager.getJobDao().getJob( entityManager, UUID.fromString( objectId ) ) != null ) {
                return jobManager.getListOfJobUITableColumns();
            }
            return new TableUI();
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Object > getAuditTrailDataByIdListOuts( String objectId, String userId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< Object > list = new ArrayList<>();
            SuSEntity susObj = susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( objectId ) );

            if ( susObj instanceof ContainerEntity ) {
                SuSObjectModel entityModel = configManager.getObjectTypeByIdAndConfigName( susObj.getTypeId().toString(),
                        susObj.getConfig() );
                List< SuSEntity > list1 = susDAO.getAllFilteredLinkedItemsWithParentAndLifeCycleForAuditTrail( entityManager,
                        SuSEntity.class, filter, UUID.fromString( objectId ), userId,
                        lifeCycleManager.getOwnerVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                        lifeCycleManager.getAnyVisibleStatusByPolicyId( entityModel.getLifeCycle() ), PermissionMatrixEnum.READ.getKey(),
                        configManager.getTypesFromConfiguration( susObj.getConfig() ), true );

                List< VersionPrimaryKey > listOfIds = list1.stream().map( SuSEntity::getComposedId ).toList();
                List< TranslationEntity > translationEntityList = translationDAO
                        .getAllTranslationsByListOfIds( entityManager, listOfIds );
                for ( SuSEntity suSEntity : list1 ) {
                    list.add( dataObjectManager.createGenericDTOFromObjectEntity( entityManager, userId, null, suSEntity,
                            translationEntityList.stream().filter( translationEntity -> translationEntity.getSuSEntity().getComposedId()
                                    .equals( suSEntity.getComposedId() ) ).toList() ) );
                }
            } else if ( susObj != null ) {
                List< TranslationEntity > translationEntityList = translationDAO
                        .getAllTranslationsByListOfIds( entityManager, Collections.singletonList( susObj.getComposedId() ) );
                if ( susObj instanceof DataObjectImageEntity ) {
                    translateName(
                            TokenizedLicenseUtil
                                    .getUser( BundleUtils.getUserTokenFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() ) ),
                            susObj,
                            translationEntityList.stream().filter(
                                            translationEntity -> translationEntity.getSuSEntity().getComposedId().equals( susObj.getComposedId() ) )
                                    .toList() );
                    list.add( dataObjectManager.createObjectImageDTOFromObjectEntity( null, susObj, false ) );
                } else {
                    list.add( dataObjectManager.createGenericDTOFromObjectEntity( entityManager, userId, null, susObj,
                            translationEntityList ) );
                }
                filter.setFilteredRecords( ( long ) ConstantsInteger.INTEGER_VALUE_ONE );
                filter.setTotalRecords( ( long ) ConstantsInteger.INTEGER_VALUE_ONE );
            }

            return PaginationUtil.constructFilteredResponse( filter, list );
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Object > getAuditTrailDataByIdListIns( String objectId, String userId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< Object > list = new ArrayList<>();
            SuSEntity susObj = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( objectId ) );

            if ( susObj instanceof ContainerEntity || susObj instanceof DataObjectEntity ) {

                List< Relation > listRels = susDAO.getListByPropertyDesc( entityManager, ConstantsDAO.CHILD, UUID.fromString( objectId ) );

                // removing duplicates
                Relation withOutDublicates = new Relation();
                for ( Relation relation : listRels ) {
                    if ( relation.getType() == AuditTrailRelationType.RELATION_USED
                            || relation.getType() == AuditTrailRelationType.RELATION_CREATED ) {
                        withOutDublicates = relation;
                        break;
                    }
                }
                listRels.clear();
                listRels.add( withOutDublicates );

                filter.setFilteredRecords( ( long ) ConstantsInteger.INTEGER_VALUE_ZERO );
                filter.setTotalRecords( ( long ) ConstantsInteger.INTEGER_VALUE_ZERO );

                for ( Relation relation : listRels ) {
                    if ( relation.getType() == AuditTrailRelationType.RELATION_USED
                            || relation.getType() == AuditTrailRelationType.RELATION_CREATED ) {
                        SuSEntity susObjs = susDAO.getLatestObjectById( entityManager, SuSEntity.class,
                                UUID.fromString( relation.getParent().toString() ) );
                        if ( susObjs instanceof ContainerEntity || susObjs instanceof DataObjectEntity ) {
                            List< VersionPrimaryKey > listOfIds = Arrays.asList( susObj.getComposedId() );
                            List< TranslationEntity > translationEntityList = translationDAO
                                    .getAllTranslationsByListOfIds( entityManager, listOfIds );
                            list.add( dataObjectManager.createGenericDTOFromObjectEntity( entityManager, userId, null, susObjs,
                                    translationEntityList ) );

                            filter.setFilteredRecords( filter.getFilteredRecords() + 1 );
                            filter.setTotalRecords( filter.getTotalRecords() + 1 );
                        }
                    }
                }
            } else {
                List< VersionPrimaryKey > listOfIds = Arrays.asList( susObj.getComposedId() );
                List< TranslationEntity > translationEntityList = translationDAO
                        .getAllTranslationsByListOfIds( entityManager, listOfIds );
                list.add(
                        dataObjectManager.createGenericDTOFromObjectEntity( entityManager, userId, null, susObj, translationEntityList ) );
                filter.setFilteredRecords( ( long ) ConstantsInteger.INTEGER_VALUE_ONE );
                filter.setTotalRecords( ( long ) ConstantsInteger.INTEGER_VALUE_ONE );
            }

            return PaginationUtil.constructFilteredResponse( filter, list );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuditTrailDTO getAuditTrailByObjectIdIns( String objectId, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity susEntity = susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( objectId ) );
            String viewLink = getViewLink( entityManager, objectId, userId, susEntity );
            AuditTrailDTO auditTrail = new AuditTrailDTO();
            Elements elements = new Elements();

            ArrayList< NodeIdNameData > nodes = new ArrayList<>();
            ArrayList< SourceTargetData > edges = new ArrayList<>();

            final List< Relation > listRels = susDAO.getListByPropertyDesc( entityManager, ConstantsDAO.CHILD,
                    UUID.fromString( objectId ) );

            UserDTO user = TokenizedLicenseUtil
                    .getUser( BundleUtils.getUserTokenFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() ) );
            List< TranslationEntity > translationEntityList = translationDAO
                    .getAllTranslationsByListOfIds( entityManager,
                            Collections.singletonList( susEntity.getComposedId() ) );
            translateName( user, susEntity, translationEntityList );
            addBaseClassNodeData( susEntity.getComposedId().getId().toString(), susEntity.getName(), susEntity.getTypeId().toString(),
                    AuditTrailRelationType.ORIGIN, nodes, viewLink );

            Iterator< Relation > list = listRels.iterator();
            while ( list.hasNext() ) {
                Relation relation = list.next();
                if ( relation.getType() == AuditTrailRelationType.RELATION_USED
                        || relation.getType() == AuditTrailRelationType.RELATION_CREATED ) {
                    addNodesAndEdgesOuts( entityManager, objectId, userId, nodes, edges, relation.getParent() );
                }
            }

            elements.setEdges( edges );
            elements.setNodes( nodes );
            auditTrail.setElements( elements );

            return auditTrail;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Translate name.
     *
     * @param user
     *         the user
     * @param entity
     *         the entity
     * @param translationEntities
     *         the translation entities
     */
    private void translateName( UserDTO user, SuSEntity entity, List< TranslationEntity > translationEntities ) {
        if ( PropertiesManager.hasTranslation() && null != user ) {
            if ( user.getId().equals( ConstantsID.SUPER_USER_ID ) ) {
                return;
            } else if ( null != user.getUserDetails() ) {
                String userLang = user.getUserDetails().iterator().next().getLanguage();
                translationEntities.forEach( translation -> {
                    if ( userLang.equals( translation.getLanguage() ) && null != translation.getName()
                            && !translation.getName().isEmpty() ) {
                        entity.setName( translation.getName() );
                    }
                } );
            }
        }
    }

    /**
     * Adds the nodes and edges outs.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     * @param nodes
     *         the nodes
     * @param edges
     *         the edges
     * @param relationId
     *         the relation id
     */
    private void addNodesAndEdgesOuts( EntityManager entityManager, String objectId, String userId, ArrayList< NodeIdNameData > nodes,
            ArrayList< SourceTargetData > edges, UUID relationId ) {
        if ( relationId != null ) {
            SuSEntity susEntity = susDAO.getLatestNonDeletedObjectById( entityManager, relationId );

            if ( susEntity == null ) {
                return;
            }

            String viewLink = null;
            viewLink = getViewLink( entityManager, objectId, userId, susEntity );

            if ( !isNodeListHasBaseClass( nodes, susEntity ) ) {

                if ( susEntity instanceof ContainerEntity && null != susEntity.getTypeId() ) {
                    addBaseClassNodeData( susEntity.getComposedId().getId().toString(), susEntity.getName(),
                            susEntity.getTypeId().toString(), AuditTrailRelationType.SIBLING_ORIGIN, nodes, viewLink );
                } else if ( ( null != susEntity.getTypeId() ) ) {
                    addBaseClassNodeData( susEntity.getComposedId().getId().toString(), susEntity.getName(),
                            susEntity.getTypeId().toString(), AuditTrailRelationType.DECENDENT, nodes, viewLink );
                }

                addEdge( susEntity.getComposedId().getId().toString(), edges, objectId );

                // if object have any parent find out
                final List< Relation > listRels = susDAO.getListByPropertyDesc( entityManager, ConstantsDAO.CHILD, relationId );
                if ( listRels != null ) {
                    for ( Relation relation : listRels ) {
                        if ( relation.getType() == AuditTrailRelationType.RELATION_USED
                                || relation.getType() == AuditTrailRelationType.RELATION_CREATED ) {
                            addNodesAndEdgesOuts( entityManager, susEntity.getComposedId().getId().toString(), userId, nodes, edges,
                                    relation.getParent() );
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets the view link.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     * @param susEntity
     *         the sus entity
     *
     * @return the view link
     */
    private String getViewLink( EntityManager entityManager, String objectId, String userId, SuSEntity susEntity ) {
        String viewLink = null;
        if ( susEntity != null ) {
            TableUI ui;
            try {
                ui = dataObjectManager.getListOfObjectsUITableColumns( entityManager, userId, objectId, susEntity.getTypeId().toString() );
            } catch ( Exception e ) {
                return viewLink;
            }
            if ( ui != null ) {
                for ( TableColumn c : ui.getColumns() ) {
                    if ( "name".equalsIgnoreCase( c.getName() ) ) {
                        return c.getRenderer().getUrl().replace( "{id}", susEntity.getComposedId().getId().toString() );
                    }
                }
            }
        }
        return viewLink;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuditTrailDTO getAuditTrailByObjectIdOuts( String objectId, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity susEntity = susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( objectId ) );
            String viewLink = getViewLink( entityManager, objectId, userId, susEntity );
            AuditTrailDTO auditTrail = new AuditTrailDTO();
            Elements elements = new Elements();

            ArrayList< NodeIdNameData > nodes = new ArrayList<>();
            ArrayList< SourceTargetData > edges = new ArrayList<>();

            final List< Relation > listRels = susDAO.getListByPropertyDesc( entityManager, ConstantsDAO.PARENT,
                    UUID.fromString( objectId ) );

            // compare relation table ids and non-deleted object ids
            compareRelationAndNonDeletedIds( entityManager, listRels );

            Iterator< Relation > list = listRels.iterator();
            if ( null != susEntity ) {
                addBaseClassNodeData( susEntity.getComposedId().getId().toString(), susEntity.getName(), susEntity.getTypeId().toString(),
                        AuditTrailRelationType.ORIGIN, nodes, viewLink );

                while ( list.hasNext() ) {
                    Relation relation = list.next();
                    if ( relation.getType() == AuditTrailRelationType.RELATION_USED
                            || relation.getType() == AuditTrailRelationType.RELATION_CREATED ) {
                        addNodesAndEdgesIns( entityManager, objectId, userId, nodes, edges, relation.getChild() );
                    }
                }
            }

            elements.setEdges( edges );
            elements.setNodes( nodes );
            auditTrail.setElements( elements );

            return auditTrail;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Compare relation and non deleted ids.
     *
     * @param listRels
     *         the list rels
     */
    private void compareRelationAndNonDeletedIds( EntityManager entityManager, final List< Relation > listRels ) {
        List< UUID > idList = new ArrayList<>();
        for ( Relation relation : listRels ) {
            idList.add( relation.getChild() );
        }

        List< SuSEntity > nondeletedObj = susDAO.getLatestNonDeletedObjectsByListOfIds( entityManager, idList );
        Iterator< Relation > list = listRels.iterator();
        while ( list.hasNext() ) {
            Relation relation = list.next();
            boolean findId = true;
            for ( SuSEntity suSEntity2 : nondeletedObj ) {
                if ( suSEntity2.getComposedId().getId().equals( relation.getChild() ) ) {
                    findId = false;
                    break;
                }
            }
            if ( findId ) {
                list.remove();
            }
        }
    }

    /**
     * Adds the nodes and edges ins.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     * @param nodes
     *         the nodes
     * @param edges
     *         the edges
     * @param relationId
     *         the relation id
     */
    private void addNodesAndEdgesIns( EntityManager entityManager, String objectId, String userId, ArrayList< NodeIdNameData > nodes,
            ArrayList< SourceTargetData > edges, UUID relationId ) {

        if ( relationId != null ) {
            SuSEntity susEntity = susDAO.getLatestNonDeletedObjectById( entityManager, relationId );

            if ( susEntity == null ) {
                return;
            }

            String viewLink = null;
            viewLink = getViewLink( entityManager, objectId, userId, susEntity );

            if ( !isNodeListHasBaseClass( nodes, susEntity ) ) {

                if ( susEntity instanceof ContainerEntity ) {
                    addBaseClassNodeData( susEntity.getComposedId().getId().toString(), susEntity.getName(),
                            susEntity.getTypeId().toString(), AuditTrailRelationType.SIBLING_ORIGIN, nodes, viewLink );
                } else {
                    addBaseClassNodeData( susEntity.getComposedId().getId().toString(), susEntity.getName(),
                            susEntity.getTypeId().toString(), AuditTrailRelationType.DECENDENT, nodes, viewLink );
                }
                addEdge( objectId, edges, susEntity.getComposedId().getId().toString() );

                // if object have any parent find out
                final List< Relation > listRels = susDAO.getListByPropertyDesc( entityManager, ConstantsDAO.CHILD, relationId );
                if ( listRels != null ) {
                    for ( Relation relation : listRels ) {
                        if ( relation.getType() == AuditTrailRelationType.RELATION_USED ) {
                            addNodesAndEdgesIns( entityManager, susEntity.getComposedId().getId().toString(), userId, nodes, edges,
                                    relation.getChild() );
                        }
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getDataTableContext( String userIdFromGeneralHeader, String objectId, FiltersDTO filter, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String projOrObjectId = objectId;
            for ( Object items : filter.getItems() ) {
                if ( items.toString().equals( objectId ) ) {
                    List< SuSEntity > parentEntities = susDAO.getParents( entityManager,
                            susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( objectId ) ) );
                    SuSEntity parentEntity = parentEntities.get( 0 );
                    projOrObjectId = parentEntity.getComposedId().getId().toString();
                    break;
                }
            }
            return dataObjectManager.getDataContextRouter( entityManager, userIdFromGeneralHeader, projOrObjectId, filter, token );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectViewDTO > getAuditTrailTableUIView( String objectId, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity susObj = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            if ( susObj == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.PROJECT_NOT_EXIST_WITH_ID.getKey(), objectId ) );
            }
            return dataObjectManager.getObjectViewList( entityManager, susObj, userId, GenericDTO.GENERIC_DTO_TYPE );
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO saveObjectView( String userId, String objectId, String viewJson, boolean isUpdateable ) {
        return saveOrUpdateObjectView( userId, objectId, viewJson, isUpdateable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO saveOrUpdateObjectView( String userId, String objectId, String viewJson, boolean isUpdateable ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity entity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            if ( entity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.PROJECT_NOT_EXIST_WITH_ID.getKey(), objectId ) );
            }
            if ( entity.getConfig() != null ) {
                SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( entity.getTypeId().toString(),
                        entity.getConfig() );
                String config = entity.getConfig();
                return objectViewManager.saveOrUpdateObjectView( entityManager,
                        dataObjectManager.prepareObjectViewDTO( config, viewJson,
                                dataObjectManager.getObjectViewKey( susObjectModel, GenericDTO.GENERIC_DTO_TYPE ), objectId, isUpdateable ),
                        userId );
            } else {
                return objectViewManager.saveOrUpdateObjectView( entityManager,
                        dataObjectManager.prepareObjectViewDTO( null, viewJson, GenericDTO.GENERIC_DTO_TYPE, objectId, isUpdateable ),
                        userId );
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteObjectView( String viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.deleteObjectView( entityManager, UUID.fromString( viewId ) );
        } finally {
            entityManager.close();
        }
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

    /**
     * Sets translation dao.
     *
     * @param translationDAO
     *         the translation dao
     */
    public void setTranslationDAO( TranslationDAO translationDAO ) {
        this.translationDAO = translationDAO;
    }

}
