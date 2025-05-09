package de.soco.software.simuspace.server.manager.impl;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.core.classloader.annotations.PrepareForTest;

import de.soco.software.simuspace.server.dao.CategoryDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.CategoryEntity;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.workflow.model.Category;
import de.soco.software.simuspace.workflow.model.impl.CategoryImpl;

/**
 * Test Cases for CategoryManagerImpl class.
 */
@PrepareForTest( CategoryManagerImpl.class )
public class CategoryManagerImplTest {

    /**
     * The category manager impl reference
     */
    private CategoryManagerImpl manager;

    /**
     * The category dao reference
     */
    private CategoryDAO categoryDAO;

    /**
     * The category impl reference
     */
    private Category categoryImpl;

    /**
     * The category entity reference
     */
    private CategoryEntity categoryEntity;

    /**
     * The Constant WORK_FLOW_ID.
     */
    private static final UUID WORK_FLOW_ID = UUID.randomUUID();

    /**
     * The Constant CATEGORY_NAME.
     */
    private static final String CATEGORY_NAME = "Test Category";

    /**
     * The Constant INVALID_CATEGORY_NAME.
     */
    private static final String INVALID_CATEGORY_NAME = "@Test Category";

    /**
     * The Constant LENGTH_65_STRING.
     */
    private static final String LENGTH_65_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890123";

    /**
     * The Constant CATEGORY_ID.
     */
    private static final UUID CATEGORY_ID = UUID.randomUUID();

    /**
     * The Constant DEFAULT_BE_USER_ID.
     */
    private static final UUID DEFAULT_BE_USER_ID = UUID.randomUUID();

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private LicenseManager licenseManager;

    /**
     * The Constant mockControl.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * To initialize the objects and mocking objects
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        manager = new CategoryManagerImpl();
        categoryDAO = mockControl.createMock( CategoryDAO.class );
        licenseManager = mockControl.createMock( LicenseManager.class );
        manager.setCategoryDAO( categoryDAO );
        manager.setLicenseManager( licenseManager );
    }

    /**
     * Should add category when valid category impl is given.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldAddCategoryWhenValidCategoryImplIsGiven() throws SusException {
        fillCategoryImpl();
        fillCategoryEntity();
        EasyMock.expect( categoryDAO.addCategory( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( CategoryEntity.class ) ) )
                .andReturn( categoryEntity ).anyTimes();
        mockControl.replay();
        final Category expected = manager.addCategory( DEFAULT_BE_USER_ID, categoryImpl );
        Assert.assertNotNull( expected );
        Assert.assertEquals( expected.getId(), categoryImpl.getId() );
        Assert.assertEquals( expected.getName(), categoryImpl.getName() );
    }

    /**
     * Should throw exception when category name length is more than Sixty Four.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldThrowExceptionWhenCategoryNameLengthIsMoreThanSixtyFour() throws SusException {
        fillCategoryImpl();
        fillCategoryEntity();
        thrown.expect( SusException.class );
        categoryImpl.setName( RandomStringUtils.randomAlphabetic( ConstantsLength.STANDARD_NAME_LENGTH + 1 ) );

        mockControl.replay();
        manager.addCategory( DEFAULT_BE_USER_ID, categoryImpl );
    }

    /**
     * Should throw exception when invalid category name is provided.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldThrowExceptionWhenInvalidCategoryNameIsProvided() throws SusException {
        fillCategoryImpl();
        fillCategoryEntity();
        thrown.expect( SusException.class );
        categoryImpl.setName( INVALID_CATEGORY_NAME );

        mockControl.replay();
        manager.addCategory( DEFAULT_BE_USER_ID, categoryImpl );
    }

    /**
     * Should update category when valid category impl is given.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldUpdateCategoryWhenValidCategoryImplIsGiven() throws SusException {
        fillCategoryImpl();
        fillCategoryEntity();
        EasyMock.expect( categoryDAO.getCategoryById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( categoryEntity ).anyTimes();
        EasyMock.expect(
                        categoryDAO.updateCategory( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( CategoryEntity.class ) ) )
                .andReturn( categoryEntity ).anyTimes();
        mockControl.replay();
        final Category expected = manager.updateCategory( DEFAULT_BE_USER_ID, categoryImpl );
        Assert.assertNotNull( expected );
        Assert.assertEquals( expected.getId(), categoryImpl.getId() );
        Assert.assertEquals( expected.getName(), categoryImpl.getName() );
    }

    /**
     * Should throw exception when category does not exist for update.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldThrowExceptionWhenCategoryDoesNotExistForUpdate() throws SusException {
        fillCategoryImpl();
        fillCategoryEntity();
        thrown.expect( SusException.class );
        EasyMock.expect( categoryDAO.getCategoryById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( null ).anyTimes();

        mockControl.replay();
        manager.updateCategory( DEFAULT_BE_USER_ID, categoryImpl );
    }

    /**
     * Should return true when category is deleted succesfully.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldReturnTrueWhenCategoryIsDeletedSuccesfully() throws SusException {
        fillCategoryEntity();
        EasyMock.expect( categoryDAO.getCategoryById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( categoryEntity ).anyTimes();
        EasyMock.expect( categoryDAO.removeCategory( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( true ).anyTimes();

        mockControl.replay();
        final boolean expected = manager.deleteCategory( DEFAULT_BE_USER_ID, CATEGORY_ID.toString() );
        Assert.assertTrue( expected );

    }

    /**
     * Should return expection if In valid UUID category id is given.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldReturnExpectionIfInValidUUIDCategoryIdIsGiven() throws SusException {
        final String invalidUUID = "124-143-1234";
        fillCategoryEntity();
        thrown.expect( SusException.class );
        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.INVALID_UUID, invalidUUID ) );

        mockControl.replay();
        manager.deleteCategory( DEFAULT_BE_USER_ID, invalidUUID );
    }

    /**
     * Should return expection if category id does not exist for delete.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldReturnExpectionIfCategoryIdDoesNotExistForDelete() throws SusException {

        fillCategoryEntity();
        thrown.expect( SusException.class );
        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.NO_CATEGORY_EXIST_WITH_ID, CATEGORY_ID ) );
        EasyMock.expect( categoryDAO.getCategoryById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( null ).anyTimes();

        mockControl.replay();
        manager.deleteCategory( DEFAULT_BE_USER_ID, CATEGORY_ID.toString() );
    }

    /**
     * Should return category entity if valid category idis given.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldReturnCategoryEntityIfValidCategoryIdisGiven() throws SusException {

        fillCategoryEntity();
        fillCategoryImpl();
        EasyMock.expect( categoryDAO.getCategoryById( EasyMock.anyObject( EntityManager.class ), CATEGORY_ID ) ).andReturn( categoryEntity )
                .anyTimes();

        mockControl.replay();
        final Category expected = manager.getCategoryById( CATEGORY_ID.toString() );
        Assert.assertNotNull( expected );
        Assert.assertEquals( expected.getId(), categoryImpl.getId() );
        Assert.assertEquals( expected.getName(), categoryImpl.getName() );
    }

    /**
     * Should return expection when get category by id does not exist.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldReturnExpectionWhenGetCategoryByIdDoesNotExist() throws SusException {

        fillCategoryEntity();
        thrown.expect( SusException.class );
        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.NO_CATEGORY_EXIST_WITH_ID, CATEGORY_ID ) );
        EasyMock.expect( categoryDAO.getCategoryById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( null ).anyTimes();

        mockControl.replay();
        manager.getCategoryById( CATEGORY_ID.toString() );
    }

    /**
     * Should return category impl by id with valid category id.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldReturnCategoryImplByIdWithValidCategoryId() throws SusException {
        final int category_dummy_count = 5;
        fillCategoryEntity();
        fillCategoryImpl();
        EasyMock.expect( categoryDAO.getCategoryById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( categoryEntity ).anyTimes();
        EasyMock.expect(
                        categoryDAO.getWorkflowCountByCategoryId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( category_dummy_count ).anyTimes();

        mockControl.replay();
        final Category expected = manager.getCategoryById( CATEGORY_ID.toString() );
        Assert.assertEquals( expected.getId(), categoryImpl.getId() );
        Assert.assertEquals( expected.getName(), categoryImpl.getName() );
        Assert.assertTrue( expected.getCount() == category_dummy_count );
    }

    /**
     * Should return exception when invalid workflow id is given.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldReturnExceptionWhenInvalidWorkflowIdIsGiven() throws SusException {
        final String invalidUUID = "124-143-1234";

        thrown.expect( SusException.class );
        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID, invalidUUID ) );
        mockControl.replay();
        manager.getCategoryListByWorkflowId( invalidUUID );

    }

    /**
     * Should return category impl list when Dao return filled list.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldReturnCategoryImplListWhenDaoreturnFilledList() throws SusException {

        fillCategoryEntity();
        fillCategoryImpl();
        final List< CategoryEntity > actual = new ArrayList<>();
        actual.add( categoryEntity );
        EasyMock.expect( categoryDAO.getCategories( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( actual ).anyTimes();

        mockControl.replay();
        final List< Category > expected = manager.getCategories();
        Assert.assertNotNull( expected );
        Assert.assertEquals( expected.size(), actual.size() );

        for ( final Category category : expected ) {
            Assert.assertNotNull( category.getId() );
            Assert.assertNotNull( category.getName() );
        }

    }

    /**
     * Fill category entity.
     */
    private void fillCategoryEntity() {
        categoryEntity = new CategoryEntity();
        categoryEntity.setId( CATEGORY_ID );
        categoryEntity.setName( CATEGORY_NAME );

    }

    /**
     * Fill category impl.
     */
    private void fillCategoryImpl() {
        categoryImpl = new CategoryImpl();
        categoryImpl.setId( CATEGORY_ID.toString() );
        categoryImpl.setName( CATEGORY_NAME );
    }

}
