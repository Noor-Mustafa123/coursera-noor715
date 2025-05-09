package de.soco.software.simuspace.suscore.license.manager;

import javax.persistence.EntityManager;

import java.util.List;

import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.data.common.model.MonitorLicenseCurveDTO;
import de.soco.software.simuspace.suscore.data.entity.MonitorLicenseEntity;
import de.soco.software.suscore.jsonschema.model.HistoryMap;

/**
 * The Interface MonitorLicenseManager.
 *
 * @author noman arshad
 */
public interface MonitorLicenseManager {

    /**
     * Gets license history ui.
     *
     * @return the license history ui
     */
    List< TableColumn > getLicenseHistoryUI();

    /**
     * Gets filtered records between dates.
     *
     * @param entityManager
     *         the entity manager
     * @param historyMapObj
     *         the history map obj
     *
     * @return the filtered records between dates
     */
    List< MonitorLicenseCurveDTO > getFilteredRecordsBetweenDates( EntityManager entityManager, HistoryMap historyMapObj );

    /**
     * Add monitoring history monitor license entity.
     *
     * @param monitorLicenseEntity
     *         the monitor license entity
     *
     * @return the monitor license entity
     *
     * @apiNote To be used in service calls only
     */
    MonitorLicenseEntity addMonitoringHistory( MonitorLicenseEntity monitorLicenseEntity );

}
