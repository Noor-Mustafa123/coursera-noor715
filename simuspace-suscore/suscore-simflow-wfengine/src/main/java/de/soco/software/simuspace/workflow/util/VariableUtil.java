package de.soco.software.simuspace.workflow.util;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.model.ObjectVariableDTO;
import de.soco.software.simuspace.suscore.common.model.TemplateVariableDTO;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.data.common.model.ParserVariableDTO;
import de.soco.software.simuspace.suscore.data.entity.RegexEntity;
import de.soco.software.simuspace.suscore.data.entity.TemplateEntity;
import de.soco.software.simuspace.suscore.data.model.CustomVariableDTO;
import de.soco.software.simuspace.suscore.data.model.DesignVariableDTO;
import de.soco.software.simuspace.suscore.data.model.ObjectiveVariableDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowModel;
import de.soco.software.simuspace.workflow.model.impl.Field;

/**
 * The Class VariableUtil.
 */
public class VariableUtil {

    /* ####################  Design Variable DTO preparators ######################  */

    /**
     * Prepare design variable DTO from parser variable DTO.
     *
     * @param parserVariableDTO
     *         the parser variable DTO
     * @param namePrefix
     *         the name prefix
     * @param workflowId
     *         the workflow id
     *
     * @return the design variable DTO
     */

    public static DesignVariableDTO prepareDesignVariableDTOFromParserVariableDTO( ParserVariableDTO parserVariableDTO, String namePrefix,
            UUID workflowId ) {
        DesignVariableDTO designVariableDTO = new DesignVariableDTO();
        designVariableDTO.setLabel( parserVariableDTO.getVariableName() );
        designVariableDTO.setName( namePrefix + parserVariableDTO.getName() );
        designVariableDTO.setType( checkVariableValueType( parserVariableDTO.getScannedValue() ) );
        designVariableDTO.setNominalValue( parserVariableDTO.getScannedValue() );
        designVariableDTO.setIndex( parserVariableDTO.getId() );
        designVariableDTO.setWorkflowId( workflowId );
        return designVariableDTO;
    }

    /**
     * Prepare design variable DTO from object variable DTO.
     *
     * @param objectVariable
     *         the object variable
     * @param namePrefix
     *         the name prefix
     * @param workflowId
     *         the workflow id
     *
     * @return the design variable DTO
     */
    public static DesignVariableDTO prepareDesignVariableDTOFromObjectVariableDTO( ObjectVariableDTO objectVariable, String namePrefix,
            UUID workflowId ) {
        DesignVariableDTO designVariableDTO = new DesignVariableDTO();
        designVariableDTO.setLabel( objectVariable.getVariableName() );
        designVariableDTO.setName( namePrefix + objectVariable.getVariableName() );
        designVariableDTO.setType(
                checkVariableValueType( objectVariable.getHighlight() != null ? objectVariable.getHighlight().getMatch() : null ) );
        designVariableDTO.setNominalValue( objectVariable.getHighlight() != null ? objectVariable.getHighlight().getMatch() : null );
        designVariableDTO.setWorkflowId( workflowId );
        designVariableDTO.setCreatedOn( objectVariable.getCreatedOn() );
        return designVariableDTO;
    }

    /**
     * Prepare design variable DTO from regex entity.
     *
     * @param regexEntity
     *         the regex entity
     * @param namePrefix
     *         the name prefix
     * @param workflowId
     *         the workflow id
     *
     * @return the design variable DTO
     */
    public static DesignVariableDTO prepareDesignVariableDTOFromRegexEntity( RegexEntity regexEntity, String namePrefix, UUID workflowId ) {

        DesignVariableDTO designVariableDTO = new DesignVariableDTO();
        designVariableDTO.setLabel( regexEntity.getVariableName() );
        designVariableDTO.setName( namePrefix + regexEntity.getVariableName() );
        designVariableDTO.setType( checkVariableValueType( regexEntity.getScannedValue() ) );
        designVariableDTO.setNominalValue( regexEntity.getScannedValue() );
        designVariableDTO.setWorkflowId( workflowId );
        return designVariableDTO;
    }

    /**
     * Prepare design variable DTO from template entity.
     *
     * @param templateEntity
     *         the template entity
     * @param namePrefix
     *         the name prefix
     * @param workflowId
     *         the workflow id
     *
     * @return the design variable DTO
     */
    public static DesignVariableDTO prepareDesignVariableDTOFromTemplateEntity( TemplateEntity templateEntity, String namePrefix,
            UUID workflowId ) {

        DesignVariableDTO designVariableDTO = new DesignVariableDTO();
        designVariableDTO.setLabel( templateEntity.getVariableName() );
        designVariableDTO.setName( namePrefix + templateEntity.getVariableName() );
        designVariableDTO.setType( checkVariableValueType( templateEntity.getMatch() ) );
        designVariableDTO.setNominalValue( templateEntity.getMatch() );
        designVariableDTO.setWorkflowId( workflowId );
        return designVariableDTO;
    }

    /**
     * Prepare design variable DTO from template variable DTO.
     *
     * @param templateVariableDTO
     *         the template variable DTO
     * @param namePrefix
     *         the name prefix
     * @param workflowId
     *         the workflow id
     *
     * @return the design variable DTO
     */
    public static DesignVariableDTO prepareDesignVariableDTOFromTemplateVariableDTO( TemplateVariableDTO templateVariableDTO,
            String namePrefix, UUID workflowId ) {

        DesignVariableDTO designVariableDTO = new DesignVariableDTO();
        designVariableDTO.setLabel( templateVariableDTO.getVariableName() );
        designVariableDTO.setName( namePrefix + templateVariableDTO.getVariableName() );
        designVariableDTO.setType( checkVariableValueType( templateVariableDTO.getMatch() ) );
        designVariableDTO.setNominalValue( templateVariableDTO.getMatch() );
        designVariableDTO.setWorkflowId( workflowId );
        designVariableDTO.setCreatedOn( templateVariableDTO.getCreatedOn() );
        return designVariableDTO;
    }

    /* ####################  Objective Variable DTO preparators ######################  */

    /**
     * Prepare objective variable DTO from parser variable DTO.
     *
     * @param parserVariableDTO
     *         the parser variable DTO
     * @param workflowId
     *         the workflow id
     *
     * @return the objective variable DTO
     */
    public static ObjectiveVariableDTO prepareObjectiveVariableDTOFromParserVariableDTO( ParserVariableDTO parserVariableDTO,
            String namePrefix, UUID workflowId ) {
        ObjectiveVariableDTO objective = new ObjectiveVariableDTO();
        objective.setLabel( parserVariableDTO.getVariableName() );
        objective.setCreatedOn( new Date() );
        objective.setName( namePrefix + parserVariableDTO.getName() );
        objective.setNominalValue( parserVariableDTO.getScannedValue() );
        objective.setWorkflowId( workflowId );
        return objective;
    }

    /**
     * Prepare objective variable DTO from object variable DTO.
     *
     * @param objectVariable
     *         the object variable
     * @param namePrefix
     *         the name prefix
     * @param workflowId
     *         the workflow id
     *
     * @return the objective variable DTO
     */
    public static ObjectiveVariableDTO prepareObjectiveVariableDTOFromObjectVariableDTO( ObjectVariableDTO objectVariable,
            String namePrefix, UUID workflowId ) {
        ObjectiveVariableDTO objectiveVariableDTO = new ObjectiveVariableDTO();
        objectiveVariableDTO.setId( objectVariable.getId() );
        objectiveVariableDTO.setLabel( objectVariable.getVariableName() );
        objectiveVariableDTO.setName( namePrefix + objectVariable.getVariableName() );
        objectiveVariableDTO.setNominalValue( objectVariable.getHighlight() != null ? objectVariable.getHighlight().getMatch() : null );
        objectiveVariableDTO.setWorkflowId( workflowId );
        objectiveVariableDTO.setCreatedOn( objectVariable.getCreatedOn() );
        return objectiveVariableDTO;
    }

    /**
     * Prepare objective variable DTO from regex entity.
     *
     * @param regexEntity
     *         the regex entity
     * @param namePrefix
     *         the name prefix
     * @param workflowId
     *         the workflow id
     *
     * @return the objective variable DTO
     */
    public static ObjectiveVariableDTO prepareObjectiveVariableDTOFromRegexEntity( RegexEntity regexEntity, String namePrefix,
            UUID workflowId ) {

        ObjectiveVariableDTO objectiveVariableDTO = new ObjectiveVariableDTO();
        objectiveVariableDTO.setId( regexEntity.getId() );
        objectiveVariableDTO.setLabel( regexEntity.getVariableName() );
        objectiveVariableDTO.setName( namePrefix + regexEntity.getVariableName() );
        objectiveVariableDTO.setNominalValue( regexEntity.getScannedValue() );
        objectiveVariableDTO.setWorkflowId( workflowId );
        return objectiveVariableDTO;
    }

    /**
     * Prepare objective variable DTO from template variable DTO.
     *
     * @param templateEntity
     *         the template entity
     * @param namePrefix
     *         the name prefix
     * @param workflowId
     *         the workflow id
     *
     * @return the objective variable DTO
     */
    public static ObjectiveVariableDTO prepareObjectiveVariableDTOFromTemplateEntity( TemplateEntity templateEntity, String namePrefix,
            UUID workflowId ) {

        ObjectiveVariableDTO objectiveVariableDTO = new ObjectiveVariableDTO();
        objectiveVariableDTO.setId( templateEntity.getId() );
        objectiveVariableDTO.setLabel( templateEntity.getVariableName() );
        objectiveVariableDTO.setName( namePrefix + templateEntity.getVariableName() );
        objectiveVariableDTO.setNominalValue( templateEntity.getMatch() );
        objectiveVariableDTO.setWorkflowId( workflowId );
        return objectiveVariableDTO;
    }

    /**
     * Prepare objective variable DTO from template variable DTO.
     *
     * @param templateVariableDTO
     *         the template variable DTO
     * @param namePrefix
     *         the name prefix
     * @param workflowId
     *         the workflow id
     *
     * @return the objective variable DTO
     */
    public static ObjectiveVariableDTO prepareObjectiveVariableDTOFromTemplateVariableDTO( TemplateVariableDTO templateVariableDTO,
            String namePrefix, UUID workflowId ) {

        ObjectiveVariableDTO objectiveVariableDTO = new ObjectiveVariableDTO();
        objectiveVariableDTO.setId( templateVariableDTO.getId() );
        objectiveVariableDTO.setLabel( templateVariableDTO.getVariableName() );
        objectiveVariableDTO.setName( namePrefix + templateVariableDTO.getVariableName() );
        objectiveVariableDTO.setNominalValue( templateVariableDTO.getMatch() );
        objectiveVariableDTO.setWorkflowId( workflowId );
        objectiveVariableDTO.setCreatedOn( templateVariableDTO.getCreatedOn() );
        return objectiveVariableDTO;
    }

    /* ####################  Custom Variable DTO preparators ######################  */

    /**
     * Prepare custom variable DTO from parser variable DTO.
     *
     * @param parserVariableDTO
     *         the parser variable DTO
     * @param elementName
     *         the element name
     * @param fieldName
     *         the field name
     * @param workflowId
     *         the workflow id
     *
     * @return the custom variable DTO
     */
    public static CustomVariableDTO prepareCustomVariableDTOFromParserVariableDTO( ParserVariableDTO parserVariableDTO, String elementName,
            String fieldName, UUID workflowId ) {
        CustomVariableDTO customVariableDTO = new CustomVariableDTO();
        customVariableDTO.setLabel( parserVariableDTO.getVariableName() );
        customVariableDTO.setName( parserVariableDTO.getName() );
        customVariableDTO.setType( checkVariableValueType( parserVariableDTO.getScannedValue() ) );
        customVariableDTO.setValue( parserVariableDTO.getScannedValue() );
        customVariableDTO.setWorkflowId( workflowId );
        customVariableDTO.setElementName( elementName );
        customVariableDTO.setFieldName( fieldName );
        return customVariableDTO;

    }

    /**
     * Prepare custom variable DTO from object variable DTO.
     *
     * @param objectVariable
     *         the object variable
     * @param elementName
     *         the element name
     * @param fieldName
     *         the field name
     * @param workflowId
     *         the workflow id
     *
     * @return the custom variable DTO
     */
    public static CustomVariableDTO prepareCustomVariableDTOFromObjectVariableDTO( ObjectVariableDTO objectVariable, String elementName,
            String fieldName, UUID workflowId ) {
        CustomVariableDTO customVariableDTO = new CustomVariableDTO();
        String name = objectVariable.getHighlight() != null ? objectVariable.getHighlight().getMatch() : null;
        customVariableDTO.setLabel( name );
        customVariableDTO.setName( name );
        customVariableDTO.setType( checkVariableValueType( objectVariable.getVariableName() ) );
        customVariableDTO.setValue( objectVariable.getVariableName() );
        customVariableDTO.setWorkflowId( workflowId );
        customVariableDTO.setCreatedOn( objectVariable.getCreatedOn() );
        customVariableDTO.setElementName( elementName );
        customVariableDTO.setFieldName( fieldName );
        return customVariableDTO;
    }

    /**
     * Prepare custom variable DTO from regex entity.
     *
     * @param regexEntity
     *         the regex entity
     * @param elementName
     *         the element name
     * @param fieldName
     *         the field name
     * @param workflowId
     *         the workflow id
     *
     * @return the custom variable DTO
     */
    public static CustomVariableDTO prepareCustomVariableDTOFromRegexEntity( RegexEntity regexEntity, String elementName, String fieldName,
            UUID workflowId ) {
        CustomVariableDTO customVariableDTO = new CustomVariableDTO();
        customVariableDTO.setLabel( regexEntity.getScannedValue() );
        customVariableDTO.setName( regexEntity.getScannedValue() );
        customVariableDTO.setType( checkVariableValueType( regexEntity.getVariableName() ) );
        customVariableDTO.setValue( regexEntity.getVariableName() );
        customVariableDTO.setWorkflowId( workflowId );
        customVariableDTO.setElementName( elementName );
        customVariableDTO.setFieldName( fieldName );
        return customVariableDTO;
    }

    /**
     * Prepare custom variable DTO from template entity.
     *
     * @param templateEntity
     *         the template entity
     * @param elementName
     *         the element name
     * @param fieldName
     *         the field name
     * @param workflowId
     *         the workflow id
     *
     * @return the custom variable DTO
     */
    public static CustomVariableDTO prepareCustomVariableDTOFromTemplateEntity( TemplateEntity templateEntity, String elementName,
            String fieldName, UUID workflowId ) {
        CustomVariableDTO customVariableDTO = new CustomVariableDTO();
        customVariableDTO.setLabel( templateEntity.getMatch() );
        customVariableDTO.setName( templateEntity.getMatch() );
        customVariableDTO.setType( checkVariableValueType( templateEntity.getVariableName() ) );
        customVariableDTO.setValue( templateEntity.getVariableName() );
        customVariableDTO.setWorkflowId( workflowId );
        customVariableDTO.setElementName( elementName );
        customVariableDTO.setFieldName( fieldName );
        return customVariableDTO;
    }

    /**
     * Prepare custom variable DTO from template variable DTO.
     *
     * @param templateVariableDTO
     *         the template variable DTO
     * @param elementName
     *         the element name
     * @param fieldName
     *         the field name
     * @param workflowId
     *         the workflow id
     *
     * @return the custom variable DTO
     */
    public static CustomVariableDTO prepareCustomVariableDTOFromTemplateVariableDTO( TemplateVariableDTO templateVariableDTO,
            String elementName, String fieldName, UUID workflowId ) {

        CustomVariableDTO customVariableDTO = new CustomVariableDTO();
        customVariableDTO.setLabel( templateVariableDTO.getMatch() );
        customVariableDTO.setName( templateVariableDTO.getMatch() );
        customVariableDTO.setType( checkVariableValueType( templateVariableDTO.getVariableName() ) );
        customVariableDTO.setValue( templateVariableDTO.getVariableName() );
        customVariableDTO.setWorkflowId( workflowId );
        customVariableDTO.setCreatedOn( templateVariableDTO.getCreatedOn() );
        customVariableDTO.setElementName( elementName );
        customVariableDTO.setFieldName( fieldName );
        return customVariableDTO;
    }

    /* ####################  General methods ######################  */

    /**
     * Check type.
     *
     * @param match
     *         the match
     *
     * @return the string
     */
    public static String checkVariableValueType( String match ) {
        if ( match == null || match.isEmpty() ) {
            return null;
        }

        try {
            Integer.parseInt( match );
            return "Integer";
        } catch ( NumberFormatException e ) {
            // ignored exception
        }
        try {
            Float.parseFloat( match );
            return "Float";
        } catch ( NumberFormatException e ) {
            // ignored exception
        }

        return "String";
    }

    /**
     * Replace custom variables in workflow model.
     *
     * @param fieldName
     *         the field name
     * @param customVariables
     *         the custom variables
     * @param elementName
     *         the element name
     * @param workflowModel
     *         the workflow model
     */
    public static void replaceCustomVariablesInWorkflowModel( String fieldName, List< CustomVariableDTO > customVariables,
            String elementName, WorkflowModel workflowModel ) {
        if ( null != workflowModel.getNodes() && !customVariables.isEmpty() ) {
            workflowModel.getNodes().stream().filter( node -> elementName.equals( node.getData().getName() ) )
                    .forEach( node -> node.getData().getFields().stream().filter( field -> field.getName().equalsIgnoreCase( fieldName ) )
                            .forEach( field -> ( ( Field< String > ) field ).setValue( JsonUtils.toJson( customVariables ) ) ) );
        }
    }

}
