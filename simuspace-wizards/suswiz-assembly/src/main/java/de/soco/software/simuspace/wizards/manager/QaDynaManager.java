package de.soco.software.simuspace.wizards.manager;

import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.wizards.model.QADyna.QADynaCombinationDTO;
import de.soco.software.simuspace.wizards.model.QADyna.QADynaFormDTO;
import de.soco.software.simuspace.wizards.model.QADyna.QADynaInputForm;
import de.soco.software.simuspace.wizards.model.QADyna.QADynaPPOForm;
import de.soco.software.simuspace.wizards.model.QADyna.QADynaParameterForm;

/**
 * The Interface WizardsManager.
 */
public interface QaDynaManager {

    /**
     * Gets qa dyna tabs ui.
     *
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     *
     * @return the qa dyna tabs ui
     */
    SubTabsItem getQaDynaTabsUI( String userId, String projectId );

    /**
     * Prepare base tab ui list.
     *
     * @param userId
     *         the user id
     * @param userName
     *         the username
     * @param token
     *         the token
     * @param projectId
     *         the project id
     *
     * @return the list
     */
    List< UIFormItem > prepareBaseTabUI( String userId, String userName, String token, String projectId );

    /**
     * Save base fields.
     *
     * @param userId
     *         the user id from general header
     * @param projectId
     *         the project id
     * @param payload
     *         the payload
     *
     * @return the qa dyna form dto
     */
    QADynaFormDTO saveBaseFields( String userId, String projectId, String payload );

    /**
     * Prepare param tab ui list.
     *
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     *
     * @return the list
     */
    List< UIFormItem > prepareParamTabUI( String userId, String userName, String projectId, String selectionId );

    /**
     * Save param fields qa dyna form dto.
     *
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     * @param dynaParamForm
     *         the dyna param form
     *
     * @return the qa dyna form dto
     */
    QADynaFormDTO saveParamFields( String userId, String projectId, String selectionId, QADynaParameterForm dynaParamForm );

    /**
     * Qa dyna options ui list.
     *
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     * @param setNumber
     *         the set number
     * @param option
     *         the option
     *
     * @return the list
     */
    List< UIFormItem > qaDynaOptionsUI( String userId, String userName, String projectId, String selectionId, String setNumber,
            String option );

    /**
     * Qa dyna file selection ui list.
     *
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     * @param setNumber
     *         the set number
     * @param whichFile
     *         the which file
     * @param option
     *         the option
     *
     * @return the list
     */
    List< UIFormItem > qaDynaFileSelectionUI( String userId, String projectId, String selectionId, String setNumber, String whichFile,
            String option );

    /**
     * Prepare ez field ui list.
     *
     * @param ezTypeName
     *         the ez type name
     *
     * @return the list
     */
    List< UIFormItem > prepareEZFieldUI( String ezTypeName );

    /**
     * Qa dyna review ui list.
     *
     * @param userId
     *         the user id from general header
     * @param projectId
     *         the project id
     *
     * @return the list
     */
    List< UIFormItem > qaDynaReviewUi( String userId, String projectId );

    /**
     * Qa dyna review table ui list.
     *
     * @param userId
     *         the user id from general header
     *
     * @return the list
     */
    List< TableColumn > qaDynaReviewTableUI( String userId );

    /**
     * Prepare input combinations for qa dyna list.
     *
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     * @param stringObjectMap
     *         the string object map
     *
     * @return the list
     */
    List< QADynaCombinationDTO > getQaDynaInputTable( String userId, String projectId, Map< String, Object > stringObjectMap );

    /**
     * Save input review fields qa dyna form dto.
     *
     * @param userId
     *         the user id from general header
     * @param token
     *         the token
     * @param projectId
     *         the project id
     * @param qaDynaInputForm
     *         the qa dyna input form
     */
    QADynaFormDTO saveInputReviewFields( String userId, String token, String projectId, QADynaInputForm qaDynaInputForm );

    /**
     * Prepare ppo tab ui list.
     *
     * @param userId
     *         the user id
     * @param userName
     *         the username
     * @param projectId
     *         the project id
     *
     * @return the list
     */
    List< UIFormItem > preparePPOTabUI( String userId, String userName, String projectId );

    /**
     * Save ppo field and submit.
     *
     * @param userId
     *         the user id
     * @param token
     *         the token
     * @param projectId
     *         the project id
     * @param qaDynaPPOForm
     *         the qa dyna ppo form
     */
    void savePPOFieldAndSubmit( String userId, String token, String projectId, QADynaPPOForm qaDynaPPOForm );

}