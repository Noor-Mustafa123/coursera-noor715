package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class AboutSimuspace is for to show about and support menu.
 *
 * @author Noman Arshad
 */
@Getter
@Setter
@Entity
@Table( name = "about" )
public class AboutSimuspaceEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -231173280304860000L;

    /**
     * The id.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The name.
     */
    @Column( name = "version" )
    private String version;

    /**
     * The name.
     */
    @Column( name = "type" )
    private String type;

    /**
     * The name.
     */
    @Column( name = "buildFeSimuspace" )
    private String buildFeSimuspace;

    /**
     * The name.
     */
    @Column( name = "buildBeSimuspace" )
    private String buildBeSimuspace;

}
