package querylang.parsing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import querylang.db.Database;
import querylang.queries.Query;

import static org.junit.jupiter.api.Assertions.*;

class QueryParserTest {
    private QueryParser parser;
    private Database database;
    @BeforeEach
    void setUp(){
        parser = new QueryParser();
        database = new Database();
    }
    private String run(String line) {
        ParsingResult<Query> result = parser.parse(line);
        assertTrue(result.isPresent());
        return result.getValue().execute(database).message();
    }
    @Test
    void unknownCommand() {
        assertTrue(parser.parse("GIVEME1BDOLLARS").isError());
    }
    @Test
    void emptyLine() {
        assertTrue(parser.parse("").isError());
    }
    @Test
    void clearParses() {
        assertTrue(parser.parse("CLEAR").isPresent());
    }
    @Test
    void clearIsCaseFree() {
        assertTrue(parser.parse("clear").isPresent());
        assertTrue(parser.parse("ClEaR").isPresent());
    }
    @Test
    void insertParses() {
        assertTrue(parser.parse("INSERT (Guitar, Yamaha, Tokyo, 12)").isPresent());
    }
    @Test
    void insertActuallyAdds() {
        run("INSERT (Guitar, Yamaha, Tokyo, 12)");
        assertEquals(1, database.size());}
    @Test
    void insertNoBrackets() {
        assertTrue(parser.parse("INSERT Guitar, Yamaha, Tokyo, 12").isError());
    }
    @Test
    void insertStripSpaces() {
        run("INSERT (                Guitar  ,  Yamaha , Tokyo , 12)");
        assertEquals("Guitar", database.getAll().getFirst().name());
    }
    @Test
    void insertNot4Fields() {
        assertTrue(parser.parse("INSERT (Guitar, Yamaha, Tokyo)").isError());
    }
    @Test
    void insertEmptyName() {
        assertTrue(parser.parse("INSERT ( , Yamaha, Tokyo, 12)").isError());
    }
    @Test
    void insertNegativeQuantity() {
        assertTrue(parser.parse("INSERT (Guitar, Yamaha, Tokyo, -1)").isError());
    }
    @Test
    void insertNotNumber() {
        assertTrue(parser.parse("INSERT (Guitar, Yamaha, Tokyo, qwe)").isError());
    }
    @Test
    void removeParses() {
        assertTrue(parser.parse("REMOVE 67").isPresent());}

    @Test
    void removeMissingProduct() {
        assertEquals("Product with id 42 not found", run("REMOVE 42"));
    }
    @Test
    void removeExistingProduct() {
        run("INSERT (Guitar, Yamaha, Tokyo, 12)");
        assertEquals("Product with id 0 was removed successfully", run("REMOVE 0"));
        assertEquals(0, database.size());
    }
    @Test
    void removeWithoutId() {
        assertTrue(parser.parse("REMOVE").isError());
    }
    @Test
    void removeNonNumberIsError() {
        assertTrue(parser.parse("REMOVE qwe").isError());
    }
    @Test
    void selectStarParses() {
        assertTrue(parser.parse("SELECT *").isPresent());
    }
    @Test
    void selectFieldsParses() {
        assertTrue(parser.parse("SELECT (name, manufacturer)").isPresent());
    }
    @Test
    void selectStarPrintsAllFields() {
        run("INSERT (Guitar, Yamaha, Tokyo, 12)");
        assertEquals("0, Guitar, Yamaha, Tokyo, 12", run("SELECT *"));
    }
    @Test
    void selectOneField() {
        run("INSERT (Guitar, Yamaha, Tokyo, 12)");
        assertEquals("Guitar", run("SELECT (name)"));
    }

    @Test
    void selectSameField() {
        run("INSERT (Guitar, Yamaha, Tokyo, 12)");
        assertEquals("Guitar, Guitar, Guitar", run("SELECT (name, name, name)"));
    }
    @Test
    void selectIsCaseFree() {
        assertTrue(parser.parse("sElect (name)").isPresent());
    }
    @Test
    void selectFieldNameIsNotCaseFree() {
        assertTrue(parser.parse("SELECT (NAME)").isError());
    }
    @Test
    void selectUnknownField() {
        assertTrue(parser.parse("SELECT (dog)").isError());
    }
    @Test
    void selectEmptyFieldList() {
        assertTrue(parser.parse("SELECT ()").isError());
    }
    @Test
    void selectNoClosingBracket() {
        assertTrue(parser.parse("SELECT (name").isError());
    }
    @Test
    void selectWrongSeparator() {
        assertTrue(parser.parse("SELECT (name,manufacturer)").isError());
    }
    @Test
    void selectWithFilterParses() {
        assertTrue(parser.parse("SELECT (name) FILTER(city, Tokyo)").isPresent());
    }
    @Test
    void filterSelect() {
        run("INSERT (Guitar, Yamaha, Tokyo, 12)");
        run("INSERT (Pie, X5, Moscow, 12)");
        assertEquals("Pie", run("SELECT (name) FILTER(city, Moscow)"));
    }
    @Test
    void twoFilters() {
        run("INSERT (Guitar, Yamaha, Tokyo, 12)");
        run("INSERT (Pie, X5, Moscow, 12)");
        run("INSERT (Pie, X5, Moscow, 67)");
        assertEquals("1", run("SELECT (id) FILTER(city, Moscow) FILTER(quantity, 12)"));}
    @Test
    void filterWrongSeparator() {
        assertTrue(parser.parse("SELECT (name) FILTER(city,Moscow)").isError());
    }
    @Test
    void selectWithOrderParses() {
        assertTrue(parser.parse("SELECT * ORDER(quantity, ASC)").isPresent());
    }
    @Test
    void orderAsc() {
        run("INSERT (A, Q, W, 5)");
        run("INSERT (B, Q, W, 1)");
        assertEquals("B\nA", run("SELECT (name) ORDER(quantity, ASC)"));
    }
    @Test
    void orderDesc() {
        run("INSERT (A, Q, W, 5)");
        run("INSERT (B, Q, W, 1)");
        assertEquals("A\nB", run("SELECT (name) ORDER(quantity, DESC)"));
    }
    @Test
    void orderTieBreaksById() {
        run("INSERT (A, Q, W, 7)");
        run("INSERT (B, Q, W, 7)");
        assertEquals("A\nB", run("SELECT (name) ORDER(quantity, ASC)"));
    }
    @Test
    void orderString() {
        run("INSERT (q, w, Vladimir, 1)");
        run("INSERT (r, t, Moscow, 1)");
        run("INSERT (d, f, Tokyo, 1)");
        assertEquals("r\nd\nq", run("SELECT (name) ORDER(city, ASC)"));
    }
    @Test
    void orderTwiceIsError() {
        assertTrue(parser.parse("SELECT * ORDER(id, ASC) ORDER(name, ASC)").isError());
    }
    @Test
    void filterAndOrderInAnyOrderParses() {
        assertTrue(parser.parse(
                "SELECT * FILTER(city, Moscow) ORDER(quantity, ASC) FILTER(manufacturer, Yamaha)"
        ).isPresent());
    }
    @Test
    void orderAndFilter() {
        run("INSERT (a, b, Moscow, 5)");
        run("INSERT (c, b, Tokyo, 1)");
        run("INSERT (d, b, Moscow, 1)");
        assertEquals("d\na", run("SELECT (name) ORDER(quantity, ASC) FILTER(city, Moscow)"));
    }

}