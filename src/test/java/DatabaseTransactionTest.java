import org.example.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTransactionTest {
    private Database database;

    @BeforeEach
    void setUp() {
        database = new Database();
    }

    @Test
    void rollbackWithoutTransaction() {
        assertFalse(database.rollback());
    }

    @Test
    void rollbackRestoresPreviousValue() {
        database.set("a", "10");

        database.begin();
        database.set("a", "20");

        assertTrue(database.rollback());
        assertEquals("10", database.get("a"));
        assertEquals(1, database.getCount("10"));
        assertEquals(0, database.getCount("20"));
    }

    @Test
    void rollbackRemovesNewKey() {
        database.begin();
        database.set("a", "10");

        database.rollback();

        assertNull(database.get("a"));
        assertEquals(0, database.getCount("10"));
    }

    @Test
    void nestedRollbackOnlyRollsBackLastTransaction() {
        database.set("a", "10");

        database.begin();
        database.set("a", "20");

        database.begin();
        database.set("a", "30");

        database.rollback();

        assertEquals("20", database.get("a"));

        database.rollback();

        assertEquals("10", database.get("a"));
    }

    @Test
    void commitTransaction() {
        database.begin();
        database.set("a", "20");

        database.begin();
        database.set("a", "30");

        assertTrue(database.commit());
        assertEquals("30", database.get("a"));
        assertEquals(1, database.getCount("30"));
    }

    @Test
    void commitWithoutTransaction() {
        assertFalse(database.commit());
    }

    @Test
    void rollbackAfterCommitTransaction() {
        database.begin();
        database.set("a", "20");

        database.begin();
        database.set("a", "30");

        database.commit();

        assertFalse(database.rollback());
        assertEquals("30", database.get("a"));
    }

    @Test
    void rollbackRestoresDeletedKey() {
        database.set("a", "10");

        database.begin();
        database.delete("a");

        assertNull(database.get("a"));

        database.rollback();

        assertEquals("10", database.get("a"));
        assertEquals(1, database.getCount("10"));
    }

}
