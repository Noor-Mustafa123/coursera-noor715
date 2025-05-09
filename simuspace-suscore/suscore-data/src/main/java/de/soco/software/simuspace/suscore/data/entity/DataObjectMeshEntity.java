package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import java.io.Serial;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class DataObjectMeshEntity.
 */
@Getter
@Setter
@Entity
@Indexed
public class DataObjectMeshEntity extends DataObjectFileEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The preview image.
     */
    @Column( name = "preview_image" )
    private String previewImage;

}
