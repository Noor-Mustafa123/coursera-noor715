package de.soco.software.simuspace.suscore.object.manager;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.utility.ConstantsEntities;
import de.soco.software.simuspace.suscore.object.utility.ConstantsObjectTypes;

/**
 * Object Base Manager Class for all managers of suscore to provide the utility methods for object Managers
 *
 * @author Nosheen.Sharif
 */
public abstract class SuSObjectBaseManager {

    public String getDefaultStatusForObjectByType( String objectType ) {
        // will be implemeneted may be with lifecycle story
        // need statusConfig(lifeCycle) for each object type of suscore
        return objectType;
    }

    /**
     * Fill base entity from model su s entity.
     *
     * @param entity
     *         the entity
     * @param model
     *         the model
     *
     * @return the su s entity
     */
    protected SuSEntity fillBaseEntityFromModel( SuSEntity entity, SuSObjectModel model ) {
        if ( model != null && entity != null ) {

            // set UUID for Enitty creation

            if ( model.isUpdate() && null != model.getId() && StringUtils.isNotBlank( model.getId() ) ) {
                entity.setComposedId( new VersionPrimaryKey( UUID.fromString( model.getId() ),
                        model.getVersion() != null ? model.getVersion().getId() : SusConstantObject.DEFAULT_VERSION_NO ) );

            } else {
                entity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(),
                        model.getVersion() != null ? model.getVersion().getId() : SusConstantObject.DEFAULT_VERSION_NO ) );

            }

            entity.setCreatedOn( model.getCreatedOn() );
            entity.setName( model.getName() );
            // set current date for update
            if ( model.isUpdate() ) {
                entity.setModifiedOn( new Date() );
            } else {
                entity.setModifiedOn( model.getModifiedOn() );
            }
        }

        return entity;
    }

    /**
     * Prepare base model from entity su s object model.
     *
     * @param entity
     *         the entity
     *
     * @return the su s object model
     */
    protected SuSObjectModel prepareBaseModelFromEntity( SuSEntity entity ) {
        SuSObjectModel model = null;
        if ( entity != null ) {
            model = new SuSObjectModel( entity.getComposedId().getId().toString() );

            model.setStatus( entity.getLifeCycleStatus() );
            model.setVersion( new VersionDTO( entity.getComposedId().getVersionId() ) );
            model.setCreatedOn( entity.getCreatedOn() );
            model.setModifiedOn( entity.getModifiedOn() );
            model.setName( entity.getName() );
        }

        return model;
    }

    /**
     * Validate su s model notification.
     *
     * @param model
     *         the model
     *
     * @return the notification
     */
    protected Notification validateSuSModel( SuSObjectModel model ) {
        Notification notify = new Notification();

        if ( model != null ) {

            // validate UUId for update case

            if ( model.isUpdate() && !de.soco.software.simuspace.suscore.common.util.ValidationUtils.validateUUIDString( model.getId() ) ) {
                notify.addError( new Error( MessageBundleFactory.getMessage( Messages.PARENT_ID_IS_NOT_VALID.getKey(), model.getId() ) ) );
            }
            if ( StringUtils.isBlank( model.getName() ) ) {

                notify.addError( new Error( MessageBundleFactory.getMessage( Messages.OBJECT_NAME_CANNOT_EMPTY.getKey() ) ) );

            } else {
                notify.addNotification(
                        de.soco.software.simuspace.suscore.common.util.ValidationUtils.validateFieldAndLength( model.getName(),

                                SusConstantObject.OBJECT_NAME + ConstantsString.STANDARD_SEPARATOR + model.getName(),
                                ConstantsLength.STANDARD_NAME_LENGTH, false, true ) );
            }

            if ( StringUtils.isBlank( model.getClassName() ) ) {
                notify.addError( new Error( MessageBundleFactory.getMessage( Messages.OBJECT_CLASS_NAME_CANNOT_EMPTY.getKey() ) ) );
            } else {
                try {
                    Class.forName( model.getClassName() );
                } catch ( ClassNotFoundException e ) {
                    notify.addError( new Error( MessageBundleFactory.getMessage( Messages.OBJECT_CLASS_IS_NOT_VALID.getKey() ) ) );
                }
            }

        }
        return notify;

    }

    /**
     * Gets object type class.
     *
     * @param objectType
     *         the object type
     *
     * @return the object type class
     */
    protected Class< ? > getObjectTypeClass( String objectType ) {
        if ( StringUtils.isNotBlank( objectType ) ) {
            try {

                return switch ( objectType.toLowerCase().trim() ) {
                    case ConstantsObjectTypes.PROJECT_TYPE -> Class.forName( ConstantsEntities.PROJECT_ENTITY_NAME );
                    case ConstantsObjectTypes.VARIANT_TYPE -> Class.forName( ConstantsEntities.VARIANT_ENTITY_NAME );
                    default -> throw new SusException(
                            ( MessageBundleFactory.getMessage( Messages.OBJECT_TYPE_IS_NOT_VALID.getKey(), objectType ) ) );
                };

            } catch ( ClassNotFoundException e ) {
                throw new SusException( ( MessageBundleFactory.getMessage( Messages.OBJECT_TYPE_IS_NOT_VALID.getKey(), objectType ) ) );

            }
        }
        return null;

    }

    /**
     * Gets qualified class name.
     *
     * @param susEntityClass
     *         the sus entity class
     *
     * @return the qualified class name
     */
    protected Class< ? > getQualifiedClassName( String susEntityClass ) {
        if ( StringUtils.isNotBlank( susEntityClass ) ) {
            try {

                return Class.forName( susEntityClass );

            } catch ( ClassNotFoundException e ) {
                throw new SusException( ( MessageBundleFactory.getMessage( Messages.OBJECT_TYPE_IS_NOT_VALID.getKey(), susEntityClass ) ) );

            }
        }
        return null;

    }

}
