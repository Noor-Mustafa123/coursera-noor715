/*
 *
 */

package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class SectionEntity.
 *
 * @author Ahsan.Khan
 */
@Getter
@Setter
@Entity
@Table( name = "section" )
@org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
public class SectionEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1273177665827932128L;

    /**
     * The id.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The title.
     */
    @Column( name = "title" )
    private String title;

    /**
     * The name.
     */
    @Column( name = "name" )
    private String name;

    /**
     * The description.
     */
    @Column( name = "description" )
    private String description;

    /**
     * The type.
     */
    @Column( name = "type" )
    private String type;

    @Column( name = "selection_id" )
    private UUID selectionId;

    /**
     * The order.
     */
    @Column( name = "orderId" )
    @Generated( GenerationTime.INSERT )
    private Integer sectionOrder;

    /**
     * The report entity.
     */
    @ManyToOne
    private ReportEntity reportEntity;

    /**
     * Instantiates a new section entity.
     *
     * @param id
     *         the id
     * @param title
     *         the title
     * @param name
     *         the name
     * @param description
     *         the description
     * @param type
     *         the type
     * @param selectionId
     *         the selection id
     */
    public SectionEntity( UUID id, String title, String name, String description, String type, UUID selectionId ) {
        super();
        this.id = id;
        this.title = title;
        this.name = name;
        this.description = description;
        this.type = type;
        this.selectionId = selectionId;
    }

    /**
     * Instantiates a new section entity.
     */
    public SectionEntity() {
        super();
    }

}
