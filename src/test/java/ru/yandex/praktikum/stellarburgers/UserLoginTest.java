package ru.yandex.praktikum.stellarburgers;

import io.qameta.allure.Description;
import ru.yandex.praktikum.stellarburgers.clients.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import ru.yandex.praktikum.stellarburgers.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest {
    public UserClient userClient;
    public User user;
    Response response;

    @Before
    public void setup() {
        userClient = new UserClient();
        user = User.getRandomUser();
        userClient.createUser(user);
    }
    // Тест 1: логин под существующим пользователем
    @Test
    @DisplayName("Login user with all required fields")
    @Description("Positive test, a successful server response is 200, the response body contains success: true")
    public void userLoginTest() {
        response = userClient.loginUser(user);
        response.then().assertThat().body("success", equalTo(true)).and().statusCode(200);
    }
    // Тест 2: логин с неверным логином
    @Test
    @DisplayName("Login user with wrong login")
    @Description("Negative test, for a request with an incorrect email, the system responds with a 401 code")
    public void userLoginWithIncorrectEmailTest() {
        user.setEmail("test");
        response = userClient.loginUser(user);
        response.then().assertThat().body("success", equalTo(false)).and().statusCode(401);
    }
    // Тест 3: логин с неверным паролем
    @Test
    @DisplayName("Login user with wrong password")
    @Description("Negative scenario, for a request with an incorrect password, the system responds with a 401 code")
    public void userLoginWithIncorrectPasswordTest() {
        user.setPassword("test");
        response = userClient.loginUser(user);
        response.then().assertThat().body("success", equalTo(false)).and().statusCode(401);
    }
    @After
    public void deleteUser() {
        userClient.deleteUser();
    }
}
