import com.google.gson.Gson;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CourierSteps {
    public final static String BASE_URL = "/api/v1/courier";

    public static void createCourier(String login, String password, String firstName) {
        CreateCourier courier = new CreateCourier(login, password, firstName);
        Gson gson = new Gson();

        String jsonBody = gson.toJson(courier);

        given()
                .header("Content-type", "application/json")
                .body(jsonBody)
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    public static int loginAndGetCourierId(String login, String password) {
        LoginCourier loginRequest = new LoginCourier(login, password);
        Gson gson = new Gson();
        String jsonBody = gson.toJson(loginRequest);

        return given()
                .header("Content-type", "application/json")
                .body(jsonBody)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    public static void deleteCourier(int courierId) {
        DeleteCourier deleteRequest = new DeleteCourier(courierId);
        Gson gson = new Gson();
        String jsonBody = gson.toJson(deleteRequest);

        given()
                .header("Content-type", "application/json")
                .body(jsonBody)
                .when()
                .delete(BASE_URL + "/" + courierId)
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    public static void checkMissingFields(String jsonBody) {
        given()
                .header("Content-type", "application/json")
                .body(jsonBody)
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}



