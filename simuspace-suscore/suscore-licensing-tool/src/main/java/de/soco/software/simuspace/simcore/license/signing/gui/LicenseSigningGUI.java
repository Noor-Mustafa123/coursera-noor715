/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.simcore.license.signing.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.soco.software.simuspace.simcore.license.signing.model.LicenseDTO;
import de.soco.software.simuspace.simcore.license.signing.util.LicenseUtil;

/**
 * The Class responsible to provide graphic user interface for user to select the template license json and private key to sign the
 * license.
 *
 * @author M.Nasir.Farooq
 */
public class LicenseSigningGUI extends JFrame {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 453243424237758907L;

    private static final String EXCEPTION_POPUP_MESSAGE_PRE_FIX = "Exception: ";

    /**
     * The Constant FILE_SAVED_MESSAGE.
     */
    private static final String FILE_SAVED_MESSAGE = "File Saved at ";

    /**
     * The j frame.
     */
    JFrame jFrame;

    /**
     * The j panel.
     */
    JPanel jPanel;

    /**
     * The txt private key file.
     */
    private TextField txtLicenseTemplate;

    private TextField txtPrivateKeyFile;

    /**
     * The filebytes.
     */
    byte[] filebytes = null;

    /**
     * Show GUI.
     */
    public void showGUI() {

        jFrame = new JFrame();
        jPanel = new JPanel();
        jPanel.setLayout( null );

        /**
         * First Row
         */
        JLabel lblLicenseTemplate = new JLabel( "Select License JSON" );
        lblLicenseTemplate.setBounds( 10, 10, 190, 25 );

        txtLicenseTemplate = new TextField( 20 );
        txtLicenseTemplate.setBounds( 200, 10, 190, 26 );

        JButton btnBrowseLicenseTemplate = new JButton( "Browse" );
        btnBrowseLicenseTemplate.setBounds( 400, 10, 80, 28 );

        /**
         * Second Row
         */
        JLabel lblPrivateKeyFile = new JLabel( "Select PrivateKey File" );
        lblPrivateKeyFile.setBounds( 10, 40, 190, 25 );

        txtPrivateKeyFile = new TextField( 20 );
        txtPrivateKeyFile.setBounds( 200, 40, 190, 26 );

        JButton btnBrowsePrivateKeyFile = new JButton( "Browse" );
        btnBrowsePrivateKeyFile.setBounds( 400, 40, 80, 28 );

        /**
         * 3rd Row
         */
        JButton btnSignLicense = new JButton( "  Sign  " );
        btnSignLicense.setBounds( 200, 80, 80, 28 );

        JButton btnClose = new JButton( "Close" );
        btnClose.setBounds( 300, 80, 80, 28 );

        /**
         * Add all components in JPanel
         */
        jPanel.add( lblLicenseTemplate );
        jPanel.add( txtLicenseTemplate );
        jPanel.add( btnBrowseLicenseTemplate );
        jPanel.add( lblPrivateKeyFile );
        jPanel.add( txtPrivateKeyFile );
        jPanel.add( btnBrowsePrivateKeyFile );
        jPanel.add( btnSignLicense );
        jPanel.add( btnClose );

        jPanel.setSize( 500, 250 );
        jFrame.add( jPanel );

        /**
         * Set JFrame properties
         */
        jFrame.setLocation( 750, 300 );
        jFrame.pack();
        jFrame.setSize( 500, 250 );
        jFrame.setTitle( "License Generator" );
        jFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        jFrame.setVisible( true );

        btnBrowseLicenseTemplate.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent arg0 ) {

                openForLicense();

            }
        } );
        btnBrowsePrivateKeyFile.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent arg0 ) {

                openForPfx();

            }
        } );
        btnSignLicense.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent arg0 ) {
                try {
                    LicenseDTO signedLicense = LicenseUtil.signLicense( txtLicenseTemplate.getText(), txtPrivateKeyFile.getText() );
                    String strSignedLicense = LicenseUtil.toJson( signedLicense, LicenseDTO.class );
                    save( strSignedLicense.getBytes() );
                } catch ( Exception e ) {
                    JOptionPane.showMessageDialog( null, EXCEPTION_POPUP_MESSAGE_PRE_FIX + e.getMessage() );
                }
            }
        } );
        btnClose.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent arg0 ) {
                System.exit( 0 );// NOSONAR
            }
        } );
    }

    /**
     * Open for license.
     */
    private void openForLicense() {
        JFileChooser chooser = new JFileChooser();
        int ret = chooser.showOpenDialog( null );
        if ( ret != JFileChooser.CANCEL_OPTION ) {
            File file = chooser.getSelectedFile();
            txtLicenseTemplate.setText( file.getAbsolutePath() );
        }
    }

    /**
     * Open for pfx.
     */
    private void openForPfx() {
        JFileChooser chooser = new JFileChooser();
        int ret = chooser.showOpenDialog( null );
        if ( ret != JFileChooser.CANCEL_OPTION ) {
            File file = chooser.getSelectedFile();
            txtPrivateKeyFile.setText( file.getAbsolutePath() );
        }
    }

    /**
     * Saves the bytes to the chosen path.
     *
     * @param signedXmlDocument
     *         the a signed xml document
     *
     * @throws IOException
     */
    private void save( byte[] signedXmlDocument ) throws IOException {
        JFileChooser chooser = new JFileChooser();
        int ret = chooser.showSaveDialog( null );
        if ( ret != JFileChooser.CANCEL_OPTION ) {
            File file = chooser.getSelectedFile();
            try ( FileOutputStream fos = new FileOutputStream( file ) ) {
                fos.write( signedXmlDocument );
                JOptionPane.showMessageDialog( null, FILE_SAVED_MESSAGE + file.getAbsolutePath() );
            }
        }
    }

}