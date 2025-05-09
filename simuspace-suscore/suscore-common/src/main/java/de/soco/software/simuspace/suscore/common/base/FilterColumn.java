/*
 *
 */

package de.soco.software.simuspace.suscore.common.base;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * The Class FilterColumn which is mapped to filters json sent by the front end to render UI columns and filter records.
 *
 * @author Zeeshan jamal
 */
@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties( ignoreUnknown = true )
public class FilterColumn implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The name.
     */
    private String name;

    /**
     * The visible.
     */
    private boolean visible;

    /**
     * The dir.
     */
    private String dir;

    /**
     * The filters.
     */
    private List< Filter > filters;

    /**
     * The reorder.
     */
    private int reorder;

    /**
     * The Type.
     */
    private String type;

}
