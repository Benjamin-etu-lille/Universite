

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;


public class MainTest {
    @Test
    public <T> void testVentilation() throws IOException {
        List<T[]> res = Ventilateur.ventilation("./../res/ingrediant.csv");
        assertEquals("banane", res.get(0)[0]);
        assertNotEquals(null, res.get(1)[0]);
        assertNotEquals("", res.get(2)[3]);
        assertNotEquals("poire", res.get(3)[0]);
    }
}