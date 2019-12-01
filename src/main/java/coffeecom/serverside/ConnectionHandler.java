package coffeecom.serverside;

import coffeecom.formatting.ConsoleOutput;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * Class used for handling each connection the server has to a client.
 * One-to-many relationship: one server can have multiple clients.
 * Therefore a server can have multiple `Thread`s, one for each client, where one `Thread` runs `ConnectionHandler`.
 */
public class ConnectionHandler implements Runnable {

    /**
     * The connection/client to handle
     */
    private Socket client;

    /**
     * The clients username
     */
    private String username;

    /**
     * The standard output to the client
     */
    private BufferedWriter clientOut;

    /**
     * The standard input from the client
     */
    private BufferedReader clientIn;

    /**
     * A `ConsoleOutput` for writing formatted text to console
     */
    ConsoleOutput out = new ConsoleOutput(true);

    /**
     * Constructor for each client-connection.
     * Gets clients socket and username and stores them.
     *
     * @param client   A reference to the client
     * @param username The clients username
     */
    ConnectionHandler(Socket client, String username) {
        this.client = client;
        this.username = username;
    }

    /**
     * This method runs on `Thread.start()`.
     */
    public void run() {
        try {
            // Handling output to client
            clientOut = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            // Handling input from client
            clientIn = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // Creating a String displaying all clients connected to the server
            StringBuilder connectedClients = new StringBuilder();
            for (String name : Server.getUsernames())
                connectedClients.append(name).append(" ");

            // Welcome-msg to client
            // TODO: Improve method of displaying clients in lobby
            clientOut.write(out.serverMessageGet("Connected to server.   Lobby [" + connectedClients + "]"));
            clientOut.newLine();
            clientOut.flush();

            // Connection-loop (runs while input from client is not finished)
            String clientInputLine;  // Line sent from client
            do {
                clientInputLine = clientIn.readLine();  // Reading input from client

                Server.broadcast(this.client, username, clientInputLine);  // Broadcasting this message to all other clients
            } while (clientInputLine != null);  // clientLine == null ==> end of stream

        } catch (SocketException se) {
            out.serverMessage("Client " + username + " disconnected.");

            // TODO: Create a better way of removing client/username
            Server.getClients().remove(this.client);  // Removes client from server
            Server.getUsernames().remove(username);  // Removes username from server
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

