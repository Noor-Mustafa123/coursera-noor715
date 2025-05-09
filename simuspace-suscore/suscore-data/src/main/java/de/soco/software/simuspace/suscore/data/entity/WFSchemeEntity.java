package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;

import java.io.Serial;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class WFSchemeEntity.
 */
@Getter
@Setter
@Entity
public class WFSchemeEntity extends SuSEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -6309027003483269916L;

    /**
     * The category.
     */
    private int category;

    /**
     * The algo config.
     */
    @OneToOne
    @JoinColumns( @JoinColumn( name = "algoConfig_id", referencedColumnName = "id" ) )
    DocumentEntity algoConfig;

}
