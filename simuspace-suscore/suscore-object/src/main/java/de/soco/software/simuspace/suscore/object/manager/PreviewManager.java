package de.soco.software.simuspace.suscore.object.manager;

import javax.persistence.EntityManager;

import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.data.entity.DataObject3DceetronEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectCurveEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectHtmlsEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectMovieEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectPredictionModelEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.model.DataObjectHtmlDTO;

/**
 * The interface Preview manager.
 */
public interface PreviewManager {

    /**
     * Create preview.
     *
     * @param entityManager
     *         the entity manager
     * @param documentDTO
     *         the document dto
     * @param userId
     *         the user id
     * @param dataObjectEntity
     *         the data object entity
     * @param documentEntity
     *         the document entity
     * @param objectDTO
     *         the object dto
     */
    void createPreview( EntityManager entityManager, DocumentDTO documentDTO, String userId, DataObjectEntity dataObjectEntity,
            DocumentEntity documentEntity, Object objectDTO );

    /**
     * Create data object movie files in fe temp.
     *
     * @param entityManager
     *         the entity manager
     * @param documentDTO
     *         the document dto
     * @param userID
     *         the user id
     * @param dataObjectEntity
     *         the data object entity
     */
    void createDataObjectMovieFilesInFeTemp( EntityManager entityManager, DocumentDTO documentDTO, String userID,
            DataObjectMovieEntity dataObjectEntity );

    /**
     * Create data object thumb nail.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param dataObjectImageEntity
     *         the data object image entity
     * @param documentDTO
     *         the document dto
     */
    void createDataObjectThumbNail( EntityManager entityManager, String userId, DataObjectEntity dataObjectImageEntity,
            DocumentDTO documentDTO );

    /**
     * Create data object curve.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param dataObjectCurve
     *         the data object curve
     * @param documentDTO
     *         the document dto
     */
    void createDataObjectCurve( EntityManager entityManager, String userId, DataObjectCurveEntity dataObjectCurve,
            DocumentDTO documentDTO );

    /**
     * Create data object prediction model files in fe temp.
     *
     * @param entityManager
     *         the entity manager
     * @param dataObjectPredictionEntity
     *         the data object prediction entity
     * @param documentDTO
     *         the document dto
     */
    void createDataObjectPredictionModelFilesInFETemp( EntityManager entityManager,
            DataObjectPredictionModelEntity dataObjectPredictionEntity, DocumentDTO documentDTO );

    /**
     * Create data object html files in fe temp.
     *
     * @param dataObjectHtmlsEntity
     *         the data object htmls entity
     * @param documentDTO
     *         the document dto
     * @param objectDTO
     *         the object dto
     */
    void createDataObjectHtmlFilesInFETemp( DataObjectHtmlsEntity dataObjectHtmlsEntity, DocumentDTO documentDTO,
            DataObjectHtmlDTO objectDTO );

    /**
     * Create data object ceetron files in fe cee.
     *
     * @param dataObject3DceetronEntity
     *         the data object 3 dceetron entity
     */
    void createDataObjectCeetronFilesInFeCee( DataObject3DceetronEntity dataObject3DceetronEntity );

    /**
     * Create data object pdf preview in fe temp.
     *
     * @param entityManager
     *         the entity manager
     * @param documentDTO
     *         the document dto
     */
    void createDataObjectPDFPreviewInFeTemp( EntityManager entityManager, DocumentDTO documentDTO );

}
