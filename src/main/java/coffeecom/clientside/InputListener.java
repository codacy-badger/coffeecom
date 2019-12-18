package coffeecom.clientside;

import coffeecom.datastream.StreamPackage;
import coffeecom.formatting.ConsoleOutput;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Class for constantly listening to input from server.
 * One-to-one relationship: one client has one `Thread` that runs `InputListener`.
 * Prints input to clients console.
 */
public class InputListener implements Runnable {

    /**
     * Standard in (from server)
     */
    private ObjectInputStream in;

    /**
     * The username associated with the client
     */
    private String username;

    /**
     * Controls output to console
     */
    private static ConsoleOutput consoleFormat;

    /**
     * @param in       The standard input (server sends data to this)
     * @param username The username associated with the client
     */
    InputListener(ObjectInputStream in, String username) {
        // Decides if color formatting should be used
        // TODO: Get `activateColors` from user input or configuration file
        boolean activateColors = true;
        consoleFormat = new ConsoleOutput(activateColors);  // Activates `ConsoleOutput` with colors on

        this.in = in;
        this.username = username;
    }

    /**
     * This method runs on `Thread.start()`.
     * Constantly listens for input from server.
     */
    public void run() {
        try {
            while (true) {
                // Fetch data sent from server; the input from server
                StreamPackage streamPackage = (StreamPackage) in.readObject();
                String inputMsg = streamPackage.getMsg();

                consoleFormat.deleteLine();  // Delete prompting-line for client
                System.out.println(inputMsg);

                // Every time there is input, print new line for client to write on
                consoleFormat.clientMessagePrint(username, "");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }
}
