package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;

/**
 * The Class SchemeOptionSchemaEntity.
 *
 * @author Ali Haider
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table( name = "scheme_option_schema" )
public class SchemeSchemaEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -1557123208566206137L;

    /**
     * id of the object_json_schema.
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

    /**
     * content of the object_json_schema.
     */
    @Column( name = "content" )
    @Lob
    private byte[] content;

    /**
     * workflow can have custom attributes.
     */
    @ManyToOne( optional = false, fetch = FetchType.EAGER )
    @JoinColumns( value = { @JoinColumn( name = "workflow_id", referencedColumnName = "id" ),
            @JoinColumn( name = "workflow_version_id", referencedColumnName = "version_id" ) } )
    private WorkflowEntity workflow;

    /**
     * A full Constructor.
     *
     * @param id
     *         the id
     * @param content
     *         the content
     */
    public SchemeSchemaEntity( UUID id, String content ) {
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
