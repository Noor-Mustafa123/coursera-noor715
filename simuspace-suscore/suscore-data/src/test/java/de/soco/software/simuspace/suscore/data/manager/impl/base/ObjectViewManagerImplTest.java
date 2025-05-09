/*
 *
 */

package de.soco.software.simuspace.suscore.data.manager.impl.base;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.common.dao.ObjectViewDAO;
import de.soco.software.simuspace.suscore.data.common.dao.UserCommonDAO;
import de.soco.software.simuspace.suscore.data.entity.ObjectViewEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class ObjectViewManagerImplTest for testing public methods of ObjectViewManagerImpl.
 *
 * @author zeeshan jamal
 */
public class ObjectViewManagerImplTest {

    /**
     * The view manager impl.
     */
    private ObjectViewManagerImpl objectViewManagerImpl;

    /**
     * The view DAO.
     */
    private ObjectViewDAO objectViewDAO;

    /**
     * The user common DAO.
     */
    private UserCommonDAO userCommonDAO;

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
     * The Constant OBJECT_VIEW_NAME.
     */
    public static final String OBJECT_VIEW_NAME = "testView";

    /**
     * The Constant OBJECT_VIEW_KEY.
     */
    public static final String OBJECT_VIEW_KEY = "object-key";

    /**
     * The Constant OBJECT_VIEW_JSON.
     */
    public static final String OBJECT_VIEW_JSON = "{}";

    /**
     * The Constant OBJECT_VIEW_TYPE.
     */
    public static final String OBJECT_VIEW_TYPE = "table";

    /**
     * The Constant INVALID_OBJECT_VIEW_TYPE.
     */
    public static final String INVALID_OBJECT_VIEW_TYPE = "testViewType";

    /**
     * The Constant IS_DEFAULT_VIEW.
     */
    public static final boolean IS_DEFAULT_VIEW = false;

    /**
     * The Constant OBJECT_VIEW_CREATED_BY_USER_ID.
     */
    public static final UUID OBJECT_VIEW_CREATED_BY_USER_ID = UUID.randomUUID();

    /**
     * The Constant OBJECT_ID.
     */
    public static final UUID OBJECT_ID = UUID.randomUUID();

    /**
     * The Constant OBJECT_VIEW_COMPOSITE_OBJECT.
     */
    public static final VersionPrimaryKey OBJECT_VIEW_COMPOSITE_OBJECT = new VersionPrimaryKey( OBJECT_ID,
            ConstantsInteger.INTEGER_VALUE_ONE );

    /**
     * To initialize the objects and mocking objects.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        objectViewManagerImpl = new ObjectViewManagerImpl();
        objectViewDAO = mockControl.createMock( ObjectViewDAO.class );
        userCommonDAO = mockControl.createMock( UserCommonDAO.class );

        objectViewManagerImpl.setObjectViewDAO( objectViewDAO );
        objectViewManagerImpl.setUserCommonDAO( userCommonDAO );
    }

    /**
     * Should successfully add object view when valid object view type is provided.
     */
    @Test
    public void shouldSuccessfullyAddObjectViewWhenValidObjectViewTypeIsProvided() {
        ObjectViewEntity viewEntity = prepareObjectViewEntity();
        ObjectViewDTO expected = prepareObjectViewDTO( ConstantsString.EMPTY_STRING, OBJECT_VIEW_NAME, OBJECT_VIEW_KEY, OBJECT_VIEW_JSON,
                null, OBJECT_VIEW_TYPE, IS_DEFAULT_VIEW );
        EasyMock.expect(
                        userCommonDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( viewEntity.getCreatedBy() );
        EasyMock.expect(
                        objectViewDAO.saveOrUpdate( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( ObjectViewEntity.class ) ) )
                .andReturn( viewEntity );
        EasyMock.expect(
                        objectViewDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( viewEntity );
        mockControl.replay();
        ObjectViewDTO actual = objectViewManagerImpl.saveOrUpdateObjectView( EasyMock.anyObject( EntityManager.class ), expected,
                OBJECT_VIEW_CREATED_BY_USER_ID.toString() );
        Assert.assertEquals( expected, actual );
        Assert.assertEquals( expected.getName(), actual.getName() );
        Assert.assertEquals( expected.getObjectViewType(), actual.getObjectViewType() );
        Assert.assertEquals( expected.getObjectViewKey(), actual.getObjectViewKey() );
        Assert.assertEquals( expected.getObjectViewJson(), actual.getObjectViewJson() );
    }

    /**
     * Show throw exception when object view with the same name is saved again.
     */
    @Test
    public void showThrowExceptionWhenObjectViewWithTheSameNameIsSavedAgain() {
        ObjectViewDTO expected = prepareObjectViewDTO( ConstantsString.EMPTY_STRING, OBJECT_VIEW_NAME, OBJECT_VIEW_KEY, OBJECT_VIEW_JSON,
                null, OBJECT_VIEW_TYPE, IS_DEFAULT_VIEW );
        EasyMock.expect( objectViewDAO.isObjectViewAlreadyExists( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString(), EasyMock.anyObject( UUID.class ), EasyMock.anyString() ) ).andReturn( true );
        mockControl.replay();
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.VIEW_ALREADY_EXIST.getKey(), expected.getName() ) );
        objectViewManagerImpl.saveOrUpdateObjectView( expected, OBJECT_VIEW_CREATED_BY_USER_ID.toString() );
    }

    /**
     * Show throw exception when object view with the same name is provided on update.
     */
    @Test
    public void showThrowExceptionWhenObjectViewWithTheSameNameIsProvidedOnUpdate() {
        ObjectViewDTO expected = prepareObjectViewDTO( OBJECT_VIEW_COMPOSITE_OBJECT.getId().toString(), OBJECT_VIEW_KEY, OBJECT_VIEW_KEY,
                OBJECT_VIEW_JSON, null, OBJECT_VIEW_TYPE, IS_DEFAULT_VIEW );
        ObjectViewEntity viewEntity = prepareObjectViewEntity();
        EasyMock.expect(
                        objectViewDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( viewEntity );
        EasyMock.expect( objectViewDAO.isObjectViewAlreadyExists( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString(), EasyMock.anyObject( UUID.class ), EasyMock.anyString() ) ).andReturn( true );
        mockControl.replay();
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.VIEW_ALREADY_EXIST.getKey(), expected.getName() ) );
        objectViewManagerImpl.saveOrUpdateObjectView( EasyMock.anyObject( EntityManager.class ), expected,
                OBJECT_VIEW_CREATED_BY_USER_ID.toString() );
    }

    /**
     * Should successfully update object view when valid object view type is provided.
     */
    @Test
    public void shouldSuccessfullyUpdateObjectViewWhenValidObjectViewTypeIsProvided() {
        ObjectViewEntity viewEntity = prepareObjectViewEntity();
        ObjectViewDTO expected = prepareObjectViewDTO( OBJECT_VIEW_COMPOSITE_OBJECT.getId().toString(), OBJECT_VIEW_NAME, OBJECT_VIEW_KEY,
                OBJECT_VIEW_JSON, null, OBJECT_VIEW_TYPE, IS_DEFAULT_VIEW );
        EasyMock.expect(
                        userCommonDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( viewEntity.getCreatedBy() );
        EasyMock.expect(
                        objectViewDAO.saveOrUpdate( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( ObjectViewEntity.class ) ) )
                .andReturn( viewEntity );
        EasyMock.expect(
                        objectViewDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( viewEntity );
        mockControl.replay();
        ObjectViewDTO actual = objectViewManagerImpl.saveOrUpdateObjectView( EasyMock.anyObject( EntityManager.class ), expected,
                OBJECT_VIEW_CREATED_BY_USER_ID.toString() );
        Assert.assertEquals( expected, actual );
        Assert.assertEquals( expected.getName(), actual.getName() );
        Assert.assertEquals( expected.getObjectViewType(), actual.getObjectViewType() );
        Assert.assertEquals( expected.getObjectViewKey(), actual.getObjectViewKey() );
        Assert.assertEquals( expected.getObjectViewJson(), actual.getObjectViewJson() );
    }

    /**
     * Should not successfully add object view and throw exception when in valid object view type is provided.
     */
    @Test
    public void shouldNotSuccessfullyAddObjectViewAndThrowExceptionWhenInValidObjectViewTypeIsProvided() {
        ObjectViewDTO expected = prepareObjectViewDTO( OBJECT_VIEW_COMPOSITE_OBJECT.getId().toString(), OBJECT_VIEW_NAME, OBJECT_VIEW_KEY,
                OBJECT_VIEW_JSON, null, INVALID_OBJECT_VIEW_TYPE, IS_DEFAULT_VIEW );
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.VIEW_TYPE_NOT_SUPPORTED.getKey(), INVALID_OBJECT_VIEW_TYPE ) );
        objectViewManagerImpl.saveOrUpdateObjectView( expected, OBJECT_VIEW_CREATED_BY_USER_ID.toString() );

    }

    /**
     * Should successfully get object view list when valid object view key is provided.
     */
    @Test
    public void shouldSuccessfullyGetObjectViewListWhenValidObjectViewKeyIsProvided() {
        List< ObjectViewEntity > expected = new ArrayList<>();
        expected.add( prepareObjectViewEntity() );
        EasyMock.expect( objectViewDAO.getUserObjectViewsByKey( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ),
                EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( expected );
        mockControl.replay();
        List< ObjectViewDTO > actual = objectViewManagerImpl.getUserObjectViewsByKey( EasyMock.anyObject( EntityManager.class ),
                OBJECT_VIEW_KEY, OBJECT_VIEW_CREATED_BY_USER_ID.toString(), null );
        Assert.assertEquals( expected.size(), actual.size() );
        Assert.assertEquals( expected.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getId().toString(),
                actual.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getId() );
        Assert.assertEquals( expected.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getObjectViewName(),
                actual.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getName() );
        Assert.assertEquals( expected.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getObjectViewType(),
                actual.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getObjectViewType() );
        Assert.assertEquals( expected.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getObjectViewKey(),
                actual.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getObjectViewKey() );
        Assert.assertEquals( expected.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getObjectViewJson(),
                actual.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getObjectViewJson() );
    }

    /**
     * Should successfully set current object view as default view when valid view id is provided.
     */
    @Test
    public void shouldSuccessfullySetCurrentObjectViewAsDefaultViewWhenValidViewIdIsProvided() {
        ObjectViewEntity viewEntity = prepareObjectViewEntity();
        ObjectViewDTO expected = prepareObjectViewDTO( OBJECT_VIEW_COMPOSITE_OBJECT.getId().toString(), OBJECT_VIEW_NAME, OBJECT_VIEW_KEY,
                OBJECT_VIEW_JSON, null, OBJECT_VIEW_TYPE, IS_DEFAULT_VIEW );
        EasyMock.expect(
                        userCommonDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( viewEntity.getCreatedBy() ).anyTimes();
        EasyMock.expect( objectViewDAO.getUserDefaultObjectViewByKey( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( UUID.class ), EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( viewEntity );
        EasyMock.expect(
                        objectViewDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( viewEntity );
        EasyMock.expect(
                        objectViewDAO.saveOrUpdate( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( ObjectViewEntity.class ) ) )
                .andReturn( viewEntity ).anyTimes();
        mockControl.replay();
        ObjectViewDTO actual = objectViewManagerImpl.saveDefaultObjectView( EasyMock.anyObject( EntityManager.class ),
                OBJECT_VIEW_COMPOSITE_OBJECT.getId(), OBJECT_VIEW_CREATED_BY_USER_ID.toString(), OBJECT_VIEW_TYPE, null );
        Assert.assertEquals( expected, actual );
        Assert.assertEquals( expected.getId().toString(), actual.getId() );
        Assert.assertEquals( expected.getName(), actual.getName() );
        Assert.assertEquals( expected.getObjectViewType(), actual.getObjectViewType() );
        Assert.assertEquals( expected.getObjectViewKey(), actual.getObjectViewKey() );
        Assert.assertEquals( expected.getObjectViewJson(), actual.getObjectViewJson() );
    }

    /**
     * Should successfully delete object view when valid view id is given.
     */
    @Test
    public void shouldSuccessfullyDeleteObjectViewWhenValidViewIdIsGiven() {
        ObjectViewEntity viewEntity = prepareObjectViewEntity();
        EasyMock.expect(
                        objectViewDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( viewEntity );
        mockControl.replay();
        Assert.assertTrue(
                objectViewManagerImpl.deleteObjectView( EasyMock.anyObject( EntityManager.class ), OBJECT_VIEW_COMPOSITE_OBJECT.getId() ) );
    }

    /**
     * Should not successfully delete object view when in valid view id is given.
     */
    @Test
    public void shouldNotSuccessfullyDeleteObjectViewWhenInValidViewIdIsGiven() {
        Assert.assertFalse(
                objectViewManagerImpl.deleteObjectView( EasyMock.anyObject( EntityManager.class ), OBJECT_VIEW_COMPOSITE_OBJECT.getId() ) );
    }

    /**
     * Should successfully get view when valid view id is provided.
     */
    @Test
    public void shouldSuccessfullyGetViewWhenValidViewIdIsProvided() {
        ObjectViewEntity viewEntity = prepareObjectViewEntity();
        ObjectViewDTO expected = prepareObjectViewDTO( OBJECT_VIEW_COMPOSITE_OBJECT.getId().toString(), OBJECT_VIEW_NAME, OBJECT_VIEW_KEY,
                OBJECT_VIEW_JSON, null, OBJECT_VIEW_TYPE, IS_DEFAULT_VIEW );
        EasyMock.expect(
                        objectViewDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( viewEntity );
        mockControl.replay();
        ObjectViewDTO actual = objectViewManagerImpl.getObjectViewById( EasyMock.anyObject( EntityManager.class ),
                OBJECT_VIEW_COMPOSITE_OBJECT.getId() );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should not successfully get object view when in valid view id is provided.
     */
    @Test
    public void shouldNotSuccessfullyGetObjectViewWhenInValidViewIdIsProvided() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
        objectViewManagerImpl.getObjectViewById( OBJECT_VIEW_COMPOSITE_OBJECT.getId() );
    }

    /**
     * Prepare object view DTO.
     *
     * @param viewId
     *         the view id
     * @param objectViewName
     *         the object view name
     * @param objectViewKey
     *         the object view key
     * @param objectViewJson
     *         the object view json
     * @param objectViewCreatedBy
     *         the object view created by
     * @param objectViewType
     *         the object view type
     * @param defaultView
     *         the default view
     *
     * @return the object view DTO
     */
    public ObjectViewDTO prepareObjectViewDTO( String viewId, String objectViewName, String objectViewKey, String objectViewJson,
            UserDTO objectViewCreatedBy, String objectViewType, boolean defaultView ) {
        ObjectViewDTO objectViewDTO = new ObjectViewDTO( viewId.toString(), objectViewName, objectViewKey, objectViewJson,
                objectViewCreatedBy, objectViewType, defaultView );
        objectViewDTO.setObjectId( OBJECT_ID.toString() );
        return objectViewDTO;
    }

    /**
     * Prepare object view entity.
     *
     * @return the object view entity
     */
    public ObjectViewEntity prepareObjectViewEntity() {
        ObjectViewEntity objectViewEntity = new ObjectViewEntity( OBJECT_ID, OBJECT_VIEW_NAME, OBJECT_VIEW_KEY, OBJECT_VIEW_JSON, null,
                OBJECT_VIEW_TYPE, IS_DEFAULT_VIEW );
        objectViewEntity.setObjectId( OBJECT_ID );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( OBJECT_VIEW_CREATED_BY_USER_ID );
        objectViewEntity.setCreatedBy( userEntity );
        objectViewEntity.setModifiedBy( userEntity );
        return objectViewEntity;
    }

}
