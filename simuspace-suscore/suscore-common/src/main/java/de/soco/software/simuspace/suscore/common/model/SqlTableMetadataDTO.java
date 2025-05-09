package de.soco.software.simuspace.suscore.common.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Sql table metadata dto.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SqlTableMetadataDTO implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -4477856041609407821L;

    /**
     * The Name.
     */
    private String name;

    /**
     * The Columns.
     */
    private List< SqlColumnMetaData > columns;

    /**
     * The type Sql column meta data.
     */
    public record SqlColumnMetaData( String name, String dataType ) {

    }

}
