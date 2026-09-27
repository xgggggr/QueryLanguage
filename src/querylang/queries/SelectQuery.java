package querylang.queries;

import querylang.db.Database;
import querylang.db.Product;
import querylang.result.QueryResult;
import querylang.result.SelectQueryResult;
import querylang.util.FieldGetter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class SelectQuery implements Query {
    private final List<? extends FieldGetter> getters;
    private final Predicate<? super Product> predicate;
    private final Comparator<? super Product> comparator;

    public SelectQuery(
            List<? extends FieldGetter> getters,
            Predicate<? super Product> predicate,
            Comparator<? super Product> comparator
    ) {
        this.getters = getters;
        this.predicate = predicate;
        this.comparator = comparator;
    }

    @Override
    public SelectQueryResult execute(Database database) {
        List<List<String>> selectedValues = new ArrayList<>();
        database.getAll().stream()
                .filter(predicate)
                .sorted(comparator)
                .forEach(product -> {
                    List<String> line = new ArrayList<>();
                    for(FieldGetter getter: getters){
                        line.add(getter.getFieldValue(product));
                    }
                    selectedValues.add(line);
                });
        return new SelectQueryResult(selectedValues);
    }
}
