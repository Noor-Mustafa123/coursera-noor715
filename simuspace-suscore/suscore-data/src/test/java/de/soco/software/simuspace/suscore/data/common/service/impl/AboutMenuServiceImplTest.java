package de.soco.software.simuspace.suscore.data.common.service.impl;

import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.model.AboutDTO;
import de.soco.software.simuspace.suscore.data.manager.base.AboutMenuManager;
import de.soco.software.simuspace.suscore.data.manager.impl.base.AboutMenuManagerImpl;

/**
 * The Class AboutMenuServiceImplTest is test the about menu for simuspace.
 *
 * @author noman arshad
 */
public class AboutMenuServiceImplTest {

    /**
     * The Constant ABOUT.
     */
    private static final String ABOUT = "About Menu";

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
     * The selectionmanager.
     */
    private AboutMenuManager aboutMenuManager;

    /**
     * The about menu service.
     */
    private AboutMenuServiceImpl aboutMenuService;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        aboutMenuService = new AboutMenuServiceImpl();
        aboutMenuManager = mockControl.createMock( AboutMenuManagerImpl.class );

        aboutMenuService.setAboutMenuManager( aboutMenuManager );
    }

    /**
     * Should successfully return about entity object when about menu called.
     */
    @Test
    public void shouldSuccessfullyReturnAboutEntityObjectWhenAboutMenuCalled() {
        EasyMock.expect( aboutMenuManager.aboutMenu() ).andReturn( prepareAboutEntity() ).anyTimes();
        mockControl.replay();
        Response actualResponse = aboutMenuService.getAboutMenu();
        Assert.assertNotNull( actualResponse );
        Assert.assertEquals( HttpStatus.SC_OK, actualResponse.getStatus() );

    }

    /**
     * Should successfully return about UI form when about menu UI called.
     */
    @Test
    public void shouldSuccessfullyReturnAboutUIFormWhenAboutMenuUICalled() {
        EasyMock.expect( aboutMenuManager.aboutMenu() ).andReturn( prepareAboutEntity() ).anyTimes();
        mockControl.replay();
        Response actualResponse = aboutMenuService.getAboutMenuUI();
        Assert.assertNotNull( actualResponse );
        Assert.assertEquals( HttpStatus.SC_OK, actualResponse.getStatus() );

    }

    /**
     * Prepare about entity.
     *
     * @return the about DTO
     */
    private AboutDTO prepareAboutEntity() {
        AboutDTO aboutDTO = new AboutDTO();
        aboutDTO.setBuildBe( ABOUT );
        aboutDTO.setBuildFe( ABOUT );
        aboutDTO.setVersion( ABOUT );
        aboutDTO.setType( ABOUT );

        return aboutDTO;
    }

}
