package de.soco.software.simuspace.suscore.object.report.service.impl;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.cxf.message.Message;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.model.SectionDTO;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.object.report.manager.ReportManager;
import de.soco.software.simuspace.suscore.object.report.service.rest.ReportService;

import j2html.tags.ContainerTag;

/**
 * The Class ReportServiceImpl.
 *
 * @author Ahsan.Khan
 */
public class ReportServiceImpl extends SuSBaseService implements ReportService {

    /**
     * The report manager.
     */
    private ReportManager reportManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createSection( String filterJson, UUID reportId ) {
        try {
            SectionDTO sectionDTO = reportManager.createSection( getUserIdFromGeneralHeader(),
                    JsonUtils.jsonToObject( filterJson, SectionDTO.class ), reportId );
            if ( sectionDTO != null ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.SECTION_CREATED.getKey() ), sectionDTO );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.SECTION_NOT_CREATED.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSectionList( UUID reportId ) {
        try {
            return ResponseUtils.success( reportManager.getSectionList( getUserIdFromGeneralHeader(), reportId ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createSectionUI() {
        try {
            return ResponseUtils.success( reportManager.createSectionUI( getUserIdFromGeneralHeader() ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editSectionUI( UUID sectionId ) {
        try {
            return ResponseUtils.success( reportManager.editSectionUI( getUserIdFromGeneralHeader(), sectionId ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateSection( String filterJson ) {
        try {
            return ResponseUtils.success(
                    reportManager.updateSection( getUserIdFromGeneralHeader(), JsonUtils.jsonToObject( filterJson, SectionDTO.class ) ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     */
    @Override
    public Response generateSectionPreview( UUID sectionId ) throws IOException {
        try {
            ContainerTag tag = reportManager.generateSectionPreview( sectionId );
            return ResponseUtils.success( tag != null ? tag.render() : null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response previewReport( UUID reportId ) {
        try {
            ContainerTag tag = reportManager.previewReport( reportId );
            return ResponseUtils.success( tag != null ? tag.render() : null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response downloadPreviewReportPdf( UUID reportId ) {
        try {
            Map< String, String > map = new HashMap<>();
            map.put( "url", reportManager.downloadPreviewReportPdf( reportId ) );
            return ResponseUtils.success( map );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response downloadPreviewSectionPdf( UUID sectionId ) {
        try {
            Map< String, String > map = new HashMap<>();
            map.put( "url", reportManager.downloadPreviewSectionPdf( sectionId ) );
            return ResponseUtils.success( map );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response downloadPreviewReportDocx( UUID reportId ) {
        try {
            Map< String, String > map = new HashMap<>();
            map.put( "url", reportManager.downloadPreviewReportDocx( reportId ) );
            return ResponseUtils.success( map );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response downloadPreviewSectionDocx( UUID sectionId ) {
        try {
            Map< String, String > map = new HashMap<>();
            map.put( "url", reportManager.downloadPreviewSectionDocx( sectionId ) );
            return ResponseUtils.success( map );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response downloadPreviewReportPdfLink( UUID reportId, UriInfo uriInfo ) {
        try {
            File file = reportManager.previewReportPdf( reportId );
            ResponseBuilder response = Response.ok( ( Object ) file );
            response.header( "Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" );
            response.header( Message.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM );
            return response.build();
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response downloadPreviewSectionPdfLink( UUID sectionId ) {
        try {
            File file = reportManager.previewSectionPdf( sectionId );
            ResponseBuilder response = Response.ok( ( Object ) file );
            response.header( "Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" );
            response.header( Message.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM );
            return response.build();
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     */
    @Override
    public Response downloadPreviewReportDocxLink( UUID reportId, UriInfo uriInfo ) throws IOException {
        try {
            File file = reportManager.previewReportDocx( reportId );
            ResponseBuilder response = Response.ok( ( Object ) file );
            response.header( "Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" );
            response.header( Message.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM );
            return response.build();
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response downloadPreviewSectionDocxLink( UUID sectionId ) {
        try {
            File file = reportManager.previewSectionDocx( sectionId );
            ResponseBuilder response = Response.ok( ( Object ) file );
            response.header( "Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" );
            response.header( Message.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM );
            return response.build();
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response orderingSections( UUID reportId, String filterJson ) {
        try {
            return ResponseUtils.success( reportManager.getSectionOrderList( getUserIdFromGeneralHeader(), reportId,
                    JsonUtils.jsonToList( filterJson, UUID.class ) ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editReportForm( UUID reportId ) {
        return null;
    }

    /**
     * Gets the report manager.
     *
     * @return the report manager
     */
    public ReportManager getReportManager() {
        return reportManager;
    }

    /**
     * Sets the report manager.
     *
     * @param reportManager
     *         the new report manager
     */
    public void setReportManager( ReportManager reportManager ) {
        this.reportManager = reportManager;
    }

}
