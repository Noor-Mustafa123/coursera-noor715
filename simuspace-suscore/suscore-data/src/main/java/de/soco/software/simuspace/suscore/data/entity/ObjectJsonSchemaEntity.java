package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.util.ByteUtil;

/**
 * The persistent class for the object_json_schema database table.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table( name = "object_json_schema" )
public class ObjectJsonSchemaEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id of the object_json_schema
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * content of the object_json_schema
     */
    @Column( name = "content" )
    @Lob
    private byte[] content;

    /**
     * name of the object_json_schema
     */
    @NotNull
    @Column
    private String name;

    /**
     * createdOn date of object_json_schema
     */
    @Temporal( TemporalType.TIMESTAMP )
    private Date createdOn;

    // bi-directional many-to-one association to parentSchema
    @ManyToOne
    @JoinColumn( name = "parent_id" )
    private ObjectJsonSchemaEntity parentSchema;

    // bi-directional many-to-one association to parentSchema
    @OneToMany( mappedBy = "parentSchema", fetch = FetchType.EAGER )
    private List< ObjectJsonSchemaEntity > objectJsonSchemaChildList;

    /**
     * Custom constructor to set ID at the time of init.
     *
     * @param id
     *         the id
     */
    public ObjectJsonSchemaEntity( UUID id ) {
        this.id = id;
    }

    /**
     * A full Constructor
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param content
     *         the content
     * @param parentId
     *         the parentId
     */
    public ObjectJsonSchemaEntity( UUID id, String name, String content, UUID parentId ) {
        this.id = id;
        this.name = name;
        this.content = ByteUtil.convertStringToByte( content );
        this.parentSchema = new ObjectJsonSchemaEntity( parentId );
    }

    /**
     * @return contents of the object
     */
    public String getContent() {
        return ByteUtil.convertByteToString( this.content );
    }

    /**
     * Set content to the object
     *
     * @param content
     *         the content
     */
    public void setContent( String content ) {
        this.content = ByteUtil.convertStringToByte( content );
    }

    /**
     * Helper function to add OjbectJsonSchema to the list
     */
    public ObjectJsonSchemaEntity addObjectJsonSchema( ObjectJsonSchemaEntity objectJsonSchema ) {
        getObjectJsonSchemaChildList().add( objectJsonSchema );
        objectJsonSchema.setParentSchema( this );

        return objectJsonSchema;
    }

    /**
     * Helper function to remove OjbectJsonSchema from the list
     */
    public ObjectJsonSchemaEntity removeObjectJsonSchema( ObjectJsonSchemaEntity objectJsonSchema ) {
        getObjectJsonSchemaChildList().remove( objectJsonSchema );
        objectJsonSchema.setParentSchema( null );

        return objectJsonSchema;
    }

}