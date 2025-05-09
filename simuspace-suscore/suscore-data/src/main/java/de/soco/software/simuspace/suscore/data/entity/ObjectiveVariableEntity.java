package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class ObjectiveVariableEntity.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class ObjectiveVariableEntity extends VariableEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 3261136100951192712L;

    /**
     * The max.
     */
    private String goal;

    /**
     * The options.
     */
    private String options;

    /**
     * Instantiates a new objective variable entity.
     *
     * @param label
     *         the label
     * @param name
     *         the name
     * @param nominalValue
     *         the nominal value
     */
    public ObjectiveVariableEntity( String label, String name, String nominalValue ) {
        super( label, name, nominalValue );
    }

}
