package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;

import java.io.Serial;
import java.util.UUID;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import lombok.Getter;
import lombok.Setter;

/**
 * Database Entity Mapping Class for project.
 *
 * @author noman arshad
 */
@Getter
@Setter
@Entity
@Indexed( index = "WorkflowProjectEntity" )
public class WorkflowProjectEntity extends ContainerEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The Constant CLASS_ID.
     */
    public static final UUID CLASS_ID = UUID.fromString( "3550a554-81ec-46b2-a01e-19d8a8ae14cd" );

    /**
     * The type.
     */
    private String type;

    /**
     * No-argument constructor.
     */
    public WorkflowProjectEntity() {
        super();
    }

    /**
     * Constructor to set ID.
     *
     * @param composedId
     *         the composed id
     */
    public WorkflowProjectEntity( VersionPrimaryKey composedId ) {
        super.setComposedId( composedId );
    }

}
