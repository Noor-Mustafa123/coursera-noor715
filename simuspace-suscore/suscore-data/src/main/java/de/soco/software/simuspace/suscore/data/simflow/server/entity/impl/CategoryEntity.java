package de.soco.software.simuspace.suscore.data.simflow.server.entity.impl;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.simflow.server.base.AbstractPersistentObject;

/**
 * This class represent the Category for anyObject(like workflow) . Also Entity mapped to database
 *
 * @author Nosheen.Sharif
 */
@Entity
@Table( name = "category" )
@Deprecated( forRemoval = true )
public class CategoryEntity extends AbstractPersistentObject implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 8606222259997663895L;

    /**
     * Instantiates a new category entity.
     */
    public CategoryEntity() {
        super();
    }

    /**
     * Instantiates a new category entity.
     *
     * @param id
     *         the id
     */
    public CategoryEntity( UUID id ) {
        setId( id );
    }

    /**
     * Instantiates a new category entity.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     */
    public CategoryEntity( UUID id, String name ) {
        this( id );
        setName( name );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "CategoryEntity[id='" + getId() + "', name='" + getName() + "']";
    }

}
