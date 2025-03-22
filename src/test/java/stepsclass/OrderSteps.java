package StepsClass;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderSteps {
    private final static String BASE_URL = "/api/v1/orders";

    @Step("Создание заказа")
    public static String createOrderData(String firstName, String lastName, String address, String metroStation,
                                         String phone, int rentTime, String deliveryDate, String comment, String[] colors) {
        String colorArray = String.join("\",\"", colors);
        return String.format("{\"firstName\": \"%s\", \"lastName\": \"%s\", \"address\": \"%s\", \"metroStation\": \"%s\", " +
                        "\"phone\": \"%s\", \"rentTime\": %d, \"deliveryDate\": \"%s\", \"comment\": \"%s\", \"color\": [\"%s\"]}",
                firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colorArray);
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
