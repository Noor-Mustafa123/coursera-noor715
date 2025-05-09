package de.soco.software.simuspace.suscore.object.manager;

import javax.persistence.EntityManager;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.data.entity.DashboardWidgetEntity;
import de.soco.software.simuspace.suscore.data.entity.DataDashboardEntity;
import de.soco.software.simuspace.suscore.data.entity.DataSourceEntity;
import de.soco.software.simuspace.suscore.data.model.DashboardWidgetDTO;
import de.soco.software.simuspace.suscore.data.model.DataDashboardDTO;
import de.soco.software.simuspace.suscore.data.model.DataSourceDTO;
import de.soco.software.simuspace.suscore.data.model.DatabaseDataSourceDTO;

/**
 * The interface Data dashboard manager.
 */
public interface DataDashboardManager {

    /**
     * Gets dashboard permission.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     *
     * @return the dashboard permission
     */
    boolean getDashboardPermission( String token, String objectId );

    /**
     * Test connection.
     *
     * @param token
     *         the token
     * @param dataSourceDTO
     *         the data source dto
     *
     * @return the boolean
     */
    boolean testConnection( String token, DatabaseDataSourceDTO dataSourceDTO );

    /**
     * Gets data source preview.
     *
     * @param token
     *         the token
     * @param dataSourceDTO
     *         the data source dto
     *
     * @return the database preview
     */
    Object getDataSourcePreview( String objectId, String token, String dataSourceDTO );

    /**
     * Prepare data dashboard dto data dashboard dto.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     * @param dashboardEntity
     *         the dashboard entity
     * @param setCustomAttributes
     *         the set custom attributes
     *
     * @return the data dashboard dto
     */
    DataDashboardDTO prepareDataDashboardDTO( EntityManager entityManager, UUID id, DataDashboardEntity dashboardEntity,
            boolean setCustomAttributes );

    /**
     * Create object form dashboard data source map.
     *
     * @param userId
     *         the user id
     * @param dataSource
     *         the data source
     *
     * @return the map
     */
    UIForm createObjectFormDashboardDataSource( String userId, String objectId, String dataSource );

    /**
     * Gets data source form for dashboard edit.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param dataSource
     *         the data source
     *
     * @return the data source form for dashboard edit
     */
    UIForm getDataSourceFormForDashboardEdit( String userId, String objectId, String dataSource );

    /**
     * Gets widget types field.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     *
     * @return the widget types field
     */
    UIForm getWidgetTypesField( String token, String objectId );

    /**
     * Gets widget form by type.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget type
     *
     * @return the widget form by type
     */
    UIForm getWidgetFormByType( String token, String objectId, String widgetId );

    /**
     * Gets fields for data source or transformation.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget type
     * @param sourceType
     *         the widget source
     *
     * @return the fields for data source or transformation
     */
    UIForm getFieldForDataSourceType( String token, String objectId, String widgetId, String sourceType );

    /**
     * Gets field for data source.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param sourceType
     *         the source type
     * @param dataSourceId
     *         the data source id
     *
     * @return the field for data source
     */
    UIForm getFieldForDataSource( String token, String objectId, String widgetId, String sourceType, String dataSourceId );

    /**
     * Gets schema list field by data source.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param dataSourceId
     *         the data source id
     *
     * @return the schema list field by data source
     */
    List< SelectOptionsUI > getSchemaListFieldByDataSource( String token, String objectId, String widgetId, String dataSourceId );

    /**
     * Gets tables list field by data source and schema.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param schema
     *         the schema name
     *
     * @return the tables list field by data source and schema
     */
    List< SelectOptionsUI > getTablesListFieldByDataSourceAndSchema( String token, String objectId, String widgetId, String schema );

    /**
     * Gets data sources list by dashboard id.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     *
     * @return the data sources list by dashboard id
     */
    List< DataSourceDTO > getDataSourcesListByDashboardId( String token, String objectId );

    /**
     * Add data source to dashboard data source dto.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param dataSourceJson
     *         the data source json
     *
     * @return the data source dto
     */
    DataSourceDTO addDataSourceToDashboard( String token, String objectId, String dataSourceJson );

    /**
     * Update data source data source dto.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param dataSourceId
     *         the data source id
     * @param dataSourceJson
     *         the data source json
     *
     * @return the data source dto
     */
    DataSourceDTO updateDataSource( String token, String objectId, String dataSourceId, String dataSourceJson );

    /**
     * Delete data source boolean.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param dataSourceId
     *         the data source id
     *
     * @return the boolean
     */
    Boolean deleteDataSource( String token, String objectId, String dataSourceId );

    /**
     * Create data source form map.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     *
     * @return the map
     */
    UIForm createDataSourceForm( String token, String objectId );

    /**
     * Edit data source form map.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param dataSourceId
     *         the data source id
     *
     * @return the map
     */
    UIForm editDataSourceForm( String token, String objectId, String dataSourceId );

    /**
     * Gets data source by id.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param dataSourceId
     *         the data source id
     *
     * @return the data source by id
     */
    DataSourceDTO getDataSourceById( String token, String objectId, String dataSourceId );

    /**
     * Gets columns list field by data source and schema and table.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param sourceType
     *         the source type
     * @param table
     *         the table
     *
     * @return the columns list field by data source and schema and table
     */
    List< SelectOptionsUI > getColumnsListFieldByDataSourceAndSchemaAndTable( String token, String objectId, String widgetId,
            String sourceType, String table );

    /**
     * Gets filter columns by data source and schema and table.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param table
     *         the table
     *
     * @return the filter columns by data source and schema and table
     */
    TableUI getFilterColumnsByDataSourceAndSchemaAndTable( String token, String objectId, String table );

    /**
     * Gets widgets list by dashboard id.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     *
     * @return the widgets list by dashboard id
     */
    List< DashboardWidgetDTO > getWidgetsListByDashboardId( String token, String objectId );

    /**
     * Add widget to dashboard dashboard widget dto.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetJson
     *         the widget json
     *
     * @return the dashboard widget dto
     */
    DashboardWidgetDTO addWidgetToDashboard( String token, String objectId, String widgetJson );

    /**
     * Update widget dashboard widget dto.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param widgetJson
     *         the widget json
     *
     * @return the dashboard widget dto
     */
    DashboardWidgetDTO updateWidget( String token, String objectId, String widgetId, String widgetJson );

    /**
     * Delete widget boolean.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     *
     * @return the boolean
     */
    Boolean deleteWidget( String token, String objectId, String widgetId );

    /**
     * Gets widget result set.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetJson
     *         the widget json
     * @param pythonMode
     *         the python mode
     *
     * @return the widget result set
     */
    Object getWidgetResultSet( String token, String objectId, String widgetJson, String pythonMode );

    /**
     * Gets python script field.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param pythonOutput
     *         the python output
     *
     * @return the python script field
     */
    UIForm getWidgetPythonScriptField( String token, String objectId, String pythonOutput );

    /**
     * Gets widget python fields.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param scriptOption
     *         the script option
     *
     * @return the widget python fields
     */
    UIForm getWidgetPythonFields( String token, String objectId, String scriptOption );

    /**
     * Gets widget python input preview.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetJson
     *         the widget json
     *
     * @return the widget python input preview
     */
    String getWidgetPythonInputPreview( String token, String objectId, String widgetJson );

    /**
     * Gets widget python output preview.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetJson
     *         the widget json
     *
     * @return the widget python output preview
     */
    Object getWidgetPythonOutputPreview( String token, String objectId, String widgetJson );

    /**
     * Run widget python process result.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetJson
     *         the widget json
     *
     * @return the process result
     */
    ProcessResult runWidgetPython( String token, String objectId, String widgetJson );

    /**
     * Gets applicable aggregate methods.
     *
     * @param tokenFromGeneralHeader
     *         the token from general header
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param groupBy
     *         the group by
     *
     * @return the applicable aggregate methods
     */
    List< SelectOptionsUI > getApplicableAggregateMethods( String tokenFromGeneralHeader, String objectId, String widgetId,
            String groupBy );

    /**
     * Create copies of data sources for new version list.
     *
     * @param dataSources
     *         the data sources
     * @param updatedEntity
     *         the version id
     * @param newVersionId
     *         the new version id
     *
     * @return the list
     */
    List< DataSourceEntity > createCopiesOfDataSourcesForNewVersion( List< DataSourceEntity > dataSources,
            DataDashboardEntity updatedEntity, int newVersionId );

    /**
     * Create copies of widgets for new version list.
     *
     * @param widgets
     *         the data sources
     * @param updatedEntity
     *         the version id
     * @param newVersionId
     *         the new version id
     *
     * @return the list
     */
    List< DashboardWidgetEntity > createCopiesOfWidgetsForNewVersion( List< DashboardWidgetEntity > widgets,
            DataDashboardEntity updatedEntity, int newVersionId );

    /**
     * Gets widget options form.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param pythonMode
     *         the python mode
     *
     * @return the widget options form
     */
    UIForm getWidgetOptionsForm( String token, String objectId, String widgetId, String pythonMode );

    /**
     * Gets widget filters.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param pythonMode
     *         the python mode
     *
     * @return the widget filters
     */
    List< TableColumn > getWidgetFilters( String token, String objectId, String widgetId, String pythonMode );

    /**
     * Gets other data source fields.
     *
     * @param tokenFromGeneralHeader
     *         the token from general header
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param otherOption
     *         the other option
     *
     * @return the other data source fields
     */
    UIForm getOtherDataSourceFields( String tokenFromGeneralHeader, String objectId, String widgetId, String otherOption );

    /**
     * Gets select options for material inspector.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param fieldName
     *         the field name
     * @param fieldValue
     *         the field value
     *
     * @return the select options for material inspector
     */
    List< SelectOptionsUI > getSelectOptionsForMaterialInspector( String token, String objectId, String widgetId, String fieldName,
            String fieldValue );

    /**
     * Gets select options for metal base.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param fieldName
     *         the field name
     * @param fieldValue
     *         the field value
     *
     * @return the select options for metal base
     */
    List< SelectOptionsUI > getSelectOptionsForMetalBase( String token, String objectId, String widgetId, String fieldName,
            String fieldValue );

    /**
     * Gets dynamic fields for material inspector.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param fieldName
     *         the field name
     * @param fieldValue
     *         the field value
     *
     * @return the dynamic fields for material inspector
     */
    UIForm getDynamicFieldsForMaterialInspector( String token, String objectId, String widgetId, String fieldName, String fieldValue );

    /**
     * Gets dynamic fields for metal base.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param fieldName
     *         the field name
     * @param fieldValue
     *         the field value
     *
     * @return the dynamic fields for metal base
     */
    UIForm getDynamicFieldsForMetalBase( String token, String objectId, String widgetId, String fieldName, String fieldValue );

    /**
     * Perform widget action object.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param action
     *         the action
     * @param data
     *         the data
     *
     * @return the object
     */
    Object performWidgetAction( String token, String objectId, String widgetId, String action, String data );

    /**
     * Gets download file path.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param action
     *         the action
     * @param token
     *         the token
     *
     * @return the download file path
     */
    Path getDownloadFilePath( String objectId, String widgetId, String action, String token );

    /**
     * Add widget to group dashboard widget dto.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param groupId
     *         the group id
     * @param widgetId
     *         the widget id
     *
     * @return the dashboard widget dto
     */
    DashboardWidgetDTO addWidgetToGroup( String token, String objectId, String groupId, String widgetId );

    /**
     * Remove widget from group dashboard widget dto.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param groupId
     *         the group id
     * @param widgetId
     *         the widget id
     *
     * @return the dashboard widget dto
     */
    DashboardWidgetDTO removeWidgetFromGroup( String token, String objectId, String groupId, String widgetId );

    /**
     * Gets widgets in group.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param groupId
     *         the group id
     *
     * @return the widgets in group
     */
    List< DashboardWidgetDTO > getWidgetsInGroup( String token, String objectId, String groupId );

    /**
     * Un group widgets boolean.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param groupId
     *         the group id
     *
     * @return the boolean
     */
    Boolean unGroupWidgets( String token, String objectId, String groupId );

    /**
     * Gets line widget options by type.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param lineWidgetType
     *         the line widget type
     *
     * @return the line widget options by type
     */
    UIForm getLineWidgetOptionsByType( String token, String objectId, String widgetId, String lineWidgetType );

    /**
     * Gets chart options field for mix chart.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     *
     * @return the chart options field for mix chart
     */
    UIForm getCurveOptionsFieldForMixChart( String token, String objectId, String widgetId );

    /**
     * Gets value options field for radar chart.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     *
     * @return the value options field for radar chart
     */
    UIForm getValueOptionsFieldForRadarChart( String token, String objectId, String widgetId );

    /**
     * Gets chart options related fields for mix chart.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param option
     *         the option
     *
     * @return the chart options related fields for mix chart
     */
    UIForm getCurveOptionsRelatedFieldsForMixChart( String token, String objectId, String widgetId, String option );

    /**
     * Gets text input fields.
     *
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param textInputMethod
     *         the text input method
     *
     * @return the text input fields
     */
    UIForm getTextInputFields( String token, String objectId, String widgetId, String textInputMethod );

    Boolean updateDataSourceCache( String token, String objectId, String sourceId );

    TableUI getTableWidgetUi( String token, String objectId, String widgetId );

    FilteredResponse< Map< String, Object > > getListForTableWidget( String token, String objectId, String widgetId, String filterJson );

}