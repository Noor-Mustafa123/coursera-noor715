package de.soco.software.simuspace.suscore.common.ui;

import java.util.ArrayList;
import java.util.List;

import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.suscore.jsonschema.model.OVAConfigTab;

/**
 * This Class Maps to Sub-Tabs view for any sus-object In a Tab.
 *
 * @author Nosheen.Sharif
 */
public class SubTabsUI {

    /**
     * The id.
     */
    private String id;

    /**
     * name of the sub-tab.
     */
    private String name;

    /**
     * The title.
     */
    private String title;

    /**
     * The Language.
     */
    private String language;

    /**
     * Default Constructor for SubTabsUI.
     */
    public SubTabsUI() {
        super();
    }

    /**
     * Constructor With Parameters.
     *
     * @param name
     *         the name
     */
    public SubTabsUI( String name ) {
        this.name = name;
    }

    /**
     * Instantiates a new sub tabs UI.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     */
    public SubTabsUI( String id, String name ) {
        this.id = id;
        this.name = name;
    }

    /**
     * Instantiates a new sub tabs UI.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param title
     *         the title
     */
    public SubTabsUI( String id, String name, String title ) {
        this.id = id;
        this.name = name;
        this.title = title;
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
     * Gets language.
     *
     * @return the language
     */
    public String getLanguage() {
        return language;
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
     *         the name to set
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Get SubTabs List.
     *
     * @param withVersion
     *         the with version
     * @param jsonData
     *         the json data
     *
     * @return List<SubTabsUI>
     */
    public static List< SubTabsUI > getSubTabsList( List< OVAConfigTab > jsonData ) {
        List< SubTabsUI > subTabsUI = new ArrayList<>();
        for ( OVAConfigTab ovaConfigTab : jsonData ) {
            if ( ovaConfigTab.isVisible() ) {
                subTabsUI.add( new SubTabsUI( ovaConfigTab.getTypeId(), ovaConfigTab.getKey(),
                        MessageBundleFactory.getMessage( ovaConfigTab.getTitle() ) ) );
            }
        }
        return subTabsUI;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
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
        SubTabsUI other = ( SubTabsUI ) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
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
     * Sets language.
     *
     * @param language
     *         the language
     */
    public void setLanguage( String language ) {
        this.language = language;
    }

}
