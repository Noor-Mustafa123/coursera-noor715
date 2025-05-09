package de.soco.software.simuspace.server.DAO.impl;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.server.dao.WorkflowDAO;
import de.soco.software.simuspace.server.dao.impl.WorkflowDAOImpl;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.data.activator.Activator;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.CategoryEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowCategoryEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;

/***
 * Test Cases for WorkflowDAOImpl
 *
 * @author Nosheen.Sharif
 */

@RunWith( PowerMockRunner.class )
@PrepareForTest( { Activator.class } )

public class WorkflowDAOImplTest {

    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The Constant WORK_FLOW_ID.
     */
    private static final UUID WORK_FLOW_ID = UUID.randomUUID();

    /**
     * The Constant TEST_DESCRIPTION.
     */
    private static final String TEST_DESCRIPTION = "Test Description";

    /**
     * The Constant WORK_FLOW_ENTITY_NAME.
     */
    private static final String WORK_FLOW_ENTITY_NAME = "test workflow entity name";

    /**
     * The Constant USER_ID.
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * The Constant COMMENTS.
     */
    private static final String COMMENTS = "Test comments";

    /**
     * The Constant VERSION_ID for workflow.
     */
    private static final int VERSION_ID = 1;

    /**
     * The Constant STATUS_ID for workflow status.
     */
    private static final int STATUS_ID = 1;

    /**
     * The Constant VERSION_NOT_EXIST.
     */
    private static final int VERSION_NOT_EXIST = -1;

    /**
     * The Constant SIM_ID.
     */
    private static final UUID SIM_ID = UUID.randomUUID();

    /**
     * The Constant CATEGORY_ID.
     */
    private static final UUID CATEGORY_ID = UUID.randomUUID();

    /**
     * The Constant CATEGORY_NAME.
     */
    private static final String CATEGORY_NAME = "Test Category";

    /**
     * The Constant is used for resultset size.
     */
    private static final long RESULT_SET_LIST_COUNT = 10;

    /**
     * The workflow dao referenece for mocking .
     */
    private WorkflowDAO workflowDao;

    /**
     * The work flow entity referenece for mocking.
     */
    private WorkflowEntity workFlowEntity;

    /**
     * The user entity referenece for mocking.
     */
    private UserEntity userEntity;

    /**
     * Generic Rule for the expected exception
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * Sets the up. Called before each method
     *
     * @throws Exception
     *             the exception
     */

    /**
     * Replay configration.
     */
    private void replayConfigration() {
        HibernateTestConfigration.mockControl().replay();

    }

    /**
     * Fill work flow entity.
     */
    private void fillWorkFlowEntity() {

        workFlowEntity = new WorkflowEntity();
        fillWorkflowPrimaryKey();
        workFlowEntity.setName( WORK_FLOW_ENTITY_NAME );
        fillUserEntity();
        workFlowEntity.setCreatedBy( userEntity );
        workFlowEntity.setIsPrivateWorkflow( false );
        workFlowEntity.setActive( true );
        workFlowEntity.setCreatedOn( new Date() );
        workFlowEntity.setDescription( TEST_DESCRIPTION );
        workFlowEntity.setKeyuser( userEntity );
        workFlowEntity.setCreatedBy( userEntity );

    }

    /**
     * Fill user entity.
     */
    private void fillUserEntity() {
        userEntity = new UserEntity();
        userEntity.setId( USER_ID );
    }

    /**
     * Fill workflow primary key.
     */
    private void fillWorkflowPrimaryKey() {
        workFlowEntity.setComposedId( new VersionPrimaryKey( WORK_FLOW_ID, VERSION_ID ) );
    }

    /**
     * Fill workflow category entity.
     *
     * @return the workflow category entity
     */
    private WorkflowCategoryEntity fillWorkflowCategoryEntity() {

        fillWorkFlowEntity();

        final WorkflowCategoryEntity entity = new WorkflowCategoryEntity();
        entity.setCategory( getCategoryEntity() );
        entity.setId( UUID.randomUUID() );
        entity.setWorkflow( workFlowEntity );

        return entity;
    }

    /**
     * Fill category entity.
     */
    private CategoryEntity getCategoryEntity() {
        final CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId( CATEGORY_ID );
        categoryEntity.setName( CATEGORY_NAME );
        return categoryEntity;
    }

    /**
     * Sets the up.
     *
     * @throws Exception
     *         the exception
     */
    @Before
    public void setUp() throws Exception {
        HibernateTestConfigration.setUp();
        PowerMockito.spy( Activator.class );
        PowerMockito.when( Activator.getSession() ).thenReturn( HibernateTestConfigration.getSession() );
        workflowDao = new WorkflowDAOImpl();

    }

    /* *************************************************************************
     * <getWorkflowById under test>
     * *************************************************************************
     */

    /* *************************************************************************
     * <getLastestVersionIdByWorkflow under test>
     * *************************************************************************
     */

    /* *************************************************************************
     * <getWorkflowVersionsById under test>
     * *************************************************************************
     */

    /**
     * Should return list of workflows W ith all versions by workflow id.
     */
    @Test
    public void shouldReturnListOfWorkflowsWithAllVersionsByWorkflowId() {
        fillWorkFlowEntity();
        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( WorkflowEntity.class );
        EasyMock.expect( c.list() ).andStubAnswer( () -> {
            final List< WorkflowEntity > newlist = new ArrayList<>();
            newlist.add( workFlowEntity );
            return newlist;
        } );
        replayConfigration();
        final List< WorkflowEntity > result = workflowDao.getWorkflowVersionsById( EasyMock.anyObject( EntityManager.class ),
                WORK_FLOW_ID );
        Assert.assertNotNull( result );
        for ( final WorkflowEntity actual : result ) {

            Assert.assertNotNull( actual.getComposedId().getId() );
            Assert.assertNotNull( actual.getComposedId().getVersionId() );
            Assert.assertNotNull( actual.getName() );
            Assert.assertNotNull( actual.getDescription() );
            Assert.assertNotNull( actual.getCreatedOn() );
        }
    }

    /* *************************************************************************
     * <getWorkflowByIdAndVersionId under test>
     * *************************************************************************
     */

    /**
     * Should return list of workflows W ith all versions by workflow id.
     */
    @Test
    public void shouldReturnWorkflowsByIdAndVersionId() {

        fillWorkFlowEntity();
        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( WorkflowEntity.class );
        EasyMock.expect( c.uniqueResult() ).andStubAnswer( () -> workFlowEntity );
        replayConfigration();
        final WorkflowEntity result = workflowDao.getWorkflowByIdAndVersionId( EasyMock.anyObject( EntityManager.class ), WORK_FLOW_ID,
                VERSION_ID );
        Assert.assertNotNull( result );

        Assert.assertEquals( workFlowEntity.getComposedId().getId(), result.getComposedId().getId() );
        Assert.assertEquals( workFlowEntity.getComposedId().getVersionId(), result.getComposedId().getVersionId() );
        Assert.assertEquals( workFlowEntity.getName(), result.getName() );
        Assert.assertEquals( workFlowEntity.getDescription(), result.getDescription() );
    }

    /* *************************************************************************
     * <updateWorkflow under test>
     * *************************************************************************
     */

    /**
     * Should save workflow with new version.
     */
    @Test
    public void shouldSaveWorkflowWithNewVersion() {
        fillWorkFlowEntity();
        final List< WorkflowEntity > list = new ArrayList<>();
        list.add( workFlowEntity );
        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( WorkflowEntity.class );
        EasyMock.expect( c.uniqueResult() ).andReturn( ConstantsInteger.INTEGER_VALUE_ONE );
        EasyMock.expect( c.list() ).andStubAnswer( () -> {
            final List< WorkflowEntity > newlist = new ArrayList<>();
            newlist.add( workFlowEntity );
            return newlist;
        } );
        replayConfigration();
        final WorkflowEntity entity = workflowDao.updateWorkflow( EasyMock.anyObject( EntityManager.class ), workFlowEntity );
        Assert.assertEquals( workFlowEntity.getComposedId().getId(), entity.getComposedId().getId() );
        Assert.assertEquals( workFlowEntity.getComposedId().getVersionId(), entity.getComposedId().getVersionId() );
        Assert.assertEquals( workFlowEntity.getName(), entity.getName() );
        Assert.assertEquals( workFlowEntity.getDescription(), entity.getDescription() );

    }

    /* *************************************************************************
     * <getWorkflowListByCategoryId under test>
     * *************************************************************************
     */

    /**
     * Should return workflow entity list when valid category id is passed.
     */
    @Test
    public void shouldReturnWorkflowEntityListWhenValidCategoryIdIsPassed() {
        fillWorkFlowEntity();

        final Query q = HibernateTestConfigration.getQuery();
        EasyMock.expect( q.list() ).andStubAnswer( () -> {
            final List< UUID > newlist = new ArrayList<>();
            newlist.add( 0, UUID.randomUUID() );
            return newlist;
        } );
        replayConfigration();
        final List< UUID > result = workflowDao.getWorkflowListByCategoryId( EasyMock.anyObject( EntityManager.class ), CATEGORY_ID );
        Assert.assertNotNull( result );
        Assert.assertFalse( result.isEmpty() );

        for ( final UUID actual : result ) {

            Assert.assertNotNull( actual );

        }

    }

    /* *************************************************************************
     * <getWorkflowListWithNoCategory under test>
     * *************************************************************************
     */

    /**
     * Should return workflow list by valid user id.
     */
    /* *************************************************************************
     * <getWorkflowList under test>
     * *************************************************************************
     */
    @Test
    public void shouldReturnWorkflowListByValidUserId() {
        fillWorkFlowEntity();

        final Query q = HibernateTestConfigration.getQuery();
        EasyMock.expect( q.list() ).andStubAnswer( () -> {
            final List< WorkflowEntity > newlist = new ArrayList<>();
            newlist.add( workFlowEntity );
            return newlist;
        } );
        replayConfigration();
        final List< WorkflowEntity > result = workflowDao.getWorkflowList( EasyMock.anyObject( EntityManager.class ) );
        for ( final WorkflowEntity actual : result ) {

            Assert.assertNotNull( actual.getComposedId().getId() );
            Assert.assertNotNull( actual.getComposedId().getVersionId() );
            Assert.assertNotNull( actual.getName() );
            Assert.assertNotNull( actual.getDescription() );
            Assert.assertNotNull( actual.getCreatedOn() );
        }

    }

    /**
     * Should return null if user id null is passed.
     */
    @Test
    public void shouldThrowExceptionIfUserIdNullIsPassed() {
        thrown.expect( SusDataBaseException.class );
        fillWorkFlowEntity();

        HibernateTestConfigration.getQuery();

        replayConfigration();
        workflowDao.getWorkflowIdsList( EasyMock.anyObject( EntityManager.class ) );

    }

    /* *************************************************************************
     * <getWorkflowVersionsWithoutDefinition under test>
     * *************************************************************************
     */

    /**
     * Should return version list with valid input.
     */
    @Test
    public void shouldReturnVersionListWithValidInput() {
        fillWorkFlowEntity();

        HibernateTestConfigration.getQuery();
        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( WorkflowEntity.class );

        EasyMock.expect( c.list() ).andStubAnswer( () -> {
            final List< WorkflowEntity > newlist = new ArrayList<>();
            newlist.add( workFlowEntity );
            return newlist;
        } );

        replayConfigration();
        final List< WorkflowEntity > result = workflowDao.getWorkflowVersionsWithoutDefinition( EasyMock.anyObject( EntityManager.class ),
                WORK_FLOW_ID );
        for ( final WorkflowEntity actual : result ) {

            Assert.assertNotNull( actual.getComposedId().getId() );
            Assert.assertNotNull( actual.getComposedId().getVersionId() );
            Assert.assertNotNull( actual.getName() );
            Assert.assertNotNull( actual.getDescription() );
            Assert.assertNotNull( actual.getCreatedOn() );
        }

    }

    /**
     * Should throw exception when invalid input is given to get workflow versions list.
     */
    @Test
    public void shouldThrowExceptionWhenInvalidInputIsGivenToGetWorkflowVersionsList() {
        thrown.expect( SusDataBaseException.class );
        HibernateTestConfigration.getQuery();

        replayConfigration();
        final List< WorkflowEntity > result = workflowDao.getWorkflowVersionsWithoutDefinition( EasyMock.anyObject( EntityManager.class ),
                null );
        Assert.assertNull( result );

    }

    /* *************************************************************************
     * <isAlreadyWipWorkflowExist under test>
     * *************************************************************************
     */

    /**
     * Should return true if wip exist by valid workflow id.
     */
    @Test
    public void shouldReturnTrueIfWipExistByValidWorkflowId() {
        fillWorkFlowEntity();

        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( WorkflowEntity.class );
        EasyMock.expect( c.uniqueResult() ).andReturn( workFlowEntity ).anyTimes();
        replayConfigration();
        final boolean result = workflowDao.isAlreadyWipWorkflowExist( EasyMock.anyObject( EntityManager.class ), USER_ID, WORK_FLOW_ID );
        Assert.assertTrue( result );

    }

    /**
     * Should return false by invalid input.
     */
    @Test
    public void shouldReturnFalseByInvalidInput() {
        fillWorkFlowEntity();

        HibernateTestConfigration.prepareAndReturnCriteria( WorkflowEntity.class );
        replayConfigration();
        final boolean result = workflowDao.isAlreadyWipWorkflowExist( EasyMock.anyObject( EntityManager.class ), null, null );
        Assert.assertFalse( result );

    }

    /**
     * Should return false if no wip exist by valid input.
     */
    @Test
    public void shouldReturnFalseIfNoWipExistByValidInput() {
        fillWorkFlowEntity();

        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( WorkflowEntity.class );
        EasyMock.expect( c.uniqueResult() ).andReturn( null ).anyTimes();
        replayConfigration();
        final boolean result = workflowDao.isAlreadyWipWorkflowExist( EasyMock.anyObject( EntityManager.class ), USER_ID, WORK_FLOW_ID );
        Assert.assertFalse( result );

    }

    /* *************************************************************************
     * <isWorkflowExistInCategory under test>
     * *************************************************************************
     */

    /**
     * Should return true if workflow exist in category.
     */
    @Test
    public void shouldReturnTrueIfWorkflowExistInCategory() {

        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( WorkflowCategoryEntity.class );
        EasyMock.expect( c.uniqueResult() ).andReturn( fillWorkflowCategoryEntity() ).anyTimes();
        replayConfigration();
        final boolean result = workflowDao.isWorkflowExistInCategory( EasyMock.anyObject( EntityManager.class ), workFlowEntity,
                CATEGORY_ID );
        Assert.assertTrue( result );

    }

    /**
     * Should return false if workflow not exist in category.
     */
    @Test
    public void shouldReturnFalseIfWorkflowNotExistInCategory() {
        thrown.expect( SusDataBaseException.class );
        HibernateTestConfigration.prepareAndReturnCriteria( WorkflowCategoryEntity.class );

        replayConfigration();
        workflowDao.isWorkflowExistInCategory( EasyMock.anyObject( EntityManager.class ), workFlowEntity, CATEGORY_ID );

    }

    /**
     * Should throw exception for workflow in category if invalid input is given.
     */
    @Test
    public void shouldThrowExceptionForWorkflowInCategoryIfInvalidInputIsGiven() {
        thrown.expect( SusDataBaseException.class );
        HibernateTestConfigration.prepareAndReturnCriteria( WorkflowCategoryEntity.class );

        replayConfigration();
        workflowDao.isWorkflowExistInCategory( EasyMock.anyObject( EntityManager.class ), null, null );

    }

    /* *************************************************************************
     * <getTotalJobCountByWorkflowIdAndVersion under test>
     * *************************************************************************
     */

    /**
     * Should return zero when invalid workflow id is given to get total jobs count.
     */
    @Test
    public void shouldReturnZeroWhenInvalidWorkflowIdIsGivenToGetTotalJobsCount() {

        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( JobEntity.class );
        EasyMock.expect( c.uniqueResult() ).andReturn( 0 ).anyTimes();
        replayConfigration();
        final long result = workflowDao.getTotalJobCountByWorkflowIdAndVersion( EasyMock.anyObject( EntityManager.class ), null, 0 );
        Assert.assertEquals( ConstantsInteger.INTEGER_VALUE_ZERO, result );

    }

    /**
     * Should return count when valid input is given to get total jobs count.
     */
    @Test
    public void shouldReturnCountWhenValidInputIsGivenToGetTotalJobsCount() {
        final long dummy_count = 10;
        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( JobEntity.class );
        EasyMock.expect( c.uniqueResult() ).andReturn( dummy_count ).anyTimes();
        replayConfigration();
        final long result = workflowDao.getTotalJobCountByWorkflowIdAndVersion( EasyMock.anyObject( EntityManager.class ), WORK_FLOW_ID,
                VERSION_ID );
        Assert.assertTrue( result > 0 );

    }

    /* *************************************************************************
     * <getCompletedJobCountByWorkflowIdAndVersion under test>
     * *************************************************************************
     */

    /**
     * Should return zero when invalid workflow id is given to get total jobs count.
     */
    @Test
    public void shouldReturnZeroWhenInvalidWorkflowIdIsGivenToGetCompletedJobsCount() {

        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( JobEntity.class );
        EasyMock.expect( c.uniqueResult() ).andReturn( 0 ).anyTimes();
        replayConfigration();
        final long result = workflowDao.getCompletedJobCountByWorkflowIdAndVersion( EasyMock.anyObject( EntityManager.class ), null, 0 );
        Assert.assertEquals( ConstantsInteger.INTEGER_VALUE_ZERO, result );

    }

    /**
     * Should return count when valid input is given to get total jobs count.
     */
    @Test
    public void shouldReturnCountWhenValidInputIsGivenToGetCompletedJobsCount() {

        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( JobEntity.class );
        EasyMock.expect( c.uniqueResult() ).andReturn( RESULT_SET_LIST_COUNT ).anyTimes();
        replayConfigration();
        final long result = workflowDao.getCompletedJobCountByWorkflowIdAndVersion( EasyMock.anyObject( EntityManager.class ), WORK_FLOW_ID,
                VERSION_ID );
        Assert.assertTrue( result > 0 );

    }

}