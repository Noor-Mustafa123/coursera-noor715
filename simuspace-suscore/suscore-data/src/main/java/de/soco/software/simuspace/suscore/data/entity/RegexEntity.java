package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.UUID;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class RegexEntity.
 */
@Getter
@Setter
@Entity
@org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
@Table( name = "regex" )
public class RegexEntity {

    /**
     * The id.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The variable name.
     */
    @Column( name = "variable_name" )
    private String variableName;

    /**
     * The line regex.
     */
    @Column( name = "line_regex" )
    private String lineRegex;

    /**
     * The line match.
     */
    @Column( name = "line_match" )
    private String lineMatch;

    /**
     * The line offset.
     */
    @Column( name = "line_offset" )
    private String lineOffset;

    /**
     * The variable regex.
     */
    @Column( name = "variable_regex" )
    private String variableRegex;

    /**
     * The variable match.
     */
    @Column( name = "variable_match" )
    private String variableMatch;

    /**
     * The variable group.
     */
    @Column( name = "variable_group" )
    private String variableGroup;

    /**
     * The regex form json.
     */
    @Column( name = "regex_form_json" )
    private String regexFormJson;

    /**
     * The selection id.
     */
    @Column( name = "job_workflow_id", nullable = false )
    @Type( type = "uuid-char" )
    private UUID selectionId;

    /**
     * The scanned line.
     */
    @Column( name = "scanned_line" )
    private String scannedLine;

    /**
     * The scanned value.
     */
    @Column( name = "scanned_value" )
    private String scannedValue;

    /**
     * The start.
     */
    @Column( name = "scanned_start" )
    private String start;

    /**
     * The end.
     */
    @Column( name = "scanned_end" )
    private String end;

    /**
     * Copy.
     *
     * @return the regex entity
     */
    public RegexEntity copy() {
        RegexEntity copy = new RegexEntity();
        copy.setId( UUID.randomUUID() );
        copy.setStart( getStart() );
        copy.setEnd( getEnd() );
        copy.setLineRegex( getLineRegex() );
        copy.setLineMatch( getLineMatch() );
        copy.setLineOffset( getLineOffset() );
        copy.setScannedLine( getScannedLine() );
        copy.setScannedValue( getScannedValue() );
        copy.setVariableRegex( getVariableRegex() );
        copy.setVariableName( getVariableName() );
        copy.setVariableGroup( getVariableGroup() );
        copy.setVariableMatch( getVariableMatch() );
        copy.setRegexFormJson( getRegexFormJson() );
        copy.setSelectionId( getSelectionId() );
        return copy;
    }

}
