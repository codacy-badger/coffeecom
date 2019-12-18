package coffeecom.serverside;

import coffeecom.datastream.StreamPackage;
import coffeecom.formatting.ConsoleOutput;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Class used for handling each connection the server has to a client.
 * One-to-many relationship: one server can have multiple clients -> multiple `ConnectionHandler`.
 * Therefore a server can have multiple `Thread`s, one for each client, where one `Thread` runs `ConnectionHandler`.
 */
public class ConnectionHandler implements Runnable {

    /**
     * The clients socket
     */
    private Socket clientSocket;

    /**
     * The clients username
     */
    private String username;

    /**
     * The standard output to the client
     */
    private ObjectOutputStream out;

    /**
     * The standard input from the client
     */
    private ObjectInputStream in;

    /**
     * The connection with the client
     */
    private Connection connection;

    /**
     * A `ConsoleOutput` for writing formatted text to console
     */
    ConsoleOutput consoleFormat = new ConsoleOutput(true);


    ConnectionHandler(Connection conn) {
        this.connection = conn;
        this.clientSocket = conn.getSocket();
        this.in = conn.getIn();
        this.out = conn.getOut();
        this.username = conn.getClient().getUsername();
    }

    /**
     * This method runs on `Thread.start()`.
     */
    public void run() {
        try {
            // Connection-loop (runs while input from client is not finished)
            StreamPackage inPackage;
            String clientInputLine;  // Line sent from client
            do {
                // Input from client
                inPackage = (StreamPackage) in.readObject();
                clientInputLine = inPackage.getMsg();

                // Print to server-console
                consoleFormat.clientMessage(username, clientInputLine);

                // Broadcast message to all clients
                MainServer.broadcast(connection.getSocket(), connection.getClient(), connection.getOut(), clientInputLine);
            } while (clientInputLine != null);  // clientLine == null ==> end of stream

        } catch (SocketException se) {
            consoleFormat.serverMessage("Client " + username + " disconnected.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

