package de.soco.software.simuspace.suscore.core.service.impl;

import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.core.manager.AdditionalAttributeManager;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.core.model.SelectionItemOrder;
import de.soco.software.simuspace.suscore.core.service.SelectionService;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;

/**
 * The Class SelectionServiceImpl to handle selections related operations.
 *
 * @author Noman Arshad
 */
public class SelectionServiceImpl extends SuSBaseService implements SelectionService {

    /**
     * The Constant SELECTION_ORIGIN.
     */
    private static final String SELECTION_ORIGIN = "selections";

    /**
     * The Constant SSFE_ORIGIN.
     */
    private static final String SSFE_ORIGIN = "ssfe";

    /**
     * The selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The additional attribute manager.
     */
    private AdditionalAttributeManager additionalAttributeManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveAndUpdateSelections( String jsonSelection ) {
        try {
            FiltersDTO filterDTO = JsonUtils.jsonToObject( jsonSelection, FiltersDTO.class );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.SELECTION_SAVED.getKey() ),
                    selectionManager.createSelection( getUserIdFromGeneralHeader(), filterDTO.isSsfe() ? SSFE_ORIGIN : SELECTION_ORIGIN,
                            filterDTO ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveAndUpdateSelectionsWithOrigin( String origin, String jsonSelection ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.SELECTION_SAVED.getKey() ),
                    selectionManager.createSelection( getUserIdFromGeneralHeader(), origin,
                            JsonUtils.jsonToObject( jsonSelection, FiltersDTO.class ) ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllSelectionIds( String selectionId ) {
        try {
            return ResponseUtils.success( selectionManager.getSelectedIdsListBySelectionId( selectionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response addSelection( String selectionId, String filterJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.SELECTION_ADDED_IN_EXISTING_SELECTION.getKey() ),
                    selectionManager.addSelectionItemsInExistingSelection( selectionId,
                            JsonUtils.jsonToObject( filterJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response removeSelection( String selectionId, String filterJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.SELECTION_REMOVED_IN_EXISTING_SELECTION.getKey() ),
                    selectionManager.removeSelectionItemsInExistingSelection( selectionId,
                            JsonUtils.jsonToObject( filterJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSelectionSusEntityUI( String selectionId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.SELECTION_OF_ID.getKey() ) + ConstantsString.SPACE + selectionId ),
                    new TableUI( selectionManager.getGenericDTOUI( getUserIdFromGeneralHeader(), selectionId ), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateSelectionTableView( String selectionId, String columnsJson ) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = ( JSONObject ) parser.parse( columnsJson );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    selectionManager.updateSelectionView( getUserIdFromGeneralHeader(), selectionId,
                            JsonUtils.jsonToList( json.get( "columns" ).toString(), TableColumn.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSelectionSusEntityList( String selectionId, String filter ) {
        try {

            SelectionEntity selectionEntity = selectionManager.getSelectionEntityById( selectionId );
            if ( selectionEntity.getAdditionalAttributesJson() != null && !selectionEntity.getAdditionalAttributesJson().isEmpty() ) {
                return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                                MessageBundleFactory.getMessage( Messages.SELECTION_OF_ID.getKey() ) + ConstantsString.SPACE + selectionId ),
                        additionalAttributeManager.getGenericDTOListWithAdditionalAttribute( selectionId,
                                JsonUtils.jsonToObject( filter, FiltersDTO.class ) ) );
            } else {
                return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                                MessageBundleFactory.getMessage( Messages.SELECTION_OF_ID.getKey() ) + ConstantsString.SPACE + selectionId ),
                        selectionManager.getGenericDTOList( getUserIdFromGeneralHeader(), selectionId,
                                JsonUtils.jsonToObject( filter, FiltersDTO.class ) ) );
            }

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSelectionSusEntityList( String filter ) {
        try {
            return ResponseUtils.success( selectionManager.getGenericDTOList( JsonUtils.jsonToObject( filter, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the selection manager.
     *
     * @return the selection manager
     */
    public SelectionManager getSelectionManager() {
        return selectionManager;
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
     * {@inheritDoc}
     */
    @Override
    public Response reOrderSelection( String selectionId, String filter ) {
        try {
            return ResponseUtils.success(
                    selectionManager.reOrderSelection( selectionId, JsonUtils.jsonToList( filter, SelectionItemOrder.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllSelectionWFObjectSort( String selectionId ) {
        try {
            return ResponseUtils.success( selectionManager.getAllSelectionWFObjectSort( selectionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveWFObjectSortedSelection( String selectionId, String payload ) {
        try {
            return ResponseUtils.success( selectionManager.saveSelection( selectionId, payload ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveAndUpdateSelectionitemsWithAttributes( String selectionId, String selectionItemId, String payload ) {
        try {
            return ResponseUtils.success(
                    selectionManager.saveSelectionItemsWithAttributes( getUserIdFromGeneralHeader(), selectionId, selectionItemId, payload,
                            "Attributes Added" ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAttributeUI( String selectionId ) {
        try {
            return ResponseUtils.success( additionalAttributeManager.getSelectionAttributeUI( getUserIdFromGeneralHeader(), selectionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAttributeUiCreate( String selectionId ) {
        try {
            return ResponseUtils.success(
                    additionalAttributeManager.addAdditionalAttributeUiForm( getUserIdFromGeneralHeader(), selectionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAttributeUiList( String selectionId, String jsonFilter ) {
        try {
            return ResponseUtils.success( additionalAttributeManager.getSelectionAttributeList( getUserIdFromGeneralHeader(), selectionId,
                    JsonUtils.jsonToObject( jsonFilter, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveAttributeUi( String selectionId, String jsonAttrib ) {
        try {
            return ResponseUtils.success(
                    additionalAttributeManager.saveAttributeUiWithSelectionId( getUserIdFromGeneralHeader(), selectionId, jsonAttrib ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAttributeContext( String selectionId, String jsonFilter ) {
        try {
            return ResponseUtils.success( additionalAttributeManager.getAttributeContext( getUserIdFromGeneralHeader(), selectionId,
                    JsonUtils.jsonToObject( jsonFilter, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getEditAttributeForm( String selectionId, String attribSelectionId ) {
        try {
            return ResponseUtils.success(
                    additionalAttributeManager.getEditAttributeForm( getUserIdFromGeneralHeader(), selectionId, attribSelectionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteAttributeBySelection( String selectionId, String attribSelectionId ) {
        try {
            return ResponseUtils.success(
                    additionalAttributeManager.deleteAttributeBySelection( getUserIdFromGeneralHeader(), selectionId, attribSelectionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateAttributeWIthSelection( String selectionId, String attribSelectionId, String jsonAtrib ) {
        try {
            return ResponseUtils.success(
                    additionalAttributeManager.updateAttributeWIthSelection( getUserIdFromGeneralHeader(), selectionId, attribSelectionId,
                            jsonAtrib ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveAttributeValuesWithSelection( String selectionId, String attribSelectionId, String jsonFilter ) {
        try {
            return ResponseUtils.success(
                    additionalAttributeManager.saveAttributeValuesWithSelection( getUserIdFromGeneralHeader(), selectionId,
                            attribSelectionId, jsonFilter ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveSsfeAttributeValuesWithSelection( String selectionId, String jsonFilter ) {
        try {
            return ResponseUtils.success(
                    additionalAttributeManager.saveSsfeAttributeValuesWithSelection( getUserIdFromGeneralHeader(), selectionId,
                            jsonFilter ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAttributeUiCreateOption( String selectionId, String attribSelectionId, String option ) {
        try {
            return ResponseUtils.success(
                    additionalAttributeManager.getAttributeUiCreateOption( getUserIdFromGeneralHeader(), selectionId, attribSelectionId,
                            option ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the additional attribute manager.
     *
     * @return the additional attribute manager
     */
    public AdditionalAttributeManager getAdditionalAttributeManager() {
        return additionalAttributeManager;
    }

    /**
     * Sets the additional attribute manager.
     *
     * @param additionalAttributeManager
     *         the new additional attribute manager
     */
    public void setAdditionalAttributeManager( AdditionalAttributeManager additionalAttributeManager ) {
        this.additionalAttributeManager = additionalAttributeManager;
    }

}
