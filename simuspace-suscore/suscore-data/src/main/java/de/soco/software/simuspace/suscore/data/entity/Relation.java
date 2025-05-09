package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class Relation.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
@Table( name = "relation" )
public class Relation {

    /**
     * The id.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The parent.
     */
    @Column( name = "parent_oid" )
    @Type( type = "uuid-char" )
    private UUID parent;

    /**
     * The child.
     */
    @Column( name = "child_oid" )
    @Type( type = "uuid-char" )
    private UUID child;

    /**
     * The type.
     */
    @Column( name = "link_type" )
    @Type( type = "int" )
    @ColumnDefault( "0" )
    private int type;

    /**
     * The created on.
     */
    @Column( name = "created_on" )
    private Date createdOn;

    /**
     * The audit log entity.
     */
    @ManyToOne( cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    @ToString.Exclude
    private AuditLogEntity auditLogEntity;

    /**
     * Instantiates a new relation.
     *
     * @param parent
     *         the parent
     * @param child
     *         the child
     */
    public Relation( UUID parent, UUID child ) {
        super();
        this.id = UUID.randomUUID();
        this.parent = parent;
        this.child = child;
        this.createdOn = new Date();
    }

    /**
     * Instantiates a new relation.
     *
     * @param parent
     *         the parent
     * @param child
     *         the child
     * @param type
     *         the type
     */
    public Relation( UUID parent, UUID child, int type ) {
        super();
        this.id = UUID.randomUUID();
        this.parent = parent;
        this.child = child;
        this.type = type;
        this.createdOn = new Date();
    }

}
