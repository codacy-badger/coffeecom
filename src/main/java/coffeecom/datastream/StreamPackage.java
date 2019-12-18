package coffeecom.datastream;

import coffeecom.entities.Entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is what is being sent back and forth between server and client(s).
 * Contains useful data, usually messages from client(s).
 */
public class StreamPackage implements Serializable {

    /**
     * Which application-side sends the package, must always be specified.
     * Is either Type.Server/Type.Client.
     */
    private Entity sender;

    /**
     * The msg to be sent
     */
    private String msg;

    /**
     * If multiple msg's, e.g. for displaying previous log
     */
    private ArrayList<String> log;

    /**
     * Constructor for only msg attachment
     */
    public StreamPackage(Entity sender, String msg) {
        this.sender = sender;
        this.msg = msg;
        log = null;
    }

    public Entity getSender() {
        return sender;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<String> getLog() {
        return log;
    }
}
