package model.delivery;

import java.util.List;

public class CheckDate {
    private Object delivery_check_id;
    private Object delivery_point_id;
    private List<Product> products;

    public CheckDate(Object delivery_check_id, List<Product> products) {
        this.delivery_check_id = delivery_check_id;
        this.products = products;
    }

    public CheckDate(Object delivery_check_id, Object delivery_point_id, List<Product> products) {
        this.delivery_check_id = delivery_check_id;
        this.delivery_point_id = delivery_point_id;
        this.products = products;
    }
}
