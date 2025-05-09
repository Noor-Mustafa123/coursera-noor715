package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import de.soco.software.simuspace.suscore.common.util.ByteUtil;

/**
 * Database Entity Mapping Class for persistence and retrieval of selection id.
 *
 * @author Noman Arshad
 */
@Getter
@Setter
@Entity
@ToString
@Table( name = "table_Selection" )
@org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
public class SelectionEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -8510503009549791559L;

    /**
     * The id.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The origin.
     */
    @Column( name = "origin" )
    private String origin;

    /**
     * The user entity.
     */
    @ManyToOne
    private UserEntity userEntity;

    /**
     * The created on.
     */
    @Column( name = "createdon" )
    private Date createdon;

    /**
     * The modified on.
     */
    @Column( name = "modifiedon" )
    private Date modifiedOn;

    /**
     * The items.
     */
    @OneToMany( fetch = FetchType.EAGER, mappedBy = "selectionEntity", orphanRemoval = true, cascade = CascadeType.ALL )
    private Set< SelectionItemEntity > items;

    /**
     * The additional attributes json.
     */
    @Column( name = "additionalAttributesJson" )
    @Lob
    private byte[] additionalAttributesJson;

    @Column( name = "json" )
    @Lob
    private byte[] json;

    /**
     * Instantiates a new selection entity.
     */
    public SelectionEntity() {
        super();
    }

    /**
     * Instantiates a new selection entity.
     *
     * @param id
     *         the id
     * @param origin
     *         the origin
     * @param createdOn
     *         the created on
     * @param modifiedOn
     *         the modified on
     * @param items
     *         the items
     */
    public SelectionEntity( UUID id, String origin, Date createdOn, Date modifiedOn, Set< SelectionItemEntity > items ) {
        super();
        this.id = id;
        this.origin = origin;
        this.createdon = createdOn;
        this.modifiedOn = modifiedOn;
        this.items = items;
    }

    /**
     * Gets the additional attributes json.
     *
     * @return the additional attributes json
     */
    public String getAdditionalAttributesJson() {
        return ByteUtil.convertByteToString( additionalAttributesJson );
    }

    /**
     * Sets the additional attributes json.
     *
     * @param additionalAttributesJson
     *         the new additional attributes json
     */
    public void setAdditionalAttributesJson( String additionalAttributesJson ) {
        this.additionalAttributesJson = ByteUtil.convertStringToByte( additionalAttributesJson );
    }

    /**
     * Copy.
     *
     * @return the selection entity
     */
    public SelectionEntity copy() {
        SelectionEntity copy = new SelectionEntity();
        copy.setId( UUID.randomUUID() );
        copy.setCreatedon( new Date() );
        copy.setJson( getJson() );
        copy.setOrigin( getOrigin() );
        copy.setAdditionalAttributesJson( getAdditionalAttributesJson() );
        copy.setUserEntity( getUserEntity() );
        copy.setModifiedOn( new Date() );
        copy.setItems( getItems() );
        return copy;
    }

}
