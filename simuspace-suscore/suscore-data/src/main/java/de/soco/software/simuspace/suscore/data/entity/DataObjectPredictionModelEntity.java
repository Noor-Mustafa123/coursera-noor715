package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import java.io.Serial;
import java.util.UUID;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Indexed
public class DataObjectPredictionModelEntity extends DataObjectFileEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 6432594457894154205L;

    /**
     * The Constant CLASS_ID.
     */
    public static final UUID CLASS_ID = UUID.fromString( "ac01986a-a21a-42e4-ba74-24ac95bb5257" );

    /**
     * The file.
     */
    @OneToOne
    private DocumentEntity jsonFile;

    /**
     * The file.
     */
    @OneToOne
    private DocumentEntity binFile;

}
