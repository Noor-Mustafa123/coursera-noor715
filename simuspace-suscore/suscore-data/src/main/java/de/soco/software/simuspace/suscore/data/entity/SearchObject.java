package de.soco.software.simuspace.suscore.data.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class SearchObject.
 *
 * @author Ali Haider
 */
@Getter
@Setter
public class SearchObject {

    /**
     * The name.
     */
    private String name;

    /**
     * The icon.
     */
    private String icon;

    /**
     * The selected.
     */
    private boolean selected;

    /**
     * The entity config.
     */
    private List< SearchObjectModel > configs;

    /**
     * Instantiates a new search object.
     *
     * @param name
     *         the name
     * @param icon
     *         the icon
     * @param entityConfig
     *         the entity config
     */
    public SearchObject( String name, String icon, List< SearchObjectModel > entityConfig ) {
        super();
        this.name = name;
        this.icon = icon;
        this.configs = entityConfig;
    }

    /**
     * Gets the entity config.
     *
     * @return the entity config
     */
    public List< SearchObjectModel > getEntityConfig() {
        return configs;
    }

    /**
     * Sets the entity config.
     *
     * @param entityConfig
     *         the new entity config
     */
    public void setEntityConfig( List< SearchObjectModel > entityConfig ) {
        this.configs = entityConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
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
        SearchObject other = ( SearchObject ) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}
