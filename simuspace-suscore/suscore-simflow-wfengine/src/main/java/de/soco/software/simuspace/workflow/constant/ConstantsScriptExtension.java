package de.soco.software.simuspace.workflow.constant;

/**
 * Utility class which will contain all the constants for Script Extensions
 *
 * @author Zeeshan Jamal
 */
public class ConstantsScriptExtension {

    /**
     * The Constant BAT.
     */
    public static final String BAT = ".bat";

    /**
     * The Constant SH.
     */
    public static final String SH = ".sh";

    /**
     * The shell script indication.
     */
    public static final String SHELL_SCRIPT_INDICATION = "#!/bin/bash";

    /**
     * The cmd script indication.
     */
    public static final String CMD_SCRIPT_INDICATION = "@echo off";

    /**
     * The WIN_ENCODER support to special characters.
     */
    public static final String WIN_ENCODER = "CHCP 1252";

    /**
     * The Constant PY.
     */
    public static final String PY = ".py";

    /**
     * The Constant PYTHON_SCRIPT_INDICATION.
     */
    public static final String PYTHON_SCRIPT_INDICATION = "# -*- coding: utf-8 -*-";

    /**
     * Private constructor to avoid instantiation.
     */
    private ConstantsScriptExtension() {
        // Private constructor to hide the implicit public one.
    }

}
