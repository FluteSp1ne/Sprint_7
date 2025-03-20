import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class CreatingOrderTest extends BaseTest {

    private static final String FIRST_NAME = "Alan";
    private static final String LAST_NAME = "Wake";
    private static final String ADDRESS = "OceanView, 0";
    private static final String METRO_STATION = "Kaldera";
    private static final String PHONE = "+7 903 022 10 01";
    private static final int RENT_TIME = 5;
    private static final String DELIVERY_DATE = "2025-09-09";
    private static final String COMMENT = "Where are you, Alice?";

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
    public void creatingOrder() {
        String[] colorsArray = color.isEmpty() ? new String[]{} : color.split(",");
        String orderData = OrderSteps.createOrderData(FIRST_NAME, LAST_NAME, ADDRESS, METRO_STATION, PHONE, RENT_TIME, DELIVERY_DATE, COMMENT, colorsArray);
        trackId = OrderSteps.sendOrderRequest(orderData);
    }

    @After
    @Step("Отмена заказа")
    public void cancelOrder() {
        if (trackId != null) {
            OrderSteps.cancelOrder(trackId);
        }
    }
}





