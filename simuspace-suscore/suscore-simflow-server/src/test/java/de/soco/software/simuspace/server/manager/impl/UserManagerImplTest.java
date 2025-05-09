package de.soco.software.simuspace.server.manager.impl;

import javax.persistence.EntityManager;

import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

import de.soco.software.simuspace.server.dao.WorkflowUserDAO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;

/**
 * Test Cases for UserManager Class
 *
 * @author Nosheen.Sharif
 */
@PrepareForTest( UserManagerImplTest.class )
public class UserManagerImplTest {

    private static IMocksControl mockControl = EasyMock.createControl();

    private static final UUID USER_ID = UUID.randomUUID();

    private static final UUID SIM_ID = UUID.randomUUID();

    private static final String USER_NAME = "abc";

    private static final String GET_SIM_USER_METHOD = "getSimUser";

    private static final String PREPARE_USER_DTO_FROM_ENTITY = "prepareUserDtoFromUserEntity";

    private static final String PREPARE_USER_ENTITY_FROM_DTO = "prepareUserEntity";

    private WorkflowUserDAO userDao;

    private static WorkflowUserManagerImpl manager;

    private UserDTO userDto;

    private static UserEntity userEntity;

    static IMocksControl mockControl() {
        return mockControl;
    }

    @BeforeClass
    public static void setUp() {
        manager = new WorkflowUserManagerImpl();
        fillUserEntity();

    }

    /**
     * Should return user entity if user with sim id already exist.
     *
     * @throws Exception
     *         the exception
     */

    @Test
    public void shouldReturnUserEntityIfUserWithSimIdAlreadyExist() throws Exception {

        userDao = mockControl.createMock( WorkflowUserDAO.class );

        EasyMock.expect( userDao.isUserExisting( EasyMock.anyObject( EntityManager.class ), SIM_ID ) ).andReturn( true );

        EasyMock.expect( userDao.getUserIdBySimId( EasyMock.anyObject( EntityManager.class ), SIM_ID ) ).andReturn( userEntity );

        manager.setUserDao( userDao );
        mockControl.replay();

        final UserEntity entity = Whitebox.invokeMethod( manager, GET_SIM_USER_METHOD, SIM_ID );
        Assert.assertEquals( userEntity.getId(), entity.getId() );

    }

    /**
     * Should return user entity from user dto.
     *
     * @throws Exception
     *         the exception
     */

    @Test
    public void shouldReturnUserEntityFromUserDto() throws Exception {
        final UserDTO dto = Whitebox.invokeMethod( manager, PREPARE_USER_DTO_FROM_ENTITY, userEntity );
        Assert.assertEquals( userEntity.getId().toString(), dto.getId() );

    }

    /**
     * Should return user entity from user DTO.
     *
     * @throws Exception
     *         the exception
     */

    @Test
    public void shouldReturnUserEntityFromUserDTO() throws Exception {
        fillUserDto();
        final UserEntity entity = Whitebox.invokeMethod( manager, PREPARE_USER_ENTITY_FROM_DTO, userDto );
        Assert.assertEquals( userDto.getId(), entity.getId().toString() );

    }

    /**
     * Fill user dto.
     */
    private void fillUserDto() {
        userDto = new UserDTO();
        userDto.setId( UUID.randomUUID().toString() );
        userDto.setFirstName( USER_NAME );
    }

    /**
     * Fill user entity.
     */
    private static void fillUserEntity() {
        userEntity = new UserEntity();
        userEntity.setId( USER_ID );
    }

}
