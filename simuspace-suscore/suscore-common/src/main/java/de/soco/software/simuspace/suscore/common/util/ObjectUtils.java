package de.soco.software.simuspace.suscore.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * A utility class for object that would hold the methods for comparing objects
 *
 * @author Zeeshan jamal
 */
public class ObjectUtils {

    /**
     * A method for comparing two objects and get the differences auditing.
     *
     * @param source
     *         the source
     * @param target
     *         the target
     *
     * @return the changes in source and target
     */
    public static List< String > compareObjects( Object source, Object target ) {
        List< Field > fields = new ArrayList<>();
        for ( Class< ? > sourceClass = source.getClass(); sourceClass != null; sourceClass = sourceClass.getSuperclass() ) {
            fields.addAll( Arrays.asList( sourceClass.getDeclaredFields() ) );
        }
        List< String > changes = new ArrayList<>();
        Object sourceObject;
        Object targetObject;
        try {
            for ( Field field : fields ) {
                field.setAccessible( true );
                if ( Collection.class.isAssignableFrom( field.getType() ) ) {
                    changes.addAll( invokeListObjectMethods( field, source, target ) );
                    continue;
                }
                sourceObject = field.get( source );
                targetObject = field.get( target );
                if ( sourceObject != null && targetObject != null && !sourceObject.equals( targetObject )
                        && !"file".equals( field.getName() ) ) {
                    changes.add( "Field Name : " + field.getName() + " | original value : " + sourceObject + " | changed value : "
                            + targetObject );
                }
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, e.getClass() );
        }
        return changes;
    }

    /**
     * A method for comparing two lists
     *
     * @param source
     *         the source
     * @param target
     *         the target
     *
     * @return the List of changes in source and target
     */
    public static List< String > compareLists( Collection< ? > source, Collection< ? > target ) {
        List< String > changes = new ArrayList<>();
        if ( source != null && target != null ) {

            Iterator< ? > sourceIterator = source.iterator();
            Iterator< ? > targetIterator = target.iterator();
            Object sourceObject;
            Object targetObject;
            try {
                while ( sourceIterator.hasNext() && targetIterator.hasNext() ) {
                    Object sourceOb = sourceIterator.next();
                    Object targetOb = targetIterator.next();
                    Field[] fields = sourceOb.getClass().getDeclaredFields();
                    for ( Field field : fields ) {
                        field.setAccessible( true );
                        if ( field.get( sourceOb ) instanceof UUID ) {
                            continue;
                        }
                        sourceObject = field.get( sourceOb );
                        targetObject = field.get( targetOb );
                        if ( sourceObject != null && targetObject != null && !sourceObject.equals( targetObject ) ) {
                            changes.add( "Field Name : " + field.getName() + " | original value : " + sourceObject + " | changed value : "
                                    + targetObject );
                        }
                    }
                }
            } catch ( Exception e ) {
                ExceptionLogger.logException( e, e.getClass() );
            }
        }
        return changes;
    }

    /**
     * @param field
     *         the field
     * @param source
     *         the source
     * @param target
     *         the target
     *
     * @return the list of changes
     */
    private static List< String > invokeListObjectMethods( Field field, Object source, Object target ) {

        for ( Method method : source.getClass().getMethods() ) {
            if ( ( method.getName().startsWith( "get" ) ) && ( method.getName().length() == ( field.getName().length() + 3 ) )
                    && method.getName().toLowerCase().endsWith( field.getName().toLowerCase() ) ) {
                // MZ: Method found, run it
                try {
                    Collection< ? > targetCollection = ( Collection< ? > ) target.getClass().getMethod( method.getName() ).invoke( target );
                    Collection< ? > sourceCollection = ( Collection< ? > ) method.invoke( source );
                    return compareLists( sourceCollection, targetCollection );
                } catch ( Exception e ) {
                    ExceptionLogger.logException( e, e.getClass() );
                }

            }
        }
        return new ArrayList<>();
    }

}
