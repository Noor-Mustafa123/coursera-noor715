package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;

import java.io.Serial;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class TrainingAlgoEntity.
 *
 * @author noman arshad
 */
@Getter
@Setter
@Entity
public class TrainingAlgoEntity extends SuSEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The algo config.
     */
    @OneToOne
    @JoinColumns( @JoinColumn( name = "algoConfig_id", referencedColumnName = "id" ) )
    DocumentEntity algoConfig;

}
