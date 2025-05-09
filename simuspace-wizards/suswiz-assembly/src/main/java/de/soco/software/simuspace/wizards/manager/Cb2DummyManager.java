/**
 *
 */

package de.soco.software.simuspace.wizards.manager;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchSubModelTableDTO;
import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchEntity;

/**
 * The Interface Cb2DummyManager.
 *
 * @author noman arshad
 */
public interface Cb2DummyManager {

    /**
     * Gets the list of cb 2 objects.
     *
     * @param reqJson
     *         the req json
     * @param userId
     *         the user id
     * @param bmwCaeBenchJsonType
     *         the bmw cae bench json type
     * @param filter
     *         the filter
     * @param node
     *         the node
     * @param userName
     *         the user name
     *
     * @return the list of cb 2 objects
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws NoSuchAlgorithmException
     *         the no such algorithm exception
     * @throws KeyStoreException
     *         the key store exception
     * @throws KeyManagementException
     *         the key management exception
     */
    String getListOfCb2Objects( String reqJson, String userId, String bmwCaeBenchJsonType, FiltersDTO filter, String node, String userName )
            throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException;

    /**
     * Gets list of cb 2 objects new.
     *
     * @param bmwCaeBenchJsonType
     *         the bmw cae bench json type
     * @param filter
     *         the filter
     * @param node
     *         the node
     * @param userName
     *         the username
     *
     * @return the list of cb 2 objects new
     */
    String getListOfCb2InputDecks( String userId, String userName, String node, String bmwCaeBenchJsonType, FiltersDTO filter );

    /**
     * Gets the variant sub models list of cb 2 objects.
     *
     * @param searchModel
     *         the search model
     * @param bmwCaeBenchJsonType
     *         the bmw cae bench json type
     * @param userName
     *         the user name
     * @param simdefOid
     *         the simdef oid
     *
     * @return the variant sub models list of cb 2 objects
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws NoSuchAlgorithmException
     *         the no such algorithm exception
     * @throws KeyStoreException
     *         the key store exception
     * @throws KeyManagementException
     *         the key management exception
     */
    List< BmwCaeBenchSubModelTableDTO > getVariantSubModelsListOfCb2Objects( String searchModel, String bmwCaeBenchJsonType,
            String userName, String simdefOid ) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException;

    /**
     * Gets the cb 2 objects.
     *
     * @param searchQuery
     *         the search query
     * @param bmwCAEBenchJsonType
     *         the bmw CAE bench json type
     * @param userName
     *         the user name
     *
     * @return the cb 2 objects
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws NoSuchAlgorithmException
     *         the no such algorithm exception
     * @throws KeyStoreException
     *         the key store exception
     * @throws KeyManagementException
     *         the key management exception
     */
    List< BmwCaeBenchEntity > getCb2Objects( String searchQuery, String bmwCAEBenchJsonType, String userName )
            throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException;

    /**
     * Gets senario list of cb 2 objects.
     *
     * @param userName
     *         the user name
     * @param selectedTreeObjName
     *         the selected tree obj name
     * @param filter
     *         the filter
     *
     * @return the senario list of cb 2 objects
     */
    String getSenarioListOfCb2Objects( String userName, String selectedTreeObjName, FiltersDTO filter );

    /**
     * Gets cb 2 run general project items drop down.
     *
     * @param userName
     *         the user name
     * @param selectedTreeObjName
     *         the selected tree obj name
     *
     * @return the cb 2 run general project items drop down
     */
    JSONObject getCb2RunGeneralProjectItemsDropDown( String userName, String selectedTreeObjName );

    /**
     * Gets cb 2 run variant project phase drop down.
     *
     * @param userName
     *         the user name
     *
     * @return the cb 2 run variant project phase drop down
     */
    JSONObject getCb2RunVariantProjectPhaseDropDown( String userName );

    /**
     * Gets cb 2 run variant solver parameters.
     *
     * @param userName
     *         the user name
     *
     * @return the cb 2 run variant solver parameters
     */
    Map< String, String > getCb2RunVariantSolverParameters( String userName );

    /**
     * Gets cb 2 run variant solver download pim.
     *
     * @param userName
     *         the user name
     * @param pimName
     *         the pim name
     * @param pimOid
     *         the pim oid
     *
     * @return the cb 2 run variant solver download pim
     */
    String getCb2RunVariantSolverDownloadPIM( String userName, String pimName, String pimOid );

    /**
     * Gets cb 2 run general tab object type drop down.
     *
     * @param userName
     *         the user name
     * @param disciplineContextName
     *         the discipline context name
     *
     * @return the cb 2 run general tab object type drop down
     */
    JSONObject getCb2RunGeneralTabObjectTypeDropDown( String userName, String disciplineContextName );

    /**
     * Gets cb 2 run variant assemble app param.
     *
     * @param userName
     *         the user name
     * @param projectPath
     *         the project path
     *
     * @return the cb 2 run variant assemble app param
     */
    String getCb2RunVariantAssembleAppParam( String userName, String projectPath );

    /**
     * Gets cb 2 run variant solve app param.
     *
     * @param userName
     *         the user name
     * @param projectPath
     *         the project path
     *
     * @return the cb 2 run variant solve app param
     */
    String getCb2RunVariantSolveAppParam( String userName, String projectPath );

    /**
     * Gets cb 2 run variant post processing app param.
     *
     * @param userName
     *         the user name
     * @param projectPath
     *         the project path
     *
     * @return the cb 2 run variant post processing app param
     */
    String getCb2RunVariantPostProcessingAppParam( String userName, String projectPath );

    /**
     * Gets cb 2 study defs by variant.
     *
     * @param userName
     *         the user name
     * @param variantOid
     *         the variant oid
     *
     * @return the cb 2 study defs by variant
     */
    List< Map< String, String > > getCb2StudyDefsByVariant( String userName, String variantOid );

    /**
     * Gets cb 2 scenarios by variant.
     *
     * @param userName
     *         the user name
     * @param variantOid
     *         the variant oid
     *
     * @return the cb 2 scenarios by variant
     */
    List< Map< String, String > > getCb2ScenariosByVariant( String userName, String variantOid );

    /**
     * Gets cb 2 setup by study def.
     *
     * @param userName
     *         the user name
     * @param assembleSolvePost
     *         the assemble solve post
     * @param studydefOid
     *         the studydef oid
     *
     * @return the cb 2 setup by study def
     */
    List< Map< String, String > > getCb2SetupByStudyDef( String userName, String assembleSolvePost, String studydefOid );

    /**
     * Download pim by oid and read pim map.
     *
     * @param userName
     *         the user name
     * @param jsonQueryResponseObjPim
     *         the json query response obj pim
     *
     * @return the map
     */
    Map< String, Map< String, String > > downloadPimByOidAndReadPim( String userName, JSONObject jsonQueryResponseObjPim );

    /**
     * Gets cb 2 run variant post form xml.
     *
     * @param userName
     *         the user name
     * @param simDefObjectId
     *         the sim def object id
     *
     * @return the cb 2 run variant post form xml
     */
    JSONObject getCb2RunVariantPostFormXML( String userName, String simDefObjectId );

    /**
     * Gets cb 2 run variant assemble form xml.
     *
     * @param userName
     *         the user name
     * @param simDefObjectId
     *         the sim def object id
     *
     * @return the cb 2 run variant assemble form xml
     */
    JSONObject getCb2RunVariantAssembleFormXML( String userName, String simDefObjectId );

    /**
     * Gets cb 2 run variant solver form xml.
     *
     * @param userName
     *         the user name
     * @param simDefObjectId
     *         the sim def object id
     *
     * @return the cb 2 run variant solver form xml
     */
    JSONObject getCb2RunVariantSolverFormXML( String userName, String simDefObjectId );

    /**
     * Gets cb 2 sim defs by scenario.
     *
     * @param userName
     *         the user name
     * @param variantAndScenarioOid
     *         the variant and scenario oid
     *
     * @return the cb 2 sim defs by scenario
     */
    List< Map< String, String > > getCb2SimDefsByScenario( String userName, String variantAndScenarioOid );

}