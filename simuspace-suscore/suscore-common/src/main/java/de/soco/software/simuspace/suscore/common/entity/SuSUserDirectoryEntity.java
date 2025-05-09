package de.soco.software.simuspace.suscore.common.entity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import java.io.Serial;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The entity class for user directory to persist in repository.
 *
 * @author M.Nasir.Farooq
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class SuSUserDirectoryEntity extends SystemEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Name of ldap configuration.
     */
    @Column( name = "name" )
    private String name;

    /**
     * Description of configuration.
     */
    @Column( name = "description" )
    private String description;

    /**
     * Status of config (1=ACTIVE , 0=DEACTIVE).
     */
    @Column( name = "status" )
    private Boolean status;

    /**
     * Directory Type (0=SIMSPACE_INTERNAL_DIRECTORY ,1=LDAP_DIRECTORY, 2=ACTIVE_DIRECTORY, 3=MACHINE_USER_DIRECTORY ).
     */
    @Column( name = "type", nullable = false )
    private String type;

    /**
     * The list of users relating to this directory.
     */
    @OneToMany( fetch = FetchType.EAGER, mappedBy = "directory" )
    private List< UserEntity > users;

    @OneToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "user_directory_attribute_id" )
    private UserDirectoryAttributeEntity userDirectoryAttribute;

}