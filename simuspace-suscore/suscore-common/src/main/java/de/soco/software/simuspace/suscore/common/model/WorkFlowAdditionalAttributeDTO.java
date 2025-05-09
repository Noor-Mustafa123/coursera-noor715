package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;
import java.util.List;

import de.soco.software.suscore.jsonschema.model.BindFrom;
import de.soco.software.suscore.jsonschema.model.Messages;
import de.soco.software.suscore.jsonschema.model.Rules;

/**
 * The Class WorkFlowAdditionalAttribute.
 *
 * @author noman arshad
 */
public class WorkFlowAdditionalAttributeDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -2766730248203940378L;

    /**
     * The Constant COLOR_STATIC.
     */
    public static final String COLOR_STATIC = "colorstatic";

    /**
     * The Constant TEXT_STATIC.
     */
    public static final String TEXT_STATIC = "textstatic";

    /**
     * The Constant TEXT_RANDOM.
     */
    public static final String TEXT_RANDOM = "textrandom";

    /**
     * the user uuid.
     */
    @UIFormField( name = "id", title = "3000021x4", isAsk = false, type = "hidden" )
    @UIColumn( data = "id", filter = "uuid", renderer = "hidden", title = "3000021x4", name = "id", type = "link", isShow = false )
    private String id;

    /**
     * The reference.
     */
    @UIFormField( name = "name", title = "3000032x4", orderNum = 15 )
    @UIColumn( data = "name", filter = "", renderer = "text", title = "3000032x4", name = "name", isSortable = false, orderNum = 15, width = 0 )
    private String name;

    /**
     * The input decks.
     */
    @UIFormField( name = "label", title = "3000140x4", orderNum = 17 )
    @UIColumn( data = "label", filter = "", renderer = "text", title = "3000140x4", name = "label", isSortable = false, orderNum = 17 )
    private String label;

    /**
     * The type.
     */
    @UIFormField( name = "type", title = "3000051x4", orderNum = 18 )
    @UIColumn( data = "type", filter = "", renderer = "text", title = "3000051x4", name = "type", isSortable = false, orderNum = 18 )
    private String type;

    /**
     * The project.
     */
    @UIFormField( name = "required", title = "9900009x4", orderNum = 20 )
    @UIColumn( data = "required", filter = "", renderer = "text", title = "9900009x4", name = "required", isSortable = false, orderNum = 20 )
    private String required;

    /**
     * The bind from.
     */
    private BindFrom bindFrom;

    /**
     * The value.
     */
    private Object value;

    /**
     * The convert.
     */
    private String convert;

    /**
     * The help.
     */
    private String help;

    /**
     * The rules.
     */
    private Rules rules;

    /**
     * The messages.
     */
    private Messages messages;

    /**
     * The options.
     */
    // SelectObjectUI as Object
    private List< Object > options;

    /**
     * The multiple.
     */
    private boolean multiple;

    /**
     * The color static.
     */
    private String staticOrRandomOption;

    public WorkFlowAdditionalAttributeDTO() {
    }

    /**
     * Gets the rules.
     *
     * @return the rules
     */
    public Rules getRules() {
        return rules;
    }

    /**
     * Gets the help.
     *
     * @return the help
     */
    public String getHelp() {
        return help;
    }

    /**
     * Sets the help.
     *
     * @param help
     *         the new help
     */
    public void setHelp( String help ) {
        this.help = help;
    }

    /**
     * Gets the messages.
     *
     * @return the messages
     */
    public Messages getMessages() {
        return messages;
    }

    /**
     * Sets the messages.
     *
     * @param messages
     *         the new messages
     */
    public void setMessages( Messages messages ) {
        this.messages = messages;
    }

    /**
     * Sets the rules.
     *
     * @param rules
     *         the new rules
     */
    public void setRules( Rules rules ) {
        this.rules = rules;
    }

    /**
     * Gets the convert.
     *
     * @return the convert
     */
    public String getConvert() {
        return convert;
    }

    /**
     * Sets the convert.
     *
     * @param convert
     *         the new convert
     */
    public void setConvert( String convert ) {
        this.convert = convert;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label.
     *
     * @param label
     *         the new label
     */
    public void setLabel( String label ) {
        this.label = label;
    }

    /**
     * Gets the bind from.
     *
     * @return the bind from
     */
    public BindFrom getBindFrom() {
        return bindFrom;
    }

    /**
     * Sets the bind from.
     *
     * @param bindFrom
     *         the new bind from
     */
    public void setBindFrom( BindFrom bindFrom ) {
        this.bindFrom = bindFrom;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets the required.
     *
     * @return the required
     */
    public String getRequired() {
        return required;
    }

    /**
     * Sets the required.
     *
     * @param required
     *         the new required
     */
    public void setRequired( String required ) {
        this.required = required;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * Checks if is multiple.
     *
     * @return true, if is multiple
     */
    public boolean isMultiple() {
        return multiple;
    }

    /**
     * Sets the multiple.
     *
     * @param multiple
     *         the new multiple
     */
    public void setMultiple( boolean multiple ) {
        this.multiple = multiple;
    }

    /**
     * Gets the options.
     *
     * @return the options
     */
    public List< Object > getOptions() {
        return options;
    }

    /**
     * Sets the options.
     *
     * @param options
     *         the new options
     */
    public void setOptions( List< Object > options ) {
        this.options = options;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *         the new value
     */
    public void setValue( Object value ) {
        this.value = value;
    }

    /**
     * Gets the static or random option.
     *
     * @return the static or random option
     */
    public String getStaticOrRandomOption() {
        return staticOrRandomOption;
    }

    /**
     * Sets the static or random option.
     *
     * @param staticOrRandomOption
     *         the new static or random option
     */
    public void setStaticOrRandomOption( String staticOrRandomOption ) {
        this.staticOrRandomOption = staticOrRandomOption;
    }

    public static WorkFlowAdditionalAttributeDTO prepareWorkFlowAdditionalAttributeDTOFromDto( WorkFlowAdditionalAttributeDTO dto ) {
        WorkFlowAdditionalAttributeDTO existingAtrib = new WorkFlowAdditionalAttributeDTO();
        existingAtrib.setType( dto.getType() );
        existingAtrib.setOptions( dto.getOptions() );
        existingAtrib.setValue( dto.getValue() );
        existingAtrib.setName( dto.getName() );
        existingAtrib.setLabel( dto.getLabel() );
        existingAtrib.setStaticOrRandomOption( dto.getStaticOrRandomOption() );
        existingAtrib.setMultiple( dto.isMultiple() );
        existingAtrib.setRules( dto.getRules() );
        existingAtrib.setBindFrom( dto.getBindFrom() );
        existingAtrib.setHelp( dto.getHelp() );
        existingAtrib.setMessages( dto.getMessages() );
        existingAtrib.setId( dto.getId() );
        existingAtrib.setConvert( dto.getConvert() );
        return existingAtrib;
    }

}
