package de.soco.software.simuspace.suscore.common.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Widget category group dto.
 */
@Getter
@Setter
@JsonIgnoreProperties( ignoreUnknown = true )
public class WidgetCategoryGroupDTO implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -8215487157147553755L;

    /**
     * The Built in.
     */
    private List< WidgetDTO > builtIn;

    /**
     * The Preview.
     */
    private List< WidgetDTO > preview;

}
