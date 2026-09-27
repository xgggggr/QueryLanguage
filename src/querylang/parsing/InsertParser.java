package querylang.parsing;
import querylang.db.Product;
import querylang.queries.InsertQuery;
import querylang.queries.Query;
public class InsertParser {

    private InsertParser(){}

    public static ParsingResult<Query> parse(String arguments){
        if(arguments.length() < 2){
            return ParsingResult.error("INSERT has no arguments");
        }
        if(arguments.charAt(0) != '(' ||  arguments.charAt(arguments.length()-1) != ')'){
            return ParsingResult.error("arguments should be given in (...)");
        }
        String content = arguments.substring(1, arguments.length()-1);
        String[] args = content.split(",", -1);
        if(args.length != 4){
            return ParsingResult.error("4 arugemnts should be given in INSERT");

        }
        String name = args[0].strip();
        String manufacturer = args[1].strip();
        String city = args[2].strip();
        String quantity_str = args[3].strip();
        if(name.isEmpty()){
            return ParsingResult.error("name must not be empty");
        }
        if(city.isEmpty()){
            return ParsingResult.error("city must not be empty");
        }
        if(manufacturer.isEmpty()){
            return ParsingResult.error("manufacturer must not be empty");
        }
        if(quantity_str.isEmpty()){
            return ParsingResult.error("quantity must not be empty");
        }
        int quantity;
        try{
            quantity = Integer.parseInt(quantity_str);
        } catch (NumberFormatException e) {
            return ParsingResult.error("quantity must be a number");
        }
        if(quantity < 0){
            return ParsingResult.error("quantity should be non-negative");
        }
        Product product = new Product(0, name, manufacturer, city, quantity);
        return ParsingResult.of(new InsertQuery(product));
    }
}
