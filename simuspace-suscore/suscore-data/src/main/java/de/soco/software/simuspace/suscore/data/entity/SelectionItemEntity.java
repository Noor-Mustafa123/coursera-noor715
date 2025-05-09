package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
import lombok.ToString;

import de.soco.software.simuspace.suscore.common.util.ByteUtil;

/**
 * Database Entity Mapping Class for persistence and retrieval of selected users.
 *
 * @author Noman Arshad
 */
@Getter
@Setter
@ToString
@Entity
@Table( name = "table_selectionItems" )
@org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
public class SelectionItemEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 4625904053729805800L;

    /**
     * The id.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The item.
     */
    // Editable
    @Column( name = "item" )
    @Lob
    private byte[] item;

    /**
     * The selection entity.
     */
    @ManyToOne( fetch = FetchType.LAZY, optional = false )
    @JoinColumn( name = "selection_id" )
    @ToString.Exclude
    private SelectionEntity selectionEntity;

    /**
     * The item order.
     */
    @Column( name = "item_order", columnDefinition = "int default 0" )
    private Integer itemOrder = 0;

    /**
     * The additional attributes json.
     */
    @Column( name = "additionalAttributesJson" )
    @Lob
    private byte[] additionalAttributesJson;

    /**
     * Instantiates a new selection item entity.
     */
    public SelectionItemEntity() {
        super();
    }

    /**
     * Instantiates a new selection item entity.
     *
     * @param item
     *         the item
     */
    public SelectionItemEntity( String item ) {
        super();
        this.item = ByteUtil.convertStringToByte( item );
    }

    /**
     * Instantiates a new selection item entity.
     *
     * @param sid
     *         the sid
     * @param item
     *         the item
     * @param selectionEntity
     *         the selection entity
     */
    public SelectionItemEntity( UUID sid, String item, SelectionEntity selectionEntity ) {
        super();
        this.id = sid;
        this.item = ByteUtil.convertStringToByte( item );
        this.selectionEntity = selectionEntity;
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
     * Gets the item.
     *
     * @return the item
     */
    public String getItem() {
        return ByteUtil.convertByteToString( this.item );
    }

    /**
     * Sets the item.
     *
     * @param item
     *         the new item
     */
    public void setItem( String item ) {
        this.item = ByteUtil.convertStringToByte( item );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( item == null ) ? 0 : item.hashCode() );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        SelectionItemEntity other = ( SelectionItemEntity ) obj;
        if ( item == null ) {
            if ( other.item != null ) {
                return false;
            }
        } else if ( !item.equals( other.item ) ) {
            return false;
        }
        return true;
    }

    /**
     * Copy.
     *
     * @return the selection item entity
     */
    public SelectionItemEntity copy() {
        SelectionItemEntity copy = new SelectionItemEntity();
        copy.setId( UUID.randomUUID() );
        copy.setItem( getItem() );
        copy.setSelectionEntity( getSelectionEntity() );
        copy.setItemOrder( getItemOrder() );
        copy.setAdditionalAttributesJson( getAdditionalAttributesJson() );
        return copy;
    }

}
