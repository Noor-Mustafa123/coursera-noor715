package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class JobIdsEntity.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table( name = " job_ids" )
public class JobIdsEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 2460589784438258618L;

    /**
     * The id.
     */
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE )
    @Column( name = "id", updatable = false, nullable = false )
    private Integer id;

    /**
     * The uuid.
     */
    @Type( type = "uuid-char" )
    private UUID uuid;

    /**
     * The name.
     */
    @Column( name = "type" )
    private String type;

    /**
     * Instantiates a new job ids entity.
     *
     * @param uuid
     *         the uuid
     * @param type
     *         the type
     */
    public JobIdsEntity( UUID uuid, String type ) {
        super();
        this.uuid = uuid;
        this.type = type;
    }

}
