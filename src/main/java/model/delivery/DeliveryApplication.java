package model.delivery;

import java.util.Objects;

public class DeliveryApplication {
    private String bill_date;
    private String bill_number;
    private String delivery_address;
    private String delivery_date_plan;
    private String invoice_date;
    private String invoice_number;
    private String operator_name;
    private String track_number;
    private String track_url;
    private String user_id;
    private Object cargo_places;
    private Integer address_check_id;
    private Integer invoice_id;
    private Integer delivery_application_id;
    private DeliveryPoint point;

    public DeliveryApplication(String bill_date, String bill_number, String delivery_address, String delivery_date_plan, String invoice_date, String invoice_number, String operator_name, String user_id, Object cargo_places, Integer address_check_id, Integer invoice_id, Integer delivery_application_id) {
        this.bill_date = bill_date;
        this.bill_number = bill_number;
        this.delivery_address = delivery_address;
        this.delivery_date_plan = delivery_date_plan;
        this.invoice_date = invoice_date;
        this.invoice_number = invoice_number;
        this.operator_name = operator_name;
        this.user_id = user_id;
        this.cargo_places = cargo_places;
        this.address_check_id = address_check_id;
        this.invoice_id = invoice_id;
        this.delivery_application_id = delivery_application_id;
    }

    public DeliveryApplication(String bill_date, String bill_number, String delivery_address, String delivery_date_plan, String invoice_date, String invoice_number, String operator_name, String track_number, String track_url, String user_id, Object cargo_places, Integer address_check_id, Integer invoice_id, Integer delivery_application_id) {
        this.bill_date = bill_date;
        this.bill_number = bill_number;
        this.delivery_address = delivery_address;
        this.delivery_date_plan = delivery_date_plan;
        this.invoice_date = invoice_date;
        this.invoice_number = invoice_number;
        this.operator_name = operator_name;
        this.track_number = track_number;
        this.track_url = track_url;
        this.user_id = user_id;
        this.cargo_places = cargo_places;
        this.address_check_id = address_check_id;
        this.invoice_id = invoice_id;
        this.delivery_application_id = delivery_application_id;
    }

    public DeliveryApplication() {
    }

    public Integer getDeliveryCheck() {
        return address_check_id;
    }

    public Integer getInvoiceId() {
        return invoice_id;
    }

    public Integer getId() {
        return delivery_application_id;
    }

    public DeliveryPoint getPoint() {
        return point;
    }

    public String getTrackNumber() {
        return track_number;
    }

    public String getTrackUrl() {
        return track_url;
    }

    public void setPoint(DeliveryPoint point) {
        this.point = point;
    }

    public void setDeliveryAddress(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public void setCargoPlaces(Object cargo_places) {
        this.cargo_places = cargo_places;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryApplication that = (DeliveryApplication) o;
        if (this.point != null){
            return Objects.equals(bill_date, that.bill_date)
                && Objects.equals(bill_number, that.bill_number)
                && Objects.equals(delivery_address, that.delivery_address)
                && Objects.equals(delivery_date_plan, that.delivery_date_plan)
                && Objects.equals(invoice_date, that.invoice_date)
                && Objects.equals(invoice_number, that.invoice_number)
                && Objects.equals(operator_name, that.operator_name)
                && Objects.equals(track_number, that.track_number)
                && Objects.equals(track_url, that.track_url)
                && Objects.equals(user_id, that.user_id)
                && Objects.equals(cargo_places, that.cargo_places)
                && Objects.equals(point.getName(), that.point.getName())
                && Objects.equals(point.getAddress(), that.point.getAddress())
                && Objects.equals(point.getLat(), that.point.getLat())
                && Objects.equals(point.getLng(), that.point.getLng())
                && Objects.equals(point.getUrl(), that.point.getUrl())
                && Objects.equals(point.getPhone(), that.point.getPhone())
                && Objects.equals(point.getDescription(), that.point.getDescription());
        }
        else {
            return Objects.equals(bill_date, that.bill_date)
                && Objects.equals(bill_number, that.bill_number)
                && Objects.equals(delivery_address, that.delivery_address)
                && Objects.equals(delivery_date_plan, that.delivery_date_plan)
                && Objects.equals(invoice_date, that.invoice_date)
                && Objects.equals(invoice_number, that.invoice_number)
                && Objects.equals(operator_name, that.operator_name)
                && Objects.equals(track_number, that.track_number)
                && Objects.equals(track_url, that.track_url)
                && Objects.equals(user_id, that.user_id)
                && Objects.equals(cargo_places, that.cargo_places);
        }

    }

    @Override
    public String toString() {
        return "DeliveryApplication{" +
                "bill_date='" + bill_date + '\'' +
                ", bill_number='" + bill_number + '\'' +
                ", delivery_address='" + delivery_address + '\'' +
                ", delivery_date_plan='" + delivery_date_plan + '\'' +
                ", invoice_date='" + invoice_date + '\'' +
                ", invoice_number='" + invoice_number + '\'' +
                ", operator_name='" + operator_name + '\'' +
                ", track_number='" + track_number + '\'' +
                ", track_url='" + track_url + '\'' +
                ", user_id='" + user_id + '\'' +
                ", cargo_places=" + cargo_places +
                ", address_check_id=" + address_check_id +
                ", invoice_id=" + invoice_id +
                ", delivery_application_id=" + delivery_application_id +
                ", point=" + point +
                '}';
    }
}
