package ru.yandex.praktikum.stellarburgers;

import io.qameta.allure.Description;
import ru.yandex.praktikum.stellarburgers.clients.IngredientClient;
import ru.yandex.praktikum.stellarburgers.clients.OrderClient;
import ru.yandex.praktikum.stellarburgers.clients.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import ru.yandex.praktikum.stellarburgers.models.Ingredient;
import ru.yandex.praktikum.stellarburgers.models.Order;
import ru.yandex.praktikum.stellarburgers.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;


public class CreateOrderTest {
    public User user;
    public UserClient userClient;
    public OrderClient oderClient;
    public Order oder;
    public Ingredient allIngredient;
    public IngredientClient ingredientClient;
    private String accessToken;
    public List<String> ingredients;
    Response response;

    @Before
    public void setup() {
        ingredientClient = new IngredientClient();
        allIngredient = ingredientClient.getIngredient();
        ingredients = new ArrayList<>();
        ingredients.add(allIngredient.data.get(0).get_id());
        ingredients.add(allIngredient.data.get(1).get_id());
        ingredients.add(allIngredient.data.get(2).get_id());
        user = User.getRandomUser();
        userClient = new UserClient();
        userClient.createUser(user);
        Response responseForToken = userClient.loginUser(user);
        responseForToken.then().assertThat().body("success", equalTo(true)).and().statusCode(200);
        accessToken = responseForToken.then().extract().path("accessToken");

    }
    // Тест 1: создание заказа с авторизацией
    @Test
    @DisplayName("Create order by authorized user")
    @Description("Positive test, a successful server response is 200, the response body contains success: true")
    public void createOderWithAuthorizationTest() {
        oder = new Order(ingredients);
        oderClient = new OrderClient();
        response = oderClient.createOrderByAuthorizedUser(accessToken, oder);
        response.then().assertThat().body("success", equalTo(true)).and().statusCode(200);

    }
    // Тест 2: создание заказа без авторизации
    @Test
    @DisplayName("Create order by unauthorized user")
    @Description("Positive test, a successful server response is 200, the response body contains success: true")
    public void createOderWithoutAuthorizationTest() {
        oderClient = new OrderClient();
        oder = new Order(ingredients);
        response = oderClient.createOrderByUnauthorizedUser(oder);
        response.then().assertThat().statusCode(200).and().body("success", equalTo(true));
    }
    // Тест 3: создание заказа с неверным хешем ингредиентов
    @Test
    @DisplayName("Create order with wrong hash ingredients")
    @Description("Negative test, for a request with wrong hash ingredients, the system responds with a 500 code")
    public void createOderWithIncorrectIngredient() {

        ingredients.add("Test");
        oder = new Order(ingredients);
        oderClient = new OrderClient();
        response = oderClient.createOrderByAuthorizedUser(accessToken, oder);
        response.then().assertThat().statusCode(500);
    }
    // Тест 4: создание заказа без ингредиентов
    @Test
    @DisplayName("Create order without ingredients")
    @Description("Negative test, for a request without ingredients, the system responds with a 400 code")
    public void createOderWithNullIngredient() {

        ingredients.clear();
        oder = new Order(ingredients);
        oderClient = new OrderClient();
        response = oderClient.createOrderByAuthorizedUser(accessToken, oder);
        response.then().assertThat().statusCode(400);
    }

    @After
    public void deleteUser(){
        userClient.deleteUser();
    }

}
