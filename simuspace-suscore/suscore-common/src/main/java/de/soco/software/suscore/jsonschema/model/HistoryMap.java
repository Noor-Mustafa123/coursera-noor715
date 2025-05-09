package de.soco.software.suscore.jsonschema.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class HistoryMap.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class HistoryMap implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 5670476904858974500L;

    /**
     * The date range.
     */
    private String dateRange;

    /**
     * The modules.
     */
    private List< String > modules;

    /**
     * The to date.
     */
    private Date toDate;

    /**
     * The from date.
     */
    private Date fromDate;

    /**
     * Gets the date range.
     *
     * @return the date range
     */
    public String getDateRange() {
        return dateRange;
    }

    /**
     * Sets the date range.
     *
     * @param dateRange
     *         the new date range
     */
    public void setDateRange( String dateRange ) {
        this.dateRange = dateRange;
    }

    /**
     * Gets the modules.
     *
     * @return the modules
     */
    public List< String > getModules() {
        return modules;
    }

    /**
     * Gets the to date.
     *
     * @return the to date
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * Sets the to date.
     *
     * @param toDate
     *         the new to date
     */
    public void setToDate( Date toDate ) {
        this.toDate = toDate;
    }

    /**
     * Gets the from date.
     *
     * @return the from date
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * Sets the from date.
     *
     * @param fromDate
     *         the new from date
     */
    public void setFromDate( Date fromDate ) {
        this.fromDate = fromDate;
    }

    /**
     * Sets the modules.
     *
     * @param modules
     *         the new modules
     */
    public void setModules( List< String > modules ) {
        this.modules = modules;
    }

}
