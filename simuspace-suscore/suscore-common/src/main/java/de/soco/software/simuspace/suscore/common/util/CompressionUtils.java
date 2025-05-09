package de.soco.software.simuspace.suscore.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Base64;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.exceptions.SusException;

@Log4j2
public class CompressionUtils {

    public static byte[] serialize( Session session ) {
        try {
            return compress( toString( ( Serializable ) session ) );
        } catch ( Exception e ) {
            log.error( "serialize session error", e );
            throw new SusException( "serialize session error", e );
        }
    }

    private static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString( baos.toByteArray() );
    }

    public static Session deserialize( byte[] sessionStr ) {
        try {
            return getSimpleSessionFromString( decompress( sessionStr ) );
        } catch ( Exception e ) {
            log.error( "deserialize session error", e );
            throw new SusException( "deserialize session error", e );
        }
    }

    private static SimpleSession getSimpleSessionFromString( String s ) throws IOException, ClassNotFoundException {
        SimpleSession simpleSession = null;
        byte[] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream( new ByteArrayInputStream( data ) );
        simpleSession = ( SimpleSession ) ois.readObject();
        ois.close();
        return simpleSession;
    }

    public static byte[] compress( String text ) throws IOException {
        return compress( text.getBytes() );
    }

    public static byte[] compress( byte[] bArray ) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try ( DeflaterOutputStream dos = new DeflaterOutputStream( os ) ) {
            dos.write( bArray );
        }
        return os.toByteArray();
    }

    public static String decompress( byte[] compressedTxt ) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try ( OutputStream ios = new InflaterOutputStream( os ) ) {
            ios.write( compressedTxt );
        }
        return os.toString();
    }

    private CompressionUtils() {
    }

}
