package de.soco.software.simuspace.suscore.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.UIFormField;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@JsonIgnoreProperties( ignoreUnknown = true )
public abstract class WidgetSource {

    /**
     * The Data source or transformation.
     */

    @UIFormField( name = "sourceType", title = "3000238x4", type = "select", required = true )
    private String sourceType;

}

