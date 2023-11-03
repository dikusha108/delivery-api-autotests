package model;

public class BaseResponse {
    private Integer id;
    private String res;

    public BaseResponse() {}

    public BaseResponse(Integer id, String res) {
        this.id = id;
        this.res = res;
    }

    public BaseResponse(String res) {
        this.res = res;
    }

    public Integer getId() {
        return id;
    }

    public String getRes() {
        return res;
    }
}
