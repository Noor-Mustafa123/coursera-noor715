package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * An entity class that would be mapped to the section related to objects and responsible for managing Section Objects.
 *
 * @author Ahsan.Khan
 */
@Getter
@Setter
@Entity
@Table( name = "section_objects" )
@org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
public class SectionObjectEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 4389726255955028774L;

    /**
     * The primary key.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The section entity reference .
     */
    @ManyToOne( fetch = FetchType.EAGER )
    private SectionEntity sectionEntity;

    /**
     * The object entity reference .
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumns( value = { @JoinColumn( name = "object_id", referencedColumnName = "id" ),
            @JoinColumn( name = "object_version_id", referencedColumnName = "version_id" ) } )
    private SuSEntity objectEntity;

}
