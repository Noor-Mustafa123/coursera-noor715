package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A pojo class for mapping language data to json and back to class.
 *
 * @author Zeeshan jamal
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class LanguageDTO {

    /**
     * id
     */
    private String id;

    /**
     * name
     */
    private String name;

    /**
     * Instantiates a new language DTO.
     */
    public LanguageDTO() {
        super();
    }

    /**
     * Instantiates a new language DTO.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     */
    public LanguageDTO( String id, String name ) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *         the id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *         the name
     */
    public void setName( String name ) {
        this.name = name;
    }

}
