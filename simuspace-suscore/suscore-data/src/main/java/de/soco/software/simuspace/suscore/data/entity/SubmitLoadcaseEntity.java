package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class SubmitLoadcaseEntity. *
 *
 * @author noman arshad
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table( name = "submit_loadcase" )
public class SubmitLoadcaseEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 6436900947023507732L;

    /**
     * The id.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The json schema.
     */
    @Column( name = "loadcase_list_json", length = 10485760 )
    private String loadcaseListjson;

    /**
     * The job completed.
     */
    @Column( name = "job_completed" )
    Boolean jobCompleted;

    /**
     * * Instantiates a new submit loadcase entity. * * @param loadcaseListjson * the loadcase listjson * @param jobCompleted * the job
     * completed
     */
    public SubmitLoadcaseEntity( String loadcaseListjson, Boolean jobCompleted ) {
        super();
        this.loadcaseListjson = loadcaseListjson;
        this.jobCompleted = jobCompleted;
    }

    public SubmitLoadcaseEntity( UUID id, String loadcaseListjson, Boolean jobCompleted ) {
        super();
        this.loadcaseListjson = loadcaseListjson;
        this.jobCompleted = jobCompleted;
        this.id = id;
    }

}