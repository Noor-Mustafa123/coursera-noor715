package de.soco.software.simuspace.workflow.model.impl;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Select script payload.
 */
@Getter
@Setter
@JsonIgnoreProperties( ignoreUnknown = true )
@NoArgsConstructor
@AllArgsConstructor
public class SelectScriptPayload implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 4885960484465966828L;

    /**
     * The Field name.
     */
    private String fieldName;

    /**
     * The Input argument.
     */
    private List< Field > inputArgument;

}
