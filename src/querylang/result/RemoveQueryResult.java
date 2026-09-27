package querylang.result;

public class RemoveQueryResult implements QueryResult {
    private final int id;
    private final boolean success;

    public RemoveQueryResult(int id, boolean success) {
        this.id = id;
        this.success = success;
    }

    @Override
    public String message() {
        if(success){
            return "Product with id " + id + " was removed successfully";
        }
        return "Product with id " + id + " not found";
    }
}
