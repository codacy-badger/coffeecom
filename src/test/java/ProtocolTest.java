import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static coffeecom.Protocol.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProtocolTest {

    String validStr = "type=server" + delimeter() + "msg=Test" + delimeter() + "sender=Lars";
    String notValidStr = "type=server" + delimeter() + "msg=Test";


    @Test
    public void validTest () {
        assertTrue(valid(validStr));
        assertFalse(valid(notValidStr));
    }

    @Test
    public void fieldsTest () {
        assertEquals(fields(validStr).length, 3);  // Should be 3 fields in validStr

        // Assert that each field is retrieved
        String[] allFields = {"type=server", "msg=Test", "sender=Lars"};
        List<String> allFieldsList = Arrays.asList(allFields);
        for (String str : fields(validStr))
            assertTrue(allFieldsList.contains(str));
    }

    @Test
    public void extractTest () {
        assertEquals(extract(validStr, "msg"), "Test");
    }

    @Test
    public void typeTest () {
        assertEquals(type(validStr), "server");
    }

    @Test
    public void msgTest () {
        assertEquals(msg(validStr), "Test");
    }

    @Test
    public void msgSender () {
        assertEquals(sender(validStr), "Lars");
    }
}
