package de.soco.software.simuspace.suscore.object.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.BreadCrumbDTO;
import de.soco.software.simuspace.suscore.common.model.BreadCrumbItemDTO;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.object.manager.BreadCrumbManager;
import de.soco.software.simuspace.suscore.object.service.rest.BreadCrumbService;

/**
 * The Class is responsible to test public functions of {@link BreadCrumbService}}.
 *
 * @author M.Nasir.Farooq
 */
public class BreadCrumbServiceImplTest {

    /**
     * The Constant CREATE_WORKFLOW_CONTEXT.
     */
    private static final String CREATE_WORKFLOW_CONTEXT = "/object-tree/f7622723-4672-45bf-b678-b100a45f6115/context";

    /**
     * The Constant CREATE_WORKFLOW_CONTEXT_URL.
     */
    private static final String CREATE_WORKFLOW_CONTEXT_URL = "create/workflow/acb314b9-35be-4798-8385-aca95ef70830";

    /**
     * The Constant WORKFLOW_PROJECT_CONTEXT.
     */
    private static final String WORKFLOW_PROJECT_CONTEXT = "/object-tree/acb314b9-35be-4798-8385-aca95ef70830/context";

    /**
     * The Constant ALL_WORKFLOW_CONTEXT.
     */
    private static final String ALL_WORKFLOW_CONTEXT = "/object-tree/dc14ac39-1243-484a-94ba-12db7bb46930/context";

    /**
     * The Constant WORKFLOWS_CONTEXT.
     */
    private static final String WORKFLOWS_CONTEXT = "/object-tree/6bf18669-57c1-434e-9d86-1f0ddb59aec9/context";

    /**
     * The Constant CREATE_WORKFLOW_CONTEXT_NAME.
     */
    private static final String CREATE_WORKFLOW_CONTEXT_NAME = "Create workflow";

    /**
     * The Constant PROJECT_NAME_WORKFLOW.
     */
    private static final String PROJECT_NAME_WORKFLOW = "workflow name";

    /**
     * The Constant PROJECT_NAME_ALL_WORKFLOWS.
     */
    private static final String PROJECT_NAME_ALL_WORKFLOWS = "All Workflows";

    /**
     * The Constant PROJECT_NAME_WORKFLOWS.
     */
    private static final String PROJECT_NAME_WORKFLOWS = "Workflows";

    /**
     * The Constant OBJECT_ID.
     */
    private static final String OBJECT_ID = UUID.randomUUID().toString();

    /**
     * FAILURE_OF_BREAD_CRUMB constant.
     */
    private static final String FAILURE_OF_BREAD_CRUMB = "Unable to get BreadCrumb";

    /**
     * The Constant ERROR_MESSAGE.
     */
    private static final String ERROR_MESSAGE = "ERROR";

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The data Service Reference.
     */
    private static BreadCrumbServiceImpl serviceImpl;

    /**
     * The Data Manager Reference.
     */
    private static BreadCrumbManager breadCrumbManager;

    /**
     * set Up.
     *
     * @throws Exception
     *         the exception
     */
    @BeforeClass
    public static void tearUp() throws Exception {
        mockControl.resetToNice();
        breadCrumbManager = mockControl.createMock( BreadCrumbManager.class );
        serviceImpl = new BreadCrumbServiceImpl();
        serviceImpl.setBreadCrumbManager( breadCrumbManager );
    }

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        mockControl.resetToNice();
    }

    /**
     * Should successfully get create workflow bread crumb when valid input is provided.
     */
    @Test
    public void shouldSuccessfullyGetCreateWorkflowBreadCrumbWhenValidInputIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.createWorkflowBreadCrumb( OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Should failure response when manager does not return any bread crumb for create workflow.
     */
    @Test
    public void shouldFailureResponseWhenManagerDoesNotReturnAnyBreadCrumbForCreateWorkflow() {

        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( null ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.createWorkflowBreadCrumb( OBJECT_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNull( actualResponse.getData() );
        Assert.assertNotNull( actualResponse.getMessage() );
        Assert.assertEquals( FAILURE_OF_BREAD_CRUMB, actualResponse.getMessage().getContent() );
    }

    /**
     * Should successfully get workflow view bread crumb when valid input is provided.
     */
    @Test
    public void shouldSuccessfullyGetWorkflowViewBreadCrumbWhenValidInputIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();

        mockControl.replay();
        Response actual = serviceImpl.getWorkflowProjectViewBreadCrumb( OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Should successfully get job single view bread crumb when valid input is provided.
     */
    @Test
    public void shouldSuccessfullyGetJobSingleViewBreadCrumbWhenValidInputIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();

        mockControl.replay();
        Response actual = serviceImpl.getJobSingleViewBreadCrumb( OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Should throw exception in job single view bread crumb when managerthrow sus exception.
     */
    @Test
    public void shouldThrowExceptionInJobSingleViewBreadCrumbWhenManagerthrowSusException() {

        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andThrow( new SusException( ERROR_MESSAGE ) );
        mockControl.replay();
        Response actual = serviceImpl.getJobSingleViewBreadCrumb( OBJECT_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNull( actualResponse.getData() );
        Assert.assertNotNull( actualResponse.getMessage() );
        Assert.assertEquals( ERROR_MESSAGE, actualResponse.getMessage().getContent() );

    }

    /**
     * Should throw exception in job table bread crumb when managerthrow sus exception.
     */
    @Test
    public void shouldThrowExceptionInJobTableBreadCrumbWhenManagerthrowSusException() {

        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andThrow( new SusException( ERROR_MESSAGE ) );
        mockControl.replay();
        Response actual = serviceImpl.getJobBreadCrumb();
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNull( actualResponse.getData() );
        Assert.assertNotNull( actualResponse.getMessage() );
        Assert.assertEquals( ERROR_MESSAGE, actualResponse.getMessage().getContent() );

    }

    /**
     * Should successfully get job table bread crumb when valid input is provided.
     */
    @Test
    public void shouldSuccessfullyGetJobTableBreadCrumbWhenValidInputIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();

        mockControl.replay();
        Response actual = serviceImpl.getJobBreadCrumb();
        assertResponse( actual, expected );
    }

    /**
     * Should failure response when manager does not return any bread crumb for view workflow.
     */
    @Test
    public void shouldFailureResponseWhenManagerDoesNotReturnAnyBreadCrumbForViewWorkflow() {

        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( null ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.createWorkflowBreadCrumb( OBJECT_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNull( actualResponse.getData() );
        Assert.assertNotNull( actualResponse.getMessage() );
        Assert.assertEquals( FAILURE_OF_BREAD_CRUMB, actualResponse.getMessage().getContent() );
    }

    /**
     * Should successfully create deleted objects view bread crumb when valid input is provided.
     */
    @Test
    public void shouldSuccessfullyCreateDeletedObjectsViewBreadCrumbWhenValidInputIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.createDeletedObjectsBreadCrumb();
        assertResponse( actual, expected );
    }

    /**
     * Should successfully create add meta data to object bread crumb when valid object id is provided.
     */
    @Test
    public void shouldSuccessfullyCreateAddMetaDataToObjectBreadCrumbWhenValidObjectIdIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.createAddMetaDataToObjectBreadCrumb( OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Should successfully create add meta data to object bread crumb when valid object id is provided.
     */
    @Test
    public void shouldSuccessfullyCreateUpdateDataObjectBreadCrumbWhenValidObjectIdIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.updateDataObjectBreadCrumb( OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Should successfully create data projects bread crumb when valid object id is provided.
     */
    @Test
    public void shouldSuccessfullyCreateStatusProjectsBreadCrumbWhenValidObjectIdIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.createStatusProjectsBreadCrumb( OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Should successfully import workflow projects bread crumb when valid object id is provided.
     */
    @Test
    public void shouldSuccessfullyImportWorkflowProjectsBreadCrumbWhenValidObjectIdIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.importWorkflowProjectsBreadCrumb( OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Should successfully edit role bread crumb when valid object id is provided.
     */
    @Test
    public void shouldSuccessfullyEditRoleBreadCrumbWhenValidObjectIdIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.editRoleBreadCrumb( OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Should successfully manage role bread crumb when valid object id is provided.
     */
    @Test
    public void shouldSuccessfullyManageRoleBreadCrumbWhenValidObjectIdIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.manageRoleBreadCrumb( OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Should successfully create data projects bread crumb when valid object id is provided.
     */
    @Test
    public void shouldSuccessfullyCreateDataProjectsBreadCrumbWhenValidObjectIdIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.createDataProjectsBreadCrumb( OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Should successfully edit group bread crumb when valid object id is provided.
     */
    @Test
    public void shouldSuccessfullyEditGroupBreadCrumbWhenValidObjectIdIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.editGroupBreadCrumb( OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Should successfully edit system license bread crumb when valid object id is provided.
     */
    @Test
    public void shouldSuccessfullyEditSystemLicenseBreadCrumbWhenValidObjectIdIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.editSystemLicenseBreadCrumb( OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Should successfully add permissions to object bread crumb when valid object id is provided.
     */
    @Test
    public void shouldSuccessfullyAddPermissionsToObjectBreadCrumbWhenValidObjectIdIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.addPermissionsToObjectBreadCrumb( OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Should successfully edit system user bread crumb when valid object id is provided.
     */
    @Test
    public void shouldSuccessfullyEditSystemUserBreadCrumbWhenValidObjectIdIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.editSystemUserBreadCrumb( OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Should successfully edit system directory bread crumb when valid object id is provided.
     */
    @Test
    public void shouldSuccessfullyEditSystemDirectoryBreadCrumbWhenValidObjectIdIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.editSystemDirectoryBreadCrumb( OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Should successfully update change status bread crumb when valid object id is provided.
     */
    @Test
    public void shouldSuccessfullyUpdateChangeStatusBreadCrumbWhenValidObjectIdIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.updateChangeStatusBreadCrumb( OBJECT_ID, OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Should successfully update metadata of project bread crumb when valid object id is provided.
     */
    @Test
    public void shouldSuccessfullyUpdateMetadataOfProjectBreadCrumbWhenValidObjectIdIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.updateProjectMetadataBreadCrumb( OBJECT_ID, OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Should successfully add permissions to project bread crumb when valid object id is provided.
     */
    @Test
    public void shouldSuccessfullyAddPermissionsToProjectBreadCrumbWhenValidObjectIdIsProvided() {

        BreadCrumbDTO expected = fillBreadCrumbDTO();
        EasyMock.expect( breadCrumbManager.createBreadCrumb( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = serviceImpl.projectPermBreadCrumb( OBJECT_ID );
        assertResponse( actual, expected );
    }

    /**
     * Assert response.
     *
     * @param actual
     *         the actual
     * @param expected
     *         the expected
     */
    private void assertResponse( Response actual, BreadCrumbDTO expected ) {
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        List< BreadCrumbItemDTO > crumbItemDTOs = new ArrayList<>();
        crumbItemDTOs = JsonUtils.linkedMapObjectToClassObject( actualResponse.getData(), crumbItemDTOs.getClass() );
        Assert.assertEquals( expected.getBreadCrumbItems().size(), crumbItemDTOs.size() );
    }

    /**
     * Fill bread crumb DTO.
     *
     * @return the bread crumb DTO
     */
    private BreadCrumbDTO fillBreadCrumbDTO() {
        BreadCrumbDTO breadCrumbDTO = new BreadCrumbDTO();
        List< BreadCrumbItemDTO > crumbItemDTOs = new ArrayList<>();
        crumbItemDTOs.add( new BreadCrumbItemDTO( PROJECT_NAME_WORKFLOWS, null, WORKFLOWS_CONTEXT ) );
        crumbItemDTOs.add( new BreadCrumbItemDTO( PROJECT_NAME_ALL_WORKFLOWS, null, ALL_WORKFLOW_CONTEXT ) );
        crumbItemDTOs.add( new BreadCrumbItemDTO( PROJECT_NAME_WORKFLOW, null, WORKFLOW_PROJECT_CONTEXT ) );
        crumbItemDTOs.add( new BreadCrumbItemDTO( CREATE_WORKFLOW_CONTEXT_NAME, CREATE_WORKFLOW_CONTEXT_URL, CREATE_WORKFLOW_CONTEXT ) );
        breadCrumbDTO.setBreadCrumbItems( crumbItemDTOs );
        return breadCrumbDTO;
    }

}
