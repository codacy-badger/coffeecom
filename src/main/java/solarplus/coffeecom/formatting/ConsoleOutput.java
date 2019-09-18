package solarplus.coffeecom.formatting;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.Color.*;

/**
 * This class is used for writing out text to console.
 * The class uses the Jansi-library to increase compatibility with different machines/OS's.
 */
public class ConsoleOutput {

    // This is the Ansi object used to create formatted `String`s
    private Ansi ansi;

    // Color settings
    private final Ansi.Color systemClr = RED;
    private final Ansi.Color serverClr = GREEN;
    private final Ansi.Color clientClr = YELLOW;

    /**
     * Calls `AnsiConsole.systemInstall()` or `AnsiConsole.systemUninstall()` based on the `install` argument
     *
     * @param install Activates Jansi if true, uninstalls if false
     */
    public ConsoleOutput(boolean install) {
        ansi = new Ansi();
        if (install)
            AnsiConsole.systemInstall();  // Install Jansi
        else
            AnsiConsole.systemUninstall();  // If you have installed 2 times, you need to uninstall 2 times to reset
    }

    /**
     * Method for resetting the current `Ansi`-`String`.
     */
    private void reset() {
        // TODO: reset current Ansi-String in a better way
        ansi = new Ansi();
    }

    /**
     * Clears the current screen.
     */
    public void clear() {
        reset();
        ansi.eraseScreen();
    }

    /**
     * Prints a system message on a new line with color formatting.
     * e.g. [  SYSTEM  ] msg
     *
     * @param msg The message to be printed
     */
    public void systemMessage(String msg) {
        reset();
        System.out.println(ansi.a("[  ").fg(systemClr).a("SYSTEM").reset().a("  ] " + msg).reset());
    }

    /**
     * Prints a system message on the same line with color formatting.
     * e.g. [  SYSTEM  ] msg
     *
     * @param msg The message to be printed
     */
    public void systemMessagePrint(String msg) {
        reset();
        System.out.print(ansi.a("[  ").fg(systemClr).a("SYSTEM").reset().a("  ] " + msg).reset());
    }

    /**
     * Prints a system error-message on a new line with color formatting.
     * e.g. [  SYSTEM  ] msg
     *
     * @param msg The message to be printed
     */
    public void systemErrorMessage(String msg) {
        reset();
        System.err.println(ansi.a("[  ").fg(systemClr).a("SYSTEM").reset().a("  ] " + msg).reset());
    }

    /**
     * Prints a server message on a new line with color formatting.
     * e.g. [  SERVER  ] msg
     *
     * @param msg The message to be printed
     */
    public void serverMessage(String msg) {
        reset();
        System.out.println(ansi.a("[  ").fg(serverClr).a("SERVER").reset().a("  ] " + msg).reset());
    }

    /**
     * Prints a server message on the same line with color formatting.
     * e.g. [  SERVER  ] msg
     *
     * @param msg The message to be printed
     */
    public void serverMessagePrint(String msg) {
        reset();
        System.out.print(ansi.a("[  ").fg(serverClr).a("SERVER").reset().a("  ] " + msg).reset());
    }

    /**
     * Returns a server message with color formatting.
     * e.g. [  SERVER  ] msg
     *
     * @param msg The message to be printed
     * @return A String with formatted server-message
     */
    public String serverMessageGet(String msg) {
        reset();
        return ansi.a("[  ").fg(serverClr).a("SERVER").reset().a("  ] " + msg).reset().toString();
    }

    /**
     * Prints a client message on a new line with color formatting.
     * e.g. [  USERNAME  ] msg
     *
     * @param username The username associated with the client
     * @param msg      The message to be printed
     */
    public void clientMessage(String username, String msg) {
        reset();
        System.out.println(ansi.a("[  ").fg(clientClr).a(username).reset().a("  ] " + msg).reset());
    }

    /**
     * Prints a client message on the same line with color formatting.
     * e.g. [  USERNAME  ] msg
     *
     * @param username The username associated with the client
     * @param msg      The message to be printed
     */
    public void clientMessagePrint(String username, String msg) {
        reset();
        System.out.print(ansi.a("[  ").fg(clientClr).a(username).reset().a("  ] " + msg).reset());
    }
}
