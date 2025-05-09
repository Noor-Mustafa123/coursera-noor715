package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;

/**
 * The type Design variable label entity.
 */
@Getter
@Setter
@Entity
@Table( name = "design_variable_label" )
public class DesignVariableLabelEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 4467214477422598247L;

    /**
     * The id.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The Expression.
     */
    @Column( name = "design_variable_expression" )
    private String expression;

    /**
     * workflow can have custom attributes.
     */
    @ManyToOne( optional = false, fetch = FetchType.EAGER )
    @JoinColumns( value = { @JoinColumn( name = "workflow_id", referencedColumnName = "id" ),
            @JoinColumn( name = "workflow_version_id", referencedColumnName = "version_id" ) } )
    private WorkflowEntity workflow;

}
