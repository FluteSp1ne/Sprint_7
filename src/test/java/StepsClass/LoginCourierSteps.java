package StepsClass;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class LoginCourierSteps {

    private final static String BASE_URL = "/api/v1/courier/login";

    @Step("Вход курьера с логином и паролем")
    public static ValidatableResponse loginCourier(String login, String password) {
        return given()
                .header("Content-type", "application/json")
                .body(String.format("{\"login\": \"%s\", \"password\": \"%s\"}", login, password))
                .when()
                .post(BASE_URL)
                .then();
    }

    @Step("Попытка входа курьера без одного из полей с данными '{jsonBody}'")
    public static Response loginWithoutField(String jsonBody) {
        return given()
                .header("Content-type", "application/json")
                .body(jsonBody)
                .when()
                .post(BASE_URL)
                .then()
                .extract().response();
    }

}
