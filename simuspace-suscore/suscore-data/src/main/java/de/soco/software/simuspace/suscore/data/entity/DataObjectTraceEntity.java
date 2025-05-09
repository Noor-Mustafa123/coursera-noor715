package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import java.io.Serial;
import java.util.UUID;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class DataObjectTraceEntity.
 *
 * @author noman arshad
 */
@Getter
@Setter
@ToString
@Entity
@Indexed
public class DataObjectTraceEntity extends DataObjectFileEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -6725822449740482506L;

    /**
     * The Constant CLASS_ID.
     */
    public static final UUID CLASS_ID = UUID.fromString( "23ef59e6-c349-4ac6-88de-2b3cba641a35" );

    /**
     * Instantiates a new data object trace entity.
     */
    public DataObjectTraceEntity() {
        super();
    }

    /**
     * The layout.
     */
    @Column( name = "plot" )
    @Lob
    private byte[] plot;

    /**
     * The plot type.
     */
    @Column( name = "plotType" )
    private String plotType;

}
