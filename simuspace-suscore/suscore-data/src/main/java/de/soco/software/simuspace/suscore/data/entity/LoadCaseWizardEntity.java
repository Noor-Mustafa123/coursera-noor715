package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class LoadCaseEntity.
 */
@Getter
@Setter
@Entity
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
@Table( name = "loadcase_wizard" )
public class LoadCaseWizardEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -4862123666880860155L;

    /**
     * The Id.
     */
    @Column( name = "id" )
    @Type( type = "uuid-char" )
    @Id
    private UUID id;

    /**
     * The Assembly selection id.
     */
    @Column( name = "assembly_selection" )
    @Type( type = "uuid-char" )
    private UUID assemblySelectionId;

    /**
     * The Solver selection id.
     */
    @Column( name = "solver_selection" )
    @Type( type = "uuid-char" )
    private UUID solverSelectionId;

    /**
     * The Post selection id.
     */
    @Column( name = "post_selection" )
    @Type( type = "uuid-char" )
    private UUID postSelectionId;

}
