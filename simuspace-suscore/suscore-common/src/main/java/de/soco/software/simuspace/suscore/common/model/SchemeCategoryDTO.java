package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.enums.simflow.SchemeCategoryEnum;

/**
 * The Class SchemeCategoryDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class SchemeCategoryDTO {

    /**
     * The id.
     */
    private int id;

    /**
     * The name.
     */
    private String name;

    public SchemeCategoryDTO() {
    }

    public SchemeCategoryDTO( int id, String name ) {
        super();
        this.id = id;
        this.name = name;
    }

    public SchemeCategoryDTO( SchemeCategoryEnum schemeCategoryEnum ) {
        super();
        this.id = schemeCategoryEnum.getKey();
        this.name = schemeCategoryEnum.getValue();
    }

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
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( int id ) {
        this.id = id;
    }

}
