package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;

import java.io.Serial;
import java.util.Date;
import java.util.UUID;

/**
 * Container Entity class A base class for container type object.
 */
@Entity
public class ContainerEntity extends SuSEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1370165458533421412L;

    public ContainerEntity() {
    }

    public ContainerEntity( VersionPrimaryKey composedId, String lifeCycleStatus, String config, UUID typeId, Date createdOn,
            Date modifiedOn, UserEntity createdBy, UserEntity modifiedBy, String name, String description, Long entitySize, String icon,
            String selectedTranslations ) {
        super( composedId, lifeCycleStatus, config, typeId, createdOn, modifiedOn, createdBy, modifiedBy, name, description, entitySize,
                icon, selectedTranslations );
    }

}
