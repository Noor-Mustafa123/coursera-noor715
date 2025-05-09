package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class CustomVariableEntity.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class CustomVariableEntity extends VariableEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 7108535546403050816L;

    /**
     * The type.
     */
    private String type;

    /**
     * The Element name.
     */
    private String elementName;

    /**
     * The Field name.
     */
    private String fieldName;

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
    public CustomVariableEntity( String label, String name, String type, String nominalValue ) {
        super( label, name, nominalValue );
        this.type = type;
    }

}
