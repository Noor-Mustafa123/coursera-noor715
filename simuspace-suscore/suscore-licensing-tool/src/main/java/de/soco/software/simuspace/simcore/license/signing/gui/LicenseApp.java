/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.simcore.license.signing.gui;

import javax.swing.*;

/**
 * The Class responsible to launch the license signing tool.
 *
 * @author M.Nasir.Farooq
 */
public class LicenseApp {

    /**
     * The main method.
     *
     * @param args
     *         the arguments
     *
     * @throws UnsupportedLookAndFeelException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public static void main( String[] args )
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );

        LicenseSigningGUI gui = new LicenseSigningGUI();
        gui.showGUI();

    }

}
