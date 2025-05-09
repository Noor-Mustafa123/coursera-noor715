/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.BaseBO;
import de.soco.software.simuspace.suscore.common.base.BusinessObject;
import de.soco.software.simuspace.suscore.common.base.DataTransferObject;
import de.soco.software.simuspace.suscore.common.constants.ConstantsIJson;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;

/**
 * The Class is responsible for parsing Objects to Json and Json to Objects.
 */
@Log4j2
public class JsonUtils {

    /**
     * The Constant WEBSERVICE_INPUT_CANT_BE_NULL.
     */
    private static final String WEBSERVICE_INPUT_CANT_BE_NULL = "Input can not be null";

    /**
     * The Constant WEBSERVICE_INVALID_PARAMETERS_SUPLIED.
     */
    private static final String WEBSERVICE_INVALID_PARAMETERS_SUPLIED = "Invalid parameters provided.";

    /**
     * The Constant WEBSERVICE_JSON_PARSING_ERROR.
     */
    private static final String WEBSERVICE_JSON_PARSING_ERROR = "Unable to parse the json.";

    /**
     * The constant mapper.
     */
    private static ObjectMapper mapper = new ObjectMapper().configure( JsonParser.Feature.ALLOW_COMMENTS, true )
            .configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );

    /**
     * Instantiates a new json utils.
     */
    private JsonUtils() {

    }

    /**
     * Will convert Json stream to instance of given value type passed as parameters.
     *
     * @param <T>
     *         instance of given value type.
     * @param json
     *         the json
     * @param clazz
     *         the clazz
     *
     * @return the t
     *
     * @description method takes String as json and Class name and return an Object of that class after parsing json to that Object.
     */
    public static < T > T jsonToObject( String json, Class< T > clazz ) {
        T object = null;

        if ( json == null ) {
            throw new JsonSerializationException( new Exception( WEBSERVICE_INPUT_CANT_BE_NULL ), HttpStatus.SC_BAD_REQUEST,
                    JsonUtils.class );
        }

        if ( !isValidJSON( json ) ) {
            throw new JsonSerializationException( new Exception( WEBSERVICE_INVALID_PARAMETERS_SUPLIED ), HttpStatus.SC_BAD_REQUEST,
                    JsonUtils.class );
        }

        try {
            object = mapper.readValue( json, clazz );
        } catch ( final Exception e ) {
            log.error( WEBSERVICE_INVALID_PARAMETERS_SUPLIED, e );
            throw new JsonSerializationException( WEBSERVICE_INVALID_PARAMETERS_SUPLIED, e, HttpStatus.SC_BAD_REQUEST, JsonUtils.class );
        }

        return object;
    }

    /**
     * Filters out the fields of an object.
     *
     * @param notRequiredProperties
     *         the not required properties
     * @param obj
     *         the obj
     *
     * @return the string
     *
     * @description Method to create json for adding mix-in annotations to use for augmenting Business Object class.
     */
    public static String toFilteredJson( String[] notRequiredProperties, Object obj ) {
        String strJSON = "";
        FilterProvider filters = null;

        final String filter = getFilterByCheckingObjectType( obj );
        final Class< ? > clazz = getFilterClassByCheckingObjectType( obj );

        try {

            final SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
            simpleFilterProvider.setFailOnUnknownId( false );
            filters = simpleFilterProvider.addFilter( filter, SimpleBeanPropertyFilter.serializeAllExcept( notRequiredProperties ) );
            // BO stands for Business Object.
            mapper.addMixInAnnotations( Object.class, clazz );

            mapper.setFilters( filters );
            mapper.configure( com.fasterxml.jackson.databind.MapperFeature.USE_ANNOTATIONS, true );
            strJSON = mapper.writeValueAsString( obj );
        } catch ( final Exception e ) {
            log.error( WEBSERVICE_JSON_PARSING_ERROR, e );
            throw new JsonSerializationException( WEBSERVICE_JSON_PARSING_ERROR, e, JsonUtils.class );
        }
        return strJSON;
    }

    /**
     * Gets the filter by checking object type.
     *
     * @param object
     *         the object
     *
     * @return the filter by checking object type
     */
    private static String getFilterByCheckingObjectType( final Object object ) {
        if ( null == object ) {
            return null;
        }

        String filter = ConstantsIJson.BO_FILTER;
        if ( ( object instanceof List< ? > ) && CollectionUtil.isNotEmpty( CommonUtils.castObjectToList( object ) ) ) {

            if ( BusinessObject.class.isAssignableFrom( CommonUtils.castObjectToList( object ).get( 0 ).getClass() ) ) {
                filter = ConstantsIJson.BO_FILTER;
            } else if ( DataTransferObject.class.isAssignableFrom( CommonUtils.castObjectToList( object ).get( 0 ).getClass() ) ) {
                filter = ConstantsIJson.DTO_FILTER;
            }

        } else {
            if ( BusinessObject.class.isAssignableFrom( object.getClass() ) ) {
                filter = ConstantsIJson.BO_FILTER;
            } else if ( DataTransferObject.class.isAssignableFrom( object.getClass() ) ) {
                filter = ConstantsIJson.DTO_FILTER;
            }
        }

        return filter;
    }

    /**
     * Custom tailored function for converting the json file to a databind.JsonNode.
     *
     * @param fileReader
     *         the file reader
     *
     * @return JsonNode for further processing
     */
    public static com.fasterxml.jackson.databind.JsonNode toJsonNode( final FileReader fileReader ) {
        com.fasterxml.jackson.databind.JsonNode object = null;

        final com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        synchronized ( mapper ) {
            try {
                object = mapper.readValue( fileReader, com.fasterxml.jackson.databind.JsonNode.class );
            } catch ( final Exception e ) {
                log.error( MessageBundleFactory.getMessage( Messages.WEBSERVICE_INVALID_PARAMETERS_SUPLIED.getKey() ), e );
                throw new JsonSerializationException(
                        MessageBundleFactory.getMessage( Messages.WEBSERVICE_INVALID_PARAMETERS_SUPLIED.getKey() ), e,
                        HttpStatus.SC_BAD_REQUEST, JsonUtils.class );
            }
        }

        return object;
    }

    /**
     * Overloaded custom tailored function for converting the json file to a databind.JsonNode.
     *
     * @param fileName
     *         the file name
     *
     * @return JsonNode for further processing
     */
    public static com.fasterxml.jackson.databind.JsonNode toJsonNode( final String fileName ) {
        com.fasterxml.jackson.databind.JsonNode object = null;

        final com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        synchronized ( mapper ) {
            try {
                object = mapper.readValue( fileName, com.fasterxml.jackson.databind.JsonNode.class );
            } catch ( final Exception e ) {
                log.error( MessageBundleFactory.getMessage( Messages.WEBSERVICE_INVALID_PARAMETERS_SUPLIED.getKey() ), e );
                throw new JsonSerializationException(
                        MessageBundleFactory.getMessage( Messages.WEBSERVICE_INVALID_PARAMETERS_SUPLIED.getKey() ), e,
                        HttpStatus.SC_BAD_REQUEST, JsonUtils.class );
            }
        }

        return object;
    }

    /**
     * Convert map to string.
     *
     * @param parameters
     *         the parameters
     *
     * @return the string
     */
    public static String convertMapToString( Map< String, String > parameters ) {
        ObjectMapper mapper = new ObjectMapper();

        String param = null;
        try {
            if ( parameters != null ) {
                param = mapper.writeValueAsString( parameters );
            }
        } catch ( Exception e ) {
            throw new JsonSerializationException( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ), e,
                    JsonUtils.class );
        }
        return param;
    }

    /**
     * Convert list to json.
     *
     * @param <T>
     *         the type parameter
     * @param list
     *         the list
     *
     * @return the string
     */
    public static < T > String convertListToJson( List< T > list ) {
        ObjectMapper mapper = new ObjectMapper();

        String param = null;
        try {
            if ( list != null ) {
                param = mapper.writeValueAsString( list );
            }
        } catch ( Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ), e );
            throw new JsonSerializationException( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ), e,
                    JsonUtils.class );
        }
        return param;
    }

    /**
     * Convert map to string.
     *
     * @param parameters
     *         the parameters
     *
     * @return the string
     */
    public static String convertMapToStringGernericValue( Map< String, Object > parameters ) {
        ObjectMapper mapper = new ObjectMapper();

        String param = null;
        try {
            if ( parameters != null ) {
                param = mapper.writeValueAsString( parameters );
            }
        } catch ( Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ), e );
            throw new JsonSerializationException( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ), e,
                    JsonUtils.class );
        }
        return param;
    }

    /**
     * Convert string to map.
     *
     * @param json
     *         the json
     *
     * @return the map
     */
    @SuppressWarnings( "unchecked" )
    public static Map< String, String > convertStringToMap( String json ) {
        ObjectMapper mapper = new ObjectMapper();
        Map< String, String > parameters = new HashMap<>();
        if ( !StringUtils.isBlank( json ) ) {
            try {
                parameters = mapper.readValue( json, Map.class );
            } catch ( Exception e ) {
                log.error( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ), e );
                throw new JsonSerializationException( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ), e,
                        JsonUtils.class );
            }
        }
        return parameters;
    }

    /**
     * Convert string to map.
     *
     * @param json
     *         the json
     *
     * @return the map
     */
    @SuppressWarnings( "unchecked" )
    public static Map< String, Object > convertStringToMapGenericValue( String json ) {
        ObjectMapper mapper = new ObjectMapper();
        Map< String, Object > parameters = new HashMap<>();
        if ( !StringUtils.isBlank( json ) ) {
            try {
                parameters = mapper.readValue( json, Map.class );
            } catch ( Exception e ) {
                log.error( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ), e );
                throw new JsonSerializationException( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ), e,
                        JsonUtils.class );
            }
        }
        return parameters;
    }

    /**
     * Gets the filter class by checking object type.
     *
     * @param object
     *         the object
     *
     * @return the filter by checking object type
     */
    private static Class< ? > getFilterClassByCheckingObjectType( final Object object ) {
        if ( null == object ) {
            return null;
        }
        Class< ? > clazz = BusinessObject.class;
        if ( ( object instanceof List< ? > ) && CollectionUtil.isNotEmpty( CommonUtils.castObjectToList( object ) ) ) {

            if ( BusinessObject.class.isAssignableFrom( CommonUtils.castObjectToList( object ).get( 0 ).getClass() ) ) {
                clazz = BusinessObject.class;
            } else if ( DataTransferObject.class.isAssignableFrom( CommonUtils.castObjectToList( object ).get( 0 ).getClass() ) ) {
                clazz = DataTransferObject.class;
            }

        } else {
            if ( BusinessObject.class.isAssignableFrom( object.getClass() ) ) {
                clazz = BusinessObject.class;
            } else if ( DataTransferObject.class.isAssignableFrom( object.getClass() ) ) {
                clazz = DataTransferObject.class;
            }
        }
        return clazz;
    }

    /**
     * Will convert Json stream to instance of given value type as parameters.
     *
     * @param <T>
     *         instance of given value type.
     * @param jsonStream
     *         the json stream
     * @param clazz
     *         the clazz
     *
     * @return the t
     *
     * @throws JsonSerializationException
     *         the json serialization exception
     * @description method takes InputStream and Class name and return an Object of that class after parsing InputStream to that Object.
     */
    public static < T > T jsonStreamToObject( InputStream jsonStream, Class< T > clazz ) {
        T object = null;

        if ( jsonStream == null ) {
            return null;
        }

        try {
            object = mapper.readValue( jsonStream, clazz );
        } catch ( final Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ), e );
            throw new JsonSerializationException( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ), e,
                    JsonUtils.class );
        }

        return object;
    }

    /**
     * Gets the value.
     *
     * @param srcJson
     *         the src json
     * @param key
     *         the key
     *
     * @return the value
     *
     * @throws IOException
     *         the IO exception
     */
    public static String getValue( final String srcJson, final String key ) throws IOException {

        if ( StringUtils.isNotBlank( srcJson ) && StringUtils.isNotBlank( key ) ) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode;
            rootNode = objectMapper.readTree( srcJson );
            JsonNode idNode = rootNode.path( key );
            return idNode.textValue();
        }
        return null;

    }

    /**
     * Json to list.
     *
     * @param <T>
     *         the generic type
     * @param json
     *         the json
     * @param clazz
     *         the clazz
     *
     * @return the list
     */
    public static < T > List< T > jsonToList( String json, Class< T > clazz ) {

        if ( json == null ) {
            throw new JsonSerializationException( new Exception( WEBSERVICE_INPUT_CANT_BE_NULL ), HttpStatus.SC_BAD_REQUEST,
                    JsonUtils.class );
        }

        List< T > list = null;
        final TypeFactory t = mapper.getTypeFactory();
        try {
            list = mapper.readValue( json, t.constructCollectionType( ArrayList.class, clazz ) );
        } catch ( final Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ), e );
            throw new JsonSerializationException( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ), e,
                    JsonUtils.class );
        }
        return list;
    }

    /**
     * Will convert {@link Map} to json string.
     *
     * @param valueMap
     *         the value map
     *
     * @return the string
     *
     * @description method takes Map and return Json as String after parsing Map to Json.
     */
    public static String toJson( Map< ?, ? > valueMap ) {
        String obj = null;

        try {
            mapper.addMixInAnnotations( Object.class, BaseBO.class );
            obj = mapper.writeValueAsString( valueMap );
        } catch ( final Exception e ) {
            log.error( MessagesUtil.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR ), e );
            throw new JsonSerializationException( MessagesUtil.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR ), e, JsonUtils.class );
        }

        return obj;
    }

    /**
     * Will convert Json stream to map object passed as parameters.
     *
     * @param json
     *         the json
     * @param valueMap
     *         the map object where json is to parsed
     *
     * @return the map
     */
    public static Map< ?, ? > jsonToMap( String json, Map< ?, ? > valueMap ) {

        if ( json == null ) {
            throw new JsonSerializationException( new Exception( WEBSERVICE_INPUT_CANT_BE_NULL ), HttpStatus.SC_BAD_REQUEST,
                    JsonUtils.class );
        }

        try {
            valueMap = mapper.readValue( json, valueMap.getClass() );

        } catch ( final Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ), e );
            throw new JsonSerializationException( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ), e,
                    JsonUtils.class );
        }

        return valueMap;

    }

    /**
     * To json dto.
     *
     * @param list
     *         the list of dt os
     *
     * @return the string
     */
    public static String toJson( List< ? > list ) {

        if ( list == null ) {
            throw new JsonSerializationException( new Exception( WEBSERVICE_INPUT_CANT_BE_NULL ), HttpStatus.SC_BAD_REQUEST,
                    JsonUtils.class );
        } else if ( list.isEmpty() ) {
            return ConstantsString.EMPTY_STRING;
        }

        String obj = "";
        Class< ? > clazz = list.get( 0 ).getClass();
        try {
            mapper.addMixInAnnotations( Object.class, clazz );
            obj = mapper.writeValueAsString( list );
        } catch ( Exception e ) {
            log.error( MessagesUtil.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR ), e );
            throw new JsonSerializationException( MessagesUtil.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR ), e, JsonUtils.class );
        }

        return obj;
    }

    /**
     * To json.
     *
     * @param obj
     *         the obj
     *
     * @return the string
     */
    public static String toJson( Object obj ) {
        String jsonObj = "";
        try {
            if ( null == obj ) {
                return null;
            }
            mapper.addMixInAnnotations( Object.class, obj.getClass() );
            jsonObj = mapper.writeValueAsString( obj );
        } catch ( Exception e ) {
            log.error( WEBSERVICE_JSON_PARSING_ERROR, e );
            throw new JsonSerializationException( WEBSERVICE_JSON_PARSING_ERROR, e, JsonUtils.class );
        }
        return jsonObj;
    }

    /**
     * To json.
     *
     * @param obj
     *         the obj
     * @param filterClass
     *         the filter class
     *
     * @return the string
     */
    public static String toJson( Object obj, Class< ? > filterClass ) {
        String jsonObj = "";
        try {
            if ( null == obj ) {
                return null;
            }
            mapper.addMixInAnnotations( Object.class, filterClass );
            jsonObj = mapper.writeValueAsString( obj );
        } catch ( Exception e ) {
            log.error( WEBSERVICE_JSON_PARSING_ERROR, e );
            throw new JsonSerializationException( WEBSERVICE_JSON_PARSING_ERROR, e, JsonUtils.class );
        }
        return jsonObj;
    }

    /**
     * A utility function to break the payload to certain object and find if the duplicate keys are present in that object
     * <p>
     * The first parameter should contain the complete JSON and the second parameter should point out the object inside that json. The
     * function breaks the @objectJson and extract the key value map of the provided @objectKey.
     * <p>
     * In case no duplicate key found or the objectJson is null or empty, FALSE is returned.
     *
     * @param objectJson
     *         the object json
     * @param objectKey
     *         the object key
     *
     * @return boolean
     */
    public static boolean isDuplicateKeyPresentInsideCertainObjectOfPayload( String objectJson, String objectKey ) {
        if ( StringUtils.isEmpty( objectJson ) ) {
            return false;
        }
        List< String > keys = new ArrayList<>();
        String subString = objectJson.substring( objectJson.indexOf( objectKey ) );
        String data = subString.substring( subString.indexOf( ConstantsString.CURLY_LEFT_BRACKET ) + ConstantsInteger.INTEGER_VALUE_ONE,
                subString.indexOf( ConstantsString.CURLY_RIGHT_BRACKET ) );
        String[] keyValueArray = data.split( ConstantsString.COMMA );
        for ( String s : keyValueArray ) {
            String key = s.split( ConstantsString.COLON )[ ConstantsInteger.INTEGER_VALUE_ZERO ].trim().toUpperCase();
            if ( keys.contains( key ) ) {
                return true;
            }
            keys.add( key );
        }
        return false;
    }

    /**
     * To json dto.
     *
     * @param <T>
     *         the type parameter
     * @param clazz
     *         the clazz
     *
     * @return the string
     */
    public static < T > String toJsonString( T clazz ) {

        if ( clazz == null ) {
            throw new JsonSerializationException( new Exception( WEBSERVICE_INPUT_CANT_BE_NULL ), HttpStatus.SC_BAD_REQUEST,
                    JsonUtils.class );
        }
        try {
            return mapper.writeValueAsString( clazz );
        } catch ( Exception e ) {
            log.error( MessagesUtil.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR ), e );
            throw new JsonSerializationException( MessagesUtil.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR ), e, JsonUtils.class );
        }
    }

    /**
     * Will convert {@link Object} to json string.
     *
     * @param object
     *         the object
     *
     * @return the string
     */
    public static String objectToJson( Object object ) {
        try {
            return mapper.writeValueAsString( object );
        } catch ( final Exception e ) {
            log.error( MessagesUtil.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR ), e );
            throw new JsonSerializationException( MessagesUtil.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR ), e, JsonUtils.class );
        }
    }

    /**
     * Checks if is valid JSON.
     *
     * @param json
     *         the json
     *
     * @return true, if is valid JSON
     */
    public static boolean isValidJSON( final String json ) {
        try {

            JSONParser jsonParser = new JSONParser();
            jsonParser.parse( json );
            return true;

        } catch ( Exception e ) {
            ExceptionLogger.logException( e, JsonUtils.class );
            return false;
        }
    }

    /**
     * Converts the linked map object from response to proper class object.
     *
     * @param <T>
     *         the generic type
     * @param value
     *         the value
     * @param clazz
     *         the clazz
     *
     * @return the t
     */
    public static < T > T linkedMapObjectToClassObject( Object value, Class< T > clazz ) {
        T clazzObject = null;

        // This is where the code loops. The statement is executed over and over again
        // without executing.
        try {
            clazzObject = mapper.convertValue( value, clazz );
        } catch ( IllegalArgumentException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ), e );
            throw new JsonSerializationException( MessageBundleFactory.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR.getKey() ), e,
                    JsonUtils.class );
        }

        return clazzObject;
    }

    /**
     * Json to list.
     *
     * @param json
     *         the json
     * @param obj
     *         the obj
     *
     * @return the list
     */
    public static List< ? > jsonToList( String json, List< ? > obj ) {
        List< ? > list = null;

        try {
            list = mapper.readValue( json, obj.getClass() );
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, JsonUtils.class );
            throw new JsonSerializationException( MessagesUtil.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR ), e, 400,
                    JsonUtils.class );
        }

        return list;
    }

    /**
     * Json to list.
     *
     * @param json
     *         the json
     * @param obj
     *         the obj
     *
     * @return the list
     */
    public static < T > List< T > jsonToTypeList( String json ) {
        List< T > list = null;

        try {
            list = mapper.readValue( json, list.getClass() );
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, JsonUtils.class );
            throw new JsonSerializationException( MessagesUtil.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR ), e, 400,
                    JsonUtils.class );
        }

        return list;
    }

    /**
     * Convert input stream to json node.
     *
     * @param inputStream
     *         the input stream
     *
     * @return the json node
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public static JsonNode convertInputStreamToJsonNode( InputStream inputStream ) throws IOException {

        if ( inputStream == null ) {
            throw new JsonSerializationException(
                    new Exception( MessageBundleFactory.getMessage( Messages.WEBSERVICE_INPUT_CANT_BE_NULL.getKey() ) ),
                    HttpStatus.SC_BAD_REQUEST, JsonUtils.class );
        }
        JsonNode jsonObj = null;
        try {
            jsonObj = mapper.readTree( inputStream );
        } catch ( final Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.WEBSERVICE_INVALID_PARAMETERS_SUPLIED.getKey() ), e );
            throw new JsonSerializationException(
                    MessageBundleFactory.getMessage( Messages.WEBSERVICE_INVALID_PARAMETERS_SUPLIED.getKey() ), e,
                    HttpStatus.SC_BAD_REQUEST, JsonUtils.class );
        }
        return jsonObj;
    }

    /**
     * Convert object to json node json node.
     *
     * @param object
     *         the object
     *
     * @return the json node
     */
    public static JsonNode convertObjectToJsonNode( Object object ) {

        if ( object == null ) {
            throw new JsonSerializationException(
                    new Exception( MessageBundleFactory.getMessage( Messages.WEBSERVICE_INPUT_CANT_BE_NULL.getKey() ) ),
                    HttpStatus.SC_BAD_REQUEST, JsonUtils.class );
        }
        JsonNode jsonObj = null;
        try {
            jsonObj = mapper.convertValue( object, JsonNode.class );
        } catch ( final Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.WEBSERVICE_INVALID_PARAMETERS_SUPLIED.getKey() ), e );
            throw new JsonSerializationException(
                    MessageBundleFactory.getMessage( Messages.WEBSERVICE_INVALID_PARAMETERS_SUPLIED.getKey() ), e,
                    HttpStatus.SC_BAD_REQUEST, JsonUtils.class );
        }
        return jsonObj;
    }

    /**
     * Json file to object list.
     *
     * @param <T>
     *         the generic type
     * @param jsonFile
     *         the json file
     * @param clazz
     *         the clazz
     *
     * @return the list
     */
    public static < T > List< T > jsonFileToObjectList( File jsonFile, Class< T > clazz ) {

        List< T > list = null;

        final TypeFactory t = mapper.getTypeFactory();

        try ( InputStream inputStream = new FileInputStream( jsonFile ) ) {
            list = mapper.readValue( inputStream, t.constructCollectionType( ArrayList.class, clazz ) );
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, JsonUtils.class );
            throw new JsonSerializationException( MessagesUtil.getMessage( Messages.WEBSERVICE_JSON_PARSING_ERROR ), e, 400,
                    JsonUtils.class );
        }

        return list;
    }

    /**
     * Json object fix for role.
     *
     * @param objectJson
     *         the object json
     *
     * @return the JSON object
     *
     * @throws ParseException
     *         the parse exception
     */
    public static JSONObject jsonObjectFixForRole( String objectJson ) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = ( JSONObject ) parser.parse( objectJson );
        String tId = ( String ) json.get( "groups" );
        json.remove( "groups" );
        json.put( "selectionId", tId );
        return json;
    }

    /**
     * Json object fix for group.
     *
     * @param objectJson
     *         the object json
     *
     * @return the JSON object
     *
     * @throws ParseException
     *         the parse exception
     */
    public static JSONObject jsonObjectFixForGroup( String objectJson ) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = ( JSONObject ) parser.parse( objectJson );
        String tId = ( String ) json.get( "users" );
        json.remove( "users" );
        json.put( "selectionId", tId );
        return json;
    }

    /**
     * Json to object.
     *
     * @param <T>
     *         the generic type
     * @param targetConfigStream
     *         the target config stream
     * @param clazz
     *         the clazz
     *
     * @return the t
     */
    public static < T > T jsonToObject( InputStream targetConfigStream, Class< T > clazz ) {
        T object = null;

        synchronized ( mapper ) {
            try {
                object = mapper.readValue( targetConfigStream, clazz );
            } catch ( final Exception e ) {
                log.error( MessagesUtil.getMessage( Messages.WEBSERVICE_INVALID_PARAMETERS_SUPLIED ), e );
                throw new JsonSerializationException( MessagesUtil.getMessage( Messages.WEBSERVICE_INVALID_PARAMETERS_SUPLIED ), e,
                        HttpStatus.SC_BAD_REQUEST, JsonUtils.class );
            }
        }

        return object;
    }

    /**
     * Converts json conf files to Maps
     *
     * @param propFileName
     *         the prop file name
     *
     * @return the Map
     */
    public static Map< ?, ? > jsonConfToMap( String propFileName ) {
        try ( InputStream is = new FileInputStream( propFileName ) ) {
            return JsonUtils.jsonToObject( is, Map.class );
        } catch ( FileNotFoundException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), propFileName ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), propFileName ), e );
        } catch ( IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_FILE.getKey(), propFileName ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_FILE.getKey(), propFileName ), e );
        }
    }

    /**
     * To JSON object.
     *
     * @param objectJson
     *         the object json
     *
     * @return the JSON object
     *
     * @throws ParseException
     *         the parse exception
     */
    public static JSONObject toJSONObject( String objectJson ) throws ParseException {
        JSONParser parser = new JSONParser();
        return ( JSONObject ) parser.parse( objectJson );
    }

    /**
     * Convert sting to json node json node.
     *
     * @param jsonString
     *         the json string
     *
     * @return the json node
     */
    public static JsonNode convertStingToJsonNode( String jsonString ) {

        if ( jsonString == null ) {
            throw new JsonSerializationException(
                    new Exception( MessageBundleFactory.getMessage( Messages.WEBSERVICE_INPUT_CANT_BE_NULL.getKey() ) ),
                    HttpStatus.SC_BAD_REQUEST, JsonUtils.class );
        }
        JsonNode jsonObj;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonObj = objectMapper.readTree( jsonString );
        } catch ( final Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.WEBSERVICE_INVALID_PARAMETERS_SUPLIED.getKey() ), e );
            throw new JsonSerializationException(
                    MessageBundleFactory.getMessage( Messages.WEBSERVICE_INVALID_PARAMETERS_SUPLIED.getKey() ), e,
                    HttpStatus.SC_BAD_REQUEST, JsonUtils.class );
        }
        return jsonObj;
    }

}
