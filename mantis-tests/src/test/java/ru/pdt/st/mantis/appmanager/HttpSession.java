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

  private CloseableHttpClient httpclient;
  private ApplicationManager app;

  public HttpSession(ApplicationManager app) {
    this.app = app;
    httpclient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();
  }

  public boolean login(String username, String password) throws IOException {
    HttpPost postStepOne = new HttpPost(app.getProperty("web.baseUrl") + "/login_page.php");
    List<NameValuePair> paramsStepOne = new ArrayList<>();
    paramsStepOne.add(new BasicNameValuePair("username", username));
    paramsStepOne.add(new BasicNameValuePair("return", "index.php"));
    postStepOne.setEntity(new UrlEncodedFormEntity(paramsStepOne));
    httpclient.execute(postStepOne);
    HttpPost postStepTwo = new HttpPost(app.getProperty("web.baseUrl") + "/login_password_page.php");
    List<NameValuePair> paramsStepTwo = new ArrayList<>();
    paramsStepTwo.add(new BasicNameValuePair("username", username));
    paramsStepTwo.add(new BasicNameValuePair("password", password));
    paramsStepTwo.add(new BasicNameValuePair("secure_session", "on"));
    paramsStepTwo.add(new BasicNameValuePair("return", "index.php"));
    postStepTwo.setEntity(new UrlEncodedFormEntity(paramsStepTwo));
    CloseableHttpResponse response = httpclient.execute(postStepTwo);
    String body = geTestForm(response);
    return body.contains(String.format("<span class=\"user-info\">%s</span>", username));
  }

  private String geTestForm(CloseableHttpResponse response) throws IOException {
    try {
      return EntityUtils.toString(response.getEntity());
    } finally {
      response.close();
    }
  }

  public boolean isLoggedInAs(String username) throws IOException {
    HttpGet get = new HttpGet(app.getProperty("web.baseUrl") + "/index.php");
    CloseableHttpResponse response = httpclient.execute(get);
    String body = geTestForm(response);
    //return body.contains(String.format("<span class=\"italic\">%s</span>", username));
    return body.contains(String.format("<span class=\"user-info\">%s</span>", username));
  }
}
