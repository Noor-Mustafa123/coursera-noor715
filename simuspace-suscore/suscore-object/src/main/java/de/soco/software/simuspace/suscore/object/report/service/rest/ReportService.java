package de.soco.software.simuspace.suscore.object.report.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.io.IOException;
import java.util.UUID;

/**
 * The Class ReportService.
 *
 * @author Ahsan.Khan
 */
public interface ReportService {

    /**
     * Creates the section.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the response
     */
    @POST
    @Path( "{reportId}/section" )
    @Produces( MediaType.APPLICATION_JSON )
    Response createSection( String filterJson, @PathParam( "reportId" ) UUID reportId );

    /**
     * Gets the section list.
     *
     * @param reportId
     *         the report id
     *
     * @return the section list
     */
    @GET
    @Path( "{reportId}/section/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getSectionList( @PathParam( "reportId" ) UUID reportId );

    /**
     * Creates the section UI.
     *
     * @return the response
     */
    @GET
    @Path( "section/ui/create" )
    @Produces( MediaType.APPLICATION_JSON )
    Response createSectionUI();

    /**
     * Edits the section UI.
     *
     * @param sectionId
     *         the section id
     *
     * @return the response
     */
    @GET
    @Path( "/section/ui/{sectionId}/edit" )
    @Produces( MediaType.APPLICATION_JSON )
    Response editSectionUI( @PathParam( "sectionId" ) UUID sectionId );

    /**
     * Update section.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the response
     */
    @POST
    @Path( "section/edit" )
    @Produces( MediaType.APPLICATION_JSON )
    Response updateSection( String filterJson );

    /**
     * Generate section preview.
     *
     * @param sectionId
     *         the section id
     *
     * @return the response
     *
     * @throws IOException
     */
    @GET
    @Path( "section/{sectionId}/preview" )
    @Produces( MediaType.APPLICATION_JSON )
    Response generateSectionPreview( @PathParam( "sectionId" ) UUID sectionId ) throws IOException;

    /**
     * Preview report.
     *
     * @param reportId
     *         the report id
     *
     * @return the response
     */
    @GET
    @Path( "{reportId}/ui/general/preview" )
    @Produces( MediaType.APPLICATION_JSON )
    Response previewReport( @PathParam( "reportId" ) UUID reportId );

    /**
     * Edits the report form.
     *
     * @param reportId
     *         the report id
     *
     * @return the response
     */
    @GET
    @Path( "{reportId}/ui/general/edit" )
    @Produces( MediaType.APPLICATION_JSON )
    Response editReportForm( @PathParam( "reportId" ) UUID reportId );

    /**
     * Download preview report pdf.
     *
     * @param reportId
     *         the report id
     *
     * @return the response
     */
    @GET
    @Path( "{reportId}/export/pdf" )
    @Produces( MediaType.APPLICATION_JSON )
    Response downloadPreviewReportPdf( @PathParam( "reportId" ) UUID reportId );

    /**
     * Download preview section pdf.
     *
     * @param sectionId
     *         the section id
     *
     * @return the response
     */
    @GET
    @Path( "section/{sectionId}/export/pdf" )
    @Produces( MediaType.APPLICATION_JSON )
    Response downloadPreviewSectionPdf( @PathParam( "sectionId" ) UUID sectionId );

    /**
     * Download preview report docx.
     *
     * @param reportId
     *         the report id
     *
     * @return the response
     */
    @GET
    @Path( "{reportId}/export/doc" )
    @Produces( MediaType.APPLICATION_JSON )
    Response downloadPreviewReportDocx( @PathParam( "reportId" ) UUID reportId );

    /**
     * Download preview section docx.
     *
     * @param sectionId
     *         the section id
     *
     * @return the response
     */
    @GET
    @Path( "section/{sectionId}/export/doc" )
    @Produces( MediaType.APPLICATION_JSON )
    Response downloadPreviewSectionDocx( @PathParam( "sectionId" ) UUID sectionId );

    /**
     * Download preview report pdf link.
     *
     * @param reportId
     *         the report id
     * @param uriInfo
     *         the uri info
     *
     * @return the response
     */
    @GET
    @Path( "{reportId}/pdf/link" )
    @Produces( MediaType.APPLICATION_OCTET_STREAM )
    Response downloadPreviewReportPdfLink( @PathParam( "reportId" ) UUID reportId, @Context UriInfo uriInfo );

    /**
     * Download preview section pdf link.
     *
     * @param sectionId
     *         the section id
     *
     * @return the response
     */
    @GET
    @Path( "section/{sectionId}/pdf/link" )
    @Produces( MediaType.APPLICATION_OCTET_STREAM )
    Response downloadPreviewSectionPdfLink( @PathParam( "sectionId" ) UUID sectionId );

    /**
     * Download preview report docx link.
     *
     * @param reportId
     *         the report id
     * @param uriInfo
     *         the uri info
     *
     * @return the response
     *
     * @throws IOException
     */
    @GET
    @Path( "{reportId}/docx/link" )
    @Produces( MediaType.APPLICATION_OCTET_STREAM )
    Response downloadPreviewReportDocxLink( @PathParam( "reportId" ) UUID reportId, @Context UriInfo uriInfo ) throws IOException;

    /**
     * Download preview section docx link.
     *
     * @param sectionId
     *         the section id
     *
     * @return the response
     */
    @GET
    @Path( "section/{sectionId}/docx/link" )
    @Produces( MediaType.APPLICATION_OCTET_STREAM )
    Response downloadPreviewSectionDocxLink( @PathParam( "sectionId" ) UUID sectionId );

    /**
     * Ordering sections.
     *
     * @param reportId
     *         the report id
     * @param filterJson
     *         the filter json
     *
     * @return the response
     */
    @POST
    @Path( "{reportId}/section/order" )
    @Produces( MediaType.APPLICATION_JSON )
    Response orderingSections( @PathParam( "reportId" ) UUID reportId, String filterJson );

}
