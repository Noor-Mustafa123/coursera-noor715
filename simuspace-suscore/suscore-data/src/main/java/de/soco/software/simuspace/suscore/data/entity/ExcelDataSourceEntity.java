package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The type Dashboard data source database.
 */
@Getter
@Setter
@ToString
@Entity
public class ExcelDataSourceEntity extends DataSourceEntity {

    /**
     * The Selection entity.
     */
    @OneToOne
    private SelectionEntity selectionEntity;

    /**
     * The File path.
     */
    private String filePath;

}



