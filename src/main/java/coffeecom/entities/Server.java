package coffeecom.entities;

/**
 * Represents a server.
 */
public class Server implements Entity {

    @Override
    public Type getType() {
        return Type.SERVER;
    }
}
