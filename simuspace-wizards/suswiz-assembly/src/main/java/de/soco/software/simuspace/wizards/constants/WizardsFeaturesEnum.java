package de.soco.software.simuspace.wizards.constants;

/**
 * The Enum WizardsFeaturesEnum.
 */
public enum WizardsFeaturesEnum {

    /**
     * The config root.
     */
    CONFIGURATION( "Configuration", "cc24f7dc-a1f4-11e8-98d0-529269fb1459", "" ),

    /**
     * The loadcases.
     */
    LOADCASES( "Loadcases", "cc250c04-a1f4-11e8-98d0-529269fb1459",
            "de.soco.software.simuspace.suscore.data.entity.LoadCaseProjectEntity" ),

    /**
     * The wfschemes.
     */
    WFSCHEMES( "WFSchemes", "39371a5a-2440-49fb-bd53-f0c2d1ad5017",
            "de.soco.software.simuspace.suscore.data.entity.WFSchemeProjectEntity" ),

    /**
     * The training algo.
     */
    TRAINING_ALGO( "TrainingAlgo", "62db977b-e952-4d60-91e2-e8d44e1ddf07",
            "de.soco.software.simuspace.suscore.data.entity.TrainingAlgoNodeEntity" );

    /**
     * Instantiates a new simuspace features enum.
     *
     * @param key
     *         the key
     * @param id
     *         the id
     * @param type
     *         the type
     */
    WizardsFeaturesEnum( String key, String id, String type ) {
        this.key = key;
        this.id = id;
        this.type = type;
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
     * The id.
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
     * Gets the id.
     *
     * @return the id
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
