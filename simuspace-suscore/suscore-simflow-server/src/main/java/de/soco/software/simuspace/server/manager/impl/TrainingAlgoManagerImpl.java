package de.soco.software.simuspace.server.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.dao.TrainingAlgoDAO;
import de.soco.software.simuspace.server.manager.BaseManager;
import de.soco.software.simuspace.server.manager.TrainingAlgoManager;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMode;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.WfFieldsUiDTO;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.SuSVaultUtils;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.TrainingAlgoEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.TrainingAlgoDTO;
import de.soco.software.simuspace.suscore.document.dao.DocumentDAO;
import de.soco.software.simuspace.suscore.document.manager.DocumentManager;
import de.soco.software.simuspace.suscore.user.manager.UserManager;

/**
 * The Class TrainingAlgoManagerImpl.
 *
 * @author noman arshad
 */
@Log4j2
public class TrainingAlgoManagerImpl extends BaseManager implements TrainingAlgoManager {

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The user manager.
     */
    private UserManager userManager;

    /**
     * The training algo DAO.
     */
    private TrainingAlgoDAO trainingAlgoDAO;

    /**
     * The document manager.
     */
    private DocumentManager documentManager;

    /**
     * The context menu manager.
     */
    private ContextMenuManager contextMenuManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Gets the trainer algo names.
     *
     * @param userUID
     *         the user UID
     *
     * @return the trainer algo names
     */
    @Override
    public List< WfFieldsUiDTO > getTrainerAlgoNames( String userUID ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< WfFieldsUiDTO > fieldsList = new ArrayList<>();

            List< TrainingAlgoEntity > trainingAlgoEntities = trainingAlgoDAO.getTrainingAlgoList( entityManager );
            for ( TrainingAlgoEntity trainingAlgoEntry : trainingAlgoEntities ) {
                fieldsList.add( new WfFieldsUiDTO( trainingAlgoEntry.getName(), trainingAlgoEntry.getName(), null ) );
            }

            return fieldsList;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the trainer algo script path.
     *
     * @param algoName
     *         the algo Name
     *
     * @return the trainer algo script path
     */
    @Override
    public String getTrainerAlgoScriptPath( String algoName ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TrainingAlgoEntity trainingAlgoEntity = trainingAlgoDAO.getTrainingAlgoEntityByName( entityManager, algoName );
            InputStream decryptedFileStream = SuSVaultUtils.getDecryptionSteamFromPath( trainingAlgoEntity.getAlgoConfig().getId(),
                    ConstantsInteger.INTEGER_VALUE_ONE, PropertiesManager.getVaultPath(),
                    prepareEncryptionDecryptionDTO( trainingAlgoEntity.getAlgoConfig().getEncryptionDecryption() ) );
            JSONParser parser = new JSONParser();
            JSONObject jsonAlgoConfig = ( JSONObject ) parser.parse(
                    new InputStreamReader( decryptedFileStream, FileUtils.UTF8_ENCODING ) );
            Map< String, Object > command = ( Map< String, Object > ) jsonAlgoConfig.get( "command" );
            return ( ( String ) command.get( "execute" ) ).replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH,
                    PropertiesManager.getScriptsPath() );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getTrainingAlgoUI( String userIdFromGeneralHeader ) {
        return GUIUtils.listColumns( TrainingAlgoDTO.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Object > getTrainingAlgoData( String userIdFromGeneralHeader, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getTrainingAlgoData( entityManager, userIdFromGeneralHeader, filter );
        } finally {
            entityManager.close();
        }
    }

    private FilteredResponse< Object > getTrainingAlgoData( EntityManager entityManager, String userIdFromGeneralHeader,
            FiltersDTO filter ) {
        userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.TRAINING_ALGO.getId(),
                PermissionMatrixEnum.READ.getValue(), Messages.NO_RIGHTS_TO_READ.getKey(), "TrainingAlgo" );

        List< SuSEntity > suSEntities = susDAO.getAllFilteredRecords( entityManager, TrainingAlgoEntity.class, filter );
        List< Object > objectDTOList = new ArrayList<>();
        for ( SuSEntity e : suSEntities ) {
            objectDTOList.add( createTrainingAlgoDTOFromEntity( ( TrainingAlgoEntity ) e ) );
        }
        return PaginationUtil.constructFilteredResponse( filter, objectDTOList );
    }

    /**
     * Creates the scheme DTO from entity.
     *
     * @param trainingAlgoEntity
     *         the scheme entity
     *
     * @return the WF scheme DTO
     */
    private TrainingAlgoDTO createTrainingAlgoDTOFromEntity( TrainingAlgoEntity trainingAlgoEntity ) {
        TrainingAlgoDTO trainingAlgoDTO = new TrainingAlgoDTO();
        trainingAlgoDTO.setId( trainingAlgoEntity.getComposedId().getId() );
        trainingAlgoDTO.setName( trainingAlgoEntity.getName() );
        trainingAlgoDTO.setDescription( trainingAlgoEntity.getDescription() );
        trainingAlgoDTO.setAlgoConfig( documentManager.prepareDocumentDTO( trainingAlgoEntity.getAlgoConfig() ) );
        return trainingAlgoDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm createTrainingAlgoUI( String userIdFromGeneralHeader ) {
        return GUIUtils.createFormFromItems( GUIUtils.prepareForm( true, new TrainingAlgoDTO() ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingAlgoDTO createTrainingAlgo( String userId, TrainingAlgoDTO trainingAlgoDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.TRAINING_ALGO.getId(),
                    PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(), Messages.NO_RIGHTS_TO_CREATE.getKey(), "WFScheme" );
            if ( trainingAlgoDAO.getNonDeletedObjectList( entityManager ).stream()
                    .anyMatch( obj -> obj.getName().equals( trainingAlgoDTO.getName() ) ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.ALGO_CONFIGURATION_CANNOT_CONTAIN_SAME_NAME.getKey() ) );
            }
            TrainingAlgoEntity trainingAlgoEntity = createTrainingAlgoEntityFromDTO( trainingAlgoDTO );
            trainingAlgoEntity.setCreatedBy( userCommonManager.getUserEntityById( entityManager, UUID.fromString( userId ) ) );
            SuSEntity udatedEntity = susDAO.saveOrUpdate( entityManager, trainingAlgoEntity );
            susDAO.createRelation( entityManager,
                    new Relation( UUID.fromString( SimuspaceFeaturesEnum.TRAINING_ALGO.getId() ), udatedEntity.getComposedId().getId() ) );
            return createTrainingAlgoDTOFromEntity( ( TrainingAlgoEntity ) udatedEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Creates the WF scheme entity from DTO.
     *
     * @param trainingAlgoDTO
     *         the scheme DTO
     *
     * @return the WF scheme entity
     */
    private TrainingAlgoEntity createTrainingAlgoEntityFromDTO( TrainingAlgoDTO trainingAlgoDTO ) {
        TrainingAlgoEntity trainingAlgoEntity = new TrainingAlgoEntity();
        trainingAlgoEntity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), 1 ) );
        trainingAlgoEntity.setName( trainingAlgoDTO.getName() );
        trainingAlgoEntity.setDescription( trainingAlgoDTO.getDescription() );
        trainingAlgoEntity.setAlgoConfig( documentManager.prepareEntityFromDocumentDTO( trainingAlgoDTO.getAlgoConfig() ) );
        trainingAlgoEntity.setCreatedOn( new Date() );
        trainingAlgoEntity.setModifiedOn( new Date() );
        return trainingAlgoEntity;
    }

    /**
     * Gets the user common manager.
     *
     * @return the user common manager
     */
    public UserCommonManager getUserCommonManager() {
        return userCommonManager;
    }

    /**
     * Sets the user common manager.
     *
     * @param userCommonManager
     *         the new user common manager
     */
    public void setUserCommonManager( UserCommonManager userCommonManager ) {
        this.userCommonManager = userCommonManager;
    }

    /**
     * Gets the user manager.
     *
     * @return the user manager
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Sets the user manager.
     *
     * @param userManager
     *         the new user manager
     */
    public void setUserManager( UserManager userManager ) {
        this.userManager = userManager;
    }

    /**
     * Gets the training algo DAO.
     *
     * @return the training algo DAO
     */
    public TrainingAlgoDAO getTrainingAlgoDAO() {
        return trainingAlgoDAO;
    }

    /**
     * Sets the training algo DAO.
     *
     * @param trainingAlgoDAO
     *         the new training algo DAO
     */
    public void setTrainingAlgoDAO( TrainingAlgoDAO trainingAlgoDAO ) {
        this.trainingAlgoDAO = trainingAlgoDAO;
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
     * Gets the document manager.
     *
     * @return the document manager
     */
    public DocumentManager getDocumentManager() {
        return documentManager;
    }

    /**
     * Sets the document manager.
     *
     * @param documentManager
     *         the new document manager
     */
    public void setDocumentManager( DocumentManager documentManager ) {
        this.documentManager = documentManager;
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
     * Gets the context menu manager.
     *
     * @return the context menu manager
     */
    public ContextMenuManager getContextMenuManager() {
        return contextMenuManager;
    }

    /**
     * Sets the context menu manager.
     *
     * @param contextMenuManager
     *         the new context menu manager
     */
    public void setContextMenuManager( ContextMenuManager contextMenuManager ) {
        this.contextMenuManager = contextMenuManager;
    }

    /**
     * Gets the training algo context.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param filter
     *         the filter
     *
     * @return the training algo context
     */
    @Override
    public List< ContextMenuItem > getTrainingAlgoContext( String userIdFromGeneralHeader, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // case of select all from data table
        try {
            if ( filter.getItems().isEmpty() && filter.getLength().toString().equalsIgnoreCase( "-1" ) ) {

                Long maxResults = susDAO.getAllFilteredRecordCountWithParentId( entityManager, TrainingAlgoEntity.class, filter,
                        UUID.fromString( SimuspaceFeaturesEnum.TRAINING_ALGO.getId() ) );
                filter.setLength( Integer.valueOf( maxResults.toString() ) );
                List< SuSEntity > allObjectsList = susDAO.getAllFilteredRecordsWithParent( entityManager, TrainingAlgoEntity.class, filter,
                        UUID.fromString( SimuspaceFeaturesEnum.TRAINING_ALGO.getId() ) );
                List< Object > itemsList = new ArrayList<>();
                allObjectsList.stream().forEach( susEntity -> itemsList.add( susEntity.getComposedId().getId() ) );

                filter.setItems( itemsList );
            }

            List< Object > selectedIds = filter.getItems();
            if ( CollectionUtil.isNotEmpty( selectedIds ) && selectedIds.size() == ConstantsInteger.INTEGER_VALUE_ONE ) {
                return contextMenuManager.getTrainingAlgoContextMenu( UUID.fromString( selectedIds.get( 0 ).toString() ), false );
            } else {
                final SelectionResponseUI selectionResponseUI = selectionManager.createSelection( entityManager, userIdFromGeneralHeader,
                        SelectionOrigins.CONTEXT, filter );
                return contextMenuManager.getTrainingAlgoContextMenu( UUID.fromString( selectionResponseUI.getId() ), true );
            }
        } finally {
            entityManager.close();
        }

    }

    /**
     * Delete training algo.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     * @param mode
     *         the mode
     */
    @Override
    public void deleteTrainingAlgo( String userIdFromGeneralHeader, String selectionId, String mode ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.TRAINING_ALGO.getId(),
                    PermissionMatrixEnum.DELETE.getValue(), Messages.NO_RIGHTS_TO_DELETE.getKey(), "TrainingAlgo" );

            if ( mode.equals( ConstantsMode.SINGLE ) ) {
                List< SuSEntity > deletedSchemeVersions = prepareTrainingAlgoVersionsForDelete( entityManager, userIdFromGeneralHeader,
                        UUID.fromString( selectionId ) );
                susDAO.saveOrUpdateBulk( entityManager, deletedSchemeVersions );
            } else {
                List< UUID > selectedObjects = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );
                for ( UUID uuid : selectedObjects ) {
                    List< SuSEntity > deletedSchemeVersions = prepareTrainingAlgoVersionsForDelete( entityManager, userIdFromGeneralHeader,
                            uuid );
                    susDAO.saveOrUpdateBulk( entityManager, deletedSchemeVersions );
                }
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare training algo versions for delete.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param schemeId
     *         the scheme id
     *
     * @return the list
     */
    private List< SuSEntity > prepareTrainingAlgoVersionsForDelete( EntityManager entityManager, String userIdFromGeneralHeader,
            UUID schemeId ) {
        List< SuSEntity > schemeVersions = susDAO.getObjectVersionListById( entityManager, TrainingAlgoEntity.class, schemeId );
        for ( SuSEntity entity : schemeVersions ) {
            entity.setDelete( Boolean.TRUE );
            entity.setDeletedOn( new Date() );
            entity.setModifiedOn( new Date() );
            entity.setDeletedBy(
                    userCommonManager.getUserCommonDAO().findById( entityManager, UUID.fromString( userIdFromGeneralHeader ) ) );
        }
        return schemeVersions;
    }

    /**
     * Update training algo.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param algoId
     *         the algo id
     * @param trainingAlgoDTO
     *         the training algo DTO
     *
     * @return the object
     */
    @Override
    public TrainingAlgoDTO updateTrainingAlgo( String userIdFromGeneralHeader, String algoId, TrainingAlgoDTO trainingAlgoDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.TRAINING_ALGO.getId(),
                    PermissionMatrixEnum.WRITE.getValue(), Messages.NO_RIGHTS_TO_WRITE.getKey(), "TrainingAlgo" );
            if ( trainingAlgoDAO.getNonDeletedObjectList( entityManager ).stream().anyMatch(
                    obj -> obj.getName().equals( trainingAlgoDTO.getName() ) && !Objects.equals( obj.getComposedId().getId(),
                            UUID.fromString( algoId ) ) ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.ALGO_CONFIGURATION_CANNOT_CONTAIN_SAME_NAME.getKey() ) );
            }
            TrainingAlgoEntity trainingAlgoEntity = ( TrainingAlgoEntity ) susDAO.getLatestNonDeletedObjectById( entityManager,
                    UUID.fromString( algoId ) );
            trainingAlgoEntity.setName( trainingAlgoDTO.getName() );
            trainingAlgoEntity.setDescription( trainingAlgoDTO.getDescription() );
            trainingAlgoEntity.setAlgoConfig( documentManager.prepareEntityFromDocumentDTO( trainingAlgoDTO.getAlgoConfig() ) );
            trainingAlgoEntity.setModifiedBy(
                    userCommonManager.getUserEntityById( entityManager, UUID.fromString( userIdFromGeneralHeader ) ) );
            SuSEntity updatedEntity = susDAO.saveOrUpdate( entityManager, trainingAlgoEntity );
            return createTrainingAlgoDTOFromEntity( ( TrainingAlgoEntity ) updatedEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Edits the training algo UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param algoId
     *         the algo id
     *
     * @return the list
     */
    @Override
    public UIForm editTrainingAlgoUI( String userIdFromGeneralHeader, String algoId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return GUIUtils.createFormFromItems(
                    GUIUtils.prepareForm( false, getTrainingAlgo( entityManager, UUID.fromString( algoId ) ) ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the training algo.
     *
     * @param entityManager
     *         the entity manager
     * @param schemeId
     *         the scheme id
     *
     * @return the training algo
     */
    private Object getTrainingAlgo( EntityManager entityManager, UUID schemeId ) {
        return createTrainingAlgoDTOFromEntity( ( TrainingAlgoEntity ) susDAO.getLatestNonDeletedObjectById( entityManager, schemeId ) );
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
     * Sets document dao.
     *
     * @param documentDAO
     *         the document dao
     */
    public void setDocumentDAO( DocumentDAO documentDAO ) {
        this.documentDAO = documentDAO;
    }

}
