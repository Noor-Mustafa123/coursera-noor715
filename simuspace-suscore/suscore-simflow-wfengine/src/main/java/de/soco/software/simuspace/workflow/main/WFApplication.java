package de.soco.software.simuspace.workflow.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.model.FileObject;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.workflow.activator.WfEngineActivate;
import de.soco.software.simuspace.workflow.model.impl.JobParametersImpl;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.processing.impl.WorkflowExecutionManagerImpl;
import de.soco.software.simuspace.workflow.properties.EnginePropertiesManager;

/**
 * The Class WFApplication.
 *
 * @author Huzaifah
 */
public class WFApplication {

    /**
     * The main method.
     *
     * @param args
     *         the arguments
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public static void main( String[] args ) throws IOException {
        // This updateLog4jLogFilePath method should always stay on top:log4j properties being updated
        updateLog4jLogFilePath();

        if ( args.length == ConstantsInteger.INTEGER_VALUE_ONE ) {
            String userHome = new WorkflowExecutionManagerImpl().getUserHome();
            File file = new File( args[ ConstantsInteger.INTEGER_VALUE_ZERO ] );

            try ( FileOutputStream f = new FileOutputStream( file ); ObjectOutputStream o = new ObjectOutputStream( f ) ) {
                o.writeObject( userHome );
            }
        } else {
            if ( args[ ConstantsInteger.INTEGER_VALUE_ZERO ].contains( "wf" ) ) {
                final String wfFilePath = args[ 1 ];
                JobParametersImpl jsonPram = JsonUtils.jsonToObject( new String( Files.readAllBytes( Paths.get( wfFilePath ) ) ),
                        JobParametersImpl.class );
                getServerProperties( jsonPram );
                new WorkflowExecutionManagerImpl().executeWorkflow( jsonPram );
            } else {
                String path = args[ ConstantsInteger.INTEGER_VALUE_ZERO ].contains( ConstantsString.SPACE_ALTERNATE )
                        ? args[ ConstantsInteger.INTEGER_VALUE_ZERO ].replace( ConstantsString.SPACE_ALTERNATE, ConstantsString.SPACE )
                        : args[ ConstantsInteger.INTEGER_VALUE_ZERO ];
                String show = args[ ConstantsInteger.INTEGER_VALUE_ONE ];
                String tempPath = args[ ConstantsInteger.INTEGER_VALUE_TWO ];
                try {
                    List< FileObject > list = new WorkflowExecutionManagerImpl().getFileList( path, show );
                    writeToFile( JsonUtils.convertListToJson( list ), tempPath );
                } catch ( Exception e ) {
                    writeToFile( e.toString(), tempPath );
                }
            }
        }
    }

    /**
     * Gets server properties.
     *
     * @param jsonPram
     *         the json pram
     */
    private static void getServerProperties( JobParametersImpl jsonPram ) {
        String url = jsonPram.getServer().getProtocol() + jsonPram.getServer().getHostname() + ConstantsString.COLON
                + jsonPram.getServer().getPort() + "/api/properties";
        SusResponseDTO response = SuSClient.getRequest( url, prepareHeaders( jsonPram.getRequestHeaders() ) );
        Map< String, Object > map = new HashMap<>();
        if ( null != response.getData() ) {
            EnginePropertiesManager
                    .setProps( ( Map< String, Object > ) JsonUtils.jsonToMap( JsonUtils.objectToJson( response.getData() ), map ) );
        }
    }

    /**
     * Prepare headers map.
     *
     * @param headers
     *         the headers
     *
     * @return the map
     */
    protected static Map< String, String > prepareHeaders( RequestHeaders headers ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.JOB_TOKEN, headers.getJobAuthToken() );
        return requestHeaders;
    }

    /**
     * Update log 4 j log file path.
     */
    private static void updateLog4jLogFilePath() {
        try {
            WfEngineActivate activeLog = new WfEngineActivate();
            activeLog.configure();
        } catch ( Exception e ) {
            System.err.println( "error updating log4j path : WfApplication main " + e.getMessage() );
        }
    }

    /**
     * Write to file.
     *
     * @param content
     *         the content
     * @param tempPath
     *         the temp path
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private static void writeToFile( String content, String tempPath ) throws IOException {
        try ( BufferedWriter writer = Files.newBufferedWriter( Paths.get( tempPath ), StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                StandardOpenOption.WRITE ) ) {
            writer.write( content );
        }
    }

}
