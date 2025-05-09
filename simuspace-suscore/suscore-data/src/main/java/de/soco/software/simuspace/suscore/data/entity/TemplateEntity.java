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
 * The Class TemplateEntity.
 *
 * @author Fahad Rafi
 */
@Getter
@Setter
@Entity
@org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
@Table( name = "template" )
public class TemplateEntity {

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
    @Column( name = "line_number" )
    private String lineNumber;

    /**
     * The line match.
     */
    @Column( name = "column_start" )
    private String start;

    /**
     * The line offset.
     */
    @Column( name = "column_end" )
    private String end;

    /**
     * The variable regex.
     */
    @Column( name = "scanned_value" )
    private String match;

    /**
     * The selection id.
     */
    @Column( name = "job_workflow_id", nullable = false )
    @Type( type = "uuid-char" )
    private UUID selectionId;

    /**
     * Copy.
     *
     * @return the template entity
     */
    public TemplateEntity copy() {
        TemplateEntity copy = new TemplateEntity();
        copy.setId( UUID.randomUUID() );
        copy.setSelectionId( getSelectionId() );
        copy.setStart( getStart() );
        copy.setEnd( getEnd() );
        copy.setVariableName( getVariableName() );
        copy.setMatch( getMatch() );
        copy.setLineNumber( getLineNumber() );
        return copy;
    }

}
