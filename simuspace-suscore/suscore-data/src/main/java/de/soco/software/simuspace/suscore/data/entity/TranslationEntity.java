package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class TranslationEntity.
 *
 * @author Ali Haider
 */
@Getter
@Setter
@Entity
@Table( name = "translation" )
public class TranslationEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 7909941192135381985L;

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
    @Column( name = "language" )
    private String language;

    /**
     * The file.
     */
    @OneToOne
    @JoinColumns( @JoinColumn( name = "document_id", referencedColumnName = "id" ) )
    DocumentEntity file;

    /**
     * The translated attachments
     */
    @ManyToMany( fetch = FetchType.LAZY )
    @JoinTable( name = "translation_attachments" )
    private Set< DocumentEntity > attachments = new HashSet<>();

    /**
     * The susentity.
     */
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinTable( name = "susEntity_translation", joinColumns = {
            @JoinColumn( name = "translation_id", insertable = false, updatable = false, referencedColumnName = "id" ) }, inverseJoinColumns = {
            @JoinColumn( name = "susentity_id", insertable = false, updatable = false, referencedColumnName = "id" ),
            @JoinColumn( name = "susentity_version_id", insertable = false, updatable = false, referencedColumnName = "version_id" ) } )
    private SuSEntity susentity;

    /**
     * Instantiates a new translation entity.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param language
     *         the language
     * @param selected
     *         the selected
     * @param file
     *         the file
     */
    public TranslationEntity( String name, String language, DocumentEntity file ) {
        super();
        this.id = UUID.randomUUID();
        this.name = name;
        this.language = language;
        this.file = file;
    }

    /**
     * Instantiates a new translation entity.
     */
    public TranslationEntity() {
        super();
    }

    /**
     * Gets the sus entity.
     *
     * @return the sus entity
     */
    public SuSEntity getSuSEntity() {
        return susentity;
    }

    /**
     * Sets the sus entity.
     *
     * @param susentity
     *         the new sus entity
     */
    public void setSuSEntity( SuSEntity susentity ) {
        this.susentity = susentity;
    }

}
