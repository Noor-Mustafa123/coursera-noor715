package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;

import java.io.Serial;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

/**
 * The Class DataObjectTargetValueEntity.
 */
@Entity
@Indexed
public class DataObjectTargetValueEntity extends DataObjectValueEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

}
