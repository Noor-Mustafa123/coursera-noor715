package de.soco.software.simuspace.suscore.object.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.cxf.phase.PhaseInterceptorChain;

import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.BreadCrumbDTO;
import de.soco.software.simuspace.suscore.common.model.BreadCrumbItemDTO;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.BundleUtils;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.common.dao.JobDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.LibraryEntity;
import de.soco.software.simuspace.suscore.data.entity.ProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.ProjectOverviewEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.SystemContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.VariantEntity;
import de.soco.software.simuspace.suscore.data.entity.WorkflowProjectEntity;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.object.manager.BreadCrumbManager;
import de.soco.software.simuspace.suscore.object.manager.SuSObjectBaseManager;
import de.soco.software.simuspace.suscore.object.manager.SuSObjectTreeManager;

/**
 * The Class is responsible for create bread crumb.
 */
public class BreadCrumbManagerImpl extends SuSObjectBaseManager implements BreadCrumbManager {

    /**
     * The Constant URL_PART_VERSION.
     */
    private static final String URL_PART_VERSION = "/version/";

    /**
     * The Constant LIST_FIRST_INDEX.
     */
    private static final int LIST_FIRST_INDEX = 0;

    /**
     * The Constant VIEW_WORKFLOW_PROJECT_TABLE_URL.
     */
    private static final String VIEW_WORKFLOW_PROJECT_TABLE_URL = "view/workflow/project/";

    /**
     * The Constant VIEW_WORKFLOW.
     */
    private static final String VIEW_WORKFLOW = "view/workflow/";

    /**
     * The Constant VIEW_JOB.
     */
    private static final String VIEW_JOB = "view/job/";

    /**
     * The Constant PROJECT_ENTITY_URL_VIEW_DATA_PROJECT.
     */
    private static final String ENTITY_URL_VIEW_DATA = "view/data/project/";

    /**
     * The Constant DATA_OBJECT_ENTITY_URL_VIEW_DATA_OBJECT.
     */
    private static final String DATA_OBJECT_ENTITY_URL_VIEW_DATA_OBJECT = "view/data/object/";

    /**
     * The Constant OBJECT_TREE_URL.
     */
    private static final String OBJECT_TREE_URL = "/object-tree/";

    /**
     * The Constant SYSTEM_AUDIT_LOG.
     */
    private static final String SYSTEM_AUDIT_LOG = "system/auditlog";

    /**
     * The Constant SYSTEM_LOCATIONS.
     */
    private static final String SYSTEM_LOCATIONS = "system/locations";

    /**
     * The Constant SYSTEM_GROUPS.
     */
    private static final String SYSTEM_GROUPS = "system/permissions/groups";

    /**
     * The Constant SYSTEM_PERMISSIONS_ROLES.
     */
    private static final String SYSTEM_PERMISSIONS_ROLES = "system/permissions/roles";

    /**
     * The Constant SYSTEM_USER_DIRECTORIES.
     */
    private static final String SYSTEM_USER_DIRECTORIES = "system/user-directories";

    /**
     * The Constant SYSTEM_USERS.
     */
    private static final String SYSTEM_USERS = "system/users";

    /**
     * The Constant SYSTEM_LICENSE.
     */
    private static final String SYSTEM_LICENSE = "system/license";

    /**
     * The Constant DELETED_OBJECTS.
     */
    private static final String DELETED_OBJECTS = "data/deletedobjects";

    /**
     * The Constant WORKFLOW_SCHEME.
     */
    private static final String WORKFLOW_SCHEME = "system/wfScheme";

    /**
     * The Constant LOADCASE.
     */
    private static final String LOADCASE = "system/loadcase";

    /**
     * The Constant TRAINING_ALGO.
     */
    private static final String TRAINING_ALGO = "system/trainingAlgo";

    /**
     * The Constant CONTEXT.
     */
    private static final String CONTEXT = "/context";

    /**
     * The Constant EDIT.
     */
    private static final String EDIT = "edit";

    /**
     * The Constant ID.
     */
    private static final String ID = "{id}";

    /**
     * The Constant CONTEXT.
     */
    private static final String HIDDEN_SCHEME_IMAGE_CONTAINER = "_proj_contain_hidden_scheme_plot_";

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private JobDAO jobDao;

    /**
     * The tree manager.
     */
    private SuSObjectTreeManager treeManager;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public BreadCrumbDTO createBreadCrumb( String userId, String uuid, String api ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return createBreadCrumb( entityManager, userId, uuid, api );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BreadCrumbDTO createBreadCrumb( EntityManager entityManager, String userId, String uuid, String api ) {
        if ( ValidationUtils.validateUUIDString( uuid ) ) {
            SuSEntity latestSuSEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( uuid ) );
            if ( latestSuSEntity == null ) {
                JobEntity jobEntity = jobDao.getJob( entityManager, UUID.fromString( uuid ) );
                if ( jobEntity == null ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), uuid ) );
                }
                return getBreadCrumbDTOForJobEntity( entityManager, userId, api, jobEntity );
            } else {
                return getBreadCrumbDTOForSusEntity( entityManager, userId, api, latestSuSEntity );
            }
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_ID_IS_NOT_VALID.getKey(), uuid ) );
        }
    }

    private BreadCrumbDTO getBreadCrumbDTOForSusEntity( EntityManager entityManager, String userId, String api,
            SuSEntity latestSuSEntity ) {
        BreadCrumbDTO breadCrumbDTO;
        List< SuSEntity > susEntityList = recursivelyCreateBreadCrumbForSusEntity( entityManager, latestSuSEntity );
        if ( !latestSuSEntity.isHidden() ) {
            Collections.reverse( susEntityList );
        }
        susEntityList.add( latestSuSEntity );
        List< BreadCrumbItemDTO > breadCrumbItemDTOList = processBreadCrumbForSusEntity( entityManager, userId, susEntityList, api );

        // this case is for hidden data objects (plotting : bubble : generate image etc)
        if ( latestSuSEntity.isHidden() ) {
            prepareBreadcrumbForHiddenObjects( breadCrumbItemDTOList );
        }

        breadCrumbDTO = new BreadCrumbDTO();
        breadCrumbDTO.setBreadCrumbItems( breadCrumbItemDTOList );
        return breadCrumbDTO;
    }

    private BreadCrumbDTO getBreadCrumbDTOForJobEntity( EntityManager entityManager, String userId, String api, JobEntity jobEntity ) {
        BreadCrumbDTO breadCrumbDTO;
        List< JobEntity > jobEntityList = recursivelyCreateBreadCrumbForJobEntity( entityManager, jobEntity );
        Collections.reverse( jobEntityList );
        jobEntityList.add( jobEntity );
        List< BreadCrumbItemDTO > breadCrumbItemDTOList = processBreadCrumbForJobEntity( entityManager, userId, jobEntityList, api );

        breadCrumbDTO = new BreadCrumbDTO();
        breadCrumbDTO.setBreadCrumbItems( breadCrumbItemDTOList );
        return breadCrumbDTO;
    }

    /**
     * Prepare breadcrumb for hidden objects.
     *
     * @param breadCrumbItemDTOList
     *         the bread crumb item DTO list
     */
    private void prepareBreadcrumbForHiddenObjects( List< BreadCrumbItemDTO > breadCrumbItemDTOList ) {
        boolean container = true;
        if ( breadCrumbItemDTOList.size() >= 2 ) {
            for ( BreadCrumbItemDTO breadCrumbItemDTO : breadCrumbItemDTOList ) {
                if ( container ) {
                    breadCrumbItemDTO.setName( "Plot" );
                    container = false;
                }
            }
        }
    }

    /**
     * Recursively Create Bread Crumb.
     *
     * @param entityManager
     *         the entity manager
     * @param susEntity
     *         the sus entity
     *
     * @return the List
     */
    private List< SuSEntity > recursivelyCreateBreadCrumbForSusEntity( EntityManager entityManager, SuSEntity susEntity ) {
        List< SuSEntity > susEntityList = new ArrayList<>();
        List< SuSEntity > parentSuSEntity = susDAO.getParents( entityManager, susEntity, null );
        if ( CollectionUtil.isNotEmpty( parentSuSEntity ) ) {
            SuSEntity child = parentSuSEntity.get( LIST_FIRST_INDEX );
            // this condition deal If cyclic relation occur
            if ( !child.getComposedId().getId().toString().equals( susEntity.getComposedId().getId().toString() ) ) {
                susEntityList.add( child );
                List< SuSEntity > nestedSuSEntityList = recursivelyCreateBreadCrumbForSusEntity( entityManager,
                        susEntityList.get( LIST_FIRST_INDEX ) );
                susEntityList.addAll( nestedSuSEntityList );

            }
        }
        return susEntityList;
    }

    /**
     * Recursively Create Bread Crumb.
     *
     * @param entityManager
     *         the entity manager
     * @param jobEntity
     *         the job entity
     *
     * @return the List
     */
    private List< JobEntity > recursivelyCreateBreadCrumbForJobEntity( EntityManager entityManager, JobEntity jobEntity ) {
        List< JobEntity > susEntityList = new ArrayList<>();

        if ( jobEntity != null ) {
            JobEntity jobParent = new JobEntity();
            jobParent.setName( SimuspaceFeaturesEnum.JOBS.getKey() );
            jobParent.setId( UUID.fromString( SimuspaceFeaturesEnum.JOBS.getId() ) );
            susEntityList.add( jobParent );
            Collections.reverse( susEntityList );
        }

        return susEntityList;
    }

    /**
     * Process Bread Crumb.
     *
     * @param userId
     *         the user id
     * @param susEntityList
     *         the sus entity list
     * @param api
     *         the api
     *
     * @return The List
     */
    private List< BreadCrumbItemDTO > processBreadCrumbForSusEntity( EntityManager entityManager, String userId,
            List< SuSEntity > susEntityList, String api ) {
        List< BreadCrumbItemDTO > breadCrumbItemDTOList = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( susEntityList ) ) {
            for ( SuSEntity susEntity : susEntityList ) {
                if ( !susEntity.getName().equalsIgnoreCase( SimuspaceFeaturesEnum.ROOT.getKey() ) ) {
                    BreadCrumbItemDTO breadCrumbItemDTO = prepareBreadCrumbItems( susEntity );
                    breadCrumbItemDTOList.add( breadCrumbItemDTO );
                    if ( ValidationUtils.isNotNullOrEmpty( api ) ) {
                        BreadCrumbItemDTO contextMenuBreadCrumb = prepareContextMenuBreadCrumbItemsForSusEntity( entityManager, userId,
                                susEntity, api );
                        if ( contextMenuBreadCrumb != null ) {
                            breadCrumbItemDTOList.add( contextMenuBreadCrumb );
                        }
                    }
                }
            }
        }
        return breadCrumbItemDTOList;

    }

    /**
     * Process Bread Crumb.
     *
     * @param userId
     *         the user id
     * @param susEntityList
     *         the sus entity list
     * @param api
     *         the api
     *
     * @return The List
     */
    private List< BreadCrumbItemDTO > processBreadCrumbForJobEntity( EntityManager entityManager, String userId,
            List< JobEntity > jobEntityList, String api ) {
        List< BreadCrumbItemDTO > breadCrumbItemDTOList = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( jobEntityList ) ) {
            for ( JobEntity jobEntity : jobEntityList ) {
                if ( !jobEntity.getName().equalsIgnoreCase( SimuspaceFeaturesEnum.ROOT.getKey() ) ) {
                    BreadCrumbItemDTO breadCrumbItemDTO = prepareBreadCrumbItemsForJob( jobEntity );
                    breadCrumbItemDTOList.add( breadCrumbItemDTO );
                    if ( ValidationUtils.isNotNullOrEmpty( api ) ) {
                        BreadCrumbItemDTO contextMenuBreadCrumb = prepareContextMenuBreadCrumbItemsForJobEntity( entityManager, userId,
                                jobEntity, api );
                        if ( contextMenuBreadCrumb != null ) {
                            breadCrumbItemDTOList.add( contextMenuBreadCrumb );
                        }
                    }
                }
            }
        }
        return breadCrumbItemDTOList;

    }

    /**
     * Check Entity Or Set Bread Crumb Data.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return BreadCrumbItemDTO
     */
    private BreadCrumbItemDTO prepareBreadCrumbItems( SuSEntity susEntity ) {
        BreadCrumbItemDTO breadCrumbItemDTO = new BreadCrumbItemDTO();
        UserDTO user = TokenizedLicenseUtil.getUser(
                BundleUtils.getUserTokenFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() ) );
        if ( susEntity instanceof SystemContainerEntity ) {
            if ( susEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.ALLWORKFLOWS.getId() )
                    || susEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.WORKFLOWS_PLOTTING.getId() ) ) {
                setNameAndUrl( breadCrumbItemDTO, translateSystemEntityName( susEntity ),
                        VIEW_WORKFLOW_PROJECT_TABLE_URL + susEntity.getComposedId().getId() );
            }
            if ( susEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.DATA.getId() ) ) {
                setNameAndUrl( breadCrumbItemDTO, susEntity.getName(), ENTITY_URL_VIEW_DATA + susEntity.getComposedId().getId() );
            }
            if ( susEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.SYSTEM.getId() ) || susEntity.getComposedId()
                    .getId().toString().equals( SimuspaceFeaturesEnum.RIGHTS.getId() ) || susEntity.getComposedId().getId().toString()
                    .equals( SimuspaceFeaturesEnum.CONFIGURATION.getId() ) ) {
                breadCrumbItemDTO.setName( translateSystemEntityName( susEntity ) );
            }
        }

        if ( susEntity instanceof ProjectEntity ) {
            setNameAndUrl( breadCrumbItemDTO, translateName( user, susEntity ), ENTITY_URL_VIEW_DATA + susEntity.getComposedId().getId() );
        } else if ( susEntity instanceof DataObjectEntity ) {
            setNameAndUrl( breadCrumbItemDTO, translateName( user, susEntity ),
                    DATA_OBJECT_ENTITY_URL_VIEW_DATA_OBJECT + susEntity.getComposedId().getId() );
        } else if ( susEntity instanceof LibraryEntity ) {
            setNameAndUrl( breadCrumbItemDTO, translateName( user, susEntity ), ENTITY_URL_VIEW_DATA + susEntity.getComposedId().getId() );
        } else if ( susEntity instanceof VariantEntity || susEntity instanceof ProjectOverviewEntity ) {
            setNameAndUrl( breadCrumbItemDTO, translateName( user, susEntity ), ENTITY_URL_VIEW_DATA + susEntity.getComposedId().getId() );
        } else if ( susEntity instanceof WorkflowProjectEntity ) {
            setNameAndUrl( breadCrumbItemDTO, translateName( user, susEntity ),
                    VIEW_WORKFLOW_PROJECT_TABLE_URL + susEntity.getComposedId().getId() );
        } else if ( susEntity instanceof WorkflowEntity ) {
            breadCrumbItemDTO.setUrl(
                    VIEW_WORKFLOW + susEntity.getComposedId().getId() + URL_PART_VERSION + susEntity.getComposedId().getVersionId() );
        } else if ( susEntity.getName().equalsIgnoreCase( SimuspaceFeaturesEnum.JOBS.getKey() ) ) {
            breadCrumbItemDTO = setSystemJobsBreadCrumb( susEntity );
        } else if ( susEntity.getName().equalsIgnoreCase( SimuspaceFeaturesEnum.SEARCH.getKey() ) ) {
            breadCrumbItemDTO = setSystemSearchBreadCrumb( susEntity );
        } else if ( susEntity.getName().equalsIgnoreCase( SimuspaceFeaturesEnum.AUDIT.getKey() ) ) {
            breadCrumbItemDTO = setSystemAuditLogBreadCrumb( susEntity );
        } else if ( susEntity.getName().equalsIgnoreCase( SimuspaceFeaturesEnum.GROUPS.getKey() ) ) {
            breadCrumbItemDTO = setViewSystemGroupsBreadCrumb( susEntity );
        } else if ( susEntity.getName().equalsIgnoreCase( SimuspaceFeaturesEnum.ROLES.getKey() ) ) {
            breadCrumbItemDTO = setSystemPermissionRolesBreadCrumb( susEntity );
        } else if ( susEntity.getName().equalsIgnoreCase( SimuspaceFeaturesEnum.DIRECTORIES.getKey() ) ) {
            breadCrumbItemDTO = setSystemUserDirectoriesBreadCrumb( susEntity );
        } else if ( susEntity.getName().equalsIgnoreCase( SimuspaceFeaturesEnum.USERS.getKey() ) ) {
            breadCrumbItemDTO = setSystemUsersBreadCrumb( susEntity );
        } else if ( susEntity.getName().equalsIgnoreCase( SimuspaceFeaturesEnum.LICENSES.getKey() ) ) {
            breadCrumbItemDTO = setSystemLicenseBreadCrumb( susEntity );
        } else if ( susEntity.getName().equalsIgnoreCase( SimuspaceFeaturesEnum.DELETED_OBJECTS.getKey() ) ) {
            breadCrumbItemDTO = setSystemDeletedObjectsBreadCrumb( susEntity );
        } else if ( susEntity.getName().equalsIgnoreCase( SimuspaceFeaturesEnum.LOCATIONS.getKey() ) ) {
            setNameAndUrl( breadCrumbItemDTO, translateSystemEntityName( susEntity ), SYSTEM_LOCATIONS );
        } else if ( susEntity.getName().equalsIgnoreCase( SimuspaceFeaturesEnum.WFSCHEMES.getKey() ) ) {
            breadCrumbItemDTO = setWFSchemeBreadCrumb( susEntity );
        } else if ( susEntity.getName().equalsIgnoreCase( SimuspaceFeaturesEnum.LOADCASES.getKey() ) ) {
            breadCrumbItemDTO = setLoadcaseBreadCrumb( susEntity );
        } else if ( susEntity.getName().equalsIgnoreCase( SimuspaceFeaturesEnum.TRAINING_ALGO.getKey() ) ) {
            breadCrumbItemDTO = setTrainingAlgoBreadCrumb( susEntity );
        }

        if ( null == breadCrumbItemDTO.getName() ) {
            breadCrumbItemDTO.setName( translateName( user, susEntity ) );
        }

        breadCrumbItemDTO.setContext( OBJECT_TREE_URL + susEntity.getComposedId().getId() + CONTEXT );

        if ( isHiddenContent( susEntity ) ) {
            breadCrumbItemDTO.setUrl( null );
        }
        breadCrumbItemDTO.setItemId( susEntity.getComposedId().getId() );
        return breadCrumbItemDTO;
    }

    /**
     * Sets name and url.
     *
     * @param breadCrumbItemDTO
     *         the bread crumb item dto
     * @param name
     *         the name
     * @param url
     *         the url
     */
    private void setNameAndUrl( BreadCrumbItemDTO breadCrumbItemDTO, String name, String url ) {
        breadCrumbItemDTO.setName( name );
        breadCrumbItemDTO.setUrl( url );
    }

    /**
     * Check Entity Or Set Bread Crumb Data.
     *
     * @param jobEntity
     *         the job entity
     *
     * @return BreadCrumbItemDTO bread crumb item dto
     */
    private BreadCrumbItemDTO prepareBreadCrumbItemsForJob( JobEntity jobEntity ) {
        BreadCrumbItemDTO breadCrumbItemDTO = new BreadCrumbItemDTO();
        if ( SimuspaceFeaturesEnum.JOBS.getId().equals( jobEntity.getId().toString() ) ) {
            breadCrumbItemDTO.setUrl( VIEW_JOB + SimuspaceFeaturesEnum.JOBS.getId() );
        } else {
            breadCrumbItemDTO.setUrl( VIEW_JOB + jobEntity.getId() );
        }
        if ( null == breadCrumbItemDTO.getName() ) {
            breadCrumbItemDTO.setName( jobEntity.getName() );
        }
        breadCrumbItemDTO.setContext( OBJECT_TREE_URL + jobEntity.getId() + CONTEXT );
        breadCrumbItemDTO.setItemId( jobEntity.getId() );
        return breadCrumbItemDTO;
    }

    /**
     * Translate name.
     *
     * @param user
     *         the userDTO
     * @param entity
     *         the entity
     */
    private String translateName( UserDTO user, SuSEntity entity ) {
        if ( PropertiesManager.hasTranslation() && null != user ) {
            String userLang =
                    user.getId().equals( ConstantsID.SUPER_USER_ID ) || user.getUserDetails() == null || user.getUserDetails().isEmpty()
                            ? ConstantsString.DEFAULT_LANGUAGE : user.getUserDetails().iterator().next().getLanguage();

            entity.getTranslation().forEach( translation -> {
                if ( userLang.equals( translation.getLanguage() ) && null != translation.getName() && !translation.getName().isEmpty() ) {
                    entity.setName( translation.getName() );
                }
            } );

        }
        return entity.getName();
    }

    /**
     * Translate system entity name.
     *
     * @param entity
     *         the entity
     *
     * @return the string
     */
    private String translateSystemEntityName( SuSEntity entity ) {
        return MessageBundleFactory.getMessage( SimuspaceFeaturesEnum.getCodeById( entity.getComposedId().getId().toString() ) );
    }

    /**
     * Is Hidden Content.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return The boolean
     */
    private boolean isHiddenContent( SuSEntity susEntity ) {
        return susEntity.getName().equalsIgnoreCase( HIDDEN_SCHEME_IMAGE_CONTAINER );
    }

    /**
     * Prepare Context Menu Bread Crumb Item.
     *
     * @param userId
     *         the user id
     * @param susEntity
     *         the sus entity
     * @param api
     *         the api
     *
     * @return The Bread Crumb Item DTO
     */
    private BreadCrumbItemDTO prepareContextMenuBreadCrumbItemsForSusEntity( EntityManager entityManager, String userId,
            SuSEntity susEntity, String api ) {
        BreadCrumbItemDTO contextCrumbItemDTO = null;
        if ( ValidationUtils.validateUUIDString( susEntity.getComposedId().getId().toString() ) ) {
            List< ContextMenuItem > contextMenuItemList = treeManager.findMenu( entityManager, userId, "",
                    susEntity.getComposedId().getId().toString() );
            for ( ContextMenuItem contextMenuItem : contextMenuItemList ) {
                String contextTitle = contextMenuItem.getTitle();
                String contextUrl = contextMenuItem.getUrl();
                if ( null != api && api.contains( EDIT ) ) {
                    contextUrl = contextUrl.replace( ID,
                            api.substring( api.lastIndexOf( ConstantsString.FORWARD_SLASH ) + ConstantsInteger.INTEGER_VALUE_ONE ) );
                }

                if ( contextUrl.equals( api ) ) {
                    contextCrumbItemDTO = new BreadCrumbItemDTO();
                    setNameAndUrl( contextCrumbItemDTO, contextTitle, contextUrl );
                    contextCrumbItemDTO.setContext( OBJECT_TREE_URL + UUID.randomUUID() + CONTEXT );
                    contextCrumbItemDTO.setItemId( susEntity.getComposedId().getId() );
                }
            }
        }
        return contextCrumbItemDTO;
    }

    /**
     * Prepare Context Menu Bread Crumb Item.
     *
     * @param userId
     *         the user id
     * @param jobEntity
     *         the sus entity
     * @param api
     *         the api
     *
     * @return The Bread Crumb Item DTO
     */
    private BreadCrumbItemDTO prepareContextMenuBreadCrumbItemsForJobEntity( EntityManager entityManager, String userId,
            JobEntity jobEntity, String api ) {
        BreadCrumbItemDTO contextCrumbItemDTO = null;
        if ( ValidationUtils.validateUUIDString( jobEntity.getId().toString() ) ) {
            List< ContextMenuItem > contextMenuItemList = treeManager.findMenu( entityManager, userId, "", jobEntity.getId().toString() );
            for ( ContextMenuItem contextMenuItem : contextMenuItemList ) {
                String contextTitle = contextMenuItem.getTitle();
                String contextUrl = contextMenuItem.getUrl();
                if ( null != api && api.contains( EDIT ) ) {
                    contextUrl = contextUrl.replace( ID,
                            api.substring( api.lastIndexOf( ConstantsString.FORWARD_SLASH ) + ConstantsInteger.INTEGER_VALUE_ONE ) );
                }

                if ( contextUrl.equals( api ) ) {
                    contextCrumbItemDTO = new BreadCrumbItemDTO();
                    contextCrumbItemDTO.setName( contextTitle );
                    contextCrumbItemDTO.setUrl( contextUrl );
                    contextCrumbItemDTO.setContext( OBJECT_TREE_URL + UUID.randomUUID() + CONTEXT );
                    contextCrumbItemDTO.setItemId( jobEntity.getId() );
                }
            }
        }
        return contextCrumbItemDTO;
    }

    /**
     * Set System Audit Log Bread Crumb.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return BreadCrumbItemDTO
     */
    private BreadCrumbItemDTO setSystemAuditLogBreadCrumb( SuSEntity susEntity ) {
        BreadCrumbItemDTO breadCrumbItemDTO = new BreadCrumbItemDTO();
        setNameAndUrl( breadCrumbItemDTO, translateSystemEntityName( susEntity ), SYSTEM_AUDIT_LOG );
        breadCrumbItemDTO.setContext( SYSTEM_AUDIT_LOG );
        breadCrumbItemDTO.setItemId( susEntity.getComposedId().getId() );
        return breadCrumbItemDTO;
    }

    /**
     * Set View System Groups Bread Crumb.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return BreadCrumbItemDTO
     */
    private BreadCrumbItemDTO setViewSystemGroupsBreadCrumb( SuSEntity susEntity ) {
        BreadCrumbItemDTO breadCrumbItemDTO = new BreadCrumbItemDTO();
        setNameAndUrl( breadCrumbItemDTO, translateSystemEntityName( susEntity ), SYSTEM_GROUPS );
        breadCrumbItemDTO.setContext( SYSTEM_GROUPS );
        breadCrumbItemDTO.setItemId( susEntity.getComposedId().getId() );
        return breadCrumbItemDTO;
    }

    /**
     * Set System Permission Roles Bread Crumb.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return BreadCrumbItemDTO
     */
    private BreadCrumbItemDTO setSystemPermissionRolesBreadCrumb( SuSEntity susEntity ) {
        BreadCrumbItemDTO breadCrumbItemDTO = new BreadCrumbItemDTO();
        setNameAndUrl( breadCrumbItemDTO, translateSystemEntityName( susEntity ), SYSTEM_PERMISSIONS_ROLES );
        breadCrumbItemDTO.setContext( SYSTEM_PERMISSIONS_ROLES );
        breadCrumbItemDTO.setItemId( susEntity.getComposedId().getId() );
        return breadCrumbItemDTO;
    }

    /**
     * Set System User Directories Bread Crumb.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return BreadCrumbItemDTO
     */
    private BreadCrumbItemDTO setSystemUserDirectoriesBreadCrumb( SuSEntity susEntity ) {
        BreadCrumbItemDTO breadCrumbItemDTO = new BreadCrumbItemDTO();
        setNameAndUrl( breadCrumbItemDTO, translateSystemEntityName( susEntity ), SYSTEM_USER_DIRECTORIES );
        breadCrumbItemDTO.setContext( SYSTEM_USER_DIRECTORIES );
        breadCrumbItemDTO.setItemId( susEntity.getComposedId().getId() );
        return breadCrumbItemDTO;
    }

    /**
     * Set System Users Bread Crumb.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return BreadCrumbItemDTO
     */
    private BreadCrumbItemDTO setSystemUsersBreadCrumb( SuSEntity susEntity ) {
        BreadCrumbItemDTO breadCrumbItemDTO = new BreadCrumbItemDTO();
        setNameAndUrl( breadCrumbItemDTO, translateSystemEntityName( susEntity ), SYSTEM_USERS );
        breadCrumbItemDTO.setContext( SYSTEM_USERS );
        breadCrumbItemDTO.setItemId( susEntity.getComposedId().getId() );
        return breadCrumbItemDTO;
    }

    /**
     * Set System License Bread Crumb.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return BreadCrumbItemDTO
     */
    private BreadCrumbItemDTO setSystemLicenseBreadCrumb( SuSEntity susEntity ) {
        BreadCrumbItemDTO breadCrumbItemDTO = new BreadCrumbItemDTO();
        setNameAndUrl( breadCrumbItemDTO, translateSystemEntityName( susEntity ), SYSTEM_LICENSE );
        breadCrumbItemDTO.setContext( SYSTEM_LICENSE );
        breadCrumbItemDTO.setItemId( susEntity.getComposedId().getId() );
        return breadCrumbItemDTO;
    }

    /**
     * Sets the system deleted objects bread crumb.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the bread crumb item DTO
     */
    private BreadCrumbItemDTO setSystemDeletedObjectsBreadCrumb( SuSEntity susEntity ) {
        BreadCrumbItemDTO breadCrumbItemDTO = new BreadCrumbItemDTO();
        setNameAndUrl( breadCrumbItemDTO, translateSystemEntityName( susEntity ), DELETED_OBJECTS );
        breadCrumbItemDTO.setContext( DELETED_OBJECTS );
        breadCrumbItemDTO.setItemId( susEntity.getComposedId().getId() );
        return breadCrumbItemDTO;
    }

    /**
     * Sets the WF scheme bread crumb.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the bread crumb item DTO
     */
    private BreadCrumbItemDTO setWFSchemeBreadCrumb( SuSEntity susEntity ) {
        BreadCrumbItemDTO breadCrumbItemDTO = new BreadCrumbItemDTO();
        setNameAndUrl( breadCrumbItemDTO, translateSystemEntityName( susEntity ), WORKFLOW_SCHEME );
        breadCrumbItemDTO.setContext( OBJECT_TREE_URL + susEntity.getComposedId().getId() + CONTEXT );
        breadCrumbItemDTO.setItemId( susEntity.getComposedId().getId() );
        return breadCrumbItemDTO;
    }

    /**
     * Sets the loadcase bread crumb.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the bread crumb item DTO
     */
    private BreadCrumbItemDTO setLoadcaseBreadCrumb( SuSEntity susEntity ) {
        BreadCrumbItemDTO breadCrumbItemDTO = new BreadCrumbItemDTO();
        setNameAndUrl( breadCrumbItemDTO, translateSystemEntityName( susEntity ), LOADCASE );
        breadCrumbItemDTO.setContext( OBJECT_TREE_URL + susEntity.getComposedId().getId() + CONTEXT );
        breadCrumbItemDTO.setItemId( susEntity.getComposedId().getId() );
        return breadCrumbItemDTO;
    }

    /**
     * Sets the training algo bread crumb.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the bread crumb item DTO
     */
    private BreadCrumbItemDTO setTrainingAlgoBreadCrumb( SuSEntity susEntity ) {
        BreadCrumbItemDTO breadCrumbItemDTO = new BreadCrumbItemDTO();
        setNameAndUrl( breadCrumbItemDTO, translateSystemEntityName( susEntity ), TRAINING_ALGO );
        breadCrumbItemDTO.setContext( OBJECT_TREE_URL + susEntity.getComposedId().getId() + CONTEXT );
        breadCrumbItemDTO.setItemId( susEntity.getComposedId().getId() );
        return breadCrumbItemDTO;
    }

    /**
     * Sets the system jobs bread crumb.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the bread crumb item DTO
     */
    private BreadCrumbItemDTO setSystemJobsBreadCrumb( SuSEntity susEntity ) {
        BreadCrumbItemDTO breadCrumbItemDTO = new BreadCrumbItemDTO();
        setNameAndUrl( breadCrumbItemDTO, translateSystemEntityName( susEntity ), VIEW_JOB );
        breadCrumbItemDTO.setContext( VIEW_JOB );
        breadCrumbItemDTO.setItemId( susEntity.getComposedId().getId() );
        return breadCrumbItemDTO;
    }

    /**
     * Sets the system search bread crumb.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the bread crumb item DTO
     */
    private BreadCrumbItemDTO setSystemSearchBreadCrumb( SuSEntity susEntity ) {
        BreadCrumbItemDTO breadCrumbItemDTO = new BreadCrumbItemDTO();
        setNameAndUrl( breadCrumbItemDTO, translateSystemEntityName( susEntity ), ConstantsString.EMPTY_STRING );
        breadCrumbItemDTO.setItemId( susEntity.getComposedId().getId() );
        return breadCrumbItemDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BreadCrumbDTO getGroupUsersBreadCrumb( String userId, String uuid ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final SuSUserGroupDTO suSUserGroupDTO = userCommonManager.getUserGroupByIdForBreadcrumb( entityManager,
                    UUID.fromString( uuid ) );
            BreadCrumbItemDTO groupNameBreadCrumbItemDTO = new BreadCrumbItemDTO();
            groupNameBreadCrumbItemDTO.setName( suSUserGroupDTO.getName() );
            groupNameBreadCrumbItemDTO.setContext( "system/permissions/group/" + uuid + CONTEXT );
            groupNameBreadCrumbItemDTO.setItemId( UUID.fromString( uuid ) );
            BreadCrumbDTO breadCrumbDTO = createBreadCrumb( entityManager, userId, SimuspaceFeaturesEnum.GROUPS.getId(), null );
            List< BreadCrumbItemDTO > breadCrumbItemDTOList = breadCrumbDTO.getBreadCrumbItems();
            breadCrumbItemDTOList.add( groupNameBreadCrumbItemDTO );
            breadCrumbDTO.setBreadCrumbItems( breadCrumbItemDTOList );
            return breadCrumbDTO;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BreadCrumbDTO getUserGroupsBreadCrumb( String userId, String uuid ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final UserDTO user = userCommonManager.getUserById( entityManager, UUID.fromString( uuid ) );
            BreadCrumbItemDTO groupNameBreadCrumbItemDTO = new BreadCrumbItemDTO();
            groupNameBreadCrumbItemDTO.setName( user.getName() );
            groupNameBreadCrumbItemDTO.setContext( "system/user/" + uuid + CONTEXT );
            groupNameBreadCrumbItemDTO.setItemId( UUID.fromString( uuid ) );
            BreadCrumbDTO breadCrumbDTO = createBreadCrumb( entityManager, userId, SimuspaceFeaturesEnum.USERS.getId(), null );
            List< BreadCrumbItemDTO > breadCrumbItemDTOList = breadCrumbDTO.getBreadCrumbItems();
            breadCrumbItemDTOList.add( groupNameBreadCrumbItemDTO );
            breadCrumbDTO.setBreadCrumbItems( breadCrumbItemDTOList );
            return breadCrumbDTO;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BreadCrumbDTO getRoleGroupsBreadCrumb( String userId, String roleId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            BreadCrumbItemDTO roleBreadCrumbItemDTO = new BreadCrumbItemDTO();
            roleBreadCrumbItemDTO.setName( userCommonManager.getRoleNameById( entityManager, UUID.fromString( roleId ) ) );
            roleBreadCrumbItemDTO.setContext( "system/permissions/role/" + roleId + CONTEXT );
            roleBreadCrumbItemDTO.setItemId( UUID.fromString( roleId ) );
            BreadCrumbDTO breadCrumbDTO = createBreadCrumb( entityManager, userId, SimuspaceFeaturesEnum.ROLES.getId(), null );
            List< BreadCrumbItemDTO > breadCrumbItemDTOList = breadCrumbDTO.getBreadCrumbItems();
            breadCrumbItemDTOList.add( roleBreadCrumbItemDTO );
            breadCrumbDTO.setBreadCrumbItems( breadCrumbItemDTOList );
            return breadCrumbDTO;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BreadCrumbDTO createRunWorkflowBreadCrumb( String userId, String uuid, String api ) {
        return createRunBreadCrumb( userId, uuid, api );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BreadCrumbDTO createRunSchemeBreadCrumb( String userId, String uuid, String api ) {
        return createRunBreadCrumb( userId, uuid, api );
    }

    /**
     * Create run bread crumb bread crumb dto.
     *
     * @param userId
     *         the user id
     * @param uuid
     *         the uuid
     * @param api
     *         the api
     *
     * @return the bread crumb dto
     */
    private BreadCrumbDTO createRunBreadCrumb( String userId, String uuid, String api ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            BreadCrumbDTO breadCrumb = createBreadCrumb( entityManager, userId, uuid, api );
            List< BreadCrumbItemDTO > list = breadCrumb.getBreadCrumbItems();
            list.get( list.size() - 1 ).setUrl( api );
            breadCrumb.setBreadCrumbItems( list );
            return breadCrumb;
        } finally {
            entityManager.close();
        }
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
     * Sets the tree manager.
     *
     * @param treeManager
     *         the new tree manager
     */
    public void setTreeManager( SuSObjectTreeManager treeManager ) {
        this.treeManager = treeManager;
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

    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void setJobDao( JobDAO jobDao ) {
        this.jobDao = jobDao;
    }

}
