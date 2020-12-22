package ru.pdt.st.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.restassured.RestAssured;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.junit.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class RestAssuredTests {

  @BeforeClass
  public void init() {
    // При каждом обращении к RESTAssured выполняется аутентификация
    RestAssured.authentication = RestAssured.basic("288f44776e7bec4bf44fdfeb1e646490", "");
  }

  @Test
  public void testCreateIssue() throws IOException {
    // Получаем множество объектов типа Issue до добавления нового баг-репорта
    Set<Issue> oldIssues = getIssues();
    // Создаем новый объект типа Issue (вызываем конструктор)
    Issue newIssue = new Issue().withSubject("Test create issue").withDescription("Test create issue description");
    // Создание баг-репорта в баг-трекере Bugify и возвращаем ИД нового баг-репорта
    int issueId = createIssue(newIssue);
    // Получаем новый список объектов типа Issue
    Set<Issue> newIssues = getIssues();
    // Добавляем в старый список новый объект типа Issue
    oldIssues.add(newIssue.withId(issueId));
    // Сравниваем новый и старый список модельных объектов типа Issue
    assertEquals(newIssues, oldIssues);
  }

  private Set<Issue> getIssues() throws IOException {
    // Получение множества объектов типа Issue (авторизация в @BeforeClass)
    String json = RestAssured.get("https://bugify.stqa.ru/api/issues.json").asString();
    // Анализируем ответ
    // JsonElement parsed = new JsonParser().parse(json); - из Лекция 9.4. REST. Взаимодействие с Bugify
    JsonElement parsed = JsonParser.parseString(json); // Замена IntelliJ IDEA
    // Извлечение по ключу нужной части из JsonElement
    JsonElement issues = parsed.getAsJsonObject().get("issues");
    // Преобразование списка issues во множество элементов типа Issue
    return new Gson().fromJson(issues, new TypeToken<Set<Issue>>() {
    }.getType());
  }

  private int createIssue(Issue newIssue) throws IOException {
    // Запрос создания баг-репорта в Bugify (авторизация в @BeforeClass)
    String json = RestAssured.given().param("subject", newIssue.getSubject())
                       .param("description", newIssue.getDescription())
                       .post("https://bugify.stqa.ru/api/issues.json").asString();
    // Анализируем ответ
    JsonElement parsed = JsonParser.parseString(json);
    // Извлечение ИД нового баг-репорта и возврат результата в виде целого цисла
    return parsed.getAsJsonObject().get("issue_id").getAsInt();
  }

}
