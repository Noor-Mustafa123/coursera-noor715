package de.soco.software.simuspace.suscore.object.oilswell.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * The Class PredictionPayloadDTO.
 */
@JsonInclude( JsonInclude.Include.NON_NULL )
public class PredictionPayloadDTO {

    /**
     * The wellid.
     */
    private String wellid;

    /**
     * The tbgpres.
     */
    private String tbgpres;

    /**
     * The csgpres.
     */
    private String csgpres;

    /**
     * The gasrate.
     */
    private String gasrate;

    /**
     * The datetime.
     */
    private String datetime;

    /**
     * The currentstate.
     */
    private String currentstate;

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

    /**
     * Gets the tbgpres.
     *
     * @return the tbgpres
     */
    public String getTbgpres() {
        return tbgpres;
    }

    /**
     * Sets the tbgpres.
     *
     * @param tbgpres
     *         the new tbgpres
     */
    public void setTbgpres( String tbgpres ) {
        this.tbgpres = tbgpres;
    }

    /**
     * Gets the csgpres.
     *
     * @return the csgpres
     */
    public String getCsgpres() {
        return csgpres;
    }

    /**
     * Sets the csgpres.
     *
     * @param csgpres
     *         the new csgpres
     */
    public void setCsgpres( String csgpres ) {
        this.csgpres = csgpres;
    }

    /**
     * Gets the gasrate.
     *
     * @return the gasrate
     */
    public String getGasrate() {
        return gasrate;
    }

    /**
     * Sets the gasrate.
     *
     * @param gasrate
     *         the new gasrate
     */
    public void setGasrate( String gasrate ) {
        this.gasrate = gasrate;
    }

    /**
     * Gets the datetime.
     *
     * @return the datetime
     */
    public String getDatetime() {
        return datetime;
    }

    /**
     * Sets the datetime.
     *
     * @param datetime
     *         the new datetime
     */
    public void setDatetime( String datetime ) {
        this.datetime = datetime;
    }

    /**
     * Gets the currentstate.
     *
     * @return the currentstate
     */
    public String getCurrentstate() {
        return currentstate;
    }

    /**
     * Sets the currentstate.
     *
     * @param currentstate
     *         the new currentstate
     */
    public void setCurrentstate( String currentstate ) {
        this.currentstate = currentstate;
    }

    /**
     * Instantiates a new prediction payload DTO.
     */
    public PredictionPayloadDTO() {
    }

}
