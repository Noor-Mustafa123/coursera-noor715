package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import java.io.Serial;
import java.util.UUID;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class DataObjectHtmlsEntity.
 *
 * @author noman arshad
 */
@Getter
@Setter
@Entity
@Indexed
public class DataObjectHtmlsEntity extends DataObjectFileEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -2880422266891041796L;

    /**
     * The Constant CLASS_ID.
     */
    public static final UUID CLASS_ID = UUID.fromString( "6808d8ae-892d-4172-9bac-894d4be08791" );

    /**
     * The html.
     */

    @Column( name = "htmlJson" )
    @Lob
    private byte[] htmlJson;

}
