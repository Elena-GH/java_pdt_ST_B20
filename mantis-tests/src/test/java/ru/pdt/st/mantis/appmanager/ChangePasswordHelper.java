package ru.pdt.st.mantis.appmanager;

import org.openqa.selenium.By;

public class ChangePasswordHelper extends HelperBase {

  // Конструктор класса ChangePasswordHelper. В нем происходит инициализация браузера
  public ChangePasswordHelper(ApplicationManager app) {
    super(app);
  }

  // Авторизация UI
  public void loginUI(String username, String password) {
    wd.get(app.getProperty("web.baseUrl"));
    type(By.name("username"), username);
    click(By.cssSelector("input[value='Войти']"));
    type(By.name("password"), password);
    click(By.cssSelector("input[value='Войти']"));
  }

  public void start(int id) {
    click(By.xpath("//a[contains(@href, 'manage_overview_page.php')]"));
    click(By.xpath("//a[contains(@href, 'manage_user_page.php')]"));
    click(By.cssSelector(String.format("a[href='manage_user_edit_page.php?user_id=%s']", id)));
    click(By.cssSelector("input[value='Сбросить пароль']"));
  }
}
