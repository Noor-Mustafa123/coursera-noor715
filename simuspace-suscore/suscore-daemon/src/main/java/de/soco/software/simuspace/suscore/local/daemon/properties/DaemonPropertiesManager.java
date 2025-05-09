package de.soco.software.simuspace.suscore.local.daemon.properties;

import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;

/**
 * A singleton utility class that would load the sus.simuspace.cfg file properties.
 *
 * @author Ali Haider
 */
public class DaemonPropertiesManager {

    /**
     * The instance.
     */
    private static DaemonPropertiesManager instance = null;

    /**
     * The Props
     */
    private static Map< String, Object > props;

    /**
     * Gets the single instance of DaemonPropertiesManager.
     *
     * @return single instance of DaemonPropertiesManager
     */
    public static DaemonPropertiesManager getInstance() {
        if ( instance == null ) {
            instance = new DaemonPropertiesManager();
        }
        return instance;
    }

    /**
     * Gets the props.
     *
     * @return the props
     */
    public Map< String, Object > getProps() {
        return props;
    }

    /**
     * Sets the props.
     *
     * @param props
     *         the props
     */
    public void setProps( Map< String, Object > props ) {
        DaemonPropertiesManager.props = props;
    }

    /**
     * Gets the mat plot lib.
     *
     * @return the mat plot lib
     */
    public static List< String > getMatPlotLib() {
        return ( List< String > ) props.get( "matplotlibPaths" );
    }

    /**
     * Has translation boolean.
     *
     * @return the boolean
     */
    public static boolean hasTranslation() {
        return ( Boolean ) props.get( ConstantsFileProperties.TRANSLATION );
    }

}