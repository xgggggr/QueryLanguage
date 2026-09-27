package querylang.db;

import java.util.Objects;

// КЛАСС ЗАПРЕЩАЕТСЯ ИЗМЕНЯТЬ!
public final class Product {
    private final int id;
    private final String name;
    private final String manufacturer;
    private final String city;
    private final int quantity;

    public Product(int id, String name, String manufacturer, String city, int quantity) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.city = city;
        this.quantity = quantity;
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String manufacturer() {
        return manufacturer;
    }

    public String city() {
        return city;
    }

    public int quantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Product that = (Product) obj;
        return this.id == that.id &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.manufacturer, that.manufacturer) &&
                Objects.equals(this.city, that.city) &&
                this.quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, manufacturer, city, quantity);
    }

    @Override
    public String toString() {
        return "Product[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "manufacturer=" + manufacturer + ", " +
                "city=" + city + ", " +
                "quantity=" + quantity + ']';
    }
}
