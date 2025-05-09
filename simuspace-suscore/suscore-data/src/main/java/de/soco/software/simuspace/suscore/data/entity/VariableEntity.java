package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;

/**
 * The Class VariableEntity.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Inheritance( strategy = InheritanceType.SINGLE_TABLE )
@Table( name = "variables" )
@DiscriminatorColumn( name = "classname" )
public class VariableEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -9128175320750428257L;

    /**
     * The id.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The label.
     */
    private String label;

    /**
     * The name.
     */
    private String name;

    /**
     * The nominal value.
     */
    private String nominalValue;

    /**
     * workflow can have custom attributes.
     */
    @ManyToOne( optional = false, fetch = FetchType.EAGER )
    @JoinColumns( value = { @JoinColumn( name = "workflow_id", referencedColumnName = "id" ),
            @JoinColumn( name = "workflow_version_id", referencedColumnName = "version_id" ) } )
    private WorkflowEntity workflow;

    /**
     * The created on.
     */
    @Column( name = "created_on" )
    private Date createdOn;

    /**
     * The user id.
     */
    @Column( name = "userId" )
    private UUID userId;

    /**
     * Instantiates a new design variable entity.
     *
     * @param label
     *         the label
     * @param name
     *         the name
     * @param type
     *         the type
     * @param nominalValue
     *         the nominal value
     */
    public VariableEntity( String label, String name, String nominalValue ) {
        super();
        this.label = label;
        this.name = name;
        this.nominalValue = nominalValue;
    }

}
