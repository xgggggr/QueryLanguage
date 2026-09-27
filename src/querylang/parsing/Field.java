package querylang.parsing;

import querylang.db.Product;
import querylang.util.FieldGetter;

import java.util.Comparator;
import java.util.List;

public class Field {
    private final String fieldName;
    private final FieldGetter getter;
    private final Comparator<Product> comparator;
    private Field(String fieldName, FieldGetter getter, Comparator<Product> comparator){
        this.fieldName = fieldName;
        this.getter = getter;
        this.comparator = comparator;
    }
    public String fieldName(){
        return fieldName;
    }
    public FieldGetter getter(){
        return getter;
    }
    public Comparator<Product> comparator(){
        return comparator;
    }
    private static final Field ID = new Field("id", product -> String.valueOf(product.id()), Comparator.comparingInt(Product::id));

    private static final Field NAME = new Field("name", Product::name, Comparator.comparing(Product::name));

    private static final Field MANUFACTURER = new Field("manufacturer", Product::manufacturer, Comparator.comparing(Product::manufacturer));

    private static final Field CITY = new Field("city", Product::city, Comparator.comparing(Product::city));

    private static final Field QUANTITY = new Field("quantity", product -> String.valueOf(product.quantity()), Comparator.comparingInt(Product::quantity));
    private static final List<Field> ALL = List.of(ID, NAME, MANUFACTURER, CITY, QUANTITY);
    public static List<Field> all() {
        return ALL;
    }
    public static Field Searcher(String name) {
        for (Field field : ALL) {
            if (field.fieldName.equals(name)) {
                return field;
            }
        }
        return null;
    }
}

