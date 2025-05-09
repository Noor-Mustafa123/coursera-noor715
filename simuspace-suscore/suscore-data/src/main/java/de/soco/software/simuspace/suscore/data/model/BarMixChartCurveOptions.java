package de.soco.software.simuspace.suscore.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Bar 2 d chart options.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode( callSuper = true )
@JsonIgnoreProperties( ignoreUnknown = true )
public class BarMixChartCurveOptions extends MixChartCurveOptions {

}
