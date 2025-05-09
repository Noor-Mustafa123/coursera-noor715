package de.soco.software.simuspace.suscore.location.manager;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.model.Directory;
import de.soco.software.simuspace.suscore.common.model.FileObject;
import de.soco.software.simuspace.suscore.data.model.UserUrlDTO;

public interface SsfsManager {

    /**
     * Save or update user url history.
     *
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param locationId
     *         the location id
     * @param userUrl
     *         the user url
     *
     * @return the user url DTO
     *
     * @apiNote To be used in service calls only
     */
    UserUrlDTO saveOrUpdateUserUrlHistory( String userId, String userName, UUID locationId, UserUrlDTO userUrl );

    /**
     * Gets the user url history.
     *
     * @param userId
     *         the user id
     * @param locationId
     *         the location id
     *
     * @return the user url history
     *
     * @apiNote To be used in service calls only
     */
    List< UserUrlDTO > getUserUrlHistory( String userId, UUID locationId );

    /**
     * Gets the files.
     *
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param directry
     *         the directry
     *
     * @return the files
     *
     * @apiNote To be used in service calls only
     */
    List< FileObject > getFiles( String userId, String userName, Directory directry );

    /**
     * Gets file content.
     *
     * @param directory
     *         the directory
     *
     * @return the file content
     *
     * @throws IOException
     *         the io exception
     */
    String getFileContent( Directory directory ) throws IOException;

}
