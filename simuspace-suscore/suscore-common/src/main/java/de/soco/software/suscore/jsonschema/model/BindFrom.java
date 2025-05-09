package de.soco.software.suscore.jsonschema.model;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class BindFrom.
 *
 * @author noman arshad
 */
@Setter
@Getter
public class BindFrom {

    /**
     * The Url.
     */
    private String url;

    /**
     * Instantiates a new Bind from.
     */
    public BindFrom() {
        super();
    }

    /**
     * Instantiates a new bind from.
     *
     * @param url
     *         the url
     */
    public BindFrom( String url ) {
        super();
        this.url = url;
    }

}
