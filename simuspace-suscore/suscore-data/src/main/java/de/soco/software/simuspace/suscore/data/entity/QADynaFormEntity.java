package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.util.ByteUtil;

/**
 * The Class QADynaFormSchemaEntity.
 *
 * @author Ali Haider
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table( name = "qa_dyna_schema" )
public class QADynaFormEntity implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -2301690559529616600L;

    /**
     * The Id.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The name.
     */
    private String name;

    /**
     * The user id.
     */
    @Column( name = "user_id" )
    private UUID userId;

    @ManyToOne( fetch = FetchType.LAZY )
    private SuSEntity project;

    /**
     * content of the object_json_schema.
     */
    @Column( name = "content" )
    @Lob
    private byte[] content;

    /**
     * A full Constructor.
     *
     * @param id
     *         the id
     * @param content
     *         the content
     */
    public QADynaFormEntity( UUID id, String content ) {
        this.id = id;
        this.content = ByteUtil.convertStringToByte( content );
    }

    /**
     * Gets the content.
     *
     * @return contents of the object
     */
    public String getContent() {
        return ByteUtil.convertByteToString( this.content );
    }

    /**
     * Set content to the object.
     *
     * @param content
     *         the new content
     */
    public void setContent( String content ) {
        this.content = ByteUtil.convertStringToByte( content );
    }

}
