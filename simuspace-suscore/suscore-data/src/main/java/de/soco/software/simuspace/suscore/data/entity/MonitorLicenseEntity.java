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

/**
 * The Class MonitorLicenseEntity.
 *
 * @author noman arshad
 * @since 2.0
 */
@Getter
@Setter
@Entity
@Table( name = "monitor_license" )
public class MonitorLicenseEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1902799186301124533L;

    /**
     * The id.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The token.
     */
    @Column( name = "token" )
    private String token;

    /**
     * The userId.
     */
    @Column( name = "userId" )
    private String userId;

    /**
     * The description.
     */
    @Column( name = "description" )
    private String description;

    /**
     * Creation date of object.
     */
    @Column( name = "created_on" )
    private Date createdOn;

    /**
     * The license module count.
     */
    @Column( name = "dataLicense" )
    private Integer dataLicense = 0;

    /**
     * The user license.
     */
    @Column( name = "userLicense" )
    private Integer userLicense = 0;

    /**
     * The workflow license.
     */
    @Column( name = "workflowLicense" )
    private Integer workflowLicense = 0;

    /**
     * The post license.
     */
    @Column( name = "postLicense" )
    private Integer postLicense = 0;

    /**
     * The assembly license.
     */
    @Column( name = "assemblyLicense" )
    private Integer assemblyLicense = 0;

    /**
     * The cb 2 license.
     */
    @Column( name = "cb2License" )
    private Integer cb2License = 0;

    /**
     * The doe license.
     */
    @Column( name = "doeLicense" )
    private Integer doeLicense = 0;

    /**
     * The optimization license.
     */
    @Column( name = "optimizationLicense" )
    private Integer optimizationLicense = 0;

}
