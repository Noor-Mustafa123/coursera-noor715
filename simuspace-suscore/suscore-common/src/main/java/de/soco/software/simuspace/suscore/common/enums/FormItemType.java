package de.soco.software.simuspace.suscore.common.enums;

import java.util.Arrays;

import lombok.Getter;

/**
 * The enum Form item type.
 */
@Getter
public enum FormItemType {

    /**
     * Select form item type.
     */
    SELECT( new String[]{ "select" } ),

    /**
     * Image form item type.
     */
    IMAGE( new String[]{ "file" } ),

    /**
     * General form item type.
     */
    GENERAL( new String[]{} ),

    /**
     * Input table form item type.
     */
    INPUT_TABLE( new String[]{ "input-table" } ),

    /**
     * Section form item type.
     */
    SECTION( new String[]{ "section" } ),

    /**
     * Table form item type.
     */
    TABLE( new String[]{ "table", "connected-table" } ),

    /**
     * The Button.
     */
    BUTTON( new String[]{ "button" } ),

    /**
     * The Code.
     */
    CODE( new String[]{ "code" } );

    /**
     * The Keys.
     */
    private final String[] keys;

    /**
     * Instantiates a new Form item type.
     *
     * @param keys
     *         the keys
     */
    FormItemType( String[] keys ) {
        this.keys = keys;
    }

    /**
     * Has key boolean.
     *
     * @param key
     *         the key
     *
     * @return the boolean
     */
    public boolean hasKey( String key ) {
        return Arrays.asList( this.keys ).contains( key );
    }
}
