package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * A database mapping class of language
 *
 * @author zeeshan jamal
 */
@Getter
@Setter
@Entity
@Table( name = "language" )
public class LanguageEntity {

    /**
     * The language uuid.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * name of the language locale
     */
    @Column
    private String name;

}
