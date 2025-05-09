package de.soco.software.simuspace.suscore.common.util;

import java.util.List;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;

/**
 * The type Config util.
 */
public class ConfigUtil {

    /**
     * The type Config.
     */
    public record Config( String file, String label ) {

    }

    /**
     * File names list.
     *
     * @param configs
     *         the configs
     *
     * @return the list
     */
    public static List< String > fileNames( List< Config > configs ) {
        return configs.stream().map( Config::file ).toList();
    }

    /**
     * Config names list.
     *
     * @param configs
     *         the configs
     *
     * @return the list
     */
    public static List< String > labelNames( List< Config > configs ) {
        return configs.stream().map( Config::label ).toList();
    }

    /**
     * Label by file string.
     *
     * @param configs
     *         the configs
     * @param file
     *         the file
     *
     * @return the string
     */
    public static String labelByFile( List< Config > configs, String file ) {
        return configs.stream().filter( config -> config.file.equals( file ) ).map( Config::label ).findFirst()
                .orElse( ConstantsString.EMPTY_STRING );
    }

    /**
     * File by label string.
     *
     * @param configs
     *         the configs
     * @param label
     *         the label
     *
     * @return the string
     */
    public static String fileByLabel( List< Config > configs, String label ) {
        return configs.stream().filter( config -> config.label.equals( label ) ).map( Config::file ).findFirst()
                .orElse( ConstantsString.EMPTY_STRING );
    }

}
