package de.soco.software.simuspace.server.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import de.soco.software.simuspace.server.manager.TemplateManager;
import de.soco.software.simuspace.server.service.rest.BaseService;
import de.soco.software.simuspace.server.service.rest.TemplateService;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.model.TemplateScanDTO;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;

/**
 * The Class TemplateServiceImpl.
 *
 * @author Fahad Rafi
 */
public class TemplateServiceImpl extends BaseService implements TemplateService {

    /**
     * The template manager.
     */
    private TemplateManager templateManager;

    /**
     * Gets the template manager.
     *
     * @return the template manager
     */
    public TemplateManager getTemplateManager() {
        return templateManager;
    }

    /**
     * Sets the template manager.
     *
     * @param templateManager
     *         the new template manager
     */
    public void setTemplateManager( TemplateManager templateManager ) {
        this.templateManager = templateManager;
    }

    /**
     * Gets the template.
     *
     * @param json
     *         the json
     *
     * @return the template
     */
    @Override
    public Response getTemplate( String json ) {
        try {
            return ResponseUtils.success(
                    templateManager.getTemplateSelectorModel( getUserIdStringFromGeneralHeader(), getUserNameFromGeneralHeader(), json ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets the template UI.
     *
     * @param id
     *         the id
     *
     * @return the template UI
     */
    @Override
    public Response getTemplateUI( String id ) {
        try {
            return ResponseUtils.success( new TableUI( templateManager.getTemplateUi( id ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets the template list.
     *
     * @param selectionId
     *         the selectionId
     * @param json
     *         the json
     *
     * @return the template list
     */
    @Override
    public Response getTemplateList( UUID selectionId, String json ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( json, FiltersDTO.class );
            return ResponseUtils.success( templateManager.getTemplateList( selectionId, filter ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets the template edit.
     *
     * @param templateId
     *         the template id
     *
     * @return the template edit
     */
    @Override
    public Response getTemplateEdit( String templateId ) {
        try {
            return ResponseUtils.success( templateManager.getTemplateEdit( getUserIdStringFromGeneralHeader(), templateId ) );

        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Creates the template UI from.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @Override
    public Response createTemplateUIFrom( String id ) {
        try {
            return ResponseUtils.success( templateManager.createTemplateUIForm( id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Creates the template.
     *
     * @param selectionId
     *         the selectionId
     * @param json
     *         the json
     *
     * @return the response
     */
    @Override
    public Response createTemplate( UUID selectionId, String json ) {
        try {
            return ResponseUtils
                    .success( templateManager.createTemplate( selectionId, JsonUtils.jsonToObject( json, TemplateScanDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets the file content.
     *
     * @param id
     *         the id
     *
     * @return the file content
     */
    @Override
    public Response getFileContent( UUID id ) {
        try {
            return ResponseUtils
                    .success( templateManager.getFileContent( getUserIdStringFromGeneralHeader(), getUserNameFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Delete template.
     *
     * @param selectionId
     *         the selection id
     * @param json
     *         the json
     *
     * @return the response
     */
    @Override
    public Response deleteTemplate( UUID selectionId, String json ) {
        try {
            JsonNode jsonNode = JsonUtils.toJsonNode( json );
            List< String > ids = new ArrayList<>();
            ids = ( List< String > ) JsonUtils.jsonToList( jsonNode.get( "selection" ).toString(), ids );
            return ResponseUtils.success( templateManager.deleteTemplate( ids ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Edits the template UI from.
     *
     * @param selectionId
     *         the selection id
     * @param id
     *         the id
     *
     * @return the response
     */
    @Override
    public Response editTemplateUIFrom( UUID selectionId, UUID id ) {
        try {
            return ResponseUtils.success( templateManager.editTemplateUIForm( id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Update template.
     *
     * @param selectionId
     *         the selection id
     * @param id
     *         the id
     * @param json
     *         the json
     *
     * @return the response
     */
    @Override
    public Response updateTemplate( UUID selectionId, UUID id, String json ) {
        try {
            return ResponseUtils.success( templateManager.updateTemplate( id, JsonUtils.jsonToObject( json, TemplateScanDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

}
