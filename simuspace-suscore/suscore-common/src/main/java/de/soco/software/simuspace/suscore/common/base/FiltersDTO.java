/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.base;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class Filters.
 *
 * @author Shan.Arshad
 */

public class FiltersDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The draw.
     */
    private Integer draw;

    /**
     * The start.
     */
    private Integer start;

    /**
     * The length.
     */
    private Integer length;

    /**
     * columns to filter.
     */
    private List< FilterColumn > columns;

    /**
     * The search query.
     */
    private String search;

    /**
     * The search in.
     */
    private List< String > searchIn;

    /**
     * The id list.
     */
    private List< Object > items;

    /**
     * The search id.
     */
    private UUID searchId;

    /**
     * The current view.
     */
    private String currentView;

    /**
     * The my searches.
     */
    private boolean mySearches;

    /**
     * The type.
     */
    private String type;

    /**
     * The total records.
     */
    private Long totalRecords;

    /**
     * The filtered records.
     */
    private Long filteredRecords;

    /**
     * The type class.
     */
    @JsonIgnore
    private List< String > typeClass;

    /**
     * The reload UI.
     */
    private boolean reloadUI;

    /**
     * The ssfe.
     */
    private boolean ssfe;

    /**
     * Instantiates a new filters.
     */
    public FiltersDTO() {

    }

    /**
     * Instantiates a new filters DTO.
     *
     * @param draw
     *         the draw
     * @param start
     *         the start
     * @param length
     *         the length
     * @param filteredRecords
     *         the filtered records
     */
    public FiltersDTO( Integer draw, Integer start, Integer length, Long filteredRecords ) {
        this.draw = draw;
        this.start = start;
        this.length = length;
        this.filteredRecords = filteredRecords;
    }

    /**
     * Instantiates a new filters DTO.
     *
     * @param draw
     *         the draw
     * @param start
     *         the start
     * @param length
     *         the length
     */
    public FiltersDTO( Integer draw, Integer start, Integer length ) {
        this.draw = draw;
        this.start = start;
        this.length = length;
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
     * Gets the search.
     *
     * @return the search
     */
    public String getSearch() {
        return search;
    }

    /**
     * Sets the search.
     *
     * @param search
     *         the search to set
     */
    public void setSearch( String search ) {
        this.search = search;
    }

    /**
     * Gets the items.
     *
     * @return the items
     */
    public List< Object > getItems() {
        return items;
    }

    /**
     * Sets the items.
     *
     * @param items
     *         the new items
     */
    public void setItems( List< Object > items ) {
        this.items = items;
    }

    /**
     * Gets the search in.
     *
     * @return the search in
     */
    public List< String > getSearchIn() {
        return searchIn;
    }

    /**
     * Sets the search in.
     *
     * @param searchIn
     *         the new search in
     */
    public void setSearchIn( List< String > searchIn ) {
        this.searchIn = searchIn;
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
     * Checks if is my searches.
     *
     * @return true, if is my searches
     */
    public boolean isMySearches() {
        return mySearches;
    }

    /**
     * Sets the my searches.
     *
     * @param mySearches
     *         the new my searches
     */
    public void setMySearches( boolean mySearches ) {
        this.mySearches = mySearches;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * Gets the total records.
     *
     * @return the totalRecords
     */
    public Long getTotalRecords() {
        return totalRecords;
    }

    /**
     * Sets the total records.
     *
     * @param totalRecords
     *         the totalRecords to set
     */
    public void setTotalRecords( Long totalRecords ) {
        this.totalRecords = totalRecords;
    }

    /**
     * Gets the filtered records.
     *
     * @return the filtered records
     */
    public Long getFilteredRecords() {
        return filteredRecords;
    }

    /**
     * Sets the filtered records.
     *
     * @param filteredRecords
     *         the new filtered records
     */
    public void setFilteredRecords( Long filteredRecords ) {
        this.filteredRecords = filteredRecords;
    }

    /**
     * Gets the columns.
     *
     * @return the columns
     */
    public List< FilterColumn > getColumns() {
        return columns;
    }

    /**
     * Sets the columns.
     *
     * @param columns
     *         the new columns
     */
    public void setColumns( List< FilterColumn > columns ) {
        this.columns = columns;
    }

    /**
     * Gets the type class.
     *
     * @return the type class
     */
    public List< String > getTypeClass() {
        return typeClass;
    }

    /**
     * Sets the type class.
     *
     * @param typeClass
     *         the new type class
     */
    public void setTypeClass( List< String > typeClass ) {
        this.typeClass = typeClass;
    }

    /**
     * Checks if is reload UI.
     *
     * @return true, if is reload UI
     */
    public boolean isReloadUI() {
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
     * Checks if is server side file explorer.
     *
     * @return true, if is ssfe
     */
    public boolean isSsfe() {
        return ssfe;
    }

    /**
     * Sets the server side file explorer.
     *
     * @param ssfe
     *         the new ssfe
     */
    public void setSsfe( boolean ssfe ) {
        this.ssfe = ssfe;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( draw == null ) ? 0 : draw.hashCode() );
        result = prime * result + ( ( filteredRecords == null ) ? 0 : filteredRecords.hashCode() );
        result = prime * result + ( ( length == null ) ? 0 : length.hashCode() );
        result = prime * result + ( ( start == null ) ? 0 : start.hashCode() );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        FiltersDTO other = ( FiltersDTO ) obj;
        if ( search == null ) {
            if ( other.search != null ) {
                return false;
            }
        } else if ( !search.equals( other.search ) ) {
            return false;
        }
        if ( searchIn == null ) {
            if ( other.searchIn != null ) {
                return false;
            }
        } else if ( !searchIn.equals( other.searchIn ) ) {
            return false;
        }
        if ( columns == null ) {
            if ( other.columns != null ) {
                return false;
            }
        } else if ( !columns.equals( other.columns ) ) {
            return false;
        }
        if ( length == null ) {
            if ( other.length != null ) {
                return false;
            }
        } else if ( !length.equals( other.length ) ) {
            return false;
        }
        if ( start == null ) {
            if ( other.start != null ) {
                return false;
            }
        } else if ( !start.equals( other.start ) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FiltersDTO [draw=" + draw + ", start=" + start + ", length=" + length + ", columns=" + columns + ", search=" + search
                + ", searchIn=" + searchIn + ", items=" + items + ", searchId=" + searchId + ", currentView=" + currentView
                + ", mySearches=" + mySearches + ", type=" + type + ", totalRecords=" + totalRecords + ", filteredRecords="
                + filteredRecords + ", typeClass=" + typeClass + ", reloadUI=" + reloadUI + "]";
    }

}
