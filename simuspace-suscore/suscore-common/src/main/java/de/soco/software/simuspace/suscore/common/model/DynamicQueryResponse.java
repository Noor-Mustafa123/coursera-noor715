package de.soco.software.simuspace.suscore.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Dynamic query response.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DynamicQueryResponse {

    /**
     * The Metadata.
     */
    private DynamicQueryMetadata metadata;

    /**
     * The Data.
     */
    private DynamicQueryData data;

}
