/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class UserUrlEntity to save user urls.
 *
 * @author Ali Haider
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table
@Entity( name = "user_url" )
public class UserUrlEntity extends SystemEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -3618749053414182248L;

    /**
     * The path.
     */
    @Column( name = "path" )
    private String path;

    /**
     * The label.
     */
    @Column( name = "label" )
    private String label;

    /**
     * The bookmark.
     */
    @Column( name = "bookmark" )
    private int bookmark;

    /**
     * The location id.
     */
    @Column( name = "location_id" )
    private UUID locationId;

    /**
     * Instantiates a new user url entity.
     *
     * @param id
     *         the id
     * @param userEntity
     *         the user entity
     * @param path
     *         the path
     * @param label
     *         the label
     * @param bookmark
     *         the bookmark
     */
    public UserUrlEntity( final String path, final String label, final int bookmark ) {
        super();
        this.path = path;
        this.label = label;
        this.bookmark = bookmark;
    }

}
