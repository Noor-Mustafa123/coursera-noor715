package de.soco.software.simuspace.suscore.common.properties;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.suscore.jsonschema.reader.ConfigFilePathReader;

/**
 * The Singleton Class DesignPlotingConfig for reading configurations for scheme design plots
 *
 * @author noman arshad
 */
@Log4j2
public class DesignPlotingConfig {

    private static final String DELETE_IN_HOURS = "deleteInHours";

    private static final String MATPLOTLIB = "matplotlib";

    private static final String BUBBLECHART = "bubblechart";

    private static final String HEATMAP = "heatmap";

    private static final String CORRELATION = "correlation";

    /**
     * the singleton instance
     */
    private static DesignPlotingConfig instance = null;

    /**
     * The time to dell static.
     */
    private static String timeToDell;

    /**
     * The heatmap.
     */
    private static String heatmapPath;

    /**
     * The bubblechart.
     */
    private static String bubblechartPath;

    /**
     * The correlation.
     */
    private static String correlationPath;

    /**
     * The matplotlib.
     */
    private static Map< String, String > matplotlibPaths;

    private DesignPlotingConfig() {
        init();
    }

    /**
     * @return the singleton instance
     */
    public static DesignPlotingConfig getInstance() {
        checkInstance();
        return instance;
    }

    /**
     * Checks if singleton instance is initialized. If not, initializes the singleton instance
     */
    public static void checkInstance() {
        if ( instance == null ) {
            log.info( "initializing instance" );
            instance = new DesignPlotingConfig();
        }
    }

    /**
     * the init method
     */
    private void init() {
        readConfig();
    }

    /**
     * Gets the python path by id.
     *
     * @param option
     *         the option
     *
     * @return the python path by id
     */
    public static String getPythonPathById( int option ) {
        checkInstance();
        return switch ( option ) {
            case 1 -> heatmapPath;
            case 2 -> bubblechartPath;
            case 3 -> correlationPath;
            default -> "";
        };
    }

    /**
     * Gets the option name by id.
     *
     * @param option
     *         the option
     *
     * @return the option name by id
     */
    public static String getOptionNameById( int option ) {
        return switch ( option ) {
            case 1 -> ConstantsString.HEATMAP;
            case 2 -> ConstantsString.PLOT_BUBBLE_CHART;
            case 3 -> ConstantsString.PLOT_CORRELATION;
            case 4 -> ConstantsString.GENERATE_IMAGE;
            default -> " ";
        };

    }

    /**
     * Gets the option id by name.
     *
     * @param name
     *         the name
     *
     * @return the option id by name
     */
    public static String getOptionIdByName( String name ) {
        if ( name.equalsIgnoreCase( ConstantsString.HEATMAP_KEY ) ) {
            return "1";
        } else if ( name.equalsIgnoreCase( ConstantsString.PLOT_BUBBLE_CHART_KEY ) ) {
            return "2";
        } else if ( name.equalsIgnoreCase( ConstantsString.PLOT_CORRELATION_KEY ) ) {
            return "3";
        } else if ( name.equalsIgnoreCase( ConstantsString.GENERATE_IMAGE_KEY ) ) {
            return "4";
        }
        return " ";

    }

    /**
     * Gets generate image options.
     *
     * @return the generate image options
     */
    public static Set< String > getGenerateImageOptions() {
        checkInstance();
        return matplotlibPaths != null ? matplotlibPaths.keySet() : new HashSet<>();
    }

    /**
     * Gets generate image python file path.
     *
     * @return the generate image options
     */
    public static String getGenerateImagePythonFilePath( String key ) {
        checkInstance();
        return matplotlibPaths != null ? matplotlibPaths.get( key ) : "";

    }

    /**
     * Reads the config file.
     */
    private static void readConfig() {
        try {
            Map< String, Object > allProperties = ( Map< String, Object > ) JsonUtils
                    .jsonConfToMap( ConfigFilePathReader.PROPERTIES_SCHEME_PLOT );
            Map< String, String > matplotlib = ( Map< String, String > ) allProperties.get( MATPLOTLIB );
            for ( String key : matplotlib.keySet() ) {
                matplotlib.put( key, matplotlib.get( key ).replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH,
                        PropertiesManager.getScriptsPath() ) );
            }
            setMatplotlibPaths( matplotlib );
            setBubblechartPath( ( ( String ) allProperties.get( BUBBLECHART ) )
                    .replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, PropertiesManager.getScriptsPath() ) );
            setHeatmapPath( ( ( String ) allProperties.get( HEATMAP ) ).replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH,
                    PropertiesManager.getScriptsPath() ) );
            setCorrelationPath( ( ( String ) allProperties.get( CORRELATION ) )
                    .replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, PropertiesManager.getScriptsPath() ) );
            setTimeToDell( ( ( String ) allProperties.get( DELETE_IN_HOURS ) ) );

        } catch ( Exception e ) {
            log.error( "Error reading " + ConfigFilePathReader.PROPERTIES_SCHEME_PLOT, e );
            throw new SusException( "Error reading " + ConfigFilePathReader.PROPERTIES_SCHEME_PLOT + e.getMessage() );
        }
    }

    /**
     * @return the time top delete from config
     */
    public static String getTimeToDell() {
        return timeToDell;
    }

    /**
     * @param timeToDell
     *         the time to delete
     */
    public static void setTimeToDell( String timeToDell ) {
        DesignPlotingConfig.timeToDell = timeToDell;
    }

    /**
     * @return the heatmap Path from config
     */
    public static String getHeatmapPath() {
        return heatmapPath;
    }

    /**
     * @param heatmapPath
     *         the heatmap Path
     */
    public static void setHeatmapPath( String heatmapPath ) {
        DesignPlotingConfig.heatmapPath = heatmapPath;
    }

    /**
     * @return the bubblechart Path from config
     */
    public static String getBubblechartPath() {
        return bubblechartPath;
    }

    /**
     * @param bubblechartPath
     *         the bubblechart path
     */
    public static void setBubblechartPath( String bubblechartPath ) {
        DesignPlotingConfig.bubblechartPath = bubblechartPath;
    }

    /**
     * @return the correlation Path from config
     */
    public static String getCorrelationPath() {
        return correlationPath;
    }

    /**
     * @param correlationPath
     *         the correlation Path
     */
    public static void setCorrelationPath( String correlationPath ) {
        DesignPlotingConfig.correlationPath = correlationPath;
    }

    /**
     * @return the matplotlib Paths from config
     */
    public static Map< String, String > getMatplotlibPaths() {
        return matplotlibPaths;
    }

    /**
     * @param matplotlibPaths
     *         the matplotlib Paths
     */
    public static void setMatplotlibPaths( Map< String, String > matplotlibPaths ) {
        DesignPlotingConfig.matplotlibPaths = matplotlibPaths;
    }

}
