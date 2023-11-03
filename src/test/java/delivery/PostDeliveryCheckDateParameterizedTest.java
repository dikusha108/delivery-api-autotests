package delivery;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import model.delivery.Address;
import model.delivery.CheckDate;
import model.delivery.PostDeliveryCheckAddressResponse;
import model.delivery.Product;
import model.errors.BaseResponseError;
import model.errors.ValidationError;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static data.Errors.getUnavailableAddressError;
import static data.Errors.getValidationError;
import static data.TestParams.getMaterialId1;
import static steps.BaseSteps.checkBody;
import static steps.DeliverySteps.*;

@Epic("/delivery")
@Feature("/check/address")
public class PostDeliveryCheckDateParameterizedTest {
    @ParameterizedTest
    @ValueSource(strings = {"qwe", ""})
    @DisplayName("Проверка даты доставки с не валидным  delivery_address_check_id")
    public void postDeliveryCheckDateWithInvalidDeliveryCheckId(String param) {
        // Отправляем запрос на проверку даты доставки
        ValidationError actual = postDeliveryCheckDate(
                "correct_token",
                new CheckDate(param, List.of(new Product(getMaterialId1(), 1))),
                422,
                ValidationError.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, getValidationError("type_error.integer", "value_error"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"qwe", ""})
    @DisplayName("Проверка даты доставки с не валидным  delivery_point_id")
    public void postDeliveryCheckDateWithInvalidDeliveryPointId(String param) {
        // Отправляем запрос на проверку даты доставки
        ValidationError actual = postDeliveryCheckDate(
                "correct_token",
                new CheckDate(null, param, List.of(new Product(getMaterialId1(), 1))),
                422,
                ValidationError.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, getValidationError("type_error.integer", "value_error"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Московский проспект 119, Витебск, Беларусь",
            "ул. Авиамоторная, д. 55 стр.1, Москва",
            "ул. Ленина, 6, Гвардейск, Калининградская обл.",
            "123123123йцфв1в2"})
    @DisplayName("Проверка даты доставки с delivery_address_check_id недоступным для доставки ")
    @Disabled("Доставка до двери пока отключена")
    public void postDeliveryCheckDateWithMoscowDeliveryCheckId(String param) {
        // Проверяем возможность доставки по адреса
        PostDeliveryCheckAddressResponse deliveryCheckAddress = postDeliveryCheckAddress(
                "correct_token",
                new Address(param),
                200,
                PostDeliveryCheckAddressResponse.class
        );

        // Отправляем запрос на проверку даты доставки
        BaseResponseError actual = postDeliveryCheckDate(
                "correct_token",
                new CheckDate(deliveryCheckAddress.getDeliveryCheckId(), List.of(new Product(getMaterialId1(), 1))),
                403,
                BaseResponseError.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, getUnavailableAddressError());
    }

    @ParameterizedTest
    @ValueSource(strings = {"qwe", ""})
    @DisplayName("Проверка даты доставки с не валидным  product_id")
    public void postDeliveryCheckDateWithInvalidProductId(String param) {
        // Отправляем запрос на проверку даты доставки
        ValidationError actual = postDeliveryCheckDate(
                "correct_token",
                new CheckDate(getDeliveryCheckId(), List.of(new Product(param, 1))),
                422,
                ValidationError.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, getValidationError("type_error.integer"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"qwe", ""})
    @DisplayName("Проверка даты доставки с не валидным  quantity")
    public void postDeliveryCheckDateWithInvalidQuantity(String param) {
        // Отправляем запрос на проверку даты доставки
        ValidationError actual = postDeliveryCheckDate(
                "correct_token",
                new CheckDate(getDeliveryCheckId(), List.of(new Product(getMaterialId1(), param))),
                422,
                ValidationError.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, getValidationError("type_error.integer"));
    }
}
