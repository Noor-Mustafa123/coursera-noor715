package de.soco.software.simuspace.suscore.data.common.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;

/**
 * The type Su s object cache dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuSObjectCacheDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -5163921878776264022L;

    /**
     * The Type id.
     */
    private UUID typeId;

    /**
     * The Type.
     */
    private String type;

    /**
     * The Id.
     */
    private UUID id;

    /**
     * The Name.
     */
    private String name;

    /**
     * The Description.
     */
    private String description;

    /**
     * The Created on.
     */
    private Date createdOn;

    /**
     * The Modified on.
     */
    private Date modifiedOn;

    /**
     * The Life cycle status.
     */
    private StatusDTO lifeCycleStatus;

    /**
     * The Created by.
     */
    private UserDTO createdBy;

    /**
     * The Modified by.
     */
    private UserDTO modifiedBy;

    /**
     * The Link.
     */
    private String link;

    /**
     * The Size.
     */
    private String size;

    /**
     * The Auto deleted.
     */
    private boolean autoDeleted;

    /**
     * The Version.
     */
    private VersionDTO version;

    /**
     * The Url type.
     */
    private String urlType;

    /**
     * The Icon.
     */
    private String icon;

    /**
     * The Url.
     */
    private String url;

    /**
     * The Path.
     */
    private String path;

}
