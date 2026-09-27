package querylang.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private Database database;
    @BeforeEach
    void setUp(){
        database = new Database();
    }
    @Test
    void newDatabaseIsEmpty() {
        assertEquals(0, database.size());
        assertTrue(database.getAll().isEmpty());
    }
    @Test
    void addReturnsZeroForFirstProduct() {
        int id = database.add(new Product(0, "a", "b", "c", 12));
        assertEquals(0, id);
    }
    @Test
    void addSetsIdAuto() {
        database.add(new Product(67, "a", "b", "c", 1));
        Product saved = database.getAll().get(0);
        assertEquals(0, saved.id());
    }
    @Test
    void removeExistingProduct() {
        int id = database.add(new Product(0, "a", "b", "c", 1));
        assertTrue(database.remove(id));
        assertEquals(0, database.size());
    }
    @Test
    void removeMissingProduct() {
        assertFalse(database.remove(67));
    }
    @Test
    void clearRemovesEverything() {
        database.add(new Product(0, "a", "b", "c", 1));
        database.add(new Product(0, "q", "w", "e", 2));
        database.clear();
        assertEquals(0, database.size());
    }


}