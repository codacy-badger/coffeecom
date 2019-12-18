package coffeecom.entities;

import java.awt.*;

/**
 * Represents a client.
 */
public class Client implements Entity {

    private String username;

    private Color clr;

    private String inetAddress = null;

    public Client(String username) {
        this.username = username;
    }

    @Override
    public Type getType() {
        return Type.CLIENT;
    }

    public String getUsername() {
        return this.username;
    }

    public void setInetAddress(String inetAddress) {
        this.inetAddress = inetAddress;
    }

    public String getInetAddress() {
        return this.inetAddress;
    }
}
