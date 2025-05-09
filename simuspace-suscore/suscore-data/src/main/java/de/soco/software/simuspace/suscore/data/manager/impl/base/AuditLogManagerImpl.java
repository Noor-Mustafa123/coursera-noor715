package de.soco.software.simuspace.suscore.data.manager.impl.base;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.subject.Subject;
import org.hibernate.jpa.QueryHints;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.util.BundleUtils;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.CompressionUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.data.common.dao.AuditLogDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.dao.TranslationDAO;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.SuscoreSession;
import de.soco.software.simuspace.suscore.data.entity.TranslationEntity;
import de.soco.software.simuspace.suscore.data.manager.base.AuditLogManager;
import de.soco.software.simuspace.suscore.data.manager.base.LicenseConfigurationCommonManager;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;

/**
 * An implementation class of AuditLogManager which holds all the business logic related to audit log and to communicate with audit log
 * dao.
 *
 * @author Zeeshan jamal
 */
public class AuditLogManagerImpl implements AuditLogManager {

    /**
     * The Constant RESTRICTED_USER.
     */
    private static final String RESTRICTED_USER = "Yes";

    /**
     * auditLogDAO reference.
     */
    private AuditLogDAO auditLogDAO;

    /**
     * The translation DAO.
     */
    private TranslationDAO translationDAO;

    /**
     * The sus DAO.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The license configuration common manager.
     */
    private LicenseConfigurationCommonManager licenseConfigurationCommonManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    private static final String AUDIT = "Audit";

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< AuditLogDTO > searchAuditLog( String userId, FiltersDTO filtersDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< AuditLogDTO > listToReturn;
        FilteredResponse< AuditLogDTO > auditLogDTOFilteredResponse;
        try {
            if ( !licenseConfigurationCommonManager.isFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.AUDIT.getKey(),
                    userId ) ) {
                UserDTO userDTO = getUserCommonManager().getUserById( entityManager, UUID.fromString( userId ) );
                throw new IncorrectCredentialsException(
                        MessageBundleFactory.getMessage( Messages.CANNOT_VERIFY_LICENSE.getKey(), userDTO.getName() ) );
            }
            if ( !isPermitted( entityManager, userId,
                    SimuspaceFeaturesEnum.AUDIT.getId() + ConstantsString.COLON + PermissionMatrixEnum.READ.getValue() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_READ.getKey(), AUDIT ) );
            }
            isRestricted( entityManager, UUID.fromString( userId ) );
            listToReturn = new ArrayList<>();
            final List< AuditLogEntity > resultSet = auditLogDAO.getAllFilteredRecords( entityManager, AuditLogEntity.class, filtersDTO );
            for ( AuditLogEntity auditLogEntity : resultSet ) {
                AuditLogDTO auditLogDTO = new AuditLogDTO();
                auditLogDTO.prepareAuditLogDTOFromEntity( auditLogEntity );
                listToReturn.add( auditLogDTO );
            }
            auditLogDTOFilteredResponse = PaginationUtil.constructFilteredResponse( filtersDTO, listToReturn );
        } finally {
            entityManager.close();
        }

        return auditLogDTOFilteredResponse;
    }

    /**
     * Checks if is permitted.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param resourcePermissionSet
     *         the resource permission set
     *
     * @return true, if is permitted
     */
    private boolean isPermitted( EntityManager entityManager, String userId, String resourcePermissionSet ) {
        if ( userId.equals( ConstantsID.SUPER_USER_ID ) ) {
            return true;
        }
        Subject subject = getSubjectByUserIdFromActiveSessions( entityManager, userId );
        if ( subject != null ) {
            boolean isPermited = subject.isPermitted( resourcePermissionSet );
            subject.getSession().touch();
            return isPermited;
        }
        return false;
    }

    /**
     * Gets the subject by user id from active sessions.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the subject by user id from active sessions
     */
    private Subject getSubjectByUserIdFromActiveSessions( EntityManager entityManager, String userId ) {
        SuscoreSession sessionEnt = getResultByUserId( entityManager, userId );
        if ( sessionEnt != null ) {
            Subject subject = new Subject.Builder().sessionId( CompressionUtils.deserialize( sessionEnt.getSession() ).getId() )
                    .buildSubject();
            return subject;
        }
        return null;
    }

    /**
     * Gets the result by user id.
     *
     * @param entityManager
     *         the entity manager
     * @param userid
     *         the userid
     *
     * @return the result by user id
     */
    private SuscoreSession getResultByUserId( EntityManager entityManager, String userid ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< SuscoreSession > query = cb.createQuery( SuscoreSession.class );
        Root< SuscoreSession > root = query.from( SuscoreSession.class );
        Predicate predicate = cb.equal( root.get( "userId" ), userid );
        query.where( predicate );
        return entityManager.createQuery( query ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList().stream().findFirst()
                .orElse( null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< AuditLogDTO > getLogListByDataObject( UUID objectId, FiltersDTO filtersDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        FilteredResponse< AuditLogDTO > auditLogDTOFilteredResponse;
        try {
            List< AuditLogDTO > listToReturn = new ArrayList<>();
            final List< AuditLogEntity > resultSet = auditLogDAO.getAllFilteredRecordsByObjectId( entityManager, AuditLogEntity.class,
                    objectId, filtersDTO );
            List< TranslationEntity > translationEntityList = translationDAO.getAllTranslationsByListOfUUID( entityManager,
                    resultSet.stream().map( auditLogEntity -> UUID.fromString( auditLogEntity.getObjectId() ) ).toList() );
            UserDTO user = TokenizedLicenseUtil
                    .getUser( BundleUtils.getUserTokenFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() ) );
            resultSet.forEach(
                    entity -> translateName( user, entity, translationEntityList.stream().filter( translationEntity -> translationEntity
                            .getSuSEntity().getComposedId().getId().toString().equals( entity.getObjectId() ) ).toList() ) );
            for ( AuditLogEntity auditLogEntity : resultSet ) {
                AuditLogDTO auditLogDTO = new AuditLogDTO();
                auditLogDTO.prepareAuditLogDTOFromEntity( auditLogEntity );
                listToReturn.add( auditLogDTO );
            }
            auditLogDTOFilteredResponse = PaginationUtil.constructFilteredResponse( filtersDTO, listToReturn );
        } finally {
            entityManager.close();
        }
        return auditLogDTOFilteredResponse;
    }

    /**
     * Translate name.
     *
     * @param user
     *         the user
     * @param entity
     *         the entity
     * @param translationEntities
     *         the translation entities
     */
    private void translateName( UserDTO user, AuditLogEntity entity, List< TranslationEntity > translationEntities ) {
        if ( PropertiesManager.hasTranslation() && null != user ) {
            if ( user.getId().equals( ConstantsID.SUPER_USER_ID ) ) {
                return;
            } else if ( null != user.getUserDetails() ) {
                String userLang = user.getUserDetails().iterator().next().getLanguage();
                translationEntities.forEach( translation -> {
                    if ( userLang.equals( translation.getLanguage() ) && null != translation.getName()
                            && !translation.getName().isEmpty() ) {
                        entity.setObjectName( translation.getName() );
                    }
                } );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< AuditLogDTO > getLogListByObjectAndVersionId( UUID objectId, int versionId, FiltersDTO filtersDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        FilteredResponse< AuditLogDTO > auditLogDTOFilteredResponse;
        try {
            List< AuditLogDTO > listToReturn = new ArrayList<>();
            final List< AuditLogEntity > resultSet = auditLogDAO.getAllFilteredRecordsByObjectIdAndVersionId( entityManager,
                    AuditLogEntity.class, objectId, versionId, filtersDTO );
            for ( AuditLogEntity auditLogEntity : resultSet ) {
                AuditLogDTO auditLogDTO = new AuditLogDTO();
                auditLogDTO.prepareAuditLogDTOFromEntity( auditLogEntity );
                listToReturn.add( auditLogDTO );
            }
            auditLogDTOFilteredResponse = PaginationUtil.constructFilteredResponse( filtersDTO, listToReturn );
        } finally {
            entityManager.close();
        }
        return auditLogDTOFilteredResponse;
    }

    @Override
    public List< Object > getAllValuesForAuditLogTableColumn( String columnName, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var allColumns = GUIUtils.listColumns( AuditLogDTO.class );
            GUIUtils.validateColumnForAllValues( columnName, allColumns );
            List< Object > allValues;
            if ( ConstantsString.DETAILS.equals( columnName ) ) {
                allValues = new ArrayList<>();
                var byteValues = auditLogDAO.getAllPropertyValues( entityManager, columnName );
                for ( var byteValue : byteValues ) {
                    allValues.add( ByteUtil.convertByteToString( ( byte[] ) byteValue ) );
                }
            } else {
                allValues = auditLogDAO.getAllPropertyValues( entityManager, columnName );
            }
            return allValues;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Checks if is restricted.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     */
    private void isRestricted( EntityManager entityManager, UUID userId ) {
        UserDTO userDTO = getUserCommonManager().getUserById( entityManager, userId );
        if ( userDTO != null && userDTO.getRestricted().equals( RESTRICTED_USER ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_RESTRICTED_TO_SYSTEM.getKey(), userDTO.getName() ) );
        }
    }

    /**
     * Sets the audit log DAO.
     *
     * @param auditLogDAO
     *         the new audit log DAO
     */
    public void setAuditLogDAO( AuditLogDAO auditLogDAO ) {
        this.auditLogDAO = auditLogDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewManager getObjectViewManager() {
        return objectViewManager;
    }

    /**
     * Sets the object view manager.
     *
     * @param objectViewManager
     *         the new object view manager
     */
    public void setObjectViewManager( ObjectViewManager objectViewManager ) {
        this.objectViewManager = objectViewManager;
    }

    /**
     * Gets the user common manager.
     *
     * @return the user common manager
     */
    public UserCommonManager getUserCommonManager() {
        return userCommonManager;
    }

    /**
     * Sets the user common manager.
     *
     * @param userCommonManager
     *         the new user common manager
     */
    public void setUserCommonManager( UserCommonManager userCommonManager ) {
        this.userCommonManager = userCommonManager;
    }

    /**
     * Sets the entity manager factory.
     *
     * @param entityManagerFactory
     *         the new entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * @return the licenseConfigurationCommonManager
     */
    public LicenseConfigurationCommonManager getLicenseConfigurationCommonManager() {
        return licenseConfigurationCommonManager;
    }

    /**
     * @param licenseConfigurationCommonManager
     *         the licenseConfigurationCommonManager to set
     */
    public void setLicenseConfigurationCommonManager( LicenseConfigurationCommonManager licenseConfigurationCommonManager ) {
        this.licenseConfigurationCommonManager = licenseConfigurationCommonManager;
    }

    /**
     * Gets the translation DAO.
     *
     * @return the translation DAO
     */
    public TranslationDAO getTranslationDAO() {
        return translationDAO;
    }

    /**
     * Sets the translation DAO.
     *
     * @param translationDAO
     *         the new translation DAO
     */
    public void setTranslationDAO( TranslationDAO translationDAO ) {
        this.translationDAO = translationDAO;
    }

    /**
     * Gets the sus DAO.
     *
     * @return the sus DAO
     */
    public SuSGenericObjectDAO< SuSEntity > getSusDAO() {
        return susDAO;
    }

    /**
     * Sets the sus DAO.
     *
     * @param susDAO
     *         the new sus DAO
     */
    public void setSusDAO( SuSGenericObjectDAO< SuSEntity > susDAO ) {
        this.susDAO = susDAO;
    }

}