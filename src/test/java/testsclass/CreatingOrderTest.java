package testsclass;

import dataclass.CreateOrder;
import stepsclass.BaseTest;
import stepsclass.OrderSteps;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CreatingOrderTest extends BaseTest {

    private String trackId;
    private String color;
    private String description;

    public CreatingOrderTest(String color, String description) {
        this.color = color;
        this.description = description;
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                { "", "Создание заказа без выбора цвета" },
                { "BLACK", "Создание заказа с выбором цвета черного" },
                { "GREY", "Создание заказа с выбором цвета серого" },
                { "GREY,BLACK", "Создание заказа с выбором нескольких цветов" }
        });
    }

    @Test
    @Step("Тесты на создание заказа с разными параметрами выбора цвета")
    public void testCreateNewOrder() {
        List<String> colors = Arrays.asList("BLACK", "GREY");
        CreateOrder orderRequest = new CreateOrder("Alan", "Wake", "OceanView, 0", "Kaldera",
                "+7 903 022 10 01", 5, "2025-09-09", "Where are you, Alice?", colors);

        Response response = OrderSteps.createOrder(orderRequest);

        assertEquals(201, response.getStatusCode());
        assertTrue(response.jsonPath().getString("track") != null);

        trackId = response.jsonPath().getString("track");
    }


    @After
    @Step("Отмена заказа")
    public void cancelOrder() {
        if (trackId != null) {
            Response response = OrderSteps.cancelOrder(trackId);
            assertEquals(200, response.getStatusCode());
            boolean ok = response.jsonPath().getBoolean("ok");
            assertTrue(ok);
        }
    }
}





