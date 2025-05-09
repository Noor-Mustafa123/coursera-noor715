package de.soco.software.simuspace.suscore.core.manager;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.core.dao.SelectionDAO;
import de.soco.software.simuspace.suscore.core.dao.SelectionItemDAO;
import de.soco.software.simuspace.suscore.core.model.SelectionItemOrder;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionItemEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.model.SsfeSelectionDTO;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;

/**
 * The interface SelectionManager contain methods for selectionsManagerImpl provide methods to save or update and get selections.
 *
 * @author Noman Arshad
 */
public interface SelectionManager {

    /**
     * Save and update selections.
     *
     * @param userId
     *         the user id
     * @param origin
     *         the origin
     * @param filter
     *         the filter
     *
     * @return the uuid
     *
     * @apiNote To be used in service calls only
     */
    SelectionResponseUI createSelection( String userId, String origin, FiltersDTO filter );

    /**
     * Save and update selections.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param selectionOrigin
     *         the Selection Origin
     * @param filter
     *         the filter
     *
     * @return the uuid
     */
    SelectionResponseUI createSelection( EntityManager entityManager, String userId, SelectionOrigins selectionOrigin, FiltersDTO filter );

    /**
     * Save and update selections.
     *
     * @param userId
     *         the user id
     * @param origin
     *         the origin
     * @param itemId
     *         the item Id
     *
     * @return the uuid
     *
     * @apiNote To be used in service calls only
     */
    SelectionResponseUI createSelectionForSingleItem( String userId, String origin, String itemId );

    /**
     * Save and update selections.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param origin
     *         the origin
     * @param itemId
     *         the item Id
     *
     * @return the uuid
     */
    SelectionResponseUI createSelectionForSingleItem( EntityManager entityManager, String userId, String origin, String itemId );

    /**
     * Save and update selection for multiple items.
     *
     * @param userId
     *         the user id
     * @param origin
     *         the origin
     * @param itemIds
     *         the item ids
     *
     * @return the uuid
     *
     * @apiNote To be used in service calls only
     */
    SelectionResponseUI createSelectionForMultipleItems( String userId, String origin, List< String > itemIds );

    /**
     * Save and update selection for multiple items.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param origin
     *         the origin
     * @param itemIds
     *         the item ids
     *
     * @return the uuid
     */
    SelectionResponseUI createSelectionForMultipleItems( EntityManager entityManager, String userId, String origin,
            List< String > itemIds );

    /**
     * Gets the user selections by selection id.
     *
     * @param entityManager
     *         the entity manager
     * @param sId
     *         the s id
     * @param filter
     *         the filter
     *
     * @return the user selections by selection id
     */
    List< SelectionItemEntity > getUserSelectionsBySelectionIds( EntityManager entityManager, String sId, FiltersDTO filter );

    /**
     * Gets the selected id list by selection id.
     *
     * @param sId
     *         the s id
     *
     * @return the selected id list by selection id
     *
     * @apiNote To be used in service calls only
     */
    List< ? > getSelectedIdsListBySelectionId( String sId );

    /**
     * Gets the selected id list by selection id.
     *
     * @param sId
     *         the s id
     *
     * @return the selected id list by selection id
     *
     * @apiNote To be used in service calls only
     */
    List< SsfeSelectionDTO > getSelectedFilesFromSsfsSelection( EntityManager entityManager, String sId );

    /**
     * Gets selected ids list by selection id.
     *
     * @param entityManager
     *         the entity manager
     * @param sId
     *         the s id
     *
     * @return the selected ids list by selection id
     */
    List< UUID > getSelectedIdsListBySelectionId( EntityManager entityManager, String sId );

    /**
     * Prepare selection response form selection entity selection response ui.
     *
     * @param entityManager
     *         the entity manager
     * @param exitingSelection
     *         the exiting selection
     * @param origin
     *         the origin
     *
     * @return the selection response ui
     */
    SelectionResponseUI prepareSelectionResponseFormSelectionEntity( EntityManager entityManager, String exitingSelection, String origin );

    /**
     * Removes the selection.
     *
     * @param id
     *         the id
     * @param isSelectionEntity
     *         the is selection entity
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean removeSelection( UUID id, boolean isSelectionEntity );

    /**
     * Removes the selection.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     * @param isSelectionEntity
     *         the is selection entity
     *
     * @return true, if successful
     */
    boolean removeSelection( EntityManager entityManager, UUID id, boolean isSelectionEntity );

    /**
     * Adds the selection items in existing selection.
     *
     * @param selectionId
     *         the selection id
     * @param selectionItems
     *         the selection items
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    SelectionResponseUI addSelectionItemsInExistingSelection( String selectionId, FiltersDTO selectionItems );

    /**
     * Adds the selection items in existing selection.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selection id
     * @param selectionItems
     *         the selection items
     *
     * @return true, if successful
     */
    SelectionResponseUI addSelectionItemsInExistingSelection( EntityManager entityManager, String selectionId, FiltersDTO selectionItems );

    /**
     * Removes the selection items in existing selection.
     *
     * @param selectionId
     *         the selection id
     * @param selectionItems
     *         the selection items
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    SelectionResponseUI removeSelectionItemsInExistingSelection( String selectionId, FiltersDTO selectionItems );

    /**
     * Removes the selection items in existing selection.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selection id
     * @param selectionItems
     *         the selection items
     *
     * @return true, if successful
     */
    SelectionResponseUI removeSelectionItemsInExistingSelection( EntityManager entityManager, String selectionId,
            FiltersDTO selectionItems );

    /**
     * Remove all selection items.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selection id
     */
    void removeAllSelectionItems( EntityManager entityManager, String selectionId );

    /**
     * Gets the generic DTOUI.
     *
     * @param userId
     *         the user id
     * @param selectionId
     *         the selection id
     *
     * @return the generic DTOUI
     *
     * @apiNote To be used in service calls only
     */
    List< TableColumn > getGenericDTOUI( String userId, String selectionId );

    /**
     * Gets the generic DTO list.
     *
     * @param selectionId
     *         the selection id
     * @param filter
     *         the filter
     *
     * @return the generic DTO list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< ? > getGenericDTOList( String userId, String selectionId, FiltersDTO filter );

    /**
     * Gets the generic DTO list.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selection id
     * @param filter
     *         the filter
     *
     * @return the generic DTO list
     */
    FilteredResponse< ? > getGenericDTOList( EntityManager entityManager, String userId, String selectionId, FiltersDTO filter );

    /**
     * Gets the generic DTO list.
     *
     * @param filter
     *         the filter
     *
     * @return the generic DTO list
     *
     * @apiNote To be used in service calls only
     */
    List< GenericDTO > getGenericDTOList( FiltersDTO filter );

    /**
     * Send customer event.
     *
     * @param userId
     *         the user id
     * @param updateEntity
     *         the update entity
     * @param action
     *         the action
     *
     * @apiNote To be used in service calls only
     */
    void sendCustomerEvent( String userId, SuSEntity updateEntity, String action );

    /**
     * Send customer event.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param updateEntity
     *         the update entity
     * @param action
     *         the action
     */
    void sendCustomerEvent( EntityManager entityManager, String userId, SuSEntity updateEntity, String action );

    /**
     * Send Customer events on create.
     *
     * @param userId
     *         the user id
     * @param createdEntity
     *         the created entity
     * @param parentEntity
     *         the parent entity
     *
     * @apiNote To be used in service calls only
     */
    void sendCustomerEventOnCreate( String userId, SuSEntity createdEntity, SuSEntity parentEntity );

    /**
     * Send customer event for language.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param updateEntity
     *         the update entity
     */
    void sendCustomerEventForLanguage( EntityManager entityManager, String userId, UserEntity updateEntity );

    /**
     * Send Customer events on create.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param createdEntity
     *         the created entity
     * @param parentEntity
     *         the parent entity
     */
    void sendCustomerEventOnCreate( EntityManager entityManager, String userId, SuSEntity createdEntity, SuSEntity parentEntity );

    /**
     * Send event.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param id
     *         the id
     * @param parentId
     *         the parent id
     * @param action
     *         the action
     */
    void sendEvent( EntityManager entityManager, String userId, String id, UUID parentId, String action );

    /**
     * Re order selection.
     *
     * @param selectionId
     *         the selection id
     * @param orderFilter
     *         the json to object
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean reOrderSelection( String selectionId, List< SelectionItemOrder > orderFilter );

    /**
     * Re order selection.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selection id
     * @param orderFilter
     *         the json to object
     *
     * @return true, if successful
     */
    boolean reOrderSelection( EntityManager entityManager, String selectionId, List< SelectionItemOrder > orderFilter );

    /**
     * Gets the selected ids string list by selection id.
     *
     * @param sId
     *         the s id
     *
     * @return the selected ids string list by selection id
     *
     * @apiNote To be used in service calls only
     */
    List< String > getSelectedIdsStringListBySelectionId( String sId );

    /**
     * Gets the selected ids string list by selection id.
     *
     * @param entityManager
     *         the entity manager
     * @param sId
     *         the s id
     *
     * @return the selected ids string list by selection id
     */
    List< String > getSelectedIdsStringListBySelectionId( EntityManager entityManager, String sId );

    /**
     * Gets the sorted selection.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the sorted selection
     */
    List< BmwCaeBenchEntity > getSortedSelection( String selectionId );

    /**
     * Gets the sorted selection.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selection id
     *
     * @return the sorted selection
     */
    List< BmwCaeBenchEntity > getSortedSelection( EntityManager entityManager, String selectionId );

    /**
     * Gets the dummy files selection.
     *
     * @param selectionId
     *         the selection id
     * @param filterJson
     *         the filter json
     *
     * @return the dummy files selection
     *
     * @apiNote To be used in service calls only
     */
    boolean saveSelection( String selectionId, String filterJson );

    /**
     * Gets the dummy files selection.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selection id
     * @param filterJson
     *         the filter json
     *
     * @return the dummy files selection
     */
    boolean saveSelection( EntityManager entityManager, String selectionId, String filterJson );

    /**
     * Gets the sorted selection.
     *
     * @param sId
     *         the s id
     *
     * @return the sorted selection
     *
     * @apiNote To be used in service calls only
     */
    List< ? > getAllSelectionWFObjectSort( String sId );

    /**
     * Gets the selection item DAO.
     *
     * @return the selection item DAO
     */
    SelectionItemDAO getSelectionItemDAO();

    /**
     * Gets the config manager.
     *
     * @return the config manager
     */
    ObjectTypeConfigManager getConfigManager();

    /**
     * Gets the selection DAO.
     *
     * @return the selection DAO
     */
    SelectionDAO getSelectionDAO();

    /**
     * Save selection items with attributes.
     *
     * @param userId
     *         the user id
     * @param selectionId
     *         the selection id
     * @param selectionItemId
     *         the selection item id
     * @param additionalAttributesJson
     *         the additional attributes json
     * @param string
     *         the string
     *
     * @return the selection item entity
     *
     * @apiNote To be used in service calls only
     */
    SelectionItemEntity saveSelectionItemsWithAttributes( String userId, String selectionId, String selectionItemId,
            String additionalAttributesJson, String string );

    /**
     * Gets the additional attribute manager.
     *
     * @return the additional attribute manager
     */
    AdditionalAttributeManager getAdditionalAttributeManager();

    /**
     * Update selection view.
     *
     * @param userId
     *         the user id
     * @param selectionId
     *         the selection id
     * @param updateColumns
     *         the update columns
     *
     * @return the object view DTO
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO updateSelectionView( String userId, String selectionId, List< TableColumn > updateColumns );

    /**
     * Delete all selections with origin.
     *
     * @param entityManager
     *         the entity manager
     * @param origin
     *         the origin
     */
    void deleteAllSelectionsWithOrigin( EntityManager entityManager, String origin );

    /**
     * Prepare generic DTO from object entity.
     *
     * @param entityManager
     *         the entity manager
     * @param projectId
     *         the project id
     * @param susEntity
     *         the sus entity
     *
     * @return the generic DTO
     */
    GenericDTO prepareGenericDTOFromObjectEntity( EntityManager entityManager, UUID projectId, SuSEntity susEntity );

    /**
     * Duplicate selection and selection items if attribute exists selection response ui.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selection id
     * @param userId
     *         the user id
     * @param origin
     *         the origin
     *
     * @return the selection response ui
     */
    SelectionResponseUI duplicateSelectionAndSelectionItemsIfAttributeExists( EntityManager entityManager, String selectionId,
            String userId, String origin );

    /**
     * Gets selection entity by id.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the selection entity by id
     *
     * @apiNote To be used in service calls only
     */
    SelectionEntity getSelectionEntityById( String selectionId );

    /**
     * Gets selection entity by id.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the selection entity by id
     */
    SelectionEntity getSelectionEntityById( EntityManager entityManager, String selectionId );

    /**
     * Duplicate selection and selection items.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionEntity
     *         the selection entity
     * @param updateIdInJson
     *         the update id in json
     *
     * @return the selection entity
     */
    SelectionEntity duplicateSelectionAndSelectionItems( EntityManager entityManager, SelectionEntity selectionEntity,
            boolean updateIdInJson );

}
