package de.soco.software.simuspace.suscore.common.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * Database mapping entity of Custom attribute.
 *
 * @author Zeeshan jamal
 * @UpdatedBy M.Nasir.Farooq
 */
@Getter
@Setter
public class CustomAttributeEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -2576838608010381201L;

    /**
     * The custom attribute uuid.
     */
    @Id
    @Type( type = "uuid-char" )
    @Column( name = "custom_attribute_id" )
    private UUID id;

    /**
     * name of the custom attribute.
     */
    @Column
    private String name;

    /**
     * type of the custom attribute.
     */
    @Column
    private String type;

    /**
     * defaultValue of the custom attribute.
     */
    @Column
    private String value;

    /**
     * optons of the custom attribute.
     */
    @Column
    private String options;

    /**
     * The sus entity.
     */
    @ManyToOne
    @JoinColumns( value = { @JoinColumn( name = "object_id", referencedColumnName = "id" ),
            @JoinColumn( name = "object_version_id", referencedColumnName = "version_id" ) } )
    private SuSEntity susEntity;

}
