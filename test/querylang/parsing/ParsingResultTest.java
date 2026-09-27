package querylang.parsing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParsingResultTest {
    @Test
    void ofIsPresent() {
        ParsingResult<String> result = ParsingResult.of("67");
        assertTrue(result.isPresent());
        assertFalse(result.isError());
    }
    @Test
    void ofKeepsValue() {
        ParsingResult<String> result = ParsingResult.of("67");
        assertEquals("67", result.getValue());
    }
    @Test
    void errorIsError() {
        ParsingResult<String> result = ParsingResult.error("bad");
        assertTrue(result.isError());
        assertFalse(result.isPresent());
    }
    @Test
    void mapChangesValue() {
        ParsingResult<Integer> result = ParsingResult.of(2).map(x -> x + 1);
        assertEquals(3, result.getValue());
    }

}