package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;

import java.io.Serial;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class ReportEntity.
 */
@Getter
@Setter
@Entity
@Indexed( index = "ReportEntity" )
public class ReportEntity extends AbstractModelEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

}
