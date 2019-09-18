package solarplus.coffeecom.serverside;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import static solarplus.coffeecom.formatting.OutputFormats.*;

public class ConnectionHandler implements Runnable {

    // The connection/client to handle
    private Socket client;

    // The clients username
    private String username;

    // The standard output to the client
    private BufferedWriter out;

    // The standard input to the client
    private BufferedReader in;

    /**
     * Constructor for each client-connection, assigning client.
     *
     * @param client A reference to the client
     */
    public ConnectionHandler(Socket client, String username) {
        this.client = client;
        this.username = username;
    }

    /**
     * This method runs on Thread.start().
     */
    public void run() {
        try {
            // => Handling output
            OutputStreamWriter outputWriter = new OutputStreamWriter(client.getOutputStream());
            out = new BufferedWriter(outputWriter);

            // <= Handling input
            InputStreamReader inputReader = new InputStreamReader(client.getInputStream());
            in = new BufferedReader(inputReader);

            // Creating a `String` with all users connected
            StringBuilder connectedClients = new StringBuilder();
            for (String name : Server.usernames)
                connectedClients.append(name).append(" ");

            // Welcome-msg to client
            // TODO: Improve method of displaying clients in lobby
            out.write("[  " + format("COFFEECOM", GREEN) + "  ] Connected to server. Lobby: " + connectedClients);
            out.newLine();
            out.flush();

            // Server-loop (runs while input from client is not finished)
            String clientLine;  // Line sent from client
            do {
                clientLine = in.readLine();  // Reading input from client

                Server.broadcast(this.client, username, clientLine);  // Broadcasting this message to all other clients
            } while (clientLine != null);  // clientLine = null ==> end of stream

        } catch (SocketException se) {
            displayNewLine("SERVER", "Client " + username + " disconnected.", GREEN);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

