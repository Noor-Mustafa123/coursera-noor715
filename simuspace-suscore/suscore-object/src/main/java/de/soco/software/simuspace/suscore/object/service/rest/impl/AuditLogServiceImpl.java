package de.soco.software.simuspace.suscore.object.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.manager.base.AuditLogManager;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.object.service.rest.AuditLogService;

/**
 * A class holding end point implementations for system container.
 *
 * @author Zeesahn jamal
 */
public class AuditLogServiceImpl extends SuSBaseService implements AuditLogService {

    /**
     * audit log reference.
     */
    private AuditLogManager auditLogManager;

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.service.rest.AuditLogService#searchAuditLog(java.lang.String)
     */
    @Override
    public Response searchAuditLog( String objectJson ) {

        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.AUDIT.getKey() ) ),
                    auditLogManager.searchAuditLog( getUserIdFromGeneralHeader(),
                            JsonUtils.jsonToObject( objectJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.service.rest.AuditLogService#listAuditLogsUI()
     */
    @Override
    public Response listAuditLogsUI() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.AUDIT.getKey() ) ), new TableUI( GUIUtils.listColumns( AuditLogDTO.class ),
                    auditLogManager.getObjectViewManager()
                            .getUserObjectViewsByKey( ConstantsObjectViewKey.GENERAL_AUDIT_TABLE_KEY, getUserIdFromGeneralHeader(),
                                    null ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.service.rest.AuditLogService#listAuditLogColumns()
     */
    @Override
    public Response listAuditLogColumns() {
        try {
            return ResponseUtils.success( GUIUtils.listColumns( AuditLogDTO.class ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.UserService#getAllViews()
     */
    @Override
    public Response getAllViews() {
        try {
            return ResponseUtils.success( auditLogManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.GENERAL_AUDIT_TABLE_KEY, getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#getView(java.lang.String)
     */
    @Override
    public Response getView( String viewId ) {
        try {
            return ResponseUtils.success( auditLogManager.getObjectViewManager().getObjectViewById( UUID.fromString( viewId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#saveView(java.lang.String)
     */
    @Override
    public Response saveView( String viewJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.GENERAL_AUDIT_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    auditLogManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#setViewAsDefault(java.lang.String)
     */
    @Override
    public Response setViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    auditLogManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.GENERAL_AUDIT_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#deleteView(java.lang.String)
     */
    @Override
    public Response deleteView( String viewId ) {
        try {
            if ( auditLogManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#updateView(java.lang.String, java.lang.String)
     */
    @Override
    public Response updateView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.GENERAL_AUDIT_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    auditLogManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getAllValuesForAuditLogTableColumn( String column ) {
        try {
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.ALL_VALUES_FOR_COLUMN_RETURNED_SUCCESSFULLY.getKey(), column ),
                    auditLogManager.getAllValuesForAuditLogTableColumn( column, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the audit log manager.
     *
     * @return the audit log manager
     */
    public AuditLogManager getAuditLogManager() {
        return auditLogManager;
    }

    /**
     * Sets the audit log manager.
     *
     * @param auditLogManager
     *         the new audit log manager
     */
    public void setAuditLogManager( AuditLogManager auditLogManager ) {
        this.auditLogManager = auditLogManager;
    }

}
