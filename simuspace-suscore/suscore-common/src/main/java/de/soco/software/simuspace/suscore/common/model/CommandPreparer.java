package de.soco.software.simuspace.suscore.common.model;

import java.util.Arrays;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * The type Command preparer.
 */
public class CommandPreparer {

    /**
     * The constant SUDO_COMMAND_LENGTH.
     */
    private static final int SUDO_COMMAND_LENGTH = PropertiesManager.getSudoCommandConfigurations().length;

    /**
     * Instantiates a new Command preparer.
     */
    private CommandPreparer() {
    }

    /**
     * Instantiates a new Command preparer.
     *
     * @param size
     *         the size
     *
     * @return the string [ ]
     */
    public static String[] prepareCommand( int size ) {
        var command = new String[ size ];
        // Initialize the command array with empty strings
        Arrays.fill( command, ConstantsString.EMPTY_STRING );
        return command;
    }

    /**
     * Sets user uid.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param userUid
     *         the user uid
     */
    public static void setUserUid( boolean isImpersonated, String[] command, String userUid ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_ZERO ] = userUid;
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_ELEVEN ] = userUid;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_TEN ] = userUid;
        }
    }

    /**
     * Sets engine script file.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param scriptFile
     *         the script file
     */
    public static void setEngineScriptFile( boolean isImpersonated, String[] command, String scriptFile ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_ONE ] = scriptFile;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_ZERO ] = scriptFile;
        }
    }

    /**
     * Sets cb 2 param file.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param paramFile
     *         the param file
     */
    public static void setCB2ParamFile( boolean isImpersonated, String[] command, String paramFile ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_SEVEN ] = paramFile;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_SIX ] = paramFile;
        }
    }

    /**
     * Sets java path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param javaPath
     *         the java path
     */
    public static void setJavaPath( boolean isImpersonated, String[] command, String javaPath ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_TWO ] = javaPath;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_ONE ] = javaPath;
        }
    }

    /**
     * Sets karaf path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param karafPath
     *         the karaf path
     */
    public static void setKarafPath( boolean isImpersonated, String[] command, String karafPath ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_THREE ] = karafPath;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_TWO ] = karafPath;
        }
    }

    /**
     * Sets python path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param pythonPath
     *         the python path
     */
    public static void setPythonPath( boolean isImpersonated, String[] command, String pythonPath ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_FOUR ] = pythonPath;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_THREE ] = pythonPath;
        }
    }

    /**
     * Sets operation.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param operation
     *         the operation
     */
    public static void setOperation( boolean isImpersonated, String[] command, String operation ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_FIVE ] = operation;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_FOUR ] = operation;
        }
    }

    /**
     * Gets operation.
     *
     * @param command
     *         the command
     *
     * @return the operation
     */
    public static String getOperation( String[] command ) {
        if ( command[ 0 ].equals( "sudo" ) ) {
            return command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_FIVE ];
        } else {
            return command[ ConstantsInteger.INTEGER_VALUE_FOUR ];
        }
    }

    /**
     * Sets script path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param scriptPath
     *         the workflow path
     */
    public static void setScriptPath( boolean isImpersonated, String[] command, String scriptPath ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_SIX ] = scriptPath;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_FIVE ] = scriptPath;
        }
    }

    /**
     * Sets engine path.
     *
     * @param isImpersonated
     *         is impersonated
     * @param command
     *         the command
     * @param scriptPath
     *         the script path
     */
    public static void setEnginePath( boolean isImpersonated, String[] command, String scriptPath ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_SIX ] = scriptPath;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_FIVE ] = scriptPath;
        }
    }

    /**
     * Sets work flow path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param workflowPath
     *         the workflow path
     */
    public static void setWorkFlowPath( boolean isImpersonated, String[] command, String workflowPath ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_SEVEN ] = workflowPath;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_SIX ] = workflowPath;
        }
    }

    /**
     * Sets work flow path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param treePath
     *         the tree path
     */
    public static void setCB2TreePath( boolean isImpersonated, String[] command, String treePath ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_SIX ] = treePath;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_FIVE ] = treePath;
        }
    }

    /**
     * Sets process id.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param processId
     *         the process id
     */
    public static void setProcessId( boolean isImpersonated, String[] command, String processId ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_SIX ] = processId;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_FIVE ] = processId;
        }
    }

    /**
     * Sets server dir or file path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param serverDirOrFilePath
     *         the server dir or file path
     */
    public static void setServerDirOrFilePath( boolean isImpersonated, String[] command, String serverDirOrFilePath ) {
        String replaced = serverDirOrFilePath.contains( ConstantsString.SPACE ) ? serverDirOrFilePath.replace( ConstantsString.SPACE,
                ConstantsString.SPACE_ALTERNATE ) : serverDirOrFilePath;
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_SEVEN ] = replaced;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_SIX ] = replaced;
        }
    }

    /**
     * Sets src path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param srcPath
     *         the src path
     */
    public static void setSrcPath( boolean isImpersonated, String[] command, String srcPath ) {
        setServerDirOrFilePath( isImpersonated, command, srcPath );
    }

    /**
     * Sets cb 2 oid.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param oid
     *         the oid
     */
    public static void setCB2Oid( boolean isImpersonated, String[] command, String oid ) {
        setServerDirOrFilePath( isImpersonated, command, oid );
    }

    /**
     * Sets cb 2 object type.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param objectType
     *         the object type
     */
    public static void setCB2ObjectType( boolean isImpersonated, String[] command, String objectType ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_TEN ] = objectType;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_NINE ] = objectType;
        }
    }

    /**
     * Sets file data.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param fileData
     *         the file data
     */
    public static void setFileData( boolean isImpersonated, String[] command, String fileData ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_EIGHT ] = fileData;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_SEVEN ] = fileData;
        }
    }

    /**
     * Sets dest path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param destPath
     *         the dest path
     */
    public static void setDestPath( boolean isImpersonated, String[] command, String destPath ) {
        setFileData( isImpersonated, command, destPath );
    }

    /**
     * Sets action.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param action
     *         the action
     */
    public static void setAction( boolean isImpersonated, String[] command, String action ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_NINE ] = action;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_EIGHT ] = action;
        }
    }

    /**
     * Sets process flag.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param flag
     *         the flag
     */
    public static void setProcessFlag( boolean isImpersonated, String[] command, String flag ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_NINE ] = flag;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_EIGHT ] = flag;
        }
    }

    /**
     * Sets temp path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param tempPath
     *         the temp path
     */
    public static void setTempPath( boolean isImpersonated, String[] command, String tempPath ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_TEN ] = tempPath;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_NINE ] = tempPath;
        }
    }

    /**
     * Sets password.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param password
     *         the password
     */
    public static void setPassword( boolean isImpersonated, String[] command, String password ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_TWELVE ] = password;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_ELEVEN ] = password;
        }
    }

    /**
     * Sets execution host address.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param address
     *         the address
     */
    public static void setExecutionHostAddress( boolean isImpersonated, String[] command, String address ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_THIRTEEN ] = address;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_TWELVE ] = address;
        }
    }

    /**
     * Sets execution command.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param executionCommand
     *         the execution command
     */
    public static void setExecutionCommand( boolean isImpersonated, String[] command, String executionCommand ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_FOURTEEN ] = executionCommand;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_THIRTEEN ] = executionCommand;
        }
    }

}
