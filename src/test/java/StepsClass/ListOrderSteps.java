package StepsClass;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class ListOrderSteps {

    private final static String BASE_URL = "/api/v1/orders";

    @Step("Отправка GET-запроса для получения списка заказов")
    public static Response sendGetOrderRequest() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(BASE_URL)
                .then()
                .extract().response();
    }
}
