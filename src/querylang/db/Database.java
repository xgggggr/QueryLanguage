package querylang.db;

import java.util.ArrayList;
import java.util.List;

// КЛАСС ЗАПРЕЩАЕТСЯ ИЗМЕНЯТЬ!
public class Database {
    private final List<Product> products = new ArrayList<>();
    private int nextId = 0;

    /**
     * Получение неизменяемого списка всех товаров в базе данных.
     */
    public List<Product> getAll() {
        return List.copyOf(products);
    }

    /**
     * Добавляет товар в базу данных.
     * Идентификатор товару присваивается автоматически.
     * Возвращается идентификатор добавленного товара.
     */
    public int add(Product product) {
        int id = nextId++;
        product = new Product(id, product.name(), product.manufacturer(), product.city(), product.quantity());
        products.add(product);
        return id;
    }

    /**
     * Удаляет товар с указанным идентификатором.
     * @return false, если такого товара не существует;
     *         true, если товар существует и был успешно удалён.
     */
    public boolean remove(int id) {
        return products.removeIf(product -> id == product.id());
    }

    /**
     * Возвращает количество записей в базе данных.
     */
    public int size() {
        return products.size();
    }

    /**
     * Очищает базу данных.
     */
    public void clear() {
        products.clear();
    }
}
