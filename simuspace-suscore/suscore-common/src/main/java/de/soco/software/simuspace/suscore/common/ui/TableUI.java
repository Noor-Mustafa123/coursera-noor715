package de.soco.software.simuspace.suscore.common.ui;

import java.util.ArrayList;
import java.util.List;

import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;

/**
 * The Table User interface class used for viewing columns and data on front end.
 */
public class TableUI {

    /**
     * List of all columns *.
     */

    private List< TableColumn > columns;

    /**
     * The filters json.
     */
    private String currentView;

    /**
     * List of views to be rendered *.
     */
    private List< ObjectViewDTO > views;

    /**
     * Default Constructor.
     */
    public TableUI() {
        super();
    }

    /**
     * Instantiates a new table UI.
     *
     * @param columns
     *         the columns
     * @param views
     *         the views
     */
    public TableUI( List< TableColumn > columns, List< ObjectViewDTO > views, String currentView ) {
        super();
        this.columns = columns;
        this.views = views;
        this.currentView = currentView;
    }

    /**
     * Instantiates a new table UI.
     *
     * @param columns
     *         the columns
     * @param views
     *         the views
     */
    public TableUI( List< TableColumn > columns, List< ObjectViewDTO > views ) {
        super();
        this.columns = columns;
        this.views = views;
        this.currentView = "";
    }

    /**
     * Parameterized Constructor.
     *
     * @param columns
     *         the columns
     */
    public TableUI( List< TableColumn > columns ) {
        super();
        this.columns = columns;
        this.views = new ArrayList<>();
    }

    /**
     * List of columns to be rendered *.
     *
     * @return the columns
     */
    public List< TableColumn > getColumns() {
        return columns;
    }

    /**
     * Sets the columns.
     *
     * @param columns
     *         the new columns
     */
    public void setColumns( List< TableColumn > columns ) {
        this.columns = columns;
    }

    /**
     * Gets the views.
     *
     * @return the views
     */
    public List< ObjectViewDTO > getViews() {
        return views;
    }

    /**
     * Sets the views.
     *
     * @param views
     *         the new views
     */
    public void setViews( List< ObjectViewDTO > views ) {
        this.views = views;
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

}
