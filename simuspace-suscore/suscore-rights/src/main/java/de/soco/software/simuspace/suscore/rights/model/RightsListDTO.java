package de.soco.software.simuspace.suscore.rights.model;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Rights list dto.
 */
@Getter
@Setter
public class RightsListDTO {

    /**
     * The Groups list.
     */
    Map< String, Object > groupsList;

    /**
     * The Roles list.
     */
    Map< String, Object > rolesList;

}