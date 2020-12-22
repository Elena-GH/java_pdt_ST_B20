package ru.pdt.st.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class RestTests {

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
    // Получение множества объектов типа Issue (без авторизацией)
    // String json= Request.Get("http://demo.bugify.com/api/issues.json").execute().returnContent().asString();
    // Получение множества объектов типа Issue (с авторизацией)
    String json = getExecutor().execute(Request.Get("https://bugify.stqa.ru/api/issues.json"))
            .returnContent().asString();
    // Анализируем ответ
    // JsonElement parsed = new JsonParser().parse(json); - из Лекция 9.4. REST. Взаимодействие с Bugify
    JsonElement parsed = JsonParser.parseString(json); // Замена IntelliJ IDEA
    // Извлечение по ключу нужной части из JsonElement
    JsonElement issues = parsed.getAsJsonObject().get("issues");
    // Преобразование списка issues во множество элементов типа Issue
    return new Gson().fromJson(issues, new TypeToken<Set<Issue>>(){}.getType());
  }

  private Executor getExecutor() {
    // Авторизация
    return Executor.newInstance().auth("288f44776e7bec4bf44fdfeb1e646490", "");
  }

  private int createIssue(Issue newIssue) throws IOException {
    // Запрос создания баг-репорта в Bugify
    String json = getExecutor().execute(Request.Post("https://bugify.stqa.ru/api/issues.json")
            .bodyForm(new BasicNameValuePair("subject", newIssue.getSubject()),
                      new BasicNameValuePair("description", newIssue.getDescription())))
            .returnContent().asString();
    // Анализируем ответ
    JsonElement parsed = JsonParser.parseString(json);
    // Извлечение ИД нового баг-репорта и возврат результата в виде целого цисла
    return parsed.getAsJsonObject().get("issue_id").getAsInt();
  }

}
