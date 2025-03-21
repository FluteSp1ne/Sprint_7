package StepsClass;

import io.restassured.RestAssured;
import io.qameta.allure.Step;
import org.junit.Before;

public class BaseTest {

    @Before
    @Step("Настройка тестового окружения")
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
}