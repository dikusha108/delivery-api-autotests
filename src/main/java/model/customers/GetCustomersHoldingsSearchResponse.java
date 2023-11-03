package model.customers;

import java.util.List;

public class GetCustomersHoldingsSearchResponse {
    private Boolean has_agreement;
    private List<Customer> customers;

    public GetCustomersHoldingsSearchResponse(Boolean has_agreement, List<Customer> customers) {
        this.has_agreement = has_agreement;
        this.customers = customers;
    }

    public List<Customer> getCustomers() {
        return customers;
    }
}
