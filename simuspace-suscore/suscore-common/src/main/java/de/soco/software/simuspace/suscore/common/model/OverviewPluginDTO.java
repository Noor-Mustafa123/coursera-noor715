package de.soco.software.simuspace.suscore.common.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * The type Overview plugin dto.
 */
public class OverviewPluginDTO implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 3718190932818012964L;

    /**
     * The Id.
     */
    private String id;

    /**
     * The Name.
     */
    private String name;

    /**
     * The Languages.
     */
    private List< String > languages;

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name
     *         the name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets languages.
     *
     * @return the languages
     */
    public List< String > getLanguages() {
        return languages;
    }

    /**
     * Sets languages.
     *
     * @param languages
     *         the languages
     */
    public void setLanguages( List< String > languages ) {
        this.languages = languages;
    }

    /**
     * Sets id.
     *
     * @param id
     *         the id
     */
    public void setId( String id ) {
        this.id = id;
    }

}
