package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import lombok.Getter;
import lombok.Setter;

/**
 * A mapping class for the data object related DML operations.
 *
 * @author fahad
 */
@Getter
@Setter
@Entity
@Indexed( index = "DataObjectEntity" )
public class DataObjectEntity extends SuSEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The Constant CLASS_ID.
     */
    public static final UUID CLASS_ID = UUID.fromString( "1bc384cb-5aa3-449e-8798-c158942a703c" );

    /**
     * The file.
     */
    @OneToOne
    @JoinColumns( @JoinColumn( name = "document_id", referencedColumnName = "id" ) )
    DocumentEntity file;

    /**
     * The checked out.
     */
    @Column( name = "checked_out" )
    private Boolean checkedOut = false;

    /**
     * The checked out user.
     */
    @OneToOne
    @JoinColumn( name = "checked_out_user", referencedColumnName = "id" )
    private UserEntity checkedOutUser;

    /**
     * The attachments.
     */
    @Column( unique = false )
    @ManyToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JoinTable( name = "dataobject_attachments" )
    private Set< DocumentEntity > attachments = new HashSet<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( checkedOut == null ) ? 0 : checkedOut.hashCode() );
        result = prime * result + ( ( checkedOutUser == null ) ? 0 : checkedOutUser.hashCode() );
        result = prime * result + ( ( file == null ) ? 0 : file.hashCode() );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( !super.equals( obj ) ) {
            return false;
        }
        if ( !( obj instanceof DataObjectEntity ) ) {
            return false;
        }
        DataObjectEntity other = ( DataObjectEntity ) obj;
        if ( checkedOut == null ) {
            if ( other.checkedOut != null ) {
                return false;
            }
        } else if ( !checkedOut.equals( other.checkedOut ) ) {
            return false;
        }
        if ( checkedOutUser == null ) {
            if ( other.checkedOutUser != null ) {
                return false;
            }
        } else if ( !checkedOutUser.equals( other.checkedOutUser ) ) {
            return false;
        }
        if ( file == null ) {
            if ( other.file != null ) {
                return false;
            }
        } else if ( !file.equals( other.file ) ) {
            return false;
        }
        return true;
    }

}
