package de.soco.software.simuspace.server.DAO.impl;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.server.dao.impl.WorkflowCategoryDAOImpl;
import de.soco.software.simuspace.suscore.data.activator.Activator;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.CategoryEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowCategoryEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;

/**
 * Test Cases for WorkflowCategoryDAOImpl.
 *
 * @author Nosheen.Sharif
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { Activator.class } )
public class WorkflowCategoryDAOImplTest {

    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The workflow category dao reference.
     */
    private WorkflowCategoryDAOImpl workflowCategoryDao;

    /**
     * The work flow entity.
     */
    private WorkflowEntity workFlowEntity;

    private CategoryEntity categoryEntity;

    /**
     * The user entity.
     */
    private UserEntity userEntity;

    /**
     * The dummy Constant CATEGORY_ID.
     */
    private static final UUID CATEGORY_ID = UUID.randomUUID();

    /**
     * The dummy Constant ID.
     */
    private static final UUID ID = UUID.randomUUID();

    /**
     * The dummy Constant WORK_FLOW_ID.
     */
    private static final UUID WORK_FLOW_ID = UUID.randomUUID();

    /**
     * The dummy Constant USER_ID.
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * The dummy Constant VERSION_ID.
     */
    private static final int VERSION_ID = 1;

    /**
     * The dummy Constant SIM_ID.
     */
    private static final UUID SIM_ID = UUID.randomUUID();

    /**
     * The dummy Constant TEST_DESCRIPTION.
     */
    private static final String TEST_DESCRIPTION = "Test Description";

    /**
     * The dummy Constant COMMENTS.
     */
    private static final String COMMENTS = "Test comments";

    /**
     * The dummy Constant WORK_FLOW_NAME.
     */
    private static final String WORK_FLOW_NAME = "test workflow name";

    /**
     * The dummy Constant CATEGORY_NAME.
     */
    private static final String CATEGORY_NAME = "Test Category";

    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * Sets the up. Called before each method
     *
     * @throws Exception
     *         the exception
     */

    @Before
    public void setUp() throws Exception {
        HibernateTestConfigration.setUp();
        PowerMockito.spy( Activator.class );
        PowerMockito.when( Activator.getSession() ).thenReturn( HibernateTestConfigration.getSession() );
        workflowCategoryDao = new WorkflowCategoryDAOImpl();

    }

    /* *************************************************************************
     * <assignCategoriesToWorkflow under test>
     * *************************************************************************
     */

    /**
     * Should assign category to Workflow categories.
     */
    @Test
    public void shouldAssignCategoryToWorkflowCategoriesSuccessfully() {

        replayConfigration();

        final boolean expected = workflowCategoryDao.assignCategoriesToWorkflow( EasyMock.anyObject( EntityManager.class ),
                fillWorkflowCategoryEntityList(), WORK_FLOW_ID );
        Assert.assertTrue( expected );

    }

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
        workFlowEntity.setName( WORK_FLOW_NAME );
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
     * Fill workflow category entity list.
     *
     * @return the list
     */
    private List< WorkflowCategoryEntity > fillWorkflowCategoryEntityList() {
        fillCategoryEntity();
        fillWorkFlowEntity();
        final List< WorkflowCategoryEntity > list = new ArrayList<>();
        final WorkflowCategoryEntity entity = new WorkflowCategoryEntity();
        entity.setCategory( categoryEntity );
        entity.setId( ID );
        entity.setWorkflow( workFlowEntity );
        list.add( entity );
        return list;
    }

    /**
     * Fill category entity.
     */
    private void fillCategoryEntity() {
        categoryEntity = new CategoryEntity();
        categoryEntity.setId( CATEGORY_ID );
        categoryEntity.setName( CATEGORY_NAME );

    }

    private void fillUserEntity() {
        userEntity = new UserEntity();
        userEntity.setId( USER_ID );
    }

    private void fillWorkflowPrimaryKey() {
        workFlowEntity.setComposedId( new VersionPrimaryKey( WORK_FLOW_ID, VERSION_ID ) );
    }

}
