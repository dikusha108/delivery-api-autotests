package data;

import model.errors.BaseResponseError;
import model.errors.Detail;
import model.errors.ValidationError;

import java.util.ArrayList;
import java.util.List;

public class Errors {
    // response для ошибки с некорректным токеном
    public static BaseResponseError getIncorrectTokenError() {
        return new BaseResponseError("Передан неверный токен авторизации!");
    }

    // response для ошибки валидации при передаче не валидного целочисленного значения (422)
    public static ValidationError getValidationError(String ...types) {
        List<Detail> details = new ArrayList<>();
        for (String type : types) {
            details.add(new Detail(type));
        }
        return new ValidationError(details);
    }

    // response для ошибки с не существующим ID проверки / заявки на доставку
    public static BaseResponseError getUnexistingApplicationError() {
        return new BaseResponseError("Некорректный ID проверки / заявки на доставку или ID накладной!");
    }

    // response для ошибки с не доступным адресом
    public static BaseResponseError getUnavailableAddressError() {
        return new BaseResponseError("Доставка по данному адресу недоступна!");
    }
}