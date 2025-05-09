package de.soco.software.simuspace.suscore.plugin.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.plugin.dto.PluginDTO;
import de.soco.software.simuspace.suscore.plugin.manager.PluginManager;

/**
 * Test Cases for PluginServiceImpl Class
 *
 * @author Nosheen.Sharif
 */
public class PluginServiceImplTest {

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
     * The PLugin Service Refernence.
     */
    private PluginServiceImpl service;

    /**
     * The manager plugin referring.
     */
    private PluginManager manager;

    /**
     * Dummy id of plugin
     */
    private static final UUID ID = UUID.randomUUID();

    /**
     * Dummy author of plugin
     */
    private static final String AUTHOR = "testAuthor";

    /**
     * Dummy compatible of plugin
     */
    private static final String COMPATIBLE_VERSION = "2.0.0";

    /**
     * Dummy name of plugin
     */
    private static final String NAME = "Test_plugin";

    /**
     * Dummy Source of plugin
     */
    private static final String SOURCE = "test_source";

    /**
     * Dummy status of plugin
     */
    private static final String STATUS = "dummy status";

    /**
     * Dummy version of plugin
     */
    private static final String VERSION = "1.0.0";

    /**
     * Dummy License of plugin
     */
    private static final String LICENSE = "Apache 2.0";

    /**
     * Valid Zip File Path.
     */
    private static final String VALID_ZIP_PATH = "{\"path\":\"src/test/resources/Test.zip\"}";

    /**
     * Invalid Zip file path
     */
    private static final String INVALID_ZIP_PATH = "src/test123/resources/Test.zip";

    /**
     * The Dummy EMPTY STRING for test cases.
     */
    private static final String EMPTY_STRING = "";

    /**
     * The Dummy PLUGIN_START_SUCCESS STRING for test cases.
     */
    private static final String PLUGIN_ENABLE_SUCCESS = "Plugin Enables Successfully";

    /**
     * The Dummy PLUGIN_START_SUCCESS STRING for test cases.
     */
    private static final String PLUGIN_START_SUCCESS = "Plugin Started Successfully";

    /**
     * The Dummy PLUGIN_START_FAILED STRING for test cases.
     */
    private static final String PLUGIN_START_FAILED = "Plugin failed to start";

    /**
     * Dummy plugin DTO object initilization for test cases
     */
    private PluginDTO pluginDto = new PluginDTO();

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    public static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * set Up
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        mockControl().resetToNice();
        manager = mockControl().createMock( PluginManager.class );
        service = new PluginServiceImpl();
        service.setManager( manager );

    }

    /**
     * ************************ getPluginList
     *
     *
     ************************************
     */

    /**
     * Should Get List Of Plugins In Response If Manager Return List Of Plugins
     */
    @Test
    public void shouldGetListOfPluginsInResponseIfManagerReturnListOfPlugins() {
        EasyMock.expect( manager.getPluginList() ).andReturn( getListofPluginDTO() ).anyTimes();
        mockControl().replay();
        Response actual = service.getPluginList();
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertFalse( ( ( List< ? > ) actualResponse.getData() ).isEmpty() );

    }

    /**
     * Should Get Empty List Of Plugins In Response If Manager Return List Of Plugins Is Empty
     */
    @Test
    public void shouldGetEmptyListOfPluginsInResponseIfManagerReturnListOfPluginsIsEmpty() {
        EasyMock.expect( manager.getPluginList() ).andReturn( new ArrayList<>() ).anyTimes();
        mockControl().replay();
        Response actual = service.getPluginList();
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertTrue( ( ( List< ? > ) actualResponse.getData() ).isEmpty() );

    }

    /**
     * ************************ addPlugin
     *
     *
     ************************************
     */

    /**
     * Should Successfully Get Plugin DTO In Response Data If Valid Zip File Path Is Given
     */
    @Test
    public void shouldSuccessfullyGetPluginDataInResponseDataIfValidZipFilePathIsGiven() {
        PluginDTO expected = filledPluginDTO();
        EasyMock.expect( manager.addPlugin( EasyMock.anyObject() ) ).andReturn( filledPluginDTO() ).anyTimes();
        mockControl().replay();
        Response actual = service.addPlugin( VALID_ZIP_PATH );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );

        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );

        String actualDtoStr = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
        Assert.assertEquals( expected, JsonUtils.jsonToObject( actualDtoStr, PluginDTO.class ) );

    }

    /**
     * Should Not Get Data In Response If InValid Zip File Path Is Given To Add Plugin
     */
    @Test
    public void shouldNotGetDataInResponseIfInValidZipFilePathIsGivenToAddPlugin() {
        SusResponseDTO expected = null;
        Response actual = service.addPlugin( INVALID_ZIP_PATH );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );

        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( actualResponse.getData(), expected );

    }

    /**
     * Should Not Get Data In Response If Null Is Given To Add Plugin
     */
    @Test
    public void shouldNotGetDataInResponseIfNullIsGivenToAddPlugin() {
        SusResponseDTO expected = null;
        Response actual = service.addPlugin( null );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, actual.getStatus() );

        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( actualResponse.getData(), expected );

    }

    /**
     * Should Not Get Data In Response If Empty String Is Given To Add Plugin
     */
    @Test
    public void shouldNotGetDataInResponseIfEmptyStringIsGivenToAddPlugin() {
        SusResponseDTO expected = null;
        Response actual = service.addPlugin( EMPTY_STRING );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );

        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( actualResponse.getData(), expected );

    }

    /**
     * ************************ enablePlugin
     *
     *
     * ***********************************
     */
    @Test
    public void shouldSuccessFullyEnablePluginIfManagerReturnValidPluginDTOObject() {
        PluginDTO expected = filledPluginDTO();
        EasyMock.expect( manager.enablePlugin( EasyMock.anyObject( PluginDTO.class ) ) ).andReturn( expected ).anyTimes();
        mockControl().replay();
        Response actual = service.enablePlugin( ID.toString() );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );

        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( PLUGIN_ENABLE_SUCCESS, actualResponse.getMessage().getContent() );

        String actualDtoStr = JsonUtils.convertMapToString( ( Map< String, String > ) actualResponse.getData() );
        Assert.assertEquals( expected, JsonUtils.jsonToObject( actualDtoStr, PluginDTO.class ) );

    }

    /**
     * ************************ startPlugin
     *
     *
     * ***********************************
     */
    @Test
    public void shouldSuccessFullyStartPluginIfManagerReturnSuccessForStartingPlugin() {
        boolean expected = Boolean.TRUE;
        EasyMock.expect( manager.startPlugin( EasyMock.anyObject( PluginDTO.class ) ) ).andReturn( expected ).anyTimes();
        mockControl().replay();

        Response actual = service.startPlugin( ID.toString() );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );

        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( PLUGIN_START_SUCCESS, actualResponse.getMessage().getContent() );
        Assert.assertTrue( ( boolean ) actualResponse.getData() );

    }

    @Test
    public void shouldFailedToStartPluginIfManagerReturnFailureForStartingPlugin() {
        boolean expected = Boolean.FALSE;
        EasyMock.expect( manager.startPlugin( EasyMock.anyObject( PluginDTO.class ) ) ).andReturn( expected ).anyTimes();
        mockControl().replay();
        Response actual = service.startPlugin( ID.toString() );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );

        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( PLUGIN_START_FAILED, actualResponse.getMessage().getContent() );
        Assert.assertFalse( ( boolean ) actualResponse.getData() );

    }

    /***************************** HELPER METHODS ************************************/

    /**
     * Get Filled Plugin DTO List
     *
     * @return
     */
    private List< PluginDTO > getListofPluginDTO() {
        List< PluginDTO > list = new ArrayList<>();
        list.add( filledPluginDTO() );
        return list;
    }

    /**
     * Fill Plugin Entity
     *
     * @return
     */
    private PluginDTO filledPluginDTO() {

        pluginDto.setAuthor( AUTHOR );
        pluginDto.setId( ID.toString() );
        pluginDto.setCompaitableVersion( COMPATIBLE_VERSION );
        pluginDto.setLicense( LICENSE );
        pluginDto.setName( NAME );
        pluginDto.setSource( SOURCE );
        pluginDto.setStatus( STATUS );
        pluginDto.setVersion( VERSION );
        return pluginDto;
    }

}
