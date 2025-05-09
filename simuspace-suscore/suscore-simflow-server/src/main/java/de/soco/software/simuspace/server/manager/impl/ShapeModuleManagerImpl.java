package de.soco.software.simuspace.server.manager.impl;

import javax.ws.rs.core.UriInfo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.manager.BaseManager;
import de.soco.software.simuspace.server.manager.ShapeModuleManager;
import de.soco.software.simuspace.server.model.jsonschema.SMJSONSchema;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.InputTableFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SectionFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.suscore.jsonschema.model.Rules;

@Log4j2
public class ShapeModuleManagerImpl extends BaseManager implements ShapeModuleManager {

    String definitions = "$defs";

    int numMultipleFields = 0;

    String parentField = null;

    Boolean flag = null;

    Boolean internalFlag = Boolean.FALSE;

    List< String > op = new ArrayList<>();

    private String regexPattern;

    private boolean rule;

    String typeOfEntry = null;

    String nameOfEntry;

    List< String > nameOfEntries = Arrays.asList( "geometry_list", "analysis_wrapper_list", "mesh_motion_list", "parameterization_list",
            "mapper_list", "coupling_list", "optimization" );

    List< String > geometry_name = new ArrayList<>();

    List< String > wrapper_name = new ArrayList<>();

    List< String > param_name = new ArrayList<>();

    List< String > opt_name = new ArrayList<>();

    List< String > geometry_name_options = new ArrayList<>();

    List< String > wrapper_name_options = new ArrayList<>();

    List< String > opt_name_options = new ArrayList<>();

    List< String > responses_name = new ArrayList<>();

    List< String > responses_name_options = new ArrayList<>();

    List< String > submeshes_name_options = new ArrayList<>();

    private List< String > submeshes_name = new ArrayList<>();

    WorkflowManagerImpl workflowManager = new WorkflowManagerImpl();

    @Override
    public SMJSONSchema getSMJsonSchema() {
        SMJSONSchema smjsonSchema = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            // smjsonSchema = mapper.readValue( Paths.get( workflowManager.downloadShapeModuleSchema().toString() ).toFile(),
            // SMJSONSchema.class );
            smjsonSchema = mapper.readValue( Paths.get( PropertiesManager.getSMSchemaPath() ).toFile(), SMJSONSchema.class );
        } catch ( Exception ex ) {
            throw new SusException( ex.getMessage() );
        }
        return smjsonSchema;
    }

    @Override
    public List< UIFormItem > getSMInCustomFlag( UriInfo uriInfo ) {
        List< UIFormItem > listUserDirectory = new ArrayList<>();
        log.debug( "entered in getCustomFlagUI method" );

        SelectFormItem item = prepareFieldShowHiddenOrNot();
        listUserDirectory.add( item );

        return listUserDirectory;
    }

    private SelectFormItem prepareFieldShowHiddenOrNot() {
        SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        List< SelectOptionsUI > options = createBooleanOptionsList( "" );

        return setUIFormFieldsParams( item, options, "workflow/plugin/ShapeModule/ShapeModule.{__value__}", "Show Non-Required Fields",
                "show_non-required_fields", Boolean.FALSE, prepareRule( Boolean.TRUE, null ), Boolean.FALSE.toString() );
    }

    private SelectFormItem setUIFormFieldsParams( SelectFormItem item, List< SelectOptionsUI > options, String bindFrom, String label,
            String name, Boolean multiple, Map< String, Object > required, String value ) {
        item.setOptions( options );
        item.setBindFrom( bindFrom );
        item.setLabel( label );
        item.setName( name );
        item.setType( "select" );
        item.setReadonly( false );
        item.setMultiple( multiple );
        item.setRules( required );
        item.setValue( value );
        // item.setValue( null );

        Map< String, Object > message = new HashMap<>();
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        item.setMessages( message );
        return item;
    }

    private SelectFormItem prepareFieldforMultipleSameFields( boolean requiredornot, Object entry, int num, String myParent ) {
        SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        setUIFormFieldsParams( item, getNumbersInSelectField(),
                "workflow/plugin/ShapeModule/" + entry + ".{__value__}" + ( myParent != null ? "." + myParent : "" ), "Multiple_" + entry,
                "Multiple_" + ( myParent != null ? myParent : "" ) + entry, Boolean.FALSE, prepareRule( requiredornot, null ), "1" );

        return item;
    }

    private List< SelectOptionsUI > getNumbersInSelectField() {
        List< SelectOptionsUI > options = new ArrayList<>();
        List< String > optionsAsList = new ArrayList<>();

        for ( int i = 1; i <= 10; i++ ) {
            optionsAsList.add( String.valueOf( i ) );
        }

        IntStream.range( 0, optionsAsList.size() ).forEach( index -> {
            SelectOptionsUI objectItem = new SelectOptionsUI();
            objectItem.setId( optionsAsList.get( index ) );
            objectItem.setName( optionsAsList.get( index ) );
            options.add( objectItem );
        } );
        return options;
    }

    @Override
    public List< UIFormItem > getCustomFlagPluginUI( String parent, String childs ) {
        List< UIFormItem > listUserDirectory = new ArrayList<>();

        com.fasterxml.jackson.databind.JsonNode smjsonNode = null;
        try {
            // aa = JsonUtils.toJsonNode( new FileReader( Paths.get( workflowManager.downloadShapeModuleSchema().toString() ).toFile() ) );
            smjsonNode = JsonUtils.toJsonNode( new FileReader( Paths.get( PropertiesManager.getSMSchemaPath().toString() ).toFile() ) );
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        }
        Object parsedJsonObject = Configuration.defaultConfiguration().jsonProvider().parse( smjsonNode.toString() );

        List< String > numberList = new ArrayList<>();
        for ( int i = 1; i <= 10; i++ ) {
            numberList.add( Integer.toString( i ) );
        }

        if ( childs.contains( "ShapeModule" ) ) {
            geometry_name_options = new ArrayList<>();
            wrapper_name_options = new ArrayList<>();
            responses_name_options = new ArrayList<>();
            submeshes_name_options = new ArrayList<>();
            internalFlag = Boolean.FALSE;
        } else {
            if ( nameOfEntries.contains( childs ) ) {
                listUserDirectory = extractFieldsFromSchema( childs, listUserDirectory );
            } else if ( numberList.contains( childs ) ) {
                for ( int index = 1; index <= Integer.parseInt( childs ); index++ ) {
                    SelectFormItem selectFormItem = prepareSelectUI( "text", "File Extension" + index, "File_Extension" + index, null,
                            Boolean.FALSE, prepareRule( rule, regexPattern ), prepareMessage(), null, null, Collections.emptyMap(), null );
                    listUserDirectory.add( selectFormItem );
                }
            } else {
                getFieldsForParents( childs, listUserDirectory, parsedJsonObject );
            }
        }
        return listUserDirectory;
    }

    private void removeNonRequiredField( List< UIFormItem > listUserDirectory ) {
        Iterator< UIFormItem > iterator = listUserDirectory.iterator();
        while ( iterator.hasNext() ) {
            UIFormItem uiFormItem = iterator.next();
            if ( null != uiFormItem.getRules() && uiFormItem.getRules().get( "required" ).toString().equals( Boolean.FALSE.toString() ) ) {
                iterator.remove();
            }
        }
    }

    private List< UIFormItem > extractFieldsFromSchema( String child, List< UIFormItem > listUserDirectory ) {
        SMJSONSchema smjsonSchema = getSMJsonSchema();
        List< String > requiredparams = smjsonSchema.getRequired();

        smjsonSchema.getProperties().entrySet().stream().forEach( e -> {
            // Section
            boolean existsInRequiredParams = requiredparams.contains( e.getKey() );
            Boolean isMultiple = e.getKey().contains( "_list" ) ? Boolean.TRUE : Boolean.FALSE;
            typeOfEntry = e.getKey().contains( "_list" ) ? "array" : "object";
            nameOfEntry = typeOfEntry.equals( "object" ) ? e.getKey() : null;

            if ( e.getKey().equalsIgnoreCase( child ) ) {
                SectionFormItem sectionFormItem = prepareSection( existsInRequiredParams, e.getKey(), "section" );
                listUserDirectory.add( sectionFormItem );

                SelectFormItem item = prepareField( smjsonSchema, e.getKey(), op, isMultiple );
                listUserDirectory.add( item );
            }

        } );
        return listUserDirectory;
    }

    private void getFieldsForParents( String childs, List< UIFormItem > listUserDirectory, Object ss ) {
        String[] output = childs.split( "\\." );
        parentField = output[ 0 ];

        /*
         * if (output.length > 3) { String[] lastThreeParts = Arrays.copyOfRange(output,
         * output.length - 3, output.length);
         *
         * StringBuilder firstPart = new StringBuilder(output[0]); for (int i = 1; i <=
         * output.length - 4; i++) { firstPart.append(".").append(output[i]); }
         * lastThreeParts[0] = firstPart.toString(); output = lastThreeParts; }
         */

        Integer result = ( childs.contains( "." ) && output.length > 1 ) ? isInteger( output[ 1 ] ) : null;

        if ( result != null ) {
            numMultipleFields = result;
            if ( output.length >= 3 ) {
                try {
                    flag = Boolean.parseBoolean( output[ 2 ] );
                } catch ( Exception e ) {
                    flag = flag;
                }
            } else if ( output.length == 2 ) {
                try {
                    flag = Boolean.parseBoolean( output[ 1 ] );
                } catch ( Exception e ) {
                    flag = flag;
                }
            } else {
                flag = Boolean.TRUE;
            }
        }

        createFieldsForEachField( parentField, listUserDirectory, ss, output );
    }

    private void createFieldsForEachField( String childs, List< UIFormItem > listUserDirectory, Object ss, String[] output ) {
        if ( numMultipleFields == 0 ) {
            fieldsCreation( parentField, listUserDirectory, ss, 0, output );
        }
        childs = parentField;
        if ( output.length > 2 && ( output[ 2 ].equals( Boolean.TRUE.toString() ) || output[ 2 ].equals( Boolean.FALSE.toString() ) )
                && numMultipleFields != 0 ) {
            fieldsCreation( childs, listUserDirectory, ss, numMultipleFields, output );
        } else {
            for ( int i = 1; i <= numMultipleFields; i++ ) {
                fieldsCreation( childs, listUserDirectory, ss, i, output );
            }
        }
        numMultipleFields = 0;
    }

    private void fieldsCreation( String childs, List< UIFormItem > listUserDirectory, Object ss, int i, String[] output ) {
        String[] childAsArray = childs.split( "," );
        boolean isObjectEntry = ( i == 0 ) && ( typeOfEntry != null ) && typeOfEntry.equals( "object" );

        for ( String entry : childAsArray ) {
            String nameOfSection = extractSectionAndField( listUserDirectory, i, op, entry, output, ss );
            String myParent = output.length > 3 ? output[ 3 ] : "";
            if ( ( i > 0 && op.contains( entry ) && !( output.length > 2 ) ) || ( isObjectEntry && op.contains( entry ) && !( output.length
                    > 2 ) && entry.toLowerCase().contains( nameOfEntry ) ) ) {
                SelectFormItem item = createSelectUI( entry, i, ss );
                if ( item != null ) {
                    listUserDirectory.add( item );
                }
            }

            if ( output.length == 2 && flag.equals( Boolean.TRUE ) ) {
                processAllFields( listUserDirectory, ss, i, op, entry, output );
            }

            if ( output.length > 2 && ( output[ 2 ].equals( Boolean.TRUE.toString() ) || output[ 2 ].equals( Boolean.FALSE.toString() ) )
                    && op.contains( entry ) ) {
                processAllFields( listUserDirectory, ss, i, op, entry, output );
                if ( flag.equals( Boolean.FALSE ) ) {
                    removeNonRequiredField( listUserDirectory );
                }
            } else if ( !op.contains( entry ) ) {
                //
                if ( entry.toString().matches( ".*\\d$" ) ) {
                    entry = entry.toString().replaceAll( "\\d$", "" );
                }
                if ( output.length > 3 && output[ 3 ].toString().matches( ".*\\d$" ) ) {
                    output[ 3 ] = output[ 3 ].toString().replaceAll( "\\d$", "" );
                    // if ( output[ 2 ].equals( Boolean.FALSE.toString() ) ) {
                    // flag = Boolean.TRUE;
                    // }
                }

                //
                String typeOfField = extractType( ss, entry );
                if ( output.length > 3 && (
                        ( output[ 0 ].toLowerCase().contains( "response" ) && !( output[ 0 ].toLowerCase().contains( "settings" ) ) )
                                || output[ 0 ].toLowerCase().contains( "tricks" ) || output[ 0 ].toLowerCase().contains( "submesh" )
                                || output[ 0 ].toLowerCase().equals( "objective" ) || output[ 0 ].toLowerCase()
                                .contains( "constraint" ) ) ) {
                    if ( ( output.length == 2 && ( param_name.contains( output[ 2 ] ) || wrapper_name.contains( output[ 2 ] )
                            || geometry_name.contains( output[ 2 ] ) || opt_name.contains( output[ 2 ] ) ) ) ||
                            ( output.length == 4 && ( param_name.contains( output[ 3 ] ) || wrapper_name.contains( output[ 3 ] )
                                    || opt_name.contains( output[ 3 ] ) || geometry_name.contains( output[ 3 ] ) ) ) && typeOfField != null
                                    && typeOfField.equals( "object" ) ) {
                        String nameOfMultipleField = ( myParent.equals( "" ) ) ? nameOfSection : nameOfSection + "." + myParent;
                        SelectFormItem mutlipleTimesSameField = prepareFieldforMultipleSameFields( Boolean.TRUE, nameOfSection, i,
                                myParent + i );
                        internalFlag = flag;
                        listUserDirectory.add( mutlipleTimesSameField );
                    }
                } else {
                    if ( output.length == 3 ) {
                        String nameOfSections = ( numMultipleFields > 0 ) ? entry.toString() + i : entry.toString();
                        SectionFormItem sectionFormItem = prepareSection( Boolean.TRUE, nameOfSections, "section" );
                        listUserDirectory.add( sectionFormItem );
                        if ( output[ 0 ].toLowerCase().contains( "response" ) ) {
                            responses_name.add( nameOfSections );
                            if ( nameOfSections.matches( ".*\\d$" ) && !responses_name_options.contains( nameOfSections )
                                    && responses_name.contains( nameOfSections ) ) {
                                responses_name_options.add( nameOfSections );
                            }
                        } else if ( output[ 0 ].toLowerCase().contains( "submesh" ) ) {
                            submeshes_name.add( nameOfSections );
                            if ( nameOfSections.matches( ".*\\d$" ) && !submeshes_name_options.contains( nameOfSections )
                                    && submeshes_name.contains( nameOfSections ) ) {
                                submeshes_name_options.add( nameOfSections );
                            }
                        }
                        processAllFields( listUserDirectory, ss, i, op, entry, output );
                        if ( internalFlag.equals( Boolean.FALSE ) ) {
                            removeNonRequiredField( listUserDirectory );
                        }
                    } else {
                        // processAllFields( listUserDirectory, ss, i, op, entry, output );
                        if ( entry.toLowerCase().contains( "response" ) && entry.toLowerCase().contains( "settings" ) ) {
                            processAllFields( listUserDirectory, ss, i, op, entry, output );
                            if ( internalFlag.equals( Boolean.FALSE ) ) {
                                removeNonRequiredField( listUserDirectory );
                            }
                        } else {
                            processAllFields( listUserDirectory, ss, i, op, entry, output );
                            if ( flag.equals( Boolean.FALSE ) ) {
                                removeNonRequiredField( listUserDirectory );
                            }
                        }

                    }
                }
            }
        }
    }

    private SelectFormItem createSelectUI( String entry, int i, Object ss ) {

        JsonNode jsonNodeDe = extractDefinitionsFromSchema( entry, ss );

        List< String > fieldNamesList = new ArrayList<>();
        List< String > requiresParamsList = new ArrayList<>();

        if ( jsonNodeDe.get( "properties" ) != null ) {
            JsonNode rootPropertiesJsonNode = jsonNodeDe.get( "properties" );

            requiresParamsList = requiredFields( jsonNodeDe );

            Iterator< Entry< String, JsonNode > > fields = rootPropertiesJsonNode.fields();
            while ( fields.hasNext() ) {
                Entry< String, JsonNode > fieldEntry = fields.next();
                String fieldName = fieldEntry.getKey();
                fieldNamesList.add( fieldName );
            }
        }

        String fieldName = entry + "." + i + ".";
        List< SelectOptionsUI > selectOptions = createBooleanOptionsList( fieldName );
        String fieldId = "workflow/plugin/ShapeModule/" + fieldName + "{__value__}";
        String label = "Show Non-Required Fields";
        String optionId = fieldName + "show_non-required_fields";

        if ( !( requiresParamsList.equals( fieldNamesList ) ) ) {
            return setUIFormFieldsParams( ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT ), selectOptions, fieldId, label,
                    optionId, Boolean.FALSE, prepareRule( Boolean.TRUE, null ), Boolean.FALSE.toString() );
        } else {
            flag = Boolean.TRUE;
            return null;
        }
    }

    private JsonNode extractDefinitionsFromSchema( String entry, Object ss ) {
        final Object de = JsonPath.read( ss, "$.$defs." + entry );
        ObjectMapper mper = new ObjectMapper();
        JsonNode jsonNodeDe = mper.convertValue( de, JsonNode.class );
        return jsonNodeDe;
    }

    private String extractType( Object ss, Object entry ) {
        JsonNode jsonNodeDe = extractDefinitionsFromSchema( entry.toString(), ss );
        String type = null;

        if ( jsonNodeDe.get( "type" ) != null ) {
            type = jsonNodeDe.get( "type" ).asText();
        }

        return type;
    }

    private void processAllFields( List< UIFormItem > listUserDirectory, Object ss, int i, List< String > op, Object entry,
            String[] output ) {

        JsonNode jsonNodeDe = extractDefinitionsFromSchema( entry.toString(), ss );

        if ( jsonNodeDe.get( "properties" ) != null ) {
            JsonNode rootPropertiesJsonNode = jsonNodeDe.get( "properties" );
            List< String > requiresParamsList = requiredFields( jsonNodeDe );

            Iterator< Entry< String, JsonNode > > fields = rootPropertiesJsonNode.fields();
            while ( fields.hasNext() ) {
                Map.Entry< String, JsonNode > field = fields.next();
                boolean existsInRequiredParams = requiresParamsList.contains( field.getKey() );
                String checkKeys = field.getValue().toString();
                //
                JsonNode fieldValue = field.getValue();
                String type = fieldValue.has( "type" ) ? fieldValue.get( "type" ).asText() : null;
                String toolTip = getToolTip( fieldValue );

                if ( type != null ) {
                    if ( type.equals( "string" ) && ( field.getKey().toString().contains( "geometry" ) || field.getKey().toString()
                            .equals( "solver_name" ) || ( ( parentField.contains( "Objective" ) || parentField.contains( "Constraint" ) )
                            && field.getKey().equals( "name" ) ) ) || field.getKey().toString().equals( "sub_mesh_name" ) ) {
                        List< SelectOptionsUI > options = null;
                        if ( field.getKey().toString().contains( "geometry" ) ) {
                            options = getSelectObjectUIs( geometry_name_options );
                        } else if ( field.getKey().toString().equals( "solver_name" ) ) {
                            options = getSelectObjectUIs( wrapper_name_options );
                        } else if ( field.getKey().toString().equals( "sub_mesh_name" ) ) {
                            if ( !( submeshes_name_options.contains( "edges" ) ) && !submeshes_name_options.contains( "boundary" )
                                    || !submeshes_name_options.contains( "solid" ) || !submeshes_name_options.contains(
                                    "design_surface" ) ) {
                                submeshes_name_options.add( "edges" );
                                submeshes_name_options.add( "boundary" );
                                submeshes_name_options.add( "solid" );
                                submeshes_name_options.add( "design_surface" );
                            }
                            options = getSelectObjectUIs( submeshes_name_options );
                        } else {
                            options = getSelectObjectUIs( responses_name_options );
                        }
                        SelectFormItem selectFormItem = prepareSelectUI( "select", field.getKey(), setFieldName( entry, field, i, output ),
                                getDefaultValue( rootPropertiesJsonNode, field ), Boolean.FALSE,
                                prepareRule( existsInRequiredParams, getRegexPattern( fieldValue ) ), prepareMessage(), options, null,
                                Collections.emptyMap(), toolTip );
                        listUserDirectory.add( selectFormItem );
                    }

                    if ( type.equals( "boolean" ) || type.equals( "integer" ) || type.equals( "number" ) ) {
                        type = type.equals( "number" ) ? "float" : type;
                        List< SelectOptionsUI > options = type.equals( "boolean" ) ? createBooleanOptionsList( entry + "." ) : null;
                        SelectFormItem selectFormItem = prepareSelectUI( type, field.getKey(), setFieldName( entry, field, i, output ),
                                getDefaultValue( rootPropertiesJsonNode, field ), Boolean.FALSE,
                                prepareRule( existsInRequiredParams, getRegexPattern( fieldValue ) ), prepareMessage(), options, null,
                                Collections.emptyMap(), toolTip );
                        listUserDirectory.add( selectFormItem );
                    } else if ( ( type.equals( "string" ) && !( field.getKey().toString().contains( "geometry_name" ) ) && !( field.getKey()
                            .toString().equals( "solver_name" ) ) ) && !checkKeys.contains( "items" ) && !checkKeys.contains( "anyOf" )
                            && !checkKeys.contains( "allOf" ) && !checkKeys.contains( "$ref" ) && !checkKeys.contains( "enum" ) && !(
                            parentField.contains( "Objective" ) || parentField.contains( "Constraint" ) ) && !( field.getKey()
                            .equals( "sub_mesh_name" ) ) && !( field.getKey().equals( "geometry" ) ) && !(
                            field.getKey().toString().contains( "Destination" ) || field.getKey().toString().contains( "Origin" ) ) ) {
                        type = field.getKey().contains( "_path" ) ? FieldTypes.SERVER_FILE_EXPLORER.getType() : "text";
                        String value = ( field.getKey().equals( "submesh_separator" ) ? "."
                                : fieldValue.has( "default" ) ? fieldValue.get( "default" ).asText() : "" );
                        value = ( value != null && !value.equals( "null" ) ) ? value : "";
                        SelectFormItem selectFormItem = prepareSelectUI( type, field.getKey(), setFieldName( entry, field, i, output ),
                                value, Boolean.FALSE, prepareRule( existsInRequiredParams, getRegexPattern( fieldValue ) ),
                                prepareMessage(), null, null, Collections.emptyMap(), toolTip );
                        listUserDirectory.add( selectFormItem );
                    } else if ( ( checkKeys.contains( "items" ) || checkKeys.contains( "anyOf" ) || checkKeys.contains( "allOf" )
                            || checkKeys.contains( "$ref" ) || checkKeys.contains( "enum" ) ) ) {
                        handleArrayTypeFields( listUserDirectory, i, entry, field, existsInRequiredParams, fieldValue, type, toolTip, type,
                                output, rootPropertiesJsonNode );
                    }
                } else if ( ( checkKeys.contains( "items" ) || checkKeys.contains( "anyOf" ) || checkKeys.contains( "allOf" )
                        || checkKeys.contains( "$ref" ) || checkKeys.contains( "enum" ) || checkKeys.contains( "const" ) )
                        && ( !checkKeys.contains( "maxItems" ) ) ) {

                    SectionFormItem sectionFormItem = prepareSection( existsInRequiredParams, field.getKey(), "section" );
                    listUserDirectory.add( sectionFormItem );

                    SMJSONSchema ioObject = JsonUtils.jsonToObject( fieldValue.toString(), SMJSONSchema.class );
                    getSelectField( listUserDirectory, i, entry, field, existsInRequiredParams, ioObject, toolTip,
                            field.getValue().isArray() ? Boolean.TRUE : Boolean.FALSE, output, rootPropertiesJsonNode );
                }
            }
        }
    }

    private void handleArrayTypeFields( List< UIFormItem > listUserDirectory, int i, Object entry, Map.Entry< String, JsonNode > field,
            boolean existsInRequiredParams, JsonNode fieldValue, String type, String toolTip, String typeOfField, String[] output,
            JsonNode rootPropertiesJsonNode ) {

        List< String > coordinateList = null;
        JsonNode itemsNode = field.getValue().get( "items" );
        boolean isMultiple = typeOfField.equals( "array" ) ? Boolean.TRUE : Boolean.FALSE;

        if ( type.equals( "array" ) && itemsNode != null && itemsNode.has( "type" ) && !( itemsNode.has( "enum" ) || itemsNode.has(
                "anyOf" ) || itemsNode.has( "allOf" ) ) ) {
            String itemType = itemsNode.get( "type" ).asText();
            if ( itemType.equals( "number" ) ) {
                if ( field.getValue().get( "maxItems" ) != null && field.getValue().get( "minItems" ) != null ) {

                    coordinateList = field.getValue().get( "minItems" ).asInt() >= 3 ? Arrays.asList( "X", "Y", "Z" )
                            : field.getValue().get( "minItems" ).asInt() >= 2 ? Arrays.asList( "Y", "Z" ) : null;
                }
                InputTableFormItem selectUI = ( InputTableFormItem ) GUIUtils.createFormItem( FormItemType.INPUT_TABLE );
                selectUI.setType( "input-table" );
                selectUI.setLabel( field.getKey() );
                selectUI.setName( setFieldName( entry, field, i, output ) );
                selectUI.setMultiple( Boolean.FALSE );
                selectUI.setRules( prepareRule( existsInRequiredParams, "^-?\\d+$" ) );
                selectUI.setMessages( prepareMessage() );
                selectUI.setTooltip( toolTip );
                selectUI.setFields( coordinateList != null ? coordinateList.stream()
                        .map( label -> new InputTableFormItem.OptionField( label, FieldTypes.TEXT.getType(), new ArrayList<>(), "No" ) )
                        .toList() : null );

                listUserDirectory.add( selectUI );

            } else if ( itemType.equals( "string" ) ) {
                JsonNode defaultVal = fieldValue.get( "default" );
                if ( defaultVal != null && ( fieldValue.get( "default" ).isArray() ) ) {
                    List< String > optionList = new ArrayList<>();
                    for ( JsonNode element : defaultVal ) {
                        optionList.add( element.asText() );
                    }

                    String value = String.join( ",", optionList );

                    SelectFormItem selectFormItem1 = prepareSelectUI( "select", field.getKey(), setFieldName( entry, field, i, output ),
                            value, isMultiple,
                            prepareRule( existsInRequiredParams, isMultiple ? null : getRegexPattern( field.getValue().get( "items" ) ) ),
                            prepareMessage(), getSelectObjectUIs( optionList ), null, Collections.emptyMap(), toolTip );
                    listUserDirectory.add( selectFormItem1 );

                    regexPattern = getRegexPattern( field.getValue().get( "items" ) );
                    rule = existsInRequiredParams;

                    SelectFormItem selectFormItem2 = prepareSelectUI( "select", "File Extensions",
                            "add_multiple_files_to_by_default" + field.getKey(), null, isMultiple,
                            prepareRule( existsInRequiredParams, null ), prepareMessage(), getNumbersInSelectField(),
                            "workflow/plugin/ShapeModule/{__value__}", Collections.emptyMap(), toolTip );
                    listUserDirectory.add( selectFormItem2 );

                }
            }
        } else {
            SMJSONSchema ioObject = JsonUtils.jsonToObject( fieldValue.toString(), SMJSONSchema.class );
            getSelectField( listUserDirectory, i, entry, field, existsInRequiredParams, ioObject, toolTip, isMultiple, output,
                    rootPropertiesJsonNode );
        }
    }

    private String getDefaultValue( JsonNode rootPropertiesJsonNode, Map.Entry< String, JsonNode > field ) {
        String value;
        JsonNode intDefaultValue = rootPropertiesJsonNode.get( field.getKey() );
        value = intDefaultValue.has( "default" ) ? intDefaultValue.get( "default" ).asText() : "";
        return value;
    }

    private String getToolTip( JsonNode fieldValue ) {
        String toolTip;
        toolTip = fieldValue.has( "markdownDescription" ) || fieldValue.has( "description" ) ? fieldValue.has( "markdownDescription" )
                ? fieldValue.get( "markdownDescription" ).asText() : fieldValue.get( "description" ).asText() : null;
        return toolTip;
    }

    private String getRegexPattern( JsonNode fieldValue ) {
        String regexPatternInternal;
        regexPatternInternal = fieldValue.has( "pattern" ) ? fieldValue.get( "pattern" ).asText() : null;
        return regexPatternInternal;
    }

    private String setFieldName( Object entry, Entry< String, JsonNode > field, int index, String[] output ) {
        // return ( output[ 0 ].equals( entry ) ) ? ( entry + "." + output[ output.length - 1 ] + "." + index + "." + field.getKey() )
        // : ( output[ 0 ] + "." + output[ output.length - 1 ] + "." + index + "." + field.getKey() );
        return entry + String.valueOf( index ) + "." + output[ output.length - 1 ] + "." + String.valueOf( index ) + "." + field.getKey();
    }

    private String getBindFrom( SMJSONSchema ioObject, int i, Entry< String, JsonNode > field ) {
        String bindFrom = null;
        boolean containsConst;
        if ( ioObject.getAnyOf() != null ) {
            containsConst = ioObject.getAnyOf().stream().anyMatch( element -> element.containsKey( "const" ) );
        } else {
            containsConst = false;
        }

        if ( CollectionUtils.isNotEmpty( ioObject.getAnyOf() ) || CollectionUtils.isNotEmpty( ioObject.getAllOf() )
                || CollectionUtils.isNotEmpty( ioObject.getOneOf() ) || ioObject.get$ref() != null ) {
            if ( containsConst ) {
                bindFrom = null;
            } else {
                bindFrom = "workflow/plugin/ShapeModule/{__value__}" + "." + i + "." + flag + "." + parentField + ( i > 0 ? i : "" );
            }
        } else if ( field.getValue().get( "items" ) != null ) {
            JsonNode itemsNode = field.getValue().get( "items" );
            if ( itemsNode.has( "allOf" ) || itemsNode.has( "anyOf" ) || itemsNode.has( "oneOf" ) || itemsNode.has( "$ref" ) ) {
                bindFrom = "workflow/plugin/ShapeModule/{__value__}" + "." + i + "." + flag + "." + parentField + ( i > 0 ? i : "" );
            }
        }

        return bindFrom;
    }

    private String getSelectValue( Map.Entry< String, JsonNode > field, List< SelectOptionsUI > selectOptionsUIS,
            boolean existsInRequiredParams ) {
        String value = null;
        if ( field.getKey().equals( "design_variable_list" ) ) {
            value = selectOptionsUIS.get( 0 ).getId();
        } else {
            value = existsInRequiredParams ? selectOptionsUIS.get( 0 ).getId() : null;
        }
        return value;
    }

    private Map< String, Object > createLiveSearch() {
        Map< String, Object > liveSearch = new HashMap<>();
        liveSearch.put( "liveSearch", Boolean.TRUE );
        return liveSearch;
    }

    private void getSelectField( List< UIFormItem > listUserDirectory, int i, Object entry, Map.Entry< String, JsonNode > field,
            boolean existsInRequiredParams, SMJSONSchema ioObject, String toolTip, boolean isMultiple, String[] output,
            JsonNode rootPropertiesJsonNode ) {
        List< String > option = null;
        List< String > filtered_options = new ArrayList<>();
        List< SelectOptionsUI > selectOptionsUIS = null;

        if ( CollectionUtils.isNotEmpty( ioObject.getAnyOf() ) ) {
            boolean containsConst;
            if ( ioObject.getAnyOf() != null ) {
                containsConst = ioObject.getAnyOf().stream().anyMatch( element -> element.containsKey( "const" ) );
            } else {
                containsConst = false;
            }
            if ( containsConst ) {
                option = ioObject.getAnyOf().stream().filter( element -> element.containsKey( "const" ) )
                        .map( element -> element.get( "const" ) ).collect( Collectors.toList() );
            } else if ( field.getKey().equals( "mesh_type" ) ) {
                List< String > enumValues = Arrays.asList( "surface", "solid", "bar" );
                ObjectMapper mapper = new ObjectMapper();
                JsonNode enumNode = mapper.valueToTree( enumValues );
                option = StreamSupport.stream( enumNode.spliterator(), false ).map( JsonNode::asText ).collect( Collectors.toList() );
            } else if ( field.getKey().equals( "limit_type" ) ) {
                List< String > enumValues = Arrays.asList( "relative", "absolute" );
                ObjectMapper mapper = new ObjectMapper();
                JsonNode enumNode = mapper.valueToTree( enumValues );
                option = StreamSupport.stream( enumNode.spliterator(), false ).map( JsonNode::asText ).collect( Collectors.toList() );
            } else {
                option = getListOfOptionFromListOfMap( ioObject.getAnyOf() );
            }

        } else if ( CollectionUtils.isNotEmpty( ioObject.getAllOf() ) ) {
            option = getListOfOptionFromListOfMap( ioObject.getAllOf() );
        } else if ( CollectionUtils.isNotEmpty( ioObject.getOneOf() ) ) {
            option = getListOfOptionFromListOfMap( ioObject.getOneOf() );
        } else if ( null != field.getValue().get( "const" ) ) {
            option = null;

            JsonNode intDefaultValue = rootPropertiesJsonNode.get( field.getKey() );
            String value = intDefaultValue.has( "const" ) ? intDefaultValue.get( "const" ).asText() : "";
            SelectFormItem selectFormItem = prepareSelectUI( "text", field.getKey(), setFieldName( entry, field, i, output ), value,
                    isMultiple, prepareRule( existsInRequiredParams, getRegexPattern( field.getValue() ) ), prepareMessage(), null, null,
                    createLiveSearch(), toolTip );
            listUserDirectory.add( selectFormItem );
        } else if ( null != field.getValue().get( "enum" ) ) {
            JsonNode enumNode = field.getValue().get( "enum" );
            option = StreamSupport.stream( enumNode.spliterator(), false ).map( JsonNode::asText ).collect( Collectors.toList() );
        } else if ( null != ioObject.get$ref() ) {
            Map< String, String > map = new HashMap<>();
            map.put( "$ref", ioObject.get$ref() );
            option = getListOfOptionFromListOfMap( Arrays.asList( map ) );
        } else if ( null != field.getValue().get( "items" ) ) {
            if ( field.getValue().get( "items" ).get( "anyOf" ) != null ) {
                List< Map< String, String > > itemList = itemsOfanyOf( ioObject );
                option = getListOfOptionFromListOfMap( itemList );
            } else if ( field.getValue().get( "items" ).get( "oneOf" ) != null ) {
                List< Map< String, String > > itemList = itemsOfanyOf( ioObject );
                option = getListOfOptionFromListOfMap( itemList );
            } else if ( field.getValue().get( "items" ).get( "enum" ) != null ) {
                JsonNode enumNode = field.getValue().get( "items" );
                option = StreamSupport.stream( ( enumNode.get( "enum" ) ).spliterator(), false ).map( JsonNode::asText )
                        .collect( Collectors.toList() );
            } else {
                JsonNode $refNode = field.getValue().get( "items" );
                Map< String, String > map = new HashMap<>();
                map.put( "$ref", $refNode.get( "$ref" ).asText() );
                option = getListOfOptionFromListOfMap( Arrays.asList( map ) );
            }
        }
        if ( null == field.getValue().get( "const" ) ) {
            selectOptionsUIS = getSelectObjectUIs( filteredOptions( option, filtered_options ) );

            SelectFormItem selectFormItem = prepareSelectUI( "select", field.getKey(), setFieldName( entry, field, i, output ),
                    getSelectValue( field, selectOptionsUIS, existsInRequiredParams ), isMultiple,
                    prepareRule( existsInRequiredParams, getRegexPattern( field.getValue() ) ), prepareMessage(), selectOptionsUIS,
                    !field.getKey().equals( "mesh_type" ) && !field.getKey().equals( "limit_type" ) ? getBindFrom( ioObject, i, field )
                            : null, createLiveSearch(), toolTip );
            listUserDirectory.add( selectFormItem );
        }
    }

    private List< String > filteredOptions( List< String > options, List< String > filteredOption ) {
        Pattern pattern = Pattern.compile( "Old$" );
        for ( String str : options ) {
            Matcher matcher = pattern.matcher( str );
            if ( !matcher.find() ) {
                filteredOption.add( str );
            }
        }
        return filteredOption;
    }

    private List< SelectOptionsUI > createBooleanOptionsList( String name ) {
        List< SelectOptionsUI > options = new ArrayList<>();
        List< Boolean > optionsAsList = new ArrayList<>();
        optionsAsList.add( Boolean.TRUE );
        optionsAsList.add( Boolean.FALSE );
        IntStream.range( 0, optionsAsList.size() ).forEach( index -> {
            SelectOptionsUI objectItem = new SelectOptionsUI();
            objectItem.setId( optionsAsList.get( index ).toString() );
            objectItem.setName( optionsAsList.get( index ).toString() );
            options.add( objectItem );
        } );
        return options;
    }

    private String extractSectionAndField( List< UIFormItem > listUserDirectory, int i, List< String > op, Object entry, String[] output,
            Object ss ) {
        String nameOfSections = ( numMultipleFields > 0 ) ? entry.toString() + i : entry.toString();
        boolean existsInRequiredParams = op.contains( entry );
        if ( op.contains( entry.toString() ) ) {

            if ( !( output.length > 2 ) ) {
                // String nameOfSections = entry.toString() + i;
                SectionFormItem sectionFormItem = prepareSection( existsInRequiredParams, nameOfSections, "section" );
                listUserDirectory.add( sectionFormItem );
                String modifiedName = nameOfSections.replaceAll( "\\d+$", "" );
                if ( nameOfSections.matches( ".*\\d$" ) ) {
                    if ( !geometry_name_options.contains( nameOfSections ) && geometry_name.contains( modifiedName ) ) {
                        geometry_name_options.add( nameOfSections );
                    } else if ( !wrapper_name_options.contains( nameOfSections ) && wrapper_name.contains( modifiedName ) ) {
                        wrapper_name_options.add( nameOfSections );
                    } else if ( !opt_name_options.contains( nameOfSections ) && opt_name.contains( modifiedName ) ) {
                        opt_name_options.add( nameOfSections );
                    }
                }
            }
            if ( numMultipleFields < 1 && !( typeOfEntry.equals( "object" ) && entry.toString().toLowerCase().contains( nameOfEntry ) ) ) {
                SelectFormItem mutlipleTimesSameField = prepareFieldforMultipleSameFields( existsInRequiredParams, entry, i, null );
                listUserDirectory.add( mutlipleTimesSameField );
            }
        }
        return nameOfSections;
    }

    private Integer isInteger( String childs ) {
        Integer integer = null;
        try {
            integer = Integer.parseInt( childs );
        } catch ( Exception e ) {
            log.info( "Not Integer" );
        }
        return integer;
    }

    private List< String > requiredFields( JsonNode jsonNodeDe ) {
        List< String > requiresParamsList = new ArrayList<>();
        if ( jsonNodeDe.get( "required" ) != null ) {
            JsonNode requiresParamsNode = jsonNodeDe.get( "required" );
            for ( JsonNode element : requiresParamsNode ) {
                requiresParamsList.add( element.asText() );
            }
        }
        return requiresParamsList;
    }

    private List< Map< String, String > > itemsOfanyOf( SMJSONSchema ioObject ) {
        JSONObject jsonObject = new JSONObject( JsonUtils.toJsonString( ioObject.getItems() ) );
        JSONArray jsonArray;
        if ( jsonObject.has( "oneOf" ) ) {
            jsonArray = jsonObject.getJSONArray( "oneOf" );
        } else {
            jsonArray = jsonObject.getJSONArray( "anyOf" );
        }

        List< Map< String, String > > refList = new ArrayList<>();
        for ( int i = 0; i < jsonArray.length(); i++ ) {
            JSONObject item = jsonArray.getJSONObject( i );
            if ( item.has( "oneOf" ) ) {
                JSONArray oneOfArray = item.getJSONArray( "oneOf" );
                for ( int j = 0; j < oneOfArray.length(); j++ ) {
                    JSONObject subItem = oneOfArray.getJSONObject( j );
                    String ref = subItem.getString( "$ref" );
                    Map< String, String > refMap = new HashMap<>();
                    refMap.put( "$ref", ref );
                    refList.add( refMap );
                }
            } else {
                String ref = item.getString( "$ref" );
                Map< String, String > refMap = new HashMap<>();
                refMap.put( "$ref", ref );
                refList.add( refMap );
            }
        }
        return refList;
    }

    private List< SelectOptionsUI > getSelectObjectUIs( List< String > optionsAsList ) {
        List< SelectOptionsUI > options = new ArrayList<>();
        IntStream.range( 0, optionsAsList.size() ).forEach( index -> {
            SelectOptionsUI objectItem = new SelectOptionsUI();
            objectItem.setId( optionsAsList.get( index ) );
            objectItem.setName( optionsAsList.get( index ) );
            options.add( objectItem );
        } );
        return options;
    }

    private List< String > getListOfOptionFromListOfMap( List< Map< String, String > > anyOf ) {
        return anyOf.stream().flatMap( item -> item.values().stream().map( value -> value.replace( "#/$defs/", "" ) ) )
                .collect( Collectors.toList() );
    }

    private Map< String, Object > prepareMessage() {
        Map< String, Object > message = new HashMap<>();
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        return message;
    }

    @SuppressWarnings( "unchecked" )
    private Map< String, Object > prepareRule( Boolean req, String pattern ) {
        Rules rule = new Rules();
        rule.setRequired( req );
        rule.setMaxlength( 1024 );
        rule.setPattern( pattern );
        Map< String, Object > rulesForSetting = new HashMap<>();
        rulesForSetting = ( Map< String, Object > ) JsonUtils.jsonToMap( JsonUtils.toJson( rule ), rulesForSetting );
        return rulesForSetting;
    }

    private SelectFormItem prepareSelectUI( String type, String label, String name, String value, Boolean isMultiple,
            Map< String, Object > rules, Map< String, Object > message, List< SelectOptionsUI > options, String bindFrom,
            Map< String, Object > picker, String toolTip ) {
        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setType( type );
        selectFormItem.setLabel( label );
        selectFormItem.setName( name );
        selectFormItem.setValue( value );
        selectFormItem.setMultiple( isMultiple );
        selectFormItem.setRules( rules );
        selectFormItem.setMessages( message );
        selectFormItem.setOptions( options );
        selectFormItem.setBindFrom( bindFrom );
        selectFormItem.setPicker( picker );
        selectFormItem.setTooltip( toolTip );
        if ( type.equals( "float" ) || type.equals( "integer" ) ) {
            // float floatValue = Float.parseFloat( "0.0" );
            // int intValue = Integer.parseInt( "0" );
            selectFormItem.setConvert( type );
        }
        return selectFormItem;
    }

    private SelectFormItem prepareField( SMJSONSchema smjsonSchema, String immediateNode, List< String > op, Boolean isMultiple ) {
        SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        List< SelectOptionsUI > options = new ArrayList<>();
        List< String > filteredOptions = new ArrayList<>();
        List< String > optionsAsList = filteredOptions( listOfOptions( smjsonSchema, immediateNode ), filteredOptions );
        List< String > requiredparams = smjsonSchema.getRequired();

        for ( String elem : optionsAsList ) {
            op.add( elem );
            if ( immediateNode.contains( "geometry_" ) ) {
                geometry_name.add( elem );
            } else if ( immediateNode.contains( "wrapper_" ) ) {
                wrapper_name.add( elem );
            } else if ( immediateNode.contains( "parameterization_" ) ) {
                param_name.add( elem );
            } else if ( immediateNode.contains( "optimization" ) ) {
                opt_name.add( elem );
            }
        }

        IntStream.range( 0, filteredOptions.size() ).forEach( index -> {
            SelectOptionsUI objectItem = new SelectOptionsUI();
            objectItem.setId( filteredOptions.get( index ) );
            objectItem.setName( filteredOptions.get( index ) );
            options.add( objectItem );
        } );

        boolean existsInRequiredParams = requiredparams.contains( immediateNode );
        setUIFormFieldsParams( item, options, "workflow/plugin/ShapeModule/{__value__}", immediateNode, immediateNode, isMultiple,
                prepareRule( ( existsInRequiredParams ? Boolean.TRUE : Boolean.FALSE ), null ), null );
        return item;
    }

    private SectionFormItem prepareSection( boolean existsInRequiredParams2, String plugin, String type ) {
        SectionFormItem sectionFormItem = ( SectionFormItem ) GUIUtils.createFormItem( FormItemType.SECTION );
        sectionFormItem.setTitle( ( plugin.replace( "_", " " ) ).toUpperCase() );
        sectionFormItem.setMode( "template" );
        sectionFormItem.setType( type );
        sectionFormItem.setName( type );
        sectionFormItem.setRules( prepareRule( ( existsInRequiredParams2 ? Boolean.TRUE : Boolean.FALSE ), null ) );
        return sectionFormItem;
    }

    private List< String > listOfOptions( SMJSONSchema smjsonSchema, String plugin ) {
        List< Map< String, String > > options = new ArrayList<>();
        SMJSONSchema propertyItem = smjsonSchema.getProperties().get( plugin );
        // // for direct options with items
        if ( null != propertyItem.getItems() ) {
            JsonNode itemsJsonNode = JsonUtils.convertObjectToJsonNode( propertyItem.getItems() );
            if ( itemsJsonNode.has( "anyOf" ) ) {
                itemsJsonNode = itemsJsonNode.get( "anyOf" );
            } else if ( itemsJsonNode.has( "oneOf" ) ) {
                itemsJsonNode = itemsJsonNode.get( "oneOf" );
            }
            List< Map< String, String > > maps = new ArrayList<>();
            // for multiple options through items
            if ( null != itemsJsonNode && itemsJsonNode.isArray() ) {
                for ( JsonNode jsonNode : itemsJsonNode ) {
                    maps.add( jsonNodeToMap( jsonNode ) );
                }
                options.addAll( maps );
            } else {
                // for single options through items
                JsonNode itemJsonNode = JsonUtils.convertObjectToJsonNode( propertyItem.getItems() );
                options.add( jsonNodeToMap( itemJsonNode ) );
            }
        } else {
            // for direct options without items
            options.addAll( propertyItem.getOneOf() );
        }
        return options.stream().flatMap( item -> item.values().stream().map( value -> value.replace( "#/$defs/", "" ) ) )
                .collect( Collectors.toList() );
    }

    private Map< String, String > jsonNodeToMap( JsonNode jsonNode ) {
        Map< String, String > map = new HashMap<>();
        map = ( Map< String, String > ) JsonUtils.jsonToMap( jsonNode.toString(), map );
        return map;
    }

}