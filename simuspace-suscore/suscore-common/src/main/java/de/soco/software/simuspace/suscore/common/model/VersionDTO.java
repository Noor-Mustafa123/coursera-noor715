package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class provide version id (other attributes can be added as per need) as object representation
 *
 * @author Nosheen.Sharif
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class VersionDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -2438961329807506778L;

    /**
     * The version id of any object which is versionable.
     */
    @UIFormField( name = "version.id", title = "3000057x4", isAsk = false )
    @UIColumn( data = "version.id", filter = "uuid", renderer = "text", title = "3000057x4", name = "composedId.versionId" )
    private int id;

    /**
     * Instantiates a new version.
     */
    public VersionDTO() {
        super();
    }

    /**
     * Instantiates a new version.
     *
     * @param id
     *         the id
     */
    public VersionDTO( int id ) {
        this();
        this.id = id;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( int id ) {
        this.id = id;

    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
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
        VersionDTO other = ( VersionDTO ) obj;
        if ( id != other.id ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "VersionDTO [id=" + id + "]";
    }

}
