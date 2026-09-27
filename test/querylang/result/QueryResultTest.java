package querylang.result;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QueryResultTest {
    @Test
    void insertMessage() {
        InsertQueryResult result = new InsertQueryResult(3);
        assertEquals("Product with id 3 was added successfully", result.message());
    }

    @Test
    void clearMessage() {
        ClearQueryResult result = new ClearQueryResult(3);
        assertEquals("3 products were removed successfully", result.message());
    }
    @Test
    void clearMessageZero() {
        ClearQueryResult result = new ClearQueryResult(0);
        assertEquals("0 products were removed successfully", result.message());
    }
    @Test
    void removeSuccessMessage() {
        RemoveQueryResult result = new RemoveQueryResult(1, true);
        assertEquals("Product with id 1 was removed successfully", result.message());
    }
    @Test
    void removeNotFoundMessage() {
        RemoveQueryResult result = new RemoveQueryResult(67, false);
        assertEquals("Product with id 67 not found", result.message());}
    @Test
    void selectOneField() {
        List<List<String>> values = new ArrayList<>();
        values.add(List.of("Guitar"));
        SelectQueryResult result = new SelectQueryResult(values);
        assertEquals("Guitar", result.message());
    }
    @Test
    void selectRowWithManyFields() {
        List<List<String>> values = new ArrayList<>();
        values.add(List.of("0", "Guitar", "Yamaha", "Tokyo", "12"));
        SelectQueryResult result = new SelectQueryResult(values);
        assertEquals("0, Guitar, Yamaha, Tokyo, 12", result.message());
    }
    @Test
    void selectEmptyGivesEmptyString() {
        SelectQueryResult result = new SelectQueryResult(new ArrayList<>());
        assertEquals("", result.message());
    }
}