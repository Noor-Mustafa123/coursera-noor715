package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import java.io.Serial;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import lombok.Getter;
import lombok.Setter;

/**
 * Database Entity Mapping Class for project.
 *
 * @author Nosheen.Sharif
 */
@Getter
@Setter
@Entity
@Indexed( index = "ProjectEntity" )
public class ProjectEntity extends ContainerEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The Constant CLASS_ID.
     */
    public static final UUID CLASS_ID = UUID.fromString( "d8c36d5a-f7b3-4978-8940-a73550895c59" );

    /**
     * The type.
     */
    private String type;

    /**
     * The Html json.
     */
    @Column( name = "htmlJson" )
    @Lob
    private byte[] htmlJson;

    /**
     * The encoding.
     */
    @Column( name = "encoding" )
    private String encoding;

    /**
     * The file.
     */
    @OneToOne
    @JoinColumns( @JoinColumn( name = "document_id", referencedColumnName = "id" ) )
    private DocumentEntity file;

    @Column( unique = false )
    @ManyToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JoinTable( name = "dataobject_attachments", joinColumns = { @JoinColumn( name = "dataobjectentity_id", referencedColumnName = "id" ),
            @JoinColumn( name = "dataobjectentity_version_id", referencedColumnName = "version_id" ) } )
    private Set< DocumentEntity > attachments = new HashSet<>();

    /**
     * No-argument constructor.
     */
    public ProjectEntity() {
        super();
    }

    /**
     * Constructor to set ID.
     *
     * @param composedId
     *         the composed id
     */
    public ProjectEntity( VersionPrimaryKey composedId ) {
        super.setComposedId( composedId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        if ( !super.equals( o ) ) {
            return false;
        }
        ProjectEntity that = ( ProjectEntity ) o;
        return Arrays.equals( getHtmlJson(), that.getHtmlJson() ) && Objects.equals( getEncoding(), that.getEncoding() )
                && Objects.equals( getFile(), that.getFile() ) && Objects.equals( getAttachments(), that.getAttachments() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = Objects.hash( super.hashCode(), getEncoding(), getFile(), getAttachments() );
        result = 31 * result + Arrays.hashCode( getHtmlJson() );
        return result;
    }

}
