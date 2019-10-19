package solarplus.coffeecom.clientside;

import solarplus.coffeecom.formatting.ConsoleOutput;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Class for constantly listening to input from server.
 * One-to-one relationship: one client has one `Thread` that runs `InputListener`.
 * Prints input to clients console.
 */
public class InputListener implements Runnable {

    /**
     * The standard input from server
     */
    private BufferedReader serverIn;

    /**
     * The username associated with the client
     */
    private String username;

    /**
     * Controls output to console
     */
    private static ConsoleOutput out;

    /**
     * Sets standard output and clients username.
     *
     * @param serverIn The standard input from the server the client is connected to
     * @param username The username associated with the client
     */
    InputListener(BufferedReader serverIn, String username) {
        // Decides if color formatting should be used
        // TODO: Get `activateColors` from user input or configuration file
        boolean activateColors = true;
        out = new ConsoleOutput(activateColors);  // Activates `ConsoleOutput` with colors on

        this.serverIn = serverIn;
        this.username = username;
    }

    /**
     * This method runs on `Thread.start()`.
     * Constantly listens for input from server.
     */
    public void run() {
        try {
            while (true) {
                String input = serverIn.readLine();  // Input received

                out.deleteLine();  // Delete prompting-line for client
                System.out.println(input);

                // Every time there is input, print new line for client to write on
                out.clientMessagePrint(username, "");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
