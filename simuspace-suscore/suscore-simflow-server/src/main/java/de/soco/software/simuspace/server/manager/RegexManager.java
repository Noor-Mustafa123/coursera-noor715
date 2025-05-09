package de.soco.software.simuspace.server.manager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.server.dao.RegexDAO;
import de.soco.software.simuspace.server.model.RegexScanDTO;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.data.entity.RegexEntity;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;

/**
 * The interface Regex manager.
 */
public interface RegexManager {

    /**
     * Gets regex edit.
     *
     * @param userId
     *         the user id
     * @param regexId
     *         the regex id
     *
     * @return the regex edit
     *
     * @apiNote To be used in service calls only
     */
    Object getRegexEdit( String userId, String regexId );

    /**
     * Gets regex dropdown selected.
     *
     * @param selectedOption
     *         the selected option
     * @param wfId
     *         the wf id
     * @param userId
     *         the user id
     *
     * @return the regex dropdown selected
     *
     * @apiNote To be used in service calls only
     */
    UIForm getRegexDropdownSelected( String selectedOption, String wfId, UUID userId );

    /**
     * Gets regex dropdown selector.
     *
     * @param selectedOption
     *         the selected option
     * @param wfId
     *         the wf id
     *
     * @return the regex dropdown selected
     */
    UIForm getRegexCb2SelectorUI( String selectedOption, String wfId );

    /**
     * Gets file content.
     *
     * @param userId
     *         the user id
     * @param userUID
     *         the user uid
     * @param id
     *         the id
     *
     * @return the file content
     */
    Object getFileContent( String userId, String userUID, UUID id );

    /**
     * Gets regex ui.
     *
     * @param selectionId
     *         the selectionId
     *
     * @return the regex ui
     *
     * @apiNote To be used in service calls only
     */
    List< TableColumn > getRegexUi( String selectionId );

    /**
     * Create regex regex scan dto.
     *
     * @param selectionId
     *         the workflow id
     * @param regexScanDTO
     *         the regex scan dto
     *
     * @return the regex scan dto
     *
     * @apiNote To be used in service calls only
     */
    RegexScanDTO createRegex( UUID selectionId, RegexScanDTO regexScanDTO );

    /**
     * Update regex regex scan dto.
     *
     * @param id
     *         the id
     * @param regexScanDTO
     *         the regex scan dto
     *
     * @return the regex scan dto
     *
     * @apiNote To be used in service calls only
     */
    RegexScanDTO updateRegex( UUID id, RegexScanDTO regexScanDTO );

    /**
     * Gets regex.
     *
     * @param id
     *         the id
     *
     * @return the regex
     *
     * @apiNote To be used in service calls only
     */
    RegexScanDTO getRegex( UUID id );

    /**
     * Gets regex list.
     *
     * @param selectionId
     *         the selection id
     * @param filter
     *         the filter
     *
     * @return the regex list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< RegexScanDTO > getRegexList( UUID selectionId, FiltersDTO filter );

    /**
     * Delete regex boolean.
     *
     * @param ids
     *         the ids
     *
     * @return the boolean
     *
     * @apiNote To be used in service calls only
     */
    Boolean deleteRegex( List< String > ids );

    /**
     * Gets regex selector model.
     *
     * @param userIdStringFromGeneralHeader
     *         the user id string from general header
     * @param userNameFromGeneralHeader
     *         the user name from general header
     * @param json
     *         the json
     *
     * @return the regex selector model
     *
     * @apiNote To be used in service calls only
     */
    Object getRegexSelectorModel( String userIdStringFromGeneralHeader, String userNameFromGeneralHeader, String json );

    /**
     * Duplicate regex entities by selection id.
     *
     * @param entityManager
     *         the entity manager
     * @param oldSelectionId
     *         the old selection id
     * @param newSelectionId
     *         the new selection id
     *
     * @return the list
     */
    List< RegexEntity > duplicateRegexEntitiesBySelectionId( UUID oldSelectionId, UUID newSelectionId );

    /**
     * Gets regex context.
     *
     * @param id
     *         the id
     *
     * @return the regex context
     */
    List< ContextMenuItem > getRegexContext( UUID id );

    /**
     * Create regex ui form list.
     *
     * @param id
     *         the id
     *
     * @return the list
     */
    UIForm createRegexUIForm( String id );

    /**
     * Edit regex ui form list.
     *
     * @param id
     *         the id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm editRegexUIForm( UUID id );

    /**
     * Gets regex dao.
     *
     * @return the regex dao
     */
    RegexDAO getRegexDAO();

}
