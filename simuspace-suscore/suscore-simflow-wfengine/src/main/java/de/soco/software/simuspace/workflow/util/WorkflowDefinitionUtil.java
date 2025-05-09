package de.soco.software.simuspace.workflow.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.TransferOperationType;
import de.soco.software.simuspace.suscore.common.enums.simflow.ElementKeys;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.FileInfo;
import de.soco.software.simuspace.suscore.common.model.ObjectVariableDTO;
import de.soco.software.simuspace.suscore.common.model.ScanFileDTO;
import de.soco.software.simuspace.suscore.common.model.TemplateFileDTO;
import de.soco.software.simuspace.suscore.common.model.TemplateVariableDTO;
import de.soco.software.simuspace.suscore.common.model.TransferLocationObject;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.OSValidator;
import de.soco.software.simuspace.suscore.common.util.ZipUsingJavaUtil;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowDefinitionDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowElement;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.NodeEdge;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.UserWFElementImpl;

/**
 * The Class WorkflowDefinitionUtil.
 *
 * @author noman arshad
 */
@Log4j2
public class WorkflowDefinitionUtil {

    /**
     * The Constant API_LOCATION_EXPORT_FILE.
     */
    private static final String API_LOCATION_EXPORT_FILE = "/api/core/location/export/staging/file";

    /**
     * The FILES_DIR.
     */
    private static final String FILES = "files";

    /**
     * The Constant ITEMS.
     */
    private static final String ITEMS = "items";

    /**
     * The Constant ELEMENTS.
     */
    public static final String ELEMENTS = "elements";

    /**
     * The Constant NODES.
     */
    public static final String NODES = "nodes";

    /**
     * The Constant DATA.
     */
    public static final String DATA = "data";

    /**
     * The Constant FIELDS.
     */
    public static final String FIELDS = "fields";

    public static final String INFO = "info";

    /**
     * The Constant VARIABLE_MODE.
     */
    public static final String VARIABLE_MODE = "variable-mode";

    /**
     * The Constant TYPE.
     */
    public static final String TYPE = "type";

    /**
     * The Constant VALUE.
     */
    public static final String VALUE = "value";

    /**
     * Checks if is local.
     *
     * @param runsOn
     *         the runs on
     *
     * @return true, if is local
     */
    public static boolean isLocal( String runsOn ) {
        return runsOn.equals( LocationsEnum.LOCAL_LINUX.getId() ) || runsOn.equals( LocationsEnum.LOCAL_WINDOWS.getId() );
    }

    /**
     * Gets the workflow definition.
     *
     * @param workflowDefinitionMap
     *         the workflow definition map
     *
     * @return the workflow definition
     */
    public static WorkflowDefinitionDTO getWorkflowDefinitionDTOFromMap( Map< String, Object > workflowDefinitionMap ) {
        String json = null;

        if ( workflowDefinitionMap != null ) {
            log.debug( ">>getWorkflowDefinitionDTOFromMap workflowDefinitionMap " + workflowDefinitionMap );
            json = JsonUtils.toJson( workflowDefinitionMap );
        }
        return JsonUtils.jsonToObject( json, WorkflowDefinitionDTO.class );
    }

    /**
     * Gets the workflow definition.
     *
     * @param workflowDefinitionMap
     *         the workflow definition map
     *
     * @return the workflow definition
     */
    public static JSONObject getWorkflowDefinitionAsJSONObject( Map< String, Object > workflowDefinitionMap ) {
        String json = null;
        try {
            if ( workflowDefinitionMap != null ) {
                log.debug( ">>getWorkflowDefinitionDTOFromMap workflowDefinitionMap " + workflowDefinitionMap );
                json = JsonUtils.toJson( workflowDefinitionMap );
            }
            return JsonUtils.toJSONObject( json );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }

    /**
     * Gets nodes from definition as json object.
     *
     * @param definition
     *         the definition
     *
     * @return the nodes from definition as json object
     */
    public static JSONArray getNodesFromDefinitionAsJSONObject( JSONObject definition ) {
        var elements = ( JSONObject ) definition.get( ELEMENTS );
        if ( elements == null ) {
            return null;
        }
        return ( JSONArray ) elements.get( NODES );
    }

    /**
     * Sets the workflow elements.
     *
     * @param def
     *         the def
     *
     * @return the list
     */
    public static List< UserWFElement > setWorkflowElements( WorkflowDefinitionDTO def ) {
        final List< UserWFElement > userElements = new ArrayList<>();
        for ( final WorkflowElement workflowElement : def.getElements().getNodes() ) {
            final UserWFElement element = workflowElement.getData();
            if ( ElementKeys.getkeys().contains( element.getKey() ) ) {
                userElements.add( element );
            } else {
                final List< NodeEdge > edges = def.getElements().getEdges();
                edges.removeIf(
                        ed -> ed.getData().getSource().equals( element.getId() ) || ed.getData().getTarget().equals( element.getId() ) );
            }
        }
        return userElements;
    }

    /**
     * Scanfilesprepare scan files.
     *
     * @param jobParameters
     *         the job parameters
     * @param fieldTemplateType
     *         the field template type
     *
     * @return the list
     */
    public static List< ScanFileDTO > scanfilesprepareScanFiles( JobParameters jobParameters, String fieldTemplateType ) {
        List< ScanFileDTO > list = new ArrayList<>();
        // Getting workflow elements from job parameter
        final WorkflowDefinitionDTO jobParamDef = getWorkflowDefinitionDTOFromMap( jobParameters.getWorkflow().prepareDefinition() );
        final List< UserWFElement > jobWFElements = setWorkflowElements( jobParamDef );

        // get paths of all local files used in workflow
        for ( UserWFElement element : jobWFElements ) {
            final List< Field< ? > > elementFields = element.getFields();
            for ( final Field< ? > field : elementFields ) {
                if ( field.getType().equals( FieldTypes.REGEX_FILE.getType() ) && field.getTemplateType().equals( fieldTemplateType ) ) {
                    list.add( JsonUtils.jsonToObject( JsonUtils.toJson( field.getValue() ), ScanFileDTO.class ) );
                }
            }
        }
        return list;
    }

    /**
     * Rename local paths to staging path.
     *
     * @param jobWFElements
     *         the job WF elements
     * @param stagingPath
     *         the staging path
     * @param jobParamDef
     *         the job param def
     * @param jobParameters
     *         the job parameters
     */
    public static void renameLocalPathsToStagingPath( List< UserWFElement > jobWFElements, String stagingPath,
            WorkflowDefinitionDTO jobParamDef, JobParameters jobParameters ) {

        for ( UserWFElement element : jobWFElements ) {
            final List< Field< ? > > elementFields = element.getFields();
            for ( final Field< ? > field : elementFields ) {
                if ( ( field.getType().equals( "os-file" ) || field.getType().equals( "os-directory" ) ) && !field.isVariableMode() ) {
                    Map< String, Object > map = ( Map< String, Object > ) field.getValue();
                    ArrayList< String > path = ( ArrayList< String > ) map.get( ITEMS );

                    if ( new File( path.get( 0 ) ).exists() ) {
                        String serverPath = "";
                        if ( field.getType().equals( "os-file" ) ) {
                            serverPath = stagingPath + File.separator + jobParameters.getWorkflow().getId() + File.separator + FILES
                                    + path.get( 0 ).substring( path.get( 0 ).lastIndexOf( File.separator ) );

                        } else if ( field.getType().equals( "os-directory" ) ) {

                            if ( OSValidator.isWindows() ) {
                                serverPath = ( stagingPath + File.separator + jobParameters.getWorkflow().getId() + File.separator + FILES
                                        + path.get( 0 ).substring( path.get( 0 ).lastIndexOf( File.separator ) ) ).replace( "\\", "/" );
                            } else {
                                serverPath = stagingPath + File.separator + jobParameters.getWorkflow().getId() + File.separator + FILES
                                        + path.get( 0 ).substring( path.get( 0 ).lastIndexOf( File.separator ) );
                            }
                        }

                        path.set( 0, serverPath.replace( "\\", "/" ) );
                        map.replace( ITEMS, path );
                    }
                } else if ( field.getType().equals( FieldTypes.REGEX_FILE.getType() ) ) {
                    Map< String, Object > map = ( Map< String, Object > ) field.getValue();
                    String path = map.get( "file" ).toString();

                    if ( new File( path ).exists() ) {
                        String serverPath = stagingPath + File.separator + jobParameters.getWorkflow().getId() + File.separator + FILES
                                + path.substring( path.lastIndexOf( File.separator ) );
                        map.replace( "file", serverPath );
                    }
                }
            }
        }

        updateElementsOfJobParameters( jobParamDef, jobParameters );
    }

    /**
     * Rename local paths to staging path with OS validator.
     *
     * @param jobWFElements
     *         the job WF elements
     * @param stagingPath
     *         the staging path
     * @param jobParamDef
     *         the job param def
     * @param jobParameters
     *         the job parameters
     */
    public static void renameLocalPathsToStagingPathWithOSValidator( List< UserWFElement > jobWFElements, String stagingPath,
            WorkflowDefinitionDTO jobParamDef, JobParameters jobParameters ) {

        for ( UserWFElement element : jobWFElements ) {
            final List< Field< ? > > elementFields = element.getFields();
            for ( final Field< ? > field : elementFields ) {
                if ( ( field.getType().equals( "os-file" ) || field.getType().equals( "os-directory" ) ) && !field.isVariableMode() ) {
                    Map< String, Object > map = ( Map< String, Object > ) field.getValue();
                    ArrayList< String > path = ( ArrayList< String > ) map.get( ITEMS );

                    String serverPath = "";

                    if ( field.getType().equals( "os-file" ) ) {
                        serverPath = stagingPath + File.separator + jobParameters.getWorkflow().getId() + File.separator + FILES
                                + path.get( 0 ).substring( path.get( 0 ).lastIndexOf( File.separator ) );

                    } else if ( field.getType().equals( "os-directory" ) ) {
                        serverPath = stagingPath + File.separator + jobParameters.getWorkflow().getId() + File.separator + FILES
                                + path.get( 0 );
                    }

                    path.set( 0, OSValidator.convertPathToRelitiveOS( serverPath ) );
                    map.replace( ITEMS, path );

                } else if ( field.getType().equals( FieldTypes.REGEX_FILE.getType() ) ) {
                    Map< String, Object > map = ( Map< String, Object > ) field.getValue();
                    String path = map.get( "file" ).toString();

                    String serverPath = stagingPath + File.separator + jobParameters.getWorkflow().getId() + File.separator + FILES
                            + path.substring( path.lastIndexOf( File.separator ) );
                    map.replace( "file", OSValidator.convertPathToRelitiveOS( serverPath ) );

                }
            }
        }

        updateElementsOfJobParameters( jobParamDef, jobParameters );
    }

    /**
     * Update elements of job parameters.
     *
     * @param jobParamDef
     *         the job param def
     * @param jobParameters
     *         the job parameters
     */
    public static void updateElementsOfJobParameters( WorkflowDefinitionDTO jobParamDef, JobParameters jobParameters ) {
        String json = null;
        if ( jobParamDef != null ) {
            json = JsonUtils.objectToJson( jobParamDef );
        }
        Map< String, Object > elementsDef = new HashMap<>();
        elementsDef = ( Map< String, Object > ) JsonUtils.jsonToMap( json, elementsDef );
        jobParameters.getWorkflow().setElements( ( Map< String, Object > ) elementsDef.get( "elements" ) );
    }

    /**
     * Prepare template file.
     *
     * @param scanFile
     *         the scan file
     * @param designSummary
     *         the design summary
     *
     * @return the list
     */
    public static List< String > prepareTemplateFile( List< ScanFileDTO > scanFile, Map< String, Object > designSummary ) {
        List< String > paths = new ArrayList<>();
        for ( ScanFileDTO scanFileDTO : scanFile ) {
            File inputFile;
            String fullPath = null;
            List< ObjectVariableDTO > variables = scanFileDTO.getVariables();
            String fileName = new File( scanFileDTO.getFile() ).getName();
            Map< Integer, ObjectVariableDTO > map = new HashMap<>();
            for ( ObjectVariableDTO objectVariableDTO : variables ) {
                map.put( Integer.parseInt( objectVariableDTO.getHighlight().getLineNumber() ), objectVariableDTO );
            }
            try ( BufferedReader br = new BufferedReader( new FileReader( scanFileDTO.getFile() ) ) ) {
                String line;
                int lineCount = 0;
                inputFile = File.createTempFile( fileName, "" );

                fullPath = inputFile.getAbsolutePath();
                try ( FileWriter writer = new FileWriter( fullPath ) ) {
                    while ( ( line = br.readLine() ) != null ) {
                        StringBuilder buf = new StringBuilder( line );
                        if ( map.containsKey( lineCount ) ) {
                            ObjectVariableDTO response = map.get( lineCount );
                            writer.write( buf.replace( Integer.parseInt( response.getHighlight().getStart() ),
                                    Integer.parseInt( response.getHighlight().getEnd() ),
                                    ( designSummary.get( response.getVariableName().toLowerCase() ) != null
                                            ? designSummary.get( response.getVariableName().toLowerCase() ).toString()
                                            : "0" ) )
                                    + ConstantsString.NEW_LINE );
                        } else {
                            writer.write( buf + ConstantsString.NEW_LINE );
                        }
                        lineCount++;
                    }
                }
            } catch ( NumberFormatException | IOException e ) {
                log.error( "Error", e );
            }

            paths.add( fullPath );
        }
        return paths;
    }

    /**
     * Prepare template file in staging in job container.
     *
     * @param scanFile
     *         the scan file
     * @param designSummary
     *         the design summary
     * @param filePath
     *         the file path
     */
    public static void prepareRegexFileInStagingInJobContainer( List< ScanFileDTO > scanFile, Map< String, Object > designSummary,
            String filePath ) {
        for ( ScanFileDTO scanFileDTO : scanFile ) {
            List< ObjectVariableDTO > variables = scanFileDTO.getVariables();
            Map< Integer, ObjectVariableDTO > map = new HashMap<>();
            for ( ObjectVariableDTO objectVariableDTO : variables ) {
                map.put( Integer.parseInt( objectVariableDTO.getHighlight().getLineNumber() ), objectVariableDTO );
            }
            try ( BufferedReader br = new BufferedReader( new FileReader( scanFileDTO.getFile() ) ) ) {
                String line;
                int lineCount = 0;
                try ( FileWriter writer = new FileWriter( filePath ) ) {
                    while ( ( line = br.readLine() ) != null ) {
                        StringBuilder buf = new StringBuilder( line );
                        if ( map.containsKey( lineCount ) ) {
                            ObjectVariableDTO response = map.get( lineCount );
                            writer.write( buf.replace( Integer.parseInt( response.getHighlight().getStart() ),
                                    Integer.parseInt( response.getHighlight().getEnd() ),
                                    ( designSummary.get( response.getVariableName() ) != null
                                            ? designSummary.get( response.getVariableName() ).toString()
                                            : "0" ) )
                                    + ConstantsString.NEW_LINE );
                        } else {
                            writer.write( buf + ConstantsString.NEW_LINE );
                        }
                        lineCount++;
                    }
                }
            } catch ( NumberFormatException | IOException e ) {
                log.error( "Error", e );
            }
        }
    }

    /**
     * Prepare template file in staging in job container.
     *
     * @param templateFile
     *         the template file
     * @param designSummary
     *         the design summary
     * @param filePath
     *         the file path
     */
    public static void prepareTemplateFileInStagingInJobContainer( List< TemplateFileDTO > templateFile,
            Map< String, Object > designSummary, String filePath ) {
        for ( TemplateFileDTO templateFileDTO : templateFile ) {
            List< TemplateVariableDTO > variables = templateFileDTO.getVariables();
            Map< Integer, TemplateVariableDTO > map = new HashMap<>();
            for ( TemplateVariableDTO templateVariableDTO : variables ) {
                map.put( Integer.parseInt( templateVariableDTO.getLineNumber() ), templateVariableDTO );
            }
            try ( BufferedReader br = new BufferedReader( new FileReader( templateFileDTO.getFile() ) ) ) {
                String line;
                int lineCount = 0;
                try ( FileWriter writer = new FileWriter( filePath ) ) {
                    while ( ( line = br.readLine() ) != null ) {
                        StringBuilder buf = new StringBuilder( line );
                        if ( map.containsKey( lineCount ) ) {
                            TemplateVariableDTO response = map.get( lineCount );
                            writer.write( buf.replace( Integer.parseInt( response.getStart() ), Integer.parseInt( response.getEnd() ),
                                    ( designSummary.get( response.getVariableName().toLowerCase() ) != null
                                            ? designSummary.get( response.getVariableName().toLowerCase() ).toString()
                                            : "0" ) )
                                    + ConstantsString.NEW_LINE );
                        } else {
                            writer.write( buf + ConstantsString.NEW_LINE );
                        }
                        lineCount++;
                    }
                }
            } catch ( NumberFormatException | IOException e ) {
                log.error( "Error", e );
            }
        }
    }

    /**
     * Prepare template file data for staging.
     *
     * @param designSummary
     *         the design summary
     *
     * @return the string builder
     */
    public static StringBuilder prepareRegexFileDataForStaging( String originalStagingFile, Map< String, Object > designSummary,
            Map< Integer, ObjectVariableDTO > map ) {
        StringBuilder bufferStore = new StringBuilder();
        try ( BufferedReader br = new BufferedReader( new FileReader( OSValidator.convertPathToRelitiveOS( originalStagingFile ) ) ) ) {
            String line;
            int lineCount = 0;
            while ( ( line = br.readLine() ) != null ) {
                StringBuilder buf = new StringBuilder( line );
                if ( map.containsKey( lineCount ) ) {
                    ObjectVariableDTO response = map.get( lineCount );

                    bufferStore.append( buf.replace( Integer.parseInt( response.getHighlight().getStart() ),
                            Integer.parseInt( response.getHighlight().getEnd() ),
                            ( designSummary.get( response.getVariableName() ) != null
                                    ? designSummary.get( response.getVariableName() ).toString()
                                    : "0" ) )
                            + ConstantsString.NEW_LINE );
                } else {
                    bufferStore.append( buf + ConstantsString.NEW_LINE );
                }
                lineCount++;
            }
        } catch ( NumberFormatException | IOException e ) {
            log.error( "prepareRegexFileDataForStaging Error", e );
        }
        return bufferStore;
    }

    /**
     * Prepare template file data for staging pak.
     *
     * @param originalFileStagingPath
     *         the original file staging path
     * @param templateFile
     *         the template file
     * @param designSummary
     *         the design summary
     *
     * @return the string builder
     */
    public static StringBuilder prepareTemplateFileDataForStaging( String originalFileStagingPath, List< TemplateFileDTO > templateFile,
            Map< String, Object > designSummary ) {
        StringBuilder bufferStore = new StringBuilder();

        Map< Integer, List< TemplateVariableDTO > > map = new HashMap<>();
        for ( TemplateFileDTO templateFileDTO : templateFile ) {
            List< TemplateVariableDTO > variables = templateFileDTO.getVariables();

            for ( TemplateVariableDTO templateVariableDTO : variables ) {

                if ( map.containsKey( Integer.valueOf( templateVariableDTO.getLineNumber() ) ) ) {
                    List< TemplateVariableDTO > listTemplate = map.get( Integer.valueOf( templateVariableDTO.getLineNumber() ) );
                    listTemplate.add( templateVariableDTO );
                    map.put( Integer.parseInt( templateVariableDTO.getLineNumber() ), listTemplate );
                } else {
                    List< TemplateVariableDTO > listTemplate = new ArrayList<>();
                    listTemplate.add( templateVariableDTO );
                    map.put( Integer.parseInt( templateVariableDTO.getLineNumber() ), listTemplate );
                }
            }

            try {
                designSummary.remove( "expNum" );
                designSummary.remove( "id" );
            } catch ( Exception e ) {
                log.error( "map key removal ERROR : ", e );
            }

            try ( BufferedReader br = new BufferedReader(
                    new FileReader( OSValidator.convertPathToRelitiveOS( originalFileStagingPath ) ) ) ) {
                String line;
                int lineCount = 0;
                while ( ( line = br.readLine() ) != null ) {
                    StringBuilder buf2 = new StringBuilder( line );
                    if ( map.containsKey( lineCount ) ) {
                        List< TemplateVariableDTO > templateList = map.get( lineCount );

                        List< TemplateVariableDTO > animals = templateList.stream()
                                .sorted( Comparator.comparing( TemplateVariableDTO::getEnd ) ).toList();

                        boolean iterationLine = true;
                        int lastIndexFlag = 0;
                        for ( TemplateVariableDTO templateVariableDTO : animals ) {

                            String designVal = getDesignSummaryValueByKey( designSummary, templateVariableDTO.getVariableName() );
                            if ( designVal != null ) {

                                int firstIndex;
                                if ( iterationLine ) {
                                    firstIndex = buf2.toString().indexOf( templateVariableDTO.getMatch() );
                                    iterationLine = false;
                                } else {
                                    firstIndex = buf2.toString().indexOf( templateVariableDTO.getMatch(), lastIndexFlag );
                                }
                                int lastIndex = ( firstIndex + templateVariableDTO.getMatch().length() );
                                buf2.replace( firstIndex, lastIndex, designVal );
                                lastIndexFlag = firstIndex + designVal.length();
                            }
                        }
                        bufferStore.append( buf2 + ConstantsString.NEW_LINE );
                    } else {
                        bufferStore.append( buf2 + ConstantsString.NEW_LINE );
                    }
                    lineCount++;
                }
            } catch ( NumberFormatException | IOException e ) {
                log.error( "Error", e );
            }

        }

        return bufferStore;
    }

    private static String getDesignSummaryValueByKey( Map< String, Object > designSummary, String keyNamevar ) {
        if ( !designSummary.isEmpty() && designSummary.containsKey( keyNamevar ) ) {
            return ( String ) designSummary.get( keyNamevar );
        }
        return null;
    }

    /**
     * Adds the workflow files to given path.
     *
     * @param filePaths
     *         the file paths
     * @param jobParameters
     *         the job parameters
     * @param destinationPath
     *         the destination path
     */
    public static void copyWorkflowFilesZipToGivenPath( Set< String > filePaths, JobParameters jobParameters, String destinationPath ) {
        try {
            String directoryPath = destinationPath + File.separator + jobParameters.getWorkflow().getId();

            File directory = Paths.get( directoryPath ).toFile();
            if ( directory.exists() || directory.mkdirs() ) {
                String zipPath = directoryPath + File.separator + jobParameters.getName();
                FileUtils.zipFiles( new ArrayList<>( filePaths ), zipPath );
                log.debug( "Files Ziped at : " + zipPath );

                File file = new File( destinationPath + File.separator + jobParameters.getWorkflow().getId() + File.separator + FILES );
                file.mkdirs();
                FileUtils.extractZipFile( zipPath, file.getPath() );

            }
        } catch ( Exception e ) {
            log.error( "File extraction Failed :", e );
        }

    }

    /**
     * Export workflow files to location staging.
     *
     * @param jobParameters
     *         the job parameters
     * @param location
     *         the location
     * @param url
     *         the url
     */
    public static void exportWorkflowFilesToLocationStaging( JobParameters jobParameters, LocationDTO location, String url ) {
        TransferLocationObject transferLocationObject = new TransferLocationObject();
        transferLocationObject
                .setFilePath( File.separator + jobParameters.getWorkflow().getId() + File.separator + jobParameters.getName() );
        transferLocationObject.setTargetAddress( location.getUrl() );
        transferLocationObject.setTargetToken( location.getAuthToken() );
        transferLocationObject.setOperation( TransferOperationType.COPY );
        final String josnObjectDTO = JsonUtils.toJsonString( transferLocationObject );
        SuSClient.postRequest( url + API_LOCATION_EXPORT_FILE, josnObjectDTO,
                prepareHeaders( location.getAuthToken(), jobParameters.getJobRunByUserUID() ) );
    }

    /**
     * It adds headers for required for communication with server.<br>
     *
     * @param authToken
     *         the auth token
     *
     * @return requestHeaders
     */
    public static Map< String, String > prepareHeaders( String authToken ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTHORIZATION, authToken );
        return requestHeaders;
    }

    /**
     * It adds headers for required for communication with server.<br>
     *
     * @param authToken
     *         the auth token
     *
     * @return requestHeaders
     */
    public static Map< String, String > prepareHeaders( String authToken, String userUid ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTHORIZATION, authToken );
        requestHeaders.put( ConstantRequestHeader.USER_UID, userUid );

        return requestHeaders;
    }

    /**
     * Upload workflow files to server.
     *
     * @param jobParameters
     *         the job parameters
     * @param url
     *         the url
     * @param filePaths
     *         the filePaths
     *
     * @return the string
     */
    public static String uploadWorkflowFilesToServer( JobParameters jobParameters, String url, Set< String > filePaths,
            Set< String > dirPaths ) {
        String directoryPath = System.getProperty( "java.io.tmpdir" );
        File directory = Paths.get( directoryPath ).toFile();
        String zipPath;
        log.debug( "copying server file with impersonation" );
        if ( !directory.exists() ) {
            LinuxUtils.createDirectory( jobParameters.getJobRunByUserUID(), directory.getAbsolutePath() );
            log.debug( "DIR created Impersonated" );
        }
        zipPath = directoryPath + File.separator + jobParameters.getName();
        try {
            log.debug( "zipping files : " + zipPath );
            ZipUsingJavaUtil.zipFilesAndFolders( filePaths, dirPaths, zipPath );
        } catch ( Exception e ) {
            log.debug( e.getMessage(), e );
        }

        File file = new File( directoryPath + File.separator + jobParameters.getName() );

        String destPath = File.separator + jobParameters.getWorkflow().getId() + File.separator + jobParameters.getName();

        SuSClient.uploadStagingFileRequest( url + "/api/document/stagingupload", file,

                prepareDownloadHeaders( jobParameters.getJobRunByUserUID(), jobParameters.getRequestHeaders().getToken(), destPath ) );
        return zipPath;
    }

    /**
     * Prepare download headers.
     *
     * @param token
     *         the token
     * @param destPath
     *         the dest path
     *
     * @return the map
     */
    public static Map< String, String > prepareDownloadHeaders( String userUid, String token, String destPath ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTHORIZATION, token );
        requestHeaders.put( ConstantRequestHeader.DEST_PATH, destPath );
        requestHeaders.put( ConstantRequestHeader.USER_UID, userUid );

        return requestHeaders;
    }

    /**
     * It adds headers for required for communication with server.
     *
     * @param headers
     *         the headers
     *
     * @return requestHeaders
     */
    public static Map< String, String > prepareHeaders( RequestHeaders headers ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( "User-Agent", headers.getUserAgent() );
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, headers.getToken() );

        return requestHeaders;
    }

    /**
     * Gets the server files.
     *
     * @param request
     *         the request
     *
     * @return the server files
     */
    public static List< FileInfo > getServerFiles( SusResponseDTO request ) {
        if ( request != null ) {
            String json = JsonUtils.toJson( request.getData() );
            return JsonUtils.jsonToList( json, FileInfo.class );
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * This method generate a list of work flow elements from work flow definition.
     *
     * @param def
     *         the work flow definition data transfer object
     *
     * @return the list
     */
    public static List< UserWFElement > prepareWorkflowElements( WorkflowDefinitionDTO def ) {
        final List< UserWFElement > userElements = new ArrayList<>();
        if ( ( def.getElements() == null ) || CollectionUtil.isEmpty( def.getElements().getNodes() ) ) {
            return userElements;
        }
        for ( final WorkflowElement workflowElement : def.getElements().getNodes() ) {
            WorkflowElement newWorkflowElement = JsonUtils.jsonToObject( JsonUtils.toJson( workflowElement ), WorkflowElement.class );

            final UserWFElement element = newWorkflowElement.getData();
            final UserWFElement wfElement = new UserWFElementImpl();
            wfElement.setId( element.getId() );

            wfElement.setDescription( element.getDescription() );
            wfElement.setComments( element.getComments() );
            wfElement.setKey( element.getKey() );
            wfElement.setName( element.getName() );
            wfElement.setRunMode( element.getRunMode() );
            wfElement.setFields( element.getFields() );
            wfElement.setInfo( element.getInfo() );

            userElements.add( wfElement );
        }
        return userElements;
    }

    /**
     * Gets the fields as JSON object.
     *
     * @param newFields
     *         the new fields
     *
     * @return the fields as JSON object
     */
    public static JSONObject getFieldsAsJSONObject( List< Field< ? > > newFields ) {
        String fieldsJsonString = JsonUtils.toJson( newFields );
        log.fatal( fieldsJsonString );
        var fieldsMap = Collections.singletonMap( "fields", fieldsJsonString );
        try {
            return JsonUtils.toJSONObject( JsonUtils.objectToJson( fieldsMap ) );
        } catch ( ParseException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }

}
