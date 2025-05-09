package de.soco.software.simuspace.suscore.common.model;

import java.util.HashMap;

/**
 * The type Metadata.
 */
public class DynamicQueryMetadata extends HashMap< String, DynamicQueryMetadata.Metadata > {

    public record Metadata( String title, String type ) {

    }

    /**
     * Sets type by column.
     *
     * @param columnName
     *         the column name
     * @param columnType
     *         the column type
     */
    public void setMetadataByColumn( String columnName, String columnType, String title ) {
        this.put( columnName, new Metadata( title, columnType ) );
    }

}
