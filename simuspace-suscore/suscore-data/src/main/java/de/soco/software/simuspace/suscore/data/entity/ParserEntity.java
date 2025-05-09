package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import java.util.UUID;

import org.hibernate.annotations.Type;

import de.soco.software.simuspace.suscore.common.util.ByteUtil;

/**
 * The Class AbaqusSchema.
 *
 * @author noman arshad
 * @since 2.0
 */
@Entity
@Table( name = "parser_entity" )
public class ParserEntity {

    /**
     * The primary key.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The name.
     */
    @Column( name = "name" )
    private String name;

    /**
     * The name.
     */
    @Column( name = "type" )
    private String type;

    /**
     * The json schema.
     */
    @Column( name = "value_attribute" )
    @Lob
    private byte[] jsonSchema;

    /**
     * The form json.
     */
    @Column( name = "formJson" )
    @Lob
    private byte[] formJson;

    /**
     * Instantiates a new parser entity.
     */
    public ParserEntity() {

    }

    /**
     * Instantiates a new parser entity.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param type
     *         the type
     * @param jsonSchema
     *         the json schema
     */
    public ParserEntity( UUID id, String name, String type, String jsonSchema ) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
        this.jsonSchema = ByteUtil.convertStringToByte( jsonSchema );
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( UUID id ) {
        this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets the json schema.
     *
     * @return the json schema
     */
    public String getJsonSchemaAsString() {
        return ByteUtil.convertByteToString( this.jsonSchema );
    }

    /**
     * Sets the json schema.
     *
     * @param jsonSchema
     *         the new json schema
     */
    public void setJsonSchema( String jsonSchema ) {
        this.jsonSchema = ByteUtil.convertStringToByte( jsonSchema );
    }

    /**
     * Gets the json schema.
     *
     * @return the json schema
     */
    public byte[] getJsonSchema() {
        return this.jsonSchema;
    }

    /**
     * Sets the json schema.
     *
     * @param jsonSchema
     *         the new json schema
     */
    public void setJsonSchema( byte[] jsonSchema ) {
        this.jsonSchema = jsonSchema;
    }

    /**
     * Gets the form json.
     *
     * @return the form json
     */
    public String getFormJsonAsString() {
        return ByteUtil.convertByteToString( this.formJson );
    }

    /**
     * Sets the form json.
     *
     * @param formJson
     *         the new form json
     */
    public void setFormJson( String formJson ) {
        this.formJson = ByteUtil.convertStringToByte( formJson );
    }

    /**
     * Gets the form json.
     *
     * @return the form json
     */
    public byte[] getFormJson() {
        return this.formJson;
    }

    /**
     * Sets the form json.
     *
     * @param formJson
     *         the new form json
     */
    public void setFormJson( byte[] formJson ) {
        this.formJson = formJson;
    }

    /**
     * Copy.
     *
     * @return the parser entity
     */
    public ParserEntity copy() {
        ParserEntity copy = new ParserEntity();
        copy.setId( UUID.randomUUID() );
        copy.setName( getName() );
        copy.setType( getType() );
        copy.setFormJson( getFormJson() );
        copy.setJsonSchema( getJsonSchema() );
        return copy;
    }

}
