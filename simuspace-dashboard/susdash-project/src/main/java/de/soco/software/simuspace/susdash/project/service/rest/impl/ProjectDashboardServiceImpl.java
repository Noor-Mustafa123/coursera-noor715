package de.soco.software.simuspace.susdash.project.service.rest.impl;

import javax.ws.rs.core.Response;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Message;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.susdash.core.model.ProjectDashboardDTO;
import de.soco.software.simuspace.susdash.project.manager.ProjectDashboardManager;
import de.soco.software.simuspace.susdash.project.service.rest.ProjectDashboardService;

/**
 * The Class ProjectDashboardServiceImpl.
 */
public class ProjectDashboardServiceImpl extends SuSBaseService implements ProjectDashboardService {

    /**
     * The project dashboard manager.
     */
    private ProjectDashboardManager projectDashboardManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getProjectStructureChart( String objectId, String dashBoardLicenseJson ) {
        try {
            ProjectDashboardDTO projectDashboardDTO = JsonUtils.jsonToObject( dashBoardLicenseJson, ProjectDashboardDTO.class );
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.PROJECT_DASHBOARD_TREE_GENERATED_SUCCESSFULLY.getKey() ),
                    projectDashboardManager.getProjectStructureChart( objectId, projectDashboardDTO, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getProjectStructureUI( String objectId ) {
        try {
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.PROJECT_DASHBOARD_TREE_GENERATED_SUCCESSFULLY.getKey() ),
                    projectDashboardManager.getProjectStructureTableUI( getUserIdFromGeneralHeader(), objectId,
                            getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getProjectStructureTable( String objectId, String filtersJson ) {
        try {
            final FiltersDTO filter = filtersJson != null ? JsonUtils.jsonToObject( filtersJson, FiltersDTO.class ) : null;
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.PROJECT_DASHBOARD_TREE_GENERATED_SUCCESSFULLY.getKey() ),
                    projectDashboardManager.getProjectStructureTable( objectId, getUserIdFromGeneralHeader(), filter,
                            getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getProjectStructureViews( String ojectId ) {
        try {
            return ResponseUtils.success( projectDashboardManager.getUserProjectStructureViews( getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createProjectStructureView( String ojectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    projectDashboardManager.saveOrUpdateProjectStructureView( objectJson, getUserIdFromGeneralHeader(), true ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateProjectStructureView( String ojectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    projectDashboardManager.saveOrUpdateProjectStructureView( objectJson, getUserIdFromGeneralHeader(), false ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteProjectStructureView( String ojectId, String viewId ) {
        try {
            if ( projectDashboardManager.deleteProjectStructureView( viewId ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getProjectStructureContext( String objectId, String json ) {
        try {
            return ResponseUtils.success( null );
        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getProjectLifecycleUI( String objectId ) {
        try {
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.PROJECT_DASHBOARD_TREE_GENERATED_SUCCESSFULLY.getKey() ),
                    projectDashboardManager.getProjectLifecycleTableUI( getUserIdFromGeneralHeader(), objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getProjectLifecycleTable( String objectId, String filtersJson ) {
        try {
            final FiltersDTO filter = filtersJson != null ? JsonUtils.jsonToObject( filtersJson, FiltersDTO.class ) : null;
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.PROJECT_DASHBOARD_TREE_GENERATED_SUCCESSFULLY.getKey() ),
                    projectDashboardManager.getProjectLifecycleTable( objectId, getUserIdFromGeneralHeader(), filter,
                            getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getProjectLifecycleViews( String ojectId ) {
        try {
            return ResponseUtils.success( projectDashboardManager.getUserProjectLifecycleViews( getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createProjectLifecycleView( String ojectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    projectDashboardManager.saveOrUpdateProjectLifecycleView( objectJson, getUserIdFromGeneralHeader(), true ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateProjectLifecycleView( String ojectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    projectDashboardManager.saveOrUpdateProjectLifecycleView( objectJson, getUserIdFromGeneralHeader(), false ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteProjectLifecycleView( String ojectId, String viewId ) {
        try {
            if ( projectDashboardManager.deleteProjectLifecycleView( viewId ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getProjectLifecycleContext( String objectId, String json ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( json, FiltersDTO.class );
            return ResponseUtils
                    .success( projectDashboardManager.getProjectLifecycleContext( objectId, filter, getTokenFromGeneralHeader() ) );
        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response changeProjectLifecycle( String objectId, String selectionId, String statusId ) {
        try {
            return ResponseUtils.success( Message.SUCCESS,
                    projectDashboardManager.changeProjectLifecycle( objectId, selectionId, statusId, getTokenFromGeneralHeader() ) );
        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateDashboardCache( String objectId ) {
        try {
            projectDashboardManager.updateDashboardCache( objectId );
            return ResponseUtils.success( null );
        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getLastModified( String objectId ) {
        try {
            return ResponseUtils.success( projectDashboardManager.getDashboardLastModified( objectId, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Sets materials manager.
     *
     * @param projectDashboardManager
     *         the materials manager
     */
    public void setProjectDashboardManager( ProjectDashboardManager projectDashboardManager ) {
        this.projectDashboardManager = projectDashboardManager;
    }

}
