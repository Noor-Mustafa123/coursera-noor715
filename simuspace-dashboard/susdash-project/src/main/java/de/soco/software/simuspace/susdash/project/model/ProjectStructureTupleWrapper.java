package de.soco.software.simuspace.susdash.project.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Materials table dto.
 */
public class ProjectStructureTupleWrapper {

    /**
     * The Entry.
     */
    private Map< String, ProjectStructureTableCell > entry;

    /**
     * Instantiates a new Materials table dto.
     */
    public ProjectStructureTupleWrapper() {
        this.entry = new HashMap<>();
    }

    /**
     * Gets entry.
     *
     * @return the entry
     */
    public Map< String, ProjectStructureTableCell > getEntry() {
        return entry;
    }

    /**
     * Sets entry.
     *
     * @param entry
     *         the entry
     */
    public void setEntry( Map< String, ProjectStructureTableCell > entry ) {
        this.entry = entry;
    }

    /**
     * Put if absent.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    public void putIfAbsent( String key, ProjectStructureTableCell value ) {
        entry.putIfAbsent( key, value );
    }

    /**
     * Put.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    public void put( String key, ProjectStructureTableCell value ) {
        entry.put( key, value );
    }

    /**
     * Contains key boolean.
     *
     * @param key
     *         the key
     *
     * @return the boolean
     */
    public boolean containsKey( String key ) {
        return entry.containsKey( key );
    }

    /**
     * Get materials table cell.
     *
     * @param key
     *         the key
     *
     * @return the materials table cell
     */
    public ProjectStructureTableCell get( String key ) {
        return entry.get( key );
    }

    /**
     * Gets name by column name.
     *
     * @param columnName
     *         the column name
     *
     * @return the name by column name
     */
    public String getNameByColumnName( String columnName ) {
        var value = entry.get( columnName );
        if ( value != null ) {
            return value.getName();
        } else {
            return null;
        }
    }

}
