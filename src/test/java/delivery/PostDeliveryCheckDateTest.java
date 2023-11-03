package delivery;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import model.delivery.CheckDate;
import model.delivery.PostDeliveryCheckDateResponse;
import model.delivery.Product;
import model.errors.BaseResponseError;
import model.errors.ValidationError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.List;

import static data.Errors.getIncorrectTokenError;
import static data.Errors.getValidationError;
import static data.TestParams.getDeliveryPointId;
import static data.TestParams.getMaterialId1;
import static steps.BaseSteps.checkBody;
import static steps.DeliverySteps.getDeliveryCheckId;
import static steps.DeliverySteps.postDeliveryCheckDate;

@Epic("/delivery")
@Feature("/check/date")
@Execution(ExecutionMode.SAME_THREAD)
public class PostDeliveryCheckDateTest {
    @Test
    @DisplayName("Проверка даты доставки с валидными параметрами с delivery_check_id")
    public void postDeliveryCheckDateWithDeliveryCheckId() {
        // Отправляем запрос на проверку даты доставки
        BaseResponseError actual = postDeliveryCheckDate(
                "correct_token",
                new CheckDate(getDeliveryCheckId(), List.of(new Product(getMaterialId1(), 1))),
                403, //200,
                BaseResponseError.class//PostDeliveryCheckDateResponse.class
        );

        checkBody(actual, new BaseResponseError("Доставка до двери недоступна на данный момент - только доставка до ПВЗ"));
    }

    @Test
    @DisplayName("Проверка даты доставки с валидными параметрами с delivery_point_id")
    public void postDeliveryCheckDateWithDeliveryPointId() {
        // Отправляем запрос на проверку даты доставки
        PostDeliveryCheckDateResponse actual = postDeliveryCheckDate(
                "correct_token",
                new CheckDate(null, getDeliveryPointId(), List.of(new Product(getMaterialId1(), 1))),
                200,
                PostDeliveryCheckDateResponse.class
        );

        //checkBody(actual, null);
    }

    @Test
    @DisplayName("Проверка даты доставки с валидными параметрами с delivery_check_id и delivery_point_id")
    public void postDeliveryCheckDateWithDeliveryCheckIdAndDeliveryPointId() {
        // Отправляем запрос на проверку даты доставки
        ValidationError actual = postDeliveryCheckDate(
                "correct_token",
                new CheckDate(getDeliveryCheckId(), getDeliveryPointId(), List.of(new Product(getMaterialId1(), 1))),
                422,
                ValidationError.class
        );

        checkBody(actual, getValidationError("value_error"));
    }

    @Test
    @DisplayName("Проверка даты доставки с не существующим delivery_address_check_id")
    public void postDeliveryCheckDateWithUnexistingDeliveryCheckId() {
        // Отправляем запрос на проверку даты доставки
        BaseResponseError actual = postDeliveryCheckDate(
                "correct_token",
                new CheckDate(getDeliveryCheckId() + "123", List.of(new Product(getMaterialId1(), 1))),
                403,
                BaseResponseError.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, new BaseResponseError("Доставка до двери недоступна на данный момент - только доставка до ПВЗ"));
        //checkBody(actual, new BaseResponseError("Некорректный ID проверки возможности доставки!"));
    }

    @Test
    @DisplayName("Проверка даты доставки с не существующим delivery_point_id")
    public void postDeliveryCheckDateWithUnexistingDeliveryPointId() {
        // Отправляем запрос на проверку даты доставки
        BaseResponseError actual = postDeliveryCheckDate(
                "correct_token",
                new CheckDate(null,"000123", List.of(new Product(getMaterialId1(), 1))),
                403,
                BaseResponseError.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, new BaseResponseError("Некорректный ID ПВЗ!"));
    }

    @Test
    @DisplayName("Проверка даты доставки с пустым products")
    public void postDeliveryCheckDateWithEmptyProducts() {
        // Отправляем запрос на проверку даты доставки
        ValidationError actual = postDeliveryCheckDate(
                "correct_token",
                new CheckDate(getDeliveryCheckId(), List.of()),
                422,
                ValidationError.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, getValidationError("value_error.list.min_items"));
    }

    @Test
    @DisplayName("Проверка даты доставки с не корректным токеном")
    public void postDeliveryCheckDateWithIncorrectToken() {
        // Отправляем запрос на проверку даты доставки
        BaseResponseError actual = postDeliveryCheckDate(
                "incorrect_token",
                new CheckDate(getDeliveryCheckId(), List.of(new Product(getMaterialId1(), 1))),
                401,
                BaseResponseError.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, getIncorrectTokenError());
    }
}
