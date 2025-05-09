package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import java.io.Serial;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class DataObjectCurveEntity.
 */
@Getter
@Setter
@Entity
@Indexed
public class DataObjectCurveEntity extends DataObjectValueEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The file.
     */
    @OneToOne
    @Cascade( { CascadeType.ALL } )
    private DocumentEntity curvethumbNail;

}
