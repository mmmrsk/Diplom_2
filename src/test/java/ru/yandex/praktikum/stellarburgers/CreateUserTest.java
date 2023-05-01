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

public class CreateUserTest {
    public UserClient userClient;
    public User user;
    Response response;

    @Before
    public void setup() {
        userClient = new UserClient();
    }
    // Тест 1: создание уникального пользователя
    @Test
    @DisplayName("Creating a user")
    @Description("Positive test, a successful server response is 200, the response body contains success: true")
    public void creatingAUserTest() {

        user = User.getRandomUser();
        response = userClient.createUser(user);
        response.then().assertThat().body("success", equalTo(true)).and().statusCode(200);
    }
    // Тест 2: создание пользователя, который уже зарегистрирован
    @Test
    @DisplayName("Creating an existing user")
    @Description("Negative test, the system responds with a 403 code")
    public void creatingAnExistingUserTest() {

        user = User.getRandomUser();
        userClient.createUser(user);
        response = userClient.createUser(user);
        response.then().assertThat().body("success", equalTo(false)).and().statusCode(403);
    }
    // Тест 3: создание пользователя без поля емейл
    @Test
    @DisplayName("Create user without email field")
    @Description("Negative test, the system responds with a 403 code")
    public void userCreatedWithoutEmailTest() {

        user = User.getRandomUser();
        user.setEmail(null);
        userClient.createUser(user);
        response = userClient.createUser(user);
        response.then().assertThat().body("success", equalTo(false)).and().statusCode(403);
    }
    // Тест 4: создание пользователя без поля пароль
    @Test
    @DisplayName("Create user without password field")
    @Description("Negative test, the system responds with a 403 code")
    public void userCreatedWithoutPasswordTest() {

        user = User.getRandomUser();
        user.setPassword(null);
        userClient.createUser(user);
        response = userClient.createUser(user);
        response.then().assertThat().body("success", equalTo(false)).and().statusCode(403);
    }
    // Тест 5: создание пользователя без поля имя
    @Test
    @DisplayName("Create user without name field")
    @Description("Negative test, the system responds with a 403 code")
    public void userCreatedWithoutNameTest() {

        user = User.getRandomUser();
        user.setName(null);
        userClient.createUser(user);
        response = userClient.createUser(user);
        response.then().assertThat().body("success", equalTo(false)).and().statusCode(403);
    }

    @After
    public void deleteUser() {
        userClient.deleteUser();
    }
}
