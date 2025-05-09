package de.soco.software.simuspace.server.model.jsonschema;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties( ignoreUnknown = true )
public class SMJSONSchema {

    Object title;

    String description;

    Object type = "object";

    Map< String, SMJSONSchema > properties;

    Boolean additionalProperties;

    List< String > required;

    List< SMSnippet > defaultSnippets;

    Map< String, SMJSONSchema > $defs;

    @JsonProperty( "enum" )
    List< String > enum_;

    String pattern;

    @JsonProperty( "default" )
    Object default_;

    String markdownDescription;

    Integer minItems;

    Integer maxItems;

    Integer minLength;

    Integer maxLength;

    Double multipleOf;

    Double minimum;

    Double maximum;

    Double exclusiveMinimum;

    Double exclusiveMaximum;

    Object items;

    List< Map< String, String > > anyOf;

    List< Map< String, String > > allOf;

    List< Map< String, String > > oneOf;

    String $ref;

    Object examples;

    public Object getTitle() {
        return title;
    }

    public void setTitle( Object title ) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Object getType() {
        return type;
    }

    public void setType( Object type ) {
        this.type = type;
    }

    public Map< String, SMJSONSchema > getProperties() {
        return properties;
    }

    public void setProperties( Map< String, SMJSONSchema > properties ) {
        this.properties = properties;
    }

    public Boolean getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties( Boolean additionalProperties ) {
        this.additionalProperties = additionalProperties;
    }

    public List< String > getRequired() {
        return required;
    }

    public void setRequired( List< String > required ) {
        this.required = required;
    }

    public List< SMSnippet > getDefaultSnippets() {
        return defaultSnippets;
    }

    public void setDefaultSnippets( List< SMSnippet > defaultSnippets ) {
        this.defaultSnippets = defaultSnippets;
    }

    public Map< String, SMJSONSchema > getDefinitions() {
        return $defs;
    }

    public void setDefinitions( Map< String, SMJSONSchema > $defs ) {
        this.$defs = $defs;
    }

    public List< String > getEnum_() {
        return enum_;
    }

    public void setEnum_( List< String > enum_ ) {
        this.enum_ = enum_;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern( String pattern ) {
        this.pattern = pattern;
    }

    public Object getDefault_() {
        return default_;
    }

    public void setDefault_( Object default_ ) {
        this.default_ = default_;
    }

    public String getMarkdownDescription() {
        return markdownDescription;
    }

    public void setMarkdownDescription( String markdownDescription ) {
        this.markdownDescription = markdownDescription;
    }

    public Integer getMinItems() {
        return minItems;
    }

    public void setMinItems( Integer minItems ) {
        this.minItems = minItems;
    }

    public Integer getMaxItems() {
        return maxItems;
    }

    public void setMaxItems( Integer maxItems ) {
        this.maxItems = maxItems;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength( Integer minLength ) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength( Integer maxLength ) {
        this.maxLength = maxLength;
    }

    public Double getMultipleOf() {
        return multipleOf;
    }

    public void setMultipleOf( Double multipleOf ) {
        this.multipleOf = multipleOf;
    }

    public Double getMinimum() {
        return minimum;
    }

    public void setMinimum( Double minimum ) {
        this.minimum = minimum;
    }

    public Double getMaximum() {
        return maximum;
    }

    public void setMaximum( Double maximum ) {
        this.maximum = maximum;
    }

    public Object getItems() {
        return items;
    }

    public void setItems( Object items ) {
        this.items = items;
    }

    public List< Map< String, String > > getAnyOf() {
        return anyOf;
    }

    public void setAnyOf( List< Map< String, String > > anyOf ) {
        this.anyOf = anyOf;
    }

    public List< Map< String, String > > getOneOf() {
        return oneOf;
    }

    public void setOneOf( List< Map< String, String > > oneOf ) {
        this.oneOf = oneOf;
    }

    public Double getExclusiveMinimum() {
        return exclusiveMinimum;
    }

    public void setExclusiveMinimum( Double exclusiveMinimum ) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    public Double getExclusiveMaximum() {
        return exclusiveMaximum;
    }

    public void setExclusiveMaximum( Double exclusiveMaximum ) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    public List< Map< String, String > > getAllOf() {
        return allOf;
    }

    public void setAllOf( List< Map< String, String > > allOf ) {
        this.allOf = allOf;
    }

    public String get$ref() {
        return $ref;
    }

    public void set$ref( String $ref ) {
        this.$ref = $ref;
    }

    public Object getExamples() {
        return examples;
    }

    public void setExamples( Object examples ) {
        this.examples = examples;
    }

}