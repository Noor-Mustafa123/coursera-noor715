package de.soco.software.simuspace.suscore.data.manager.impl.base;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ModuleLicenseDTO;
import de.soco.software.simuspace.suscore.common.model.UserLimitDTO;
import de.soco.software.simuspace.suscore.common.util.DateFormatStandard;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LicenseUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.common.dao.LicenseConfigurationDAO;
import de.soco.software.simuspace.suscore.data.common.dao.LicenseUserDAO;
import de.soco.software.simuspace.suscore.data.entity.LicenseEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseFeatureEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseUserEntity;
import de.soco.software.simuspace.suscore.data.manager.base.LicenseConfigurationCommonManager;

public class LicenseConfigurationCommonManagerImpl implements LicenseConfigurationCommonManager {

    private LicenseConfigurationDAO licenseConfigDao;

    private LicenseUserDAO licenseUsersDao;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    @Override
    public boolean isFeatureAllowedToUser( EntityManager entityManager, String feature, String userId ) {
        if ( StringUtils.isBlank( userId ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_ID_CANNOT_BE_NULL.getKey() ) );
        }
        return isUserLicenseExistForModule( entityManager, feature, userId );
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
            LicenseUserEntity licenseUserEntityByModuleAndUser = licenseUsersDao.getLicenseUserEntityByModuleAndUser( entityManager,
                    UUID.fromString( userId ), featureEntity.getLicenseEntity().getModule() );
            return licenseUserEntityByModuleAndUser != null;
        } else {
            return false;
        }

    }

    private ModuleLicenseDTO getModuleLicenseToCheckIsUserLicenseExistForModule( EntityManager entityManager, String module ) {
        if ( StringUtils.isBlank( module ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MODULE_NAME_CANNOT_BE_NULL.getKey() ) );
        }
        ModuleLicenseDTO moduleLicenseDTO = prepareLicenseWithoutUserLimitations( entityManager,
                licenseConfigDao.getModuleLicense( entityManager, module ) );
        if ( !LicenseUtils.verifyLicenseExpiry( moduleLicenseDTO ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.LICENSE_IS_EXPIRED.getKey(), moduleLicenseDTO.getModule() ) );
        }
        if ( moduleLicenseDTO != null && !LicenseUtils.verifyLicense( moduleLicenseDTO ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.LICENSE_SIGNATURE_IS_NOT_VALID.getKey() ) );
        }
        return moduleLicenseDTO;
    }

    private ModuleLicenseDTO prepareLicenseWithoutUserLimitations( EntityManager entityManager, LicenseEntity licenseEntity ) {
        if ( licenseEntity == null ) {
            return null;
        }
        UserLimitDTO userLimit = new UserLimitDTO( licenseEntity.getAllowedUsers(), licenseEntity.getRestrictedUsers() );
        Set< String > features = new HashSet<>();
        List< LicenseFeatureEntity > licenseFeatureEntities = licenseConfigDao.getLicenseFeaturesByLicense( entityManager, licenseEntity );
        licenseFeatureEntities.stream().forEach( featureEntity -> features.add( featureEntity.getFeature() ) );
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
     * @param licenseConfigDao
     *         the licenseConfigDao to set
     */
    public void setLicenseConfigDao( LicenseConfigurationDAO licenseConfigDao ) {
        this.licenseConfigDao = licenseConfigDao;
    }

    /**
     * @param licenseUsersDao
     *         the licenseUsersDao to set
     */
    public void setLicenseUsersDao( LicenseUserDAO licenseUsersDao ) {
        this.licenseUsersDao = licenseUsersDao;
    }

    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}