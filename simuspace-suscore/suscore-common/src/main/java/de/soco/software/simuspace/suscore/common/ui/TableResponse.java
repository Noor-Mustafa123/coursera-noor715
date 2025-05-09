package de.soco.software.simuspace.suscore.common.ui;

import java.util.List;

/**
 * The table Response class used for showing total records and all the data.
 *
 * @param <E>
 */
public class TableResponse< E > {

    private int draw = 3;

    private int start = 0;

    private int displayStart = 0;

    private int length = 10;

    private int recordsTotal = 10;

    private int recordsFiltered = 10;

    private String searchId;

    private String filtersJson;

    private List< E > data;

    public int getDraw() {
        return draw;
    }

    public void setDraw( int draw ) {
        this.draw = draw;
    }

    public int getStart() {
        return start;
    }

    public void setStart( int start ) {
        this.start = start;
    }

    public int getDisplayStart() {
        return displayStart;
    }

    public void setDisplayStart( int displayStart ) {
        this.displayStart = displayStart;
    }

    public int getLength() {
        return length;
    }

    public void setLength( int length ) {
        this.length = length;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal( int recordsTotal ) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered( int recordsFiltered ) {
        this.recordsFiltered = recordsFiltered;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId( String searchId ) {
        this.searchId = searchId;
    }

    public String getFiltersJson() {
        return filtersJson;
    }

    public void setFiltersJson( String filtersJson ) {
        this.filtersJson = filtersJson;
    }

    public List< E > getData() {
        return data;
    }

    public void setData( List< E > data ) {
        this.data = data;
    }

    public TableResponse() {
        super();
    }

}
