package de.soco.software.simuspace.workflow.constant;

/**
 * Utility class which will contains all the work flow engine Constants.
 *
 * NOTE: Please create related interface or enum for every constant enter or use already created interfaces which best accommodates you.
 *
 * @author Ali Haider
 */
public class ConstantsWFE {

    /**
     * The Interface CommandExecutionConst.
     */
    public interface CommandExecutionConst {

        /**
         * The Constant NIX_C_FLAG.
         */
        String NIX_C_FLAG = "-c";

        /**
         * The Constant NIX_COMMAND.
         */
        String NIX_COMMAND = "/bin/sh";

        /**
         * The Constant WIN_C_FLAG.
         */
        String WIN_C_FLAG = "/c";

        /**
         * The Constant WIN_COMMAND.
         */
        String WIN_COMMAND = "cmd.exe";

    }

    /**
     * The Interface StringConst.
     */
    public interface StringConst {

        /**
         * The separation between variable portions.
         */
        String SEPARATION_BETWEEN_VARIABLE_PORTIONS = ".";

        /**
         * The variable key leading braces.
         */
        String VARIABLE_KEY_LEADING_BRACES = "{{";

        /**
         * The variable key trailing braces.
         */
        String VARIABLE_KEY_TRAILING_BRACES = "}}";

    }

}
