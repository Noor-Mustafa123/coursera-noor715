package de.soco.software.simuspace.suscore.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Search util.
 */
public class SearchUtil {

    /**
     * The type Name index pair.
     */
    public record NameIndexPair( String name, Integer index ) {

    }

    /**
     * The constant typeMap.
     */
    private static final Map< String, NameIndexPair > indexMap = new HashMap<>();

    /**
     * Gets type map.
     *
     * @return the type map
     */
    public static Map< String, NameIndexPair > getIndexMap() {
        return indexMap;
    }

    /**
     * Create name index pair name index pair.
     *
     * @param name
     *         the name
     * @param index
     *         the index
     *
     * @return the name index pair
     */
    public static NameIndexPair createNameIndexPair( String name, Integer index ) {
        return new NameIndexPair( name, index );
    }

}
