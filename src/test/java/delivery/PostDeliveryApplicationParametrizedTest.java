package delivery;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import model.delivery.Address;
import model.delivery.PostDeliveryCheckAddressResponse;
import model.errors.BaseResponseError;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static data.Errors.getUnavailableAddressError;
import static data.RequestBody.getDeliveryApplicationBody;
import static steps.BaseSteps.checkBody;
import static steps.DeliverySteps.postDeliveryApplication;
import static steps.DeliverySteps.postDeliveryCheckAddress;

@Epic("/delivery")
@Feature("/application")
@Execution(ExecutionMode.SAME_THREAD)
@Disabled
public class PostDeliveryApplicationParametrizedTest {
    @ParameterizedTest
    @ValueSource(strings = {
            "Московский проспект 119, Витебск, Беларусь",
            "ул. Авиамоторная, д. 55 стр.1, Москва",
            "ул. Ленина, 6, Гвардейск, Калининградская обл.",
            "123123123йцфв1в2"})
    @DisplayName("Создание заявки на доставку с delivery_check_id не доступным для заказа")
    public void postDeliveryApplicationWithUnavailableAddress(String param) {
        // Проверяем возможность доставки по адреса
        PostDeliveryCheckAddressResponse deliveryCheckAddress = postDeliveryCheckAddress(
                "correct_token",
                new Address(param),
                200,
                PostDeliveryCheckAddressResponse.class
        );

        // Создаем заявку на отправку (фактический результат)
        BaseResponseError actual = postDeliveryApplication(
                "correct_token",
                getDeliveryApplicationBody(deliveryCheckAddress.getDeliveryCheckId(), null),
                403,
                BaseResponseError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getUnavailableAddressError());
    }
}
