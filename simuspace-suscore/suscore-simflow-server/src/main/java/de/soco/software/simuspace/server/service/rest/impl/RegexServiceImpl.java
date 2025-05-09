package de.soco.software.simuspace.server.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import de.soco.software.simuspace.server.manager.RegexManager;
import de.soco.software.simuspace.server.model.RegexScanDTO;
import de.soco.software.simuspace.server.service.rest.BaseService;
import de.soco.software.simuspace.server.service.rest.RegexService;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;

/**
 * The type Regex service.
 */
public class RegexServiceImpl extends BaseService implements RegexService {

    /**
     * The Regex manager.
     */
    private RegexManager regexManager;

    @Override
    public Response createRegex( UUID selectionId, String json ) {
        try {
            return ResponseUtils.success( regexManager.createRegex( selectionId, JsonUtils.jsonToObject( json, RegexScanDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response updateRegex( UUID selectionId, UUID id, String json ) {
        try {
            return ResponseUtils.success( regexManager.updateRegex( id, JsonUtils.jsonToObject( json, RegexScanDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response deleteRegex( UUID selectionId, String json ) {
        try {
            JsonNode jsonNode = JsonUtils.toJsonNode( json );
            List< String > ids = new ArrayList<>();
            ids = ( List< String > ) JsonUtils.jsonToList( jsonNode.get( "selection" ).toString(), ids );
            return ResponseUtils.success( regexManager.deleteRegex( ids ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getRegex( UUID id ) {
        try {
            return ResponseUtils.success( regexManager.getRegex( id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getRegexContext( UUID id ) {
        try {
            return ResponseUtils.success( regexManager.getRegexContext( id ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getRegex( String json ) {
        try {
            return ResponseUtils.success(
                    regexManager.getRegexSelectorModel( getUserIdStringFromGeneralHeader(), getUserNameFromGeneralHeader(), json ) );

        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getRegexDropdownSelected( String wfId, String selectedOption ) {

        try {

            return ResponseUtils.success( regexManager.getRegexDropdownSelected( selectedOption, wfId, getUserIdFromGeneralHeader() ) );

        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }

    }

    @Override
    public Response getRegexCb2SelectorUI( String wfId, String selectedOption ) {

        try {

            return ResponseUtils.success( regexManager.getRegexCb2SelectorUI( selectedOption, wfId ) );

        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }

    }

    @Override
    public Response getFileContent( UUID id ) {
        try {
            return ResponseUtils
                    .success( regexManager.getFileContent( getUserIdStringFromGeneralHeader(), getUserNameFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getRegexUI( String selectionId ) {
        try {
            return ResponseUtils.success( new TableUI( regexManager.getRegexUi( selectionId ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response createRegexUIFrom( String id ) {
        try {
            return ResponseUtils.success( regexManager.createRegexUIForm( id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response editRegexUIFrom( UUID selectionId, UUID id ) {
        try {
            return ResponseUtils.success( regexManager.editRegexUIForm( id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getRegexList( UUID selectionId, String json ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( json, FiltersDTO.class );
            return ResponseUtils.success( regexManager.getRegexList( selectionId, filter ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getRegexEdit( String regexId ) {
        try {
            return ResponseUtils.success( regexManager.getRegexEdit( getUserIdStringFromGeneralHeader(), regexId ) );

        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets regex manager.
     *
     * @return the regex manager
     */
    public RegexManager getRegexManager() {
        return regexManager;
    }

    /**
     * Sets regex manager.
     *
     * @param regexManager
     *         the regex manager
     */
    public void setRegexManager( RegexManager regexManager ) {
        this.regexManager = regexManager;
    }

}
