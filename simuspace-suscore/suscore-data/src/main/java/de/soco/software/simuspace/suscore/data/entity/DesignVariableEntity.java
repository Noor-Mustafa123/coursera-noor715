package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class DesignVariableEntity.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class DesignVariableEntity extends VariableEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -9128175320750428257L;

    /**
     * The type.
     */
    private String type;

    /**
     * The design variable min.
     */
    private String designVariableMin;

    /**
     * The design variable max.
     */
    private String designVariableMax;

    /**
     * The step.
     */
    private String step;

    /**
     * The values.
     */
    private String designVariableValues;

    /**
     * The optional.
     */
    private String optional;

    /**
     * The index.
     */
    private String indexNumber;

    /**
     * The algo type.
     */
    private String algoType;

    /**
     * The level.
     */
    @Column( name = "`level`" )
    private String level;

    /**
     * The start.
     */
    @Column( name = "`start`" )
    private String start;

    /**
     * The stop.
     */
    private String stop;

    /**
     * The interval.
     */
    private String interval;

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
    public DesignVariableEntity( String label, String name, String type, String nominalValue ) {
        super( label, name, nominalValue );
        this.setType( type );
    }

}
