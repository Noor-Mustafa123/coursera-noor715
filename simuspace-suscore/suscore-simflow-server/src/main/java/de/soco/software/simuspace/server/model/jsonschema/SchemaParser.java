package de.soco.software.simuspace.server.model.jsonschema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SchemaParser {

    public static SMJSONSchema parse( JsonNode rootNode ) {
        SMJSONSchema schema = null;

        if ( rootNode.isObject() ) {
            ObjectNode objectNode = ( ObjectNode ) rootNode;
            Iterator< Map.Entry< String, JsonNode > > iter = objectNode.fields();

            while ( iter.hasNext() ) {
                schema = prepareObject( iter.next() );
            }

        } else if ( rootNode.isValueNode() ) {
            ArrayNode arrayNode = ( ArrayNode ) rootNode;
            Iterator< JsonNode > iter = arrayNode.elements();

            while ( iter.hasNext() ) {
                schema = parse( iter.next() );
            }
        }
        return schema;
    }

    private static Object prepareValue( JsonNode valueNode ) {
        Object result = null;
        if ( valueNode.isBoolean() ) {
            result = valueNode.asBoolean();
        }
        if ( valueNode.isInt() ) {
            result = valueNode.asInt();
        }
        if ( valueNode.isDouble() ) {
            result = valueNode.asDouble();
        }
        if ( valueNode.isTextual() ) {
            result = valueNode.asText();
        }

        return result;
    }

    private static List< Object > prepareArray( JsonNode arrayNode ) {

        List< Object > items = new ArrayList< Object >();

        Iterator< JsonNode > iter = arrayNode.elements();
        while ( iter.hasNext() ) {
            JsonNode element = iter.next();
            if ( element.isObject() ) {
                items.add( parse( element ) );
            } else if ( element.isArray() ) {
                items.addAll( prepareArray( element ) );
            } else if ( element.isValueNode() ) {

                items.add( prepareValue( element ) );
            }
        }

        return items;

    }

    private static SMJSONSchema prepareObject( Map.Entry< String, JsonNode > entry ) {

        SMJSONSchema schema = new SMJSONSchema();

        String key = entry.getKey();
        JsonNode value = entry.getValue();

        if ( key.equals( "title" ) ) {
            schema.setTitle( value.asText() );
        }

        if ( key.equals( "description" ) ) {
            schema.setDescription( value.asText() );
        }

        if ( key.equals( "type" ) ) {
            schema.setType( value.asText() );
        }

        if ( key.equals( "properties" ) ) {
            Map< String, SMJSONSchema > x = new HashMap<>();
            x.put( "xx", parse( value ) );
            schema.setProperties( x );
        }

        if ( key.equals( "additionalProperties" ) ) {
            schema.setAdditionalProperties( value.asBoolean() );
        }

        if ( key.equals( "required" ) ) {
            schema.setRequired( ( ArrayList ) prepareArray( value ) );
        }

        return schema;

    }

}