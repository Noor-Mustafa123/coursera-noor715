package de.soco.software.simuspace.suscore.data.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import de.soco.software.simuspace.suscore.common.enums.DashboardEnums;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.data.entity.CSVDataSourceEntity;
import de.soco.software.simuspace.suscore.data.entity.DataDashboardEntity;
import de.soco.software.simuspace.suscore.data.entity.DataSourceEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The type Csv data source dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode( callSuper = true )
@JsonIgnoreProperties( ignoreUnknown = true )
public class CSVDataSourceDTO extends DataSourceDTO {

    /**
     * The File selection.
     */
    @UIFormField( name = "fileSelection", title = "3000259x4", type = "server-file-explorer", orderNum = 20, section = "Data Source Settings" )
    private String fileSelection;

    /**
     * The File path.
     */
    private String filePath;

    /**
     * The Delimiter.
     */
    @UIFormField( name = "delimiter", title = "3000260x4", type = "select", orderNum = 21, section = "Data Source Settings" )
    private String delimiter;

    /**
     * Prepare entity data source entity.
     *
     * @param dashboardEntity
     *         the dashboard entity
     * @param userEntity
     *         the user entity
     *
     * @return the data source entity
     */
    @Override
    public DataSourceEntity prepareEntity( DataDashboardEntity dashboardEntity, UserEntity userEntity ) {
        if ( DashboardEnums.DashboardDataSourceOptions.CSV.getId().equalsIgnoreCase( getSourceType() ) ) {
            CSVDataSourceEntity csvDataSource = new CSVDataSourceEntity();
            csvDataSource.setCompositeId( new VersionPrimaryKey( UUID.randomUUID(), dashboardEntity.getComposedId().getVersionId() ) );
            SelectionEntity selectionEntity = new SelectionEntity();
            selectionEntity.setId( UUID.fromString( getFileSelection() ) );
            csvDataSource.setSelectionEntity( selectionEntity );
            csvDataSource.setFilePath( getFilePath() );
            csvDataSource.setDelimiter( getDelimiter() );
            super.prepareEntity( dashboardEntity, userEntity, csvDataSource );
            return csvDataSource;
        }
        return null;
    }

}
