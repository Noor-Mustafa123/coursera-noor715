package de.soco.software.simuspace.suscore.license.dao.impl;

import javax.persistence.EntityManager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.constants.ConstantsLicenseType;
import de.soco.software.simuspace.suscore.data.entity.MonitorLicenseEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.license.dao.MonitorLicenseDAO;

/**
 * The Class MonitorLicenseDAOImpl.
 *
 * @author noman arshad
 */
public class MonitorLicenseDAOImpl extends AbstractGenericDAO< MonitorLicenseEntity > implements MonitorLicenseDAO {

    @Override
    public List< MonitorLicenseEntity > getAllRecordsBetweenDates( EntityManager entityManager, List< String > modulesList, Date dateFrom,
            Date dateTo ) {

        Map< String, Object > params = new HashMap<>();

        for ( String module : modulesList ) {

            if ( module.equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_DATA ) ) {
                params.put( "dataLicense", null );
            } else if ( module.equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER ) ) {
                params.put( "workflowLicense", null );
            } else if ( module.equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_USER ) ) {
                params.put( "userLicense", null );
            } else if ( module.equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER_ASSEMBLY ) ) {
                params.put( "assemblyLicense", null );
            } else if ( module.equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER_POST ) ) {
                params.put( "postLicense", null );
            } else if ( module.equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_CB2 ) ) {
                params.put( "cb2License", null );
            } else if ( module.equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_DOE ) ) {
                params.put( "doeLicense", null );
            } else if ( module.equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_OPTIMIZATION ) ) {
                params.put( "optimizationLicense", null );
            }
        }
        return getAllRecordsBetweenDates( entityManager, MonitorLicenseEntity.class, params, dateFrom, dateTo );
    }

}
