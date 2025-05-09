package de.soco.software.simuspace.suscore.data.dbhelper;

/**
 * @author fahad
 */
public class SuSQueryConstants {

    public static final String GET_QUERY_FOR_PROJECT_WITH_OBJECT_TYPES = "select distinct prj.id, prj.project_name, prj.version_label, prj.life_cycle_strategy_id, prj.contains, objtype.table_name from object_type objtype, container_object_relationship cor, project prj where cor.container_id=prj.id and cor.object_id=objtype.Object_Type_id and prj.id =CAST(:uuID AS uuid) ";

    public static final String GET_ALL_OBJECT_TYPES_UNDER_PROJECT = "select * from  ";

    public static final String CREATE_OBJECT = "";

    /**
     * Query string for getting file id from project_metadata table
     */
    public static final String SELECT_FROM_PROJECT_METADATA = "SELECT ome FROM ProjectMetadataEntity ome ";

    /**
     * Query string for getting file id from variant_metadata table
     */
    public static final String SELECT_FROM_VARIANT_METADATA = "SELECT ome FROM VariantMetadataEntity ome ";

    public static String returnTableNameQuery( String tableName ) {
        return GET_ALL_OBJECT_TYPES_UNDER_PROJECT.concat( "" ).concat( tableName );

    }

}
