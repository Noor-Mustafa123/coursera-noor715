package de.soco.software.simuspace.suscore.data.manager.impl.base;

import javax.persistence.EntityManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.cxf.helpers.IOUtils;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.core.TreeNode;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.activator.Activator;
import de.soco.software.simuspace.suscore.data.common.dao.ContextMenuDAO;
import de.soco.software.simuspace.suscore.data.entity.ObjectJsonSchemaEntity;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.RouterConfigItem;
import de.soco.software.simuspace.suscore.data.model.RouterConfigList;

/**
 * A class responsible to test the public methods of ContextMenuManagerImpl.
 *
 * @author M.Nasir.Farooq
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { Activator.class } )
public class ContextMenuManagerImplTest {

    /**
     * contextMenuManagerImpl instance.
     */
    private static ContextMenuManagerImpl contextMenuManagerImpl;

    /**
     * contextMenuDAO instance.
     */
    private static ContextMenuDAO contextMenuDAO;

    /**
     * The Constant PLUGIN_NAME.
     */
    private static final String PLUGIN_NAME = "test_plugin";

    /**
     * The Constant DELETE_OBJECT_CONTEXT_URL.
     */
    public static final String DELETE_OBJECT_CONTEXT_URL = "delete/data/object/{objectId}/?mode=single";

    /**
     * The Constant OBJECT_ID_PARAM.
     */
    public static final String OBJECT_ID_PARAM = "{objectId}";

    /**
     * The constant for create container.
     */
    public static final String DELETE_OBJECT_TITLE = "4100007x4";

    /**
     * The Constant OBJECT_ID.
     */
    private static final String OBJECT_ID = UUID.randomUUID().toString();

    /**
     * The Constant SYNC_CONTEXT_URL.
     */
    private static final String SYNC_CONTEXT_URL = "Sync Url";

    /**
     * The Constant SYNC_CONTEXT_TITLE.
     */
    private static final String SYNC_CONTEXT_TITLE = "Sync Files";

    /**
     * The Constant ROUTER_FILE_PATH.
     */
    private static final String ROUTER_FILE_PATH = "src/test/resources/router.json";

    /**
     * The Constant STATIC_METHOD_GET_ROUTERS_BY_PLUGIN_NAME.
     */
    private static final String STATIC_METHOD_GET_ROUTERS_BY_PLUGIN_NAME = "getRoutersBypluginName";

    /**
     * The Constant STRING_ENCODING.
     */
    private static final String STRING_ENCODING = "UTF-8";

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    private static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * Setup things before it starts testing.
     */
    @BeforeClass
    public static void beforeClass() {
        mockControl().resetToNice();
        contextMenuDAO = mockControl().createMock( ContextMenuDAO.class );
        contextMenuManagerImpl = new ContextMenuManagerImpl();
        contextMenuManagerImpl.setContextMenuDAO( contextMenuDAO );
    }

    /**
     * Should return bulk delete context menu item when more then one ids are selected in get context menu filter.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldReturnBulkDeleteContextMenuItemWhenValidItemsAreSelectedInGetContextMenuFilter() throws Exception {
        mockActivatorStaticFunctions();
        FiltersDTO filtersDTO = new FiltersDTO();
        List< Object > selectedIds = new ArrayList<>();
        selectedIds.add( UUID.randomUUID() );
        selectedIds.add( UUID.randomUUID() );
        filtersDTO.setItems( selectedIds );
        UUID selectionId = UUID.randomUUID();

        ObjectJsonSchemaEntity masterEntity = new ObjectJsonSchemaEntity();
        masterEntity.setId( UUID.randomUUID() );

        EasyMock.expect( contextMenuDAO.save( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( masterEntity )
                .anyTimes();
        mockControl.replay();
        List< ContextMenuItem > contextMenuItems = contextMenuManagerImpl.getContextMenu( EasyMock.anyObject( EntityManager.class ),
                PLUGIN_NAME, TreeNode.class, filtersDTO );
        Assert.assertFalse( contextMenuItems.isEmpty() );
        Assert.assertEquals( ConstantsInteger.INTEGER_VALUE_ONE, contextMenuItems.size() );
        List< RouterConfigItem > routerConfigItems = Activator.getRoutersBypluginName( PLUGIN_NAME );

        assertRoutersForBulkDeleteMode( contextMenuItems, routerConfigItems, selectionId );
    }

    /**
     * Assert routers for bulk delete mode.
     *
     * @param contextMenuItems
     *         the context menu items
     * @param routerConfigItems
     *         the router config items
     * @param selectionId
     *         the selection id
     */
    private void assertRoutersForBulkDeleteMode( List< ContextMenuItem > contextMenuItems, List< RouterConfigItem > routerConfigItems,
            UUID selectionId ) {
        for ( ContextMenuItem contextMenuItem : contextMenuItems ) {
            for ( RouterConfigItem routerConfigItem : routerConfigItems ) {

                if ( routerConfigItem.getType().contentEquals( RouterConfigItem.TREE_CONTEXT )
                        && routerConfigItem.getUrl().contains( RouterConfigItem.ID_PATTERN )
                        && routerConfigItem.getUrl().contains( RouterConfigItem.QUERY_PATTERN ) ) {
                    String url = routerConfigItem.getUrl().replace( RouterConfigItem.ID_PATTERN, selectionId.toString() )
                            .replace( RouterConfigItem.QUERY_PATTERN, RouterConfigItem.MODE_BULK );
                    routerConfigItem.setUrl( url );
                    routerConfigItem.setTitle(
                            MessageBundleFactory.getMessage( routerConfigItem.getTitle() ) + ContextMenuItem.BULK_SUFIX_TO_TITLE );
                }
                if ( contextMenuItem.getUrl().contentEquals( routerConfigItem.getUrl() ) ) {
                    Assert.assertTrue( contextMenuItem.isEqualsTo( routerConfigItem ) );
                    break;
                }
            }
        }
    }

    /**
     * Assert routers for single delete mode.
     *
     * @param contextMenuItems
     *         the context menu items
     * @param routerConfigItems
     *         the router config items
     * @param selectedIds
     *         the selected ids
     */
    private void assertRoutersForSingleDeleteMode( List< ContextMenuItem > contextMenuItems, List< RouterConfigItem > routerConfigItems,
            List< Object > selectedIds ) {
        for ( ContextMenuItem contextMenuItem : contextMenuItems ) {
            for ( RouterConfigItem routerConfigItem : routerConfigItems ) {

                if ( TreeNode.class.getSimpleName().equals( routerConfigItem.getClassName() )
                        && routerConfigItem.getType().contentEquals( RouterConfigItem.TREE_CONTEXT )
                        && routerConfigItem.getUrl().contains( RouterConfigItem.ID_PATTERN )
                        && routerConfigItem.getUrl().contains( RouterConfigItem.QUERY_PATTERN ) ) {
                    String urlWithParam = routerConfigItem.getUrl()
                            .replace( RouterConfigItem.ID_PATTERN, selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ).toString() )
                            .replace( RouterConfigItem.QUERY_PATTERN, RouterConfigItem.MODE_SINGLE );
                    routerConfigItem.setUrl( urlWithParam );
                } else if ( TreeNode.class.getSimpleName().equals( routerConfigItem.getClassName() )
                        && routerConfigItem.getType().contentEquals( RouterConfigItem.TREE_CONTEXT )
                        && routerConfigItem.getUrl().contains( RouterConfigItem.ID_PATTERN ) ) {
                    String url = routerConfigItem.getUrl().replace( RouterConfigItem.ID_PATTERN,
                            selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ).toString() );
                    routerConfigItem.setUrl( url );
                }
                if ( contextMenuItem.getUrl().contentEquals( routerConfigItem.getUrl() ) ) {
                    Assert.assertTrue( contextMenuItem.isEqualsTo( routerConfigItem ) );
                    break;
                }
            }
        }
    }

    /**
     * A method to mock static functions.
     *
     * @throws Exception
     *         the exception
     */
    private static void mockActivatorStaticFunctions() throws Exception {

        PowerMockito.spy( Activator.class );

        InputStream targetStream = new FileInputStream( new File( ROUTER_FILE_PATH ) );
        String routerText = IOUtils.toString( targetStream, STRING_ENCODING );
        RouterConfigList licenseRouterConfig = JsonUtils.jsonToObject( routerText, RouterConfigList.class );

        PowerMockito.doReturn( licenseRouterConfig.getRoutes() ).when( Activator.class, STATIC_METHOD_GET_ROUTERS_BY_PLUGIN_NAME,
                PLUGIN_NAME );
    }

    /**
     * Should return workflow context menu item when valid parameter is provided.
     */
    @Test
    public void shouldReturnWorkflowContextMenuItemWhenValidParameterIsProvided() {
        FiltersDTO expectedSelection = new FiltersDTO();
        expectedSelection.setItems( Arrays.asList( OBJECT_ID ) );
        List< ContextMenuItem > expected = new ArrayList<>();
        expected.add( prepareDeleteObjectContext( UUID.fromString( OBJECT_ID ) ) );
        List< ContextMenuItem > actual = contextMenuManagerImpl.getWorkflowContextMenu( PLUGIN_NAME, OBJECT_ID, FiltersDTO.class,
                expectedSelection );
        Assert.assertNotNull( actual );
        Assert.assertEquals( expected.size(), actual.size() );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Prepare delete object context.
     *
     * @param objectId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareDeleteObjectContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( DELETE_OBJECT_CONTEXT_URL.replace( OBJECT_ID_PARAM, ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( DELETE_OBJECT_TITLE ) );
        return containerCMI;
    }

}
