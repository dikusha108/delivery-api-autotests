package model.delivery;

public class PostDeliveryCheckDateResponse {
    private Integer delivery_check_id;
    private String min_delivery_date;

    public PostDeliveryCheckDateResponse(Integer delivery_check_id) {
        this.delivery_check_id = delivery_check_id;
    }
}
