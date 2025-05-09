package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class DummyTypeEntity.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table( name = "dummy_type" )
public class DummyTypeEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -2497583913943433112L;

    /**
     * The primary key.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The name.
     */
    private String dummyTypeName;

    /**
     * The solver type name.
     */
    private String solverType;

    /**
     * Instantiates a new dummy type entity.
     *
     * @param id
     *         the id
     * @param dummyTypeName
     *         the dummy type name
     */
    public DummyTypeEntity( UUID id, String dummyTypeName, String solverType ) {
        super();
        this.id = id;
        this.dummyTypeName = dummyTypeName;
        this.solverType = solverType;
    }

}
