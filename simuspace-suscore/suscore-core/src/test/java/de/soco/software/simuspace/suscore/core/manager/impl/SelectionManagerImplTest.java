package de.soco.software.simuspace.suscore.core.manager.impl;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserProfile;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.core.dao.SelectionDAO;
import de.soco.software.simuspace.suscore.core.dao.SelectionItemDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.dao.UserCommonDAO;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionItemEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.impl.ObjectTypeConfigManagerImpl;

/**
 * The Class SelectionManagerImplTest for testing public methods of SelectionManagerImplTest.
 *
 * @author Noman Arshad
 */
public class SelectionManagerImplTest {

    /**
     * The selection manager impl.
     */
    private SelectionManagerImpl selectionManagerImpl;

    /**
     * The selection DAO.
     */
    private SelectionDAO selectionDAO;

    /**
     * The selection item DAO.
     */
    private SelectionItemDAO selectionItemDAO;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The user common manager impl.
     */
    private UserCommonDAO userCommonDAO;

    /**
     * The filter.
     */
    private static FiltersDTO filter;

    /**
     * The user entity.
     */
    private static UserEntity userEntity;

    /**
     * The selection entity.
     */
    private static SelectionEntity expectedSelectionEntity;

    /**
     * The selection item entit.
     */
    private static SelectionItemEntity expectedSelectionItemEntit;

    /**
     * The selection entity list.
     */
    private static List< SelectionEntity > expectedSelectionEntityList;

    /**
     * The Constant mockControl.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * The Constant ORIGIN_USER.
     */
    public static final String ORIGIN_USER = "User";

    /**
     * The Constant ORIGIN_Group.
     */
    public static final String ORIGIN_GROUP = "group";

    /**
     * The Constant USER_ID_RANDOM.
     */
    public static final UUID USER_ID_RANDOM = UUID.randomUUID();

    /**
     * The Constant SELECTION_ID.
     */
    public static final UUID SELECTION_ID = UUID.fromString( "cd18eff4-b50b-4b13-aa92-6f4f92259ddc" );

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
     * The Constant ACTIVE.
     */
    public static final String ACTIVE = "Active";

    /**
     * The active account status.
     */
    public static final boolean ACCOUNT_STATUS_ACTIVE = true;

    /**
     * The Constant UID.
     */
    private static final String UID = "sces160";

    /**
     * The Constant PASSWD.
     */
    private static final String PASSWD = "aaZZa44e";

    /**
     * The constant SUPER_USER_ID.
     */
    public static final Integer SUPER_USER_ID = 0;

    /**
     * The Constant SELECTION_ITEMS_NOT_SAVED.
     */
    private static final String SELECTION_ITEMS_NOT_SAVED = "Selection item not saved";

    /**
     * The Constant PROVIDED_USER_NOT_AVAILIBLE.
     */
    private static final String PROVIDED_USER_NOT_AVAILIBLE = "Provided user not availabe";

    /**
     * The Constant SELECTION_NOT_SAVED.
     */
    private static final String SELECTION_NOT_SAVED = "Selection not saved";

    /**
     * The Constant LIFECYCLE.
     */
    private static final String LIFECYCLE = "553536c7-71ec-409d-8f48-ec779a98a68e";

    /**
     * The Constant ALL_VISIBILITY.
     */
    private static final String ALL_VISIBILITY = "all";

    /**
     * The life cycle manager.
     */
    private LifeCycleManager lifeCycleManager;

    private UserDTO user;

    /**
     * The sus dao.
     */
    private SuSGenericObjectDAO< SuSEntity > susDao;

    /**
     * The config manager.
     */
    private ObjectTypeConfigManagerImpl configManager;

    /**
     * The Constant OBJECT_IDENTITY_ID.
     */
    private static final UUID OBJECT_IDENTITY_ID = UUID.randomUUID();

    /**
     * Dummy Object Id.
     */
    private static final UUID DATA_OBJECT_ID = UUID.randomUUID();

    /**
     * Dummy Version Id for test Cases.
     */
    private static final int DEFAULT_VERSION_ID = 1;

    /**
     * Dummy Data Object Name of an object.
     */
    private static final String DATA_OBJECT_NAME = "Test Data Object name";

    /**
     * The Constant OBJECT_TYPE_ID.
     */
    private static final UUID OBJECT_TYPE_ID = UUID.randomUUID();

    /**
     * The Constant DEFAULT_LIFECYCLE_STATUS.
     */
    private static final String WIP_LIFECYCLE_STATUS_ID = "553536c7-71ec-409d-8f48-ec779a98a68e";

    /**
     * The Constant SUPER_USER_NAME.
     */
    private static final String SUPER_USER_NAME = "simuspace";

    /**
     * Dummy project Id.
     */
    private static final UUID PROJECT_ID = UUID.randomUUID();

    /**
     * The Constant WIP_LIFECYCLE_STATUS_NAME.
     */
    private static final String WIP_LIFECYCLE_STATUS_NAME = "WIP";

    /**
     * To initialize the objects and mocking objects.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        selectionManagerImpl = new SelectionManagerImpl();
        lifeCycleManager = mockControl.createMock( LifeCycleManager.class );
        selectionDAO = mockControl.createMock( SelectionDAO.class );
        selectionItemDAO = mockControl.createMock( SelectionItemDAO.class );
        userCommonDAO = mockControl.createMock( UserCommonDAO.class );
        userCommonManager = mockControl.createMock( UserCommonManager.class );
        susDao = mockControl.createMock( SuSGenericObjectDAO.class );
        configManager = mockControl.createMock( ObjectTypeConfigManagerImpl.class );

        selectionManagerImpl.setSelectionItemDAO( selectionItemDAO );
        selectionManagerImpl.setSelectionDAO( selectionDAO );
        selectionManagerImpl.setUserCommonManager( userCommonManager );
        selectionManagerImpl.setLifeCycleManager( lifeCycleManager );
        selectionManagerImpl.setSusDAO( susDao );
        selectionManagerImpl.setConfigManager( configManager );

        EasyMock.expect( configManager.getStatusByIdandObjectType( EasyMock.anyObject(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( getStatusDto() ).anyTimes();

    }

    /**
     * Inits the.
     */
    @BeforeClass
    public static void init() {
        filter = prepareFilter();
        userEntity = fillUserEntity();
        expectedSelectionEntity = PrepareSelectionEntity();
        expectedSelectionEntity.setUserEntity( userEntity );
        expectedSelectionItemEntit = getSelectionItemEntity( expectedSelectionEntity );
        expectedSelectionEntityList = prepareListBySelectionEntity( expectedSelectionEntity );
    }

    /**
     * Should successfully save or update selections when valid user make selections.
     */
    @Test
    public void shouldSuccessfullySaveOrUpdateSelectionsWhenValidUserMakeSelections() {
        prepareMockMethods();

        SelectionResponseUI sid = selectionManagerImpl.createSelection( USER_ID_RANDOM.toString(), ORIGIN_USER, filter );
        Assert.assertEquals( sid.getId(), expectedSelectionEntity.getId().toString() );

    }

    /**
     * Should throw exception when selection items are not saved.
     */
    @Test
    public void shouldThrowExceptionWhenSelectionItemsAreNotSaved() {
        thrown.expect( SusException.class );
        thrown.expectMessage( SELECTION_ITEMS_NOT_SAVED );
        EasyMock.expect( userCommonManager.getUserCommonDAO() ).andReturn( userCommonDAO );
        EasyMock.expect(
                        userCommonDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( selectionDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SelectionEntity.class ) ) )
                .andReturn( expectedSelectionEntity ).anyTimes();
        EasyMock.expect(
                        selectionItemDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SelectionItemEntity.class ) ) )
                .andReturn( expectedSelectionItemEntit ).anyTimes();
        EasyMock.expect( selectionDAO.getObjectListByProperty( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedSelectionEntityList ).anyTimes();
        mockControl.replay();

        selectionManagerImpl.createSelection( EasyMock.anyObject( EntityManager.class ), USER_ID_RANDOM.toString(), SelectionOrigins.USER,
                filter );
    }

    /**
     * Should throw exception when user id is not exists in database.
     */
    @Test
    public void shouldThrowExceptionWhenUserIdIsNotExistsInDatabase() {
        thrown.expect( SusException.class );
        thrown.expectMessage( PROVIDED_USER_NOT_AVAILIBLE );
        EasyMock.expect( userCommonManager.getUserCommonDAO() ).andReturn( userCommonDAO );
        EasyMock.expect(
                        userCommonDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( null ).anyTimes();
        EasyMock.expect( selectionDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SelectionEntity.class ) ) )
                .andReturn( expectedSelectionEntity ).anyTimes();
        EasyMock.expect(
                        selectionItemDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SelectionItemEntity.class ) ) )
                .andReturn( expectedSelectionItemEntit ).anyTimes();
        EasyMock.expect( selectionDAO.getObjectListByProperty( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedSelectionEntityList ).anyTimes();
        mockControl.replay();

        selectionManagerImpl.createSelection( EasyMock.anyObject( EntityManager.class ), USER_ID_RANDOM.toString(), SelectionOrigins.USER,
                filter );
    }

    /**
     * Should throw exception when selection is not saved.
     */
    @Test
    public void shouldThrowExceptionWhenSelectionIsNotSaved() {
        thrown.expect( SusException.class );
        thrown.expectMessage( SELECTION_NOT_SAVED );
        EasyMock.expect( userCommonManager.getUserCommonDAO() ).andReturn( userCommonDAO );
        EasyMock.expect(
                        userCommonDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect(
                        selectionItemDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SelectionItemEntity.class ) ) )
                .andReturn( expectedSelectionItemEntit ).anyTimes();
        EasyMock.expect( selectionDAO.getObjectListByProperty( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedSelectionEntityList ).anyTimes();
        mockControl.replay();

        selectionManagerImpl.createSelection( EasyMock.anyObject( EntityManager.class ), USER_ID_RANDOM.toString(), SelectionOrigins.USER,
                filter );
    }

    /**
     * Should successfully return all the selected ids list when selection id is provided.
     */
    @Test
    public void shouldSuccessfullyReturnAllTheSelectedIdsListWhenSelectionIdIsProvided() {

        SelectionItemEntity selectionItemEntity = getSelectionItemEntity( expectedSelectionEntity );
        List< SelectionItemEntity > expectedSelectionItemEntityList = prepareListBySelectionItemEntity( selectionItemEntity );

        EasyMock.expect( selectionItemDAO.getObjectListByProperty( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedSelectionItemEntityList ).anyTimes();
        mockControl.replay();
        List< UUID > expectedSelectionItemsList = selectionManagerImpl
                .extractIdsFromSelectionItemEntityList( expectedSelectionItemEntityList );
        List< UUID > actualUseridsList = selectionManagerImpl.getSelectedIdsListBySelectionId( EasyMock.anyObject( EntityManager.class ),
                SELECTION_ID.toString() );

        Assert.assertEquals( actualUseridsList, expectedSelectionItemsList );
    }

    /**
     * Should successfully return all selections when selection id is provided.
     */
    @Test
    public void shouldSuccessfullyReturnAllSelectionsWhenSelectionIdIsProvided() {

        SelectionEntity selectionEntity = PrepareSelectionEntity();
        selectionEntity.setUserEntity( userEntity );
        SelectionItemEntity expectedSelectionItemEntity = getSelectionItemEntity( selectionEntity );
        List< SelectionItemEntity > expectedSelectionItemEntityList = prepareListBySelectionItemEntity( expectedSelectionItemEntity );

        EasyMock.expect( selectionItemDAO.getPaginatedSelectionByProperties( EasyMock.anyObject( EntityManager.class ),
                        EasyMock.anyString(), EasyMock.anyObject( UUID.class ), EasyMock.anyObject( FiltersDTO.class ) ) )
                .andReturn( expectedSelectionItemEntityList ).anyTimes();
        mockControl.replay();

        List< SelectionItemEntity > actualSelectionItemList = selectionManagerImpl
                .getUserSelectionsBySelectionIds( EasyMock.anyObject( EntityManager.class ), SELECTION_ID.toString(), filter );
        Iterator< SelectionItemEntity > actualSelectionsItemList = actualSelectionItemList.iterator();
        Iterator< SelectionItemEntity > expectedSelectionItemsEntityList = expectedSelectionItemEntityList.iterator();
        while ( actualSelectionsItemList.hasNext() && expectedSelectionItemsEntityList.hasNext() ) {
            SelectionItemEntity actualSelectionItemEntityObject = actualSelectionsItemList.next();
            SelectionItemEntity expectedSelectionItemEntityObject = expectedSelectionItemsEntityList.next();
            Assert.assertEquals( actualSelectionItemEntityObject.getId(), expectedSelectionItemEntityObject.getId() );
            Assert.assertEquals( actualSelectionItemEntityObject.getItem(), expectedSelectionItemEntityObject.getItem() );
        }
    }

    /**
     * Should successfully genrate selection id and prepare user entity when super user make selections.
     */
    @Test
    public void shouldSuccessfullyGenrateSelectionIdAndPrepareUserEntityWhenSuperUserMakeSelections() {

        prepareMockMethods();

        SelectionResponseUI selectionIdGenrated = selectionManagerImpl.createSelection( ConstantsUserProfile.SUPER_USER_ID.toString(),
                ORIGIN_GROUP, filter );
        Assert.assertNotNull( selectionIdGenrated );
    }

    /**
     * Should remove selection item if valid id is provided.
     */
    @Test
    public void shouldRemoveSelectionItemIfValidIdIsProvided() {
        SelectionEntity selectionEntity = PrepareSelectionEntity();
        selectionEntity.setUserEntity( userEntity );
        SelectionItemEntity expectedSelectionItemEntity = getSelectionItemEntity( selectionEntity );
        EasyMock.expect( selectionItemDAO.findById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( expectedSelectionItemEntity ).anyTimes();
        mockControl.replay();
        boolean actual = selectionManagerImpl.removeSelection( SELECTION_ID, false );
        Assert.assertTrue( actual );
    }

    /**
     * Should return success if selection item is not exist with id.
     */
    @Test
    public void shouldReturnSuccessIfSelectionItemIsNotExistWithId() {
        EasyMock.expect( selectionItemDAO.findById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( null ).anyTimes();
        mockControl.replay();
        boolean actual = selectionManagerImpl.removeSelection( SELECTION_ID, false );
        Assert.assertTrue( actual );
    }

    /**
     * Should remove selection if valid id is provided.
     */
    @Test
    public void shouldRemoveSelectionIfValidIdIsProvided() {
        SelectionEntity selectionEntity = PrepareSelectionEntity();
        selectionEntity.setUserEntity( userEntity );
        EasyMock.expect( selectionDAO.findById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( selectionEntity ).anyTimes();
        mockControl.replay();
        boolean actual = selectionManagerImpl.removeSelection( SELECTION_ID, true );
        Assert.assertTrue( actual );
    }

    /**
     * Should return success if selection is not exist with id.
     */
    @Test
    public void shouldReturnSuccessIfSelectionIsNotExistWithId() {
        EasyMock.expect( selectionDAO.findById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( null ).anyTimes();
        mockControl.replay();
        boolean actual = selectionManagerImpl.removeSelection( SELECTION_ID, true );
        Assert.assertTrue( actual );
    }

    /**
     * ********************************** Get All OBJECT ***************************************.
     */

    @Test
    public void shouldSuccessfullyAddItemsToExistingSelectionWhenValidParameterAreProvided() {

        prepareMockMethods();
        FiltersDTO filter = prepareFilter();
        SelectionResponseUI actual = selectionManagerImpl.addSelectionItemsInExistingSelection( SELECTION_ID.toString(), filter );
        Assert.assertNotNull( actual );
    }

    /**
     * Should throw exception selection items not saved in add items to existing selection when in valid parameter are provided.
     */
    @Test
    public void shouldThrowExceptionSelectionItemsNotSavedInAddItemsToExistingSelectionWhenInValidParameterAreProvided() {

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.SELECTION_ITEMS_NOT_SAVED.getKey() ) );

        FiltersDTO filter = prepareFilter();
        List< SelectionItemEntity > expectedSelectionItemEntityList = prepareListBySelectionItemEntity( expectedSelectionItemEntit );
        EasyMock.expect( selectionItemDAO.getObjectListByProperty( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedSelectionItemEntityList ).anyTimes();
        EasyMock.expect(
                        selectionDAO.getLatestObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
                .andReturn( null ).anyTimes();
        mockControl.replay();
        selectionManagerImpl.addSelectionItemsInExistingSelection( SELECTION_ID.toString(), filter );

    }

    /**
     * Should successfully remove items to existing selection when valid parameter are provided.
     */
    @Test
    public void shouldSuccessfullyRemoveItemsToExistingSelectionWhenValidParameterAreProvided() {

        prepareMockMethods();
        FiltersDTO filter = prepareFilter();
        SelectionResponseUI actual = selectionManagerImpl.removeSelectionItemsInExistingSelection( SELECTION_ID.toString(), filter );
        Assert.assertNotNull( actual );
    }

    /**
     * Should throw exception selection items not saved in remove items to existing selection when in valid parameter are provided.
     */
    @Test
    public void shouldThrowExceptionSelectionItemsNotSavedInRemoveItemsToExistingSelectionWhenInValidParameterAreProvided() {

        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.SELECTION_ITEMS_NOT_REMOVED.getKey() ) );
        FiltersDTO filter = prepareFilter();
        EasyMock.expect( selectionItemDAO.getObjectListByProperty( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( null ).anyTimes();

        mockControl.replay();
        selectionManagerImpl.removeSelectionItemsInExistingSelection( EasyMock.anyObject( EntityManager.class ), SELECTION_ID.toString(),
                filter );

    }

    /**
     * Should successfully return generic DTOUI.
     */
    @Test
    public void shouldSuccessfullyReturnGenericDTOUIWhenValidSelectionIdIsGiven() {
        List< TableColumn > genericDTOui = selectionManagerImpl.getGenericDTOUI( SELECTION_ID.toString(), null );
        Assert.assertNotNull( genericDTOui );
        Assert.assertFalse( genericDTOui.isEmpty() );

    }

    /**
     * Should successfully return generic dto list when valid parameter is provided.
     */
    @Test
    public void shouldSuccessfullyReturnGenericDtoListWhenValidParameterIsProvided() {

        prepareMockMethods();
        GenericDTO expected = createGenericDTOFromObjectEntity( PROJECT_ID, getDataObjectEntity() );
        filter.setFilteredRecords( 1L );
        FilteredResponse< ? > actual = ( FilteredResponse< ? > ) selectionManagerImpl.getGenericDTOList( USER_ID_RANDOM.toString(),
                SELECTION_ID.toString(), filter );
        for ( Object actualGenericDTO : actual.getData() ) {
            Assert.assertEquals( expected.getName(), ( ( GenericDTO ) actualGenericDTO ).getName() );
            Assert.assertEquals( expected.getId(), ( ( GenericDTO ) actualGenericDTO ).getId() );

        }
    }

    /**
     * Creates the generic DTO from object entity.
     *
     * @param projectId
     *         the project id
     * @param susEntity
     *         the sus entity
     *
     * @return the object
     */
    private GenericDTO createGenericDTOFromObjectEntity( UUID projectId, SuSEntity susEntity ) {
        GenericDTO genericDTO = null;
        if ( susEntity != null ) {
            genericDTO = new GenericDTO();
            genericDTO.setName( susEntity.getName() );
            genericDTO.setId( susEntity.getComposedId().getId() );
            genericDTO.setCreatedOn( susEntity.getCreatedOn() );
            genericDTO.setModifiedOn( susEntity.getModifiedOn() );
            genericDTO.setParentId( projectId );
        }
        return genericDTO;
    }

    /**
     * Prepare filter For table.
     *
     * @return the filters DTO
     */
    private FiltersDTO fillFilterForDataTable() {
        FiltersDTO filtersDTO = new FiltersDTO( ConstantsInteger.INTEGER_VALUE_ONE, ConstantsInteger.INTEGER_VALUE_ZERO,
                ConstantsInteger.INTEGER_VALUE_TEN );
        FilterColumn filterColumn = new FilterColumn();

        filterColumn.setName( SuSEntity.FIELD_NAME_MODIFIED_ON );
        filterColumn.setDir( ConstantsString.SORTING_DIRECTION_DESCENDING );
        filtersDTO.setColumns( Arrays.asList( filterColumn ) );
        filtersDTO.setFilteredRecords( ( long ) ConstantsInteger.INTEGER_VALUE_TEN );

        return filtersDTO;
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    private UserDTO getUser() {
        user = new UserDTO();
        user.setSurName( SUPER_USER_NAME );
        user.setFirstName( SUPER_USER_NAME );
        return user;
    }

    /**
     * A method to populate the DataObject Entity for Expected Result of test.
     *
     * @return projectEntity;
     */
    private DataObjectEntity fillDataObjectEntity() {

        DataObjectEntity dob = new DataObjectEntity();
        dob.setComposedId( new VersionPrimaryKey( DATA_OBJECT_ID, DEFAULT_VERSION_ID ) );
        dob.setName( DATA_OBJECT_NAME );
        dob.setTypeId( OBJECT_TYPE_ID );
        dob.setLifeCycleStatus( WIP_LIFECYCLE_STATUS_ID );

        return dob;
    }

    /**
     * Gets the data object entity.
     *
     * @return the data object entity
     */
    private SuSEntity getDataObjectEntity() {
        SuSEntity suSEntity = new DataObjectEntity();
        suSEntity.setLifeCycleStatus( LIFECYCLE );
        suSEntity.setName( ConstantsString.SIMUSPACE );
        suSEntity.setOwner( new UserEntity( OBJECT_IDENTITY_ID ) );
        suSEntity.setCreatedOn( new Date() );
        suSEntity.setModifiedOn( new Date() );
        suSEntity.setTypeId( PROJECT_ID );
        suSEntity.getComposedId().setId( DATA_OBJECT_ID );

        return suSEntity;
    }

    /**
     * Prepare filter.
     *
     * @return the filters DTO
     */
    private static FiltersDTO prepareFilter() {
        List< Object > items = new ArrayList<>();
        items.add( ITEM_ID_ONE );
        items.add( ITEM_ID_TWO );
        items.add( ITEM_ID_THREE );
        FiltersDTO filter = new FiltersDTO();
        filter.setItems( items );
        filter.setStart( 0 );
        return filter;
    }

    /**
     * Prepare selection entity.
     *
     * @return the selection entity
     */
    private static SelectionEntity PrepareSelectionEntity() {
        SelectionEntity selectionEntity = new SelectionEntity();
        selectionEntity.setId( SELECTION_ID );
        selectionEntity.setOrigin( ORIGIN_USER );
        return selectionEntity;
    }

    /**
     * Gets the selection item entity.
     *
     * @param selectionEntity
     *         the selection entity
     *
     * @return the selection item entity
     */
    private static SelectionItemEntity getSelectionItemEntity( SelectionEntity selectionEntity ) {
        SelectionItemEntity selectionItemEntity = new SelectionItemEntity();
        selectionItemEntity.setId( USER_ID_RANDOM );
        selectionItemEntity.setItem( ITEM_ID_ONE.toString() );
        selectionItemEntity.setSelectionEntity( selectionEntity );
        return selectionItemEntity;
    }

    /**
     * Fill user entity.
     *
     * @return the user entity
     */
    private static UserEntity fillUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserUid( UID );
        userEntity.setFirstName( UID );
        userEntity.setStatus( ACCOUNT_STATUS_ACTIVE );
        userEntity.setRestricted( false );
        userEntity.setId( USER_ID_RANDOM );
        userEntity.setPassword( FileUtils.getSha256CheckSum( PASSWD ) );
        return userEntity;
    }

    /**
     * Prepare list by selection entity.
     *
     * @param selectionEntity
     *         the selection entity
     *
     * @return the list
     */
    private static List< SelectionEntity > prepareListBySelectionEntity( SelectionEntity selectionEntity ) {
        List< SelectionEntity > selectionEntityList = new ArrayList<>();
        selectionEntityList.add( selectionEntity );
        selectionEntityList.add( selectionEntity );
        return selectionEntityList;
    }

    /**
     * Prepare list by selection item entity.
     *
     * @param selectionitemEntity
     *         the selectionitem entity
     *
     * @return the list
     */
    private List< SelectionItemEntity > prepareListBySelectionItemEntity( SelectionItemEntity selectionitemEntity ) {
        List< SelectionItemEntity > selectionItemEntityList = new ArrayList<>();
        selectionItemEntityList.add( selectionitemEntity );
        return selectionItemEntityList;
    }

    /**
     * Prepare mock methods.
     */
    private void prepareMockMethods() {
        EasyMock.expect( userCommonManager.getUserCommonDAO() ).andReturn( userCommonDAO );
        EasyMock.expect(
                        userCommonDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userEntity ).anyTimes();
        EasyMock.expect( selectionDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SelectionEntity.class ) ) )
                .andReturn( expectedSelectionEntity ).anyTimes();
        EasyMock.expect(
                        selectionItemDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SelectionItemEntity.class ) ) )
                .andReturn( expectedSelectionItemEntit ).anyTimes();
        EasyMock.expect( selectionDAO.getObjectListByProperty( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedSelectionEntityList ).anyTimes();
        EasyMock.expect( selectionDAO.saveOrUpdate( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
                .andReturn( expectedSelectionEntity ).anyTimes();
        EasyMock.expect(
                        selectionDAO.getLatestObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
                .andReturn( expectedSelectionEntity ).anyTimes();
        List< SelectionItemEntity > expectedSelectionItemEntityList = prepareListBySelectionItemEntity( expectedSelectionItemEntit );
        EasyMock.expect( selectionItemDAO.getObjectListByProperty( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedSelectionItemEntityList ).anyTimes();
        EasyMock.expect(
                        susDao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( getDataObjectEntity() ).anyTimes();
        List< SuSEntity > allObjects = new ArrayList<>();
        EasyMock.expect( susDao.getLatestNonDeletedObjectsByListOfIds( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
                .andReturn( allObjects ).anyTimes();
        EasyMock.expect( configManager.getDefaultStatusByObjectTypeId( EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( getStatusDto() ).anyTimes();
        EasyMock.expect( selectionItemDAO.getPaginatedSelectionByProperties( EasyMock.anyObject( EntityManager.class ),
                        EasyMock.anyString(), EasyMock.anyObject( UUID.class ), EasyMock.anyObject( FiltersDTO.class ) ) )
                .andReturn( expectedSelectionItemEntityList ).anyTimes();

        EasyMock.expect( lifeCycleManager.getOwnerVisibleStatusByPolicyId( EasyMock.anyString() ) )
                .andReturn( Arrays.asList( "553536c7-71ec-409d-8f48-ec779a98a68e", "d762f4ef-e706-4a44-a46d-6b334745e2e5",
                        "29d94aa2-62f2-4add-9233-2f4781545c35" ) )
                .anyTimes();
        EasyMock.expect( lifeCycleManager.getAnyVisibleStatusByPolicyId( EasyMock.anyString() ) )
                .andReturn( Arrays.asList( "d762f4ef-e706-4a44-a46d-6b334745e2e5", "29d94aa2-62f2-4add-9233-2f4781545c35" ) ).anyTimes();

        SuSObjectModel model = new SuSObjectModel();
        model.setLifeCycle( "ca836b74-d07c-4df2-8e57-c3d608523116" );
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( model )
                .anyTimes();

        EasyMock.expect( selectionDAO.getLatestObjectByIdWithLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( expectedSelectionEntity ).anyTimes();
        mockControl.replay();
    }

    /**
     * Gets the status dto.
     *
     * @return the status dto
     */
    private StatusDTO getStatusDto() {
        return new StatusDTO( WIP_LIFECYCLE_STATUS_ID, WIP_LIFECYCLE_STATUS_NAME );
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

}
