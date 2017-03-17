package ke.co.greencredit.nunua.Product;

/**
 * Created by Samson on 10/1/2015.
 */
public class Product implements java.io.Serializable {
    String id, name, unit_price, category, quantity, unit_measure, image_path, store_id;

    public Product(String id, String store_id, String name, String unit_price, String category, String quantity, String unit_measure, String image_path) {
        this.id = id;
        this.name = name;
        this.unit_price = unit_price;
        this.category = category;
        this.quantity = quantity;
        this.unit_measure = unit_measure;
        this.image_path = image_path;
        this.store_id = store_id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit_measure() {
        return unit_measure;
    }

    public void setUnit_measure(String unit_measure) {
        this.unit_measure = unit_measure;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }
}