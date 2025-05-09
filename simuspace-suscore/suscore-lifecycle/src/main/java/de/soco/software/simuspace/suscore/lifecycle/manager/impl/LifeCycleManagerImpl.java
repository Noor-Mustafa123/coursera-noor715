package de.soco.software.simuspace.suscore.lifecycle.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.StatusConfigDTO;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.lifecycle.constants.ConstantsLifeCycle;
import de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager;
import de.soco.software.simuspace.suscore.lifecycle.model.LifeCycleConfigration;
import de.soco.software.simuspace.suscore.lifecycle.model.LifeCycleDTO;
import de.soco.software.suscore.jsonschema.model.LifeCyclePolicyConfigration;
import de.soco.software.suscore.jsonschema.model.LifeCyclePolicyDTO;
import de.soco.software.suscore.jsonschema.reader.ConfigFilePathReader;

/**
 * Class Containing all the business logic for SuS Entity Status Life Cycle.
 *
 * @author Nosheen.Sharif
 */
@Log4j2
public class LifeCycleManagerImpl implements LifeCycleManager {

    /**
     * The life cycle list.
     */
    private List< LifeCycleDTO > lifeCycleList = new ArrayList<>();

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Init method for bootup.
     */
    public void init() {
        if ( !PropertiesManager.isMasterLocation() ) {
            return;
        }

        Notification notif = new Notification();
        lifeCycleList = getLifeCycleConfigurationByFileName( ConstantsLifeCycle.LIFE_CYCLE_CONFIG_FILE_NAME );
        List< String > statusIds = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( lifeCycleList ) ) {
            notif.addNotification( validateLifeCycleConfiguration( lifeCycleList ) );
            for ( LifeCycleDTO lifeCycleDTO : lifeCycleList ) {
                notif.addNotification( validateLifeCycleStatusConfig( lifeCycleDTO ) );
                for ( final StatusConfigDTO statusDto : lifeCycleDTO.getStatusConfiguration() ) {
                    statusIds.add( statusDto.getId() );
                }
            }
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            List< String > uuids;
            try {
                uuids = susDAO.getAllLifeCycleStatusIds( entityManager );
            } finally {
                entityManager.close();
            }
            for ( String uuid : uuids ) {
                if ( de.soco.software.simuspace.suscore.common.util.StringUtils.isNotNullOrEmpty( uuid ) && !statusIds.contains( uuid ) ) {
                    notif.addError( new Error( MessageBundleFactory.getMessage( Messages.STATUS_IS_BEING_USED_IN_SUS_ENTITY.getKey() ) ) );
                    break;
                }
            }
        }
        if ( notif.hasErrors() ) {
            throw new SusException( notif.getMessages().toString() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatusDTO getDefaultStatusByLifeCycleId( String lifeCycleId ) {
        LifeCycleDTO lifeCycleDTO = getLifeCycleById( lifeCycleId );
        if ( lifeCycleDTO != null ) {
            for ( StatusConfigDTO statusDTo : lifeCycleDTO.getStatusConfiguration() ) {
                if ( statusDTo.getId().equals( lifeCycleDTO.getApplicable().getDefaultStatus() ) ) {
                    return new StatusDTO( statusDTo.getId(), statusDTo.getName() );
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatusDTO getStatusByLifeCycleNameAndStatusId( String lifeCycleId, String statusId ) {
        LifeCycleDTO lifeCycleDTO = getLifeCycleById( lifeCycleId );
        if ( lifeCycleDTO != null ) {
            for ( StatusConfigDTO statusDTo : lifeCycleDTO.getStatusConfiguration() ) {
                if ( statusDTo.getId().equals( statusId ) ) {
                    return new StatusDTO( statusDTo.getId(), statusDTo.getName() );
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatusConfigDTO getLifeCycleStatusByStatusId( String lifeCycleStatus ) {
        for ( final LifeCycleDTO lifeCycleDTO : lifeCycleList ) {
            for ( StatusConfigDTO statusCon : lifeCycleDTO.getStatusConfiguration() ) {
                if ( lifeCycleStatus.equals( statusCon.getId() ) ) {
                    return statusCon;
                }
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager#getDefaultStatusOfAnyObject()
     */
    @Override
    public String getDefaultStatusOfJob() {
        for ( final LifeCycleDTO lifeCycleDTO : lifeCycleList ) {
            if ( "LC_JOB".equals( lifeCycleDTO.getName() )
                    || "841fa6fb-1e6e-46a3-b844-4809ef631a3b".equalsIgnoreCase( lifeCycleDTO.getId() ) ) {
                return lifeCycleDTO.getApplicable().getDefaultStatus();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< LifeCycleDTO > getLifeCycleConfigurationByFileName( String fileName ) {
        List< LifeCycleDTO > lifeCycles = new ArrayList<>();
        try ( InputStream stream = ConfigFilePathReader.getFileByNameFromKaraf( fileName ) ) {
            lifeCycles = JsonUtils.jsonStreamToObject( stream, LifeCycleConfigration.class ).getLifeCycles();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return lifeCycles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< LifeCyclePolicyDTO > getLifeCyclePolicyConfigurationByFileName() {
        List< LifeCyclePolicyDTO > lifeCyclePolicies = new ArrayList<>();
        try ( InputStream stream = ConfigFilePathReader.getFileByNameFromKaraf( ConstantsLifeCycle.LIFE_CYCLE_POLICY_CONFIG_FILE_NAME ) ) {
            lifeCyclePolicies = JsonUtils.jsonStreamToObject( stream, LifeCyclePolicyConfigration.class ).getLifeCyclePolicyDTO();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return lifeCyclePolicies;
    }

    /**
     * Validate status config file.
     *
     * @param list
     *         the list
     *
     * @return the notification
     */
    private Notification validateLifeCycleConfiguration( List< LifeCycleDTO > list ) {

        final Notification notification = new Notification();
        final Set< String > lifCyclesNameSet = new HashSet<>();
        // **Life Cycle Name should be unique
        for ( LifeCycleDTO lifeCycleDto : list ) {
            // ** Id Should not be null
            if ( UUID.fromString( lifeCycleDto.getId() ) == null ) {
                notification
                        .addError( new Error( MessageBundleFactory.getMessage( Messages.LIFE_CYCLE_ID_CANNOT_BE_NULL_EMPTY.getKey() ) ) );
            }
            if ( !lifCyclesNameSet.add( lifeCycleDto.getName() ) ) {
                notification.addError( new Error(
                        MessageBundleFactory.getMessage( Messages.LIFE_CYCLE_NAME_ALREADY_PRESENT.getKey(), lifeCycleDto.getName() ) ) );
            }
        }

        return notification;
    }

    /**
     * Validate life cycle status config.
     *
     * @param statusConfig
     *         the status config
     *
     * @return the notification
     */
    private Notification validateLifeCycleStatusConfig( LifeCycleDTO statusConfig ) {
        final Notification notification = new Notification();
        final Set< String > statusNameset = new HashSet<>();
        final Set< String > statusIdset = new HashSet<>();
        if ( null == statusConfig.getStatusConfiguration() || statusConfig.getStatusConfiguration().isEmpty() ) {
            notification.addError( new Error(
                    MessageBundleFactory.getMessage( Messages.LIFE_CYCLE_STATUS_CONFIGURATION_CANNOT_BE_NULL_EMPTY.getKey() ) ) );
            return notification;
        }
        validateLifeCycleStatusConfigList( statusConfig, notification, statusNameset, statusIdset );
        if ( null == statusConfig.getApplicable() ) {
            notification.addError(
                    new Error( MessageBundleFactory.getMessage( Messages.LIFE_CYCLE_APPLICABLE_KEY_CANNOT_BE_NULL_EMPTY.getKey() ) ) );
            return notification;
        }
        // ** check for default type in validationList
        if ( !statusConfig.getApplicable().getValidStatusList().contains( statusConfig.getApplicable().getDefaultStatus() ) ) {

            notification.addError( new Error( MessageBundleFactory.getMessage( Messages.STATUS_NOT_VALID_FOR_OBJECT.getKey(),
                    statusConfig.getApplicable().getDefaultStatus() ) ) );
        }
        return notification;
    }

    /**
     * Validate life cycle status config list.
     *
     * @param statusConfig
     *         the status config
     * @param notification
     *         the notification
     * @param statusNameset
     *         the status nameset
     * @param statusIdset
     *         the status idset
     */
    private void validateLifeCycleStatusConfigList( LifeCycleDTO statusConfig, final Notification notification,
            final Set< String > statusNameset, final Set< String > statusIdset ) {
        for ( final StatusConfigDTO statusDto : statusConfig.getStatusConfiguration() ) {
            // ** Status Id Should not be null
            if ( UUID.fromString( statusDto.getId() ) == null ) {
                notification.addError(
                        new Error( MessageBundleFactory.getMessage( Messages.LIFE_CYCLE_STATUS_ID_CANNOT_BE_NULL_EMPTY.getKey() ) ) );
            }
            // ** Status Name should not empty
            if ( StringUtils.isBlank( statusDto.getName() ) ) {
                notification.addError(
                        new Error( MessageBundleFactory.getMessage( Messages.LIFE_CYCLE_STATUS_NAME_CANNOT_BE_NULL_EMPTY.getKey() ) ) );
            } else {

                // ** Status Name Length should be 64 (default)
                // ** Status Name should not contains special Characters .default name chk
                notification.addNotification( ValidationUtils.validateFieldAndLength( statusDto.getName(), ConstantsLifeCycle.NAME,
                        ConstantsLength.STANDARD_NAME_LENGTH, false, true ) );
            }
            // **Status Name should be unique
            if ( !statusNameset.add( statusDto.getName() ) ) {
                notification.addError( new Error(
                        MessageBundleFactory.getMessage( Messages.LIFE_CYCLE_STATUS_NAME_BE_UNIQUE.getKey(), statusDto.getName() ) ) );
            }
            // ** Status Id should be unique
            if ( !statusIdset.add( statusDto.getId() ) ) {
                notification.addError( new Error(
                        MessageBundleFactory.getMessage( Messages.LIFE_CYCLE_STATUS_ID_BE_UNIQUE.getKey(), statusDto.getId() ) ) );
            }
            // *** CanMoveToStatus Should contain Unique Ids.(remove duplicates at BE)
            final Set< String > hs = new HashSet<>( statusDto.getCanMoveToStatus() );
            statusDto.setCanMoveToStatus( new ArrayList<>( hs ) );

            // ** if unique id true then moveOldVersionToStatus cannot be null
            if ( statusDto.isUnique() && ( statusDto.getMoveOldVersionToStatus() == null ) ) {
                notification.addError( new Error( MessageBundleFactory
                        .getMessage( Messages.LIFE_CYCLE_STATUS_MOVE_TO_CANNOT_BE_NULL.getKey(), statusDto.getName() ) ) );
            }
        }
    }

    /**
     * Gets the life cycle by name.
     *
     * @param id
     *         the name
     *
     * @return the life cycle by name
     */
    private LifeCycleDTO getLifeCycleById( String id ) {
        LifeCycleDTO dto = null;
        if ( StringUtils.isNotEmpty( id ) && CollectionUtil.isNotEmpty( lifeCycleList ) ) {
            for ( LifeCycleDTO lifeCycleDTO : lifeCycleList ) {
                if ( id.equals( lifeCycleDTO.getId() ) ) {
                    dto = lifeCycleDTO;
                }
            }
        }

        return dto;
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

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager#getOwnerVisibleStatusByPolicyId(java.lang.String)
     */
    @Override
    public List< String > getOwnerVisibleStatusByPolicyId( String lifeCycleId ) {
        List< String > status = new ArrayList<>();

        LifeCycleDTO lifeCycleDTO = getLifeCycleById( lifeCycleId );

        if ( lifeCycleDTO != null ) {
            for ( StatusConfigDTO statusDTo : lifeCycleDTO.getStatusConfiguration() ) {
                if ( "all".equalsIgnoreCase( statusDTo.getVisible() ) || "owner".equalsIgnoreCase( statusDTo.getVisible() ) ) {
                    status.add( statusDTo.getId() );
                }
            }
        }

        return status;
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager#getAnyVisibleStatusByPolicyId(java.lang.String)
     */
    @Override
    public List< String > getAnyVisibleStatusByPolicyId( String lifeCycleId ) {
        List< String > status = new ArrayList<>();

        LifeCycleDTO lifeCycleDTO = getLifeCycleById( lifeCycleId );

        if ( lifeCycleDTO != null ) {
            for ( StatusConfigDTO statusDTo : lifeCycleDTO.getStatusConfiguration() ) {
                if ( "all".equalsIgnoreCase( statusDTo.getVisible() ) ) {
                    status.add( statusDTo.getId() );
                }
            }
        }

        return status;
    }

    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}
