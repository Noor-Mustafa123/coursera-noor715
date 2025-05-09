package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class BmwCaeBenchNodeEntity.
 *
 * @author noman arshad
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table( name = "bmw_node" )
@JsonIgnoreProperties( ignoreUnknown = true )
public class BmwCaeBenchNodeEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 2521252433064152254L;

    /**
     * The primary key.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The type.
     */
    private String type;

    /**
     * The name.
     */
    private String node;

}
