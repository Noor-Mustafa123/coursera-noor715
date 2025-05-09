package de.soco.software.simuspace.suscore.common.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The class BreadCrumbItemDTO store values for making bread crumb
 *
 * @author Zain ul Hassan
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties( ignoreUnknown = true )
public class BreadCrumbItemDTO implements Serializable {

    /**
     * Constant serialVersionUID
     */
    @Serial
    private static final long serialVersionUID = 260533707958521242L;

    private UUID itemId;

    /**
     * The BreadCrumbItemDTO Name.
     */
    private String name;

    /**
     * The BreadCrumbItemDTO Url.
     */
    private String url;

    /**
     * The BreadCrumbItemDTO Context.
     */
    private String context;

    /**
     * Default Constructor
     */
    public BreadCrumbItemDTO() {
        super();
    }

    /**
     * Constructor Using Fields
     *
     * @param name
     *         the name
     * @param url
     *         the url
     * @param context
     *         the context
     */
    public BreadCrumbItemDTO( String name, String url, String context ) {
        this.name = name;
        this.url = url;
        this.context = context;
    }

}