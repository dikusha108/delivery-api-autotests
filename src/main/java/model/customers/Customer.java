package model.customers;

public class Customer {
    private String system;
    private String user_id;
    private String corr_id_holding;
    private String delivery_is_available;

    public Customer(String system, String user_id, String corr_id_holding, String delivery_is_available) {
        this.system = system;
        this.user_id = user_id;
        this.corr_id_holding = corr_id_holding;
        this.delivery_is_available = delivery_is_available;
    }

    public Customer(String system, String user_id, String corr_id_holding) {
        this.system = system;
        this.user_id = user_id;
        this.corr_id_holding = corr_id_holding;
    }

    public Customer() {
    }

    public String getDeliveryIsAvailable() {
        return delivery_is_available;
    }
}
