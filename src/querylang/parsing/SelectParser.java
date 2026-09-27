package querylang.parsing;

import querylang.db.Product;
import querylang.queries.Query;
import querylang.queries.SelectQuery;
import querylang.util.FieldGetter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class SelectParser {
    public static ParsingResult<Query> parse(String arguments) {
        int argsEnd;
        Predicate<Product> predicate = product -> true;
        Comparator<Product> comparator = null;
        boolean orderUsed = false;
        if(arguments.startsWith("*")){
            argsEnd = 1;
        }
        else if(arguments.startsWith("(")){
            int bracket = arguments.indexOf(')');
            if(bracket == -1){
                return ParsingResult.error("no ')' is given ");
            }
            argsEnd = bracket + 1;
        }
        else{
            return ParsingResult.error("'*' or '(...)' were expected");
        }
        String argsPart = arguments.substring(0, argsEnd);
        String anotherPart = arguments.substring(argsEnd);
        ParsingResult<List<FieldGetter>> fields = parseFieldList(argsPart);
        if (fields.isError()) {
            return ParsingResult.error(fields.getErrorMessage());
        }
        List<FieldGetter> getters = fields.getValue();
        int i = 0;
        while(i < anotherPart.length()){
            if (anotherPart.charAt(i) != ' ') {
                return ParsingResult.error("unexpected text after field list");
            }
            while (i < anotherPart.length() && anotherPart.charAt(i) == ' ') {
                i++;
            }
            if (i >= anotherPart.length()) {
                return ParsingResult.error("command expected after space");
            }
            int openBracket = anotherPart.indexOf('(', i);
            if (openBracket < 0) {
                return ParsingResult.error("'(' should be given in command");
            }
            String name = anotherPart.substring(i, openBracket).toUpperCase();
            int nextCom = findNextCommand(anotherPart, openBracket + 1);
            int searchUpTo;
            if (nextCom < 0) {
                searchUpTo = anotherPart.length() - 1;
            } else {
                searchUpTo = nextCom - 1;
            }
            int closedBracket = anotherPart.lastIndexOf(')', searchUpTo);
            if (closedBracket <= openBracket) {
                return ParsingResult.error("no closing ')' in command");
            }
            String body = anotherPart.substring(openBracket + 1, closedBracket);
            if (name.equals("FILTER")) {
                ParsingResult<Predicate<Product>> filter = parseFilter(body);
                if (filter.isError()) {
                    return ParsingResult.error(filter.getErrorMessage());
                }
                predicate = predicate.and(filter.getValue());
            }
            else if(name.equals("ORDER")){
                if (orderUsed) {
                    return ParsingResult.error("ORDER can be used only once");
                }
                orderUsed = true;
                ParsingResult<Comparator<Product>> order = parseOrder(body);
                if (order.isError()) {
                    return ParsingResult.error(order.getErrorMessage());
                }
                comparator = order.getValue();
            }
            else{
                return ParsingResult.error("Unknown subcommand");
            }
            i = closedBracket + 1;
        }
        Comparator<Product> finalComparator;
        if (comparator == null) {
            finalComparator = Comparator.comparingInt(Product::id);
        } else {
            finalComparator = comparator.thenComparingInt(Product::id);
        }

        return ParsingResult.of(new SelectQuery(getters, predicate, finalComparator));
    }
    private static ParsingResult<List<FieldGetter>> parseFieldList(String fieldsPart) {
        List<FieldGetter> getters = new ArrayList<>();
        if (fieldsPart.equals("*")) {
            for (Field field : Field.all()) {
                getters.add(field.getter());
            }
            return ParsingResult.of(getters);
        }
        if (!fieldsPart.startsWith("(") || !fieldsPart.endsWith(")")) {
            return ParsingResult.error("Field list must be '*' or '(...)'");
        }
        String content = fieldsPart.substring(1, fieldsPart.length() - 1);
        if (content.isEmpty()) {
            return ParsingResult.error("Field list must not be empty");
        }
        String[] parts = content.split(",", -1);
        for (int k = 0; k < parts.length; k++) {
            String fieldName;
            if (k == 0) {
                fieldName = parts[k];
            }
            else {
                if (parts[k].isEmpty() || parts[k].charAt(0) != ' ') {
                    return ParsingResult.error("Fields must be separated by ', '");
                }
                fieldName = removeSpaces(parts[k]);
            }

            Field field = Field.Searcher(fieldName);
            if (field == null) {
                return ParsingResult.error("Unknown field");
            }
            getters.add(field.getter());
        }
        return ParsingResult.of(getters);
    }
    private static String removeSpaces(String text) {
        int s = 0;
        while (s < text.length() && text.charAt(s) == ' ') {
            s +=1 ;
        }
        return text.substring(s);
    }
    private static int findNextCommand(String text, int from) {
        String upperText = text.toUpperCase();
        for (int j = from; j < upperText.length(); j++) {
            if (upperText.startsWith(" FILTER(", j) || upperText.startsWith(" ORDER(", j)) {
                return j;
            }
        }
        return -1;
    }
    private static ParsingResult<Predicate<Product>> parseFilter(String body) {
        int comma = body.indexOf(',');
        if (comma < 0) {
            return ParsingResult.error("FILTER expects 'field, value'");
        }
        String fieldName = body.substring(0, comma);
        String after = body.substring(comma + 1);
        if (after.isEmpty() || after.charAt(0) != ' ') {
            return ParsingResult.error("space must be given after field name");
        }
        String value = removeSpaces(after);

        Field field = Field.Searcher(fieldName);
        if (field == null) {
            return ParsingResult.error("Unknown field");
        }

        FieldGetter getter = field.getter();
        return ParsingResult.of(product -> getter.getFieldValue(product).equals(value));
    }
    private static ParsingResult<Comparator<Product>> parseOrder(String body) {
        int comma = body.indexOf(',');
        if (comma < 0) {
            return ParsingResult.error("ORDER expects 'field, ASC|DESC'");
        }
        String fieldName = body.substring(0, comma);
        String after = body.substring(comma + 1);
        if (after.isEmpty() || after.charAt(0) != ' ') {
            return ParsingResult.error("space must be given after field name");
        }
        String direction = removeSpaces(after);

        Field field = Field.Searcher(fieldName);
        if (field == null) {
            return ParsingResult.error("Unknown field");
        }

        Comparator<Product> comparator = field.comparator();
        if (direction.equals("ASC")) {
            return ParsingResult.of(comparator);
        } else if (direction.equals("DESC")) {
            return ParsingResult.of(comparator.reversed());
        } else {
            return ParsingResult.error("ORDER must be 'ASC' or 'DESC'");
        }
    }



    }
