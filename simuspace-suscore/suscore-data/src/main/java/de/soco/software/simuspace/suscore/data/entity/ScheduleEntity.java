package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class ScheduleEntity.
 *
 * @author noman arshad
 */
@Getter
@Setter
@ToString
@Entity
@Table( name = "schedule_entity" )
public class ScheduleEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The primary key.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * Creation date of object.
     */
    @Column( name = "name" )
    private String name;

    /**
     * Creation date of object.
     */
    @Column( name = "process" )
    private String process;

    /**
     * Creation date of object.
     */
    @Column( name = "last_run" )
    private Date lastRun;

    /**
     * Creation date of object.
     */
    @Column( name = "next_run" )
    private Date nextRun;

    /**
     * isDeleted.
     */
    @Column( name = "is_delete" )
    private boolean isDelete;

    /**
     * Checks if is delete.
     *
     * @return true, if is delete
     */
    public boolean isDelete() {
        return isDelete;
    }

    /**
     * Sets the delete.
     *
     * @param isDelete
     *         the new delete
     */
    public void setDelete( boolean isDelete ) {
        this.isDelete = isDelete;
    }

    /**
     * Instantiates a new schedule entity.
     */
    public ScheduleEntity() {
        super();
    }

}
