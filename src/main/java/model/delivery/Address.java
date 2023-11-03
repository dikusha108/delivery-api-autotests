package model.delivery;

public class Address {
    private String address;
    private Boolean check_for_delivery;

    public Address(String address) {
        this.address = address;
    }

    public Address(String address, Boolean check_for_delivery) {
        this.address = address;
        this.check_for_delivery = check_for_delivery;
    }
}
