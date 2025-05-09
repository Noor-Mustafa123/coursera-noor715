package de.soco.software.simuspace.suscore.data.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;

public class DataUtils {

    /**
     * The Constant ENTITY_CLASS_FIELD_NAME.
     */
    private static final String ENTITY_CLASS_FIELD_NAME = "ENTITY_CLASS";

    /**
     * Initialize DTO entity.
     *
     * @param className
     *         the class name
     *
     * @return the object
     */
    public static Object initializeDTOEntity( String className ) {

        Class< ? > entityClass = ReflectionUtils.getFieldTypeByName( initializeObject( className ).getClass(), ENTITY_CLASS_FIELD_NAME );
        return initializeObject( entityClass.getName() );
    }

    /**
     * Initialize object.
     *
     * @param className
     *         the class name
     *
     * @return the object
     */
    public static Object initializeObject( String className ) {

        try {
            return Class.forName( className ).newInstance();
        } catch ( InstantiationException | IllegalAccessException | ClassNotFoundException e ) {
            ExceptionLogger.logException( e, DataUtils.class );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.CLASS_NOT_FOUND_OR_NOT_ABLE_TO_INITIALIZE.getKey(), className ) );
        }
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
