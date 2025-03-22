package stepsclass;

import com.google.gson.Gson;
import dataclass.CreateOrder;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderSteps {
    private final static String BASE_URL = "/api/v1/orders";

    @Step("Создание заказа")
    public static Response createOrder(CreateOrder orderRequest) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(orderRequest);

        return given()
                .header("Content-type", "application/json")
                .body(jsonBody)
                .when()
                .post(BASE_URL)
                .then()
                .extract().response();
    }

    @Step("Получение заказа")
    public static Response sendOrderRequest(String orderData) {
        return given()
                .header("Content-type", "application/json")
                .body(orderData)
                .when()
                .post(BASE_URL)
                .then()
                .extract().response();
    }


    @Step("Отмена заказа по {trackId}")
    public static Response cancelOrder(String trackId) {
        return given()
                .header("Content-type", "application/json")
                .body("{\"track\": \"" + trackId + "\"}")
                .when()
                .put("/api/v1/orders/cancel?track=" + trackId)
                .then()
                .extract().response();
    }
}
