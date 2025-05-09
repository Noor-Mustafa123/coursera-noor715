package de.soco.software.simuspace.suscore.plugin.manager.impl;

import javax.persistence.EntityManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.data.entity.PluginEntity;
import de.soco.software.simuspace.suscore.plugin.dao.PluginDAO;
import de.soco.software.simuspace.suscore.plugin.dto.PluginDTO;

/**
 * Test Cases for PluginManagerImpl Class
 *
 * @author Nosheen.Sharif
 */

public class PluginManagerImplTest {

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
     * First Index of ArrayList
     */
    private static final int FIRST_INDEX = 0;

    /**
     * constant for vaid zip fiel path for plugin
     */
    private static final String VALID_ZIP_FILE_PATH = "src/test/resources/TestFol.zip";

    /**
     * text file path for test case
     */
    private static final String TEXT_FILE_PATH = "src/test/resources/test.txt";

    /**
     * invalid file path for plugin
     */
    private static final String INVALID_FILE_PATH = "src" + File.separator + "test123" + File.separator + "resources" + File.separator
            + "TestFol.zip";

    /**
     * Error message if file does not exist at path
     */
    private static final String FILE_PATH_DOES_NOT_EXIST = "File path does Not Exist :";

    /**
     * Error message for zip file not selected for plugin as input
     */
    private static final String ZIP_FILE_NOT_SELECTED = "Selected File is Not of zip extension";

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
     * Dummy GROUP_ID of plugin
     */
    private static final String GROUP_ID = "de.soco.software.simuspace";

    /**
     * Dummy ARTIFACT_ID of plugin
     */
    private static final String ARTIFACT_ID = "sus-core-plugin";

    /**
     * Dummy License of plugin
     */
    private static final String LICENSE = "Apache 2.0";

    /**
     * Validation Message for Artifact
     */
    private static final String ARTIFACT_MSG = "Artifact Id can not be null or empty.";

    /**
     * Validation Message for Group Id
     */
    private static final String GRP_ID_MSG = "Group Id can not be null or empty.";

    /**
     * Validation Message for Plugin Name
     */
    private static final String PLUGIN_NAME_MSG = "Plugin Name  can not be null or empty.";

    /**
     * Validation Message for Version
     */
    private static final String VERSION_MSG = "Version can not be null or empty.";

    /**
     * Dummy UserId for test Cases
     */
    private PluginManagerImpl manager;

    /**
     * Dummy plugin DTO object initilization for test cases
     */
    private PluginDTO pluginDto;

    /**
     * Dummy plugin Entity object initilization for test cases
     */
    private PluginEntity entity = new PluginEntity();

    /**
     * PLuginDAO reference for mocking
     */
    private PluginDAO dao;

    /**
     * To initialize the objects and mocking objects
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        manager = new PluginManagerImpl();
        dao = mockControl.createMock( PluginDAO.class );
        manager.setPluginDao( dao );

    }

    /****************************** ADD Plugin Test Cases ************************************/

    /**
     * Should Throw Exception If Invalid File Path Is Given To Add Plugin
     */
    @Test
    public void shouldThrowExceptionIfInvalidFilePathIsGivenToAddPlugin() {

        thrown.expect( SusException.class );
        thrown.expectMessage( FILE_PATH_DOES_NOT_EXIST + INVALID_FILE_PATH );
        manager.addPlugin( INVALID_FILE_PATH );

    }

    /**
     * Should Throw Exception If File Path Is Not Of Zip Extension To Add Plugin
     */
    @Test
    public void shouldThrowExceptionIfFilePathIsNotOfZipExtensionToAddPlugin() {

        thrown.expect( SusException.class );
        thrown.expectMessage( ZIP_FILE_NOT_SELECTED );
        manager.addPlugin( TEXT_FILE_PATH );

    }

    /**
     * Should Get Error Message For Add Plugin If Artifact Id Is Empty
     */
    @Test
    public void shouldGetErrorMessageForAddPluginIfArtifactIdIsEmpty() {
        filledPluginDTO();
        pluginDto.setArtifactId( StringUtils.EMPTY );
        Notification actual = pluginDto.validate();
        Assert.assertTrue( actual.hasErrors() );
        Assert.assertEquals( actual.getMessages().get( FIRST_INDEX ), ARTIFACT_MSG );

    }

    /**
     * Should Get Error Message For Add Plugin If Group Id Is Empty
     */
    @Test
    public void shouldGetErrorMessageForAddPluginIfGroupIdIsEmpty() {
        filledPluginDTO();
        pluginDto.setGroupId( StringUtils.EMPTY );
        Notification actual = pluginDto.validate();
        Assert.assertTrue( actual.hasErrors() );
        Assert.assertEquals( actual.getMessages().get( FIRST_INDEX ), GRP_ID_MSG );

    }

    /**
     * Should Get Error Message For Add Plugin If Name Is Empty
     */
    @Test
    public void shouldGetErrorMessageForAddPluginIfNameIsEmpty() {
        filledPluginDTO();
        pluginDto.setName( StringUtils.EMPTY );
        Notification actual = pluginDto.validate();
        Assert.assertTrue( actual.hasErrors() );
        Assert.assertEquals( actual.getMessages().get( FIRST_INDEX ), PLUGIN_NAME_MSG );

    }

    /**
     * Should Get Error Message For Add Plugin If Version Is Empty
     */
    @Test
    public void shouldGetErrorMessageForAddPluginIfVersionIsEmpty() {
        filledPluginDTO();
        pluginDto.setVersion( StringUtils.EMPTY );
        Notification actual = pluginDto.validate();
        Assert.assertTrue( actual.hasErrors() );
        Assert.assertEquals( actual.getMessages().get( FIRST_INDEX ), VERSION_MSG );

    }

    /****************************** GET Plugin List Test Cases ************************************/

    /**
     * Should Get List Of Plugins If Database Tables has Records In Plugin Tables
     */
    @Test
    public void shouldGetListOfPluginsIfDatabaseTableshasRecordsInPluginTables() {
        List< PluginEntity > expected = getListofPluginEntity();
        EasyMock.expect( dao.getPluginList( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( expected ).anyTimes();
        mockControl.replay();
        List< PluginDTO > actual = manager.getPluginList();
        for ( PluginDTO acutualDto : actual ) {
            Assert.assertEquals( acutualDto.getId(), expected.get( FIRST_INDEX ).getPluginId().toString() );
            Assert.assertEquals( acutualDto.getArtifactId(), expected.get( FIRST_INDEX ).getArtifactId() );
            Assert.assertEquals( acutualDto.getGroupId(), expected.get( FIRST_INDEX ).getGroupId() );
            Assert.assertEquals( acutualDto.getName(), expected.get( FIRST_INDEX ).getPluginName() );
            Assert.assertEquals( acutualDto.getVersion(), expected.get( FIRST_INDEX ).getVersion() );
            Assert.assertEquals( acutualDto.getSummary(), expected.get( FIRST_INDEX ).getSummary() );
            Assert.assertEquals( acutualDto.getSource(), expected.get( FIRST_INDEX ).getSource() );
            Assert.assertEquals( acutualDto.getStatus(), expected.get( FIRST_INDEX ).getStatus() );
            Assert.assertEquals( acutualDto.getLicense(), expected.get( FIRST_INDEX ).getLicense() );
            Assert.assertEquals( acutualDto.getAuthor(), expected.get( FIRST_INDEX ).getAuthor() );
        }
    }

    /**
     * Should Get Empty List Of Plugins If Database Tables has No Records In Plugin Tables
     */
    @Test
    public void shouldGetEmptyListOfPluginsIfDatabaseTableshasNoRecordsInPluginTables() {
        List< PluginEntity > expected = new ArrayList<>();
        EasyMock.expect( dao.getPluginList( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( expected ).anyTimes();
        mockControl.replay();
        List< PluginDTO > actual = manager.getPluginList();
        Assert.assertTrue( actual.isEmpty() );

    }

    /**
     * Should Get Empty List Of Plugins If Dao Return Null
     */
    @Test
    public void shouldGetEmptyListOfPluginsIfDaoReturnNull() {
        List< PluginEntity > expected = null;
        EasyMock.expect( dao.getPluginList( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( expected ).anyTimes();
        mockControl.replay();
        List< PluginDTO > actual = manager.getPluginList();
        Assert.assertTrue( actual.isEmpty() );
    }

    /***************************** HELPER METHODS ************************************/

    /**
     * Get Filled Plugin Entity List
     *
     * @return
     */
    private List< PluginEntity > getListofPluginEntity() {
        List< PluginEntity > list = new ArrayList<>();
        list.add( filledPluginEntity() );
        return list;
    }

    /**
     * Fill Plugin Entity
     *
     * @return
     */
    private PluginEntity filledPluginEntity() {

        entity.setAuthor( AUTHOR );
        entity.setPluginId( ID );
        entity.setCompaitableVersion( COMPATIBLE_VERSION );
        entity.setLicense( LICENSE );
        entity.setPluginName( NAME );
        entity.setSource( SOURCE );
        entity.setStatus( STATUS );
        entity.setVersion( VERSION );
        entity.setGroupId( GROUP_ID );
        entity.setArtifactId( ARTIFACT_ID );
        return entity;
    }

    /**
     * Fill Plugin Entity
     *
     * @return
     */
    private PluginDTO filledPluginDTO() {
        pluginDto = new PluginDTO();
        pluginDto.setAuthor( AUTHOR );
        pluginDto.setId( ID.toString() );
        pluginDto.setCompaitableVersion( COMPATIBLE_VERSION );
        pluginDto.setLicense( LICENSE );
        pluginDto.setName( NAME );
        pluginDto.setSource( SOURCE );
        pluginDto.setStatus( STATUS );
        pluginDto.setVersion( VERSION );
        pluginDto.setGroupId( GROUP_ID );
        pluginDto.setArtifactId( ARTIFACT_ID );
        return pluginDto;
    }

}
