package de.soco.software.simuspace.suscore.common.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The type Dynamic query data.
 */
public class DynamicQueryData extends HashMap< String, List< Object > > {

    /**
     * Add value by column.
     *
     * @param columnName
     *         the column name
     * @param value
     *         the value
     */
    public void addValueByColumn( String columnName, Object value ) {
        if ( !keySet().contains( columnName ) ) {
            this.put( columnName, new ArrayList<>() );
        }
        this.get( columnName ).add( value );
    }

}
