import org.example.Database;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;

public class DatabaseTest {
    private Database database;

    @BeforeEach
    void setUp() {
        database = new Database();
    }

    @Test
    void setAndGet() {
        database.set("a", "10");

        assertEquals("10", database.get("a"));
    }

    @Test
    void getUnknownKey() {
        assertNull(database.get("a"));
    }

    @Test
    void getCountForMultipleKeys() {
        database.set("a", "10");
        database.set("b", "10");

        assertEquals(2, database.getCount("10"));
    }

    @Test
    void overwriteValueUpdatesCount() {
        database.set("a", "10");
        database.set("a", "20");

        assertEquals("20", database.get("a"));
        assertEquals(0, database.getCount("10"));
        assertEquals(1, database.getCount("20"));
    }

    @Test
    void setSameValueDoesNotChangeCount() {
        database.set("a", "10");
        database.set("a", "10");

        assertEquals(1, database.getCount("10"));
    }

    @Test
    void deleteOneOfMultipleKeysWithSameValue() {
        database.set("a", "10");
        database.set("b", "10");
        database.delete("a");

        assertEquals(1, database.getCount("10"));
        assertNull(database.get("a"));
        assertEquals("10", database.get("b"));
    }

    @Test
    void deleteExistingKey() {
        database.set("a", "10");
        database.delete("a");

        assertEquals(0, database.getCount("10"));
        assertNull(database.get("a"));
    }

    @Test
    void deleteUnknownKey() {
        database.delete("a");

        assertNull(database.get("a"));
    }

    @Test
    void getCountUnknownValue() {
        assertEquals(0, database.getCount("10"));
    }


}
