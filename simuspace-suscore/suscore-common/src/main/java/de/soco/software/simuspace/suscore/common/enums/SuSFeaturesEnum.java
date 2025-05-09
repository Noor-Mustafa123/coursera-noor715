package de.soco.software.simuspace.suscore.common.enums;

/**
 * This class provides only features for simuspace.
 */
public enum SuSFeaturesEnum {

    /**
     * The license.
     */
    LICENSE( "License", "3008fb63-1880-449a-956e-85c0470f6eac", "de.soco.software.simuspace.suscore.data.entity.LicenseEntity" ),

    /**
     * The users.
     */
    USERS( "Users", "2d365f88-7dd4-4b2e-99bf-a53ff492ce22", "de.soco.software.simuspace.suscore.data.entity.UserEntity" ),

    /**
     * The directories.
     */
    DIRECTORIES( "Directories", "f60de89e-f872-43eb-92e7-883db82df74e",
            "de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity" ),

    /**
     * The role.
     */
    ROLE( "Role", "76597708-a578-489f-a465-a5db77a56f5f", "de.soco.software.simuspace.suscore.data.entity.RoleEntity" ),

    /**
     * The groups.
     */
    GROUPS( "Groups", "70729fba-9121-484a-b8b4-4b57ddc71751", "de.soco.software.simuspace.suscore.data.entity.GroupEntity" ),

    /**
     * The audit.
     */
    AUDIT( "Audit Logs", "d2134fde-45d9-4045-b821-788e41e70398", "de.soco.software.simuspace.suscore.data.entity.AuditLogEntity" ),

    /**
     * The locations.
     */
    LOCATIONS( "Locations", "a2355d86-79b0-4fa5-855c-c8dd1fae87bd", "de.soco.software.simuspace.suscore.data.entity.LocationEntity" ),

    /**
     * The deleted objects.
     */
    DELETED_OBJECTS( "Deleted objects", "acbb88c3-5a12-498d-ab23-26dc7d6fc000",
            "de.soco.software.simuspace.suscore.data.entity.DeletedObjectEntity" ),

    /**
     * The loadcases.
     */
    LOADCASES( "Loadcases", "cc250c04-a1f4-11e8-98d0-529269fb1459", "de.soco.software.simuspace.suscore.data.entity.LoadCaseEntity" ),

    /**
     * The wfschemes.
     */
    WFSCHEMES( "WFSchemes", "39371a5a-2440-49fb-bd53-f0c2d1ad5017", "de.soco.software.simuspace.suscore.data.entity.WFSchemeEntity" ),

    TRAINING_ALGO( "TrainingAlgo", "62db977b-e952-4d60-91e2-e8d44e1ddf07",
            "de.soco.software.simuspace.suscore.data.entity.TrainingAlgoNodeEntity" );

    /**
     * Instantiates a new simuspace features enum fully qualified.
     *
     * @param key
     *         the key
     * @param id
     *         the id
     * @param type
     *         the type
     */
    SuSFeaturesEnum( String key, String id, String type ) {
        this.key = key;
        this.id = id;
        this.type = type;
    }

    /**
     * key of the constant
     */
    private final String key;

    /**
     * value against the constant key
     */
    private final String id;

    /**
     * The type.
     */
    private final String type;

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }
}