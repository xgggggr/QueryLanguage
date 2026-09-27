package querylang.result;

import java.util.List;

public class SelectQueryResult implements QueryResult {
    private final List<List<String>> selectedValues;

    public SelectQueryResult(List<List<String>> selectedValues) {
        this.selectedValues = List.copyOf(selectedValues);
    }

    @Override
    public String message() {
        String s = "";
        for(int i = 0; i < selectedValues.size(); ++i){
            if(i == selectedValues.size() - 1){
                s += String.join(", ", selectedValues.get(i));
            }
            else{
                s += String.join(", ", selectedValues.get(i))+"\n";
            }
        }
        return s;
    }
}
