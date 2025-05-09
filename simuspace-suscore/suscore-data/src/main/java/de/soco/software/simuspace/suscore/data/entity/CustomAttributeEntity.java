package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.util.ByteUtil;

/**
 * Database mapping entity of Custom attribute.
 *
 * @author Zeeshan jamal
 * @UpdatedBy M.Nasir.Farooq
 */
@Getter
@Setter
@Entity
@Table( name = "custom_attributes" )
@org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
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
     * The value assign to any key
     */
    @Column
    @Lob
    private byte[] value;

    /**
     * options of the custom attribute.
     */
    @Column
    private String options;

    /**
     * The multiple.
     */
    @Column
    private Boolean multiple;

    @Column( length = 1024 )
    private String bindVisibility;

    /**
     * The tooltip.
     */
    private String tooltip;

    /**
     * The sus entity.
     */
    @ManyToOne
    @JoinColumns( value = { @JoinColumn( name = "object_id", referencedColumnName = "id" ),
            @JoinColumn( name = "object_version_id", referencedColumnName = "version_id" ) } )
    private SuSEntity susEntity;

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Object getValueAsObject() {
        return ByteUtil.convertByteToObject( value );
    }

    /**
     * Checks if is multiple.
     *
     * @return the boolean
     */
    public Boolean isMultiple() {
        return multiple;
    }

}
