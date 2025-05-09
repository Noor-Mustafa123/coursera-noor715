package de.soco.software.simuspace.suscore.data.simflow.server.entity.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;

/**
 * Entity Class for job's globalVariable map
 *
 * @author Shahzeb Iqbal
 */
@Entity
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
@Table( name = "job_global_variables" )
public class JobGlobalVariablesEntity implements Serializable {

    /**
     * The id ( primary key )
     */
    @Id
    @Column( name = "id", nullable = false )
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The jobEntity
     */
    private UUID jobEntityId;

    /**
     * The globalVariables
     */
    @Column( name = "variables_content" )
    @Lob
    private byte[] globalVariables;

    /**
     * The userId
     */
    private UUID userId;

    /**
     * Parameterized Constructor
     *
     * @param id
     *         the id
     * @param jobEntityId
     *         the jobEntityId
     * @param globalVariables
     *         the globalVariables
     * @param userId
     *         the userId
     */
    public JobGlobalVariablesEntity( UUID id, UUID jobEntityId, byte[] globalVariables, UUID userId ) {
        this.id = id;
        this.jobEntityId = jobEntityId;
        this.globalVariables = globalVariables;
        this.userId = userId;
    }

    /**
     * The constructor
     */
    public JobGlobalVariablesEntity() {

    }

    /**
     * gets the id
     *
     * @return id
     */
    public UUID getId() {
        return id;
    }

    /**
     * sets the id
     *
     * @param id
     *         the id
     */
    public void setId( UUID id ) {
        this.id = id;
    }

    /**
     * gets the jobEntityId
     *
     * @return jobEntityId
     */
    public UUID getJobEntityId() {
        return jobEntityId;
    }

    /**
     * sets the JobEntityId
     *
     * @param jobEntityId
     *         the jobEntity
     */
    public void setJobEntityId( UUID jobEntityId ) {
        this.jobEntityId = jobEntityId;
    }

    /**
     * gets the globalVariables
     *
     * @return globalVariables
     */
    public byte[] getGlobalVariables() {
        return globalVariables;
    }

    /**
     * sets the globalVariables
     *
     * @param globalVariables
     *         the globalVariables
     */
    public void setGlobalVariables( byte[] globalVariables ) {
        this.globalVariables = globalVariables;
    }

    /**
     * gets the userId
     *
     * @return userId
     */
    public UUID getUserId() {
        return this.userId;
    }

    /**
     * sets the userId
     *
     * @param userId
     *         the userId
     */
    public void setUserId( UUID userId ) {
        this.userId = userId;
    }

}
