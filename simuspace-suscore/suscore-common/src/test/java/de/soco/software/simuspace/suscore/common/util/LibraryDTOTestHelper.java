package de.soco.software.simuspace.suscore.common.util;

import java.util.List;

/**
 * This is added for the purpose of testing invoke methods in {@linkplain ReflectionUtils}.
 *
 * @author M.Nasir.Farooq
 */
public class LibraryDTOTestHelper {

    /**
     * The name.
     */
    private String name;

    /**
     * The custom attrib.
     */
    private List< String > customAttrib;

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets the custom attrib.
     *
     * @return the custom attrib
     */
    public List< String > getCustomAttrib() {
        return customAttrib;
    }

    /**
     * Sets the custom attrib.
     *
     * @param customAttrib
     *         the new custom attrib
     */
    public void setCustomAttrib( List< String > customAttrib ) {
        this.customAttrib = customAttrib;
    }

    /**
     * Creates the library entity from library DTO.
     *
     * @return the library entity
     */
    public String prepareEntity( String userId ) {

        return getName();
    }

}
