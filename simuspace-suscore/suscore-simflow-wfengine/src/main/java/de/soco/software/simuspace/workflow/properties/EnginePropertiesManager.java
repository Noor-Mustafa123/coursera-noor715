package de.soco.software.simuspace.workflow.properties;

import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;

/**
 * The Engine properties manager.
 *
 * @author Ali Haider
 */
public class EnginePropertiesManager {

    /**
     * The instance.
     */
    private static EnginePropertiesManager instance = null;

    /**
     * The Props
     */
    private static Map< String, Object > props;

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static EnginePropertiesManager getInstance() {
        if ( instance == null ) {
            instance = new EnginePropertiesManager();
        }
        return instance;
    }

    /**
     * Sets props.
     *
     * @param props
     *         the props
     */
    public static void setProps( Map< String, Object > props ) {
        EnginePropertiesManager.props = props;
    }

    /**
     * Checks for translation.
     *
     * @return true, if successful
     */
    public static boolean hasTranslation() {
        return ( Boolean ) props.get( ConstantsFileProperties.TRANSLATION );
    }

    /**
     * Gets the karaf base path.
     *
     * @return the karaf base path
     */
    public static String getKarafBasePath() {
        return ( String ) props.get( "karaf.base" );
    }

    /**
     * Gets the karaf script path.
     *
     * @return the karaf script path
     */
    public static String getKarafScriptPath() {
        return ( String ) props.get( "karaf.script" );
    }

    /**
     * Gets file extensions.
     *
     * @param className
     *         the class name
     *
     * @return the file extensions
     */
    public static List< String > getFileExtensions( String className ) {
        return ( ( Map< String, List< String > > ) props.get( ConstantsFileProperties.FILE_EXTENSIONS ) ).get( className );
    }

}
