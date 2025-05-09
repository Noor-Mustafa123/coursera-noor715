package de.soco.software.simuspace.suscore.common.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.suscore.jsonschema.model.OVAConfigTab;

/**
 * Test Cases for Class SubTabsUI
 *
 * @author Nosheen.Sharif
 */

@RunWith( PowerMockRunner.class )
public class SubTabsUITest {

    /**
     * Setup the class for mocks
     */
    @Before
    public void setUp() {

    }

    /***************************************
     * Helper Methods
     ************************************************/
    /**
     * Dummy Tabs For Test Expected Case
     *
     * @return List of Tabs names
     */

    private List< String > getGeneralTabsList() {
        List< String > subTbs = new ArrayList<>();
        subTbs.add( SusConstantObject.PROPERTIES_TAB );
        subTbs.add( SusConstantObject.VERSIONS_TAB );
        subTbs.add( SusConstantObject.PERMISSION_TAB );
        subTbs.add( SusConstantObject.METADATA_TAB );
        return subTbs;
    }

    /**
     * Returns a dummpy list of tabs read from {@link OVAConfigFilePathReader}
     *
     * @return
     */
    private List< OVAConfigTab > getTabsList() {
        List< OVAConfigTab > configTabs = new ArrayList<>();
        List< String > keys = Arrays.asList( SusConstantObject.PROPERTIES_TAB, SusConstantObject.VERSIONS_TAB,
                SusConstantObject.PERMISSION_TAB );
        for ( String key : keys ) {
            configTabs.add( new OVAConfigTab( key, StringUtils.capitalize( key ), true ) );
        }
        return configTabs;
    }

}
