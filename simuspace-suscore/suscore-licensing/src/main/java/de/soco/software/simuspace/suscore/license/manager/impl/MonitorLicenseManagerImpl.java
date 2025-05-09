package de.soco.software.simuspace.suscore.license.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDate;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLicenseType;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.MonitorLicenseDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.model.MonitorLicenseCurveDTO;
import de.soco.software.simuspace.suscore.data.entity.MonitorLicenseEntity;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;
import de.soco.software.simuspace.suscore.license.dao.MonitorLicenseDAO;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.license.manager.MonitorLicenseManager;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.suscore.jsonschema.model.HistoryMap;

/**
 * The Class MonitorLicenseManagerImpl.
 *
 * @author noman arshad
 */
public class MonitorLicenseManagerImpl implements MonitorLicenseManager {

    /**
     * The monitor license DAO.
     */
    private MonitorLicenseDAO monitorLicenseDAO;

    /**
     * The configuration manager.
     */
    private LicenseConfigurationManagerImpl configurationManager;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The token manager.
     */
    private UserTokenManager tokenManager;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Gets the license history UI.
     *
     * @return the license history UI
     */
    @Override
    public List< TableColumn > getLicenseHistoryUI() {
        return GUIUtils.listColumns( MonitorLicenseDTO.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MonitorLicenseEntity addMonitoringHistory( MonitorLicenseEntity monitorLicenseEntity ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return monitorLicenseDAO.saveOrUpdate( entityManager, monitorLicenseEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< MonitorLicenseCurveDTO > getFilteredRecordsBetweenDates( EntityManager entityManager, HistoryMap historyMapObj ) {
        HistoryMap dateRange = new HistoryMap();

        if ( historyMapObj != null && historyMapObj.getDateRange() != null && !historyMapObj.getDateRange().isEmpty() ) {
            dateRange = DateUtils.computeToDateAndFromdate( historyMapObj );
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( new Date() );
            calendar.add( Calendar.DAY_OF_YEAR, -30 );
            dateRange.setToDate( new Date() );
            dateRange.setFromDate( calendar.getTime() );

        }

        // selection of modules from FE is not yet needed so its temprarily OFF.
        // Bypassing SIMuSPACE.DATA license for only 1 license History

        List< String > modulesList = new ArrayList<>();
        modulesList.add( "SIMuSPACE.DATA" );

        List< MonitorLicenseCurveDTO > allList = new ArrayList<>();

        for ( int i = 0; i < modulesList.size(); i++ ) {
            String selectedModule = modulesList.get( i );
            List< String > moduless = new ArrayList<>();
            moduless.add( selectedModule );

            MonitorLicenseCurveDTO curveDTO = prepareMonitorLicenseCurveDTO( entityManager, dateRange, modulesList, moduless );
            allList.add( curveDTO );
        }

        return allList;
    }

    /**
     * Prepare monitor license curve dto monitor license curve dto.
     *
     * @param entityManager
     *         the entity manager
     * @param dateRange
     *         the date range
     * @param modulesList
     *         the modules list
     * @param moduless
     *         the moduless
     *
     * @return the monitor license curve dto
     */
    private MonitorLicenseCurveDTO prepareMonitorLicenseCurveDTO( EntityManager entityManager, HistoryMap dateRange,
            List< String > modulesList, List< String > moduless ) {
        MonitorLicenseCurveDTO curveDTO = new MonitorLicenseCurveDTO();
        try {
            curveDTO.setName( "Usage" );
            curveDTO.setxUnit( "TimeStamp" );
            curveDTO.setyUnit( "Consumed Count" );
            curveDTO.setxDimension( "Time" );
            curveDTO.setyDimension( "Module" );

            List< Object > curve = new ArrayList<>();
            List< MonitorLicenseEntity > list = monitorLicenseDAO.getAllRecordsBetweenDates( entityManager, moduless,
                    dateRange.getFromDate(), dateRange.getToDate() );

            for ( MonitorLicenseEntity currentMonitorLicenseEntity : list ) {
                List< Object > pair = new ArrayList<>( 2 );
                pair.add( currentMonitorLicenseEntity.getCreatedOn().toString() );
                pair.add( getLicenseCount( modulesList, currentMonitorLicenseEntity ) );
                curve.add( pair );
            }

            if ( !curve.isEmpty() ) {
                curveDTO.setCurve( curve );
            } else {
                curveDTO.setCurve( getEmptyHistoryCurve( dateRange ) );
            }

        } catch ( Exception e ) {
            throw new SusException( "Monitor History Curve Failed " + e.getMessage() );
        }
        return curveDTO;
    }

    /**
     * Get empty history curve.
     *
     * @param dateRange
     *         the date Range
     *
     * @return the empty history
     */
    private List< Object > getEmptyHistoryCurve( HistoryMap dateRange ) {
        List< Object > curve = new ArrayList<>( 2 );
        List< Object > fromDate = new ArrayList<>( 2 );
        fromDate.add( new SimpleDateFormat( ConstantsDate.DATE_ONLY_FORMAT ).format( dateRange.getFromDate() ) );
        fromDate.add( 0 );
        curve.add( fromDate );

        List< Object > toDate = new ArrayList<>( 2 );
        toDate.add( new SimpleDateFormat( ConstantsDate.DATE_ONLY_FORMAT ).format( dateRange.getToDate() ) );
        toDate.add( 0 );
        curve.add( toDate );

        return curve;
    }

    /**
     * Gets the license count.
     *
     * @param modulesList
     *         the modules list
     * @param monitorLicenseEntity
     *         the monitor license entity
     *
     * @return the license count
     */
    private int getLicenseCount( List< String > modulesList, MonitorLicenseEntity monitorLicenseEntity ) {
        int count = 0;
        for ( String module : modulesList ) {

            if ( module.equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_DATA ) ) {
                if ( monitorLicenseEntity.getDataLicense() != null ) {
                    count = count + monitorLicenseEntity.getDataLicense();
                }
            } else if ( module.equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER ) ) {
                if ( monitorLicenseEntity.getWorkflowLicense() != null ) {
                    count = count + monitorLicenseEntity.getWorkflowLicense();
                }
            } else if ( module.equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_USER ) ) {
                if ( monitorLicenseEntity.getUserLicense() != null ) {
                    count = count + monitorLicenseEntity.getUserLicense();
                }
            } else if ( module.equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER_ASSEMBLY ) ) {

                if ( monitorLicenseEntity.getAssemblyLicense() != null ) {
                    count = count + monitorLicenseEntity.getAssemblyLicense();
                }
            } else if ( module.equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER_POST ) ) {
                if ( monitorLicenseEntity.getPostLicense() != null ) {
                    count = count + monitorLicenseEntity.getPostLicense();
                }
            } else if ( module.equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_CB2 ) ) {
                if ( monitorLicenseEntity.getCb2License() != null ) {
                    count = count + monitorLicenseEntity.getCb2License();
                }
            }
        }
        return count;
    }

    public void addEmptyEntry() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            MonitorLicenseEntity monitorLicenseEntity = new MonitorLicenseEntity();
            monitorLicenseEntity.setId( java.util.UUID.randomUUID() );
            monitorLicenseEntity.setDescription( "Karaf Start/Stop" );
            monitorLicenseEntity.setCreatedOn( new Date() );
            monitorLicenseDAO.saveOrUpdate( entityManager, monitorLicenseEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Sets the monitor license DAO.
     *
     * @param monitorLicenseDAO
     *         the new monitor license DAO
     */
    public void setMonitorLicenseDAO( MonitorLicenseDAO monitorLicenseDAO ) {
        this.monitorLicenseDAO = monitorLicenseDAO;
    }

    /**
     * Gets the configuration manager.
     *
     * @return the configuration manager
     */
    public LicenseConfigurationManagerImpl getConfigurationManager() {
        return configurationManager;
    }

    /**
     * Sets the configuration manager.
     *
     * @param configurationManager
     *         the new configuration manager
     */
    public void setConfigurationManager( LicenseConfigurationManagerImpl configurationManager ) {
        this.configurationManager = configurationManager;
    }

    /**
     * Gets the license manager.
     *
     * @return the license manager
     */
    public LicenseManager getLicenseManager() {
        return licenseManager;
    }

    /**
     * Sets the license manager.
     *
     * @param licenseManager
     *         the new license manager
     */
    public void setLicenseManager( LicenseManager licenseManager ) {
        this.licenseManager = licenseManager;
    }

    /**
     * Gets the user common manager.
     *
     * @return the user common manager
     */
    public UserCommonManager getUserCommonManager() {
        return userCommonManager;
    }

    /**
     * Sets the user common manager.
     *
     * @param userCommonManager
     *         the new user common manager
     */
    public void setUserCommonManager( UserCommonManager userCommonManager ) {
        this.userCommonManager = userCommonManager;
    }

    /**
     * Gets the token manager.
     *
     * @return the token manager
     */
    public UserTokenManager getTokenManager() {
        return tokenManager;
    }

    /**
     * Sets the token manager.
     *
     * @param tokenManager
     *         the new token manager
     */
    public void setTokenManager( UserTokenManager tokenManager ) {
        this.tokenManager = tokenManager;
    }

    /**
     * Gets the selection manager.
     *
     * @return the selection manager
     */
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    /**
     * Sets the selection manager.
     *
     * @param selectionManager
     *         the new selection manager
     */
    public void setSelectionManager( SelectionManager selectionManager ) {
        this.selectionManager = selectionManager;
    }

    /**
     * Gets the permission manager.
     *
     * @return the permission manager
     */
    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    /**
     * Sets the permission manager.
     *
     * @param permissionManager
     *         the new permission manager
     */
    public void setPermissionManager( PermissionManager permissionManager ) {
        this.permissionManager = permissionManager;
    }

    /**
     * Sets entity manager factory.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}
