/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.constants.simflow;

/**
 * The Class PermissionLabels.
 *
 * @author Faisal.Hameed
 * @since 1.0
 */
public class PermissionLabels {

    /**
     * The Interface Operations.
     */
    public interface Operations {

        /**
         * The create.
         */
        String CREATE = "CR";

        /**
         * The read.
         */
        String READ = "RD";

        /**
         * The update.
         */
        String UPDATE = "UP";

        /**
         * The delete.
         */
        String DELETE = "DL";

        /**
         * The execute.
         */
        String EXECUTE = "XC";

        /**
         * The manage.
         */
        String MANAGE = "MN";

        /**
         * The kill.
         */
        String KILL = "KL";

        /**
         * The share.
         */
        String SHARE = "SH";

        /**
         * The create id.
         */
        int CREATE_ID = 1;

        /**
         * The read id.
         */
        int READ_ID = 2;

        /**
         * The update id.
         */
        int UPDATE_ID = 3;

        /**
         * The delete id.
         */
        int DELETE_ID = 4;

        /**
         * The execute id.
         */
        int EXECUTE_ID = 5;

        /**
         * The manage id.
         */
        int MANAGE_ID = 6;

        /**
         * The kill id.
         */
        int KILL_ID = 7;

        /**
         * The share id.
         */
        int SHARE_ID = 8;

    }

    /**
     * The Interface Resources.
     */
    public interface Resources {

        /**
         * The project.
         */
        String PROJECT = "pRoJ";

        /**
         * The project instance.
         */
        String PROJECT_INSTANCE = "pRoJi_";

        String LOCATION_INSTANCE = "lOci_";

        String SOLVER_INSTANCE = "sOli_";

        String SOLVER_VERSION_INSTANCE = "sOlVri_";

    }

    public enum DataResources {

        // Family Setting
        /**
         * The family.
         */
        FAMILY( "fMl", "Family" ),
        /**
         * The family type.
         */
        FAMILY_TYPE( "fMlT", "Family Type" ),
        /**
         * The dataobject format.
         */
        DATAOBJECT_FORMAT( "dOf", "Object Format" ),
        /**
         * The dataobject type.
         */
        DATAOBJECT_TYPE( "dOt", "Object Type" ),
        /**
         * The dataobject.
         */
        DATAOBJECT( "dObJ", "Data Object" ),
        /**
         * The process type.
         */
        PROCESS_TYPE( "pRcStP", "Process Type" ),

        /**
         * The discipline.
         */
        DISCIPLINE( "dSpL", "Disciplines" ),

        /**
         * The run import.
         */
        RUN_IMPORT( "runImport", "Run Import" ),
        /**
         * The project.
         */
        PROJECT( "pRoJ", "Projects" ),
        /**
         * The variant.
         */
        VARIANT( "vRnT", "SimVariant" ),

        // General
        /**
         * The user.
         */
        USER( "uSr", "Users" ),
        /**
         * The status.
         */
        STATUS( "sTaTs", "Status" ),
        /**
         * The ldap.
         */
        LDAP( "lDaP", "User Directories" ),
        /**
         * The policy.
         */
        POLICY( "pLcY", "Lifecycle Policies" ),
        /**
         * The process.
         */
        PROCESS( "_process", "Process" ),

        // Permissions
        /**
         * The profile permissions.
         */
        PROFILE_PERMISSIONS( "pRoPr", "Profile Permissions" ),
        /**
         * The user permissions.
         */
        USER_PERMISSIONS( "uSrPr", "User Permissions" ),
        /**
         * The global permissions.
         */
        GLOBAL_PERMISSIONS( "gLbPr", "Global Permissions" ),

        /**
         * The search.
         */
        SEARCH( "sRch", "Search" ),
        /**
         * The audit.
         */
        AUDIT( "aUdt", "Audit" ),
        /**
         * The dashboard.
         */
        DASHBOARD( "daShbrd", "Dashboards" ),

        /**
         * The upload server side.
         */
        UPLOAD_SERVER_SIDE( "uploadServerSide", "Upload Server Side" ),
        /**
         * The upload client side.
         */
        UPLOAD_CLIENT_SIDE( "uploadClientSide", "Upload Client Side" ),

        // Gadgets
        /**
         * The gadget quick links.
         */
        GADGET_QUICK_LINKS( "quickLinks", "Gadgets-Quick Links" ),
        /**
         * The gadget my processes.
         */
        GADGET_MY_PROCESSES( "myProcesses", "Gadgets-My Processes" ),
        /**
         * The gadget my result.
         */
        GADGET_MY_RESULT( "myResults", "Gadgets-My SimResults" ),
        /**
         * The gadget my variants.
         */
        GADGET_MY_VARIANTS( "myVariants", "Gadgets-My SimVariants" ),
        /**
         * The gadget my decks.
         */
        GADGET_MY_DECKS( "myDecks", "Gadgets-My Decks" ),
        /**
         * The gadget active users.
         */
        GADGET_ACTIVE_USERS( "activeusers", "Gadgets-Active Users" ),
        /**
         * The gadget system health.
         */
        GADGET_SYSTEM_HEALTH( "syshealth", "Gadgets-System Health" ),
        /**
         * The gadget system info.
         */
        GADGET_SYSTEM_INFO( "sysInfo", "Gadgets-System Information" );

        /**
         * The key.
         */
        private final String key;

        /**
         * The name.
         */
        private final String name;

        /**
         * Instantiates a new resource.
         *
         * @param key
         *         the key
         * @param name
         *         the name
         */
        DataResources( String key, String name ) {
            this.key = key;
            this.name = name;
        }

        public String[] getRunResources() {

            return new String[]{};
        }

        /**
         * Gets the key.
         *
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * Gets the name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }
    }

    /**
     * The Enum Resource. Defines all available resources in the system.
     */
    public enum RunResources {

        // HPC setting
        /**
         * The host.
         */
        HOST( "hOsT", "Nodes" ),
        /**
         * The hpc.
         */
        HPC( "hPc", "HPC" ),
        /**
         * The hpc cell.
         */
        HPC_CELL( "hPcCl", "HPC Cells" ),
        /**
         * The pe.
         */
        PE( "pE", "Job Environments" ),
        /**
         * The queue.
         */
        QUEUE( "qUe", "Applications" ),
        /**
         * The solver.
         */
        SOLVER( "sOl", "Solvers" ),
        /**
         * The solver family.
         */
        SOLVER_FAMILY( "sOlFm", "Solver Family" ),
        /**
         * The solver version.
         */
        SOLVER_VERSION( "sOlVr", "Solver Versions" ),

        /**
         * The license server.
         */
        LICENSE_SERVER( "lcns_srvr", "Licence Server" ),

        /**
         * The resource monitor.
         */
        RESOURCE_MONITOR( "rSr_mnTor", "Resource Monitor" ),

        /**
         * The node monitor.
         */
        NODE_MONITOR( "nMonT", "Node Monitor" ),

        /**
         * The app monitor.
         */
        APP_MONITOR( "aMonT", "App Monitor" ),

        /**
         * The license monitor.
         */
        LICENSE_MONITOR( "lMonT", "License Monitor" ),

        /**
         * The run simulation.
         */
        RUN_SIMULATION( "runSimulation", "Run Simulation" );

        /**
         * The key.
         */
        private final String key;

        /**
         * The name.
         */
        private final String name;

        /**
         * Instantiates a new resource.
         *
         * @param key
         *         the key
         * @param name
         *         the name
         */
        RunResources( String key, String name ) {
            this.key = key;
            this.name = name;
        }

        public String[] getRunResources() {

            return new String[]{};
        }

        /**
         * Gets the key.
         *
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * Gets the name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

    }

    /**
     * The Enum Resource. Defines all available resources in the system.
     */
    public enum LocationResources {

        /**
         * The location.
         */
        LOCATION( "lOc", "Locations" ),
        /**
         * The protocol configurations.
         */
        PROTOCOL_CONFIGURATIONS( "protocolCfg", "Protocols" ),
        /**
         * The location instance.
         */
        LOCATION_INSTANCE( "lOci_", "Location Instance" );

        /**
         * The key.
         */
        private final String key;

        /**
         * The name.
         */
        private final String name;

        /**
         * Instantiates a new resource.
         *
         * @param key
         *         the key
         * @param name
         *         the name
         */
        LocationResources( String key, String name ) {
            this.key = key;
            this.name = name;
        }

        public String[] getRunResources() {

            return new String[]{};
        }

        /**
         * Gets the key.
         *
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * Gets the name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }
    }

    /**
     * The Enum Resource. Defines all available resources in the system.
     */
    public enum Resource {

        // Family Setting
        /**
         * The family.
         */
        FAMILY( "fMl", "Family" ),
        /**
         * The family type.
         */
        FAMILY_TYPE( "fMlT", "Family Type" ),
        /**
         * The dataobject format.
         */
        DATAOBJECT_FORMAT( "dOf", "Object Format" ),
        /**
         * The dataobject type.
         */
        DATAOBJECT_TYPE( "dOt", "Object Type" ),
        /**
         * The dataobject.
         */
        DATAOBJECT( "dObJ", "Data Object" ),
        /**
         * The process type.
         */
        PROCESS_TYPE( "pRcStP", "Process Type" ),

        // HPC setting
        /**
         * The department.
         */
        DEPARTMENT( "dEpt", "Departments" ),
        /**
         * The location.
         */
        LOCATION( "lOc", "Locations" ),
        /**
         * The protocol configurations.
         */
        PROTOCOL_CONFIGURATIONS( "protocolCfg", "Protocols" ),
        /**
         * The location instance.
         */
        LOCATION_INSTANCE( "lOci_", "Location Instance" ),
        /**
         * The host.
         */
        HOST( "hOsT", "Nodes" ),
        /**
         * The hpc.
         */
        HPC( "hPc", "HPC" ),
        /**
         * The hpc cell.
         */
        HPC_CELL( "hPcCl", "HPC Cells" ),
        /**
         * The pe.
         */
        PE( "pE", "Job Environments" ),
        /**
         * The queue.
         */
        QUEUE( "qUe", "Applications" ),

        // General
        /**
         * The user.
         */
        USER( "uSr", "Users" ),
        /**
         * The status.
         */
        STATUS( "sTaTs", "Status" ),
        /**
         * The ldap.
         */
        LDAP( "lDaP", "User Directories" ),
        /**
         * The policy.
         */
        POLICY( "pLcY", "Lifecycle Policies" ),
        /**
         * The process.
         */
        PROCESS( "_process", "Process" ),

        // Permissions
        /**
         * The profile permissions.
         */
        PROFILE_PERMISSIONS( "pRoPr", "Profile Permissions" ),
        /**
         * The user permissions.
         */
        USER_PERMISSIONS( "uSrPr", "User Permissions" ),
        /**
         * The global permissions.
         */
        GLOBAL_PERMISSIONS( "gLbPr", "Global Permissions" ),

        // Misc
        /**
         * The run simulation.
         */
        RUN_SIMULATION( "runSimulation", "Run Simulation" ),
        /**
         * The run import.
         */
        RUN_IMPORT( "runImport", "Run Import" ),
        /**
         * The memcache.
         */
        MEMCACHE( "mEmC", "Memcache" ),
        /**
         * The scheduler.
         */
        SCHEDULER( "sChD", "Scheduler" ),
        /**
         * The work flow.
         */
        WORK_FLOW( "wOfL", "Workflow" ),
        /**
         * The upload server side.
         */
        UPLOAD_SERVER_SIDE( "uploadServerSide", "Upload Server Side" ),
        /**
         * The upload client side.
         */
        UPLOAD_CLIENT_SIDE( "uploadClientSide", "Upload Client Side" ),

        // Solver setting
        /**
         * The solver.
         */
        SOLVER( "sOl", "Solvers" ),
        /**
         * The solver family.
         */
        SOLVER_FAMILY( "sOlFm", "Solver Family" ),
        /**
         * The solver version.
         */
        SOLVER_VERSION( "sOlVr", "Solver Versions" ),
        /**
         * The discipline.
         */
        DISCIPLINE( "dSpL", "Disciplines" ),

        // Others
        /**
         * The project.
         */
        PROJECT( "pRoJ", "Projects" ),
        /**
         * The project instance.
         */
        PROJECT_INSTANCE( "pRoJi_", "Project Instance" ),
        /**
         * The variant.
         */
        VARIANT( "vRnT", "SimVariant" ),
        /**
         * The search.
         */
        SEARCH( "sRch", "Search" ),
        /**
         * The permissions.
         */
        PERMISSIONS( "pRmsn", "Permissions" ),
        /**
         * The license server.
         */
        LICENSE_SERVER( "lcns_srvr", "Licence Server" ),
        /**
         * The license.
         */
        WFD_LICENSE( "lIcE", "WFD-License" ),
        /**
         * The audit.
         */
        AUDIT( "aUdt", "Audit" ),
        /**
         * The resource monitor.
         */
        RESOURCE_MONITOR( "rSr_mnTor", "Resource Monitor" ),

        /**
         * The node monitor.
         */
        NODE_MONITOR( "nMonT", "Node Monitor" ),

        /**
         * The app monitor.
         */
        APP_MONITOR( "aMonT", "App Monitor" ),

        /**
         * The license monitor.
         */
        LICENSE_MONITOR( "lMonT", "License Monitor" ),
        /**
         * The dashboard.
         */
        DASHBOARD( "daShbrd", "Dashboards" ),

        /**
         * The unlock file.
         */
        UNLOCK_FILE( "unlk_file", "Unlock File" ),

        // Gadgets
        /**
         * The gadget quick links.
         */
        GADGET_QUICK_LINKS( "quickLinks", "Gadgets-Quick Links" ),
        /**
         * The gadget my processes.
         */
        GADGET_MY_PROCESSES( "myProcesses", "Gadgets-My Processes" ),
        /**
         * The gadget my result.
         */
        GADGET_MY_RESULT( "myResults", "Gadgets-My SimResults" ),
        /**
         * The gadget my variants.
         */
        GADGET_MY_VARIANTS( "myVariants", "Gadgets-My SimVariants" ),
        /**
         * The gadget my decks.
         */
        GADGET_MY_DECKS( "myDecks", "Gadgets-My Decks" ),
        /**
         * The gadget active users.
         */
        GADGET_ACTIVE_USERS( "activeusers", "Gadgets-Active Users" ),
        /**
         * The gadget system health.
         */
        GADGET_SYSTEM_HEALTH( "syshealth", "Gadgets-System Health" ),
        /**
         * The gadget system info.
         */
        GADGET_SYSTEM_INFO( "sysInfo", "Gadgets-System Information" ),

        // Extra Resources
        USER_PROFILES( "", "User Profiles" ),
        USER_DETAIL( "", "User Detail" ),
        NOTIFICATIONS( "", "Notifications" ),
        WIDGET_SETTING( "", "Widget Settings" ),
        PROJECT_FILTER( "", "Project Filter" ),
        RESOURCE( "", "Resource" ),
        PROJECT_PERMISSION( "", "Project Permissions" ),
        PROFILE( "", "Profile" ),
        LDAP_ATTRIBUTES( "", "LDAP attributes" ),
        LDAP_MAPPING( "", "LDAP mapping" ),
        DATA_OBJECT_TYPE_ALLOWED_INPUT( "", "Object Type Allowed Input" ),
        DATA_OBJECT_TYPE_SETTINGS( "", "Object Type Settings" ),
        PROCESS_TYPE_ALLOWED_INPUT( "", "Process Type Allowed Input" ),
        PROCESS_TYPE_SETTINGS( "", "Process Type Settings" ),
        OBJECTSTATUS( "", "ObjectStatus" ),
        STATUS_SETTINGS( "", "Status Settings" ),
        META_DATA( "", "MetaData" ),
        OBJECT_META_DATA( "", "Object MetaData" );

        /**
         * Instantiates a new resource.
         *
         * @param key
         *         the key
         * @param name
         *         the name
         */
        Resource( String key, String name ) {
            this.key = key;
            this.name = name;
        }

        public String[] getRunResources() {

            return new String[]{};
        }

        /**
         * Gets the key.
         *
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * Sets the key.
         *
         * @param key
         *         the new key
         */
        public void setKey( String key ) {
            this.key = key;
        }

        /**
         * Gets the name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the name.
         *
         * @param name
         *         the new name
         */
        public void setName( String name ) {
            this.name = name;
        }

        /**
         * The key.
         */
        private String key;

        /**
         * The name.
         */
        private String name;

    }

    /**
     * The Enum Operation. Defines the all available operations for permissions.
     */
    public enum Operation {

        /**
         * The create.
         */
        CREATE( 1, "CR", "Create" ),
        /**
         * The read.
         */
        READ( 2, "RD", "Read" ),
        /**
         * The update.
         */
        UPDATE( 3, "UP", "Update" ),
        /**
         * The delete.
         */
        DELETE( 4, "DL", "Delete" ),
        /**
         * The execute.
         */
        EXECUTE( 5, "XC", "Execute" ),
        /**
         * The manage.
         */
        MANAGE( 6, "MN", "Manage" ),
        /**
         * The kill.
         */
        KILL( 7, "KL", "Kill" ),
        /**
         * The share.
         */
        SHARE( 8, "SH", "Share" );

        /**
         * Instantiates a new operation.
         *
         * @param id
         *         the id
         * @param key
         *         the key
         * @param name
         *         the name
         */
        Operation( int id, String key, String name ) {
            this.id = id;
            this.key = key;
            this.name = name;
        }

        /**
         * Gets the key.
         *
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * Gets the name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Gets the id.
         *
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * The key.
         */
        private final String key;

        /**
         * The name.
         */
        private final String name;

        /**
         * The id.
         */
        private final int id;

    }

}
