package querylang.util;

import querylang.db.Product;

// ИНТЕРФЕЙС ЗАПРЕЩАЕТСЯ ИЗМЕНЯТЬ!
@FunctionalInterface
public interface FieldGetter {
    String getFieldValue(Product product);
}
