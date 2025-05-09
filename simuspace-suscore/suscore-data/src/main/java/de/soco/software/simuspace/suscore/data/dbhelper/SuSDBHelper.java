package de.soco.software.simuspace.suscore.data.dbhelper;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;

/**
 * @author fahad
 */
public class SuSDBHelper {

    public static Session session;

    @SuppressWarnings( "unchecked" )
    public static List< Object[] > cretaeNativeSQLForProjectTopLevelAndReturnList() {

        return session.createSQLQuery( SuSQueryConstants.GET_QUERY_FOR_PROJECT_WITH_OBJECT_TYPES )
                .addScalar( "id", StandardBasicTypes.UUID_BINARY ).addScalar( "project_name", StandardBasicTypes.STRING )
                .addScalar( "table_name", StandardBasicTypes.STRING ).setParameter( "uuID", "56a697e8-1854-11e7-93ae-92361f002671" ).list();
    }

    @SuppressWarnings( "unchecked" )
    public static List< Object[] > cretaeNativeSQLForObjectTypesUnderProject( String tableName ) {
        return session.createSQLQuery( SuSQueryConstants.returnTableNameQuery( tableName ) )
                .addScalar( "id", StandardBasicTypes.UUID_BINARY ).addScalar( tableName + "_name", StandardBasicTypes.STRING ).list();

    }

    public static Session getSession() {
        return session;
    }

    public static void setSession( Session session ) {
        SuSDBHelper.session = session;
    }

}
