package de.soco.software.simuspace.suscore.notification.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class UIStateDTO for sending ui status of users.
 *
 * @author Zeeshan jamal
 */
public class UIStateDTO {

    /**
     * The urls.
     */
    private List< UrlDTO > urls = new ArrayList<>();

    /**
     * The update tree.
     */
    private TreeStateDTO tree;

    /**
     * Gets the urls.
     *
     * @return the urls
     */
    public List< UrlDTO > getUrls() {
        return urls;
    }

    /**
     * Sets the urls.
     *
     * @param urls
     *         the new urls
     */
    public void setUrls( List< UrlDTO > urls ) {
        this.urls = urls;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( urls == null ) ? 0 : urls.hashCode() );
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
        UIStateDTO other = ( UIStateDTO ) obj;
        if ( tree != other.tree ) {
            return false;
        }
        if ( urls == null ) {
            if ( other.urls != null ) {
                return false;
            }
        } else if ( !urls.equals( other.urls ) ) {
            return false;
        }
        return true;
    }

    /**
     * Gets the tree.
     *
     * @return the tree
     */
    public TreeStateDTO getTree() {
        return tree;
    }

    /**
     * Sets the tree.
     *
     * @param tree
     *         the new tree
     */
    public void setTree( TreeStateDTO tree ) {
        this.tree = tree;
    }

}
