package de.soco.software.simuspace.suscore.object.service.rest.impl;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.cxf.message.Message;

import lombok.Setter;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.model.DatabaseDataSourceDTO;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.object.manager.DataDashboardManager;
import de.soco.software.simuspace.suscore.object.service.rest.DataDashboardService;

/**
 * The type Data dashboard service.
 */
@Setter
public class DataDashboardServiceImpl extends SuSBaseService implements DataDashboardService {

    /**
     * The Data dashboard manager.
     */
    DataDashboardManager dataDashboardManager;

    /**
     * Gets config from object and data source.
     *
     * @param objectId
     *         the object id
     *
     * @return the config from object and data source
     */
    @Override
    public Response getDashboardPermission( String objectId ) {
        try {
            return ResponseUtils.success( dataDashboardManager.getDashboardPermission( getTokenFromGeneralHeader(), objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Test data source response.
     *
     * @param parametersJson
     *         the parameters json
     *
     * @return the response
     */
    @Override
    public Response testDataSource( String parametersJson ) {
        try {
            DatabaseDataSourceDTO parameters = JsonUtils.jsonToObject( parametersJson, DatabaseDataSourceDTO.class );
            if ( dataDashboardManager.testConnection( getTokenFromGeneralHeader(), parameters ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.EXTERNAL_DATABASE_CONNECTION_SUCCESS.getKey() ),
                        "" );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.FAILED_TO_CONNECT_EXTERNAL_DATABASE.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Preview data source response.
     *
     * @param parametersJson
     *         the parameters json
     *
     * @return the response
     */
    @Override
    public Response previewDataSource( String objectId, String parametersJson ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getDataSourcePreview( objectId, getTokenFromGeneralHeader(), parametersJson ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Create object form dashboard data source response.
     *
     * @param dataSource
     *         the data source
     *
     * @return the response
     */
    @Override
    public Response createObjectFormDashboardDataSource( String objectId, String dataSource ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.createObjectFormDashboardDataSource( getUserIdFromGeneralHeader(), objectId, dataSource ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets config from object and data source.
     *
     * @param objectId
     *         the object id
     * @param dataSource
     *         the data source
     *
     * @return the config from object and data source
     */
    @Override
    public Response getConfigFromObjectAndDataSource( String objectId, String dataSource ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getDataSourceFormForDashboardEdit( getUserIdFromGeneralHeader(), objectId, dataSource ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets widget types field.
     *
     * @param objectId
     *         the object id
     *
     * @return the widget types field
     */
    @Override
    public Response getWidgetTypesField( String objectId ) {
        try {
            return ResponseUtils.success( dataDashboardManager.getWidgetTypesField( getTokenFromGeneralHeader(), objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets fields for data source or transformation.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget type
     * @param sourceType
     *         the widget source
     *
     * @return the fields for data source or transformation
     */
    @Override
    public Response getFieldForDataSourceType( String objectId, String widgetId, String sourceType ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getFieldForDataSourceType( getTokenFromGeneralHeader(), objectId, widgetId, sourceType ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets field for data source.
     *
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
    @Override
    public Response getFieldForDataSource( String objectId, String widgetId, String sourceType, String dataSourceId ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getFieldForDataSource( getTokenFromGeneralHeader(), objectId, widgetId, sourceType,
                            dataSourceId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets other data source fields.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param otherOption
     *         the other option
     *
     * @return the other data source fields
     */
    @Override
    public Response getOtherDataSourceFields( String objectId, String widgetId, String otherOption ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getOtherDataSourceFields( getTokenFromGeneralHeader(), objectId, widgetId, otherOption ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets select options for material inspector.
     *
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
    @Override
    public Response getSelectOptionsForMaterialInspector( String objectId, String widgetId, String fieldName, String fieldValue ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getSelectOptionsForMaterialInspector( getTokenFromGeneralHeader(), objectId, widgetId, fieldName,
                            fieldValue ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets select options for metal base.
     *
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
    @Override
    public Response getSelectOptionsForMetalBase( String objectId, String widgetId, String fieldName, String fieldValue ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getSelectOptionsForMetalBase( getTokenFromGeneralHeader(), objectId, widgetId, fieldName,
                            fieldValue ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets dynamic fields for material inspector.
     *
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
    @Override
    public Response getDynamicFieldsForMaterialInspector( String objectId, String widgetId, String fieldName, String fieldValue ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getDynamicFieldsForMaterialInspector( getTokenFromGeneralHeader(), objectId, widgetId, fieldName,
                            fieldValue ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets dynamic fields for metal base.
     *
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
    @Override
    public Response getDynamicFieldsForMetalBase( String objectId, String widgetId, String fieldName, String fieldValue ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getDynamicFieldsForMetalBase( getTokenFromGeneralHeader(), objectId, widgetId, fieldName,
                            fieldValue ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets schema list field by data source.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param dataSourceId
     *         the data source id
     *
     * @return the schema list field by data source
     */
    @Override
    public Response getSchemaListFieldByDataSource( String objectId, String widgetId, String dataSourceId ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getSchemaListFieldByDataSource( getTokenFromGeneralHeader(), objectId, widgetId, dataSourceId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets tables list field by data source and schema.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param schema
     *         the schema name
     *
     * @return the tables list field by data source and schema
     */
    @Override
    public Response getTablesListFieldByDataSourceAndSchema( String objectId, String widgetId, String schema ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getTablesListFieldByDataSourceAndSchema( getTokenFromGeneralHeader(), objectId, widgetId,
                            schema ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets columns list field by data source and schema and value.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param sourceType
     *         the source type
     * @param value
     *         the value
     *
     * @return the columns list field by data source and schema and value
     */
    @Override
    public Response getColumnsListFieldByDataSourceAndSchemaAndTable( String objectId, String widgetId, String sourceType, String value ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getColumnsListFieldByDataSourceAndSchemaAndTable( getTokenFromGeneralHeader(), objectId, widgetId,
                            sourceType, value ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets filter columns by data source and schema and table.
     *
     * @param objectId
     *         the object id
     * @param table
     *         the table
     *
     * @return the filter columns by data source and schema and table
     */
    @Override
    public Response getFilterColumnsByDataSourceAndSchemaAndTable( String objectId, String table ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getFilterColumnsByDataSourceAndSchemaAndTable( getTokenFromGeneralHeader(), objectId, table ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets widget form by type.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget type
     *
     * @return the widget form by type
     */
    @Override
    public Response getWidgetFormByType( String objectId, String widgetId ) {
        try {
            return ResponseUtils.success( dataDashboardManager.getWidgetFormByType( getTokenFromGeneralHeader(), objectId, widgetId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* ************************************************** Data sources Crud start ******************************************************* */

    /**
     * Gets data sources list by dashboard id.
     *
     * @param objectId
     *         the object id
     *
     * @return the data sources list by dashboard id
     */
    @Override
    public Response getDataSourcesListByDashboardId( String objectId ) {
        try {
            return ResponseUtils.success( dataDashboardManager.getDataSourcesListByDashboardId( getTokenFromGeneralHeader(), objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Add data source to dashboard response.
     *
     * @param objectId
     *         the object id
     * @param dataSourceJson
     *         the data source json
     *
     * @return the response
     */
    @Override
    public Response addDataSourceToDashboard( String objectId, String dataSourceJson ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.addDataSourceToDashboard( getTokenFromGeneralHeader(), objectId, dataSourceJson ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Update data source response.
     *
     * @param objectId
     *         the object id
     * @param dataSourceId
     *         the data source id
     * @param dataSourceJson
     *         the data source json
     *
     * @return the response
     */
    @Override
    public Response updateDataSource( String objectId, String dataSourceId, String dataSourceJson ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.updateDataSource( getTokenFromGeneralHeader(), objectId, dataSourceId, dataSourceJson ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Delete data source response.
     *
     * @param objectId
     *         the object id
     * @param dataSourceId
     *         the data source id
     *
     * @return the response
     */
    @Override
    public Response deleteDataSource( String objectId, String dataSourceId ) {
        try {
            return ResponseUtils.success( dataDashboardManager.deleteDataSource( getTokenFromGeneralHeader(), objectId, dataSourceId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Edit data source form response.
     *
     * @param objectId
     *         the object id
     * @param dataSourceId
     *         the data source id
     *
     * @return the response
     */
    @Override
    public Response editDataSourceForm( String objectId, String dataSourceId ) {
        try {
            return ResponseUtils.success( dataDashboardManager.editDataSourceForm( getTokenFromGeneralHeader(), objectId, dataSourceId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Create data source form response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @Override
    public Response createDataSourceForm( String objectId ) {
        try {
            return ResponseUtils.success( dataDashboardManager.createDataSourceForm( getTokenFromGeneralHeader(), objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets data source by id.
     *
     * @param objectId
     *         the object id
     * @param dataSourceId
     *         the data source id
     *
     * @return the data source by id
     */
    @Override
    public Response getDataSourceById( String objectId, String dataSourceId ) {
        try {
            return ResponseUtils.success( dataDashboardManager.getDataSourceById( getTokenFromGeneralHeader(), objectId, dataSourceId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* ************************************************** Data sources Crud end ********************************************************* */
    /* ***************************************************** widget Crud **************************************************************** */

    /**
     * Gets data sources list by dashboard id.
     *
     * @param objectId
     *         the object id
     *
     * @return the data sources list by dashboard id
     */
    @Override
    public Response getWidgetsListByDashboardId( String objectId ) {
        try {
            return ResponseUtils.success( dataDashboardManager.getWidgetsListByDashboardId( getTokenFromGeneralHeader(), objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Add data source to dashboard response.
     *
     * @param objectId
     *         the object id
     * @param widgetJson
     *         the data source json
     *
     * @return the response
     */
    @Override
    public Response addWidgetToDashboard( String objectId, String widgetJson ) {
        try {
            return ResponseUtils.success( dataDashboardManager.addWidgetToDashboard( getTokenFromGeneralHeader(), objectId, widgetJson ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Update data source response.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the data source id
     * @param widgetJson
     *         the data source json
     *
     * @return the response
     */
    @Override
    public Response updateWidget( String objectId, String widgetId, String widgetJson ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.updateWidget( getTokenFromGeneralHeader(), objectId, widgetId, widgetJson ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Delete data source response.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the data source id
     *
     * @return the response
     */
    @Override
    public Response deleteWidget( String objectId, String widgetId ) {
        try {
            return ResponseUtils.success( dataDashboardManager.deleteWidget( getTokenFromGeneralHeader(), objectId, widgetId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* *************************************************** widget Crud end ************************************************************** */

    /**
     * Run query for widget response.
     *
     * @param objectId
     *         the object id
     * @param widgetJson
     *         the widget json
     * @param pythonMode
     *         the python mode
     *
     * @return the response
     */
    @Override
    public Response runQueryForWidget( String objectId, String widgetJson, String pythonMode ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getWidgetResultSet( getTokenFromGeneralHeader(), objectId, widgetJson, pythonMode ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets python script field.
     *
     * @param objectId
     *         the object id
     * @param pythonOutput
     *         the python output
     *
     * @return the python script field
     */
    @Override
    public Response getWidgetPythonScriptField( String objectId, String pythonOutput ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getWidgetPythonScriptField( getTokenFromGeneralHeader(), objectId, pythonOutput ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets widget python fields.
     *
     * @param objectId
     *         the object id
     * @param option
     *         the option
     *
     * @return the widget python fields
     */
    @Override
    public Response getWidgetPythonFields( String objectId, String option ) {
        try {
            return ResponseUtils.success( dataDashboardManager.getWidgetPythonFields( getTokenFromGeneralHeader(), objectId, option ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets widget python input preview.
     *
     * @param objectId
     *         the object id
     * @param widgetJson
     *         the widget json
     *
     * @return the widget python input preview
     */
    @Override
    public Response getWidgetPythonInputPreview( String objectId, String widgetJson ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getWidgetPythonInputPreview( getTokenFromGeneralHeader(), objectId, widgetJson ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets widget python output preview.
     *
     * @param objectId
     *         the object id
     * @param widgetJson
     *         the widget json
     *
     * @return the widget python output preview
     */
    @Override
    public Response getWidgetPythonOutputPreview( String objectId, String widgetJson ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getWidgetPythonOutputPreview( getTokenFromGeneralHeader(), objectId, widgetJson ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Run widget python response.
     *
     * @param objectId
     *         the object id
     * @param widgetJson
     *         the widget json
     *
     * @return the response
     */
    @Override
    public Response runWidgetPython( String objectId, String widgetJson ) {
        try {
            return ResponseUtils.success( dataDashboardManager.runWidgetPython( getTokenFromGeneralHeader(), objectId, widgetJson ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets applicable aggregate methods.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param groupBy
     *         the group by
     *
     * @return the applicable aggregate methods
     */
    @Override
    public Response getApplicableAggregateMethods( String objectId, String widgetId, String groupBy ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getApplicableAggregateMethods( getTokenFromGeneralHeader(), objectId, widgetId, groupBy ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets widget options form.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param pythonMode
     *         the python mode
     *
     * @return the widget options form
     */
    @Override
    public Response getWidgetOptionsForm( String objectId, String widgetId, String pythonMode ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getWidgetOptionsForm( getTokenFromGeneralHeader(), objectId, widgetId, pythonMode ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets widget filters.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param pythonMode
     *         the python mode
     *
     * @return the widget filters
     */
    @Override
    public Response getWidgetFilters( String objectId, String widgetId, String pythonMode ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getWidgetFilters( getTokenFromGeneralHeader(), objectId, widgetId, pythonMode ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Perform widget action response.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param action
     *         the action
     * @param data
     *         the data
     *
     * @return the response
     */
    @Override
    public Response performWidgetAction( String objectId, String widgetId, String action, String data ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.performWidgetAction( getTokenFromGeneralHeader(), objectId, widgetId, action, data ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Perform widget action response.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param action
     *         the action
     *
     * @return the response
     */
    @Override
    public Response performWidgetAction( String objectId, String widgetId, String action ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.performWidgetAction( getTokenFromGeneralHeader(), objectId, widgetId, action, null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Download widget file response.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param action
     *         the action
     *
     * @return the response
     */
    @Override
    public Response downloadWidgetFile( String objectId, String widgetId, String action ) {
        try {
            Path file = dataDashboardManager.getDownloadFilePath( objectId, widgetId, action, getTokenFromGeneralHeader() );
            return downloadExportFile( file );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Add widget to group response.
     *
     * @param objectId
     *         the object id
     * @param groupId
     *         the group id
     * @param widgetId
     *         the widget id
     *
     * @return the response
     */
    @Override
    public Response addWidgetToGroup( String objectId, String groupId, String widgetId ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.addWidgetToGroup( getTokenFromGeneralHeader(), objectId, groupId, widgetId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Remove widget from group response.
     *
     * @param objectId
     *         the object id
     * @param groupId
     *         the group id
     * @param widgetId
     *         the widget id
     *
     * @return response response
     */
    @Override
    public Response removeWidgetFromGroup( String objectId, String groupId, String widgetId ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.removeWidgetFromGroup( getTokenFromGeneralHeader(), objectId, groupId, widgetId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets widgets in group.
     *
     * @param objectId
     *         the object id
     * @param groupId
     *         the group id
     *
     * @return widgets in group
     */
    @Override
    public Response getWidgetsInGroup( String objectId, String groupId ) {
        try {
            return ResponseUtils.success( dataDashboardManager.getWidgetsInGroup( getTokenFromGeneralHeader(), objectId, groupId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Update group response.
     *
     * @param objectId
     *         the object id
     * @param groupId
     *         the group id
     * @param widgetJson
     *         the widget json
     *
     * @return response response
     */
    @Override
    public Response updateGroup( String objectId, String groupId, String widgetJson ) {
        try {
            return ResponseUtils.success( dataDashboardManager.updateWidget( getTokenFromGeneralHeader(), objectId, groupId, widgetJson ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Delete group response.
     *
     * @param objectId
     *         the object id
     * @param groupId
     *         the group id
     *
     * @return response response
     */
    @Override
    public Response unGroupWidgets( String objectId, String groupId ) {
        try {
            return ResponseUtils.success( dataDashboardManager.unGroupWidgets( getTokenFromGeneralHeader(), objectId, groupId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets line widget options by type.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param lineWidgetType
     *         the line widget type
     *
     * @return the line widget options by type
     */
    @Override
    public Response getLineWidgetOptionsByType( String objectId, String widgetId, String lineWidgetType ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getLineWidgetOptionsByType( getTokenFromGeneralHeader(), objectId, widgetId, lineWidgetType ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /**
     * Gets text input fields.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param textInputMethod
     *         the text input method
     *
     * @return the text input fields
     */
    @Override
    public Response getTextInputFields( String objectId, String widgetId, String textInputMethod ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getTextInputFields( getTokenFromGeneralHeader(), objectId, widgetId, textInputMethod ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /**
     * Gets chart options field for mix chart.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     *
     * @return the chart options field for mix chart
     */
    @Override
    public Response getCurveOptionsFieldForMixChart( String objectId, String widgetId ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getCurveOptionsFieldForMixChart( getTokenFromGeneralHeader(), objectId, widgetId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets value options field for radar chart.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     *
     * @return the value options field for radar chart
     */
    @Override
    public Response getValueOptionsFieldForRadarChart( String objectId, String widgetId ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getValueOptionsFieldForRadarChart( getTokenFromGeneralHeader(), objectId, widgetId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets chart options related fields for mix chart.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param option
     *         the option
     *
     * @return the chart options related fields for mix chart
     */
    @Override
    public Response getCurveOptionsRelatedFieldsForMixChart( String objectId, String widgetId, String option ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getCurveOptionsRelatedFieldsForMixChart( getTokenFromGeneralHeader(), objectId, widgetId,
                            option ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    @Override
    public Response updateDataSourceCache( String objectId, String sourceId ) {
        try {
            return ResponseUtils.success( dataDashboardManager.updateDataSourceCache( getTokenFromGeneralHeader(), objectId, sourceId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getUIForTableWidget( String objectId, String widgetId ) {
        try {
            return ResponseUtils.success( dataDashboardManager.getTableWidgetUi( getTokenFromGeneralHeader(), objectId, widgetId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getListForTableWidget( String objectId, String widgetId, String filterJson ) {
        try {
            return ResponseUtils.success(
                    dataDashboardManager.getListForTableWidget( getTokenFromGeneralHeader(), objectId, widgetId, filterJson ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Download export file response.
     *
     * @param file
     *         the file
     *
     * @return the response
     *
     * @throws IOException
     *         the io exception
     */
    private static Response downloadExportFile( Path file ) throws IOException {
        if ( Files.exists( file ) ) {
            Response.ResponseBuilder response = Response.ok( file.toFile() );
            response.header( "Content-Disposition", "attachment; filename=\"" + file.getFileName().toString() + "\"" );
            response.header( Message.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM );
            response.header( "File-Size", Files.size( file ) );
            return response.build();
        } else {
            return ResponseUtils.failure(
                    MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), file.toAbsolutePath().toString() ) );
        }
    }

}