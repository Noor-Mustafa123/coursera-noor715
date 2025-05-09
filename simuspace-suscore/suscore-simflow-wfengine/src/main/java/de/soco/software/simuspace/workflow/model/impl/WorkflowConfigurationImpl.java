package de.soco.software.simuspace.workflow.model.impl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.enums.simflow.ElementKeys;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldModes;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.RunMode;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.workflow.constant.ConstantsElementKey;
import de.soco.software.simuspace.workflow.constant.ConstantsElementProps;
import de.soco.software.simuspace.workflow.model.ElementConfig;
import de.soco.software.simuspace.workflow.model.WorkflowConfiguration;

/**
 * This Class contains properties of configuration for a workflow.
 *
 * @author Huzaifah
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class WorkflowConfigurationImpl implements WorkflowConfiguration, Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The Constant CLIENT_MODE.
     */
    private static final String CLIENT_MODE = "client";

    /**
     * The applicable. This means an element as key can accept incoming connection from which other elements
     */
    private Map< String, List< String > > applicable;

    /**
     * The elements.
     */
    private transient List< WorkflowConfigElements > elements;

    /**
     * Instantiates a new config impl.
     */
    public WorkflowConfigurationImpl() {
        super();
    }

    /**
     * Instantiates a new config impl.
     *
     * @param elements
     *         the elements
     * @param applicable
     *         the applicable
     */
    public WorkflowConfigurationImpl( List< WorkflowConfigElements > elements, Map< String, List< String > > applicable ) {
        super();
        this.elements = elements;
        this.applicable = applicable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map< String, List< String > > getApplicable() {
        return applicable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< WorkflowConfigElements > getElements() {
        return elements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicable( Map< String, List< String > > applicable ) {
        this.applicable = applicable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setElements( List< WorkflowConfigElements > elements ) {
        this.elements = elements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ConfigImpl [elements='" + elements + "', applicable='" + applicable + "']";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Notification validate() {

        final Notification notif = new Notification();
        final HashSet< String > elementNameset = new HashSet<>();

        for ( final WorkflowConfigElements workflowElements : getElements() ) {
            if ( workflowElements.getElement() == null ) {
                continue;
            }

            // to check the element name uniqueness
            final ElementConfig element = workflowElements.getElement().getData();
            if ( !elementNameset.add( element.getName() ) ) {
                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.DUPLICATE_ELEMENT_NAME, element.getName() ) ) );
            }

            if ( StringUtils.isNotNullOrEmpty( element.getKey() ) ) {

                // common validation done
                notif.addNotification( validateElementCommonFields( element ) );
                if ( ConstantsElementKey.WFE_IO.contentEquals( element.getKey() ) ) {
                    notif.addNotification( validateInputElement( element ) );
                } else if ( ConstantsElementKey.WFE_SCRIPT.contentEquals( element.getKey() ) ) {
                    notif.addNotification( validateScriptElement( element ) );
                } else if ( ConstantsElementKey.WFE_SCRIPT_PYTHON.contentEquals( element.getKey() ) ) {
                    notif.addNotification( validateScriptElement( element ) );
                }
            } else {
                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.EMPTY_ELEMENT_KEY ) ) );
            }
        }
        notif.addNotification( this.validateApplicable( getApplicable() ) );
        return notif;

    }

    /**
     * This method is specifically written to validate either a WF element is applicable or not
     *
     * @param applicable
     *         the applicable by applicable means an element as key can accept incoming connection from which other elements
     *
     * @return the notification
     */
    private Notification validateApplicable( Map< String, List< String > > applicable ) {
        final Notification notif = new Notification();
        for ( final Map.Entry< String, List< String > > entry : applicable.entrySet() ) {
            if ( !ElementKeys.getkeys().contains( entry.getKey() ) ) {

                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.INVALID_APPLICABLE_KEY, entry.getKey() ) ) );
            }

            for ( final String value : entry.getValue() ) {
                if ( !ElementKeys.getkeys().contains( value ) ) {
                    notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.INVALID_APPLICABLE_VALUE, value ) ) );
                }
            }
        }
        return notif;
    }

    /**
     * Validate element common fields. This method is specifically written to Validate All the common Fields i.e, name, key , description
     * ,version, icon, shape, comments more fields can be added
     *
     * @param element
     *         the Config WorkFlow element
     *
     * @return notification
     *
     * @throws SusException
     *         the sus exception
     */

    private Notification validateElementCommonFields( ElementConfig element ) {

        final Notification notif = new Notification();
        notif.addNotification( StringUtils.validateFieldAndLength( element.getName(), ConstantsElementProps.NAME,
                ConstantsLength.STANDARD_NAME_LENGTH, Boolean.FALSE, Boolean.FALSE ) );
        notif.addNotification( StringUtils.validateFieldAndLength( element.getKey(), ConstantsElementProps.KEY,
                ConstantsLength.STANDARD_NAME_LENGTH, Boolean.FALSE, Boolean.FALSE ) );
        notif.addNotification( StringUtils.validateFieldAndLength( element.getDescription(), ConstantsElementProps.DESCRIPTION,
                ConstantsLength.STANDARD_COMMENT_LENGTH, Boolean.TRUE, Boolean.FALSE ) );
        notif.addNotification( StringUtils.validateFieldAndLength( element.getComments(), ConstantsElementProps.COMMENTS,
                ConstantsLength.STANDARD_COMMENT_LENGTH, Boolean.TRUE, Boolean.FALSE ) );
        notif.addNotification( StringUtils.validateFieldAndLength( element.getShape(), ConstantsElementProps.SHAPE,
                ConstantsLength.STANDARD_NAME_LENGTH, Boolean.TRUE, Boolean.FALSE ) );
        notif.addNotification( StringUtils.validateFieldAndLength( element.getIcon(), ConstantsElementProps.ICON,
                ConstantsLength.STANDARD_NAME_LENGTH, Boolean.TRUE, Boolean.FALSE ) );
        notif.addNotification( StringUtils.validateFieldAndLength( element.getInfo().getVersion(), ConstantsElementProps.VERSION,
                ConstantsLength.STANDARD_NAME_LENGTH, Boolean.FALSE, Boolean.FALSE ) );

        for ( final Field< ? > field : element.getFields() ) {
            if ( field.getMode().equals( FieldModes.USER.getType() ) && !element.getAllowedFieldKeys().contains( field.getType() ) ) {

                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.FIELD_NOT_ALLOWED, field.getType() ) ) );

            }
        }
        // to check Field Names to be unique with in element
        final HashSet< String > fieldNameset = new HashSet<>();
        for ( final Field< ? > field : element.getFields() ) {
            if ( !FieldTypes.getTypes().contains( field.getType() ) ) {

                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.FILED_KEY_NOT_ALLOWED, field.getType() ) ) );

            }
            if ( !fieldNameset.add( field.getName() ) ) {

                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.DUPLICATE_FIELD, field.getName(), element.getName() ) ) );

            }
        }
        return notif;

    }

    /**
     * Validate input element. this method is specifically written to validate an input element of Work flow Configuration. this method
     * validates Allowed FeildKeys i,e "section", "text", "dropdown", "textarea". FieldKeys Enum is used to check either an input element
     * field is allowed or not.
     *
     * @param element
     *         the workflow input element
     *
     * @return notification notification
     *
     * @throws SusException
     *         the sus exception
     */
    private Notification validateInputElement( ElementConfig element ) {
        final Notification notif = new Notification();

        if ( !CollectionUtil.isNotEmpty( element.getAllowedFieldKeys() ) ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ALLOWED_KEYS_CANT_BE_NULL ) ) );
        } else {
            for ( final String elementKey : element.getAllowedFieldKeys() ) {
                if ( !FieldTypes.getTypes().contains( elementKey ) ) {

                    notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.INVALID_FIELD_TYPE, elementKey ) ) );

                }
            }
        }
        return notif;

    }

    /**
     * validateScriptElement is a private method. this method is specifically written for the validation of script element this method
     * validates RunMode Keys i.e, [client,server, user-selected, null] RunMode Enum is used to validate either element has a valid
     *
     * @param element
     *         the Work Flow script element
     *
     * @return notification notification
     */
    private Notification validateScriptElement( ElementConfig element ) {
        final Notification notif = new Notification();
        // run mode check
        if ( StringUtils.isNotNullOrEmpty( element.getRunMode() ) ) {

            if ( !RunMode.getRunModes().contains( element.getRunMode() ) && !element.getRunMode().equalsIgnoreCase( CLIENT_MODE ) ) {

                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.INVALID_RUN_MODE, element.getRunMode() ) ) );

            }
        } else {

            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.EMPTY_RUN_MODE ) ) );
        }
        return notif;
    }

}
