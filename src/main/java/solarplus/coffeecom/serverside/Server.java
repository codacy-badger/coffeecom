/*
 * Run this class to start the server.
 *
 * @param args [OPTIONAL] The port the server should try to connect to
 * @author solarplus
 */
package solarplus.coffeecom.serverside;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.NumberFormatException;
import java.lang.IllegalThreadStateException;

import static solarplus.coffeecom.formatting.OutputFormats.*;

public class Server {

    private static final int MAX_CLIENTS = 10;

    // Contain references to all clients (Sockets) connected to the server
    private static ArrayList<Socket> clients = new ArrayList<>();

    private static final String APPLICATION_NAME = "CoffeeCom";


    /**
     * This program can be started with one argument for port.
     */
    public static void main(String[] args) {
        // System start-up message
        clear();
        System.out.println("=====> " + APPLICATION_NAME + " <=====");

        // Getting port
        int port = 0;  // Port of 0 automatically assigns port

        // Trying to get the port from 'args'
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException nfe) {
                displayNewLine("SYSTEM", "Could not parse port number", RED);
            }
        } else {
            try {
                display("SYSTEM", "Input port: ", RED);

                // Getting input from user
                Scanner input = new Scanner(System.in);
                port = Integer.parseInt(input.nextLine());
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

        try {
            ServerSocket serversocket = new ServerSocket(port);
            clear();  // Clearing terminal-screen

            // Server start-up message
            System.out.println("=====> " + APPLICATION_NAME + " <=====");
            displayNewLine("SYSTEM", "Created server-socket", RED);
            displayNewLine("SERVER", "PORT: " + serversocket.getLocalPort(), GREEN);
            displayNewLine("SERVER", "Listening for connections..", GREEN);

            // Listen for connections => Create new Thread for handling connections for each connection/client
            while (clients.size() < MAX_CLIENTS) {
                Socket client = serversocket.accept();  // Listening for connection to server
                clients.add(client);  // Adding client to list of clients

                // Gathering information from client
                int clientNum = clients.size() - 1;
                String inetAddress = client.getInetAddress().toString();

                // Broadcast to other clients that a new client has connected
                broadcast(client, "[  " + format("SERVER", GREEN) + "  ] Client " + clientNum + " connected.");

                // Print to server that a new client is connected
                displayNewLine("SERVER", "Client " + clientNum + " connected", GREEN);
                displayNewLine("CLIENT " + clientNum, "IP: " + inetAddress, YELLOW);

                ConnectionHandler ch = new ConnectionHandler(client);  // Creating a new ConnectionHandler for the client
                Thread t = new Thread(ch);  // Create a new thread for the client using the ConnectionHandler
                // Starts thread
                try {
                    t.start();  // Starts thread
                } catch (IllegalThreadStateException itse) {
                    itse.printStackTrace();
                }
            }
        } catch (IOException ioe) {
            displayNewLine("SYSTEM", "Something went wrong trying to create a server-socket. Exiting ", RED);
            loadingScreen(3000, 5);
            ioe.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Sends a message to all clients except the origin client.
     * The client who sent the message already sees the message in own console -> No need to broadcast to that client.
     *
     * @param originClient The `Socket` that sent the message
     * @param msg          The message to be broadcasted
     */
    public static void broadcast(Socket originClient, String msg) {
        System.out.println(msg);

        // Iterating through all clients connected to the server
        for (Socket client : clients) {
            if (client == originClient)  // If current client is the client who sent the message -> Don't send
                continue;
            try {
                // Using every socket's outputstream to send message to corresponding client
                OutputStreamWriter writerOut = new OutputStreamWriter(client.getOutputStream());
                BufferedWriter out = new BufferedWriter(writerOut);

                // Writing out to client
                out.write(msg);
                out.newLine();  // Creating newline to end the current line
                out.flush();
            } catch (IOException ioe) {
                System.err.println("[  " + format("SYSTEM", RED) + "  ] Error: Could not broadcast message: \"" + msg + "\".");
                ioe.printStackTrace();
            }
        }
    }
}

