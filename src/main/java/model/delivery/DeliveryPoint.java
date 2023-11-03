package model.delivery;

public class DeliveryPoint {
    private Integer id;
    private String name;
    private String address;
    private String lat;
    private String lng;
    private String url;
    private String phone;
    private String description;
    private String city;
    private String streetType;
    private String street;
    private String house;
    private String transport_company;
    private Boolean transport_company_is_active;
    private Boolean no_weight_limits;
    private Boolean multiplace;
    private Boolean city_fias_id_is_valid;

    public DeliveryPoint(String name, String address, String lat, String lng, String url, String phone, String description) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.url = url;
        this.phone = phone;
        this.description = description;
    }

    public DeliveryPoint(Integer id, String name, String address, String lat, String lng, String url, String phone, String description, String city, String streetType, String street, String house, String transport_company, Boolean transport_company_is_active, Boolean no_weight_limits, Boolean multiplace, Boolean city_fias_id_is_valid) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.url = url;
        this.phone = phone;
        this.description = description;
        this.city = city;
        this.streetType = streetType;
        this.street = street;
        this.house = house;
        this.transport_company = transport_company;
        this.transport_company_is_active = transport_company_is_active;
        this.no_weight_limits = no_weight_limits;
        this.multiplace = multiplace;
        this.city_fias_id_is_valid = city_fias_id_is_valid;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getUrl() {
        return url;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getCity() {
        return city;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "DeliveryPoint{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", url='" + url + '\'' +
                ", phone='" + phone + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
