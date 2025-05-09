package de.soco.software.simuspace.suscore.common.enums;

import java.util.Arrays;

/**
 * The Enum ExitCodesAndSignals.
 */
public enum ExitCodesAndSignals {

    /**
     * The success.
     */
    SUCCESS( 0, "Success", "Process exited normally" ),

    /**
     * The error.
     */
    ERROR( 1, "Error", "Process exited on error" ),

    /**
     * The sighup.
     */
    SIGHUP( 128 + 1, "SIGHUP",
            "If a process is being run from terminal and that terminal suddenly goes away then the process receives this signal." ),

    /**
     * The sigint.
     */
    SIGINT( 128 + 2, "SIGINT", "The process was 'interrupted'. This happens when you press Control+C on the controlling terminal." ),

    /**
     * The sigquit.
     */
    SIGQUIT( 128 + 3, "SIGQUIT", "" ),

    /**
     * The sigill.
     */
    SIGILL( 128 + 4, "SIGILL", "Illegal instruction. The program contained some machine code the CPU can't understand." ),

    /**
     * The sigtrap.
     */
    SIGTRAP( 128 + 5, "SIGTRAP", "This signal is used mainly from within debuggers and program tracers." ),

    /**
     * The sigabrt.
     */
    SIGABRT( 128 + 6, "SIGABRT", "The program called the abort() function. This is an emergency stop." ),

    /**
     * The sigbus.
     */
    SIGBUS( 128 + 7, "SIGBUS",
            "An attempt was made to access memory incorrectly. This can be caused by alignment errors in memory access etc." ),

    /**
     * The sigfpe.
     */
    SIGFPE( 128 + 8, "SIGFPE", "A floating point exception happened in the program." ),

    /**
     * The sigkill.
     */
    SIGKILL( 128 + 9, "SIGKILL", "The process was explicitly killed by somebody wielding the kill program." ),

    /**
     * The sigusr1.
     */
    SIGUSR1( 128 + 10, "SIGUSR1", "Left for the programmers to do whatever they want." ),

    /**
     * The sigsegv.
     */
    SIGSEGV( 128 + 11, "SIGSEGV",
            "An attempt was made to access memory not allocated to the process. This is often caused by reading off the end of arrays etc." ),

    /**
     * The sigusr2.
     */
    SIGUSR2( 128 + 12, "SIGUSR2", "Left for the programmers to do whatever they want." ),

    /**
     * The sigpipe.
     */
    SIGPIPE( 128 + 13, "SIGPIPE",
            "If a process is producing output that is being fed into another process that consume it via a pipe ('producer | consumer') and the consumer dies then the producer is sent this signal." ),

    /**
     * The sigalrm.
     */
    SIGALRM( 128 + 14, "SIGALRM",
            "A process can request a 'wake up call' from the operating system at some time in the future by calling the alarm() function. When that time comes round the wake-up call consists of this signal." ),

    /**
     * The sigterm.
     */
    SIGTERM( 128 + 15, "SIGTERM", "The process was explicitly killed by somebody wielding the kill program." ),

    /**
     * The unused16.
     */
    UNUSED16( 128 + 16, "UNUSED", "" ),

    /**
     * The sigchld.
     */
    SIGCHLD( 128 + 17, "SIGCHLD",
            "The process had previously created one or more child processes with the fork() function. One or more of these processes has since died." ),

    /**
     * The sigcont.
     */
    SIGCONT( 128 + 18, "SIGCONT", "" ),

    /**
     * The sigstop.
     */
    SIGSTOP( 128 + 19, "SIGSTOP", "" ),

    /**
     * The sigtstp.
     */
    SIGTSTP( 128 + 20, "SIGTSTP", "" ),

    /**
     * The sigttin.
     */
    SIGTTIN( 128 + 21, "SIGTTIN", "" ),

    /**
     * The sigttou.
     */
    SIGTTOU( 128 + 22, "SIGTTOU", "" ),

    /**
     * The sigurg.
     */
    SIGURG( 128 + 23, "SIGURG", "" ),

    /**
     * The sigxcpu.
     */
    SIGXCPU( 128 + 24, "SIGXCPU", "" ),

    /**
     * The sigxfsz.
     */
    SIGXFSZ( 128 + 25, "SIGXFSZ", "" ),

    /**
     * The sigvtalrm.
     */
    SIGVTALRM( 128 + 26, "SIGVTALRM", "" ),

    /**
     * The sigprof.
     */
    SIGPROF( 128 + 27, "SIGPROF", "" ),

    /**
     * The sigwinch.
     */
    SIGWINCH( 128 + 28, "SIGWINCH", "" ),

    /**
     * The sigio.
     */
    SIGIO( 128 + 29, "SIGIO", "" ),

    /**
     * The sigpwr.
     */
    SIGPWR( 128 + 30, "SIGPWR", "" ),

    /**
     * The sigsys.
     */
    SIGSYS( 128 + 31, "SIGSYS", "Unused." ),

    /**
     * The unknown.
     */
    UNKNOWN( -1, "Unknown", "Process exited with a code unknown to SIMuSPACE. Please Contact Support" );

    /**
     * The exit code.
     */
    private final int exitCode;

    /**
     * The id.
     */
    private final String id;

    /**
     * The message.
     */
    private final String message;

    /**
     * Instantiates a new exit codes and signals.
     *
     * @param exitCode
     *         the exit code
     * @param id
     *         the id
     * @param message
     *         the message
     */
    ExitCodesAndSignals( int exitCode, String id, String message ) {
        this.exitCode = exitCode;
        this.id = id;
        this.message = message;
    }

    /**
     * Gets the by exit code.
     *
     * @param code
     *         the code
     *
     * @return the by exit code
     */
    public static ExitCodesAndSignals getByExitCode( int code ) {
        return Arrays.stream( values() ).sequential().filter( exitCode -> exitCode.exitCode == code ).findFirst().orElse( UNKNOWN );
    }

    /**
     * Gets the exit code.
     *
     * @return the exit code
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the flag.
     *
     * @return the flag
     */
    public String getFlag() {
        return String.valueOf( exitCode - 128 );
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "ExitCodesAndSignals{ exitCode=" + exitCode + ", id=" + id + ", message=" + message + " }";

    }
}
