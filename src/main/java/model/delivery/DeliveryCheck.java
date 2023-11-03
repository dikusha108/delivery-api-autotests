package model.delivery;

public class DeliveryCheck {
    private Integer id;
    private String  address;
    private String  address_status;
    private String  locality_status;
    private String  delivery_status;

    public DeliveryCheck(Integer id, String address_status, String locality_status, String delivery_status) {
        this.id = id;
        this.address_status = address_status;
        this.locality_status = locality_status;
        this.delivery_status = delivery_status;
    }

    public DeliveryCheck(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
