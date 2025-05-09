package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
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
 * The type Homepage widget entity.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table( name = "user_widget" )
public class UserWidgetEntity implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -9072516948256314058L;

    @Id
    private UUID id;

    /**
     * The Widget type.
     */
    private String widgetType;

    /**
     * The Order num.
     */
    private Integer orderNum;

    /**
     * The Size.
     */
    private String size;

    /**
     * The Widget category.
     */
    private String widgetCategory;

    @ManyToOne( fetch = FetchType.LAZY )
    private UserEntity userEntity;

    @Lob
    private byte[] configuration;

    @ToString.Exclude
    @ManyToOne( fetch = FetchType.LAZY )
    private UserEntity createdBy;

    /**
     * The Modified by.
     */
    @ToString.Exclude
    @ManyToOne( fetch = FetchType.LAZY )
    private UserEntity modifiedBy;

    private Date createdOn;

    /**
     * The Modified on.
     */
    private Date modifiedOn;

}
