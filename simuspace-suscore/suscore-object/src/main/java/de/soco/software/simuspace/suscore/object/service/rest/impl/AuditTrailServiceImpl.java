package de.soco.software.simuspace.suscore.object.service.rest.impl;

import javax.ws.rs.core.Response;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.data.utility.ContextUtil;
import de.soco.software.simuspace.suscore.object.manager.AuditTrailManager;
import de.soco.software.simuspace.suscore.object.service.rest.AuditTrailService;

/**
 * The Class AuditTrailServiceImpl.
 */
public class AuditTrailServiceImpl extends SuSBaseService implements AuditTrailService {

    /**
     * The audit trail manager.
     */
    AuditTrailManager auditTrailManager;

    /**
     * Gets the audit trail manager.
     *
     * @return the audit trail manager
     */
    public AuditTrailManager getAuditTrailManager() {
        return auditTrailManager;
    }

    /**
     * Sets the audit trail manager.
     *
     * @param auditTrailManager
     *         the new audit trail manager
     */
    public void setAuditTrailManager( AuditTrailManager auditTrailManager ) {
        this.auditTrailManager = auditTrailManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAuditTrailListOutsDetailsByObjectId( String objectId, String filter ) {
        try {
            final FiltersDTO filtersDTO = JsonUtils.jsonToObject( filter, FiltersDTO.class );
            return ResponseUtils.success(
                    auditTrailManager.getAuditTrailDataByIdListOuts( objectId, getUserIdFromGeneralHeader(), filtersDTO ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAuditTrailListInsDetailsByObjectId( String objectId, String filter ) {
        try {
            final FiltersDTO filtersDTO = JsonUtils.jsonToObject( filter, FiltersDTO.class );
            return ResponseUtils.success(
                    auditTrailManager.getAuditTrailDataByIdListIns( objectId, getUserIdFromGeneralHeader(), filtersDTO ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAuditTrailContextIns( String objectId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext(
                    auditTrailManager.getDataTableContext( getUserIdFromGeneralHeader(), objectId, filter,
                            getTokenFromGeneralHeader() ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAuditTrailContextOuts( String objectId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext(
                    auditTrailManager.getDataTableContext( getUserIdFromGeneralHeader(), objectId, filter,
                            getTokenFromGeneralHeader() ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAuditTrailByObjectIdIns( String objectId ) {
        try {
            return ResponseUtils.success( auditTrailManager.getAuditTrailByObjectIdIns( objectId, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAuditTrailByObjectIdOuts( String objectId ) {
        try {
            return ResponseUtils.success( auditTrailManager.getAuditTrailByObjectIdOuts( objectId, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAuditTrailDetailsUIOuts( String objectId ) {
        try {
            return ResponseUtils.success( auditTrailManager.getAuditTrailTableUI( objectId, getUserIdFromGeneralHeader() ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAuditTrailDetailsUIns( String objectId ) {
        try {
            return ResponseUtils.success( auditTrailManager.getAuditTrailTableUI( objectId, getUserIdFromGeneralHeader() ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAuditTrailByObjectId( String objectId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.AUDIT.getKey() ) + ConstantsString.SPACE + MessageBundleFactory.getMessage(
                                    Messages.TRAIL.getKey() ) ),
                    auditTrailManager.getAuditTrailByObjectIdIns( objectId, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAuditTrailDetailsUIView( String objectId ) {
        try {
            return ResponseUtils.success( auditTrailManager.getAuditTrailTableUIView( objectId, getUserIdFromGeneralHeader() ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response auditTrailSaveObjectView( String objectId, String viewJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    auditTrailManager.saveObjectView( getUserIdFromGeneralHeader(), objectId, viewJson, false ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response auditTrailUpdateObjectView( String objectId, String viewId, String viewJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    auditTrailManager.saveOrUpdateObjectView( getUserIdFromGeneralHeader(), objectId, viewJson, true ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response auditTrailDeleteObjectView( String objectId, String viewId ) {
        try {
            if ( auditTrailManager.deleteObjectView( viewId ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

}
