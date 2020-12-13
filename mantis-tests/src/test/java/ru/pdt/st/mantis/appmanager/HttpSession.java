package ru.pdt.st.mantis.appmanager;


import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpSession {

  private ApplicationManager app;
  private CloseableHttpClient httpclient;

  // Конструктор класса HttpSession. В нем происходит инициализация HTTP-клиента
  public HttpSession(ApplicationManager app) {
    this.app = app;
    httpclient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();
  }

  // Вход в систему
  public boolean login(String username, String password) throws IOException {
    // Адрес запроса
    HttpPost post = new HttpPost(app.getProperty("web.baseUrl") + "/login.php");
    // Параметры запроса
    List<NameValuePair> params = new ArrayList<>();
    params.add(new BasicNameValuePair("username", username));
    params.add(new BasicNameValuePair("password", password));
    params.add(new BasicNameValuePair("secure_session", "on"));
    params.add(new BasicNameValuePair("return", "index.php"));
    // Упаковка параметров в запрос
    post.setEntity(new UrlEncodedFormEntity(params));
    // Отправка запроса
    CloseableHttpResponse response = httpclient.execute(post);
    // Получение ответа
    String body = geTestForm(response);
    // Анализ ответа
    return body.contains(String.format("<span class=\"user-info\">%s</span>", username));
  }

  // Получение текста ответа от сервера
  private String geTestForm(CloseableHttpResponse response) throws IOException {
    try {
      return EntityUtils.toString(response.getEntity());
    } finally {
      response.close();
    }
  }

  // Метод проверяет какой пользователь залогинен в приложении
  public boolean isLoggedInAs(String username) throws IOException {
    HttpGet get = new HttpGet(app.getProperty("web.baseUrl") + "/index.php");
    CloseableHttpResponse response = httpclient.execute(get);
    String body = geTestForm(response);
    //return body.contains(String.format("<span class=\"italic\">%s</span>", username));
    return body.contains(String.format("<span class=\"user-info\">%s</span>", username));
  }
}
