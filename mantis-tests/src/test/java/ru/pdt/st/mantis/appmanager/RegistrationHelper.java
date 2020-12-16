package ru.pdt.st.mantis.appmanager;

import org.openqa.selenium.By;

public class RegistrationHelper extends HelperBase {

  // Конструктор класса RegistrationHelper. В нем происходит инициализация браузера
  public RegistrationHelper(ApplicationManager app) {
    super(app);
  }

  // Заполнение и отправка формы регистрации
  public void start(String username, String email) {
    wd.get(app.getProperty("web.baseUrl") + "/signup_page.php");
    type(By.name("username"), username);
    type(By.name("email"), email);
    click(By.cssSelector("input[value='Зарегистрироваться']"));
  }

  // Установка и подтверждение пароля
  public void finish(String confirmationLink, String password) {
    wd.get(confirmationLink);
    type(By.name("password"), password);
    type(By.name("password_confirm"), password);
    wd.findElement(By.tagName("button")).click();
  }

}
