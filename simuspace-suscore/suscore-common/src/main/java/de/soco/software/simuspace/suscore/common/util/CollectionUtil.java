package de.soco.software.simuspace.suscore.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ComparisonDTO;

/**
 * The Class CollectionUtil. This class contains all the utility functions that could apply on a collection
 */
@Log4j2
public class CollectionUtil {

    /**
     * Avoid Instantiation a new collection util.
     */
    private CollectionUtil() {

    }

    /**
     * Clear Collection.
     *
     * @param col
     *         the collection
     *
     * @return true, if successful
     */
    public static boolean clear( Collection< ? > col ) {

        if ( col != null && !col.isEmpty() ) {
            col.clear();
            return true;
        }
        return false;
    }

    /**
     * Checks if is empty.
     *
     * @param collection
     *         the collection
     *
     * @return true, if is empty
     */
    public static boolean isEmpty( Collection< ? > collection ) {

        return ( collection == null ) || collection.isEmpty();
    }

    /**
     * Checks if is empty.
     *
     * @param array
     *         the array
     *
     * @return true, if is empty
     */
    public static boolean isEmpty( Object[] array ) {
        return array == null || array.length <= 0;
    }

    /**
     * Checks if is not empty.
     *
     * @param collection
     *         the collection
     *
     * @return true, if is not empty
     */
    public static boolean isNotEmpty( Collection< ? > collection ) {

        return !isEmpty( collection );
    }

    /**
     * Checks if is not empty array.
     *
     * @param array
     *         the array
     *
     * @return true, if is not empty array
     */
    public static boolean isNotEmptyArray( Object[] array ) {
        return !isEmpty( array );
    }

    /**
     * Gets the UUID list from string type.
     *
     * @param list
     *         the list
     *
     * @return the UUID list from string type
     */
    public static List< UUID > getUUIDListFromStringType( List< String > list ) {
        List< UUID > str = new ArrayList<>();
        for ( String uuid : list ) {
            str.add( UUID.fromString( uuid ) );
        }
        return str;
    }

    /**
     * Gets the compared list.
     *
     * @param addedList
     *         the added list
     * @param removed
     *         the removed
     *
     * @return the compared list
     */
    public static ComparisonDTO getComparedList( List< UUID > addedList, List< UUID > removed ) {

        ComparisonDTO dto = new ComparisonDTO();

        List< UUID > newAdd = new ArrayList<>( addedList );
        List< UUID > newRemoved = new ArrayList<>( removed );

        newAdd.removeAll( removed );
        newRemoved.removeAll( addedList );

        dto.setAdded( newAdd );
        dto.setRemoved( newRemoved );

        return dto;
    }

    /**
     * @param inputByte
     *         the input Byte Array
     *
     * @return null if the InputByte is null or empty
     */
    public static Map< ?, ? > convertByteArrayToMap( byte[] inputByte ) {
        Map< ?, ? > map = null;
        if ( ( inputByte != null ) && ( inputByte.length > ConstantsInteger.INTEGER_VALUE_ZERO ) ) {

            try ( ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream( inputByte );
                    ObjectInputStream ois = new ObjectInputStream( byteArrayInputStream ) ) {

                map = ( Map< ?, ? > ) ois.readObject();

            } catch ( final IOException | ClassNotFoundException e ) {
                log.error( e.getMessage(), e );
                throw new SusException( e.getMessage(), e.getCause() );

            }

        }
        return map;

    }

    /**
     * Prepare byte array from map.
     *
     * @param map
     *         the map
     *
     * @return the byte[] null if the map is null or empty
     */
    public static byte[] convertMapToByteArray( Map< ?, ? > map ) {
        byte[] result = null;
        if ( ( map != null ) && ( !map.isEmpty() ) ) {
            try ( ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream( bos ) ) {
                oos.writeObject( map );
                result = bos.toByteArray();

            } catch ( final IOException e ) {
                log.error( e.getMessage(), e );
                throw new SusException( e.getMessage(), e.getCause() );

            }
        }
        return result;
    }

}
