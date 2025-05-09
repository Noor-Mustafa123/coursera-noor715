package de.soco.software.simuspace.simcore.license.signing.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import de.soco.software.simuspace.simcore.license.signing.model.LicenseDTO;
import de.soco.software.simuspace.simcore.license.signing.util.LicenseUtil;

public class SignAll {

    public static void main( String[] args ) {
        String inputPath = "";
        inputPath = "C:\\Users\\Huzaifah\\Desktop\\2.0_License\\huzvm";
        File path = new File( inputPath );
        File[] matchingFiles = path.listFiles( new FilenameFilter() {

            public boolean accept( File dir, String name ) {
                return name.startsWith( "unsigned" ) && name.endsWith( "json" );
            }
        } );
        for ( File f : matchingFiles ) {
            File key = new File( inputPath, "simspace-license-private.key" );

            LicenseDTO signedLicense = LicenseUtil.signLicense( f.toString(), key.toString() );
            String strSignedLicense = LicenseUtil.toJson( signedLicense, LicenseDTO.class );

            File outFile = new File( inputPath, f.getName().replace( "unsigned", "signed" ) );
            try ( FileOutputStream fos = new FileOutputStream( outFile ) ) {
                fos.write( strSignedLicense.getBytes() );
            } catch ( FileNotFoundException e ) {
                e.printStackTrace();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }

    }

}
