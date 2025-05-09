package de.soco.software.simuspace.suscore.common.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Bind to.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties( ignoreUnknown = true )
public class BindTo {

    /**
     * The url.
     */
    private String url;

    /**
     * The params.
     */
    private Map< String, String > params = new HashMap<>();

    /**
     * The name.
     */
    private String name;

}
