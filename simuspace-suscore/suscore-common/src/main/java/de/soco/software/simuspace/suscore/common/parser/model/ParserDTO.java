package de.soco.software.simuspace.suscore.common.parser.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class ParserDTO.
 *
 * @author noman arshad
 * @since 2.0
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ParserDTO {

    /**
     * The part.
     */
    private List< ParserPartDTO > part;

    /**
     * The parameter.
     */
    private Map< String, String > parameter;

    /**
     * The selected entries.
     */
    private List< ParserPartDTO > selectedEntries;

    /**
     * Gets the part.
     *
     * @return the part
     */
    public List< ParserPartDTO > getPart() {
        return part;
    }

    /**
     * Sets the part.
     *
     * @param part
     *         the new part
     */
    public void setPart( List< ParserPartDTO > part ) {
        this.part = part;
    }

    /**
     * Gets the parameter.
     *
     * @return the parameter
     */
    public Map< String, String > getParameter() {
        return parameter;
    }

    /**
     * Sets the parameter.
     *
     * @param parameter
     *         the parameter
     */
    public void setParameter( Map< String, String > parameter ) {
        this.parameter = parameter;
    }

    /**
     * Gets the selected entries.
     *
     * @return the selected entries
     */
    public List< ParserPartDTO > getSelectedEntries() {
        return selectedEntries;
    }

    /**
     * Sets the selected entries.
     *
     * @param selectedEntries
     *         the new selected entries
     */
    public void setSelectedEntries( List< ParserPartDTO > selectedEntries ) {
        this.selectedEntries = selectedEntries;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "ParserDTO [part=" + part + ", parameter=" + parameter + ", selectedEntries=" + selectedEntries + "]";
    }

}
