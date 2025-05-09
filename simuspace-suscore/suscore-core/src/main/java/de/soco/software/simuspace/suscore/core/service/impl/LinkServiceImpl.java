package de.soco.software.simuspace.suscore.core.service.impl;

import javax.ws.rs.core.Response;

import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.core.manager.LinkManager;
import de.soco.software.simuspace.suscore.core.service.LinkService;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;

/**
 * The Class LinkServiceImpl handles linking objects selections.
 *
 * @author Ahsan.Khan
 */
public class LinkServiceImpl extends SuSBaseService implements LinkService {

    /**
     * The link manager.
     */
    private LinkManager linkManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response objectsLinking( String filterJson ) {
        try {
            Map< String, String > map = JsonUtils.convertStringToMap( filterJson );
            return ResponseUtils.success( linkManager.objectsLinking( UUID.fromString( getUserIdFromGeneralHeader() ),
                    UUID.fromString( map.get( "linkFrom" ) ), UUID.fromString( map.get( "linkTo" ) ) ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response removeLinkObjectById( UUID objectId, String selectionFrom ) {
        try {
            Map< String, String > selection = JsonUtils.convertStringToMap( selectionFrom );
            if ( linkManager.removeLinkObjectById( UUID.fromString( getUserIdFromGeneralHeader() ), objectId,
                    UUID.fromString( selection.get( "linkFrom" ) ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.LINK_DELETED.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.LINK_NOT_DELETED.getKey() ) );
            }
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getLinkedRelationByChildId( UUID objectId ) {
        try {
            return ResponseUtils.success( linkManager.getLinkedRelationByChildId( objectId ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the link manager.
     *
     * @return the link manager
     */
    public LinkManager getLinkManager() {
        return linkManager;
    }

    /**
     * Sets the link manager.
     *
     * @param linkManager
     *         the new link manager
     */
    public void setLinkManager( LinkManager linkManager ) {
        this.linkManager = linkManager;
    }

}