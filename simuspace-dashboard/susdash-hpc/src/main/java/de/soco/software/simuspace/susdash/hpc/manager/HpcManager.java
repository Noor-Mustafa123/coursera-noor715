package de.soco.software.simuspace.susdash.hpc.manager;

import javax.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.model.BreadCrumbItemDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.susdash.core.model.HpcDashBoardDTO;
import de.soco.software.simuspace.susdash.core.model.HpcJobDTO;
import de.soco.software.simuspace.susdash.core.model.HpcJobFileDTO;
import de.soco.software.simuspace.susdash.core.model.HpcJobPendingMessageDTO;
import de.soco.software.simuspace.susdash.core.model.HpcJobPlotDTO;
import de.soco.software.simuspace.susdash.core.model.HpcJobPropertiesDTO;

/**
 * The Interface HpcManager.
 */
public interface HpcManager {

    /* ************************UGE************************ */

    /**
     * Gets the hpc list.
     *
     * @param userName
     *         the user name from general header
     * @param filter
     *         the filter
     *
     * @return the hpc list
     */

    FilteredResponse< HpcJobDTO > getHpcList( String userName, FiltersDTO filter );

    Boolean killHpcJob( String objectId, String jobId, String userNameFromGeneralHeader );

    /**
     * Gets the live interval.
     *
     * @return the live interval
     */
    Integer getLiveInterval();

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    ObjectViewManager getObjectViewManager();

    /**
     * Gets the list of hpc UI table columns.
     *
     * @param objectId
     *         the object id
     *
     * @return the list of hpc UI table columns
     */
    TableUI getListOfHpcUITableColumns( String objectId );

    /**
     * Gets the hpc bread crumb.
     *
     * @param hpcJobId
     *         the hpc job id
     * @param userName
     *         the user name from general header
     *
     * @return the hpc bread crumb
     */
    List< BreadCrumbItemDTO > getHpcBreadCrumb( String hpcJobId, String userName );

    /**
     * Gets the hpc job subtabs.
     *
     * @param userName
     *         the user name from general header
     * @param jobId
     *         the job id
     *
     * @return the hpc job subtabs
     */
    SubTabsItem getHpcJobSubtabs( String userName, String jobId );

    /* ************************PROPERTIES************************ */

    /**
     * Gets the list of hpc properties UI table columns.
     *
     * @param jobId
     *         the job id
     * @param userName
     *         the user name
     *
     * @return the list of hpc properties UI table columns
     */

    TableUI getListOfHpcPropertiesUITableColumns( String jobId, String userName );

    /**
     * Gets the hpc properties list.
     *
     * @param userName
     *         the user name from general header
     * @param hpcJobId
     *         the hpc job id
     * @param filter
     *         the filter
     *
     * @return the hpc properties list
     */
    FilteredResponse< HpcJobPropertiesDTO > getHpcPropertiesList( String userName, String hpcJobId, FiltersDTO filter );

    /* ************************FILES************************ */

    /**
     * Gets the list of hpc files UI table columns.
     *
     * @return the list of hpc files UI table columns
     */

    TableUI getListOfHpcFilesUITableColumns();

    /**
     * Gets the hpc files by job id.
     *
     * @param userName
     *         the user name
     * @param hpcJobId
     *         the hpc job id
     * @param filter
     *         the filter
     *
     * @return the hpc files by job id
     */
    FilteredResponse< HpcJobFileDTO > getHpcFilesByJobId( String userName, String hpcJobId, FiltersDTO filter );

    /**
     * Gets the files context router.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     * @param userName
     *         the user name
     * @param filter
     *         the filter
     * @param clazz
     *         the clazz
     * @param token
     *         the token
     *
     * @return the files context router
     */
    List< ContextMenuItem > getFilesContextRouter( String objectId, String jobId, String userName, FiltersDTO filter, Class< ? > clazz,
            String token );

    /**
     * Download file.
     *
     * @param jobId
     *         the job id
     * @param fileName
     *         the file name
     * @param token
     *         the token
     *
     * @return the response
     */
    Response downloadFile( String jobId, String fileName, String token );

    /**
     * Tail file.
     *
     * @param jobId
     *         the job id
     * @param name
     *         the name
     * @param filterMap
     *         the filter map
     * @param userName
     *         the user name
     *
     * @return the response
     */
    Object tailFile( String jobId, String name, Map< String, ? > filterMap, String userName );

    /* ************************MONITOR************************ */

    /**
     * Gets the job curve.
     *
     * @param userName
     *         the user name
     * @param jobId
     *         the job id
     *
     * @return the job curve
     */
    HpcJobPlotDTO getJobCurve( String userName, String jobId );

    /* ************************PENDING MESSAGES************************ */

    /**
     * Gets hpc pending messages ui table column.
     *
     * @param jobId
     *         the job id
     * @param userName
     *         the user name
     *
     * @return the hpc pending messages UI table column
     */
    TableUI getHpcPendingMessagesUITableColumn( String jobId, String userName );

    /**
     * Gets the hpc pending messages list.
     *
     * @param userName
     *         the user name from general header
     * @param hpcJobId
     *         the hpc job id
     * @param filter
     *         the filter
     *
     * @return the hpc pending messages list
     */
    FilteredResponse< HpcJobPendingMessageDTO > getHpcPendingMessagesList( String userName, String hpcJobId, FiltersDTO filter );

    /**
     * Prepare object view.
     *
     * @param viewJson
     *         the view json
     * @param key
     *         the key
     * @param save
     *         the save
     *
     * @return the object view DTO
     */
    ObjectViewDTO prepareObjectView( String viewJson, String key, boolean save );

    /**
     * Gets hpc fem zip type by name.
     *
     * @param objectId
     *         the object id
     * @param solver
     *         the solver
     * @param type
     *         the type
     * @param userName
     *         the user name
     * @param hpcDahBoardDTO
     *         the hpc femzip DTO
     *
     * @return the hpc fem zip type by name
     */
    Map< String, String > getHpcFemZipChartBySovlerAndType( String objectId, String solver, String type, String userName,
            HpcDashBoardDTO hpcDahBoardDTO );

    /**
     * Gets hpc ls dyna board.
     *
     * @param objectId
     *         the object id
     * @param board
     *         the board
     * @param userName
     *         the user name
     * @param hpcDahBoardDTO
     *         the hpc femzip dto
     *
     * @return the hpc ls dyna board
     */
    Map< String, String > getHpcBoard( String objectId, String solver, String board, String userName, HpcDashBoardDTO hpcDahBoardDTO );

    List< ContextMenuItem > getUgeJobsContext( String objectId, FiltersDTO filter, String userUid );

}