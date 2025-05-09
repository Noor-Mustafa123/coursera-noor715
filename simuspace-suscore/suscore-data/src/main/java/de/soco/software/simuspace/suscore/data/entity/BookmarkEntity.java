package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class BookmarkEntity.
 */
@Getter
@Setter
@Table
@Entity( name = "bookmark" )
public class BookmarkEntity extends SystemEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 9068198255932226977L;

    /**
     * The name.
     */
    @Column( name = "name" )
    private String name;

    /**
     * The url.
     */
    @Column( name = "url" )
    private String url;

    @Column( name = "type" )
    private String type;

}
