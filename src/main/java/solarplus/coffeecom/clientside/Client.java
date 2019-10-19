/*
 * The class clients should use to interact with a CoffeeCom-server.
 */
package solarplus.coffeecom.clientside;

import solarplus.coffeecom.formatting.CoffeeComBanner;
import solarplus.coffeecom.formatting.ConsoleOutput;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Main class for the CoffeeCom-client - connects and communicates with the CoffeeCom-server.
 */
public class Client {

    /**
     * Name of application.
     */
    private static final String APPLICATION_NAME = "CoffeeCom";

    /**
     * Controls output to console
     */
    private static ConsoleOutput out;

    public static void main(String[] args) {
        // Decides if color formatting should be used
        // TODO: Get `activateColors` from user input or configuration file
        boolean activateColors = true;
        out = new ConsoleOutput(activateColors);  // Activates `ConsoleOutput` with colors on

        try {
            Scanner input = new Scanner(System.in);  // For user-input

            // Welcome screen -> Printing banner
            out.clear();
            CoffeeComBanner.printBanner();
            System.out.println();  // Empty line to give more space to banner

            // Fetching IP
            out.systemMessage("You need to connect to a " + APPLICATION_NAME + "-server.");
            out.systemMessagePrint("IP: ");
            String ip = input.nextLine();

            // Fetching port
            out.systemMessagePrint("PORT: ");
            int port = Integer.parseInt(input.nextLine());

            // Fetching username
            out.systemMessagePrint("USERNAME: ");
            String username = input.nextLine();

            // Creating a socket and connecting to server
            Socket socket = new Socket(ip, port);

            // Output to server
            BufferedWriter serverOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Input from server
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Sending username to server
            // TODO: Implement a better way of sending username and possibly timestamps
            serverOut.write(username);
            serverOut.newLine();  // Ends the current line
            serverOut.flush();  // Sending msg.

            // Constantly listening for input from server
            InputListener listener = new InputListener(serverIn, username);
            Thread t = new Thread(listener);
            t.start();

            /*
             * Loop - constantly asks user for input to be sent to server
             */
            do {
                // TODO: Implement commands from user, like '/exit'
                // Asking for input from user
                out.clientMessagePrint(username, "");
                String inputMsg = input.nextLine();

                // Writing to server
                serverOut.write(inputMsg);
                serverOut.newLine();  // Ends the current line
                serverOut.flush();  // Sending msg.
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

