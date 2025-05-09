package de.soco.software.simuspace.server.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.UUID;

import de.soco.software.simuspace.server.manager.ParserManager;
import de.soco.software.simuspace.server.service.rest.BaseService;
import de.soco.software.simuspace.server.service.rest.ParserService;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.common.model.ParserVariableDTO;

/**
 * The Class ParserServiceImpl.
 *
 * @author noman arshad
 */
public class ParserServiceImpl extends BaseService implements ParserService {

    /**
     * The parser manager.
     */
    ParserManager parserManager;

    /**
     * Gets the parser manager.
     *
     * @return the parser manager
     */
    public ParserManager getParserManager() {
        return parserManager;
    }

    /**
     * Sets the parser manager.
     *
     * @param parserManager
     *         the new parser manager
     */
    public void setParserManager( ParserManager parserManager ) {
        this.parserManager = parserManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getParser( String json ) {
        try {
            return ResponseUtils.success(
                    parserManager.getParserObject( getUserIdFromGeneralHeader().toString(), getUserNameFromGeneralHeader(), json ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getParserAvailableUi( String parserId ) {
        try {
            return ResponseUtils
                    .success( new TableUI( parserManager.getParserAvailableUi( getUserIdFromGeneralHeader().toString(), parserId ),
                            parserManager.getObjectViewManager().getUserObjectViewsByKey(
                                    ConstantsObjectViewKey.PARSER_OBJECT_AVAILABLE_TABLE_KEY, getUserIdFromGeneralHeader().toString(),
                                    null ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getParserSelectedUi( String parserId ) {
        try {
            return ResponseUtils
                    .success( new TableUI( parserManager.getParserSelectedUi( getUserIdFromGeneralHeader().toString(), parserId ),
                            parserManager.getObjectViewManager().getUserObjectViewsByKey(
                                    ConstantsObjectViewKey.PARSER_OBJECT_SELECTED_TABLE_KEY, getUserIdFromGeneralHeader().toString(),
                                    null ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getParserEdit( String parserId ) {
        try {
            return ResponseUtils.success( parserManager.getParserEdit( getUserIdFromGeneralHeader().toString(), parserId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getParserAvailableList( String parserId, String jsonFilter ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( jsonFilter, FiltersDTO.class );
            return ResponseUtils
                    .success( parserManager.getParserAvailableList( getUserIdFromGeneralHeader().toString(), parserId, filter ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getParserSelectedList( String parserId, String jsonFilter ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( jsonFilter, FiltersDTO.class );
            return ResponseUtils
                    .success( parserManager.getParserSelectedList( getUserIdFromGeneralHeader().toString(), parserId, filter ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getParserUi( String parserId ) {
        try {
            return ResponseUtils.success( new TableUI( parserManager.getParserUi( parserId ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getParserList( String parserId, String jsonFilter ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( jsonFilter, FiltersDTO.class );
            return ResponseUtils.success( parserManager.getParserList( getUserIdFromGeneralHeader().toString(), parserId, filter ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getParserObjectSelectedUI( String parserId, String objectSelected ) {
        try {
            return ResponseUtils.success(
                    parserManager.getParserObjectSelectedUI( getUserIdFromGeneralHeader().toString(), objectSelected, parserId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getParserObjectCB2SelectorUI( String parserId, String typeSelected ) {
        try {
            return ResponseUtils.success(
                    parserManager.getParserObjectCB2SelectorUI( getUserIdFromGeneralHeader().toString(), typeSelected, parserId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getParserObjectSelectedUIForCB2Element( String objectSelected ) {
        try {
            return ResponseUtils.success(
                    parserManager.getParserObjectSelectedUIForCB2Element( getUserIdFromGeneralHeader().toString(), objectSelected ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getParserObjectAvailableContext( String parserId, String json ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( json, FiltersDTO.class );
            return ResponseUtils
                    .success( parserManager.getParserObjectAvailableContext( getUserIdFromGeneralHeader().toString(), parserId, filter ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getParserObjectSelectedContext( String parserId, String json ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( json, FiltersDTO.class );
            return ResponseUtils
                    .success( parserManager.getParserObjectSelectedContext( getUserIdFromGeneralHeader().toString(), parserId, filter ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response addEntryToParserSelectedList( String parserId, String selectionId ) {
        try {
            return ResponseUtils.success(
                    parserManager.addEntryToParserSelectedList( getUserIdFromGeneralHeader().toString(), parserId, selectionId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveEntryToParserAvailableAndSelectedList( String parserId, String json ) {
        try {
            final ParserVariableDTO parserVariableDTO = JsonUtils.jsonToObject( json, ParserVariableDTO.class );
            return ResponseUtils.success( parserManager.saveEntryToParserAvailableAndSelectedList( getUserIdFromGeneralHeader().toString(),
                    parserId, parserVariableDTO ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getEditFormForSelectedList( String parserId, String selectionId ) {
        try {
            return ResponseUtils
                    .success( parserManager.getEditFormForSelectedList( getUserIdFromGeneralHeader().toString(), parserId, selectionId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response removeEntryFromParserSelectedList( String parserId, String selectionId ) {
        try {
            return ResponseUtils.success(
                    parserManager.removeEntryFromParserSelectedList( getUserIdFromGeneralHeader().toString(), parserId, selectionId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllParserSelectedViews( String parserId ) {
        try {
            return ResponseUtils.success( parserManager.getObjectViewManager().getUserObjectViewsByKey(
                    ConstantsObjectViewKey.PARSER_OBJECT_SELECTED_TABLE_KEY, getUserIdFromGeneralHeader().toString(), null ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllParserAvailableViews( String parserId ) {
        try {
            return ResponseUtils.success( parserManager.getObjectViewManager().getUserObjectViewsByKey(
                    ConstantsObjectViewKey.PARSER_OBJECT_AVAILABLE_TABLE_KEY, getUserIdFromGeneralHeader().toString(), null ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveParserSelectedView( String viewJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.PARSER_OBJECT_SELECTED_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    parserManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveParserAvailableView( String viewJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.PARSER_OBJECT_AVAILABLE_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    parserManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteParserSelectedView( String viewId ) {
        try {
            if ( parserManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteParserAvailableView( String viewId ) {
        try {
            if ( parserManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateParserSelectedView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.PARSER_OBJECT_SELECTED_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    parserManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateParserAvailableView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.PARSER_OBJECT_AVAILABLE_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    parserManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setParserSelectedViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    parserManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ),
                            getUserIdFromGeneralHeader().toString(), ConstantsObjectViewKey.PARSER_OBJECT_SELECTED_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setParserAvailableViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    parserManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ),
                            getUserIdFromGeneralHeader().toString(), ConstantsObjectViewKey.PARSER_OBJECT_AVAILABLE_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.server.service.rest.ParserService#setParserAvailableViewAsDefault(java.lang.String, java.lang.String)
     */
    @Override
    public Response setParserAvailableViewAsDefault( String parserId, String filepath ) {
        try {
            return ResponseUtils.success( parserManager.prepareObjectivevariableForEngine( getUserIdFromGeneralHeader().toString(),
                    getUserNameFromGeneralHeader(), parserId, filepath ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.server.service.rest.ParserService#getParserSelectedFilePathByParserId(java.lang.String)
     */
    @Override
    public Response getParserSelectedFilePathByParserId( String parserId ) {
        try {
            return ResponseUtils.success( parserManager.getParserSelectedFilePathByParserId( getUserNameFromGeneralHeader(), parserId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

}
