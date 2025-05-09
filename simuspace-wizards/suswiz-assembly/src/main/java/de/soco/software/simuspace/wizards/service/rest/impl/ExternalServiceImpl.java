package de.soco.software.simuspace.wizards.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.stream.Collectors;

import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.cb2.model.Cb2ScenerioTreeChildrenDTO;
import de.soco.software.simuspace.suscore.common.cb2.model.Cb2TreeChildrenDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.BmwCaeBenchEnums;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchInputDeckDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchKeyResultsDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchProjectsDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchReportDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchResultDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchSenarioDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchStoryBoardDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchSubModelDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchVariantDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.WorkFlowAdditionalAttributeDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.model.ObjectTreeViewDTO;
import de.soco.software.simuspace.suscore.data.model.TreeNodeDTO;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.object.manager.SuSObjectTreeManager;
import de.soco.software.simuspace.wizards.manager.LoadcaseManager;
import de.soco.software.simuspace.wizards.manager.WizardsManager;
import de.soco.software.simuspace.wizards.service.rest.ExternalService;

/**
 * The Class ExternalServiceImpl.
 */
public class ExternalServiceImpl extends SuSBaseService implements ExternalService {

    /**
     * The Constant SELECTION_ORIGIN.
     */
    private static final String SELECTION_ORIGIN = "selections";

    private static final String DATA_NODE = "/Data/";

    /**
     * The selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The tree manager.
     */
    private SuSObjectTreeManager treeManager;

    /**
     * The wizards manager.
     */
    private WizardsManager wizardsManager;

    /**
     * The loadcase manager.
     */
    private LoadcaseManager loadcaseManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveAndUpdateSelections( String jsonSelection ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.SELECTION_SAVED.getKey() ),
                    selectionManager.createSelection( getUserIdFromGeneralHeader(), SELECTION_ORIGIN,
                            JsonUtils.jsonToObject( jsonSelection, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response filterObjectTree( String external, String objectJson ) {
        Response response = null;
        try {
            ObjectTreeViewDTO request = JsonUtils.jsonToObject( objectJson, ObjectTreeViewDTO.class );
            switch ( external ) {
                case "bmw-dummyfiles":
                case "bmw-loadcases":
                    List< TreeNodeDTO > returnModel = treeManager.getTree( external, getUserIdFromGeneralHeader(), null, request,
                            getTokenFromGeneralHeader() );
                    response = ResponseUtils.success( returnModel );
                    break;
                case "bmw-cb2-keyresults":
                case "bmw-cb2-result":
                case "bmw-cb2-submodel":
                case "bmw-cb2-inputdeck":
                case "bmw-cb2-variant":
                case "bmw-cb2-project-tree":
                case "bmw-cb2-storyboard":
                case "bmw-cb2-report":
                case "bmw-cb2-project":

                    List< Cb2TreeChildrenDTO > cb2TreeList = wizardsManager
                            .getBmwCaeBenchTreeData( getUserIdFromGeneralHeader() ).getData();

                    if ( request != null && !StringUtils.isEmpty( request.getSearch() ) ) {
                        response = ResponseUtils.success( searchCb2Tree( request.getSearch(), cb2TreeList.get( 0 ), BmwCaeBenchEnums.getValueByKey( external ) ) );
                        break;
                    } else if ( !cb2TreeList.isEmpty() ) {
                        for ( Cb2TreeChildrenDTO cb2TreeChildrenDTO : cb2TreeList ) {
                            cb2TreeChildrenDTO.setTitle( BmwCaeBenchEnums.getValueByKey( external ) );
                            break;
                        }
                        response = ResponseUtils.success( cb2TreeList );
                        break;
                    }
                case "bmw-cb2-scenario-tree":
                    List< Cb2ScenerioTreeChildrenDTO > cb2SenarioTreeList = wizardsManager.readScenerioTreeJsonFile().getData();
                    response = ResponseUtils.success( cb2SenarioTreeList );
                    break;

            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
        return response;
    }

    public List< Cb2TreeChildrenDTO > searchCb2Tree( String value, Cb2TreeChildrenDTO start, String caeBench ) {
        List< Cb2TreeChildrenDTO > cb2TreeChildrenDTOs = new ArrayList<>();
        Queue< Cb2TreeChildrenDTO > queue = new LinkedList<>();
        queue.add( start );
        Cb2TreeChildrenDTO currentNode;
        while ( !queue.isEmpty() ) {
            currentNode = queue.remove();
            if ( currentNode.getTitle().equalsIgnoreCase( value ) ) {
                cb2TreeChildrenDTOs.add( currentNode );
            } else if ( null != currentNode.getChildren() ) {
                queue.addAll( currentNode.getChildren() );
            }
        }

        Cb2TreeChildrenDTO parentNode = prepareCb2TreeChildrenDTO( start.getId(), start.isLazy(), start.getDescription(),
                start.getElement(), start.isExpanded(), start.isFolder(), start.getPath(), start.getState(),
                caeBench, start.getUrl(), start.getIcon() );

        List< Cb2TreeChildrenDTO > result = new ArrayList<>();

        for ( Cb2TreeChildrenDTO cb2TreeChildrenDTO : cb2TreeChildrenDTOs ) {
            String[] paths = Arrays.stream(
                            cb2TreeChildrenDTO.getPath().replaceFirst( DATA_NODE, StringUtils.EMPTY ).split( ConstantsString.FORWARD_SLASH ) )
                    .toArray( String[]::new );
            Cb2TreeChildrenDTO indexNode = ( Cb2TreeChildrenDTO ) SerializationUtils.clone( start );

            prepareForSingleNode( 0, paths, indexNode );
            result.add( indexNode.getChildren().get( 0 ) );
        }
        parentNode.setChildren( result );
        return Arrays.asList( parentNode );
    }

    private void prepareForSingleNode( int index, String[] paths, Cb2TreeChildrenDTO start ) {
        if ( index != paths.length ) {
            String nodeName = paths[ index ];
            List< Cb2TreeChildrenDTO > childsToRemove = start.getChildren().stream()
                    .filter( element -> !nodeName.equalsIgnoreCase( element.getTitle() ) ).collect( Collectors.toList() );
            start.getChildren().removeAll( childsToRemove );
            index = index + 1;
            if ( index <= paths.length ) {
                prepareForSingleNode( index, paths, start.getChildren().get( 0 ) );
            }
        } else {
            start.setChildren( null );
        }
    }

    private Cb2TreeChildrenDTO prepareCb2TreeChildrenDTO( String id, boolean lazy, String description, String element, boolean expanded,
            boolean folder, String path, int state, String title, String url, String icon ) {
        Cb2TreeChildrenDTO result = new Cb2TreeChildrenDTO();
        result.setId( id );
        result.setLazy( lazy );
        result.setDescription( description );
        result.setElement( element );
        result.setExpanded( expanded );
        result.setFolder( folder );
        result.setPath( path );
        result.setState( state );
        result.setTitle( title );
        result.setUrl( url );
        result.setIcon( icon );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response externalUi( String external, String solverType, String selectionId ) {
        return externalUi( external, selectionId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response externalWorkflowUi( String external, String id ) {
        return externalUi( external, id );
    }

    /**
     * Adds the additional attribute in ui.
     *
     * @param listColumns
     *         the list columns
     * @param selectionId
     *         the selection id
     */
    private void addAdditionalAttributeInUi( List< TableColumn > listColumns, String selectionId ) {
        try {
            UUID.fromString( selectionId );
        } catch ( Exception e ) {
            return;
        }
        SelectionEntity entity = selectionManager.getSelectionEntityById( selectionId );
        if ( entity == null ) {
            throw new SusException( "Selection does not exist." );
        }
        if ( null != entity.getAdditionalAttributesJson() ) {
            int orderNum = selectionManager.getAdditionalAttributeManager().extractHighesOrderNumber( listColumns );
            List< WorkFlowAdditionalAttributeDTO > listAttrib = JsonUtils.jsonToList( entity.getAdditionalAttributesJson(),
                    WorkFlowAdditionalAttributeDTO.class );
            for ( WorkFlowAdditionalAttributeDTO workFlowAdditionalAttribute : listAttrib ) {
                listColumns.add( selectionManager.getAdditionalAttributeManager().prepareTableColForAdditionalAttrib( ( ++orderNum ),
                        workFlowAdditionalAttribute ) );
            }
        }
    }

    /**
     * External ui.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     *
     * @return the response
     */
    private Response externalUi( String external, String selectionId ) {
        Response response = null;
        if ( external.equalsIgnoreCase( "bmw-loadcases" ) ) {
            response = ResponseUtils.success( new TableUI( loadcaseManager.getLoadcaseUI( getUserIdFromGeneralHeader() ),
                    loadcaseManager.getObjectViewManager().getUserObjectViewsByKey( ConstantsObjectViewKey.BMW_LOADCASE_TABLE_KEY,
                            getUserIdFromGeneralHeader(), selectionId ) ) );
        } else if ( BmwCaeBenchEnums.CB2_KEYRESULTS.getKey().equals( external ) ) {
            response = prepareResponse( selectionId, ConstantsObjectViewKey.BMW_KEYRESULT_FILES_KEY, BmwCaeBenchKeyResultsDTO.class );
        } else if ( BmwCaeBenchEnums.CB2_INPUT_DECK.getKey().equals( external ) ) {
            response = prepareResponse( selectionId, ConstantsObjectViewKey.BMW_INPUTDECK_FILES_KEY, BmwCaeBenchInputDeckDTO.class );
        } else if ( BmwCaeBenchEnums.CB2_RESULT.getKey().equals( external ) ) {
            response = prepareResponse( selectionId, ConstantsObjectViewKey.BMW_CB2_RESULT_FILES_KEY, BmwCaeBenchResultDTO.class );
        } else if ( BmwCaeBenchEnums.CB2_REPORT.getKey().equals( external ) ) {
            response = prepareResponse( selectionId, ConstantsObjectViewKey.BMW_CB2_REPORT_FILES_KEY, BmwCaeBenchReportDTO.class );
        } else if ( BmwCaeBenchEnums.CB2_STORY_BOARD.getKey().equals( external ) ) {
            response = prepareResponse( selectionId, ConstantsObjectViewKey.BMW_CB2_STORY_BOARD_FILES_KEY, BmwCaeBenchStoryBoardDTO.class );
        } else if ( BmwCaeBenchEnums.CB2_SUBMODEL.getKey().equals( external )
                || BmwCaeBenchEnums.CB2_DUMMY_TEMP_TREE.getKey().equals( external ) ) {
            response = prepareResponse( selectionId, ConstantsObjectViewKey.BMW_SUBMODELS_FILES_KEY, BmwCaeBenchSubModelDTO.class );
        } else if ( BmwCaeBenchEnums.CB2_VARIANT.getKey().equals( external ) ) {
            response = prepareResponse( selectionId, ConstantsObjectViewKey.BMW_VARIANT_FILES_KEY, BmwCaeBenchVariantDTO.class );
        } else if ( BmwCaeBenchEnums.CB2_OBJECT_TREE.getKey().equals( external ) ) {
            response = prepareResponse( selectionId, ConstantsObjectViewKey.BMW_CB2_OBJECT_TREE_KEY, BmwCaeBenchSubModelDTO.class );
        } else if ( BmwCaeBenchEnums.CB2_SCENARIO.getKey().equals( external ) ) {
            response = prepareResponse( selectionId, ConstantsObjectViewKey.BMW_CB2_OBJECT_SENARIO_KEY, BmwCaeBenchSenarioDTO.class );
        } else if ( BmwCaeBenchEnums.CB2_PROJECT.getKey().equals( external ) ) {
            response = prepareResponse( selectionId, ConstantsObjectViewKey.BMW_CB2_PROJECTS_FILES_KEY, BmwCaeBenchProjectsDTO.class );
        } else if ( FieldTypes.OBJECT.getType().equals( external ) ) {
            response = prepareResponse( selectionId, null, GenericDTO.class );
        }

        return response;
    }

    /**
     * Prepare response.
     *
     * @param selectionId
     *         the selection id
     * @param key
     *         the key
     * @param clazz
     *         the clazz
     *
     * @return the response
     */
    private Response prepareResponse( String selectionId, String key, Class< ? >... clazz ) {
        List< TableColumn > listColumns;
        listColumns = GUIUtils.listColumns( clazz );
        addAdditionalAttributeInUi( listColumns, selectionId );
        listColumns = wizardsManager.applyViewToSelectionColumns( selectionId, listColumns );
        return ResponseUtils.success( new TableUI( listColumns,
                loadcaseManager.getObjectViewManager().getUserObjectViewsByKey( key, getUserIdFromGeneralHeader(), selectionId ) ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response externalList( String external, String solverType, String id, String payload ) {
        try {
            FiltersDTO filter;
            filter = JsonUtils.jsonToObject( payload, FiltersDTO.class );
            switch ( external ) {
                case "bmw-dummyfiles" -> {
                    return ResponseUtils.success( wizardsManager.getBmwCaeBenchObjectListFromCB2( getUserIdFromGeneralHeader(),
                            getUserNameFromGeneralHeader(), getTokenFromGeneralHeader(), id, filter, external, solverType ) );
                }
                case "bmw-cb2-project-tree", "bmw-cb2-keyresults", "bmw-cb2-result", "bmw-cb2-submodel", "bmw-cb2-inputdeck",
                     "bmw-cb2-variant", "bmw-cb2-storyboard", "bmw-cb2-report", "bmw-cb2-project" -> {
                    return ResponseUtils.success(
                            wizardsManager.getBmwCaeBenchObjectListFromCB2( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(),
                                    getTokenFromGeneralHeader(), id, filter, BmwCaeBenchEnums.getValueByKey( external ), solverType ) );
                }
                case "bmw-loadcases" -> {
                    return ResponseUtils
                            .success( wizardsManager.getLoadCaseSelectedDummyTypeList( getUserIdFromGeneralHeader(), id, solverType,
                                    filter ) );
                }
                default -> {
                }
            }
            return ResponseUtils.success( "External Type Doest Match Any Type" );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response externalWorkflowList( String external, String id, String payload ) {
        try {
            FiltersDTO filter;
            filter = JsonUtils.jsonToObject( payload, FiltersDTO.class );
            return ResponseUtils
                    .success( wizardsManager.getBmwCaeBenchObjectListFromCB2( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(),
                            getTokenFromGeneralHeader(), id, filter, BmwCaeBenchEnums.getValueByKey( external ), "" ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response addSelection( String external, String selectionId, String filterJson ) {
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
    public Response removeSelection( String external, String selectionId, String filterJson ) {
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
    public Response externalSelectionUI( String external, String selectionId ) {
        try {
            return externalUi( external, "", selectionId );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response externalSelectionList( String external, String selectionId, String payload ) {

        if ( external.equalsIgnoreCase( "bmw-loadcases" ) ) {
            return ResponseUtils.success( wizardsManager.getLoadCaseSelection( selectionId, payload ) );
        } else if ( external.equalsIgnoreCase( "object" ) ) {
            FiltersDTO filter = new FiltersDTO();
            filter.setDraw( 1 );
            filter.setStart( 0 );
            filter.setLength( Integer.MAX_VALUE );
            return ResponseUtils
                    .success( wizardsManager.getGenericDTOListBySelection( getUserIdFromGeneralHeader(), selectionId, filter ) );
        } else {
            return ResponseUtils.success( wizardsManager.getBmwCaeBenchObjectSelectionWithAttributes( selectionId, payload,
                    BmwCaeBenchEnums.getValueByKey( external ) ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveSortedSelection( String external, String selectionId, String payload ) {
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
    public Response externalAllSelectionIds( String external, String selectionId ) {
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
    public Response externalAllSelectionIdsType( String external, String selectionId ) {
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
    public Response getSortedSelection( String external, String selectionId ) {
        Response response = null;
        try {
            switch ( external ) {

                case "bmw-dummyfiles":
                case "bmw-cb2-keyresults":
                case "bmw-cb2-result":
                case "bmw-cb2-submodel":
                case "bmw-cb2-inputdeck":
                case "bmw-cb2-variant":
                case "bmw-cb2-scenario-tree":
                case "bmw-cb2-project":
                    response = ResponseUtils.success( wizardsManager.getBmwCaeBenchSortedSelection( selectionId ) );
                    break;
                case "bmw-loadcases":
                    break;
                case "bmw-cb2-project-tree":
                    response = ResponseUtils
                            .success( wizardsManager.getSortedSelection( selectionId, BmwCaeBenchEnums.getValueByKey( external ) ) );
                    break;
                default:
                    break;
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getExternalSelectorViewsForSolverType( String external, String id ) {
        Response response = null;
        try {
            switch ( external ) {
                case "bmw-cb2-keyresults" -> response = ResponseUtils.success( loadcaseManager.getObjectViewManager()
                        .getUserObjectViewsByKey( ConstantsObjectViewKey.BMW_KEYRESULT_FILES_KEY, getUserIdFromGeneralHeader(), id ) );
                case "bmw-cb2-result" -> response = ResponseUtils.success( loadcaseManager.getObjectViewManager()
                        .getUserObjectViewsByKey( ConstantsObjectViewKey.BMW_CB2_RESULT_FILES_KEY, getUserIdFromGeneralHeader(), id ) );
                case "bmw-cb2-submodel", "bmw-dummyfiles" -> response = ResponseUtils.success( loadcaseManager.getObjectViewManager()
                        .getUserObjectViewsByKey( ConstantsObjectViewKey.BMW_SUBMODELS_FILES_KEY, getUserIdFromGeneralHeader(), id ) );
                case "bmw-cb2-inputdeck" -> response = ResponseUtils.success( loadcaseManager.getObjectViewManager()
                        .getUserObjectViewsByKey( ConstantsObjectViewKey.BMW_INPUTDECK_FILES_KEY, getUserIdFromGeneralHeader(), id ) );
                case "bmw-cb2-variant" -> response = ResponseUtils.success( loadcaseManager.getObjectViewManager()
                        .getUserObjectViewsByKey( ConstantsObjectViewKey.BMW_VARIANT_FILES_KEY, getUserIdFromGeneralHeader(), id ) );
                case "bmw-loadcases" -> response = ResponseUtils.success( loadcaseManager.getObjectViewManager()
                        .getUserObjectViewsByKey( ConstantsObjectViewKey.BMW_LOADCASE_TABLE_KEY, getUserIdFromGeneralHeader(), id ) );
                case "bmw-cb2-project-tree" -> response = ResponseUtils.success( loadcaseManager.getObjectViewManager()
                        .getUserObjectViewsByKey( ConstantsObjectViewKey.BMW_CB2_OBJECT_TREE_KEY, getUserIdFromGeneralHeader(), id ) );
                case "bmw-cb2-storyboard" -> response = ResponseUtils.success( loadcaseManager.getObjectViewManager()
                        .getUserObjectViewsByKey( ConstantsObjectViewKey.BMW_CB2_STORY_BOARD_FILES_KEY, getUserIdFromGeneralHeader(),
                                id ) );
                case "bmw-cb2-report" -> response = ResponseUtils.success( loadcaseManager.getObjectViewManager()
                        .getUserObjectViewsByKey( ConstantsObjectViewKey.BMW_CB2_REPORT_FILES_KEY, getUserIdFromGeneralHeader(), id ) );
                case "bmw-cb2-project" -> response = ResponseUtils.success( loadcaseManager.getObjectViewManager()
                        .getUserObjectViewsByKey( ConstantsObjectViewKey.BMW_CB2_PROJECTS_FILES_KEY, getUserIdFromGeneralHeader(), id ) );
                default -> {
                }
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
        return response;
    }

    @Override
    public Response getExternalSelectorViewsForDataProject( String external, String id ) {
        return getExternalSelectorViewsForSolverType( external, id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveExternalSelectorViewForSolverType( String external, String id, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            switch ( external ) {
                case "bmw-cb2-keyresults" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_KEYRESULT_FILES_KEY );
                case "bmw-cb2-result" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_CB2_RESULT_FILES_KEY );
                case "bmw-cb2-submodel", "bmw-dummyfiles" ->
                        objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_SUBMODELS_FILES_KEY );
                case "bmw-cb2-inputdeck" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_INPUTDECK_FILES_KEY );
                case "bmw-cb2-variant" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_VARIANT_FILES_KEY );
                case "bmw-loadcases" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_LOADCASE_TABLE_KEY );
                case "bmw-cb2-project-tree" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_CB2_OBJECT_TREE_KEY );
                case "bmw-cb2-report" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_CB2_REPORT_FILES_KEY );
                case "bmw-cb2-storyboard" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_CB2_STORY_BOARD_FILES_KEY );
                case "bmw-cb2-project" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_CB2_PROJECTS_FILES_KEY );
                default -> {
                }
            }
            objectViewDTO.setObjectId( id );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    loadcaseManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response saveExternalSelectorViewForDataProject( String external, String id, String objectJson ) {
        return saveExternalSelectorViewForSolverType( external, id, objectJson );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setExternalSelectorViewAsDefault( String external, String viewId, String id ) {
        Response response = null;
        try {
            switch ( external ) {
                case "bmw-cb2-keyresults":
                    response = ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                            loadcaseManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ),
                                    getUserIdFromGeneralHeader(), ConstantsObjectViewKey.BMW_KEYRESULT_FILES_KEY, id ) );
                    break;
                case "bmw-cb2-result":
                    response = ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                            loadcaseManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ),
                                    getUserIdFromGeneralHeader(), ConstantsObjectViewKey.BMW_CB2_RESULT_FILES_KEY, id ) );
                    break;
                case "bmw-cb2-submodel":
                    response = ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                            loadcaseManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ),
                                    getUserIdFromGeneralHeader(), ConstantsObjectViewKey.BMW_SUBMODELS_FILES_KEY, id ) );
                    break;
                case "bmw-cb2-inputdeck":
                    response = ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                            loadcaseManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ),
                                    getUserIdFromGeneralHeader(), ConstantsObjectViewKey.BMW_INPUTDECK_FILES_KEY, id ) );
                    break;
                case "bmw-cb2-variant":
                    response = ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                            loadcaseManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ),
                                    getUserIdFromGeneralHeader(), ConstantsObjectViewKey.BMW_VARIANT_FILES_KEY, id ) );
                    break;
                case "bmw-dummyfiles":
                    response = ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                            loadcaseManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ),
                                    getUserIdFromGeneralHeader(), ConstantsObjectViewKey.BMW_SUBMODELS_FILES_KEY, id ) );
                    break;
                case "bmw-loadcases":
                    response = ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                            loadcaseManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ),
                                    getUserIdFromGeneralHeader(), ConstantsObjectViewKey.BMW_LOADCASE_TABLE_KEY, id ) );
                    break;
                case "bmw-cb2-project-tree":
                    response = ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                            loadcaseManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ),
                                    getUserIdFromGeneralHeader(), ConstantsObjectViewKey.BMW_CB2_OBJECT_TREE_KEY, id ) );
                    break;
                case "bmw-cb2-report":
                    response = ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                            loadcaseManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ),
                                    getUserIdFromGeneralHeader(), ConstantsObjectViewKey.BMW_CB2_REPORT_FILES_KEY, id ) );
                    break;
                case "bmw-cb2-storyboard":
                    response = ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                            loadcaseManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ),
                                    getUserIdFromGeneralHeader(), ConstantsObjectViewKey.BMW_CB2_STORY_BOARD_FILES_KEY, id ) );
                    break;
                case "bmw-cb2-project":
                    response = ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                            loadcaseManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ),
                                    getUserIdFromGeneralHeader(), ConstantsObjectViewKey.BMW_CB2_PROJECTS_FILES_KEY, id ) );

                default:
                    break;
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteExternalSelectorViewForSolverType( String viewId ) {
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

    @Override
    public Response deleteExternalSelectorView( String viewId ) {
        return deleteExternalSelectorViewForSolverType( viewId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateExternalSelectorViewForSolverType( String external, String viewId, String id, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            switch ( external ) {
                case "bmw-cb2-keyresults" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_KEYRESULT_FILES_KEY );
                case "bmw-cb2-result" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_CB2_RESULT_FILES_KEY );
                case "bmw-cb2-submodel", "bmw-dummyfiles" ->
                        objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_SUBMODELS_FILES_KEY );
                case "bmw-cb2-inputdeck" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_INPUTDECK_FILES_KEY );
                case "bmw-cb2-variant" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_VARIANT_FILES_KEY );
                case "bmw-loadcases" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_LOADCASE_TABLE_KEY );
                case "bmw-cb2-project-tree" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_CB2_OBJECT_TREE_KEY );
                case "bmw-cb2-report" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_CB2_REPORT_FILES_KEY );
                case "bmw-cb2-storyboard" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_CB2_STORY_BOARD_FILES_KEY );
                case "bmw-cb2-project" -> objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BMW_CB2_PROJECTS_FILES_KEY );
                default -> {
                }
            }
            objectViewDTO.setObjectId( id );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    loadcaseManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response updateExternalSelectorView( String external, String viewId, String id, String objectJson ) {
        return updateExternalSelectorViewForSolverType( external, viewId, id, objectJson );
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
     * Gets the tree manager.
     *
     * @return the tree manager
     */
    public SuSObjectTreeManager getTreeManager() {
        return treeManager;
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
     * Gets the wizards manager.
     *
     * @return the wizards manager
     */
    public WizardsManager getWizardsManager() {
        return wizardsManager;
    }

    /**
     * Sets the wizards manager.
     *
     * @param wizardsManager
     *         the new wizards manager
     */
    public void setWizardsManager( WizardsManager wizardsManager ) {
        this.wizardsManager = wizardsManager;
    }

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

    /**
     * Gets the selection attribute UI.
     *
     * @param external
     *         the external
     * @param selectionid
     *         the selectionid
     *
     * @return the selection attribute UI
     */
    @Override
    public Response getSelectionAttributeUI( String external, String selectionid ) {
        try {
            return ResponseUtils.success(
                    selectionManager.getAdditionalAttributeManager().getSelectionAttributeUI( getUserIdFromGeneralHeader(), selectionid ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the attribute ui list.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param jsonFilter
     *         the json filter
     *
     * @return the attribute ui list
     */
    @Override
    public Response getAttributeUiList( String external, String selectionId, String jsonFilter ) {
        try {
            return ResponseUtils.success( selectionManager.getAdditionalAttributeManager().getSelectionAttributeList(
                    getUserIdFromGeneralHeader(), selectionId, JsonUtils.jsonToObject( jsonFilter, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the attribute ui create.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     *
     * @return the attribute ui create
     */
    @Override
    public Response getAttributeUiCreate( String external, String selectionId ) {
        try {
            return ResponseUtils.success( selectionManager.getAdditionalAttributeManager()
                    .addAdditionalAttributeUiForm( getUserIdFromGeneralHeader(), selectionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Save attribute ui.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param jsonAttrib
     *         the json attrib
     *
     * @return the response
     */
    @Override
    public Response saveAttributeUi( String external, String selectionId, String jsonAttrib ) {
        try {
            return ResponseUtils.success( selectionManager.getAdditionalAttributeManager()
                    .saveAttributeUiWithSelectionId( getUserIdFromGeneralHeader(), selectionId, jsonAttrib ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the attribute context.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param jsonFilter
     *         the json filter
     *
     * @return the attribute context
     */
    @Override
    public Response getAttributeContext( String external, String selectionId, String jsonFilter ) {
        try {
            return ResponseUtils.success( selectionManager.getAdditionalAttributeManager().getAttributeContext(
                    getUserIdFromGeneralHeader(), selectionId, JsonUtils.jsonToObject( jsonFilter, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the edits the attribute form.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param attribSelectionId
     *         the attrib selection id
     *
     * @return the edits the attribute form
     */
    @Override
    public Response getEditAttributeForm( String external, String selectionId, String attribSelectionId ) {
        try {
            return ResponseUtils.success( selectionManager.getAdditionalAttributeManager()
                    .getEditAttributeForm( getUserIdFromGeneralHeader(), selectionId, attribSelectionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /**
     * Delete attribute by selection.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param attribSelectionId
     *         the attrib selection id
     *
     * @return the response
     */
    @Override
    public Response deleteAttributeBySelection( String external, String selectionId, String attribSelectionId ) {
        try {
            return ResponseUtils.success( selectionManager.getAdditionalAttributeManager()
                    .deleteAttributeBySelection( getUserIdFromGeneralHeader(), selectionId, attribSelectionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Update attribute W ith selection.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param attribSelectionId
     *         the attrib selection id
     * @param jsonAtrib
     *         the json atrib
     *
     * @return the response
     */
    @Override
    public Response updateAttributeWIthSelection( String external, String selectionId, String attribSelectionId, String jsonAtrib ) {
        try {
            return ResponseUtils.success( selectionManager.getAdditionalAttributeManager()
                    .updateAttributeWIthSelection( getUserIdFromGeneralHeader(), selectionId, attribSelectionId, jsonAtrib ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Save attribute values with selection.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     * @param attribSelectionId
     *         the attrib selection id
     * @param jsonFilter
     *         the json filter
     *
     * @return the response
     */
    @Override
    public Response saveAttributeValuesWithSelection( String external, String selectionId, String attribSelectionId, String jsonFilter ) {
        try {
            return ResponseUtils.success( selectionManager.getAdditionalAttributeManager()
                    .saveAttributeValuesWithSelection( getUserIdFromGeneralHeader(), selectionId, attribSelectionId, jsonFilter ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the attribute ui create option.
     *
     * @param external
     *         the external
     * @param option
     *         the option
     *
     * @return the attribute ui create option
     */
    @Override
    public Response getAttributeUiCreateOption( String external, String selectionId, String attribSelectionId, String option ) {
        try {
            return ResponseUtils.success( selectionManager.getAdditionalAttributeManager()
                    .getAttributeUiCreateOption( getUserIdFromGeneralHeader(), selectionId, attribSelectionId, option ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the all selection ids.
     *
     * @param external
     *         the external
     * @param selectionId
     *         the selection id
     *
     * @return the all selection ids
     */
    @Override
    public Response getAllSelectionIds( String external, String selectionId ) {
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
    public Response updateExternalSelectionTableView( String external, String selectionId, String columnsJson ) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = ( JSONObject ) parser.parse( columnsJson );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    wizardsManager.updateSelectionView( getUserIdFromGeneralHeader(), selectionId,
                            JsonUtils.jsonToList( json.get( "columns" ).toString(), TableColumn.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getBmwCaeBenchVariantSubModelObjectListFromCB2( String selectionId, String simdefOid ) {
        try {
            return ResponseUtils.success( wizardsManager.getBmwCaeBenchVariantSubModelObjectListFromCB2( getUserIdFromGeneralHeader(),
                    getUserNameFromGeneralHeader(), getTokenFromGeneralHeader(), selectionId, BmwCaeBenchEnums.CB2_VARIANT.getValue(),
                    simdefOid ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response addBmwCaeBenchVariantSubModelFormUI( String selectionId, String simdefOid ) {
        try {
            return ResponseUtils.success( wizardsManager.addBmwCaeBenchVariantSubModelFormUI( getUserIdFromGeneralHeader(), selectionId,
                    getUserNameFromGeneralHeader(), simdefOid ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response addBmwCaeBenchVariantSubModelFormOptions( String selectionId, String typeId, String simdefOid ) {
        try {
            return ResponseUtils.success( wizardsManager.getSubModelTypeOptions( getUserIdFromGeneralHeader(),
                    getUserNameFromGeneralHeader(), selectionId, typeId, getTokenFromGeneralHeader(), simdefOid ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getProjectItemOptions( String projectPath ) {
        try {
            return ResponseUtils.success( wizardsManager.getProjectItemOptions( getUserNameFromGeneralHeader(), projectPath ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getProjectPhaseOptions( String projectPath ) {
        try {
            return ResponseUtils.success( wizardsManager.getProjectPhaseOptions( getUserNameFromGeneralHeader(), projectPath ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

}
