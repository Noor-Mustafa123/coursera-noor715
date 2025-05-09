package de.soco.software.simuspace.suscore.common.enums;

/**
 * The Enum SusTree contains simuspace features which are shown in tree.
 *
 * @author Ahsan khan
 */
public enum SimuspaceFeaturesEnum {

    /**
     * The root.
     */
    ROOT( "Root", "de7dd517-5a0a-49ea-bea1-33fe2b334a69", "4000001x4" ),

    /**
     * The data.
     */
    DATA( "Data", "97fe736a-011f-46e3-971a-8ba77f711020", "4000017x4" ),

    /**
     * The system.
     */
    SYSTEM( "System", "9489ae1c-8e20-407c-8f30-f7357fb38016", "4000002x4" ),

    /**
     * The license.
     */
    LICENSES( "License", "3008fb63-1880-449a-956e-85c0470f6eac", "4000008x4" ),

    /**
     * The users.
     */
    USERS( "Users", "2d365f88-7dd4-4b2e-99bf-a53ff492ce22", "4000009x4" ),

    /**
     * The locations.
     */
    LOCATIONS( "Locations", "a2355d86-79b0-4fa5-855c-c8dd1fae87bd", "4000010x4" ),

    /**
     * The directories.
     */
    DIRECTORIES( "Directories", "f60de89e-f872-43eb-92e7-883db82df74e", "4000011x4" ),

    /**
     * The role.
     */
    ROLES( "Roles", "76597708-a578-489f-a465-a5db77a56f5f", "4000013x4" ),

    /**
     * The groups.
     */
    GROUPS( "Groups", "70729fba-9121-484a-b8b4-4b57ddc71751", "4000015x4" ),

    /**
     * The permissions.
     */
    RIGHTS( "Rights", "f91e8282-6079-4067-97f8-2d0d1f7cf11e", "3000389x4" ),

    /**
     * The audit.
     */
    AUDIT( "Audit Logs", "d2134fde-45d9-4045-b821-788e41e70398", "4000014x4" ),

    /**
     * The workflows.
     */
    WORKFLOWS( "Workflows", "6bf18669-57c1-434e-9d86-1f0ddb59aec9", "4000003x4" ),

    /**
     * The workflows_plotting.
     */
    WORKFLOWS_PLOTTING( "Plotting Workflows", "3765b33e-77a8-47a6-9492-8164e6e96c0e", "4000018x4" ),

    /**
     * The Jobs.
     */
    JOBS( "Jobs", "fd6aa6b6-fb12-4bea-ae22-7df7ccda657d", "4000004x4" ),

    /**
     * The elements.
     */
    ELEMENTS( "Elements", "ba949444-95b9-4b5d-a1ea-5df3e9bb108a", "4000006x4" ),

    /**
     * The allworkflows.
     */
    ALLWORKFLOWS( "Workflows", "dc14ac39-1243-484a-94ba-12db7bb46930", "4000007x4" ),

    /**
     * The deleted objects.
     */
    DELETED_OBJECTS( "Deleted objects", "acbb88c3-5a12-498d-ab23-26dc7d6fc000", "4000016x4" ),

    /**
     * The IMAGE_DATA_OBJECTS.
     */
    IMAGE_DATA_OBJECTS( "DataObjectImage", "67edfe99-bc9a-4f5a-a73d-b7c7b4c53081", "4000028x4" ),

    /**
     * The configuration.
     */
    CONFIGURATION( "Configuration", "cc24f7dc-a1f4-11e8-98d0-529269fb1459", "4000019x4" ),

    /**
     * The loadcases.
     */
    LOADCASES( "Loadcases", "cc250c04-a1f4-11e8-98d0-529269fb1459", "4000020x4" ),

    /**
     * The wfschemes.
     */
    WFSCHEMES( "WFSchemes", "39371a5a-2440-49fb-bd53-f0c2d1ad5017", "4000021x4" ),

    /**
     * The search.
     */
    SEARCH( "Search", "f258568b-e91c-4d6a-8b3a-922b41e2013f", "4000005x4" ),

    /**
     * The assembly.
     */
    ASSEMBLY( "Assembly", "1d0c5e21-617a-48b3-a28d-43124eab6163", "4000022x4" ),

    /**
     * The post.
     */
    POST( "Post", "06d5f03e-ac0b-4e4f-9678-f1eeb22ea72c", "4000023x4" ),

    /**
     * The cb2_connector.
     */
    CB2_CONNECTOR( "Cb2 connector", "57e76a44-11e2-4206-bf07-5523d64ec83f", "4000024x4" ),

    /**
     * The doe.
     */
    DOE( "Doe", "05af4280-1683-4351-aaf3-2aedb7414e27", "4000025x4" ),

    /**
     * The optimization.
     */
    OPTIMIZATION( "Optimization", "a662f78a-0b01-44c6-904b-b21893f76d09", "4000026x4" ),

    /**
     * The training algo.
     */
    TRAINING_ALGO( "TrainingAlgo", "62db977b-e952-4d60-91e2-e8d44e1ddf07", "4000027x4" ),

    /**
     * The workflow manager fature key.
     */
    WORKFLOW_MANAGER( "manager", "67935bc2-0506-11ee-be56-0242ac120002", "4000027x4" ),

    /**
     * The workflow user component key.
     */
    WORKFLOW_USER( "user", "711a595c-0506-11ee-be56-0242ac120002", "4000027x4" );

    /**
     * Instantiates a new simuspace features enum.
     *
     * @param key
     *         the key
     * @param id
     *         the id
     * @param code
     *         the code
     */
    SimuspaceFeaturesEnum( String key, String id, String code ) {
        this.key = key;
        this.id = id;
        this.code = code;
    }

    /**
     * The key.
     */
    private final String key;

    /**
     * The id.
     */
    private final String id;

    /**
     * The code.
     */
    private final String code;

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the code.
     *
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the code by id.
     *
     * @param id
     *         the id
     *
     * @return the code
     */
    public static String getCodeById( String id ) {
        String code = null;
        for ( SimuspaceFeaturesEnum simuspaceFeaturesEnum : values() ) {
            if ( simuspaceFeaturesEnum.getId().equalsIgnoreCase( id ) ) {
                code = simuspaceFeaturesEnum.getCode();
                break;
            }
        }
        return code;
    }

    /**
     * Gets key by id.
     *
     * @param id
     *         the id
     *
     * @return the key by id
     */
    public static String getKeyById( String id ) {
        String key = null;
        for ( SimuspaceFeaturesEnum simuspaceFeaturesEnum : values() ) {
            if ( simuspaceFeaturesEnum.getId().equalsIgnoreCase( id ) ) {
                key = simuspaceFeaturesEnum.getKey();
                break;
            }
        }
        return key;
    }
}