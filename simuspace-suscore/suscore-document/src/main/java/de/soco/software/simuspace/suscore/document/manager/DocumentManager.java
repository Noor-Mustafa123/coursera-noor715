package de.soco.software.simuspace.suscore.document.manager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.document.dao.DocumentDAO;

/**
 * @author Ahmar Nadeem
 *
 * A contract for all document related functionalities
 */
public interface DocumentManager {

    /**
     * Gets the document list.
     *
     * @param filter
     *         the filter
     *
     * @return the document list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< DocumentDTO > getDocumentList( FiltersDTO filter );

    /**
     * Gets the top document list by user id.
     *
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     *
     * @return the top document list by user id
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< DocumentDTO > getDocumentListByUserId( UUID userId, FiltersDTO filter );

    /**
     * Gets the document.
     *
     * @param id
     *         the id
     *
     * @return the document
     *
     * @apiNote To be used in service calls only
     */
    DocumentDTO getDocumentById( UUID id );

    /**
     * Gets the document.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the document
     */
    DocumentDTO getDocumentById( EntityManager entityManager, UUID id );

    /**
     * Gets the documentEntity.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the document
     */
    DocumentEntity getDocumentEntityById( EntityManager entityManager, UUID id );

    /**
     * Update document.
     *
     * @param entityManager
     *         the entity manager
     * @param document
     *         the document
     *
     * @return the document
     */
    DocumentDTO updateDocument( EntityManager entityManager, DocumentDTO document );

    /**
     * Delete document.
     *
     * @param id
     *         the id
     *
     * @return the document
     *
     * @apiNote To be used in service calls only
     */
    boolean deleteDocument( UUID id );

    /**
     * Save document.
     *
     * @param entityManager
     *         the entity manager
     * @param document
     *         the document
     *
     * @return the document entity
     */
    DocumentDTO saveDocument( EntityManager entityManager, DocumentDTO document );

    /**
     * a helper function to write the file contents to the disk and return the new file path.
     *
     * @param browserDetails
     *         the browser details
     * @param document
     *         the document
     * @param stream
     *         the stream
     * @param printWriter
     *         the print writer
     *
     * @return the file
     */
    File writeToDiskInVault( String browserDetails, DocumentDTO document, InputStream stream );

    /**
     * Reads a document content from disk and returns a file
     *
     * @param document
     *         with documentId and versionId
     *
     * @return {@link File}
     */
    InputStream readVaultFromDisk( DocumentDTO document );

    /**
     * Read document from temp.
     *
     * @param document
     *         the document
     *
     * @return the input stream
     */
    InputStream readDocumentFromTemp( DocumentDTO document );

    /**
     * Prepare entity from document DTO.
     *
     * @param documentDTO
     *         the document DTO
     *
     * @return the document entity
     */
    DocumentEntity prepareEntityFromDocumentDTO( DocumentDTO documentDTO );

    /**
     * Prepare document DTO.
     *
     * @param entity
     *         the entity
     *
     * @return the document DTO
     */
    DocumentDTO prepareDocumentDTO( DocumentEntity entity );

    /**
     * Prepare entity from document DTO.
     *
     * @param documentDTO
     *         the document DTO
     * @param existingJsonEntity
     *         the existing json entity
     *
     * @return the document entity
     */
    DocumentEntity prepareEntityFromDocumentDTO( DocumentDTO documentDTO, DocumentEntity existingJsonEntity );

    /**
     * Write to disk in FE temp.
     *
     * @param entityManager
     *         the entity manager
     * @param document
     *         the document
     * @param tempPath
     *         the temp path
     *
     * @return the file
     */
    String writeToDiskInFETemp( EntityManager entityManager, DocumentDTO document, String tempPath );

    /**
     * Write to disk in FE temp.
     *
     * @param entityManager
     *         the entity manager
     * @param document
     *         the document
     * @param tempPath
     *         the temp path
     * @param hex
     *         the hex
     *
     * @return the file
     */
    String writeToDiskInFETemp( EntityManager entityManager, DocumentDTO document, String tempPath, String hex );

    /**
     * Gets the document DAO.
     *
     * @return the document DAO
     */
    DocumentDAO getDocumentDAO();

    /**
     * Gets the document by name.
     *
     * @param entityManager
     *         the entity manager
     * @param name
     *         the name
     *
     * @return the document by name
     */
    DocumentDTO getDocumentByName( EntityManager entityManager, String name );

    /**
     * Gets the document path in hex.
     *
     * @param document
     *         the document
     *
     * @return the document path in hex
     */
    String getDocumentPathInHex( DocumentDTO document );

    /**
     * Write all file to disk in FE temp.
     *
     * @param entityManager
     *         the entity manager
     * @param document
     *         the document
     * @param tempPath
     *         the temp path
     * @param fileExtension
     *         the file extension
     *
     * @return the string
     */
    String writeAllFileToDiskInFETemp( EntityManager entityManager, DocumentDTO document, String tempPath, String fileExtension );

    /**
     * Update document.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return the document entity
     */
    DocumentEntity saveOrUpdateDocument( EntityManager entityManager, DocumentEntity entity );

    /**
     * Gets the documents by ids.
     *
     * @param entityManager
     *         the entity manager
     * @param ids
     *         the ids
     *
     * @return the documents by ids
     */
    List< DocumentDTO > getDocumentsByIds( EntityManager entityManager, List< UUID > ids );

    /**
     * Gets entity manager factory.
     *
     * @return the entity manager factory
     */
    EntityManagerFactory getEntityManagerFactory();

}
