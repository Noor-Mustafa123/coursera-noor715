package de.soco.software.simuspace.suscore.data.entity;

import java.util.UUID;

/**
 * This interface defines object versions in the system with a VersionPrimaryKey as identifier consist of uuid and version Id . Some
 * entities in the system can implement this interface to have different revisions of objects persisted in database.
 *
 * @author Nosheen.Sharif
 */
public interface Versionable {

    /**
     * Gets id.
     *
     * @return the id
     */
    UUID getId();

    /**
     * Sets id.
     *
     * @param id
     *         the id
     */
    void setId( UUID id );

    /**
     * Gets the version id.
     *
     * @return the version id
     */
    int getVersionId();

    /**
     * Sets the version id.
     *
     * @param id
     *         the new version id
     */
    void setVersionId( int id );

}
