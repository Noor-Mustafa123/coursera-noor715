package de.soco.software.simuspace.suscore.object.service.rest;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;

/**
 * The interface Data dashboard service.
 */
@WebService
@Consumes( { MediaType.APPLICATION_JSON } )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = true, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface DataDashboardService {

    /**
     * Gets config from object and data source.
     *
     * @param objectId
     *         the object id
     *
     * @return the config from object and data source
     */
    @GET
    @Path( "/{objectId}/permission" )
    Response getDashboardPermission( @PathParam( "objectId" ) String objectId );

    /**
     * Test data source response.
     *
     * @param parametersJson
     *         the parameters json
     *
     * @return the response
     */
    @POST
    @Path( "/source/test" )
    Response testDataSource( String parametersJson );

    /**
     * Preview data source response.
     *
     * @param parametersJson
     *         the parameters json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/source/preview" )
    Response previewDataSource( @PathParam( "objectId" ) String objectId, String parametersJson );

    /**
     * Create object form dashboard data source response.
     *
     * @param dataSource
     *         the data source
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/ui/create/dataSource/{dataSource}" )
    Response createObjectFormDashboardDataSource( @PathParam( "objectId" ) String objectId, @PathParam( "dataSource" ) String dataSource );

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
    @GET
    @Path( "/{objectId}/ui/edit/dataSource/{dataSource}" )
    Response getConfigFromObjectAndDataSource( @PathParam( "objectId" ) String objectId, @PathParam( "dataSource" ) String dataSource );

    /**
     * Gets widget types field.
     *
     * @param objectId
     *         the object id
     *
     * @return the widget types field
     */
    @GET
    @Path( "/{objectId}/widget/list" )
    Response getWidgetTypesField( @PathParam( "objectId" ) String objectId );

    /**
     * Gets fields for data source or transformation.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param sourceType
     *         the widget source
     *
     * @return the fields for data source or transformation
     */
    @GET
    @Path( "/{objectId}/widget/{widgetId}/sourceType/{sourceType}" )
    Response getFieldForDataSourceType( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @PathParam( "sourceType" ) String sourceType );

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
    @GET
    @Path( "/{objectId}/widget/{widgetId}/sourceType/{sourceType}/dataSource/{dataSourceId}" )
    Response getFieldForDataSource( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @PathParam( "sourceType" ) String sourceType, @PathParam( "dataSourceId" ) String dataSourceId );

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
    @GET
    @Path( "/{objectId}/widget/{widgetId}/source/other/ui/{otherOption}" )
    Response getOtherDataSourceFields( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @PathParam( "otherOption" ) String otherOption );

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
    @GET
    @Path( "/{objectId}/widget/{widgetId}/materialInspector/{fieldName}/{fieldValue}" )
    Response getSelectOptionsForMaterialInspector( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @PathParam( "fieldName" ) String fieldName, @PathParam( "fieldValue" ) String fieldValue );

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
    @GET
    @Path( "/{objectId}/widget/{widgetId}/metalBase/{fieldName}/{fieldValue}" )
    Response getSelectOptionsForMetalBase( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @PathParam( "fieldName" ) String fieldName, @PathParam( "fieldValue" ) String fieldValue );

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
    @GET
    @Path( "/{objectId}/widget/{widgetId}/materialInspector/ui/{fieldName}/{fieldValue}" )
    Response getDynamicFieldsForMaterialInspector( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @PathParam( "fieldName" ) String fieldName, @PathParam( "fieldValue" ) String fieldValue );

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
    @GET
    @Path( "/{objectId}/widget/{widgetId}/metalBase/ui/{fieldName}/{fieldValue}" )
    Response getDynamicFieldsForMetalBase( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @PathParam( "fieldName" ) String fieldName, @PathParam( "fieldValue" ) String fieldValue );

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
    @GET
    @Path( "/{objectId}/widget/{widgetId}/dataSource/schemas/{dataSource}" )
    Response getSchemaListFieldByDataSource( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @PathParam( "dataSource" ) String dataSourceId );

    /**
     * Gets tables list field by data source and schema.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param schema
     *         the schema
     *
     * @return the tables list field by data source and schema
     */
    @GET
    @Path( "/{objectId}/widget/{widgetId}/dataSource/tables/{schema}" )
    Response getTablesListFieldByDataSourceAndSchema( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @PathParam( "schema" ) String schema );

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
    @GET
    @Path( "/{objectId}/widget/{widgetId}/sourceType/{sourceType}/columns/{value}" )
    Response getColumnsListFieldByDataSourceAndSchemaAndTable( @PathParam( "objectId" ) String objectId,
            @PathParam( "widgetId" ) String widgetId, @PathParam( "sourceType" ) String sourceType, @PathParam( "value" ) String value );

    /**
     * Gets filter columns list field by data source and schema and table.
     *
     * @param objectId
     *         the object id
     * @param table
     *         the table
     *
     * @return the filter columns list field by data source and schema and table
     */
    @GET
    @Path( "/{objectId}/filter/columns/{table}" )
    Response getFilterColumnsByDataSourceAndSchemaAndTable( @PathParam( "objectId" ) String objectId, @PathParam( "table" ) String table );

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
    @GET
    @Path( "/{objectId}/widget/{widgetId}/form" )
    Response getWidgetFormByType( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId );

    /* ************************************************** Data sources Crud ************************************************************* */

    /**
     * Gets data sources list by dashboard id.
     *
     * @param objectId
     *         the object id
     *
     * @return the data sources list by dashboard id
     */
    @GET
    @Path( "/{objectId}/dataSource/list" )
    Response getDataSourcesListByDashboardId( @PathParam( "objectId" ) String objectId );

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
    @POST
    @Path( "/{objectId}/dataSource" )
    Response addDataSourceToDashboard( @PathParam( "objectId" ) String objectId, String dataSourceJson );

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
    @PUT
    @Path( "/{objectId}/dataSource/{dataSourceId}" )
    Response updateDataSource( @PathParam( "objectId" ) String objectId, @PathParam( "dataSourceId" ) String dataSourceId,
            String dataSourceJson );

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
    @DELETE
    @Path( "/{objectId}/dataSource/{dataSourceId}" )
    Response deleteDataSource( @PathParam( "objectId" ) String objectId, @PathParam( "dataSourceId" ) String dataSourceId );

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
    @GET
    @Path( "/{objectId}/dataSource/ui/edit/{dataSourceId}" )
    Response editDataSourceForm( @PathParam( "objectId" ) String objectId, @PathParam( "dataSourceId" ) String dataSourceId );

    /**
     * Create data source form response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/dataSource/ui/create" )
    Response createDataSourceForm( @PathParam( "objectId" ) String objectId );

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
    @GET
    @Path( "/{objectId}/dataSource/{dataSourceId}" )
    Response getDataSourceById( @PathParam( "objectId" ) String objectId, @PathParam( "dataSourceId" ) String dataSourceId );

    /* ************************************************** Data sources Crud end ********************************************************* */
    /* ***************************************************** widget Crud **************************************************************** */

    /**
     * Gets widgets list by dashboard id.
     *
     * @param objectId
     *         the object id
     *
     * @return the widgets list by dashboard id
     */
    @GET
    @Path( "/{objectId}/widgets/list" )
    Response getWidgetsListByDashboardId( @PathParam( "objectId" ) String objectId );

    /**
     * Add widget to dashboard response.
     *
     * @param objectId
     *         the object id
     * @param widgetJson
     *         the widget json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/widget" )
    Response addWidgetToDashboard( @PathParam( "objectId" ) String objectId, String widgetJson );

    /**
     * Update widget response.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     * @param widgetJson
     *         the widget json
     *
     * @return the response
     */
    @PUT
    @Path( "/{objectId}/widget/{widgetId}" )
    Response updateWidget( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId, String widgetJson );

    /**
     * Delete widget response.
     *
     * @param objectId
     *         the object id
     * @param widgetId
     *         the widget id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{objectId}/widget/{widgetId}" )
    Response deleteWidget( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId );

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
    @POST
    @Path( "/{objectId}/widget/data" )
    Response runQueryForWidget( @PathParam( "objectId" ) String objectId, String widgetJson,
            @QueryParam( "pythonMode" ) String pythonMode );

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
    @GET
    @Path( "/{objectId}/python/ui/{pythonOutput}" )
    Response getWidgetPythonScriptField( @PathParam( "objectId" ) String objectId, @PathParam( "pythonOutput" ) String pythonOutput );

    /**
     * Gets python script field.
     *
     * @param objectId
     *         the object id
     * @param option
     *         the option
     *
     * @return the python script field
     */
    @GET
    @Path( "/{objectId}/python/options/ui/{option}" )
    Response getWidgetPythonFields( @PathParam( "objectId" ) String objectId, @PathParam( "option" ) String option );

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
    @POST
    @Path( "/{objectId}/python/input/preview" )
    Response getWidgetPythonInputPreview( @PathParam( "objectId" ) String objectId, String widgetJson );

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
    @POST
    @Path( "/{objectId}/python/output/preview" )
    Response getWidgetPythonOutputPreview( @PathParam( "objectId" ) String objectId, String widgetJson );

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
    @POST
    @Path( "/{objectId}/python/run" )
    Response runWidgetPython( @PathParam( "objectId" ) String objectId, String widgetJson );

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
    @GET
    @Path( "/{objectId}/widget/{widgetId}/groupBy/{groupBy}" )
    Response getApplicableAggregateMethods( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @PathParam( "groupBy" ) String groupBy );

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
    @GET
    @Path( "/{objectId}/widget/{widgetId}/options/form" )
    Response getWidgetOptionsForm( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @QueryParam( "pythonMode" ) String pythonMode );

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
    @GET
    @Path( "/{objectId}/widget/{widgetId}/filters" )
    Response getWidgetFilters( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @QueryParam( "pythonMode" ) String pythonMode );

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
    @POST
    @Path( "/{objectId}/widget/{widgetId}/action/{action}" )
    Response performWidgetAction( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @PathParam( "action" ) String action, String data );

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
    @GET
    @Path( "/{objectId}/widget/{widgetId}/action/{action}" )
    Response performWidgetAction( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @PathParam( "action" ) String action );

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
    @GET
    @Path( "/{objectId}/widget/{widgetId}/download/{action}" )
    @Produces( { MediaType.APPLICATION_OCTET_STREAM } )
    Response downloadWidgetFile( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @PathParam( "action" ) String action );

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
    @GET
    @Path( "/{objectId}/group/{groupId}/widget/{widgetId}/add" )
    Response addWidgetToGroup( @PathParam( "objectId" ) String objectId, @PathParam( "groupId" ) String groupId,
            @PathParam( "widgetId" ) String widgetId );

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
     * @return the response
     */
    @GET
    @Path( "/{objectId}/group/{groupId}/widget/{widgetId}/remove" )
    Response removeWidgetFromGroup( @PathParam( "objectId" ) String objectId, @PathParam( "groupId" ) String groupId,
            @PathParam( "widgetId" ) String widgetId );

    /**
     * Gets widgets in group.
     *
     * @param objectId
     *         the object id
     * @param groupId
     *         the group id
     *
     * @return the widgets in group
     */
    @GET
    @Path( "/{objectId}/group/{groupId}/widgets/list" )
    Response getWidgetsInGroup( @PathParam( "objectId" ) String objectId, @PathParam( "groupId" ) String groupId );

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
     * @return the response
     */
    @PUT
    @Path( "/{objectId}/group/{groupId}" )
    Response updateGroup( @PathParam( "objectId" ) String objectId, @PathParam( "groupId" ) String groupId, String widgetJson );

    /**
     * Delete group response.
     *
     * @param objectId
     *         the object id
     * @param groupId
     *         the group id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{objectId}/group/{groupId}" )
    Response unGroupWidgets( @PathParam( "objectId" ) String objectId, @PathParam( "groupId" ) String groupId );

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
    @GET
    @Path( "{objectId}/widget/{widgetId}/lineWidget/{type}" )
    Response getLineWidgetOptionsByType( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @PathParam( "type" ) String lineWidgetType );

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
    @GET
    @Path( "{objectId}/widget/{widgetId}/textWidget/{type}" )
    Response getTextInputFields( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @PathParam( "type" ) String textInputMethod );

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
    @GET
    @Path( "{objectId}/widget/{widgetId}/mix/addCurve" )
    Response getCurveOptionsFieldForMixChart( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId );

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
    @GET
    @Path( "{objectId}/widget/{widgetId}/radar/addValue" )
    Response getValueOptionsFieldForRadarChart( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId );

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
    @GET
    @Path( "{objectId}/widget/{widgetId}/curveOptions/{option}" )
    Response getCurveOptionsRelatedFieldsForMixChart( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId,
            @PathParam( "option" ) String option );

    @POST
    @Path( "{objectId}/source/{sourceId}/cache/update" )
    Response updateDataSourceCache( @PathParam( "objectId" ) String objectId, @PathParam( "sourceId" ) String sourceId );

    @GET
    @Path( "/{objectId}/widget/{widgetId}/table/ui" )
    Response getUIForTableWidget( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId );

    @POST
    @Path( "/{objectId}/widget/{widgetId}/table/list" )
    Response getListForTableWidget( @PathParam( "objectId" ) String objectId, @PathParam( "widgetId" ) String widgetId, String filterJson );

}