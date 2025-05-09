package de.soco.software.simuspace.suscore.object.utility;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class which will contain all the name of ObjectTypes exist in suscore
 *
 * @author Nosheen.Sharif
 */
public class ConstantsObjectTypes {

    /**
     * The PROJECT_TYPE constant.
     */
    public static final String PROJECT_TYPE = "project";

    /**
     * The VARIANT_TYPE constant.
     */
    public static final String VARIANT_TYPE = "variant";

    /**
     * The IMAGE_TYPE constant.
     */
    public static final String IMAGE_TYPE = "Image";

    /**
     * The PDF_TYPE constant
     */
    public static final String PDF_TYPE = "PDF";

    /**
     * The MOVIE_TYPE constant.
     */
    public static final String MOVIE_TYPE = "Movie";

    /**
     * The PREDICTION_MODEL_TYPE constant.
     */
    public static final String PREDICTION_MODEL_TYPE = "PredictionModel";

    /**
     * The List of possible object types in sus core.
     */
    public static final List< String > VALID_OBJECT_TYPES = Arrays.asList( PROJECT_TYPE, VARIANT_TYPE );

    /**
     * Constant Param ObjectId For Url.
     */
    public static final String OBJECT_ID_PARAM = "objectId";

    /**
     * Constant Param ObjectId For Url.
     */
    public static final String SEARCH_ID_PARAM = "searchId";

    /**
     * The Constant VERSION_ID_PARAM.
     */
    public static final String VERSION_ID_PARAM = "versionId";

    /**
     * The Constant PROJECT_ID_PARAM.
     */
    public static final String PROJECT_ID_PARAM = "projectId";

    /**
     * The Constant SELECTION_ID_PARAM.
     */
    public static final String SELECTION_ID_PARAM = "selectionId";

    /**
     * Private Constructor to avoid Object Instantiation
     */

    private ConstantsObjectTypes() {

    }

}
