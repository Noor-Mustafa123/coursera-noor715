package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Data dashboard entity.
 */
@Getter
@Setter
@Entity
@Indexed
public class DataDashboardEntity extends SuSEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1450076461451048367L;

    /**
     * The Constant CLASS_ID.
     */
    public static final UUID CLASS_ID = UUID.fromString( "8062c46e-58c1-11ec-bf63-0242ac130002" );

    /**
     * The Data sources.
     */
    //owner of the relation
    @OneToMany( fetch = FetchType.LAZY, mappedBy = "dashboard", cascade = CascadeType.ALL, orphanRemoval = true )
    private List< DataSourceEntity > dataSources;

    /**
     * The Widgets.
     */
    //owner of the relation
    @OneToMany( fetch = FetchType.LAZY, mappedBy = "dashboard", cascade = CascadeType.ALL, orphanRemoval = true )
    private List< DashboardWidgetEntity > widgets;

}
