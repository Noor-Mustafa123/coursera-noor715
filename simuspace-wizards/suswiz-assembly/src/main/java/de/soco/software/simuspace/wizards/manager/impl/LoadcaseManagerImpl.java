package de.soco.software.simuspace.wizards.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDbOperationTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.SuSFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.model.DummyTypeDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.OSValidator;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.entity.DummyTypeEntity;
import de.soco.software.simuspace.suscore.data.entity.LoadCaseEntity;
import de.soco.software.simuspace.suscore.data.entity.LoadCaseWizardEntity;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.LoadCaseDTO;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.executor.service.ThreadPoolExecutorService;
import de.soco.software.simuspace.suscore.location.manager.LocationManager;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.permissions.model.ResourceAccessControlDTO;
import de.soco.software.simuspace.suscore.user.manager.UserManager;
import de.soco.software.simuspace.wizards.constants.WizardsFeaturesEnum;
import de.soco.software.simuspace.wizards.dao.DummyTypeDAO;
import de.soco.software.simuspace.wizards.dao.LoadcaseWizDAO;
import de.soco.software.simuspace.wizards.manager.LoadcaseManager;
import de.soco.software.simuspace.wizards.model.LoadcaseWizardDTO;
import de.soco.software.simuspace.wizards.model.VariantLoadcaseDTO;
import de.soco.software.simuspace.wizards.threads.DeleteLoadcaseThread;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.SystemWorkflow;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;

/**
 * The Class LoadcaseManagerImpl.
 */
@Log4j2
public class LoadcaseManagerImpl implements LoadcaseManager {

    /**
     * The Constant LOADCASE.
     */
    private static final String LOADCASE = "Loadcases";

    /**
     * The Constant INFO.
     */
    private static final String INFO = "Info";

    /**
     * The Constant BULK_JOB_NAME_DELETE_OBJECT.
     */
    private static final String BULK_JOB_NAME_DELETE_OBJECT = "Bulk Objects are being deleted";

    /**
     * The Constant DELETE_OBJECT_DESCRIPTION.
     */
    private static final String DELETE_OBJECT_JOB_DESCRIPTION = "Job to delete object and its dependencies";

    /**
     * The Constant MACHINE.
     */
    private static final String MACHINE = "server";

    /**
     * The Constant SERVER.
     */
    private static final String RUN_MODE = MACHINE;

    /**
     * The sus DAO.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The loadcase wiz DAO.
     */
    private LoadcaseWizDAO loadcaseWizDAO;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The user manager.
     */
    private UserManager userManager;

    /**
     * The selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The job manager.
     */
    private JobManager jobManager;

    /**
     * The location manager.
     */
    private LocationManager locationManager;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The thread pool executor service.
     */
    private ThreadPoolExecutorService threadPoolExecutorService;

    /**
     * The dummy type DAO.
     */
    private DummyTypeDAO dummyTypeDAO;

    /**
     * The Entity Manager Factory
     */
    private EntityManagerFactory entityManagerFactory;

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.wizards.manager.LoadcaseManager#getLoadcaseUI(java .lang.String)
     */
    @Override
    public List< TableColumn > getLoadcaseUI( String userIdFromGeneralHeader ) {
        return GUIUtils.listColumns( LoadCaseDTO.class );
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.wizards.manager.LoadcaseManager#getLoadcaseData( java.lang.String,
     * de.soco.software.simuspace.suscore.common.base.FiltersDTO)
     */
    @Override
    public FilteredResponse< Object > getLoadcaseData( String userIdFromGeneralHeader, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.LOADCASES.getId(),
                    PermissionMatrixEnum.READ.getValue(), Messages.NO_RIGHTS_TO_READ.getKey(), LOADCASE );
            List< SuSEntity > suSEntities = susDAO.getAllFilteredRecords( entityManager, LoadCaseEntity.class, filter );
            List< Object > objectDTOList = new ArrayList<>();
            for ( SuSEntity e : suSEntities ) {
                objectDTOList.add( createLoadcaseDTOFromEntity( e ) );

            }
            filter.setTotalRecords( filter.getFilteredRecords() );
            return PaginationUtil.constructFilteredResponse( filter, objectDTOList );
        } finally {
            entityManager.close();
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.wizards.manager.LoadcaseManager#createLoadcaseUI( java.lang.String)
     */
    @Override
    public UIForm createLoadcaseUI( String userIdFromGeneralHeader ) {
        return GUIUtils.createFormFromItems( GUIUtils.prepareForm( true, new LoadcaseWizardDTO() ) );
    }

    @Override
    public LoadcaseWizardDTO createLoadcase( String userId, LoadcaseWizardDTO loadcaseWizardDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DummyTypeEntity dummyTypeEntity = null;
            if ( loadcaseWizardDTO.getDummyTypeDTO() != null && loadcaseWizardDTO.getDummyTypeDTO().getDummyTypeName() != null
                    && loadcaseWizardDTO.getDummyTypeDTO().getSolverType() != null ) {
                dummyTypeEntity = dummyTypeDAO.getDummyTypeByName( entityManager, loadcaseWizardDTO.getDummyTypeDTO().getDummyTypeName(),
                        loadcaseWizardDTO.getDummyTypeDTO().getSolverType() );
            }
            return createLoadcase( entityManager, userId, loadcaseWizardDTO, dummyTypeEntity );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public LoadcaseWizardDTO createLoadcase( EntityManager entityManager, String userId, LoadcaseWizardDTO loadcaseWizardDTO,
            DummyTypeEntity dummyTypeEntity ) {
        userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.LOADCASES.getId(),
                PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(), Messages.NO_RIGHTS_TO_CREATE.getKey(), LOADCASE );

        LoadCaseEntity loadCaseEntity = createLoadcaseEntityFromDTO( loadcaseWizardDTO, dummyTypeEntity );
        UserEntity userEntity = userCommonManager.getUserEntityById( entityManager, UUID.fromString( userId ) );
        loadCaseEntity.setCreatedBy( userEntity );
        loadCaseEntity.setModifiedBy( userEntity );
        SuSEntity updatedEntity = susDAO.saveOrUpdate( entityManager, loadCaseEntity );
        susDAO.createRelation( entityManager,
                new Relation( UUID.fromString( WizardsFeaturesEnum.LOADCASES.getId() ), updatedEntity.getComposedId().getId() ) );

        LoadCaseWizardEntity wizardEntity = new LoadCaseWizardEntity();
        wizardEntity.setId( updatedEntity.getComposedId().getId() );
        wizardEntity.setAssemblySelectionId( loadcaseWizardDTO.getAssemblySelectionId() );
        wizardEntity.setSolverSelectionId( loadcaseWizardDTO.getSolverSelectionId() );
        wizardEntity.setPostSelectionId( loadcaseWizardDTO.getPostSelectionId() );

        addToAcl( entityManager, userId, updatedEntity,
                susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( SuSFeaturesEnum.LOADCASES.getId() ) ) );
        loadcaseWizDAO.saveOrUpdate( entityManager, wizardEntity );

        return createLoadcaseWizardDTOFromEntity( updatedEntity, wizardEntity );
    }

    /**
     * Adds the to acl.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param createdEntity
     *         the created entity
     * @param parentEntity
     *         the parent entity
     */
    private void addToAcl( EntityManager entityManager, String userId, SuSEntity createdEntity, SuSEntity parentEntity ) {
        log.info( MessageBundleFactory.getMessage( Messages.SAVING_OBJECT_AS_RESOURCE.getKey() ) );
        ResourceAccessControlDTO resourceAccessControlDTO = permissionManager.prepareResourceAccessControlDTOFromObject( entityManager,
                createdEntity, parentEntity );
        if ( resourceAccessControlDTO != null ) {
            resourceAccessControlDTO.setSecurityIdentity(
                    userCommonManager.getAclCommonSecurityIdentityDAO().getSecurityIdentityBySid( entityManager, UUID.fromString( userId ) )
                            .getId() );
            permissionManager.addObjectToAcl( entityManager, resourceAccessControlDTO );
            permissionManager.saveSelectionForNewEntity( entityManager, userId, createdEntity, parentEntity );
        }
    }

    /**
     * Creates the loadcase wizard DTO from entity.
     *
     * @param e
     *         the e
     * @param we
     *         the we
     *
     * @return the loadcase wizard DTO
     */
    private LoadcaseWizardDTO createLoadcaseWizardDTOFromEntity( SuSEntity e, LoadCaseWizardEntity we ) {
        LoadcaseWizardDTO result = new LoadcaseWizardDTO();
        result.setName( e.getName() );
        result.setId( e.getComposedId().getId() );
        result.setVersion( new VersionDTO( e.getComposedId().getVersionId() ) );
        result.setDescription( e.getDescription() );

        if ( e.getCreatedBy() != null ) {
            result.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( e.getCreatedBy() ) );
        }
        if ( e.getModifiedBy() != null ) {
            result.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( e.getModifiedBy() ) );
        }

        if ( e instanceof LoadCaseEntity loadCaseEntity ) {
            result.setTimeout( loadCaseEntity.getTimeout() );
            result.setIsInternal( loadCaseEntity.getIsInternal() );
        }

        if ( we != null ) {
            if ( we.getAssemblySelectionId() != null ) {
                result.setAssemblySelectionId( we.getAssemblySelectionId() );
            }
            if ( we.getSolverSelectionId() != null ) {
                result.setSolverSelectionId( we.getSolverSelectionId() );
            }
            if ( we.getPostSelectionId() != null ) {
                result.setPostSelectionId( we.getPostSelectionId() );
            }
        }
        return result;
    }

    /**
     * Creates the loadcase DTO from entity.
     *
     * @param e
     *         the e
     *
     * @return the load case DTO
     */
    @Override
    public LoadCaseDTO createLoadcaseDTOFromEntity( SuSEntity e ) {
        LoadCaseDTO loadCaseDTO = null;
        if ( e != null ) {
            loadCaseDTO = new LoadCaseDTO();
            loadCaseDTO.setName( e.getName() );
            loadCaseDTO.setId( e.getComposedId().getId() );
            loadCaseDTO.setVersion( new VersionDTO( e.getComposedId().getVersionId() ) );
            loadCaseDTO.setDescription( e.getDescription() );
            loadCaseDTO.setCreatedOn( e.getCreatedOn() );
            loadCaseDTO.setModifiedOn( e.getModifiedOn() );
            if ( e.getCreatedBy() != null ) {
                loadCaseDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( e.getCreatedBy() ) );
            }
            if ( e.getModifiedBy() != null ) {
                loadCaseDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( e.getModifiedBy() ) );
            }

            if ( e instanceof LoadCaseEntity loadCaseEntity ) {
                loadCaseDTO.setTimeout( loadCaseEntity.getTimeout() );
                loadCaseDTO.setIsInternal( loadCaseEntity.getIsInternal() );
                if ( loadCaseEntity.getDummyTypeEntity() != null ) {
                    DummyTypeDTO dummyTypeDTO = new DummyTypeDTO();
                    dummyTypeDTO.setId( loadCaseEntity.getDummyTypeEntity().getId() );
                    dummyTypeDTO.setDummyTypeName( loadCaseEntity.getDummyTypeEntity().getDummyTypeName() );
                    loadCaseDTO.setDummyTypeDTO( dummyTypeDTO );
                }
            }
        }
        return loadCaseDTO;
    }

    /**
     * Creates the varaint loadcase DTO from entity.
     *
     * @param e
     *         the e
     *
     * @return the variant loadcase DTO
     */
    private VariantLoadcaseDTO createVaraintLoadcaseDTOFromEntity( SuSEntity e ) {
        VariantLoadcaseDTO result = new VariantLoadcaseDTO();
        result.setName( e.getName() );
        result.setId( e.getComposedId().getId() );
        result.setVersion( new VersionDTO( e.getComposedId().getVersionId() ) );
        result.setDescription( e.getDescription() );
        return result;
    }

    /**
     * Creates the loadcase entity from DTO.
     *
     * @param loadCaseDTO
     *         the load case DTO
     * @param dummyTypeEntity
     *         the dummy type entity
     *
     * @return the load case entity
     */
    private LoadCaseEntity createLoadcaseEntityFromDTO( LoadCaseDTO loadCaseDTO, DummyTypeEntity dummyTypeEntity ) {
        LoadCaseEntity loadCaseEntity = new LoadCaseEntity();
        loadCaseEntity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), 1 ) );
        loadCaseEntity.setName( loadCaseDTO.getName() );
        loadCaseEntity.setDescription( loadCaseDTO.getDescription() );
        loadCaseEntity.setTimeout( loadCaseDTO.getTimeout() );
        loadCaseEntity.setIsInternal( loadCaseDTO.getIsInternal() );
        loadCaseEntity.setSize( null );
        loadCaseEntity.setCreatedOn( new Date() );
        loadCaseEntity.setModifiedOn( new Date() );
        loadCaseEntity.setDummyTypeEntity( dummyTypeEntity );

        return loadCaseEntity;
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

    @Override
    public LoadCaseDTO getLoadcase( EntityManager entityManager, String userIdFromGeneralHeader, UUID loadCaseId ) {
        return createLoadcaseDTOFromEntity( susDAO.getLatestNonDeletedObjectById( entityManager, loadCaseId ) );
    }

    /**
     * Gets loadcase wizard.
     *
     * @param entityManager
     *         the entity manager
     * @param loadCaseId
     *         the load case id
     *
     * @return the loadcase wizard
     */
    private LoadcaseWizardDTO getLoadcaseWizard( EntityManager entityManager, UUID loadCaseId ) {
        return createLoadcaseWizardDTOFromEntity( susDAO.getLatestNonDeletedObjectById( entityManager, loadCaseId ),
                loadcaseWizDAO.findById( entityManager, loadCaseId ) );
    }

    @Override
    public VariantLoadcaseDTO getVaraintLoadcase( EntityManager entityManager, String userIdFromGeneralHeader, UUID loadCaseId ) {
        return createVaraintLoadcaseDTOFromEntity( susDAO.getLatestNonDeletedObjectById( entityManager, loadCaseId ) );
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.wizards.manager.LoadcaseManager# getLoadcaseContextMenu(java.lang.String,
     * de.soco.software.simuspace.suscore.common.base.FiltersDTO)
     */
    @Override
    public List< ContextMenuItem > getLoadcaseContextMenu( String userIdFromGeneralHeader, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // case of select all from data table
        try {
            if ( filter.getItems().isEmpty() && filter.getLength().toString().equalsIgnoreCase( "-1" ) ) {

                Long maxResults = susDAO.getAllFilteredRecordCountWithParentId( entityManager, LoadCaseEntity.class, filter,
                        UUID.fromString( SimuspaceFeaturesEnum.LOADCASES.getId() ) );
                filter.setLength( Integer.valueOf( maxResults.toString() ) );
                List< SuSEntity > allObjectsList = susDAO.getAllFilteredRecordsWithParent( entityManager, LoadCaseEntity.class, filter,
                        UUID.fromString( SimuspaceFeaturesEnum.LOADCASES.getId() ) );
                List< Object > itemsList = new ArrayList<>();
                allObjectsList.stream().forEach( susEntity -> itemsList.add( susEntity.getComposedId().getId() ) );

                filter.setItems( itemsList );
            }

            List< ContextMenuItem > loadcaseContext = new ArrayList<>();
            List< Object > selectedIds = filter.getItems();
            if ( CollectionUtil.isNotEmpty( selectedIds ) && selectedIds.size() == ConstantsInteger.INTEGER_VALUE_ONE ) {
                loadcaseContext.add( new ContextMenuItem( "edit/config/loadcase/" + selectedIds.get( 0 ), "",
                        MessageBundleFactory.getMessage( "4100014x4" ) ) );
                loadcaseContext.add( new ContextMenuItem( "delete/config/loadcase/" + selectedIds.get( 0 ) + "/?mode=single", "",
                        MessageBundleFactory.getMessage( "4100007x4" ) ) );
            } else {
                SelectionResponseUI selectionResponseUI = selectionManager.createSelection( entityManager, userIdFromGeneralHeader,
                        SelectionOrigins.CONTEXT, filter );
                loadcaseContext.add( new ContextMenuItem( "delete/config/loadcase/" + selectionResponseUI.getId() + "/?mode=bulk", "",
                        MessageBundleFactory.getMessage( "4100082x4" ) ) );
            }
            return loadcaseContext;
        } finally {
            entityManager.close();
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.wizards.manager.LoadcaseManager#editLoadcaseUI( java.lang.String, java.lang.String)
     */
    @Override
    public UIForm editLoadcaseUI( String userIdFromGeneralHeader, String loadcaseId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return GUIUtils.createFormFromItems(
                    GUIUtils.prepareForm( false, getLoadcaseWizard( entityManager, UUID.fromString( loadcaseId ) ) ) );
        } finally {
            entityManager.close();
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.wizards.manager.LoadcaseManager#updateLoadcase( java.lang.String, java.lang.String,
     * de.soco.software.simuspace.wizards.model.LoadcaseWizardDTO)
     */
    @Override
    public LoadcaseWizardDTO updateLoadcase( String userIdFromGeneralHeader, String loadcaseId, LoadcaseWizardDTO loadcaseWizardDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.LOADCASES.getId(),
                    PermissionMatrixEnum.WRITE.getValue(), Messages.NO_RIGHTS_TO_WRITE.getKey(), LOADCASE );

            LoadCaseEntity entity = ( LoadCaseEntity ) susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( loadcaseId ) );
            LoadCaseEntity loadCaseEntity = createLoadcaseEntityFromDTO( loadcaseWizardDTO,
                    entity != null ? entity.getDummyTypeEntity() : null );
            loadCaseEntity.setModifiedBy(
                    userCommonManager.getUserEntityById( entityManager, UUID.fromString( userIdFromGeneralHeader ) ) );
            if ( entity != null ) {
                loadCaseEntity.setCreatedBy( entity.getCreatedBy() );
                loadCaseEntity.setCreatedOn( entity.getCreatedOn() );
            }
            loadCaseEntity.setComposedId(
                    new VersionPrimaryKey( UUID.fromString( loadcaseId ), entity.getComposedId().getVersionId() + 1 ) );

            SuSEntity updatedEntity = susDAO.saveOrUpdate( entityManager, loadCaseEntity );

            LoadCaseWizardEntity wizardEntity = new LoadCaseWizardEntity();
            wizardEntity.setId( updatedEntity.getComposedId().getId() );
            wizardEntity.setAssemblySelectionId( loadcaseWizardDTO.getAssemblySelectionId() );
            wizardEntity.setSolverSelectionId( loadcaseWizardDTO.getSolverSelectionId() );
            wizardEntity.setPostSelectionId( loadcaseWizardDTO.getPostSelectionId() );

            loadcaseWizDAO.saveOrUpdate( entityManager, wizardEntity );

            return createLoadcaseWizardDTOFromEntity( updatedEntity, wizardEntity );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public boolean deleteLoadcase( String userIdFromGeneralHeader, UUID loadcaseId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return deleteLoadcase( entityManager, userIdFromGeneralHeader, loadcaseId );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public boolean deleteLoadcase( EntityManager entityManager, String userIdFromGeneralHeader, UUID loadcaseId ) {
        userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.LOADCASES.getId(),
                PermissionMatrixEnum.DELETE.getValue(), Messages.NO_RIGHTS_TO_DELETE.getKey(), LOADCASE );
        LoadCaseEntity entity = ( LoadCaseEntity ) susDAO.getLatestNonDeletedObjectById( entityManager, loadcaseId );

        List< SuSEntity > susEntityVersions = susDAO.getObjectVersionListById( entityManager, SuSEntity.class, loadcaseId );
        susEntityVersions.forEach( suSEntity -> {
            if ( !suSEntity.isDelete() && suSEntity.getComposedId().getVersionId() == entity.getComposedId().getVersionId() ) {
                suSEntity.setAuditLogEntity(
                        AuditLogDTO.prepareAuditLogEntityForSusEntity( entity.getName(), ConstantsDbOperationTypes.DELETED,
                                userIdFromGeneralHeader, entity ) );
            }
            suSEntity.setDelete( Boolean.TRUE );
            suSEntity.setDeletedBy(
                    userCommonManager.getUserCommonDAO().findById( entityManager, UUID.fromString( userIdFromGeneralHeader ) ) );
            suSEntity.setDeletedOn( new Date() );
            suSEntity.setModifiedOn( new Date() );
        } );
        susDAO.saveOrUpdateBulk( entityManager, susEntityVersions );
        return true;
    }

    @Override
    public int deleteLoadcaseInBulk( String userId, String selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UUID > selectedObjects = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );

            List< SuSEntity > susEntityList = new ArrayList<>();
            for ( UUID uuid : selectedObjects ) {
                SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, uuid );
                susEntityList.add( susEntity );
            }

            Job deleteProjectJob = prepareJobModel( entityManager, BULK_JOB_NAME_DELETE_OBJECT, DELETE_OBJECT_JOB_DESCRIPTION,
                    SystemWorkflow.DELETE );
            List< LogRecord > jobLog = new ArrayList<>();
            jobLog.add( new LogRecord( INFO, MessageBundleFactory.getMessage( Messages.GOING_TO_DELETE_OBJECT_AND_DEPENDENCIES.getKey() ),
                    new Date() ) );
            deleteProjectJob.setLog( jobLog );
            deleteProjectJob.setCreatedBy( new UserDTO( userId ) );
            deleteProjectJob.setWorkflowId( UUID.fromString( SystemWorkflow.DELETE.getId() ) );
            Job savedDeletedProjectJob = jobManager.createJob( entityManager, UUID.fromString( userId ), deleteProjectJob );

            DeleteLoadcaseThread deleteObjectThread = new DeleteLoadcaseThread( userId, susEntityList, savedDeletedProjectJob, this,
                    jobManager, entityManagerFactory );
            threadPoolExecutorService.deleteExecute( savedDeletedProjectJob.getId(), deleteObjectThread );

            return susEntityList.size();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare job model job.
     *
     * @param entityManager
     *         the entity manager
     * @param jobName
     *         the job name
     * @param description
     *         the description
     * @param workflow
     *         the workflow
     *
     * @return the job
     */
    private Job prepareJobModel( EntityManager entityManager, String jobName, String description, SystemWorkflow workflow ) {
        Status status = new Status( WorkflowStatus.RUNNING );

        final Job jobImpl = new JobImpl();
        final LocationDTO locationDTO = locationManager.getLocation( entityManager, LocationsEnum.DEFAULT_LOCATION.getId() );
        jobImpl.setDescription( description );
        jobImpl.setName( jobName );
        jobImpl.setWorkflowId( UUID.fromString( workflow.getId() ) );
        jobImpl.setWorkflowName( workflow.getName() );
        jobImpl.setRunMode( RUN_MODE );
        jobImpl.setRunsOn( locationDTO );
        jobImpl.setOs( OSValidator.getOperationSystemName() );

        try {
            jobImpl.setMachine( InetAddress.getLocalHost().getHostName() );
        } catch ( final UnknownHostException e ) {
            log.error( "Machine Name Error: " + e.getMessage() );
        }

        jobImpl.setStatus( status );
        jobImpl.setWorkflowVersion( new VersionDTO( SusConstantObject.DEFAULT_VERSION_NO ) );

        return jobImpl;
    }

    /**
     * Sets the thread pool executor service.
     *
     * @param threadPoolExecutorService
     *         the new thread pool executor service
     */
    public void setThreadPoolExecutorService( ThreadPoolExecutorService threadPoolExecutorService ) {
        this.threadPoolExecutorService = threadPoolExecutorService;
    }

    /**
     * Sets the location manager.
     *
     * @param locationManager
     *         the new location manager
     */
    public void setLocationManager( LocationManager locationManager ) {
        this.locationManager = locationManager;
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
     * Sets the loadcase wiz DAO.
     *
     * @param loadcaseWizDAO
     *         the new loadcase wiz DAO
     */
    public void setLoadcaseWizDAO( LoadcaseWizDAO loadcaseWizDAO ) {
        this.loadcaseWizDAO = loadcaseWizDAO;
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.wizards.manager.LoadcaseManager# getObjectViewManager()
     */
    public ObjectViewManager getObjectViewManager() {
        return objectViewManager;
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.wizards.manager.LoadcaseManager#
     * setObjectViewManager(de.soco.software.simuspace.suscore.data.manager.base. ObjectViewManager)
     */
    public void setObjectViewManager( ObjectViewManager objectViewManager ) {
        this.objectViewManager = objectViewManager;
    }

    /**
     * Sets user manager.
     *
     * @param userManager
     *         the user manager
     */
    public void setUserManager( UserManager userManager ) {
        this.userManager = userManager;
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
     * Sets the selection manager.
     *
     * @param selectionManager
     *         the new selection manager
     */
    public void setSelectionManager( SelectionManager selectionManager ) {
        this.selectionManager = selectionManager;
    }

    /**
     * Sets the permission manager.
     *
     * @param permissionManager
     *         the new permission manager
     */
    public void setPermissionManager( PermissionManager permissionManager ) {
        this.permissionManager = permissionManager;
    }

    /**
     * Sets the dummy type DAO.
     *
     * @param dummyTypeDAO
     *         the new dummy type DAO
     */
    public void setDummyTypeDAO( DummyTypeDAO dummyTypeDAO ) {
        this.dummyTypeDAO = dummyTypeDAO;
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
