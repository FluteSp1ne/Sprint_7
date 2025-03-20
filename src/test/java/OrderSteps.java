import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class OrderSteps {
    private final static String BASE_URL = "/api/v1/orders";

    public static String createOrderData(String firstName, String lastName, String address, String metroStation,
                                         String phone, int rentTime, String deliveryDate, String comment, String[] colors) {
        String colorArray = String.join("\",\"", colors);
        return String.format("{\"firstName\": \"%s\", \"lastName\": \"%s\", \"address\": \"%s\", \"metroStation\": \"%s\", " +
                        "\"phone\": \"%s\", \"rentTime\": %d, \"deliveryDate\": \"%s\", \"comment\": \"%s\", \"color\": [\"%s\"]}",
                firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colorArray);
    }

    public static String sendOrderRequest(String orderData) {
        return given()
                .header("Content-type", "application/json")
                .body(orderData)
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(201)
                .body("$", hasKey("track"))
                .extract().path("track").toString();
    }

    public static void cancelOrder(String trackId) {
        given()
                .header("Content-type", "application/json")
                .body("{\"track\": \"" + trackId + "\"}")
                .when()
                .put("/api/v1/orders/cancel")
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }
}
