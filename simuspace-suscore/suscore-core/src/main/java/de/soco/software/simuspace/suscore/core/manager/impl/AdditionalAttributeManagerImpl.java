package de.soco.software.simuspace.suscore.core.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.WorkFlowAdditionalAttributeDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.Renderer;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.core.dao.SelectionDAO;
import de.soco.software.simuspace.suscore.core.dao.SelectionItemDAO;
import de.soco.software.simuspace.suscore.core.manager.AdditionalAttributeManager;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionItemEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.SsfeSelectionDTO;
import de.soco.software.suscore.jsonschema.model.BindFrom;
import de.soco.software.suscore.jsonschema.model.Rules;

/**
 * The Class AdditionalAttributeManagerImpl.
 *
 * @author noman arshad
 */
@Log4j2
public class AdditionalAttributeManagerImpl implements AdditionalAttributeManager {

    private static final String BOOLEAN = "boolean";

    /**
     * The Constant RGBA.
     */
    private static final String RGBA = "rgba(";

    /**
     * The Constant COLOR.
     */
    private static final String COLOR = "color";

    /**
     * The Constant INTEGER.
     */
    private static final String INTEGER = "integer";

    /**
     * The Constant TEXT.
     */
    private static final String TEXT = "text";

    /**
     * The Constant RANDOM.
     */
    private static final String RANDOM = "random";

    /**
     * The Constant STATIC.
     */
    private static final String STATIC = "static";

    /**
     * The Constant STATIC_OPTION.
     */
    private static final String STATIC_OPTION = "staticOrRandomOption";

    /**
     * The Constant VALUE.
     */
    private static final String VALUE = "value";

    /**
     * The Constant DEFAULT_VALUE_LABEL.
     */
    private static final String DEFAULT_VALUE_LABEL = "Default Value";

    /**
     * The Constant OPTIONS.
     */
    private static final String OPTIONS = "options";

    /**
     * The common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The selection DAO.
     */
    private SelectionDAO selectionDAO;

    /**
     * The selection item DAO.
     */
    private SelectionItemDAO selectionItemDAO;

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The Entity manager factory reference.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI getSelectionAttributeUI( String userIdFromGeneralHeader, String selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TableUI tableUI;
        try {
            List< TableColumn > columns = GUIUtils.listColumns( WorkFlowAdditionalAttributeDTO.class );
            List< ObjectViewDTO > views = objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.USER_TABLE_KEY,
                    userIdFromGeneralHeader, null );
            tableUI = new TableUI( columns, views );
        } finally {
            entityManager.close();
        }
        return tableUI;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< WorkFlowAdditionalAttributeDTO > getSelectionAttributeList( String userIdFromGeneralHeader, String selectionId,
            FiltersDTO filtersDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        FilteredResponse< WorkFlowAdditionalAttributeDTO > filteredResponse;
        try {
            SelectionEntity entity = selectionDAO.getLatestObjectById( entityManager, SelectionEntity.class,
                    UUID.fromString( selectionId ) );
            if ( entity == null ) {
                throw new SusException( "Selection for Attribute Do not exists." );
            }

            if ( entity.getAdditionalAttributesJson() == null ) {
                filtersDTO.setTotalRecords( Long.valueOf( "0" ) );
                filtersDTO.setFilteredRecords( Long.valueOf( "0" ) );
                return PaginationUtil.constructFilteredResponse( filtersDTO, new ArrayList<>() );
            }
            List< WorkFlowAdditionalAttributeDTO > list = JsonUtils.jsonToList( entity.getAdditionalAttributesJson(),
                    WorkFlowAdditionalAttributeDTO.class );
            filtersDTO.setTotalRecords( ( long ) list.size() );
            filtersDTO.setFilteredRecords( ( long ) list.size() );

            List< WorkFlowAdditionalAttributeDTO > listFilterd = list.subList( filtersDTO.getStart(),
                    ( list.size() - filtersDTO.getStart() ) <= filtersDTO.getLength() ? list.size()
                            : ( filtersDTO.getStart() + filtersDTO.getLength() ) );

            filteredResponse = PaginationUtil.constructFilteredResponse( filtersDTO, listFilterd );
        } finally {
            entityManager.close();
        }
        return filteredResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< ? > getGenericDTOListWithAdditionalAttribute( String selectionId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        FilteredResponse< Object > filteredResponse;
        try {
            List< Object > genericDTO = new ArrayList<>();
            SelectionEntity selectionEnt = selectionDAO.getLatestObjectById( entityManager, SelectionEntity.class,
                    UUID.fromString( selectionId ) );
            /*
             * NOTE: prepare generic DTO along with Additional Attributes : Special case of
             * wf Attrib
             */
            List< WorkFlowAdditionalAttributeDTO > listAttrib = null;
            listAttrib = JsonUtils.jsonToList( selectionEnt.getAdditionalAttributesJson(), WorkFlowAdditionalAttributeDTO.class );
            Map< String, List< WorkFlowAdditionalAttributeDTO > > updateAttributeList = new HashMap<>();
            Set< SelectionItemEntity > selectionItemEntityList = selectionEnt.getItems();
            List< SelectionItemEntity > selectedObjects = selectionManager.getUserSelectionsBySelectionIds( entityManager, selectionId,
                    filter );
            boolean isSsfe = selectionEnt.getOrigin().equalsIgnoreCase( SelectionOrigins.SSFE.getOrigin() );
            if ( isSsfe ) {
                genericDTO = prepareGenericDTOSsfe( entityManager, selectionEnt, listAttrib, updateAttributeList, selectedObjects );
            } else {
                genericDTO = prepareGenericDTOObject( entityManager, selectionEnt, listAttrib, updateAttributeList, selectedObjects );
            }
            try {
                if ( !updateAttributeList.isEmpty() ) {
                    updateSelectionItemsByAttributrDTO( entityManager, updateAttributeList, selectionEnt.getItems(), isSsfe );
                }
            } catch ( Exception e ) {
                log.error( "updating attribute in list api failed", e );
            }
            filteredResponse = PaginationUtil.constructFilteredResponse( filter, genericDTO );
        } finally {
            entityManager.close();
        }
        return filteredResponse;
    }

    /**
     * Prepare generic DTO object.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionEnt
     *         the selection ent
     * @param listAttrib
     *         the list attrib
     * @param updateAttributeList
     *         the update attribute list
     * @param selectedObjects
     *         the selected objects
     *
     * @return the list
     */
    private List< Object > prepareGenericDTOObject( EntityManager entityManager, SelectionEntity selectionEnt,
            List< WorkFlowAdditionalAttributeDTO > listAttrib, Map< String, List< WorkFlowAdditionalAttributeDTO > > updateAttributeList,
            List< SelectionItemEntity > selectedObjects ) {
        List< Object > genericDTO = new ArrayList<>();
        List< UUID > selectedIdUUID = new ArrayList<>();
        for ( SelectionItemEntity selectionEntity : selectedObjects ) {
            selectedIdUUID.add( UUID.fromString( selectionEntity.getItem() ) );
        }
        List< SuSEntity > listEntiry = susDAO.getLatestNonDeletedObjectsByListOfIds( entityManager, selectedIdUUID );
        for ( SuSEntity suSEntity : listEntiry ) {
            if ( suSEntity != null ) {
                List< WorkFlowAdditionalAttributeDTO > updateAttributeMap = new ArrayList<>();
                GenericDTO generic = selectionManager.prepareGenericDTOFromObjectEntity( entityManager, null, suSEntity );
                Map< String, Object > map = new HashMap<>();
                map = ( Map< String, Object > ) JsonUtils.jsonToMap( JsonUtils.toJson( generic ), map );
                if ( listAttrib != null ) {
                    List< WorkFlowAdditionalAttributeDTO > itemSelectionList = getWorkFlowAdditionalAttributeAgainsSelection(
                            selectionEnt.getItems(), suSEntity );
                    for ( final WorkFlowAdditionalAttributeDTO sAttrib : listAttrib ) {
                        AtomicInteger isChanged = new AtomicInteger( ConstantsInteger.INTEGER_VALUE_ZERO );
                        WorkFlowAdditionalAttributeDTO selectionItemAttributeObj = getWorkFlowAdditionalAttributeDtoByName(
                                itemSelectionList, sAttrib, isChanged, selectionEnt.getItems() );
                        map.put( sAttrib.getName(), selectionItemAttributeObj );
                        if ( !String.valueOf( isChanged.intValue() )
                                .equalsIgnoreCase( String.valueOf( ConstantsInteger.INTEGER_VALUE_ZERO ) ) ) {
                            updateAttributeMap.add( selectionItemAttributeObj );
                        }
                    }
                }
                if ( !updateAttributeMap.isEmpty() ) {
                    updateAttributeList.put( suSEntity.getComposedId().getId().toString(), updateAttributeMap );
                }
                genericDTO.add( map );
            }
        }
        return genericDTO;
    }

    /**
     * Prepare generic DTO ssfe.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionEnt
     *         the selection ent
     * @param listAttrib
     *         the list attrib
     * @param updateAttributeList
     *         the update attribute list
     * @param selectedObjects
     *         the selected objects
     *
     * @return the list
     */
    private List< Object > prepareGenericDTOSsfe( EntityManager entityManager, SelectionEntity selectionEnt,
            List< WorkFlowAdditionalAttributeDTO > listAttrib, Map< String, List< WorkFlowAdditionalAttributeDTO > > updateAttributeList,
            List< SelectionItemEntity > selectedObjects ) {
        List< Object > genericDTO = new ArrayList<>();
        List< SsfeSelectionDTO > selectedObj = extractSsfeFromSelectionItemEntity( selectedObjects );
        for ( SsfeSelectionDTO ssfeSelectionDTO : selectedObj ) {
            Map< String, Object > map = new HashMap<>();
            map = ( Map< String, Object > ) JsonUtils.jsonToMap( JsonUtils.toJson( ssfeSelectionDTO ), map );
            List< WorkFlowAdditionalAttributeDTO > updateAttributeMap = new ArrayList<>();
            if ( listAttrib != null ) {
                List< WorkFlowAdditionalAttributeDTO > itemSelectionList = getAdditionalAttributeAgainSelection( selectionEnt.getItems(),
                        ssfeSelectionDTO );
                Map< String, Object > customAttributeMap = new HashMap<>();
                for ( final WorkFlowAdditionalAttributeDTO sAttrib : listAttrib ) {
                    AtomicInteger isChanged = new AtomicInteger( ConstantsInteger.INTEGER_VALUE_ZERO );
                    WorkFlowAdditionalAttributeDTO selectionItemAttributeObj = getWorkFlowAdditionalAttributeDtoByName( itemSelectionList,
                            sAttrib, isChanged, selectionEnt.getItems() );
                    map.put( sAttrib.getName(), selectionItemAttributeObj );
                    customAttributeMap.put( sAttrib.getName(), selectionItemAttributeObj.getValue() );
                    if ( !String.valueOf( isChanged.intValue() )
                            .equalsIgnoreCase( String.valueOf( ConstantsInteger.INTEGER_VALUE_ZERO ) ) ) {
                        updateAttributeMap.add( selectionItemAttributeObj );
                    }
                }
                map.put( "customAttributes", customAttributeMap );
            }
            if ( !updateAttributeMap.isEmpty() ) {
                updateAttributeList.put( ssfeSelectionDTO.getFullPath(), updateAttributeMap );
            }
            genericDTO.add( map );
        }
        try {
            if ( !updateAttributeList.isEmpty() ) {
                updateSelectionItemsByAttributrDTO( entityManager, updateAttributeList, selectionEnt.getItems(),
                        selectionEnt.getOrigin().equalsIgnoreCase( "ssfe" ) );
            }
        } catch ( Exception e ) {
            log.error( "updating attribute in list api failed", e );
        }
        return genericDTO;
    }

    /**
     * Extract ssfe from selection item entity.
     *
     * @param entityManager
     *         the entity manager
     * @param objectListByProperty
     *         the object list by property
     *
     * @return the list
     */
    private List< SsfeSelectionDTO > extractSsfeFromSelectionItemEntity( List< SelectionItemEntity > objectListByProperty ) {
        List< SsfeSelectionDTO > ssfeDTO = new ArrayList<>();
        for ( SelectionItemEntity selectionItemEntity : objectListByProperty ) {
            ssfeDTO.add( JsonUtils.jsonToObject( selectionItemEntity.getItem(), SsfeSelectionDTO.class ) );
        }
        return ssfeDTO;
    }

    /**
     * Update selection items by attributr DTO.
     *
     * @param entityManager
     *         the entity manager
     * @param updateAttributeList
     *         the update attribute list
     * @param selectionItemEntityList
     *         the selection item entity list
     */
    private void updateSelectionItemsByAttributrDTO( EntityManager entityManager,
            Map< String, List< WorkFlowAdditionalAttributeDTO > > updateAttributeList, Set< SelectionItemEntity > selectionItemEntityList,
            boolean isSsfe ) {
        for ( SelectionItemEntity itemEntity : selectionItemEntityList ) {
            List< WorkFlowAdditionalAttributeDTO > foundValue = isSsfe
                    ? updateAttributeList.get( JsonUtils.jsonToObject( itemEntity.getItem(), SsfeSelectionDTO.class ).getFullPath() )
                    : updateAttributeList.get( itemEntity.getItem() );
            if ( foundValue != null ) {
                List< WorkFlowAdditionalAttributeDTO > itemListAtr = new ArrayList<>();
                if ( itemEntity.getAdditionalAttributesJson() != null && !itemEntity.getAdditionalAttributesJson().isEmpty() ) {
                    itemListAtr = JsonUtils.jsonToList( itemEntity.getAdditionalAttributesJson(), WorkFlowAdditionalAttributeDTO.class );
                    for ( WorkFlowAdditionalAttributeDTO existingAtrib : itemListAtr ) {
                        for ( WorkFlowAdditionalAttributeDTO found : foundValue ) {
                            if ( existingAtrib.getId().equalsIgnoreCase( found.getId() ) ) {

                                existingAtrib.setType( found.getType() );
                                existingAtrib.setOptions( found.getOptions() );
                                existingAtrib.setValue( found.getValue() );
                                existingAtrib.setName( found.getName() );
                                existingAtrib.setLabel( found.getLabel() );
                                existingAtrib.setStaticOrRandomOption( found.getStaticOrRandomOption() );
                                existingAtrib.setMultiple( found.isMultiple() );
                                existingAtrib.setRules( found.getRules() );
                                existingAtrib.setBindFrom( found.getBindFrom() );
                                existingAtrib.setHelp( found.getHelp() );
                                existingAtrib.setMessages( found.getMessages() );
                                existingAtrib.setId( found.getId() );
                                existingAtrib.setConvert( found.getConvert() );
                            }
                        }
                    }

                    itemEntity.setAdditionalAttributesJson( JsonUtils.toJson( itemListAtr ) );
                    selectionItemDAO.saveOrUpdate( entityManager, itemEntity );

                } else if ( foundValue != null && !foundValue.isEmpty() ) {
                    itemEntity.setAdditionalAttributesJson( JsonUtils.toJson( foundValue ) );
                    selectionItemDAO.saveOrUpdate( entityManager, itemEntity );
                }

            }
        }
    }

    /**
     * Gets the work flow additional attribute by name 2.
     *
     * @param attribList
     *         the attrib list
     * @param sAttrib
     *         the s attrib
     * @param isChanged
     *         the is changed
     * @param selectionItemEntityList
     *         the selection item entity list
     *
     * @return the work flow additional attribute by name 2
     */

    @Override
    public WorkFlowAdditionalAttributeDTO getWorkFlowAdditionalAttributeDtoByName( List< WorkFlowAdditionalAttributeDTO > attribList,
            WorkFlowAdditionalAttributeDTO sAttrib, AtomicInteger isChanged, Set< SelectionItemEntity > selectionItemEntityList ) {
        WorkFlowAdditionalAttributeDTO attribMain = null;
        boolean isFound = false;
        if ( attribList != null ) {
            for ( WorkFlowAdditionalAttributeDTO sItemAttrib : attribList ) {
                if ( sItemAttrib.getId().equalsIgnoreCase( sAttrib.getId() )
                        && sItemAttrib.getType().equalsIgnoreCase( sAttrib.getType() ) ) {
                    isFound = true;
                    if ( sItemAttrib.getType().equalsIgnoreCase( COLOR ) ) {
                        if ( sItemAttrib.getValue() != null && !sItemAttrib.getValue().toString().isEmpty() ) {
                            return sItemAttrib;
                        }
                        String colorType = ( String ) sAttrib.getOptions().get( 0 );
                        if ( colorType.contains( STATIC ) ) {
                            sItemAttrib.setValue( sItemAttrib.getStaticOrRandomOption() == null ? sAttrib.getStaticOrRandomOption()
                                    : sItemAttrib.getStaticOrRandomOption() );
                            isChanged.set( isChanged.intValue() + 1 );
                            return sItemAttrib;
                        } else if ( colorType.contains( RANDOM ) ) {
                            Color randomColor = new Color( new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat() );
                            sItemAttrib.setValue( RGBA + randomColor.getRed() + "," + randomColor.getGreen() + "," + randomColor.getBlue()
                                    + "," + 1 + ")" );
                            isChanged.set( isChanged.intValue() + 1 );
                            return sItemAttrib;
                        }
                    } else if ( sItemAttrib.getType().equalsIgnoreCase( TEXT ) ) {
                        if ( sItemAttrib.getValue() != null && !sItemAttrib.getValue().toString().isEmpty() ) {
                            return sItemAttrib;
                        }
                        String textType = ( String ) sAttrib.getOptions().get( 0 );
                        if ( textType.contains( STATIC ) ) {
                            sItemAttrib.setValue( sItemAttrib.getStaticOrRandomOption() );
                            isChanged.set( isChanged.intValue() + 1 );
                            return sItemAttrib;
                        } else if ( textType.contains( WorkFlowAdditionalAttributeDTO.TEXT_RANDOM ) ) {
                            String maxInt = getMaxTextIntFromSelectionItemEntityList( selectionItemEntityList, sAttrib );
                            sItemAttrib.setValue( sAttrib.getStaticOrRandomOption() + "_" + ( maxInt == null ? "0" : maxInt ) );
                            isChanged.set( isChanged.intValue() + 1 );
                            return sItemAttrib;
                        }

                    } else if ( sItemAttrib.getType().equalsIgnoreCase( BOOLEAN ) ) {
                        if ( sItemAttrib.getValue() != null && !sItemAttrib.getValue().toString().isEmpty() ) {
                            return sItemAttrib;
                        }

                        if ( !sAttrib.getOptions().isEmpty() ) {
                            String textType = ( String ) sAttrib.getOptions().get( 0 );
                            sItemAttrib.setValue( textType );
                            isChanged.set( isChanged.intValue() + 1 );
                            return sItemAttrib;

                        }
                    } else {
                        return sItemAttrib;
                    }
                    break;
                }
            }
        }

        if ( attribMain == null ) {
            isChanged.set( isChanged.intValue() + 1 );
            attribMain = prepareNewAttributeWithValue( sAttrib, selectionItemEntityList );
        }
        return attribMain;
    }

    /**
     * Gets the max text int from selection item entity list.
     *
     * @param selectionItemEntityList
     *         the selection item entity list
     *
     * @return the max text int from selection item entity list
     */
    private String getMaxTextIntFromSelectionItemEntityList( Set< SelectionItemEntity > selectionItemEntityList,
            WorkFlowAdditionalAttributeDTO sAttrib ) {
        String maxInt = null;
        if ( selectionItemEntityList != null ) {
            for ( SelectionItemEntity selectionItemEntity : selectionItemEntityList ) {
                if ( selectionItemEntity.getAdditionalAttributesJson() != null
                        && !selectionItemEntity.getAdditionalAttributesJson().isEmpty() ) {
                    List< WorkFlowAdditionalAttributeDTO > listAttrib = JsonUtils
                            .jsonToList( selectionItemEntity.getAdditionalAttributesJson(), WorkFlowAdditionalAttributeDTO.class );
                    for ( WorkFlowAdditionalAttributeDTO attribute : listAttrib ) {
                        if ( attribute.getId().equalsIgnoreCase( sAttrib.getId() ) && "Text".equalsIgnoreCase( attribute.getType() ) ) {
                            String textType = ( String ) attribute.getOptions().get( 0 );
                            if ( textType.contains( WorkFlowAdditionalAttributeDTO.TEXT_RANDOM ) ) {

                                String[] arrOfStr = attribute.getValue().toString().split( "_" );
                                if ( arrOfStr != null && arrOfStr.length > 0 ) {
                                    String val = arrOfStr[ arrOfStr.length - 1 ];
                                    if ( val != null && !val.isEmpty() ) {
                                        try {
                                            Integer.valueOf( val );
                                            if ( maxInt == null || Integer.valueOf( val ) > Integer.valueOf( maxInt ) ) {
                                                maxInt = val;
                                            }
                                        } catch ( NumberFormatException e ) {
                                            log.warn( e );
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return maxInt;
    }

    /**
     * Prepare new attribute with value.
     *
     * @param sAttrib
     *         the s attrib
     * @param selectionItemEntityList
     *         the selection item entity list
     *
     * @return the work flow additional attribute DTO
     */
    private WorkFlowAdditionalAttributeDTO prepareNewAttributeWithValue( WorkFlowAdditionalAttributeDTO sAttrib,
            Set< SelectionItemEntity > selectionItemEntityList ) {
        WorkFlowAdditionalAttributeDTO attribMain = null;
        WorkFlowAdditionalAttributeDTO sAttrib2 = WorkFlowAdditionalAttributeDTO.prepareWorkFlowAdditionalAttributeDTOFromDto( sAttrib );
        attribMain = sAttrib2;
        sAttrib2.setId( sAttrib.getId() );
        if ( sAttrib2.getType().equalsIgnoreCase( COLOR ) ) {
            if ( sAttrib2.getValue() != null && !sAttrib2.getValue().toString().isEmpty() ) {
                return sAttrib2;
            } else {
                String colorType = ( String ) sAttrib2.getOptions().get( 0 );

                if ( colorType.contains( STATIC ) ) {
                    sAttrib2.setValue( sAttrib2.getStaticOrRandomOption() );
                } else if ( colorType.contains( RANDOM ) ) {
                    Color randomColor = new Color( new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat() );
                    sAttrib2.setValue(
                            RGBA + randomColor.getRed() + "," + randomColor.getGreen() + "," + randomColor.getBlue() + "," + 1 + ")" );
                }
                attribMain = sAttrib2;
            }
        }
        if ( sAttrib2.getType().equalsIgnoreCase( TEXT ) ) {
            if ( sAttrib2.getValue() != null && !sAttrib2.getValue().toString().isEmpty() ) {
                return sAttrib2;
            } else {
                String textType = ( String ) sAttrib2.getOptions().get( 0 );
                if ( textType.contains( STATIC ) ) {
                    sAttrib2.setValue( sAttrib2.getStaticOrRandomOption() );
                } else if ( textType.contains( WorkFlowAdditionalAttributeDTO.TEXT_RANDOM ) ) {
                    String maxInt = getMaxTextIntFromSelectionItemEntityList( selectionItemEntityList, sAttrib );
                    sAttrib2.setValue( sAttrib2.getStaticOrRandomOption() + "_"
                            + ( maxInt == null ? "0" : String.valueOf( Integer.valueOf( maxInt ) + 1 ) ) );
                }
                attribMain = sAttrib2;
            }
        } else if ( sAttrib2.getType().equalsIgnoreCase( BOOLEAN ) ) {

            if ( sAttrib.getValue() != null && !sAttrib.getValue().toString().isEmpty() ) {
                return sAttrib;

            }
            if ( !sAttrib.getOptions().isEmpty() ) {
                String textType = ( String ) sAttrib.getOptions().get( 0 );
                sAttrib2.setValue( textType );
                attribMain = sAttrib2;
            }
        }
        return attribMain;
    }

    /**
     * Adds the additional attribut to table columns.
     *
     * @param entity
     *         the entity
     * @param tableColumnList
     *         the table column list
     */
    @Override
    public void addAdditionalAttributToTableColumns( SelectionEntity entity, List< TableColumn > tableColumnList ) {
        int orderNum = extractHighesOrderNumber( tableColumnList );
        List< WorkFlowAdditionalAttributeDTO > listAttrib = JsonUtils.jsonToList( entity.getAdditionalAttributesJson(),
                WorkFlowAdditionalAttributeDTO.class );
        for ( WorkFlowAdditionalAttributeDTO workFlowAdditionalAttribute : listAttrib ) {
            tableColumnList.add( prepareTableColForAdditionalAttrib( ( ++orderNum ), workFlowAdditionalAttribute ) );
        }
    }

    /**
     * Extract highes order number.
     *
     * @param tableColumnList
     *         the table column list
     *
     * @return the int
     */
    @Override
    public int extractHighesOrderNumber( List< TableColumn > tableColumnList ) {
        int orderNum = 0;
        for ( TableColumn tableColumn : tableColumnList ) {
            if ( tableColumn.getOrderNum() > orderNum ) {
                orderNum = tableColumn.getOrderNum();
            }
        }
        return orderNum;
    }

    /**
     * Prepare table col for additional attrib.
     *
     * @param orderNum
     *         the order num
     * @param workFlowAdditionalAttribute
     *         the work flow additional attribute
     *
     * @return the table column
     */
    @Override
    public TableColumn prepareTableColForAdditionalAttrib( int orderNum, WorkFlowAdditionalAttributeDTO workFlowAdditionalAttribute ) {
        TableColumn tableColumn = new TableColumn();
        tableColumn.setData( workFlowAdditionalAttribute.getName() );
        tableColumn.setName( workFlowAdditionalAttribute.getName() );
        tableColumn.setOrderNum( orderNum );
        tableColumn.setRenderer( new Renderer( "attribute", 0, workFlowAdditionalAttribute.getName(), true ) );
        tableColumn.setSortable( true );
        tableColumn.setTitle( workFlowAdditionalAttribute.getName() );
        tableColumn.setFilter( "" );
        tableColumn.setRotated( false );
        tableColumn.setVisible( true );
        return tableColumn;
    }

    /**
     * Gets the work flow additional attribute agains selection.
     *
     * @param selectionItemEntityList
     *         the selection item entity list
     * @param suSEntity
     *         the su S entity
     *
     * @return the work flow additional attribute again selection
     */
    public static List< WorkFlowAdditionalAttributeDTO > getWorkFlowAdditionalAttributeAgainsSelection(
            Set< SelectionItemEntity > selectionItemEntityList, SuSEntity suSEntity ) {

        for ( SelectionItemEntity selectionItemEntity : selectionItemEntityList ) {
            if ( suSEntity.getComposedId().getId().equals( UUID.fromString( selectionItemEntity.getItem() ) )
                    && selectionItemEntity.getAdditionalAttributesJson() != null ) {
                return JsonUtils.jsonToList( selectionItemEntity.getAdditionalAttributesJson(), WorkFlowAdditionalAttributeDTO.class );
            }
        }
        return null;
    }

    /**
     * Gets the additional attribute again selection.
     *
     * @param items
     *         the items
     * @param ssfeSelectionDTO
     *         the ssfe selection DTO
     *
     * @return the additional attribute again selection
     */
    private List< WorkFlowAdditionalAttributeDTO > getAdditionalAttributeAgainSelection( Set< SelectionItemEntity > items,
            SsfeSelectionDTO ssfeSelectionDTO ) {
        for ( SelectionItemEntity selectionItemEntity : items ) {
            if ( ssfeSelectionDTO.getFullPath()
                    .equals( JsonUtils.jsonToObject( selectionItemEntity.getItem(), SsfeSelectionDTO.class ).getFullPath() )
                    && selectionItemEntity.getAdditionalAttributesJson() != null ) {
                return JsonUtils.jsonToList( selectionItemEntity.getAdditionalAttributesJson(), WorkFlowAdditionalAttributeDTO.class );
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object saveAttributeUiWithSelectionId( String userIdFromGeneralHeader, String selectionId, String jsonAttrib ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SelectionEntity entity = selectionDAO.getLatestObjectById( entityManager, SelectionEntity.class,
                    UUID.fromString( selectionId ) );
            List< WorkFlowAdditionalAttributeDTO > list = new ArrayList<>();

            if ( entity.getAdditionalAttributesJson() != null ) {
                list = JsonUtils.jsonToList( entity.getAdditionalAttributesJson(), WorkFlowAdditionalAttributeDTO.class );
            }
            jsonAttrib = fixJsonForOptionArray( jsonAttrib );
            WorkFlowAdditionalAttributeDTO attribObjTosave = JsonUtils.jsonToObject( jsonAttrib, WorkFlowAdditionalAttributeDTO.class );

            int idMax = 0;
            for ( WorkFlowAdditionalAttributeDTO workFlowAdditionalAttribute : list ) {
                if ( workFlowAdditionalAttribute.getName().equalsIgnoreCase( attribObjTosave.getName() ) ) {
                    throw new SusException( "Additional attrib Name can not be same " + attribObjTosave.getName() );
                }
                if ( Integer.valueOf( workFlowAdditionalAttribute.getId() ) > idMax ) {
                    idMax = Integer.valueOf( workFlowAdditionalAttribute.getId() );
                }
            }
            attribObjTosave.setId( String.valueOf( ++idMax ) );
            attribObjTosave.setRequired( String.valueOf( attribObjTosave.getRules().getRequired() ) );
            list.add( attribObjTosave );
            entity.setAdditionalAttributesJson( JsonUtils.toJson( list ) );
            Set< SelectionItemEntity > sListItem = entity.getItems();

            // update selection item Attrb json
            // add color values in selection Items if color set to Random
            if ( entity.getAdditionalAttributesJson() != null && !entity.getAdditionalAttributesJson().isEmpty() && sListItem != null
                    && !sListItem.isEmpty() && attribObjTosave.getType().equalsIgnoreCase( TEXT ) ) {
                Object optionvalue = attribObjTosave.getOptions().get( 0 );
                if ( optionvalue.toString().contains( WorkFlowAdditionalAttributeDTO.TEXT_RANDOM ) ) {
                    int maxInt = 0;
                    for ( SelectionItemEntity itemEntity : sListItem ) {
                        List< WorkFlowAdditionalAttributeDTO > itemListAtr = new ArrayList<>();
                        if ( itemEntity.getAdditionalAttributesJson() != null && !itemEntity.getAdditionalAttributesJson().isEmpty() ) {
                            itemListAtr = JsonUtils.jsonToList( itemEntity.getAdditionalAttributesJson(),
                                    WorkFlowAdditionalAttributeDTO.class );
                        }
                        attribObjTosave.setValue( attribObjTosave.getStaticOrRandomOption() + "_" + maxInt++ );
                        itemListAtr.add( attribObjTosave );

                        if ( !itemListAtr.isEmpty() ) {
                            itemEntity.setAdditionalAttributesJson( JsonUtils.toJson( itemListAtr ) );
                        }
                    }
                    entity.setItems( sListItem );
                }
            }
            selectionDAO.saveOrUpdate( entityManager, entity );
        } finally {
            entityManager.close();
        }
        return jsonAttrib;
    }

    /**
     * Fix json for option array.
     *
     * @param jsonAttrib
     *         the json attrib
     *
     * @return the string
     */
    private String fixJsonForOptionArray( String jsonAttrib ) {
        // add Array [] in key: options value
        JSONObject jsonObj = new JSONObject( jsonAttrib );
        try {
            if ( jsonObj.has( OPTIONS ) ) {
                if ( jsonObj.get( "type" ).equals( "select" ) ) {
                    JSONArray ids = jsonObj.getJSONArray( OPTIONS );
                    List< Object > val = new ArrayList<>();
                    for ( int i = 0; i < ids.length(); i++ ) {
                        String id = ids.getString( i ).toString();
                        val.add( id );
                    }
                    jsonObj.put( OPTIONS, val );
                    jsonAttrib = jsonObj.toString();
                } else {
                    String optionValue = ( String ) jsonObj.get( OPTIONS );
                    List< Object > val = new ArrayList<>();
                    val.add( optionValue );
                    jsonObj.put( OPTIONS, val );
                    jsonAttrib = jsonObj.toString();
                }
            }
        } catch ( JSONException e ) {
            log.warn( e.getMessage(), e );
        }
        return jsonAttrib;
    }

    /**
     * Adds the additional attribute ui form.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     *
     * @return the object
     */
    @Override
    public Object addAdditionalAttributeUiForm( String userIdFromGeneralHeader, String selectionId ) {
        String attribSelectionId = null;
        String jsonTemplate =
                "[{\"label\":\"Name\",\"name\":\"name\",\"type\":\"text\",\"value\":\"\",\"rules\":{\"required\":true,\"maxlength\":64,\"alphaNumeric\":true},\"messages\":{\"minlength\":\"Please enter atleast 1 character\",\"maxlength\":\"Please enter no more than 64 characters\",\"required\":\"Name field cannot be empty\"}},{\"label\":\"Label\",\"name\":\"label\",\"type\":\"text\",\"value\":\"\",\"rules\":{\"required\":true,\"maxlength\":64},\"messages\":{\"minlength\":\"Please enter atleast 1 character\",\"maxlength\":\"Please enter no more than 64 characters\",\"required\":\"Label field cannot be empty\"}},{\"label\":\"Value\",\"name\":\"value\",\"type\":\"hidden\",\"value\":\"\"},{\"type\":\"select\",\"name\":\"type\",\"value\":\"text\",\"label\":\"Type\",\"rules\":{\"required\":true},\"options\":[{\"id\":\"integer\",\"name\":\"Integer\"},{\"id\":\"float\",\"name\":\"Float\"},{\"id\":\"boolean\",\"name\":\"Boolean\"},{\"id\":\"color\",\"name\":\"Color\"},{\"id\":\"text\",\"name\":\"Text\"},{\"id\":\"select\",\"name\":\"Select\"},{\"id\":\"textarea\",\"name\":\"Textarea\"},{\"id\":\"date\",\"name\":\"Date\"},{\"id\":\"dateRange\",\"name\":\"Date Range\"}],"
                        + "\"bindFrom\":{\"url\":\"/selection/" + selectionId + "/attribute/" + attribSelectionId
                        + "/ui/createoption/{__value__}\"}},"
                        + "{\"type\":\"select\",\"name\":\"rules.required\",\"value\":\"false\",\"label\":\"Required\",\"help\":\"select yes if this parameter is compulsory\",\"convert\":\"boolean\",\"options\":[{\"id\":\"true\",\"name\":\"Yes\"},{\"id\":\"false\",\"name\":\"No\"}]}]";
        return JsonUtils.jsonToList( jsonTemplate, WorkFlowAdditionalAttributeDTO.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getAttributeUiCreateOption( String userIdFromGeneralHeader, String selectionId, String attribSelectionId,
            String option ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< WorkFlowAdditionalAttributeDTO > attribForm;
        try {
            attribForm = new ArrayList<>();

            WorkFlowAdditionalAttributeDTO workFlowAdditionalAttribute = new WorkFlowAdditionalAttributeDTO();
            boolean isUndefined = true;

            if ( !"null".equalsIgnoreCase( attribSelectionId )
                    && ( !"undefined".equalsIgnoreCase( selectionId ) || !"undefined".equalsIgnoreCase( attribSelectionId ) ) ) {
                isUndefined = false;
                SelectionEntity mainObjSelection = selectionDAO.getLatestObjectById( entityManager, SelectionEntity.class,
                        UUID.fromString( selectionId ) );
                List< String > attributeSelectedIdList = selectionManager.getSelectedIdsStringListBySelectionId( entityManager,
                        attribSelectionId );

                String attrib1 = attributeSelectedIdList.get( 0 );

                List< WorkFlowAdditionalAttributeDTO > listAttrib = JsonUtils.jsonToList( mainObjSelection.getAdditionalAttributesJson(),
                        WorkFlowAdditionalAttributeDTO.class );
                for ( WorkFlowAdditionalAttributeDTO workFlowAdditional : listAttrib ) {
                    if ( workFlowAdditional.getId().equalsIgnoreCase( attrib1 ) ) {
                        workFlowAdditionalAttribute = workFlowAdditional;
                        break;
                    }
                }
            }

            switch ( option ) {
                case INTEGER -> {
                    Rules rules = new Rules( false, 255, false, false, null );
                    WorkFlowAdditionalAttributeDTO attrib = new WorkFlowAdditionalAttributeDTO();
                    attrib.setName( VALUE );
                    attrib.setLabel( DEFAULT_VALUE_LABEL );
                    attrib.setType( INTEGER );
                    attrib.setRules( rules );
                    if ( !isUndefined ) {
                        attrib.setValue( workFlowAdditionalAttribute.getValue() );
                    }
                    attrib.setHelp( "please enter Integer value" );
                    attribForm.add( attrib );
                }

                case "float" -> {
                    Rules rules = new Rules( false, 255, false, false, null );
                    WorkFlowAdditionalAttributeDTO attrib = new WorkFlowAdditionalAttributeDTO();
                    attrib.setName( VALUE );
                    attrib.setLabel( DEFAULT_VALUE_LABEL );
                    attrib.setType( "float" );
                    attrib.setRules( rules );
                    if ( !isUndefined ) {
                        attrib.setValue( workFlowAdditionalAttribute.getValue() );
                    }
                    attrib.setHelp( "please enter floating point value" );
                    attribForm.add( attrib );
                }

                case "textarea" -> {
                    Rules rules = new Rules( false, 255, false, false, null );
                    WorkFlowAdditionalAttributeDTO attrib = new WorkFlowAdditionalAttributeDTO();
                    attrib.setName( VALUE );
                    attrib.setLabel( DEFAULT_VALUE_LABEL );
                    attrib.setType( "textarea" );
                    attrib.setRules( rules );
                    if ( !isUndefined ) {
                        attrib.setValue( workFlowAdditionalAttribute.getValue() );
                    }
                    attrib.setHelp( "please enter text value" );
                    attribForm.add( attrib );
                }

                case "date" -> {
                    Rules rules = new Rules( false, 255, false, false, null );
                    WorkFlowAdditionalAttributeDTO attrib = new WorkFlowAdditionalAttributeDTO();
                    attrib.setName( VALUE );
                    attrib.setLabel( DEFAULT_VALUE_LABEL );
                    attrib.setType( "date" );
                    attrib.setRules( rules );
                    if ( !isUndefined ) {
                        attrib.setValue( workFlowAdditionalAttribute.getValue() );
                    }
                    attrib.setHelp( "please enter date" );
                    attribForm.add( attrib );
                }

                case "dateRange" -> {
                    Rules rules = new Rules( false, 255, false, false, null );
                    WorkFlowAdditionalAttributeDTO attrib = new WorkFlowAdditionalAttributeDTO();
                    attrib.setName( VALUE );
                    attrib.setLabel( DEFAULT_VALUE_LABEL );
                    attrib.setType( "dateRange" );
                    attrib.setRules( rules );
                    if ( !isUndefined ) {
                        attrib.setValue( workFlowAdditionalAttribute.getValue() );
                    }
                    attrib.setHelp( "please enter date range" );
                    attribForm.add( attrib );
                }

                case "select" -> {
                    Rules rules = new Rules( true, 255, false, false, null );
                    List< Object > optionList = new ArrayList<>();
                    optionList.add( new SelectOptionsUI( "true", "Yes" ) );
                    optionList.add( new SelectOptionsUI( "false", "No" ) );

                    WorkFlowAdditionalAttributeDTO select1 = new WorkFlowAdditionalAttributeDTO();
                    select1.setName( "multiple" );
                    select1.setLabel( "MultiSelect" );
                    select1.setOptions( optionList );
                    select1.setType( "select" );
                    select1.setRules( rules );
                    if ( !isUndefined ) {
                        select1.setValue( workFlowAdditionalAttribute.isMultiple() );
                    }
                    select1.setHelp( "select yes if dropdown can have multiple selected values" );
                    attribForm.add( select1 );

                    WorkFlowAdditionalAttributeDTO select2 = new WorkFlowAdditionalAttributeDTO();
                    select2.setName( OPTIONS );
                    select2.setLabel( "Options" );
                    select2.setConvert( "splitByComma" );
                    select2.setType( TEXT );
                    if ( !isUndefined ) {
                        select2.setValue( workFlowAdditionalAttribute.getOptions().toString().replace( "[", "" ).replace( ", ", "," )
                                .replace( "]", "" ).trim() );
                    }
                    select2.setHelp( "Enter comma separated values without spaces i.e. option1,options2" );
                    attribForm.add( select2 );

                    WorkFlowAdditionalAttributeDTO select3 = new WorkFlowAdditionalAttributeDTO();
                    select3.setName( VALUE );
                    select3.setLabel( VALUE );
                    select3.setType( TEXT );
                    if ( !isUndefined ) {
                        select3.setValue( workFlowAdditionalAttribute.getValue() );
                    }
                    select3.setHelp( "Enter default value for options" );
                    attribForm.add( select3 );

                    return attribForm;

                }
                case COLOR -> {

                    Rules rules = new Rules( true, 255, false, false, null );
                    List< Object > optionList = new ArrayList<>();
                    optionList.add( new SelectOptionsUI( "empty", "Empty" ) );
                    optionList.add( new SelectOptionsUI( WorkFlowAdditionalAttributeDTO.COLOR_STATIC, "Static" ) );
                    optionList.add( new SelectOptionsUI( RANDOM, "Random" ) );

                    WorkFlowAdditionalAttributeDTO attrib = new WorkFlowAdditionalAttributeDTO();
                    attrib.setName( OPTIONS );
                    attrib.setLabel( "Options" );
                    attrib.setOptions( optionList );
                    attrib.setType( "select" );
                    attrib.setRules( rules );
                    if ( !isUndefined ) {
                        attrib.setValue( workFlowAdditionalAttribute.getOptions().get( 0 ).toString() );
                    }
                    attrib.setBindFrom(
                            new BindFrom(
                                    "/selection/" + selectionId + "/attribute/" + attribSelectionId + "/ui/createoption/{__value__}" ) );
                    attrib.setHelp( "select option from dropdown" );
                    attribForm.add( attrib );

                }
                case WorkFlowAdditionalAttributeDTO.COLOR_STATIC -> {
                    Rules rules = new Rules( true, 255, false, false, null );
                    WorkFlowAdditionalAttributeDTO attrib = new WorkFlowAdditionalAttributeDTO();
                    attrib.setName( STATIC_OPTION );
                    attrib.setLabel( DEFAULT_VALUE_LABEL );
                    attrib.setType( COLOR );
                    attrib.setRules( rules );
                    if ( !isUndefined ) {
                        attrib.setValue( workFlowAdditionalAttribute.getStaticOrRandomOption() );
                    }
                    attrib.setHelp( "select option from dropdown" );
                    attribForm.add( attrib );
                }
                case TEXT -> {

                    Rules rules = new Rules( true, 255, false, false, null );
                    List< Object > optionList = new ArrayList<>();
                    optionList.add( new SelectOptionsUI( "empty", "Empty" ) );
                    optionList.add( new SelectOptionsUI( WorkFlowAdditionalAttributeDTO.TEXT_STATIC, "Static" ) );
                    optionList.add( new SelectOptionsUI( WorkFlowAdditionalAttributeDTO.TEXT_RANDOM, "Random" ) );

                    WorkFlowAdditionalAttributeDTO select1 = new WorkFlowAdditionalAttributeDTO();
                    select1.setName( OPTIONS );
                    select1.setLabel( "Options" );
                    select1.setOptions( optionList );
                    select1.setType( "select" );
                    select1.setRules( rules );
                    if ( !isUndefined ) {
                        select1.setValue( workFlowAdditionalAttribute.getOptions().get( 0 ).toString() );
                    }
                    select1.setBindFrom(
                            new BindFrom(
                                    "/selection/" + selectionId + "/attribute/" + attribSelectionId + "/ui/createoption/{__value__}" ) );
                    select1.setHelp( "select option from dropdown" );
                    attribForm.add( select1 );

                }
                case WorkFlowAdditionalAttributeDTO.TEXT_STATIC -> {
                    Rules rules = new Rules( true, 255, false, false, null );
                    WorkFlowAdditionalAttributeDTO attrib = new WorkFlowAdditionalAttributeDTO();
                    attrib.setName( STATIC_OPTION );
                    attrib.setLabel( DEFAULT_VALUE_LABEL );
                    attrib.setType( TEXT );
                    attrib.setRules( rules );
                    if ( !isUndefined ) {
                        attrib.setValue( workFlowAdditionalAttribute.getStaticOrRandomOption() );
                    }
                    attrib.setHelp( "please write static text" );
                    attribForm.add( attrib );

                }
                case WorkFlowAdditionalAttributeDTO.TEXT_RANDOM -> {
                    Rules rules = new Rules( true, 255, false, false, null );
                    WorkFlowAdditionalAttributeDTO attrib = new WorkFlowAdditionalAttributeDTO();
                    attrib.setName( STATIC_OPTION );
                    attrib.setLabel( DEFAULT_VALUE_LABEL );
                    attrib.setType( TEXT );
                    attrib.setRules( rules );
                    if ( !isUndefined ) {
                        attrib.setValue( workFlowAdditionalAttribute.getStaticOrRandomOption() );
                    }
                    attrib.setHelp( "please write random prefix for text" );
                    attribForm.add( attrib );
                }
                case BOOLEAN -> {

                    Rules rules = new Rules( true, 255, false, false, null );
                    List< Object > optionList = new ArrayList<>();

                    optionList.add( new SelectOptionsUI( "true", "True" ) );
                    optionList.add( new SelectOptionsUI( "false", "False" ) );

                    WorkFlowAdditionalAttributeDTO select1 = new WorkFlowAdditionalAttributeDTO();
                    select1.setName( OPTIONS );
                    select1.setLabel( "Options" );
                    select1.setOptions( optionList );
                    select1.setType( "select" );
                    select1.setRules( rules );
                    if ( !isUndefined ) {
                        select1.setValue( workFlowAdditionalAttribute.getOptions().get( 0 ).toString() );
                    }
                    select1.setBindFrom(
                            new BindFrom(
                                    "/selection/" + selectionId + "/attribute/" + attribSelectionId + "/ui/createoption/{__value__}" ) );
                    select1.setHelp( "select option from dropdown to make it default" );
                    attribForm.add( select1 );

                }
            }
        } finally {
            entityManager.close();
        }

        if ( attribForm != null && !attribForm.isEmpty() ) {
            return attribForm;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getAttributeContext( String userId, String selectionId, FiltersDTO filter ) {
        // case of select all from data table
        List< ContextMenuItem > cml;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter.getItems().isEmpty() && "-1".equalsIgnoreCase( filter.getLength().toString() ) ) {
                List< Object > items = new ArrayList<>();
                SelectionEntity selection = selectionDAO.getLatestObjectById( entityManager, SelectionEntity.class,
                        UUID.fromString( selectionId ) );

                List< WorkFlowAdditionalAttributeDTO > listAttrib = JsonUtils.jsonToList( selection.getAdditionalAttributesJson(),
                        WorkFlowAdditionalAttributeDTO.class );
                for ( WorkFlowAdditionalAttributeDTO object : listAttrib ) {
                    items.add( object.getId() );
                }
                filter.setItems( items );
            }
            List< Object > selectedIds = filter.getItems();

            SelectionResponseUI attibSelection = selectionManager.createSelection( entityManager, userId, SelectionOrigins.CONTEXT,
                    filter );

            cml = new ArrayList<>();
            if ( CollectionUtil.isNotEmpty( selectedIds ) ) {
                if ( selectedIds.size() == ConstantsInteger.INTEGER_VALUE_ONE ) {
                    cml.add( prepareEditAtrributeContext( selectionId, attibSelection.getId() ) );
                }
                cml.add( prepareDeleteAtrributeContext( selectionId, attibSelection.getId() ) );
            }
        } finally {
            entityManager.close();
        }
        return cml;
    }

    /**
     * Prepare delete atrribute context.
     *
     * @param selectionId
     *         the selection id
     * @param attibSelection
     *         the attib selection
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareDeleteAtrributeContext( String selectionId, String attibSelection ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( "selection/{selectionId}/attribute/{attribSelectionId}/delete".replace( "{selectionId}", selectionId )
                .replace( "{attribSelectionId}", attibSelection ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( "4100007x4" ) );
        containerCMI.setDivider( false );
        containerCMI.setIcon( "fa fa-trash-o" );
        return containerCMI;
    }

    /**
     * Prepare edit atrribute context.
     *
     * @param selectionId
     *         the selection id
     * @param attibSelection
     *         the attib selection
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareEditAtrributeContext( String selectionId, String attibSelection ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( "selection/{selectionId}/attribute/{attribSelectionId}/edit".replace( "{selectionId}", selectionId )
                .replace( "{attribSelectionId}", attibSelection ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( "4100014x4" ) );
        containerCMI.setIcon( "fa fa-edit" );
        containerCMI.setDivider( false );
        return containerCMI;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getEditAttributeForm( String userIdFromGeneralHeader, String selectionId, String attribSelectionId ) {

        WorkFlowAdditionalAttributeDTO workFlowAdditionalAttribute;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            workFlowAdditionalAttribute = new WorkFlowAdditionalAttributeDTO();
            SelectionEntity mainObjSelection = selectionDAO.getLatestObjectById( entityManager, SelectionEntity.class,
                    UUID.fromString( selectionId ) );
            List< String > attributeSelectedIdList = selectionManager.getSelectedIdsStringListBySelectionId( entityManager,
                    attribSelectionId );

            String attrib = attributeSelectedIdList.get( 0 );

            List< WorkFlowAdditionalAttributeDTO > listAttrib = JsonUtils.jsonToList( mainObjSelection.getAdditionalAttributesJson(),
                    WorkFlowAdditionalAttributeDTO.class );
            for ( WorkFlowAdditionalAttributeDTO workFlowAdditional : listAttrib ) {
                if ( workFlowAdditional.getId().equalsIgnoreCase( attrib ) ) {
                    workFlowAdditionalAttribute = workFlowAdditional;
                    break;
                }
            }
        } finally {
            entityManager.close();
        }

        String jsonTemplate =
                "[{\"label\":\"Name\",\"name\":\"name\",\"type\":\"text\",\"value\":\"\",\"rules\":{\"required\":true,\"maxlength\":64,\"alphaNumeric\":true},\"messages\":{\"minlength\":\"Please enter atleast 1 character\",\"maxlength\":\"Please enter no more than 64 characters\",\"required\":\"Name field cannot be empty\"}},{\"label\":\"Label\",\"name\":\"label\",\"type\":\"text\",\"value\":\"\",\"rules\":{\"required\":true,\"maxlength\":64},\"messages\":{\"minlength\":\"Please enter atleast 1 character\",\"maxlength\":\"Please enter no more than 64 characters\",\"required\":\"Label field cannot be empty\"}},{\"label\":\"Value\",\"name\":\"value\",\"type\":\"hidden\",\"value\":\"\"},{\"type\":\"select\",\"name\":\"type\",\"value\":\"text\",\"label\":\"Type\",\"rules\":{\"required\":true},\"options\":[{\"id\":\"integer\",\"name\":\"Integer\"},{\"id\":\"float\",\"name\":\"Float\"},{\"id\":\"boolean\",\"name\":\"Boolean\"},{\"id\":\"color\",\"name\":\"Color\"},{\"id\":\"text\",\"name\":\"Text\"},{\"id\":\"select\",\"name\":\"Select\"},{\"id\":\""
                        + "\",\"name\":\"Textarea\"},{\"id\":\"date\",\"name\":\"Date\"},{\"id\":\"dateRange\",\"name\":\"Date Range\"}],"
                        + "\"bindFrom\":{\"url\":\"/selection/" + selectionId + "/attribute/" + attribSelectionId
                        + "/ui/createoption/{__value__}\"}},"
                        + "{\"type\":\"select\",\"name\":\"rules.required\",\"value\":\"false\",\"label\":\"Required\",\"help\":\"select yes if this parameter is compulsory\",\"convert\":\"boolean\",\"options\":[{\"id\":\"true\",\"name\":\"Yes\"},{\"id\":\"false\",\"name\":\"No\"}]}]";

        List< WorkFlowAdditionalAttributeDTO > atribListTemplate = JsonUtils.jsonToList( jsonTemplate,
                WorkFlowAdditionalAttributeDTO.class );

        setAttributeTemplateValue( workFlowAdditionalAttribute, atribListTemplate );

        WorkFlowAdditionalAttributeDTO hidden = prepareHiddenAttribute( workFlowAdditionalAttribute );
        atribListTemplate.add( hidden );

        return atribListTemplate;
    }

    /**
     * Prepare hidden attribute work flow additional attribute dto.
     *
     * @param workFlowAdditionalAttribute
     *         the work flow additional attribute
     *
     * @return the work flow additional attribute dto
     */
    private static WorkFlowAdditionalAttributeDTO prepareHiddenAttribute( WorkFlowAdditionalAttributeDTO workFlowAdditionalAttribute ) {
        WorkFlowAdditionalAttributeDTO hidden = new WorkFlowAdditionalAttributeDTO();
        hidden.setId( workFlowAdditionalAttribute.getId() );
        hidden.setName( "id" );
        hidden.setType( "hidden" );
        hidden.setRequired( "false" );
        Rules dd = new Rules();
        dd.setRequired( false );
        hidden.setRules( dd );
        hidden.setValue( workFlowAdditionalAttribute.getId() );
        return hidden;
    }

    /**
     * Sets attribute template value.
     *
     * @param workFlowAdditionalAttribute
     *         the work flow additional attribute
     * @param atribListTemplate
     *         the atrib list template
     */
    private void setAttributeTemplateValue( WorkFlowAdditionalAttributeDTO workFlowAdditionalAttribute,
            List< WorkFlowAdditionalAttributeDTO > atribListTemplate ) {
        for ( WorkFlowAdditionalAttributeDTO atribtemplate : atribListTemplate ) {
            atribtemplate.setId( workFlowAdditionalAttribute.getId() );
            if ( "Name".equalsIgnoreCase( atribtemplate.getName() ) ) {
                atribtemplate.setValue( workFlowAdditionalAttribute.getName() );

            } else if ( "Label".equalsIgnoreCase( atribtemplate.getName() ) ) {
                atribtemplate.setValue( workFlowAdditionalAttribute.getLabel() );
            } else if ( "id".equalsIgnoreCase( atribtemplate.getName() ) ) {
                atribtemplate.setValue( workFlowAdditionalAttribute.getId() );
            } else if ( "type".equalsIgnoreCase( atribtemplate.getName() ) ) {
                atribtemplate.setValue( workFlowAdditionalAttribute.getType() );
            } else if ( "rules.required".equalsIgnoreCase( atribtemplate.getName() ) ) {
                atribtemplate.setValue( workFlowAdditionalAttribute.getRequired() );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object deleteAttributeBySelection( String userIdFromGeneralHeader, String selectionId, String attribSelectionId ) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< WorkFlowAdditionalAttributeDTO > listAttrib;
        try {
            SelectionEntity mainObjSelection = selectionDAO.getLatestObjectById( entityManager, SelectionEntity.class,
                    UUID.fromString( selectionId ) );
            List< String > attributeSelectedIdList = selectionManager.getSelectedIdsStringListBySelectionId( entityManager,
                    attribSelectionId );
            listAttrib = JsonUtils.jsonToList( mainObjSelection.getAdditionalAttributesJson(), WorkFlowAdditionalAttributeDTO.class );
            Iterator< WorkFlowAdditionalAttributeDTO > listAttribIterator = listAttrib.iterator();
            while ( listAttribIterator.hasNext() ) {
                WorkFlowAdditionalAttributeDTO workFlowAdditionalAttribute = listAttribIterator.next();
                for ( String idsDell : attributeSelectedIdList ) {
                    if ( workFlowAdditionalAttribute.getId().equalsIgnoreCase( idsDell ) ) {
                        listAttribIterator.remove();
                    }
                }
            }
            mainObjSelection.setAdditionalAttributesJson( !listAttrib.isEmpty() ? JsonUtils.toJson( listAttrib ) : null );
            // delete attrib from selection item
            deleteAttribFromSelectionItem( mainObjSelection, attributeSelectedIdList );
            selectionDAO.saveOrUpdate( entityManager, mainObjSelection );
        } finally {
            entityManager.close();
        }
        return listAttrib;

    }

    /**
     * Delete attrib from selection item.
     *
     * @param mainObjSelection
     *         the main obj selection
     * @param attributeSelectedIdList
     *         the attribute selected id list
     */
    private static void deleteAttribFromSelectionItem( SelectionEntity mainObjSelection, List< String > attributeSelectedIdList ) {
        for ( SelectionItemEntity selectionItem : mainObjSelection.getItems() ) {
            if ( selectionItem.getAdditionalAttributesJson() != null ) {
                List< WorkFlowAdditionalAttributeDTO > listAttribItems = JsonUtils.jsonToList( selectionItem.getAdditionalAttributesJson(),
                        WorkFlowAdditionalAttributeDTO.class );
                Iterator< WorkFlowAdditionalAttributeDTO > listAttribIterator2 = listAttribItems.iterator();
                while ( listAttribIterator2.hasNext() ) {
                    WorkFlowAdditionalAttributeDTO atribObjItem = listAttribIterator2.next();
                    for ( String idsDell : attributeSelectedIdList ) {
                        if ( idsDell.equalsIgnoreCase( atribObjItem.getId() ) ) {
                            listAttribIterator2.remove();
                        }
                    }
                }
                selectionItem.setAdditionalAttributesJson( !listAttribItems.isEmpty() ? JsonUtils.toJson( listAttribItems ) : null );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object updateAttributeWIthSelection( String userIdFromGeneralHeader, String selectionId, String attribSelectionId,
            String jsonAtrib ) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        WorkFlowAdditionalAttributeDTO attribObj;
        try {
            boolean isTypeChanged = false;
            jsonAtrib = fixJsonForOptionArray( jsonAtrib );
            attribObj = JsonUtils.jsonToObject( jsonAtrib, WorkFlowAdditionalAttributeDTO.class );
            SelectionEntity mainObjSelection = selectionDAO.getLatestObjectById( entityManager, SelectionEntity.class,
                    UUID.fromString( selectionId ) );

            List< WorkFlowAdditionalAttributeDTO > listAttrib = JsonUtils.jsonToList( mainObjSelection.getAdditionalAttributesJson(),
                    WorkFlowAdditionalAttributeDTO.class );
            // please add lable dublication condition
            for ( WorkFlowAdditionalAttributeDTO workFlowAdditionalAttribute : listAttrib ) {

                if ( workFlowAdditionalAttribute.getName().equalsIgnoreCase( attribObj.getName() )
                        && !attribObj.getId().equalsIgnoreCase( workFlowAdditionalAttribute.getId() ) ) {
                    throw new SusException( "Additional attrib Name can not be same " + attribObj.getName() );
                }

                if ( attribObj.getId().equalsIgnoreCase( workFlowAdditionalAttribute.getId() ) ) {
                    isTypeChanged = setAtrribObjectInWorkflowAdditionAttribute( isTypeChanged, attribObj, workFlowAdditionalAttribute );

                }
            }
            mainObjSelection.setAdditionalAttributesJson( JsonUtils.toJson( listAttrib ) );

            if ( isTypeChanged ) {
                // update items set attribute values
                updateItemsAttributeValues( attribObj, mainObjSelection );
            }

            selectionDAO.saveOrUpdate( entityManager, mainObjSelection );
        } finally {
            entityManager.close();
        }
        return attribObj;
    }

    /**
     * Update items attribute values.
     *
     * @param attribObj
     *         the attrib obj
     * @param mainObjSelection
     *         the main obj selection
     */
    private void updateItemsAttributeValues( WorkFlowAdditionalAttributeDTO attribObj, SelectionEntity mainObjSelection ) {
        Set< SelectionItemEntity > itemEnList = mainObjSelection.getItems();
        for ( SelectionItemEntity selectionItem : itemEnList ) {

            List< WorkFlowAdditionalAttributeDTO > listAttribItems = JsonUtils.jsonToList( selectionItem.getAdditionalAttributesJson(),
                    WorkFlowAdditionalAttributeDTO.class );
            listAttribItems.removeIf( atribObjItem -> attribObj.getId().equalsIgnoreCase( atribObjItem.getId() ) );

            WorkFlowAdditionalAttributeDTO attribMain = prepareNewAttributeWithValue( attribObj, mainObjSelection.getItems() );
            listAttribItems.add( attribMain );
            selectionItem.setAdditionalAttributesJson( JsonUtils.toJson( listAttribItems ) );
        }
        mainObjSelection.setItems( itemEnList );
    }

    /**
     * Sets atrrib object in workflow addition attribute.
     *
     * @param isTypeChanged
     *         the is type changed
     * @param attribObj
     *         the attrib obj
     * @param workFlowAdditionalAttribute
     *         the work flow additional attribute
     *
     * @return the atrrib object in workflow addition attribute
     */
    private static boolean setAtrribObjectInWorkflowAdditionAttribute( boolean isTypeChanged, WorkFlowAdditionalAttributeDTO attribObj,
            WorkFlowAdditionalAttributeDTO workFlowAdditionalAttribute ) {
        if ( !workFlowAdditionalAttribute.getType().equalsIgnoreCase( attribObj.getType() )
                || ( null != workFlowAdditionalAttribute.getStaticOrRandomOption() && !workFlowAdditionalAttribute.getStaticOrRandomOption()
                .equalsIgnoreCase( attribObj.getStaticOrRandomOption() ) ) ) {
            isTypeChanged = true;
        }
        workFlowAdditionalAttribute.setType( attribObj.getType() );
        workFlowAdditionalAttribute.setOptions( attribObj.getOptions() );

        workFlowAdditionalAttribute.setName( attribObj.getName() );
        workFlowAdditionalAttribute.setLabel( attribObj.getLabel() );
        workFlowAdditionalAttribute.setStaticOrRandomOption( attribObj.getStaticOrRandomOption() );
        workFlowAdditionalAttribute.setMultiple( attribObj.isMultiple() );
        workFlowAdditionalAttribute.setRules( attribObj.getRules() );
        workFlowAdditionalAttribute.setBindFrom( attribObj.getBindFrom() );
        workFlowAdditionalAttribute.setHelp( attribObj.getHelp() );
        workFlowAdditionalAttribute.setMessages( attribObj.getMessages() );
        workFlowAdditionalAttribute.setId( attribObj.getId() );
        workFlowAdditionalAttribute.setConvert( attribObj.getConvert() );
        workFlowAdditionalAttribute.setRequired( String.valueOf( attribObj.getRules().getRequired() ) );

        if ( attribObj.getType().equalsIgnoreCase( COLOR ) && attribObj.getOptions().contains( "empty" ) ) {
            workFlowAdditionalAttribute.setValue( null );
            workFlowAdditionalAttribute.setStaticOrRandomOption( null );
            isTypeChanged = true;
        } else {
            workFlowAdditionalAttribute.setValue( attribObj.getValue() );
        }
        return isTypeChanged;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object saveAttributeValuesWithSelection( String userIdFromGeneralHeader, String selectionId, String attribSelectionId,
            String jsonTemplate ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SelectionEntity mainObjSelection = selectionDAO.getLatestObjectById( entityManager, SelectionEntity.class,
                    UUID.fromString( selectionId ) );
            Set< SelectionItemEntity > attributeSelectedIdList = mainObjSelection.getItems();

            Map< String, String > map = new HashMap<>();
            map = ( Map< String, String > ) JsonUtils.jsonToMap( jsonTemplate, map );

            List< WorkFlowAdditionalAttributeDTO > listAttrib = null;
            if ( mainObjSelection.getAdditionalAttributesJson() != null ) {
                listAttrib = JsonUtils.jsonToList( mainObjSelection.getAdditionalAttributesJson(), WorkFlowAdditionalAttributeDTO.class );
            }

            for ( SelectionItemEntity selectionItemEntity : attributeSelectedIdList ) {
                if ( selectionItemEntity.getItem().equalsIgnoreCase( attribSelectionId ) ) {
                    selectionItemEntity.setAdditionalAttributesJson( JsonUtils.toJson( getMapValueFromListAttrib( listAttrib, map ) ) );
                    break;
                }
            }

            mainObjSelection.setItems( attributeSelectedIdList );
            selectionDAO.saveOrUpdate( entityManager, mainObjSelection );
        } finally {
            entityManager.close();
        }
        return jsonTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object saveSsfeAttributeValuesWithSelection( String userIdFromGeneralHeader, String selectionId, String jsonTemplate ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SelectionEntity mainObjSelection = selectionDAO.getLatestObjectById( entityManager, SelectionEntity.class,
                    UUID.fromString( selectionId ) );
            Set< SelectionItemEntity > attributeSelectedIdList = null != mainObjSelection ? mainObjSelection.getItems() : new HashSet<>();
            Map< String, String > map = new HashMap<>();
            map = ( Map< String, String > ) JsonUtils.jsonToMap( jsonTemplate, map );
            List< WorkFlowAdditionalAttributeDTO > listAttrib = null;
            if ( mainObjSelection.getAdditionalAttributesJson() != null ) {
                listAttrib = JsonUtils.jsonToList( mainObjSelection.getAdditionalAttributesJson(), WorkFlowAdditionalAttributeDTO.class );
            }
            for ( SelectionItemEntity selectionItemEntity : attributeSelectedIdList ) {
                if ( JsonUtils.jsonToObject( selectionItemEntity.getItem(), SsfeSelectionDTO.class ).getFullPath()
                        .equalsIgnoreCase( map.get( "fullPath" ) ) ) {
                    selectionItemEntity.setAdditionalAttributesJson( JsonUtils.toJson( getMapValueFromListAttrib( listAttrib, map ) ) );
                    break;
                }
            }
            mainObjSelection.setItems( attributeSelectedIdList );
            selectionDAO.saveOrUpdate( entityManager, mainObjSelection );
        } finally {
            entityManager.close();
        }
        return jsonTemplate;
    }

    /**
     * Gets the map value from list attrib.
     *
     * @param listAttrib
     *         the list attrib
     * @param map
     *         the map
     *
     * @return the map value from list attrib
     */
    public static List< WorkFlowAdditionalAttributeDTO > getMapValueFromListAttrib( List< WorkFlowAdditionalAttributeDTO > listAttrib,
            Map< String, String > map ) {
        for ( WorkFlowAdditionalAttributeDTO workFlowAdditionalAttribute : listAttrib ) {
            Object value = map.get( workFlowAdditionalAttribute.getName() );
            workFlowAdditionalAttribute.setStaticOrRandomOption( null );
            workFlowAdditionalAttribute.setValue( value );
        }
        return listAttrib;

    }

    /**
     * Gets the user common manager.
     *
     * @return the user common manager
     */
    public UserCommonManager getUserCommonManager() {
        return userCommonManager;
    }

    /**
     * Sets the user common manager.
     *
     * @param userCommonManager
     *         the new user common manager
     */
    public void setUserCommonManager( UserCommonManager userCommonManager ) {
        this.userCommonManager = userCommonManager;
    }

    /**
     * Gets the selection DAO.
     *
     * @return the selection DAO
     */
    public SelectionDAO getSelectionDAO() {
        return selectionDAO;
    }

    /**
     * Sets the selection DAO.
     *
     * @param selectionDAO
     *         the new selection DAO
     */
    public void setSelectionDAO( SelectionDAO selectionDAO ) {
        this.selectionDAO = selectionDAO;
    }

    /**
     * Gets the selection item DAO.
     *
     * @return the selection item DAO
     */
    public SelectionItemDAO getSelectionItemDAO() {
        return selectionItemDAO;
    }

    /**
     * Sets the selection item DAO.
     *
     * @param selectionItemDAO
     *         the new selection item DAO
     */
    public void setSelectionItemDAO( SelectionItemDAO selectionItemDAO ) {
        this.selectionItemDAO = selectionItemDAO;
    }

    /**
     * Gets the sus DAO.
     *
     * @return the sus DAO
     */
    public SuSGenericObjectDAO< SuSEntity > getSusDAO() {
        return susDAO;
    }

    /**
     * Sets the sus DAO.
     *
     * @param susDAO
     *         the new sus DAO
     */
    public void setSusDAO( SuSGenericObjectDAO< SuSEntity > susDAO ) {
        this.susDAO = susDAO;
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
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    public ObjectViewManager getObjectViewManager() {
        return objectViewManager;
    }

    /**
     * Sets the object view manager.
     *
     * @param objectViewManager
     *         the new object view manager
     */
    public void setObjectViewManager( ObjectViewManager objectViewManager ) {
        this.objectViewManager = objectViewManager;
    }

    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}
