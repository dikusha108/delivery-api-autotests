package delivery;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import model.delivery.CheckDay;
import model.delivery.PostDeliveryCheckDayResponse;
import model.errors.BaseResponseError;
import model.errors.ValidationError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static data.Errors.getIncorrectTokenError;
import static data.Errors.getValidationError;
import static data.TestParams.*;
import static steps.BaseSteps.checkBody;
import static steps.DeliverySteps.postDeliveryCheckDay;

@Epic("/delivery")
@Feature("/check/day")
public class PostDeliveryCheckDayTest {
    @Test
    @DisplayName("Проверка даты с рабочим днем")
    public void postDeliveryCheckDayWithWorkingDay() {
        String day = getRandomString(getWorkingDays());
        // Отправляем запрос на проверку даты
        PostDeliveryCheckDayResponse actual = postDeliveryCheckDay(
                "correct_token",
                new CheckDay(day),
                200,
                PostDeliveryCheckDayResponse.class
        );

        // Ожидаемый результат
        PostDeliveryCheckDayResponse expected = new PostDeliveryCheckDayResponse(day, true);

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, expected);
    }

    @Test
    @DisplayName("Проверка даты с выходным днем")
    public void postDeliveryCheckDayWithWeekend() {
        String day = getRandomString(getWeekends());
        // Отправляем запрос на проверку даты
        PostDeliveryCheckDayResponse actual = postDeliveryCheckDay(
                "correct_token",
                new CheckDay(day),
                200,
                PostDeliveryCheckDayResponse.class
        );

        // Ожидаемый результат
        PostDeliveryCheckDayResponse expected = new PostDeliveryCheckDayResponse(day, false);

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, expected);
    }

    @Test
    @DisplayName("Проверка даты с праздничным днем")
    public void postDeliveryCheckDayWithHoliday() {
        String day = getRandomString(getHolidays());
        // Отправляем запрос на проверку даты
        PostDeliveryCheckDayResponse actual = postDeliveryCheckDay(
                "correct_token",
                new CheckDay(day),
                200,
                PostDeliveryCheckDayResponse.class
        );

        // Ожидаемый результат
        PostDeliveryCheckDayResponse expected = new PostDeliveryCheckDayResponse(day, false);

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, expected);
    }

    @Test
    @DisplayName("Проверка даты с прошедшим днем")
    public void postDeliveryCheckDayWithPastDay() {
        String day = "2022-01-02";
        // Отправляем запрос на проверку даты
        PostDeliveryCheckDayResponse actual = postDeliveryCheckDay(
                "correct_token",
                new CheckDay(day),
                200,
                PostDeliveryCheckDayResponse.class
        );

        // Ожидаемый результат
        PostDeliveryCheckDayResponse expected = new PostDeliveryCheckDayResponse(day, null);

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, expected);
    }

    @Test
    @DisplayName("Проверка даты с не валидным day")
    public void postDeliveryCheckDayWithInvalidDay() {
        // Отправляем запрос на проверку даты
        ValidationError actual = postDeliveryCheckDay(
                "correct_token",
                new CheckDay("2022-24-02"),
                422,
                ValidationError.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, getValidationError("value_error.date"));
    }

    @Test
    @DisplayName("Проверка даты с пустым day")
    public void postDeliveryCheckDayWithEmptyDay() {
        // Отправляем запрос на проверку даты
        ValidationError actual = postDeliveryCheckDay(
                "correct_token",
                new CheckDay(""),
                422,
                ValidationError.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, getValidationError("value_error.date"));
    }

    @Test
    @DisplayName("Проверка даты с не корректным токеном")
    public void postDeliveryCheckDayWithIncorrectToken() {
        // Отправляем запрос на проверку даты
        BaseResponseError actual = postDeliveryCheckDay(
                "incorrect_token",
                new CheckDay(getRandomString(getHolidays())),
                401,
                BaseResponseError.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, getIncorrectTokenError());
    }

}
