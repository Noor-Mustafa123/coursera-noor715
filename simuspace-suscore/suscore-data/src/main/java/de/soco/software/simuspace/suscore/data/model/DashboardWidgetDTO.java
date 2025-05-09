package de.soco.software.simuspace.suscore.data.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.enums.DashboardEnums;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The type Dashboard widget dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties( value = { "source", "options" }, allowGetters = true, ignoreUnknown = true )
public class DashboardWidgetDTO {

    /**
     * The Id.
     */
    @Getter
    @Setter
    @UIFormField( name = "id", title = "3000021x4", type = "hidden", show = false )
    private String id;

    /**
     * The Id.
     */
    @Getter
    @Setter
    @UIFormField( name = "versionId", title = "3000261x4", type = "hidden", show = false )
    private Integer versionId;

    /**
     * The Type.
     */
    @Getter
    @Setter
    @UIFormField( name = "type", title = "3000240x4", type = "hidden", show = false, required = true )
    private String type;

    /**
     * The Relation.
     */
    @Getter
    @Setter
    @UIFormField( name = "type", title = "3000240x4", type = "hidden", show = false, required = true )
    private DashboardEnums.WIDGET_RELATION relation;

    /**
     * The Group id.
     */
    @Getter
    @Setter
    @UIFormField( name = "type", title = "3000240x4", type = "hidden", show = false, required = true )
    private String groupId;

    /**
     * The Source.
     */
    private WidgetSource source;

    /**
     * The Options.
     */
    private WidgetOptions options;

    /**
     * The Columns.
     */
    @Getter
    @Setter
    private List< FilterColumn > columns;

    /**
     * The Configuration.
     */
    @Getter
    @Setter
    @UIFormField( name = "config", title = "3000005x4", type = "hidden", section = "options" )
    private Map< String, Object > configuration;

    /**
     * Sets source.
     *
     * @param source
     *         the source
     */
    @JsonIgnore
    public void setSource( WidgetSource source ) {
        this.source = source;
    }

    /**
     * Sets options.
     *
     * @param options
     *         the options
     */
    @JsonIgnore
    public void setOptions( WidgetOptions options ) {
        this.options = options;
    }

    /**
     * Gets options.
     *
     * @return the options
     */
    @JsonProperty( "options" )
    public WidgetOptions getOptions() {
        return options;
    }

    /**
     * Gets source.
     *
     * @return the source
     */
    @JsonProperty( "source" )
    public WidgetSource getSource() {
        return source;
    }

}
