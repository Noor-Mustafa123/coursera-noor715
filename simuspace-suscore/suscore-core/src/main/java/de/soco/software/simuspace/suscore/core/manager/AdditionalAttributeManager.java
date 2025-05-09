package de.soco.software.simuspace.suscore.core.manager;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.model.WorkFlowAdditionalAttributeDTO;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionItemEntity;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;

/**
 * The Interface AdditionalAttributeManager.
 *
 * @author noman arshad
 */
public interface AdditionalAttributeManager {

    /**
     * Gets the selection attribute UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     *
     * @return the selection attribute UI
     *
     * @throws Exception
     *         the exception
     * @apiNote To be used in service calls only
     */
    TableUI getSelectionAttributeUI( String userIdFromGeneralHeader, String selectionId ) throws Exception;

    /**
     * Gets the selection attribute list.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the selection attribute list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< WorkFlowAdditionalAttributeDTO > getSelectionAttributeList( String userIdFromGeneralHeader, String selectionId,
            FiltersDTO filtersDTO );

    /**
     * Save attribute ui with selection id.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     * @param atrib
     *         the atrib
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object saveAttributeUiWithSelectionId( String userIdFromGeneralHeader, String selectionId, String atrib );

    /**
     * Adds the additional attribute ui form.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     *
     * @return the object
     *
     * @throws Exception
     *         the exception
     */
    Object addAdditionalAttributeUiForm( String userIdFromGeneralHeader, String selectionId ) throws Exception;

    /**
     * Gets the attribute context.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     * @param jsonToObject
     *         the json to object
     *
     * @return the attribute context
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getAttributeContext( String userIdFromGeneralHeader, String selectionId, FiltersDTO jsonToObject );

    /**
     * Gets the edits the attribute form.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     * @param attribSelectionId
     *         the attrib selection id
     *
     * @return the edits the attribute form
     *
     * @apiNote To be used in service calls only
     */
    Object getEditAttributeForm( String userIdFromGeneralHeader, String selectionId, String attribSelectionId );

    /**
     * Delete attribute by selection.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     * @param attribSelectionId
     *         the attrib selection id
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object deleteAttributeBySelection( String userIdFromGeneralHeader, String selectionId, String attribSelectionId );

    /**
     * Update attribute W ith selection.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     * @param attribSelectionId
     *         the attrib selection id
     * @param jsonAtrib
     *         the json atrib
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object updateAttributeWIthSelection( String userIdFromGeneralHeader, String selectionId, String attribSelectionId, String jsonAtrib );

    /**
     * Save attribute values with selection.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     * @param attribSelectionId
     *         the attrib selection id
     * @param jsonFilter
     *         the json filter
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object saveAttributeValuesWithSelection( String userIdFromGeneralHeader, String selectionId, String attribSelectionId,
            String jsonFilter );

    /**
     * Save ssfe attribute values with selection.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     * @param jsonTemplate
     *         the json template
     *
     * @return the object
     */
    Object saveSsfeAttributeValuesWithSelection( String userIdFromGeneralHeader, String selectionId, String jsonTemplate );

    /**
     * Gets the attribute ui create option.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     * @param attribSelectionId
     *         the attrib selection id
     * @param option
     *         the option
     *
     * @return the attribute ui create option
     *
     * @throws Exception
     *         the exception
     * @apiNote To be used in service calls only
     */
    Object getAttributeUiCreateOption( String userIdFromGeneralHeader, String selectionId, String attribSelectionId, String option )
            throws Exception;

    /**
     * Gets the generic DTO list with additional attribute.
     *
     * @param selectionId
     *         the selection id
     * @param filter
     *         the filter
     *
     * @return the generic DTO list with additional attribute
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< ? > getGenericDTOListWithAdditionalAttribute( String selectionId, FiltersDTO filter );

    /**
     * Adds the additional attribut to table columns.
     *
     * @param entity
     *         the entity
     * @param tableColumnList
     *         the table column list
     */
    void addAdditionalAttributToTableColumns( SelectionEntity entity, List< TableColumn > tableColumnList );

    /**
     * Prepare table col for additional attrib.
     *
     * @param orderNum
     *         the order num
     * @param workFlowAdditionalAttribute
     *         the work flow additional attribute
     *
     * @return the table column
     */
    TableColumn prepareTableColForAdditionalAttrib( int orderNum, WorkFlowAdditionalAttributeDTO workFlowAdditionalAttribute );

    /**
     * Extract highes order number.
     *
     * @param tableColumnList
     *         the table column list
     *
     * @return the int
     */
    int extractHighesOrderNumber( List< TableColumn > tableColumnList );

    /**
     * Gets the work flow additional attribute dto by name.
     *
     * @param attribList
     *         the attrib list
     * @param sAttrib
     *         the s attrib
     * @param isChanged
     *         the is changed
     * @param selectionItemEntityList
     *         the selection item entity list
     *
     * @return the work flow additional attribute dto by name
     */
    WorkFlowAdditionalAttributeDTO getWorkFlowAdditionalAttributeDtoByName( List< WorkFlowAdditionalAttributeDTO > attribList,
            WorkFlowAdditionalAttributeDTO sAttrib, AtomicInteger isChanged, Set< SelectionItemEntity > selectionItemEntityList );

}
