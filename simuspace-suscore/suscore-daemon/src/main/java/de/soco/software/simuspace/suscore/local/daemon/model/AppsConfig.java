package de.soco.software.simuspace.suscore.local.daemon.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class AppsConfig {

    /**
     * The shell.
     */
    private Map< String, Map< String, String > > shell;

    /**
     * Gets the Application names For Extension.
     *
     * @return the application names
     */
    public Set< String > getApplicationsForExtension( String fileExtension ) {
        Set< String > names = new HashSet<>();
        Map< String, String > apps = new HashMap<>();
        if ( null != fileExtension && !fileExtension.isEmpty() ) {
            for ( Map.Entry< String, Map< String, String > > entry : getShell().entrySet() ) {
                if ( entry.getKey().contains( fileExtension ) ) {
                    apps = entry.getValue();
                    break;
                }
            }
            for ( Map.Entry< String, String > entry : apps.entrySet() ) {
                names.add( entry.getKey() );
            }
        }
        return names;
    }

    /**
     * Gets the command for applications.
     *
     * @return the applications
     */
    public String getCommandForApplication( String application, String fileExtension ) {
        String command = "";
        Map< String, String > apps = new HashMap<>();

        for ( Map.Entry< String, Map< String, String > > entry : getShell().entrySet() ) {
            if ( entry.getKey().contains( fileExtension ) ) {
                apps = entry.getValue();
                break;
            }
        }

        for ( Map.Entry< String, String > entry : apps.entrySet() ) {
            if ( entry.getKey().equalsIgnoreCase( application ) ) {
                command = entry.getValue();
                break;
            }
        }

        return command;
    }

    /**
     * Gets the command for file extension.
     *
     * @return the applications
     */
    public String getCommandForFileExtension( String fileExtension ) {
        Map< String, String > apps = new HashMap<>();

        for ( Map.Entry< String, Map< String, String > > entry : getShell().entrySet() ) {
            if ( entry.getKey().contains( fileExtension ) ) {
                apps = entry.getValue();
                break;
            }
        }

        return apps.get( apps.keySet().toArray()[ 0 ] ); // returns the first app it finds since application is not provided
    }

    /**
     * Gets the shell.
     *
     * @return the shell
     */
    public Map< String, Map< String, String > > getShell() {
        return shell;
    }

    /**
     * Sets the shell.
     *
     * @param shell
     *         the shell
     */
    public void setShell( Map< String, Map< String, String > > shell ) {
        this.shell = shell;
    }

}
