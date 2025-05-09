/*
 *
 */

package de.soco.software.simuspace.wizards.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.enums.BmwCaeBenchEnums;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchSubModelDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.common.model.Cb2VariantWizardDTO;
import de.soco.software.simuspace.suscore.data.entity.VariantWizardEntity;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.wizards.manager.Cb2DummyManager;
import de.soco.software.simuspace.wizards.manager.LoadcaseManager;
import de.soco.software.simuspace.wizards.manager.WizardsManager;
import de.soco.software.simuspace.wizards.model.DummyVariantDTO;
import de.soco.software.simuspace.wizards.model.VariantWizardDTO;
import de.soco.software.simuspace.wizards.service.rest.WizardsService;

/**
 * The service class for object tree providing API's like getObjectTree and filterObjectTree.
 */
public class WizardsServiceImpl extends SuSBaseService implements WizardsService {

    private WizardsManager wizardsManager;

    private Cb2DummyManager cb2DummyManager;

    private LoadcaseManager loadcaseManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getTabsUI( String id ) {
        try {

            return ResponseUtils.success( wizardsManager.getTabsUI( getUserIdFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2TabsUI( String id ) {
        try {

            return ResponseUtils.success( wizardsManager.getTabsUI( getUserIdFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
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
     * Gets the cb 2 dummy manager.
     *
     * @return the cb 2 dummy manager
     */
    public Cb2DummyManager getCb2DummyManager() {
        return cb2DummyManager;
    }

    /**
     * Sets the cb 2 dummy manager.
     *
     * @param cb2DummyManager
     *         the new cb 2 dummy manager
     */
    public void setCb2DummyManager( Cb2DummyManager cb2DummyManager ) {
        this.cb2DummyManager = cb2DummyManager;
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
     * Gets the general tab UI.
     *
     * @param id
     *         the id
     *
     * @return the general tab UI
     */
    @Override
    public Response getGeneralTabUI( String id ) {
        try {

            return ResponseUtils.success( wizardsManager.getGeneralTabUI( getUserIdFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2GeneralTabUI( String id ) {
        try {
            return ResponseUtils
                    .success( wizardsManager.getCb2GeneralTabUI( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the variant.
     *
     * @param id
     *         the id
     *
     * @return the variant
     */
    @Override
    public Response getVariant( String id ) {
        try {

            return ResponseUtils.success( wizardsManager.getVariant( getUserIdFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2Variant( String id ) {
        try {

            return ResponseUtils.success( wizardsManager.getCb2Variant( getUserIdFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Save variant.
     *
     * @param id
     *         the id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @Override
    public Response saveVariant( String id, String payload ) {
        try {

            return ResponseUtils.success( "Saved successfully", wizardsManager.saveVariant( getTokenFromGeneralHeader(),
                    getUserIdFromGeneralHeader(), id, JsonUtils.jsonToObject( payload, VariantWizardDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response saveCb2Variant( String id, String payload ) {
        try {
            Cb2VariantWizardDTO obj = JsonUtils.jsonToObject( payload, Cb2VariantWizardDTO.class );
            return ResponseUtils.success( "Saved successfully",
                    wizardsManager.saveCb2Variant( getTokenFromGeneralHeader(), getUserIdFromGeneralHeader(), id, obj ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Run variant.
     *
     * @param id
     *         the id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @Override
    public Response runVariant( String id, String payload ) {
        try {
            return ResponseUtils.success( "Submitted successfully",
                    wizardsManager.runVariant( getTokenFromGeneralHeader(), getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(),
                            id, JsonUtils.jsonToObject( payload, VariantWizardDTO.class ), payload ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response runCb2Variant( String variantId, String payload ) {
        try {

            return ResponseUtils.success( "Submitted successfully",
                    wizardsManager.runCb2Variant( getTokenFromGeneralHeader(), getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(),
                            variantId, JsonUtils.jsonToObject( payload, Cb2VariantWizardDTO.class ), payload ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the objects tab UI.
     *
     * @param id
     *         the id
     *
     * @return the objects tab UI
     */
    @Override
    public Response getObjectsTabUI( String id ) {
        try {
            return ResponseUtils.success( new TableUI( wizardsManager.getObjectsTabUI( getUserIdFromGeneralHeader(), id ),
                    wizardsManager.getObjectViewManager().getUserObjectViewsByKey( ConstantsObjectViewKey.RUN_VARIANT_OBJECTS_TABLE_KEY,
                            getUserIdFromGeneralHeader(), null ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2ObjectsTabUI( String id ) {
        try {

            return ResponseUtils.success( new TableUI( wizardsManager.getCb2ObjectsTabUI( getUserIdFromGeneralHeader(), id ),
                    wizardsManager.getObjectViewManager().getUserObjectViewsByKey( ConstantsObjectViewKey.RUN_VARIANT_OBJECTS_TABLE_KEY,
                            getUserIdFromGeneralHeader(), null ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the objects tab data.
     *
     * @param id
     *         the id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the objects tab data
     */
    @Override
    public Response getObjectsTabData( String id, String objectFilterJson ) {
        try {

            return ResponseUtils.success( wizardsManager.getObjectsTabData( getUserIdFromGeneralHeader(), id,
                    JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2ObjectsTabData( String id, String objectFilterJson ) {
        try {

            return ResponseUtils.success( wizardsManager.getCb2ObjectsTabData( getUserIdFromGeneralHeader(), id,
                    JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Save objects order.
     *
     * @param id
     *         the id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @Override
    public Response saveObjectsOrder( String id, String payload ) {
        try {

            return ResponseUtils.success(
                    wizardsManager.saveObjectsOrder( getUserIdFromGeneralHeader(), id, JsonUtils.jsonToList( payload, UUID.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /**
     * Gets the assembly tab UI.
     *
     * @param id
     *         the id
     *
     * @return the assembly tab UI
     */
    @Override
    public Response getAssemblyTabUI( String id ) {
        try {
            return ResponseUtils.success( wizardsManager.getAssemblyTabUI( getUserIdFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2AssemblyTabUI( String id ) {
        try {
            return ResponseUtils
                    .success( wizardsManager.getCb2AssemblyTabUI( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the solver tab UI.
     *
     * @param id
     *         the id
     *
     * @return the solver tab UI
     */
    @Override
    public Response getSolverTabUI( String id ) {
        try {
            return ResponseUtils.success( wizardsManager.getSolverTabUI( getUserIdFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2SolverTabUI( String id ) {
        try {
            return ResponseUtils
                    .success( wizardsManager.getCb2SolverTabUI( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the post tab UI.
     *
     * @param id
     *         the id
     *
     * @return the post tab UI
     */
    @Override
    public Response getPostTabUI( String id ) {
        try {
            return ResponseUtils.success( wizardsManager.getPostTabUI( getUserIdFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2PostTabUI( String id ) {
        try {
            return ResponseUtils
                    .success( wizardsManager.getCb2PostTabUI( getUserIdFromGeneralHeader(), id, getUserNameFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the submit tab UI.
     *
     * @param id
     *         the id
     *
     * @return the submit tab UI
     */
    @Override
    public Response getSubmitTabUI( String id ) {
        try {

            return ResponseUtils.success( new TableUI( wizardsManager.getSubmitTabUI( getUserIdFromGeneralHeader(), id ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2SubmitTabUI( String id ) {
        try {

            return ResponseUtils.success( new TableUI( wizardsManager.getSubmitTabUI( getUserIdFromGeneralHeader(), id ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the workflow fields.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the workflow fields
     */
    @Override
    public Response getWorkflowFields( String workflowId ) {
        try {
            return ResponseUtils.success( wizardsManager.getWorkflowRunFields( getUserIdFromGeneralHeader(), workflowId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2WorkflowFields( String workflowId ) {
        try {
            return ResponseUtils.success( wizardsManager.getWorkflowRunFields( getUserIdFromGeneralHeader(), workflowId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the submit tab data.
     *
     * @param id
     *         the id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the submit tab data
     */
    @Override
    public Response getSubmitTabData( String id, String objectFilterJson ) {
        try {

            return ResponseUtils.success( wizardsManager.getSubmitTabData( getUserIdFromGeneralHeader(), id,
                    JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2SubmitTabData( String id, String objectFilterJson ) {
        try {

            return ResponseUtils.success( wizardsManager.getCb2SubmitTabData( getUserIdFromGeneralHeader(), id,
                    JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Change object to loadcase relation.
     *
     * @param variantId
     *         the variant id
     * @param objectId
     *         the object id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @Override
    public Response changeObjectToLoadcaseRelation( String variantId, String objectId, String payload ) {
        boolean isSuccess = false;
        try {
            Map< String, Object > checkBoxStateMap = new HashMap<>();
            checkBoxStateMap = ( Map< String, Object > ) JsonUtils.jsonToMap( payload, checkBoxStateMap );
            for ( Entry< String, Object > entry : checkBoxStateMap.entrySet() ) {
                String[] split = entry.getKey().split( "\\." );
                UUID loadcaseId = UUID.fromString( split[ 1 ] );
                Integer value = Integer.valueOf( entry.getValue().toString() );

                isSuccess = wizardsManager.changeObjectToLoadcaseRelation( getUserIdFromGeneralHeader(), variantId, objectId, loadcaseId,
                        value );
            }

            if ( isSuccess ) {
                return ResponseUtils.success( isSuccess );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.PERMISSION_NOT_APPLIED_SUCCESSFULLY.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response changeCb2ObjectToLoadcaseRelation( String variantId, String objectId, String payload ) {
        return changeObjectToLoadcaseRelation( variantId, objectId, payload );
    }

    /**
     * Change submit loadcase relation.
     *
     * @param variantId
     *         the variant id
     * @param loadcaseId
     *         the loadcase id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @Override
    public Response changeSubmitLoadcaseRelation( String variantId, String loadcaseId, String payload ) {
        boolean isSuccess = false;
        try {
            Map< String, Object > checkBoxStateMap = new HashMap<>();
            checkBoxStateMap = ( Map< String, Object > ) JsonUtils.jsonToMap( payload, checkBoxStateMap );
            for ( Entry< String, Object > entry : checkBoxStateMap.entrySet() ) {
                Integer value = Integer.valueOf( entry.getValue().toString() );
                if ( entry.getKey().equalsIgnoreCase( "assemble" ) ) {
                    wizardsManager.checkIfLicenseFeatureAllowedToUser( SimuspaceFeaturesEnum.ASSEMBLY.getKey() );
                } else if ( entry.getKey().equalsIgnoreCase( "post" ) ) {
                    wizardsManager.checkIfLicenseFeatureAllowedToUser( SimuspaceFeaturesEnum.POST.getKey() );
                }
                isSuccess = wizardsManager.changeLoadcaseSubmit( getUserIdFromGeneralHeader(), variantId, loadcaseId, entry.getKey(),
                        value );
            }

            if ( isSuccess ) {
                return ResponseUtils.success( isSuccess );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.PERMISSION_NOT_APPLIED_SUCCESSFULLY.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response changeCb2SubmitLoadcaseRelation( String variantId, String loadcaseId, String payload ) {
        return changeSubmitLoadcaseRelation( variantId, loadcaseId, payload );
    }

    /**
     * Gets the copy vairnat UI.
     *
     * @param refId
     *         the ref id
     *
     * @return the copy vairnat UI
     */
    @Override
    public Response getCopyVairnatUI( String refId ) {
        try {
            return ResponseUtils.success( wizardsManager.getCopyVairnatUI( getUserIdFromGeneralHeader(), refId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2CopyVairnatUI( String refId ) {
        try {
            return ResponseUtils.success( wizardsManager.getCopyVairnatUI( getUserIdFromGeneralHeader(), refId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Creates the vairnat copy.
     *
     * @param refId
     *         the ref id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @Override
    public Response createVairnatCopy( String refId, String payload ) {
        try {

            return ResponseUtils.success( "Created successfully", wizardsManager.createVariantCopy( getUserIdFromGeneralHeader(), refId,
                    JsonUtils.jsonToObject( payload, VariantWizardDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the dummy option form.
     *
     * @param variantId
     *         the variantId
     * @param selectioId
     *         the selectionId
     *
     * @return the dummy option form
     */
    @Override
    public Response getDummyOptionFormForReferenceSelection( String variantId, String solverType, String selectioId ) {
        try {
            List< UUID > slectedVariantList = wizardsManager.getSelectedIdsListBySelectionId( selectioId );
            UUID variantcontainer = slectedVariantList.get( 0 );
            VariantWizardEntity wizEntity = wizardsManager.getVariantWizardEntityById( variantcontainer );
            if ( null == wizEntity || null == wizEntity.getFormJson() ) {
                return ResponseUtils.failure( "Selected object either not a DummyVariant or empty DummyVariant" );
            }
            DummyVariantDTO dummyVariant = JsonUtils.jsonToObject( wizEntity.getFormJson(), DummyVariantDTO.class );
            return ResponseUtils.success( wizardsManager.getDummyOptionFormForReferenceSelection( getUserIdFromGeneralHeader(), variantId,
                    variantcontainer.toString(), dummyVariant, wizEntity.getSolverType(), solverType, "3" ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the dummy refrence option form.
     *
     * @param variantId
     *         the option id
     *
     * @return the dummy refrence option form
     */
    @Override
    public Response getDummyRefrenceOptionForm( String variantId ) {
        try {
            return ResponseUtils.success( wizardsManager.getDummyRefrenceOptionForm( variantId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getDummyRefrenceCb2OptionForm( String variantId ) {
        try {
            return ResponseUtils.success( wizardsManager.getDummyRefrenceCb2OptionForm( variantId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the dummy file UI.
     *
     * @return the dummy file UI
     */
    @Override
    public Response getDummyFileUI() {
        try {
            return ResponseUtils.success( new TableUI( GUIUtils.listColumns( BmwCaeBenchSubModelDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the dummy file list.
     *
     * @param dummyType
     *         the dummy type
     * @param payload
     *         the payload
     *
     * @return the dummy file list
     */
    @Override
    public Response getDummyFileList( String dummyType, String payload ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( payload, FiltersDTO.class );
            return ResponseUtils
                    .success( wizardsManager.getBmwCaeBenchObjectListFromCB2( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(),
                            getTokenFromGeneralHeader(), dummyType, filter, BmwCaeBenchEnums.CB2_DUMMY_TEMP_TREE.getKey(), "1" ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the load case UI.
     *
     * @return the load case UI
     */
    @Override
    public Response getLoadCaseUI() {
        try {
            return ResponseUtils.success( new TableUI( loadcaseManager.getLoadcaseUI( getUserIdFromGeneralHeader() ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the load case selected dummy type list.
     *
     * @param dummyType
     *         the dummy type
     * @param payload
     *         the payload
     *
     * @return the load case selected dummy type list
     */
    @Override
    public Response getLoadCaseSelectedDummyTypeList( String dummyType, String payload ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( payload, FiltersDTO.class );
            return ResponseUtils
                    .success( wizardsManager.getLoadCaseSelectedDummyTypeList( getUserIdFromGeneralHeader(), dummyType, "1", filter ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the dummy type form.
     *
     * @param solverType
     *         the type id
     * @param refVariant
     *         the ref variant
     * @param dummyType
     *         the dummy type
     *
     * @return the dummy type form
     */
    @Override
    public Response getDummyTypeForm( String solverType, String refVariant, String dummyType ) {
        try {
            return ResponseUtils
                    .success( wizardsManager.getDummyTypeForm( getUserIdFromGeneralHeader(), solverType, dummyType, refVariant ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the dummy type form.
     *
     * @param solverType
     *         the type id
     * @param refVariant
     *         the ref variant
     * @param dummyType
     *         the dummy type
     *
     * @return the dummy type form
     */
    @Override
    public Response getDummyTypeFormForReferenceSelection( String solverType, String refVariant, String dummyType, String referenceId ) {
        try {
            return ResponseUtils.success( wizardsManager.getDummyTypeFormForReferenceSelection( getUserIdFromGeneralHeader(), solverType,
                    dummyType, refVariant, referenceId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the dummy load case form.
     *
     * @param id
     *         the id
     *
     * @return the dummy load case form
     */
    @Override
    public Response getDummyLoadCaseForm( UUID id ) {
        try {
            return ResponseUtils.success( wizardsManager.getDummyLoadCaseForm( getUserIdFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Fetch all cb 2 sub models.
     *
     * @param json
     *         the json
     *
     * @return the response
     */
    @Override
    public Response fetchAllCb2SubModels( String json ) {
        try {
            return ResponseUtils.info( "APi can not be called without node :", null );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Creates the and run dummy variant.
     *
     * @param parentId
     *         the parent id
     * @param json
     *         the json
     *
     * @return the response
     */
    @Override
    public Response createAndRunDummyVariant( String parentId, String json ) {
        try {
            return ResponseUtils.success( wizardsManager.createAndRunDummyVariant( getTokenFromGeneralHeader(),
                    getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(), parentId, json ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the objects views.
     *
     * @return the objects views
     */
    @Override
    public Response getObjectsViews() {
        try {
            return ResponseUtils.success( wizardsManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.RUN_VARIANT_OBJECTS_TABLE_KEY, getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Save objects view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @Override
    public Response saveObjectsView( String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.RUN_VARIANT_OBJECTS_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    wizardsManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Sets the objects view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @Override
    public Response setObjectsViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    wizardsManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.RUN_VARIANT_OBJECTS_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Delete objects view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @Override
    public Response deleteObjectsView( String viewId ) {
        try {
            if ( wizardsManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Update objects view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @Override
    public Response updateObjectsView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.RUN_VARIANT_OBJECTS_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    wizardsManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the dummy files selection.
     *
     * @param selectionId
     *         the selection id
     * @param filterJson
     *         the filter json
     *
     * @return the dummy files selection
     */
    @Override
    public Response getDummyFilesSelection( String selectionId, String filterJson ) {
        try {
            return ResponseUtils.success( wizardsManager.getBmwCaeBenchObjectSelection( selectionId, filterJson,
                    BmwCaeBenchEnums.CB2_DUMMY_TEMP_TREE.getValue() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the load case selection.
     *
     * @param selectionId
     *         the selection id
     * @param filterJson
     *         the filter json
     *
     * @return the load case selection
     */
    @Override
    public Response getLoadCaseSelection( String selectionId, String filterJson ) {
        try {
            return ResponseUtils.success( wizardsManager.getLoadCaseSelection( selectionId, filterJson ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the dummy refrence variant selection form.
     *
     * @param variantId
     *         the variantid
     * @param optionId
     *         the option id
     *
     * @return the dummy refrence variant selection form
     */
    @Override
    public Response getDummyRefrenceVariantSelectionForm( String variantId, String solverType, String optionId ) {
        try {
            return ResponseUtils.success(
                    wizardsManager.getDummyRefrenceVariantSelectionForm( getUserIdFromGeneralHeader(), variantId, solverType, optionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getDummyRefrenceSolverSelectionForm( String variantId, String solverType ) {
        try {
            return ResponseUtils.success( wizardsManager.getDummyRefrenceVariantForm( solverType, variantId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the bmw cae bench tree data.
     *
     * @return the bmw cae bench tree data
     */
    @Override
    public Response getBmwCaeBenchTreeData() {
        try {
            return ResponseUtils
                    .success( wizardsManager.getBmwCaeBenchTreeData( getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getBmwCaeBenchObjetBySelection( String selectionId ) {
        try {
            return ResponseUtils.success( wizardsManager.getBmwCaeBenchObjetBySelection( selectionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2RunGeneralDropDown( String varId, String selectionId ) {
        try {
            return ResponseUtils.success( wizardsManager.getCb2GeneralDropDown( getUserIdFromGeneralHeader(),
                    getUserNameFromGeneralHeader(), varId, selectionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2RunGeneralDefinitionDropDown( String varId, String selectionId, String itemName ) {
        try {
            return ResponseUtils.success( wizardsManager.getCb2GeneralDefinitionDropDown( getUserIdFromGeneralHeader(),
                    getUserNameFromGeneralHeader(), varId, selectionId, itemName ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response saveCb2ObjectsOrder( String id, String payload ) {
        try {
            return ResponseUtils.success(
                    wizardsManager.saveObjectsOrder( getUserIdFromGeneralHeader(), id, JsonUtils.jsonToList( payload, UUID.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2ParamsBySetup( String assembleSolvePost, String dynamicValKeyName, String setupOid ) {
        try {
            return ResponseUtils.success( wizardsManager.getCb2ParamsBySetup( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(),
                    assembleSolvePost, dynamicValKeyName, setupOid ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2StudyDefsByVariant( String variantSId ) {
        try {
            return ResponseUtils.success(
                    wizardsManager.getCb2StudyDefsByVariant( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(), variantSId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2ScenariosByVariant( String variantSId ) {
        try {
            return ResponseUtils.success(
                    wizardsManager.getCb2ScenariosByVariant( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(), variantSId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2SimDefsByScenario( String variantAndScenarioOid ) {
        try {
            return ResponseUtils.success( wizardsManager.getCb2SimDefsByScenario( getUserIdFromGeneralHeader(),
                    getUserNameFromGeneralHeader(), variantAndScenarioOid ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getCb2SetupByStudyDef( String assembleSolvePost, String studydefOid ) {
        try {
            return ResponseUtils.success( wizardsManager.getCb2SetupByStudyDef( getUserIdFromGeneralHeader(),
                    getUserNameFromGeneralHeader(), assembleSolvePost, studydefOid ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

}
