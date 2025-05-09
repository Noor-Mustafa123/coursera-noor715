package de.soco.software.simuspace.wizards.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONObject;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.cb2.model.BmwCaeBenchTreeDTO;
import de.soco.software.simuspace.suscore.common.cb2.model.Cb2TreeChildrenDTO;
import de.soco.software.simuspace.suscore.common.client.CB2Client;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.BmwCaeBenchEnums;
import de.soco.software.simuspace.suscore.common.enums.BmwCaeBenchInputDeckEnum;
import de.soco.software.simuspace.suscore.common.enums.BmwCaeBenchKeyResultEnum;
import de.soco.software.simuspace.suscore.common.enums.BmwCaeBenchResultEnum;
import de.soco.software.simuspace.suscore.common.enums.BmwCaeBenchSubModelEnums;
import de.soco.software.simuspace.suscore.common.enums.BmwCaeBenchVariantEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchField;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchInputDeckDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchKeyResultsDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchProjectsDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchReportDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchResultDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchSenarioDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchSubModelDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchSubModelTableDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchVariantDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchEntity;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.wizards.manager.Cb2DummyManager;

/**
 * The Class Cb2DummyManagerImpl.
 *
 * @author noman arshad , umer tariq
 */
@Log4j2
public class Cb2DummyManagerImpl extends SuSBaseService implements Cb2DummyManager {

    /**
     * The Constant END_FILTER_EXP.
     */
    private static final String END_FILTER_EXP = "*'";

    /**
     * The Constant START_FILTER_EXP.
     */
    private static final String START_FILTER_EXP = "'*";

    /**
     * The Constant DOES_NOT_CONTAIN.
     */
    private static final String DOES_NOT_CONTAIN = "Does Not Contain";

    /**
     * The Constant CONTAINS.
     */
    private static final String CONTAINS = "Contains";

    /**
     * The equals.
     */
    private static final String EQUALS = "Equals";

    /**
     * The not equals.
     */
    private static final String NOT_EQUALS = "Does Not Equal";

    /**
     * The begins with.
     */
    private static final String BEGINS_WITH = "Begins With";

    /**
     * The ends with.
     */
    private static final String ENDS_WITH = "Ends With";

    /**
     * The cb 2 tree children DTO global.
     */
    private Cb2TreeChildrenDTO cb2TreeChildrenDTOGlobal;

    /**
     * The constant SUBMODELS_JSON_FILE_PATH.
     */
    private static final String SUBMODELS_JSON_FILE_PATH = PropertiesManager.getUserDefaultServerTempPath( "%s" )
            + "/submodelsTree.json";

    private static final Object RELEASE_LEVEL_LABEL = "releaseLevelLabel";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getListOfCb2Objects( String reqJson, String userId, String bmwCaeBenchJsonType, FiltersDTO filter, String node,
            String userName ) {
        try {

            // temp data for testing without cb2 user

            /*
             * filter.setTotalRecords( ( long ) 10 ); filter.setFilteredRecords( ( long ) 10
             * ); String dummyData =
             * "[{\"owner\":\"q127038\",\"variantLabel\":\"Cabrio. Komplett Fahrzeug, nicht zerlegt\",\"createdAt\":\"2008-07-30 10:48\",\"assembleType\":\"\",\"carProject\":\"/Data/Car/Z8/E52/EK/Preprocessing\",\"modelDefLabel\":\"M99_X_Sonstiges\",\"name\":\"Cabrio. Komplett Fahrzeug, nicht zerlegt\",\"description\":\"Komplettfahrzeug, nicht zerlegt. Ziel Archivierung. Quelle caefs1 Archiv. Kommentar: E52 Baugruppe BG2; Lastfall ist US-NCAP; Status zum Projektende; Berechnet von Condat \\tE52 \\tBG2 \\tFrontcrash \\tq117755 (Juergen Lescheticky, EK-214, +49-89-382-21253)\\n\",\"oid\":\"41381511:B3Q\"},{\"owner\":\"q161820\",\"variantLabel\":\"e52c_sitz_rechts_001_01.tail\",\"createdAt\":\"2003-05-07 11:53\",\"assembleType\":\"\",\"carProject\":\"/Data/Car/Z8/E52/EK/Preprocessing\",\"modelDefLabel\":\"M08_Sitze\",\"name\":\"e52c_sitz_rechts_001_01.tail\",\"description\":\"-\\n\",\"oid\":\"18239:B3Q\"},{\"owner\":\"q161820\",\"variantLabel\":\"e52c_coupe_rumpf_020.dat\",\"createdAt\":\"2003-06-25 15:06\",\"assembleType\":\"\",\"carProject\":\"/Data/Car/Z8/E52/EK/Preprocessing\",\"modelDefLabel\":\"M01_Karosseriegerippe\",\"name\":\"e52c_coupe_rumpf_020.dat\",\"description\":\"- Anbindungsnullschalen für TVKL entfernt\\n\",\"oid\":\"16370:B3Q\"},{\"owner\":\"q161820\",\"variantLabel\":\"frontal_odb_pos_a01.dat\",\"createdAt\":\"2003-05-07 12:42\",\"assembleType\":\"\",\"carProject\":\"/Data/Car/Z8/E52/EK/Preprocessing\",\"modelDefLabel\":\"M98_X_Barriere\",\"name\":\"frontal_odb_pos_a01.dat\",\"description\":\"- weiche Barriere aus E87 Proj.\\n- auf E52-Position verschoben (-108/-18/-7)\\n\",\"oid\":\"16221:B3Q\"},{\"owner\":\"q161820\",\"variantLabel\":\"e52c_tuer_links_001_01.tail\",\"createdAt\":\"2003-05-07 11:54\",\"assembleType\":\"\",\"carProject\":\"/Data/Car/Z8/E52/EK/Preprocessing\",\"modelDefLabel\":\"M04_Tueren\",\"name\":\"e52c_tuer_links_001_01.tail\",\"description\":\"-\\n\",\"oid\":\"18270:B3Q\"},{\"owner\":\"q161820\",\"variantLabel\":\"e52c_coupe_rumpf_013.dat\",\"createdAt\":\"2003-06-12 15:38\",\"assembleType\":\"\",\"carProject\":\"/Data/Car/Z8/E52/EK/Preprocessing\",\"modelDefLabel\":\"M01_Karosseriegerippe\",\"name\":\"e52c_coupe_rumpf_013.dat\",\"description\":\"- Fehler korr\\n\",\"oid\":\"19579:B3Q\"},{\"owner\":\"q161820\",\"variantLabel\":\"e52c_hd80_fmvss301_a02.dat\",\"createdAt\":\"2003-07-07 09:32\",\"assembleType\":\"\",\"carProject\":\"/Data/Car/Z8/E52/EK/Preprocessing\",\"modelDefLabel\":\"M98_X_Barriere\",\"name\":\"e52c_hd80_fmvss301_a02.dat\",\"description\":\"- DIS3D für Fahrbahnaufwärtsbewegung entfernt.\\n\",\"oid\":\"21175:B3Q\"},{\"owner\":\"q161820\",\"variantLabel\":\"e52c_driveline_001_02.tail\",\"createdAt\":\"2003-06-05 16:18\",\"assembleType\":\"\",\"carProject\":\"/Data/Car/Z8/E52/EK/Preprocessing\",\"modelDefLabel\":\"M4X_Driveline_Antrieb\",\"name\":\"e52c_driveline_001_02.tail\",\"description\":\"- Drivelline ohne KJOINTS (Beams als Ersatz)\\n\",\"oid\":\"20274:B3Q\"},{\"owner\":\"q161820\",\"variantLabel\":\"e52c_coupe_rumpf_010.dat\",\"createdAt\":\"2003-06-06 16:07\",\"assembleType\":\"\",\"carProject\":\"/Data/Car/Z8/E52/EK/Preprocessing\",\"modelDefLabel\":\"M01_Karosseriegerippe\",\"name\":\"e52c_coupe_rumpf_010.dat\",\"description\":\"- Bodenblech vo (130161) innen und aussen verschweisst.\\n- Heckscheibe angebunden\\n- Standarddämpfung für Shells\\n(Fehler korr.)\\n\",\"oid\":\"19665:B3Q\"},{\"owner\":\"q161820\",\"variantLabel\":\"e52c_driveline_001_01.tail\",\"createdAt\":\"2003-05-07 10:17\",\"assembleType\":\"\",\"carProject\":\"/Data/Car/Z8/E52/EK/Preprocessing\",\"modelDefLabel\":\"M4X_Driveline_Antrieb\",\"name\":\"e52c_driveline_001_01.tail\",\"description\":\"- Drivelline Basis aus E52\\n\",\"oid\":\"17513:B3Q\"}]"
             * ; return dummyData;
             */

            List< NameValuePair > cb2QueryPayload = new ArrayList<>();

            boolean filterExists = isFilterExists( filter );
            if ( filterExists ) {
                // prepare query for filtered records
                Cb2TreeChildrenDTO nodeTree = readSubmodelTreeJsonAndGetNodeByOID( userId, userName, node );
                String projectPath = extractProjectPath( nodeTree );
                int pageNum = ( filter.getStart() + filter.getLength() ) / ( filter.getLength() );
                cb2QueryPayload.add( new BasicNameValuePair( "type",
                        bmwCaeBenchJsonType.equals( BmwCaeBenchEnums.CB2_DUMMY_TEMP_TREE.getValue() )
                                ? BmwCaeBenchEnums.CB2_SUBMODEL.getValue()
                                : bmwCaeBenchJsonType ) );
                cb2QueryPayload.add( new BasicNameValuePair( "expr", getExprForCb2Filters( projectPath, filter, bmwCaeBenchJsonType ) ) );
                cb2QueryPayload.add( new BasicNameValuePair( "pageSize", filter.getLength().toString() ) );
                cb2QueryPayload.add( new BasicNameValuePair( "page", String.valueOf( pageNum ) ) );
            } else {
                JSONObject jsonReqObj = new JSONObject( reqJson );
                String query = jsonReqObj.getString( "query" );
                String sortedQuery;
                if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_PROJECT.getValue() ) ) {
                    // special case project get
                    sortedQuery = query + addSorting( bmwCaeBenchJsonType, filter );
                } else {
                    // for all other caebench type
                    String[] splitQuery = query.split( bmwCaeBenchJsonType );
                    String baseQuery =
                            splitQuery[ ConstantsInteger.INTEGER_VALUE_ZERO ] + bmwCaeBenchJsonType + addSorting( bmwCaeBenchJsonType,
                                    filter );
                    sortedQuery = ( splitQuery.length == ConstantsInteger.INTEGER_VALUE_ONE ) ? baseQuery
                            : baseQuery + splitQuery[ ConstantsInteger.INTEGER_VALUE_ONE ];
                }
                int pageNum = ( filter.getStart() + filter.getLength() ) / ( filter.getLength() );
                cb2QueryPayload.add( new BasicNameValuePair( "query", sortedQuery ) );
                cb2QueryPayload.add( new BasicNameValuePair( "pageSize", filter.getLength().toString() ) );
                cb2QueryPayload.add( new BasicNameValuePair( "page", String.valueOf( pageNum ) ) );
            }
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );
            addFieldsForCb2Filters( cb2QueryPayload, bmwCaeBenchJsonType );

            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            JSONObject jsonQueryResponseObj = CB2Client.PostCB2( userName, cb2QueryPayload );
            // set filter Values from json response
            if ( jsonQueryResponseObj != null && jsonQueryResponseObj.has( "total" ) ) {
                Object pageSize = jsonQueryResponseObj.get( "total" );
                filter.setFilteredRecords( Long.valueOf( pageSize.toString() ) );
                filter.setTotalRecords( Long.valueOf( pageSize.toString() ) );
            }

            // prepare sub models for dummy files from cb2 api response
            if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_VARIANT.getValue() ) ) {
                return prepareFeilds( jsonQueryResponseObj, BmwCaeBenchVariantDTO.class );
            } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_SUBMODEL.getValue() ) || bmwCaeBenchJsonType.contains(
                    BmwCaeBenchEnums.CB2_OBJECT_TREE.getValue() ) || bmwCaeBenchJsonType.contains(
                    BmwCaeBenchEnums.CB2_DUMMY_TEMP_TREE.getValue() ) ) {
                return prepareFeilds( jsonQueryResponseObj, BmwCaeBenchSubModelDTO.class );
            } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_INPUT_DECK.getValue() ) ) {
                return prepareFeilds( jsonQueryResponseObj, BmwCaeBenchInputDeckDTO.class );
            } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_KEYRESULTS.getValue() ) ) {
                return prepareFeilds( jsonQueryResponseObj, BmwCaeBenchKeyResultsDTO.class );
            } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_RESULT.getValue() ) ) {
                return prepareFeilds( jsonQueryResponseObj, BmwCaeBenchResultDTO.class );
            } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_REPORT.getValue() ) ) {
                return prepareFeilds( jsonQueryResponseObj, BmwCaeBenchReportDTO.class );
            } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_PROJECT.getValue() ) ) {
                return prepareFeilds( jsonQueryResponseObj, BmwCaeBenchProjectsDTO.class );
            } else {
                return prepareFeilds( jsonQueryResponseObj, BmwCaeBenchSubModelDTO.class );
            }

        } catch ( final Exception e ) {
            log.error( "CB2 error : " + e.getMessage(), e );
            throw new SusException( "CB2 error : " + e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getListOfCb2InputDecks( String userId, String userName, String node, String bmwCaeBenchJsonType, FiltersDTO filter ) {
        try {
            List< NameValuePair > cb2QueryPayload = new ArrayList<>();
            Cb2TreeChildrenDTO nodeTree = readSubmodelTreeJsonAndGetNodeByOID( userId, userName, node );
            String projectPath = extractProjectPath( nodeTree );
            int pageNum = ( filter.getStart() + filter.getLength() ) / ( filter.getLength() );
            cb2QueryPayload.add( new BasicNameValuePair( "type", bmwCaeBenchJsonType ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr",
                    getExprForQADynaBaseInputDecks( projectPath, PropertiesManager.qaDynaBaseInputBaseStatus() ) ) );
            cb2QueryPayload.add( new BasicNameValuePair( "pageSize", filter.getLength().toString() ) );
            cb2QueryPayload.add( new BasicNameValuePair( "page", String.valueOf( pageNum ) ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );
            addFieldsForCb2Filters( cb2QueryPayload, bmwCaeBenchJsonType );
            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            JSONObject jsonQueryResponseObj = CB2Client.PostCB2( userName, cb2QueryPayload );
            // set filter Values from json response
            if ( jsonQueryResponseObj.has( "total" ) ) {
                Object pageSize = jsonQueryResponseObj.get( "total" );
                filter.setFilteredRecords( Long.valueOf( pageSize.toString() ) );
                filter.setTotalRecords( Long.valueOf( pageSize.toString() ) );
            }
            return prepareFeilds( jsonQueryResponseObj, BmwCaeBenchInputDeckDTO.class );
        } catch ( final Exception e ) {
            log.error( "CB2 error : " + e.getMessage(), e );
            throw new SusException( "CB2 error : " + e.getMessage() );
        }
    }

    /**
     * Gets expr for qa dyna base input decks.
     *
     * @param projectPath
     *         the project path
     * @param baseStatus
     *         the base status
     *
     * @return the expr for qa dyna base input decks
     */
    private String getExprForQADynaBaseInputDecks( String projectPath, String baseStatus ) {
        return "[" + "baseStatus=='" + baseStatus + "'&&project.name=='" + projectPath + "*'" + "]";
    }

    /**
     * Adds the sorting.
     *
     * @param bmwCaeBenchJsonType
     *         the bmw cae bench json type
     * @param filter
     *         the filter
     *
     * @return the string
     */
    private String addSorting( String bmwCaeBenchJsonType, FiltersDTO filter ) {

        String sortString = ConstantsString.EMPTY_STRING;
        String fieldName;
        if ( filter.getColumns() != null ) {
            for ( FilterColumn filterColumn : filter.getColumns() ) {
                if ( ValidationUtils.isNotNullOrEmpty( filterColumn.getDir() ) ) {
                    if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_VARIANT.getValue() ) ) {
                        fieldName = BmwCaeBenchVariantEnum.getValueByKey( filterColumn.getName() );
                    } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_SUBMODEL.getValue() ) || bmwCaeBenchJsonType.contains(
                            BmwCaeBenchEnums.CB2_OBJECT_TREE.getValue() ) || bmwCaeBenchJsonType.contains(
                            BmwCaeBenchEnums.CB2_DUMMY_TEMP_TREE.getValue() ) ) {
                        fieldName = BmwCaeBenchSubModelEnums.getValueByKey( filterColumn.getName() );
                    } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_INPUT_DECK.getValue() ) ) {
                        fieldName = BmwCaeBenchInputDeckEnum.getValueByKey( filterColumn.getName() );
                    } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_KEYRESULTS.getValue() ) ) {
                        fieldName = BmwCaeBenchKeyResultEnum.getValueByKey( filterColumn.getName() );
                    } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_RESULT.getValue() ) ) {
                        fieldName = BmwCaeBenchResultEnum.getValueByKey( filterColumn.getName() );
                    } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_PROJECT.getValue() ) ) {
                        fieldName = BmwCaeBenchResultEnum.getValueByKey( filterColumn.getName() );
                    } else {
                        fieldName = BmwCaeBenchSubModelEnums.getValueByKey( filterColumn.getName() );
                    }
                    if ( filterColumn.getDir().equalsIgnoreCase( ConstantsString.SORTING_DIRECTION_ASCENDING ) ) {
                        sortString = ".sort(" + fieldName + ", 'a')";
                    } else {
                        sortString = ".sort(" + fieldName + ", 'd')";
                    }
                }
            }
        }
        if ( sortString.isEmpty() ) {
            sortString = ".sort( createdAt, 'd')";
        }
        return sortString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< BmwCaeBenchSubModelTableDTO > getVariantSubModelsListOfCb2Objects( String variantObjectId, String bmwCaeBenchJsonType,
            String userName, String simdefOid ) {
        try {

            String study = "[objectId=='" + variantObjectId + "'].studiedIn";
            List< NameValuePair > cb2QueryPayload = new ArrayList<>();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "Variant" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr", study ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );
            if ( log.isDebugEnabled() ) {
                log.debug( "CB2 Query  :::::::::: " + cb2QueryPayload );
            }
            JSONObject jsonQueryResponseObjstudy = CB2Client.PostCB2( userName, cb2QueryPayload );

            String studyOid = extractNameOidFromCB2Resp( jsonQueryResponseObjstudy, "name", null ).get( 0 ).get( "id" );

            // <CB2>/cb2/servlet/rest/simgen/GetMatrix?study=AAAAGj2_SUU:AJo
            StringBuilder buildFilterQuery = new StringBuilder();
            buildFilterQuery.append( "study=" + studyOid );
            buildFilterQuery.append( "&format=json" );
            if ( log.isDebugEnabled() ) {
                log.debug( "CB2 Filter Query  :::::::::: " + buildFilterQuery );
            }
            JSONObject jsonQueryResponseObj = CB2Client.GetCB2( userName, "/cb2/servlet/rest/simgen/GetMatrix?",
                    buildFilterQuery.toString() );

            List< Map< String, Boolean > > submodelSortedObjectIds = extractSubmodelObjectIdsFromGetMatrix( jsonQueryResponseObj,
                    simdefOid );

            String submodelsStr = String.join( ",",
                    submodelSortedObjectIds.stream().map( name -> ( "'" + name.keySet().iterator().next() + "'" ) )
                            .collect( Collectors.toList() ) );

            if ( log.isDebugEnabled() ) {
                log.debug( "CB2 submodelsStr  :::::::::: " + submodelsStr );
            }

            String submodelExpr = "[objectId==[" + submodelsStr + "]]";

            List< NameValuePair > cb2SubModelQueryPayload = new ArrayList<>();
            cb2SubModelQueryPayload.add( new BasicNameValuePair( "type", bmwCaeBenchJsonType ) );
            cb2SubModelQueryPayload.add( new BasicNameValuePair( "expr", submodelExpr ) );
            cb2SubModelQueryPayload.add( new BasicNameValuePair( "pageSize", "1000" ) );
            cb2SubModelQueryPayload.add( new BasicNameValuePair( "format", "json" ) );

            List< String > fields = addFieldsForCb2Filters( BmwCaeBenchEnums.CB2_SUBMODEL.getValue() );

            for ( String f : fields ) {
                cb2SubModelQueryPayload.add( new BasicNameValuePair( "f", f ) );

            }

            JSONObject jsonQuerySubmodelResponseObj = CB2Client.PostCB2( userName, cb2SubModelQueryPayload );

            // prepare sub models for dummy files from cb2 api response
            return prepareSubModelTableList( submodelSortedObjectIds, jsonQuerySubmodelResponseObj );

        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    private List< Map< String, Boolean > > extractSubmodelObjectIdsFromGetMatrix( JSONObject jsonQueryResponseObj, String simdefOid ) {

        List< Map< String, Boolean > > submodelObjectIds = new ArrayList<>();

        if ( jsonQueryResponseObj.has( "matrix" ) ) {
            JSONArray itemsObj = jsonQueryResponseObj.getJSONObject( "matrix" ).getJSONArray( "items" );
            JSONArray simulationsObj = jsonQueryResponseObj.getJSONObject( "matrix" ).getJSONArray( "simulations" );

            // find index of simdef in simulations
            int simIndex = -1;
            for ( int i = 0; i < simulationsObj.length(); i++ ) {
                String simId = simulationsObj.getJSONObject( i ).getString( "oid" );
                if ( simdefOid.equals( simId ) ) {
                    simIndex = i;
                    break;
                }
            }

            for ( int i = 0; i < itemsObj.length(); i++ ) {
                String soid = itemsObj.getJSONObject( i ).getString( "oid" );
                if ( soid.isEmpty() || soid == null ) {
                    continue;
                }
                String objId = soid.split( ":" )[ 0 ];
                String chkBoxState = itemsObj.getJSONObject( i ).getJSONArray( "models" ).getJSONObject( simIndex ).getString( "type" );
                boolean isSelected = chkBoxState.equals( "Optional Default On" );
                Map< String, Boolean > modelitem = new HashMap<>();
                modelitem.put( objId, isSelected );
                log.info( modelitem.keySet().stream().map( key -> key + "=" + modelitem.get( key ) )
                        .collect( Collectors.joining( ", ", "{", "}" ) ) );
                submodelObjectIds.add( modelitem );
            }
        }
        return submodelObjectIds;
    }

    @Override
    public List< BmwCaeBenchEntity > getCb2Objects( String searchModel, String bmwCaeBenchJsonType, String userName ) {
        try {
            int pageSize = 1000;
            StringBuilder initialbuildFilterQuery = new StringBuilder();
            initialbuildFilterQuery.append( "type=" + bmwCaeBenchJsonType );
            initialbuildFilterQuery.append( "&expr=" + "[" + searchModel + "]" );
            initialbuildFilterQuery.append( "&pageSize=" + pageSize );
            initialbuildFilterQuery.append( "&format=json" );
            JSONObject jsonQueryResponseObj = CB2Client.GetCB2( userName, initialbuildFilterQuery.toString() );
            // prepare sub models for dummy files from cb2 api response
            List< BmwCaeBenchEntity > listFromCB2 = JsonUtils.jsonToList(
                    prepareFeilds( jsonQueryResponseObj, BmwCaeBenchSubModelDTO.class ), BmwCaeBenchEntity.class );

            if ( Integer.parseInt( jsonQueryResponseObj.get( "numPages" ).toString() ) > 1 ) {
                int pages_total = Integer.parseInt( jsonQueryResponseObj.get( "numPages" ).toString() );
                int page_nr = 2;
                while ( page_nr <= pages_total ) {
                    StringBuilder buildFilterQuery = new StringBuilder();
                    buildFilterQuery.append( "type=" + bmwCaeBenchJsonType );
                    buildFilterQuery.append( "&expr=" + "[" + searchModel + "]" );
                    buildFilterQuery.append( "&pageSize=" + pageSize );
                    buildFilterQuery.append( "&page=" + page_nr );
                    buildFilterQuery.append( "&format=json" );
                    JSONObject jsonQueryResponseObjInternal = CB2Client.GetCB2( userName, buildFilterQuery.toString() );
                    listFromCB2.addAll( JsonUtils.jsonToList( prepareFeilds( jsonQueryResponseObjInternal, BmwCaeBenchSubModelDTO.class ),
                            BmwCaeBenchEntity.class ) );
                    page_nr = page_nr + 1;
                }

            }
            return listFromCB2;
        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    /**
     * Prepare expr for sub variant.
     *
     * @param projectPath
     *         the project path
     *
     * @return the string
     */
    private String prepareExprForSubVariant( String projectPath ) {

        StringBuilder buildFilterQuery = new StringBuilder();
        buildFilterQuery.append( "objectId==" + "'" + projectPath + "'" );
        return "[" + buildFilterQuery + "]" + ".components.component:SubModelVariant.models:SubModel";
    }

    /**
     * Extract project path.
     *
     * @param nodeTree
     *         the node tree
     *
     * @return the string
     */
    private String extractProjectPath( Cb2TreeChildrenDTO nodeTree ) {
        String projectPath = null;
        if ( nodeTree != null ) {
            if ( nodeTree.getPath().equalsIgnoreCase( "/Data" ) ) {
                projectPath = null;
            } else {
                projectPath = nodeTree.getPath();
            }
        }
        return projectPath;
    }

    /**
     * Adds the fields for cb 2 filters.
     *
     * @param cb2QueryPayload
     *         the cb 2 query payload
     * @param bmwCaeBenchJsonType
     *         the bmw cae bench json type
     */
    private void addFieldsForCb2Filters( List< NameValuePair > cb2QueryPayload, String bmwCaeBenchJsonType ) {
        List< TableColumn > columns;
        if ( bmwCaeBenchJsonType.equalsIgnoreCase( BmwCaeBenchEnums.CB2_VARIANT.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchVariantDTO.class );
        } else if ( bmwCaeBenchJsonType.equalsIgnoreCase( BmwCaeBenchEnums.CB2_SUBMODEL.getValue() )
                || bmwCaeBenchJsonType.equalsIgnoreCase( BmwCaeBenchEnums.CB2_OBJECT_TREE.getValue() )
                || bmwCaeBenchJsonType.equalsIgnoreCase( BmwCaeBenchEnums.CB2_DUMMY_TEMP_TREE.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchSubModelDTO.class );
        } else if ( bmwCaeBenchJsonType.equalsIgnoreCase( BmwCaeBenchEnums.CB2_INPUT_DECK.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchInputDeckDTO.class );
        } else if ( bmwCaeBenchJsonType.equalsIgnoreCase( BmwCaeBenchEnums.CB2_KEYRESULTS.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchKeyResultsDTO.class );
        } else if ( bmwCaeBenchJsonType.equalsIgnoreCase( BmwCaeBenchEnums.CB2_RESULT.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchResultDTO.class );
        } else if ( bmwCaeBenchJsonType.equalsIgnoreCase( BmwCaeBenchEnums.CB2_REPORT.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchReportDTO.class );
        } else if ( bmwCaeBenchJsonType.equalsIgnoreCase( BmwCaeBenchEnums.CB2_STORY_BOARD.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchDTO.class );
        } else if ( bmwCaeBenchJsonType.equalsIgnoreCase( BmwCaeBenchEnums.CB2_PROJECT.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchProjectsDTO.class );
        } else {
            columns = GUIUtils.listColumns( BmwCaeBenchSubModelDTO.class );
        }
        for ( TableColumn tableColumn : columns ) {
            if ( tableColumn.getData().equals( BmwCaeBenchSubModelEnums.OWNER.getKey() ) ) {
                cb2QueryPayload.add( new BasicNameValuePair( "f", BmwCaeBenchSubModelEnums.OWNER.getValue() ) );
            } else if ( tableColumn.getData().equals( BmwCaeBenchSubModelEnums.CREATED_BY.getKey() ) ) {
                cb2QueryPayload.add( new BasicNameValuePair( "f", BmwCaeBenchSubModelEnums.CREATED_BY.getValue() ) );
            } else if ( tableColumn.getData().equals( BmwCaeBenchSubModelEnums.INPUT_DECK.getKey() ) ) {
                cb2QueryPayload.add( new BasicNameValuePair( "f", BmwCaeBenchSubModelEnums.INPUT_DECK.getValue() ) );
            } else {
                cb2QueryPayload.add( new BasicNameValuePair( "f", tableColumn.getData() ) );
            }
        }
    }

    /**
     * Adds the fields for cb 2 filters.
     *
     * @param bmwCaeBenchJsonType
     *         the bmw cae bench json type
     */
    private List< String > addFieldsForCb2Filters( String bmwCaeBenchJsonType ) {
        List< TableColumn > columns;
        if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_VARIANT.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchVariantDTO.class );
        } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_SUBMODEL.getValue() ) || bmwCaeBenchJsonType.contains(
                BmwCaeBenchEnums.CB2_OBJECT_TREE.getValue() ) || bmwCaeBenchJsonType.contains(
                BmwCaeBenchEnums.CB2_DUMMY_TEMP_TREE.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchSubModelTableDTO.class );
        } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_INPUT_DECK.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchInputDeckDTO.class );
        } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_KEYRESULTS.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchKeyResultsDTO.class );
        } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_RESULT.getValue() ) ) {
            columns = GUIUtils.listColumns( BmwCaeBenchResultDTO.class );
        } else {
            columns = GUIUtils.listColumns( BmwCaeBenchSubModelDTO.class );
        }

        List< String > fields = new ArrayList<>();
        for ( TableColumn tableColumn : columns ) {
            fields.add( tableColumn.getData() );
        }
        return fields;
    }

    /**
     * Gets the bmw enum value by property.
     *
     * @param colName
     *         the col name
     * @param bmwCaeBenchJsonType
     *         the bmw cae bench json type
     *
     * @return the bmw enum value by property
     */
    private String getBmwEnumValueByProperty( String colName, String bmwCaeBenchJsonType ) {
        if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_VARIANT.getValue() ) ) {
            return BmwCaeBenchVariantEnum.getValueByKey( colName );
        } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_SUBMODEL.getValue() ) || bmwCaeBenchJsonType.contains(
                BmwCaeBenchEnums.CB2_OBJECT_TREE.getValue() ) || bmwCaeBenchJsonType.contains(
                BmwCaeBenchEnums.CB2_DUMMY_TEMP_TREE.getValue() ) ) {
            return BmwCaeBenchSubModelEnums.getValueByKey( colName );
        } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_INPUT_DECK.getValue() ) ) {
            return BmwCaeBenchInputDeckEnum.getValueByKey( colName );
        } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_KEYRESULTS.getValue() ) ) {
            return BmwCaeBenchKeyResultEnum.getValueByKey( colName );
        } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_RESULT.getValue() ) ) {
            return BmwCaeBenchResultEnum.getValueByKey( colName );
        } else {
            return BmwCaeBenchSubModelEnums.getValueByKey( colName );
        }
    }

    /**
     * Gets the expr for cb 2 filters.
     *
     * @param projectPath
     *         the project path
     * @param filter
     *         the filter
     * @param bmwCaeBenchJsonType
     *         the bmw cae bench json type
     *
     * @return the expr for cb 2 filters
     */
    private String getExprForCb2Filters( String projectPath, FiltersDTO filter, String bmwCaeBenchJsonType ) {

        /*
         * NOTE: cb2 Filter Json Example : expr
         * [name=='*G70_I_VKBG_CFUNPR56C_F00EG008_B58_AWD*'&&project.name=='/Data/Car/LG
         * /G70/EG/Crash']
         */

        StringBuilder buildFilterQuery = new StringBuilder();
        if ( filter.getColumns() != null ) {
            for ( FilterColumn colm : filter.getColumns() ) {
                if ( colm.getFilters() != null && !colm.getFilters().isEmpty() ) {
                    for ( Filter ftl : colm.getFilters() ) {
                        String condition = getConditionForCb2( buildFilterQuery, ftl );
                        String operator = getOperatorForCb2( ftl );
                        if ( colm.getName().equals( RELEASE_LEVEL_LABEL ) ) {
                            buildFilterQuery.append( condition + getBmwEnumValueByProperty( colm.getName(), bmwCaeBenchJsonType ) + operator
                                    + ftl.getValue() );
                        } else {
                            buildFilterQuery.append( condition + getBmwEnumValueByProperty( colm.getName(), bmwCaeBenchJsonType ) + operator
                                    + START_FILTER_EXP + ftl.getValue() + END_FILTER_EXP );
                        }
                    }
                }
            }
        }

        if ( filter.getSearch() != null && !filter.getSearch().isEmpty() ) {
            String condition = "&&";
            if ( buildFilterQuery.toString().isEmpty() ) {
                condition = "";
            }
            String operator = "==";
            for ( String col : getSearchColumsByType( bmwCaeBenchJsonType ) ) {
                if ( col.equalsIgnoreCase( "name" ) || col.equalsIgnoreCase( "label" ) ) {
                    buildFilterQuery.append( condition + getBmwEnumValueByProperty( col, bmwCaeBenchJsonType ) + operator + START_FILTER_EXP
                            + filter.getSearch() + END_FILTER_EXP );
                    condition = "||";
                }
            }
        }

        if ( projectPath != null ) {
            // add unique project.name Filter to query to get perticulat containers objects
            if ( buildFilterQuery.toString().isEmpty() ) {
                buildFilterQuery.append( "project.name==" + "'" + projectPath + "*'" );
            } else {
                buildFilterQuery.append( "&&project.name==" + "'" + projectPath + "*'" );
            }

        }
        return "[" + buildFilterQuery + "]" + addSorting( bmwCaeBenchJsonType, filter );
    }

    /**
     * Gets the search colums by type.
     *
     * @param bmwCaeBenchJsonType
     *         the bmw cae bench json type
     *
     * @return the search colums by type
     */
    public List< String > getSearchColumsByType( String bmwCaeBenchJsonType ) {
        List< String > colList = new ArrayList<>();
        // apply search to only these cols
        if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_VARIANT.getValue() ) ) {
            colList.add( BmwCaeBenchVariantEnum.NAME.getKey() );
            colList.add( BmwCaeBenchVariantEnum.LABEL.getKey() );
            // colList.add( BmwCaeBenchVariantEnum.CREATED_AT.getKey() );
        } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_SUBMODEL.getValue() ) || bmwCaeBenchJsonType.contains(
                BmwCaeBenchEnums.CB2_OBJECT_TREE.getValue() ) || bmwCaeBenchJsonType.contains(
                BmwCaeBenchEnums.CB2_DUMMY_TEMP_TREE.getValue() ) ) {
            colList.add( BmwCaeBenchSubModelEnums.NAME.getKey() );
            colList.add( BmwCaeBenchSubModelEnums.DESCRIPTION.getKey() );
            colList.add( BmwCaeBenchSubModelEnums.ASSEMBLY_TYPE.getKey() );
            colList.add( BmwCaeBenchSubModelEnums.OWNER.getKey() );
            colList.add( BmwCaeBenchSubModelEnums.VARIANT_LABLE.getKey() );
        } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_INPUT_DECK.getValue() ) ) {
            colList.add( BmwCaeBenchInputDeckEnum.NAME.getKey() );
            colList.add( BmwCaeBenchInputDeckEnum.DESCRIPTION.getKey() );
            colList.add( BmwCaeBenchInputDeckEnum.OWNER.getKey() );
        } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_KEYRESULTS.getValue() ) ) {
            colList.add( BmwCaeBenchKeyResultEnum.NAME.getKey() );
            colList.add( BmwCaeBenchKeyResultEnum.DESCRIPTION.getKey() );
            colList.add( BmwCaeBenchKeyResultEnum.OWNER.getKey() );
            colList.add( BmwCaeBenchKeyResultEnum.TYPE.getKey() );
        } else if ( bmwCaeBenchJsonType.contains( BmwCaeBenchEnums.CB2_RESULT.getValue() ) ) {
            colList.add( BmwCaeBenchResultEnum.NAME.getKey() );
            colList.add( BmwCaeBenchResultEnum.DESCRIPTION.getKey() );
            colList.add( BmwCaeBenchResultEnum.OWNER.getKey() );
        }
        return colList;
    }

    /**
     * Read submodel tree json and get node by OID.
     *
     * @param userName
     *         the username
     * @param node
     *         the node
     *
     * @return the cb 2 tree children DTO
     */
    private Cb2TreeChildrenDTO readSubmodelTreeJsonAndGetNodeByOID( String userId, String userName, String node ) {

        BmwCaeBenchTreeDTO bmwCaeBenchTreeDTO = readTreeJsonFileWithImpersonation( String.format( SUBMODELS_JSON_FILE_PATH,
                TokenizedLicenseUtil.getUserUID( userId ) ), userName );
        if ( bmwCaeBenchTreeDTO == null ) {
            throw new SusException( "CB2 api prepration failed : Cant read " + SUBMODELS_JSON_FILE_PATH );
        }
        getNodeFromCb2TreeJson( bmwCaeBenchTreeDTO.getData().get( 0 ), node );
        Cb2TreeChildrenDTO nodeTree = this.cb2TreeChildrenDTOGlobal;
        this.cb2TreeChildrenDTOGlobal = null;
        if ( nodeTree != null ) {
            log.info( nodeTree.toString() );
        }
        return nodeTree;
    }

    /**
     * Checks if is filter exists.
     *
     * @param filter
     *         the filter
     *
     * @return true, if is filter exists
     */
    private boolean isFilterExists( FiltersDTO filter ) {
        if ( filter.getSearch() != null && !filter.getSearch().isEmpty() ) {
            return true;
        }
        if ( filter.getColumns() != null ) {
            for ( FilterColumn colm : filter.getColumns() ) {
                if ( colm.getFilters() != null && !colm.getFilters().isEmpty() ) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the node from cb 2 tree json.
     *
     * @param cb2TreeChildrenDTO
     *         the cb 2 tree children DTO
     * @param node
     *         the node
     */
    public void getNodeFromCb2TreeJson( Cb2TreeChildrenDTO cb2TreeChildrenDTO, String node ) {
        if ( cb2TreeChildrenDTO.getTitle().equalsIgnoreCase( node ) || cb2TreeChildrenDTO.getId().equalsIgnoreCase( node ) ) {
            this.cb2TreeChildrenDTOGlobal = cb2TreeChildrenDTO;
        } else {
            if ( CollectionUtil.isNotEmpty( cb2TreeChildrenDTO.getChildren() ) ) {
                for ( Cb2TreeChildrenDTO child : cb2TreeChildrenDTO.getChildren() ) {
                    getNodeFromCb2TreeJson( child, node );
                }
            }
        }
    }

    /**
     * Prepare feilds.
     *
     * @param jsonQueryResponseObj
     *         the json query response obj
     * @param clazz
     *         the clazz
     *
     * @return the string
     */
    private String prepareFeilds( JSONObject jsonQueryResponseObj, Class< ? > clazz ) {
        List< Map< String, String > > payloadList = new ArrayList<>();
        List< TableColumn > columns = GUIUtils.listColumns( clazz );

        if ( jsonQueryResponseObj.has( "objects" ) ) {
            JSONArray objects = jsonQueryResponseObj.getJSONArray( "objects" );
            for ( int i = 0; i < objects.length(); i++ ) {
                JSONObject subObjects = objects.getJSONObject( i );
                Map< String, String > payloadMap = new HashMap<>();

                if ( subObjects.has( "attrs" ) ) {
                    JSONObject attrs = subObjects.getJSONObject( "attrs" );

                    for ( TableColumn tableColumn : columns ) {
                        String column;
                        if ( tableColumn.getData().equals( BmwCaeBenchSubModelEnums.OWNER.getKey() ) ) {
                            column = BmwCaeBenchSubModelEnums.OWNER.getValue();
                        } else if ( tableColumn.getData().equals( BmwCaeBenchSubModelEnums.CREATED_BY.getKey() ) ) {
                            column = BmwCaeBenchSubModelEnums.CREATED_BY.getValue();
                        } else if ( tableColumn.getData().equals( BmwCaeBenchSubModelEnums.INPUT_DECK.getKey() ) ) {
                            column = BmwCaeBenchSubModelEnums.INPUT_DECK.getValue();
                        } else {
                            column = tableColumn.getData();
                        }

                        if ( attrs.has( column ) ) {
                            JSONObject lable = attrs.getJSONObject( column );
                            String lableValue = "";
                            if ( lable.has( "text" ) ) {
                                lableValue = lable.getString( "text" );
                            } else if ( lable.has( "items" ) ) {
                                JSONArray arr = lable.getJSONArray( "items" );
                                for ( int j = 0; j < arr.length(); j++ ) {
                                    String inputDeck = arr.getJSONObject( j ).getString( "text" );
                                    if ( j == arr.length() - 1 ) {
                                        lableValue = lableValue + inputDeck;
                                    } else {
                                        lableValue = lableValue + inputDeck + ", ";
                                    }
                                }
                            }
                            payloadMap.put( tableColumn.getData(), lableValue );
                        }
                    }
                }
                if ( subObjects.has( "oid" ) ) {
                    payloadMap.put( "oid", subObjects.getString( "oid" ) );
                    // payloadMap.put( "id", UUID.randomUUID().toString() );
                }
                payloadList.add( payloadMap );
            }
        }
        if ( payloadList.isEmpty() ) {
            return null;
        }
        return JsonUtils.toJson( payloadList );
    }

    /**
     * Prepare feilds.
     *
     * @param submodelSortedObjectIds
     *         the submodel sorted object ids
     * @param jsonQueryResponseObj
     *         the json query response obj
     *
     * @return the string
     */
    private List< BmwCaeBenchSubModelTableDTO > prepareSubModelTableList( List< Map< String, Boolean > > submodelSortedObjectIds,
            JSONObject jsonQueryResponseObj ) {
        List< BmwCaeBenchSubModelTableDTO > bmwCaeBenchDtolist = new ArrayList<>();
        List< TableColumn > columns = GUIUtils.listColumns( BmwCaeBenchSubModelTableDTO.class );

        for ( Map< String, Boolean > sortedSubModelObjectId : submodelSortedObjectIds ) {
            if ( jsonQueryResponseObj.has( "objects" ) ) {
                JSONArray objects = jsonQueryResponseObj.getJSONArray( "objects" );
                for ( int i = 0; i < objects.length(); i++ ) {
                    JSONObject subObjects = objects.getJSONObject( i );
                    BmwCaeBenchSubModelTableDTO subModelTableDTO = new BmwCaeBenchSubModelTableDTO();

                    if ( subObjects.has( "attrs" ) ) {
                        JSONObject attrs = subObjects.getJSONObject( "attrs" );
                        String currentObjectId = subObjects.getString( "oid" ).split( ":" )[ 0 ];
                        if ( !sortedSubModelObjectId.containsKey( currentObjectId ) ) {
                            continue;
                        }

                        subModelTableDTO.setIsSelected( sortedSubModelObjectId.get( currentObjectId ) );

                        for ( TableColumn tableColumn : columns ) {
                            String column = tableColumn.getData();
                            if ( attrs.has( column ) ) {
                                JSONObject lable = attrs.getJSONObject( column );
                                String lableValue = "N/A";
                                if ( lable.has( "text" ) ) {
                                    lableValue = lable.getString( "text" );
                                }
                                String idValue = "N/A";
                                if ( lable.has( "oid" ) ) {
                                    idValue = lable.getString( "oid" );
                                }

                                if ( tableColumn.getData().equals( BmwCaeBenchSubModelEnums.NAME.getKey() ) ) {
                                    subModelTableDTO.setName( lableValue );
                                } else if ( tableColumn.getData().equals( BmwCaeBenchSubModelEnums.DESCRIPTION.getKey() ) ) {
                                    subModelTableDTO.setDescription( lableValue );
                                } else if ( tableColumn.getData().equals( BmwCaeBenchSubModelEnums.CREATED_AT.getKey() ) ) {
                                    subModelTableDTO.setCreatedAt( lableValue );
                                } else if ( tableColumn.getData().equals( BmwCaeBenchSubModelEnums.PROJECT.getKey() ) ) {
                                    subModelTableDTO.setProject( lableValue );
                                } else if ( tableColumn.getData().equals( BmwCaeBenchSubModelEnums.ASSEMBLY_TYPE.getKey() ) ) {
                                    BmwCaeBenchField field = new BmwCaeBenchField( idValue, lableValue );
                                    subModelTableDTO.setAssembleType( field );
                                } else if ( tableColumn.getData().equals( BmwCaeBenchSubModelEnums.CAR_PROJECT.getKey() ) ) {
                                    BmwCaeBenchField field = new BmwCaeBenchField( idValue, lableValue );
                                    subModelTableDTO.setCarProject( field );
                                } else if ( tableColumn.getData().equals( BmwCaeBenchSubModelEnums.CREATED_BY.getKey() ) ) {
                                    BmwCaeBenchField field = new BmwCaeBenchField( idValue, lableValue );
                                    subModelTableDTO.setCreatedBy( field );
                                } else if ( tableColumn.getData().equals( BmwCaeBenchSubModelEnums.OWNER.getKey() ) ) {
                                    BmwCaeBenchField field = new BmwCaeBenchField( idValue, lableValue );
                                    subModelTableDTO.setOwner( field );
                                } else if ( tableColumn.getData().equals( "variant" ) ) {
                                    BmwCaeBenchField field = new BmwCaeBenchField( idValue, lableValue );
                                    subModelTableDTO.setVariant( field );
                                } else if ( tableColumn.getData().equals( "projectPhase" ) ) {
                                    BmwCaeBenchField field = new BmwCaeBenchField( idValue, lableValue );
                                    subModelTableDTO.setProjectPhase( field );
                                } else if ( tableColumn.getData().equals( "department" ) ) {
                                    BmwCaeBenchField field = new BmwCaeBenchField( idValue, lableValue );
                                    subModelTableDTO.setDepartment( field );
                                } else if ( tableColumn.getData().equals( "format" ) ) {
                                    BmwCaeBenchField field = new BmwCaeBenchField( idValue, lableValue );
                                    subModelTableDTO.setFormat( field );
                                } else if ( tableColumn.getData().equals( "item" ) ) {
                                    BmwCaeBenchField field = new BmwCaeBenchField( idValue, lableValue );
                                    subModelTableDTO.setItem( field );
                                } else if ( tableColumn.getData().equals( "itemDefinition" ) ) {
                                    BmwCaeBenchField field = new BmwCaeBenchField( idValue, lableValue );
                                    subModelTableDTO.setItemDefinition( field );
                                }

                                if ( subModelTableDTO.getId() == null && subObjects.has( "oid" ) ) {
                                    subModelTableDTO.setId( subObjects.getString( "oid" ) );
                                }

                            }
                        }
                    }
                    bmwCaeBenchDtolist.add( subModelTableDTO );
                }
            }
        }
        if ( bmwCaeBenchDtolist.isEmpty() ) {
            return null;
        }
        return bmwCaeBenchDtolist;
    }

    /**
     * Read tree json file with impersonation.
     *
     * @param jsonPath
     *         the json path
     * @param userName
     *         the username
     *
     * @return the bmw cae bench tree DTO
     */
    private static BmwCaeBenchTreeDTO readTreeJsonFileWithImpersonation( String jsonPath, String userName ) {
        try {
            String destFileCookie = PropertiesManager.getDefaultServerTempPath() + File.separator + "cb2Tree_" + UUID.randomUUID() + ".txt";
            LinuxUtils.copyFileFromSrcPathToDestPathWithImpersonation( userName, jsonPath, destFileCookie );

            File cb2TreeFile = new File( destFileCookie );
            FileUtils.setGlobalAllFilePermissions( cb2TreeFile );

            try ( InputStream is = new FileInputStream( cb2TreeFile ) ) {
                return JsonUtils.jsonToObject( is, BmwCaeBenchTreeDTO.class );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }
            return null;

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return null;
    }

    /**
     * Gets the condition for cb 2.
     *
     * @param buildFilterQuery
     *         the build filter query
     * @param ftl
     *         the ftl
     *
     * @return the condition for cb 2
     */
    private static String getConditionForCb2( StringBuilder buildFilterQuery, Filter ftl ) {
        String condition = "";
        if ( ftl.getCondition().equalsIgnoreCase( "AND" ) ) {
            condition = "&&";
        } else if ( ftl.getCondition().equalsIgnoreCase( "OR" ) ) {
            condition = "||";
        }

        if ( buildFilterQuery.toString().equalsIgnoreCase( "" ) || buildFilterQuery.toString().isEmpty() ) {
            condition = "";
        }
        return condition;
    }

    /**
     * Gets the operator for cb 2.
     *
     * @param ftl
     *         the ftl
     *
     * @return the operator for cb 2
     */
    private static String getOperatorForCb2( Filter ftl ) {
        String operator = "";
        if ( ftl.getOperator().equalsIgnoreCase( CONTAINS ) ) {
            operator = "==";
        } else if ( ftl.getOperator().equalsIgnoreCase( DOES_NOT_CONTAIN ) ) {
            operator = "!=";
        } else if ( ftl.getOperator().equalsIgnoreCase( EQUALS ) ) {
            operator = "==";
        } else if ( ftl.getOperator().equalsIgnoreCase( NOT_EQUALS ) ) {
            operator = "!=";
        } else if ( ftl.getOperator().equalsIgnoreCase( BEGINS_WITH ) ) {
            operator = "==";
        } else if ( ftl.getOperator().equalsIgnoreCase( ENDS_WITH ) ) {
            operator = "==";
        }
        return operator;
    }

    @Override
    public String getSenarioListOfCb2Objects( String userName, String selectedTreeObjName, FiltersDTO filter ) {
        try {
            List< NameValuePair > cb2QueryPayload = prepareScenarioQuery( selectedTreeObjName, filter );

            if ( log.isDebugEnabled() ) {
                log.debug( "CB2 Query  :::::::::: " + cb2QueryPayload );
            }
            JSONObject jsonQueryResponseObj = CB2Client.PostCB2( userName, cb2QueryPayload );

            // set filter Values from json response
            if ( jsonQueryResponseObj != null && jsonQueryResponseObj.has( "total" ) ) {
                Object pageSize = jsonQueryResponseObj.get( "total" );
                filter.setFilteredRecords( Long.valueOf( pageSize.toString() ) );
                filter.setTotalRecords( Long.valueOf( pageSize.toString() ) );
            }
            return prepareFeilds( jsonQueryResponseObj, BmwCaeBenchSenarioDTO.class );
        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    private List< NameValuePair > prepareScenarioQuery( String selectedTreeObjName, FiltersDTO filter ) {
        String senariosQuery;
        if ( filter != null && filter.getSearch() != null && StringUtils.isNotEmpty( filter.getSearch() ) ) {
            senariosQuery =
                    "[ label=='*" + filter.getSearch() + "' || name=='*" + filter.getSearch() + "' || description=='*" + filter.getSearch()
                            + "']";
        } else {
            selectedTreeObjName = selectedTreeObjName.replace( "/", "*" );
            senariosQuery = "[ label=='" + selectedTreeObjName + "']";
        }

        int pageNum = ( filter.getStart() + filter.getLength() ) / ( filter.getLength() );
        List< NameValuePair > cb2QueryPayload = new ArrayList<>();

        cb2QueryPayload.add( new BasicNameValuePair( "type", "Scenario" ) );
        cb2QueryPayload.add( new BasicNameValuePair( "expr", senariosQuery ) );
        cb2QueryPayload.add( new BasicNameValuePair( "pageSize", filter.getLength().toString() ) );
        cb2QueryPayload.add( new BasicNameValuePair( "page", String.valueOf( pageNum ) ) );
        cb2QueryPayload.add( new BasicNameValuePair( "f", "name" ) );
        cb2QueryPayload.add( new BasicNameValuePair( "f", "label" ) );
        cb2QueryPayload.add( new BasicNameValuePair( "f", "description" ) );
        cb2QueryPayload.add( new BasicNameValuePair( "f", "type" ) );
        cb2QueryPayload.add( new BasicNameValuePair( "f", "disciplineContext.name" ) );
        cb2QueryPayload.add( new BasicNameValuePair( "f", "createdAt" ) );
        cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );
        return cb2QueryPayload;
    }

    @Override
    public JSONObject getCb2RunGeneralProjectItemsDropDown( String userName, String selectedTreeObjName ) {
        try {
            // selectedTreeObjName = "/Data/Car/LR/RR25/EK/Crash";
            String itemDropDown = "[project.name=='" + selectedTreeObjName + "' && definition.name=='*berech*']";
            List< NameValuePair > cb2QueryPayload = new ArrayList<>();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "Item" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr", itemDropDown ) );
            cb2QueryPayload.add( new BasicNameValuePair( "f", "label" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "f", "definition" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "f", "disciplineContext" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "f", "objectId" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "f", "definition.variantDefs[0]" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );
            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            return CB2Client.PostCB2( userName, cb2QueryPayload );
        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    @Override
    public JSONObject getCb2RunGeneralTabObjectTypeDropDown( String userName, String disciplineContextName ) {
        try {
            String obejctTypeQuery = "[name=='" + disciplineContextName + "*' && parent.name=='SimulationVariant']";
            List< NameValuePair > cb2QueryPayload = new ArrayList<>();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "ObjectType" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr", obejctTypeQuery ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );
            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            return CB2Client.PostCB2( userName, cb2QueryPayload );
        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    /*    @Override
    public JSONObject getCb2RunGeneralProjectItemsDefinitionDropDown( String userName, String selectedTreeObjName, String itemId ) {
        try {
            // selectedTreeObjName = "/Data/Car/LR/RR25/EK/Crash";
            String itemDropDown = "[project.name=='" + selectedTreeObjName + "' && definition.name=='*berech*' && oid=='" + itemId + "']";
            List< NameValuePair > cb2QueryPayload = new ArrayList< NameValuePair >();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "Item" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr", itemDropDown ) );

            cb2QueryPayload.add( new BasicNameValuePair( "f", "label" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "f", "definition.name" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "f", "disciplineContext.name" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );

            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            JSONObject jsonQueryResponseObj = CB2Client.PostCB2( userName, cb2QueryPayload );
            return jsonQueryResponseObj;

        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(),e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }*/

    @Override
    public JSONObject getCb2RunVariantProjectPhaseDropDown( String userName ) {

        try {
            String itemDropDown = "[name  =='*' && isActive=='true']";
            List< NameValuePair > cb2QueryPayload = new ArrayList<>();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "ProjectPhase" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr", itemDropDown ) );
            cb2QueryPayload.add( new BasicNameValuePair( "f", "name" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );

            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            return CB2Client.PostCB2( userName, cb2QueryPayload );

        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    @Override
    public List< Map< String, String > > getCb2StudyDefsByVariant( String userName, String variantOid ) {

        try {
            String studyDefination = "[objectId=='" + variantOid.split( ":" )[ 0 ] + "'].studiedIn:SimulationStudy.definition";
            List< NameValuePair > cb2QueryPayload = new ArrayList<>();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "Variant" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr", studyDefination ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );
            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            JSONObject jsonQueryResponseObj = CB2Client.PostCB2( userName, cb2QueryPayload );

            List< Map< String, String > > simulationDefinationIds = extractNameOidFromCB2Resp( jsonQueryResponseObj, "name", null );
            Map< String, String > noneId = new HashMap<>();
            noneId.put( "id", "None" );
            noneId.put( "name", "None" );
            simulationDefinationIds.add( 0, noneId );
            return simulationDefinationIds;
        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    @Override
    public List< Map< String, String > > getCb2SimDefsByScenario( String userName, String variantAndScenarioOid ) {

        log.info( "variantAndScenarioOid  :::::::::: " + variantAndScenarioOid );
        String[] splitt = variantAndScenarioOid.split( "_" );
        log.info( "splitt  :::::::::: " + splitt );
        String scenarioOid = splitt[ 0 ];
        log.info( "scenarioOid  :::::::::: " + scenarioOid );
        String variantOid = splitt[ 1 ];
        log.info( "variantOid  :::::::::: " + variantOid );
        try {
            String studyDefination =
                    "[objectId=='" + variantOid.split( ":" )[ 0 ] + "'].studiedIn:SimulationStudy..simulationDefs.[scenario.objectId=='"
                            + scenarioOid.split( ":" )[ 0 ] + "']";
            List< NameValuePair > cb2QueryPayload = new ArrayList<>();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "Variant" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr", studyDefination ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );
            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            JSONObject jsonQueryResponseObj = CB2Client.PostCB2( userName, cb2QueryPayload );

            List< Map< String, String > > simulationDefinationIds = extractNameOidFromCB2Resp( jsonQueryResponseObj, "name", null );
            Map< String, String > noneId = new HashMap<>();
            noneId.put( "id", "None" );
            noneId.put( "name", "None" );
            simulationDefinationIds.add( 0, noneId );
            return simulationDefinationIds;
        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    @Override
    public List< Map< String, String > > getCb2ScenariosByVariant( String userName, String variantOid ) {

        try {
            String studyDefination = "[name=='*' && simulationDefs.simStudies.subject.objectId=='" + variantOid.split( ":" )[ 0 ] + "']";
            List< NameValuePair > cb2QueryPayload = new ArrayList<>();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "Scenario" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr", studyDefination ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );
            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            JSONObject jsonQueryResponseObj = CB2Client.PostCB2( userName, cb2QueryPayload );

            return extractNameOidFromCB2Resp( jsonQueryResponseObj, "label", variantOid );
        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    @Override
    public List< Map< String, String > > getCb2SetupByStudyDef( String userName, String assembleSolvePost, String studydefOid ) {

        String param = switch ( assembleSolvePost ) {
            case "assemble" -> "assembleSetup_Inherited";
            case "solver" -> "solverSetup_Inherited";
            case "post" -> "postProcessSetup_Inherited";
            default -> "postProcessSetup_Inherited";
        };

        try {
            String studyDefination = "[objectId=='" + studydefOid.split( ":" )[ 0 ] + "'].availableSimulationDef." + param;
            List< NameValuePair > cb2QueryPayload = new ArrayList<>();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "SimulationStudyDef" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr", studyDefination ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );
            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            JSONObject jsonQueryResponseObj = CB2Client.PostCB2( userName, cb2QueryPayload );

            return extractNameOidFromCB2Resp( jsonQueryResponseObj, "label", null );
        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    private List< Map< String, String > > extractNameOidFromCB2Resp( JSONObject jsonQueryResponseObj, String nameAttr, String appendOid ) {
        List< Map< String, String > > output = new ArrayList<>();

        if ( jsonQueryResponseObj.has( "objects" ) ) {
            JSONArray objects = jsonQueryResponseObj.getJSONArray( "objects" );
            for ( int i = 0; i < objects.length(); i++ ) {
                JSONObject subObject = objects.getJSONObject( i );

                Map< String, String > cb2objects = new HashMap<>();
                cb2objects.put( "name", String.valueOf( subObject.getJSONObject( "attrs" ).getJSONObject( nameAttr ).get( "text" ) ) );
                if ( null == appendOid ) {
                    cb2objects.put( "id", String.valueOf( subObject.get( "oid" ) ) );
                } else {
                    cb2objects.put( "id", subObject.get( "oid" ) + "_" + appendOid );
                }
                output.add( cb2objects );
            }

        }
        return output;
    }

    @Override
    public JSONObject getCb2RunVariantSolverFormXML( String userName, String simDefObjectId ) {

        try {
            String query = "[objectId=='" + simDefObjectId + "'].solverSetup_Inherited";
            List< NameValuePair > cb2QueryPayload = new ArrayList<>();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "SimulationDef" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr", query ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );
            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            return CB2Client.PostCB2( userName, cb2QueryPayload );
        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    @Override
    public JSONObject getCb2RunVariantAssembleFormXML( String userName, String simDefObjectId ) {

        try {
            String query = "[objectId=='" + simDefObjectId + "'].assembleSetup_Inherited";
            List< NameValuePair > cb2QueryPayload = new ArrayList<>();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "SimulationDef" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr", query ) );
            cb2QueryPayload.add( new BasicNameValuePair( "f", "displayConfig.files" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );
            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            return CB2Client.PostCB2( userName, cb2QueryPayload );
        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    @Override
    public JSONObject getCb2RunVariantPostFormXML( String userName, String simDefObjectId ) {

        try {
            String query = "[objectId=='" + simDefObjectId + "'].postProcessSetup_Inherited";
            List< NameValuePair > cb2QueryPayload = new ArrayList<>();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "SimulationDef" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr", query ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );
            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            return CB2Client.PostCB2( userName, cb2QueryPayload );
        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    @Override
    public String getCb2RunVariantAssembleAppParam( String userName, String projectPath ) {
        try {
            // projectPath = "/Data/Car/LG/G05/EK";
            String projPimExp = "[name=='" + projectPath + "']";
            List< NameValuePair > cb2QueryPayload = new ArrayList<>();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "Project" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr", projPimExp ) );
            cb2QueryPayload.add( new BasicNameValuePair( "f", "disciplineContext.name" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );

            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            JSONObject jsonQueryResponseObj = CB2Client.PostCB2( userName, cb2QueryPayload );

            String disciplineContextName = extractDisciplineContextName( jsonQueryResponseObj );

            // disciplineContextName = "FGS";
            // AssembleParams query
            String assemblePramExp = "[ name=='*' && context.name == '" + disciplineContextName + "']";
            List< NameValuePair > cb2QueryPayloadPim = new ArrayList<>();
            cb2QueryPayloadPim.add( new BasicNameValuePair( "type", "AssembleParams" ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "expr", assemblePramExp ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "f", "label" ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "f", "displayConfig.files" ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "format", "json" ) );

            log.info( "CB2 Query  :::::::::: " + cb2QueryPayloadPim );
            JSONObject jsonQueryResponseObjPim = CB2Client.PostCB2( userName, cb2QueryPayloadPim );

            // String pimName = extractPimName( jsonQueryResponseObjPim );
            return extractPimObjectId( jsonQueryResponseObjPim );

        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    @Override
    public String getCb2RunVariantSolveAppParam( String userName, String projectPath ) {
        try {
            // projectPath = "/Data/Car/LG/G05/EK";
            String projPimExp = "[name=='" + projectPath + "']";
            List< NameValuePair > cb2QueryPayload = new ArrayList<>();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "Project" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr", projPimExp ) );
            cb2QueryPayload.add( new BasicNameValuePair( "f", "disciplineContext.name" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );

            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            JSONObject jsonQueryResponseObj = CB2Client.PostCB2( userName, cb2QueryPayload );

            String disciplineContextName = extractDisciplineContextName( jsonQueryResponseObj );

            // disciplineContextName = "FGS";
            // AssembleParams query
            String assemblePramExp = "[ name=='*' && context.name == '" + disciplineContextName + "']";
            List< NameValuePair > cb2QueryPayloadPim = new ArrayList<>();
            cb2QueryPayloadPim.add( new BasicNameValuePair( "type", "SolverParams" ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "expr", assemblePramExp ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "f", "label" ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "f", "displayConfig.files" ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "format", "json" ) );

            log.info( "CB2 Query  :::::::::: " + cb2QueryPayloadPim );
            JSONObject jsonQueryResponseObjPim = CB2Client.PostCB2( userName, cb2QueryPayloadPim );

            // String pimName = extractPimName( jsonQueryResponseObjPim );
            return extractPimObjectId( jsonQueryResponseObjPim );

        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    @Override
    public String getCb2RunVariantPostProcessingAppParam( String userName, String projectPath ) {
        try {
            // projectPath = "/Data/Car/LG/G05/EK";
            String projPimExp = "[name=='" + projectPath + "']";
            List< NameValuePair > cb2QueryPayload = new ArrayList<>();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "Project" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr", projPimExp ) );
            cb2QueryPayload.add( new BasicNameValuePair( "f", "disciplineContext.name" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );

            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            JSONObject jsonQueryResponseObj = CB2Client.PostCB2( userName, cb2QueryPayload );

            String disciplineContextName = extractDisciplineContextName( jsonQueryResponseObj );

            // disciplineContextName = "FGS";
            // AssembleParams query
            String assemblePramExp = "[ name=='*' && context.name == '" + disciplineContextName + "']";
            List< NameValuePair > cb2QueryPayloadPim = new ArrayList<>();
            cb2QueryPayloadPim.add( new BasicNameValuePair( "type", "PostProcessingParams" ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "expr", assemblePramExp ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "f", "label" ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "f", "displayConfig.files" ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "format", "json" ) );

            log.info( "CB2 Query  :::::::::: " + cb2QueryPayloadPim );
            JSONObject jsonQueryResponseObjPim = CB2Client.PostCB2( userName, cb2QueryPayloadPim );

            // String pimName = extractPimName( jsonQueryResponseObjPim );
            return extractPimObjectId( jsonQueryResponseObjPim );

        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    /*  @Override
    public JSONObject getCb2PramsByPramTypeAndProjectPath( String userName, String projectPath, String paramType ) {
        try {
            // projectPath = "/Data/Car/LG/G05/EK";
            String projPimExp = "[name=='" + projectPath + "']";
            List< NameValuePair > cb2QueryPayload = new ArrayList< NameValuePair >();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "Project" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr", projPimExp ) );
            cb2QueryPayload.add( new BasicNameValuePair( "f", "disciplineContext.name" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );

            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            JSONObject jsonQueryResponseObj = CB2Client.PostCB2( userName, cb2QueryPayload );

            String disciplineContextName = extractDisciplineContextName( jsonQueryResponseObj );

            // disciplineContextName = "FGS";
            // AssembleParams query
            String assemblePramExp = "[ name=='*' && context.name == '" + disciplineContextName + "']";
            List< NameValuePair > cb2QueryPayloadPim = new ArrayList< NameValuePair >();
            cb2QueryPayloadPim.add( new BasicNameValuePair( "type", paramType ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "expr", assemblePramExp ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "f", "label" ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "f", "displayConfig.files" ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "format", "json" ) );

            log.info( "CB2 Query  :::::::::: " + cb2QueryPayloadPim );
            JSONObject jsonQueryResponseObjPim = CB2Client.PostCB2( userName, cb2QueryPayloadPim );
            return jsonQueryResponseObjPim;
        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(),e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }
    */
    @Override
    public Map< String, Map< String, String > > downloadPimByOidAndReadPim( String userName, JSONObject jsonQueryResponseObjPim ) {
        try {
            String pimOid = extractPimObjectId( jsonQueryResponseObjPim );
            InputStream inputStream = getPimXmlByObjectId( userName, pimOid );
            return getAllPimXmlOptions( inputStream );
        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    /* @Override
    public Map< String, Map< String, String > > getCb2RunVariantAssemblyPimOrDownloadPIM( String userName, String projectPath ) {
        try {
            // projectPath = "/Data/Car/LG/G05/EK";
            String projPimExp = "[name=='" + projectPath + "']";
            List< NameValuePair > cb2QueryPayload = new ArrayList< NameValuePair >();
            cb2QueryPayload.add( new BasicNameValuePair( "type", "Project" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "expr", projPimExp ) );
            cb2QueryPayload.add( new BasicNameValuePair( "f", "disciplineContext.name" ) );
            cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );

            log.info( "CB2 Query  :::::::::: " + cb2QueryPayload );
            JSONObject jsonQueryResponseObj = CB2Client.PostCB2( userName, cb2QueryPayload );

            String disciplineContextName = extractDisciplineContextName( jsonQueryResponseObj );

            // disciplineContextName = "FGS";
            // AssembleParams query
            String assemblePramExp = "[ name=='*' && context.name == '" + disciplineContextName + "']";
            List< NameValuePair > cb2QueryPayloadPim = new ArrayList< NameValuePair >();
            cb2QueryPayloadPim.add( new BasicNameValuePair( "type", "AssembleParams" ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "expr", assemblePramExp ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "f", "label" ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "f", "displayConfig.files" ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "format", "json" ) );

            log.info( "CB2 Query  :::::::::: " + cb2QueryPayloadPim );
            JSONObject jsonQueryResponseObjPim = CB2Client.PostCB2( userName, cb2QueryPayloadPim );

            String pimName = extractPimName( jsonQueryResponseObjPim );
            String pimOid = extractPimObjectId( jsonQueryResponseObjPim );
            String pimType = extractPimObjectType( jsonQueryResponseObjPim );

            // change this path later
            File file = new File( PropertiesManager.getKarafPath() + "/conf/Pim/" + pimName );
            if ( !file.exists() ) {
                downloadPimXmlByObjectId( userName, pimOid, file );
            }

            FileUtils.setGlobalAllFilePermissions( file );
            Map< String, Map< String, String > > map = getAllCb2AssemblyOptionFromPim( file.getAbsolutePath() );
            Map< String, String > pimMap = new HashMap< String, String >();
            pimMap.put( "pimType", pimType );
            map.put( "assembletPimType", pimMap );
            return map;
        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(),e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }*/

    private void downloadPimXmlByObjectId( String userName, String pimOid, File file )
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        // pimOid = "AAAAAQAjUjw:AAAAAQAjITM";
        String file_url = "/vault?oid=" + pimOid + "&expr=displayConfig.files" + ".data";
        CB2Client.GetCb2FileOrDownlaod( userName, file_url, file.getAbsolutePath() );
    }

    private InputStream getPimXmlByObjectId( String userName, String pimOid )
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        // pimOid = "AAAAAQAjUjw:AAAAAQAjITM";
        String file_url = "/vault?oid=" + pimOid + "&expr=displayConfig.files" + ".data";
        return CB2Client.GetCb2FileOrDownlaod( userName, file_url );
    }

    private File pimFilePath( String pimName ) {
        String fileFullPath = PropertiesManager.getKarafPath() + File.separator + "conf" + File.separator + "Pim";
        File dir = new File( fileFullPath );
        if ( !dir.exists() ) {
            dir.mkdirs();
        }
        return new File( fileFullPath + File.separator + pimName );
    }

    private String extractPimObjectId( JSONObject jsonQueryResponseObjPim ) {
        if ( jsonQueryResponseObjPim.has( "objects" ) ) {
            JSONArray objects = jsonQueryResponseObjPim.getJSONArray( "objects" );
            JSONObject subObjects = objects.getJSONObject( 0 );
            return String.valueOf( subObjects.get( "oid" ) );
        }
        return null;
    }

    private List< String > extractObjectIds( JSONObject jsonQueryResponseObjPim ) {
        List< String > objectIds = new ArrayList<>();
        if ( jsonQueryResponseObjPim.has( "objects" ) ) {
            JSONArray objects = jsonQueryResponseObjPim.getJSONArray( "objects" );
            for ( int i = 0; i < objects.length(); i++ ) {
                JSONObject subObjects = objects.getJSONObject( 0 );
                objectIds.add( String.valueOf( subObjects.get( "oid" ) ) );
            }

        }
        return objectIds;
    }

    private String extractDisciplineContextName( JSONObject jsonQueryResponseObj ) {
        if ( jsonQueryResponseObj.has( "objects" ) ) {
            JSONArray objects = jsonQueryResponseObj.getJSONArray( "objects" );
            for ( int i = 0; i < objects.length(); i++ ) {
                JSONObject subObjects = objects.getJSONObject( i );
                if ( subObjects.has( "attrs" ) ) {
                    JSONObject attrs = subObjects.getJSONObject( "attrs" );
                    JSONObject disciplineContext = attrs.getJSONObject( "disciplineContext.name" );
                    return String.valueOf( disciplineContext.get( "text" ) );
                }
            }
        }
        return null;
    }

    @Override
    public String getCb2RunVariantSolverDownloadPIM( String userName, String pimName, String pimOid ) {
        try {

            File file = pimFilePath( pimName );
            if ( !file.exists() ) {
                downloadPimXmlByObjectId( userName, pimOid, file );
            }
            FileUtils.setGlobalAllFilePermissions( file.toPath() );
            return file.getAbsolutePath();

        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    @Override
    public Map< String, String > getCb2RunVariantSolverParameters( String userName ) {
        try {
            // SolverParams query
            // String assemblePramExp = "[label == '*']";
            String assemblePramExp = "[name == '*' && context.name == 'Crash']";
            List< NameValuePair > cb2QueryPayloadPim = new ArrayList<>();
            cb2QueryPayloadPim.add( new BasicNameValuePair( "type", "SolverParams" ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "expr", assemblePramExp ) );
            cb2QueryPayloadPim.add( new BasicNameValuePair( "format", "json" ) );

            log.info( "CB2 Query  :::::::::: " + cb2QueryPayloadPim );
            JSONObject jsonQueryResponseObjPim = CB2Client.PostCB2( userName, cb2QueryPayloadPim );

            log.info( "" + jsonQueryResponseObjPim );

            return extractSolverParameters( jsonQueryResponseObjPim );

        } catch ( final Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

    private Map< String, String > extractSolverParameters( JSONObject jsonQueryResponseObjPim ) {
        Map< String, String > solverParamMap = new HashMap<>();
        if ( jsonQueryResponseObjPim.has( "objects" ) ) {
            JSONArray objects = jsonQueryResponseObjPim.getJSONArray( "objects" );
            for ( int i = 0; i < objects.length(); i++ ) {
                JSONObject subObjects = objects.getJSONObject( i );
                if ( subObjects.has( "oid" ) && subObjects.has( "type" ) ) {
                    solverParamMap.put( subObjects.get( "oid" ).toString(), subObjects.get( "type" ).toString() );
                }
            }
        }
        return solverParamMap;
    }

    /*
     * @Override public List< Map< String, String > > getcb2subModelsNames( String
     * reqJson, String userId ) throws IOException, NoSuchAlgorithmException,
     * KeyStoreException, KeyManagementException {
     *
     * UserDTO user = userCommonManager.getUserById( UUID.fromString( userId ) );
     * List< NameValuePair > requestPayloadLogin = new ArrayList< NameValuePair >( 2
     * );
     *
     * if ( user != null ) { requestPayloadLogin.add( new BasicNameValuePair(
     * "user", user.getUserUid() ) ); requestPayloadLogin.add( new
     * BasicNameValuePair( "passwd", permissionManager.getPasswordById( userId ) )
     * ); }
     *
     * List< Map< String, String > > result = new ArrayList<>(); if (
     * result.isEmpty() ) { Map< String, String > map = new HashMap<>(); map.put(
     * "oid", "b" ); map.put( "name", "b" ); map.put( "createdAt", "b" ); map.put(
     * "Modulelabel", "b" ); map.put( "variantlabel", "b" ); map.put(
     * "modelStatelabel", "b" ); map.put( "modelDefLabel", "b" ); map.put(
     * "description", "b" ); map.put( "owner", "b" ); map.put( "assembleType", "b"
     * ); map.put( "carProject", "b" ); result.add( map ); return result; } ///
     * #####################Temp for local // System.getProperties().put("proxySet",
     * "true"); // System.getProperties().put("socksProxyHost", "127.0.0.1"); //
     * System.getProperties().put("socksProxyPort", "8095"); ///
     * #####################Temp for local
     *
     * final SSLContextBuilder builder = new SSLContextBuilder();
     *
     * builder.loadTrustMaterial( null, new TrustSelfSignedStrategy() ); final
     * SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
     * builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );
     * CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(
     * sslsf ).build(); HttpHost targetHost = new HttpHost(
     * PropertiesManager.cb2Ip(), 443, "https" );
     *
     * // LOGIN try { HttpPost httpPostLogin = new HttpPost(
     * "/cb2/servlet/rest/login" );
     *
     * httpPostLogin.setEntity( new UrlEncodedFormEntity( requestPayloadLogin,
     * "UTF-8" ) ); CloseableHttpResponse responseLogin = httpclient.execute(
     * targetHost, httpPostLogin ); try { EntityUtils.consume(
     * responseLogin.getEntity() ); } finally { responseLogin.close(); } } finally {
     * // httpclient.close(); } // Cb2 Get SubModels try { JSONObject jsonReqObj =
     * new JSONObject( reqJson ); String subModelNames = jsonReqObj.getString(
     * "query" );
     *
     * HttpPost requestCb2Query = new HttpPost( "/cb2/servlet/rest/query" );
     *
     * List< NameValuePair > cb2QueryPayload = new ArrayList< NameValuePair >( 4 );
     * cb2QueryPayload.add( new BasicNameValuePair( "query", subModelNames ) );
     * cb2QueryPayload.add( new BasicNameValuePair( "f", "name" ) );
     * cb2QueryPayload.add( new BasicNameValuePair( "f", "createdAt" ) );
     * cb2QueryPayload.add( new BasicNameValuePair( "f", "assembleType" ) );
     * cb2QueryPayload.add( new BasicNameValuePair( "f", "carProject.name" ) );
     * cb2QueryPayload.add( new BasicNameValuePair( "f", "Module.label" ) );
     * cb2QueryPayload.add( new BasicNameValuePair( "f", "variant.label" ) );
     * cb2QueryPayload.add( new BasicNameValuePair( "f", "description" ) );
     * cb2QueryPayload.add( new BasicNameValuePair( "f", "modelDefLabel" ) );
     * cb2QueryPayload.add( new BasicNameValuePair( "f", "reference" ) );
     * cb2QueryPayload.add( new BasicNameValuePair( "f", "files" ) );
     * cb2QueryPayload.add( new BasicNameValuePair( "f", "owner" ) );
     * cb2QueryPayload.add( new BasicNameValuePair( "f", "modelState.label" ) );
     * cb2QueryPayload.add( new BasicNameValuePair( "pageSize", "1000" ) );
     * cb2QueryPayload.add( new BasicNameValuePair( "format", "json" ) );
     *
     * requestCb2Query.setEntity( new UrlEncodedFormEntity( cb2QueryPayload, "UTF-8"
     * ) ); CloseableHttpResponse cb2QueryResponse = httpclient.execute( targetHost,
     * requestCb2Query ); JSONObject jsonQueryResponseObj = new JSONObject(
     * EntityUtils.toString( cb2QueryResponse.getEntity() ) ); if (
     * jsonQueryResponseObj.has( "objects" ) ) { JSONArray objects =
     * jsonQueryResponseObj.getJSONArray( "objects" );
     *
     * for ( int i = 0; i < objects.length(); i++ ) { JSONObject subObjects =
     * objects.getJSONObject( i ); Map< String, String > subModles = new HashMap<
     * String, String >(); if ( subObjects.has( "attrs" ) ) { JSONObject attrs =
     * subObjects.getJSONObject( "attrs" ); if ( attrs.has( "name" ) ) { JSONObject
     * name = attrs.getJSONObject( "name" ); String subModelName = name.getString(
     * "text" ); subModles.put( "name", subModelName ); } if ( attrs.has(
     * "createdAt" ) ) { JSONObject createdAt = attrs.getJSONObject( "createdAt" );
     * String createdAtDate = createdAt.getString( "text" ); subModles.put(
     * "createdAt", createdAtDate ); } if ( attrs.has( "Module.label" ) ) {
     * JSONObject moduleLable = attrs.getJSONObject( "Module.label" ); String
     * moduleLableText = moduleLable.getString( "text" ); subModles.put(
     * "Modulelabel", moduleLableText ); } if ( attrs.has( "variant.label" ) ) {
     * JSONObject variantLabel = attrs.getJSONObject( "variant.label" ); String
     * variantLabelText = variantLabel.getString( "text" ); subModles.put(
     * "variantlabel", variantLabelText ); } if ( attrs.has( "modelState.label" ) )
     * { JSONObject modelStatelabel = attrs.getJSONObject( "modelState.label" );
     * String modelStatelabelText = modelStatelabel.getString( "text" );
     * subModles.put( "modelStatelabel", modelStatelabelText ); } if ( attrs.has(
     * "modelDefLabel" ) ) { JSONObject modelDefLabel = attrs.getJSONObject(
     * "modelDefLabel" ); String modelDefLabelText = modelDefLabel.getString( "text"
     * ); subModles.put( "modelDefLabel", modelDefLabelText ); }
     *
     * if ( attrs.has( "description" ) ) { JSONObject description =
     * attrs.getJSONObject( "description" ); String descriptionText = "N/A"; if (
     * description.has( "text" ) ) { descriptionText = description.getString( "text"
     * ); } subModles.put( "description", descriptionText ); } if ( attrs.has(
     * "owner" ) ) { JSONObject owner = attrs.getJSONObject( "owner" ); String
     * ownerName = owner.getString( "text" ); subModles.put( "owner", ownerName ); }
     * if ( attrs.has( "assembleType" ) ) { JSONObject assembleType =
     * attrs.getJSONObject( "assembleType" ); String subModelAssemblyType =
     * assembleType.getString( "text" ); subModles.put( "assembleType",
     * subModelAssemblyType ); } if ( attrs.has( "carProject.name" ) ) { JSONObject
     * carProject = attrs.getJSONObject( "carProject.name" ); String project =
     * carProject.getString( "text" ); subModles.put( "carProject", project ); }
     *
     * } if ( subObjects.has( "oid" ) ) { String oid = subObjects.getString( "oid"
     * ); subModles.put( "oid", oid );
     *
     * }
     *
     * result.add( subModles );
     *
     * }
     *
     * }
     *
     * } finally {
     *
     * } /// #####################Temp for local //
     * System.getProperties().put("proxySet", "false"); //
     * System.clearProperty("proxySet"); // System.clearProperty("socksProxyHost");
     * // System.clearProperty("socksProxyPort"); /// #####################Temp for
     * local return result;
     *
     * }
     */

    /*    public Map< String, Map< String, String > > getAllCb2AssemblyOptionFromPim( String filePimPath ) {
        Map< String, Map< String, String > > cb2Options = new HashMap< String, Map< String, String > >();
        try {
            // Document document = readXmlPimFile(filePimPath);
            Document document = null;
            String adapterversion = "adapterversion";
            String assemblyscript = "assemblyscript";
            String scriptversion = "scriptversion";

            Element classElement = document.getRootElement();
            List< Node > nodeList = classElement.content();
            for ( Node node : nodeList ) {
                if ( node.hasContent() ) {
                    Element element = ( Element ) node;
                    if ( element.attributeValue( "name" ).equalsIgnoreCase( adapterversion ) ) {
                        cb2Options.put( adapterversion, extractXmlField( element ) );
                    } else if ( element.attributeValue( "name" ).equalsIgnoreCase( assemblyscript ) ) {
                        cb2Options.put( assemblyscript, extractXmlField( element ) );
                    } else if ( element.attributeValue( "name" ).equalsIgnoreCase( scriptversion ) ) {
                        cb2Options.put( scriptversion, extractXmlField( element ) );
                    }
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return cb2Options;
    }*/

    public static Map< String, String > extractXmlField( Element element ) {
        Map< String, String > map = new HashMap<>();
        List< Node > fieldsList = element.content();
        for ( int i = 1; i < fieldsList.size(); i++ ) {
            Node field = fieldsList.get( i );
            try {
                Element elementField = ( Element ) field;
                map.put( elementField.attribute( "name" ).getText(),
                        ( elementField.attribute( "value" ) != null ? elementField.attribute( "value" ).getText() : "" ) );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }
            // i++;
        }
        return map;
    }

    private static Map< String, Map< String, String > > getAllPimXmlOptions( InputStream inputStream ) {
        try {
            Map< String, Map< String, String > > cb2Options = new HashMap<>();
            // filePimPath = "/sus/Cb2RunVariantSenarios/ABAQUS Explicit FG2.xml";

            SAXReader reader = new SAXReader();
            Document document = reader.read( inputStream );

            Element classElement = document.getRootElement();
            List< Node > nodeList = classElement.content();
            for ( Node node : nodeList ) {
                if ( node.hasContent() ) {
                    try {
                        Element element = ( Element ) node;
                        if ( element.getPath().equalsIgnoreCase( "/displayconfig/field" ) ) {
                            Map< String, String > map = new HashMap<>();
                            List< Node > fieldsList = element.content();
                            for ( int i = 1; i < fieldsList.size(); i++ ) {
                                Node field = fieldsList.get( i );
                                try {
                                    if ( field.getNodeType() == Node.ELEMENT_NODE ) {
                                        Element elementField = ( Element ) field;
                                        map.put( elementField.attribute( "name" ).getText(), ( elementField.attribute( "value" ) != null
                                                ? elementField.attribute( "value" ).getText()
                                                : "" ) );
                                    }
                                } catch ( Exception e ) {
                                    log.error( e.getMessage(), e );
                                }
                            }
                            if ( !map.isEmpty() ) {
                                cb2Options.put( element.attributeValue( "name" ), map );
                            }
                        }
                    } catch ( Exception e ) {
                        log.error( e.getMessage(), e );
                    }
                }
            }
            return cb2Options;

        } catch ( Exception e ) {
            log.error( "CB2 Error: " + e.getMessage(), e );
            try {
                inputStream.close();
            } catch ( IOException e1 ) {
                log.error( e1.getMessage(), e1 );
            }
            throw new SusException( "CB2 Error: " + e.getMessage() );
        }
    }

}