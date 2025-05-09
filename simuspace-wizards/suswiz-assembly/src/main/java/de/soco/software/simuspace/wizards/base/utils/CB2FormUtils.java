package de.soco.software.simuspace.wizards.base.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;

public class CB2FormUtils {

    public static UIFormItem prepareSelectItemFromDropDown( JsonNode fieldNode ) {
        SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        prepareCommonAttributes( item, fieldNode );
        item.setType( FieldTypes.SELECTION.getType() );
        item.setMultiple( false );
        List< SelectOptionsUI > selectOptionsUIList = new ArrayList<>();

        // Iterate over the elements in the "validValues" array
        for ( JsonNode valueNode : fieldNode.get( "validValues" ) ) {
            String id = valueNode.get( "v" ).asText();
            String name = valueNode.get( "l" ).asText();
            SelectOptionsUI selectOptionsUI = new SelectOptionsUI( id, name );
            selectOptionsUIList.add( selectOptionsUI );
        }
        item.setOptions( selectOptionsUIList );
        return item;
    }

    public static UIFormItem prepareBooleanItemFromCheckBox( JsonNode fieldNode ) {
        SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        prepareCommonAttributes( item, fieldNode );
        item.setType( FieldTypes.BOOLEAN.getType() );
        return item;
    }

    public static UIFormItem prepareFormItemFromOneRef( JsonNode fieldNode ) {
        UIFormItem item = GUIUtils.createFormItem();
        prepareCommonAttributes( item, fieldNode );
        item.setType( FieldTypes.TEXT.getType() );
        item.setReadonly( true );
        return item;
    }

    public static UIFormItem prepareFormItemFromTextField( JsonNode fieldNode ) {
        UIFormItem item = GUIUtils.createFormItem();
        prepareCommonAttributes( item, fieldNode );
        item.setType( FieldTypes.TEXT.getType() );
        return item;
    }

    private static void prepareCommonAttributes( UIFormItem item, JsonNode fieldNode ) {
        if ( fieldNode.has( "visible" ) ) {
            item.setShow( fieldNode.get( "visible" ).asText() );
        }
        if ( fieldNode.has( "name" ) ) {
            item.setName( fieldNode.get( "name" ).asText() );
        }
        if ( fieldNode.has( "tooltip" ) ) {
            item.setTooltip( fieldNode.get( "tooltip" ).asText() );
        }
        if ( fieldNode.has( "label" ) ) {
            item.setLabel( fieldNode.get( "label" ).asText() );
        }
        if ( fieldNode.has( "value" ) ) {
            item.setValue( fieldNode.get( "value" ).asText() );
        }
        boolean isRequired = fieldNode.has( "required" ) && fieldNode.get( "required" ).asBoolean();
        if ( isRequired ) {
            Map< String, Object > rules = new HashMap<>();
            Map< String, Object > message = new HashMap<>();
            rules.put( "required", true );
            message.put( "required", "Must Chose Option" );
            item.setRules( rules );
            item.setMessages( message );
        }

    }

}
