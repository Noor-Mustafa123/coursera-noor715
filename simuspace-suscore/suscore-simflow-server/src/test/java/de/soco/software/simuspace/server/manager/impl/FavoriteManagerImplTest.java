package de.soco.software.simuspace.server.manager.impl;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.server.dao.FavoriteDAO;
import de.soco.software.simuspace.server.dao.WorkflowDAO;
import de.soco.software.simuspace.server.manager.WorkflowManager;
import de.soco.software.simuspace.server.manager.WorkflowUserManager;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.FavoriteEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.workflow.dto.WorkflowDTO;

/**
 * This class is to test the FavoriteManager implementation class methods.
 *
 * @author Zeeshan jamal
 */

public class FavoriteManagerImplTest {

    /**
     * The Constant default user id to by pass permission checking.
     */
    private static final UUID DEFAULT_USER_ID = UUID.randomUUID();

    /**
     * The Constant USER_ID.
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * The Constant SIM_ID.
     */
    private static final UUID SIM_ID = UUID.randomUUID();

    /**
     * The Constant for work flow name.
     */
    private static final String WORK_FLOW_NAME = "test workflow name";

    /**
     * The Constant for invalid work flow id.
     */
    private static final String INVALID_WORKFLOW_ID = "012edhhe";

    private static final String WORK_FLOW_ENTITY_NAME = "test workflow entity name";

    /**
     * The Constant for work flow comments.
     */
    private static final String COMMENTS = "Test comments";

    /**
     * The Constant for work flow description.
     */
    private static final String TEST_DESCRIPTION = "Test Description";

    /**
     * The Constant for empty work flow id.
     */
    private static final String EMPTY_WORKFLOW_ID = "";

    /**
     * The Constant for work flow id.
     */
    private static final UUID WORK_FLOW_ID = UUID.randomUUID();

    /**
     * The Constant for work flow id.
     */
    private static final UUID FAVORITE_ID = UUID.randomUUID();

    /**
     * The Constant for work flow version.
     */
    private static final int VERSION_ID = 1;

    /**
     * The Constant for user name.
     */
    private static final String USER_NAME = "user";

    /**
     * Generic Rule for the expected exception
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * The favoriteManagerImpl object for expected results
     */
    private FavoriteManagerImpl favoriteManagerImpl;

    /**
     * The favoriteDAO object
     */
    private FavoriteDAO favoriteDAO;

    /**
     * The userEntity object
     */
    private UserEntity userEntity;

    /**
     * The favoriteEntity object
     */
    private FavoriteEntity favoriteEntity;

    /**
     * The userManager object
     */
    private WorkflowUserManager userManager;

    /**
     * The workFlowEntity object
     */
    private WorkflowEntity workFlowEntity;

    /**
     * The userDto object for mocking and preparing expected results
     */
    private UserDTO userDto;

    /**
     * The workFlowManager object
     */
    private WorkflowManager workFlowManager;

    /**
     * The workFlowDTO object
     */
    private WorkflowDTO workFlowDTO;

    /**
     * The workFlowDAO object
     */
    private WorkflowDAO workFlowDAO;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * Sets the up.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        favoriteManagerImpl = new FavoriteManagerImpl();
        favoriteDAO = mockControl().createMock( FavoriteDAO.class );
        userManager = mockControl().createMock( WorkflowUserManager.class );
        workFlowManager = mockControl().createMock( WorkflowManager.class );
        workFlowDAO = mockControl().createMock( WorkflowDAO.class );
        licenseManager = mockControl.createMock( LicenseManager.class );
        favoriteManagerImpl.setFavoriteDAO( favoriteDAO );
        favoriteManagerImpl.setWorkflowManager( workFlowManager );
        favoriteManagerImpl.setLicenseManager( licenseManager );

    }

    /**
     * When adding a workflow to favorite it should throw exception if invalid workflow id is given
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shoudThrowExceptionWhenAddingAWorkFlowToFavoriteIfInvalidWorkFlowIdIsGiven() throws SusException {

        thrown.expect( SusException.class );
        thrown.expectMessage( MessagesUtil.getMessage( WFEMessages.INVALID_UUID, INVALID_WORKFLOW_ID ) );
        EasyMock.expect( licenseManager.isLicenseAddedAgainstUserForModule( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyString() ) ).andReturn( true ).anyTimes();
        mockControl.replay();
        favoriteManagerImpl.addWorkflowToFavorite( DEFAULT_USER_ID, INVALID_WORKFLOW_ID );
    }

    /**
     * Returns the favorite workflowDTO list against the specific user id
     *
     * @throws SusException
     *         the sus exception
     */

    @Test
    public void shouldReturnFavoriteWorFlowDTOListWhenValidUserIdIsGiven() throws SusException {

        fillUserEntity();
        final List< WorkflowEntity > list = new ArrayList<>();
        fillWorkFlowEntity();
        list.add( workFlowEntity );

        EasyMock.expect( favoriteDAO.getFavouriteWorkFlowListByUserId( EasyMock.anyObject( EntityManager.class ), userEntity.getId() ) )
                .andReturn( list ).anyTimes();
        mockControl.replay();

    }

    /**
     * Should return WorkFLowDTO with favorite flag false when workFlow is removed from favorites successfully.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnWorkFlowDTOWithFavoriteFlagFalseWhenWorkFlowIsRemovedFromFavoritesSuccessfully() throws Exception {

        fillWorkflowDto();
        fillWorkFlowEntity();
        fillUserEntity();

        EasyMock.expect( workFlowManager.getUserManager() ).andReturn( userManager ).anyTimes();
        EasyMock.expect( favoriteDAO.isWorkFlowAlreadyFavorite( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject() ) ).andReturn( true ).anyTimes();

        mockControl.replay();

        favoriteManagerImpl.getWorkflowManager().setUserManager( userManager );
        final WorkflowDTO workflowDTO = favoriteManagerImpl.addWorkflowToFavorite( DEFAULT_USER_ID, WORK_FLOW_ID.toString() );

        Assert.assertFalse( workflowDTO.isFavorite() );

    }

    /**
     * Fill user Entity.
     */
    private void fillUserEntity() {
        userEntity = new UserEntity();
        userEntity.setId( USER_ID );
    }

    /**
     * Fill favorite Entity.
     */
    private void fillFavoriteEntity() {
        favoriteEntity = new FavoriteEntity();
        favoriteEntity.setId( FAVORITE_ID );
        fillWorkFlowEntity();
        favoriteEntity.setWorkflow( workFlowEntity );
    }

    /**
     * Fill workflow Entity.
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
        workFlowEntity.setVersionId( workFlowEntity.getComposedId().getVersionId() );

    }

    private void fillWorkflowPrimaryKey() {
        workFlowEntity.setComposedId( new VersionPrimaryKey( WORK_FLOW_ID, VERSION_ID ) );
    }

    /**
     * Fill workflow dto.
     */
    private void fillWorkflowDto() {
        workFlowDTO = new WorkflowDTO();
        workFlowDTO.setName( WORK_FLOW_NAME );
        workFlowDTO.setId( WORK_FLOW_ID.toString() );
        workFlowDTO.setVersion( new VersionDTO() );
        workFlowDTO.setFavorite( true );
        fillUserDto();
        workFlowDTO.setCreatedBy( userDto );

        workFlowDTO.setVersion( new VersionDTO( VERSION_ID ) );

    }

    /**
     * Fill user dto.
     */

    private void fillUserDto() {
        userDto = new UserDTO();
        userDto.setId( UUID.randomUUID().toString() );
        userDto.setFirstName( USER_NAME );
    }

}
