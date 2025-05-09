package de.soco.software.simuspace.suscore.object.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.ObjectTreeViewDTO;
import de.soco.software.simuspace.suscore.data.model.TreeNodeDTO;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.data.utility.ContextUtil;
import de.soco.software.simuspace.suscore.object.manager.SuSObjectTreeManager;
import de.soco.software.simuspace.suscore.object.service.rest.SuSObjectTreeService;

/**
 * The service class for object tree providing API's like getObjectTree and filterObjectTree.
 */
public class SuSObjectTreeServiceImpl extends SuSBaseService implements SuSObjectTreeService {

    /**
     * SusObjectManager reference.
     */
    private SuSObjectTreeManager treeManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectTree() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.SIMUSPACE_TREE_GENERATED_SUCCESSFULLY.getKey() ),
                    treeManager.getTree( null, getUserIdFromGeneralHeader(), null, new ObjectTreeViewDTO(), getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response filterObjectTree( String objectJson ) {
        try {
            ObjectTreeViewDTO request = JsonUtils.jsonToObject( objectJson, ObjectTreeViewDTO.class );
            List< TreeNodeDTO > returnModel = treeManager.getTree( null, getUserIdFromGeneralHeader(), null, request,
                    getTokenFromGeneralHeader() );
            return ResponseUtils.success( returnModel );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectTreeForField( String fieldType ) {
        try {

            return ResponseUtils.success( treeManager.getTreeForField( getTokenFromGeneralHeader(), new ObjectTreeViewDTO(), fieldType ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response filterObjectTreeChildren( String containerId, String objectJson ) {
        try {
            ObjectTreeViewDTO request = JsonUtils.jsonToObject( objectJson, ObjectTreeViewDTO.class );
            List< TreeNodeDTO > returnModel = treeManager.getTree( null, getUserIdFromGeneralHeader(), UUID.fromString( containerId ),
                    request, getTokenFromGeneralHeader() );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.OBJECT_TREE_CHILDREN.getKey() ) ), returnModel );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getTreeContext( String containerId, String objectJson ) {
        return getTreeContext( containerId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getTreeContext( String containerId ) {
        try {
            List< ContextMenuItem > contextMenu = treeManager.findMenu( getUserIdFromGeneralHeader(), getTokenFromGeneralHeader(),
                    containerId );
            ListIterator< ContextMenuItem > iterator = contextMenu.listIterator();
            while ( iterator.hasNext() ) {
                ContextMenuItem contextMenuItem = iterator.next();
                if ( ( containerId.equals( SimuspaceFeaturesEnum.ROLES.getId() ) || containerId.equals(
                        SimuspaceFeaturesEnum.GROUPS.getId() ) || containerId.equals( SimuspaceFeaturesEnum.LOCATIONS.getId() )
                        || containerId.equals( SimuspaceFeaturesEnum.DIRECTORIES.getId() ) || containerId.equals(
                        SimuspaceFeaturesEnum.LICENSES.getId() ) || containerId.equals( SimuspaceFeaturesEnum.USERS.getId() ) )
                        && contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( "4100014x4" ) ) ) {
                    iterator.remove();
                }
                if ( contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( "4100115x4" ) )
                        || contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( "4100065x4" ) )
                        || contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( "4100062x4" ) )
                        || contextMenuItem.getTitle().equalsIgnoreCase( MessageBundleFactory.getMessage( "4100116x4" ) ) ) {
                    iterator.remove();
                }
            }

            return ResponseUtils.success( ContextUtil.allOrderedContext( contextMenu ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllObjectTreeViews() {
        try {
            return ResponseUtils.success( treeManager.getAllObjectTreeViews( getUserIdFromGeneralHeader(), getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveObjectTreeView( String objectJson ) {
        try {
            ObjectTreeViewDTO treeViewDTO = JsonUtils.jsonToObject( objectJson, ObjectTreeViewDTO.class );
            if ( CollectionUtils.isEmpty( treeViewDTO.getView_nodes() ) ) {
                treeViewDTO.setView_nodes(
                        treeManager.getTree( null, getUserIdFromGeneralHeader(), null, null, getTokenFromGeneralHeader() ) );
            }
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    treeManager.saveObjectTreeView( treeViewDTO, getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveAsObjectTreeView( String objectJson ) {
        try {
            ObjectTreeViewDTO treeViewDTO = JsonUtils.jsonToObject( objectJson, ObjectTreeViewDTO.class );
            if ( CollectionUtils.isEmpty( treeViewDTO.getView_nodes() ) ) {
                treeViewDTO.setView_nodes(
                        treeManager.getTree( null, getUserIdFromGeneralHeader(), null, null, getTokenFromGeneralHeader() ) );
            }
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    treeManager.saveAsObjectTreeView( treeViewDTO, getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setObjectTreeViewAsDefault( String id ) {

        try {
            treeManager.setObjectTreeViewAsDefault( UUID.fromString( id ), getUserIdFromGeneralHeader() );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ), true );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteObjectTreeView( String id ) {

        try {
            if ( treeManager.deleteObjectTreeView( UUID.fromString( id ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateObjectTreeView( String id, String objectJson ) {

        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    treeManager.updateObjectTreeView( JsonUtils.jsonToObject( objectJson, ObjectTreeViewDTO.class ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the tree manager.
     *
     * @return the tree manager
     */
    public SuSObjectTreeManager getTreeManager() {
        return treeManager;
    }

    /**
     * Sets the tree manager.
     *
     * @param treeManager
     *         the new tree manager
     */
    public void setTreeManager( SuSObjectTreeManager treeManager ) {
        this.treeManager = treeManager;
    }

}
