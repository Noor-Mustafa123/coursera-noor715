package de.soco.software.simuspace.suscore.data.utility;

import java.util.HashMap;
import java.util.Map;

import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * Utility class which will contain all the Qualified Name of Entity Classes of suscore
 *
 * @author Nosheen.Sharif
 */
public class ConstantsEntities {

    /**
     * The PROJECT_ENTITY_NAME constant.
     */
    public static final String PROJECT_ENTITY_NAME = "de.soco.software.simuspace.suscore.data.entity.ProjectEntity";

    /**
     * The VARIANT_ENTITY_NAME constant.
     */
    public static final String VARIANT_ENTITY_NAME = "de.soco.software.simuspace.suscore.data.entity.VariantEntity";

    /**
     * The index map.
     */
    public static Map< String, VersionPrimaryKey > indexMap = new HashMap<>();

    /**
     * Private Constructor to avoid Object Instantiation
     */
    private ConstantsEntities() {

    }

}
