package coffeecom.serverside;

import coffeecom.entities.Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Represents a server -> client connection.
 */
public class Connection {

    /**
     * Socket of client
     */
    private Socket socket;

    /**
     * Represents client
     */
    private Client client;

    /**
     * Clients input / output
     */
    private ObjectInputStream in;
    private ObjectOutputStream out;


    public Connection(Socket socket, Client client, ObjectInputStream in, ObjectOutputStream out) {
        this.socket = socket;
        this.client = client;
        this.in = in;
        this.out = out;
    }

    public Socket getSocket() {
        return socket;
    }

    public Client getClient() {
        return client;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }
}
