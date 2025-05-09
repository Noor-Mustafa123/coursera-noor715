package de.soco.software.simuspace.susdash.pst.manager;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.susdash.pst.model.CoupleDTO;
import de.soco.software.simuspace.susdash.pst.model.PreReqDTO;
import de.soco.software.simuspace.susdash.pst.model.PstDTO;

/**
 * The interface Pst manager.
 */
public interface PstManager {

    /**
     * Gets pst test chart.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the pst test chart
     */
    Map< String, Object > getPstTestJson( String objectId, String token );

    /**
     * Gets pst test archive.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param archive
     *         the archive
     *
     * @return the pst test archive
     */
    Map< String, Object > getPstTestArchive( String objectId, String token, String archive );

    /**
     * Gets pst bench chart.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the pst bench chart
     */
    Map< String, Object > getPstBenchData( String objectId, String token );

    /**
     * Gets pst bench archive.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param archive
     *         the archive
     *
     * @return the pst bench archive
     */
    Map< String, Object > getPstBenchArchive( String objectId, String token, String archive );

    /**
     * Couple test schedule boolean.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param coupleDTO
     *         the couple dto
     *
     * @return the boolean
     */
    Boolean coupleTestSchedule( String objectId, String token, CoupleDTO coupleDTO );

    /**
     * Edit test schedule boolean.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param pstDTO
     *         the pst dto
     *
     * @return the boolean
     */
    Boolean editTestSchedule( String objectId, String token, PstDTO pstDTO );

    /**
     * Add test schedule boolean.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param pstDTO
     *         the pst dto
     *
     * @return the boolean
     */
    Boolean addTestSchedule( String objectId, String token, PstDTO pstDTO );

    /**
     * Edit test schedule ui list.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param pstDTO
     *         the pst dto
     *
     * @return the list
     */
    List< UIFormItem > editTestScheduleUI( String objectId, String token, PstDTO pstDTO );

    /**
     * Add test schedule ui list.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the list
     */
    List< UIFormItem > addTestScheduleUI( String objectId, String token );

    /**
     * Update pre req boolean.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param preReq
     *         the pre req
     *
     * @return the boolean
     */
    Boolean updatePreReq( String objectId, String token, PreReqDTO preReq );

    /**
     * Update dashboard data boolean.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param action
     *         the action
     *
     * @return the boolean
     */
    Boolean updateDashboardData( String objectId, String token, String action );

    /**
     * Create dashboard data boolean.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the boolean
     */
    Boolean createDashboardData( String objectId, String token );

    /**
     * Delete pst planning views boolean.
     *
     * @param viewId
     *         the view id
     *
     * @return the boolean
     */
    boolean deletePstPlanningViews( String viewId );

    /**
     * Gets user pst planning views.
     *
     * @param token
     *         the token
     *
     * @return the user pst planning views
     */
    List< ObjectViewDTO > getUserPstPlanningViews( String token );

    /**
     * Save or update pst planning views object view dto.
     *
     * @param objectJson
     *         the object json
     * @param token
     *         the token
     * @param save
     *         the save
     *
     * @return the object view dto
     */
    ObjectViewDTO saveOrUpdatePstPlanningViews( String objectJson, String token, boolean save );

    /**
     * Gets ace lunge file.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the ace lunge file
     */
    Path getAceLungeFile( String objectId, String token );

    /**
     * Gets test differences file.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the test differences file
     */
    Path getTestDifferencesFile( String objectId, String token );

    /**
     * Create pst planning archive boolean.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param archiveName
     *         the archive name
     *
     * @return the boolean
     */
    boolean createPstPlanningArchive( String objectId, String token, String archiveName );

    /**
     * Gets archives list.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the archives list
     */
    Map< String, Object > getArchivesList( String objectId, String token );

    /**
     * Gets status colors.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the status colors
     */
    Map< String, Object > getLegend( String objectId, String token );

    /**
     * Gets machine.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the machine
     */
    Map< String, Object > getMachine( String objectId, String token );

}
