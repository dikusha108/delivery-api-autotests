package delivery;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import model.delivery.Address;
import model.delivery.PostDeliveryCheckAddressResponse;
import model.errors.BaseResponseError;
import model.errors.ValidationError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static data.Errors.getIncorrectTokenError;
import static data.Errors.getValidationError;
import static data.TestParams.*;
import static steps.BaseSteps.checkBody;
import static steps.DeliverySteps.postDeliveryCheckAddress;

@Epic("/delivery")
@Feature("/check/address")
@Execution(ExecutionMode.SAME_THREAD)
public class PostDeliveryCheckAddressTest {
    @Test
    @DisplayName("Проверка возможности доставки по адресу внутри г. Москва")
    public void postDeliveryCheckAddressWithMoscowAddress() {
        // Проверяем возможность доставки по адреса (фактический результат)
        PostDeliveryCheckAddressResponse actual = postDeliveryCheckAddress(
                "correct_token",
                new Address(getRandomString(getMoscowAddresses()), false),
                200,
                PostDeliveryCheckAddressResponse.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual,
                new PostDeliveryCheckAddressResponse(
                        "Moscow",
                        "not_checked",
                        null,
                        null,
                        null
                ),
                "delivery_check_id"
        );
    }

    @Test
    @DisplayName("Проверка возможности доставки по адресу вне г. Москва")
    public void postDeliveryCheckAddressWithOutsideMoscowAddress() {
        for (String address : getOutsideMoscowAddressesMap().keySet()){
            // Проверяем возможность доставки по адреса (фактический результат)
            PostDeliveryCheckAddressResponse actual = postDeliveryCheckAddress(
                    "correct_token",
                    new Address(address, false),
                    200,
                    PostDeliveryCheckAddressResponse.class
            );

            String transport_company = "";
            switch (getOutsideMoscowAddressesMap().get(address)) {
                case "Приволжский" :
                    transport_company = getPecom();
                    break;
                case "Дальневосточный" :
                    transport_company = getPecom();
                    break;
                case "Южный" :
                    transport_company = getPecom();
                    break;
                case "Северо-Кавказский" :
                    transport_company = getPecom();
                    break;
                case "Уральский" :
                    transport_company = getCdek();
                    break;
                case "Сибирский" :
                    transport_company = getCdek();
                    break;
                case "Северо-Западный" :
                    transport_company = getCdek();
                    break;
                case "Центральный" :
                    transport_company = getCdek();
                    break;
            }

            //Сравниваем фактический результат с ожидаемым
            checkBody(actual,
                    new PostDeliveryCheckAddressResponse(
                            "outside_moscow",
                            "not_checked",
                            null,
                            null,//getOutsideMoscowAddressesMap().get(address),
                            null //transport_company
                    ),
                    "delivery_check_id"
            );
        }
    }

    @Test
    @DisplayName("Проверка возможности доставки по адресу внутри г. Калининград")
    public void postDeliveryCheckAddressWithKaliningradAddress() {
        // Проверяем возможность доставки по адреса (фактический результат)
        PostDeliveryCheckAddressResponse actual = postDeliveryCheckAddress(
                "correct_token",
                new Address(getRandomString(getKaliningradAddresses()), false),
                200,
                PostDeliveryCheckAddressResponse.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual,
                new PostDeliveryCheckAddressResponse(
                        "Kaliningrad",
                        "not_checked",
                        null,//"Доставка осуществляется только в Регионы",
                        null,
                        null
                ),
                "delivery_check_id"
        );
    }

    @Test
    @DisplayName("Проверка возможности доставки по адресу вне РФ")
    public void postDeliveryCheckAddressWithOutsideRussiaAddress() {
        // Проверяем возможность доставки по адреса (фактический результат)
        PostDeliveryCheckAddressResponse actual = postDeliveryCheckAddress(
                "correct_token",
                new Address(getRandomString(getOutsideRussiaAddresses()), false),
                200,
                PostDeliveryCheckAddressResponse.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual,
                new PostDeliveryCheckAddressResponse(
                        "outside_russia",
                        "not_checked",
                        null,//"Доставка осуществляется только в Регионы",
                        null,
                        null
                ),
                "delivery_check_id"
        );
    }

    @Test
    @DisplayName("Проверка возможности доставки с не валидным адресом")
    public void postDeliveryCheckAddressWithInvalidAddress() {
        // Проверяем возможность доставки по адреса (фактический результат)
        PostDeliveryCheckAddressResponse actual = postDeliveryCheckAddress(
                "correct_token",
                new Address("123123123йцфв1в2", false),
                200,
                PostDeliveryCheckAddressResponse.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual,
                new PostDeliveryCheckAddressResponse(
                        "not_valid",
                        "not_checked",
                        null,//"Доставка осуществляется только в Регионы",
                        null,
                        null
                ),
                "delivery_check_id"
        );
    }

    @Test
    @DisplayName("Проверка возможности доставки с пустым адресом")
    public void postDeliveryCheckAddressWithEmptyAddress() {
        // Проверяем возможность доставки по адреса (фактический результат)
        ValidationError actual = postDeliveryCheckAddress(
                "correct_token",
                new Address("", false),
                422,
                ValidationError.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, getValidationError("value_error.any_str.min_length"));
    }

    @Test
    @DisplayName("Проверка возможности доставки с не корректным токеном")
    public void postDeliveryCheckAddressWithIncorrectToken() {
        // Проверяем возможность доставки по адреса (фактический результат)
        BaseResponseError actual = postDeliveryCheckAddress(
                "incorrect_token",
                new Address(getRandomString(getOutsideMoscowAddresses())),
                401,
                BaseResponseError.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, getIncorrectTokenError());
    }

    @Test
    @DisplayName("Проверка возможности доставки c check_for_delivery = true")
    public void postDeliveryCheckAddressWithCheckForDeliveryTrue() {
        // Проверяем возможность доставки по адреса (фактический результат)
        BaseResponseError actual = postDeliveryCheckAddress(
                "correct_token",
                new Address(getRandomString(getMoscowAddresses()), true),
                403,
                BaseResponseError.class
        );

        //Сравниваем фактический результат с ожидаемым
        checkBody(actual, new BaseResponseError("Доставка до двери недоступна на данный момент - только доставка до ПВЗ"));
    }
 }