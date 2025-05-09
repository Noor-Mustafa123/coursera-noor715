package de.soco.software.simuspace.suscore.license.dao;

import javax.persistence.EntityManager;

import java.util.Date;
import java.util.List;

import de.soco.software.simuspace.suscore.data.entity.MonitorLicenseEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface MonitorLicenseDAO.
 *
 * @author noman arshad
 */
public interface MonitorLicenseDAO extends GenericDAO< MonitorLicenseEntity > {

    /**
     * Gets all records between dates.
     *
     * @param entityManager
     *         the entity manager
     * @param modulesList
     *         the modules list
     * @param dateFrom
     *         the date from
     * @param dateTo
     *         the date to
     *
     * @return the all records between dates
     */
    List< MonitorLicenseEntity > getAllRecordsBetweenDates( EntityManager entityManager, List< String > modulesList, Date dateFrom,
            Date dateTo );

}
