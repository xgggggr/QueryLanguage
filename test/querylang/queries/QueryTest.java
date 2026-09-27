package querylang.queries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import querylang.db.Database;
import querylang.db.Product;
import querylang.util.FieldGetter;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class QueryTest {
    private Database database;

    @BeforeEach
    void setUp() {
        database = new Database();
    }
    @Test
    void insertAddsProduct() {
        InsertQuery query = new InsertQuery(new Product(0, "Guitar", "Yamaha", "Tokyo", 12));
        query.execute(database);
        assertEquals(1, database.size());
    }
    @Test
    void insertResultHasId() {
        InsertQuery query = new InsertQuery(new Product(0, "Guitar", "Yamaha", "Tokyo", 12));
        assertEquals("Product with id 0 was added successfully", query.execute(database).message());
    }
    @Test
    void removeExisting() {
        int id = database.add(new Product(0, "Guitar", "Yamaha", "Tokyo", 12));
        RemoveQuery query = new RemoveQuery(id);
        String message = query.execute(database).message();
        assertEquals("Product with id 0 was removed successfully", message);
        assertEquals(0, database.size());
    }
    @Test
    void removeMissing() {
        RemoveQuery query = new RemoveQuery(7);
        assertEquals("Product with id 7 not found", query.execute(database).message());
    }
    @Test
    void clearEmptiesDatabase() {
        database.add(new Product(0, "Guitar", "Yamaha", "Tokyo", 12));
        database.add(new Product(0, "q", "w", "e", 2));
        ClearQuery query = new ClearQuery();
        String message = query.execute(database).message();
        assertEquals("2 products were removed successfully", message);
        assertEquals(0, database.size());
    }
    @Test
    void selectName() {
        database.add(new Product(0, "Guitar", "Yamaha", "Tokyo", 12));
        FieldGetter nameGetter = Product::name;
        Predicate<Product> all = product -> true;
        Comparator<Product> byId = Comparator.comparingInt(Product::id);
        SelectQuery query = new SelectQuery(List.of(nameGetter), all, byId);
        assertEquals("Guitar", query.execute(database).message());
    }
    @Test
    void selectWithFilter() {
        database.add(new Product(0, "Guitar", "Yamaha", "Tokyo", 12));
        database.add(new Product(0, "Pie", "X5", "Moscow", 12));
        FieldGetter nameGetter = Product::name;
        Predicate<Product> moscow = product -> product.city().equals("Moscow");
        Comparator<Product> byId = Comparator.comparingInt(Product::id);
        SelectQuery query = new SelectQuery(List.of(nameGetter), moscow, byId);
        assertEquals("Pie", query.execute(database).message());
    }



}