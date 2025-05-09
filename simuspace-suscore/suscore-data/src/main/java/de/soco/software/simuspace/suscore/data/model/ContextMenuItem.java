package de.soco.software.simuspace.suscore.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The context menu item class used for rendering context menu at front end.
 *
 * @author Huzaifah
 * @author sces130
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ContextMenuItem {

    /**
     * The Constant BULK_SUFIX_TO_TITLE.
     */
    public static final String BULK_SUFIX_TO_TITLE = " Bulk";

    /**
     * The Constant DEFUALT_VISIBILITY.
     */
    public static final String DEFUALT_VISIBILITY = "both";

    /**
     * the url to open when we select something from context menu.
     */
    private String url;

    /**
     * title showed in context menu.
     */

    private String title;

    /**
     * The icon.
     */
    private String icon;

    private boolean divider = false;

    /**
     * The visibility.
     */
    private String visibility = DEFUALT_VISIBILITY;

    private String linkClass;

    /**
     * Instantiates a new context menu item.
     *
     * @param url
     *         the url
     * @param icon
     *         the icon
     * @param title
     *         the title
     */
    public ContextMenuItem( String url, String icon, String title ) {
        this.url = url;
        this.icon = icon;
        this.title = title;
    }

    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url.
     *
     * @param url
     *         the new url
     */
    public void setUrl( String url ) {
        this.url = url;
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
     * Instantiates a new context menu item.
     */
    public ContextMenuItem() {
        super();
    }

    /**
     * Gets the icon.
     *
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the icon.
     *
     * @param icon
     *         the new icon
     */
    public void setIcon( String icon ) {
        this.icon = icon;
    }

    /**
     * Gets the visibility.
     *
     * @return the visibility
     */
    public String getVisibility() {
        return visibility;
    }

    /**
     * Sets the visibility.
     *
     * @param visibility
     *         the new visibility
     */
    public void setVisibility( String visibility ) {
        this.visibility = visibility;
    }

    /**
     * Checks if is equals.
     *
     * @param configItem
     *         the obj
     *
     * @return true, if is equals
     */
    public boolean isEqualsTo( RouterConfigItem configItem ) {
        if ( configItem == null ) {
            return false;
        }
        RouterConfigItem other = configItem;
        if ( icon == null ) {
            if ( other.getIcon() != null ) {
                return false;
            }
        } else if ( !icon.equals( other.getIcon() ) ) {
            return false;
        }
        if ( title == null ) {
            if ( other.getTitle() != null ) {
                return false;
            }
        } else if ( !title.equals( other.getTitle() ) ) {
            return false;
        }
        if ( url == null ) {
            if ( other.getUrl() != null ) {
                return false;
            }
        } else if ( !url.equals( other.getUrl() ) ) {
            return false;
        }
        if ( visibility == null ) {
            if ( other.getVisibility() != null ) {
                return false;
            }
        } else if ( !visibility.equals( other.getVisibility() ) ) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( icon == null ) ? 0 : icon.hashCode() );
        result = prime * result + ( ( title == null ) ? 0 : title.hashCode() );
        result = prime * result + ( ( url == null ) ? 0 : url.hashCode() );
        result = prime * result + ( ( visibility == null ) ? 0 : visibility.hashCode() );
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
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        ContextMenuItem other = ( ContextMenuItem ) obj;
        if ( icon == null ) {
            if ( other.icon != null ) {
                return false;
            }
        } else if ( !icon.equals( other.icon ) ) {
            return false;
        }
        if ( title == null ) {
            if ( other.title != null ) {
                return false;
            }
        } else if ( !title.equals( other.title ) ) {
            return false;
        }
        if ( url == null ) {
            if ( other.url != null ) {
                return false;
            }
        } else if ( !url.equals( other.url ) ) {
            return false;
        }
        if ( visibility == null ) {
            if ( other.visibility != null ) {
                return false;
            }
        } else if ( !visibility.equals( other.visibility ) ) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ContextMenuItem [url=" + url + ", title=" + title + ", icon=" + icon + ", visibility=" + visibility + "]";
    }

    public boolean isDivider() {
        return divider;
    }

    public void setDivider( boolean divider ) {
        this.divider = divider;
    }

    public String getLinkClass() {
        return linkClass;
    }

    public void setLinkClass( String linkClass ) {
        this.linkClass = linkClass;
    }

}
