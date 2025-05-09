package de.soco.software.simuspace.wizards.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMode;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.wizards.manager.LoadcaseManager;
import de.soco.software.simuspace.wizards.model.LoadcaseWizardDTO;
import de.soco.software.simuspace.wizards.service.rest.LoadcaseService;

/**
 * The Class LoadcaseServiceImpl.
 */
public class LoadcaseServiceImpl extends SuSBaseService implements LoadcaseService {

    /**
     * The loadcase manager.
     */
    private LoadcaseManager loadcaseManager;

    /**
     * Gets the loadcase manager.
     *
     * @return the loadcase manager
     */
    public LoadcaseManager getLoadcaseManager() {
        return loadcaseManager;
    }

    /**
     * Sets the loadcase manager.
     *
     * @param loadcaseManager
     *         the new loadcase manager
     */
    public void setLoadcaseManager( LoadcaseManager loadcaseManager ) {
        this.loadcaseManager = loadcaseManager;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.soco.software.simuspace.wizards.service.rest.LoadcaseService#getLoadcaseUI
     * ()
     */
    @Override
    public Response getLoadcaseUI() {
        try {

            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.LOADCASE.getKey() ) ),
                    new TableUI( loadcaseManager.getLoadcaseUI( getUserIdFromGeneralHeader() ), loadcaseManager.getObjectViewManager()
                            .getUserObjectViewsByKey( ConstantsObjectViewKey.LOADCASE_TABLE_KEY, getUserIdFromGeneralHeader(), null ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.wizards.service.rest.LoadcaseService#
     * getLoadcaseData(java.lang.String)
     */
    @Override
    public Response getLoadcaseData( String objectFilterJson ) {
        try {

            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.LOADCASE.getKey() ) ),
                    loadcaseManager.getLoadcaseData( getUserIdFromGeneralHeader(),
                            JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.wizards.service.rest.LoadcaseService#
     * createLoadcaseUI()
     */
    @Override
    public Response createLoadcaseUI() {
        try {

            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.CREATE_LOADCASE.getKey() ) ),
                    loadcaseManager.createLoadcaseUI( getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.wizards.service.rest.LoadcaseService#
     * createLoadcase(java.lang.String)
     */
    @Override
    public Response createLoadcase( String payload ) {
        try {

            return ResponseUtils.success( loadcaseManager.createLoadcase( getUserIdFromGeneralHeader(),
                    JsonUtils.jsonToObject( payload, LoadcaseWizardDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.wizards.service.rest.LoadcaseService#
     * editLoadcaseUI(java.lang.String)
     */
    @Override
    public Response editLoadcaseUI( String loadcaseId ) {
        try {
            return ResponseUtils.success( loadcaseManager.editLoadcaseUI( getUserIdFromGeneralHeader(), loadcaseId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.wizards.service.rest.LoadcaseService#
     * updateLoadcase(java.lang.String, java.lang.String)
     */
    @Override
    public Response updateLoadcase( String loadcaseId, String payload ) {
        try {

            return ResponseUtils.success( loadcaseManager.updateLoadcase( getUserIdFromGeneralHeader(), loadcaseId,
                    JsonUtils.jsonToObject( payload, LoadcaseWizardDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.wizards.service.rest.LoadcaseService#deleteLoadcase(java.lang.String)
     */
    @Override
    public Response deleteLoadcase( UUID loadcaseId, String mode ) {
        try {
            if ( mode.equals( ConstantsMode.SINGLE ) ) {
                return ResponseUtils.success( loadcaseManager.deleteLoadcase( getUserIdFromGeneralHeader(), loadcaseId ) );
            } else {
                return ResponseUtils.success( loadcaseManager.deleteLoadcaseInBulk( getUserIdFromGeneralHeader(), loadcaseId.toString() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.wizards.service.rest.LoadcaseService#
     * getLoadcaseContextMenu(java.lang.String)
     */
    @Override
    public Response getLoadcaseContextMenu( String objectFilterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class );
            return ResponseUtils.success( loadcaseManager.getLoadcaseContextMenu( getUserIdFromGeneralHeader(), filter ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.wizards.service.rest.LoadcaseService#
     * getLoadcaseViews()
     */
    @Override
    public Response getLoadcaseViews() {
        try {
            return ResponseUtils.success( loadcaseManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.LOADCASE_TABLE_KEY, getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.wizards.service.rest.LoadcaseService#
     * saveLoadcaseView(java.lang.String)
     */
    @Override
    public Response saveLoadcaseView( String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.LOADCASE_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    loadcaseManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.wizards.service.rest.LoadcaseService#
     * setLoadcaseViewAsDefault(java.lang.String)
     */
    @Override
    public Response setLoadcaseViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    loadcaseManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.LOADCASE_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.wizards.service.rest.LoadcaseService#
     * deleteLoadcaseView(java.lang.String)
     */
    @Override
    public Response deleteLoadcaseView( String viewId ) {
        try {
            if ( loadcaseManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.wizards.service.rest.LoadcaseService#
     * updateLoadcaseView(java.lang.String, java.lang.String)
     */
    @Override
    public Response updateLoadcaseView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.LOADCASE_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    loadcaseManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

}
