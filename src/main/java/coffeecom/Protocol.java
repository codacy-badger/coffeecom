package coffeecom;

/**
 * Can only send one String at a time between server/client(s), therefore the Strings needs to be formatted
 * in a certain way to allow for multiple pieces of information to be sent.
 *
 * A protocol string consists of multiple values, with a delimiter for each value statement.
 * The delimeter: ';&' (without backticks)
 *
 * Example of valid protocol: "type=client;&msg=Hello everybody!;&sender=Daniel"
 * There can't be spaces on either size of '=' (without backticks)
 *
 * A legal protocol string must have:
 *     1. type. Which msg-type it is. Either server/client/cmd.
 *     2. msg. The message to be sent.
 *     3. sender. Who sent the message. Either server/[clientname]
 */
public class Protocol {

    private static final String EXAMPLE_CLIENT = "type=client;&msg=Hello!;&sender=Daniel";
    private static final String EXAMPLE_SERVER = "type=server;&msg=Lobby: Daniel, Lars;&sender=server";
    private static final String EXAMPLE_COMMAND = "type=cmd;&msg=:kick Daniel;&sender=Lars";

    public static String getExampleClient () { return EXAMPLE_CLIENT; }
    public static String getExampleServer () { return EXAMPLE_SERVER; }

    public static String delimeter () { return ";&"; }

    // All required fields
    public static String field1() { return "type="; }
    public static String field2() { return "msg="; }
    public static String field3() { return "sender="; }


    /**
     * Checks if a given String is legal according to the protocol.
     * Every String must return true from this method before sending it.
     *
     * @param raw The String to be checked.
     * @return true if legal String according to protocol, false otherwise
     */
    public static boolean valid (String raw) {
        return raw.contains(field1()) && raw.contains(field2()) && raw.contains(field3());
    }

    /**
     * Returns every field in protocol String.
     *
     * @param protocol The protocol-String to be analyzed.
     * @return A String[] containing all the fields.
     */
    public static String[] fields (String protocol) {
        return protocol.split(delimeter());
    }

    /**
     * Extracts a given field-value from the protocol.
     *
     * @param protocol The protocol to extract from
     * @param field The field to extract value from
     * @return the value if found, null if not
     */
    public static String extract (String protocol, String field) {
        String fieldStr = null;

        // Traversing the fields
        for (String str : fields(protocol)) {
            if (str.startsWith(field))
                fieldStr = str;
        }

        String value = null;
        if (fieldStr != null)
            value = fieldStr.split("=")[1];  // Get the value after '='

        return value;
    }

    /**
     * Extracts the type of a protocol.
     *
     * @param protocol The protocol to extract from
     * @return The type-value
     */
    public static String type (String protocol) {
        return extract(protocol, "type");
    }

    /**
     * Extracts msg of a protocol.
     *
     * @param protocol The protocol to extract from
     * @return The msg-value
     */
    public static String msg (String protocol) {
        return extract(protocol, "msg");
    }

    /**
     * Extracts sender of a protocol.
     *
     * @param protocol The protocol to extract from
     * @return The sender-value
     */
    public static String sender (String protocol) {
        return extract(protocol, "sender");
    }
}
