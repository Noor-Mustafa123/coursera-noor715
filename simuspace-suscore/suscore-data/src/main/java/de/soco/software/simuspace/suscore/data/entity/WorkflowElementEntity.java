package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import java.io.Serial;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class WorkflowElementEntity.
 */
@Getter
@Setter
@Entity
public class WorkflowElementEntity extends WorkflowTemplateEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The executable on client.
     */
    @Column( name = "executable_onclient" )
    private String executableOnClient;

    /**
     * The executable on server.
     */
    @Column( name = "executable_onserver" )
    private String executableOnServer;

    /**
     * The executable on both.
     */
    @Column( name = "executable_onboth" )
    private String executableOnBoth;

}
