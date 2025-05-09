package de.soco.software.simuspace.suscore.common.base;

import java.util.ArrayList;
import java.util.List;

/**
 * An object of this class collects together information about errors and other information to be shown to user. The List is usually
 * augmented when any error occurs during validations or other run time errors.
 *
 * @author M.Nasir.Farooq
 */
public class TransferResult {

    /**
     * The List of errors which occur during run time.
     */
    private List< String > errors = new ArrayList<>();

    /**
     * The List of info which occur during run time.
     */
    private List< String > infos = new ArrayList<>();

    /**
     * The transfered objects.
     */
    private List< Object > transferedObjects = new ArrayList<>();

    /**
     * Adds an error in the list of errors.
     *
     * @param error
     *         An error object with contains the message of error
     *
     * @return the list of errors
     */
    public List< String > addError( String error ) {
        errors.add( error );
        return errors;
    }

    /**
     * Adds the object.
     *
     * @param object
     *         the object
     *
     * @return the list
     */
    public List< Object > addObject( Object object ) {
        transferedObjects.add( object );
        return transferedObjects;
    }

    /**
     * Adds an info in the list of infos.
     *
     * @param info
     *         An error object with contains the message of error
     *
     * @return the list of errors
     */
    public List< String > addInfo( String info ) {
        infos.add( info );
        return infos;
    }

    /**
     * Adds errors in the list of errors.
     *
     * @param errorList
     *         An error object with contains the message of error
     *
     * @return the list of errors
     */
    public List< String > addErrors( List< String > errorList ) {
        errors.addAll( errorList );
        return errors;
    }

    /**
     * Adds infos in the list of infos.
     *
     * @param infosList
     *         An error object with contains the message of error
     *
     * @return the list of errors
     */
    public List< String > addInfos( List< String > infosList ) {
        infos.addAll( infosList );
        return infos;
    }

    /**
     * Gets the error list in Notification.
     *
     * @return the list of errors
     */

    public List< String > getErrors() {
        return errors;
    }

    /**
     * Gets the error list in Notification.
     *
     * @return the list of errors
     */

    public List< String > getMessages() {
        final List< String > messages = new ArrayList<>();

        for ( final String error : errors ) {
            messages.add( error );
        }

        return messages;
    }

    /**
     * Checks if Notification has some errors or the list is empty.
     *
     * @return boolean
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * Checks if Notification has some infos or the list is empty.
     *
     * @return boolean
     */
    public boolean hasInfos() {
        return !infos.isEmpty();
    }

    /**
     * Sets the list of errors.
     *
     * @param errors
     *         the new errors
     */
    public void setErrors( List< String > errors ) {
        this.errors = errors;
    }

    /**
     * Gets the infos.
     *
     * @return the infos
     */
    public List< String > getInfos() {
        return infos;
    }

    public List< Object > getTransferedObjects() {
        return transferedObjects;
    }

    public void setTransferedObjects( List< Object > transferedObjects ) {
        this.transferedObjects = transferedObjects;
    }

    public void setInfos( List< String > infos ) {
        this.infos = infos;
    }

}
