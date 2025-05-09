package de.soco.software.simuspace.suscore.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import lombok.extern.log4j.Log4j2;

@Log4j2
public final class ByteUtil {

    private ByteUtil() {
        // private to prevent instantiation
    }

    /**
     * Convert byte to string.
     *
     * @param bytes
     *         the bytes
     *
     * @return the string
     */
    public static String convertByteToString( byte[] bytes ) {
        String dataString = null;
        if ( bytes != null && bytes.length > 0 ) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream( bytes );
                ObjectInputStream ois = new ObjectInputStream( bis );
                dataString = ( String ) ois.readObject();
                bis.close();
                ois.close();

            } catch ( Exception e ) {
                log.warn( e.getMessage() + "Returning null" );
                if ( e instanceof StreamCorruptedException ) {
                    return new String( bytes );
                }
            }
        }
        return dataString;

    }

    /**
     * Convert byte to object.
     *
     * @param bytes
     *         the bytes
     *
     * @return the object
     */
    public static Object convertByteToObject( byte[] bytes ) {
        Object object = null;
        if ( bytes != null && bytes.length > 0 ) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream( bytes );
                ObjectInputStream ois = new ObjectInputStream( bis );
                object = ois.readObject();
                bis.close();
                ois.close();

            } catch ( Exception e ) {
                log.warn( e.getMessage() + "Returning null" );
                return null;
            }
        }
        return object;

    }

    /**
     * Convert string to byte.
     *
     * @param dataString
     *         the data string
     *
     * @return the byte[]
     */
    public static byte[] convertStringToByte( String dataString ) {
        byte[] result = null;
        if ( dataString != null && !dataString.isEmpty() ) {

            try ( ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream( bos ) ) {

                oos.writeObject( dataString );
                result = bos.toByteArray();
            } catch ( final IOException e ) {
                log.warn( e.getMessage() + "Returning null" );
                result = null;
            }

        }
        return result;
    }

    /**
     * Convert object to byte.
     *
     * @param object
     *         the data string
     *
     * @return the byte[]
     */
    public static byte[] convertObjectToByte( Object object ) {
        byte[] result = null;
        if ( object != null ) {

            try ( ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream( bos ) ) {

                oos.writeObject( object );
                result = bos.toByteArray();
            } catch ( final IOException e ) {
                log.warn( e.getMessage() + "Returning null" );
                result = null;
            }

        }
        return result;
    }

    /**
     * Creates the deep copy through serialization.
     *
     * @param oldObj
     *         the old obj
     *
     * @return the object
     *
     * @throws IOException
     *         the io exception
     * @throws ClassNotFoundException
     *         the class not found exception
     */
    public static Object createDeepCopyThroughSerialization( Object oldObj ) throws IOException, ClassNotFoundException {
        try ( ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream( bos ) ) {
            oos.writeObject( oldObj );
            oos.flush();
            byte[] byteArray = bos.toByteArray();
            try ( ByteArrayInputStream bin = new ByteArrayInputStream( byteArray ); ObjectInputStream ois = new ObjectInputStream( bin ) ) {
                return ois.readObject();
            }
        }
    }

}