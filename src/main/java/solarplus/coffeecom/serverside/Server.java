/*
 * Run this class to start the server.
 *
 * @param args [OPTIONAL] The port the server should try to connect to
 * @author solarplus
 */
package solarplus.coffeecom.serverside;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static solarplus.coffeecom.formatting.OutputFormats.*;

public class Server {

    private static final String APPLICATION_NAME = "CoffeeCom";

    // Max amount of clients that can connect
    private static final int MAX_CLIENTS = 10;

    // Contain references to all clients (Sockets) connected to the server
    private static ArrayList<Socket> clients = new ArrayList<>();

    // Contain all names in the server
    public static ArrayList<String> usernames = new ArrayList<>();

    /**
     * This program can be started with one argument for port-number.
     */
    public static void main(String[] args) {
        // Gets port number from arguments, user input or default value
        int port = getPort(args);

        // Creates a ServerSocket
        ServerSocket serverSocket = createServerSocket(port);

        // Displays a startup-message for the server-console
        assert serverSocket != null;
        showStartupMessage(serverSocket);

        // Initiating the main server-loop
        // The loop connects to clients and handles them
        try {
            serverLoop(serverSocket);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Main loop for the server, connects to clients and handles them.
     *
     * @param serverSocket The ServerSocket to connect to clients
     */
    private static void serverLoop(ServerSocket serverSocket) throws IOException {
        // Listen for connections
        // For each connection/client -> Create new Thread and handle the connection
        while (clients.size() < MAX_CLIENTS) {
            Socket client = serverSocket.accept();  // Listening for connection to server
            clients.add(client);  // Adding client to list of clients

            // Getting input-stream from client
            InputStreamReader readerIn = new InputStreamReader(client.getInputStream());
            BufferedReader in = new BufferedReader(readerIn);

            // Gathering information from client
            String inetAddress = client.getInetAddress().toString();

            displayNewLine("SERVER", "Awaiting username from client...", RED);
            String username = in.readLine();  // Client always sends a username before anything else
            usernames.add(username);
            displayNewLine("SERVER", "Username from client received!", RED);

            // Print to server that a new client is connected
            displayNewLine("SERVER", username + " connected", GREEN);
            displayNewLine(username, "IP: " + inetAddress, YELLOW);

            // Broadcast to other clients that a new client has connected
            broadcast(client, "SERVER", username + " has connected.");

            // Sets up a new Thread to handle the client
            handleClient(client, username);
        }
    }

    /**
     * Creates a `ConnectionHandler` and a `Thread` to handle the connection.
     *
     * @param client   The Socket to handle
     * @param username The username of the client (Socket)
     */
    private static void handleClient(Socket client, String username) {
        ConnectionHandler ch = new ConnectionHandler(client, username);  // Creating a new ConnectionHandler for the client
        Thread t = new Thread(ch);  // Create a new thread for the client using the ConnectionHandler

        // Starts thread
        try {
            t.start();
        } catch (IllegalThreadStateException itse) {
            itse.printStackTrace();
        }
    }

    /**
     * Sends a message to all clients except the origin client.
     * The client who sent the message already sees the message in own console -> No need to broadcast to that client.
     *
     * @param originClient The `Socket` that sent the message
     * @param msg          The message to be broadcasted
     */
    public static void broadcast(Socket originClient, String username, String msg) {
        // Iterating through all clients connected to the server
        for (Socket client : clients) {
            if (client == originClient)  // If current client is the client who sent the message -> Don't send
                continue;
            try {
                // Creating writers to write out message to socket
                OutputStreamWriter writerOut = new OutputStreamWriter(client.getOutputStream());
                BufferedWriter out = new BufferedWriter(writerOut);

                // Writing out to client
                out.write("[  " + username + "  ] " + msg);
                out.newLine();  // Creating newline to end the current line
                out.flush();  // Send msg.
            } catch (IOException ioe) {
                System.err.println("[  " + format("SYSTEM", RED) + "  ] Error: Could not broadcast message: \"" + msg + "\" from " + username + ".");
                ioe.printStackTrace();
            }
        }
    }

    /**
     * Method for assigning port to the server.
     * Updates value either from program arguments, user input or default values.
     *
     * @param args The arguments given when executing the program
     */
    private static int getPort(String[] args) {
        clear();
        System.out.println("=====> " + APPLICATION_NAME + ":CONFIG" + " <=====");

        // Trying to get the port from `args`
        if (args.length == 1) {
            try {
                displayNewLine("SYSTEM", "Port number successfully parsed.", RED);
                display("SYSTEM", "Starting server ", RED);
                loadingScreen(3000, 5);

                return Integer.parseInt(args[0]);
            } catch (NumberFormatException nfe) {
                displayNewLine("SYSTEM", "Could not parse port number", RED);
            }
        }

        // Getting port via input from user
        else {
            try {
                display("SYSTEM", "Input port: ", RED);

                // Getting input from user
                Scanner input = new Scanner(System.in);
                return Integer.parseInt(input.nextLine());
            } catch (NumberFormatException nfe) {
                displayNewLine("SYSTEM", "Could not parse port number, assigning automatic port.", RED);
                display("SYSTEM", "Starting server ", RED);
                loadingScreen(5000, 5);
            } catch (Exception e) {
                displayNewLine("SYSTEM", "Something went wrong collecting input, assigning automatic port.", RED);
                display("SYSTEM", "Starting server ", RED);
                loadingScreen(5000, 5);
            }
        }

        return 0;  // Port of 0 automatically assigns valid port-number
    }

    /**
     * Method for creating a ServerSocket with a given port and catching exceptions.
     *
     * @return A ServerSocket with an assigned port or null if an error has occurred
     */
    private static ServerSocket createServerSocket(int port) {
        try {
            return new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return null;  // ERROR
        }
    }

    /**
     * Creating a startup-message based on information from a given ServerSocket.
     *
     * @param serverSocket The ServerSocket to gather information from
     */
    private static void showStartupMessage(ServerSocket serverSocket) {
        clear();
        System.out.println("=====> " + APPLICATION_NAME + " <=====");
        displayNewLine("SYSTEM", format("Successfully", GREEN) + " created server-socket", RED);
        displayNewLine("SERVER", "PORT: " + serverSocket.getLocalPort(), GREEN);
        displayNewLine("SERVER", "Listening for connections..", GREEN);
    }
}
