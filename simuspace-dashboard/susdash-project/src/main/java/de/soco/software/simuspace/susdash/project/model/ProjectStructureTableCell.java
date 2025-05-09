package de.soco.software.simuspace.susdash.project.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The type Materials table cell.
 */
public class ProjectStructureTableCell {

    /**
     * Instantiates a new Materials table cell.
     *
     * @param name
     *         the name
     * @param url
     *         the url
     */
    public ProjectStructureTableCell( String name, String url ) {
        this.name = name;
        this.url = url;
    }

    /**
     * Instantiates a new Materials table cell.
     *
     * @param name
     *         the name
     */
    public ProjectStructureTableCell( String name ) {
        this.name = name;
    }

    public ProjectStructureTableCell() {

    }

    /**
     * The Name.
     */
    private String name;

    /**
     * The Url.
     */
    private String url;

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
     * Gets url.
     *
     * @return the url
     */
    @JsonInclude( Include.NON_NULL )
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url
     *         the url
     */
    public void setUrl( String url ) {
        this.url = url;
    }

}
