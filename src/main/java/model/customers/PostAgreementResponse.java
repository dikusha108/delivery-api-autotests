package model.customers;

public class PostAgreementResponse {
    private Customer customer;
    private String status;
    private String agreement_is_needed;

    public PostAgreementResponse(Customer customer) {
        this.customer = customer;
    }

    public PostAgreementResponse(Customer customer, String status, String agreement_is_needed) {
        this.customer = customer;
        this.status = status;
        this.agreement_is_needed = agreement_is_needed;
    }
}
