package model.customers;

public class Contract {
    private Integer id;
    private String number;
    private String date;
    private Integer type_id;
    private String type_name;

    public Contract(Integer id, String number, String date, Integer type_id, String type_name) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.type_id = type_id;
        this.type_name = type_name;
    }
}
