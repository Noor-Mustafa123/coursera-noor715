package de.soco.software.simuspace.wizards.manager;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.cb2.model.BmwCaeBenchScenerioTreeDTO;
import de.soco.software.simuspace.suscore.common.cb2.model.BmwCaeBenchTreeDTO;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchDTO;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchSubModelTableDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.data.common.model.Cb2VariantWizardDTO;
import de.soco.software.simuspace.suscore.data.entity.VariantWizardEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.LoadCaseDTO;
import de.soco.software.simuspace.wizards.model.DummyVariantDTO;
import de.soco.software.simuspace.wizards.model.VariantWizardDTO;

/**
 * The Interface WizardsManager.
 */
public interface WizardsManager {

    /**
     * Gets the tabs UI.
     *
     * @param userId
     *         the user id
     * @param id
     *         the id
     *
     * @return the tabs UI
     *
     * @apiNote To be used in service calls only
     */
    SubTabsItem getTabsUI( String userId, String id );

    /**
     * Gets the general tab UI.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     *
     * @return the general tab UI
     *
     * @apiNote To be used in service calls only
     */
    UIForm getGeneralTabUI( String userId, String objectId );

    /**
     * Gets the variant.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     *
     * @return the variant
     *
     * @apiNote To be used in service calls only
     */
    VariantWizardDTO getVariant( String userIdFromGeneralHeader, String id );

    /**
     * Save variant.
     *
     * @param token
     *         the token
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     * @param variantWizardDTO
     *         the variant wizard DTO
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object saveVariant( String token, String userIdFromGeneralHeader, String id, VariantWizardDTO variantWizardDTO );

    /**
     * Gets the objects tab UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     *
     * @return the objects tab UI
     *
     * @apiNote To be used in service calls only
     */
    List< TableColumn > getObjectsTabUI( String userIdFromGeneralHeader, String id );

    /**
     * Gets the objects tab data.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     * @param jsonToObject
     *         the json to object
     *
     * @return the objects tab data
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Object > getObjectsTabData( String userIdFromGeneralHeader, String id, FiltersDTO jsonToObject );

    /**
     * Gets the assembly tab UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     *
     * @return the assembly tab UI
     *
     * @apiNote To be used in service calls only
     */
    UIForm getAssemblyTabUI( String userIdFromGeneralHeader, String id );

    /**
     * Gets the solver tab UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     *
     * @return the solver tab UI
     *
     * @apiNote To be used in service calls only
     */
    UIForm getSolverTabUI( String userIdFromGeneralHeader, String id );

    /**
     * Gets the post tab UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     *
     * @return the post tab UI
     *
     * @apiNote To be used in service calls only
     */
    UIForm getPostTabUI( String userIdFromGeneralHeader, String id );

    /**
     * Gets the workflow run fields.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param workflowId
     *         the workflow id
     *
     * @return the workflow run fields
     *
     * @apiNote To be used in service calls only
     */
    UIForm getWorkflowRunFields( String userIdFromGeneralHeader, String workflowId );

    /**
     * Save objects order.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     * @param jsonToList
     *         the json to list
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean saveObjectsOrder( String userIdFromGeneralHeader, String id, List< UUID > jsonToList );

    /**
     * Gets the submit tab UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     *
     * @return the submit tab UI
     */
    List< TableColumn > getSubmitTabUI( String userIdFromGeneralHeader, String id );

    /**
     * Gets the submit tab data.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     * @param jsonToObject
     *         the json to object
     *
     * @return the submit tab data
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Object > getSubmitTabData( String userIdFromGeneralHeader, String id, FiltersDTO jsonToObject );

    /**
     * Change object to loadcase relation.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param variantId
     *         the variant id
     * @param objectId
     *         the object id
     * @param loadcaseId
     *         the loadcase id
     * @param value
     *         the value
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean changeObjectToLoadcaseRelation( String userIdFromGeneralHeader, String variantId, String objectId, UUID loadcaseId,
            Integer value );

    /**
     * Change loadcase submit.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param variantId
     *         the variant id
     * @param key
     *         the key
     * @param loadcaseId
     *         the loadcase id
     * @param value
     *         the value
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean changeLoadcaseSubmit( String userIdFromGeneralHeader, String variantId, String key, String loadcaseId, Integer value );

    /**
     * Run variant.
     *
     * @param token
     *         the token
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param uid
     *         the uid
     * @param id
     *         the id
     * @param variantWizardDTO
     *         the variant wizard DTO
     * @param json
     *         the json
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object runVariant( String token, String userIdFromGeneralHeader, String uid, String id, VariantWizardDTO variantWizardDTO,
            String json );

    /**
     * Gets the copy vairnat UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param refId
     *         the ref id
     *
     * @return the copy vairnat UI
     *
     * @apiNote To be used in service calls only
     */
    UIForm getCopyVairnatUI( String userIdFromGeneralHeader, String refId );

    /**
     * Creates the vairnat copy.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param refId
     *         the ref id
     * @param variantWizardDTO
     *         the variant wizard DTO
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object createVariantCopy( String userIdFromGeneralHeader, String refId, VariantWizardDTO variantWizardDTO );

    /**
     * Gets the dummy option form.
     *
     * @param userId
     *         the user id
     * @param variantId
     *         the variant id
     * @param referenceId
     *         the reference id
     * @param dummyVariant
     *         the dummy variant
     * @param solverType
     *         the solver type
     * @param toExecuteSolverType
     *         the to execute solver type
     * @param variantReference
     *         the variant reference
     *
     * @return the dummy option form
     *
     * @apiNote To be used in service calls only
     */
    UIForm getDummyOptionFormForReferenceSelection( String userId, String variantId, String referenceId, DummyVariantDTO dummyVariant,
            String solverType, String toExecuteSolverType, String variantReference );

    /**
     * Gets the dummy files UI.
     *
     * @return the dummy files UI
     */
    List< TableColumn > getDummyFilesUI();

    /**
     * Gets the sorted selection.
     *
     * @param sId
     *         the s id
     * @param external
     *         the external
     *
     * @return the sorted selection
     *
     * @apiNote To be used in service calls only
     */
    List< BmwCaeBenchDTO > getSortedSelection( String sId, String external );

    /**
     * Gets the load case selected dummy type list.
     *
     * @param userId
     *         the user id
     * @param dummyType
     *         the dummy type
     * @param solverType
     *         the solver type
     * @param filter
     *         the filter
     *
     * @return the load case selected dummy type list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< LoadCaseDTO > getLoadCaseSelectedDummyTypeList( String userId, String dummyType, String solverType,
            FiltersDTO filter );

    /**
     * Gets the dummy type form.
     *
     * @param userId
     *         the user id
     * @param solverType
     *         the solver type
     * @param dummyType
     *         the dummy type
     * @param refVariant
     *         the ref variant
     *
     * @return the dummy type form
     *
     * @apiNote To be used in service calls only
     */
    UIForm getDummyTypeForm( String userId, String solverType, String dummyType, String refVariant );

    /**
     * Gets the dummy type form.
     *
     * @param userId
     *         the user id
     * @param solverType
     *         the solver type
     * @param dummyType
     *         the dummy type
     * @param refVariant
     *         the ref variant
     * @param referenceId
     *         the reference id
     *
     * @return the dummy type form
     *
     * @apiNote To be used in service calls only
     */
    UIForm getDummyTypeFormForReferenceSelection( String userId, String solverType, String dummyType, String refVariant,
            String referenceId );

    /**
     * Gets the dummy load case form.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param loadCaseId
     *         the load case id
     *
     * @return the dummy load case form
     *
     * @apiNote To be used in service calls only
     */
    UIForm getDummyLoadCaseForm( String userIdFromGeneralHeader, UUID loadCaseId );

    /**
     * Creates the and run dummy variant.
     *
     * @param token
     *         the token
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param uid
     *         the uid
     * @param parentId
     *         the parent id
     * @param json
     *         the json
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object createAndRunDummyVariant( String token, String userIdFromGeneralHeader, String uid, String parentId, String json );

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    ObjectViewManager getObjectViewManager();

    /**
     * Gets the load case selection.
     *
     * @param selectionId
     *         the selection id
     * @param filterJson
     *         the filter json
     *
     * @return the load case selection
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< LoadCaseDTO > getLoadCaseSelection( String selectionId, String filterJson );

    /**
     * Gets the dummy refrence option form.
     *
     * @param id
     *         the id
     *
     * @return the dummy refrence option form
     */
    UIForm getDummyRefrenceOptionForm( String id );

    /**
     * Gets the dummy refrence variant form.
     *
     * @param optionId
     *         the option id
     * @param variantId
     *         the variant id
     *
     * @return the dummy refrence variant form
     */
    UIForm getDummyRefrenceVariantForm( String optionId, String variantId );

    /**
     * Gets the dummy refrence variant selection form.
     *
     * @param userId
     *         the user id from general header
     * @param variantId
     *         the variantid
     * @param solverType
     *         the solver type
     * @param optionId
     *         the option id
     *
     * @return the dummy refrence variant selection form
     *
     * @apiNote To be used in service calls only
     */
    UIForm getDummyRefrenceVariantSelectionForm( String userId, String variantId, String solverType, String optionId );

    /**
     * Gets the bmw cae bench tree data.
     *
     * @param userId
     *         the user id
     *
     * @return the bmw cae bench tree data
     */
    BmwCaeBenchTreeDTO getBmwCaeBenchTreeData( String userId );

    /**
     * Gets the bmw cae bench object selection.
     *
     * @param selectionId
     *         the selection id
     * @param filterJson
     *         the filter json
     * @param external
     *         the external
     *
     * @return the bmw cae bench object selection
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< BmwCaeBenchDTO > getBmwCaeBenchObjectSelection( String selectionId, String filterJson, String external );

    /**
     * Gets the bmw cae bench sorted selection.
     *
     * @param sId
     *         the s id
     *
     * @return the bmw cae bench sorted selection
     *
     * @apiNote To be used in service calls only
     */
    List< BmwCaeBenchDTO > getBmwCaeBenchSortedSelection( String sId );

    /**
     * Gets the bmw cae bench object list from cb 2.
     *
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param token
     *         the token
     * @param node
     *         the node
     * @param filter
     *         the filter
     * @param caeBenchType
     *         the cae bench type
     * @param solverType
     *         the solver type
     *
     * @return the bmw cae bench object list from cb 2
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< ? > getBmwCaeBenchObjectListFromCB2( String userId, String userName, String token, String node, FiltersDTO filter,
            String caeBenchType, String solverType );

    /**
     * Gets bmw cae bench object new.
     *
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param token
     *         the token
     * @param node
     *         the node
     * @param filter
     *         the filter
     * @param caeBenchType
     *         the cae bench type
     * @param solverType
     *         the solver type
     *
     * @return the bmw cae bench object new
     */
    List< BmwCaeBenchDTO > getBmwCaeBenchInputDeckForQADyna( String userId, String userName, String token, FiltersDTO filter,
            String caeBenchType );

    /**
     * Gets the bmw cae bench object selection with attributes.
     *
     * @param selectionId
     *         the selection id
     * @param filterJson
     *         the filter json
     * @param externalFlag
     *         the external flag
     *
     * @return the bmw cae bench object selection with attributes
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< ? > getBmwCaeBenchObjectSelectionWithAttributes( String selectionId, String filterJson, String externalFlag );

    /**
     * Update selection view.
     *
     * @param userId
     *         the user id
     * @param selectionId
     *         the selection id
     * @param updateColumns
     *         the update columns
     *
     * @return the object view DTO
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO updateSelectionView( String userId, String selectionId, List< TableColumn > updateColumns );

    /**
     * Apply saved view in ui.
     *
     * @param selectionId
     *         the selectionId
     * @param listColumns
     *         the listColumns
     *
     * @return the list with views applied
     *
     * @apiNote To be used in service calls only
     */
    List< TableColumn > applyViewToSelectionColumns( String selectionId, List< TableColumn > listColumns );

    /**
     * Gets the bmw cae bench variant sub model object list from CB 2.
     *
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param token
     *         the token
     * @param selectionId
     *         the selection id
     * @param caeBenchType
     *         the cae bench type
     * @param simdefOid
     *         the simdef oid
     *
     * @return the bmw cae bench variant sub model object list from CB 2
     *
     * @apiNote To be used in service calls only
     */
    List< BmwCaeBenchSubModelTableDTO > getBmwCaeBenchVariantSubModelObjectListFromCB2( String userId, String userName, String token,
            String selectionId, String caeBenchType, String simdefOid );

    /**
     * Adds the bmw cae bench variant sub model form UI.
     *
     * @param userId
     *         the user id
     * @param selectionId
     *         the selection id
     * @param userName
     *         the user name
     * @param simdefOid
     *         the simdef oid
     *
     * @return the list
     */
    UIForm addBmwCaeBenchVariantSubModelFormUI( String userId, String selectionId, String userName, String simdefOid );

    /**
     * Gets the parser object selected UI for CB 2 element.
     *
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param selectionId
     *         the selection id
     * @param dropDown
     *         the drop down
     * @param auth
     *         the auth
     * @param simdefOid
     *         the simdef oid
     *
     * @return the parser object selected UI for CB 2 element
     *
     * @apiNote To be used in service calls only
     */
    Object getSubModelTypeOptions( String userId, String userName, String selectionId, String dropDown, String auth, String simdefOid );

    /**
     * Gets project phase options.
     *
     * @param userName
     *         the user name
     * @param projectPath
     *         the project path
     *
     * @return the project phase options
     */
    List< SelectOptionsUI > getProjectPhaseOptions( String userName, String projectPath );

    /**
     * Gets project item options.
     *
     * @param userName
     *         the user name
     * @param projectPath
     *         the project path
     *
     * @return the project item options
     */
    List< SelectOptionsUI > getProjectItemOptions( String userName, String projectPath );

    /**
     * Gets bmw cae bench objet by selection.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the bmw cae bench objet by selection
     *
     * @apiNote To be used in service calls only
     */
    List< BmwCaeBenchDTO > getBmwCaeBenchObjetBySelection( String selectionId );

    /**
     * Gets dummy refrence cb 2 option form.
     *
     * @param variantId
     *         the variant id
     *
     * @return the dummy refrence cb 2 option form
     */
    UIForm getDummyRefrenceCb2OptionForm( String variantId );

    /**
     * Run cb 2 variant object.
     *
     * @param token
     *         the token
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param uid
     *         the uid
     * @param id
     *         the id
     * @param variantWizardDTO
     *         the variant wizard dto
     * @param json
     *         the json
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object runCb2Variant( String token, String userIdFromGeneralHeader, String uid, String id, Cb2VariantWizardDTO variantWizardDTO,
            String json );

    /**
     * Read scenerio tree json file bmw cae bench scenerio tree dto.
     *
     * @return the bmw cae bench scenerio tree dto
     */
    BmwCaeBenchScenerioTreeDTO readScenerioTreeJsonFile();

    /**
     * Gets cb 2 objects tab data.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     * @param jsonToObject
     *         the json to object
     *
     * @return the cb 2 objects tab data
     *
     * @apiNote To be used in service calls only
     */
    Object getCb2ObjectsTabData( String userIdFromGeneralHeader, String id, FiltersDTO jsonToObject );

    /**
     * Gets cb 2 submit tab data.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     * @param jsonToObject
     *         the json to object
     *
     * @return the cb 2 submit tab data
     *
     * @apiNote To be used in service calls only
     */
    Object getCb2SubmitTabData( String userIdFromGeneralHeader, String id, FiltersDTO jsonToObject );

    /**
     * Gets cb 2 assembly tab ui.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param userName
     *         the user name
     * @param id
     *         the id
     *
     * @return the cb 2 assembly tab ui
     *
     * @apiNote To be used in service calls only
     */
    Object getCb2AssemblyTabUI( String userIdFromGeneralHeader, String userName, String id );

    /**
     * Gets cb 2 solver tab ui.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param userName
     *         the user name
     * @param id
     *         the id
     *
     * @return the cb 2 solver tab ui
     *
     * @apiNote To be used in service calls only
     */
    Object getCb2SolverTabUI( String userIdFromGeneralHeader, String userName, String id );

    /**
     * Gets cb 2 post tab ui.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     * @param userName
     *         the user name
     *
     * @return the cb 2 post tab ui
     *
     * @apiNote To be used in service calls only
     */
    Object getCb2PostTabUI( String userIdFromGeneralHeader, String id, String userName );

    /**
     * Gets cb 2 objects tab ui.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     *
     * @return the cb 2 objects tab ui
     *
     * @apiNote To be used in service calls only
     */
    List< TableColumn > getCb2ObjectsTabUI( String userIdFromGeneralHeader, String id );

    /**
     * Gets cb 2 general tab ui.
     *
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param objectId
     *         the object id
     *
     * @return the cb 2 general tab ui
     *
     * @apiNote To be used in service calls only
     */
    UIForm getCb2GeneralTabUI( String userId, String userName, String objectId );

    /**
     * Gets cb 2 general drop down.
     *
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param objectId
     *         the object id
     * @param id
     *         the id
     *
     * @return the cb 2 general drop down
     *
     * @apiNote To be used in service calls only
     */
    UIForm getCb2GeneralDropDown( String userId, String userName, String objectId, String id );

    /**
     * Save cb 2 variant object.
     *
     * @param token
     *         the token
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     * @param variantWizardDTO
     *         the variant wizard dto
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object saveCb2Variant( String token, String userIdFromGeneralHeader, String id, Cb2VariantWizardDTO variantWizardDTO );

    /**
     * Gets cb 2 variant.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     *
     * @return the cb 2 variant
     *
     * @apiNote To be used in service calls only
     */
    Cb2VariantWizardDTO getCb2Variant( String userIdFromGeneralHeader, String id );

    /**
     * Gets cb 2 general definition drop down.
     *
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param varId
     *         the var id
     * @param sid
     *         the sid
     * @param itemId
     *         the item id
     *
     * @return the cb 2 general definition drop down
     *
     * @apiNote To be used in service calls only
     */
    UIForm getCb2GeneralDefinitionDropDown( String userId, String userName, String varId, String sid, String itemId );

    /**
     * Gets cb 2 study defs by variant.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param userNameFromGeneralHeader
     *         the user name from general header
     * @param variantSId
     *         the variant s id
     *
     * @return the cb 2 study defs by variant
     *
     * @apiNote To be used in service calls only
     */
    List< Map< String, String > > getCb2StudyDefsByVariant( String userIdFromGeneralHeader, String userNameFromGeneralHeader,
            String variantSId );

    /**
     * Gets cb 2 params by setup.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param userNameFromGeneralHeader
     *         the user name from general header
     * @param assembleSolvePost
     *         the assemble solve post
     * @param dynamicValKeyName
     *         the dynamic val key name
     * @param setupOid
     *         the setup oid
     *
     * @return the cb 2 params by setup
     */
    Object getCb2ParamsBySetup( String userIdFromGeneralHeader, String userNameFromGeneralHeader, String assembleSolvePost,
            String dynamicValKeyName, String setupOid );

    /**
     * Gets cb 2 scenarios by variant.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param userNameFromGeneralHeader
     *         the user name from general header
     * @param variantSId
     *         the variant s id
     *
     * @return the cb 2 scenarios by variant
     *
     * @apiNote To be used in service calls only
     */
    List< Map< String, String > > getCb2ScenariosByVariant( String userIdFromGeneralHeader, String userNameFromGeneralHeader,
            String variantSId );

    /**
     * Gets cb 2 setup by study def.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param userNameFromGeneralHeader
     *         the user name from general header
     * @param assembleSolvePost
     *         the assemble solve post
     * @param studydefOid
     *         the studydef oid
     *
     * @return the cb 2 setup by study def
     */
    List< Map< String, String > > getCb2SetupByStudyDef( String userIdFromGeneralHeader, String userNameFromGeneralHeader,
            String assembleSolvePost, String studydefOid );

    /**
     * Gets cb 2 sim defs by scenario.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param userNameFromGeneralHeader
     *         the user name from general header
     * @param variantAndScenarioOid
     *         the variant and scenario oid
     *
     * @return the cb 2 sim defs by scenario
     */
    List< Map< String, String > > getCb2SimDefsByScenario( String userIdFromGeneralHeader, String userNameFromGeneralHeader,
            String variantAndScenarioOid );

    /**
     * Gets generic dto list by selection.
     *
     * @param selectionId
     *         the selection id
     * @param filter
     *         the filter
     *
     * @return the generic dto list by selection
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< ? > getGenericDTOListBySelection( String userId, String selectionId, FiltersDTO filter );

    /**
     * Check if feature allowed to user.
     *
     * @param feature
     *         the feature
     *
     * @return true, if is feature allowed to user
     *
     * @apiNote To be used in service calls only
     */
    boolean checkIfLicenseFeatureAllowedToUser( String feature );

    /**
     * Gets the selected id list by selection id.
     *
     * @param sId
     *         the s id
     *
     * @return the selected id list by selection id
     *
     * @apiNote To be used in service calls only
     */
    List< UUID > getSelectedIdsListBySelectionId( String sId );

    /**
     * Gets variant wizard entity by id.
     *
     * @param id
     *         the id
     *
     * @return the variant wizard entity by id
     *
     * @apiNote To be used in service calls only
     */
    VariantWizardEntity getVariantWizardEntityById( UUID id );

}
