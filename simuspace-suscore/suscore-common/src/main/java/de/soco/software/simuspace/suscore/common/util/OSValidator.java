package de.soco.software.simuspace.suscore.common.util;

/**
 * The Class OSValidator provides any Operating System detection for server acknowledge.
 *
 * @author Ahsan Khan
 */
public class OSValidator {

    /**
     * The Constant OS_NAME.
     */
    private static final String OS_NAME = "os.name";

    /**
     * The Constant DETECT_WINDOWS.
     */
    private static final String DETECT_WINDOWS = "win";

    /**
     * The Constant DETECT_MAC.
     */
    private static final String DETECT_MAC = "mac";

    /**
     * The Constant DETECT_NIX.
     */
    private static final String DETECT_NIX = "nix";

    /**
     * The Constant DETECT_NUX.
     */
    private static final String DETECT_NUX = "nux";

    /**
     * The Constant DETECT_AIX.
     */
    private static final String DETECT_AIX = "aix";

    /**
     * The Constant DETECT_SOLARIS.
     */
    private static final String DETECT_SOLARIS = "sunos";

    /**
     * The os.
     */
    private static final String OS = System.getProperty( OS_NAME ).toLowerCase();

    /**
     * Checks if is windows.
     *
     * @return true, if is windows
     */
    public static boolean isWindows() {
        return ( OS.contains( DETECT_WINDOWS ) );
    }

    /**
     * Checks if is mac.
     *
     * @return true, if is mac
     */
    public static boolean isMac() {
        return ( OS.contains( DETECT_MAC ) );
    }

    /**
     * Checks if is unix.
     *
     * @return true, if is unix
     */
    public static boolean isUnix() {
        return ( OS.contains( DETECT_NIX ) || OS.contains( DETECT_NUX ) || OS.contains( DETECT_AIX ) );
    }

    /**
     * Checks if is solaris.
     *
     * @return true, if is solaris
     */
    public static boolean isSolaris() {
        return ( OS.contains( DETECT_SOLARIS ) );
    }

    /**
     * Gets the operation system name.
     *
     * @return the operation system name
     */
    public static String getOperationSystemName() {
        return OS;
    }

    public static String convertPathToRelitiveOS( String path ) {
        if ( isUnix() ) {
            path = path.replace( "\\", "/" );
        } else {
            path = path.replace( "/", "\\" );
        }
        return path;
    }

    public static String convertToLinuxPath( String path ) {
        return path.replace( "\\", "/" );
    }

}
