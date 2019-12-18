package coffeecom.serverside;

import coffeecom.datastream.StreamPackage;
import coffeecom.entities.Client;
import coffeecom.entities.Server;
import coffeecom.formatting.CoffeeComBanner;
import coffeecom.formatting.ConsoleOutput;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
public class MainServer {

    /**
     * Name of application.
     */
    private static final String APPLICATION_NAME = "CoffeeCom";

    /**
     * Max amount of connections that can connect to the server
     */
    private static final int MAX_CLIENTS = 10;

    /**
     * Contains connections to all clients connected to server.
     * 'Connection'-class contains useful information about client; socket, streams and misc.
     */
    private static ArrayList<Connection> connections = new ArrayList<>();

    /**
     * Controls output to server-console.
     */
    private static ConsoleOutput consoleFormat;

    /**
     * Main method for the CoffeeCom-`Server`.
     *
     * @param args Should be one argument (length = 1) - the port you want to connect the server to
     */
    public static void main(String[] args) {
        // Decides if color formatting should be used
        boolean activateColors = true;
        consoleFormat = new ConsoleOutput(activateColors);  // Activates `ConsoleOutput` with colors on

        // Welcome screen -> Printing banner
        consoleFormat.clear();
        CoffeeComBanner.printBanner();
        System.out.println();  // Empty line to give more space to banner

        // Gets port number from arguments, user input or default value
        int port = getPort(args);

        // Creates a ServerSocket
        ServerSocket serverSocket = createServerSocket(port);
        if (serverSocket == null)
            throw new NullPointerException();

        // Displays a startup-message for the server-console
        showStartupMessage(serverSocket);

        // Initiating the main server-loop
        // The loop connects to connections and handles them
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
     * @param serverSocket The ServerSocket that connects to clients
     */
    private static void serverLoop(ServerSocket serverSocket) throws IOException {
        // Listen for clients
        // For each connection/client -> Create new Thread and handle the client
        while (connections.size() < MAX_CLIENTS) {
            Socket socket = serverSocket.accept();  // Listening for connections to server

            // Clients I/O
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            // Client always sends the first package
            // First package contains username
            Client client = null;  // Represent the client -> store references
            try {
                StreamPackage firstPackage = ((StreamPackage) in.readObject());
                client = (Client) firstPackage.getSender();

                // Gathering address from client
                String inetAddress = socket.getInetAddress().toString();
                client.setInetAddress(inetAddress);
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
            }

            // Create the connection
            Connection conn = new Connection(socket, client, in, out);
            connections.add(conn);

            // Print to server that a new client is connected
            consoleFormat.serverMessage(client.getUsername() + " connected.");
            consoleFormat.serverMessage("IP: " + client.getInetAddress());

            // TODO: broadcast that new user connected to all clients

            // Send welcome msg to client
            out.writeObject(new StreamPackage(new Server(), consoleFormat.serverMessageGet("Hello from the CoffeeCom-server!")));
            out.flush();

            // Sets up a new Thread to handle the client
            handleConnection(conn);
        }
    }

    /**
     * Creates a `ConnectionHandler` and a `Thread` to handle the client.
     */
    private static void handleConnection(Connection conn) {
        ConnectionHandler ch = new ConnectionHandler(conn);  // Creating a new ConnectionHandler for the client
        Thread t = new Thread(ch);  // Create a new thread for the client using the ConnectionHandler

        // Start thread
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
     * Gets all connections server.
     *
     * @return An ArrayList<Connection> with representing connections to clients
     */
    public static ArrayList<Connection> getClients() {
        return connections;
    }

    /**
     * Sends a message to all connections except the origin client.
     * The client who sent the message already sees the message in own console -> No need to broadcast to that client.
     * Also prints time of broadcast.
     *
     * @param originSocket The socket of the client who sent the msg
     * @param originClient The client who sent the msg
     * @param out          The stdout of the client who sent the msg
     * @param msg          The message to be broadcast
     */
    public static void broadcast(Socket originSocket, Client originClient, ObjectOutputStream out, String msg) {
        // Iterating through all connections to the server
        for (Connection conn : connections) {
            if (conn.getSocket() == originSocket)  // If current client is the client who sent the message -> Don't send
                continue;
            try {
                // Writing to each client
                String outLine = "[  " + originClient.getUsername() + " " + getCurrentServerTime() + " ] " + msg;
                conn.getOut().writeObject(new StreamPackage(conn.getClient(), outLine));
                conn.getOut().flush();  // Send msg.
            } catch (IOException ioe) {
                consoleFormat.systemErrorMessage("Error: Could not broadcast message: \"" + msg + "\" from " + conn.getClient().getUsername() + ".");
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
        System.out.println("=====> CONFIG <=====");

        // Trying to get the port from `args`
        if (args.length == 1) {
            try {
                consoleFormat.systemMessage("Port number successfully parsed.");
                consoleFormat.systemMessage("Starting server...");
                consoleFormat.clear();

                return Integer.parseInt(args[0]);
            } catch (NumberFormatException nfe) {
                consoleFormat.systemMessage("Could not parse port-number.");
            }
        }

        // Getting port via input from user
        else {
            try {
                consoleFormat.systemMessagePrint("PORT: ");

                // Getting input from user
                Scanner input = new Scanner(System.in);
                return Integer.parseInt(input.nextLine());
            } catch (NumberFormatException nfe) {
                consoleFormat.systemMessage("Could not parse port number, assigning automatic port.");
            } catch (Exception e) {
                consoleFormat.systemMessage("Something went wrong gathering input, assigning automatic port.");
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
        consoleFormat.clear();
        CoffeeComBanner.printBanner();
        System.out.println();  // Empty line to give more space to banner
        consoleFormat.systemMessage("Successfully created server-socket.");
        consoleFormat.serverMessage("PORT: " + serverSocket.getLocalPort());
        consoleFormat.serverMessage("Listening for connections..");
    }
}
