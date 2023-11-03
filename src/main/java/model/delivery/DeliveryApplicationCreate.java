package model.delivery;

public class DeliveryApplicationCreate {
    private Object delivery_check_id;
    private Object point_id;
    private Object invoice_id;
    private String system;
    private Object user_id;
    private String user_email;
    private Object corr_id;
    private Object corr_id_holding;
    private Object contact_person_id;
    private Object delivery_address_id;
    private String receiver_fio;
    private String receiver_organization_name;
    private String receiver_organization_inn;
    private String receiver_organization_kpp;
    private String receiver_phone;
    private String address_comment;
    private String cargo_comment;
    private String delivery_date_desired;
    private String delivery_time_from;
    private String delivery_time_to;

    public DeliveryApplicationCreate(
            Object delivery_check_id,
            Object point_id,
            Object invoice_id,
            String system,
            Object user_id,
            String user_email,
            Object corr_id,
            Object corr_id_holding,
            Object contact_person_id,
            Object delivery_address_id,
            String receiver_fio,
            String receiver_organization_name,
            String receiver_organization_inn,
            String receiver_organization_kpp,
            String receiver_phone,
            String address_comment,
            String cargo_comment,
            String delivery_date_desired,
            String delivery_time_from,
            String delivery_time_to
    ) {
        this.delivery_check_id = delivery_check_id;
        this.point_id = point_id;
        this.invoice_id = invoice_id;
        this.system = system;
        this.user_id = user_id;
        this.user_email = user_email;
        this.corr_id = corr_id;
        this.corr_id_holding = corr_id_holding;
        this.contact_person_id = contact_person_id;
        this.delivery_address_id = delivery_address_id;
        this.receiver_fio = receiver_fio;
        this.receiver_organization_name = receiver_organization_name;
        this.receiver_organization_inn = receiver_organization_inn;
        this.receiver_organization_kpp = receiver_organization_kpp;
        this.receiver_phone = receiver_phone;
        this.address_comment = address_comment;
        this.cargo_comment = cargo_comment;
        this.delivery_date_desired = delivery_date_desired;
        this.delivery_time_from = delivery_time_from;
        this.delivery_time_to = delivery_time_to;
    }

    public Object getDeliveryCheckId() {
        return delivery_check_id;
    }

    public void setDeliveryCheckId(Integer delivery_check_id) {
        this.delivery_check_id = delivery_check_id;
    }

    public void setInvoiceId(Object invoice_id) {
        this.invoice_id = invoice_id;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public void setUserId(Object user_id) {
        this.user_id = user_id;
    }

    public void setUserEmail(String user_email) {
        this.user_email = user_email;
    }

    public void setCorrId(Object corr_id) {
        this.corr_id = corr_id;
    }

    public void setCorrIdHolding(Object corr_id_holding) {
        this.corr_id_holding = corr_id_holding;
    }

    public void setContactPersonId(Object contact_person_id) {
        this.contact_person_id = contact_person_id;
    }

    public void setDeliveryAddressId(Object delivery_address_id) {
        this.delivery_address_id = delivery_address_id;
    }

    public void setReceiverFio(String receiver_fio) {
        this.receiver_fio = receiver_fio;
    }

    public void setReceiverOrganizationName(String receiver_organization_name) {
        this.receiver_organization_name = receiver_organization_name;
    }

    public void setReceiverOrganizationInn(String receiver_organization_inn) {
        this.receiver_organization_inn = receiver_organization_inn;
    }

    public void setReceiverOrganizationKpp(String receiver_organization_kpp) {
        this.receiver_organization_kpp = receiver_organization_kpp;
    }

    public void setReceiverPhone(String receiver_phone) {
        this.receiver_phone = receiver_phone;
    }

    public void setAddressComment(String address_comment) {
        this.address_comment = address_comment;
    }

    public void setCargoComment(String cargo_comment) {
        this.cargo_comment = cargo_comment;
    }

    public void setDeliveryDateDesired(String delivery_date_desired) {
        this.delivery_date_desired = delivery_date_desired;
    }

    public void setDeliveryTimeFrom(String delivery_time_from) {
        this.delivery_time_from = delivery_time_from;
    }

    public void setDeliveryTimeTo(String delivery_time_to) {
        this.delivery_time_to = delivery_time_to;
    }
}
