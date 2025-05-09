package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.io.Serial;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class LoadCaseEntity.
 */
@Getter
@Setter
@Entity
public class LoadCaseEntity extends AbstractWorkflowEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The Dummy type entity.
     */
    @ManyToOne( fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    @JoinColumn( name = "dummy_id" )
    private DummyTypeEntity dummyTypeEntity;

}
