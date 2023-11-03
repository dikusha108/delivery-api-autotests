package model.delivery;

public class PostDeliveryCheckAddressResponse {
    private Integer delivery_check_id;
    private String address_status;
    private String delivery_status;
    private String delivery_not_allowed_cause;
    private String federal_district;
    private String transport_company;

    public PostDeliveryCheckAddressResponse(
            String address_status,
            String delivery_status,
            String delivery_not_allowed_cause,
            String federal_district,
            String transport_company
    ) {
        this.address_status = address_status;
        this.delivery_status = delivery_status;
        this.delivery_not_allowed_cause = delivery_not_allowed_cause;
        this.federal_district = federal_district;
        this.transport_company = transport_company;
    }

    public Integer getDeliveryCheckId() {
        return delivery_check_id;
    }
}
