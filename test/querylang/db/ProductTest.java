package querylang.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test
    void gettersTest(){
        Product product = new Product(0, "Guitar", "Yamaha", "Tokyo", 12);
        assertEquals(0, product.id());
        assertEquals("Guitar", product.name());
        assertEquals("Yamaha", product.manufacturer());
        assertEquals("Tokyo", product.city());
        assertEquals(12, product.quantity());
    }
    @Test
    void equalTest(){
        Product a = new Product(1, "Guitar", "Yamaha", "Tokyo", 12);
        Product b = new Product(1, "Guitar", "Yamaha", "Tokyo", 12);
        assertEquals(a, b);
    }
    @Test
    void equalForDifferentProducts(){
        Product a = new Product(1, "Guitar", "Yamaha", "Tokyo", 12);
        Product b = new Product(2, "Guitar", "Yamaha", "Krasnodar", 12);
        assertNotEquals(a, b);
    }
    @Test
    void equalProductsHaveSameHashCode() {
        Product a = new Product(1, "Guitar", "Yamaha", "Tokyo", 12);
        Product b = new Product(1, "Guitar", "Yamaha", "Tokyo", 12);
        assertEquals(a.hashCode(), b.hashCode());
    }
}