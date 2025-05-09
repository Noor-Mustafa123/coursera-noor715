package de.soco.software.simuspace.susdash.hpc.manager.impl;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.message.Message;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.DashboardConfigEnums;
import de.soco.software.simuspace.suscore.common.enums.DashboardPluginEnums;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.BreadCrumbItemDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.SubTabsUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.DashboardPluginUtil;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.PythonUtils;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.simuspace.susdash.core.constants.HpcJobConstants;
import de.soco.software.simuspace.susdash.core.model.HpcDashBoardDTO;
import de.soco.software.simuspace.susdash.core.model.HpcJobDTO;
import de.soco.software.simuspace.susdash.core.model.HpcJobFileDTO;
import de.soco.software.simuspace.susdash.core.model.HpcJobPendingMessageDTO;
import de.soco.software.simuspace.susdash.core.model.HpcJobPlotDTO;
import de.soco.software.simuspace.susdash.core.model.HpcJobPropertiesDTO;
import de.soco.software.simuspace.susdash.hpc.manager.HpcManager;

/**
 * The Class HpcManagerImpl.
 */
@Log4j2
public class HpcManagerImpl implements HpcManager {

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The config manager.
     */
    private ObjectTypeConfigManager configManager;

    /**
     * Gets the hpc dto list.
     *
     * @param filter
     *         the filter
     * @param userName
     *         the username
     *
     * @return the hpc dto list
     */
    public List< HpcJobDTO > getHpcUgeDTOList( FiltersDTO filter, String userName ) {
        String data;
        prepareJobMonitoringFolderInTemp();
        String jobDataJsonPath = getJobDataPath( userName, null );
        if ( null == filter ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_USER.getKey(), SimuspaceFeaturesEnum.USERS.getKey() ) );
        }
        // replace space with ZERO WIDTH SPACE
        String filtersJson = JsonUtils.objectToJson( filter ).replaceAll( "\\s", "\u200B" );

        List< String > ugeDataCmd = new ArrayList<>(
                Arrays.asList( HpcJobConstants.ACTION, HpcJobConstants.UGE_DATA, HpcJobConstants.JD, jobDataJsonPath,
                        HpcJobConstants.FILTER, filtersJson ) );
        try {
            ProcessResult result = PythonUtils.callHpcJobMonitoringFile( getHpcJobMonitoringScriptPath(), ugeDataCmd, userName );
            data = ConstantsString.EMPTY_STRING.equals( jobDataJsonPath ) ? null
                    : new String( Files.readAllBytes( Paths.get( jobDataJsonPath ) ) );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
        JSONArray jsonArray = new JSONArray( data );
        filter.setTotalRecords( getTotalRecordsFromJson( ( JSONObject ) jsonArray.get( 6 ) ) );
        filter.setFilteredRecords( getFilteredRecordsFromJson( ( JSONObject ) jsonArray.get( 7 ) ) );
        return prepareHpcUgeDTOList( ( JSONObject ) jsonArray.get( 0 ) );
    }

    /**
     * Prepare job monitoring folder in temp.
     */
    private void prepareJobMonitoringFolderInTemp() {
        File hpc_temp = new File( HpcJobConstants.HPC_MONITOR_TEMP_PATH );
        Path hpc_temp_path = hpc_temp.toPath();
        if ( Files.notExists( hpc_temp_path ) ) {
            try {
                Files.createDirectory( hpc_temp_path );
                de.soco.software.simuspace.suscore.common.util.FileUtils.setGlobalAllFilePermissions( hpc_temp_path );
            } catch ( IOException e ) {
                log.error( MessageBundleFactory.getMessage( Messages.FAILED_TO_CREATE_DIRECTORY_AT.getKey(),
                        HpcJobConstants.HPC_MONITOR_TEMP_PATH ) );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< HpcJobDTO > getHpcList( String userName, FiltersDTO filter ) {
        List< HpcJobDTO > hpcJobDTOList = getHpcUgeDTOList( filter, userName );

        return PaginationUtil.constructFilteredResponse( filter, hpcJobDTOList );
    }

    @Override
    public Boolean killHpcJob( String objectId, String jobId, String userName ) {
        // -act Kill_Job -jid <job_id>
        List< String > pythonArgsForKill = new ArrayList<>();
        pythonArgsForKill.add( HpcJobConstants.ACTION );
        pythonArgsForKill.add( HpcJobConstants.KILL_JOB_ACTION );
        pythonArgsForKill.add( HpcJobConstants.JID );
        pythonArgsForKill.add( jobId );
        var result = PythonUtils.callHpcJobMonitoringFile( getHpcJobMonitoringScriptPath(), pythonArgsForKill, userName );
        return result.getExitValue() == 0;
    }

    /**
     * Gets the live interval.
     *
     * @return the live interval
     */
    public Integer getLiveInterval() {
        return DashboardPluginUtil.getLiveInterval( DashboardPluginEnums.HPC.getId(), DashboardConfigEnums.HPC_UGE.getConfig() );
    }

    /**
     * Prepare hpc dto list.
     *
     * @param jsonObject
     *         the json object
     *
     * @return the list
     */
    public static List< HpcJobDTO > prepareHpcUgeDTOList( JSONObject jsonObject ) {
        List< HpcJobDTO > hpcJobDTOList = new ArrayList<>();
        JSONArray value = ( JSONArray ) jsonObject.get( HpcJobConstants.SGE_JOBS );
        Configuration jsonPathConfiguration = Configuration.builder().options( Option.DEFAULT_PATH_LEAF_TO_NULL ).build();
        jsonPathConfiguration.addOptions( Option.SUPPRESS_EXCEPTIONS );

        for ( int i = 0; i < value.length(); i++ ) {
            try {
                HpcJobDTO hpcJobDTO = new HpcJobDTO();
                JSONObject jsonObj = ( JSONObject ) value.get( i );

                String jobNumber = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_NUMBER ) );
                String jobName = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_NAME ) );
                String jobOwner = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_OWNER ) );
                String jobApplication = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_APPLICATION ) );
                String jobProject = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_PROJECT ) );
                String jobSlots = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_SLOTS ) );
                String jobState = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_STATE ) );

                String jobSubmitTime = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_SUBMIT_TIME ) );

                Object jobQueue = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_QUEUE ) );

                String diskspace = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_DISKSPACE ) );

                String jobstarttime = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_START_TIME ) );

                String jobcalcstarttime = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_CALC_START_TIME ) );

                String jobworkingdir = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_WORKING_DIR ) );

                String jobresultdir = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_RESULT_DIR ) );

                String jobapplversion = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_APPL_VERSION ) );

                String jobfsdir = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_FS_DIR ) );

                String jobwfhome = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_WF_HOME ) );

                String jobwfsite = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_WF_SITE ) );

                String jobhost = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_HOST ) );

                String jobhostname = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_HOST_NAME ) );

                String jobparallelenv = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_PARALLEL_ENV ) );

                String jobnodes = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_NODES ) );

                String jobworkflow = JsonPath.using( jsonPathConfiguration ).parse( jsonObj.toString() )
                        .read( JsonPath.compile( HpcJobConstants.JB_WORKFLOW ) );

                if ( StringUtils.isNotBlank( jobNumber ) ) {
                    hpcJobDTO.setId( jobNumber );
                }
                if ( StringUtils.isNotBlank( jobName ) ) {
                    hpcJobDTO.setJobName( jobName );
                }
                if ( StringUtils.isNotBlank( jobOwner ) ) {
                    hpcJobDTO.setUser( jobOwner );
                }
                if ( StringUtils.isNotBlank( jobApplication ) ) {
                    hpcJobDTO.setApplication( jobApplication );
                }
                if ( StringUtils.isNotBlank( jobProject ) ) {
                    hpcJobDTO.setProject( jobProject );
                }
                if ( StringUtils.isNotBlank( jobSlots ) ) {
                    hpcJobDTO.setSlots( jobSlots );
                }
                if ( StringUtils.isNotBlank( jobState ) ) {
                    hpcJobDTO.setState( jobState );
                }
                if ( StringUtils.isNotBlank( jobSubmitTime ) ) {
                    hpcJobDTO.setSubmitTime( DateUtils.convertIso8601ToCustomFormat( jobSubmitTime ) );
                }
                if ( null != jobQueue ) {
                    if ( jobQueue instanceof net.minidev.json.JSONArray ) {
                        String hardReqQueue = jobQueue.toString().replaceAll( "[\\[\\]\"]", "" );
                        hpcJobDTO.setQueue( hardReqQueue );
                    } else {
                        hpcJobDTO.setQueue( jobQueue.toString() );
                    }
                }
                if ( StringUtils.isNotBlank( diskspace ) ) {
                    hpcJobDTO.setDiskSpace( diskspace );
                }

                if ( StringUtils.isNotBlank( jobstarttime ) ) {
                    hpcJobDTO.setJobStartTime( DateUtils.convertIso8601ToCustomFormat( jobstarttime ) );
                }

                if ( StringUtils.isNotBlank( jobcalcstarttime ) ) {
                    hpcJobDTO.setCalcStartTime( DateUtils.convertIso8601ToCustomFormat( jobcalcstarttime ) );
                }

                if ( StringUtils.isNotBlank( jobworkingdir ) ) {
                    hpcJobDTO.setWorkingDirectory( jobworkingdir );
                }

                if ( StringUtils.isNotBlank( jobresultdir ) ) {
                    hpcJobDTO.setResultDirectory( jobresultdir );
                }

                if ( StringUtils.isNotBlank( jobapplversion ) ) {
                    hpcJobDTO.setApplicationVersion( jobapplversion );
                }

                if ( StringUtils.isNotBlank( jobfsdir ) ) {
                    hpcJobDTO.setFSDirectory( jobfsdir );
                }

                if ( StringUtils.isNotBlank( jobwfhome ) ) {
                    hpcJobDTO.setWFHome( jobwfhome );
                }

                if ( StringUtils.isNotBlank( jobwfsite ) ) {
                    hpcJobDTO.setWFSite( jobwfsite );
                }

                if ( StringUtils.isNotBlank( jobhost ) ) {
                    hpcJobDTO.setHost( jobhost );
                }

                if ( StringUtils.isNotBlank( jobhostname ) ) {
                    hpcJobDTO.setHostName( jobhostname );
                }

                if ( StringUtils.isNotBlank( jobparallelenv ) ) {
                    hpcJobDTO.setParalleEnv( jobparallelenv );
                }

                if ( StringUtils.isNotBlank( jobnodes ) ) {
                    hpcJobDTO.setNodes( jobnodes );
                }

                if ( StringUtils.isNotBlank( jobworkflow ) ) {
                    hpcJobDTO.setWorkflow( jobworkflow );
                }

                hpcJobDTOList.add( hpcJobDTO );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
                throw new SusException( e );
            }
        }
        return hpcJobDTOList;
    }

    /**
     * Gets the total records from json.
     *
     * @param object
     *         the object
     *
     * @return the total records from json
     */
    public long getTotalRecordsFromJson( JSONObject object ) {
        String value = object.get( HpcJobConstants.TOTAL_JOBS_TAG ).toString();
        return Long.parseLong( value );
    }

    /**
     * Gets the filtered records from json.
     *
     * @param object
     *         the object
     *
     * @return the filtered records from json
     */
    public long getFilteredRecordsFromJson( JSONObject object ) {
        String value = object.get( HpcJobConstants.FILTERED_JOBS_TAG ).toString();
        return Long.parseLong( value );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI getListOfHpcUITableColumns( String objectId ) {
        final List< TableColumn > columns = GUIUtils.listColumns( HpcJobDTO.class );
        for ( final TableColumn tableColumn : columns ) {
            if ( HpcJobConstants.JOB_NAME_FIELD.equalsIgnoreCase( tableColumn.getName() ) ) {
                tableColumn.getRenderer().setUrl( String.format( "view/dashboard/hpc/%s/uge/job/{id}", objectId ) );
                tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );
            }
        }
        return new TableUI( columns );
    }

    /**
     * Gets the hpc dto from id.
     *
     * @param hpcJobId
     *         the hpc job id
     * @param userName
     *         the user name
     *
     * @return the hpc dto from id
     */
    public HpcJobDTO getHpcUgeDTOFromId( String hpcJobId, String userName ) {
        String data = null;
        Path filePath = Paths.get( getJobDataPath( userName, hpcJobId ) );
        log.debug( "reading files from " + filePath );
        if ( Files.exists( filePath ) ) {
            try {
                FileTime creationTime = Files.readAttributes( filePath, BasicFileAttributes.class ).creationTime();
                long timeDifference = Duration.between( creationTime.toInstant(), Instant.now() ).toMillis();
                log.info( "Time difference in seconds: " + timeDifference + " ms" );
                if ( timeDifference > ConstantsInteger.FILE_CREATING_TIME_DIFFERENCE ) {
                    log.info( "OverWriting existing job data file at --> " + filePath.toString() );
                    ProcessResult energyCurvesProcessResult = createJobDataFile( hpcJobId, userName, filePath );
                    if ( null != energyCurvesProcessResult ) {
                        data = readDataFromFile( filePath );
                    }
                } else {
                    log.info( "Reading existing job data file at --> " + filePath.toString() );
                    data = readDataFromFile( filePath );
                }
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
                throw new SusException( e );
            }
        } else {
            log.debug( "creating job data file at --> " + filePath.toString() );
            ProcessResult energyCurvesCommandResult = createJobDataFile( hpcJobId, userName, filePath );
            if ( null != energyCurvesCommandResult ) {
                data = readDataFromFile( filePath );
            }
        }
        JSONArray jsonArray = new JSONArray( data );
        List< HpcJobDTO > hpcJobDTOList = prepareHpcUgeDTOList( ( JSONObject ) jsonArray.get( ConstantsInteger.INTEGER_VALUE_ZERO ) );
        if ( !hpcJobDTOList.isEmpty() ) {
            return hpcJobDTOList.get( ConstantsInteger.INTEGER_VALUE_ZERO );
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.HPC_JOB_COMPLETED.getKey() ) );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< BreadCrumbItemDTO > getHpcBreadCrumb( String hpcJobId, String userName ) {
        // job breadcrumb
        BreadCrumbItemDTO jobBreadCrumb = new BreadCrumbItemDTO();
        jobBreadCrumb.setName( "Jobs" );
        jobBreadCrumb.setContext( String.format( HpcJobConstants.BREADCRUMB_JOB_CONTEXT_URL, hpcJobId ) );
        jobBreadCrumb.setItemId( UUID.fromString( SimuspaceFeaturesEnum.JOBS.getId() ) );
        // file name breadcrumb
        HpcJobDTO hpcJobDTO = getHpcUgeDTOFromId( hpcJobId, userName );
        BreadCrumbItemDTO fileBreadCrumb = new BreadCrumbItemDTO();
        fileBreadCrumb.setName( hpcJobDTO.getJobName() );
        fileBreadCrumb.setContext( String.format( HpcJobConstants.BREADCRUMB_JOB_NAME_CONTEXT_URL, hpcJobId, hpcJobDTO.getJobName() ) );
        fileBreadCrumb.setItemId( UUID.fromString( hpcJobId ) );
        // breadcrumb list
        List< BreadCrumbItemDTO > breadCrumbItems = new ArrayList<>();
        breadCrumbItems.add( jobBreadCrumb );
        breadCrumbItems.add( fileBreadCrumb );
        return breadCrumbItems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubTabsItem getHpcJobSubtabs( String userName, String jobId ) {
        HpcJobDTO hpcJobDTO = getHpcUgeDTOFromId( jobId, userName );
        boolean isJobOwner = isJobOwner( hpcJobDTO, userName );
        SuSObjectModel model = configManager.getObjectTypeByIdAndConfigName( HpcJobConstants.HPC_MONITOR_CFG_HPC_JOB_ID,
                HpcJobConstants.HPC_MONITOR_CONF );
        List< SubTabsUI > allSubTabsUIs = SubTabsUI.getSubTabsList( model.getViewConfig() );
        List< SubTabsUI > allowedSubTabUIs = new ArrayList<>();
        for ( SubTabsUI subTabUI : allSubTabsUIs ) {
            String subTabName = subTabUI.getName();
            String state = hpcJobDTO.getState();
            if ( isJobOwner ) {
                if ( HpcJobConstants.JOB_STATE_RUNNING.equalsIgnoreCase( state ) ) {
                    if ( subTabName.equalsIgnoreCase( HpcJobConstants.PROPERTIES_SUBTAB ) || subTabName.equalsIgnoreCase(
                            HpcJobConstants.FILES_SUBTAB ) || subTabName.equalsIgnoreCase( HpcJobConstants.MONITOR_SUBTAB ) ) {
                        allowedSubTabUIs.add( subTabUI );
                    }
                } else if ( HpcJobConstants.JOB_STATE_PENDING.equalsIgnoreCase( state ) ) {
                    if ( subTabName.equalsIgnoreCase( HpcJobConstants.PROPERTIES_SUBTAB ) || subTabName.equalsIgnoreCase(
                            HpcJobConstants.PENDING_MESSAGES_SUBTAB ) ) {
                        allowedSubTabUIs.add( subTabUI );
                    }
                }
            } else {
                if ( subTabName.equalsIgnoreCase( HpcJobConstants.PROPERTIES_SUBTAB ) ) {
                    allowedSubTabUIs.add( subTabUI );
                }
            }
        }

        return new SubTabsItem( hpcJobDTO.getId(), hpcJobDTO.getJobName(), 1, allowedSubTabUIs, ConstantsString.EMPTY_STRING );
    }

    /* ************************FILES************************ */

    @Override
    public TableUI getListOfHpcFilesUITableColumns() {
        final List< TableColumn > columns = GUIUtils.listColumns( HpcJobFileDTO.class );
        return new TableUI( columns );
    }

    /**
     * Gets the hpc files dto list from path.
     *
     * @return the hpc files dto list from path
     */
    public List< HpcJobFileDTO > getHpcFilesDtoListFromPath( String userName, String jobId ) {
        String data;
        String filesJsonPath = getFilesDataPath( userName, jobId );
        try {
            data = new String( Files.readAllBytes( Paths.get( filesJsonPath ) ) );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
        JSONArray jsonArray = new JSONArray( data );
        return prepareHpcFileDtoList( jsonArray );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< HpcJobFileDTO > getHpcFilesByJobId( String userName, String hpcJobId, FiltersDTO filter ) {
        String hpcJobBasePath = getHpcJobsBasePath();
        if ( new File( ( hpcJobBasePath + File.separator + hpcJobId ) ).exists() ) {
            List< HpcJobFileDTO > hpcJobFileDTOList = getHpcFileListById( filter, buildPythonArgumentForFiles( hpcJobId, userName, filter ),
                    userName, hpcJobId );
            List< HpcJobFileDTO > hpcJobFileDTOPaginatedList = PaginationUtil.getPaginatedList( hpcJobFileDTOList, filter );
            return PaginationUtil.constructFilteredResponse( filter, hpcJobFileDTOPaginatedList );
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.HPC_JOB_COMPLETED.getKey() ) );
        }
    }

    /**
     * Gets the hpc file list by id.
     *
     * @param filter
     *         the filter
     * @param buildPythonArgumentForFiles
     *         the build python argument for files
     * @param userName
     *         the user name
     *
     * @return the hpc file list by id
     */
    public List< HpcJobFileDTO > getHpcFileListById( FiltersDTO filter, List< String > buildPythonArgumentForFiles, String userName,
            String jobId ) {
        String data;
        String filesJsonPath;
        if ( null == filter ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_USER.getKey(), SimuspaceFeaturesEnum.USERS.getKey() ) );
        }
        // to get files list data
        ProcessResult hpcJobMonitorResult = PythonUtils.callHpcJobMonitoringFile( getHpcJobMonitoringScriptPath(),
                buildPythonArgumentForFiles, userName );
        filesJsonPath = getFilesDataPath( userName, jobId );
        try {
            data = new String( Files.readAllBytes( Paths.get( filesJsonPath ) ) );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
        JSONArray jsonArray = new JSONArray( data );
        filter.setTotalRecords( ( long ) jsonArray.length() );
        filter.setFilteredRecords( ( long ) jsonArray.length() );
        return prepareHpcFileDtoList( jsonArray );
    }

    /**
     * Prepare hpc file dto list.
     *
     * @param jsonArray
     *         the json array
     *
     * @return the list
     */
    public static List< HpcJobFileDTO > prepareHpcFileDtoList( JSONArray jsonArray ) {
        List< HpcJobFileDTO > hpcFilesDTOList = new ArrayList<>();
        for ( int i = 0; i < jsonArray.length(); i++ ) {
            HpcJobFileDTO hpcJobFileDTO = new HpcJobFileDTO();
            JSONObject file = ( JSONObject ) ( ( JSONObject ) jsonArray.get( i ) ).get( "file" );
            hpcJobFileDTO.setId( ( String ) file.get( "id" ) );
            hpcJobFileDTO.setName( ( String ) file.get( "name" ) );
            // convert size from byte to kiloByte
            hpcJobFileDTO.setSize(
                    org.apache.commons.io.FileUtils.byteCountToDisplaySize( Long.parseLong( ( String ) file.get( "size" ) ) ) );
            hpcJobFileDTO.setAttributes( ( String ) file.get( "permission" ) );

            String updatedOnTime = ( String ) file.get( "updated_file_time" );
            hpcJobFileDTO.setUpdatedOn( DateUtils.parseDateTimeString( updatedOnTime ) );

            hpcJobFileDTO.setPath( ( String ) file.get( "path" ) );
            hpcJobFileDTO.setDownloadable( ( String ) file.get( "downloadable" ) );
            hpcFilesDTOList.add( hpcJobFileDTO );
        }
        return hpcFilesDTOList;
    }

    /**
     * Builds the python argument for files.
     *
     * @param hpcJobId
     *         the hpc job id
     *
     * @return the list
     */
    public List< String > buildPythonArgumentForFiles( String hpcJobId, String userName, FiltersDTO filter ) {
        // replace space with ZERO WIDTH SPACE
        String filtersJson = JsonUtils.objectToJson( filter ).replaceAll( "\\s", "\u200B" );
        List< String > command = new ArrayList<>();
        command.add( HpcJobConstants.ACTION );
        command.add( HpcJobConstants.FILES );
        command.add( HpcJobConstants.FD );
        command.add( getFilesDataPath( userName, hpcJobId ) );
        command.add( HpcJobConstants.JID );
        command.add( hpcJobId );
        command.add( HpcJobConstants.SIM );
        command.add( getHpcJobsBasePath() );
        command.add( HpcJobConstants.FILTER );
        command.add( filtersJson );
        return command;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getFilesContextRouter( String objectId, String jobId, String userName, FiltersDTO filter,
            Class< ? > clazz, String token ) {
        List< HpcJobFileDTO > fileDTOs = getHpcFileDtosFromFileIds( filter, userName, jobId );
        List< ContextMenuItem > cml = new ArrayList<>();
        if ( fileDTOs.size() == 1 && fileDTOs.get( 0 ).getDownloadable().equals( "true" ) ) {
            cml.add( getDownloadContextMenu( objectId, jobId, fileDTOs, token ) );
            cml.add( getTailContextMenu( objectId, jobId, fileDTOs ) );
        }
        return cml;
    }

    /**
     * Gets the download context menu.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     * @param fileDTOs
     *         the file DT os
     * @param token
     *         the token
     *
     * @return the download context menu
     */
    public ContextMenuItem getDownloadContextMenu( String objectId, String jobId, List< HpcJobFileDTO > fileDTOs, String token ) {

        ContextMenuItem cmi = new ContextMenuItem();
        String fileName = fileDTOs.get( 0 ).getName();
        String url = String.format( "dashboard/hpc/%s/uge/job/%s/file/%s/download", objectId, jobId, fileName );
        cmi.setUrl( url );
        cmi.setTitle( MessageBundleFactory.getMessage( "4100030x4" ) );
        cmi.setIcon( null );
        cmi.setDivider( false );
        cmi.setVisibility( "both" );
        cmi.setLinkClass( null );
        return cmi;
    }

    /**
     * Gets the tail context menu.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     * @param fileDTOs
     *         the file DT os
     *
     * @return the tail context menu
     */
    public ContextMenuItem getTailContextMenu( String objectId, String jobId, List< HpcJobFileDTO > fileDTOs ) {
        ContextMenuItem cmi = new ContextMenuItem();
        String fileName = fileDTOs.get( 0 ).getName();
        cmi.setUrl( String.format( "dashboard/hpc/%s/uge/job/%s/file/%s/tail", objectId, jobId, fileName ) );
        cmi.setTitle( MessageBundleFactory.getMessage( "4100034x4" ) );
        cmi.setIcon( null );
        cmi.setDivider( false );
        cmi.setVisibility( "both" );
        cmi.setLinkClass( null );
        return cmi;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response downloadFile( String jobId, String fileName, String token ) {
        var user = TokenizedLicenseUtil.getUser( token );
        File file = new File( getHpcFileDtoFromFileName( fileName, user.getUserUid(), jobId ).get( 0 ).getPath() );
        if ( file.exists() ) {
            ResponseBuilder response = Response.ok( file );
            response.header( "Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" );
            response.header( Message.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM );
            response.header( "File-Size", file.length() );
            return response.build();
        } else {
            log.error( "File does not exists" );
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object tailFile( String jobId, String name, Map< String, ? > filterMap, String userName ) {
        int filterMapLines = Integer.parseInt( ( filterMap.get( "lines" ) ).toString() );

        Path sourceTailFilePath = Paths.get(
                getHpcFileDtoFromFileName( name, userName, jobId ).get( ConstantsInteger.INTEGER_VALUE_ZERO ).getPath() );
        Path tempTailFilePath = Paths.get( PropertiesManager.getDefaultServerTempPath(), userName, name );

        try {
            if ( Files.notExists( tempTailFilePath.getParent() ) ) {
                LinuxUtils.createDirectory( userName, tempTailFilePath.getParent().toAbsolutePath().toString() );
            }

            LinuxUtils.copyFileFromSrcPathToDestPath( userName, sourceTailFilePath.toAbsolutePath().toString(),
                    tempTailFilePath.toAbsolutePath().toString() );

            return de.soco.software.simuspace.suscore.common.util.FileUtils.readTail( tempTailFilePath, filterMapLines );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        } finally {
            LinuxUtils.deleteFileOrDirByPath( userName, tempTailFilePath.toAbsolutePath().toString() );
        }
    }

    /**
     * Gets the hpc file dto from file name.
     *
     * @param fileName
     *         the file name
     *
     * @return the hpc file dto from file name
     */
    public List< HpcJobFileDTO > getHpcFileDtoFromFileName( String fileName, String userName, String jobId ) {
        List< HpcJobFileDTO > fileDTOs = new ArrayList<>();
        List< HpcJobFileDTO > hpcJobFileDTOList = getHpcFilesDtoListFromPath( userName, jobId );
        for ( HpcJobFileDTO fileDto : hpcJobFileDTOList ) {
            if ( fileDto.getName().equals( fileName ) ) {
                fileDTOs.add( fileDto );
                break;
            }
        }
        return fileDTOs;
    }

    /**
     * Gets the hpc file dtos from file ids.
     *
     * @param filter
     *         the filter
     *
     * @return the hpc file dtos from file ids
     */
    public List< HpcJobFileDTO > getHpcFileDtosFromFileIds( FiltersDTO filter, String userName, String jobId ) {
        List< HpcJobFileDTO > fileDTOs = new ArrayList<>();
        List< Object > selectedIds = filter.getItems();

        List< HpcJobFileDTO > hpcJobFileDTOList = getHpcFilesDtoListFromPath( userName, jobId );
        for ( Object id : selectedIds ) {
            for ( HpcJobFileDTO fileDto : hpcJobFileDTOList ) {
                if ( fileDto.getId().equals( id ) ) {
                    fileDTOs.add( fileDto );
                    break;
                }
            }
        }
        return fileDTOs;
    }

    /* ************************************************ */

    /* ************************PROPERTIES************************ */

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI getListOfHpcPropertiesUITableColumns( String jobId, String userName ) {
        final List< TableColumn > columns = GUIUtils.listColumns( HpcJobPropertiesDTO.class );
        for ( TableColumn column : columns ) {
            column.setSortable( false );
            column.setFilter( "" );
        }
        return new TableUI( columns );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< HpcJobPropertiesDTO > getHpcPropertiesList( String userName, String hpcJobId, FiltersDTO filter ) {
        HpcJobDTO hpcJobDTO = getHpcUgeDTOFromId( hpcJobId, userName );
        List< HpcJobPropertiesDTO > propertiesDTO = prepareHpcPropertiesDtoFromHpcUgeDTO( hpcJobDTO );
        filter.setFilteredRecords( 1L );
        filter.setTotalRecords( 1L );
        List< HpcJobPropertiesDTO > hpcJobPropertiesDTOPaginatedList = PaginationUtil.getPaginatedList( propertiesDTO, filter );

        if ( !filter.getSearch().isEmpty() ) {
            List< HpcJobPropertiesDTO > searchList = new ArrayList<>();
            for ( HpcJobPropertiesDTO hpcJobPropertiesDTO : propertiesDTO ) {
                if ( StringUtils.containsIgnoreCase( hpcJobPropertiesDTO.getKey(), filter.getSearch() ) || StringUtils.containsIgnoreCase(
                        hpcJobPropertiesDTO.getValue(), filter.getSearch() ) ) {
                    searchList.add( hpcJobPropertiesDTO );
                }
            }
            return PaginationUtil.constructFilteredResponse( filter, searchList );
        }
        return PaginationUtil.constructFilteredResponse( filter, hpcJobPropertiesDTOPaginatedList );
    }

    /**
     * Prepare hpc properties dto from hpc dto.
     *
     * @param hpcJobDTO
     *         the hpc dto
     *
     * @return the list
     */
    private List< HpcJobPropertiesDTO > prepareHpcPropertiesDtoFromHpcUgeDTO( HpcJobDTO hpcJobDTO ) {
        String[] keys = { "Id", "Host", "Submit Host", "Job Name", "User", "Application", "Applicationn Version", "Project", "Workflow",
                "Workflow Home", "Workflow Site", "Slots", "Queue", "State", "Job Submit Time", "Job Start Time", "Calculation Start Time",
                "Working Directory", "Result Directory", "FS Directory", "Disk Space", "Parallel Environment", "Nodes" };
        HpcJobPropertiesDTO[] propertieDTOs = new HpcJobPropertiesDTO[ keys.length ];

        String[] values = { hpcJobDTO.getId(), hpcJobDTO.getHost(), hpcJobDTO.getHostName(), hpcJobDTO.getJobName(), hpcJobDTO.getUser(),
                hpcJobDTO.getApplication(), hpcJobDTO.getApplicationVersion(), hpcJobDTO.getProject(), hpcJobDTO.getWorkflow(),
                hpcJobDTO.getWFHome(), hpcJobDTO.getWFSite(), hpcJobDTO.getSlots(), hpcJobDTO.getQueue(), hpcJobDTO.getState(),
                DateUtils.convertLongDateFormatToString( hpcJobDTO.getSubmitTime() ),
                ( hpcJobDTO.getJobStartTime() != null ? DateUtils.convertLongDateFormatToString( hpcJobDTO.getJobStartTime() ) : null ),
                ( hpcJobDTO.getCalcStartTime() != null ? DateUtils.convertLongDateFormatToString( hpcJobDTO.getCalcStartTime() ) : null ),
                hpcJobDTO.getWorkingDirectory(), hpcJobDTO.getResultDirectory(), hpcJobDTO.getFSDirectory(), hpcJobDTO.getDiskSpace(),
                hpcJobDTO.getParalleEnv(), hpcJobDTO.getNodes() };

        for ( int i = 0; i < keys.length; i++ ) {
            propertieDTOs[ i ] = new HpcJobPropertiesDTO( keys[ i ], values[ i ] );
        }
        return Arrays.asList( propertieDTOs );
    }

    /* ************************************************ */

    /* ************************MONITOR************************ */

    /**
     * {@inheritDoc}
     */
    @Override
    public HpcJobPlotDTO getJobCurve( String userName, String jobId ) {
        HpcJobPlotDTO hpcJobPlotDto = new HpcJobPlotDTO();
        try {
            List< String > ugeMonitorCmd = new ArrayList<>(
                    Arrays.asList( HpcJobConstants.ACTION, HpcJobConstants.MONITOR, HpcJobConstants.EP,
                            getEnergyCurvePath( userName, jobId ), HpcJobConstants.JID, jobId, HpcJobConstants.SIM,
                            getHpcJobsBasePath() ) );
            ProcessResult result = PythonUtils.callHpcJobMonitoringFile( getHpcJobMonitoringScriptPath(), ugeMonitorCmd, userName );

            if ( null != result ) {
                List< String > errorList = getLoggerOutputFromCommandResult( result ).get( HpcJobConstants.ERROR );
                if ( !errorList.isEmpty() ) {
                    throw new SusException( errorList.toString() );
                }
            }
            File plotFile = new File( getEnergyCurvePath( userName, jobId ) );

            if ( Files.exists( plotFile.toPath() ) ) {
                try ( FileInputStream plotFileStream = new FileInputStream( plotFile ) ) {
                    hpcJobPlotDto = JsonUtils.jsonToObject( plotFileStream, HpcJobPlotDTO.class );
                }
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.HPC_JOB_COMPLETED.getKey(), plotFile.toPath() ) );
            }

        } catch ( Exception e ) {
            throw new SusException( e.getMessage() );
        }
        return hpcJobPlotDto;
    }

    /* ************************************************ */

    /* ************************PENDING MESSAGES************************ */

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI getHpcPendingMessagesUITableColumn( String jobId, String userName ) {
        final List< TableColumn > columns = GUIUtils.listColumns( HpcJobPendingMessageDTO.class );
        return new TableUI( columns );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< HpcJobPendingMessageDTO > getHpcPendingMessagesList( String userName, String hpcJobId, FiltersDTO filter ) {
        String data;

        // replace space with ZERO WIDTH SPACE
        String filtersJson = JsonUtils.objectToJson( filter ).replaceAll( "\\s", "\u200B" );

        try {
            String pendingDataJsonPath = getPendingDataPath( userName, hpcJobId );
            List< String > ugePendingMessageCmd = new ArrayList<>(
                    Arrays.asList( HpcJobConstants.ACTION, HpcJobConstants.PENDING, HpcJobConstants.PD, pendingDataJsonPath,
                            HpcJobConstants.JID, hpcJobId, HpcJobConstants.FILTER, filtersJson ) );

            ProcessResult result = PythonUtils.callHpcJobMonitoringFile( getHpcJobMonitoringScriptPath(), ugePendingMessageCmd, userName );
            Path pendingDataPath = Paths.get( pendingDataJsonPath );
            if ( Files.exists( pendingDataPath ) ) {
                data = new String( Files.readAllBytes( pendingDataPath ) );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.HPC_JOB_COMPLETED.getKey() ) );
            }
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }

        Map< String, List< String > > pendingMessagesMap = ( Map< String, List< String > > ) JsonUtils.jsonToMap( data, new HashMap<>() );
        List< String > allPendingMessages = pendingMessagesMap.get( HpcJobConstants.PENDING_MESSAGES );
        List< String > allPendingMessagesPaginatedList = PaginationUtil.getPaginatedList( allPendingMessages, filter );

        List< HpcJobPendingMessageDTO > pendingMessageDTO = new ArrayList<>();
        for ( String message : allPendingMessagesPaginatedList ) {
            pendingMessageDTO.add( new HpcJobPendingMessageDTO( message ) );
        }

        filter.setFilteredRecords( ( long ) allPendingMessages.size() );
        filter.setTotalRecords( ( long ) allPendingMessages.size() );

        return PaginationUtil.constructFilteredResponse( filter, pendingMessageDTO );
    }

    /* ************************************************ */

    /* ************************MISC.************************ */

    /**
     * Checks if is job owner.
     *
     * @param hpcJobDTO
     *         the hpc dto
     * @param userName
     *         the user name
     *
     * @return the boolean
     */
    private Boolean isJobOwner( HpcJobDTO hpcJobDTO, String userName ) {
        if ( StringUtils.isNotBlank( userName ) ) {
            return hpcJobDTO.getUser().equalsIgnoreCase( userName );
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO prepareObjectView( String viewJson, String key, boolean save ) {
        ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
        if ( save && !objectViewDTO.isDefaultView() ) {
            objectViewDTO.setId( null );
        }
        objectViewDTO.setObjectViewKey( key );
        objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
        objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
        return objectViewDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map< String, String > getHpcFemZipChartBySovlerAndType( String objectId, String solver, String type, String userName,
            HpcDashBoardDTO hpcDahBoardDTO ) {
        try {
            Path outputFilePath = prepareOutputFileInTempForUser( objectId, userName, solver, type );

            String scriptPath = DashboardPluginUtil.getScriptPath( DashboardConfigEnums.HPC_FEMZIP.getPluginId(),
                    DashboardConfigEnums.HPC_FEMZIP.getConfig(), "hpc_femzip_statistics" );
            String dateRangeFlag = hpcDahBoardDTO.getDateRange().getStart() + " -to " + hpcDahBoardDTO.getDateRange().getEnd();
            var result = PythonUtils.callHpcStatsFile( scriptPath, userName, solver, type, dateRangeFlag,
                    outputFilePath.toAbsolutePath().toString() );
            if ( result.getExitValue() == 0 ) {
                return readContentFromFile( outputFilePath, solver, type );
            } else {
                log.error( MessageBundleFactory.getDefaultMessage( Messages.FEMZIP_CHART_DATA_NOT_GENERATED.getKey(), solver, type ) );
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.FEMZIP_CHART_DATA_NOT_GENERATED.getKey(), solver, type ) );
            }
        } catch ( Exception e ) {
            log.error( MessageBundleFactory.getDefaultMessage( Messages.FEMZIP_CHART_DATA_NOT_GENERATED.getKey(), solver, type ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FEMZIP_CHART_DATA_NOT_GENERATED.getKey(), solver, type ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map< String, String > getHpcBoard( String objectId, String solver, String board, String userName,
            HpcDashBoardDTO hpcDahBoardDTO ) {
        if ( solver.equals( "abaqus" ) ) {
            return prepareAbaqusBoard( objectId, solver, board, userName, hpcDahBoardDTO );
        } else if ( solver.equals( "lsdyna" ) ) {
            return prepareLsDynaBoard( objectId, solver, board, userName, hpcDahBoardDTO );
        } else {
            return new HashMap<>();
        }
    }

    @Override
    public List< ContextMenuItem > getUgeJobsContext( String objectId, FiltersDTO filter, String userUid ) {
        if ( filter.getItems().size() > 1 ) {
            return new ArrayList<>();
        }
        var selectedJob = getHpcUgeDTOFromId( ( String ) filter.getItems().get( ConstantsInteger.INTEGER_VALUE_ZERO ), userUid );
        if ( !StringUtils.equals( selectedJob.getUser(), ( userUid ) ) ) {
            return new ArrayList<>();
        }
        List< ContextMenuItem > cml = new ArrayList<>();
        ContextMenuItem killItem = new ContextMenuItem();
        killItem.setTitle( "Kill" );
        killItem.setVisibility( "web" );
        killItem.setUrl( String.format( "dashboard/hpc/%s/uge/job/%s/kill", objectId, selectedJob.getId() ) );

        cml.add( killItem );
        return cml;
    }

    /**
     * Prepare ls dyna board map.
     *
     * @param objectId
     *         the object id
     * @param solver
     *         the solver
     * @param board
     *         the board
     * @param userName
     *         the user name
     * @param hpcDahBoardDTO
     *         the hpc dah board dto
     *
     * @return the map
     */
    private Map< String, String > prepareLsDynaBoard( String objectId, String solver, String board, String userName,
            HpcDashBoardDTO hpcDahBoardDTO ) {
        try {
            Path outputFilePath = prepareOutputFileInTempForUser( objectId, userName, solver, board );
            String scriptPath = DashboardPluginUtil.getScriptPath( DashboardConfigEnums.HPC_LSDYNA_STATISTICS.getPluginId(),
                    DashboardConfigEnums.HPC_LSDYNA_STATISTICS.getConfig(), "lsdyna_statistics" );
            String dateRangeFlag = hpcDahBoardDTO.getDateRange().getStart() + " -to " + hpcDahBoardDTO.getDateRange().getEnd();
            var result = PythonUtils.callHpcStatsFile( scriptPath, userName, solver, board, dateRangeFlag,
                    outputFilePath.toAbsolutePath().toString() );
            if ( result.getExitValue() == ConstantsInteger.INTEGER_VALUE_ZERO ) {
                return readContentFromFile( outputFilePath, solver, board );
            } else {
                log.error( MessageBundleFactory.getDefaultMessage( Messages.CHART_DATA_NOT_GENERATED.getKey(), "LS-DYNA", board ) );
                throw new SusException( MessageBundleFactory.getMessage( Messages.CHART_DATA_NOT_GENERATED.getKey(), "LS-DYNA", board ) );
            }
        } catch ( Exception e ) {
            log.error( MessageBundleFactory.getDefaultMessage( Messages.CHART_DATA_NOT_GENERATED.getKey(), "LS-DYNA", board ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.CHART_DATA_NOT_GENERATED.getKey(), "LS-DYNA", board ) );
        }
    }

    /**
     * Prepare abaqus board map.
     *
     * @param objectId
     *         the object id
     * @param solver
     *         the solver
     * @param board
     *         the board
     * @param userName
     *         the user name
     * @param hpcDahBoardDTO
     *         the hpc dah board dto
     *
     * @return the map
     */
    private Map< String, String > prepareAbaqusBoard( String objectId, String solver, String board, String userName,
            HpcDashBoardDTO hpcDahBoardDTO ) {
        try {
            Path outputFilePath = prepareOutputFileInTempForUser( objectId, userName, solver, board );
            String scriptPath = DashboardPluginUtil.getScriptPath( DashboardConfigEnums.HPC_ABAQUS_STATISTICS.getPluginId(),
                    DashboardConfigEnums.HPC_ABAQUS_STATISTICS.getConfig(), "abaqus_statistics" );
            String dateRangeFlag = hpcDahBoardDTO.getDateRange().getStart() + " -to " + hpcDahBoardDTO.getDateRange().getEnd();
            var result = PythonUtils.callHpcStatsFile( scriptPath, userName, solver, board, dateRangeFlag,
                    outputFilePath.toAbsolutePath().toString() );
            if ( result.getExitValue() == ConstantsInteger.INTEGER_VALUE_ZERO ) {
                return readContentFromFile( outputFilePath, solver, board );
            } else {
                log.error( MessageBundleFactory.getDefaultMessage( Messages.CHART_DATA_NOT_GENERATED.getKey(), "Abaqus", board ) );
                throw new SusException( MessageBundleFactory.getMessage( Messages.CHART_DATA_NOT_GENERATED.getKey(), "Abaqus", board ) );
            }
        } catch ( Exception e ) {
            log.error( MessageBundleFactory.getDefaultMessage( Messages.CHART_DATA_NOT_GENERATED.getKey(), "Abaqus", board ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.CHART_DATA_NOT_GENERATED.getKey(), "Abaqus", board ) );
        }
    }

    /**
     * Prepare femzip stats folder in temp for user string.
     *
     * @param objectId
     *         the object id
     * @param userName
     *         the user name
     * @param solver
     *         the solver
     * @param chartType
     *         the chart type
     *
     * @return the string
     *
     * @throws IOException
     *         the io exception
     */
    private Path prepareOutputFileInTempForUser( String objectId, String userName, String solver, String chartType ) throws IOException {
        Path dashFolder = Path.of( PropertiesManager.getDefaultServerTempPath() + File.separator + objectId );
        if ( Files.notExists( dashFolder ) ) {
            Files.createDirectory( dashFolder );
            de.soco.software.simuspace.suscore.common.util.FileUtils.setGlobalAllFilePermissions( dashFolder );
        }
        Path userFolderPath = Path.of( dashFolder.toAbsolutePath() + File.separator + userName );
        if ( Files.notExists( userFolderPath ) ) {
            Files.createDirectory( userFolderPath );
            de.soco.software.simuspace.suscore.common.util.FileUtils.setGlobalAllFilePermissions( userFolderPath );
        }
        Path outputFilePath = Path.of( userFolderPath.toAbsolutePath() + File.separator + solver + chartType );
        if ( Files.exists( outputFilePath ) ) {
            Files.deleteIfExists( outputFilePath );
        }
        Files.createFile( outputFilePath );
        de.soco.software.simuspace.suscore.common.util.FileUtils.setGlobalAllFilePermissions( outputFilePath );
        return outputFilePath;
    }

    /**
     * Read content from file map.
     *
     * @param outputFile
     *         the output file
     * @param solver
     *         the solver
     * @param board
     *         the board
     *
     * @return the map
     *
     * @throws IOException
     *         the io exception
     */
    private static Map< String, String > readContentFromFile( Path outputFile, String solver, String board ) throws IOException {
        Map< String, String > fileContent;
        if ( Files.exists( outputFile ) ) {
            try ( InputStream is = new FileInputStream( outputFile.toFile() ) ) {
                fileContent = JsonUtils.jsonToObject( is, Map.class );
            }
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.CHART_DATA_NOT_GENERATED.getKey(), solver, board ) );
        }
        return fileContent;
    }

    /**
     * Gets the job data path.
     *
     * @param userName
     *         the user name
     * @param hpcJobId
     *         the hpc job id
     *
     * @return the job data path
     */
    private String getJobDataPath( String userName, String hpcJobId ) {
        if ( null != hpcJobId ) {
            return HpcJobConstants.HPC_MONITOR_TEMP_PATH + File.separator + userName + File.separator + hpcJobId + File.separator
                    + HpcJobConstants.JOB_DATA_FILE_NAME;
        }
        return HpcJobConstants.HPC_MONITOR_TEMP_PATH + File.separator + userName + File.separator + HpcJobConstants.JOB_DATA_FILE_NAME;
    }

    /**
     * Gets the files data path.
     *
     * @param userName
     *         the user name
     * @param jobId
     *         the job id
     *
     * @return the files data path
     */
    private String getFilesDataPath( String userName, String jobId ) {
        return HpcJobConstants.HPC_MONITOR_TEMP_PATH + File.separator + userName + File.separator + jobId + File.separator
                + HpcJobConstants.FILES_DATA_FILE_NAME;
    }

    /**
     * Gets the pending data path.
     *
     * @param userName
     *         the user name
     * @param jobId
     *         the job id
     *
     * @return the pending data path
     */
    private String getPendingDataPath( String userName, String jobId ) {
        return HpcJobConstants.HPC_MONITOR_TEMP_PATH + File.separator + userName + File.separator + jobId + File.separator
                + HpcJobConstants.PENDING_DATA_FILE_NAME;
    }

    /**
     * Gets the energy curve path.
     *
     * @param userName
     *         the user name
     * @param jobId
     *         the job id
     *
     * @return the energy curve path
     */
    private String getEnergyCurvePath( String userName, String jobId ) {
        return HpcJobConstants.HPC_MONITOR_TEMP_PATH + File.separator + userName + File.separator + jobId + File.separator
                + HpcJobConstants.ENERGY_CURVE_FILE_NAME;
    }

    /**
     * Gets the hpc python script path.
     *
     * @return the hpc python script path
     */
    private String getHpcJobMonitoringScriptPath() {
        return DashboardPluginUtil.getScriptPath( DashboardPluginEnums.HPC.getId(), DashboardConfigEnums.HPC_UGE.getConfig(),
                HpcJobConstants.HPC_JOB_MONITORING_KEY );
    }

    /**
     * Gets the hpc jobs base path.
     *
     * @return the hpc jobs base path
     */
    private String getHpcJobsBasePath() {
        return DashboardPluginUtil.getScriptPath( DashboardPluginEnums.HPC.getId(), DashboardConfigEnums.HPC_UGE.getConfig(),
                HpcJobConstants.HPC_JOB_BASE_KEY );

    }

    /**
     * Gets the log output from command result.
     *
     * @param processResult
     *         the command result
     *
     * @return the log output from command result
     */
    private Map< String, List< String > > getLoggerOutputFromCommandResult( ProcessResult processResult ) {
        String[] outputString = processResult.getOutputString().split( "\n" );
        Map< String, List< String > > logMap = new HashMap<>();

        List< String > infoList = new ArrayList<>();
        List< String > criticalList = new ArrayList<>();
        List< String > errorList = new ArrayList<>();

        for ( String line : outputString ) {
            if ( line.contains( HpcJobConstants.INFO ) ) {
                Pattern infoRegexPattern = Pattern.compile( HpcJobConstants.PYTHON_LOGGER_INFO_REGEX );
                Matcher infoMatcher = infoRegexPattern.matcher( line );
                infoMatcher.find();
                infoList.add( infoMatcher.group( ConstantsInteger.INTEGER_VALUE_THREE ).trim() );
            } else if ( line.contains( HpcJobConstants.CRITICAL ) ) {
                Pattern criticalRegexPattern = Pattern.compile( HpcJobConstants.PYTHON_LOGGER_CRITICAL_REGEX );
                Matcher criticalMatcher = criticalRegexPattern.matcher( line );
                criticalMatcher.find();
                criticalList.add( criticalMatcher.group( ConstantsInteger.INTEGER_VALUE_THREE ).trim() );
            } else if ( line.contains( HpcJobConstants.ERROR ) ) {
                Pattern errorRegexPattern = Pattern.compile( HpcJobConstants.PYTHON_LOGGER_ERROR_REGEX );
                Matcher errorMatcher = errorRegexPattern.matcher( line );
                errorMatcher.find();
                errorList.add( errorMatcher.group( ConstantsInteger.INTEGER_VALUE_THREE ).trim() );
            }
        }
        logMap.put( HpcJobConstants.INFO, infoList );
        logMap.put( HpcJobConstants.CRITICAL, criticalList );
        logMap.put( HpcJobConstants.ERROR, errorList );
        return logMap;
    }

    /**
     * Creates the job data file.
     *
     * @param hpcJobId
     *         the hpc job id
     * @param userName
     *         the user name
     * @param filePath
     *         the file path
     *
     * @return the command result
     */
    private ProcessResult createJobDataFile( String hpcJobId, String userName, Path filePath ) {
        List< String > cmd = Arrays.asList( HpcJobConstants.ACTION, HpcJobConstants.UGE_DATA, HpcJobConstants.JD, filePath.toString(),
                HpcJobConstants.JID, hpcJobId );
        return PythonUtils.callHpcJobMonitoringFile( getHpcJobMonitoringScriptPath(), cmd, userName );
    }

    /**
     * Read data from file.
     *
     * @param filePath
     *         the file path
     *
     * @return the string
     */
    private String readDataFromFile( Path filePath ) {
        try {
            return new String( Files.readAllBytes( filePath ) );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewManager getObjectViewManager() {
        return objectViewManager;
    }

    /**
     * Sets the object view manager.
     *
     * @param objectViewManager
     *         the new object view manager
     */
    public void setObjectViewManager( ObjectViewManager objectViewManager ) {
        this.objectViewManager = objectViewManager;
    }

    /**
     * Sets the config manager.
     *
     * @param configManager
     *         the configManager to set
     */
    public void setConfigManager( ObjectTypeConfigManager configManager ) {
        this.configManager = configManager;
    }

}