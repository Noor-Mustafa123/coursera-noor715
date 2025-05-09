package de.soco.software.simuspace.suscore.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.UIColumn;

/**
 * A reflection utility class that would hold the methods related to reflection
 *
 * @author Zeeshan jamal
 */
public class ReflectionUtils {

    /**
     * The Constant PREPARE_ENTITY_METHOD_OF_DTOS.
     */
    private static final String PREPARE_ENTITY_METHOD_OF_DTOS = "prepareEntity";

    /**
     * The Constant MODIFIERS_STRING.
     */
    private static final String MODIFIERS_STRING = "modifiers";

    /**
     * Private constructor to avoid instantiation
     */
    private ReflectionUtils() {

    }

    /**
     * A method use to get the generic type of the field in a given class.
     *
     * @param clazz
     *         class name from which the field is coming
     * @param data
     *         field of class
     *
     * @return type of field
     */
    public static Class< ? > getFieldTypeByData( Class< ? > clazz, String data ) {
        Field[] attributes = clazz.getDeclaredFields();
        for ( Field field : attributes ) {
            UIColumn uiColumn = field.getAnnotation( UIColumn.class );
            if ( uiColumn != null && uiColumn.data().contentEquals( data ) ) {
                return field.getType();
            }
        }
        throw new SusException( MessageBundleFactory.getMessage( Messages.FIELD_NOT_FOUND_IN_CLASS.getKey(), data, clazz.getName() ) );
    }

    /**
     * A method use to get the generic type of the field in a given class.
     *
     * @param clazz
     *         the clazz
     * @param name
     *         the name
     *
     * @return the field type by name
     */
    public static Class< ? > getFieldTypeByName( Class< ? > clazz, String name ) {
        if ( clazz != null ) {
            List< Field > fields = getAllFields( clazz );
            for ( Field field : fields ) {
                if ( field.getName().contentEquals( name ) ) {
                    try {
                        ParameterizedType stringListType = ( ParameterizedType ) field.getGenericType();
                        return ( Class< ? > ) stringListType.getActualTypeArguments()[ 0 ];
                    } catch ( ClassCastException e ) {
                        return field.getType();
                    }
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.FIELD_NOT_FOUND_IN_CLASS.getKey(), name, clazz.getName() ) );
        }
        return null;
    }

    /**
     * A method use to get the generic type of the field in nested class, e.g : To find type of field c from a.b.c
     *
     * @param clazz
     *         the parent clazz
     * @param name
     *         the name
     *
     * @return the field type by name
     */
    public static Class< ? > getFieldTypeByNameForInnerFields( Class< ? > clazz, String name ) {
        if ( clazz != null ) {
            String fieldname = name;

            if ( name.contains( ConstantsString.DOT ) ) {
                fieldname = name.substring( name.lastIndexOf( ConstantsString.DOT ) + 1 );

                String className = name.substring( 0, name.indexOf( ConstantsString.DOT ) );
                Field classField = getFieldByName( clazz, className );
                clazz = classField.getType();
            }

            List< Field > fields = getAllFields( clazz );
            for ( Field field : fields ) {
                if ( field.getName().contentEquals( fieldname ) ) {
                    try {
                        ParameterizedType stringListType = ( ParameterizedType ) field.getGenericType();
                        return ( Class< ? > ) stringListType.getActualTypeArguments()[ 0 ];
                    } catch ( ClassCastException e ) {
                        return field.getType();
                    }
                }
            }

            // recursively keep checking for field in inner classes
            if ( name.contains( ConstantsString.DOT ) ) {
                return getFieldTypeByNameForInnerFields( clazz, name.substring( name.indexOf( ConstantsString.DOT ) + 1 ) );
            } else {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.FIELD_NOT_FOUND_IN_CLASS.getKey(), name, clazz.getName() ) );
            }

        }
        return null;
    }

    /**
     * A method use to get the generic type of the field in a given class.
     *
     * @param clazz
     *         the clazz
     * @param name
     *         the name
     *
     * @return the field type by name
     */
    public static Field getFieldByName( Class< ? > clazz, String name ) {
        if ( clazz != null ) {
            List< Field > fields = getAllFields( clazz );
            for ( Field field : fields ) {
                if ( field.getName().contentEquals( name ) ) {
                    return field;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.FIELD_NOT_FOUND_IN_CLASS.getKey(), name, clazz.getName() ) );
        }
        return null;

    }

    /**
     * A method to check if the class has given field.
     *
     * @param clazz
     *         the class
     * @param fieldName
     *         field name
     *
     * @return {@code true} if the given object has a field. false otherwise
     */
    public static boolean hasField( Class< ? > clazz, String fieldName ) {

        if ( clazz == null ) {
            return false;
        }

        List< Field > fields = getAllFields( clazz );
        for ( Field field : fields ) {
            if ( field.getName().equals( fieldName ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get all inherited and derived fields from the class.
     *
     * @param clazz
     *         the class
     *
     * @return list of Fields
     */
    public static List< Field > getAllFields( Class< ? > clazz ) {
        List< Field > fields = new ArrayList<>();
        for ( Class< ? > sourceClass = clazz; sourceClass != null; sourceClass = sourceClass.getSuperclass() ) {
            fields.addAll( Arrays.asList( sourceClass.getDeclaredFields() ) );
        }
        return fields;
    }

    /**
     * Sets final static field.
     *
     * @param clazz
     *         the clazz
     * @param fieldName
     *         the field name
     * @param newValue
     *         the new value
     *
     * @throws NoSuchFieldException
     *         the no such field exception
     * @throws IllegalAccessException
     *         the illegal access exception
     */
    public static void setFinalStaticField( Class< ? > clazz, String fieldName, Object newValue )
            throws NoSuchFieldException, IllegalAccessException {

        Field field = getFieldByName( clazz, fieldName );
        if ( field != null ) {
            field.setAccessible( true );

            // remove final modifier from field
            Field modifiersField = Field.class.getDeclaredField( MODIFIERS_STRING );
            modifiersField.setAccessible( true );
            modifiersField.setInt( field, field.getModifiers() & ~Modifier.FINAL );

            field.set( null, newValue );
        }
    }

    /**
     * Invokes the passed method.
     *
     * @param methodName
     *         the method name
     * @param objectDTO
     *         the object DTO
     *
     * @return the object
     */
    public static Object invokeMethod( String methodName, Object objectDTO ) {

        Object value = null;

        if ( objectDTO != null ) {
            Method method;

            try {
                method = objectDTO.getClass().getMethod( methodName );
                value = method.invoke( objectDTO );
            } catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                      | SecurityException e ) {
                ExceptionLogger.logException( e, ReflectionUtils.class );
                throw new SusException( MessageBundleFactory.getMessage( Messages.UNABLE_TO_PREPARE_ENTITY_OF_DTO.getKey(),
                        objectDTO.getClass().getSimpleName() ) );
            }

        }

        return value;
    }

    /**
     * Invoke method with list param.
     *
     * @param methodName
     *         the method name
     * @param objectDTO
     *         the object DTO
     * @param param
     *         the param
     *
     * @return the object
     */
    public static Object invokeMethodWithListParam( String methodName, Object objectDTO, Object... param ) {

        Object value = null;

        if ( objectDTO != null ) {
            Method method;

            try {
                method = objectDTO.getClass().getMethod( methodName, List.class );
                value = method.invoke( objectDTO, param );
            } catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                      | SecurityException e ) {
                ExceptionLogger.logException( e, ReflectionUtils.class );
                throw new SusException( MessageBundleFactory.getMessage( Messages.UNABLE_TO_PREPARE_ENTITY_OF_DTO.getKey(),
                        objectDTO.getClass().getSimpleName() ) );
            }

        }

        return value;
    }

    /**
     * Invoke set method with uuid param object.
     *
     * @param methodName
     *         the method name
     * @param objectDTO
     *         the object dto
     * @param param
     *         the param
     *
     * @return the object
     */
    public static Object invokeSetMethodWithUUIDParam( String methodName, Object objectDTO, Object... param ) {

        Object value = null;

        if ( objectDTO != null ) {
            Method method;

            try {
                method = objectDTO.getClass().getMethod( methodName, UUID.class );
                value = method.invoke( objectDTO, param );
            } catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                      | SecurityException e ) {
                ExceptionLogger.logException( e, ReflectionUtils.class );
                throw new SusException( MessageBundleFactory.getMessage( Messages.UNABLE_TO_PREPARE_ENTITY_OF_DTO.getKey(),
                        objectDTO.getClass().getSimpleName() ) );
            }

        }

        return value;
    }

    /**
     * Invokes prepare entity method of dto object.
     *
     * @param objectDTO
     *         the object DTO
     * @param userId
     *         the user id
     *
     * @return the object
     */
    public static Object invokePrepareEntity( Object objectDTO, String userId ) {
        Object entityObject = null;
        if ( objectDTO != null ) {
            Method method;
            try {
                method = objectDTO.getClass().getMethod( PREPARE_ENTITY_METHOD_OF_DTOS, String.class );
                entityObject = method.invoke( objectDTO, userId );
            } catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                      | SecurityException e ) {
                ExceptionLogger.logException( e, ReflectionUtils.class );
                throw new SusException( MessageBundleFactory.getMessage( Messages.UNABLE_TO_PREPARE_ENTITY_OF_DTO.getKey(),
                        objectDTO.getClass().getSimpleName() ) );
            }
        }

        return entityObject;
    }

}
