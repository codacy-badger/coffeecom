package coffeecom.clientside;

import coffeecom.datastream.StreamPackage;
import coffeecom.entities.Client;
import coffeecom.formatting.CoffeeComBanner;
import coffeecom.formatting.ConsoleOutput;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Main class for the CoffeeCom-client - connects and communicates with the CoffeeCom-server.
 */
public class MainClient {

    /**
     * Name of application.
     */
    private static final String APPLICATION_NAME = "CoffeeCom";

    /**
     * Controls output to console
     */
    private static ConsoleOutput consoleFormat;


    public static void main(String[] args) {
        // Decides if color formatting should be used
        // TODO: Get `activateColors` from user input or configuration file
        boolean activateColors = true;
        consoleFormat = new ConsoleOutput(activateColors);  // Activates `ConsoleOutput` with colors on

        try {
            Scanner input = new Scanner(System.in);  // For user-input

            // Welcome screen -> Printing banner
            consoleFormat.clear();
            CoffeeComBanner.printBanner();
            System.out.println();  // Empty line to give more space to banner

            // Fetching IP
            consoleFormat.systemMessage("You need to connect to a " + APPLICATION_NAME + "-server.");
            consoleFormat.systemMessagePrint("IP: ");
            String ip = input.nextLine();

            // Fetching port
            consoleFormat.systemMessagePrint("PORT: ");
            int port = Integer.parseInt(input.nextLine());

            // Fetching username
            consoleFormat.systemMessagePrint("USERNAME: ");
            String username = input.nextLine();

            // Creating a socket and connecting to server
            Socket socket = new Socket(ip, port);

            // Representing this client
            Client client = new Client(username);

            // Output to server
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            // Input from server
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Sending information to server
            try {
                StreamPackage clientInformation = new StreamPackage(client, null);
                out.writeObject(clientInformation);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Constantly listening and displaying input from server
            InputListener listener = new InputListener(in, username);
            Thread t = new Thread(listener);
            t.start();

            /*
             * Loop - constantly asks user for msg to be sent to server
             */
            StreamPackage streamPackage;
            do {
                // Asking for input from user
                consoleFormat.clientMessagePrint(username, "");
                streamPackage = new StreamPackage(client, input.nextLine());

                // Writing to server
                out.writeObject(streamPackage);
                out.flush();  // Sending msg.
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

