package solarplus.coffeecom.serverside;

import solarplus.coffeecom.formatting.ConsoleOutput;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * The main class for the CoffeeCom-server - configures and runs the server.
 * Accepts one argument - the port to connect to.
 */
public class Server {

    /**
     * Name of application.
     */
    private static final String APPLICATION_NAME = "CoffeeCom";

    /**
     * Max amount of clients that can connect to the server
     */
    private static final int MAX_CLIENTS = 10;

    /**
     * Contain references to all clients (Sockets) connected to the server
     */
    private static ArrayList<Socket> clients = new ArrayList<>();

    /**
     * Contain all usernames for clients connected to the server
     */
    private static ArrayList<String> usernames = new ArrayList<>();

    /**
     * Controls output to console
     */
    private static ConsoleOutput out;

    /**
     * Main method for the CoffeeCom-`Server`.
     *
     * @param args Should be one argument - the port you want to connect the server to
     */
    public static void main(String[] args) {
        // TODO: Print CoffeeCom-logo
        // TODO: Proper startup-screen

        // Decides if color formatting should be used
        // TODO: Get `activateColors` from user input or configuration file
        boolean activateColors = true;
        out = new ConsoleOutput(activateColors);  // Activates `ConsoleOutput` with colors on

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

            out.serverMessage("Awaiting username from client...");
            String username = in.readLine();  // Client always sends a username before anything else
            usernames.add(username);
            out.serverMessage("Username from client received!");

            // Print to server that a new client is connected
            out.serverMessage(username + " connected.");
            out.serverMessage("IP: " + inetAddress);

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
     * Returns current time of server.
     *
     * @return A String representing the current time of server
     */
    private static String getCurrentServerTime() {
        // Fetching time from server
        Date time = new Date();

        // Formatting time to HH:ss (hour:second)
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        return timeFormat.format(time);
    }

    /**
     * Fetches an ArrayList<String> of all usernames connected to the server.
     *
     * @return An ArrayList<String> containing usernames
     */
    public static ArrayList<String> getUsernames() {
        return usernames;
    }

    /**
     * Gets all clients connected to server.
     *
     * @return An ArrayList<Socket> with clients
     */
    public static ArrayList<Socket> getClients() {
        return clients;
    }

    /**
     * Sends a message to all clients except the origin client.
     * The client who sent the message already sees the message in own console -> No need to broadcast to that client.
     * Also prints time of broadcast.
     *
     * @param originClient The `Socket` that sent the message
     * @param msg          The message to be broadcasted
     */
    public static void broadcast(Socket originClient, String username, String msg) {
        out.clientMessage(username, msg + "   [" + getCurrentServerTime() + "]");

        // Iterating through all clients connected to the server
        for (Socket client : clients) {
            if (client == originClient)  // If current client is the client who sent the message -> Don't send
                continue;
            try {
                // Creating writers to write out message to socket
                OutputStreamWriter writerOut = new OutputStreamWriter(client.getOutputStream());
                BufferedWriter out = new BufferedWriter(writerOut);

                // Writing out to client
                String outLine = "[  " + username + " " + getCurrentServerTime() + " ] " + msg;
                out.write(outLine);
                out.newLine();  // Creating newline to end the current line
                out.flush();  // Send msg.
            } catch (IOException ioe) {
                out.systemErrorMessage("Error: Could not broadcast message: \"" + msg + "\" from " + username + ".");
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
        out.clear();
        System.out.println("=====> " + APPLICATION_NAME + ":CONFIG" + " <=====");

        // Trying to get the port from `args`
        if (args.length == 1) {
            try {
                out.systemMessage("Port number successfully parsed.");
                out.systemMessage("Starting server...");

                return Integer.parseInt(args[0]);
            } catch (NumberFormatException nfe) {
                out.systemMessage("Could not parse port-number.");
            }
        }

        // Getting port via input from user
        else {
            try {
                out.systemMessagePrint("PORT: ");

                // Getting input from user
                Scanner input = new Scanner(System.in);
                return Integer.parseInt(input.nextLine());
            } catch (NumberFormatException nfe) {
                out.systemMessage("Could not parse port number, assigning automatic port.");
            } catch (Exception e) {
                out.systemMessage("Something went wrong gathering input, assigning automatic port.");
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
        out.clear();
        System.out.println("=====> " + APPLICATION_NAME + " <=====");
        out.systemMessage("Successfully created server-socket.");
        out.serverMessage("PORT: " + serverSocket.getLocalPort());
        out.serverMessage("Listening for connections..");
    }
}
