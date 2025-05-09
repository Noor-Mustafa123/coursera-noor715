package de.soco.software.simuspace.suscore.core.service.impl;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.core.manager.impl.SelectionManagerImpl;

/**
 * The Class SelectionServiceImplTest contains test cases for SelectionServiceImpl class.
 *
 * @author Noman Arshad
 */
public class SelectionServiceImplTest {

    /**
     * The Constant ITEM_ID_ONE.
     */
    public static final UUID ITEM_ID_ONE = UUID.randomUUID();

    /**
     * The Constant ITEM_ID_TWO.
     */
    public static final UUID ITEM_ID_TWO = UUID.randomUUID();

    /**
     * The Constant ITEM_ID_THREE.
     */
    public static final UUID ITEM_ID_THREE = UUID.randomUUID();

    /**
     * The Constant SELECTION_ID.
     */
    public static final UUID SELECTION_ID = UUID.fromString( "cd18eff4-b50b-4b13-aa92-6f4f92259ddc" );

    /**
     * The Constant SELECTION_SAVE_PAYLOAD.
     */
    private static final String SELECTION_SAVE_PAYLOAD = "{\"items\":[\"367f97e8-b751-4e89-a73d-965e58e23f69\",\"f62c443f-8096-40fe-8491-4ca0663ca754\"]}";

    /**
     * The Constant ACTIVE.
     */
    public static final String ACTIVE = "Active";

    /**
     * The Constant ABOUT.
     */
    public static final String ABOUT = "2.0.0";

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * The Constant mockControl.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The selection service.
     */
    private SelectionServiceImpl selectionService;

    /**
     * The selectionmanager.
     */
    private SelectionManagerImpl selectionManager;

    /**
     * To initialize the objects and mocking objects.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        selectionService = new SelectionServiceImpl();
        selectionManager = mockControl.createMock( SelectionManagerImpl.class );

        selectionService.setSelectionManager( selectionManager );
    }

    /**
     * Should successfully save selections and return generated id.
     */
    @Test
    public void shouldSuccessfullySaveSelectionsAndReturnGeneratedId() {

        SelectionResponseUI selection = new SelectionResponseUI();
        selection.setId( SELECTION_ID.toString() );
        EasyMock.expect( selectionManager.createSelectionForSingleItem( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject() ) )
                .andReturn( selection ).anyTimes();
        mockControl.replay();
        Response actual = selectionService.saveAndUpdateSelections( SELECTION_SAVE_PAYLOAD );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
    }

    /**
     * Should successfully return list of ids by selection id.
     */
    @Test
    public void shouldSuccessfullyReturnListOfIdsBySelectionId() {

        List< UUID > expected = prepareListUUID();
        EasyMock.expect(
                        selectionManager.getSelectedIdsListBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( expected ).anyTimes();
        mockControl.replay();
        Response actual = selectionService.getAllSelectionIds( SELECTION_SAVE_PAYLOAD );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
    }

    /**
     * Should throw bad request when wrong parameter provided to save and update selections.
     */
    @Test
    public void shouldThrowBadRequestWhenWrongParameterProvidedToSaveAndUpdateSelections() {

        SelectionResponseUI selection = new SelectionResponseUI();
        selection.setId( SELECTION_ID.toString() );
        EasyMock.expect( selectionManager.createSelectionForSingleItem( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject() ) )
                .andReturn( selection ).anyTimes();
        mockControl.replay();
        Response actualResponse = selectionService.saveAndUpdateSelections( String.valueOf( ConstantsInteger.INTEGER_VALUE_ZERO ) );
        Assert.assertNotNull( actualResponse );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, actualResponse.getStatus() );
    }

    /**
     * Should successfully add selections in existing selection.
     */
    @Test
    public void shouldSuccessfullyAddSelectionsInExistingSelection() {

        FiltersDTO filter = new FiltersDTO();
        List< Object > list = new ArrayList<>();
        list.add( ITEM_ID_ONE );
        list.add( ITEM_ID_TWO );
        list.add( ITEM_ID_THREE );
        filter.setItems( list );
        SelectionResponseUI selection = new SelectionResponseUI();
        selection.setId( SELECTION_ID.toString() );
        EasyMock.expect( selectionManager.addSelectionItemsInExistingSelection( SELECTION_ID.toString(), filter ) ).andReturn( selection )
                .anyTimes();
        mockControl.replay();
        Response actual = selectionService.addSelection( SELECTION_ID.toString(), JsonUtils.objectToJson( filter ) );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
    }

    /**
     * Should successfully remove selections in existing selection.
     */
    @Test
    public void shouldSuccessfullyRemoveSelectionsInExistingSelection() {

        FiltersDTO filter = new FiltersDTO();
        List< Object > list = new ArrayList<>();
        list.add( ITEM_ID_ONE );
        list.add( ITEM_ID_TWO );
        list.add( ITEM_ID_THREE );
        filter.setItems( list );
        SelectionResponseUI selection = new SelectionResponseUI();
        selection.setId( SELECTION_ID.toString() );
        EasyMock.expect( selectionManager.removeSelectionItemsInExistingSelection( SELECTION_ID.toString(), filter ) )
                .andReturn( selection ).anyTimes();
        mockControl.replay();
        Response actual = selectionService.removeSelection( SELECTION_ID.toString(), JsonUtils.objectToJson( filter ) );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
    }

    /**
     * Should successfully return generic dto UI when called.
     */
    @Test
    public void shouldSuccessfullyReturnGenericDtoUIWhenCalled() {

        Response actual = selectionService.getSelectionSusEntityUI( SELECTION_ID.toString() );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );

    }

    /**
     * Should successfully return generic dto list when called.
     */
    @Test
    public void shouldSuccessfullyReturnGenericDtoListWhenCalled() {
        FiltersDTO filter = new FiltersDTO();
        List< UIFormItem > expected = null;

        List< Object > list = new ArrayList<>();
        list.add( ITEM_ID_ONE );
        list.add( ITEM_ID_TWO );
        list.add( ITEM_ID_THREE );
        filter.setItems( list );
        Response actual = selectionService.getSelectionSusEntityList( SELECTION_ID.toString(), JsonUtils.objectToJson( filter ) );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( expected, actualResponse.getData() );

    }

    /**
     * Prepare list UUID.
     *
     * @return the list
     */
    private List< UUID > prepareListUUID() {
        List< UUID > list = new ArrayList<>();
        list.add( ITEM_ID_ONE );
        list.add( ITEM_ID_TWO );
        list.add( ITEM_ID_THREE );
        return list;
    }

    /**
     * Gets the selection service.
     *
     * @return the selection service
     */
    public SelectionServiceImpl getSelectionService() {
        return selectionService;
    }

    /**
     * Sets the selection service.
     *
     * @param selectionService
     *         the new selection service
     */
    public void setSelectionService( SelectionServiceImpl selectionService ) {
        this.selectionService = selectionService;
    }

}
