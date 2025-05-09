package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import java.io.Serial;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class DataObjectGenericEntity.
 */
@Getter
@Setter
@Entity
public class DataObjectGenericEntity extends DataObjectEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The value.
     */
    @Column( name = "data_object_value" )
    String value;

}
