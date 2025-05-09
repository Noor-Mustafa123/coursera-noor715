package de.soco.software.simuspace.suscore.license.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.BaseManager;
import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDbOperationTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLicenseType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.SystemGenratedViewEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.exceptions.FeatureNotAllowedException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.ModuleLicenseDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.SuSCoreSessionDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.UserLimitDTO;
import de.soco.software.simuspace.suscore.common.model.UserTokenDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.Renderer;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.DateFormatStandard;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LicenseUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.AuditLogDAO;
import de.soco.software.simuspace.suscore.data.common.dao.LicenseConfigurationDAO;
import de.soco.software.simuspace.suscore.data.common.dao.LicenseUserDAO;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.common.model.MonitorLicenseCurveDTO;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.JobTokenEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseFeatureEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseUserEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.UserTokenEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;
import de.soco.software.simuspace.suscore.license.dao.LicenseFeatureDAO;
import de.soco.software.simuspace.suscore.license.manager.LicenseConfigurationManager;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.license.manager.MonitorLicenseManager;
import de.soco.software.simuspace.suscore.license.model.LicenseForm;
import de.soco.software.simuspace.suscore.license.model.LicenseManageForm;
import de.soco.software.simuspace.suscore.license.model.UserLicensesDTO;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.suscore.jsonschema.model.HistoryMap;

/**
 * The Class is responsible for providing license feature. Mainly is doing the license signing and then its verification if all the
 * informations seems to be valid it will preserve license. Later is providing license for usage.
 *
 * @author M.Nasir.Farooq
 */
@Log4j2
public class LicenseConfigurationManagerImpl extends BaseManager implements LicenseConfigurationManager {

    /**
     * The Constant SUS_DEFAULT_VIEW_NAME.
     */
    private static final String SUS_DEFAULT_VIEW_NAME = "SusDefault";

    /**
     * The Constant RESTRICTED_USER.
     */
    private static final String RESTRICTED_USER = "Yes";

    /**
     * The Constant FIELD_USER_NAME.
     */
    private static final String FIELD_USER_NAME = "uid";

    /**
     * The Constant MODE_SINGLE.
     */
    private static final String MODE_SINGLE = "single";

    /**
     * The Constant MODE_BULK.
     */
    private static final String MODE_BULK = "bulk";

    /**
     * The Constant USER_TYPE_COLUMN_NAME.
     */
    public static final String USER_TYPE_COLUMN_NAME = "userType";

    /**
     * The Constant MODULE_COLUMN_NAME_PREFIX.
     */
    public static final String MODULE_COLUMN_NAME_PREFIX = "modules-";

    /**
     * The Constant LICENSE_TYPE_NAMED.
     */
    public static final String LICENSE_TYPE_NAMED = "named";

    /**
     * The Stop Jobe Query Parameter to free license
     */
    public static final String FREE_LICENSE = "?mode=freeLicense";

    /**
     * The Constant LICENSE_PLUGIN_NAME.
     */
    public static final String LICENSE_PLUGIN_NAME = "plugin_license";

    /**
     * The Constant MESSAGE_FEATURE_IS_NOT_ALLOWED_TO_USER.
     */
    private static final String MESSAGE_FEATURE_IS_NOT_ALLOWED_TO_USER = " is not allowed to user or feature is not attached to any module";

    /**
     * The Constant MESSAGE_EITHER_FEATURE.
     */
    private static final String MESSAGE_EITHER_FEATURE = "Either feature: ";

    /**
     * The Constant MESSAGE_IS_ALLOWED_TO_USER.
     */
    private static final String MESSAGE_IS_ALLOWED_TO_USER = "is allowed to user.";

    /**
     * The license dao.
     */
    private LicenseConfigurationDAO licenseConfigDao;

    /**
     * The license user dao.
     */
    private LicenseUserDAO licenseUserDao;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The context menu manager.
     */
    private ContextMenuManager contextMenuManager;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private LicenseFeatureDAO licenseFeatureDAO;

    /**
     * The audit log DAO.
     */
    private AuditLogDAO auditLogDAO;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The token manager.
     */
    private UserTokenManager tokenManager;

    /**
     * The monitor license manager.
     */
    private MonitorLicenseManager monitorLicenseManager;

    /**
     * The selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Constant LICENS.
     */
    public static final String LICENSE = "License";

    /**
     * Instantiates a new license configuration manager impl.
     */
    public LicenseConfigurationManagerImpl() {
        super( LicenseConfigurationManagerImpl.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModuleLicenseDTO addLicense( String userId, ModuleLicenseDTO license ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ModuleLicenseDTO moduleLicenseDTO;
        try {
            checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.LICENSES.getKey(), getUserIdFromMessage() );

            if ( license == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.LICENSE_INPUT_CANNOT_BE_NULL.getKey() ) );
            }

            verifyLicenseFeature( license );
            UserDTO userEntity = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );

            Notification notify = license.validate();
            if ( notify.hasErrors() ) {
                throw new SusException( notify.getErrors().toString() );
            }

            if ( !userEntity.getId().equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                    SimuspaceFeaturesEnum.LICENSES.getId() + ConstantsString.COLON + PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_CREATE.getKey(), LICENSE ) );
            }
            LicenseEntity existingLicenseEntity = null;
            ModuleLicenseDTO alreadyExistingLicenseDTO = null;
            if ( StringUtils.isNotBlank( license.getModule() ) ) {
                existingLicenseEntity = licenseConfigDao.getModuleLicense( entityManager, license.getModule() );
                alreadyExistingLicenseDTO = prepareLicenseDTO( entityManager, existingLicenseEntity );
            }
            if ( alreadyExistingLicenseDTO != null ) {
                if ( !userEntity.getId().equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                        SimuspaceFeaturesEnum.LICENSES.getId() + ConstantsString.COLON + PermissionMatrixEnum.WRITE.getValue() ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_UPDATE.getKey(), LICENSE ) );
                }
                isRestricted( entityManager, UUID.fromString( userId ) );
                for ( String licenseFeature : alreadyExistingLicenseDTO.getFeatures() ) {
                    LicenseFeatureEntity licenseFeatureEntity = licenseConfigDao.getLicenseFeature( entityManager, licenseFeature );
                    licenseFeatureEntity.setLicenseEntity( null );
                    licenseFeatureDAO.saveOrUpdate( entityManager, licenseFeatureEntity );
                    licenseFeatureDAO.deleteLicenseFeature( entityManager, licenseFeatureEntity );
                }

            }
            verifyLicenseAndExpiry( license );
            for ( String licenseFeature : license.getFeatures() ) {
                LicenseFeatureEntity licenseFeatureEntity = licenseConfigDao.getLicenseFeature( entityManager, licenseFeature );
                if ( licenseFeatureEntity != null ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.FEATURE_IS_ALREADY_ATTACHED_WITH_LICENSE.getKey(),
                            licenseFeatureEntity.getFeature(), licenseFeatureEntity.getLicenseEntity().getModule() ) );
                }
            }
            LicenseEntity licenseEntity =
                    existingLicenseEntity == null ? prepareLicenseEntity( license ) : updateLicenseEntity( license, existingLicenseEntity );

            if ( alreadyExistingLicenseDTO != null ) {
                // Audit log entry with comparison before/after
                AuditLogEntity auditLog = AuditLogDTO.prepareAuditLogEntityForUpdatedObjects( userId, alreadyExistingLicenseDTO, license );
                auditLogDAO.save( entityManager, auditLog );

            } else {
                // Audit log entry
                UserEntity user = new UserEntity();
                user.setId( UUID.fromString( userId ) );
                AuditLogEntity auditLog = new AuditLogEntity( UUID.randomUUID(), new Date(), user, licenseEntity.getModule(),
                        ConstantsDbOperationTypes.CREATED,
                        "License Module:" + licenseEntity.getModule() + " " + ConstantsDbOperationTypes.CREATED,
                        ConstantsDbOperationTypes.CREATED );

                auditLogDAO.save( entityManager, auditLog );
            }

            LicenseEntity retLicenseEntity = licenseConfigDao.addLicense( entityManager, licenseEntity );
            moduleLicenseDTO = prepareLicenseDTO( entityManager, retLicenseEntity );
        } finally {
            entityManager.close();
        }
        return moduleLicenseDTO;
    }

    /**
     * Verify license feature.
     *
     * @param moduleLicenseDTO
     *         the module license DTO
     */
    private void verifyLicenseFeature( ModuleLicenseDTO moduleLicenseDTO ) {
        List< String > featureList = moduleLicenseDTO.getFeatures();
        Predicate< String > featureAllowed = feature -> featureList.get( ConstantsInteger.INTEGER_VALUE_ZERO ).equals( feature );
        Map< String, String > allowedFeatures = new HashMap<>();
        allowedFeatures.put( ConstantsLicenseType.MODULE_SIMUSPACE_DOE, SimuspaceFeaturesEnum.DOE.getKey() );
        allowedFeatures.put( ConstantsLicenseType.MODULE_SIMUSPACE_OPTIMIZATION, SimuspaceFeaturesEnum.OPTIMIZATION.getKey() );
        allowedFeatures.put( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER, SimuspaceFeaturesEnum.WORKFLOW_MANAGER.getKey() );
        allowedFeatures.put( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_USER, SimuspaceFeaturesEnum.WORKFLOW_USER.getKey() );
        allowedFeatures.put( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER_POST, SimuspaceFeaturesEnum.POST.getKey() );
        allowedFeatures.put( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER_ASSEMBLY, SimuspaceFeaturesEnum.ASSEMBLY.getKey() );

        String moduleName = moduleLicenseDTO.getModule();
        String allowedFeature = allowedFeatures.get( moduleName );

        if ( allowedFeature != null && !featureAllowed.test( allowedFeature ) ) {
            throw new FeatureNotAllowedException( MessageBundleFactory.getMessage( Messages.LICENSE_FEATURE_MISMATCH.getKey(),
                    featureList.get( ConstantsInteger.INTEGER_VALUE_ZERO ), moduleName ) );
        }

        if ( ConstantsLicenseType.MODULE_SIMUSPACE_DATA.equals( moduleName ) ) {
            ArrayList< String > dataFeatureList = new ArrayList<>(
                    Arrays.asList( SimuspaceFeaturesEnum.LICENSES.getKey(), SimuspaceFeaturesEnum.DATA.getKey(),
                            SimuspaceFeaturesEnum.ROOT.getKey(), SimuspaceFeaturesEnum.SYSTEM.getKey(),
                            SimuspaceFeaturesEnum.USERS.getKey(), SimuspaceFeaturesEnum.DIRECTORIES.getKey(),
                            SimuspaceFeaturesEnum.ROLES.getKey(), SimuspaceFeaturesEnum.GROUPS.getKey(),
                            SimuspaceFeaturesEnum.RIGHTS.getKey(), SimuspaceFeaturesEnum.AUDIT.getKey() ) );

            for ( String feature : featureList ) {
                if ( !dataFeatureList.contains( feature ) ) {
                    throw new FeatureNotAllowedException(
                            MessageBundleFactory.getMessage( Messages.LICENSE_FEATURE_MISMATCH.getKey(), feature, moduleName ) );
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModuleLicenseDTO getModuleLicense( EntityManager entityManager, String module ) {
        if ( StringUtils.isBlank( module ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MODULE_NAME_CANNOT_BE_NULL.getKey() ) );
        }
        ModuleLicenseDTO moduleLicenseDTO = prepareLicenseDTO( entityManager, licenseConfigDao.getModuleLicense( entityManager, module ) );
        if ( moduleLicenseDTO != null ) {
            verifyLicenseAndExpiry( moduleLicenseDTO );
        }
        return moduleLicenseDTO;
    }

    /**
     * Verify license and expiry.
     *
     * @param moduleLicenseDTO
     *         the module license dto
     */
    private void verifyLicenseAndExpiry( ModuleLicenseDTO moduleLicenseDTO ) {
        if ( moduleLicenseDTO == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.LICENSE_INPUT_CANNOT_BE_NULL.getKey() ) );
        }
        if ( !LicenseUtils.verifyLicenseExpiry( moduleLicenseDTO ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.LICENSE_IS_EXPIRED.getKey(), moduleLicenseDTO.getModule() ) );
        }
        if ( !LicenseUtils.verifyLicense( moduleLicenseDTO ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.LICENSE_SIGNATURE_IS_NOT_VALID.getKey(), moduleLicenseDTO.getModule() ) );
        }
    }

    /**
     * Gets module license to check is user license exist for module.
     *
     * @param entityManager
     *         the entity manager
     * @param module
     *         the module
     *
     * @return the module license to check is user license exist for module
     */
    private ModuleLicenseDTO getModuleLicenseToCheckIsUserLicenseExistForModule( EntityManager entityManager, String module ) {
        if ( StringUtils.isBlank( module ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MODULE_NAME_CANNOT_BE_NULL.getKey() ) );
        }
        ModuleLicenseDTO moduleLicenseDTO = prepareLicenseWithoutUserLimitations( entityManager,
                licenseConfigDao.getModuleLicense( entityManager, module ) );
        verifyLicenseAndExpiry( moduleLicenseDTO );
        return moduleLicenseDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< ModuleLicenseDTO > getModuleLicenseList( String userId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        FilteredResponse< ModuleLicenseDTO > moduleLicenseDTOFilteredResponse;
        try {
            UserDTO userEntity = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );
            if ( !userEntity.getId().equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                    SimuspaceFeaturesEnum.LICENSES.getId() + ConstantsString.COLON + PermissionMatrixEnum.READ.getValue() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_READ.getKey(), LICENSE ) );
            }
            isRestricted( entityManager, UUID.fromString( userId ) );
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_USER.getKey() ) );
            }

            List< LicenseEntity > licenseEntities = licenseConfigDao.getAllFilteredRecords( entityManager, LicenseEntity.class, filter );

            moduleLicenseDTOFilteredResponse = PaginationUtil.constructFilteredResponse( filter,
                    prepareLicenseDTO( entityManager, licenseEntities ) );
        } finally {
            entityManager.close();
        }
        return moduleLicenseDTOFilteredResponse;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ModuleLicenseDTO > getModulesByUserId( EntityManager entityManager, String userId ) {

        if ( userId == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_ID_CANNOT_BE_NULL.getKey() ) );
        }

        return prepareLicenseDTO( entityManager, licenseConfigDao.getModulesByUserId( entityManager, userId ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ModuleLicenseDTO > getLicenseUserEntityListByUserId( EntityManager entityManager, String userId ) {
        List< ModuleLicenseDTO > moduleLicenseDTO = new ArrayList<>();
        if ( userId == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_ID_CANNOT_BE_NULL.getKey() ) );
        }
        List< LicenseUserEntity > list = licenseUserDao.getLicenseUserEntitiesByUser( entityManager, UUID.fromString( userId ) );
        list.forEach( licenseUserEntity -> moduleLicenseDTO.add(
                prepareLicenseWithoutUserLimitations( entityManager, licenseUserEntity.getLicenseEntity() ) ) );
        return moduleLicenseDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUserValidForModule( EntityManager entityManager, String module, String userId ) {
        if ( StringUtils.isBlank( module ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MODULE_NAME_CANNOT_BE_NULL.getKey() ) );
        }
        if ( userId == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_ID_CANNOT_BE_NULL.getKey() ) );
        }
        LicenseUserEntity licenseEntity = licenseConfigDao.getLicenseByModuleAndUser( entityManager, module, userId );
        return licenseEntity != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkIfFeatureAllowedToUser( EntityManager entityManager, String feature, String userId, UUID operationUserId ) {
        if ( StringUtils.isBlank( userId ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_ID_CANNOT_BE_NULL.getKey() ) );
        } else {
            if ( !isUserLicenseExistForModule( entityManager, feature, userId, operationUserId ) ) {
                throw new FeatureNotAllowedException(
                        MessageBundleFactory.getMessage( Messages.FEATURE_NOT_ALLOWED_TO_USER.getKey(), feature ) );
            } else {
                return true;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkIfFeatureAllowedToUser( EntityManager entityManager, String feature, String userId ) {
        if ( StringUtils.isBlank( userId ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_ID_CANNOT_BE_NULL.getKey() ) );
        } else {
            if ( !isUserLicenseExistForModule( entityManager, feature, userId ) ) {
                throw new FeatureNotAllowedException(
                        MessageBundleFactory.getMessage( Messages.FEATURE_NOT_ALLOWED_TO_USER.getKey(), feature ) );
            } else {
                return true;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFeatureAllowedToUser( EntityManager entityManager, String feature, String userId ) {
        if ( StringUtils.isBlank( userId ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_ID_CANNOT_BE_NULL.getKey() ) );
        }
        return isUserLicenseExistForModule( entityManager, feature, userId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteModuleLicenseById( EntityManager entityManager, UUID id, String userId ) {

        LicenseEntity entityToDelete = licenseConfigDao.getModuleLicenseById( entityManager, id );
        if ( entityToDelete == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.LICENSE_NOT_FOUND_FOR_REQUESTED_MODULE.getKey() ) );
        }
        List< LicenseUserEntity > licenseUserEntities = licenseUserDao.getLicenseUserEntitiesByModule( entityManager, entityToDelete );
        for ( LicenseUserEntity licenseUserEntity : licenseUserEntities ) {
            licenseUserDao.delete( entityManager, licenseUserEntity );
        }
        // Audit log entry
        UserEntity user = new UserEntity();
        user.setId( UUID.fromString( userId ) );
        AuditLogEntity auditLog = new AuditLogEntity( UUID.randomUUID(), new Date(), user, entityToDelete.getModule(),
                ConstantsDbOperationTypes.DELETED, "License Module:" + entityToDelete.getModule() + " " + ConstantsDbOperationTypes.DELETED,
                ConstantsDbOperationTypes.DELETED );

        auditLogDAO.save( entityManager, auditLog );
        licenseConfigDao.deleteModuleLicense( entityManager, entityToDelete );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteModuleLicenseBySelection( String userId, String selectionId, String mode ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UserDTO userEntity = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );
            if ( !userEntity.getId().equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                    SimuspaceFeaturesEnum.LICENSES.getId() + ConstantsString.COLON + PermissionMatrixEnum.DELETE.getValue() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_DELETE.getKey(), LICENSE ) );
            }
            isRestricted( entityManager, UUID.fromString( userId ) );

            if ( mode.contentEquals( MODE_BULK ) ) {
                FiltersDTO filtersDTO = contextMenuManager.getFilterBySelectionId( entityManager, selectionId );
                List< Object > ids = filtersDTO.getItems();
                for ( Object id : ids ) {
                    deleteModuleLicenseById( entityManager, UUID.fromString( id.toString() ), userId );
                }
            } else if ( mode.contentEquals( MODE_SINGLE ) ) {
                deleteModuleLicenseById( entityManager, UUID.fromString( selectionId ), userId );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.PROVIDED_MODE_IS_NOT_SUPPORTED.getKey(), mode ) );
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepares license entity.
     *
     * @param license
     *         the license
     *
     * @return the license entity
     */
    private LicenseEntity prepareLicenseEntity( ModuleLicenseDTO license ) {
        LicenseEntity licenseEntity = new LicenseEntity( license.getCustomer(), license.getVendor(), license.getReseller(),
                license.getType(), license.getModule(), license.getUserLimit().getAllowedUsers(),
                license.getUserLimit().getRestrictedUsers(), JsonUtils.convertMapToStringGernericValue( license.getAddons() ),
                license.getLicenseType(), DateFormatStandard.toDate( license.getExpiryTime() ), license.getMacAddress(),
                license.getKeyInformation() );
        licenseEntity.setFeatures( prepareLicenseFeatureEntitySet( license.getFeatures(), licenseEntity ) );
        licenseEntity.setId( UUID.randomUUID() );
        return licenseEntity;
    }

    /**
     * Prepares license entity.
     *
     * @param license
     *         the license
     * @param existingLicenseEntity
     *         the existing license entity
     *
     * @return the license entity
     */
    private LicenseEntity updateLicenseEntity( ModuleLicenseDTO license, LicenseEntity existingLicenseEntity ) {
        existingLicenseEntity.setCustomer( license.getCustomer() );
        existingLicenseEntity.setVendor( license.getVendor() );
        existingLicenseEntity.setReseller( license.getReseller() );
        existingLicenseEntity.setType( license.getType() );
        existingLicenseEntity.setModule( license.getModule() );
        existingLicenseEntity.setAllowedUsers( license.getUserLimit().getAllowedUsers() );
        existingLicenseEntity.setRestrictedUsers( license.getUserLimit().getRestrictedUsers() );
        existingLicenseEntity.setAddons( JsonUtils.convertMapToStringGernericValue( license.getAddons() ) );
        existingLicenseEntity.setLicenseType( license.getLicenseType() );
        existingLicenseEntity.setExpiryTime( DateFormatStandard.toDate( license.getExpiryTime() ) );
        existingLicenseEntity.setMacAddress( license.getMacAddress() );
        existingLicenseEntity.setKeyInformation( license.getKeyInformation() );
        existingLicenseEntity.setFeatures( prepareLicenseFeatureEntitySet( license.getFeatures(), existingLicenseEntity ) );
        return existingLicenseEntity;
    }

    /**
     * Prepare license feature entity set.
     *
     * @param features
     *         the features
     * @param licenseEntity
     *         the license entity
     *
     * @return the sets the
     */
    private Set< LicenseFeatureEntity > prepareLicenseFeatureEntitySet( List< String > features, LicenseEntity licenseEntity ) {
        Set< LicenseFeatureEntity > featureEntities = new HashSet<>();

        for ( String licenseFeature : features ) {
            if ( StringUtils.isNotBlank( licenseFeature ) ) {
                LicenseFeatureEntity featureEntity = new LicenseFeatureEntity( licenseFeature );
                featureEntity.setLicenseEntity( licenseEntity );
                featureEntities.add( featureEntity );
            }
        }

        return featureEntities;
    }

    /**
     * Prepares license DTO.
     *
     * @param entityManager
     *         the entity manager
     * @param licenseEntity
     *         the license entity
     *
     * @return the license DTO
     */
    private ModuleLicenseDTO prepareLicenseDTO( EntityManager entityManager, LicenseEntity licenseEntity ) {

        updateTokenizedLicenseEntryMap( entityManager );
        if ( licenseEntity == null ) {
            return null;
        }

        UserLimitDTO userLimit = new UserLimitDTO( licenseEntity.getAllowedUsers(), licenseEntity.getRestrictedUsers() );

        int consumedAllowedUserLimit = 0;
        int consumedRestrictedUserLimit = 0;
        if ( licenseEntity.getLicenseType().equals( ConstantsLicenseType.LICENSE_TYPE_NAMED ) ) {
            List< LicenseUserEntity > licenseUserEntities = licenseUserDao.getLicenseUserEntitiesByModule( entityManager, licenseEntity );
            for ( LicenseUserEntity licenseUserEntity : licenseUserEntities ) {
                if ( Boolean.TRUE.equals( licenseUserEntity.getUserEntity().isRestricted() ) ) {
                    consumedRestrictedUserLimit++;
                } else if ( Boolean.FALSE.equals( licenseUserEntity.getUserEntity().isRestricted() ) ) {
                    consumedAllowedUserLimit++;
                }
            }
        } else if ( licenseEntity.getLicenseType().equals( ConstantsLicenseType.LICENSE_TYPE_TOKEN ) ) {
            for ( Entry< String, SuSCoreSessionDTO > tokenizedLicenseEntry : TokenizedLicenseUtil.getMap().entrySet() ) {
                List< LicenseUserEntity > licenseUserEntities = licenseUserDao.getLicenseUserEntitiesByUser( entityManager,
                        UUID.fromString( tokenizedLicenseEntry.getValue().getUser().getId() ) );
                List< String > moduleNames = new ArrayList<>();
                for ( LicenseUserEntity licenseUserEntity : licenseUserEntities ) {
                    moduleNames.add( licenseUserEntity.getLicenseEntity().getModule() );
                }
                if ( moduleNames.contains( licenseEntity.getModule() ) ) {
                    consumedAllowedUserLimit++;
                }
                if ( tokenizedLicenseEntry.getValue().getUser().getRestricted().equalsIgnoreCase( ConstantsStatus.YES ) ) {
                    consumedRestrictedUserLimit++;
                }
            }
        }

        UserLimitDTO consumedUsers = new UserLimitDTO( consumedAllowedUserLimit, consumedRestrictedUserLimit );
        UserLimitDTO availableUsers = new UserLimitDTO( licenseEntity.getAllowedUsers() - consumedAllowedUserLimit,
                licenseEntity.getRestrictedUsers() - consumedRestrictedUserLimit );
        Set< String > features = new HashSet<>();
        List< LicenseFeatureEntity > licenseFeatureEntities = licenseConfigDao.getLicenseFeaturesByLicense( entityManager, licenseEntity );
        for ( LicenseFeatureEntity featureEntity : licenseFeatureEntities ) {
            features.add( featureEntity.getFeature() );
        }
        ModuleLicenseDTO licenseDTO = new ModuleLicenseDTO( licenseEntity.getCustomer(), licenseEntity.getVendor(),
                licenseEntity.getReseller(), licenseEntity.getType(), licenseEntity.getModule(), userLimit,
                JsonUtils.convertStringToMapGenericValue( licenseEntity.getAddons() ), features, licenseEntity.getLicenseType(),
                DateFormatStandard.format( licenseEntity.getExpiryTime() ), licenseEntity.getMacAddress(),
                licenseEntity.getKeyInformation() );
        if ( licenseEntity.getId() != null ) {
            licenseDTO.setId( licenseEntity.getId().toString() );
        }

        licenseDTO.setConsumedUsers( consumedUsers );
        licenseDTO.setAvailableUsers( availableUsers );
        licenseDTO.setAllowedUsers( licenseEntity.getAllowedUsers() );
        licenseDTO.setRestrictedUsers( licenseEntity.getRestrictedUsers() );
        licenseDTO.setStrFeatList( null );
        return licenseDTO;
    }

    /**
     * Update tokenized license entry map.
     *
     * @param entityManager
     *         the entity manager
     */
    private void updateTokenizedLicenseEntryMap( EntityManager entityManager ) {

        List< UserTokenEntity > userTokens = tokenManager.getUserTokenDAO().getAllActiveOrRunningJobTokens( entityManager );
        Map< String, SuSCoreSessionDTO > newMap = new HashMap<>();
        userTokens.forEach( userToken -> {
            var sessionDTO = tokenManager.getSessionDTOByAuthToken( entityManager, userToken.getToken() );
            if ( sessionDTO != null ) {
                newMap.put( userToken.getToken(), sessionDTO );
            }
        } );
        licenseManager.updateTokenizedLicenseMap( newMap );

    }

    /**
     * Prepare license without user limitations module license dto.
     *
     * @param entityManager
     *         the entity manager
     * @param licenseEntity
     *         the license entity
     *
     * @return the module license dto
     */
    private ModuleLicenseDTO prepareLicenseWithoutUserLimitations( EntityManager entityManager, LicenseEntity licenseEntity ) {
        if ( licenseEntity == null ) {
            return null;
        }
        UserLimitDTO userLimit = new UserLimitDTO( licenseEntity.getAllowedUsers(), licenseEntity.getRestrictedUsers() );
        Set< String > features = new HashSet<>();
        List< LicenseFeatureEntity > licenseFeatureEntities = licenseConfigDao.getLicenseFeaturesByLicense( entityManager, licenseEntity );
        licenseFeatureEntities.forEach( featureEntity -> features.add( featureEntity.getFeature() ) );
        ModuleLicenseDTO licenseDTO = new ModuleLicenseDTO( licenseEntity.getCustomer(), licenseEntity.getVendor(),
                licenseEntity.getReseller(), licenseEntity.getType(), licenseEntity.getModule(), userLimit,
                JsonUtils.convertStringToMapGenericValue( licenseEntity.getAddons() ), features, licenseEntity.getLicenseType(),
                DateFormatStandard.format( licenseEntity.getExpiryTime() ), licenseEntity.getMacAddress(),
                licenseEntity.getKeyInformation() );
        if ( licenseEntity.getId() != null ) {
            licenseDTO.setId( licenseEntity.getId().toString() );
        }
        licenseDTO.setAllowedUsers( licenseEntity.getAllowedUsers() );
        licenseDTO.setRestrictedUsers( licenseEntity.getRestrictedUsers() );
        licenseDTO.setStrFeatList( null );
        return licenseDTO;
    }

    /**
     * Prepare license DTO.
     *
     * @param licenseEntities
     *         the license entities
     *
     * @return the list
     */
    private List< ModuleLicenseDTO > prepareLicenseDTO( EntityManager entityManager, List< LicenseEntity > licenseEntities ) {
        List< ModuleLicenseDTO > licenseDTOs = new ArrayList<>();
        if ( licenseEntities != null ) {

            for ( LicenseEntity licenseEntity : licenseEntities ) {
                licenseDTOs.add( prepareLicenseDTO( entityManager, licenseEntity ) );
            }
        }
        return licenseDTOs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkIsFeatureAllowedToUser( String feature ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return isFeatureAllowedToUser( entityManager, feature, getUserIdFromMessage() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI getLiceseUi( String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        TableUI tableUI;
        try {
            tableUI = new TableUI( GUIUtils.listColumns( ModuleLicenseDTO.class ),
                    objectViewManager.getUserObjectViewsByKey( entityManager, ConstantsObjectViewKey.LICENSE_TABLE_KEY, userId, null ) );
        } finally {
            entityManager.close();
        }
        return tableUI;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm createLicenseForm( String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< UIFormItem > uiFormItems;
        try {
            UserDTO userEntity = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );
            if ( !userEntity.getId().equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                    SimuspaceFeaturesEnum.LICENSES.getId() + ConstantsString.COLON + PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_CREATE.getKey(), LICENSE ) );
            }
            isRestricted( entityManager, UUID.fromString( userId ) );
            uiFormItems = GUIUtils.prepareForm( true, new LicenseForm() );
        } finally {
            entityManager.close();
        }
        return GUIUtils.createFormFromItems( uiFormItems );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm createLicenseFormForEdit( String userId, UUID id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< UIFormItem > uiFormItems;
        try {
            LicenseEntity entityToEdit = licenseConfigDao.getModuleLicenseById( entityManager, id );
            UserDTO userEntity = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );
            if ( !userEntity.getId().equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                    SimuspaceFeaturesEnum.LICENSES.getId() + ConstantsString.COLON + PermissionMatrixEnum.WRITE.getValue() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_UPDATE.getKey(), LICENSE ) );
            }
            isRestricted( entityManager, UUID.fromString( userId ) );
            if ( entityToEdit == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.LICENSE_NOT_FOUND_FOR_REQUESTED_MODULE.getKey() ) );
            }

            ModuleLicenseDTO moduleLicenseDTO = getModuleLicense( entityManager, entityToEdit.getModule() );

            LicenseForm licenseForm = new LicenseForm( JsonUtils.toJson( moduleLicenseDTO ) );
            uiFormItems = GUIUtils.prepareForm( false, licenseForm );
        } finally {
            entityManager.close();
        }

        return GUIUtils.createFormFromItems( uiFormItems );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ModuleLicenseDTO > getModuleLicenseList() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< ModuleLicenseDTO > moduleLicenseDTOS;
        try {
            moduleLicenseDTOS = getModuleLicenseList( entityManager );
        } finally {
            entityManager.close();
        }
        return moduleLicenseDTOS;
    }

    /**
     * {@inheritDoc}
     */
    private List< ModuleLicenseDTO > getModuleLicenseList( EntityManager entityManager ) {
        return prepareLicenseDTO( entityManager, licenseConfigDao.getModuleLicenseList( entityManager ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< LicenseEntity > getModuleLicenseEntityList() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return licenseConfigDao.getModuleLicenseList( entityManager );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getContextMenu( FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // case of select all from data table
        List< ContextMenuItem > contextMenu;
        try {
            if ( filter.getItems().isEmpty() && filter.getLength().toString().equalsIgnoreCase( "-1" ) ) {

                Long maxResults = licenseConfigDao.getAllFilteredRecordCountWithParentId( entityManager, LicenseEntity.class, filter,
                        UUID.fromString( SimuspaceFeaturesEnum.LICENSES.getId() ) );
                filter.setLength( Integer.valueOf( maxResults.toString() ) );
                List< LicenseEntity > allObjectsList = licenseConfigDao.getAllFilteredRecordsWithParent( entityManager, LicenseEntity.class,
                        filter, UUID.fromString( SimuspaceFeaturesEnum.LICENSES.getId() ) );
                List< Object > itemsList = new ArrayList<>();
                allObjectsList.forEach( entity -> itemsList.add( entity.getId() ) );

                filter.setItems( itemsList );
            }

            contextMenu = contextMenuManager.getContextMenu( entityManager, LICENSE_PLUGIN_NAME, ModuleLicenseDTO.class, filter );
        } finally {
            entityManager.close();
        }
        return contextMenu;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > manageLicenseTableUI( String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        UserDTO userEntity = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );
        if ( !userEntity.getId().equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                SimuspaceFeaturesEnum.LICENSES.getId() + ConstantsString.COLON + PermissionMatrixEnum.READ.getValue() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_READ.getKey(), LICENSE ) );
        }
        isRestricted( entityManager, UUID.fromString( userId ) );
        Field[] attributes = LicenseManageForm.class.getDeclaredFields();

        List< TableColumn > columnsList = new ArrayList<>();
        for ( Field field : attributes ) {
            log.info( field );
            if ( java.lang.reflect.Modifier.isStatic( field.getModifiers() ) ) {
                continue;
            }
            UIColumn annot = field.getAnnotation( UIColumn.class );

            if ( annot.name().contentEquals( USER_TYPE_COLUMN_NAME ) || annot.name().contentEquals( FIELD_USER_NAME ) ) {
                columnsList.add( GUIUtils.prepareTableColumn( annot ) );
            } else {
                List< ModuleLicenseDTO > moduleLicenseList = getModuleLicenseList( entityManager );
                prepareColumnForModules( columnsList, annot, moduleLicenseList );
            }

        }

        return columnsList;

    }

    private void prepareColumnForModules( List< TableColumn > columnsList, UIColumn annot, List< ModuleLicenseDTO > licenseDTOs ) {
        for ( int index = 0; index < licenseDTOs.size(); index++ ) {

            ModuleLicenseDTO moduleLicenseDTO = licenseDTOs.get( index );

            TableColumn moduleColumn = new TableColumn();
            moduleColumn.setData( annot.data().replace( LicenseManageForm.INDEX_PATTERN, ConstantsString.EMPTY_STRING + index ) );
            moduleColumn.setName( annot.name().replace( LicenseManageForm.MODULE_NAME_PATTERN, moduleLicenseDTO.getModule() ) );
            moduleColumn.setTitle( moduleLicenseDTO.getModule() );
            moduleColumn.setSortable( false );
            Renderer renderer = new Renderer();
            renderer.setType( annot.renderer() );

            renderer.setManage( Boolean.valueOf( annot.manage() ) );
            moduleColumn.setRenderer( renderer );

            columnsList.add( moduleColumn );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< UserLicensesDTO > getAllUserLicenses( FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        FilteredResponse< UserLicensesDTO > userLicensesDTOFilteredResponse;
        try {
            if ( !checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.LICENSES.getKey(), getUserIdFromMessage() ) ) {
                throw new FeatureNotAllowedException( MessageBundleFactory.getMessage( Messages.FEATURE_NOT_ALLOWED_TO_USER.getKey(),
                        SimuspaceFeaturesEnum.LICENSES.getKey() ) );
            }

            List< UserLicensesDTO > allUserLicenses = new ArrayList<>();
            List< LicenseEntity > licenseEntities = licenseConfigDao.getModuleLicenseList( entityManager );
            List< Filter > filters = new ArrayList<>();
            if ( CollectionUtils.isNotEmpty( filter.getColumns() ) ) {
                for ( FilterColumn filterColumn : filter.getColumns() ) {
                    if ( FIELD_USER_NAME.equals( filterColumn.getName() ) ) {
                        filters = filterColumn.getFilters();
                    }
                }
            }
            List< FilterColumn > userColumnList = new ArrayList<>();
            FilterColumn filterColumn = new FilterColumn();
            filterColumn.setFilters( filters );
            filterColumn.setName( UserDTO.UID_UI_COLUMN_NAME );
            if ( CollectionUtils.isNotEmpty( filter.getColumns() ) ) {
                for ( FilterColumn filt : filter.getColumns() ) {
                    if ( FIELD_USER_NAME.equals( filt.getName() ) ) {
                        filterColumn.setDir( filt.getDir() );
                    }
                }
            }
            userColumnList.add( filterColumn );
            filter.setColumns( userColumnList );
            FilteredResponse< UserDTO > userResponse = userCommonManager.getFilteredUsers( entityManager, filter );

            Map< String, Set< UUID > > moduleUsers = getUsersForAllModules( entityManager, licenseEntities );

            for ( UserDTO user : userResponse.getData() ) {

                UserLicensesDTO userLicenses = new UserLicensesDTO();
                if ( user.getRestricted().contentEquals( ConstantsStatus.YES ) ) {
                    user.setType( UserDTO.USER_TYPE_RESTRICTED );
                } else {
                    user.setType( UserDTO.USER_TYPE_FULL_RIGHTS );
                }
                userLicenses.setUser( user );

                List< CheckBox > modules = new ArrayList<>();
                for ( LicenseEntity licenseEntity : licenseEntities ) {
                    CheckBox checkBox = new CheckBox();
                    checkBox.setId( licenseEntity.getId() );
                    checkBox.setName( licenseEntity.getModule() );
                    Set< UUID > uuids = moduleUsers.get( licenseEntity.getModule() );
                    if ( uuids.contains( UUID.fromString( user.getId() ) ) ) {
                        checkBox.setValue( 1 );
                    }
                    modules.add( checkBox );
                }
                userLicenses.setModules( modules );
                allUserLicenses.add( userLicenses );
            }

            userLicensesDTOFilteredResponse = PaginationUtil.constructFilteredResponse( filter, allUserLicenses );
        } finally {
            entityManager.close();
        }
        return userLicensesDTOFilteredResponse;

    }

    /**
     * Get users for all modules.
     *
     * @param entityManager
     *         the entity manager
     * @param licenseEntities
     *         the license Entities
     *
     * @return the moduleUsers
     */
    private Map< String, Set< UUID > > getUsersForAllModules( EntityManager entityManager, List< LicenseEntity > licenseEntities ) {
        Map< String, Set< UUID > > moduleUsers = new HashMap<>();
        for ( LicenseEntity licenseEntity : licenseEntities ) {
            Set< UUID > users = licenseUserDao.getUserIDsAttachedToModule( entityManager, licenseEntity.getModule() );
            moduleUsers.put( licenseEntity.getModule(), users );
        }
        return moduleUsers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void manageUserLicense( String licenseAssignedBy, UUID licenseAssignedTo, CheckBox checkBox ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UserDTO userEntity = userCommonManager.getUserById( entityManager, UUID.fromString( licenseAssignedBy ) );
            if ( !userEntity.getId().equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager,
                    licenseAssignedBy,
                    SimuspaceFeaturesEnum.LICENSES.getId() + ConstantsString.COLON + PermissionMatrixEnum.WRITE.getValue() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_WRITE.getKey(), LICENSE ) );
            }

            checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.LICENSES.getKey(), getUserIdFromMessage(),
                    licenseAssignedTo );

            validateInputs( entityManager, licenseAssignedTo, checkBox );

            if ( checkBox.getName().startsWith( MODULE_COLUMN_NAME_PREFIX ) ) {
                updateUserModule( entityManager, licenseAssignedTo,
                        checkBox.getName().replace( MODULE_COLUMN_NAME_PREFIX, ConstantsString.EMPTY_STRING ),
                        checkBox.getValue() == ConstantsInteger.INTEGER_VALUE_ONE );

            } else if ( checkBox.getName().contentEquals( USER_TYPE_COLUMN_NAME ) ) {
                updateUserType( entityManager, licenseAssignedTo, checkBox.getValue() == ConstantsInteger.INTEGER_VALUE_ONE );
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Update default view for user data tree.
     *
     * @param id
     *         the id
     */
    @Override
    public void updateDefaultViewForUserDataTree( String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // Save Default views for Different licenses combinations
            if ( !ConstantsID.SUPER_USER_ID.equals( id ) ) {

                boolean dataLicenseConsume = false;
                boolean wfUserLicenseConsume = false;
                boolean AssemblyLicenseConsume = false;
                boolean wfManagerLicenseConsume = false;
                boolean postLicenseConsume = false;
                boolean restrictedLicense = false;

                List< ModuleLicenseDTO > modulesListOfSingleUser = getLicenseUserEntityListByUserId( entityManager, id );
                for ( ModuleLicenseDTO modulesOfSingleUser : modulesListOfSingleUser ) {
                    if ( modulesOfSingleUser.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_DATA ) ) {
                        dataLicenseConsume = true;
                    } else if ( modulesOfSingleUser.getModule()
                            .equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER ) ) {
                        wfManagerLicenseConsume = true;
                    } else if ( modulesOfSingleUser.getModule()
                            .equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER_ASSEMBLY ) ) {
                        AssemblyLicenseConsume = true;
                    } else if ( modulesOfSingleUser.getModule()
                            .equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER_POST ) ) {
                        postLicenseConsume = true;
                    } else if ( modulesOfSingleUser.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_USER ) ) {
                        wfUserLicenseConsume = true;
                    }
                }

                UserDTO userDTO = userCommonManager.getUserById( entityManager, UUID.fromString( id ) );
                int systemGenratedViewEnum = 10;
                if ( "Yes".equalsIgnoreCase( userDTO.getRestricted() ) ) {
                    if ( dataLicenseConsume && wfUserLicenseConsume && !wfManagerLicenseConsume ) {
                        // NODES: Workflows & Search & Jobs & Deleted Objects & Data - (Read only) &
                        // Configuration
                        systemGenratedViewEnum = SystemGenratedViewEnum.RESTRICTED_DATAUSER_WFUSER.getKey();
                    }
                    if ( dataLicenseConsume && !wfUserLicenseConsume && !wfManagerLicenseConsume ) {
                        // NODES: search & data (read only)
                        systemGenratedViewEnum = SystemGenratedViewEnum.RESTRICTED_DATAUSER.getKey();
                    }
                } else {
                    if ( dataLicenseConsume && wfUserLicenseConsume && wfManagerLicenseConsume ) {
                        // NODES: Workflows create/edit/use System & Search Jobs & Deleted Objects &
                        // Data & Configuration
                        systemGenratedViewEnum = SystemGenratedViewEnum.DATAUSER_WFUSER_WFMANAGER.getKey();
                    }
                    if ( dataLicenseConsume && !wfUserLicenseConsume && !wfManagerLicenseConsume ) {
                        // NODES: System & Search & Jobs & Deleted Objects & Data
                        systemGenratedViewEnum = SystemGenratedViewEnum.DATAUSER.getKey();
                    }
                    if ( dataLicenseConsume && wfUserLicenseConsume && !wfManagerLicenseConsume ) {
                        // NODES: Workflows (run only) & System & Search & Jobs & Deleted Objects & Data
                        // & Configuration
                        systemGenratedViewEnum = SystemGenratedViewEnum.DATAUSER_WFUSER.getKey();
                    }
                }

                if ( !objectViewManager.updateSystemGeneratedViewIfExists( entityManager, id, systemGenratedViewEnum ) ) {
                    objectViewManager.prepareAndSaveObjectViewDTOForSystemGenratedViews( entityManager, userDTO, systemGenratedViewEnum );
                }
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Validates user license limits and then updates user type.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param isRestricted
     *         the is restricted
     */
    private void updateUserType( EntityManager entityManager, UUID userId, boolean isRestricted ) {

        List< LicenseEntity > licenseEntities = licenseConfigDao.getModuleLicenseList( entityManager );
        for ( LicenseEntity licenseEntity : licenseEntities ) {
            int consumedAllowedUserLimit = 0;
            int consumedRestrictedUserLimit = 0;
            List< LicenseUserEntity > licenseUserEntities = licenseUserDao.getLicenseUserEntitiesByModule( entityManager, licenseEntity );
            for ( LicenseUserEntity licenseUserEntity : licenseUserEntities ) {
                if ( Boolean.TRUE.equals( licenseUserEntity.getUserEntity().isRestricted() ) ) {
                    consumedRestrictedUserLimit++;
                } else if ( Boolean.FALSE.equals( licenseUserEntity.getUserEntity().isRestricted() ) ) {
                    consumedAllowedUserLimit++;
                }
            }

            if ( isRestricted && licenseEntity.getRestrictedUsers() <= consumedRestrictedUserLimit ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.RESTRICTED_USERS_EXCEEDS_FROM_AVAILABLE.getKey(),
                        licenseEntity.getModule() ) );
            }
        }

        userCommonManager.updateUserType( entityManager, userId, isRestricted );
    }

    /**
     * Update user module.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param module
     *         the module
     * @param isChecked
     *         the is checked
     *
     * @return the license user entity
     */
    private LicenseUserEntity updateUserModule( EntityManager entityManager, UUID userId, String module, boolean isChecked ) {
        getModuleLicense( entityManager, module );
        LicenseUserEntity licenseEntityProcessed = null;
        if ( isChecked ) {
            LicenseEntity licenseEntity = licenseConfigDao.getModuleLicense( entityManager, module );

            List< LicenseUserEntity > licenseUserEntities = licenseUserDao.getLicenseUserEntitiesByModule( entityManager, licenseEntity );
            for ( LicenseUserEntity licenseUserEntity : licenseUserEntities ) {
                if ( licenseUserEntity.getUserEntity().getId().equals( userId ) ) {
                    // break and return
                    return licenseEntityProcessed;
                }
            }
            validateLimits( entityManager, userId, licenseEntity, licenseUserEntities );

            LicenseUserEntity userToAttach = new LicenseUserEntity();
            userToAttach.setId( UUID.randomUUID() );
            userToAttach.setLicenseEntity( licenseEntity );
            UserEntity userEntity = new UserEntity();
            userEntity.setId( userId );
            userToAttach.setUserEntity( userEntity );
            licenseEntityProcessed = licenseUserDao.addUserToModule( entityManager, userToAttach );
        } else {
            LicenseUserEntity existingLicenseUserEntity = licenseUserDao.getLicenseUserEntityByModuleAndUser( entityManager, userId,
                    module );
            if ( existingLicenseUserEntity != null ) {
                licenseUserDao.delete( entityManager, existingLicenseUserEntity );
                licenseEntityProcessed = existingLicenseUserEntity;
            }
        }
        return licenseEntityProcessed;
    }

    /**
     * Validates allowed and restricted users limits.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param licenseEntity
     *         the license entity
     * @param licenseUserEntities
     *         the license user entities
     */
    private void validateLimits( EntityManager entityManager, UUID userId, LicenseEntity licenseEntity,
            List< LicenseUserEntity > licenseUserEntities ) {

        if ( userCommonManager.isUserRestricted( entityManager, userId ) ) {
            validateRestrictedUserCount( licenseEntity, licenseUserEntities );
        } else {
            // skip user limit if license is token based
            if ( licenseEntity.getLicenseType() != null && licenseEntity.getLicenseType().equalsIgnoreCase( LICENSE_TYPE_NAMED ) ) {
                int consumedAllowedUserLimit = 0;
                for ( LicenseUserEntity licenseUserEntity : licenseUserEntities ) {
                    if ( Boolean.FALSE.equals( licenseUserEntity.getUserEntity().isRestricted() ) ) {
                        consumedAllowedUserLimit++;
                    }
                }
                if ( licenseEntity.getAllowedUsers() < consumedAllowedUserLimit + 1 ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.ALLOWED_USERS_EXCEEDS_FROM_AVAILABLE.getKey(),
                            licenseEntity.getModule() ) );
                }
            }
        }
    }

    /**
     * Validate restricted user count.
     *
     * @param licenseEntity
     *         the license entity
     * @param licenseUserEntities
     *         the license user entities
     */
    private void validateRestrictedUserCount( LicenseEntity licenseEntity, List< LicenseUserEntity > licenseUserEntities ) {
        int consumedRestrictedUserLimit = 0;
        for ( LicenseUserEntity licenseUserEntity : licenseUserEntities ) {
            if ( Boolean.TRUE.equals( licenseUserEntity.getUserEntity().isRestricted() ) ) {
                consumedRestrictedUserLimit++;
            }
        }

        if ( licenseEntity.getRestrictedUsers() < consumedRestrictedUserLimit + 1 ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.RESTRICTED_USERS_EXCEEDS_FROM_AVAILABLE.getKey(),
                    licenseEntity.getModule() ) );
        }
    }

    /**
     * Validate inputs.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param checkBox
     *         the check box
     */
    private void validateInputs( EntityManager entityManager, UUID userId, CheckBox checkBox ) {
        if ( userId == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_ID_CANNOT_BE_NULL.getKey() ) );
        }

        if ( checkBox == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.CHECK_BOX_STATE_SHOULD_NOT_BE_NULL.getKey() ) );
        }

        if ( checkBox.getName() == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.CHECK_BOX_COLUMN_NAME_SHOULD_NOT_BE_NULL.getKey() ) );
        }

        // workflow-user license should be assigned before you can assign
        // workflow-manager license
        if ( checkBox.getName().equals( MODULE_COLUMN_NAME_PREFIX + ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER )
                && checkBox.getValue() == 1 && !isUserValidForModule( entityManager, ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_USER,
                userId.toString() ) ) {
            throw new SusException( "Cannot assign manager license before user license" );
        }

        // workflow-manager license should be unassigned before you can unassign
        // workflow-user license
        if ( checkBox.getName().equals( MODULE_COLUMN_NAME_PREFIX + ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_USER )
                && checkBox.getValue() == 0 && isUserValidForModule( entityManager, ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER,
                userId.toString() ) ) {
            throw new SusException( "Cannot unassign user license before manager license" );
        }
        if ( checkBox.getName().equals( USER_TYPE_COLUMN_NAME ) && checkBox.getValue() == ConstantsInteger.INTEGER_VALUE_ZERO ) {
            validateUnrestrictedUserCount( entityManager, userId );
        }
    }

    /**
     * validate Unrestricted User Count
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the userId
     */
    private void validateUnrestrictedUserCount( EntityManager entityManager, UUID userId ) {
        List< LicenseUserEntity > licenseUserEntitiesByUser = licenseUserDao.getLicenseUserEntitiesByUser( entityManager, userId );
        licenseUserEntitiesByUser.stream().filter(
                        licenseUserEntity -> ( licenseUserEntity.getLicenseEntity().getLicenseType().equalsIgnoreCase( LICENSE_TYPE_NAMED ) ) )
                .forEach( licenseUserEntity -> validateUnrestrictedConsumedLimitForNamedLicense( entityManager, licenseUserEntity ) );

    }

    /**
     * validate Unrestricted Consumed Limit For Named License
     *
     * @param licenseUserEntity
     *         the licenseUserEntity
     */
    private void validateUnrestrictedConsumedLimitForNamedLicense( EntityManager entityManager, LicenseUserEntity licenseUserEntity ) {
        List< LicenseUserEntity > licenseUserEntities = licenseUserDao.getLicenseUserEntitiesByModule( entityManager,
                licenseUserEntity.getLicenseEntity() );
        if ( licenseUserEntities.stream().filter( licenseUserEntityByModule -> !licenseUserEntityByModule.getUserEntity().isRestricted() )
                .count() >= licenseUserEntity.getLicenseEntity().getAllowedUsers() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.ALLOWED_USERS_EXCEEDS_FROM_AVAILABLE.getKey(),
                    licenseUserEntity.getLicenseEntity().getModule() ) );
        }
    }

    /**
     * Is user license exist for module boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param feature
     *         the feature
     * @param userId
     *         the user id
     *
     * @return the boolean
     */
    private boolean isUserLicenseExistForModule( EntityManager entityManager, String feature, String userId ) {
        if ( userId.equals( ConstantsString.STRING_VALUE_ZERO ) || userId.equals( ConstantsString.MINUS_ONE ) ) {
            return true;
        }
        if ( userId.equals( ConstantsString.SIMUSPACE ) || userId.equals( ConstantsID.SUPER_USER_ID ) ) {
            return true;
        }
        if ( StringUtils.isBlank( feature ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.FEATURE_NAME_SHOULD_NOT_BE_NULL_OR_EMPTY.getKey() ) );
        }
        LicenseFeatureEntity featureEntity = licenseConfigDao.getLicenseFeature( entityManager, feature );
        if ( featureEntity != null ) {
            getModuleLicenseToCheckIsUserLicenseExistForModule( entityManager, featureEntity.getLicenseEntity().getModule() );
            LicenseUserEntity licenseUserEntityByModuleAndUser = licenseUserDao.getLicenseUserEntityByModuleAndUser( entityManager,
                    UUID.fromString( userId ), featureEntity.getLicenseEntity().getModule() );
            return licenseUserEntityByModuleAndUser != null;
        } else {
            return false;
        }
    }

    /**
     * Checks if is user license exist for module.
     *
     * @param entityManager
     *         the entity manager
     * @param feature
     *         the feature
     * @param userId
     *         the user id
     * @param operationUserId
     *         the operation user id
     *
     * @return true, if is user license exist for module
     */
    private boolean isUserLicenseExistForModule( EntityManager entityManager, String feature, String userId, UUID operationUserId ) {
        if ( userId.equals( ConstantsString.STRING_VALUE_ZERO ) || userId.equals( ConstantsString.MINUS_ONE ) ) {
            return true;
        }
        if ( StringUtils.isBlank( feature ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.FEATURE_NAME_SHOULD_NOT_BE_NULL_OR_EMPTY.getKey() ) );
        }
        LicenseFeatureEntity featureEntity = licenseConfigDao.getLicenseFeature( entityManager, feature );
        if ( featureEntity != null ) {
            getModuleLicense( entityManager, featureEntity.getLicenseEntity().getModule() );
            // perform token checks if Token license is availible
            if ( isTokenBasedLicenseExists( entityManager ) ) {
                Map< String, SuSCoreSessionDTO > activeTokenList = TokenizedLicenseUtil.getMap();
                for ( Entry< String, SuSCoreSessionDTO > sessionToken : activeTokenList.entrySet() ) {
                    // if user is active then throw Exception on slot selection if slot limit is
                    // exceeding the allowed user of License
                    // if user is not Active then License Assignment is allowed
                    if ( operationUserId.equals( UUID.fromString( sessionToken.getValue().getUser().getId() ) ) ) {
                        throw new SusException( "Can Not Assign/unAssign License while User is Active" );
                    }
                }
                return true;
            } else {
                if ( userId.equals( ConstantsString.SIMUSPACE ) || userId.equals( ConstantsID.SUPER_USER_ID ) ) {
                    return true;
                }
                LicenseUserEntity licenseUserEntityByModuleAndUser = licenseUserDao.getLicenseUserEntityByModuleAndUser( entityManager,
                        UUID.fromString( userId ), featureEntity.getLicenseEntity().getModule() );
                return licenseUserEntityByModuleAndUser != null;
            }
        } else {
            return false;
        }
    }

    /**
     * Gets the user from id.
     *
     * @param userId
     *         the user id
     *
     * @return the user from id
     */
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.license.manager.
     * LicenseConfigurationManager#getUserFromId(java.lang.String)
     */
    @Override
    public UserDTO getUserFromId( String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        UserDTO userDTO;
        try {
            userDTO = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );
        } finally {
            entityManager.close();
        }
        return userDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAllowedLocationsToUser( EntityManager entityManager, String userId ) {
        int allowedLocations = 0;
        List< LicenseUserEntity > licenseUserEntities = licenseUserDao.getLicenseUserEntitiesByUser( entityManager,
                UUID.fromString( userId ) );
        for ( LicenseUserEntity licenseUserEntity : licenseUserEntities ) {
            Map< String, Object > addOns = new HashMap<>();
            addOns = ( Map< String, Object > ) JsonUtils.jsonToMap( licenseUserEntity.getLicenseEntity().getAddons(), addOns );
            allowedLocations += ( int ) addOns.get( "Location" );
        }
        return allowedLocations;
    }

    /**
     * Is restricted.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     */
    private void isRestricted( EntityManager entityManager, UUID userId ) {
        UserDTO userDTO = userCommonManager.getUserById( entityManager, userId );
        if ( userDTO != null && userDTO.getRestricted().equals( RESTRICTED_USER ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_RESTRICTED_TO_SYSTEM.getKey(), userDTO.getName() ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTokenBasedLicenseExists( EntityManager entityManager ) {
        List< LicenseEntity > license = licenseConfigDao.getObjectListByProperty( entityManager, ConstantsDAO.LICENSE_TYPE,
                ConstantsDAO.TOKEN );
        return license != null && !license.isEmpty();
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
     * Gets the license config dao.
     *
     * @return the license config dao
     */
    @Override
    public LicenseConfigurationDAO getLicenseConfigDao() {
        return licenseConfigDao;
    }

    /**
     * Sets the license config dao.
     *
     * @param licenseConfigDao
     *         the new license config dao
     */
    public void setLicenseConfigDao( LicenseConfigurationDAO licenseConfigDao ) {
        this.licenseConfigDao = licenseConfigDao;
    }

    /**
     * Gets the license user dao.
     *
     * @return the license user dao
     */
    public LicenseUserDAO getLicenseUserDao() {
        return licenseUserDao;
    }

    /**
     * Sets the license user dao.
     *
     * @param licenseUserDao
     *         the new license user dao
     */
    public void setLicenseUserDao( LicenseUserDAO licenseUserDao ) {
        this.licenseUserDao = licenseUserDao;
    }

    /**
     * gets context menu manager.
     *
     * @return contextMenuManager
     */
    public ContextMenuManager getContextMenuManager() {
        return contextMenuManager;
    }

    /**
     * sets context menu manager.
     *
     * @param contextMenuManager
     *         contextMenuManager
     */
    public void setContextMenuManager( ContextMenuManager contextMenuManager ) {
        this.contextMenuManager = contextMenuManager;
    }

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.license.manager.
     * LicenseConfigurationManager#getObjectViewManager()
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
     * Gets the permission manager.
     *
     * @return the permission manager
     */
    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    /**
     * Sets the permission manager.
     *
     * @param permissionManager
     *         the new permission manager
     */
    public void setPermissionManager( PermissionManager permissionManager ) {
        this.permissionManager = permissionManager;
    }

    /**
     * Gets the license feature DAO.
     *
     * @return the licenseFeatureDAO
     */
    public LicenseFeatureDAO getLicenseFeatureDAO() {
        return licenseFeatureDAO;
    }

    /**
     * Sets the license feature DAO.
     *
     * @param licenseFeatureDAO
     *         the licenseFeatureDAO to set
     */
    public void setLicenseFeatureDAO( LicenseFeatureDAO licenseFeatureDAO ) {
        this.licenseFeatureDAO = licenseFeatureDAO;
    }

    /**
     * Gets the audit log DAO.
     *
     * @return the audit log DAO
     */
    public AuditLogDAO getAuditLogDAO() {
        return auditLogDAO;
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
     * Gets the license manager.
     *
     * @return the license manager
     */
    public LicenseManager getLicenseManager() {
        return licenseManager;
    }

    /**
     * Sets the license manager.
     *
     * @param licenseManager
     *         the new license manager
     */
    public void setLicenseManager( LicenseManager licenseManager ) {
        this.licenseManager = licenseManager;
    }

    /**
     * Gets the monitor license manager.
     *
     * @return the monitor license manager
     */
    @Override
    public MonitorLicenseManager getMonitorLicenseManager() {
        return monitorLicenseManager;
    }

    /**
     * Sets the monitor license manager.
     *
     * @param monitorLicenseManager
     *         the new monitor license manager
     */
    public void setMonitorLicenseManager( MonitorLicenseManager monitorLicenseManager ) {
        this.monitorLicenseManager = monitorLicenseManager;
    }

    /**
     * Gets the token manager.
     *
     * @return the token manager
     */
    public UserTokenManager getTokenManager() {
        return tokenManager;
    }

    /**
     * Sets the token manager.
     *
     * @param tokenManager
     *         the new token manager
     */
    public void setTokenManager( UserTokenManager tokenManager ) {
        this.tokenManager = tokenManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI getAllActiveUsersUI( String userID ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< TableColumn > columns = GUIUtils.listColumns( UserTokenDTO.class );
        TableUI tableUI;
        try {
            tableUI = new TableUI( columns,
                    objectViewManager.getUserObjectViewsByKey( entityManager, ConstantsObjectViewKey.LICENSE_TABLE_ACTIVE_USER_KEY, userID,
                            null ) );
        } finally {
            entityManager.close();
        }
        return tableUI;
    }

    /**
     * Gets the all active users list.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param filter
     *         the filter
     *
     * @return the all active users list
     */
    @Override
    public FilteredResponse< UserTokenDTO > getAllActiveUsersList( String userIdFromGeneralHeader, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        FilteredResponse< UserTokenDTO > userTokenDTOFilteredResponse;
        try {
            isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.LICENSES.getId(),
                    PermissionMatrixEnum.READ.getValue(), Messages.NO_RIGHTS_TO_READ.getKey(), SimuspaceFeaturesEnum.LICENSES.getKey() );

            List< UserTokenEntity > userTokenList = tokenManager.getUserTokenDAO()
                    .getActiveTokenOrInactiveTokenOnRunningJobByFilter( entityManager, filter );
            List< UserTokenDTO > activeUserDTOList = new ArrayList<>();
            for ( UserTokenEntity userTokenEntity : userTokenList ) {

                List< LicenseUserEntity > licenseUserEntity = licenseUserDao.getLicenseUserEntitiesByUser( entityManager,
                        userTokenEntity.getUserEntity().getId() );
                UserTokenDTO activeUserDTO = prepareActiveUserDTOByUserTokenEntity( userTokenEntity );
                if ( licenseUserEntity != null && !licenseUserEntity.isEmpty() ) {
                    StringBuilder modulesString = getModuleString( licenseUserEntity );
                    activeUserDTO.setModule( modulesString.toString() );
                }
                activeUserDTOList.add( activeUserDTO );
            }
            userTokenDTOFilteredResponse = PaginationUtil.constructFilteredResponse( filter, activeUserDTOList );
        } finally {
            entityManager.close();
        }
        return userTokenDTOFilteredResponse;
    }

    /**
     * Gets the module string.
     *
     * @param licenseUserEntity
     *         the license user entity
     *
     * @return the module string
     */
    private StringBuilder getModuleString( List< LicenseUserEntity > licenseUserEntity ) {
        StringBuilder modulesString = new StringBuilder();
        for ( LicenseUserEntity licenseModule : licenseUserEntity ) {
            modulesString.append( licenseModule.getLicenseEntity().getModule() );
            modulesString.append( System.getProperty( "line.separator" ) );
        }
        return modulesString;
    }

    /**
     * Prepare active user DTO by user token entiry.
     *
     * @param userTokenEntity
     *         the user token entity
     *
     * @return the user token DTO
     */
    public UserTokenDTO prepareActiveUserDTOByUserTokenEntity( UserTokenEntity userTokenEntity ) {
        UserTokenDTO userTokenDTO = new UserTokenDTO();
        userTokenDTO.setId( userTokenEntity.getId().toString() );
        userTokenDTO.setToken( userTokenEntity.getToken() );
        userTokenDTO.setCreatedOn( userTokenEntity.getCreatedOn() );
        userTokenDTO.setIpAddress( userTokenEntity.getIpAddress() );
        userTokenDTO.setBrowserAgent( userTokenEntity.getBrowserAgent() );
        userTokenDTO.setExpiryTime( userTokenEntity.getExpiryTime().getTime() );
        userTokenDTO.setLastRequestTime( userTokenEntity.getLastRequestTime() );
        userTokenDTO.setExpired( userTokenEntity.isExpired() );
        userTokenDTO.setUserId( userTokenEntity.getUserEntity() != null ? userTokenEntity.getUserEntity().getId().toString()
                : ConstantsInteger.INTEGER_VALUE_ZERO + ConstantsString.EMPTY_STRING );
        userTokenDTO.setUserName( userTokenEntity.getUserEntity().getUserUid() );
        return userTokenDTO;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getActiveUserContext( String userId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        SelectionResponseUI selection;
        try {
            selection = selectionManager.createSelection( entityManager, userId, SelectionOrigins.CONTEXT, filter );
        } finally {
            entityManager.close();
        }

        List< ContextMenuItem > menu = new ArrayList<>();
        if ( filter.getItems().size() == 1 ) {
            ContextMenuItem containerCMI = new ContextMenuItem();
            containerCMI.setUrl( "system/license/expire/{id}".replace( "{id}", selection.getId() ) );
            containerCMI.setTitle( MessageBundleFactory.getMessage( "4100080x4" ) );
            menu.add( containerCMI );
        }
        return menu;
    }

    /**
     * Free license by expiring tokens.
     *
     * @param sid
     *         the sid
     * @param actionByUserId
     *         the action by user id
     *
     * @return true, if successful
     */
    @Override
    public boolean freeLicenseByExpiringTokens( String selectionId, String actionByUserId, String currentToken ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            isPermitted( entityManager, actionByUserId, SimuspaceFeaturesEnum.LICENSES.getId(), PermissionMatrixEnum.MANAGE.getValue(),
                    Messages.NO_RIGHTS_TO_MANAGE.getKey(), SimuspaceFeaturesEnum.LICENSES.getKey() );
            List< UUID > selectionIdList = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );

            for ( UUID selectedTokenIds : selectionIdList ) {

                UserTokenEntity selectedToken = tokenManager.getUserTokenDAO().getUserTokenEntityById( entityManager, selectedTokenIds );
                List< JobTokenEntity > jobTokenEntities = tokenManager.getJobTokenDAO()
                        .getJobTokenEntityByAuthTokenExpireAndNonExpire( entityManager, selectedToken.getToken() );

                if ( jobTokenEntities != null && !jobTokenEntities.isEmpty() ) {
                    Runnable abortJobsByToken = () -> jobTokenEntities.forEach(
                            jobTokenEntity -> abortAllJobsByJobToken( currentToken, jobTokenEntity ) );
                    Thread thread = new Thread( abortJobsByToken );
                    thread.start();

                }

                selectedToken.setRunningJob( false );
                tokenManager.getUserTokenDAO().updateUserTokenEntity( entityManager, selectedToken );

                // logout user from SusBe : Safe approch
                SusResponseDTO response = logOutUserByUserToken( selectedToken, currentToken );
                log.debug( "Free License : " + response.getMessage() );
            }
            entityManager.close();
        } catch ( Exception e ) {
            entityManager.close();
            throw new SusException( "Free License action: failed " + e.getMessage() );
        }
        return true;
    }

    /**
     * Abort All Job By job token
     *
     * @param currentToken
     *         the currentToken
     * @param jobTokenEntity
     *         the jobTokenEntity
     *
     * @implNote To be used in threads and runnables only
     */
    private void abortAllJobsByJobToken( String currentToken, JobTokenEntity jobTokenEntity ) {
        // runnables should have their own entityManager instances
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Map< String, Object > propertyMap = new HashMap<>();
            propertyMap.put( ConstantsDAO.TOKEN, jobTokenEntity.getToken() );

            List< JobEntity > jobEntities = ( List< JobEntity > ) permissionManager.getSusDAO()
                    .getListByPropertyMapAndClass( entityManager, propertyMap, JobEntity.class );

            jobEntities.stream().filter( jobEntity -> ( WorkflowStatus.RUNNING.equals( WorkflowStatus.getById( jobEntity.getStatus() ) ) ) )
                    .forEach( runningJobEntity -> {

                        log.debug( "Free License : Stop Running Jobs :" + runningJobEntity.getName() );
                        String url = PropertiesManager.getLocationURL() + "/api/data/stop/" + runningJobEntity.getId() + FREE_LICENSE;
                        SuSClient.getRequest( url, CommonUtils.prepareHeadersWithAuthToken( currentToken ) );

                    } );
            jobTokenEntity.setExpired( true );
            tokenManager.getJobTokenDAO().updateJobTokenEntity( entityManager, jobTokenEntity );
        } finally {
            entityManager.close();
        }

    }

    /**
     * calls api to logout user by userToken
     *
     * @param userTokenEntity
     *         the userTokenEntity
     * @param currentToken
     *         the currentToken
     *
     * @return api's response dto
     */
    private SusResponseDTO logOutUserByUserToken( UserTokenEntity userTokenEntity, String currentToken ) {
        Map< String, String > logoutToken = new HashMap<>();
        logoutToken.put( "token", userTokenEntity.getToken() );
        String urlLogout = PropertiesManager.getLocationURL() + "/api/auth/logout";
        return SuSClient.postRequest( urlLogout, JsonUtils.toJson( logoutToken ), CommonUtils.prepareHeadersWithAuthToken( currentToken ) );
    }

    /**
     * Checks if is permitted.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user featureId
     * @param featureId
     *         the featureId
     * @param premissionType
     *         the premission type
     * @param message
     *         the message
     * @param featureName
     *         the featureName
     */
    private void isPermitted( EntityManager entityManager, String userId, String featureId, String premissionType, String message,
            String featureName ) {
        log.debug( ">>isPermitted: userId: " + userId );
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                featureId + ConstantsString.COLON + premissionType ) ) {
            throw new SusException( MessageBundleFactory.getMessage( message, featureName ) );
        }
        isRestricted( entityManager, userId );
    }

    /**
     * Checks if is restricted.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     */
    private void isRestricted( EntityManager entityManager, String userIdFromGeneralHeader ) {
        UserDTO userDTO = userCommonManager.getUserById( entityManager, UUID.fromString( userIdFromGeneralHeader ) );
        if ( userDTO != null && userDTO.getRestricted().equals( RESTRICTED_USER ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_RESTRICTED_TO_SYSTEM.getKey(), userDTO.getName() ) );
        }
    }

    /**
     * Gets the selection manager.
     *
     * @return the selection manager
     */
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    /**
     * Sets the selection manager.
     *
     * @param selectionManager
     *         the new selection manager
     */
    public void setSelectionManager( SelectionManager selectionManager ) {
        this.selectionManager = selectionManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< MonitorLicenseCurveDTO > getLicesnseHistory( HistoryMap historyMapObj, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            isPermitted( entityManager, userId, SimuspaceFeaturesEnum.LICENSES.getId(), PermissionMatrixEnum.READ.getValue(),
                    Messages.NO_RIGHTS_TO_READ.getKey(), SimuspaceFeaturesEnum.LICENSES.getKey() );
            List< MonitorLicenseCurveDTO > filteredRecordsBetweenDates = monitorLicenseManager.getFilteredRecordsBetweenDates(
                    entityManager, historyMapObj );
            entityManager.close();
            return filteredRecordsBetweenDates;
        } catch ( Exception e ) {
            entityManager.close();
            log.error( e.getMessage(), e );
            return new ArrayList<>();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SelectOptionsUI > getLicenseModuleOptions( String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< SelectOptionsUI > listToReturn;
        try {
            listToReturn = new ArrayList<>();
            List< LicenseEntity > licenseEntities = licenseConfigDao.getModuleLicenseList( entityManager );
            for ( LicenseEntity licenseEntity : licenseEntities ) {
                listToReturn.add( new SelectOptionsUI( licenseEntity.getModule(), licenseEntity.getModule() ) );
            }
        } finally {
            entityManager.close();
        }
        return listToReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteObjectView( UUID viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean isDeleted;
        try {
            isDeleted = objectViewManager.deleteObjectView( entityManager, viewId );
        } finally {
            entityManager.close();
        }
        return isDeleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO getObjectViewById( UUID viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ObjectViewDTO objectViewDTO;
        try {
            objectViewDTO = objectViewManager.getObjectViewById( entityManager, viewId );
        } finally {
            entityManager.close();
        }
        return objectViewDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectViewDTO > getUserObjectViewsByKey( String key, String userId, String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< ObjectViewDTO > objectViewDTOs;
        try {
            objectViewDTOs = objectViewManager.getUserObjectViewsByKey( entityManager, key, userId, objectId );
        } finally {
            entityManager.close();
        }
        return objectViewDTOs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO saveDefaultObjectView( UUID viewId, String userId, String objectViewKey, String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ObjectViewDTO objectViewDTO;
        try {
            objectViewDTO = objectViewManager.saveDefaultObjectView( entityManager, viewId, userId, objectViewKey, objectId );
        } finally {
            entityManager.close();
        }
        return objectViewDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO saveOrUpdateObjectView( ObjectViewDTO viewDTO, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ObjectViewDTO objectViewDTO;
        try {
            objectViewDTO = objectViewManager.saveOrUpdateObjectView( entityManager, viewDTO, userId );
        } finally {
            entityManager.close();
        }
        return objectViewDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserTokenDTO > getAllActiveTokens() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< UserTokenDTO > allActiveTokens;
        try {
            allActiveTokens = tokenManager.getAllActiveTokens( entityManager );
        } finally {
            entityManager.close();
        }
        return allActiveTokens;
    }

    @Override
    public List< Object > getAllValuesForLicenseModuleTableColumn( String columnName, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var allColumns = GUIUtils.listColumns( ModuleLicenseDTO.class );
            GUIUtils.validateColumnForAllValues( columnName, allColumns );
            List< Object > allValues;
            allValues = licenseConfigDao.getAllPropertyValues( entityManager, columnName );
            return allValues;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List< Object > getAllValuesForLicenseUserTableColumn( String columnName, String token ) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var allColumns = GUIUtils.listColumns( UserTokenDTO.class );
            GUIUtils.validateColumnForAllValues( columnName, allColumns );
            List< Object > allValues;
            allValues = tokenManager.getUserTokenDAO().getAllPropertyValues( entityManager, columnName );
            return allValues;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Sets entity manager factory.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}