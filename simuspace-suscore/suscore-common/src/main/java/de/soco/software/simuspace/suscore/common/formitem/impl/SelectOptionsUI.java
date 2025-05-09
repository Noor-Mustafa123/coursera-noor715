package de.soco.software.simuspace.suscore.common.formitem.impl;

/**
 * The class is used to prepare the options for drop down on FE form.
 *
 * @author M.Nasir.Farooq
 */
public class SelectOptionsUI {

    /**
     * The id.
     */
    private String id;

    /**
     * The name.
     */
    private String name;

    /**
     * The icon.
     */
    private String icon;

    /**
     * The color.
     */
    private String color;

    /**
     * Instantiates a new select object UI.
     */
    public SelectOptionsUI() {
    }

    /**
     * Instantiates a new select object UI.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     */
    public SelectOptionsUI( String id, String name ) {
        this.id = id;
        this.name = name;
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
     *         the id to set
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
     * Hash code int.
     *
     * @return the int
     */
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    /**
     * Gets the color.
     *
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color.
     *
     * @param color
     *         the color to set
     */
    public void setColor( String color ) {
        this.color = color;
    }

    /**
     * Equals boolean.
     *
     * @param obj
     *         the obj
     *
     * @return the boolean
     */
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
        SelectOptionsUI other = ( SelectOptionsUI ) obj;
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals( other.id ) ) {
            return false;
        }
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

}
