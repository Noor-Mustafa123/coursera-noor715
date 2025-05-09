package de.soco.software.simuspace.server.model;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Algorithms dto.
 */
@Setter
@Getter
public class AlgorithmDTO {

    /**
     * The Training algos list.
     */
    Map< String, Object > trainingAlgosList;

    /**
     * The Wf schemes list.
     */
    Map< String, Object > wfSchemesList;

}
