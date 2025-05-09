package de.soco.software.simuspace.suscore.object.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * The Class MetaDataKeyValueDTO is used to map the object meta data.
 */
@Getter
@Setter
public class ObjectMetaDataDTO {

    /**
     * The Constant VALUE_FIELD.
     */
    private static final String VALUE_FIELD = "value";

    /**
     * The Constant KEY_FIELD.
     */
    public static final String KEY_FIELD = "key";

    /**
     * The metadata.
     */
    private List< MetaDataEntryDTO > metadata;

    /**
     * Instantiates a new meta data key value DTO.
     */
    public ObjectMetaDataDTO() {
        super();
    }

    /**
     * Validate.
     *
     * @return the notification
     */
    public Notification validate() {
        Notification notify = new Notification();
        for ( MetaDataEntryDTO metaDataEntry : getMetadata() ) {
            if ( StringUtils.isBlank( metaDataEntry.getKey() ) ) {
                notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), KEY_FIELD ) ) );
            } else {
                notify.addNotification( de.soco.software.simuspace.suscore.common.util.ValidationUtils
                        .validateFieldAndLength( metaDataEntry.getKey(), KEY_FIELD, ConstantsLength.STANDARD_NAME_LENGTH, false, false ) );
            }
            if ( StringUtils.isBlank( metaDataEntry.getValue() ) ) {
                notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), VALUE_FIELD ) ) );
            } else {
                notify.addNotification( de.soco.software.simuspace.suscore.common.util.ValidationUtils.validateFieldAndLength(
                        metaDataEntry.getValue(), VALUE_FIELD, ConstantsLength.STANDARD_NAME_LENGTH, false, false ) );
            }
        }
        return notify;

    }

    /**
     * Validate maximum entries in meta data map.
     *
     * @param map
     *         the map
     */
    public static void validateMaximumEntriesInMetaDataMap( Map< String, String > map ) {
        if ( map.size() > ConstantsInteger.MAX_ENTRIES_IN_METADATA ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MAX_ENTRIES_VOILATED_IN_METADATA.getKey() ) );
        }
    }

}
