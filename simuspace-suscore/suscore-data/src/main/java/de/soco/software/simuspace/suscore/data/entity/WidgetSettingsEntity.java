package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.util.ByteUtil;

/**
 * The Class WidgetSettingsEntity.
 *
 * @author Ali Haider
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
@Table( name = "widgets" )
public class WidgetSettingsEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -690028958841139962L;

    /**
     * The id.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The function signature.
     */
    @Column( name = "func_signature" )
    private String functionSignature;

    /**
     * The user.
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "created_by", referencedColumnName = "id" )
    private UserEntity createdBy;

    /**
     * The view name.
     */
    @Column( name = "view_name" )
    private String viewName;

    @Column( name = "log" )
    @Lob
    private byte[] settings;

    /**
     * Creation date of object.
     */
    @Column( name = "created_on" )
    private Date createdOn;

    /**
     * modification date of object.
     */
    @Column( name = "updated_on" )
    private Date updatedOn;

    public WidgetSettingsEntity( String viewName, String settings ) {
        super();
        this.settings = ByteUtil.convertStringToByte( settings );
        this.viewName = viewName;

    }

    /**
     * Gets the settings.
     *
     * @return the settings
     */
    public String getSettings() {
        return ByteUtil.convertByteToString( this.settings );
    }

    /**
     * Sets the settings.
     *
     * @param settings
     *         the new settings
     */
    public void setSettings( String settings ) {
        this.settings = ByteUtil.convertStringToByte( settings );
    }

}
