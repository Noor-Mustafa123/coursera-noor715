package de.soco.software.simuspace.suscore.location.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDate;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserUrl;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.Directory;
import de.soco.software.simuspace.suscore.common.model.FileObject;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PasswordUtils;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.UserUrlEntity;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.data.model.UserUrlDTO;
import de.soco.software.simuspace.suscore.location.dao.SsfsDAO;
import de.soco.software.simuspace.suscore.location.manager.LocationManager;
import de.soco.software.simuspace.suscore.location.manager.SsfsManager;
import de.soco.software.simuspace.suscore.user.manager.UserManager;

/**
 * The Class SsfsManagerImpl.
 *
 * @author Ali Haider
 */
@Log4j2
public class SsfsManagerImpl implements SsfsManager {

    /**
     * The Constant PATH_KEY.
     */
    private static final String PATH_KEY = "path";

    /**
     * The Constant DIRECTORY_ONLY.
     */
    private static final String DIRECTORY_ONLY = "dirOnly";

    /**
     * The Constant USER_NAME.
     */
    private static final String USER_NAME = "userName";

    /**
     * The Constant PASS.
     */
    private static final String PASS = "password";

    /**
     * The Constant API_GET_FILES.
     */
    private static final String API_GET_FILES = "/api/core/location/files";

    /**
     * The Constant API_GET_USER_HOME.
     */
    private static final String API_GET_USER_HOME = "/api/core/location/userHome";

    /**
     * The user manager.
     */
    private UserManager userManager;

    /**
     * The ssfs DAO.
     */
    private SsfsDAO ssfsDAO;

    /**
     * The location manager.
     */
    private LocationManager locationManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserUrlDTO saveOrUpdateUserUrlHistory( String userId, String userName, UUID locationId, UserUrlDTO userUrl ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SusResponseDTO responseDTO;
            LocationDTO location = locationManager.getLocation( entityManager, locationId.toString() );
            userUrl.setLocationId( locationId );
            UserDTO user = userManager.getUserById( entityManager, userId, UUID.fromString( userId ) );
            if ( userUrl.getPath().isEmpty() ) {
                Map< String, String > pathMap = new HashMap<>();
                pathMap.put( USER_NAME, user.getUserUid() );
                pathMap.put( PASS, PasswordUtils.getPasswordById( userId ) );
                try {
                    responseDTO = SuSClient.postRequest( location.getUrl() + API_GET_USER_HOME, JsonUtils.toJson( pathMap ),
                            CommonUtils.prepareHeadersWithAuthToken( location.getAuthToken() ) );
                } catch ( Exception e ) {
                    log.error( e.getMessage(), e );
                    throw new SusException( e.getMessage() );
                }
                if ( responseDTO != null && responseDTO.getData() != null ) {
                    userUrl.setPath( responseDTO.getData().toString() );
                }

            }
            UserUrlEntity userUrlEntity = saveOrUpdateUserUrlHistory( entityManager, userId, userUrl );
            UserUrlDTO urlDTO = null;
            if ( userUrlEntity != null ) {
                urlDTO = prepareUserUrlDTO( userUrlEntity );
            }
            return urlDTO;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileContent( Directory directory ) throws IOException {
        Path path = Paths.get( directory.getDir() );
        if ( Files.notExists( path ) ) {
            throw new SusException( "File not available on server side : " + directory.getDir() );
        }
        return de.soco.software.simuspace.suscore.common.util.FileUtils.getFileFirstRequiredCharContent( directory.getDir(), 2048 );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserUrlDTO > getUserUrlHistory( String userId, UUID locationId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UserUrlEntity > allFilteredRecords = ssfsDAO.getAllUserUrlList( entityManager, userId, locationId );
            List< UserUrlDTO > userUrlDTOs = new ArrayList<>();
            for ( UserUrlEntity userUrlEntity : allFilteredRecords ) {
                userUrlDTOs.add( prepareUserUrlDTO( userUrlEntity ) );
            }
            return userUrlDTOs;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    @Override
    public List< FileObject > getFiles( String userId, String userName, Directory directry ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SusResponseDTO responseDTO;
            LocationDTO location = locationManager.getLocation( entityManager, directry.getLocationId().toString() );
            UserDTO user = userManager.getUserById( entityManager, userId, UUID.fromString( userId ) );
            entityManager.close();
            Map< String, String > pathMap = new HashMap<>();
            pathMap.put( PATH_KEY, directry.getDir() );
            pathMap.put( DIRECTORY_ONLY, directry.getShow() );
            pathMap.put( USER_NAME, user.getUserUid() );
            pathMap.put( PASS, PasswordUtils.getPasswordById( userId ) );
            if ( !LinuxUtils.checkUserImpersonation( user.getUserUid() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.USER_IS_NOT_A_SYSTEM_USER.getKey(), user.getUserUid() ) );
            }
            try {
                responseDTO = SuSClient.postRequest( location.getUrl() + API_GET_FILES, JsonUtils.toJson( pathMap ),
                        CommonUtils.prepareHeadersWithAuthToken( location.getAuthToken() ) );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
                throw new SusException( e.getMessage() );
            }
            if ( null == responseDTO || responseDTO.getData() == null ) {
                throw new SusException( "You don't have permission to read: \" " + directry.getDir() + " \"" + " or location is down" );
            }

            List< FileObject > fileList = JsonUtils.jsonToList( JsonUtils.convertListToJson( ( List< FileObject > ) responseDTO.getData() ),
                    FileObject.class );

            sortByDate( fileList );
            return fileList;
        } finally {
            if ( entityManager.isOpen() ) {
                entityManager.close();
            }
        }
    }

    /**
     * Sort by date.
     *
     * @param fileList
     *         the file list
     */
    private void sortByDate( List< FileObject > fileList ) {
        fileList.sort( ( o1, o2 ) -> {
            Date d1 = DateUtils.convertFromMilliSecondToSpecifiedFormat( o1.getUpdate(), ConstantsDate.DATE_TIME_US_FORMAT );
            Date d2 = DateUtils.convertFromMilliSecondToSpecifiedFormat( o2.getUpdate(), ConstantsDate.DATE_TIME_US_FORMAT );
            return d2.compareTo( d1 );
        } );
    }

    /**
     * Save or update user url history.
     *
     * @param userId
     *         the user id
     * @param userUrl
     *         the user url
     *
     * @return the user url entity
     */
    private UserUrlEntity saveOrUpdateUserUrlHistory( EntityManager entityManager, String userId, UserUrlDTO userUrl ) {
        UserUrlEntity userUrlEntity = null;
        if ( null != userUrl ) {
            UserEntity userEntity = userManager.getUserEntityById( entityManager, UUID.fromString( userId ) );
            userUrlEntity = prepareUserUrlEntity( userEntity, userUrl );
            // Checking if url already exist in db
            UserUrlEntity dbUserUrlEntity = ssfsDAO.getUserUrlByPath( entityManager, userUrlEntity );
            if ( dbUserUrlEntity == null ) {
                userUrlEntity = ssfsDAO.save( entityManager, userUrlEntity );
            } else {
                dbUserUrlEntity.setModifiedOn( new Date() );
                userUrlEntity = ssfsDAO.saveOrUpdate( entityManager, dbUserUrlEntity );
            }
        }
        return userUrlEntity;
    }

    /**
     * Prepare user url entity.
     *
     * @param user
     *         the user
     * @param userUrlDTO
     *         the user url DTO
     *
     * @return the user url entity
     */
    private UserUrlEntity prepareUserUrlEntity( UserEntity user, UserUrlDTO userUrlDTO ) {
        Date date = new Date();
        UserUrlEntity userUrlEntity = new UserUrlEntity();
        userUrlEntity.setId( UUID.randomUUID() );
        userUrlEntity.setPath( userUrlDTO.getPath() );
        userUrlEntity.setCreatedBy( user );
        userUrlEntity.setLabel( userUrlDTO.getLabel() );
        userUrlEntity.setCreatedOn( date );
        userUrlEntity.setModifiedOn( date );
        userUrlEntity.setLocationId( userUrlDTO.getLocationId() );
        userUrlEntity.setBookmark( ConstantsUserUrl.HISTORY );
        return userUrlEntity;
    }

    /**
     * Prepare user url DTO.
     *
     * @param userUrlEntity
     *         the user url entity
     *
     * @return the user url DTO
     */
    private UserUrlDTO prepareUserUrlDTO( UserUrlEntity userUrlEntity ) {
        UserUrlDTO userUrlDTO = new UserUrlDTO();
        userUrlDTO.setPath( userUrlEntity.getPath() );
        userUrlDTO.setId( userUrlEntity.getId() );
        userUrlDTO.setLabel( userUrlEntity.getLabel() );
        userUrlDTO.setDate( userUrlEntity.getModifiedOn() );
        userUrlDTO.setLocationId( userUrlEntity.getLocationId() );
        return userUrlDTO;
    }

    /**
     * Gets the user manager.
     *
     * @return the user manager
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Sets the user manager.
     *
     * @param userManager
     *         the new user manager
     */
    public void setUserManager( UserManager userManager ) {
        this.userManager = userManager;
    }

    /**
     * Gets the ssfs DAO.
     *
     * @return the ssfs DAO
     */
    public SsfsDAO getSsfsDAO() {
        return ssfsDAO;
    }

    /**
     * Sets the ssfs DAO.
     *
     * @param ssfsDAO
     *         the new ssfs DAO
     */
    public void setSsfsDAO( SsfsDAO ssfsDAO ) {
        this.ssfsDAO = ssfsDAO;
    }

    /**
     * Gets the location manager.
     *
     * @return the location manager
     */
    public LocationManager getLocationManager() {
        return locationManager;
    }

    /**
     * Sets the location manager.
     *
     * @param locationManager
     *         the new location manager
     */
    public void setLocationManager( LocationManager locationManager ) {
        this.locationManager = locationManager;
    }

    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}
