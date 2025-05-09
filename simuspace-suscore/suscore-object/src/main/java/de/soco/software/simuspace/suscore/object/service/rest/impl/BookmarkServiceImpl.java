package de.soco.software.simuspace.suscore.object.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.data.utility.ContextUtil;
import de.soco.software.simuspace.suscore.object.manager.BookmarkManager;
import de.soco.software.simuspace.suscore.object.model.BookmarkDTO;
import de.soco.software.simuspace.suscore.object.service.rest.BookmarkService;

/**
 * The type Bookmark service.
 */
@Log4j
@Setter
public class BookmarkServiceImpl extends SuSBaseService implements BookmarkService {

    private BookmarkManager bookmarkManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createBookmark( String bookmarkJson ) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = ( JSONObject ) parser.parse( bookmarkJson );
            BookmarkDTO bookmarkModel = JsonUtils.jsonToObject( json.toJSONString(), BookmarkDTO.class );
            bookmarkManager.setTypeForBookmarkElement( getUserIdFromGeneralHeader(), bookmarkModel );
            BookmarkDTO bookmarkDtoToReturn = bookmarkManager.createBookmark( UUID.fromString( getUserIdFromGeneralHeader() ),
                    bookmarkModel );
            if ( bookmarkDtoToReturn != null ) {
                return ResponseUtils.success( "Bookmark created successfully", bookmarkDtoToReturn );
            } else {
                return ResponseUtils.failure( "Failed to create bookmark" );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateBookmark( UUID bookmarkId, String bookmarkJson ) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = ( JSONObject ) parser.parse( bookmarkJson );
            BookmarkDTO bookmarkModel = JsonUtils.jsonToObject( json.toJSONString(), BookmarkDTO.class );
            BookmarkDTO bookmarkDtoToReturn = bookmarkManager.updateBookmark( UUID.fromString( getUserIdFromGeneralHeader() ),
                    bookmarkModel );
            if ( bookmarkDtoToReturn != null ) {
                return ResponseUtils.success( "Bookmark updated successfully", bookmarkDtoToReturn );
            } else {
                return ResponseUtils.failure( "Failed to update bookmark" );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listBookmarkTableUI() {
        try {
            return ResponseUtils.success( bookmarkManager.getListOfBookmarkUITableColumns( getUserIdFromGeneralHeader() ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFilteredBookmarkList( String objectFilterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class );
            return ResponseUtils.success( bookmarkManager.getAllBookmarks( getUserNameFromGeneralHeader(), filter ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createBookmarkForm() {
        try {
            return ResponseUtils.success( bookmarkManager.createForm() );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editBookmarkForm( UUID bookmarkId ) {
        try {
            return ResponseUtils.success( bookmarkManager.editForm( bookmarkId ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getContextRouter( String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success(
                    ContextUtil.allOrderedContext( bookmarkManager.getContextRouter( getUserNameFromGeneralHeader(), filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getTreeContextRouter() {
        try {
            return ResponseUtils.success( ContextUtil.allOrderedContext( bookmarkManager.getTreeContextRouter() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteBookmark( String id, String mode ) {
        try {
            return ResponseUtils.success( "Bookmark deleted successfully",
                    bookmarkManager.deleteBookmarkBySelection( id, mode ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllViews() {
        try {
            return ResponseUtils.success( bookmarkManager.getUserObjectViewsByKey( ConstantsObjectViewKey.BOOKMARKS_TABLE_KEY,
                    getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getView( String viewId ) {
        try {
            return ResponseUtils.success( bookmarkManager.getObjectViewById( UUID.fromString( viewId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveView( String viewJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BOOKMARKS_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    bookmarkManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    bookmarkManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.BOOKMARKS_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteView( String viewId ) {
        try {
            if ( bookmarkManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
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
    public Response updateView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.BOOKMARKS_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    bookmarkManager.saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

}
