package de.soco.software.simuspace.suscore.lifecycle.manager.impl.test;

import javax.persistence.EntityManager;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.lifecycle.manager.impl.LifeCycleManagerImpl;
import de.soco.software.suscore.jsonschema.reader.ConfigFilePathReader;

/**
 * Test Class Containing Test Cases for Class LifeCycleManagerImpl
 *
 * @author Nosheen.Sharif
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { ConfigFilePathReader.class, JsonUtils.class, PropertiesManager.class } )
public class LifeCycleManagerImplTest {

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The Constant LIFE_CYCLE_CONFIG_PATH.
     */
    private static final String LIFE_CYCLE_CONFIG_PATH = ConstantsString.TEST_RESOURCE_PATH + "lifeCycle.json";

    /**
     * The Constant LIFE_CYCLE_INVALID_CONFIG_PATH.
     */
    private static final String LIFE_CYCLE_INVALID_CONFIG_PATH = ConstantsString.TEST_RESOURCE_PATH + "lifeCycle_Invalid.json";

    /**
     * The Constant VALID_LIFE_CYCLE_NAME.
     */
    private static final String VALID_LIFE_CYCLE_NAME = "ca836b74-d07c-4df2-8e57-c3d608523116";

    /**
     * The Constant INVALID_LIFE_CYCLE_NAME.
     */
    private static final String INVALID_LIFE_CYCLE_NAME = "LC_Object_22";

    /**
     * The Constant VALID_LIFE_CYCLE_STATUS_ID.
     */
    private static final String VALID_LIFE_CYCLE_STATUS_ID = "553536c7-71ec-409d-8f48-ec779a98a68e";

    /**
     * The Constant VALID_LIFE_CYCLE_STATUS_NAME.
     */
    private static final String VALID_LIFE_CYCLE_STATUS_NAME = "WIP";

    /**
     * The Constant DELETED_LIFE_CYCLE_STATUS.
     */
    private static final String REMOVED_LIFE_CYCLE_STATUS = "c4a79d62-603e-4477-81a3-b4ea25ef381f";

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * Generic Rule for the expected exception
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * The LifeCycleManagerImpl manager reference.
     */
    private LifeCycleManagerImpl manager;

    /**
     * SusDao reference.
     */
    private SuSGenericObjectDAO< SuSEntity > dao;

    private List< String > list = new ArrayList< String >();

    /**
     * Sets the up.
     *
     * @throws Exception
     *         the exception
     */
    @Before
    public void setUp() throws Exception {
        mockControl().resetToNice();
        manager = new LifeCycleManagerImpl();
        dao = mockControl().createMock( SuSGenericObjectDAO.class );
        manager.setSusDAO( dao );
        PowerMockito.mockStatic( ConfigFilePathReader.class );

    }

    /**
     * Should success fully initialize valid life cycle file.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldSuccessFullyInitializeValidLifeCycleFile() throws Exception {
        mockLifeCycle( LIFE_CYCLE_CONFIG_PATH );
        EasyMock.expect( dao.findAll( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( new ArrayList< SuSEntity >() ).anyTimes();
        mockControl.replay();
        manager.init();
    }

    /**
     * Should not success fully initialize in valid life cycle file.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldNotSuccessFullyInitializeInValidLifeCycleFile() throws Exception {
        mockStaticMethodOfPropertiesUtilClass();
        mockLifeCycle( LIFE_CYCLE_INVALID_CONFIG_PATH );
        thrown.expect( SusException.class );
        EasyMock.expect( dao.findAll( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( new ArrayList< SuSEntity >() ).anyTimes();
        EasyMock.expect( dao.getAllLifeCycleStatusIds( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( list ).anyTimes();
        mockControl.replay();
        manager.init();
    }

    /**
     * Should not successfully initialize when used status is missing in configuration.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldNotSuccessfullyInitializeWhenUsedStatusIsMissingInConfiguration() throws Exception {
        mockStaticMethodOfPropertiesUtilClass();
        mockLifeCycle( LIFE_CYCLE_INVALID_CONFIG_PATH );
        thrown.expect( SusException.class );
        EasyMock.expect( dao.findAll( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( createSusEntityList() ).anyTimes();
        EasyMock.expect( dao.getAllLifeCycleStatusIds( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( list ).anyTimes();
        mockControl.replay();
        manager.init();
    }

    /**
     * Should get successfully get default status if life cycle exist in configration file with given name.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldGetSuccessfullyGetDefaultStatusIfLifeCycleExistInConfigrationFileWithGivenName() throws Exception {
        mockStaticMethodOfPropertiesUtilClass();
        mockLifeCycle( LIFE_CYCLE_CONFIG_PATH );
        EasyMock.expect( dao.findAll( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( new ArrayList< SuSEntity >() ).anyTimes();
        EasyMock.expect( dao.getAllLifeCycleStatusIds( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( list ).anyTimes();
        mockControl.replay();
        manager.init();
        String expectedName = VALID_LIFE_CYCLE_NAME;
        StatusDTO actual = manager.getDefaultStatusByLifeCycleId( expectedName );
        Assert.assertNotNull( actual );
        Assert.assertEquals( VALID_LIFE_CYCLE_STATUS_NAME, actual.getName() );
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
     * Should not get default status if life cycle does not exist in configration file with given name.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldNotGetDefaultStatusIfLifeCycleDoesNotExistInConfigrationFileWithGivenName() throws Exception {
        mockLifeCycle( LIFE_CYCLE_CONFIG_PATH );
        EasyMock.expect( dao.findAll( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( new ArrayList< SuSEntity >() ).anyTimes();
        mockControl.replay();
        manager.init();
        String expectedName = null;
        StatusDTO actual = manager.getDefaultStatusByLifeCycleId( expectedName );
        Assert.assertEquals( expectedName, actual );
    }

    /**
     * Should get successfully get status if life cycle exist in configration file with given name and status id.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldGetSuccessfullyGetStatusIfLifeCycleExistInConfigrationFileWithGivenNameAndStatusId() throws Exception {
        mockStaticMethodOfPropertiesUtilClass();
        mockLifeCycle( LIFE_CYCLE_CONFIG_PATH );
        EasyMock.expect( dao.findAll( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( new ArrayList< SuSEntity >() ).anyTimes();
        EasyMock.expect( dao.getAllLifeCycleStatusIds( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( list ).anyTimes();
        mockControl.replay();
        manager.init();
        String expectedName = VALID_LIFE_CYCLE_NAME;
        StatusDTO actual = manager.getStatusByLifeCycleNameAndStatusId( expectedName, VALID_LIFE_CYCLE_STATUS_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( VALID_LIFE_CYCLE_STATUS_NAME, actual.getName() );
    }

    /**
     * Should not get status if life cycle does not exist in configration file with given name and status id.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldNotGetStatusIfLifeCycleDoesNotExistInConfigrationFileWithGivenNameAndStatusId() throws Exception {
        mockLifeCycle( LIFE_CYCLE_CONFIG_PATH );
        EasyMock.expect( dao.findAll( EasyMock.anyObject( EntityManager.class ) ) ).andReturn( new ArrayList< SuSEntity >() ).anyTimes();
        mockControl.replay();
        manager.init();
        StatusDTO expected = null;
        StatusDTO actual = manager.getStatusByLifeCycleNameAndStatusId( INVALID_LIFE_CYCLE_NAME, VALID_LIFE_CYCLE_STATUS_ID );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Mock life cycle.
     *
     * @throws Exception
     *         the exception
     */
    private void mockLifeCycle( String filePath ) throws Exception {

        Mockito.when( ConfigFilePathReader.getFileByNameFromKaraf( Mockito.anyString() ) ).thenReturn( new FileInputStream( filePath ) );

    }

    /**
     * Creates the sus entity list.
     *
     * @return the list
     */
    private List< SuSEntity > createSusEntityList() {
        List< SuSEntity > suSEntities = new ArrayList<>();
        DataObjectEntity dataObjectEntity = new DataObjectEntity();
        dataObjectEntity.setLifeCycleStatus( REMOVED_LIFE_CYCLE_STATUS );
        suSEntities.add( dataObjectEntity );
        return suSEntities;

    }

}
