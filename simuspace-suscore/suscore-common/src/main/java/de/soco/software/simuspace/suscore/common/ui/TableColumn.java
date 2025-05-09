package de.soco.software.simuspace.suscore.common.ui;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;

/**
 * The Table column class used for rendering columns on the table specified by data and filters.
 */
public class TableColumn {

    /**
     * the data to be loaded/view.
     */

    private String data;

    /**
     * the title of the column.
     */

    private String title;

    /**
     * the filter used for column searching.
     */
    private Object filter;

    /**
     * if the column can be sorted or not.
     */
    private boolean sortable = true;

    /**
     * The visible.
     */
    private boolean visible = true;

    /**
     * the Renderer object.
     */
    private Renderer renderer;

    /**
     * The name.
     */
    private String name;

    /**
     * The orderNum.
     */
    private int orderNum;

    /**
     * The Rotated.
     */
    private boolean rotated = false;

    /**
     * The reorder.
     */
    private int reorder;

    /**
     * The reorder.
     */
    private boolean show = true;

    /**
     * The Width.
     */
    private int width = ConstantsInteger.DEFAULT_COLUMN_WIDTH;

    /**
     * Instantiates a new table column.
     */
    public TableColumn() {
        super();
    }

    /**
     * Instantiates a new table column.
     */
    public TableColumn( String title, String name, String data, Object filter ) {
        super();
        this.title = title;
        this.name = name;
        this.data = data;
        this.filter = filter;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Is show boolean.
     *
     * @return the boolean
     */
    public boolean isShow() {
        return show;
    }

    /**
     * Sets the data.
     *
     * @param data
     *         the new data
     */
    public void setData( String data ) {
        this.data = data;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title
     *         the new title
     */
    public void setTitle( String title ) {
        this.title = title;
    }

    /**
     * Gets the filter.
     *
     * @return the filter
     */
    public Object getFilter() {
        return filter;
    }

    /**
     * Sets the filter.
     *
     * @param filter
     *         the new filter
     */
    public void setFilter( Object filter ) {
        this.filter = filter;
    }

    /**
     * Gets the renderer.
     *
     * @return the renderer
     */
    public Renderer getRenderer() {
        return renderer;
    }

    /**
     * Sets the renderer.
     *
     * @param renderer
     *         the new renderer
     */
    public void setRenderer( Renderer renderer ) {
        this.renderer = renderer;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Checks if is sortable.
     *
     * @return true, if is sortable
     */
    public boolean isSortable() {
        return sortable;
    }

    /**
     * Sets the sortable.
     *
     * @param sortable
     *         the new sortable
     */
    public void setSortable( boolean sortable ) {
        this.sortable = sortable;
    }

    /**
     * Checks if is visible.
     *
     * @return true, if is visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the visible.
     *
     * @param visible
     *         the new visible
     */
    public void setVisible( boolean visible ) {
        this.visible = visible;
    }

    /**
     * Gets the orderNum.
     *
     * @return the orderNumr
     */
    public int getOrderNum() {
        return orderNum;
    }

    /**
     * Sets the orderNum.
     *
     * @param orderNum
     *         the new orderNum
     */
    public void setOrderNum( int orderNum ) {
        this.orderNum = orderNum;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( data == null ) ? 0 : data.hashCode() );
        result = prime * result + ( ( filter == null ) ? 0 : filter.hashCode() );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        result = prime * result + orderNum;
        result = prime * result + ( ( renderer == null ) ? 0 : renderer.hashCode() );
        result = prime * result + ( sortable ? 1231 : 1237 );
        result = prime * result + ( ( title == null ) ? 0 : title.hashCode() );
        result = prime * result + ( visible ? 1231 : 1237 );
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( !( obj instanceof TableColumn ) ) {
            return false;
        }
        TableColumn other = ( TableColumn ) obj;
        if ( data == null ) {
            if ( other.data != null ) {
                return false;
            }
        } else if ( !data.equals( other.data ) ) {
            return false;
        }
        if ( filter == null ) {
            if ( other.filter != null ) {
                return false;
            }
        } else if ( !filter.equals( other.filter ) ) {
            return false;
        }
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals( other.name ) ) {
            return false;
        }
        if ( orderNum != other.orderNum ) {
            return false;
        }
        if ( renderer == null ) {
            if ( other.renderer != null ) {
                return false;
            }
        } else if ( !renderer.equals( other.renderer ) ) {
            return false;
        }
        if ( sortable != other.sortable ) {
            return false;
        }
        if ( title == null ) {
            if ( other.title != null ) {
                return false;
            }
        } else if ( !title.equals( other.title ) ) {
            return false;
        }
        if ( visible != other.visible ) {
            return false;
        }
        return true;
    }

    public boolean isRotated() {
        return rotated;
    }

    public void setRotated( boolean rotated ) {
        this.rotated = rotated;
    }

    @Override
    public String toString() {
        return "TableColumn [data=" + data + ", title=" + title + ", filter=" + filter + ", sortable=" + sortable + ", visible=" + visible
                + ", renderer=" + renderer + ", name=" + name + ", orderNum=" + orderNum + ", rotated=" + rotated + "]";
    }

    /**
     * Gets reorder.
     *
     * @return the reorder
     */
    public int getReorder() {
        return reorder;
    }

    /**
     * Sets reorder.
     *
     * @param reorder
     *         the reorder
     */
    public void setReorder( int reorder ) {
        this.reorder = reorder;
    }

    /**
     * Sets show.
     *
     * @param show
     *         the show
     */
    public void setShow( boolean show ) {
        this.show = show;
    }

    /**
     * Sets width.
     *
     * @param width
     *         the width
     */
    public void setWidth( int width ) {
        this.width = width;
    }

}
