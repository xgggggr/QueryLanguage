package querylang.parsing;

import querylang.queries.Query;
import querylang.queries.RemoveQuery;

public class RemoveParser {
    private RemoveParser() {
    }

    static ParsingResult<Query> parse(String arguments) {
        int id;
        if (arguments.isEmpty()) {
            return ParsingResult.error("REMOVE has no id");
        }
        try {
            id = Integer.parseInt(arguments);
        } catch (NumberFormatException e) {
            return ParsingResult.error("REMOVE id must be integer");
        }
        return ParsingResult.of(new RemoveQuery(id));
    }
}

