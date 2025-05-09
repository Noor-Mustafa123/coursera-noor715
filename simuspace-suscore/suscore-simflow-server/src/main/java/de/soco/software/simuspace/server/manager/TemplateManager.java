package de.soco.software.simuspace.server.manager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.server.dao.TemplateDAO;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.model.TemplateScanDTO;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.data.entity.TemplateEntity;

/**
 * The Interface TemplateManager.
 *
 * @author Fahad Rafi
 */
public interface TemplateManager {

    /**
     * Gets the template selector model.
     *
     * @param userIdStringFromGeneralHeader
     *         the user id string from general header
     * @param userNameFromGeneralHeader
     *         the user name from general header
     * @param json
     *         the json
     *
     * @return the template selector model
     *
     * @apiNote To be used in service calls only
     */
    Object getTemplateSelectorModel( String userIdStringFromGeneralHeader, String userNameFromGeneralHeader, String json );

    /**
     * Gets the template ui.
     *
     * @param id
     *         the id
     *
     * @return the template ui
     *
     * @apiNote To be used in service calls only
     */
    List< TableColumn > getTemplateUi( String id );

    /**
     * Gets the template list.
     *
     * @param selectionId
     *         the selectionId
     * @param filter
     *         the filter
     *
     * @return the template list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< TemplateScanDTO > getTemplateList( UUID selectionId, FiltersDTO filter );

    /**
     * Gets the template edit.
     *
     * @param userId
     *         the user id
     * @param templateId
     *         the template id
     *
     * @return the template edit
     *
     * @apiNote To be used in service calls only
     */
    Object getTemplateEdit( String userId, String templateId );

    /**
     * Creates the template UI form.
     *
     * @param id
     *         the id
     *
     * @return the list
     */
    UIForm createTemplateUIForm( String id );

    /**
     * Creates the template.
     *
     * @param selectionId
     *         the workflow id
     * @param templateScanDTO
     *         the template scan DTO
     *
     * @return the template scan DTO
     *
     * @apiNote To be used in service calls only
     */
    TemplateScanDTO createTemplate( UUID selectionId, TemplateScanDTO templateScanDTO );

    /**
     * Gets the file content.
     *
     * @param userId
     *         the user id
     * @param userUID
     *         the user UID
     * @param id
     *         the id
     *
     * @return the file content
     *
     * @apiNote To be used in service calls only
     */
    Object getFileContent( String userId, String userUID, UUID id );

    /**
     * Delete template.
     *
     * @param ids
     *         the ids
     *
     * @return the boolean
     *
     * @apiNote To be used in service calls only
     */
    Boolean deleteTemplate( List< String > ids );

    /**
     * Edits the template UI form.
     *
     * @param id
     *         the id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm editTemplateUIForm( UUID id );

    /**
     * Update template.
     *
     * @param id
     *         the id
     * @param templateScanDTO
     *         the template scan DTO
     *
     * @return the template scan DTO
     *
     * @apiNote To be used in service calls only
     */
    TemplateScanDTO updateTemplate( UUID id, TemplateScanDTO templateScanDTO );

    /**
     * Duplicate template entities by selection id.
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
    List< TemplateEntity > duplicateTemplateEntitiesBySelectionId( UUID oldSelectionId, UUID newSelectionId );

    /**
     * Gets the template DAO.
     *
     * @return the template DAO
     */
    TemplateDAO getTemplateDAO();

}
