package model.errors;

import java.util.List;

public class ValidationError {
    private List<Detail> detail;

    public ValidationError(List<Detail>  detail) {
        this.detail = detail;
    }
}