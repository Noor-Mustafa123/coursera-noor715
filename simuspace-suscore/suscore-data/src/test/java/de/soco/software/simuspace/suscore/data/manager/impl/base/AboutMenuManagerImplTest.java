package de.soco.software.simuspace.suscore.data.manager.impl.base;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.AboutDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.common.dao.AboutMenuDAO;
import de.soco.software.simuspace.suscore.data.entity.AboutSimuspaceEntity;

/**
 * The Class AboutMenuManagerImplTest.
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { PropertiesManager.class } )
public class AboutMenuManagerImplTest {

    /**
     * The about menu DAO.
     */
    private AboutMenuDAO aboutMenuDAO;

    /**
     * The thrown.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * The Constant mockControl.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The about menu manager impl.
     */
    private AboutMenuManagerImpl aboutMenuManagerImpl;

    /**
     * The Constant VERSION.
     */
    private static final String VERSION = "2.0.0";

    /**
     * To initialize the objects and mocking objects.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        aboutMenuManagerImpl = new AboutMenuManagerImpl();
        aboutMenuDAO = mockControl.createMock( AboutMenuDAO.class );
        aboutMenuManagerImpl.setAboutMenuDAO( aboutMenuDAO );
    }

    /**
     * Should save about menu when init function is called.
     *
     * @throws Exception
     */
    @Test
    public void shouldSaveAboutMenuWhenInitFunctionIsCalled() throws Exception {
        mockStaticMethodOfPropertiesUtilClass();
        AboutDTO aboutDTO = new AboutDTO();
        aboutDTO.setBuildFe( VERSION );
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey() ) );
        EasyMock.expect( aboutMenuDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( null )
                .anyTimes();
        mockControl.replay();
        aboutMenuManagerImpl.saveAboutMenu( EasyMock.anyObject( EntityManager.class ), aboutDTO );

        aboutMenuManagerImpl.init();
    }

    /**
     * Mock static method of properties util class.
     *
     * @throws Exception
     *         the exception
     */
    private void mockStaticMethodOfPropertiesUtilClass() throws Exception {
        PowerMockito.spy( PropertiesManager.class );
        PowerMockito.doReturn( true ).when( PropertiesManager.class, "isMasterLocation" );
    }

    /**
     * Should return about DTO when about menu method is called.
     */
    @Test
    public void shouldReturnAboutDTOWhenAboutMenuMethodIsCalled() {
        AboutSimuspaceEntity aboutEntity = prepareAboutEntity();

        AboutDTO expected = prepareAboutDTO( aboutEntity );
        EasyMock.expect( aboutMenuDAO.getLatestObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( aboutEntity ).anyTimes();
        mockControl.replay();
        AboutDTO actula = aboutMenuManagerImpl.aboutMenu();
        Assert.assertEquals( actula.getType(), expected.getType() );
    }

    /**
     * Should return about DTOUI when about menu UI method is called.
     */
    @Test
    public void shouldReturnAboutDTOUIWhenAboutMenuUIMethodIsCalled() {

        List< TableColumn > actual = aboutMenuManagerImpl.aboutMenuUI();
        Assert.assertNotNull( actual );
        Assert.assertFalse( actual.isEmpty() );

    }

    /**
     * Prepare about DTO.
     *
     * @param aboutEntity
     *         the about entity
     *
     * @return the about DTO
     */
    private AboutDTO prepareAboutDTO( AboutSimuspaceEntity aboutEntity ) {
        AboutDTO aboutDTO = new AboutDTO();
        aboutDTO.setBuildBe( aboutEntity.getBuildBeSimuspace() );
        aboutDTO.setBuildFe( aboutEntity.getBuildFeSimuspace() );
        aboutDTO.setVersion( aboutEntity.getVersion() );
        aboutDTO.setType( aboutEntity.getType() );

        return aboutDTO;
    }

    /**
     * Prepare about entity.
     *
     * @return the about simuspace entity
     */
    private AboutSimuspaceEntity prepareAboutEntity() {
        AboutSimuspaceEntity aboutDTO = new AboutSimuspaceEntity();
        aboutDTO.setBuildBeSimuspace( VERSION );
        aboutDTO.setBuildFeSimuspace( VERSION );
        aboutDTO.setVersion( VERSION );
        aboutDTO.setType( ConstantsString.SIMUSPACE );

        return aboutDTO;
    }

}
