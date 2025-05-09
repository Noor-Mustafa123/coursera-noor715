package de.soco.software.simuspace.suscore.common.entity;

/**
 * This interface defines object versions in the system with a VersionPrimaryKey as identifier consist of uuid and version Id . Some
 * entities in the system can implement this interface to have different revisions of objects persisted in database.
 *
 * @author Nosheen.Sharif
 */
public interface Versionable {

    /**
     * Gets the version id.
     *
     * @return the version id
     */
    public int getVersionId();

    /**
     * Sets the version id.
     *
     * @param id
     *         the new version id
     */
    public void setVersionId( int id );

}
