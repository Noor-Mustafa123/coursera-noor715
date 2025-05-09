package de.soco.software.simuspace.suscore.object.oilswell.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class OilWellDataDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class OilWellDataDTO {

    /**
     * The wellid.
     */
    String wellid;

    /**
     * The time range.
     */
    OilWellDateRange time_range;

    /**
     * Gets the time range.
     *
     * @return the time range
     */
    public OilWellDateRange getTime_range() {
        return time_range;
    }

    /**
     * Sets the time range.
     *
     * @param time_range
     *         the new time range
     */
    public void setTime_range( OilWellDateRange time_range ) {
        this.time_range = time_range;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "OilWellDataDTO [time_range=" + time_range + "]";
    }

    /**
     * Gets the wellid.
     *
     * @return the wellid
     */
    public String getWellid() {
        return wellid;
    }

    /**
     * Sets the wellid.
     *
     * @param wellid
     *         the new wellid
     */
    public void setWellid( String wellid ) {
        this.wellid = wellid;
    }

}
