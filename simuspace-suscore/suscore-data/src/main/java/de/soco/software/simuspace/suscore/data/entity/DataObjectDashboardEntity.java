package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import java.io.Serial;
import java.util.UUID;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class DataObjectDashboardEntity.
 *
 * @author Ali Haider
 */
@Getter
@Setter
@Entity
@Indexed
@Deprecated( since = "soco/2.3.1/release", forRemoval = true )
public class DataObjectDashboardEntity extends DataObjectEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The Constant CLASS_ID.
     */
    public static final UUID CLASS_ID = UUID.fromString( "8062c46e-58c1-11ec-bf63-0242ac130002" );

    /**
     * The Selection.
     */
    @OneToOne
    private SelectionEntity selection;

    /**
     * The settings.
     */
    @Column( name = "settings" )
    @Lob
    private byte[] settings;

    /**
     * The plugin.
     */
    private String plugin;

}
