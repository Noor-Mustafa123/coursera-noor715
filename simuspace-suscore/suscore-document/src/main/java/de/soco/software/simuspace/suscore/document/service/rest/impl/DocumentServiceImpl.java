package de.soco.software.simuspace.suscore.document.service.rest.impl;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.message.Message;
import org.apache.http.HttpStatus;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.document.manager.DocumentManager;
import de.soco.software.simuspace.suscore.document.service.rest.DocumentService;

/**
 * The implementation of DocumentService to handle document operations.
 *
 * @author MNasir.farooq, ahmar.nadeem
 * @since 1.0
 */
public class DocumentServiceImpl extends SuSBaseService implements DocumentService {

    /**
     * The document manager.
     */
    private DocumentManager documentManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDocumentList( String filterJSON ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJSON, FiltersDTO.class );
            return ResponseUtils.success( documentManager.getDocumentList( filter ) );
        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDocumentListByUserId( UUID userId, String filterJSON ) {
        if ( userId == null ) {
            return Response.status( HttpStatus.SC_BAD_REQUEST )
                    .entity( MessageBundleFactory.getMessage( Messages.USER_ID_CANNOT_BE_NULL.getKey() ) ).build();
        }
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJSON, FiltersDTO.class );
            return ResponseUtils.success( documentManager.getDocumentListByUserId( userId, filter ) );
        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDocumentById( UUID documentId, int version ) {
        if ( documentId == null || version == ConstantsInteger.INTEGER_VALUE_ZERO ) {
            return Response.status( HttpStatus.SC_BAD_REQUEST )
                    .entity( MessageBundleFactory.getMessage( Messages.DOCUMENT_ID_CANNOT_BE_NULL.getKey() ) ).build();
        }
        try {
            DocumentDTO document = documentManager.getDocumentById( documentId );
            return ResponseUtils.success( document );
        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteDocument( UUID documentId, int version ) {
        if ( documentId == null || version == ConstantsInteger.INTEGER_VALUE_ZERO ) {
            return Response.status( HttpStatus.SC_BAD_REQUEST )
                    .entity( MessageBundleFactory.getMessage( Messages.DOCUMENT_ID_CANNOT_BE_NULL.getKey() ) ).build();
        }
        try {
            documentManager.deleteDocument( documentId );
            return Response.ok().build();
        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Sets the document manager.
     *
     * @param documentManager
     *         the documentManager to set
     */
    public void setDocumentManager( DocumentManager documentManager ) {
        this.documentManager = documentManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response downloadDocument( UUID documentId, int version ) {

        try {
            int size = 0;
            DocumentDTO document = documentManager.getDocumentById( documentId );

            try ( InputStream fileStream = documentManager.readVaultFromDisk( document ) ) {
                byte[] byteArray = IOUtils.toByteArray( fileStream );

                try ( InputStream inputS2 = new ByteArrayInputStream( byteArray ) ) {
                    /* calculate File size*/
                    size = IOUtils.toByteArray( inputS2 ).length;
                }

                ResponseBuilder response = Response.ok( new ByteArrayInputStream( byteArray ) );
                response.header( "Content-Disposition", "attachment; filename=\"" + document.getName() + "\"" );
                response.header( Message.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM );
                response.header( "File-Size", size );
                return response.build();
            }

        } catch ( Exception e ) {
            return handleException( e );
        }
    }

}
