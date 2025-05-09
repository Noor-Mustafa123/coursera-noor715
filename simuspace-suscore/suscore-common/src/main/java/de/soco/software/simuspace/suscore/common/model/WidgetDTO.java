package de.soco.software.simuspace.suscore.common.model;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Widget dto.
 */
@Getter
@Setter
@JsonIgnoreProperties( ignoreUnknown = true )
public class WidgetDTO implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -7788577617195285112L;

    /**
     * The Name.
     */
    private String name;

    /**
     * The View.
     */
    private String view;

    /**
     * The Title.
     */
    private String title;

    /**
     * The Connected.
     */
    private String connected;

    /**
     * The Auto.
     */
    private boolean auto;

}
