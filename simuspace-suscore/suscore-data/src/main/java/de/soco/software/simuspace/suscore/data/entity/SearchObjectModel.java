package de.soco.software.simuspace.suscore.data.entity;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class SearchObjectType.
 *
 * @author Ali Haider
 */
@Getter
@Setter
public class SearchObjectModel {

    /**
     * The id.
     */
    private UUID id;

    /**
     * The config.
     */
    private String config;

    /**
     * The class name.
     */
    private String className;

}
