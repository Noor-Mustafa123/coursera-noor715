package de.soco.software.simuspace.suscore.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class ObjectsSubModelDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ObjectsSubModelDTO extends BmwCaeBenchSubModelDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 8415954906214289813L;

    /**
     * The loadcases.
     */
    private List< VariantSubModelDTO > loadcases;

    /**
     * The link.
     */
    private String link;

    /**
     * Gets the loadcases.
     *
     * @return the loadcases
     */
    public List< VariantSubModelDTO > getLoadcases() {
        return loadcases;
    }

    /**
     * Sets the loadcases.
     *
     * @param loadcases
     *         the new loadcases
     */
    public void setLoadcases( List< VariantSubModelDTO > loadcases ) {
        this.loadcases = loadcases;
    }

    /**
     * Gets the link.
     *
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the link.
     *
     * @param link
     *         the new link
     */
    public void setLink( String link ) {
        this.link = link;
    }

}
