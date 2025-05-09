package de.soco.software.simuspace.wizards.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.wizards.manager.QaDynaManager;
import de.soco.software.simuspace.wizards.model.QADyna.QADynaInputForm;
import de.soco.software.simuspace.wizards.model.QADyna.QADynaPPOForm;
import de.soco.software.simuspace.wizards.model.QADyna.QADynaParameterForm;
import de.soco.software.simuspace.wizards.service.rest.QaDynaService;

/**
 * The Class QaDynaServiceImpl.
 *
 * @author Ali Haider
 */
@Setter
public class QaDynaServiceImpl extends SuSBaseService implements QaDynaService {

    /**
     * The Qa dyna manager.
     */
    private QaDynaManager qaDynaManager;

    @Override
    public Response getQaDynaTabsUI( String projectId ) {
        try {
            return ResponseUtils.success( qaDynaManager.getQaDynaTabsUI( getUserIdFromGeneralHeader(), projectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response qaDynaBaseUi( String projectId ) {
        try {
            return ResponseUtils.success( qaDynaManager.prepareBaseTabUI( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(),
                    getTokenFromGeneralHeader(), projectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response saveBaseFields( String projectId, String payload ) {
        try {
            Map< String, String > base = new HashMap<>();
            base = ( Map< String, String > ) JsonUtils.jsonToMap( payload, base );
            return ResponseUtils.success( qaDynaManager.saveBaseFields( getUserIdFromGeneralHeader(), projectId, base.get( "base" ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response qaDynaParamUi( String projectId, String selectionId ) {
        try {
            return ResponseUtils.success( qaDynaManager.prepareParamTabUI( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(),
                    projectId, selectionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response saveParamsFields( String projectId, String selectionId, String payload ) {
        try {
            return ResponseUtils.success( qaDynaManager.saveParamFields( getUserIdFromGeneralHeader(), projectId, selectionId,
                    JsonUtils.jsonToObject( payload, QADynaParameterForm.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response qaDynaOptionsUI( String projectId, String selectionId, String setNumber, String option ) {
        try {
            return ResponseUtils.success( qaDynaManager.qaDynaOptionsUI( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(),
                    projectId, selectionId, setNumber, option ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response qaDynaFileSelectionUI( String projectId, String selectionId, String setNumber, String whichFile, String option ) {
        try {
            return ResponseUtils.success( qaDynaManager.qaDynaFileSelectionUI( getUserIdFromGeneralHeader(), projectId, selectionId,
                    setNumber, whichFile, option ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response ezFieldUI( String ezTypeName ) {
        try {
            return ResponseUtils.success( qaDynaManager.prepareEZFieldUI( ezTypeName ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response qaDynaReviewUI( String projectId, String selectionId ) {
        try {
            return ResponseUtils.success( qaDynaManager.qaDynaReviewUi( getUserIdFromGeneralHeader(), projectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response qaDynaReviewTableUI( String projectId ) {
        try {
            return ResponseUtils.success( new TableUI( qaDynaManager.qaDynaReviewTableUI( getUserIdFromGeneralHeader() ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getQaDynaInputTable( String projectId, String payload ) {
        try {
            Map< String, Object > map = new HashMap<>();
            return ResponseUtils.success( qaDynaManager.getQaDynaInputTable( getUserIdFromGeneralHeader(), projectId,
                    ( Map< String, Object > ) JsonUtils.jsonToMap( payload, map ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveInputReviewFields( String projectId, String payload ) {
        try {
            return ResponseUtils.success( qaDynaManager.saveInputReviewFields( getUserIdFromGeneralHeader(), getTokenFromGeneralHeader(),
                    projectId, JsonUtils.jsonToObject( payload, QADynaInputForm.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response qaDynaPPOUI( String projectId ) {
        try {
            return ResponseUtils
                    .success( qaDynaManager.preparePPOTabUI( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(), projectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response savePPOFieldsAndSubmit( String projectId, String payload ) {
        try {
            qaDynaManager.savePPOFieldAndSubmit( getUserIdFromGeneralHeader(), getTokenFromGeneralHeader(), projectId,
                    JsonUtils.jsonToObject( payload, QADynaPPOForm.class ) );
            return ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.JOB_SUBMITTED ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

}
