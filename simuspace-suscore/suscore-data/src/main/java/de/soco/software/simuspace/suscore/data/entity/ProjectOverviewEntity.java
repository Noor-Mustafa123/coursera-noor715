package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
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
 * The type Project overview entity.
 */
@Getter
@Setter
@Entity
@Indexed( index = "ProjectOverviewEntity" )
public class ProjectOverviewEntity extends ContainerEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 13701651412L;

    /**
     * The Constant CLASS_ID.
     */
    public static final UUID CLASS_ID = UUID.fromString( "2a6c4489-2e64-48f7-8524-3aa7064af5d4" );

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

    /**
     * The attachments.
     */
    @OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JoinTable( name = "project_attachments" )
    private Set< DocumentEntity > attachments = new HashSet<>();

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
        ProjectOverviewEntity that = ( ProjectOverviewEntity ) o;
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
