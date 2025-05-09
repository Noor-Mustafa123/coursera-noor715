package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The type Dashboard widget entity.
 */
@Getter
@Setter
@ToString
@Entity
@Table( name = "dashboard_widgets" )
@NoArgsConstructor
public class DashboardWidgetEntity implements Versionable, Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -1610173627133260711L;

    /**
     * id of the object. versioning composite primary key reference
     */
    @EmbeddedId
    private VersionPrimaryKey compositeId;

    /**
     * The Type.
     */
    private String widgetType;

    /**
     * The Name.
     */
    private String name;

    /**
     * The Created by.
     */
    @ToString.Exclude
    @ManyToOne( fetch = FetchType.LAZY )
    private UserEntity createdBy;

    /**
     * The Modified by.
     */
    @ToString.Exclude
    @ManyToOne( fetch = FetchType.LAZY )
    private UserEntity modifiedBy;

    /**
     * The Created on.
     */
    private Date createdOn;

    /**
     * The Modified on.
     */
    private Date modifiedOn;

    /**
     * The Is delete.
     */
    private boolean isDelete;

    /**
     * The Deleted on.
     */
    private Date deletedOn;

    /**
     * The Dashboard.
     */
    @ToString.Exclude
    @ManyToOne( fetch = FetchType.LAZY )
    private DataDashboardEntity dashboard;

    /**
     * The Configuration.
     */
    @Lob
    private byte[] configuration;

    private Integer relation = 0;

    /**
     * Gets id.
     *
     * @return the id
     */
    @Override
    public UUID getId() {
        if ( compositeId != null ) {
            return compositeId.getId();
        }
        return null;
    }

    /**
     * Sets id.
     *
     * @param id
     *         the id
     */
    @Override
    public void setId( UUID id ) {
        if ( compositeId == null ) {
            compositeId = new VersionPrimaryKey();
        }
        compositeId.setId( id );
    }

    /**
     * Gets version id.
     *
     * @return the version id
     */
    @Override
    public int getVersionId() {
        return compositeId.getVersionId();
    }

    /**
     * Sets version id.
     *
     * @param id
     *         the id
     */
    @Override
    public void setVersionId( int id ) {
        compositeId.setVersionId( id );
    }

}
