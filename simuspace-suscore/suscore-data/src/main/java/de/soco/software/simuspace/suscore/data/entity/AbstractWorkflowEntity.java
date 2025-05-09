package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import java.io.Serial;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class AbstractWorkFlowEntity.
 */
@Getter
@Setter
@Entity
// @Inheritance ( strategy = InheritanceType.TABLE_PER_CLASS )
public abstract class AbstractWorkflowEntity extends SuSEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The timeout.
     */
    @Column( name = "timeout" )
    private String timeout;

    /**
     * The is internal.
     */
    @Column( name = "is_internal" )
    private String isInternal;

    /**
     * The composed workflow entity.
     */
    @ManyToOne( fetch = FetchType.EAGER )
    private ComposedWorkflowEntity composedWorkflowEntity;

}
