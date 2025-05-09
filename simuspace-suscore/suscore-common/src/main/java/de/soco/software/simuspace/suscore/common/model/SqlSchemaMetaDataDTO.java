package de.soco.software.simuspace.suscore.common.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Sql schema meta data dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SqlSchemaMetaDataDTO implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -460856973823637540L;

    /**
     * The Name.
     */
    private String name;

    /**
     * The Tables.
     */
    private List< SqlTableMetadataDTO > tables;

}
