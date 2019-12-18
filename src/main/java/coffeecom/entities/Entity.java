package coffeecom.entities;

import java.io.Serializable;

/**
 * Represents an entity, either server or client.
 */
public interface Entity extends Serializable {

    /**
     * @return Type of entity, either SERVER or CLIENT
     */
    Type getType();
}

