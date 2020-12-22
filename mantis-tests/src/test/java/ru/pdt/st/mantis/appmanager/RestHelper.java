package ru.pdt.st.mantis.appmanager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import ru.pdt.st.mantis.model.Issue;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RestHelper {

  private final ApplicationManager app;

  // Конструктор для SoapHelper
  public RestHelper(ApplicationManager app) {
    this.app = app;
  }

  private Executor getExecutor() {
    // Авторизация
    return Executor.newInstance().auth("288f44776e7bec4bf44fdfeb1e646490", "");
  }

  public int getIssueId() throws IOException {
    // Получение множества баг-репортов Bugify
    String json = getExecutor().execute(Request.Get("https://bugify.stqa.ru/api/issues.json"))
            .returnContent().asString();
    // Анализируем ответ
    JsonElement parsed = JsonParser.parseString(json); // Замена IntelliJ IDEA
    // Извлечение по ключу нужной части из JsonElement
    JsonElement issues = parsed.getAsJsonObject().get("issues");
    // Преобразование списка issues во множество элементов типа Issue
    List<Issue> issueList = new Gson().fromJson(issues, new TypeToken<List<Issue>>(){}.getType());
    // Получение ИД произвольного баг-репорта
    Random r = new Random();
    return issueList.get(r.nextInt(issueList.size())).getId();
  }

  public int getIssueStatus(int issueId) throws IOException {
    // Получение информации о баг-репорте по ИД
    String json = getExecutor().execute(Request.Get("https://bugify.stqa.ru/api/issues/" + issueId + ".json"))
            .returnContent().asString();
    // Анализируем ответ
    JsonElement parsed = JsonParser.parseString(json); // Замена IntelliJ IDEA
    // Извлечение статуса баг-репорта
    JsonElement issues = parsed.getAsJsonObject().get("issues");
    Set<Issue> issueSet = new Gson().fromJson(issues, new TypeToken<Set<Issue>>(){}.getType());
    return issueSet.iterator().next().getStatus();
  }

  public Set<Issue> getIssues() throws IOException {
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
    return new Gson().fromJson(issues, new TypeToken<Set<Issue>>() {
    }.getType());
  }

}
