package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;

import java.io.Serial;

import lombok.Getter;
import lombok.Setter;

/**
 * Database Entity Mapping Class for project
 *
 * @author Nosheen.Sharif
 */
@Getter
@Setter
@Entity
public class SystemContainerEntity extends ContainerEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

}
