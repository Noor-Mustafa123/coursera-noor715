package de.soco.software.simuspace.workflow.model.impl;

import de.soco.software.simuspace.workflow.model.ElementConnection;

/**
 * Element connections tells us how two work flow elements are connected to each other. It contains a source and target element id's
 *
 * @author M.Nasir.Farooq
 */
public class ElementConnectionImpl implements ElementConnection {

    /**
     * The source id.
     */
    private String source;

    /**
     * The target id.
     */
    private String target;

    /**
     * Instantiates a new element connection impl.
     */
    public ElementConnectionImpl() {
        super();
    }

    /**
     * Instantiates a new element connection impl.
     *
     * @param source
     *         the source
     * @param target
     *         the target
     */
    public ElementConnectionImpl( String source, String target ) {
        super();
        this.source = source;
        this.target = target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSource() {
        return source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTarget() {
        return target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSource( String source ) {
        this.source = source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTarget( String target ) {
        this.target = target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ElementConnectionsImpl [source='" + source + "', target='" + target + "']";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( source == null ) ? 0 : source.hashCode() );
        result = prime * result + ( ( target == null ) ? 0 : target.hashCode() );
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
        ElementConnectionImpl other = ( ElementConnectionImpl ) obj;
        if ( source == null ) {
            if ( other.source != null ) {
                return false;
            }
        } else if ( !source.equals( other.source ) ) {
            return false;
        }
        if ( target == null ) {
            if ( other.target != null ) {
                return false;
            }
        } else if ( !target.equals( other.target ) ) {
            return false;
        }
        return true;
    }

}
