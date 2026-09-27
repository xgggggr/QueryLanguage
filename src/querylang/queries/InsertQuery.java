package querylang.queries;

import querylang.db.Database;
import querylang.db.Product;
import querylang.result.InsertQueryResult;
import querylang.result.QueryResult;

public class InsertQuery implements Query {
    private final Product product;

    public InsertQuery(Product product) {
        this.product = product;
    }

    @Override
    public InsertQueryResult execute(Database database) {
        int id = database.add(product);
        return new InsertQueryResult(id);
    }
}
