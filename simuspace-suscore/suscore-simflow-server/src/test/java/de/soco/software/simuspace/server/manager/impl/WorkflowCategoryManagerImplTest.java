package de.soco.software.simuspace.server.manager.impl;

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
import org.powermock.core.classloader.annotations.PrepareForTest;

import de.soco.software.simuspace.server.dao.WorkflowCategoryDAO;
import de.soco.software.simuspace.server.manager.CategoryManager;
import de.soco.software.simuspace.server.manager.WorkflowManager;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.CategoryEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.workflow.dto.WorkflowDTO;
import de.soco.software.simuspace.workflow.model.impl.CategoryImpl;

/**
 * Test Cases for WorkflowCategoryManagerImpl Class.
 */
@PrepareForTest( WorkflowCategoryManagerImpl.class )
public class WorkflowCategoryManagerImplTest {

    private static IMocksControl mockControl = EasyMock.createControl();

    private WorkflowCategoryManagerImpl manager;

    /**
     * The workflow manager.
     */
    private WorkflowManager workflowManager;

    /**
     * The category manager.
     */
    private CategoryManager categoryManager;

    /**
     * The workflow category dao.
     */
    private WorkflowCategoryDAO workflowCategoryDao;

    private WorkflowDTO workflowDto;

    private CategoryImpl categoryImpl;

    private WorkflowEntity workFlowEntity;

    private CategoryEntity categoryEntity;

    private UserEntity userEntity;

    private static final UUID ID = UUID.randomUUID();

    private static final UUID WORK_FLOW_ID = UUID.randomUUID();

    private static final UUID CATEGORY_ID = UUID.randomUUID();

    private static final int VERSION_ID = 1;

    private static final String WORK_FLOW_NAME = "test workflow name";

    private static final String CATEGORY_NAME = "Test Category";

    private static final UUID DEFAULT_BE_USER_ID = UUID.randomUUID();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        manager = new WorkflowCategoryManagerImpl();
        mockControl().resetToNice();

        workflowManager = mockControl().createMock( WorkflowManager.class );
        categoryManager = mockControl().createMock( CategoryManager.class );
        workflowCategoryDao = mockControl().createMock( WorkflowCategoryDAO.class );
        licenseManager = mockControl.createMock( LicenseManager.class );
        manager.setCategoryManager( categoryManager );
        manager.setWorkflowCategoryDao( workflowCategoryDao );
        manager.setWorkflowManager( workflowManager );
        manager.setLicenseManager( licenseManager );

    }

    /**
     * Should return true when categories assign to workflow successfully.
     */
    @Test
    public void shouldReturnTrueWhenCategoriesAssignToWorkflowSuccessfully() {
        fillWorkflowDto();
        fillCategoryImpl();
        fillCategoryEntity();
        EasyMock.expect(
                        workflowManager.getWorkflowById( EasyMock.anyObject( EntityManager.class ), DEFAULT_BE_USER_ID, WORK_FLOW_ID.toString() ) )
                .andReturn( workflowDto ).anyTimes();
        EasyMock.expect( categoryManager.getCategoryById( EasyMock.anyObject( EntityManager.class ), CATEGORY_ID.toString() ) )
                .andReturn( categoryImpl ).anyTimes();
        EasyMock.expect( workflowCategoryDao.assignCategoriesToWorkflow( EasyMock.anyObject( EntityManager.class ),
                EasyMock.anyObject( List.class ), EasyMock.anyObject( UUID.class ) ) ).andReturn( true ).anyTimes();
        mockControl().replay();
        final boolean expected = manager.assignCategoriesToWorkflow( DEFAULT_BE_USER_ID, WORK_FLOW_ID.toString(), fillCategoriesIdsList() );
        Assert.assertTrue( expected );
    }

    /**
     * Shouldthrow exception message when workflow id given is not valid.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldthrowExceptionMessageWhenWorkflowIdGivenIsNotValid() throws SusException {
        final String invalid_uuid = "1223-2334-22-334";

        thrown.expect( SusException.class );

        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID, invalid_uuid ) );

        mockControl().replay();
        manager.assignCategoriesToWorkflow( DEFAULT_BE_USER_ID, invalid_uuid, fillCategoriesIdsList() );
    }

    /**
     * Fill categories ids list.
     *
     * @return the list
     */
    private List< String > fillCategoriesIdsList() {
        final List< String > list = new ArrayList<>();
        list.add( CATEGORY_ID.toString() );

        return list;
    }

    /**
     * Fill workflow dto.
     */
    private void fillWorkflowDto() {
        workflowDto = new WorkflowDTO();
        workflowDto.setName( WORK_FLOW_NAME );
        workflowDto.setId( WORK_FLOW_ID.toString() );
        workflowDto.setVersion( new VersionDTO( VERSION_ID ) );

    }

    /**
     * Fill category impl.
     */
    private void fillCategoryImpl() {
        categoryImpl = new CategoryImpl();
        categoryImpl.setId( CATEGORY_ID.toString() );
        categoryImpl.setName( CATEGORY_NAME );
    }

    /**
     * Fill category entity.
     */
    private void fillCategoryEntity() {
        categoryEntity = new CategoryEntity();
        categoryEntity.setId( CATEGORY_ID );
        categoryEntity.setName( CATEGORY_NAME );

    }

}
