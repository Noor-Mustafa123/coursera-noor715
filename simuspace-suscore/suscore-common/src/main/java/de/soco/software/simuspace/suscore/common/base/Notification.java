package de.soco.software.simuspace.suscore.common.base;

import java.util.ArrayList;
import java.util.List;

/**
 * An object of this class collects together information about errors and other information to be shown to user. The List is usually
 * augmented when any error occurs during validations or other run time errors.
 *
 * @author Aroosa.Bukhari
 */
public class Notification {

    /**
     * The List of errors which occur during run time.
     */
    private List< Error > errors = new ArrayList<>();

    /**
     * The List of info which occur during run time.
     */
    private List< Info > infos = new ArrayList<>();

    /**
     * Adds an error in the list of errors.
     *
     * @param error
     *         An error object with contains the message of error
     *
     * @return the list of errors
     */
    public List< Error > addError( Error error ) {
        errors.add( error );
        return errors;
    }

    /**
     * Adds an info in the list of infos.
     *
     * @param info
     *         An error object with contains the message of error
     *
     * @return the list of errors
     */
    public List< Info > addInfo( Info info ) {
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
    public List< Error > addErrors( List< Error > errorList ) {
        errors.addAll( errorList );
        return errors;
    }

    /**
     * Adds infos in the list of infos.
     *
     * @param errorList
     *         An error object with contains the message of error
     *
     * @return the list of errors
     */
    public List< Info > addInfos( List< Info > errorList ) {
        infos.addAll( errorList );
        return infos;
    }

    /**
     * Adds the notification.
     *
     * @param notif
     *         the notif
     */
    public void addNotification( Notification notif ) {
        errors.addAll( notif.getErrors() );
    }

    /**
     * Gets the error list in Notification.
     *
     * @return the list of errors
     */

    public List< Error > getErrors() {
        return errors;
    }

    /**
     * Gets the error list in Notification.
     *
     * @return the list of errors
     */

    public List< String > getMessages() {
        final List< String > messages = new ArrayList<>();

        for ( final Error error : errors ) {
            messages.add( error.getMessage() );
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
        return !errors.isEmpty();
    }

    /**
     * Sets the list of errors.
     *
     * @param errors
     *         the new errors
     */
    public void setErrors( List< Error > errors ) {
        this.errors = errors;
    }

    /**
     * Gets the infos.
     *
     * @return the infos
     */
    public List< Info > getInfos() {
        return infos;
    }

}
