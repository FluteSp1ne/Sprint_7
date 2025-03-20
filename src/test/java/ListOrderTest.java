import io.qameta.allure.Step;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.notNullValue;

public class ListOrderTest extends BaseTest {

    private final static String BASE_URL = "/api/v1/orders";

    @Test
    @Step("Получение списка заказов")
    public void testGetListOrder() {
        sendGetOrderRequest();
    }

    @Step("Отправка GET-запроса для получения списка заказов")
    private void sendGetOrderRequest() {
        given()
                .header("Content-type", "application/json")
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}

