package de.soco.software.simuspace.server.manager;

import javax.persistence.EntityManager;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.parser.model.ParserDTO;
import de.soco.software.simuspace.suscore.common.parser.model.ParserPartDTO;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.data.common.dao.ParserSchemaDAO;
import de.soco.software.simuspace.suscore.data.common.model.ParserVariableDTO;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.ParserEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.CustomVariableDTO;

/**
 * The Interface ParserManager.
 *
 * @author noman arshad
 */
public interface ParserManager {

    /**
     * Gets the parser object.
     *
     * @param userId
     *         the user id
     * @param userUID
     *         the user UID
     * @param json
     *         the json
     *
     * @return the parser object
     *
     * @throws FileNotFoundException
     *         the file not found exception
     * @apiNote To be used in service calls only
     */
    Object getParserObject( String userId, String userUID, String json ) throws FileNotFoundException;

    /**
     * Gets the parser available ui.
     *
     * @param userId
     *         the user id
     * @param parserId
     *         the parser id
     *
     * @return the parser available ui
     */
    List< TableColumn > getParserAvailableUi( String userId, String parserId );

    /**
     * Gets the parser selected ui.
     *
     * @param userId
     *         the user id
     * @param parserId
     *         the parser id
     *
     * @return the parser selected ui
     */
    List< TableColumn > getParserSelectedUi( String userId, String parserId );

    /**
     * Gets the parser edit.
     *
     * @param userId
     *         the user id
     * @param parserId
     *         the parser id
     *
     * @return the parser edit
     *
     * @apiNote To be used in service calls only
     */
    Object getParserEdit( String userId, String parserId );

    /**
     * Gets the parser available list.
     *
     * @param userId
     *         the user id
     * @param parserId
     *         the parser id
     * @param filter
     *         the filter
     *
     * @return the parser available list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< ParserVariableDTO > getParserAvailableList( String userId, String parserId, FiltersDTO filter );

    /**
     * Gets the parser selected list.
     *
     * @param userId
     *         the user id
     * @param parserId
     *         the parser id
     * @param filter
     *         the filter
     *
     * @return the parser selected list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< ParserVariableDTO > getParserSelectedList( String userId, String parserId, FiltersDTO filter );

    /**
     * Gets the parser ui.
     *
     * @param parserId
     *         the parser id
     *
     * @return the parser ui
     */
    List< TableColumn > getParserUi( String parserId );

    /**
     * Gets the parser list.
     *
     * @param userId
     *         the user id
     * @param parserId
     *         the parser id
     * @param filter
     *         the filter
     *
     * @return the parser list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< ParserVariableDTO > getParserList( String userId, String parserId, FiltersDTO filter );

    /**
     * Gets the parser object selected UI.
     *
     * @param userId
     *         the userId
     * @param objectSelected
     *         the object selected
     * @param parserId
     *         the parser id
     *
     * @return the parser object selected UI
     *
     * @apiNote To be used in service calls only
     */
    UIForm getParserObjectSelectedUI( String userId, String objectSelected, String parserId );

    /**
     * Gets the parser object CB 2 selector UI.
     *
     * @param userId
     *         the user id
     * @param typeSelected
     *         the type selected
     * @param parserId
     *         the parser id
     *
     * @return the parser object CB 2 selected UI
     */
    UIForm getParserObjectCB2SelectorUI( String userId, String typeSelected, String parserId );

    /**
     * Gets the parser object selected UI for CB 2 element.
     *
     * @param string
     *         the string
     * @param objectSelected
     *         the object selected
     *
     * @return the parser object selected UI for CB 2 element
     */
    Object getParserObjectSelectedUIForCB2Element( String string, String objectSelected );

    /**
     * Gets the parser object available context.
     *
     * @param string
     *         the string
     * @param parserId
     *         the parser id
     * @param filter
     *         the filter
     *
     * @return the parser object available context
     *
     * @apiNote To be used in service calls only
     */
    Object getParserObjectAvailableContext( String string, String parserId, FiltersDTO filter );

    /**
     * Gets the parser object selected context.
     *
     * @param string
     *         the string
     * @param parserId
     *         the parser id
     * @param filter
     *         the filter
     *
     * @return the parser object selected context
     *
     * @apiNote To be used in service calls only
     */
    Object getParserObjectSelectedContext( String string, String parserId, FiltersDTO filter );

    /**
     * Adds the entry to parser selected list.
     *
     * @param string
     *         the string
     * @param parserId
     *         the parser id
     * @param selectionId
     *         the selection id
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object addEntryToParserSelectedList( String string, String parserId, String selectionId );

    /**
     * Save entry to parser available and selected list.
     *
     * @param string
     *         the string
     * @param parserId
     *         the parser id
     * @param parserVariableDTO
     *         the parser variable DTO
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object saveEntryToParserAvailableAndSelectedList( String string, String parserId, ParserVariableDTO parserVariableDTO );

    /**
     * Gets the edits the form for selected list.
     *
     * @param string
     *         the string
     * @param parserId
     *         the parser id
     * @param selectionId
     *         the selection id
     *
     * @return the edits the form for selected list
     *
     * @apiNote To be used in service calls only
     */
    Object getEditFormForSelectedList( String string, String parserId, String selectionId );

    /**
     * Removes the entry from parser selected list.
     *
     * @param string
     *         the string
     * @param parserId
     *         the parser id
     * @param selectionId
     *         the selection id
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    Object removeEntryFromParserSelectedList( String string, String parserId, String selectionId );

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    ObjectViewManager getObjectViewManager();

    /**
     * Gets the parser schema DAO.
     *
     * @return the parser schema DAO
     */
    ParserSchemaDAO getParserSchemaDAO();

    /**
     * Gets the selected parser entries list by parser id.
     *
     * @param entityManager
     *         the entity manager
     * @param parserId
     *         the parser id
     *
     * @return the selected parser entries list by parser id
     */
    List< ParserVariableDTO > getSelectedParserEntriesListByParserId( EntityManager entityManager, String parserId );

    /**
     * Gets the selected parser entries list by parser id.
     *
     * @param parserEntities
     *         the list of parserEntities
     *
     * @return the selected parser entries list by parser id
     */
    List< ParserVariableDTO > getSelectedParserEntriesList( List< ParserEntity > parserEntities );

    /**
     * Gets the selected parser entries list custom variables.
     *
     * @param parserEntity
     *         the parser entities
     *
     * @return the selected parser entries list custom variables
     */
    List< CustomVariableDTO > getSelectedParserEntriesListCustomVariables( ParserEntity parserEntity );

    /**
     * Gets the selected internal object file path.
     *
     * @param entityManager
     *         the entity manager
     * @param parserType
     *         the parser type
     * @param selectionObject
     *         the selection object
     *
     * @return the selected internal object file path
     */
    DocumentEntity getSelectedInternalObjectFilePath( EntityManager entityManager, String parserType, String selectionObject );

    /**
     * Gets the form data from json.
     *
     * @param json
     *         the json
     *
     * @return the form data from json
     */
    Map< String, String > getFormDataFromJson( String json );

    /**
     * Convert parser variable DTO list to parser part DTO.
     *
     * @param allVariableList
     *         the all variable list
     *
     * @return the list
     */
    List< ParserPartDTO > convertParserVariableDTOListToParserPartDTO( List< ParserVariableDTO > allVariableList );

    /**
     * Gets the server file selection object by json.
     *
     * @param json
     *         the json
     *
     * @return the server file selection object by json
     */
    Map< String, String > getServerFileSelectionObjectByJson( String json );

    /**
     * Prepare part DTO from parser part DTO list.
     *
     * @param parserPartDTOList
     *         the parser part DTO list
     *
     * @return the parser DTO
     */
    ParserDTO preparePartDTOFromParserPartDTOList( List< ParserPartDTO > parserPartDTOList );

    /**
     * Prepare objectivevariable for engine.
     *
     * @param string
     *         the string
     * @param userNameFromGeneralHeader
     *         the user name from general header
     * @param parserId
     *         the parser id
     * @param filepath
     *         the filepath
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    List< ParserVariableDTO > prepareObjectivevariableForEngine( String string, String userNameFromGeneralHeader, String parserId,
            String filepath );

    /**
     * Gets the parser selected file path by parser id.
     *
     * @param userUID
     *         the user uid
     * @param parserId
     *         the parser id
     *
     * @return the parser selected file path by parser id
     *
     * @apiNote To be used in service calls only
     */
    String getParserSelectedFilePathByParserId( String userUID, String parserId );

    /**
     * Duplicate parser entity.
     *
     * @param entityManager
     *         the entity manager
     * @param value
     *         the value
     *
     * @return the parser entity
     */
    ParserEntity duplicateParserEntity( EntityManager entityManager, String value );

}
