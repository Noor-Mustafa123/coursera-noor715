package de.soco.software.simuspace.suscore.system.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * The type System dto.
 */
@Getter
@Setter
public class SystemDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7130152346485287788L;

    /**
     * The Directories list.
     */
    Map< String, Long > directoriesList;

    /**
     * The Rights list.
     */
    Map< String, Long > rightsList;

    /**
     * The Locations list.
     */
    Map< String, Object > locationsList;

    /**
     * The Users list.
     */
    Map< String, Object > usersList;

    /**
     * The Audit logs list.
     */
    Map< String, Long > auditLogsList;

    /**
     * The License module list.
     */
    Map< String, Object > licenseModuleList;

}
