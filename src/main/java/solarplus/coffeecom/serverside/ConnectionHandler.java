package solarplus.coffeecom.serverside;

import java.net.Socket;

import java.lang.Runnable;

import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;

import static solarplus.coffeecom.formatting.OutputFormats.*;  // Importing all static fields and methods

public class ConnectionHandler implements Runnable {

    // The connection/client to handle
    private Socket client;

    // The standard output to the client
    private BufferedWriter out;

    // The standard input to the client
    private BufferedReader in;

    /**
     * Constructor for each client-connection, assigning client.
     *
     * @param client A reference to the client
     */
    public ConnectionHandler(Socket client) {
        this.client = client;
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

            // Server: sending welcome message
            // TODO: Write out current clients connected to the lobby
            out.write("[  " + format("COFFEECOM", GREEN) + "  ] Connected to server.");
            out.newLine();
            out.flush();

            // Server-loop (runs while input from client is not finished)
            String clientLine;  // Line sent from client
            do {
                clientLine = in.readLine();  // Reading input from client
                System.out.println(clientLine);  // Client sends a str with identifier, e.g. [  JONAS  ] "Msg"

                Server.broadcast(this.client, clientLine);  // Broadcasting this message to all other clients
            } while (clientLine != null);  // clientLine = null ==> end of stream

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

