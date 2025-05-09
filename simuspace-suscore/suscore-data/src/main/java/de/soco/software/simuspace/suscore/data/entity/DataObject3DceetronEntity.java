package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import java.io.Serial;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Data object 3d ceetron entity.
 */
@Getter
@Setter
@Entity
@Indexed
public class DataObject3DceetronEntity extends DataObject3DEntity {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 8593184113835976318L;

    /**
     * The Config upload.
     */
    @OneToOne
    DocumentEntity configUpload;

}
