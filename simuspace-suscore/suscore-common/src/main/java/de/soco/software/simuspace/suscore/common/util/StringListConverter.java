package de.soco.software.simuspace.suscore.common.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;

@Log4j2
@Converter
public class StringListConverter implements AttributeConverter< List< String >, String > {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn( List< String > attribute ) {
        try {
            return objectMapper.writeValueAsString( attribute );
        } catch ( JsonProcessingException e ) {
            log.error( MessageBundleFactory.getDefaultMessage( Messages.JSON_TO_LIST_CONVERSION_FAILED.getKey() ), e );
            throw new SusException( MessageBundleFactory.getDefaultMessage( Messages.LIST_TO_JSON_CONVERSION_FAILED.getKey() ), e );
        }
    }

    @Override
    public List< String > convertToEntityAttribute( String dbData ) {
        try {
            if ( null == dbData ) {
                return null;
            }
            return objectMapper.readValue( dbData, List.class );
        } catch ( JsonProcessingException e ) {
            //            the error messages are hardcoded here for testing only
            log.error( MessageBundleFactory.getDefaultMessage( Messages.JSON_TO_LIST_CONVERSION_FAILED.getKey() ), e );
            throw new SusException( MessageBundleFactory.getDefaultMessage( Messages.JSON_TO_LIST_CONVERSION_FAILED.getKey() ), e );
        }
    }

}