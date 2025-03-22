package stepsclass;

import dataclass.CreateCourier;
import dataclass.DeleteCourier;
import dataclass.LoginCourier;
import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CourierSteps {
    public final static String BASE_URL = "/api/v1/courier";

    @Step("Создание курьера")
    public static Response createCourier(CreateCourier courier) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(courier);

        return given()
                .header("Content-type", "application/json")
                .body(jsonBody)
                .when()
                .post(BASE_URL)
                .then()
                .extract().response();
    }

    @Step("Вход курьера и получение {id}")
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
                .extract()
                .path("id");
    }

    @Step("Удаление курьера по {courierId}")
    public static Response deleteCourier(int courierId) {
        DeleteCourier deleteRequest = new DeleteCourier(courierId);
        Gson gson = new Gson();
        String jsonBody = gson.toJson(deleteRequest);

        return given()
                .header("Content-type", "application/json")
                .body(jsonBody)
                .when()
                .delete(BASE_URL + "/" + courierId)
                .then()
                .extract().response();
    }

    @Step("Создание курьера с пропущенным полем")
    public static Response checkMissingFields(String login, String password, String firstName) {
        CreateCourier courier = new CreateCourier(login, password, firstName);
        Gson gson = new Gson();

        String jsonBody = gson.toJson(courier);

        return given()
                .header("Content-type", "application/json")
                .body(jsonBody)
                .when()
                .post(BASE_URL)
                .then()
                .extract().response();
    }
}



