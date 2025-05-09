package de.soco.software.simuspace.suscore.common.ui;

import java.util.List;

import de.soco.software.simuspace.suscore.common.model.VersionDTO;

/**
 * This class is used for generating sub tabs and its items.
 */
public class SubTabsItem {

    /**
     * The id.
     */
    String id;

    /**
     * The title.
     */
    String title;

    /**
     * The version.
     */
    VersionDTO version;

    /**
     * The tabs.
     */
    List< SubTabsUI > tabs;

    String icon;

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
     * Gets the version.
     *
     * @return the version
     */
    public VersionDTO getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *         the new version
     */
    public void setVersion( VersionDTO version ) {
        this.version = version;
    }

    /**
     * Gets the tabs.
     *
     * @return the tabs
     */
    public List< SubTabsUI > getTabs() {
        return tabs;
    }

    /**
     * Sets the tabs.
     *
     * @param tabs
     *         the new tabs
     */
    public void setTabs( List< SubTabsUI > tabs ) {
        this.tabs = tabs;
    }

    /**
     * Instantiates a new sub tabs item.
     */
    public SubTabsItem() {
        super();
    }

    /**
     * Instantiates a new sub tabs item.
     *
     * @param id
     *         the id
     * @param title
     *         the title
     * @param version
     *         the version
     * @param tabs
     *         the tabs
     */
    public SubTabsItem( String id, String title, int version, List< SubTabsUI > tabs, String icon ) {
        super();
        this.id = id;
        this.title = title;
        this.version = new VersionDTO( version );
        this.tabs = tabs;
        this.icon = icon;
    }

    /**
     * Instantiates a new Sub tabs item.
     *
     * @param id
     *         the id
     * @param title
     *         the title
     * @param tabs
     *         the tabs
     * @param icon
     *         the icon
     */
    public SubTabsItem( String id, String title, List< SubTabsUI > tabs, String icon ) {
        super();
        this.id = id;
        this.title = title;
        this.tabs = tabs;
        this.icon = icon;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( String id ) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        result = prime * result + ( ( tabs == null ) ? 0 : tabs.hashCode() );
        result = prime * result + ( ( title == null ) ? 0 : title.hashCode() );
        result = prime * result + ( ( version == null ) ? 0 : version.hashCode() );
        return result;
    }

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
        SubTabsItem other = ( SubTabsItem ) obj;
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals( other.id ) ) {
            return false;
        }
        if ( tabs == null ) {
            if ( other.tabs != null ) {
                return false;
            }
        } else if ( !tabs.equals( other.tabs ) ) {
            return false;
        }
        if ( title == null ) {
            if ( other.title != null ) {
                return false;
            }
        } else if ( !title.equals( other.title ) ) {
            return false;
        }
        if ( version == null ) {
            if ( other.version != null ) {
                return false;
            }
        } else if ( !version.equals( other.version ) ) {
            return false;
        }
        return true;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon( String icon ) {
        this.icon = icon;
    }

}