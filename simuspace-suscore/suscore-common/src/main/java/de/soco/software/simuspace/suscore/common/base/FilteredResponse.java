/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO Software GmbH
 * All rights reserved.
 * http://www.soco-software.de/
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.base;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class FilteredResponse to be used for paginated response sent to FE..
 *
 * @param <T>
 *         the generic type
 *
 * @author Faisal.Hameed
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class FilteredResponse< Object > extends BaseBO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -2143313517520948852L;

    /**
     * The current number of draw on paginated data.
     */
    private Integer draw;

    /**
     * The start index of the records.
     */
    private Integer start;

    /**
     * The display start.
     */
    private Integer displayStart = 0;

    /**
     * The length of records on a page (to be fetched).
     */
    private Integer length;

    /**
     * The records total.
     */
    private Long recordsTotal;

    /**
     * The records filtered.
     */
    private Integer recordsFiltered;

    /**
     * The data.
     */
    private transient List< Object > data;

    /**
     * The search id.
     */
    private UUID searchId;

    /**
     * The filters json.
     */
    private String currentView;

    /**
     * The reload UI.
     */
    private boolean reloadUI;

    /**
     * Instantiates a new data object filtered response.
     */
    public FilteredResponse() {

    }

    /**
     * Gets the draw.
     *
     * @return the draw
     */
    public Integer getDraw() {
        return draw;
    }

    /**
     * Sets the draw.
     *
     * @param draw
     *         the draw to set
     */
    public void setDraw( Integer draw ) {
        this.draw = draw;
    }

    /**
     * Gets the start.
     *
     * @return the start
     */
    public Integer getStart() {
        return start;
    }

    /**
     * Sets the start.
     *
     * @param start
     *         the start to set
     */
    public void setStart( Integer start ) {
        this.start = start;
    }

    /**
     * Gets the length.
     *
     * @return the length
     */
    public Integer getLength() {
        return length;
    }

    /**
     * Sets the length.
     *
     * @param length
     *         the length to set
     */
    public void setLength( Integer length ) {
        this.length = length;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public List< Object > getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data
     *         the data to set
     */
    public void setData( List< Object > data ) {
        this.data = data;
    }

    /**
     * Gets the records total.
     *
     * @return the recordsTotal
     */
    public Long getRecordsTotal() {
        return recordsTotal;
    }

    /**
     * Sets the records total.
     *
     * @param recordsTotal
     *         the recordsTotal to set
     */
    public void setRecordsTotal( Long recordsTotal ) {
        this.recordsTotal = recordsTotal;
    }

    /**
     * Gets the records filtered.
     *
     * @return the recordsFiltered
     */
    public Integer getRecordsFiltered() {
        return recordsFiltered;
    }

    /**
     * Gets the display start.
     *
     * @return the display start
     */
    public Integer getDisplayStart() {
        return displayStart;
    }

    /**
     * Sets the display start.
     *
     * @param displayStart
     *         the new display start
     */
    public void setDisplayStart( Integer displayStart ) {
        this.displayStart = displayStart;
    }

    /**
     * Gets the search id.
     *
     * @return the search id
     */
    public UUID getSearchId() {
        return searchId;
    }

    /**
     * Sets the search id.
     *
     * @param searchId
     *         the new search id
     */
    public void setSearchId( UUID searchId ) {
        this.searchId = searchId;
    }

    /**
     * Gets the current view.
     *
     * @return the current view
     */
    public String getCurrentView() {
        return currentView;
    }

    /**
     * Sets the current view.
     *
     * @param currentView
     *         the new current view
     */
    public void setCurrentView( String currentView ) {
        this.currentView = currentView;
    }

    /**
     * Gets the reload UI.
     *
     * @return the reload UI
     */
    public boolean getReloadUI() {
        return reloadUI;
    }

    /**
     * Sets the reload UI.
     *
     * @param reloadUI
     *         the new reload UI
     */
    public void setReloadUI( boolean reloadUI ) {
        this.reloadUI = reloadUI;
    }

    /**
     * Sets the records filtered.
     *
     * @param recordsFiltered
     *         the recordsFiltered to set
     */
    public void setRecordsFiltered( int recordsFiltered ) {
        this.recordsFiltered = recordsFiltered;

        // start should be from recent page index after multi delete
        if ( recordsFiltered <= start ) {
            start = ( ( ( recordsFiltered - 1 ) / length ) * length );
            displayStart = start;
        }
    }

    @Override
    public String toString() {
        return "FilteredResponse [draw=" + draw + ", start=" + start + ", displayStart=" + displayStart + ", length=" + length
                + ", recordsTotal=" + recordsTotal + ", recordsFiltered=" + recordsFiltered + ", data=" + data + ", searchId=" + searchId
                + ", currentView=" + currentView + ", reloadUI=" + reloadUI + "]";
    }

}